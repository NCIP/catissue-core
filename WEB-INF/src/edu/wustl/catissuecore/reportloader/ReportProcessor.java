
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
 * Represents a processor which picks up the report files and then pass them to the
 * appropriate parser which parsers those files and import the data into data store.
 */
public class ReportProcessor implements Observer
{

	/**
	 * Logger.
	 */
	private transient Logger logger = Logger.getCommonLogger(ReportProcessor.class);
	/**
	 * Represents parser manager.
	 */
	protected ParserManager parserManager = null;

	/**
	 * Represents file parser.
	 */
	protected Parser parser = null;

	/**
	 * Variables for input directory file directory.
	 */
	String inputFileDir = CaTIESProperties.getValue(CaTIESConstants.INPUT_DIR);
	/**
	 * Variables for parsed file directory.
	 */
	String parsedFileDir = CaTIESProperties.getValue(CaTIESConstants.PROCESSED_FILE_DIR);
	/**
	 * Variables for bad file directory.
	 */
	String badFileDir = CaTIESProperties.getValue(CaTIESConstants.BAD_FILE_DIR);

	/**
	 * Constructor.
	 */
	public ReportProcessor()
	{
		this.parserManager = ParserManager.getInstance();
		// get instance of parser
		this.parser = this.parserManager.getParser();
	}

	/**
	 * Method to indicate notify.
	 * @param obj object
	 */
	public void notifyEvent(Object obj)
	{
		this.run(obj);
	}

	/**
	 * This method parses each report file and adds data to queue.
	 * @param obj file object
	 */
	public void run(Object obj)
	{
		String[] files = null;
		final String siteInfoFileName = CaTIESProperties
				.getValue(CaTIESConstants.SITE_INFO_FILENAME);
		try
		{
			files = (String[]) obj;
			// Loop to process all incoming files
			for (final String file : files)
			{
				try
				{
					if (this.isValidFile(this.inputFileDir + File.separator + file))
					{
						this.logger.info("parsing file " + file);
						// Initializing SiteInfoHandler to avoid restart of server to get
						//new site names added to file at run time
						SiteInfoHandler.init(siteInfoFileName);
						// calling parser to parse file
						final Long startTime = new Date().getTime();
						this.parser.parse(this.inputFileDir + File.separator + file);
						final Long endTime = new Date().getTime();
						this.logger.info("parsing of file " + file
								+ " finished. Time required:"
								+ (endTime - startTime));
					}
					else
					{
						this.logger
							.info("Bad file found." +
								" Moving file to bad files directory. Filename:"
							+ this.inputFileDir + File.separator + file);
						CSVLogger.info(CaTIESConstants.LOGGER_FILE_POLLER,
								"Bad file found "
								+ this.inputFileDir + File.separator + file);
						this.moveToBadFileDir(file);
					}
				}
				catch (final IOException ex)
				{
					this.logger.error("Error while reading file ", ex);
				}
				catch (final Exception ex)
				{
					this.moveToBadFileDir(file);
					this.logger.error("Bad File ", ex);
				}
				this.moveToProcessedFileDir(file);
			}
			files = null;
		}
		catch (final Exception ex)
		{
			this.logger.error("Error while initializing parser manager ", ex);
		}
	}

	/**
	 * Copy sourceFile to destinationFile location.
	 * @param sourceFile Source file name
	 * @param destinationFile destination file name
	 * @throws Exception generic exception
	 */
	private static void copyFile(String sourceFile, String destinationFile) throws Exception
	{
		final File tempFile = new File(sourceFile);
		final FileReader fr = new FileReader(tempFile);
		final BufferedReader br = new BufferedReader(fr);
		String line = "";
		final FileWriter fw = new FileWriter(destinationFile);
		while ((line = br.readLine()) != null)
		{
			fw.write(line);
			fw.write("\n");
		}
		fr.close();
		fw.close();
	}

	/**
	 * Function to delete files.
	 * @param filePath file Path
	 * @throws Exception generic exception
	 */
	private static void deleteFile(String filePath) throws Exception
	{

		File tempFile = null;
		tempFile = new File(filePath);
		if (tempFile.exists())
		{
			tempFile.delete();
		}
	}

	/**
	 * Function to validate the input file.
	 * @param fileName name of the input file
	 * @return boolean result of validation
	 */
	public boolean isValidFile(String fileName)
	{
		final String extension = fileName.substring(fileName.lastIndexOf("."));
		if (extension.equalsIgnoreCase(CaTIESConstants.INPUT_FILE_EXTENSION))
		{
			return true;
		}
		return false;
	}

	/**
	 * Method to move file to processed file directory.
	 * @param fileName file Name
	 * @throws Exception generic exception
	 */
	private void moveToProcessedFileDir(String fileName) throws Exception
	{
		copyFile(this.inputFileDir + File.separator + fileName, this.parsedFileDir + File.separator
				+ fileName);
		deleteFile(this.inputFileDir + File.separator + fileName);
	}

	/**
	 * Method to move file to bad file directory.
	 * @param fileName file Name
	 * @throws Exception generic exception
	 */
	private void moveToBadFileDir(String fileName) throws Exception
	{
		copyFile(this.inputFileDir + File.separator + fileName, this.badFileDir + File.separator
				+ fileName);
		deleteFile(this.inputFileDir + File.separator + fileName);
	}
}
