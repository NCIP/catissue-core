package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenQuantityUnit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenQtyUnitErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenQuantityUnitDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenQuantityUnitService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.Status;

public class SpecimenQuantityUnitServiceImpl implements	SpecimenQuantityUnitService {
	
	private DaoFactory daoFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<SpecimenQuantityUnitDetail>> getQuantityUnits() {
		try {			
			List<SpecimenQuantityUnit> units = daoFactory.getSpecimenQuantityUnitDao().listAll();
			return ResponseEvent.response(SpecimenQuantityUnitDetail.from(units));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenQuantityUnitDetail> saveOrUpdate(RequestEvent<SpecimenQuantityUnitDetail> req) {
		try {
			if (!AuthUtil.isAdmin()) {
				return ResponseEvent.userError(SpecimenQtyUnitErrorCode.NOT_ALLOWED);
			}
			
			SpecimenQuantityUnit unit = createQtyUnit(req.getPayload());
			SpecimenQuantityUnit existing = null;
			if (unit.getId() != null) {
				existing = daoFactory.getSpecimenQuantityUnitDao().getById(unit.getId());
				if (existing == null) {
					return ResponseEvent.userError(SpecimenQtyUnitErrorCode.NOT_FOUND);
				}
			} else {
				existing = daoFactory.getSpecimenQuantityUnitDao().getByClassAndType(unit.getSpecimenClass(), unit.getType());
			}
			
			if (existing != null) {
				existing.update(unit);
			} else {
				existing = unit;
			}
			
			daoFactory.getSpecimenQuantityUnitDao().saveOrUpdate(existing, true);
			return ResponseEvent.response(SpecimenQuantityUnitDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private SpecimenQuantityUnit createQtyUnit(SpecimenQuantityUnitDetail detail) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		SpecimenQuantityUnit result = new SpecimenQuantityUnit();
		BeanUtils.copyProperties(detail, result);
		
		if (StringUtils.isBlank(result.getSpecimenClass())) {
			ose.addError(SpecimenQtyUnitErrorCode.CLASS_REQUIRED);
		}
		
		if (StringUtils.isBlank(result.getUnit())) {
			ose.addError(SpecimenQtyUnitErrorCode.UNIT_REQUIRED);
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
