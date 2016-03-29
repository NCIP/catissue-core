package com.krishagni.openspecimen.rde.tokens.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.openspecimen.rde.tokens.BarcodePart;
import com.krishagni.openspecimen.rde.tokens.BarcodeToken;

@Configurable
public class CohortBarcodeToken extends AbstractLabelTmplToken implements BarcodeToken {
	
	@Autowired
	private DaoFactory daoFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public String getName() {
		return "COHORT";
	}

	@Override
	public String getReplacement(Object object) {
		String cohort = null;		
		if (object instanceof Visit) {
			cohort = ((Visit)object).getCohort();
		} else if (object instanceof Specimen) {
			cohort = ((Specimen)object).getVisit().getCohort();
		}
		
		if (StringUtils.isBlank(cohort)) {
			return null;
		}
		
		PermissibleValue pv = daoFactory.getPermissibleValueDao().getByValue("cohort", cohort);
		if (pv == null) {
			return null;
		}
		
		return pv.getConceptCode();
	}

	@Override
	public BarcodePart parse(Map<String, Object> contextMap, String input, int startIdx, String ... args) {
		String[] parts = input.substring(startIdx).split("-", 2);		
		String cohort = parts[0];
		
		BarcodePart result = new BarcodePart();
		result.setToken(getName());
		result.setStartIdx(startIdx);
		result.setEndIdx(startIdx + cohort.length());
		result.setCode(cohort);
		
		PermissibleValue pv = daoFactory.getPermissibleValueDao().getByConceptCode("cohort", cohort);
		result.setValue(pv);
		if (pv != null) {
			result.setDisplayValue(pv.getValue());
		}
		return result;
	}
}
