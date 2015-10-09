package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.Date;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.util.Utility;
import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.FormDataFilter;

public class SpecimenReceivedEventFilter implements FormDataFilter {
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public FormData execute(UserContext userCtx, FormData input) {
		if (input.getAppData() == null) {
			return input;
		}

		Long specimenId = Utility.numberToLong(input.getAppData().get("objectId"));
		if (specimenId == null) {
			return input;
		}
		
		ControlValue cv = input.getFieldValue("time");

		try {
			Specimen specimen = daoFactory.getSpecimenDao().getById(specimenId);
			Long timeInMillis = Long.parseLong(cv.getValue().toString());
			Date createdOn = new Date(timeInMillis);
			specimen.updateCreatedOn(createdOn);
		} catch (IllegalArgumentException iae) {
			throw iae;
		} catch (Exception e) {
			throw new RuntimeException("Error executing received event post filter", e);
		}
		
		return input;
	}
}
