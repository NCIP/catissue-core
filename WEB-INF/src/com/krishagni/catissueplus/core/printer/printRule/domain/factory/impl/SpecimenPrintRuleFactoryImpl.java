
package com.krishagni.catissueplus.core.printer.printRule.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import java.util.HashSet;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.UserInfo;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.printer.printRule.domain.SpecimenPrintRule;
import com.krishagni.catissueplus.core.printer.printRule.domain.factory.PrintRuleErrorCode;
import com.krishagni.catissueplus.core.printer.printRule.domain.factory.SpecimenPrintRuleFactory;
import com.krishagni.catissueplus.core.printer.printRule.events.SpecimenPrintRuleDetails;
import com.krishagni.catissueplus.core.printer.printRule.events.SpecimenPrintRulePatchDetails;

public class SpecimenPrintRuleFactoryImpl implements SpecimenPrintRuleFactory {

	private DaoFactory daoFactory;

	private static final String PRINT_RULE_NAME = "print rule name";

	private static final String PRINTER_NAME = "printer name";

	private static final String DATA_ON_LABEL = "data on label";

	private static final String SPECIMEN_CLASS = "spceimen class";

	private static final String SPECIMEN_TYPE = "specimen type";

	private static final String WORKSTATION_IP = "workstation ip";

	private static final String LABEL_TYPE = "label type";

	private static final String LOGIN_NAME = "login name";

	private static final String CP_SHORT_TITLE = "cp short title";

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public SpecimenPrintRule createSpecimenPrintRule(SpecimenPrintRuleDetails details) {
		ObjectCreationException exceptionHandler = new ObjectCreationException();

		SpecimenPrintRule specimenPrintRule = new SpecimenPrintRule();
		setName(specimenPrintRule, details.getName(), exceptionHandler);
		setSpecimenClass(specimenPrintRule, details.getSpecimenClass(), exceptionHandler);
		setSpecimenType(specimenPrintRule, details.getSpecimenType(), exceptionHandler);
		setLabelType(specimenPrintRule, details.getLabelType(), exceptionHandler);
		setWorkstationIP(specimenPrintRule, details.getWorkstationIP(), exceptionHandler);
		setPrinterName(specimenPrintRule, details.getPrinterName(), exceptionHandler);
		setDataOnLabel(specimenPrintRule, details.getDataOnLabel(), exceptionHandler);
		setLoginName(specimenPrintRule, details.getLoginName(), exceptionHandler);
		setCpShortTitle(specimenPrintRule, details.getCpShortTitle(), exceptionHandler);
		exceptionHandler.checkErrorAndThrow();
		return specimenPrintRule;
	}

	private void setName(SpecimenPrintRule specimenPrintRule, String name, ObjectCreationException exceptionHandler) {
		if (isBlank(name)) {
			exceptionHandler.addError(PrintRuleErrorCode.INVALID_ATTR_VALUE, PRINT_RULE_NAME);
			return;
		}
		specimenPrintRule.setName(name);
	}

	private void setSpecimenClass(SpecimenPrintRule specimenPrintRule, String specimenClass,
			ObjectCreationException exceptionHandler) {
		if (isBlank(specimenClass) || specimenClass.equalsIgnoreCase("Any")) {
			return;
		}

		if (!CommonValidator.isValidPv(specimenClass, SPECIMEN_CLASS)) {
			exceptionHandler.addError(PrintRuleErrorCode.INVALID_ATTR_VALUE, SPECIMEN_CLASS);
			return;
		}
		specimenPrintRule.setSpecimenClass(specimenClass);
	}

	private void setSpecimenType(SpecimenPrintRule specimenPrintRule, String specimenType,
			ObjectCreationException exceptionHandler) {
		if (isBlank(specimenType) || specimenType.equalsIgnoreCase("Any")) {
			return;
		}

		if (!CommonValidator.isValidPv(specimenType, SPECIMEN_TYPE)) {
			exceptionHandler.addError(PrintRuleErrorCode.INVALID_ATTR_VALUE, SPECIMEN_TYPE);
			return;
		}
		specimenPrintRule.setSpecimenType(specimenType);
	}

	private void setLabelType(SpecimenPrintRule specimenPrintRule, String labelType,
			ObjectCreationException exceptionHandler) {

		if (labelType.isEmpty()) {
			exceptionHandler.addError(PrintRuleErrorCode.INVALID_ATTR_VALUE, LABEL_TYPE);
			return;
		}
		specimenPrintRule.setLabelType(labelType);

	}

	private void setWorkstationIP(SpecimenPrintRule specimenPrintRule, String workstationIP,
			ObjectCreationException exceptionHandler) {
		if (isBlank(workstationIP) || workstationIP.equalsIgnoreCase("Any")) {
			return;
		}

		if (!CommonValidator.isValidIP(workstationIP)) {
			exceptionHandler.addError(PrintRuleErrorCode.INVALID_ATTR_VALUE, WORKSTATION_IP);
			return;
		}
		specimenPrintRule.setWorkstationIP(workstationIP);
	}

	private void setPrinterName(SpecimenPrintRule specimenPrintRule, String printerName,
			ObjectCreationException exceptionHandler) {
		if (isBlank(printerName)) {
			exceptionHandler.addError(PrintRuleErrorCode.INVALID_ATTR_VALUE, PRINTER_NAME);
			return;
		}
		specimenPrintRule.setPrinterName(printerName);
	}

	private void setDataOnLabel(SpecimenPrintRule specimenPrintRule, List<String> dataOnLabels,
			ObjectCreationException exceptionHandler) {
		if (dataOnLabels.isEmpty()) {
			exceptionHandler.addError(PrintRuleErrorCode.INVALID_ATTR_VALUE, DATA_ON_LABEL);
			return;
		}
		specimenPrintRule.setDataOnLabel(new HashSet<String>(dataOnLabels));
	}

	private void setCpShortTitle(SpecimenPrintRule specimenPrintRule, String cpShortTitle,
			ObjectCreationException exceptionHandler) {
		CollectionProtocol collectionProtocol = daoFactory.getCollectionProtocolDao().getCPByShortTitle(cpShortTitle);
		if (collectionProtocol == null) {
			exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, CP_SHORT_TITLE);
			return;
		}
		specimenPrintRule.setCpShortTitle(collectionProtocol.getShortTitle());

	}

	private void setLoginName(SpecimenPrintRule specimenPrintRule, UserInfo userInfo,
			ObjectCreationException exceptionHandler) {
		User user = daoFactory.getUserDao().getUserByLoginNameAndDomainName(userInfo.getLoginName(),
				userInfo.getDomainName());

		if (user == null) {
			exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, LOGIN_NAME);
			return;
		}
		specimenPrintRule.setLoginName(user.getLoginName());

	}

	//
	//	private String getDataOnLabelString(Set<String> dataOnLabels) {
	//		StringBuilder dataOnLabel = new StringBuilder("");
	//		for (String data : dataOnLabels) {
	//			dataOnLabel.append(data + ",");
	//		}
	//		return dataOnLabel.substring(0, dataOnLabel.lastIndexOf(",")).toString();
	//	}

	@Override
	public SpecimenPrintRule patchSpecimenPrintRule(SpecimenPrintRule specimenPrintRule,
			SpecimenPrintRulePatchDetails details) {
		ObjectCreationException exception = new ObjectCreationException();
		if (details.isNameModified()) {
			setName(specimenPrintRule, details.getName(), exception);
		}

		if (details.isDataOnLabelModified()) {
			setDataOnLabel(specimenPrintRule, details.getDataOnLabel(), exception);
		}

		if (details.isLabelTypeModified()) {
			setLabelType(specimenPrintRule, details.getLabelType(), exception);
		}

		if (details.isSpecimenClassModified()) {
			setSpecimenClass(specimenPrintRule, details.getSpecimenClass(), exception);
		}

		if (details.isSpecimenTypeModified()) {
			setSpecimenType(specimenPrintRule, details.getSpecimenType(), exception);
		}

		if (details.isWorkstationIPModified()) {
			setWorkstationIP(specimenPrintRule, details.getWorkstationIP(), exception);
		}

		if (details.isPrinterNameModified()) {
			setPrinterName(specimenPrintRule, details.getPrinterName(), exception);
		}

		if (details.isCpShortTitleModified()) {
			setCpShortTitle(specimenPrintRule, details.getCpShortTitle(), exception);
		}

		if (details.isLoginNameModified()) {
			setLoginName(specimenPrintRule, details.getLoginName(), exception);
		}
		exception.checkErrorAndThrow();
		return specimenPrintRule;
	}

}
