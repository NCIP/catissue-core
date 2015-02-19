package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.InstituteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.InstituteFactory;
import com.krishagni.catissueplus.core.administrative.events.DeleteInstituteOp;
import com.krishagni.catissueplus.core.administrative.events.InstituteDetail;
import com.krishagni.catissueplus.core.administrative.events.InstituteQueryCriteria;
import com.krishagni.catissueplus.core.administrative.repository.InstituteListCriteria;
import com.krishagni.catissueplus.core.administrative.services.InstituteService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class InstituteServiceImpl implements InstituteService {
	private DaoFactory daoFactory;

	private InstituteFactory instituteFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setInstituteFactory(InstituteFactory instituteFactory) {
		this.instituteFactory = instituteFactory;
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<InstituteDetail>> getInstitutes(RequestEvent<InstituteListCriteria> req) {
		try {
			List<Institute> institutes = daoFactory.getInstituteDao().getInstitutes(req.getPayload());
			return ResponseEvent.response(InstituteDetail.from(institutes));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
		
	@Override
	@PlusTransactional
	public ResponseEvent<InstituteDetail> getInstitute(RequestEvent<InstituteQueryCriteria> req) {
		try {
			InstituteQueryCriteria crit = req.getPayload();
			Institute institute = null;
		
			if (crit.getId() != null) {
				institute = daoFactory.getInstituteDao().getById(crit.getId());
			} else if (crit.getName() != null) {
				institute = daoFactory.getInstituteDao().getInstituteByName(crit.getName());
			}

			if (institute == null) {
				return ResponseEvent.userError(InstituteErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(InstituteDetail.from(institute));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<InstituteDetail> createInstitute(RequestEvent<InstituteDetail> req) {
		try {
			Institute institute = instituteFactory.createInstitute(req.getPayload());
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueInstituteName(institute.getName(), ose);
			ose.checkAndThrow();

			daoFactory.getInstituteDao().saveOrUpdate(institute);
			return ResponseEvent.response(InstituteDetail.from(institute));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<InstituteDetail> updateInstitute(RequestEvent<InstituteDetail> req) {
		try {
			InstituteDetail detail = req.getPayload();
			
			Institute existing = daoFactory.getInstituteDao().getById(detail.getId());
			
			if (existing == null) {
				return ResponseEvent.userError(InstituteErrorCode.NOT_FOUND);
			}
			
			Institute institute = instituteFactory.createInstitute(detail);
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			
			if (!existing.getName().equals(institute.getName())) {
				ensureUniqueInstituteName(institute.getName(), ose);
			}
			
			checkRemovedDeptRefs(existing, institute, ose);
			ose.checkAndThrow();
			
			existing.update(institute);
			daoFactory.getInstituteDao().saveOrUpdate(existing);
			return ResponseEvent.response(InstituteDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	private void checkRemovedDeptRefs(Institute existing, Institute institute,
			OpenSpecimenException ose) {
		Set<Department> removedDepartments = new HashSet<Department>(existing.getDepartments());
		removedDepartments.removeAll(institute.getDepartments());
		
		for (Department department : removedDepartments) {
			if (CollectionUtils.isNotEmpty(department.getUserCollection())) {
				ose.addError(InstituteErrorCode.DEPT_REF_ENTITY_FOUND);
			}
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Map<String, List>> deleteInstitute(RequestEvent<DeleteInstituteOp> req) {
		try {
			DeleteInstituteOp deleteEvent = req.getPayload();
			Long instituteId = deleteEvent.getId();
			Boolean isClosed = deleteEvent.getIsClosed();
			Institute institute = null;						
			if (instituteId != null) {
				institute = daoFactory.getInstituteDao().getById(instituteId);
			}
			
			if (institute == null) {
				return ResponseEvent.userError(InstituteErrorCode.NOT_FOUND);
			}
			
			if (!isClosed) {
				Map<String, List> dependencies = getDependencies(institute);
				if (!dependencies.isEmpty()) {
					return ResponseEvent.response(dependencies);
				}
			}
			
			institute.delete(deleteEvent.getIsClosed());
			daoFactory.getInstituteDao().saveOrUpdate(institute);
			return ResponseEvent.response(Collections.<String, List>emptyMap());
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private Map<String, List> getDependencies(Institute institute) {
		List<User> users = new ArrayList<User>();
		for(Department department : institute.getDepartments()) {
			if (CollectionUtils.isNotEmpty(department.getUserCollection())) {
				users.addAll(department.getUserCollection());
			}
		}
		
		Map<String, List> depedencies = new HashMap<String, List>();
		if (CollectionUtils.isNotEmpty(users)) {
			depedencies.put("Users", users); 
		}
		
		return depedencies;
	}

	private void ensureUniqueInstituteName(String name, OpenSpecimenException ose) {
		Institute institute = daoFactory.getInstituteDao().getInstituteByName(name);
		if (institute != null) {
			ose.addError(InstituteErrorCode.DUP_NAME);
		}
	}
}