
package edu.wustl.catissuecore.caties.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import edu.wustl.catissuecore.bizlogic.ReportLoaderQueueBizLogic;
import edu.wustl.catissuecore.bizlogic.SiteBizLogic;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.catissuecore.reportloader.HL7ParserUtil;
import edu.wustl.catissuecore.reportloader.ReportLoaderUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;

/**
 * @author
 *
 */
public class Utility
{
	/**
	 * Logger instance.
	 */
	private final static transient Logger logger = Logger.getCommonLogger(Utility.class);

	/**
	 * Generic Initialization process for caTIES servers.
	 * @throws Exception : Exception
	 */
	public static void init() throws Exception
	{
		// Initialization methods
		final String appHome = System.getProperty("user.dir");

		// Configuring logger properties
		LoggerConfig.configureLogger(appHome);

		// initializing caties property configurator
		CaTIESProperties.initBundle("caTIES");
	}

	/**
	 * @param configFileName : configFileName
	 * @return Map
	 * @throws Exception : Exception
	 */
	public static Map initializeReportSectionHeaderMap(String configFileName) throws Exception
	{
		final HashMap<String, String> abbrToHeader = new LinkedHashMap<String, String>();
		logger.info("Initializing section header map");
		// Function call to set up section header configuration from SectionHeaderConfig.txt file
		try
		{
			// set bufferedReader to read file
			final BufferedReader bufferedReader = new BufferedReader(new FileReader(configFileName));

			String line = null;
			StringTokenizer stringTokenizer;
			String name;
			String abbr;
			// iterate while file EOF
			while ((line = bufferedReader.readLine()) != null)
			{
				// seperate values for section header name,
				//abbreviation of section header and its priority
				stringTokenizer = new StringTokenizer(line, "|");
				name = stringTokenizer.nextToken().trim();
				abbr = stringTokenizer.nextToken().trim();
				stringTokenizer.nextToken().trim();

				// add abbreviation to section header maping in hash map
				abbrToHeader.put(abbr, name);
			}
			bufferedReader.close();
			logger.info("Section Headers set successfully to the map");
		}
		catch (final IOException ex)
		{
			Utility.logger.error("Error in setting Section header Priorities"+ex.getMessage(), ex);
			throw new Exception(ex.getMessage());
		}
		return abbrToHeader;
	}

	/**
	 * This method takes XML document as an input and convert.
	 * into pain text format according to its formatting style
	 * @param doc Document in XML format
	 * @param format formatted style of text
	 * @return plain text form of XML document
	 * @throws Exception exception occured while converting XML content into equivalent plain text format
	 */

	public static String convertDocumentToString(final org.jdom.Document doc, final Format format)
			throws Exception
	{
		String result = "";
		// instnatiate XMLOutputter
		final XMLOutputter outputDocument = new XMLOutputter();
		if (format != null)
		{
			// set format to XMLOutputter
			outputDocument.setFormat(format);
		}
		// instantiate ByteArrayOutputStream
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		// convert doc to byte array
		outputDocument.output(doc, byteArrayOutputStream);
		// convert byte array to string
		result = byteArrayOutputStream.toString();

		return result;
	}

	/**
	 * This method is responsible to extract only report content that contains report section text.
	 * @param deIDResponse deidentified text from native call
	 * @param dtdFilename local dtd filename for deidentified text in XML format
	 * @return returns only report text content
	 * @param reportTextTagName : reportTextTagName
	 * @param xPath : xPath
	 * @throws Exception throws exception occured while etracting report content
	 */
	public static String extractReport(String deIDResponse, final String dtdFilename,
			final String xPath, final String reportTextTagName) throws Exception
	{
		String deidSprText = "";
		try
		{
			int stringSize = deIDResponse.trim().length();
			if (deIDResponse != null && stringSize > 0)
			{
				final SAXBuilder builder = new SAXBuilder();
				builder.setEntityResolver(new EntityResolver()
				{
					public InputSource resolveEntity(String publicId, String systemId)
					{
						return new InputSource(dtdFilename);
					}
				});
				builder.setFeature("http://apache.org/xml/features/validation/schema", true);
				builder.setFeature("http://xml.org/sax/features/namespaces", true);
				final byte[] byteArray = deIDResponse.getBytes();
				final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
						byteArray);
				final Document deIDResponseDocument = builder.build(byteArrayInputStream);
				final XPath xpath = XPath.newInstance(xPath);
				final List deIdResults = xpath.selectNodes(deIDResponseDocument);
				final Iterator deIdIterator = deIdResults.iterator();
				while (deIdIterator.hasNext())
				{
					final Element deIdReportElement = (Element) deIdIterator.next();
					deidSprText = deIdReportElement.getChild(reportTextTagName).getText();
				}
			}
			else
			{
				Utility.logger.info("NO DeID response");
			}
		}
		catch (final JDOMException ex)
		{
			Utility.logger.error("Failed parsing response \n"
					+ deIDResponse + "\n\n\n"+ex.getMessage(), ex);
			throw ex;
		}
		catch (final Exception ex)
		{
			Utility.logger.error("Failed parsing response \n"
					+ deIDResponse + "\n\n\n"+ex.getMessage(), ex);
			throw ex;
		}
		return deidSprText;
	}

	/**
	 * To retrive the reportLoaderQueueObject.
	 * @param reportQueueId : reportQueueId
	 * @return ReportLoaderQueue
	 * @throws BizLogicException BizLogic Exception
	 * @throws NumberFormatException : NumberFormatException
	 */
	public static ReportLoaderQueue getReportQueueObject(String reportQueueId)
			throws NumberFormatException, BizLogicException
	{

		ReportLoaderQueue reportLoaderQueue = null;
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final ReportLoaderQueueBizLogic reportLoaderQueueBizLogic = (ReportLoaderQueueBizLogic) factory
				.getBizLogic(ReportLoaderQueue.class.getName());
		final Object object = reportLoaderQueueBizLogic.retrieve(ReportLoaderQueue.class.getName(),
				Long.valueOf(reportQueueId));
		if (object != null)
		{
			reportLoaderQueue = (ReportLoaderQueue) object;
		}
		return reportLoaderQueue;
	}

	/**
	 * To retrieve the participant present in the report.
	 * @param reportQueueId : reportQueueId
	 * @return Participant
	 * @throws Exception : Exception
	 */
	public static Participant getParticipantFromReportLoaderQueue(String reportQueueId)
			throws Exception
	{
		Participant participant = null;

		Site site = null;
		ReportLoaderQueue reportLoaderQueue = null;
		reportLoaderQueue = getReportQueueObject(reportQueueId);

		//retrieve site
		final String siteName = reportLoaderQueue.getSiteName();
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final SiteBizLogic siteBizLogic = (SiteBizLogic) factory.getBizLogic(Site.class.getName());
		final List siteList = siteBizLogic.retrieve(Site.class.getName(), Constants.SYSTEM_NAME,
				siteName);

		if ((siteList != null) && !siteList.isEmpty())
		{
			site = (Site) siteList.get(0);
		}

		//retrive the PID
		final String pidLine = ReportLoaderUtil.getLineFromReport(
				reportLoaderQueue.getReportText(), CaTIESConstants.PID);

		//Participant Object
		participant = HL7ParserUtil.parserParticipantInformation(pidLine, site);

		return participant;
	}
}