package edu.wustl.catissuecore.caties.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.PropertyConfigurator;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.ReportLoaderQueueBizLogic;
import edu.wustl.catissuecore.bizlogic.SiteBizLogic;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.catissuecore.reportloader.HL7ParserUtil;
import edu.wustl.catissuecore.reportloader.ReportLoaderUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

public class Utility 
{
	/**
	 * Generic Initialization process for caTIES servers
	 * @throws Exception
	 */
	public static void init()throws Exception
	{
		// Initialization methods
		Variables.applicationHome = System.getProperty("user.dir");
		//Logger.out = org.apache.log4j.Logger.getLogger("");
		// Configuring common logger
		Logger.configure(CaTIESConstants.LOGGER_GENERAL);
		// Configuring logger properties
		PropertyConfigurator.configure(Variables.applicationHome + File.separator+"logger.properties");
		// initializing caties property configurator
		CaTIESProperties.initBundle("caTIES");
	}
	
	
	public static Map initializeReportSectionHeaderMap(String configFileName) throws Exception
	{
		HashMap<String,String> abbrToHeader = new LinkedHashMap <String,String>();
		Logger.out.info("Initializing section header map");
		// Function call to set up section header configuration from SectionHeaderConfig.txt file
		try 
		{
			// set bufferedReader to read file
			BufferedReader br = new BufferedReader(new FileReader(configFileName));

			String line = null;
			StringTokenizer st;
			String name;
			String abbr;
			String priority;
			// iterate while file EOF
			while ((line = br.readLine()) != null) 
			{
				// sepearete values for section header name, abbreviation of section header and its priority
				st = new StringTokenizer(line, "|");
				name = st.nextToken().trim();
				abbr = st.nextToken().trim();
				priority = st.nextToken().trim();
				
				// add abbreviation to section header maping in hash map
				abbrToHeader.put(abbr, name);
			}
			Logger.out.info("Section Headers set successfully to the map");
		}
		catch (IOException ex) 
		{
			Logger.out.error("Error in setting Section header Priorities",ex);
			throw new Exception(ex.getMessage());
		}
		return abbrToHeader;
	}
	
	
	/**
	 * This method takes XML document as an input and convert into pain text format according to its formatting style
	 * @param doc Document in XML format
	 * @param format formatted style of text
	 * @return plain text form of XML document
	 * @throws Exception exception occured while converting XML content into equivalent plain text format
	 */
	
	public static String convertDocumentToString(final org.jdom.Document doc, final  Format format) throws Exception
	{
		String result = "" ;
		// instnatiate XMLOutputter 
		XMLOutputter outputDocument = new XMLOutputter() ;
		if (format != null) 
		{
			// set format to XMLOutputter
			outputDocument.setFormat(format) ;
		}
		// instantiate ByteArrayOutputStream 
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream() ;
		// convert doc to byte array
		outputDocument.output(doc, byteArrayOutputStream) ;
		// convert byte array to string
		result = byteArrayOutputStream.toString() ;
		
		return result ;
	}
	
	/**
	 * This method is responsible to extract only report content that contains report section text.
	 * @param deIDResponse deidentified text from native call
	 * @param dtdFilename local dtd filename for deidentified text in XML format
	 * @return returns only report text content
	 * @throws Exception throws exception occured while etracting report content
	 */
	public static String extractReport(String deIDResponse , final String dtdFilename, final String xPath, final String reportTextTagName) throws Exception
	{

        String deidSprText = "";
        try 
        {
        	// check for valid deid responce text
            if (deIDResponse != null && deIDResponse.trim().length() > 0)
            {
            	// instantiate SAXBuilder
                SAXBuilder builder = new SAXBuilder();
                // set EntityResolver to use local dtd file instead of the one that is specified in the xml document
                builder.setEntityResolver(new EntityResolver()
                {
                    public InputSource resolveEntity(String publicId, String systemId)
                    {
                    	// local dtd file name that has to be used
						return new InputSource(dtdFilename);
                    }
                });
   
                // set default feature values to sax builder
                builder.setFeature("http://apache.org/xml/features/validation/schema",true);
                builder.setFeature("http://xml.org/sax/features/namespaces",true);

                // convert string to byte array
                byte[] byteArray = deIDResponse.getBytes();
                // convert byte array to ByteArrayInputStream
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
                // create document using ByteArrayInputStream
                Document deIDResponseDocument = builder.build(byteArrayInputStream);
 
                // set XPath to query on XML document
                XPath xpath = XPath.newInstance(xPath);
                // fire query on the document
                List deIdResults = xpath.selectNodes(deIDResponseDocument);
                Iterator deIdIterator = deIdResults.iterator();
                // iterate to extract report text
                while (deIdIterator.hasNext()) 
                {
                	// get next element
                    Element deIdReportElement = (Element) deIdIterator.next();
                    // get report text
                    deidSprText = deIdReportElement.getChild(reportTextTagName).getText();
                }
            } 
            else 
            {
               Logger.out.info("NO DeID response");
            }
        } 
        catch (JDOMException ex) 
        {
        	Logger.out.error("Failed parsing response \n"+deIDResponse+"\n\n\n", ex);
        	throw ex;
        }
        catch(Exception ex)
        {
        	Logger.out.error("Failed parsing response \n"+deIDResponse+"\n\n\n", ex);
        	throw ex;
        }
        return deidSprText;
    }
	
	/**
	 * To retrive the reportLoaderQueueObject
	 * @param reportQueueId
	 * @return
	 * @throws DAOException
	 */
	public static ReportLoaderQueue getReportQueueObject(String reportQueueId) throws DAOException
	{
		
		ReportLoaderQueue reportLoaderQueue =null;
		ReportLoaderQueueBizLogic reportLoaderQueueBizLogic = (ReportLoaderQueueBizLogic)BizLogicFactory.getInstance().getBizLogic(ReportLoaderQueue.class.getName());
		Object object = reportLoaderQueueBizLogic.retrieve(ReportLoaderQueue.class.getName(), new Long(reportQueueId));
	    if(object != null)
		{
			reportLoaderQueue = (ReportLoaderQueue) object;
		}
	    
	    
	    return reportLoaderQueue;		
	}
	
	/**
	 * To retrieve the participant present in the report
	 * @param reportQueueId
	 * @return
	 * @throws Exception
	 * 
	 */
	public static Participant getParticipantFromReportLoaderQueue(String reportQueueId) throws Exception
	{
		Participant participant = null;
	
		Site site =null;
		ReportLoaderQueue reportLoaderQueue =null;
		reportLoaderQueue = getReportQueueObject(reportQueueId);

		//retrieve site
		String siteName = reportLoaderQueue.getSiteName();
		SiteBizLogic siteBizLogic = (SiteBizLogic)BizLogicFactory.getInstance().getBizLogic(Site.class.getName());
		List siteList = (List)siteBizLogic.retrieve(Site.class.getName(),Constants.SYSTEM_NAME, siteName);
		
		
		if((siteList!=null) && siteList.size()>0)
		{
			site = (Site)siteList.get(0);
		}
		
		
		//retrive the PID		
		String pidLine = ReportLoaderUtil.getLineFromReport(reportLoaderQueue.getReportText(), CaTIESConstants.PID);
		
		//Participant Object		
		 participant = HL7ParserUtil.parserParticipantInformation(pidLine,site);
			 
		return participant;
	}

}
