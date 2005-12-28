package edu.wustl.common.util.global;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ResourceBundle;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

public class CommonFileReader {

	/**
	 * @param args
	 */
	public String readData(String fileName)
	{
		StringBuffer buffer = new StringBuffer();
		try
		{

			String file = Variables.catissueHome +System.getProperty("file.separator") + fileName;
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			while((line = br.readLine())!= null)
			{
				//buffer.append(line+System.getProperty("line.separator"));
				buffer.append(line+"<br>");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return buffer.toString();
	}
	
	public String getFileName(String key)
	{
		ResourceBundle myResources =ResourceBundle.getBundle("ApplicationResources");
		String fileName = myResources.getString(key);
		return fileName;
		
	}

}
