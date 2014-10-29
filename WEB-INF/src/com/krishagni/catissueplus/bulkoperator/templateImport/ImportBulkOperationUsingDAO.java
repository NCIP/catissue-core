/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

/**
 *
 */

package com.krishagni.catissueplus.bulkoperator.templateImport;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.krishagni.catissueplus.bulkoperator.util.BulkOperationConstants;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.dao.DAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;

/**
 * @author shrishail_kalshetty
 *
 */
public class ImportBulkOperationUsingDAO extends AbstractImportBulkOperation
{

	/**
	 * DAO object.
	 */
	private final DAO dao;

	/**
	 * @param operationName
	 * @param dropdownName
	 * @param csvFile
	 * @param xmlFile
	 * @param mappingXml
	 * @throws Exception
	 */
	public ImportBulkOperationUsingDAO(String operationName, String dropdownName, String csvFile,
			String xmlFile, DAO dao)
	{
		this.dao = dao;
		importTemplates(operationName, dropdownName, csvFile, xmlFile,"./"+System.getProperty(BulkOperationConstants.CONFIG_DIR)
				+ File.separator +"bulkOperatorXMLTemplateRules.xml","./"+System.getProperty(BulkOperationConstants.CONFIG_DIR)
				+ File.separator +"BulkOperations.xsd");
		closeDAO();
	}

	private void closeDAO()
	{
		try
		{
			this.dao.closeSession();
		}
		catch (DAOException exception)
		{
			logger.error("");
		}
	}

	/**
	 *
	 * @param operationName
	 * @param dropdownName
	 * @param dao
	 * @return
	 * @throws DAOException
	 * @throws BulkOperationException
	 */
	@SuppressWarnings("unchecked")
	protected boolean checkAddOrEditTemplateCase(String operationName, String dropdownName)
			throws BulkOperationException
	{
		boolean flag = false;
		String query = "select operation, dropdown_name from catissue_bulk_operation "
				+ "where operation like ? or dropdown_name like ?";
		List<ColumnValueBean> paramList = new ArrayList<ColumnValueBean>();
		paramList.add(new ColumnValueBean("operation", operationName));
		paramList.add(new ColumnValueBean("dropdown_name", dropdownName));
		try
		{
			List resultList = ((JDBCDAO) this.dao).executeQuery(query, null, paramList);
			for (Object object : resultList)
			{
				List valueList = (List) object;
				flag = isTemplateExist(operationName, (String) valueList.get(0), (String) valueList
						.get(1), dropdownName);
				if (flag)
				{
					break;
				}
			}
		}
		catch (DAOException exception)
		{
			logger.debug(
					"Error in database operation. DAO object not found. Please check that application name "
							+ "exist.", exception);
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.database.error.driver.msg");
			throw new BulkOperationException(errorkey, exception, "");
		}
		return flag;
	}

	/**
	 *
	 * @param operationName
	 * @param dropdownName
	 * @param csvFile
	 * @param xmlFile
	 * @throws DAOException
	 * @throws IOException
	 * @throws BulkOperationException
	 */
	protected void editTemplate(String operationName, String dropdownName, String csvFile,
			String xmlFile) throws DAOException, BulkOperationException, IOException, SQLException
	{
		final JDBCDAO jdbcdao = (JDBCDAO) this.dao;
		final String dataBaseType = jdbcdao.getDatabaseProperties().getDataBaseType();
		List<LinkedList<ColumnValueBean>> values = new ArrayList<LinkedList<ColumnValueBean>>();
		LinkedList<ColumnValueBean> colValueBeanList = new LinkedList<ColumnValueBean>();
		String query = "";
		colValueBeanList.add(new ColumnValueBean("OPERATION", operationName));

		if (BulkOperationConstants.ORACLE_DATABASE.equalsIgnoreCase(dataBaseType))
		{
			query = "update catissue_bulk_operation set OPERATION = ?, "
					+ "CSV_TEMPLATE = ?, XML_TEMPALTE = ?,  DROPDOWN_NAME = ? "
					+ "where OPERATION = ? or DROPDOWN_NAME= ? ";
			final String csvFileData = getCSVTemplateFileData(csvFile);
			final String xmlFileData = getXMLTemplateFileData(xmlFile);
			colValueBeanList.add(new ColumnValueBean("CSV_TEMPLATE", new StringReader(csvFileData)));
			colValueBeanList.add(new ColumnValueBean("XML_TEMPALTE", new StringReader(xmlFileData)));
		}
		else if (BulkOperationConstants.MYSQL_DATABASE.equalsIgnoreCase(dataBaseType))
		{
			query = "update catissue_bulk_operation set OPERATION = ?, "
					+ "CSV_TEMPLATE = ?, XML_TEMPALTE = ?,  DROPDOWN_NAME = ? "
					+ "where OPERATION = ? or DROPDOWN_NAME= ? ";
			colValueBeanList.add(new ColumnValueBean("CSV_TEMPLATE",
					getCSVTemplateFileData(csvFile)));
			colValueBeanList.add(new ColumnValueBean("XML_TEMPALTE",
					getXMLTemplateFileData(xmlFile)));
		}

		colValueBeanList.add(new ColumnValueBean("DROPDOWN_NAME", dropdownName));
		colValueBeanList.add(new ColumnValueBean("OPERATION", operationName));
		colValueBeanList.add(new ColumnValueBean("DROPDOWN_NAME", dropdownName));
		values.add(colValueBeanList);

		jdbcdao.executeUpdate(query, values);
		jdbcdao.commit();
		logger.info("Data updated successfully.");
	}

	/**
	 *
	 * @param operationName
	 * @param dropdownName
	 * @param csvFile
	 * @param xmlFile
	 * @throws DAOException
	 * @throws BulkOperationException
	 * @throws SQLException
	 */
	@SuppressWarnings({"deprecation", "unchecked"})
	protected void addTemplate(String operationName, String dropdownName, String csvFile,
			String xmlFile) throws DAOException, IOException, BulkOperationException, SQLException
	{
		final JDBCDAO jdbcdao = (JDBCDAO) this.dao;
		final String dataBaseType = jdbcdao.getDatabaseProperties().getDataBaseType();
		List<LinkedList<ColumnValueBean>> values = new ArrayList<LinkedList<ColumnValueBean>>();
		LinkedList<ColumnValueBean> colValueBeanList = new LinkedList<ColumnValueBean>();
		String query = "";
		if (BulkOperationConstants.ORACLE_DATABASE.equalsIgnoreCase(dataBaseType))
		{
			Integer sequenceNumber = 0;
			String sequenceQuery = "select CATISSUE_BULK_OPERATION_SEQ.NEXTVAL from dual";
			List resultList = jdbcdao.executeQuery(sequenceQuery);
			for (Object object : resultList)
			{
				List valueList = (List) object;
				sequenceNumber = Integer.valueOf(valueList.get(0).toString());
			}
			query = "insert into catissue_bulk_operation "
					+ "(IDENTIFIER, OPERATION, XML_TEMPALTE, "
					+ "DROPDOWN_NAME,CSV_TEMPLATE ) values (?, ?, ?, ?, ?)";

			final String csvFileData = getCSVTemplateFileData(csvFile);
			final String xmlFileData = getXMLTemplateFileData(xmlFile);
			colValueBeanList.add(new ColumnValueBean("IDENTIFIER", sequenceNumber));
			colValueBeanList.add(new ColumnValueBean("OPERATION", operationName));
			colValueBeanList.add(new ColumnValueBean("XML_TEMPALTE", new StringReader(xmlFileData)));
			colValueBeanList.add(new ColumnValueBean("DROPDOWN_NAME", dropdownName));
			colValueBeanList.add(new ColumnValueBean("CSV_TEMPLATE", new StringReader(csvFileData)));
		}
		else if (BulkOperationConstants.MYSQL_DATABASE.equalsIgnoreCase(dataBaseType))
		{
			query = "insert into catissue_bulk_operation (OPERATION, "
					+ "CSV_TEMPLATE, XML_TEMPALTE, DROPDOWN_NAME ) values (?, ?, ?, ?)";

			colValueBeanList.add(new ColumnValueBean("OPERATION", operationName));
			colValueBeanList.add(new ColumnValueBean("CSV_TEMPLATE",
					getCSVTemplateFileData(csvFile)));
			colValueBeanList.add(new ColumnValueBean("XML_TEMPALTE",
					getXMLTemplateFileData(xmlFile)));
			colValueBeanList.add(new ColumnValueBean("DROPDOWN_NAME", dropdownName));
		}

		values.add(colValueBeanList);
		jdbcdao.executeUpdate(query, values);
		jdbcdao.commit();
		logger.info("Data updated successfully.");
	}
}
