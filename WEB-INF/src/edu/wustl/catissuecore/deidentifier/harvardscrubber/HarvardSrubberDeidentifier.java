
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

public class HarvardSrubberDeidentifier extends AbstractDeidentifier
{

	private static Scrubber scrubber;

	public DeidentifiedSurgicalPathologyReport deidentify(
			IdentifiedSurgicalPathologyReport identifiedReport) throws Exception
	{
		String scrubbed;
		synchronized (scrubber)
		{
			File fileTosScrub = getFileToScrub(identifiedReport);
			scrubbed = scrubber.scrub(fileTosScrub);
			fileTosScrub.delete();
		}

		String deidReportText = Utility.extractReport(scrubbed, CaTIESProperties
				.getValue(CaTIESConstants.HARVARD_SCRUBBER_DTD_FILENAME),
				CaTIESConstants.HARVARD_SCRUBBER_XPATH, CaTIESConstants.TAG_FULL_REPORT_TEXT);

		DeidentifiedSurgicalPathologyReport deidentifiedSurgicalPathologyReport = new DeidentifiedSurgicalPathologyReport();
		TextContent textContent = new TextContent();
		textContent.setData(deidReportText);
		textContent.setSurgicalPathologyReport(deidentifiedSurgicalPathologyReport);

		deidentifiedSurgicalPathologyReport.setTextContent(textContent);
		return deidentifiedSurgicalPathologyReport;
	}

	private File getFileToScrub(IdentifiedSurgicalPathologyReport identifiedReport)
			throws Exception
	{
		org.jdom.Document currentRequestDocument = new org.jdom.Document(new Element(
				CaTIESConstants.TAG_ENVELOPE));
		Element reportHeader = buildReportHeader(identifiedReport);
		currentRequestDocument.getRootElement().addContent(reportHeader);

		DocType docType = new DocType("HarvardScrubber", "file:///"
				+ CaTIESProperties.getValue(CaTIESConstants.HARVARD_SCRUBBER_DTD_FILENAME));

		currentRequestDocument.setDocType(docType);

		Element reportBody = buildReportBody(identifiedReport);
		currentRequestDocument.getRootElement().addContent(reportBody);

		File fileTosScrub = new File("fileTosScrub.xml");
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileTosScrub));
		bufferedWriter.write(Utility.convertDocumentToString(currentRequestDocument, Format
				.getPrettyFormat()));
		bufferedWriter.close();

		return fileTosScrub;
	}

	public void initialize() throws Exception
	{
		JDOMTools jdom = new JDOMTools();
		File configurationFile = new File(CaTIESProperties
				.getValue(CaTIESConstants.HARVARD_SCRUBBER_CONFIG_FILENAME));
		jdom.buildDoc(configurationFile);
		Document document = jdom.getDocument();
		scrubber = ScrubberBuilder.makeScrubber(document);
		scrubber.init();
	}

	public void shutdown() throws Exception
	{
		scrubber.shutdown();
	}

	private Element buildReportBody(final IdentifiedSurgicalPathologyReport identifiedReport)
	{
		Participant participant = identifiedReport.getSpecimenCollectionGroup()
				.getCollectionProtocolRegistration().getParticipant();

		Element body = new Element(CaTIESConstants.TAG_BODY);
		Element pathologyCase = new Element(CaTIESConstants.TAG_PATHOLOGY_CASE);
		pathologyCase.setAttribute(CaTIESConstants.TAG_TISSUE_ACQUISITION_DATE, identifiedReport
				.getCollectionDateTime().toString());

		Element codes = getElement(CaTIESConstants.TAG_CODES, null);
		Element clinical = new Element(CaTIESConstants.TAG_CLINICAL);
		Element patient = new Element(CaTIESConstants.TAG_PATIENT);
		Element age = new Element(CaTIESConstants.TAG_AGE);

		Date birthDate = participant.getBirthDate();
		int ageOfParticipant = 0;
		if (birthDate != null)
		{
			ageOfParticipant = new GregorianCalendar().getTime().getYear() - birthDate.getYear();
		}
		age.setAttribute("Units", "years");
		age.setText(String.valueOf(ageOfParticipant));
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
		Element gender = getElement(CaTIESConstants.TAG_GENDER, genderOfParticipant);
		List<Element> patientChild = new ArrayList<Element>();
		patientChild.add(age);
		patientChild.add(gender);

		patient.setContent(patientChild);
		clinical.setContent(patient);

		Element fullReportText = new Element(CaTIESConstants.TAG_FULL_REPORT_TEXT);
		CDATA reportText = new CDATA("");
		if (identifiedReport.getTextContent() != null)
		{
			reportText = new CDATA(identifiedReport.getTextContent().getData());
		}
		fullReportText.setContent(reportText);

		List<Element> pathologyCaseChild = new ArrayList<Element>();
		pathologyCaseChild.add(codes);
		pathologyCaseChild.add(clinical);
		pathologyCaseChild.add(fullReportText);

		pathologyCase.addContent(pathologyCaseChild);
		body.addContent(pathologyCase);
		return body;
	}

	private Element buildReportHeader(final IdentifiedSurgicalPathologyReport identifiedReport)
	{
		Participant participant = identifiedReport.getSpecimenCollectionGroup()
				.getCollectionProtocolRegistration().getParticipant();

		Element header = new Element(CaTIESConstants.TAG_HEADER);
		Element identifier = new Element(CaTIESConstants.TAG_IDENTIFIERS);

		Element firstName = getElement(CaTIESConstants.TAG_FIRST_NAME, participant.getFirstName());
		Element lastName = getElement(CaTIESConstants.TAG_LAST_NAME, participant.getLastName());
		Element dateOfBirth = getElement(CaTIESConstants.TAG_DATE_OF_BIRTH,
				edu.wustl.common.util.Utility.toString(participant.getBirthDate()));
		Element ssn = getElement(CaTIESConstants.TAG_SSN, participant.getSocialSecurityNumber());
		Element accessionNumber = getElement(CaTIESConstants.TAG_ACCESSION_NUMBER, identifiedReport
				.getSpecimenCollectionGroup().getSurgicalPathologyNumber());
		Collection<ParticipantMedicalIdentifier> participantMedicalIdentifierCollection = participant
				.getParticipantMedicalIdentifierCollection();
		List<Element> localMRNList = new ArrayList<Element>();
		for (ParticipantMedicalIdentifier pmi : participantMedicalIdentifierCollection)
		{
			Element localMRN = getElement(CaTIESConstants.TAG_LOCALMRN, pmi
					.getMedicalRecordNumber());
			localMRNList.add(localMRN);
		}
		Element source = getElement(CaTIESConstants.TAG_SOURCE, identifiedReport.getReportSource()
				.getName());

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

	private Element getElement(String elementName, String value)
	{
		Element element = new Element(elementName);
		element.setText(value);
		return element;
	}
}
