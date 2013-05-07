
package edu.wustl.catissuecore.deidentifier.harvardscrubber;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.jdom.CDATA;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.spin.loader.scrubber.impl.Scrubber;
import org.spin.loader.scrubber.impl.ScrubberBuilder;
import org.spin.loader.scrubber.tools.JDOMTools;

import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.CaTIESProperties;
import edu.wustl.catissuecore.caties.util.Utility;
import edu.wustl.catissuecore.deidentifier.AbstractDeidentifier;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.TextContent;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.global.CommonUtilities;

/**
 * @author
 *
 */
public class HarvardSrubberDeidentifier extends AbstractDeidentifier
{

	/**
	 * scrubber.
	 */
	private static Scrubber scrubber;

	/**
	 * @param identifiedReport : identifiedReport
	 * @throws Exception : Exception
	 * @return DeidentifiedSurgicalPathologyReport
	 */
	@Override
	public DeidentifiedSurgicalPathologyReport deidentify(
			IdentifiedSurgicalPathologyReport identifiedReport) throws Exception
	{
		String scrubbed;
		synchronized (scrubber)
		{
			final File fileTosScrub = this.getFileToScrub(identifiedReport);
			scrubbed = scrubber.scrub(fileTosScrub);
			fileTosScrub.delete();
		}

		String deidReportText = Utility.extractReport(scrubbed, CaTIESProperties
				.getValue(CaTIESConstants.HARVARD_SCRUBBER_DTD_FILENAME),
				CaTIESConstants.HARVARD_SCRUBBER_XPATH, CaTIESConstants.TAG_FULL_REPORT_TEXT);

		final DeidentifiedSurgicalPathologyReport deidentifiedSurgicalPathologyReport
						= new DeidentifiedSurgicalPathologyReport();
		final TextContent textContent = new TextContent();
		deidReportText = updateTextForConceptCoder(deidReportText);
		textContent.setData(deidReportText);
		textContent.setSurgicalPathologyReport(deidentifiedSurgicalPathologyReport);

		deidentifiedSurgicalPathologyReport.setTextContent(textContent);
		return deidentifiedSurgicalPathologyReport;
	}

	/**Method to update report text to add two initial new line characters
	 * since they are required for concept coding.
	 * Sometime harvard-scrubber removes the initial two new line characters. Exact scenario is not know now.
	 * @param deidReportText de-identified report text
	 * @return String
	 */
	private String updateTextForConceptCoder(String deidReportText)
	{
		if(!deidReportText.startsWith("\n\n"))
		{
			deidReportText = "\n\n" + deidReportText;
		}
		return deidReportText;
	}

	/**
	 * @param identifiedReport : identifiedReport
	 * @return : File
	 * @throws Exception : Exception
	 */
	private File getFileToScrub(IdentifiedSurgicalPathologyReport identifiedReport)
			throws Exception
	{
		final org.jdom.Document currentRequestDocument = new org.jdom.Document(new Element(
				CaTIESConstants.TAG_ENVELOPE));
		final Element reportHeader = this.buildReportHeader(identifiedReport);
		currentRequestDocument.getRootElement().addContent(reportHeader);

		final DocType docType = new DocType("HarvardScrubber", "file:///"
				+ CaTIESProperties.getValue(CaTIESConstants.HARVARD_SCRUBBER_DTD_FILENAME));

		currentRequestDocument.setDocType(docType);

		final Element reportBody = this.buildReportBody(identifiedReport);
		currentRequestDocument.getRootElement().addContent(reportBody);

		final File fileTosScrub = new File("fileTosScrub.xml");
		final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileTosScrub));
		bufferedWriter.write(Utility.convertDocumentToString(currentRequestDocument, Format
				.getPrettyFormat()));
		bufferedWriter.close();

		return fileTosScrub;
	}

	/**
	 * @throws Exception : Exception
	 */
	@Override
	public void initialize() throws Exception
	{
		final JDOMTools jdom = new JDOMTools();
		final File configurationFile = new File(CaTIESProperties
				.getValue(CaTIESConstants.HARVARD_SCRUBBER_CONFIG_FILENAME));
		jdom.buildDoc(configurationFile);
		final Document document = jdom.getDocument();
		scrubber = ScrubberBuilder.makeScrubber(document);
		scrubber.init();
	}

	/**
	 * @throws Exception : Exception
	 */
	@Override
	public void shutdown() throws Exception
	{
		scrubber.shutdown();
	}

	/**
	 * @param identifiedReport : identifiedReport
	 * @return Element
	 */
	private Element buildReportBody(final IdentifiedSurgicalPathologyReport identifiedReport)
	{
		final Participant participant = identifiedReport.getSpecimenCollectionGroup()
				.getCollectionProtocolRegistration().getParticipant();
		final Element body = new Element(CaTIESConstants.TAG_BODY);
		final Element pathologyCase = new Element(CaTIESConstants.TAG_PATHOLOGY_CASE);
		pathologyCase.setAttribute(CaTIESConstants.TAG_TISSUE_ACQUISITION_DATE, identifiedReport
				.getCollectionDateTime().toString());
		final Element codes = this.getElement(CaTIESConstants.TAG_CODES, null);
		final Element clinical = new Element(CaTIESConstants.TAG_CLINICAL);
		final Element patient = new Element(CaTIESConstants.TAG_PATIENT);
		final Element age = new Element(CaTIESConstants.TAG_AGE);
		final Date birthDate = participant.getBirthDate();
		int ageOfParticipant = 0;
		if (birthDate != null)
		{
			ageOfParticipant = new GregorianCalendar().getTime().getYear() - birthDate.getYear();
		}
		age.setAttribute("Units", "years");
		age.setText(String.valueOf(ageOfParticipant));
		String genderOfParticipant = getGenderForParticipant(participant);
		final Element gender = this.getElement(CaTIESConstants.TAG_GENDER, genderOfParticipant);
		final List<Element> patientChild = new ArrayList<Element>();
		patientChild.add(age);
		patientChild.add(gender);
		patient.setContent(patientChild);
		clinical.setContent(patient);
		final Element fullReportText = new Element(CaTIESConstants.TAG_FULL_REPORT_TEXT);
		CDATA reportText = new CDATA("");
		if (identifiedReport.getTextContent() != null)
		{
			reportText = new CDATA(identifiedReport.getTextContent().getData());
		}
		fullReportText.setContent(reportText);
		final List<Element> pathologyCaseChild = new ArrayList<Element>();
		pathologyCaseChild.add(codes);
		pathologyCaseChild.add(clinical);
		pathologyCaseChild.add(fullReportText);
		pathologyCase.addContent(pathologyCaseChild);
		body.addContent(pathologyCase);
		return body;
	}

	/**
	 * @param participant Participant
	 * @param genderOfParticipant String
	 * @return String
	 */
	private String getGenderForParticipant(final Participant participant)
	{
		String genderOfParticipant = "";
		if (participant.getGender().equalsIgnoreCase(CaTIESConstants.MALE_GENDER))
		{
			genderOfParticipant = "M";
		}
		else if (participant.getGender().equalsIgnoreCase(CaTIESConstants.FEMALE_GENDER))
		{
			genderOfParticipant = "F";
		}
		else if (participant.getGender().equalsIgnoreCase(Constants.UNKNOWN))
		{
			genderOfParticipant = "U";
		}
		return genderOfParticipant;
	}

	/**
	 * @param identifiedReport : identifiedReport
	 * @return Element
	 */
	private Element buildReportHeader(final IdentifiedSurgicalPathologyReport identifiedReport)
	{
		final Participant participant = identifiedReport.getSpecimenCollectionGroup()
				.getCollectionProtocolRegistration().getParticipant();

		final Element header = new Element(CaTIESConstants.TAG_HEADER);
		final Element identifier = new Element(CaTIESConstants.TAG_IDENTIFIERS);

		final Element firstName = this.getElement(CaTIESConstants.TAG_FIRST_NAME, participant
				.getFirstName());
		final Element lastName = this.getElement(CaTIESConstants.TAG_LAST_NAME, participant
				.getLastName());
		final Element dateOfBirth = this.getElement(CaTIESConstants.TAG_DATE_OF_BIRTH,
				CommonUtilities.toString(participant.getBirthDate()));
		final Element ssn = this.getElement(CaTIESConstants.TAG_SSN, participant
				.getSocialSecurityNumber());
		final Element accessionNumber = this.getElement(CaTIESConstants.TAG_ACCESSION_NUMBER,
				identifiedReport.getSpecimenCollectionGroup().getSurgicalPathologyNumber());
		final Collection<ParticipantMedicalIdentifier> participantMedicalIdentifierCollection = participant
				.getParticipantMedicalIdentifierCollection();
		final List<Element> localMRNList = new ArrayList<Element>();
		for (final ParticipantMedicalIdentifier pmi : participantMedicalIdentifierCollection)
		{
			final Element localMRN = this.getElement(CaTIESConstants.TAG_LOCALMRN, pmi
					.getMedicalRecordNumber());
			localMRNList.add(localMRN);
		}
		final Element source = this.getElement(CaTIESConstants.TAG_SOURCE, identifiedReport
				.getReportSource().getName());

		identifier.addContent(firstName);
		identifier.addContent(lastName);
		identifier.addContent(dateOfBirth);
		identifier.addContent(ssn);
		identifier.addContent(accessionNumber);
		identifier.addContent(localMRNList);
		identifier.addContent(source);
		header.addContent(identifier);

		return header;
	}

	/**
	 * @param elementName : elementName
	 * @param value : value
	 * @return Element
	 */
	private Element getElement(String elementName, String value)
	{
		final Element element = new Element(elementName);
		element.setText(value);
		return element;
	}
}
