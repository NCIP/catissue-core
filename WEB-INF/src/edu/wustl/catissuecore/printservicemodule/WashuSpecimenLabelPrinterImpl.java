
package edu.wustl.catissuecore.printservicemodule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.IdComparator;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.Utility;

/**
 * This Class is used to define method for Specimen label printing
 * @author Renuka_Bajpai
 */
public class WashuSpecimenLabelPrinterImpl extends SpecimenLabelPrinterImpl
{

	/**
	 * @param abstractDomainObject Specimen Object
	 * @param listMap List of Specimen details including all child specimen.
	 */
	protected void createObjectMap(AbstractDomainObject abstractDomainObject, ArrayList listMap,
			String printerType, String printerLocation, String ipAddress)
	{

		if (abstractDomainObject instanceof Specimen)
		{

			Specimen objSpecimen = (Specimen) abstractDomainObject;
			ArrayList specimenList = new ArrayList();
			specimenList.add(objSpecimen);
			getAllSpecimenList(objSpecimen, specimenList);
			Collections.sort(specimenList, new IdComparator());
			for (int cnt = 0; cnt < specimenList.size(); cnt++)
			{
				Specimen obj = (Specimen) specimenList.get(cnt);
				addDataToPrint(obj,listMap,printerType,printerLocation,ipAddress);
			}
		}

	}
	void createObjectMap(List<AbstractDomainObject> abstractDomainObjectList,ArrayList listMap,String printerType,String printerLocation,String ipAddress)
	{
		//Bug 11509 
		Collections.sort(abstractDomainObjectList,new IdComparator());
		for(AbstractDomainObject abstractDomainObject : abstractDomainObjectList)
		{
			if(abstractDomainObject instanceof Specimen)
			{
				Specimen obj = (Specimen)abstractDomainObject;	
				addDataToPrint(obj,listMap,printerType,printerLocation,ipAddress);			
			}
		}
	}
	private void addDataToPrint(Specimen specimen,ArrayList listMap,String printerType,String printerLocation,String ipAddress)
	{
		LinkedHashMap dataMap = new LinkedHashMap();
		dataMap.put("class", specimen.getClassName());
		dataMap.put("id", specimen.getId().toString());
		//if (specimen.getCollectionStatus().equals(Constants.COLLECTION_STATUS_COLLECTED))
		{
			String label = specimen.getLabel();
			dataMap.put(PrintWebServiceConstants.USER_IPADDRESS, ipAddress);
			if (specimen.getClassName() != null)
				dataMap.put(PrintWebServiceConstants.SPECIMEN_CLASS, Utility.toString(specimen
						.getClassName()));
			if (specimen.getId() != null)
				dataMap.put(PrintWebServiceConstants.SPECIMEN_IDENTIFIER, Utility
						.toString(specimen.getId()));
			if (specimen.getSpecimenType() != null)
				dataMap.put(PrintWebServiceConstants.SPECIMEN_TYPE, Utility.toString(specimen
						.getSpecimenType()));
			if (label == null)
				label = specimen.getSpecimenType();
			dataMap.put(PrintWebServiceConstants.SPECIMEN_LABEL, Utility.toString(label));
			if (specimen.getBarcode() != null)
				dataMap.put(PrintWebServiceConstants.SPECIMEN_BARCODE, Utility.toString(specimen
						.getBarcode()));
			if (specimen.getSpecimenCharacteristics() != null)
				dataMap.put(PrintWebServiceConstants.SPECIMEN_TISSUE_SITE, Utility
						.toString(specimen.getSpecimenCharacteristics().getTissueSite()));
			if (specimen.getCollectionStatus() != null)
				dataMap.put(PrintWebServiceConstants.SPECIMEN_COLLECTION_STATUS, Utility
						.toString(specimen.getCollectionStatus()));
			if (specimen.getComment() != null && !specimen.getComment().equals(""))
				dataMap.put(PrintWebServiceConstants.SPECIMEN_COMMENT, Utility.toString(specimen
						.getComment()));
			if (specimen.getCreatedOn() != null)
				dataMap.put(PrintWebServiceConstants.SPECIMEN_CREATED_ON, Utility
						.toString(specimen.getCreatedOn()));
			if (specimen.getLineage() != null)
				dataMap.put(PrintWebServiceConstants.SPECIMEN_LINEAGE, Utility.toString(specimen
						.getLineage()));
			if (specimen.getMessageLabel() != null)
				dataMap.put(PrintWebServiceConstants.SPECIMEN_MESSAGE_LABEL, Utility
						.toString(specimen.getMessageLabel()));
			if (specimen.getSpecimenPosition() != null
					&& specimen.getSpecimenPosition().getStorageContainer() != null)
			{
				dataMap.put(PrintWebServiceConstants.SPECIMEN_STORAGE_CONTAINER_NAME,
						Utility.toString(specimen.getSpecimenPosition().getStorageContainer()
								.getName()));
				dataMap.put(PrintWebServiceConstants.SPECIMEN_POSITION_DIMENSION_ONE,
						Utility.toString(specimen.getSpecimenPosition()
								.getPositionDimensionOne()));
				dataMap.put(PrintWebServiceConstants.SPECIMEN_POSITION_DIMENSION_TWO,
						Utility.toString(specimen.getSpecimenPosition()
								.getPositionDimensionTwo()));
			}

			if (specimen instanceof MolecularSpecimen)
			{
				String concentration = Utility.toString(String
						.valueOf(((MolecularSpecimen) specimen)
								.getConcentrationInMicrogramPerMicroliter()));
				dataMap.put(PrintWebServiceConstants.CONCENTRATION, concentration);
			}
			if (specimen.getAvailableQuantity() != null)
				dataMap.put(PrintWebServiceConstants.QUANTITY, Utility.toString(String
						.valueOf(specimen.getAvailableQuantity())));
			if (specimen.getPathologicalStatus() != null)
				dataMap.put(PrintWebServiceConstants.PATHOLOGICAL_STATUS, Utility
						.toString(specimen.getPathologicalStatus()));

			String cpTitle = Utility.toString(specimen.getSpecimenCollectionGroup()
					.getCollectionProtocolRegistration().getCollectionProtocol()
					.getShortTitle());//getTitle());
			String ppi = specimen.getSpecimenCollectionGroup()
					.getCollectionProtocolRegistration().getProtocolParticipantIdentifier();
			dataMap.put(PrintWebServiceConstants.CP_TITLE, cpTitle);
			if (ppi != null && !ppi.equals(""))
				dataMap.put(PrintWebServiceConstants.PARTICIPANT_PROTOCOL_IDENTIFIER,
						Utility.toString(ppi));

			listMap.add(dataMap);
		}

		
	}
}
