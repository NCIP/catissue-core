package edu.wustl.common.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
/**
 * This class for creating a file with a given list of data.
 * It creates the file according to delimeter specified.
 * For eg: if comma is the delimter specified then a CSV file is created. 
 * @author Poornima Govindrao
 *  
 */

public class ExportReport 
{
	private BufferedWriter temp;
	public ExportReport(String fileName) throws IOException
	{
		temp =  new BufferedWriter(new FileWriter(fileName));
	}
	public void writeData(List values,String delimiter) throws IOException
	{
		//Writes the list of data into file 
		String newLine = System.getProperty(("line.separator"));
		Iterator itr = values.iterator();
		while(itr.hasNext())
		{
			List rowValues = (List)itr.next();
			Iterator rowItr = rowValues.iterator();
			while(rowItr.hasNext())
			{
				String data = rowItr.next()+ delimiter;
				temp.write(data);
			}
			temp.write(newLine);
		}
	}
	public void closeFile() throws IOException
	{
		temp.close();
	}
}