
package edu.wustl.catissuecore.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.entitymanager.CategoryManager;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.templategenerator.BOCategoryTemplateGenerator;
import edu.common.dynamicextensions.util.templategenerator.BOEntityTemplateGenerator;
import edu.common.dynamicextensions.util.templategenerator.BOTemplateGeneratorUtility;
import edu.common.dynamicextensions.util.templategenerator.TemplateGenerator;
import edu.wustl.bulkoperator.metadata.BulkOperationMetaData;
import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.bizlogic.AnnotationBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;

public class BOTemplateGeneratorClient
{

	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}

	/**
	 * logger for information.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(BOTemplateGeneratorClient.class);

	/**
	 * This method will call through ant target to generate XML and CSV template for Bulk operation.
	 * @param args arguments required to generate template passed through ant target.
	 */
	public static void main(String[] args) // NOPMD by Kunal_Kamble on 12/30/10 7:42 PM
	{
		ApplicationProperties.initBundle("ApplicationResources");
		/*args=new String[]{"qasq","biomarker:SpecimenAnnotation","G:/Dynamicxtensions/bo_cp/software/bulkoperator/src/conf/mapping.xml"};
		System.setProperty("static.template.dir", "G:/Dynamicxtensions/caTissue_v1.2_RC6_TAG/BulkOperations/conf");*/
		try
		{
			validateArguments(args);
			if ("category".equals(args[0]))
			{
				exportTemplateForCategory(args);

			}
			else
			{
				exportTemplateForEntity(args);
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
			LOGGER.error(ApplicationProperties.getValue("bo.error.reading.category.file", exception
					.getLocalizedMessage()));
			LOGGER.error(exception);
		}
		catch (ApplicationException exception)
		{
			LOGGER.error(exception.getLocalizedMessage());
			LOGGER.error(exception);
		}
	}

	private static void exportTemplateForCategory(String[] args) throws FileNotFoundException,
			IOException, DynamicExtensionsSystemException, ApplicationException
	{
		if (new File(args[1]).exists())
		{
			CSVReader reader = new CSVReader(new FileReader(args[1]));
			final String[] line = reader.readNext();

			for (String categoryName : line)
			{
				createCategoryTemplate(categoryName, args[2]);
			}
		}
		else
		{
			createCategoryTemplate(args[1], args[2]);
		}
	}

	private static void exportTemplateForEntity(String[] args) throws FileNotFoundException,
			IOException, DynamicExtensionsSystemException, ApplicationException
	{
		if (new File(args[1]).exists())
		{
			CSVReader reader = new CSVReader(new FileReader(args[1]));
			final String[] line = reader.readNext();

			for (String EntityName : line)
			{
				createEntityTemplate(EntityName, args[2]);
			}
		}
		else
		{
			createEntityTemplate(args[1], args[2]);
		}
	}

	/**
	 * This method creates XML and CSV template for specified category.
	 * @param args arguments containing mapping XML file and template XML file.
	 * @param categoryName category name to generate category template.
	 * @throws DynamicExtensionsSystemException throw DESystemException.
	 * @throws ApplicationException
	 */
	private static void createCategoryTemplate(String categoryName, String mappingxml)
			throws DynamicExtensionsSystemException, ApplicationException
	{
		CategoryInterface categoryInterface = CategoryManager.getInstance().getCategoryByName(
				categoryName);
		if (categoryInterface == null)
		{
			throw new BulkOperationException(ApplicationProperties.getValue(
					"error.missing.category", categoryName));
		}
		else
		{

			generateXMLAndCSVTemplate(System.getProperty("user.dir"), mappingxml, categoryInterface);
		}
	}

	/**
	 * This method creates XML and CSV template for specified category.
	 * @param args arguments containing mapping XML file and template XML file.
	 * @param entityName category name to generate category template.
	 * @throws DynamicExtensionsSystemException throw DESystemException.
	 * @throws ApplicationException
	 */
	private static void createEntityTemplate(String entityName, String mappingxml)
			throws DynamicExtensionsSystemException, ApplicationException
	{

		String[] split = entityName.split(":");
		if(split.length<2)
		{
			throw new BulkOperationException(ApplicationProperties.getValue(
					"bo.error.entityname.format"));
		}
		EntityGroupInterface entityGroup = EntityManager.getInstance().getEntityGroupByName(
				split[0]);
		if (entityGroup == null)
		{
			throw new BulkOperationException(ApplicationProperties.getValue(
					"error.missing.entitygroup", split[0]));
		}
		EntityInterface entity = entityGroup.getEntityByName(split[1]);
		if (entity == null)
		{
			throw new BulkOperationException(ApplicationProperties.getValue("error.missing.entity",
					split[1]));
		}
		else
		{

			generateXMLAndCSVTemplate(System.getProperty("user.dir"), mappingxml, entity);
		}
	}

	/**
	 * This method validates the arguments provided.
	 * @param args arguments to be validated.
	 * @throws BulkOperationException throw BulkOperation Exception.
	 */
	private static void validateArguments(String[] args) throws BulkOperationException
	{
		final Integer minArgLength = 3;
		if (args.length < minArgLength)
		{
			throw new BulkOperationException(ApplicationProperties.getValue(
					"errors.invalid.arguement.numbers", String.valueOf(minArgLength)));
		}
		if("".equals(args[1].trim()))
		{
			throw new BulkOperationException(ApplicationProperties.getValue(
					"bo.error.no.formname"));
		}
	}

	/**
	 * Generate the XML and CSV template for category required for bulk operation.
	 * @param baseDir Base Directory to store generated template files.
	 * @param xmlFilePath XML Template file path.
	 * @param mappingXML XML file required for generation of XML template.
	 * @throws DynamicExtensionsSystemException throws DESystemException.
	 * @throws ApplicationException
	 */
	public static void generateXMLAndCSVTemplate(String baseDir, String mappingXML, Object object)
			throws DynamicExtensionsSystemException, ApplicationException
	{
		//Step1: Iterate the given category and generate XML template data.
		Map<String, String> fileNameVsTemplateFile = getFileNameVsTemplateFileMap(object);
		for (Map.Entry<String, String> entryObject : fileNameVsTemplateFile.entrySet())
		{
			TemplateGenerator templateGenarator;
			if (object instanceof CategoryInterface)
			{
				templateGenarator = new BOCategoryTemplateGenerator((CategoryInterface) object,
						entryObject.getKey());
			}
			else
			{
				templateGenarator = new BOEntityTemplateGenerator((EntityInterface) object,
						entryObject.getKey());
			}
			//Step2: Append generated XML template data to existing XML template.
			final BulkOperationMetaData bulkMetaData = templateGenarator.mergeStaticTemplate(
					entryObject.getValue(), mappingXML);
			//Step3: Write this template data in a file and store it in temporary directory.
			File newDir = new File(baseDir + File.separator + Constants.TEMPLATE_DIR);
			if (!newDir.exists())
			{
				newDir.mkdir();
			}
			File templateFile = new File(newDir + File.separator + entryObject.getKey()
					+ Constants.XML_SUFFIX);
			BOTemplateGeneratorUtility.saveXMLTemplateCopy(templateFile, mappingXML, bulkMetaData);
			//Step4: Create CSV template for existing category.
			final File csvFile = new File(newDir + File.separator + entryObject.getKey()
					+ Constants.DOT_CSV);
			final String csvTemplateString = templateGenarator.createCSVTemplate(bulkMetaData,
					csvFile);
			//Step5: Save CSV template for existing category.
			BOTemplateGeneratorUtility.saveCSVTemplateCopy(csvFile, csvTemplateString);
		}

	}

	private static Map<String, String> getFileNameVsTemplateFileMap(Object object)
			throws DynamicExtensionsSystemException, ApplicationException
	{
		AnnotationBizLogic bizLogic = new AnnotationBizLogic();
		String templateName;
		NameValueBean hookEntityBean;
		if (object instanceof CategoryInterface)
		{
			CategoryInterface category = (CategoryInterface) object;
			ContainerInterface rootContainer = (ContainerInterface) category
					.getRootCategoryElement().getContainerCollection().iterator().next();
			hookEntityBean = bizLogic.getHookEntityNameValueBeanForCategory(rootContainer.getId(),
					category.getName());
			templateName = category.getName();
		}
		else
		{
			EntityInterface entity = (EntityInterface) object;
			hookEntityBean = bizLogic.getHookEntiyNameValueBean(entity.getId(), entity.getName());
			templateName = entity.getName();
		}

		Map<String, String> fileNameVsTemplateFile = new HashMap<String, String>();
		String staticTemplateDir = System.getProperty("static.template.dir");
		if (hookEntityBean.getName().equals(AnnotationConstants.ENTITY_NAME_PARTICIPANT_REC_ENTRY))
		{
			//export all the templates for participant
			fileNameVsTemplateFile.put(templateName + "_participantId", staticTemplateDir
					+ File.separator + "participant_id_template.xml");
			fileNameVsTemplateFile.put(templateName + "_ppi", staticTemplateDir + File.separator
					+ "participant_ppi_template.xml");
		}
		else if (hookEntityBean.getName().equals(AnnotationConstants.ENTITY_NAME_SCG_REC_ENTRY))
		{
			//export all the templates for scg
			fileNameVsTemplateFile.put(templateName + "_scgId", staticTemplateDir + File.separator
					+ "scg_id_template.xml");
			fileNameVsTemplateFile.put(templateName + "_scgName", staticTemplateDir
					+ File.separator + "scg_name_template.xml");
			fileNameVsTemplateFile.put(templateName + "_scgBarcode", staticTemplateDir
					+ File.separator + "scg_barcode_template.xml");
		}
		else
		{
			//export all the templates for specimen.
			fileNameVsTemplateFile.put(templateName + "_specimenId", staticTemplateDir
					+ File.separator + "specimen_id_template.xml");
			fileNameVsTemplateFile.put(templateName + "_specimenLabel", staticTemplateDir
					+ File.separator + "specimen_label_template.xml");
			fileNameVsTemplateFile.put(templateName + "_specimenBarcode", staticTemplateDir
					+ File.separator + "specimen_barcode_template.xml");
		}
		return fileNameVsTemplateFile;
	}

}
