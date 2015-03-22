
package com.krishagni.catissueplus.bulkoperator.templateImport;


/**
 * Import Bulk Operation from UI back end target.
 * @author sagar_baldwa
 *
 */
public class ImportBulkOperationTemplate extends AbstractImportBulkOperation
{

	/**
	 * Import Bulk Operation Template.
	 * @param operationName String.
	 * @param dropdownName String.
	 * @param csvFilePath String.
	 * @param xmlFilePath String.
	 * @throws Exception Exception.
	 */
//	public ImportBulkOperationTemplate(String operationName, String dropdownName, String csvFile,
//			String xmlFile)
//	{
//		importTemplates(operationName, dropdownName, csvFile, xmlFile,"./"+System.getProperty(BulkOperationConstants.CONFIG_DIR)
//				+ File.separator +"bulkOperatorXMLTemplateRules.xml","./"+System.getProperty(BulkOperationConstants.CONFIG_DIR)
//				+ File.separator +"BulkOperations.xsd");
//	}
//
//	/**
//	 * Main method.
//	 * @param args Array of Strings.
//	 * @throws IOException
//	 */
//	public static void main(String[] args)
//	{
//		InputStream stream;
//		try
//		{
//			//System.setProperty(BulkOperationConstants.CONFIG_DIR, "software/bulkoperator/conf");
//			//args = new String[] {"Lymphoma_Labs 2.0","Lymphoma_Labs 2.0","C:/Pavan/Dynamicxtensions/CP_1.6_branch/CPUML/Lymphoma_Labs 2.0.csv","C:/Pavan/Dynamicxtensions/CP_1.6_branch/CPUML/Lymphoma_Labs 2.0.xml"};
//			//args = new String[] {"addUser","addUser","C:/Pavan/Dynamicxtensions/CP_1.6_branch/CPUML/AddUser.csv","C:/Pavan/Dynamicxtensions/CP_1.6_branch/CPUML/addUser.xml"};
//			stream = new FileInputStream(System.getProperty(BulkOperationConstants.CONFIG_DIR)
//					+ File.separator +"ApplicationResources.properties");
//			ErrorKey.addErrorKeysToMap(stream);
//		}
//		catch (FileNotFoundException fileNotExp)
//		{
//			logger.debug("Error in initializing Application Resource Properties File", fileNotExp);
//		}
//		catch (IOException e)
//		{
//			logger.debug("Error in initializing Application Resource Properties File", e);
//		}
//		String operationName = args[0];
//		String dropdownName = args[1];
//		String csvFile = args[2];
//		String xmlFile = args[3];
//		new ImportBulkOperationTemplate(operationName, dropdownName, csvFile, xmlFile);
//	}
//
//	/**
//	 * Edit Template.
//	 * @param connection Connection.
//	 * @param operationName String.
//	 * @param dropdownName String.
//	 * @param csvFile String.
//	 * @param xmlFile String.
//	 * @throws BulkOperationException BulkOperationException.
//	 * @throws IOException
//	 * @throws SQLException
//	 * @throws SQLException SQLException.
//	 */
//	protected void editTemplate(String operationName, String dropdownName, String csvFile,
//			String xmlFile) throws BulkOperationException, DAOException, IOException
//	{
//		PreparedStatement preparedStatement = null;
//		Connection connection = null;
//		try
//		{
//			connection = DBManagerImpl.getConnection();
//			BulkOperationUtility.getDatabaseType();
//				String query = "update catissue_bulk_operation set OPERATION = ?, "
//						+ "CSV_TEMPLATE = ?, XML_TEMPALTE = ?,  DROPDOWN_NAME = ? "
//						+ "where OPERATION = ? or DROPDOWN_NAME= ? ";
//				preparedStatement = connection.prepareStatement(query);
//				preparedStatement.setString(1, operationName);
//
//				String csvFileData = getCSVTemplateFileData(csvFile);
//				String xmlFileData = getXMLTemplateFileData(xmlFile);
//				StringReader csvReader = new StringReader(csvFileData);
//				preparedStatement.setCharacterStream(2, csvReader, csvFileData.length());
//
//				StringReader reader = new StringReader(xmlFileData);
//				preparedStatement.setCharacterStream(3, reader, xmlFileData.length());
//				preparedStatement.setString(4, dropdownName);
//				preparedStatement.setString(5, operationName);
//				preparedStatement.setString(6, dropdownName);
//
//			/*else if (BulkOperationConstants.MYSQL_DATABASE.equalsIgnoreCase(databaseType))
//			{
//				String query = "update catissue_bulk_operation set OPERATION = ?, "
//						+ "CSV_TEMPLATE = ?, XML_TEMPALTE = ?,  DROPDOWN_NAME = ? "
//						+ "where OPERATION = ? or DROPDOWN_NAME= ? ";
//				preparedStatement = connection.prepareStatement(query);
//				preparedStatement.setString(1, operationName);
//				preparedStatement.setString(2, csvFile);
//				preparedStatement.setString(3, xmlFile);
//				preparedStatement.setString(4, dropdownName);
//				preparedStatement.setString(5, operationName);
//				preparedStatement.setString(6, dropdownName);
//			}*/
//			int rowCount = preparedStatement.executeUpdate();
//			if (rowCount > 0)
//			{
//				logger.info("Data updated successfully. " + rowCount + " row edited");
//			}
//			else
//			{
//				logger.info("No rows updated.");
//			}
//		}
//		catch (SQLException sqlExp)
//		{
//			logger.debug("Error in updating the record in database.", sqlExp);
//			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.database.insert.update.error");
//			throw new BulkOperationException(errorkey, sqlExp, "updating");
//		}
//		finally
//		{
//			if (preparedStatement != null)
//			{
//				try
//				{
//					preparedStatement.close();
//				}
//				catch (SQLException exception)
//				{
//					throw new DAOException(null, exception,
//							"Error while closing the Prepared statement object.");
//				}
//			}
//		}
//	}
//
//	/**
//	 * Add Template.
//	 * @param connection Connection.
//	 * @param operationName String.
//	 * @param dropdownName String.
//	 * @param csvFile String.
//	 * @param xmlFile String.
//	 * @throws BulkOperationException BulkOperationException.
//	 * @throws IOException
//	 * @throws SQLException
//	 * @throws SQLException
//	 * @throws SQLException SQLException.
//	 */
//	protected void addTemplate(String operationName, String dropdownName, String csvFile,
//			String xmlFile) throws BulkOperationException, DAOException, IOException
//	{
//		PreparedStatement preparedStatement = null;
//		Statement statement = null;
//		ResultSet resultSet = null;
//		Connection connection = null;
//		try
//		{
//			connection = DBManagerImpl.getConnection();
//			if (BulkOperationConstants.ORACLE_DATABASE.equalsIgnoreCase(BulkOperationUtility
//					.getDatabaseType()))
//			{
//				String sequenceQuery = "select CATISSUE_BULK_OPERATION_SEQ.NEXTVAL from dual";
//				statement = connection.createStatement();
//				resultSet = statement.executeQuery(sequenceQuery);
//				int sequenceNumber = 0;
//				if (resultSet.next())
//				{
//					sequenceNumber = resultSet.getInt(1);
//					if (sequenceNumber > 0)
//					{
//						String query = "insert into catissue_bulk_operation "
//								+ "(IDENTIFIER, OPERATION, CSV_TEMPLATE, XML_TEMPALTE, "
//								+ "DROPDOWN_NAME ) values (?, ?, ?, ?, ?)";
//
//						preparedStatement = connection.prepareStatement(query);
//						preparedStatement.setInt(1, sequenceNumber);
//						preparedStatement.setString(2, operationName);
//
//						String csvFileData = getCSVTemplateFileData(csvFile);
//						String xmlFileData = getXMLTemplateFileData(xmlFile);
//
//						StringReader csvReader = new StringReader(csvFileData);
//						preparedStatement.setCharacterStream(3, csvReader, csvFileData.length());
//						StringReader reader = new StringReader(xmlFileData);
//						preparedStatement.setCharacterStream(4, reader, xmlFileData.length());
//						preparedStatement.setString(5, dropdownName);
//					}
//				}
//			}
//			else if (BulkOperationConstants.MYSQL_DATABASE.equalsIgnoreCase(BulkOperationUtility
//					.getDatabaseType()))
//			{
//				String query = "insert into catissue_bulk_operation (OPERATION, "
//						+ "CSV_TEMPLATE, XML_TEMPALTE, DROPDOWN_NAME ) values (?, ?, ?, ?)";
//				preparedStatement = connection.prepareStatement(query);
//				String csvFileData = getCSVTemplateFileData(csvFile);
//				String xmlFileData = getXMLTemplateFileData(xmlFile);
//				StringReader csvReader = new StringReader(csvFileData);
//				preparedStatement.setCharacterStream(2, csvReader, csvFileData.length());
//				StringReader reader = new StringReader(xmlFileData);
//				preparedStatement.setCharacterStream(3, reader, xmlFileData.length());
//
//				preparedStatement.setString(1, operationName);
//
//				preparedStatement.setString(4, dropdownName);
//			}
//			int rowCount = preparedStatement.executeUpdate();
//			logger.info("Data inserted successfully. " + rowCount + " row inserted.");
//		}
//		catch (SQLException sqlExp)
//		{
//			logger.debug("Error in inserting the record in database.", sqlExp);
//			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.database.insert.update.error");
//			throw new BulkOperationException(errorkey, sqlExp, "inserting");
//		}
//		finally
//		{
//			try
//			{
//				if (resultSet != null)
//				{
//					resultSet.close();
//				}
//				if (statement != null)
//				{
//					statement.close();
//				}
//				if (preparedStatement != null)
//				{
//					preparedStatement.close();
//				}
//			}
//			catch (SQLException exception)
//			{
//				throw new DAOException(null, exception,
//						"Error while closing the connection objects.");
//			}
//		}
//	}
//
//	/**
//	 * Check If Edit Case.
//	 * @param connection Connection.
//	 * @param operationName String.
//	 * @param dropdownName String.
//	 * @param dao TODO
//	 * @return boolean Boolean.
//	 * @throws SQLException
//	 * @throws Exception Exception.
//	 */
//	protected boolean checkAddOrEditTemplateCase(String operationName, String dropdownName)
//			throws BulkOperationException, DAOException
//	{
//		PreparedStatement preparedStatement = null;
//		ResultSet resultSet = null;
//		boolean flag = false;
//		Connection connection = null;
//		try
//		{
//			connection = DBManagerImpl.getConnection();
//			String query = "select operation, dropdown_name from catissue_bulk_operation "
//					+ "where operation like ? or dropdown_name like ?";
//			preparedStatement = connection.prepareStatement(query);
//			preparedStatement.setString(1, operationName);
//			preparedStatement.setString(2, dropdownName);
//			resultSet = preparedStatement.executeQuery();
//			if (resultSet != null)
//			{
//				while (resultSet.next())
//				{
//					String operationNameFromDB = resultSet.getString("OPERATION");
//					String dropdownNameFromDB = resultSet.getString("DROPDOWN_NAME");
//					flag = isTemplateExist(operationName, operationNameFromDB, dropdownNameFromDB,
//							dropdownName);
//					if (flag)
//					{
//						break;
//					}
//				}
//			}
//		}
//		catch (SQLException exp)
//		{
//			logger.debug("Error in database operation. Please check database driver and database "
//					+ "properties mentioned in the host application install properties file.", exp);
//			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.database.error.driver.msg");
//			throw new BulkOperationException(errorkey, exp, "");
//		}
//		finally
//		{
//			try
//			{
//				if(resultSet!=null)
//				{
//					resultSet.close();
//
//					preparedStatement.close();
//				}
//			}
//			catch (SQLException exception)
//			{
//				throw new DAOException(null, exception,
//						"Error while closing the connection objects.");
//			}
//		}
//		return flag;
//	}
}