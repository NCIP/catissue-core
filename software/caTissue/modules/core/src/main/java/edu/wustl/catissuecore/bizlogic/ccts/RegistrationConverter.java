/**
 * 
 */
package edu.wustl.catissuecore.bizlogic.ccts;

import java.io.StringReader;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import edu.duke.cabig.c3pr.webservice.common.BiologicEntity;
import edu.duke.cabig.c3pr.webservice.common.Document;
import edu.duke.cabig.c3pr.webservice.common.DocumentIdentifier;
import edu.duke.cabig.c3pr.webservice.common.Organization;
import edu.duke.cabig.c3pr.webservice.common.OrganizationIdentifier;
import edu.duke.cabig.c3pr.webservice.common.StudyProtocolDocumentVersion;
import edu.duke.cabig.c3pr.webservice.common.StudyProtocolVersion;
import edu.duke.cabig.c3pr.webservice.common.StudySiteProtocolVersionRelationship;
import edu.duke.cabig.c3pr.webservice.common.StudySubjectConsentVersion;
import edu.duke.cabig.c3pr.webservice.common.SubjectIdentifier;
import edu.duke.cabig.c3pr.webservice.iso21090.CD;
import edu.duke.cabig.c3pr.webservice.subjectregistration.ScheduledEpoch;
import edu.duke.cabig.c3pr.webservice.subjectregistration.StudySubject;
import edu.duke.cabig.c3pr.webservice.subjectregistration.StudySubjectProtocolVersionRelationship;
import edu.wustl.catissuecore.bizlogic.ICollectionProtocolBizLogic;
import edu.wustl.catissuecore.bizlogic.IParticipantBizLogic;
import edu.wustl.catissuecore.bizlogic.ISiteBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.ccts.DataQueue;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;

/**
 * Converts {@link StudySubject} instances into
 * {@link CollectionProtocolRegistration} instances and back.
 * 
 * @author Denis G. Krylov
 * 
 */
public final class RegistrationConverter implements
		IDataConverter<CollectionProtocolRegistration>, IRegistrationConverter {

	public static final String COORDINATING_CENTER_IDENTIFIER = "COORDINATING_CENTER_IDENTIFIER";
	public static final String OFF_STUDY = "Off-Study";
	public static final String ON_STUDY = "On-Study";
	public static final String CTEP = "CTEP";
	public static final String COORDINATING_CENTER_ASSIGNED_STUDY_SUBJECT_IDENTIFIER = "COORDINATING_CENTER_ASSIGNED_STUDY_SUBJECT_IDENTIFIER";
	private static final Logger logger = Logger
			.getCommonLogger(RegistrationConverter.class);
	private static final ISO21090Helper h = null;
	private static final ThreadLocal<IErrorsReporter> ierHolder = new ThreadLocal<IErrorsReporter>();

	private ISiteBizLogic siteBizLogic;
	private IParticipantConverter participantConverter;
	private ICollectionProtocolBizLogic collectionProtocolBizLogic;
	private IParticipantBizLogic participantBizLogic;

	private static final List<String> ACTIVE_STATUS_CODES = java.util.Arrays
			.asList("on-study");

	@Override
	public Class<CollectionProtocolRegistration> supports(DataQueue item) {
		if (item.isRegistrationRelated())
			return CollectionProtocolRegistration.class;
		return null;
	}

	@Override
	public CollectionProtocolRegistration convert(DataQueue item,
			IErrorsReporter errorsReporter) {
		CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
		convert(cpr, item, errorsReporter);
		return cpr;
	}

	@Override
	public void convert(CollectionProtocolRegistration cpr, DataQueue item,
			IErrorsReporter errorsReporter) {
		ierHolder.set(errorsReporter);
		try {
			StudySubject subject = convertToStudySubject(item);
			if (subject != null) {
				convert(cpr, subject);
			}
			cpr.setGridId(item.getNotification().getObjectIdValue());
		} catch (Exception e) {
			throw new ProcessingException(e);
		} finally {
			ierHolder.remove();
		}
	}

	private void convert(CollectionProtocolRegistration cpr,
			StudySubject subject) throws Exception {
		convertParticipant(cpr, subject.getEntity());
		convertProtocol(cpr, subject);
		convertRegistration(cpr, subject);
	}

	private void convertRegistration(CollectionProtocolRegistration p,
			StudySubject subject) {
		// set protocol registration id.
		if (CollectionUtils.isNotEmpty(subject.getSubjectIdentifier())) {
			for (SubjectIdentifier si : subject.getSubjectIdentifier()) {
				if (COORDINATING_CENTER_ASSIGNED_STUDY_SUBJECT_IDENTIFIER
						.equalsIgnoreCase(h.str(si.getTypeCode()))) {
					p.setProtocolParticipantIdentifier(h.str(si.getIdentifier()));
				}
			}
		}

		// set registration date based on the scheduled epoch's start date.
		Date regDate = findRegistrationDate(subject);
		if (regDate != null) {
			p.setRegistrationDate(regDate);
		} else {
			error("Protocol registration information does not contain a registration date.");
		}

		// set consent signature date based on "informedConsentDate".
		Date consentDate = findConsentDate(subject);
		if (consentDate != null) {
			p.setConsentSignatureDate(consentDate);
		}

		// Set Active/Inactive based on "statusCode".
		String statusCode = h.str(subject.getStatusCode());
		if (ACTIVE_STATUS_CODES
				.contains((statusCode + "").toLowerCase().trim())) {
			p.setActivityStatus(edu.wustl.common.util.global.Status.ACTIVITY_STATUS_ACTIVE
					.toString());
		} else {
			p.setActivityStatus(edu.wustl.common.util.global.Status.ACTIVITY_STATUS_CLOSED
					.toString());
		}
	}

	private Date findConsentDate(StudySubject subject) {
		final List<StudySubjectConsentVersion> consents = ((StudySubjectProtocolVersionRelationship) subject
				.getStudySubjectProtocolVersion())
				.getStudySubjectConsentVersion();
		if (CollectionUtils.isNotEmpty(consents)) {
			for (StudySubjectConsentVersion consent : consents) {
				Date signDate = h.toDate(consent.getInformedConsentDate());
				if (signDate != null) {
					return signDate;
				}
			}
		}
		return null;
	}

	private Date findRegistrationDate(StudySubject subject) {
		final List<ScheduledEpoch> epochList = ((StudySubjectProtocolVersionRelationship) subject
				.getStudySubjectProtocolVersion()).getScheduledEpoch();
		if (CollectionUtils.isNotEmpty(epochList)) {
			for (ScheduledEpoch epoch : epochList) {
				Date startDate = h.toDate(epoch.getStartDate());
				if (startDate != null) {
					return startDate;
				}
			}
		}
		return null;
	}

	private void convertProtocol(CollectionProtocolRegistration p,
			StudySubject studySubject) throws BizLogicException {
		// pardon the ugly null check below.
		List<DocumentIdentifier> docIdList = null;
		if (studySubject.getStudySubjectProtocolVersion() != null
				&& studySubject.getStudySubjectProtocolVersion()
						.getStudySiteProtocolVersion() != null
				&& studySubject.getStudySubjectProtocolVersion()
						.getStudySiteProtocolVersion()
						.getStudyProtocolVersion() != null
				&& studySubject.getStudySubjectProtocolVersion()
						.getStudySiteProtocolVersion()
						.getStudyProtocolVersion().getStudyProtocolDocument() != null
				&& studySubject.getStudySubjectProtocolVersion()
						.getStudySiteProtocolVersion()
						.getStudyProtocolVersion().getStudyProtocolDocument()
						.getDocument() != null
				&& (docIdList = studySubject.getStudySubjectProtocolVersion()
						.getStudySiteProtocolVersion()
						.getStudyProtocolVersion().getStudyProtocolDocument()
						.getDocument().getDocumentIdentifier()) != null) {
			CollectionProtocol protocol = findCollectionProtocol(docIdList);
			if (protocol != null) {
				p.setCollectionProtocol(protocol);
			} else {
				error("Unable to find a collection protocol referenced by this registration record. Need a protocol identified by one of the following IRB ID/Site combinations: "
						+ toString(docIdList) + ".");
			}
		} else {
			error("Protocol registration information does not identify a collection protocol properly.");
		}
	}

	private String toString(List<DocumentIdentifier> docIdList) {
		StringBuilder sb = new StringBuilder();
		for (DocumentIdentifier id : docIdList) {
			sb.append(toString(id) + ", ");
		}
		return sb.toString().replaceAll(",\\s*$", "")
				.replaceFirst("^\\s*$", "N/A");
	}

	private String toString(DocumentIdentifier id) {
		String irbId = getIrbId(id);
		String ctep = getCtepId(id);
		if (StringUtils.isNotBlank(irbId) && StringUtils.isNotBlank(ctep)) {
			return irbId + "/" + ctep;
		}
		return "";
	}

	private CollectionProtocol findCollectionProtocol(
			List<DocumentIdentifier> docIdList) throws BizLogicException {
		CollectionProtocol cp = null;
		for (DocumentIdentifier id : docIdList) {
			cp = findCollectionProtocol(id);
			if (cp != null)
				break;
		}
		return cp;
	}

	private CollectionProtocol findCollectionProtocol(DocumentIdentifier docId)
			throws BizLogicException {
		CollectionProtocol cp = null;
		String irbId = getIrbId(docId);
		String siteCtep = getCtepId(docId);
		if (StringUtils.isNotBlank(irbId) && StringUtils.isNotBlank(siteCtep)) {
			cp = collectionProtocolBizLogic.findByIRBInfo(irbId, siteCtep);
		}
		// let's also check site's existence for completeness' sake.
		if (StringUtils.isNotBlank(siteCtep)
				&& siteBizLogic.getSiteByCtepId(siteCtep) == null) {
			error("A site with CTEP ID of " + siteCtep + " cannot be found.");
		}
		// If we can't find a protocol by IRB ID/Site combination, let's attempt
		// to look by IRB ID only and return a CP
		// only if there's an UNIQUE match, eh?
		if (cp == null && StringUtils.isNotBlank(irbId)) {
			Collection<CollectionProtocol> list = collectionProtocolBizLogic
					.findByIRBInfo(irbId);
			if (CollectionUtils.isNotEmpty(list) && list.size() == 1) {
				cp = list.iterator().next();
			}
		}
		return cp;
	}

	/**
	 * @param docId
	 * @param siteCtep
	 * @return
	 */
	private String getCtepId(DocumentIdentifier docId) {
		String siteCtep = "";
		if (docId.getAssigningOrganization() != null
				&& CollectionUtils.isNotEmpty(docId.getAssigningOrganization()
						.getOrganizationIdentifier())) {
			siteCtep = h.str(docId.getAssigningOrganization()
					.getOrganizationIdentifier().get(0).getIdentifier());
		}
		return siteCtep;
	}

	/**
	 * @param docId
	 * @return
	 */
	private String getIrbId(DocumentIdentifier docId) {
		return h.str(docId.getIdentifier());
	}

	private void convertParticipant(CollectionProtocolRegistration cpr,
			BiologicEntity entity) throws Exception {

		if (entity != null) {

			// We don't apply participant information received from C3PR if we
			// find an existing participant in caTissue.
			// Why? Because C3PR captures participant demographics on two
			// levels: participant level and registration level.
			// caTissue does not make this distinction, so we will ignore
			// demographic data received from C3PR on registration
			// level.

			if (cpr.getParticipant() == null
					|| cpr.getParticipant().getId() == null) {

				// first let's get MRNs to see if this participant exists
				// locally.
				Participant p = new Participant();
				participantConverter.convert(p, entity);
				Collection<ParticipantMedicalIdentifier> pmiList = p
						.getParticipantMedicalIdentifierCollection();
				Participant existingParticipant = participantBizLogic
						.getParticipantByPMI(pmiList);

				if (existingParticipant == null) {
					cpr.setParticipant(new Participant());
					participantConverter.convert(cpr.getParticipant(), entity,
							ierHolder.get());
				} else {
					cpr.setParticipant(existingParticipant);
				}
			} else {
				cpr.setParticipant(participantBizLogic.getParticipantById(cpr
						.getParticipant().getId()));
			}

		} else {
			error("Protocol registration information does not identify a participant.");
		}
	}

	private StudySubject convertToStudySubject(DataQueue item)
			throws JAXBException {
		String xml = item.getPayload();
		StudySubject subject = null;
		if (StringUtils.isNotBlank(xml)) {
			JAXBContext jc = JAXBContext.newInstance(StudySubject.class,
					CD.class);
			Unmarshaller m = jc.createUnmarshaller();
			JAXBElement<StudySubject> element = m.unmarshal(new StreamSource(
					new StringReader(xml)), StudySubject.class);
			subject = element.getValue();
		} else {
			error("The data queue entry has an empty XML payload. Conversion cannot be performed.");
		}
		return subject;
	}

	private void error(String msg) {
		if (ierHolder.get() != null) {
			ierHolder.get().error(msg);
		}
	}

	/**
	 * @return the siteBizLogic
	 */
	public final ISiteBizLogic getSiteBizLogic() {
		return siteBizLogic;
	}

	/**
	 * @param siteBizLogic
	 *            the siteBizLogic to set
	 */
	public final void setSiteBizLogic(ISiteBizLogic siteBizLogic) {
		this.siteBizLogic = siteBizLogic;
	}

	/**
	 * @return the participantConverter
	 */
	public IParticipantConverter getParticipantConverter() {
		return participantConverter;
	}

	/**
	 * @param participantConverter
	 *            the participantConverter to set
	 */
	public void setParticipantConverter(
			IParticipantConverter participantConverter) {
		this.participantConverter = participantConverter;
	}

	/**
	 * @return the collectionProtocolBizLogic
	 */
	public ICollectionProtocolBizLogic getCollectionProtocolBizLogic() {
		return collectionProtocolBizLogic;
	}

	/**
	 * @param collectionProtocolBizLogic
	 *            the collectionProtocolBizLogic to set
	 */
	public void setCollectionProtocolBizLogic(
			ICollectionProtocolBizLogic collectionProtocolBizLogic) {
		this.collectionProtocolBizLogic = collectionProtocolBizLogic;
	}

	/**
	 * @return the participantBizLogic
	 */
	public IParticipantBizLogic getParticipantBizLogic() {
		return participantBizLogic;
	}

	/**
	 * @param participantBizLogic
	 *            the participantBizLogic to set
	 */
	public void setParticipantBizLogic(IParticipantBizLogic participantBizLogic) {
		this.participantBizLogic = participantBizLogic;
	}

	
	@Override
	public JAXBElement<StudySubject> convert(CollectionProtocolRegistration cpr) {
		StudySubject studySubject = new StudySubject();

		// set person
		BiologicEntity person = participantConverter
				.convert(cpr.getParticipant()).getValue().getEntity();
		studySubject.setEntity(person);
		studySubject
				.setStatusCode(h
						.CD(edu.wustl.common.util.global.Status.ACTIVITY_STATUS_ACTIVE
								.toString().equalsIgnoreCase(
										cpr.getActivityStatus()) ? ON_STUDY
								: OFF_STUDY));

		// copy identifiers
		studySubject.getSubjectIdentifier().add(
				convertToSubjectIdentifier(
						cpr.getProtocolParticipantIdentifier(),
						cpr.getCollectionProtocol()));

		// set studySubjectProtocolVersion
		studySubject
				.setStudySubjectProtocolVersion(getStudySubjectProtocolVersion(cpr
						.getCollectionProtocol()));

		return new JAXBElement<StudySubject>(new QName(
				Constants.NES_SUBJ_REG_NS, "studySubject"), StudySubject.class,
				studySubject);

	}

	private edu.duke.cabig.c3pr.webservice.common.StudySubjectProtocolVersionRelationship getStudySubjectProtocolVersion(
			CollectionProtocol cp) {
		StudySubjectProtocolVersionRelationship studySubjectProtocolVersion = new StudySubjectProtocolVersionRelationship();
		Site studySite = cp.getIrbSite();
		studySubjectProtocolVersion
				.setStudySiteProtocolVersion(new StudySiteProtocolVersionRelationship());

		// setup study
		studySubjectProtocolVersion.getStudySiteProtocolVersion()
				.setStudyProtocolVersion(new StudyProtocolVersion());
		studySubjectProtocolVersion.getStudySiteProtocolVersion()
				.getStudyProtocolVersion()
				.setStudyProtocolDocument(new StudyProtocolDocumentVersion());
		studySubjectProtocolVersion.getStudySiteProtocolVersion()
				.getStudyProtocolVersion().getStudyProtocolDocument()
				.setPublicTitle(h.ST(cp.getTitle()));
		studySubjectProtocolVersion.getStudySiteProtocolVersion()
				.getStudyProtocolVersion().getStudyProtocolDocument()
				.setOfficialTitle(h.ST(cp.getShortTitle()));
		studySubjectProtocolVersion.getStudySiteProtocolVersion()
				.getStudyProtocolVersion().getStudyProtocolDocument()
				.setPublicDescription(h.ST(cp.getDescriptionURL()));
		studySubjectProtocolVersion.getStudySiteProtocolVersion()
				.getStudyProtocolVersion().getStudyProtocolDocument()
				.setDocument(new Document());
		studySubjectProtocolVersion.getStudySiteProtocolVersion()
				.getStudyProtocolVersion().getStudyProtocolDocument()
				.getDocument().getDocumentIdentifier()
				.add(convertToDocumentIdentifier(cp));

		// setup studysite
		if (studySite != null) {
			studySubjectProtocolVersion
					.getStudySiteProtocolVersion()
					.setStudySite(
							new edu.duke.cabig.c3pr.webservice.common.StudySite());
			studySubjectProtocolVersion.getStudySiteProtocolVersion()
					.getStudySite().setOrganization(new Organization());
			studySubjectProtocolVersion.getStudySiteProtocolVersion()
					.getStudySite().getOrganization()
					.getOrganizationIdentifier()
					.add(convertToOrganizationIdentifier(studySite));
		}
		return studySubjectProtocolVersion;
	}

	@Override
	public OrganizationIdentifier convertToOrganizationIdentifier(Site site) {
		OrganizationIdentifier id = new OrganizationIdentifier();
		if (site!=null && StringUtils.isNotBlank(site.getCtepId())) {
			id.setTypeCode(h.CD(CTEP));
			id.setIdentifier(h.II(site.getCtepId()));
			id.setPrimaryIndicator(h.BL(true));
		}
		return id;

	}

	@Override
	public DocumentIdentifier convertToDocumentIdentifier(CollectionProtocol cp) {
		DocumentIdentifier studyId = new DocumentIdentifier();
		if (cp!=null && StringUtils.isNotBlank(cp.getIrbIdentifier())) {
			studyId.setTypeCode(h.CD(COORDINATING_CENTER_IDENTIFIER));
			studyId.setIdentifier(h.II(cp.getIrbIdentifier()));
			studyId.setPrimaryIndicator(h.BL(true));
			if (cp.getIrbSite() != null) {
				Site site = cp.getIrbSite();
				final edu.duke.cabig.c3pr.webservice.common.Organization org = new edu.duke.cabig.c3pr.webservice.common.Organization();
				studyId.setAssigningOrganization(org);
				if (StringUtils.isNotBlank(site.getCtepId())) {
					OrganizationIdentifier orgId = new OrganizationIdentifier();
					orgId.setTypeCode(h.CD(CTEP));
					orgId.setIdentifier(h.II(site.getCtepId()));
					orgId.setPrimaryIndicator(h.BL(true));
					org.getOrganizationIdentifier().add(orgId);
				}
			}
		}
		return studyId;
	}

	@Override
	public SubjectIdentifier convertToSubjectIdentifier(String ppi,
			CollectionProtocol cp) {

		SubjectIdentifier id = new SubjectIdentifier();
		if (StringUtils.isNotBlank(ppi)) {
			id.setIdentifier(h.II(ppi));
			id.setTypeCode(h
					.CD(COORDINATING_CENTER_ASSIGNED_STUDY_SUBJECT_IDENTIFIER));
			id.setPrimaryIndicator(h.BL(true));
			if (cp!=null && cp.getIrbSite() != null
					&& StringUtils.isNotBlank(cp.getIrbSite().getCtepId())) {
				Organization org = new Organization();
				OrganizationIdentifier orgId = new OrganizationIdentifier();
				orgId.setTypeCode(h.CD(CTEP));
				orgId.setIdentifier(h.II(cp.getIrbSite().getCtepId()));
				orgId.setPrimaryIndicator(h.BL(true));
				org.getOrganizationIdentifier().add(orgId);
				id.setAssigningOrganization(org);
			}

		}
		return id;
	}
}
