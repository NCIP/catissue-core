/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

import au.com.bytecode.opencsv.CSVReader;
import edu.wustl.common.exception.ErrorKey;


public class DataReader
{
	public Properties dataReaderProperties = null; 
	public DataReader(Properties properties)
	{
		dataReaderProperties = properties;
	}
	public static DataReader getNewDataReaderInstance(Properties properties)
	{
		DataReader dataReader = new DataReader(properties);
		return dataReader;
	}
	
	public DataList readData() throws BulkOperationException
	{
		DataList dataList = new DataList();		
		CSVReader reader = null;
 		List<String[]> list = null;
 		InputStream inputStream = (InputStream)dataReaderProperties.get("inputStream");
		try
		{
			reader = new CSVReader(new InputStreamReader(inputStream));
			list = reader.readAll();
			reader.close();		
			int size = list.size();
			if(size>0)
			{
				String[] headers = list.get(0);				
				for(int i=0;i<headers.length;i++)
				{
					dataList.addHeader(headers[i].trim());
				}
				dataList.addHeader(BulkOperationConstants.STATUS);
				dataList.addHeader(BulkOperationConstants.MESSAGE);
				dataList.addHeader(BulkOperationConstants.MAIN_OBJECT_ID);
			}
			if(size > 1)
			{	
				for(int i = 1; i < list.size(); i++)
				{
					String[] newValues = new String[list.get(0).length + 3];
					for(int m = 0; m < newValues.length; m++)
					{
						newValues[m] = new String();
					}
					String[] oldValues = list.get(i);
					for(int j = 0; j < oldValues.length; j++)
					{
						newValues[j] = oldValues[j]; 
					}
					dataList.addNewValue(newValues);
				}
			}
			else if(size > 0)
			{
				String[] values = new String[list.get(0).length + 3];
				for(int i = 0; i < (list.get(0).length + 3); i++)
				{
					values[i] = "";
				}
				for(int i = 0;i < (list.get(0).length + 3); i++)
				{					 
					dataList.addNewValue(values);
				}
			}
		}
		catch (FileNotFoundException fnfExpp)
		{
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.error.csv.file.not.found");
			throw new BulkOperationException(errorkey, fnfExpp, "");
		}
		catch (IOException ioExpp)
		{
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.error.csv.file.reading");
			throw new BulkOperationException(errorkey, ioExpp, "");
		}
		return dataList;
	}
}