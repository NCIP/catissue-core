package com.krishagni.catissueplus.rde.tokens.impl;

import java.util.Map;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.rde.tokens.BarcodePart;
import com.krishagni.catissueplus.rde.tokens.BarcodeToken;

public class SrCodeBarcodeToken extends AbstractLabelTmplToken implements BarcodeToken {

	@Override
	public String getName() {
		return "SR_CODE";
	}

	@Override
	public String getReplacement(Object object) {
		SpecimenRequirement sr = null;
		if (object instanceof Specimen) {
			sr = ((Specimen)object).getSpecimenRequirement();
		}
		
		if (sr == null) {
			return null;
		}
		
		return sr.getCode();
	}

	@Override
	public BarcodePart parse(Map<String, Object> contextMap, String input, int startIdx, String ... args) {		
		// TODO Auto-generated method stub
		return null;
	}
}
