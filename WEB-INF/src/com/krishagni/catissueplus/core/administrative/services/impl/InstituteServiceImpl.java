package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.domain.factory.InstituteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.InstituteFactory;
import com.krishagni.catissueplus.core.administrative.events.InstituteDetail;
import com.krishagni.catissueplus.core.administrative.events.InstituteQueryCriteria;
import com.krishagni.catissueplus.core.administrative.events.ListInstitutesCriteria;
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
	public ResponseEvent<InstituteDetail> getInstitute(RequestEvent<InstituteQueryCriteria> req) {
		try {
			InstituteQueryCriteria crit = req.getPayload();
			
			Institute institute = null;
			if (crit.getName() != null) {
				institute = daoFactory.getInstituteDao().getInstituteByName(crit.getName());
			} else if (crit.getId() != null) {
				institute = daoFactory.getInstituteDao().getInstitute(crit.getId());
			}
			
			if (institute == null) {
				return ResponseEvent.userError(InstituteErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(InstituteDetail.fromDomain(institute));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<InstituteDetail>> getInstitutes(RequestEvent<ListInstitutesCriteria> req) {
		List<Institute> list = daoFactory.getInstituteDao().getAllInstitutes(req.getPayload().maxResults());
		
		List<InstituteDetail> details = new ArrayList<InstituteDetail>();
		for (Institute institute : list) {
			details.add(InstituteDetail.fromDomain(institute));
		}
		
		return ResponseEvent.response(details);
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
			return ResponseEvent.response(InstituteDetail.fromDomain(institute));
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
			Institute existing = null;
			InstituteDetail detail = req.getPayload();
			
			if (detail.getName() != null) {
				existing = daoFactory.getInstituteDao().getInstituteByName(detail.getName());
			} else {
				existing = daoFactory.getInstituteDao().getInstitute(detail.getId());
			}
			
			if (existing == null) {
				return ResponseEvent.userError(InstituteErrorCode.NOT_FOUND);
			}
			
			Institute institute = instituteFactory.createInstitute(detail);
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			if (!existing.getName().equals(institute.getName())) {
				ensureUniqueInstituteName(institute.getName(), ose);
			}
			
			ose.checkAndThrow();
			
			existing.update(institute);
			daoFactory.getInstituteDao().saveOrUpdate(existing);
			return ResponseEvent.response(InstituteDetail.fromDomain(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<InstituteDetail> deleteInstitute(RequestEvent<InstituteQueryCriteria> req) {
		try {
			InstituteQueryCriteria crit = req.getPayload();
			
			Institute existing = null;						
			if (crit.getName() != null) {
				existing = daoFactory.getInstituteDao().getInstituteByName(crit.getName());
			} else if (crit.getId() != null) {
				existing = daoFactory.getInstituteDao().getInstitute(crit.getId());
			}
			
			if (existing == null) {
				return ResponseEvent.userError(InstituteErrorCode.NOT_FOUND);
			}
			
			existing.delete();
			daoFactory.getInstituteDao().saveOrUpdate(existing);
			return ResponseEvent.response(InstituteDetail.fromDomain(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	private void ensureUniqueInstituteName(String name, OpenSpecimenException ose) {
		Institute institute = daoFactory.getInstituteDao().getInstituteByName(name);
		if (institute != null) {
			ose.addError(InstituteErrorCode.DUP_NAME);
		}
	}
}