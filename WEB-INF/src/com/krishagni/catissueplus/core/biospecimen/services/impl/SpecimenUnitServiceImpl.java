package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenUnit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenUnitErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenUnitDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenUnitService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.Status;

public class SpecimenUnitServiceImpl implements SpecimenUnitService {
	
	private DaoFactory daoFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<SpecimenUnitDetail>> getUnits() {
		try {			
			List<SpecimenUnit> units = daoFactory.getSpecimenUnitDao().listAll();
			return ResponseEvent.response(SpecimenUnitDetail.from(units));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenUnitDetail> saveOrUpdate(RequestEvent<SpecimenUnitDetail> req) {
		try {
			if (!AuthUtil.isAdmin()) {
				return ResponseEvent.userError(SpecimenUnitErrorCode.NOT_ALLOWED);
			}
			
			SpecimenUnit unit = createUnit(req.getPayload());
			SpecimenUnit existing = null;
			if (unit.getId() != null) {
				existing = daoFactory.getSpecimenUnitDao().getById(unit.getId());
				if (existing == null) {
					return ResponseEvent.userError(SpecimenUnitErrorCode.NOT_FOUND);
				}
			} else {
				existing = daoFactory.getSpecimenUnitDao().getByClassAndType(unit.getSpecimenClass(), unit.getType());
			}
			
			if (existing != null) {
				existing.update(unit);
			} else {
				existing = unit;
			}
			
			daoFactory.getSpecimenUnitDao().saveOrUpdate(existing, true);
			return ResponseEvent.response(SpecimenUnitDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private SpecimenUnit createUnit(SpecimenUnitDetail detail) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		SpecimenUnit result = new SpecimenUnit();
		BeanUtils.copyProperties(detail, result);
		
		if (StringUtils.isBlank(result.getSpecimenClass())) {
			ose.addError(SpecimenUnitErrorCode.CLASS_REQUIRED);
		}
		
		if (StringUtils.isBlank(result.getQtyUnit())) {
			ose.addError(SpecimenUnitErrorCode.UNIT_REQUIRED);
		}
		
		if (StringUtils.isBlank(result.getActivityStatus())) {
			result.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
		} else if (!Status.isValidActivityStatus(result.getActivityStatus())) {
			ose.addError(ActivityStatusErrorCode.INVALID);
		}
		
		ose.checkAndThrow();
		return result;
	}
}
