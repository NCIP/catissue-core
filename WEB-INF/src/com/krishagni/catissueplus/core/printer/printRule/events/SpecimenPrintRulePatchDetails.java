
package com.krishagni.catissueplus.core.printer.printRule.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.events.UserInfo;
import com.krishagni.catissueplus.core.printer.printRule.domain.SpecimenPrintRule;

public class SpecimenPrintRulePatchDetails {

	private Long id;

	private String name;

	private String specimenClass = "";

	private String specimenType = "";

	private String labelType;

	private String workstationIP;

	private String cpShortTitle;

	private UserInfo loginName;

	private List<String> dataOnLabel = new ArrayList<String>();

	private String printerName;

	private List<String> modifiedAttributes = new ArrayList<String>();

	public boolean isNameModified() {
		return modifiedAttributes.contains("name");
	}

	public boolean isSpecimenClassModified() {
		return modifiedAttributes.contains("specimenClass");
	}

	public boolean isSpecimenTypeModified() {
		return modifiedAttributes.contains("specimenType");
	}

	public boolean isLabelTypeModified() {
		return modifiedAttributes.contains("labelType");
	}

	public boolean isWorkstationIPModified() {
		return modifiedAttributes.contains("workstationIP");
	}

	public boolean isDataOnLabelModified() {
		return modifiedAttributes.contains("dataOnLabel");
	}

	public boolean isPrinterNameModified() {
		return modifiedAttributes.contains("printerName");
	}

	public boolean isLoginNameModified() {
		return modifiedAttributes.contains("loginName");
	}

	public boolean isCpShortTitleModified() {
		return modifiedAttributes.contains("cpShortTitle");
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public String getWorkstationIP() {
		return workstationIP;
	}

	public void setWorkstationIP(String workstationIP) {
		this.workstationIP = workstationIP;
	}

	public List<String> getDataOnLabel() {
		return dataOnLabel;
	}

	public void setDataOnLabel(List<String> dataOnLabel) {
		this.dataOnLabel = dataOnLabel;
	}

	public String getPrinterName() {
		return printerName;
	}

	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}

	public String getCpShortTitle() {
		return cpShortTitle;
	}

	public void setCpShortTitle(String cpShortTitle) {
		this.cpShortTitle = cpShortTitle;
	}

	public UserInfo getLoginName() {
		return loginName;
	}

	public void setLoginName(UserInfo loginName) {
		this.loginName = loginName;
	}

	public List<String> getModifiedAttributes() {
		return modifiedAttributes;
	}

	public void setModifiedAttributes(List<String> modifiedAttributes) {
		this.modifiedAttributes = modifiedAttributes;
	}

	public static SpecimenPrintRuleDetails fromDomain(SpecimenPrintRule specimenPrintRule) {
		SpecimenPrintRuleDetails details = new SpecimenPrintRuleDetails();
		details.setId(specimenPrintRule.getId());
		details.setLabelType(specimenPrintRule.getLabelType());
		details.setName(specimenPrintRule.getName());
		details.setDataOnLabel(populatenewDataOnLabel(specimenPrintRule.getDataOnLabel()));
		details.setSpecimenClass(specimenPrintRule.getSpecimenClass());
		details.setSpecimenType(specimenPrintRule.getSpecimenType());
		details.setWorkstationIP(specimenPrintRule.getWorkstationIP());
		details.setPrinterName(specimenPrintRule.getPrinterName());
		details.setLoginName(getLoginNameDetails(specimenPrintRule));
		details.setCpShortTitle(specimenPrintRule.getCpShortTitle());
		return details;
	}

	private static UserInfo getLoginNameDetails(SpecimenPrintRule specimenPrintRule) {
		UserInfo userInfo = new UserInfo();
		userInfo.setLoginName(specimenPrintRule.getLoginName());
		return userInfo;
	}

	private static List<String> populatenewDataOnLabel(Set<String> dataOnLabel) {
		List<String> data = new ArrayList<String>();
		data.addAll(dataOnLabel);
		return data;
	}

	//	private static List<String> populateLabelType(String labelType) {
	//		String[] labelTypes = labelType.split("\\,");
	//		List<String> labelTypeSet = new ArrayList<String>();
	//		for (String label : labelTypes) {
	//			labelTypeSet.add(label);
	//		}
	//		return labelTypeSet;
	//	}

	//	private static Set<String> populateDataOnLabels(String dataOnLabel) {
	//		String[] dataOnLabels = dataOnLabel.split("\\,");
	//		Set<String> dataOnLabelsSet = new HashSet<String>();
	//		for (String label : dataOnLabels) {
	//			dataOnLabelsSet.add(label);
	//		}
	//		return dataOnLabelsSet;
	//	}

}
