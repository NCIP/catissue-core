package edu.wustl.catissuecore.bizlogic.bulkOperations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import au.com.bytecode.opencsv.CSVReader;
import edu.wustl.bulkoperator.metadata.BulkOperationClass;
import edu.wustl.bulkoperator.metadata.BulkOperationMetaData;
import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * Import Bulk Operation from UI back end target.
 * @author sagar_baldwa
 *
 */
public class ImportBulkOperationTemplate
{
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
		initiateProcess(args);
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
			initiateProcess(args);
		}
		catch (Exception e)
		{
			System.out.println("------------------------ERROR:--------------------------------\n");
			System.out.println("------------------------ERROR:--------------------------------\n");
			System.out.println(e.getMessage() + "\n\n");
			System.out.println("------------------------ERROR:--------------------------------\n");
			System.out.println("------------------------ERROR:--------------------------------");
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
			System.out.println("Total rows in tables now : " + rowCount);
			if(rowCount > 0)
			{
				System.out.println("Data updated successfully. " + rowCount + " rows edited");
			}
			else
			{
				System.out.println("No rows edited");
			}
		}
		catch (SQLException e)
		{
			throw new BulkOperationException("Error in updating the record in database.");
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
			System.out.println("Total rows in tables now : " + rowCount);
			System.out.println("Data inserted successfully");
		}
		catch (SQLException e)
		{
			throw new BulkOperationException("Error in inserting the record in database.");
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
						throw new BulkOperationException("Cannot insert template as " +
							"same Operation Name already exists in the database.");
					}
					else if(!operationNameFromDB.equals(operationName) &
							dropdownNameFromDB.equals(dropdownName))
					{
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
		catch (FileNotFoundException e)
		{
			System.out.println("\n");
			e.printStackTrace();
			throw new BulkOperationException("\nXML File Not Found at the specified path.");
		}
		catch (IOException e)
		{
			System.out.println("\n");
			e.printStackTrace();
			throw new BulkOperationException("\nError in reading the XML File.");
		}
		catch (Exception e)
		{
			System.out.println("\nError in encoding XML file to data stream.");
			throw e;
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
			System.out.println("Records in CSV files : " + (list.size() - 1));
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
		catch (FileNotFoundException e)
		{
			System.out.println("\n");
			e.printStackTrace();
			throw new BulkOperationException("\nCSV File Not Found at the specified path.");
		}
		catch (IOException e)
		{
			System.out.println("\n");
			e.printStackTrace();
			throw new BulkOperationException("\nError in reading the CSV File.");
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
		/*String operationName = "editReceivedEventParameters";//args[0];
		String dropdownName = "editReceivedEventParameters_1";//args[1];
		String csvFile = "E:/editReceivedEventParameters.csv";//args[2];
		String xmlFile = "E:/editReceivedEventParameters.xml";//args[3];
		String csvFileData = getCSVTemplateFileData(csvFile);
		String xmlFileData = getXMLTemplateFileData(xmlFile);*/
		if (args.length == 0)
		{
			throw new Exception("Please Specify the operation name.");
		}
		if (args.length < 2)
		{
			throw new Exception("Please specify the dropdown name.");
		}
		if (args.length < 3)
		{
			throw new Exception("Please specify the CSV template file name.");
		}
		if (args.length < 4)
		{
			throw new Exception("Please specify the XML template file name.");
		}
		BulkOperationMetaData bulkOperationMetaData =
				BulkOperationUtility.getBulkOperationMetaDataObject(args[3]);
		validateOperationName(bulkOperationMetaData, args[0]);
		//saveTemplateInDatabase(operationName, dropdownName, csvFileData, xmlFileData);
	}
}