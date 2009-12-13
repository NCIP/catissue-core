package edu.wustl.catissuecore.bizlogic.bulkOperations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import au.com.bytecode.opencsv.CSVReader;
import edu.wustl.bulkoperator.BulkOperationProcessor;
import edu.wustl.bulkoperator.metadata.Attribute;
import edu.wustl.bulkoperator.metadata.BulkOperationClass;
import edu.wustl.bulkoperator.metadata.BulkOperationMetaData;
import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;

/**
 * Import Bulk Operation from UI back end target.
 * @author sagar_baldwa
 *
 */
public class ImportBulkOperationTemplate
{
	/**
	 * logger Logger - Generic logger.
	 */
	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir") + "/BulkOperations/conf");
	}
	/**
	 * logger Logger - Generic logger.
	 */
	private static final Logger logger = Logger.getCommonLogger(
			ImportBulkOperationTemplate.class);
	/**
	 * errorList of ArrayList format containing error messages in String format.
	 */
	private static List<String> errorList = new ArrayList<String>();
	/**
	 * Import Bulk Operation Template.
	 * @param operationName String.
	 * @param dropdownName String.
	 * @param csvFilePath String.
	 * @param xmlFilePath String.
	 * @throws Exception Exception.
	 */
	public ImportBulkOperationTemplate(String operationName, String dropdownName,
			String csvFilePath, String xmlFilePath) throws Exception
	{
		String[] args = {operationName, dropdownName, csvFilePath, xmlFilePath};
		validate(args);
		initiate(args);
	}
	/**
	 * Initiate.
	 * @param args String[].
	 * @throws Exception Exception.
	 */
	private static void initiate(String[] args) throws Exception
	{
		if(errorList.isEmpty())
		{
			initiateProcess(args);
		}
		else
		{
			logger.info("------------------------ERROR:--------------------------------\n");
			for(String error : errorList)
			{
				logger.info(error);
				logger.info("\n");
			}
			logger.info("------------------------ERROR:--------------------------------");
		}
	}
	/**
	 * Main method.
	 * @param args Array of Strings.
	 */
	public static void main(String[] args)
	{
		try
		{
			validate(args);
			initiate(args);
		}
		catch (Exception exp)
		{
			logger.info("------------------------ERROR:--------------------------------\n");
			logger.debug(exp.getMessage(), exp);
			logger.info(exp.getMessage() + "\n");
			logger.info("------------------------ERROR:--------------------------------");
		}
	}

	/**
	 * Validate Operation Name.
	 * @param bulkOperationMetaData BulkOperationMetaData.
	 * @param operationName String.
	 * @throws BulkOperationException BulkOperationException.
	 */
	private static void validateOperationName(BulkOperationMetaData
			bulkOperationMetaData, String operationName) throws BulkOperationException
	{
		Collection<BulkOperationClass> classList = bulkOperationMetaData.getBulkOperationClass();
		if (classList != null)
		{
			Iterator<BulkOperationClass> iterator = classList.iterator();
			if(iterator.hasNext())
			{
				BulkOperationClass bulkOperationClass = iterator.next();
				if(bulkOperationClass.getTemplateName() == null)
				{
					throw new BulkOperationException("The keyword 'templateName' is either " +
						"missing or incorrectly written in the XML.");
				}
				if(!operationName.equals(bulkOperationClass.getTemplateName()))
				{
					throw new BulkOperationException("Operation Name does not match with " +
							"the template name specified in the XML.");
				}
			}
		}
	}
	/**
	 * Initiate Process.
	 * @param args Array of String.
	 * @throws Exception Exception.
	 * @throws Exception Exception.
	 */
	private static void initiateProcess(String[] args) throws Exception
	{
		String operationName = args[0];
		String dropdownName = args[1];
		String csvFile = args[2];
		String xmlFile = args[3];
		String csvFileData = getCSVTemplateFileData(csvFile);
		String xmlFileData = getXMLTemplateFileData(xmlFile);
		saveTemplateInDatabase(operationName, dropdownName, csvFileData, xmlFileData);
	}

	/**
	 * Add Template In Database.
	 * @param operationName String.
	 * @param dropdownName String.
	 * @param csvFileData String.
	 * @param xmlFileData String.
	 * @throws Exception Exception.
	 */
	private static void saveTemplateInDatabase(String operationName, String dropdownName,
			String csvFileData, String xmlFileData) throws Exception
	{
		Connection connection = null;
		try
		{
			connection = DBManagerImpl.getConnection();
			boolean flag = checkAddOrEditTemplateCase(connection, operationName, dropdownName);
			if(flag)
			{
				editTemplate(connection, operationName, dropdownName,
						csvFileData, xmlFileData);
			}
			else
			{
				addTemplate(connection, operationName, dropdownName,
						csvFileData, xmlFileData);
			}
		}
		finally
		{
			connection.close();
		}
	}

	/**
	 * Edit Template.
	 * @param connection Connection.
	 * @param operationName String.
	 * @param dropdownName String.
	 * @param csvFileData String.
	 * @param xmlFileData String.
	 * @throws BulkOperationException BulkOperationException.
	 * @throws SQLException SQLException.
	 */
	private static void editTemplate(Connection connection,
			String operationName, String dropdownName, String csvFileData,
			String xmlFileData) throws BulkOperationException, SQLException
	{
		PreparedStatement preparedStatement = null;
		String databaseType = null;
		try
		{
			databaseType = BulkOperationUtility.getDatabaseType();
			if(Constants.ORACLE_DATABASE.equalsIgnoreCase(databaseType))
			{
				String query = "update catissue_bulk_operation set OPERATION = ?, " +
				"CSV_TEMPLATE = ?, XML_TEMPALTE = ?,  DROPDOWN_NAME = ? " +
				"where OPERATION = ? or DROPDOWN_NAME= ? ";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, operationName);
				preparedStatement.setString(2, csvFileData);
				StringReader reader = new StringReader(xmlFileData);
				preparedStatement.setCharacterStream(3, reader, xmlFileData.length());
				preparedStatement.setString(4, dropdownName);
				preparedStatement.setString(5, operationName);
				preparedStatement.setString(6, dropdownName);
			}
			else if(Constants.MYSQL_DATABASE.equalsIgnoreCase(databaseType))
			{
				String query = "update catissue_bulk_operation set OPERATION = ?, " +
				"CSV_TEMPLATE = ?, XML_TEMPALTE = ?,  DROPDOWN_NAME = ? " +
				"where OPERATION = ? or DROPDOWN_NAME= ? ";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, operationName);
				preparedStatement.setString(2, csvFileData);
				preparedStatement.setString(3, xmlFileData);
				preparedStatement.setString(4, dropdownName);
				preparedStatement.setString(5, operationName);
				preparedStatement.setString(6, dropdownName);
			}
			int rowCount = preparedStatement.executeUpdate();
			if(rowCount > 0)
			{
				logger.info("Data updated successfully. " + rowCount + " row edited");
			}
			else
			{
				logger.info("No rows edited");
			}
		}
		catch (SQLException sqlExp)
		{
			logger.debug("Error in updating the record in database.", sqlExp);
			throw new BulkOperationException("Error in updating the record in database.", sqlExp);
		}
		finally
		{
			preparedStatement.close();
		}
	}

	/**
	 * Add Template.
	 * @param connection Connection.
	 * @param operationName String.
	 * @param dropdownName String.
	 * @param csvFileData String.
	 * @param xmlFileData String.
	 * @throws BulkOperationException BulkOperationException.
	 * @throws SQLException SQLException.
	 */
	private static void addTemplate(Connection connection, String operationName,
			String dropdownName, String csvFileData,
			String xmlFileData) throws BulkOperationException, SQLException
	{
		PreparedStatement preparedStatement = null;
		try
		{
			if(Constants.ORACLE_DATABASE.equalsIgnoreCase(
					BulkOperationUtility.getDatabaseType()))
			{
				String sequenceQuery = "select CATISSUE_BULK_OPERATION_SEQ.NEXTVAL from dual";
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sequenceQuery);
				int sequenceNumber = 0;
				if(resultSet.next())
				{
					sequenceNumber = resultSet.getInt(1);
					if(sequenceNumber > 0)
					{
						String query = "insert into catissue_bulk_operation " +
						"(IDENTIFIER, OPERATION, CSV_TEMPLATE, XML_TEMPALTE, " +
						"DROPDOWN_NAME ) values (?, ?, ?, ?, ?)";

						preparedStatement = connection.prepareStatement(query);
						preparedStatement.setInt(1, sequenceNumber);
						preparedStatement.setString(2, operationName);
						preparedStatement.setString(3, csvFileData);
						StringReader reader = new StringReader(xmlFileData);
						preparedStatement.setCharacterStream(4, reader,
								xmlFileData.length());
						preparedStatement.setString(5, dropdownName);
					}
				}
				resultSet.close();
				statement.close();
			}
			else if(Constants.MYSQL_DATABASE.equalsIgnoreCase(
					BulkOperationUtility.getDatabaseType()))
			{
				String query = "insert into catissue_bulk_operation (OPERATION, " +
						"CSV_TEMPLATE, XML_TEMPALTE, DROPDOWN_NAME ) values (?, ?, ?, ?)";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, operationName);
				preparedStatement.setString(2, csvFileData);
				preparedStatement.setString(3, xmlFileData);
				preparedStatement.setString(4, dropdownName);
			}
			int rowCount = preparedStatement.executeUpdate();
			logger.info("Data inserted successfully. " + rowCount + " row inserted.");
		}
		catch (SQLException sqlExp)
		{
			logger.debug("Error in inserting the record in database.", sqlExp);
			throw new BulkOperationException("Error in inserting the record in database.", sqlExp);
		}
		finally
		{
			preparedStatement.close();
		}
	}

	/**
	 * Check If Edit Case.
	 * @param connection Connection.
	 * @param operationName String.
	 * @param dropdownName String.
	 * @return boolean Boolean.
	 * @throws Exception Exception.
	 */
	private static boolean checkAddOrEditTemplateCase(Connection connection,
			String operationName, String dropdownName) throws Exception
	{
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean flag = false;
		try
		{
			String query = "select operation, dropdown_name from catissue_bulk_operation " +
					"where operation like ? or dropdown_name like ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, operationName);
			preparedStatement.setString(2, dropdownName);
			resultSet = preparedStatement.executeQuery();
			if(resultSet != null)
			{
				while(resultSet.next())
				{
					String operationNameFromDB = resultSet.getString("OPERATION");
					String dropdownNameFromDB = resultSet.getString("DROPDOWN_NAME");
					if(operationNameFromDB.equals(operationName) &
							dropdownNameFromDB.equals(dropdownName))
					{
						flag = true;
						break;
					}
					else if(operationNameFromDB.equals(operationName) &
							!dropdownNameFromDB.equals(dropdownName))
					{
						logger.debug("Cannot insert the template as " +
							"same Operation Name already exists in the database.");
						throw new BulkOperationException("Cannot insert the template as " +
							"same Operation Name already exists in the database.");
					}
					else if(!operationNameFromDB.equals(operationName) &
							dropdownNameFromDB.equals(dropdownName))
					{
						logger.debug("Cannot insert template as " +
							"same DropDown Name already exists in the database.");
						throw new BulkOperationException("Cannot insert template as " +
							"same DropDown Name already exists in the database.");
					}
				}
			}
		}
		finally
		{
			resultSet.close();
			preparedStatement.close();
		}
		return flag;
	}

	/**
	 * Get XML Template File Data.
	 * @param xmlFile String.
	 * @return String.
	 * @throws Exception Exception.
	 */
	private static String getXMLTemplateFileData(String xmlFile) throws Exception
	{
		StringWriter xmlFormatData = new StringWriter();
		try
		{
			DocumentBuilderFactory documentBuilderFactory =
				DocumentBuilderFactory.newInstance();
            InputStream inputStream = new FileInputStream(new File(xmlFile));
            org.w3c.dom.Document doc = documentBuilderFactory.
            			newDocumentBuilder().parse(inputStream);
            Transformer serializer = TransformerFactory.newInstance().
            			newTransformer();
            serializer.transform(new DOMSource(doc),
            			new StreamResult(xmlFormatData));
		}
		catch (FileNotFoundException fnfExp)
		{
			logger.debug("XML File Not Found at the specified path.", fnfExp);
			throw new BulkOperationException("\nXML File Not Found at the specified path.", fnfExp);
		}
		catch (IOException ioExp)
		{
			logger.debug("Error in reading the XML File.", ioExp);
			throw new BulkOperationException("\nError in reading the XML File.", ioExp);
		}
		catch (Exception exp)
		{
			logger.debug("Error in encoding XML file to data stream.", exp);
			throw exp;
		}
		return xmlFormatData.toString();
	}

	/**
	 * Get CSV Template File Data.
	 * @param csvFile String.
	 * @return String.
	 * @throws Exception Exception.
	 */
	private static String getCSVTemplateFileData(String csvFile) throws Exception
	{
		CSVReader reader = null;
 		List<String[]> list = null;
 		StringBuffer commaSeparatedString = new StringBuffer();
		try
		{
			reader = new CSVReader(new FileReader(csvFile));
			list = reader.readAll();
			reader.close();
			Iterator<String[]> iterator = list.iterator();
			if(iterator.hasNext())
			{
				String string[] = iterator.next();
				int rowDataLength = string.length;
				for(int i = 0 ; i < rowDataLength; i++)
				{
					commaSeparatedString.append(string[i]);
					if(i < rowDataLength - 1)
					{
						commaSeparatedString.append(",");
					}
				}
			}
		}
		catch (FileNotFoundException fnfExpp)
		{
			logger.debug("CSV File Not Found at the specified path.", fnfExpp);
			throw new BulkOperationException("\nCSV File Not Found at the specified path.", fnfExpp);
		}
		catch (IOException ioExpp)
		{
			logger.debug("Error in reading the CSV File.", ioExpp);
			throw new BulkOperationException("\nError in reading the CSV File.", ioExpp);
		}
		finally
		{
			reader.close();
		}
		return commaSeparatedString.toString();
	}

	/**
	 * Validate the command line arguments.
	 * @param args Array of Strings
	 * @throws Exception Exception
	 */
	private static void validate(String[] args) throws Exception
	{
		/*String operationName = "createCellSpecimenReviewParameters";//args[0];
		String dropdownName = "createCellSpecimenReviewParameters";//args[1];
		String csvFile = "E:/createCellSpecimenReviewParameters.csv";//args[2];
		String xmlFile = "E:/createCellSpecimenReviewParameters.xml";//args[3];
		List<String[]> csvData = getCSVTemplateColumnNames(csvFile);
		String xmlFileData = getXMLTemplateFileData(xmlFile);*/
		validateArgs(args);
		BulkOperationMetaData bulkOperationMetaData =
				BulkOperationUtility.getBulkOperationMetaDataObject(args[3]);
		List<String[]> csvData = BulkOperationUtility.getCSVTemplateColumnNames(args[2]);
		BulkOperationProcessor bulkOperationProcessor = new BulkOperationProcessor();
		Hashtable<String, String> csvColumnNames =
			bulkOperationProcessor.createHashTable(csvData, 1);
		if(bulkOperationMetaData == null)
		{
			logger.debug("Error in parsing the XML template file.");
			throw new BulkOperationException("Error in parsing the XML template file.");
		}
		else
		{
			validateOperationName(bulkOperationMetaData, args[0]);
			validateXmlAndCsv(bulkOperationMetaData, args[0], bulkOperationProcessor, csvColumnNames);
		}
		//saveTemplateInDatabase(operationName, dropdownName, csvFileData, xmlFileData);
	}
	/**
	 * Validate Args.
	 * @param args String[].
	 * @throws Exception Exception.
	 */
	private static void validateArgs(String[] args) throws Exception
	{
		if (args.length == 0)
		{
			logger.debug("Please Specify the operation name.");
			throw new Exception("Please Specify the operation name.");
		}
		if (args.length < 2)
		{
			logger.debug("Please specify the dropdown name.");
			throw new Exception("Please specify the dropdown name.");
		}
		if (args.length < 3)
		{
			logger.debug("Please specify the CSV template file name.");
			throw new Exception("Please specify the CSV template file name.");
		}
		if (args.length < 4)
		{
			logger.debug("Please specify the XML template file name.");
			throw new Exception("Please specify the XML template file name.");
		}
	}
	/**
	 * Validate Xml And Csv.
	 * @param bulkOperationMetaData BulkOperationMetaData.
	 * @param operationName String.
	 * @param bulkOperationProcessor BulkOperationProcessor.
	 * @param csvColumnNames Map of String, String.
	 * @throws Exception Exception.
	 */
	private static void validateXmlAndCsv(BulkOperationMetaData
		bulkOperationMetaData, String operationName,
		BulkOperationProcessor bulkOperationProcessor,
		Hashtable<String, String> csvColumnNames)
		throws Exception
	{
		Collection<BulkOperationClass> classList = bulkOperationMetaData.getBulkOperationClass();
		if (classList != null)
		{
			Iterator<BulkOperationClass> iterator = classList.iterator();
			while (iterator.hasNext())
			{
				BulkOperationClass bulkOperationClass = iterator.next();
				validateBulkOperationMainClass(bulkOperationClass, operationName, csvColumnNames);
				if(errorList.isEmpty())
				{
					validateProcessXML(bulkOperationProcessor,
							bulkOperationClass, csvColumnNames);
				}
			}
		}
	}
	/**
	 * Validate Process XML.
	 * @param bulkOperationProcessor BulkOperationProcessor.
	 * @param bulkOperationClass BulkOperationClass.
	 * @param csvColumnNames Map of String, String.
	 * @throws Exception Exception.
	 */
	private static void validateProcessXML(BulkOperationProcessor
		bulkOperationProcessor, BulkOperationClass bulkOperationClass,
		Hashtable<String, String> csvColumnNames)
		throws Exception
	{
		Object domainObject = bulkOperationClass.getClassObject().
								getConstructor().newInstance(null);
		bulkOperationProcessor.processObject(domainObject,
						bulkOperationClass, null, csvColumnNames);
	}
	/**
	 * Validate Bulk Operation Main Class..
	 * @param bulkOperationClass BulkOperationClass
	 * @param operationName String
	 * @param csvColumnNames Map of String, String.
	 * @throws Exception Exception.
	 */
	private static void validateBulkOperationMainClass(BulkOperationClass
		bulkOperationClass, String operationName, Map<String, String> csvColumnNames)
		throws Exception
	{
		try
		{
			Object classObject = bulkOperationClass.getClassObject();
		}
		catch (NullPointerException exp)
		{
			logger.debug("The keyword 'className' is either missing or incorrectly " +
				"written in the XML for the main class tag.", exp);
			throw new BulkOperationException("The keyword 'className' is either missing " +
				"or incorrectly written in the XML for the main class tag.", exp);
		}
		catch (Exception exp)
		{
			logger.debug("The 'className' value mentioned is incorrect for the main XML tag.", exp);
			throw new BulkOperationException("The 'className' value mentioned is incorrect" +
					" for the main XML tag.", exp);
		}
		try
		{
			validateXMLTagAttributes(bulkOperationClass, operationName);
			validateAssociations(bulkOperationClass, operationName, csvColumnNames);
		}
		catch (Exception exp)
		{
			logger.debug(exp.getMessage(), exp);
			throw new Exception(exp.getMessage(), exp);
		}
	}
	/**
	 * Validate Associations.
	 * @param bulkOperationClass BulkOperationClass.
	 * @param operationName String.
	 * @param csvColumnNames Hash table of String, String.
	 * @throws BulkOperationException BulkOperationException.
	 */
	private static void validateAssociations(BulkOperationClass
		bulkOperationClass, String operationName, Map<String, String> csvColumnNames)
		throws BulkOperationException
	{
		if (bulkOperationClass.getContainmentAssociationCollection() != null
				&& !bulkOperationClass.getContainmentAssociationCollection().isEmpty())
		{
			Collection<BulkOperationClass> containmentClassList = bulkOperationClass
					.getContainmentAssociationCollection();
			validateContainmentReference(operationName, containmentClassList, csvColumnNames);
		}
		if (bulkOperationClass.getReferenceAssociationCollection() != null
				&& !bulkOperationClass.getReferenceAssociationCollection().isEmpty())
		{
			Collection<BulkOperationClass> referenceClassList = bulkOperationClass
					.getReferenceAssociationCollection();
			validateContainmentReference(operationName, referenceClassList, csvColumnNames);
		}
		if (bulkOperationClass.getAttributeCollection() != null
				&& !bulkOperationClass.getAttributeCollection().isEmpty())
		{
			Collection<Attribute> attributesClassList = bulkOperationClass
					.getAttributeCollection();
			validateAttributes(attributesClassList, bulkOperationClass, csvColumnNames);
		}
	}
	/**
	 * Validate Attributes.
	 * @param attributesClassList Collection of Attributes.
	 * @param bulkOperationClass BulkOperationClass.
	 * @param csvColumnNames Hash table of String, String.
	 * @throws BulkOperationException BulkOperationException.
	 */
	private static void validateAttributes(Collection<Attribute>
		attributesClassList, BulkOperationClass bulkOperationClass,
		Map<String, String> csvColumnNames)
		throws BulkOperationException
	{
		Class classObject = bulkOperationClass.getClassObject();
		for(Attribute attribute : attributesClassList)
		{
			try
			{
				Field field = null;
				String attributeName = attribute.getName();
				if(attributeName == null)
				{
					logger.debug("The keyword 'attributeName' is either missing or incorrectly " +
							"written in the XML for "+ bulkOperationClass.getClassName() +
							" class tag.");
					errorList.add("The keyword 'attributeName' is either missing or incorrectly " +
							"written in the XML for "+ bulkOperationClass.getClassName() +
							" class tag.");
				}
				else if("".equals(attributeName.trim()))
				{
					logger.debug("The 'attributeName' value mentioned for "
							+ bulkOperationClass.getClassName() + " is incorrect.");
					errorList.add("The 'attributeName' value mentioned for "
						+ bulkOperationClass.getClassName() + " is incorrect.");
				}
				else
				{
					field = getDeclaredField(classObject, attributeName);
					if(field == null)
					{
						logger.debug("The keyword '"+ attributeName +"' is either missing or incorrectly " +
							"written in the XML for "+ bulkOperationClass.getClassName() +
							" class tag.");
						errorList.add("The keyword 'field' is either missing or incorrectly " +
							"written in the XML for "+ bulkOperationClass.getClassName() +
							" class tag.");
					}
				}
				validateColumnName(bulkOperationClass, attribute, csvColumnNames);
				validateDataType(bulkOperationClass, attribute, field, attributeName);
				validateUpdateBasedOn(bulkOperationClass, attribute);
			}
			catch (Exception exp)
			{
				logger.debug(exp.getMessage(), exp);
				errorList.add(exp.getMessage());
			}
		}
	}
	/**
	 * Validate UpdateBasedOn value.
	 * @param bulkOperationClass BulkOperationClass.
	 * @param attribute Attribute.
	 */
	private static void validateUpdateBasedOn(
			BulkOperationClass bulkOperationClass, Attribute attribute)
	{
		boolean updateBasedOn = attribute.getUpdateBasedOn();
		if(Boolean.toString(updateBasedOn) == null ||
				"".equals(Boolean.toString(updateBasedOn)))
		{
			logger.debug("The keyword 'updateBasedOn' is either missing or incorrectly " +
				"written in the XML for "+ bulkOperationClass.getClassName() + " class tag.");
			errorList.add("The keyword 'updateBasedOn' is either missing or incorrectly " +
				"written in the XML for "+ bulkOperationClass.getClassName() + " class tag.");
		}
	}
	/**
	 * Validate Data Type.
	 * @param bulkOperationClass BulkOperationClass.
	 * @param attribute Attribute.
	 * @param field Field.
	 * @param attributeName String.
	 */
	private static void validateDataType(BulkOperationClass bulkOperationClass,
			Attribute attribute, Field field, String attributeName)
	{
		String dataType = attribute.getDataType();
		if(dataType == null)
		{
			logger.debug("The keyword 'dataType' is either missing or incorrectly " +
				"written in the XML for "+ bulkOperationClass.getClassName() + " class tag.");
			errorList.add("The keyword 'dataType' is either missing or incorrectly " +
				"written in the XML for "+ bulkOperationClass.getClassName() + " class tag.");
		}
		else if(field != null)
		{
			Class fieldDataType = field.getType();
			if(!fieldDataType.toString().contains(dataType))
			{
				logger.debug("The 'fieldDataType' value mentioned for "
					+ bulkOperationClass.getClassName() + " is incorrect.");
				errorList.add("The 'fieldDataType' value mentioned for "
					+ bulkOperationClass.getClassName() + " is incorrect.");
			}
		}
	}
	/**
	 * Validate Column Name.
	 * @param bulkOperationClass BulkOperationClass.
	 * @param attribute Attribute.
	 * @param csvColumnNames Hash table of String, String.
	 */
	private static void validateColumnName(
			BulkOperationClass bulkOperationClass, Attribute attribute,
			Map<String, String> csvColumnNames)
	{
		String csvColumnName = attribute.getCsvColumnName();
		if(csvColumnName == null)
		{
			logger.debug("The keyword 'csvColumnName' is either missing or incorrectly " +
				"written in the XML for "+ bulkOperationClass.getClassName() + " class tag.");
			errorList.add("The keyword 'csvColumnName' is either missing or incorrectly " +
				"written in the XML for "+ bulkOperationClass.getClassName() + " class tag.");
		}
		else if(!csvColumnNames.containsKey(csvColumnName))
		{
			logger.debug("The column name '" + csvColumnName + "' of attribute name '"
				+ attribute.getName() + "' in " + bulkOperationClass.getClassName()
				+ " class tag mismatches with the column name specified in " +
				"the CSV template.");
			errorList.add("The column name '" + csvColumnName + "' of attribute name '"
					+ attribute.getName() + "' in " + bulkOperationClass.getClassName()
					+ " class tag mismatches with the column name specified in " +
					"the CSV template.");
		}
	}
	/**
	 * Get Declared Field.
	 * @param classObject Class.
	 * @param attributeName String.
	 * @return Field Field.
	 */
	private static Field getDeclaredField(Class classObject, String attributeName)
	{
		Field field = null;
		boolean flag = false;
		do
		{
			try
			{
				if(flag)
				{
					classObject = classObject.getSuperclass();
				}
				field = classObject.getDeclaredField(attributeName);
				flag = true;
			}
			catch (Exception exp)
			{
				flag = true;
			}
		}
		while(classObject.getSuperclass() != null && field == null);
		return field;
	}
	/**
	 * Validate Containment and Reference.
	 * @param operationName String.
	 * @param classList Collection of BulkOperationClass.
	 * @param csvColumnNames Hash table of String, String values.
	 * @throws BulkOperationException BulkOperationException.
	 */
	private static void validateContainmentReference(String operationName,
		Collection<BulkOperationClass> classList, Map<String, String> csvColumnNames)
		throws BulkOperationException
	{
		for(BulkOperationClass innerClass : classList)
		{
			try
			{
				Class innerClassObject = innerClass.getClassObject();
				validateXMLTagAttributes(innerClass, operationName);
				validateAssociations(innerClass, operationName, csvColumnNames);
			}
			catch (NullPointerException exp)
			{
				logger.debug("The keyword 'className' is either missing or incorrectly " +
					"written for a XML inner class tag.", exp);
				throw new BulkOperationException("The keyword 'className' is either missing " +
					"or incorrectly written for a XML inner class tag.", exp);
			}
			catch (Exception exp)
			{
				logger.debug("The 'className' value mentioned is incorrect in the XML class tag." ,exp);
				throw new BulkOperationException("The 'className' value mentioned is incorrect " +
					"in the XML class tag.", exp);
			}
		}
	}
	/**
	 * Validate XML Tag Attributes.
	 * @param bulkOperationClass BulkOperationClass.
	 * @param operationName String.
	 */
	private static void validateXMLTagAttributes(
			BulkOperationClass bulkOperationClass, String operationName)
	{
		getRelationShipType(bulkOperationClass);
		String templateName = bulkOperationClass.getTemplateName();
		if(!operationName.equals(templateName.trim()))
		{
			logger.debug("The keyword 'templateName' is either missing or incorrectly "
				+ "written in the XML for "+ bulkOperationClass.getClassName() + " class tag.");
			errorList.add("The keyword 'templateName' is either missing or incorrectly "
				+ "written in the XML for "+ bulkOperationClass.getClassName() + " class tag.");
		}
		boolean isOneToManyAssociation = bulkOperationClass.getIsOneToManyAssociation();
		if(Boolean.toString(isOneToManyAssociation) == null ||
				"".equals(Boolean.toString(isOneToManyAssociation)))
		{
			logger.debug("The keyword 'isOneToManyAssociation' is either missing or incorrectly "
				+ "written in the XML for "+ bulkOperationClass.getClassName() + " class tag.");
			errorList.add("The keyword 'isOneToManyAssociation' is either missing or incorrectly "
				+ "written in the XML for "+ bulkOperationClass.getClassName() + " class tag.");
		}
		validateCardinality(bulkOperationClass);
		String roleName = bulkOperationClass.getRoleName();
		if(roleName == null || "".equals(roleName.trim()))
		{
			logger.debug("The keyword 'roleName' is either missing or incorrectly " +
				"written in the XML for "+ bulkOperationClass.getClassName() + " class tag.");
			errorList.add("The keyword 'roleName' is either missing or incorrectly " +
				"written in the XML for "+ bulkOperationClass.getClassName() + " class tag.");
		}
	}
	/**
	 * Get RelationShip Type.
	 * @param bulkOperationClass BulkOperationClass.
	 */
	private static void getRelationShipType(
			BulkOperationClass bulkOperationClass)
	{
		String relationShipType = bulkOperationClass.getRelationShipType();
		if(relationShipType == null)
		{
			logger.debug("The keyword 'relationShipType' is either missing or incorrectly " +
					"written in the XML for "+ bulkOperationClass.getClassName() + " class tag.");
			errorList.add("The keyword 'relationShipType' is either missing or incorrectly " +
					"written in the XML for "+ bulkOperationClass.getClassName() + " class tag.");
		}
		else if(("".equals(relationShipType.trim())) ||
			(!"containment".equals(relationShipType.trim()) && !"main".equals(
			relationShipType.trim()) && !"association".equals(relationShipType.trim())))
		{
			logger.debug("The 'relationShipType' value mentioned for "
				+ bulkOperationClass.getClassName() + " is incorrect. " +
				"Valid values are 'main', 'assocuation' and 'containment' only.");
			errorList.add("The 'relationShipType' value mentioned for "
				+ bulkOperationClass.getClassName() + " is incorrect. " +
				"Valid values are 'main', 'assocuation' and 'containment' only.");
		}
	}
	/**
	 * Validate Cardinality.
	 * @param bulkOperationClass BulkOperationClass.
	 */
	private static void validateCardinality(
			BulkOperationClass bulkOperationClass)
	{
		String cardinality = bulkOperationClass.getCardinality();
		if(cardinality == null)
		{
			logger.debug("The keyword 'cardinality' is either missing or incorrectly " +
				"written in the XML for "+ bulkOperationClass.getClassName() + " class tag.");
			errorList.add("The keyword 'cardinality' is either missing or incorrectly " +
				"written in the XML for "+ bulkOperationClass.getClassName() + " class tag.");
		}
		else if(("".equals(cardinality.trim())) ||
			(!"*".equals(cardinality.trim()) && !"1".equals(cardinality.trim())))
		{
			logger.debug("The 'cardinality' value mentioned for "
					+ bulkOperationClass.getClassName() + " is incorrect. " +
					"Valid values are '1' and '*' only.");
			errorList.add("The 'cardinality' value mentioned for "
					+ bulkOperationClass.getClassName() + " is incorrect. " +
					"Valid values are '1' and '*' only.");
		}
	}
}