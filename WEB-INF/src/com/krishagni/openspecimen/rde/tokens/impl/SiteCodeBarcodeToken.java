package com.krishagni.openspecimen.rde.tokens.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.openspecimen.rde.tokens.BarcodePart;
import com.krishagni.openspecimen.rde.tokens.BarcodeToken;

public class SiteCodeBarcodeToken extends AbstractLabelTmplToken implements BarcodeToken {
	@Autowired
	private DaoFactory daoFactory;

	@Override
	public String getName() {
		return "SITE_CODE";
	}

	@Override
	public String getReplacement(Object object) {
		return null;
	}

	@Override
	public BarcodePart parse(Map<String, Object> contextMap, String input, int startIdx, String... args) {
		String[] parts = input.substring(startIdx).split("-", 2);
		String siteCode = parts[0];

		BarcodePart result = new BarcodePart();
		result.setToken(getName());
		result.setStartIdx(startIdx);
		result.setEndIdx(startIdx + siteCode.length());
		result.setCode(siteCode);

		Site site = daoFactory.getSiteDao().getSiteByCode(siteCode);
		if (site != null) {
			result.setValue(site);
			result.setDisplayValue(site.getName());
		}

		return result;
	}
}
