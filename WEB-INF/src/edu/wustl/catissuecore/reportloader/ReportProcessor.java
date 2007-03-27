package edu.wustl.catissuecore.reportloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.logger.Logger;

/**
 * @author sandeep_ranade
 * Represents a procesor which picks up the report files and then pass them to the 
 * appropriate parser which parsers those files and import the data into datastore.  
 */
public class ReportProcessor implements Observer
{
	
	/**
	 * Represents parser manager 
	 */
	protected ParserManager parserManager=null;
	
	/**
	 * Represents file parser 
	 */
	protected Parser parser=null;
	
	/**
	 * Constructor
	 */
	public ReportProcessor()
	{
		parserManager = ParserManager.getInstance();
		// get instance of parser
		parser = parserManager.getParser(Parser.HL7_PARSER);
		ReportLoaderQueueProcessor queueProcessor = new ReportLoaderQueueProcessor();
		// Starts ReportLoaderQueueProcessor thread
		queueProcessor.start();
	}
	
	/**
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
		File inputDir=null;
		List fileToDelete=null;
		try
		{	
			files=	(String[])obj;
			// variable to store list of files that has to be deleted from input directory after processing
			fileToDelete = new ArrayList();
			// Loop to process all incoming files
			for(int i=0;i<files.length;i++)
			{	
				try
				{		
					if(isValidFile(XMLPropertyHandler.getValue(Parser.INPUT_DIR)+File.separator+files[i]))
					{
						Logger.out.info("parsing file "+files[i]);
						// Initializing SiteInfoHandler to avoid restart of server to get new site names added to file at run time
						SiteInfoHandler.init(XMLPropertyHandler.getValue("site.info.filename"));
						// calling parser to parse file
						parser.parse(XMLPropertyHandler.getValue(Parser.INPUT_DIR)+File.separator+files[i]);
						Logger.out.info("parsing of file "+files[i]+" finished");
					}
					else
					{
						Logger.out.info("Bad file found. Moving file to bad files directory. Filename:"+XMLPropertyHandler.getValue(Parser.INPUT_DIR)+File.separator+files[i]);
						CSVLogger.info(Parser.LOGGER_FILE_POLLER,"Bad file found "+XMLPropertyHandler.getValue(Parser.INPUT_DIR)+File.separator+files[i]);
						fileToDelete.add(XMLPropertyHandler.getValue(Parser.INPUT_DIR)+File.separator+files[i]);
						copyFile(XMLPropertyHandler.getValue(Parser.INPUT_DIR)+File.separator+files[i],XMLPropertyHandler.getValue(Parser.BAD_FILE_DIR)+"/"+files[i]);
					}
					
				}
				catch(IOException ex)
				{
					Logger.out.error("Error while reading file ",ex);
				}
				catch(Exception ex)
				{     
			  	    copyFile("./"+files[i],XMLPropertyHandler.getValue(Parser.BAD_FILE_DIR)+File.separator+files[i]);
			  	    fileToDelete.add(XMLPropertyHandler.getValue(Parser.INPUT_DIR)+File.separator+files[i]);
					Logger.out.error("Bad File ",ex);
				}
				fileToDelete.add(XMLPropertyHandler.getValue(Parser.INPUT_DIR)+File.separator+files[i]);
				copyFile(XMLPropertyHandler.getValue(Parser.INPUT_DIR)+File.separator+files[i],XMLPropertyHandler.getValue(Parser.PROCESSED_FILE_DIR)+"/"+files[i]);
			}
			files=null;
			deleteFiles(fileToDelete);
				
		}
		catch(Exception ex)
		{     
	  		Logger.out.error("Error while initializing parser manager ",ex);
		}	
	}
	
	/**
	 * Copy sourceFile to destinationFile location
	 * @param sourceFile Source file name
	 * @param destinationFile desinamtion file name
	 * @throws Exception generic exception
	 * 
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
	 * Delete files
	 * @param list list of files to delete
	 * @throws Exception generic exception
	 */
	private static void deleteFiles(List list)throws Exception
	{
		
		File tempFile= null;
		boolean del=false;
		for(int i=0;i<list.size();i++)
		{
			tempFile=new File((String)list.get(i));
			if(tempFile.exists())
			{
				 del=tempFile.delete();
			}
		}
	}	
	
	/**
	 * This is the function to validate the input file 
	 * @param fileName nae of the input file
	 * @return boolean result of validation
	 */
	public boolean isValidFile(String fileName)
	{
		if(fileName.endsWith(Parser.INPUT_FILE_EXTENSION))
		{
			return true;
		}
		return false;
	}
}
