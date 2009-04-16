package edu.wustl.catissuecore.deidentifier.deid;

import java.text.DateFormat;
import java.util.Collection;

import org.jdom.CDATA;
import org.jdom.Element;

import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.common.util.logger.Logger;


/**
 * This class provides various utility methods which helps for the deidentification process
 * @author vijay_pande
 */
public class DeidUtils
{

	private static Logger logger = Logger.getCommonLogger(DeidUtils.class);
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
			
			Collection<ParticipantMedicalIdentifier> medicalIdentifierCollection=participant.getParticipantMedicalIdentifierCollection();
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
			logger.error("Error in buildReportElement method for DeID",ex);
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
					logger.error("Found bad character.");
					sb.setCharAt(idx, ' ');
				}
			}
			result = sb.toString();
		}
		catch (Exception ex) 
		{
			logger.error("Error in removeIllegalXmlCharacters method",ex);
		}

		return result;

	}
}
