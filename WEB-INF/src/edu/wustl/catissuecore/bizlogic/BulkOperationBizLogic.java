package edu.wustl.catissuecore.bizlogic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import au.com.bytecode.opencsv.CSVWriter;
import edu.wustl.bulkoperator.metadata.BulkOperationMetaData;
import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.dao.JDBCDAO;

/**
 * Bulk operation business logic from UI.
 * @author sagar_baldwa
 *
 */
public class BulkOperationBizLogic
{
	/**
	 * Get Template Name from DropDown List.
	 * @return List of NameValueBean.
	 * @throws BulkOperationException BulkOperationException.
	 * @throws ApplicationException ApplicationException.
	 */
	public List<NameValueBean> getTemplateNameDropDownList()
		throws BulkOperationException, ApplicationException
	{
		List<NameValueBean> bulkOperationList = new ArrayList<NameValueBean>();
		JDBCDAO jdbcDao = null;
		try
		{
			jdbcDao = AppUtility.openJDBCSession();
			String query = "select DROPDOWN_NAME from catissue_bulk_operation";
			List list = jdbcDao.executeQuery(query);
			if(!list.isEmpty())
			{
				Iterator iterator = list.iterator();
				while(iterator.hasNext())
				{
					List innerList = (List)iterator.next();
					String innerString = (String)innerList.get(0);
					bulkOperationList.add(new NameValueBean(innerString, innerString));
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new BulkOperationException("Error in retrieving data from" +
					"database for populating dropdown values."); 
		}
		finally
		{
			AppUtility.closeJDBCSession(jdbcDao);
		}
		return bulkOperationList;
	}

	/**
	 * Get CSV File.
	 * @param dropdownName String.
	 * @return File.
	 * @throws BulkOperationException BulkOperationException.
	 * @throws ApplicationException ApplicationException.
	 */
	public File getCSVFile(String dropdownName) throws 
				BulkOperationException, ApplicationException
	{
		File csvFile = null;
		JDBCDAO jdbcDao = null;
		try
		{
			jdbcDao = AppUtility.openJDBCSession();
			String query = "select csv_template from catissue_bulk_operation where " +
					"DROPDOWN_NAME like '" + dropdownName +"'";
			List list = jdbcDao.executeQuery(query);
			if(!list.isEmpty())
			{
				List innerList = (List)list.get(0);
				String commaSeparatedString = (String)innerList.get(0);
				csvFile = writeCSVFile(commaSeparatedString, dropdownName);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			AppUtility.closeJDBCSession(jdbcDao);
		}
		return csvFile;
	}

	/**
	 * Write CSV File.
	 * @param commaSeparatedString String.
	 * @param dropdownName String.
	 * @return File.
	 * @throws Exception Exception.
	 */
	private File writeCSVFile(String commaSeparatedString, String dropdownName)
	throws Exception
	{
		CSVWriter writer = null;
		File csvFile = null;
		try
		{
			String csvFileName = dropdownName + ".csv";
			csvFile = new File(csvFileName);
			csvFile.createNewFile();
			writer = new CSVWriter(new FileWriter(csvFileName), ',');
			String[] stringArray = commaSeparatedString.split(",");
			writer.writeNext(stringArray);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new BulkOperationException(
				"\nError in writing the CSV Template File.");
		}
		finally
		{
			writer.close();
		}
		return csvFile;
	}
	
	/**
	 * Get Operation Name And XML.
	 * @param dropdownName String.
	 * @return List of String.
	 * @throws BulkOperationException BulkOperationException.
	 * @throws ApplicationException ApplicationException.
	 */
	public List<String> getOperationNameAndXml(String dropdownName)
		throws BulkOperationException, ApplicationException
	{
		List<String> returnList = new ArrayList<String>();
		JDBCDAO jdbcDao = null;
		try
		{
			jdbcDao = AppUtility.openJDBCSession();
			String query = "select operation, xml_tempalte from " +
					"catissue_bulk_operation " +
					"where DROPDOWN_NAME like '" + dropdownName + "'";
			List list = jdbcDao.executeQuery(query);
			if(!list.isEmpty())
			{
				List innerList = (List)list.get(0);
				Iterator iterator = innerList.iterator();
				while(iterator.hasNext())
				{
					String innerString = (String)iterator.next();
					returnList.add(innerString);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new BulkOperationException("Error in retrieving operation " +
					"name from drop down name."); 
		}
		finally
		{
			AppUtility.closeJDBCSession(jdbcDao);
		}
		return returnList;
	}
	
	/**
	 * Convert String To XML.
	 * @param xmlString String.
	 * @return BulkOperationMetaData.
	 * @throws BulkOperationException BulkOperationException.
	 */
	public BulkOperationMetaData convertStringToXml(String xmlString)
		throws BulkOperationException
	{
		BulkOperationMetaData bulkOperationMetaData = null;
		try
		{
			InputSource inputSource = new InputSource(new StringReader(xmlString));
			String mappingFilePath = CommonServiceLocator.getInstance().getPropDirPath()
			+ File.separator + "mapping.xml";
			Mapping mapping = new Mapping();
			mapping.loadMapping(mappingFilePath);

			Unmarshaller un = new Unmarshaller(BulkOperationMetaData.class);
			un.setMapping(mapping);

			bulkOperationMetaData = (BulkOperationMetaData) un.unmarshal(inputSource);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new BulkOperationException("bulk.error.loading.bulk.metadata.xml.file");
		}
		return bulkOperationMetaData;
	}
}