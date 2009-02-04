import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import java.util.Properties;
import java.io.FileInputStream;


/**
 * <p>This class initializes the fields of ReportWriter.java</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class ReportWriter
{
	
	private BufferedWriter writer = null;
	
	/**
	 * Specify the reportWriter field 
	 */
	private static ReportWriter reportWriter = null;
	
	/**
	 * Specify the fileNamePrefix field 
	 */
	private String fileNamePrefix = "\\build_report_";
	
	/**
	 * Specify the dateFormatSuffix field 
	 */
	private String dateFormatSuffix = "dd_MM_yyyy_HH_mm_ss";
	
	/**
	 * It specifies the db name
	 */
	private String databaseName = "mysql";
	
	/**
	 * @return report writer object
	 */
	public static ReportWriter getInstance()
	{
		if (reportWriter == null)
		{
			reportWriter = new ReportWriter();
		}
		return reportWriter;
	}
	
	/**
	 * @return dir path
	 */
	public String getDirPath()
	{
		String currDir = System.getProperty("user.dir");
		StringBuffer dirPath = new StringBuffer(currDir);
		dirPath.append("\\report");
		System.out.println(dirPath);
		return dirPath.toString();
	}
	
	public String getFileName(String currDirString)
	{
		StringBuffer fileName = new StringBuffer(fileNamePrefix);
		StringBuffer filePath = new StringBuffer(currDirString);
		
		try
		{
			Properties properties = new Properties();
			FileInputStream propertyFile = new FileInputStream("..\\caTissueInstall.properties");
			properties.load(propertyFile);
			System.setProperties(properties);
			databaseName = System.getProperty("database.type");
		}
		catch(Exception e)
		{
			System.out.println(" Error in reading properties " + e.getMessage());
		}
		
		/*Date currDate = new Date();
	    SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatSuffix);
		String dateStr = dateFormat.format(currDate);*/
		
		/*filePath.append(fileName + dbName + "_" + dateStr +".txt");*/
		filePath.append(fileName + databaseName + ".txt");
	
		System.out.println(filePath);
		return filePath.toString();
	}
	
	public void createDir(String dirPath)
	{
		File dir = new File(dirPath);
		
		if (!dir.exists())
		{
			System.out.println("Not exists");
			boolean success = dir.mkdir();
			if (!success)
			{
				System.out.println(" File is not created");
				return;
			}
		}
	}
	
	public void createFile(String filePath)
	{
		try
		{
			System.out.println(filePath);
			writer = new BufferedWriter(new FileWriter(filePath));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void closeFile()
	{
		if (writer != null)
		{
			try
			{
				writer.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void writeToFile(String str)
	{
		try
		{
			//System.out.println(str);
			writer.write(str);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
