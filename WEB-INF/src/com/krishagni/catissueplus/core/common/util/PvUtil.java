package com.krishagni.catissueplus.core.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

@Configurable
public class PvUtil {
	private static PvUtil instance = null;

	@Autowired
	private DaoFactory daoFactory;

	public static PvUtil getInstance() {
		if (instance == null || instance.daoFactory == null) {
			instance = new PvUtil();
		}

		return instance;
	}

	public String getAbbr(String attr, String value) {
		return getAbbr(attr, value, null);
	}

	public String getAbbr(String attr, String value, String defVal) {
		PermissibleValue pv = daoFactory.getPermissibleValueDao().getByValue(attr, value);
		if (pv == null) {
			return defVal;
		}

		String abbr = pv.getProps().get(ABBREVIATION);
		if (StringUtils.isBlank(abbr)) {
			abbr = StringUtils.EMPTY;
		}

		return abbr;
	}

	private static final String ABBREVIATION = "abbreviation";
}