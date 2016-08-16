package com.krishagni.catissueplus.core.biospecimen.services.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.util.Utility;

import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.FormDataFilter;

public class SpecimenFreezeThawEventFilter implements FormDataFilter {
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public FormData execute(UserContext userCtx, FormData input) {
		try {
			if (input.getAppData() == null) {
				return input;
			}

			Long specimenId = Utility.numberToLong(input.getAppData().get("objectId"));
			if (specimenId == null) {
				return input;
			}

			ControlValue incrFreezeThaw = input.getFieldValue("incrementFreezeThaw");
			boolean increment = incrFreezeThaw == null ? false : incrFreezeThaw.getControl().fromString((String)incrFreezeThaw.getValue());
			if (increment) {
				Specimen specimen = daoFactory.getSpecimenDao().getById(specimenId);
				Integer existingCount = specimen.getFreezeThawCycles();
				specimen.setFreezeThawCycles(existingCount == null ? 1 : existingCount + 1);
			}

			return input;
		} catch (IllegalArgumentException iae) {
			throw iae;
		} catch (Exception e) {
			throw new RuntimeException("Error executing frozen event post filter", e);
		}
	}
}
