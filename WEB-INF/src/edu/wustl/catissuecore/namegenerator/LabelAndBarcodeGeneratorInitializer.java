
package edu.wustl.catissuecore.namegenerator;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;

/**
 * This class initialize  label and barcode generator depending upon configuration.
 * @author Falguni_Sachde
 *
 */
public class LabelAndBarcodeGeneratorInitializer
{

	/**
	 * This method reads configuration file and set the conditions
	 * whether automatic label ,barcode generation configured or not.
	 *
	 */
	public static void init()
	{
		LabelGenerator specimenGeneratorInstance;
		LabelGenerator storageContainerGeneratorInstance;
		LabelGenerator specimenCollectionGroupLableGeneratorInstance;
		LabelGenerator protocolParticipantIdentifierLabelGeneratorInstance;
		BarcodeGenerator specimenBarcodeGeneratorInstance;
		BarcodeGenerator storageContainerBarcodeGeneratorInstance;
		BarcodeGenerator specimenCollectionGroupBarcodeGeneratorInstance;
		BarcodeGenerator collectionProtocolRegistrationBarcodeGeneratorInstance;
		try
		{
			specimenGeneratorInstance = LabelGeneratorFactory
					.getInstance(Constants.SPECIMEN_LABEL_GENERATOR_PROPERTY_NAME);
			if (specimenGeneratorInstance != null)
			{
				Variables.isSpecimenLabelGeneratorAvl = true;
			}
			storageContainerGeneratorInstance = LabelGeneratorFactory
					.getInstance(Constants.STORAGECONTAINER_LABEL_GENERATOR_PROPERTY_NAME);
			if (storageContainerGeneratorInstance != null)
			{
				Variables.isStorageContainerLabelGeneratorAvl = true;
			}
			protocolParticipantIdentifierLabelGeneratorInstance = LabelGeneratorFactory
			.getInstance(Constants.PROTOCOL_PARTICIPANT_IDENTIFIER_LABEL_GENERATOR_PROPERTY_NAME);
			if (protocolParticipantIdentifierLabelGeneratorInstance != null)
			{
				Variables.isProtocolParticipantIdentifierLabelGeneratorAvl = true;
			}

			specimenBarcodeGeneratorInstance = BarcodeGeneratorFactory
					.getInstance(Constants.SPECIMEN_BARCODE_GENERATOR_PROPERTY_NAME);
			if (specimenBarcodeGeneratorInstance != null)
			{
				Variables.isSpecimenBarcodeGeneratorAvl = true;
			}

			storageContainerBarcodeGeneratorInstance = BarcodeGeneratorFactory
					.getInstance(Constants.STORAGECONTAINER_BARCODE_GENERATOR_PROPERTY_NAME);
			if (storageContainerBarcodeGeneratorInstance != null)
			{
				Variables.isStorageContainerBarcodeGeneratorAvl = true;
			}

			specimenCollectionGroupLableGeneratorInstance = LabelGeneratorFactory
					.getInstance(Constants.SPECIMEN_COLL_GROUP_LABEL_GENERATOR_PROPERTY_NAME);
			if (specimenCollectionGroupLableGeneratorInstance != null)
			{
				Variables.isSpecimenCollGroupLabelGeneratorAvl = true;
			}
			specimenCollectionGroupBarcodeGeneratorInstance = BarcodeGeneratorFactory
			.getInstance(Constants.SPECIMEN_COLL_GROUP_BARCODE_GENERATOR_PROPERTY_NAME);
			if (specimenCollectionGroupBarcodeGeneratorInstance != null)
			{
				Variables.isSpecimenCollGroupBarcodeGeneratorAvl = true;
			}
			collectionProtocolRegistrationBarcodeGeneratorInstance = BarcodeGeneratorFactory
					.getInstance(Constants.COLL_PROT_REG_BARCODE_GENERATOR_PROPERTY_NAME);
			if (collectionProtocolRegistrationBarcodeGeneratorInstance != null)
			{
				Variables.isCollectionProtocolRegistrationBarcodeGeneratorAvl = true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
