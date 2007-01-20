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
		parser = parserManager.getParser(Parser.HL7_PARSER);
		ReportLoaderQueueProcessor queueProcessor = new ReportLoaderQueueProcessor();
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
				fileToDelete = new ArrayList();
				for(int i=0;i<files.length;i++)
				{	
					try
					{
						parser.parse(XMLPropertyHandler.getValue(Parser.INPUT_DIR)+File.separator+files[i]);
						
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
	 * @param sourceFile
	 * @param destinationFile
	 * @throws Exception
	 * Copy sourceFile to destinationFile location
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
	 * @param list
	 * @throws Exception
	 * Delete files
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
	
	
	
}
