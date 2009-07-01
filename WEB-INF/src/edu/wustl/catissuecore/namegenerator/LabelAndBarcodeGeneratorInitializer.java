
package edu.wustl.catissuecore.namegenerator;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

/**
 * This class initialize  label and barcode generator depending upon configuration.
 * @author Falguni_Sachde
 *
 */
public class LabelAndBarcodeGeneratorInitializer
{

	/**
	 * Logger object.
	 */
	private static final Logger logger = Logger
			.getCommonLogger(LabelAndBarcodeGeneratorInitializer.class);

	/**
	 * This method reads configuration file and set the conditions
	 * whether automatic label ,barcode generation configured or not.
	 *
	 */
	public static void init()
	{
		try
		{
			setSpcimenLabelBarcodeGentorInstances();
			setStorageContainerGeneratorInstance();
		}
		catch (Exception e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	private static void setStorageContainerGeneratorInstance() throws NameGeneratorException
	{
		LabelGenerator storageContainerGeneratorInstance;

		BarcodeGenerator storageContainerBarcodeGeneratorInstance;
		storageContainerGeneratorInstance = LabelGeneratorFactory
				.getInstance(Constants.STORAGECONTAINER_LABEL_GENERATOR_PROPERTY_NAME);
		if (storageContainerGeneratorInstance != null)
		{
			Variables.isStorageContainerLabelGeneratorAvl = true;
		}

		storageContainerBarcodeGeneratorInstance = BarcodeGeneratorFactory
				.getInstance(Constants.STORAGECONTAINER_BARCODE_GENERATOR_PROPERTY_NAME);
		if (storageContainerBarcodeGeneratorInstance != null)
		{
			Variables.isStorageContainerBarcodeGeneratorAvl = true;
		}
	}

	private static void setSpcimenLabelBarcodeGentorInstances() throws NameGeneratorException
	{
		LabelGenerator specimenGeneratorInstance;
		LabelGenerator specimenCollectionGroupLableGeneratorInstance;
		LabelGenerator protocolParticipantIdentifierLabelGeneratorInstance;
		BarcodeGenerator specimenBarcodeGeneratorInstance;
		BarcodeGenerator specimenCollectionGroupBarcodeGeneratorInstance;
		BarcodeGenerator collectionProtocolRegistrationBarcodeGeneratorInstance;
		specimenGeneratorInstance = LabelGeneratorFactory
				.getInstance(Constants.SPECIMEN_LABEL_GENERATOR_PROPERTY_NAME);
		if (specimenGeneratorInstance != null)
		{
			Variables.isSpecimenLabelGeneratorAvl = true;
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

}
