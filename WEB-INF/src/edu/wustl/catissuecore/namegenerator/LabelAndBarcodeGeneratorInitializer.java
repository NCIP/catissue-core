
package edu.wustl.catissuecore.namegenerator;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

/**
 * This class initialize  label and bar code generator depending upon configuration.
 * @author Falguni_Sachde
 *
 */
public final class LabelAndBarcodeGeneratorInitializer
{

	/**
	 * Logger object.
	 */
	private static final Logger LOGGER = Logger
			.getCommonLogger(LabelAndBarcodeGeneratorInitializer.class);

	/**
	 * private constructor.
	 */
	private LabelAndBarcodeGeneratorInitializer()
	{

	}

	/**
	 * This method reads configuration file and set the conditions
	 * whether automatic label ,bar code generation configured or not.
	 *
	 */
	public static void init()
	{
		try
		{
			setSpcimenLabelBarcodeGentorInstances();
			setStorageContainerGeneratorInstance();
		}
		catch (final Exception e)
		{
			LabelAndBarcodeGeneratorInitializer.LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	/**
	 * This method set Storage Container GeneratorInstance.
	 * @throws NameGeneratorException Name Generator Exception.
	 */
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

	/**
	 * This method set Specimen Label BarcodeGentor Instances.
	 * @throws NameGeneratorException Name Generator Exception.
	 */
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
