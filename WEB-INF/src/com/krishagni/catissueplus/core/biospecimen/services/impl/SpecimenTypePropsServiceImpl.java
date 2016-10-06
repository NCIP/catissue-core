package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.administrative.events.ListPvCriteria;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenTypeProps;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenTypePropsService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.PvAttributes;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SpecimenTypePropsServiceImpl implements SpecimenTypePropsService {
	
	private DaoFactory daoFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<SpecimenTypeProps>> getProps() {
		try {
			ListPvCriteria crit = new ListPvCriteria().attribute(PvAttributes.SPECIMEN_CLASS);
			List<PermissibleValue> specimenClasses = daoFactory.getPermissibleValueDao().getPvs(crit);

			List<SpecimenTypeProps> props = new ArrayList<>();
			for (PermissibleValue specimenClass : specimenClasses) {
				for (PermissibleValue type : specimenClass.getChildren()) {
					props.add(getSpecimenTypeProps(type));
				}
			}

			return ResponseEvent.response(props);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	private SpecimenTypeProps getSpecimenTypeProps(PermissibleValue pv) {
		Map<String, String> props = new HashMap<>();
		props.put("abbreviation",        getProperty(pv, "abbreviation"));
		props.put("qtyUnit",             getProperty(pv, "quantity_unit"));
		props.put("qtyHtmlDisplayCode",  getProperty(pv, "quantity_display_unit"));
		props.put("concUnit",            getProperty(pv, "concentration_unit"));
		props.put("concHtmlDisplayCode", getProperty(pv, "concentration_display_unit"));

		SpecimenTypeProps detail = new SpecimenTypeProps();
		detail.setSpecimenClass(pv.getParent().getValue());
		detail.setSpecimenType(pv.getValue());
		detail.setProps(props);
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
