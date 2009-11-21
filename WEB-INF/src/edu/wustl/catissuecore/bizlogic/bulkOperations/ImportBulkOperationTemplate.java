package edu.wustl.catissuecore.bizlogic.bulkOperations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import au.com.bytecode.opencsv.CSVReader;
import edu.wustl.bulkoperator.util.BulkOperationException;

/**
 * Import Bulk Operation from UI back end target.
 * @author sagar_baldwa
 *
 */
public class ImportBulkOperationTemplate
{
	/**
	 * Main method.
	 * @param args Array of Strings. 
	 */
	public static void main(String[] args)
	{
		try
		{
			/*String operationName = "createFluidSpecimenReviewParameters";//args[0];
			String dropdownName = "Fluid Event";//args[1];
			String csvFile = "E:/createFluidReviewEvent.csv";//args[2];
			String xmlFile = "E:/bulkOperationMetaData.xml";//args[3];
			String csvFileData = getCSVTemplateFileData(csvFile);
			String xmlFileData = getXMLTemplateFileData(xmlFile);*/
			validate(args);
			String operationName = args[0];
			String dropdownName = args[1];
			String csvFile = args[2];
			String xmlFile = args[3];
			String csvFileData = getCSVTemplateFileData(csvFile);
			String xmlFileData = getXMLTemplateFileData(xmlFile);
			addTemplateInDatabase(operationName, dropdownName, csvFileData, xmlFileData);
		}
		catch (Exception e)
		{
			System.out.println("------------------------ERROR:--------------------------------\n");
			System.out.println("------------------------ERROR:--------------------------------\n");
			System.out.println(e.getMessage() + "\n\n");
			e.printStackTrace();
			System.out.println("------------------------ERROR:--------------------------------");
			System.out.println("------------------------ERROR:--------------------------------");
		}
	}
	
	/**
	 * Add Template In Database.
	 * @param operationName String.
	 * @param dropdownName String.
	 * @param csvFileData String.
	 * @param xmlFileData String.
	 * @throws BulkOperationException BulkOperationException.
	 * @throws SQLException BulkOperationException.
	 */
	private static void addTemplateInDatabase(String operationName, String dropdownName,
			String csvFileData, String xmlFileData) throws BulkOperationException,
			SQLException
	{
		Connection connection = null;
		try
		{
			connection = DBManagerImpl.getConnection();
			boolean flag = checkIfEditCase(connection, operationName, dropdownName);
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
			connection.close();
		}
		catch (Exception e)
		{
			throw new BulkOperationException("Error in database connection operation.");
		}
		finally
		{
			connection.close();
		}
	}
	
	private static void editTemplate(Connection connection,
			String operationName, String dropdownName, String csvFileData,
			String xmlFileData) throws BulkOperationException, SQLException
	{		
		PreparedStatement preparedStatement = null;
		try
		{
			String query = "update catissue_bulk_operation set csv_template = ?, " +
					"xml_tempalte = ?, OPERATION = ?, DROPDOWN_NAME = ? " +
					"where OPERATION = ? or DROPDOWN_NAME= ?";
			preparedStatement = connection.prepareStatement(query);			
			preparedStatement.setString(1, csvFileData);
			preparedStatement.setString(2, xmlFileData);
			preparedStatement.setString(3, operationName);
			preparedStatement.setString(4, dropdownName);
			preparedStatement.setString(5, operationName);
			preparedStatement.setString(6, dropdownName);
			int rowCount = preparedStatement.executeUpdate();
			//System.out.println("Total rows in tables now : " + rowCount);
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
			String query = "insert into catissue_bulk_operation (OPERATION, DROPDOWN_NAME, " +
					"csv_template, xml_tempalte) values (?,?,?,?)";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, operationName);
			preparedStatement.setString(2, dropdownName);
			preparedStatement.setString(3, csvFileData);
			preparedStatement.setString(4, xmlFileData);
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
	 * @return boolean.
	 * @throws Exception Exception.
	 */
	private static boolean checkIfEditCase(Connection connection,
			String operationName, String dropdownName) throws Exception
	{
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean flag = false;
		try
		{
			String query = "select * from catissue_bulk_operation where " +
					"operation like ? or dropdown_name like ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, operationName);
			preparedStatement.setString(2, dropdownName);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next())
			{
				flag = true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
		StringWriter xmlFormatData = new StringWriter();;
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

	@SuppressWarnings("unchecked")
	/**
	 * Get CSV Template File Data.
	 * @param csvFile String.
	 * @return String.
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
			throw new Exception("Please specify the XML template file name.");
		}
		if (args.length < 4)
		{
			throw new Exception("Please specify the csv template file name.");
		}				
	}
}