package com.krishagni.openspecimen.rde.tokens.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolSite;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.openspecimen.rde.tokens.BarcodePart;
import com.krishagni.openspecimen.rde.tokens.BarcodeToken;

public class CpSiteCodeBarcodeToken extends AbstractLabelTmplToken implements	BarcodeToken {

	@Override
	public String getName() {		
		return "CP_SITE_CODE";
	}

	@Override
	public String getReplacement(Object object) {
		Visit visit = null;
		if (object instanceof Visit) {
			visit = (Visit)object;
		} else if (object instanceof Specimen) {
			visit = ((Specimen)object).getVisit();
		}
		
		if (visit == null) {
			return StringUtils.EMPTY;
		}
		
		Site site = visit.getSite();
		for (CollectionProtocolSite cpSite : visit.getCollectionProtocol().getSites()) {
			if (cpSite.getSite().equals(site)) {
				return cpSite.getCode();
			}
		}
		
		return null;
	}

	@Override
	public BarcodePart parse(Map<String, Object> contextMap, String input, int startIdx, String ... args) {
		String[] parts = input.substring(startIdx).split("-", 2);
		String siteCode = parts[0];
		
		BarcodePart result = new BarcodePart();
		result.setToken(getName());
		result.setStartIdx(startIdx);
		result.setEndIdx(startIdx + siteCode.length());
		result.setCode(siteCode);
				
		CollectionProtocol cp = (CollectionProtocol)contextMap.get("cp");
		for (CollectionProtocolSite cpSite : cp.getSites()) {
			if (siteCode.equals(cpSite.getCode())) {
				result.setValue(cpSite.getSite());
				result.setDisplayValue(cpSite.getSite().getName());
				break;				
			}
		}
		
		return result;
	}
}
