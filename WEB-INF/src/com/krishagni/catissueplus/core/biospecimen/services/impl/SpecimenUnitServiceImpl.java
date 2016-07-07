package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.administrative.events.ListPvCriteria;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenUnitDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenUnitService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.PvAttributes;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SpecimenUnitServiceImpl implements SpecimenUnitService {
	
	private DaoFactory daoFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<SpecimenUnitDetail>> getUnits() {
		try {
			ListPvCriteria crit = new ListPvCriteria().attribute(PvAttributes.SPECIMEN_CLASS);
			List<PermissibleValue> specimenClasses = daoFactory.getPermissibleValueDao().getPvs(crit);

			List<SpecimenUnitDetail> units = new ArrayList<>();
			for (PermissibleValue specimenClass : specimenClasses) {
				for (PermissibleValue type : specimenClass.getChildren()) {
					units.add(getSpecimenUnitDetail(type));
				}
			}

			return ResponseEvent.response(units);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	private SpecimenUnitDetail getSpecimenUnitDetail(PermissibleValue pv) {
		SpecimenUnitDetail detail = new SpecimenUnitDetail();
		detail.setSpecimenClass(pv.getParent().getValue());
		detail.setType(pv.getValue());
		detail.setQtyUnit(getProperty(pv, "quantity_unit"));
		detail.setQtyHtmlDisplayCode(getProperty(pv, "quantity_display_unit"));
		detail.setConcUnit(getProperty(pv, "concentration_unit"));
		detail.setConcHtmlDisplayCode(getProperty(pv, "concentration_display_unit"));
		detail.setActivityStatus("Active");
		return detail;
	}

	private String getProperty(PermissibleValue pv, String prop) {
		String value = pv.getProps().get(prop);
		if (StringUtils.isBlank(value)) {
			value = pv.getParent().getProps().get(prop);
		}

		return value;
	}
}
