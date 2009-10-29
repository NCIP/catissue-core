
package edu.wustl.catissuecore.deidentifier.deid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.jdom.Element;
import org.jdom.output.Format;

import com.deid.JniDeID;

import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.CaTIESProperties;
import edu.wustl.catissuecore.caties.util.Utility;
import edu.wustl.catissuecore.deidentifier.AbstractDeidentifier;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.TextContent;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;

/**
 * @author
 *
 */
public class DeIDDeidentifier extends AbstractDeidentifier
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(DeIDDeidentifier.class);
	/**
	 * configFileName.
	 */
	protected static String configFileName;
	/**
	 * deid.
	 */
	protected static JniDeID deid;
	/**
	 * pathToConfigFiles.
	 */
	private static String pathToConfigFiles;

	/**
	 * @throws Exception : Exception
	 */
	@Override
	public void initialize() throws Exception
	{
		// To store path of the directory for the config files, here
		// 'catissue-properties' directory
		pathToConfigFiles = new String(CommonServiceLocator.getInstance().getAppHome()
				+ System.getProperty("file.separator") + "caTIES_conf"
				+ System.getProperty("file.separator"));

		// Function call to store name of config file name required for
		// de-identification native call, deid.cfg
		this.setConfigFileName();
		// Instantiates wrapper class for deid native call
		deid = new JniDeID();
		// set path of the directionary that is required by native call for
		// deidentification
		deid
				.setDictionaryLocation(CaTIESProperties
						.getValue(CaTIESConstants.DEID_DCTIONARY_FOLDER));

		this.logger.info("Loading deid library");
		// load deidLibrary required for native call
		JniDeID.loadDeidLibrary();
	}

	/**
	 * shutdown.
	 */
	@Override
	public void shutdown()
	{
		this.logger.info("Unloading deid library");
		// unload deid library
		JniDeID.unloadDeidLibrary();
	}

	/**
	 * @param identifiedReport : identifiedReport
	 * @throws Exception : Exception
	 * @return DeidentifiedSurgicalPathologyReport
	 */
	@Override
	public DeidentifiedSurgicalPathologyReport deidentify(
			IdentifiedSurgicalPathologyReport identifiedReport) throws Exception
	{
		final DeidentifiedSurgicalPathologyReport deidentifiedReport = this
				.getDeidentifiedReport(identifiedReport);
		return deidentifiedReport;
	}

	/**
	 * This method temporarily saves the name of deid config file name. This
	 * deid config file contains the keywords which will be the input for
	 * deidentification
	 * @throws Exception
	 *             Generic exception
	 */
	protected void setConfigFileName() throws Exception
	{
		// get file name of config file name required for deidentification
		final String cfgFileName = new String(pathToConfigFiles
				+ CaTIESProperties.getValue(CaTIESConstants.DEID_CONFIG_FILE_NAME));
		// create handle to file
		final File cfgFile = new File(cfgFileName);
		// set configFileName to the path of config file
		configFileName = cfgFile.getAbsolutePath();
		this.logger.info("Config file name is " + configFileName);
	}

	/**
	 * This is default run method of the thread. Which is like a deid pipeline.
	 * This pipeline manages the de-identification process.
	 * @param identifiedReport : identifiedReport
	 * @return DeidentifiedSurgicalPathologyReport
	 * @throws Exception : Exception
	 * @see java.lang.Thread#run()
	 */
	public DeidentifiedSurgicalPathologyReport getDeidentifiedReport(
			IdentifiedSurgicalPathologyReport identifiedReport) throws Exception
	{
		this.logger.info("De-identification process started for "
				+ identifiedReport.getId().toString());
		// instantiate document
		final org.jdom.Document currentRequestDocument = new org.jdom.Document(new Element(
				"Dataset"));
		final Participant participant = identifiedReport.getSpecimenCollectionGroup()
				.getCollectionProtocolRegistration().getParticipant();

		String deidText = "";
		// build report element using report text
		final Element reportElement = DeidUtils.buildReportElement(participant, identifiedReport,
				identifiedReport.getTextContent().getData());
		// add report element to root of the document
		currentRequestDocument.getRootElement().addContent(reportElement);

		// convert document into string
		final String deidRequest = Utility.convertDocumentToString(currentRequestDocument, Format
				.getPrettyFormat());

		String deidReportText = null;

		this.logger.info("Calling native call for report " + identifiedReport.getId().toString());
		// function call which contains the actual native call for
		// deidentification
		deidReportText = this.deIdentify(deidRequest);
		this.logger.info("Calling native call finished successfully for report "
				+ identifiedReport.getId().toString());
		this.logger
				.info("Extracting report text for report " + identifiedReport.getId().toString());
		// extract the report text
		deidText = Utility.extractReport(deidReportText, CaTIESProperties
				.getValue(CaTIESConstants.DEID_DTD_FILENAME), CaTIESConstants.DEID_XPATH,
				CaTIESConstants.DEID_REPORT_TEXT_TAG_NAME);
		this.logger.info("Extracting report text finished for report "
				+ identifiedReport.getId().toString());

		deidText = deidText.substring(0, deidText.lastIndexOf("||-"));
		this.logger.info("Creating deidentified report for identified report id="
				+ identifiedReport.getId().toString());
		// Create object of deidentified report
		final DeidentifiedSurgicalPathologyReport deidentifiedReport = this
				.createDeidPathologyReport(identifiedReport, deidText);
		this.logger.info("De-identification process finished for "
				+ identifiedReport.getId().toString());

		return deidentifiedReport;
	}

	/**
	 * Method to create and initialize object of.
	 * DeidentifiedSurgicalPathologyReport
	 * @param ispr
	 *            identified surgical pathology report
	 * @param deidText
	 *            de-intified text
	 * @return DeidentifiedSurgicalPathologyReport
	 * @throws Exception
	 *             a generic exception oocured while creating de-identified
	 *             report instance.
	 */
	private DeidentifiedSurgicalPathologyReport createDeidPathologyReport(
			IdentifiedSurgicalPathologyReport ispr, String deidText) throws Exception
	{
		this.logger.info("Creating deid report for identifiedReport id=" + ispr.getId());
		// instnatiate deidentified report
		final DeidentifiedSurgicalPathologyReport deidReport = new DeidentifiedSurgicalPathologyReport();

		final TextContent tc = new TextContent();
		// set deidentified text to text content
		tc.setData(deidText);
		// set identified report to deidentified report
		tc.setSurgicalPathologyReport(deidReport);

		// set text content which contains deidentified text to deidentified
		// report
		deidReport.setTextContent(tc);
		// set default value for flag for review
		deidReport.setIsFlagForReview(new Boolean(false));

		return deidReport;
	}

	/**
	 * This method is responsible for preparing and calling a native method call
	 * to convert plain text into deindentified text.
	 * @param text
	 *            text to be de-identified
	 * @return de-identified text
	 * @throws Exception
	 *             ocured while calling a native method call for
	 *             de-identification
	 */
	public String deIdentify(String text) throws Exception
	{
		String output = "";
		try
		{
			synchronized (this)
			{
				// file for xml formated input text
				final File predeidFile = new File("predeid.xml");
				// temp file for processing input file
				final File postdeidFile = new File("postdeid.tmp");

				final FileWriter fw = new FileWriter(predeidFile);
				// write contents to input xml file
				fw.write(text);
				fw.close();
				this.logger.info("Calling native call");
				deid.createDeidentifier(predeidFile.getAbsolutePath(), postdeidFile
						.getAbsolutePath()
						+ "?XML", configFileName);
				this.logger.info("Native call success");
				final BufferedReader br = new BufferedReader(new FileReader(postdeidFile));

				// read all contents from output file
				String line = "";
				while ((line = br.readLine()) != null)
				{
					// add content to output string
					output += line + "\n";
				}
				this.logger.info("Deleting temp files");
				br.close();
				// delete temporary input and output files
				predeidFile.delete();
				postdeidFile.delete();
				// notify on object to process next waiting call
				this.notifyAll();
			}
		}
		catch (final IOException ex)
		{
			this.logger.error("File system error occured while creating"
					+ " or deleting temporary files " +
					"for deidentification"+ex.getMessage(), ex);
			ex.printStackTrace();
			throw ex;
		}
		catch (final Exception ex)
		{
			this.logger.error(
					"Severe error occured in the " +
					"native method call for" +
					" deidentification"+ex.getMessage(), ex);
			ex.printStackTrace();
			throw ex;
		}
		return output;
	}
}
