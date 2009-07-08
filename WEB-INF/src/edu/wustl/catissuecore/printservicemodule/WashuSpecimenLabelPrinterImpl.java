
package edu.wustl.catissuecore.printservicemodule;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.util.global.CommonUtilities;

/**
 * This Class is used to define method for Specimen label printing.
 * @author Renuka_Bajpai
 */
public class WashuSpecimenLabelPrinterImpl extends SpecimenLabelPrinterImpl
{

	/**
	 * This method adds Data To Print.
	 * @param specimen Specimen object.
	 * @param listMap list Map
	 * @param printerType printer Type
	 * @param printerLocation printer Location
	 * @param ipAddress IP Address
	 */
	@Override
	protected void addDataToPrint(Specimen specimen, ArrayList listMap, String printerType,
			String printerLocation, String ipAddress)
	{
		final LinkedHashMap dataMap = new LinkedHashMap();
		dataMap.put("class", specimen.getClassName());
		dataMap.put("id", specimen.getId().toString());
		String label = specimen.getLabel();
		dataMap.put(PrintWebServiceConstants.USER_IPADDRESS, ipAddress);
		if (specimen.getClassName() != null)
		{
			dataMap.put(PrintWebServiceConstants.SPECIMEN_CLASS, CommonUtilities.toString(specimen
					.getClassName()));
		}
		if (specimen.getId() != null)
		{
			dataMap.put(PrintWebServiceConstants.SPECIMEN_IDENTIFIER, CommonUtilities
					.toString(specimen.getId()));
		}
		if (specimen.getSpecimenType() != null)
		{
			dataMap.put(PrintWebServiceConstants.SPECIMEN_TYPE, CommonUtilities.toString(specimen
					.getSpecimenType()));
		}
		if (label == null)
		{
			label = specimen.getSpecimenType();
		}
		dataMap.put(PrintWebServiceConstants.SPECIMEN_LABEL, CommonUtilities.toString(label));
		if (specimen.getBarcode() != null)
		{
			dataMap.put(PrintWebServiceConstants.SPECIMEN_BARCODE, CommonUtilities
					.toString(specimen.getBarcode()));
		}
		if (specimen.getSpecimenCharacteristics() != null)
		{
			dataMap.put(PrintWebServiceConstants.SPECIMEN_TISSUE_SITE, CommonUtilities
					.toString(specimen.getSpecimenCharacteristics().getTissueSite()));
		}
		if (specimen.getCollectionStatus() != null)
		{
			dataMap.put(PrintWebServiceConstants.SPECIMEN_COLLECTION_STATUS, CommonUtilities
					.toString(specimen.getCollectionStatus()));
		}
		if (specimen.getComment() != null && !specimen.getComment().equals(""))
		{
			dataMap.put(PrintWebServiceConstants.SPECIMEN_COMMENT, CommonUtilities
					.toString(specimen.getComment()));
		}
		if (specimen.getCreatedOn() != null)
		{
			dataMap.put(PrintWebServiceConstants.SPECIMEN_CREATED_ON, CommonUtilities
					.toString(specimen.getCreatedOn()));
		}
		if (specimen.getLineage() != null)
		{
			dataMap.put(PrintWebServiceConstants.SPECIMEN_LINEAGE, CommonUtilities
					.toString(specimen.getLineage()));
		}
		if (specimen.getMessageLabel() != null)
		{
			dataMap.put(PrintWebServiceConstants.SPECIMEN_MESSAGE_LABEL, CommonUtilities
					.toString(specimen.getMessageLabel()));
		}
		if (specimen.getSpecimenPosition() != null
				&& specimen.getSpecimenPosition().getStorageContainer() != null)
		{
			dataMap.put(PrintWebServiceConstants.SPECIMEN_STORAGE_CONTAINER_NAME, CommonUtilities
					.toString(specimen.getSpecimenPosition().getStorageContainer().getName()));
			dataMap.put(PrintWebServiceConstants.SPECIMEN_POSITION_DIMENSION_ONE, CommonUtilities
					.toString(specimen.getSpecimenPosition().getPositionDimensionOne()));
			dataMap.put(PrintWebServiceConstants.SPECIMEN_POSITION_DIMENSION_TWO, CommonUtilities
					.toString(specimen.getSpecimenPosition().getPositionDimensionTwo()));
		}

		if (specimen instanceof MolecularSpecimen)
		{
			final String concentration = CommonUtilities.toString(String
					.valueOf(((MolecularSpecimen) specimen)
							.getConcentrationInMicrogramPerMicroliter()));
			dataMap.put(PrintWebServiceConstants.CONCENTRATION, concentration);
		}
		if (specimen.getAvailableQuantity() != null)
		{
			dataMap.put(PrintWebServiceConstants.QUANTITY, CommonUtilities.toString(String
					.valueOf(specimen.getAvailableQuantity())));
		}
		if (specimen.getPathologicalStatus() != null)
		{
			dataMap.put(PrintWebServiceConstants.PATHOLOGICAL_STATUS, CommonUtilities
					.toString(specimen.getPathologicalStatus()));
		}

		final String cpTitle = CommonUtilities.toString(specimen.getSpecimenCollectionGroup()
				.getCollectionProtocolRegistration().
				getCollectionProtocol().getShortTitle());
		final String ppi = specimen.getSpecimenCollectionGroup()
				.getCollectionProtocolRegistration().getProtocolParticipantIdentifier();
		dataMap.put(PrintWebServiceConstants.CP_TITLE, cpTitle);
		if (ppi != null && !ppi.equals(""))
		{
			dataMap.put(PrintWebServiceConstants.PARTICIPANT_PROTOCOL_IDENTIFIER, CommonUtilities
					.toString(ppi));
		}

		listMap.add(dataMap);

	}
}
