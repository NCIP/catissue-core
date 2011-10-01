package edu.wustl.catissuecore.ctrp;

import edu.wustl.catissuecore.GSID.GSIDClient;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.AppUtility;
import gov.nih.nci.coppa.common.LimitOffset;
import gov.nih.nci.coppa.po.ClinicalResearchStaff;
import gov.nih.nci.coppa.po.CorrelationNode;
import gov.nih.nci.coppa.po.Organization;
import gov.nih.nci.coppa.po.Person;
import gov.nih.nci.coppa.services.business.business.client.BusinessClient;
import gov.nih.nci.coppa.services.entities.organization.client.OrganizationClient;
import gov.nih.nci.coppa.services.entities.person.client.PersonClient;
import gov.nih.nci.coppa.services.pa.StudyContact;
import gov.nih.nci.coppa.services.pa.StudyProtocol;
import gov.nih.nci.coppa.services.pa.studycontactservice.client.StudyContactServiceClient;
import gov.nih.nci.coppa.services.pa.studyprotocolservice.client.StudyProtocolServiceClient;
import gov.nih.nci.coppa.services.structuralroles.clinicalresearchstaff.client.ClinicalResearchStaffClient;
import gov.nih.nci.iso21090.extensions.Bl;
import gov.nih.nci.iso21090.extensions.Cd;
import gov.nih.nci.iso21090.extensions.Id;
import gov.nih.nci.services.RoleList;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.gsi.GlobusCredential;
import org.iso._21090.CD;
import org.iso._21090.DSETTEL;
import org.iso._21090.ENON;
import org.iso._21090.ENPN;
import org.iso._21090.ENXP;
import org.iso._21090.EntityNamePartType;
import org.iso._21090.II;
import org.iso._21090.ST;
import org.iso._21090.TEL;

/****
 * @author Ravi Batchu
 */

public class COPPAServiceClient {

	private static Log logger = LogFactory.getLog(COPPAServiceClient.class);
	private static final String ACTIVE_STATUS_CODE_VAL = "ACTIVE";

	public COPPAServiceClient() throws Exception {
	}

	/**
	 * Gets CTRP user name and password from ctrp.properites Logs into Dorian
	 * and get grid credential
	 * 
	 * @return
	 * @throws Exception
	 */
	private GlobusCredential getGlobusCredential() throws Exception {
		String userLogin = CTRPPropertyHandler
				.getProperty(CTRPConstants.CTRP_USER_NAME_KEY);
		String userPassword = CTRPPropertyHandler
				.getProperty(CTRPConstants.CTRP_PASSWORD_KEY);
		logger.debug(CTRPConstants.CTRP_USER_NAME_KEY + ":" + userLogin);
		
		try {
			GSIDClient.installRootCertsAndSync();   
		} catch (Exception e) {
			logger.error(e,e);     
		}

		GlobusCredential credential = CTRPGlobusCredentialCache.getCredential(
				userLogin, userPassword);
		return credential;
	}

	/**
	 * @param institution
	 * @return
	 * @throws Exception
	 *             Searches COPPA and returns list of institution matches
	 */
	public Organization[] searchOrganization(Institution institution)
			throws Exception {
		LimitOffset limit = new LimitOffset();
		limit.setLimit(100);
		limit.setOffset(0);

		Organization sp = new Organization();
		ENXP namePart = new ENXP();
		namePart.setValue(institution.getName());
		ENON name = new ENON();
		name.getPart().add(namePart);
		sp.setName(name);
		CD statusCode = new CD();
		statusCode.setCode(ACTIVE_STATUS_CODE_VAL);
		sp.setStatusCode(statusCode);
		String organizationServiceURL = CTRPPropertyHandler
				.getProperty(CTRPConstants.CTRP_PO_ORGANIZATION_SERVICE_URL_KEY);
		logger.debug(CTRPConstants.CTRP_PO_ORGANIZATION_SERVICE_URL_KEY + ":"
				+ organizationServiceURL);

		Organization[] results = new OrganizationClient(organizationServiceURL,
				getGlobusCredential()).query(sp, limit);

		if (results != null) {
			for (Organization org : results) {
				String identifier = org.getIdentifier().getExtension();
				String nameValue = org.getName().getPart().get(0).getValue();
				logger.debug((identifier + ":" + nameValue + ":"
						+ COPPAUtil.getCity(org.getPostalAddress()) + ":" + COPPAUtil
						.getState(org.getPostalAddress())));
			}
		} else {
			logger.debug("NO Results Found for:" + institution.getName());

		}
		return results;

	}

	/**
	 * @param institutionName
	 * @return
	 * @throws Exception
	 *             Searches organization by name and returns list of matches
	 */
	public Organization[] searchOrganization(String institutionName)
			throws Exception {
		Institution institution = new Institution();
		institution.setName(institutionName);
		return searchOrganization(institution);
	}

	/**
	 * @param coppaId
	 * @return
	 * @throws Exception
	 *             Retrieves organization by coppa id : root+extn
	 */
	public Organization getOrganizationById(String coppaId) throws Exception {

		Id id = new Id();
		id.setRoot(CTRPConstants.COPPA_ORGANIZATION_ROOT);
		id.setIdentifierName(CTRPConstants.COPPA_ORGANIZATION_IDENTIFIER_NAME);
		id.setExtension(coppaId);
		String organizationServiceURL = CTRPPropertyHandler
				.getProperty(CTRPConstants.CTRP_PO_ORGANIZATION_SERVICE_URL_KEY);
		logger.debug(CTRPConstants.CTRP_PO_ORGANIZATION_SERVICE_URL_KEY + ":"
				+ organizationServiceURL);

		Organization result = new OrganizationClient(organizationServiceURL,
				getGlobusCredential()).getById(id);
		return result;
	}

	/**
	 * @param coppaId
	 * @return
	 * @throws Exception
	 *             Retrieves organization by coppa id : root+extn and transforms
	 *             into domain institution object
	 */
	public Institution getRemoteInstituionById(String coppaId) throws Exception {
		Organization org = getOrganizationById(coppaId);
		return (Institution) new COPPAInstitutionTransformer().transform(org);
	}

	/**
	 * @param user
	 * @return
	 * @throws Exception
	 *             Searches COPPA and returns a list of matching persons based
	 *             on user's first name, last name and email address
	 */
	public Person[] searchPerson(User user) throws Exception {

		LimitOffset limit = new LimitOffset();
		limit.setLimit(100);
		limit.setOffset(0);

		String lastName = user.getLastName();
		String firstName = user.getFirstName();
		String emailAddress = user.getEmailAddress();

		Person searchPerson = new Person();

		CD statusCode = new CD();
		statusCode.setCode(ACTIVE_STATUS_CODE_VAL);
		searchPerson.setStatusCode(statusCode);

		ENXP lastNamePart = new ENXP();
		ENXP firstNamePart = new ENXP();
		ENPN name = new ENPN();
		if (AppUtility.isNotEmpty(lastName)) {
			lastNamePart.setValue(user.getLastName());
			lastNamePart.setType(EntityNamePartType.FAM);
			name.getPart().add(lastNamePart);
		}

		if (AppUtility.isNotEmpty(firstName)) {
			firstNamePart.setValue(user.getFirstName());
			firstNamePart.setType(EntityNamePartType.GIV);
			name.getPart().add(firstNamePart);
		}
		if (AppUtility.isNotEmpty(lastName) || AppUtility.isNotEmpty(firstName)) {
			searchPerson.setName(name);
		}

		if (AppUtility.isNotEmpty(emailAddress)) {
			TEL emailTEL = new TEL();
			emailTEL.setValue(COPPAUtil.toCOPPAEmailFormat(emailAddress));
			DSETTEL emailDSETTEL = new DSETTEL();
			emailDSETTEL.getItem().add(emailTEL);
			searchPerson.setTelecomAddress(emailDSETTEL);
		}
		String personServiceURL = CTRPPropertyHandler
				.getProperty(CTRPConstants.CTRP_PO_PERSON_SERVICE_URL_KEY);
		logger.debug(CTRPConstants.CTRP_PO_PERSON_SERVICE_URL_KEY + ":"
				+ personServiceURL);

		Person[] results = new PersonClient(personServiceURL,
				getGlobusCredential()).query(searchPerson, limit);

		if (results != null) {
			for (Person person : results) {
				String identifier = person.getIdentifier().getExtension();
				String nameValue = person.getName().getPart().get(0).getValue()
						+ " " + person.getName().getPart().get(1).getValue();
				String extn = COPPAUtil.getRemoteIdentifier(person);
				logger.debug((identifier + ":" + nameValue + ":"
						+ COPPAUtil.getCity(person) + ":"
						+ COPPAUtil.getEmailAddress(person) + ":" + COPPAUtil
						.getState(person)) + ":" + extn);
			}
		} else {
			logger.debug("NO Results Found for:" + user.getFirstTimeLogin()
					+ " " + user.getLastName());

		}
		return results;
	}

	public User[] searchRemoteUsers(User user) throws Exception {
		Person[] persons = searchPerson(user);
		return (User[]) new COPPAUserTransformer()
				.transformObjectGroup(persons);
	}

	/**
	 * @param coppaId
	 * @return
	 * @throws Exception
	 *             Retrieves NCI person entity by entity id
	 * */
	public Person getPersonById(String coppaId) throws Exception {

		Id id = new Id();
		id.setRoot(CTRPConstants.COPPA_PERSON_ROOT);
		id.setIdentifierName(CTRPConstants.COPPA_PERSON_IDENTIFIER_NAME);
		id.setExtension(coppaId);
		String organizationServiceURL = CTRPPropertyHandler
				.getProperty(CTRPConstants.CTRP_PO_PERSON_SERVICE_URL_KEY);
		logger.debug(CTRPConstants.CTRP_PO_PERSON_SERVICE_URL_KEY + ":"
				+ organizationServiceURL);

		Person result = new PersonClient(organizationServiceURL,
				getGlobusCredential()).getById(id);
		return result;
	}

	/**
	 * @param coppaId
	 * @return
	 * @throws Exception
	 *             Retrieves NCI person entity by entity id
	 * */
	public User getRemoteUserById(String coppaId) throws Exception {

		Person person = getPersonById(coppaId);
		return (User) new COPPAUserTransformer().transform(person);
	}

	public List<Institution> getRemoteInstitutionsByUserId(String coppaId)
			throws Exception {

		// To indicate that we are interested in organizations with health care
		// provider role for the given Person
		Cd cd = new Cd();
		cd.setCode(RoleList.HEALTH_CARE_PROVIDER.toString());

		// Create a person ID to get related organization correlations
		Id id = new Id();
		id.setRoot(CTRPConstants.COPPA_PERSON_ROOT);
		id.setIdentifierName(CTRPConstants.COPPA_PERSON_IDENTIFIER_NAME);
		id.setExtension(coppaId);

		Id[] queryIds = new Id[1];
		queryIds[0] = id;

		// To indicate that client does not need expanded details for person
		// entity
		Bl myTrue = new Bl();
		myTrue.setValue(true);

		// To indicate that client does not need expanded details for
		// organization entity
		Bl myFalse = new Bl();
		myFalse.setValue(true);

		String poBusinessServiceURL = CTRPPropertyHandler
				.getProperty(CTRPConstants.CTRP_PO_BUSINESS_SERVICE_URL_KEY);
		logger.debug(CTRPConstants.CTRP_PO_BUSINESS_SERVICE_URL_KEY + ":"
				+ poBusinessServiceURL);

		BusinessClient client = new BusinessClient(poBusinessServiceURL,
				getGlobusCredential());

		CorrelationNode[] remoteCorrelations = client
				.getCorrelationsByPlayerIdsWithEntities(cd, queryIds, myTrue,
						myFalse);
		COPPAInstitutionTransformer transformer = new COPPAInstitutionTransformer();
		List<Institution> institutionList = new ArrayList<Institution>();
		if (AppUtility.isNotEmpty(remoteCorrelations)) {
			for (CorrelationNode remoteCorrelation : remoteCorrelations) {
				List scoperObjectList = remoteCorrelation.getScoper()
						.getContent();
				if (AppUtility.isNotEmpty(scoperObjectList)) {
					Organization org = (Organization) scoperObjectList.get(0);
					Institution ins = transformer.transform(org);
					institutionList.add(ins);
					logger.debug("Remote Institution for:" + coppaId + ":"
							+ ins.getName() + ":" + ins.getRemoteId());
				}// if scoper list null
			}// for remote institution association list
		}// if remoteCorrelations list null
		return institutionList;
	}

	public StudyProtocol[] searchSutdyProtocol(
			CollectionProtocol collectionProtocol) throws Exception {
		LimitOffset limit = new LimitOffset();
		limit.setLimit(100);
		limit.setOffset(0);

		StudyProtocol searchStudyProtocol = new StudyProtocol();
		ST fullTitle = new ST();
		fullTitle.setValue(collectionProtocol.getTitle());
		searchStudyProtocol.setOfficialTitle(fullTitle);
		// CD statusCode = new CD();
		// statusCode.setCode(ACTIVE_STATUS_CODE_VAL);
		// sp.setStatusCode(statusCode);
		String studyProtocolServiceURL = CTRPPropertyHandler
				.getProperty(CTRPConstants.CTRP_PA_STUDY_PROTOCOL_SERVICE_URL_KEY);
		logger.debug(CTRPConstants.CTRP_PA_STUDY_PROTOCOL_SERVICE_URL_KEY + ":"
				+ studyProtocolServiceURL);

		StudyProtocol[] results = new StudyProtocolServiceClient(
				studyProtocolServiceURL, getGlobusCredential()).search(
				searchStudyProtocol, limit);

		if (results != null) {
			for (StudyProtocol sp : results) {
				logger.debug("Study Protocol Search Results for "
						+ collectionProtocol.getTitle() + ":"
						+ sp.getIdentifier().getExtension() + ":"
						+ sp.getOfficialTitle().getValue());
			}
		} else {
			logger.debug("NO Results Found for:"
					+ collectionProtocol.getTitle());

		}
		return results;

	}

	public StudyProtocol getSutdyProtocolById(String coppaId) throws Exception {
		Id id = new Id();
		id.setRoot(CTRPConstants.COPPA_STUDY_PROTOCOL_ROOT);
		id.setIdentifierName(CTRPConstants.COPPA_STUDY_PROTOCOL_IDENTIFIER_NAME);
		id.setExtension(coppaId);
		String studyProtocolServiceURL = CTRPPropertyHandler
				.getProperty(CTRPConstants.CTRP_PA_STUDY_PROTOCOL_SERVICE_URL_KEY);
		logger.debug(CTRPConstants.CTRP_PA_STUDY_PROTOCOL_SERVICE_URL_KEY + ":"
				+ studyProtocolServiceURL);

		StudyProtocol result = new StudyProtocolServiceClient(
				studyProtocolServiceURL, getGlobusCredential())
				.getStudyProtocol(id);
		if (result != null) {

			logger.debug("Retrieved Study Protocol data for " + coppaId + ":"
					+ result.getOfficialTitle().getValue());

		} else {
			logger.debug("No Study Protocol found for:" + coppaId);

		}
		return result;
	}

	public CollectionProtocol getRemoteProtocolById(String coppaId) throws Exception {
		StudyProtocol studyProtocol = getSutdyProtocolById(coppaId);
		return (CollectionProtocol) new COPPAProtocolTransformer().transform(studyProtocol);
	}
	
	public Person getPrincipalInvestigatorByStudyProtocolId(
			String coppaProtocolIdStr) throws Exception {
		// Get Study Contact by study protocol id and principal investigator
		// role
		Id studyProtocolId = new Id();
		studyProtocolId.setRoot(CTRPConstants.COPPA_STUDY_PROTOCOL_ROOT);
		studyProtocolId
				.setIdentifierName(CTRPConstants.COPPA_STUDY_PROTOCOL_IDENTIFIER_NAME);
		studyProtocolId.setExtension(coppaProtocolIdStr);

		String studyContactServiceURL = CTRPPropertyHandler
				.getProperty(CTRPConstants.CTRP_PA_STUDY_CONTACT_SERVICE_URL_KEY);
		logger.debug(CTRPConstants.CTRP_PA_STUDY_CONTACT_SERVICE_URL_KEY + ":"
				+ studyContactServiceURL);

		StudyContact searchStudyContact = new StudyContact();
		CD principalInvestigatorRoleCode = new CD();
		principalInvestigatorRoleCode
				.setCode(CTRPConstants.COPPA_STUDY_PI_ROLE_CODE);
		searchStudyContact.setRoleCode(principalInvestigatorRoleCode);

		CD activeStatusCode = new CD();
		activeStatusCode.setCode(ACTIVE_STATUS_CODE_VAL);
		// TODO uncomment active status code
		// searchStudyContact.setStatusCode(activeStatusCode);

		StudyContact[] studyContacts = new StudyContactServiceClient(
				studyContactServiceURL, getGlobusCredential())
				.getByStudyProtocolAndRole(studyProtocolId, searchStudyContact);
		if (AppUtility.isEmpty(studyContacts)) {
			logger.debug("No active principal investigators found for protocol id:"
					+ coppaProtocolIdStr);
			return null;
		}
		StudyContact studyContact = studyContacts[0];
		II clinicalResearchStaffII = studyContact.getClinicalResearchStaff();
		if (clinicalResearchStaffII == null) {
			logger.debug("No clincial research staff details found in study contact for protocol id:"
					+ coppaProtocolIdStr);
			return null;
		}

		// Get Clinical Research Staff identifier from study contact and
		// retrieve player details from PO clinical research staff service
		String crStaffServiceURL = CTRPPropertyHandler
				.getProperty(CTRPConstants.CTRP_PO_CLINICAL_RESEARCH_STAFF_SERVICE_URL_KEY);
		logger.debug(CTRPConstants.CTRP_PO_CLINICAL_RESEARCH_STAFF_SERVICE_URL_KEY
				+ ":" + studyContactServiceURL);

		Id clinicalResearchStaffId = new Id();
		clinicalResearchStaffId.setRoot(clinicalResearchStaffII.getRoot());
		clinicalResearchStaffId.setIdentifierName(clinicalResearchStaffII
				.getIdentifierName());
		clinicalResearchStaffId.setExtension(clinicalResearchStaffII
				.getExtension());

		ClinicalResearchStaff crStaff = new ClinicalResearchStaffClient(
				crStaffServiceURL, getGlobusCredential())
				.getById(clinicalResearchStaffId);

		if (crStaff == null) {
			logger.debug("No clincial research staff data found for clinicalResearchStaffId:"
					+ clinicalResearchStaffII.getExtension());
			return null;
		}
		II crStaffPlayerII = crStaff.getPlayerIdentifier();
		if (crStaffPlayerII == null) {
			logger.debug("No player data found for clinicalResearchStaffId:"
					+ clinicalResearchStaffII.getExtension());
			return null;
		}
		// Get Person information for clinical research role player
		Id personId = new Id();
		personId.setRoot(CTRPConstants.COPPA_PERSON_ROOT);
		personId.setIdentifierName(CTRPConstants.COPPA_PERSON_IDENTIFIER_NAME);
		personId.setExtension(crStaffPlayerII.getExtension());

		String personServiceURL = CTRPPropertyHandler
				.getProperty(CTRPConstants.CTRP_PO_PERSON_SERVICE_URL_KEY);
		logger.debug(CTRPConstants.CTRP_PO_PERSON_SERVICE_URL_KEY + ":"
				+ personServiceURL);

		Person piPerson = new PersonClient(personServiceURL,
				getGlobusCredential()).getById(personId);
		if (piPerson != null) {

			logger.debug("Retrieved Principal investigator for "
					+ coppaProtocolIdStr + ":"
					+ COPPAUtil.getFirstName(piPerson) + " "
					+ COPPAUtil.getLastName(piPerson));
		} else {
			logger.debug("No Study Protocol found for:" + coppaProtocolIdStr);
			return null;
		}
		// TODO Start here - Fill ctrp.properties with clinical research staff
		// url and study contact url and test this method.
		return piPerson;
	}

	public StudyProtocol getDiseaseCodesByProtocolId(String coppaId) throws Exception {
		Id id = new Id();
		id.setRoot(CTRPConstants.COPPA_STUDY_PROTOCOL_ROOT);
		id.setIdentifierName(CTRPConstants.COPPA_STUDY_PROTOCOL_IDENTIFIER_NAME);
		id.setExtension(coppaId);
		String studyProtocolServiceURL = CTRPPropertyHandler
				.getProperty(CTRPConstants.CTRP_PA_STUDY_PROTOCOL_SERVICE_URL_KEY);
		logger.debug(CTRPConstants.CTRP_PA_STUDY_PROTOCOL_SERVICE_URL_KEY + ":"
				+ studyProtocolServiceURL);

		StudyProtocol result = new StudyProtocolServiceClient(
				studyProtocolServiceURL, getGlobusCredential())
				.getStudyProtocol(id);
		if (result != null) {

			logger.debug("Retrieved Study Protocol data for " + coppaId + ":"
					+ result.getOfficialTitle().getValue());

		} else {
			logger.debug("No Study Protocol found for:" + coppaId);

		}
		return result;
	}
	
}