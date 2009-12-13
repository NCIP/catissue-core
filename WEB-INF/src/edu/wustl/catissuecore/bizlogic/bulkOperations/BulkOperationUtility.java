package edu.wustl.catissuecore.bizlogic.bulkOperations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import au.com.bytecode.opencsv.CSVReader;

import edu.wustl.bulkoperator.metadata.BulkOperationMetaData;
import edu.wustl.bulkoperator.metadata.BulkOperationMetadataUtil;
import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * Bulk Operations from UI Utility.
 * @author sagar_baldwa
 *
 */
public class BulkOperationUtility
{
	/**
	 * Get CatissueInstallProperties.
	 * @return Properties.
	 */
	public static Properties getCatissueInstallProperties()
	{
		Properties props = new Properties();
		try
		{
			FileInputStream propFile = new FileInputStream(
					Constants.CATISSUE_INTSALL_PROPERTIES_FILE);
			props.load(propFile);
		}
		catch (FileNotFoundException fnfException)
		{
			fnfException.printStackTrace();
		}
		catch (IOException ioException)
		{
			ioException.printStackTrace();
		}
		return props;
	}

	/**
	 * Get Database Type.
	 * @return String.
	 */
	public static String getDatabaseType()
	{
		Properties properties = getCatissueInstallProperties();
		return properties.getProperty("database.type");
	}
	/**
	 * Get Bulk Operation Meta Data Object.
	 * @param xmlFile String.
	 * @return BulkOperationMetaData BulkOperationMetaData.
	 */
	public static BulkOperationMetaData getBulkOperationMetaDataObject(String xmlFile)
		throws Exception
	{
		BulkOperationMetadataUtil unMarshaller = new BulkOperationMetadataUtil();
		return unMarshaller.unmarshall(xmlFile, "./catissuecore-properties/mapping.xml");
	}
	/**
	 * Get CSV Template Column Names.
	 * @param csvFile String.
	 * @return List of String[].
	 * @throws Exception Exception.
	 */
	public static List<String[]> getCSVTemplateColumnNames(String csvFilePath)
		throws Exception
	{
		CSVReader csvReader = null;
 		List<String[]> csvDataList = null;
		try
		{
			csvReader = new CSVReader(new FileReader(csvFilePath));
			csvDataList = csvReader.readAll();
		}
		catch (FileNotFoundException fnfExp)
		{
			throw new BulkOperationException("\nCSV File Not Found at the specified path.", fnfExp);
		}
		catch (IOException ioExp)
		{
			throw new BulkOperationException("\nError in reading the CSV File.", ioExp);
		}
		finally
		{
			csvReader.close();
		}
		return csvDataList;
	}
}