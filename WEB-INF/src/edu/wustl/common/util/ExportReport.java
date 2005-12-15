package edu.wustl.common.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

public class ExportReport 
{
	private BufferedWriter temp;
	public ExportReport(String fileName) throws IOException
	{
		temp =  new BufferedWriter(new FileWriter(fileName));
	}
	public void writeData(List values) throws IOException
	{
		//Writes the list of data into file as CSV 
		String newLine = System.getProperty(("line.separator"));
		Iterator itr = values.iterator();
		while(itr.hasNext())
		{
			List rowValues = (List)itr.next();
			Iterator rowItr = rowValues.iterator();
			while(rowItr.hasNext())
			{
				String data = rowItr.next()+ Constants.TAB_DELIMETER;
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