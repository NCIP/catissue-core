package edu.wustl.catissuecore.deid;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.reportloader.Parser;
import edu.wustl.common.util.logger.Logger;


/**
 * @author vijay_pande
 * This class provides various utility methods which helps for the deidentification process
 */
public class DeidUtils
{

	/**
	 * @param participant participant
	 * @param ispr identified surgical pathology report
	 * @param sprText sysnthesized text
	 * @return Elelment which contains other all the report element as childnodes
	 * This mathod takes the identified report, its participant and converts 
	 * all the information into an appropriate xml document
	 */
	protected static Element buildReportElement(final Participant participant, final IdentifiedSurgicalPathologyReport ispr, String sprText) 
	{
		Element reportElement = null;
		String mrn="";
		try 
		{
			reportElement = new Element(Parser.REPORT);

			Element patientIdElement = new Element(Parser.PATIENT_ID);
			patientIdElement.addContent(participant.getId().toString());

			Element reportTypeElement = new Element(Parser.REPORT_TYPE);
			reportTypeElement.addContent(Parser.REPORT_TYPE_VALUE);

			Element reportHeaderElement = new Element(Parser.REPORT_HEADER);
			reportHeaderElement.addContent(buildHeaderPersonElement(Parser.PARTICIPANT_NAME, participant.getLastName(), Parser.PARTICIPANT_ROLE));
			Collection mic=participant.getParticipantMedicalIdentifierCollection();
			Iterator iter=mic.iterator();
			ParticipantMedicalIdentifier pmi=null;
			while(iter.hasNext())
			{
				mrn+=" ";
				pmi=(ParticipantMedicalIdentifier)iter.next();
				mrn+=pmi.getMedicalRecordNumber();
			}

			reportHeaderElement.addContent(buildHeaderPersonElement(Parser.PARTICIPANT_MRN, mrn, Parser.PARTICIPANT_ROLE));
//			reportHeaderElement.addContent(buildHeaderDataElement(Parser.REPORT_ACCESSION_NUMBER, ispr.getAccessionNumber()));

			Element reportTextElement = new Element(Parser.REPORT_TEXT);
			sprText = DeidUtils.removeIllegalXmlCharacters(sprText);
			sprText += "\n||-"+ DateFormat.getDateTimeInstance().format(ispr.getCollectionDateTime()) + "-||";
			CDATA reportCDATA = new CDATA(sprText);
			reportTextElement.addContent(reportCDATA);

			reportElement.addContent(patientIdElement);
			reportElement.addContent(reportTypeElement);
			reportElement.addContent(reportHeaderElement);
			reportElement.addContent(reportTextElement);
		}
		catch (Exception ex) 
		{
			Logger.out.error("Error in buildReportElement method for DeID",ex);
		}
		return reportElement;
	}


	/**
	 * @param variable participant
	 * @param value participant name or medical record number
	 * @param role role of articipant
	 * @return Elelment which contains input information 
	 * @throws Exception a generic exception occured while creating person element
	 * This method converts the raw input information about participant into XML format
	 */
	
	protected static Element buildHeaderPersonElement(final String variable, final String value, final String role) throws Exception
	{
		Element headerPersonElement = null;
		
		headerPersonElement = new Element(Parser.HEADER_PERSON);
		headerPersonElement.setAttribute(Parser.ROLE, role);
		Element variableElement = new Element(Parser.VARIABLE);
		Element valueElement = new Element(Parser.VALUE);
		variableElement.addContent(variable);
		valueElement.addContent(value);
		headerPersonElement.addContent(variableElement);
		headerPersonElement.addContent(valueElement);
		
		return headerPersonElement;
	}

	/**
	 * @param variable Report Header
	 * @param value its value
	 * @return Elelemnt that contains header data element
	 * @throws Exception a generic exception occured while creating data element
	 * This method converts the raw header values into appropriate XML element
	 */
	
	protected static Element buildHeaderDataElement(final String variable, final String value) throws Exception
	{
		Element headerDataElement = null;
		
		headerDataElement = new Element(Parser.HEADER_DATA);
		Element variableElement = new Element(Parser.VARIABLE);
		Element valueElement = new Element(Parser.VALUE);
		variableElement.addContent(variable);
		valueElement.addContent(value);
		headerDataElement.addContent(variableElement);
		headerDataElement.addContent(valueElement);
		
		return headerDataElement;

	}


	/**
	 * @param deidText deidentified text
	 * @return Date collection date and time
	 * @throws Exception a generic exception occured while extracting date from report
	 * This method removes the collection date and time of the report from the deidentified text before saving it
	 */
	protected static Date extractDate(String deidText) throws Exception
	{
		Date deidDate = null;

		try 
		{
			String sentinal = "**DATE[";
			
			String dateText = deidText.substring(deidText.lastIndexOf("||-") + new String("||-").length(), deidText.lastIndexOf("-||"));

			String preDate = dateText.substring(0, dateText.lastIndexOf(sentinal));
			String postDate = dateText.substring(dateText.lastIndexOf(sentinal)
					+ sentinal.length(), dateText.length());
			postDate = postDate.replaceAll("]", "");

			StringTokenizer st = new StringTokenizer(preDate + postDate);
			String date = "";
			for (int x = 0; st.hasMoreTokens(); x++) 
			{
				String token = st.nextToken();
				if (x == 1)
				{
					token += ",";
				}
				date += token + " ";
			}

			DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM);
			deidDate = df.parse(date.trim());

		}
		catch (Exception ex) 
		{
			Logger.out.error("Extracting date from deidentified text is failed",ex);
			throw ex;
		}
		return deidDate;
	}


	/**
	 * @param sprText synthesized text
	 * @return text without ellegal XML characters
	 * This method removes all the illegal XML characters from the input text
	 */
	public static String removeIllegalXmlCharacters(String sprText) 
	{
		char illegalChar=0x1d;
		String result = sprText;
		try 
		{
			StringBuffer sb = new StringBuffer(sprText);
			for (int idx = 0; idx < sb.length(); idx++) 
			{
				if (sb.charAt(idx) == illegalChar) 
				{
					Logger.out.error("Found the bad character.");
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
	 * @param doc Document in XML format
	 * @param format formatted style of text
	 * @return plain text form of XML document
	 * @throws Exception exception occured while converting XML content into equivalent plain text format
	 * This method takes XML document as an input and convert into pain text format according to its formatting style
	 */
	
	public static String convertDocumentToString(final org.jdom.Document doc, final  Format format) throws Exception
	{
		String result = "" ;
		XMLOutputter outputDocument = new XMLOutputter() ;
		if (format != null) 
		{
			outputDocument.setFormat(format) ;
		}
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream() ;
		outputDocument.output(doc, byteArrayOutputStream) ;
		result = byteArrayOutputStream.toString() ;
		
		return result ;
	}
	
	
	/**
	 * @param deIDResponse deidentified text from native call
	 * @param dtdFilename local dtd filename for deidentified text in XML format
	 * @return returns only report text content
	 * @throws Exception throws exception occured while etracting report content
	 * This method is responsible to extract only report content that contains report section text.
	 */
	protected static String extractReport(String deIDResponse , final String dtdFilename) throws Exception
	{

        String deidSprText = "";
        
        try 
        {
            if (deIDResponse != null && deIDResponse.trim().length() > 0)
            {
                SAXBuilder builder = new SAXBuilder();
                builder.setEntityResolver(new EntityResolver()
                {
                    public InputSource resolveEntity(String publicId, String systemId)
                    {
						return new InputSource(dtdFilename);
                    }
                });
   
                builder.setFeature("http://apache.org/xml/features/validation/schema",true);
                builder.setFeature("http://xml.org/sax/features/namespaces",true);

                byte[] byteArray = deIDResponse.getBytes();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
                Document deIDResponseDocument = builder.build(byteArrayInputStream);
 
                XPath xpath = XPath.newInstance("//Report");
                List deIdResults = xpath.selectNodes(deIDResponseDocument);
                Iterator deIdIterator = deIdResults.iterator();
                while (deIdIterator.hasNext()) 
                {
                    Element deIdReportElement = (Element) deIdIterator.next();
                    deidSprText = deIdReportElement.getChild(Parser.REPORT_TEXT).getText();
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
        return deidSprText;
    }
}
