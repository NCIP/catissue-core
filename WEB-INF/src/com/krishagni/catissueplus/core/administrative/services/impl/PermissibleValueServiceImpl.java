
package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.administrative.domain.factory.PVErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.PermissibleValueFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.AddPvEvent;
import com.krishagni.catissueplus.core.administrative.events.AllPvsEvent;
import com.krishagni.catissueplus.core.administrative.events.DeletePvEvent;
import com.krishagni.catissueplus.core.administrative.events.EditPvEvent;
import com.krishagni.catissueplus.core.administrative.events.GetAllPVsEvent;
import com.krishagni.catissueplus.core.administrative.events.PermissibleValueDetails;
import com.krishagni.catissueplus.core.administrative.events.PvAddedEvent;
import com.krishagni.catissueplus.core.administrative.events.PvDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.PvEditedEvent;
import com.krishagni.catissueplus.core.administrative.events.PvInfo;
import com.krishagni.catissueplus.core.administrative.events.ValidatePvEvent;
import com.krishagni.catissueplus.core.administrative.services.PermissibleValueService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class PermissibleValueServiceImpl implements PermissibleValueService {

	private static final String PV_VALUE = "pv value";

	private static final String CONCEPT_CODE = "concept code";

	private DaoFactory daoFactory;

	private PermissibleValueFactory pvFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setPvFactory(PermissibleValueFactory pvFactory) {
		this.pvFactory = pvFactory;
	}

	@Override
	@PlusTransactional
	public PvAddedEvent createPermissibleValue(AddPvEvent event) {
		try {
			PermissibleValue permissibleValue = pvFactory.createPermissibleValue(event.getDetails());

			ObjectCreationException exceptionHandler = new ObjectCreationException();
			ensureUniqueValueInAttribute(permissibleValue, exceptionHandler);
			ensureUniqueConcepetCode(permissibleValue.getConceptCode(), exceptionHandler);
			exceptionHandler.checkErrorAndThrow();

			daoFactory.getPermissibleValueDao().saveOrUpdate(permissibleValue);
			return PvAddedEvent.ok(PermissibleValueDetails.fromDomain(permissibleValue));
		}
		catch (ObjectCreationException ce) {
			return PvAddedEvent.invalidRequest(PVErrorCode.ERRORS.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return PvAddedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public PvEditedEvent updatePermissibleValue(EditPvEvent event) {
		try {
			Long pvId = event.getDetails().getId();
			PermissibleValue oldPv = daoFactory.getPermissibleValueDao().getPermissibleValue(pvId);
			if (oldPv == null) {
				return PvEditedEvent.notFound(pvId);
			}
			PermissibleValue permissibleValue = pvFactory.createPermissibleValue(event.getDetails());
			ObjectCreationException exceptionHandler = new ObjectCreationException();
			checkChangeInValue(oldPv, permissibleValue, exceptionHandler);
			checkChangeInConceptCode(oldPv, permissibleValue, exceptionHandler);
			exceptionHandler.checkErrorAndThrow();

			oldPv.update(permissibleValue);
			daoFactory.getPermissibleValueDao().saveOrUpdate(oldPv);
			return PvEditedEvent.ok(PermissibleValueDetails.fromDomain(oldPv));
		}
		catch (ObjectCreationException ce) {
			return PvEditedEvent.invalidRequest(UserErrorCode.ERRORS.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return PvEditedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public PvDeletedEvent deletePermissibleValue(DeletePvEvent event) {
		try {
			PermissibleValue permissibleValue = daoFactory.getPermissibleValueDao().getPermissibleValue(event.getId());
			if (permissibleValue == null) {
				return PvDeletedEvent.notFound(event.getId());
			}
			daoFactory.getPermissibleValueDao().delete(permissibleValue);
			return PvDeletedEvent.ok();
		}
		catch (Exception e) {
			return PvDeletedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public AllPvsEvent getPermissibleValues(GetAllPVsEvent event) {
		List<PermissibleValue> permissibleValues = daoFactory.getPermissibleValueDao().getAllPVsByAttribute(
				event.getAttribute(), event.getSearchString(), event.getMaxResult());
		List<PvInfo> pvDetails = new ArrayList<PvInfo>();
		for (PermissibleValue pv : permissibleValues) {
			pvDetails.add(PvInfo.fromDomain(pv));
		}
		return AllPvsEvent.ok(pvDetails);
	}

	@Override
	public Boolean validate(ValidatePvEvent event) {
		//Commenting code as database doesn't containing all attribute and PV values.
		//TODO: Include all attributes and their respective PV values through script.  
		/*if (event.getParentValue() != null
				&& daoFactory.getPermissibleValueDao().isPvAvailable(event.getAttribute(), event.getParentValue(),
						event.getValues().get(0))) {
			return false;
		} else {
			List<String> values = daoFactory.getPermissibleValueDao().getAllValuesByAttribute(event.getAttribute());
			for (String value : event.getValues()) {
				if (!values.contains(value)) {
					return false;
				}
			}
		}*/

		return true;
	}

	private void ensureUniqueConcepetCode(String conceptCode, ObjectCreationException exceptionHandler) {
		if (conceptCode != null && !daoFactory.getPermissibleValueDao().isUniqueConceptCode(conceptCode)) {
			exceptionHandler.addError(PVErrorCode.DUPLICATE_CONCEPT_CODE, CONCEPT_CODE);
		}
	}

	private void ensureUniqueValueInAttribute(PermissibleValue permissibleValue, ObjectCreationException exceptionHandler) {
		if (!daoFactory.getPermissibleValueDao().isUniqueValueInAttribute(permissibleValue.getValue(),
				permissibleValue.getAttribute())) {
			exceptionHandler.addError(PVErrorCode.DUPLICATE_PV_VALUE, PV_VALUE);
		}
	}

	private void checkChangeInConceptCode(PermissibleValue oldPv, PermissibleValue permissibleValue,
			ObjectCreationException exceptionHandler) {
		if (oldPv.getConceptCode() != null && !oldPv.getConceptCode().equals(permissibleValue.getConceptCode())) {
			ensureUniqueConcepetCode(permissibleValue.getConceptCode(), exceptionHandler);
		}
	}

	private void checkChangeInValue(PermissibleValue oldPv, PermissibleValue permissibleValue,
			ObjectCreationException exceptionHandler) {
		if (!oldPv.getValue().equals(permissibleValue.getValue())) {
			ensureUniqueValueInAttribute(permissibleValue, exceptionHandler);
		}
	}

}
