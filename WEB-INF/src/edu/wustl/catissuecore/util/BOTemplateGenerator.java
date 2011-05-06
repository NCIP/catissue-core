package edu.wustl.catissuecore.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.entitymanager.CategoryManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.BOCategoryTemplateGenerator;
import edu.common.dynamicextensions.util.BOTemplateGeneratorUtility;
import edu.wustl.bulkoperator.metadata.BulkOperationClass;
import edu.wustl.bulkoperator.metadata.BulkOperationMetaData;
import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.bizlogic.AnnotationBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;

public class BOTemplateGenerator extends BOCategoryTemplateGenerator
{

	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}

	/**
	 * logger for information.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(BOTemplateGenerator.class);

	public BOTemplateGenerator(CategoryInterface category)
	{
		super(category);
	}


	/**
	 * This method will call through ant target to generate XML and CSV template for Bulk operation.
	 * @param args arguments required to generate template passed through ant target.
	 */
	public static void main(String[] args) // NOPMD by Kunal_Kamble on 12/30/10 7:42 PM
	{
		try
		{
			validateArguments(args);
			if (new File(args[0]).exists())
			{
				CSVReader reader = new CSVReader(new FileReader(args[0]));
				final String[] line = reader.readNext();

				for (String categoryName : line)
				{
					createCategoryTemplate(categoryName,args[1]);
				}
			}
			else
			{
				createCategoryTemplate(args[0],args[1]);
			}
			LOGGER.info("XML and CSV template files are saved in: ./" + Constants.TEMPLATE_DIR);
		}
		catch (BulkOperationException exception)
		{
			LOGGER.error(exception.getMsgValues());
			LOGGER.error(exception);
		}
		catch (DynamicExtensionsSystemException exception)
		{
			LOGGER.error(exception.getLocalizedMessage());
			LOGGER.error(exception);
		}
		catch (IOException exception)
		{
			LOGGER.error(ApplicationProperties.getValue("bo.error.reading.category.file",exception.getLocalizedMessage()));
			LOGGER.error(exception);
		}
	}

	/**
	 * This method creates XML and CSV template for specified category.
	 * @param args arguments containing mapping XML file and template XML file.
	 * @param categoryName category name to generate category template.
	 * @throws DynamicExtensionsSystemException throw DESystemException.
	 * @throws BulkOperationException throw BulkOperationException.
	 */
	private static void createCategoryTemplate(String categoryName,String mappingxml)
			throws DynamicExtensionsSystemException, BulkOperationException
	{
		CategoryInterface categoryInterface = CategoryManager.getInstance().getCategoryByName(
				categoryName);
		if (categoryInterface == null)
		{
			throw new BulkOperationException(ApplicationProperties.getValue("error.missing.category", categoryName));
		}
		else
		{
			BOTemplateGenerator boGen = new BOTemplateGenerator(categoryInterface);
			boGen.generateXMLAndCSVTemplate(System.getProperty("user.dir"), mappingxml);
		}
	}

	/**
	 * This method validates the arguments provided.
	 * @param args arguments to be validated.
	 * @throws BulkOperationException throw BulkOperation Exception.
	 */
	private static void validateArguments(String[] args) throws BulkOperationException
	{
		final Integer minArgLength = 1;
		if (args.length < minArgLength)
		{
			throw new BulkOperationException(ApplicationProperties.getValue("errors.invalid.arguement.numbers", String.valueOf(minArgLength)));
		}
	}

	/**
	 * Generate the XML and CSV template for category required for bulk operation.
	 * @param baseDir Base Directory to store generated template files.
	 * @param xmlFilePath XML Template file path.
	 * @param mappingXML XML file required for generation of XML template.
	 * @throws DynamicExtensionsSystemException throws DESystemException.
	 * @throws BulkOperationException throws Bulk Operation Exception.
	 */
	public void generateXMLAndCSVTemplate(String baseDir, String mappingXML)
			throws DynamicExtensionsSystemException, BulkOperationException
	{
		//Step1: Iterate the given category and generate XML template data.
		iterateCategory(bulkOperationClass);
		AnnotationBizLogic bizLogic = new AnnotationBizLogic();
		ContainerInterface rootContainer = (ContainerInterface)category.getRootCategoryElement().getContainerCollection().iterator().next();
		NameValueBean hookEntityBean = bizLogic.getHookEntityNameValueBean(rootContainer.getId());
		Map<String,String> fileNameVsTemplateFile = new HashMap<String, String>();
		String staticTemplateDir = System.getProperty("static.template.dir");
		if(hookEntityBean.getName().equals(AnnotationConstants.ENTITY_NAME_PARTICIPANT_REC_ENTRY))
		{
			//export all the templates for participant
			fileNameVsTemplateFile.put(category.getName()+"_participantId", staticTemplateDir+File.separator+"participant_id_template.xml");
			fileNameVsTemplateFile.put(category.getName()+"_ppi", staticTemplateDir+File.separator+"participant_ppi_template.xml");
		}
		else if (hookEntityBean.getName().equals(AnnotationConstants.ENTITY_NAME_SCG_REC_ENTRY))
		{
			//export all the templates for scg
			fileNameVsTemplateFile.put(category.getName()+"_scgId", staticTemplateDir+File.separator+"scg_id_template.xml");
			fileNameVsTemplateFile.put(category.getName()+"_scgName", staticTemplateDir+File.separator+"scg_name_template.xml");
			fileNameVsTemplateFile.put(category.getName()+"_scgBarcode", staticTemplateDir+File.separator+"scg_barcode_template.xml");
		}
		else
		{
			//export all the templates for specimen.
			fileNameVsTemplateFile.put(category.getName()+"_specimenId", staticTemplateDir+File.separator+"specimen_id_template.xml");
			fileNameVsTemplateFile.put(category.getName()+"_specimenLabel", staticTemplateDir+File.separator+"specimen_label_template.xml");
			fileNameVsTemplateFile.put(category.getName()+"_specimenBarcode", staticTemplateDir+File.separator+"specimen_barcode_template.xml");
		}
		for(Map.Entry<String, String> entryObject :fileNameVsTemplateFile.entrySet())
		{
			//Step2: Append generated XML template data to existing XML template.
			final BulkOperationMetaData bulkMetaData = BOTemplateGeneratorUtility
					.appnedCategoryTemplate(entryObject.getValue(), mappingXML, bulkOperationClass);
			//Step3: Write this template data in a file and store it in temporary directory.
			File newDir = saveXMLTemplateCopy(baseDir, mappingXML,
					bulkMetaData, bulkOperationClass,entryObject.getKey());
			//Step4: Create CSV template for existing category.
			final File csvFile = new File(newDir + File.separator
					+ entryObject.getKey() + Constants.DOT_CSV);
			final String csvTemplateString = BOTemplateGeneratorUtility.createCSVTemplate(bulkMetaData,
					csvFile);
			//Step5: Save CSV template for existing category.
			saveCSVTemplateCopy(csvFile, csvTemplateString);
		}
	}

	/**
	 * This method saves the XML file copy in Template directory.
	 * @param baseDir Base directory in which template directory to be created.
	 * @param mappingXML Mapping XML file path.
	 * @param bulkMetaData BulkOperationMetaData object.
	 * @param bulkOperationClass BulkOperationClass object.
	 * @param fileName
	 * @return File object.
	 * @throws DynamicExtensionsSystemException throws DynamicExtensionsSystemException.
	 */
	public File saveXMLTemplateCopy(String baseDir, String mappingXML,
			final BulkOperationMetaData bulkMetaData, BulkOperationClass bulkOperationClass, String fileName)
			throws DynamicExtensionsSystemException
	{
		File newDir = new File(baseDir + File.separator + Constants.TEMPLATE_DIR);
		if (!newDir.exists())
		{
			newDir.mkdir();
		}
		try
		{
			final String pathname = newDir + File.separator + fileName
					+ Constants.XML_SUFFIX;
			MarshalUtility.marshalObject(mappingXML, bulkMetaData, new FileWriter(
					new File(pathname)));
		}
		catch (IOException exception)
		{
			throw new DynamicExtensionsSystemException(
					"Error while creating XML template for Bulk operation.", exception);
		}
		return newDir;
	}

	/**
	 * @param file File to save.
	 * @param csvString CSV string to write in a file.
	 * @throws DynamicExtensionsSystemException throw DynamicExtensionsSystemException
	 */
	public void saveCSVTemplateCopy(File file, String csvString)
			throws DynamicExtensionsSystemException
	{
		try
		{
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
			bufferedWriter.write(csvString);
			bufferedWriter.close();
		}
		catch (IOException exception)
		{
			throw new DynamicExtensionsSystemException(
					"Error while creating CSV template for bulk operation.", exception);
		}
	}

}
