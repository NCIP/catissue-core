
package com.krishagni.catissueplus.core.printer.printService.factory.impl;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.printer.printRule.domain.SpecimenPrintRule;
import com.krishagni.catissueplus.core.printer.printRule.domain.factory.PrintRuleErrorCode;
import com.krishagni.catissueplus.core.printer.printService.factory.SpecimenLabelPrinterFactory;
import com.krishagni.catissueplus.core.tokens.factory.TokenFactory;

import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;

public class SpecimenLabelPrinterFactoryImpl implements SpecimenLabelPrinterFactory {

	private static HashMap<String, SpecimenPrintRule> printLabelRules;

	@Autowired
	private DaoFactory daoFactory;

	@Autowired
	private TokenFactory tokenFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setTokenFactory(TokenFactory tokenFactory) {
		this.tokenFactory = tokenFactory;
	}

	public static final String ANY = "Any";

	private static final String NEWLINE = "\r\n";

	private static final String END = "END";

	private static final String PRINT_RULE = "print rule";

	private static final String LABEL_NAME = "LABEL";

	private static final String CP_TITLE = "CP Short Title";

	private static final String DISPLAY_LABELQUANTITY = "LabelQuantity";

	private static final String PARTICIPANT_PROTOCOL_IDENTIFIER = "PPI";

	@Override
	public void printLabel(Specimen specimen, String ipAddress, String loginName, String cpShortTitle) throws IOException {
		if (ipAddress == null) {
			ipAddress = ANY;
		}
		populatePrintRules();
		SpecimenPrintRule printRule = getRule(specimen.getSpecimenClass(), specimen.getSpecimenType(), ipAddress,
				loginName, cpShortTitle);
		if (printRule == null) {
			reportError(PrintRuleErrorCode.NOT_FOUND, PRINT_RULE);
		}
		createFile(specimen, printRule);
	}

	private void populatePrintRules() {
		List<SpecimenPrintRule> rules = daoFactory.getSpecimenPrintRuleDao().getRules(0);
		printLabelRules = new HashMap<String, SpecimenPrintRule>();
		for (SpecimenPrintRule rule : rules) {
			String key = rule.getSpecimenClass() + "_" + rule.getSpecimenType() + "_" + rule.getWorkstationIP();
			printLabelRules.put(key, rule);
		}
	}

	public void createFile(Specimen specimen, SpecimenPrintRule printRule) throws IOException {
		String printDirectory = CommonServiceLocator.getInstance().getPropDirPath()
				+ XMLPropertyHandler.getValue("label.print.file.dir");
		checkDirectory(printDirectory);
		BufferedWriter fileWriter = null;

		String fileName = printDirectory + "/" + getFileName(specimen.getLabel()) + ".cmd";
		FileWriter file = new FileWriter(fileName);
		fileWriter = new BufferedWriter(file);

		String cmdFileData = getDataToPrint(specimen, printRule);
		if (cmdFileData != null && !cmdFileData.equals("")) {
			fileWriter.write(cmdFileData);
		}
		fileWriter.close();
	}

	private void checkDirectory(String printDirectory) {
		File dir = new File(printDirectory);
		if (dir == null || !dir.exists()) {
			dir.mkdir();
		}
	}

	private String getFileName(String label) {
		String fileName = label + "_" + new Timestamp(System.currentTimeMillis());;
		return fileName;
	}

	public static SpecimenPrintRule getRule(String specimenClass, String specimenType, String workStationIP,
			String loginName, String cpShortTitle) throws IOException {
		String matchedKey = null;

		Set<String> keySet = new HashSet<String>();
		keySet.add(getKey(specimenClass, specimenType, cpShortTitle, loginName, workStationIP));
		keySet.add(getKey(specimenClass, specimenType, cpShortTitle, loginName, ANY));
		keySet.add(getKey(specimenClass, specimenType, cpShortTitle, ANY, workStationIP));
		keySet.add(getKey(specimenClass, specimenType, cpShortTitle, ANY, ANY));
		keySet.add(getKey(specimenClass, specimenType, ANY, loginName, workStationIP));
		keySet.add(getKey(specimenClass, specimenType, ANY, loginName, ANY));
		keySet.add(getKey(specimenClass, specimenType, ANY, ANY, workStationIP));
		keySet.add(getKey(specimenClass, specimenType, ANY, ANY, ANY));

		keySet.add(getKey(specimenClass, ANY, cpShortTitle, loginName, workStationIP));
		keySet.add(getKey(specimenClass, ANY, cpShortTitle, loginName, ANY));
		keySet.add(getKey(specimenClass, ANY, cpShortTitle, ANY, workStationIP));
		keySet.add(getKey(specimenClass, ANY, cpShortTitle, ANY, ANY));
		keySet.add(getKey(specimenClass, ANY, ANY, loginName, workStationIP));
		keySet.add(getKey(specimenClass, ANY, ANY, loginName, ANY));
		keySet.add(getKey(specimenClass, ANY, ANY, ANY, workStationIP));
		keySet.add(getKey(specimenClass, ANY, ANY, ANY, ANY));

		keySet.add(getKey(ANY, ANY, cpShortTitle, loginName, workStationIP));
		keySet.add(getKey(ANY, ANY, cpShortTitle, loginName, ANY));
		keySet.add(getKey(ANY, ANY, cpShortTitle, ANY, workStationIP));
		keySet.add(getKey(ANY, ANY, cpShortTitle, ANY, ANY));
		keySet.add(getKey(ANY, ANY, ANY, loginName, workStationIP));
		keySet.add(getKey(ANY, ANY, ANY, loginName, ANY));
		keySet.add(getKey(ANY, ANY, ANY, ANY, workStationIP));
		keySet.add(getKey(ANY, ANY, ANY, ANY, ANY));

		for (String key : keySet) {
			if (printLabelRules.containsKey(key)) {
				matchedKey = key;
				break;
			}
		}

		return printLabelRules.get(matchedKey);
	}

	private static String getKey(String specimenClass, String specimenType, String cpTitle, String loginName,
			String workStationIP) {
		StringBuilder builder = new StringBuilder(100);
		builder.append(specimenClass).append("_").append(specimenType).append("_").append(cpTitle).append("_")
				.append(loginName).append("_").append(workStationIP);

		return builder.toString();
	}

	public String getDataToPrint(Specimen specimen, SpecimenPrintRule printRule) {

		Set<String> dataOnLabels = printRule.getDataOnLabel();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(LABEL_NAME + " = \"" + printRule.getLabelType() + "\"" + NEWLINE);

		for (String dataOnLabel : dataOnLabels) {
			String tokenValue = tokenFactory.getTokenValue(dataOnLabel, specimen);
			stringBuilder.append(dataOnLabel + " = \"" + tokenValue + "\"" + NEWLINE);
		}

		if (printRule.getPrinterName() != null) {
			stringBuilder.append(printRule.getPrinterName() + " = \"" + printRule.getPrinterName() + "\"" + NEWLINE);
		}

		String cpTitle = CommonUtilities.toString(specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration()
				.getCollectionProtocol().getShortTitle());

		stringBuilder.append(CP_TITLE + " = \"" + cpTitle + "\"" + NEWLINE);

		String ppi = CommonUtilities.toString(specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration()
				.getProtocolParticipantIdentifier());

		if (ppi != null && !ppi.equals("")) {
			stringBuilder.append(PARTICIPANT_PROTOCOL_IDENTIFIER + " = \"" + ppi + "\"" + NEWLINE);
		}
		stringBuilder.append(DISPLAY_LABELQUANTITY + " = \"1\"" + NEWLINE);
		stringBuilder.append(END + NEWLINE);

		return stringBuilder.toString();
	}
}
