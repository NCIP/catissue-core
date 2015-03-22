package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class SpecimenLabelPrintRule {
	private String specimenClass;
	
	private String specimenType;
	
	private String labelType;
	
	private IpAddressMatcher ipAddressMatcher;
	
	private String userLogin;
	
	private String printerName;
	
	private List<LabelTmplToken> dataTokens = new ArrayList<LabelTmplToken>();
	
	private ResourceBundleMessageSource resourceBundle;	
	
	public String getSpecimenClass() {
		return specimenClass;
	}

	public void setSpecimenClass(String specimenClass) {
		this.specimenClass = specimenClass;
	}

	public String getSpecimenType() {
		return specimenType;
	}

	public void setSpecimenType(String specimenType) {
		this.specimenType = specimenType;
	}

	public String getLabelType() {
		return labelType;
	}

	public void setLabelType(String labelType) {
		this.labelType = labelType;
	}

	public IpAddressMatcher getIpAddressMatcher() {
		return ipAddressMatcher;
	}

	public void setIpAddressMatcher(IpAddressMatcher ipAddressMatcher) {
		this.ipAddressMatcher = ipAddressMatcher;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getPrinterName() {
		return printerName;
	}

	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}

	public List<LabelTmplToken> getDataTokens() {
		return dataTokens;
	}

	public void setDataTokens(List<LabelTmplToken> dataTokens) {
		this.dataTokens = dataTokens;
	}
	
	public ResourceBundleMessageSource getResourceBundle() {
		return resourceBundle;
	}

	public void setResourceBundle(ResourceBundleMessageSource resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	public boolean isApplicableFor(Specimen specimen, User user, String ipAddr) {
		if (!isWildCard(specimenClass) && !specimen.getSpecimenClass().equals(specimenClass)) {
			return false;
		}
		
		if (!isWildCard(specimenType) && !specimen.getSpecimenType().equals(specimenType)) {
			return false;
		}
		
		if (!isWildCard(userLogin) && !user.getLoginName().equals(userLogin)) {
			return false;
		}
		
		if (ipAddressMatcher != null && !ipAddressMatcher.matches(ipAddr)) {
			return false;
		}

		return true;
	}
	
	public String formatPrintData(Specimen specimen) {
		try {
			Map<String, String> dataItems = new HashMap<String, String>();
			for (LabelTmplToken token : dataTokens) {
				String propName = resourceBundle.getMessage(token.getName(), null, Locale.getDefault());
				dataItems.put(propName, token.getReplacement(specimen));
			}
			
			return new ObjectMapper().writeValueAsString(dataItems);			
		} catch (Exception e) {
			throw OpenSpecimenException.serverError(e);
		}
	}	
	
	private boolean isWildCard(String str) {
		return StringUtils.isNotBlank(str) && str.trim().equals("*");
	}	
}
