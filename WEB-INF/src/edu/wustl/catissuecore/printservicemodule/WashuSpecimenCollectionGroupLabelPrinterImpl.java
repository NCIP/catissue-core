
package edu.wustl.catissuecore.printservicemodule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.Utility;

/**
 * This class is used to define method for Specimen label printing
 * @author Renuka_bajpai
 */
public class WashuSpecimenCollectionGroupLabelPrinterImpl
		extends
			SpecimenCollectionGroupLabelPrinterImpl
{

	/**
	 * @param abstractDomainObject Specimen Collection group
	 * @return List List of all Specimen including child Specimen
	 */
	List createObjectMap(AbstractDomainObject abstractDomainObject, String printerType,
			String printerLocation, String ipAddress)
	{
		List listMap = new ArrayList();
		if (abstractDomainObject instanceof SpecimenCollectionGroup)
		{

			final SpecimenCollectionGroup objSCG = (SpecimenCollectionGroup) abstractDomainObject;
			final Collection specimenCollection = objSCG.getSpecimenCollection();
			Iterator itr = specimenCollection.iterator();
			ArrayList specimenList = new ArrayList();
			while (itr.hasNext())
			{
				Specimen objSpecimen = (Specimen) itr.next();
				specimenList.add(objSpecimen);

			}

			for (int cnt = 0; cnt < specimenList.size(); cnt++)
			{

				Specimen obj = (Specimen) specimenList.get(cnt);
				LinkedHashMap<String, String> dataMap = new LinkedHashMap<String, String>();
				if (obj.getCollectionStatus().equals(Constants.COLLECTION_STATUS_COLLECTED))
				{
					String label = obj.getLabel();
					String barcode = obj.getBarcode();
					dataMap.put("class", obj.getClassName());
					dataMap.put("id", obj.getId().toString());
					dataMap.put(PrintWebServiceConstants.USER_IPADDRESS, ipAddress);
					if (obj.getClassName() != null)
						dataMap.put(PrintWebServiceConstants.SPECIMEN_CLASS, obj.getClassName());
					if (obj.getSpecimenType() != null)
						dataMap.put(PrintWebServiceConstants.SPECIMEN_TYPE, obj.getSpecimenType());
					if (obj.getId() != null)
						dataMap.put(PrintWebServiceConstants.SPECIMEN_IDENTIFIER, obj.getId()
								.toString());
					if (label == null)
						label = obj.getSpecimenType();
					dataMap.put(PrintWebServiceConstants.SPECIMEN_LABEL, label);
					if (obj.getBarcode() != null)
						dataMap.put(PrintWebServiceConstants.SPECIMEN_BARCODE, barcode);
					if (obj.getSpecimenCharacteristics() != null)
						dataMap.put(PrintWebServiceConstants.SPECIMEN_TISSUE_SITE, Utility
								.toString(obj.getSpecimenCharacteristics().getTissueSite()));
					if (obj.getCollectionStatus() != null)
						dataMap.put(PrintWebServiceConstants.SPECIMEN_COLLECTION_STATUS, Utility
								.toString(obj.getCollectionStatus()));
					if (obj.getComment() != null && !(obj.getComment().equals("")))
						dataMap.put(PrintWebServiceConstants.SPECIMEN_COMMENT, Utility.toString(obj
								.getComment()));
					if (obj.getCreatedOn() != null)
						dataMap.put(PrintWebServiceConstants.SPECIMEN_CREATED_ON, Utility
								.toString(obj.getCreatedOn()));
					if (obj.getLineage() != null)
						dataMap.put(PrintWebServiceConstants.SPECIMEN_LINEAGE, Utility.toString(obj
								.getLineage()));
					if (obj.getMessageLabel() != null)
						dataMap.put(PrintWebServiceConstants.SPECIMEN_MESSAGE_LABEL, Utility
								.toString(obj.getMessageLabel()));
					if (obj.getSpecimenPosition() != null
							&& obj.getSpecimenPosition().getStorageContainer() != null)
					{
						dataMap.put(PrintWebServiceConstants.SPECIMEN_STORAGE_CONTAINER_NAME,
								Utility.toString(obj.getSpecimenPosition().getStorageContainer()
										.getName()));
						dataMap.put(PrintWebServiceConstants.SPECIMEN_POSITION_DIMENSION_ONE,
								Utility.toString(obj.getSpecimenPosition()
										.getPositionDimensionOne()));
						dataMap.put(PrintWebServiceConstants.SPECIMEN_POSITION_DIMENSION_TWO,
								Utility.toString(obj.getSpecimenPosition()
										.getPositionDimensionTwo()));
					}

					if (obj instanceof MolecularSpecimen)
					{
						String concentration = Utility.toString(String
								.valueOf(((MolecularSpecimen) obj)
										.getConcentrationInMicrogramPerMicroliter()));
						dataMap.put(PrintWebServiceConstants.CONCENTRATION, concentration);
					}
					if (obj.getAvailableQuantity() != null)
						dataMap.put(PrintWebServiceConstants.QUANTITY, Utility.toString(String
								.valueOf(obj.getAvailableQuantity())));
					if (obj.getPathologicalStatus() != null)
						dataMap.put(PrintWebServiceConstants.PATHOLOGICAL_STATUS, Utility
								.toString(obj.getPathologicalStatus()));

					String cpTitle = Utility.toString(obj.getSpecimenCollectionGroup()
							.getCollectionProtocolRegistration().getCollectionProtocol()
							.getShortTitle());//getTitle());
					String ppi = obj.getSpecimenCollectionGroup()
							.getCollectionProtocolRegistration().getProtocolParticipantIdentifier();
					dataMap.put(PrintWebServiceConstants.CP_TITLE, cpTitle);
					if (ppi != null && !ppi.equals(""))
						dataMap.put(PrintWebServiceConstants.PARTICIPANT_PROTOCOL_IDENTIFIER,
								Utility.toString(ppi));
					listMap.add(dataMap);

				}
			}
		}
		return listMap;
	}
}
