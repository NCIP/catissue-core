
package com.krishagni.catissueplus.core.printer.printRule.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import java.util.Set;

import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.printer.printRule.domain.SpecimenPrintRule;
import com.krishagni.catissueplus.core.printer.printRule.domain.factory.PrintLabelType;
import com.krishagni.catissueplus.core.printer.printRule.domain.factory.PrintRuleErrorCode;
import com.krishagni.catissueplus.core.printer.printRule.domain.factory.SpecimenPrintRuleFactory;
import com.krishagni.catissueplus.core.printer.printRule.events.SpecimenPrintRuleDetails;

public class SpecimenPrintRuleFactoryImpl implements SpecimenPrintRuleFactory {

	private static final String PRINT_RULE_NAME = "print rule name";

	private static final String PRINTER_NAME = "printer name";

	private static final String DATA_ON_LABEL = "data on label";

	private static final String SPECIMEN_CLASS = "specimen class";

	private static final String SPECIMEN_TYPE = "specimen type";

	private static final String WORKSTATION_IP = "workstation ip";

	private static final String LABEL_TYPE = "label type";

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

	private void setLabelType(SpecimenPrintRule specimenPrintRule, Set<String> loginTypes,
			ObjectCreationException exceptionHandler) {

		if (loginTypes.isEmpty()) {
			exceptionHandler.addError(PrintRuleErrorCode.INVALID_ATTR_VALUE, LABEL_TYPE);
			return;
		}
		
		StringBuilder labelTypeStr = new StringBuilder("");
		for (String loginType : loginTypes) {
			if (PrintLabelType.getEnumNameForValue(loginType) == null) {
				exceptionHandler.addError(PrintRuleErrorCode.INVALID_ATTR_VALUE, LABEL_TYPE);
				return;
			}
			labelTypeStr.append(loginType + ",");
		}
		specimenPrintRule.setLabelType(labelTypeStr.substring(0, labelTypeStr.lastIndexOf(",")).toString());

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

	private void setDataOnLabel(SpecimenPrintRule specimenPrintRule, Set<String> dataOnLabels,
			ObjectCreationException exceptionHandler) {
		if (dataOnLabels.isEmpty()) {
			exceptionHandler.addError(PrintRuleErrorCode.INVALID_ATTR_VALUE, DATA_ON_LABEL);
			return;
		}
		specimenPrintRule.setDataOnLabel(getDataOnLabelString(dataOnLabels));
	}

	private String getDataOnLabelString(Set<String> dataOnLabels) {
		StringBuilder dataOnLabel = new StringBuilder("");
		for (String data : dataOnLabels) {
			dataOnLabel.append(data + ",");
		}
		return dataOnLabel.substring(0, dataOnLabel.lastIndexOf(",")).toString();
	}

	@Override
	public SpecimenPrintRule patchSpecimenPrintRule(SpecimenPrintRule specimenPrintRule, SpecimenPrintRuleDetails details) {
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
		exception.checkErrorAndThrow();
		return specimenPrintRule;
	}

}
