package edu.wustl.catissuecore.reportloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import edu.wustl.catissuecore.caties.util.CSVLogger;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.CaTIESProperties;
import edu.wustl.catissuecore.caties.util.SiteInfoHandler;
import edu.wustl.common.util.logger.Logger;

/**
 * Represents a procesor which picks up the report files and then pass them to the 
 * appropriate parser which parsers those files and import the data into datastore.  
 */
public class ReportProcessor implements Observer
{
	private transient Logger logger = Logger.getCommonLogger(ReportProcessor.class);
	/**
	 * Represents parser manager 
	 */
	protected ParserManager parserManager=null;
	
	/**
	 * Represents file parser 
	 */
	protected Parser parser=null;
	
	/**
	 * Variables for input directory, parsed file directory and bad file directory
	 */
	String inputFileDir=CaTIESProperties.getValue(CaTIESConstants.INPUT_DIR);
	String parsedFileDir=CaTIESProperties.getValue(CaTIESConstants.PROCESSED_FILE_DIR);
	String badFileDir=CaTIESProperties.getValue(CaTIESConstants.BAD_FILE_DIR);
	
	/**
	 * Constructor
	 */
	public ReportProcessor()
	{
		parserManager = ParserManager.getInstance();
		// get instance of parser
		parser = parserManager.getParser();
	}
	
	/**
	 * Method to indicate notify 
	 * @param obj object
	 */
	public void notifyEvent(Object obj)
	{
		run(obj);
	}
	
	/**
	 * This method parses each report file and adds data to queue.
	 * @param obj file object
	 */
	
	public void run(Object obj)
	{
		String[] files=null;
		String siteInfoFileName=CaTIESProperties.getValue(CaTIESConstants.SITE_INFO_FILENAME);
		try
		{	
			files=	(String[])obj;
			// Loop to process all incoming files
			for(int i=0;i<files.length;i++)
			{	
				try
				{		
					if(isValidFile(inputFileDir+File.separator+files[i]))
					{
						logger.info("parsing file "+files[i]);
						// Initializing SiteInfoHandler to avoid restart of server to get new site names added to file at run time
						SiteInfoHandler.init(siteInfoFileName);
						// calling parser to parse file
						Long startTime=new Date().getTime();
						parser.parse(inputFileDir+File.separator+files[i]);
						Long endTime=new Date().getTime();
						logger.info("parsing of file "+files[i]+" finished. Time required:"+(endTime-startTime));
					}
					else
					{
						logger.info("Bad file found. Moving file to bad files directory. Filename:"+inputFileDir+File.separator+files[i]);
						CSVLogger.info(CaTIESConstants.LOGGER_FILE_POLLER,"Bad file found "+inputFileDir+File.separator+files[i]);
						moveToBadFileDir(files[i]);
					}					
				}
				catch(IOException ex)
				{
					logger.error("Error while reading file ",ex);
				}
				catch(Exception ex)
				{     
					moveToBadFileDir(files[i]);
					logger.error("Bad File ",ex);
				}
				moveToProcessedFileDir(files[i]);
			}
			files=null;
		}
		catch(Exception ex)
		{     
			logger.error("Error while initializing parser manager ",ex);
		}	
	}
	
	/**
	 * Copy sourceFile to destinationFile location
	 * @param sourceFile Source file name
	 * @param destinationFile desinamtion file name
	 * @throws Exception generic exception
	 */
	private static void copyFile(String sourceFile,String destinationFile)throws Exception
	{
		File tempFile= new File(sourceFile);
		FileReader fr = new FileReader(tempFile);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		FileWriter fw = new FileWriter(destinationFile);
		while ((line = br.readLine()) != null)
		{
			fw.write(line);
			fw.write("\n");
		}	
		fr.close();
		fw.close();
	}
	
	/**
	 * Function to delete files
	 * @param list list of files to delete
	 * @throws Exception generic exception
	 */
	private static void deleteFile(String filePath)throws Exception
	{
		
		File tempFile= null;
		tempFile=new File(filePath);
		if(tempFile.exists())
		{
			 tempFile.delete();
		}
	}	
	
	/**
	 * Function to validate the input file 
	 * @param fileName nae of the input file
	 * @return boolean result of validation
	 */
	public boolean isValidFile(String fileName)
	{
		String extension=fileName.substring(fileName.lastIndexOf("."));
		if(extension.equalsIgnoreCase(CaTIESConstants.INPUT_FILE_EXTENSION))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Method to move file to processed file directory
	 * @param fileName
	 * @throws Exception
	 */
	private void moveToProcessedFileDir(String fileName) throws Exception
	{
		copyFile(inputFileDir+File.separator+fileName,parsedFileDir+File.separator+fileName);
		deleteFile(inputFileDir+File.separator+fileName);
	}
	
	/**
	 * Method to move file to bad file directory
	 * @param fileName
	 * @throws Exception
	 */
	private void moveToBadFileDir(String fileName) throws Exception
	{
		copyFile(inputFileDir+File.separator+fileName,badFileDir+File.separator+fileName);
		deleteFile(inputFileDir+File.separator+fileName);
	}
}
