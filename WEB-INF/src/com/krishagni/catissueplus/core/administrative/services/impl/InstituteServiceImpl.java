package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.domain.factory.InstituteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.InstituteFactory;
import com.krishagni.catissueplus.core.administrative.events.InstituteDetail;
import com.krishagni.catissueplus.core.administrative.events.InstituteQueryCriteria;
import com.krishagni.catissueplus.core.administrative.repository.InstituteListCriteria;
import com.krishagni.catissueplus.core.administrative.services.InstituteService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DeleteEntityOp;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
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
			InstituteListCriteria listCrit = req.getPayload();
			return ResponseEvent.response(daoFactory.getInstituteDao().getInstitutes(listCrit));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Long> getInstitutesCount(RequestEvent<InstituteListCriteria> req) {
		try {
			InstituteListCriteria listCrit = req.getPayload();
			return ResponseEvent.response(daoFactory.getInstituteDao().getInstitutesCount(listCrit));
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
			
			Object key = null;
			if (crit.getId() != null) {
				institute = daoFactory.getInstituteDao().getById(crit.getId());
				key = crit.getId();
			} else if (StringUtils.isNotBlank(crit.getName())) {
				institute = daoFactory.getInstituteDao().getInstituteByName(crit.getName());
				key = crit.getName();
			}
					
			if (institute == null) {
				return ResponseEvent.userError(InstituteErrorCode.NOT_FOUND, key);
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
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			
			Institute institute = instituteFactory.createInstitute(req.getPayload());
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueName(institute.getName(), ose);
			ose.checkAndThrow();

			daoFactory.getInstituteDao().saveOrUpdate(institute, true);
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
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			
			InstituteDetail detail = req.getPayload();			
			Institute existing = null;
			
			Object key = null;
			if (detail.getId() != null) {
				existing = daoFactory.getInstituteDao().getById(detail.getId());
				key = detail.getId();
			} else if (StringUtils.isNotBlank(detail.getName())) {
				existing = daoFactory.getInstituteDao().getInstituteByName(detail.getName());
				key = detail.getName();
			}
			
			if (existing == null) {
				return ResponseEvent.userError(InstituteErrorCode.NOT_FOUND, key);
			}
			
			Institute institute = instituteFactory.createInstitute(detail);
			if (!existing.getName().equals(institute.getName())) {
				OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
				ensureUniqueName(institute.getName(), ose);
				ose.checkAndThrow();
			}

			existing.update(institute);
			daoFactory.getInstituteDao().saveOrUpdate(existing);
			return ResponseEvent.response(InstituteDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<Long> req) {
		try {
			Long instituteId = req.getPayload();
			Institute existing = daoFactory.getInstituteDao().getById(instituteId);
			if (existing == null) {
				return ResponseEvent.userError(InstituteErrorCode.NOT_FOUND, instituteId);
			}
			
			return ResponseEvent.response(existing.getDependentEntities());
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<InstituteDetail> deleteInstitute(RequestEvent<DeleteEntityOp> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			
			DeleteEntityOp deleteOp = req.getPayload();
			Long instituteId = deleteOp.getId();
			Institute existing = daoFactory.getInstituteDao().getById(instituteId);
			if (existing == null) {
				return ResponseEvent.userError(InstituteErrorCode.NOT_FOUND, instituteId);
			}
			
			existing.delete(deleteOp.isClose());
			return ResponseEvent.response(InstituteDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private void ensureUniqueName(String name, OpenSpecimenException ose) {
		Institute institute = daoFactory.getInstituteDao().getInstituteByName(name);
		if (institute != null) {
			ose.addError(InstituteErrorCode.DUP_NAME);
		}
	}	
}
