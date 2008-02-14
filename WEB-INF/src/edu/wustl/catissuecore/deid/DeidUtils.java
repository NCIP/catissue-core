package edu.wustl.catissuecore.deid;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import edu.wustl.catissuecore.caties.util.CaCoreAPIService;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;


/**
 * This class provides various utility methods which helps for the deidentification process
 * @author vijay_pande
 */
public class DeidUtils
{

	/**
	 * This mathod takes the identified report, its participant and converts 
	 * all the information into an appropriate xml document
	 * @param participant participant
	 * @param ispr identified surgical pathology report
	 * @param sprText sysnthesized text
	 * @return Elelment which contains other all the report element as childnodes
	 */
	protected static Element buildReportElement(final Participant participant, final IdentifiedSurgicalPathologyReport ispr, String sprText) 
	{
		Element reportElement = null;
		String mrn="";
		try 
		{
			// create element as REPORT
			reportElement = new Element(CaTIESConstants.REPORT);
			// set participant id
			Element patientIdElement = new Element(CaTIESConstants.PATIENT_ID);
			patientIdElement.addContent(participant.getId().toString());

			// set report type
			Element reportTypeElement = new Element(CaTIESConstants.REPORT_TYPE);
			reportTypeElement.addContent(CaTIESConstants.REPORT_TYPE_VALUE);

			// create report header
			Element reportHeaderElement = new Element(CaTIESConstants.REPORT_HEADER);
			reportHeaderElement.addContent(buildHeaderPersonElement(CaTIESConstants.PARTICIPANT_NAME, participant.getLastName()+","+participant.getFirstName(), CaTIESConstants.PARTICIPANT_ROLE));
			// get participant medical identifier collection
			String hqlQuery="select elements(p.participantMedicalIdentifierCollection)" +
					" from edu.wustl.catissuecore.domain.Participant as p" +
					" where p.id="+participant.getId();
			Collection<ParticipantMedicalIdentifier> medicalIdentifierCollection=(List)CaCoreAPIService.executeQuery(hqlQuery, Participant.class.getName());
			//iterate over participant medical identifier collection
			for(ParticipantMedicalIdentifier participantMedicalIdentifier : medicalIdentifierCollection)
			{
				mrn+=" ";
				mrn+=participantMedicalIdentifier.getMedicalRecordNumber();
			}
			// add above processed collection string to header element
			reportHeaderElement.addContent(buildHeaderPersonElement(CaTIESConstants.PARTICIPANT_MRN, mrn, CaTIESConstants.PARTICIPANT_ROLE));
			
			// create and report text to textElement
			Element reportTextElement = new Element(CaTIESConstants.REPORT_TEXT);
			sprText = DeidUtils.removeIllegalXmlCharacters(sprText);
			sprText += "\n||-"+ DateFormat.getDateTimeInstance().format(ispr.getCollectionDateTime()) + "-||";
			CDATA reportCDATA = new CDATA(sprText);
			reportTextElement.addContent(reportCDATA);

			// add patientId element to root element
			reportElement.addContent(patientIdElement);
			//add report type element to root element
			reportElement.addContent(reportTypeElement);
			// add report header element to root element
			reportElement.addContent(reportHeaderElement);
			// add report text element to root element
			reportElement.addContent(reportTextElement);
		}
		catch (Exception ex) 
		{
			Logger.out.error("Error in buildReportElement method for DeID",ex);
		}
		return reportElement;
	}


	/**
	 * This method converts the raw input information about participant into XML format
	 * @param variable participant
	 * @param value participant name or medical record number
	 * @param role role of articipant
	 * @return Elelment which contains input information 
	 * @throws Exception a generic exception occured while creating person element
	 */
	
	protected static Element buildHeaderPersonElement(final String variable, final String value, final String role) throws Exception
	{
		Element headerPersonElement = null;
		// create element person
		headerPersonElement = new Element(CaTIESConstants.HEADER_PERSON);
		// set role
		headerPersonElement.setAttribute(CaTIESConstants.ROLE, role);
		// create element variable
		Element variableElement = new Element(CaTIESConstants.VARIABLE);
		// create element value
		Element valueElement = new Element(CaTIESConstants.VALUE);
		// set variable
		variableElement.addContent(variable);
		// set value
		valueElement.addContent(value);
		// add variable element to person element
		headerPersonElement.addContent(variableElement);
		// add value element to person element
		headerPersonElement.addContent(valueElement);
		
		return headerPersonElement;
	}

	/**
	 * This method converts the raw header values into appropriate XML element
	 * @param variable Report Header
	 * @param value its value
	 * @return Elelemnt that contains header data element
	 * @throws Exception a generic exception occured while creating data element
	 */
	
	protected static Element buildHeaderDataElement(final String variable, final String value) throws Exception
	{
		Element headerDataElement = null;
		// create element data
		headerDataElement = new Element(CaTIESConstants.HEADER_DATA);
		// create element variable
		Element variableElement = new Element(CaTIESConstants.VARIABLE);
		//create element value
		Element valueElement = new Element(CaTIESConstants.VALUE);
		// set variable
		variableElement.addContent(variable);
		// set value
		valueElement.addContent(value);
		// add variable element to data element
		headerDataElement.addContent(variableElement);
		// add value element to data element
		headerDataElement.addContent(valueElement);
		
		return headerDataElement;

	}

	/**
	 * This method removes all the illegal XML characters from the input text
	 * @param sprText synthesized text
	 * @return text without ellegal XML characters
	 */
	public static String removeIllegalXmlCharacters(String sprText) 
	{
		// illegal XML character
		char illegalChar=0x1d;
		String result = sprText;
		try 
		{
			StringBuffer sb = new StringBuffer(sprText);
			// loop to check each character
			for (int idx = 0; idx < sb.length(); idx++) 
			{
				// check for illegal character
				if (sb.charAt(idx) == illegalChar) 
				{
					Logger.out.error("Found bad character.");
					sb.setCharAt(idx, ' ');
				}
			}
			result = sb.toString();
		}
		catch (Exception ex) 
		{
			Logger.out.error("Error in removeIllegalXmlCharacters method",ex);
		}

		return result;

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
	protected static String extractReport(String deIDResponse , final String dtdFilename) throws Exception
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
                XPath xpath = XPath.newInstance("//Report");
                // fire query on the document
                List deIdResults = xpath.selectNodes(deIDResponseDocument);
                Iterator deIdIterator = deIdResults.iterator();
                // iterate to extract report text
                while (deIdIterator.hasNext()) 
                {
                	// get next element
                    Element deIdReportElement = (Element) deIdIterator.next();
                    // get report text
                    deidSprText = deIdReportElement.getChild(CaTIESConstants.REPORT_TEXT).getText();
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
}
