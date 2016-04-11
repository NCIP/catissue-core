package com.krishagni.openspecimen.rde.tokens.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.openspecimen.rde.tokens.BarcodePart;
import com.krishagni.openspecimen.rde.tokens.BarcodeToken;

public class CpCodeBarcodeToken extends AbstractLabelTmplToken implements BarcodeToken {
	@Override
	public String getName() {
		return "CP_CODE";
	}

	@Override
	public String getReplacement(Object o) {
		Visit visit = null;
		if (o instanceof Visit) {
			visit = (Visit)o;
		} else if (o instanceof Specimen) {
			visit = ((Specimen) o).getVisit();
		}

		if (visit == null) {
			return StringUtils.EMPTY;
		}

		return visit.getCollectionProtocol().getCode();
	}

	@Override
	public BarcodePart parse(Map<String, Object> contextMap, String input, int startIdx, String... args) {
		String[] parts = input.substring(startIdx).split("-", 2);
		String cpCode = parts[0];

		BarcodePart result = new BarcodePart();
		result.setToken(getName());
		result.setStartIdx(startIdx);
		result.setEndIdx(startIdx + cpCode.length());
		result.setCode(cpCode);

		CollectionProtocol cp = (CollectionProtocol)contextMap.get("cp");
		if (cp.getCode().equals(cpCode)) {
			result.setValue(cp);
			result.setDisplayValue(cp.getShortTitle());
		}

		return result;
	}
}
