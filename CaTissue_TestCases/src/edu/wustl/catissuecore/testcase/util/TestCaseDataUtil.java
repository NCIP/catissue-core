package edu.wustl.catissuecore.testcase.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.common.exception.ErrorKey;
import gate.annotation.TestDatabaseAnnotationSet;

import junit.framework.TestResult;

public class TestCaseDataUtil
{
	private static Map<String, DataObject> dataMap = new HashMap<String,DataObject>();

	public static void setDataObject(DataObject dataObject)
	{

		dataMap.put(dataObject.getName(), dataObject);
	}

	public static DataObject getDataObject(String key)
	{
		return dataMap.get(key);
	}

	public static void writeToFile(TestResult result,String name,String err)
	{
		File file = null;
		FileWriter writer = null;
		try
		{
			file = new File("./CaTissue_TestCases/Detailed_Report.csv");
			writer = new FileWriter(file,true);
			int valueListSize = 8;
			StringBuffer line = new StringBuffer();
//			for(int i=0;i<valueListSize;i++)
//			{
				line.setLength(0);
//				DataObject dataObject = getDataObject(name);
//				for(int i=0;i<valueListSize;i++)
//				{
				String testResult;
				if (result.wasSuccessful())
				{
					testResult = "Success";
				}
				else
				{
					testResult = "Failure";
				}
				DataObject obj = dataMap.get(name);
					line.append(obj.getId()+","+name+","+testResult+",,,,,"+err);
//				}
				line.deleteCharAt(line.length()-1);
				line.append("\n");
				writer.append(line.toString());
//			}
		}
		catch (IOException ioExp)
		{

			ioExp.printStackTrace();
		}
		finally
		{
			try
			{
				writer.close();
			}
			catch (IOException exp)
			{
				exp.printStackTrace();
			}
		}
	}
}
