
package com.krishagni.catissueplus.core.printer.printService.factory.impl;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.printer.printRule.domain.SpecimenPrintRule;
import com.krishagni.catissueplus.core.printer.printRule.domain.factory.PrintRuleErrorCode;
import com.krishagni.catissueplus.core.printer.printService.factory.DisplayPrintLabelType;
import com.krishagni.catissueplus.core.printer.printService.factory.SpecimenLabelPrinterFactory;

import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;

public class SpecimenLabelPrinterFactoryImpl implements SpecimenLabelPrinterFactory {

	private static HashMap<String, SpecimenPrintRule> printLabelRules;

	@Autowired
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public static final String ANY = "Any";

	private static final String SPECIMEN_LABEL = "Specimen Label";

	private static final String SPECIMEN_CLASS = "Specimen Class";

	private static final String SPECIMEN_BARCODE = "Specimen Barcode";

	private static final String SPECIMEN_TYPE = "Specimen Type";

	private static final String CONCENTRATION = "Concentration";

	private static final String QUANTITY = "Quantity";

	private static final String SPECIMEN_TISSUE_SITE = "Tissue site";

	private static final String NEWLINE = "\r\n";

	private static final String PATHOLOGICAL_STATUS = "Pathological status";

	private static final String SPECIMEN_IDENTIFIER = "Specimen Identifier";

	private static final String SPECIMEN_COLLECTION_STATUS = "Collection Status";

	private static final String SPECIMEN_CREATED_ON = "Created On";

	private static final String SPECIMEN_LINEAGE = "Lineage";

	private static final String SPECIMEN_COMMENT = "Comment";

	private static final String END = "END";

	private static final String SPECIMEN_STORAGE_CONTAINER_NAME = "Storage Container";

	private static final String SPECIMEN_POSITION_DIMENSION_ONE = "Position Dimension One";

	private static final String SPECIMEN_POSITION_DIMENSION_TWO = "Position Dimension Two";

	private static final String PRINT_RIULE = "print rule";

	@Override
	public void printLabel(Specimen specimen, String ipAddress) throws IOException {
		if (ipAddress == null) {
			ipAddress = ANY;
		}
		populatePrintRules();
		SpecimenPrintRule printRule = getRule(specimen.getSpecimenClass(), specimen.getSpecimenType(), ipAddress);
		if (printRule == null) {
			reportError(PrintRuleErrorCode.NOT_FOUND, PRINT_RIULE);
		}
		createFile(specimen, printRule);
	}

	private void populatePrintRules() {
		List<SpecimenPrintRule> rules = daoFactory.getSpecimenPrintRuleDao().getRules();
		printLabelRules = new HashMap<String, SpecimenPrintRule>();
		for (SpecimenPrintRule rule : rules) {
			String key = rule.getSpecimenClass() + "_" + rule.getSpecimenType() + "_" + rule.getWorkstationIP();
			printLabelRules.put(key, rule);
		}
	}

	public void createFile(Specimen specimen, SpecimenPrintRule printRule) throws IOException {
		String printDirectory = CommonServiceLocator.getInstance().getPropDirPath() + XMLPropertyHandler.getValue("label.print.file.dir");
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
		String fileName = label + "_" + new Timestamp(System.currentTimeMillis()); ;
		return fileName;
	}

	public static SpecimenPrintRule getRule(String specimenClass, String specimenType, String workStationIP)
			throws IOException {

		String key = specimenClass + "_" + specimenType + "_" + workStationIP;
		String keyAnyType = specimenClass + "_" + ANY + "_" + workStationIP;
		String keyAnyClass = ANY + "_" + specimenType + "_" + workStationIP;
		String keyAnyIP = specimenClass + "_" + specimenType + "_" + ANY;
		String keyAnyTypeAnyIP = specimenClass + "_" + ANY + "_" + ANY;
		String keyAnyClassAnyType = ANY + "_" + ANY + "_" + workStationIP;
		String keyAnyClassAnyIP = ANY + "_" + specimenType + "_" + ANY;
		String keyAny = ANY + "_" + ANY + "_" + ANY;

		if (printLabelRules.containsKey(key)) {
			return printLabelRules.get(key);
		}
		else if (printLabelRules.containsKey(keyAnyType)) {
			return printLabelRules.get(keyAnyType);
		}
		else if (printLabelRules.containsKey(keyAnyClass)) {
			return printLabelRules.get(keyAnyClass);
		}
		else if (printLabelRules.containsKey(keyAnyIP)) {
			return printLabelRules.get(keyAnyIP);
		}
		else if (printLabelRules.containsKey(keyAnyTypeAnyIP)) {
			return printLabelRules.get(keyAnyTypeAnyIP);
		}
		else if (printLabelRules.containsKey(keyAnyClassAnyType)) {
			return printLabelRules.get(keyAnyClassAnyType);
		}
		else if (printLabelRules.containsKey(keyAnyClassAnyIP)) {
			return printLabelRules.get(keyAnyClassAnyIP);
		}
		else if (printLabelRules.containsKey(keyAny)) {
			return printLabelRules.get(keyAny);
		}
		return null;
	}

	public String getDataToPrint(Specimen specimen, SpecimenPrintRule printRule) {
		String[] dataOnLabels = printRule.getDataOnLabel().split(",");

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder
				.append(DisplayPrintLabelType.LABEL_NAME.value() + " = \"" + printRule.getLabelType() + "\"" + NEWLINE);

		for (String dataOnLabel : dataOnLabels) {
			if (dataOnLabel.equalsIgnoreCase(SPECIMEN_LABEL)) {
				stringBuilder.append(DisplayPrintLabelType.LABEL.value() + " = \"" + specimen.getLabel() + "\"" + NEWLINE);
			}
			
			else if (dataOnLabel.equalsIgnoreCase(SPECIMEN_IDENTIFIER)) {
				stringBuilder.append(DisplayPrintLabelType.IDENTIFIER + " = \"" + specimen.getPathologicalStatus()
						+ "\"" + NEWLINE);
			}

			else if (dataOnLabel.equalsIgnoreCase(SPECIMEN_BARCODE)) {
				stringBuilder.append(DisplayPrintLabelType.BARCODE.value() + " = \"" + specimen.getBarcode() + "\"" + NEWLINE);
			}

			else if (dataOnLabel.equalsIgnoreCase(SPECIMEN_TYPE)) {
				stringBuilder
						.append(DisplayPrintLabelType.TYPE.value() + " = \"" + specimen.getSpecimenType() + "\"" + NEWLINE);
			}

			else if (dataOnLabel.equalsIgnoreCase(SPECIMEN_CLASS)) {
				stringBuilder.append(DisplayPrintLabelType.CLASS.value() + " = \"" + specimen.getSpecimenClass() + "\""
						+ NEWLINE);
			}

			else if (dataOnLabel.equalsIgnoreCase(CONCENTRATION)) {
				stringBuilder.append(DisplayPrintLabelType.CONCENTRATION.value() + " = \""
						+ specimen.getConcentrationInMicrogramPerMicroliter() + "\"" + NEWLINE);
			}

			else if (dataOnLabel.equalsIgnoreCase(QUANTITY)) {
				stringBuilder.append(DisplayPrintLabelType.QUANTITY.value() + " = \"" + specimen.getAvailableQuantity() + "\""
						+ NEWLINE);
			}

			else if (dataOnLabel.equalsIgnoreCase(PATHOLOGICAL_STATUS)) {
				stringBuilder.append(DisplayPrintLabelType.PATHOLOGICAL_STATUS + " = \"" + specimen.getPathologicalStatus()
						+ "\"" + NEWLINE);
			}
			
			else if (dataOnLabel.equalsIgnoreCase(SPECIMEN_IDENTIFIER)) {
				stringBuilder.append(DisplayPrintLabelType.IDENTIFIER + " = \"" + specimen.getPathologicalStatus()
						+ "\"" + NEWLINE);
			}
			
			else if (dataOnLabel.equalsIgnoreCase(SPECIMEN_LINEAGE)) {
				stringBuilder.append(DisplayPrintLabelType.LINEAGE + " = \"" + specimen.getLineage() + "\"" + NEWLINE);
			}

			else if (dataOnLabel.equalsIgnoreCase(SPECIMEN_COMMENT)) {
				stringBuilder.append(DisplayPrintLabelType.COMMENT + " = \"" + specimen.getComment() + "\"" + NEWLINE);
			}

			else if (dataOnLabel.equalsIgnoreCase(SPECIMEN_COLLECTION_STATUS)) {
				stringBuilder.append(DisplayPrintLabelType.COLLECTION_STATUS + " = \"" + specimen.getCollectionStatus() + "\""
						+ NEWLINE);
			}

			else if (dataOnLabel.equalsIgnoreCase(SPECIMEN_CREATED_ON)) {
				stringBuilder.append(DisplayPrintLabelType.CREATED_ON + " = \"" + specimen.getCreatedOn() + "\"" + NEWLINE);
			}

			else if (dataOnLabel.equalsIgnoreCase(SPECIMEN_TISSUE_SITE)) {
				stringBuilder.append(DisplayPrintLabelType.TISSUE_SITE + " = \"" + specimen.getTissueSite() + "\"" + NEWLINE);
			}

			else if (dataOnLabel.equalsIgnoreCase(SPECIMEN_STORAGE_CONTAINER_NAME)){ 
				stringBuilder.append(DisplayPrintLabelType.STORAGE_CONTAINER_NAME + " = \"" + CommonUtilities
						.toString(specimen.getSpecimenPosition().getStorageContainer().getName()) + "\"" + NEWLINE);
			}
			
			else if (dataOnLabel.equalsIgnoreCase(SPECIMEN_POSITION_DIMENSION_ONE)){ 
				stringBuilder.append(DisplayPrintLabelType.POSITION_DIMENSION_ONE + " = \"" + CommonUtilities
						.toString(specimen.getSpecimenPosition().getPositionDimensionOne()) + "\"" + NEWLINE);
			}
			
			else if (dataOnLabel.equalsIgnoreCase(SPECIMEN_POSITION_DIMENSION_TWO)){ 
				stringBuilder.append(DisplayPrintLabelType.POSITION_DIMENSION_TWO + " = \"" + CommonUtilities
						.toString(specimen.getSpecimenPosition().getPositionDimensionTwo()) + "\"" + NEWLINE);
			}
			
		}

		if (printRule.getPrinterName() != null) {
			stringBuilder.append(DisplayPrintLabelType.DISPLAY_PRINTER + " = \"" + printRule.getPrinterName() + "\""
					+ NEWLINE);
		}
		
		String cpTitle = CommonUtilities.toString(specimen.getSpecimenCollectionGroup()
				.getCollectionProtocolRegistration().getCollectionProtocol().getShortTitle());
		
		stringBuilder.append(DisplayPrintLabelType.CP_TITLE + " = \"" + cpTitle + "\"" + NEWLINE);
		
		String ppi = CommonUtilities.toString(specimen.getSpecimenCollectionGroup()
				.getCollectionProtocolRegistration().getProtocolParticipantIdentifier());
		
		if (ppi != null && !ppi.equals("")){
			stringBuilder.append(DisplayPrintLabelType.PARTICIPANT_PROTOCOL_IDENTIFIER + " = \"" + ppi + "\"" + NEWLINE);
		}

		stringBuilder.append(DisplayPrintLabelType.DISPLAY_LABELQUANTITY + " = \"1\"" + NEWLINE);
		stringBuilder.append(END + NEWLINE);

		return stringBuilder.toString();
	}
}
