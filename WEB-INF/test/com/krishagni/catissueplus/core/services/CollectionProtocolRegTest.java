
package com.krishagni.catissueplus.core.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolRegistrationFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenCollectionGroupFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.impl.CollectionProtocolRegistrationFactoryImpl;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.impl.ParticipantFactoryImpl;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.impl.SpecimenCollectionGroupFactoryImpl;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.impl.SpecimenFactoryImpl;
import com.krishagni.catissueplus.core.biospecimen.events.AllSpecimenCollGroupsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetails;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentResponseDetails;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierDetails;
import com.krishagni.catissueplus.core.biospecimen.events.CreateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetails;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenCollGroupSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenCollectionGroupInfo;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolDao;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolRegistrationDao;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.SiteDao;
import com.krishagni.catissueplus.core.biospecimen.repository.UserDao;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.impl.CollectionProtocolRegistrationServiceImpl;
import com.krishagni.catissueplus.core.common.events.EventStatus;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.beans.SessionDataBean;

public class CollectionProtocolRegTest {

	@Mock
	private DaoFactory daoFactory;

	@Mock
	private CollectionProtocolRegistrationDao registrationDao;

	@Mock
	private SiteDao siteDao;

	@Mock
	private UserDao userDao;

	@Mock
	private CollectionProtocolDao collectionProtocolDao;

	private CollectionProtocolRegistrationService registrationSvc;

	private CollectionProtocolRegistrationFactory registrationFactory;

	private ParticipantFactory participantFactory;

	private SpecimenCollectionGroupFactory scgFactory;

	private SpecimenFactory specimenFactory;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(daoFactory.getRegistrationDao()).thenReturn(registrationDao);
		when(daoFactory.getCollectionProtocolDao()).thenReturn(collectionProtocolDao);
		when(daoFactory.getSiteDao()).thenReturn(siteDao);
		when(daoFactory.getUserDao()).thenReturn(userDao);

		Site site = new Site();
		when(siteDao.getSite(anyString())).thenReturn(site);
		registrationSvc = new CollectionProtocolRegistrationServiceImpl();
		((CollectionProtocolRegistrationServiceImpl) registrationSvc).setDaoFactory(daoFactory);
		registrationFactory = new CollectionProtocolRegistrationFactoryImpl();
		((CollectionProtocolRegistrationFactoryImpl) registrationFactory).setDaoFactory(daoFactory);
		((CollectionProtocolRegistrationServiceImpl) registrationSvc).setRegistrationFactory(registrationFactory);
		participantFactory = new ParticipantFactoryImpl();
		((ParticipantFactoryImpl) participantFactory).setDaoFactory(daoFactory);
		((CollectionProtocolRegistrationFactoryImpl) registrationFactory).setParticipantFactory(participantFactory);

		specimenFactory = new SpecimenFactoryImpl();
		scgFactory = new SpecimenCollectionGroupFactoryImpl();
		((SpecimenCollectionGroupFactoryImpl) scgFactory).setSpecimenFactory(specimenFactory);

		((CollectionProtocolRegistrationFactoryImpl) registrationFactory).setScgFactory(scgFactory);

		when(collectionProtocolDao.isPpidUniqueForProtocol(anyLong(), anyString())).thenReturn(true);
		when(userDao.getUser(anyString())).thenReturn(getUser(1L));

	}

	@Test
	public void testForSuccessfulRegistrationCreation() {
		Long cpId = 1l;
		CollectionProtocol protocol = getCollectionProtocol(cpId);
		protocol.setCollectionProtocolEventCollection(getCpeCollection(protocol));
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(protocol);

		CreateRegistrationEvent reqEvent = new CreateRegistrationEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		reqEvent.setRegistrationDetails(getRegistrationDetails(cpId));

		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
		assertNotNull("Response cannot be null", response);
		CollectionProtocolRegistrationDetails actualResult = response.getRegistrationDetails();

		assertEquals(EventStatus.OK, response.getStatus());
		assertNotNull(actualResult);
		assertEquals(cpId, actualResult.getCpId());

	}

	@Test
	public void testForRegistrationDuplicatePpid() {
		when(collectionProtocolDao.isPpidUniqueForProtocol(anyLong(), anyString())).thenReturn(false);
		Long cpId = 1l;

		CollectionProtocol protocol = getCollectionProtocol(cpId);
		protocol.setCollectionProtocolEventCollection(getCpeCollection(protocol));
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(protocol);

		CreateRegistrationEvent reqEvent = new CreateRegistrationEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		reqEvent.setRegistrationDetails(getRegistrationDetails(cpId));

		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
		assertNotNull("Response cannot be null", response);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(ParticipantErrorCode.DUPLICATE_PPID.message() + " : participant protocol identifier",
				response.getMessage());
	}

	@Test
	public void testForRegistrationWithServerError() {
		Long cpId = 1l;

		CollectionProtocol protocol = getCollectionProtocol(cpId);
		protocol.setCollectionProtocolEventCollection(getCpeCollection(protocol));
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(protocol);
		doThrow(new RuntimeException()).when(registrationDao).saveOrUpdate(any(CollectionProtocolRegistration.class));
		CreateRegistrationEvent reqEvent = new CreateRegistrationEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		reqEvent.setRegistrationDetails(getRegistrationDetails(cpId));

		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
		assertNotNull("Response cannot be null", response);

		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testForSuccessfulRegistrationWithConsents() {
		Long cpId = 1l;
		CollectionProtocol protocol = getCollectionProtocol(cpId);
		ConsentTier tier = new ConsentTier();
		tier.setStatement("statement1");
		tier.setId(1l);
		Collection<ConsentTier> consentTierCollection = new HashSet<ConsentTier>();
		consentTierCollection.add(tier);
		protocol.setConsentTierCollection(consentTierCollection);
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(protocol);

		CreateRegistrationEvent reqEvent = new CreateRegistrationEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		reqEvent.setRegistrationDetails(getRegistrationDetails(cpId));

		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
		assertNotNull("Response cannot be null", response);
		CollectionProtocolRegistrationDetails actualResult = response.getRegistrationDetails();

		assertEquals(EventStatus.OK, response.getStatus());
		assertNotNull(actualResult);
		assertEquals(cpId, actualResult.getCpId());

	}

	@Test
	public void testForSuccessfulRegistrationWithEmptyConsents() {
		Long cpId = 1l;
		CollectionProtocol protocol = getCollectionProtocol(cpId);
		Collection<ConsentTier> consentTierCollection = new HashSet<ConsentTier>();
		protocol.setConsentTierCollection(consentTierCollection);
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(protocol);

		CreateRegistrationEvent reqEvent = new CreateRegistrationEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		reqEvent.setRegistrationDetails(getRegistrationDetails(cpId));

		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
		assertNotNull("Response cannot be null", response);
		CollectionProtocolRegistrationDetails actualResult = response.getRegistrationDetails();

		assertEquals(EventStatus.OK, response.getStatus());
		assertNotNull(actualResult);
		assertEquals(cpId, actualResult.getCpId());
	}

	@Test
	public void testRegistrationCreationEmptyPPID() {
		Long cpId = 1l;
		CollectionProtocol protocol = getCollectionProtocol(cpId);
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(protocol);

		CreateRegistrationEvent reqEvent = new CreateRegistrationEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		CollectionProtocolRegistrationDetails details = getRegistrationDetails(cpId);
		details.setPpid("");
		reqEvent.setRegistrationDetails(details);

		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(ParticipantErrorCode.MISSING_ATTR_VALUE.message() + " : participant protocol identifier",
				response.getMessage());

	}

	@Test
	public void testRegistrationCreationNullCP() {
		CreateRegistrationEvent reqEvent = new CreateRegistrationEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		CollectionProtocolRegistrationDetails details = getRegistrationDetails(null);
		details.setRegistrationDate(null);
		details.setPpid("");
		reqEvent.setRegistrationDetails(details);

		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(ParticipantErrorCode.MISSING_ATTR_VALUE.message() + " : collection protocol", response.getMessage());

	}

	@Test
	public void testGetScgListServerError() {
		Long cprId = 1l;
		ReqSpecimenCollGroupSummaryEvent event = new ReqSpecimenCollGroupSummaryEvent();
		event.setCollectionProtocolRegistrationId(1l);
		doThrow(new RuntimeException()).when(registrationDao).getSpecimenCollectiongroupsList(cprId);
		AllSpecimenCollGroupsSummaryEvent response = registrationSvc.getSpecimenCollGroupsList(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testGetScgList() {
		Long cprId = 1l;
		ReqSpecimenCollGroupSummaryEvent event = new ReqSpecimenCollGroupSummaryEvent();
		event.setCollectionProtocolRegistrationId(cprId);
		when(registrationDao.getSpecimenCollectiongroupsList(anyLong())).thenReturn(getSCGSummaryList(cprId));
		AllSpecimenCollGroupsSummaryEvent response = registrationSvc.getSpecimenCollGroupsList(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(getSCGSummaryList(cprId).size(), response.getSpecimenCollectionGroupsInfo().size());
	}

	@Test
	public void testRegistrationCreationInvalidCP() {
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(null);
		CreateRegistrationEvent reqEvent = new CreateRegistrationEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		CollectionProtocolRegistrationDetails details = getRegistrationDetails(1L);
		details.setPpid("");
		reqEvent.setRegistrationDetails(details);

		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(ParticipantErrorCode.INVALID_ATTR_VALUE.message() + " : collection protocol", response.getMessage());

	}

	private CollectionProtocol getCollectionProtocol(Long cpId) {
		CollectionProtocol protocol = new CollectionProtocol();
		protocol.setId(cpId);
		return protocol;
	}

	private CollectionProtocolRegistrationDetails getRegistrationDetails(Long cpId) {
		CollectionProtocolRegistrationDetails registrationDetails = new CollectionProtocolRegistrationDetails();
		registrationDetails.setCpId(cpId);
		registrationDetails.setRegistrationDate(new Date());
		registrationDetails.setBarcode("barcode1");
		registrationDetails.setPpid("cpr_ppi");
		registrationDetails.setParticipantDetails(getParticipantDto());
		registrationDetails.setConsentResponseDetails(getConsentRespDetails(registrationDetails));
		return registrationDetails;
	}

	private ConsentResponseDetails getConsentRespDetails(CollectionProtocolRegistrationDetails registrationDetails) {
		ConsentResponseDetails responseDetails = new ConsentResponseDetails();
		responseDetails.setConsentDate(new Date());
		responseDetails.setConsentUrl("www.google.com");
		responseDetails.setWitnessName("admin@admin.com");
		responseDetails.setConsentTierList(getConsentTierList());
		return responseDetails;
	}

	private List<ConsentTierDetails> getConsentTierList() {
		ConsentTierDetails tierDetails = new ConsentTierDetails();
		tierDetails.setConsentStatment("statement1");
		tierDetails.setParticipantResponse("Yes");
		List<ConsentTierDetails> list = new ArrayList<ConsentTierDetails>();
		list.add(tierDetails);
		return list;
	}

	private ParticipantDetails getParticipantDto() {
		ParticipantDetails participantDto = new ParticipantDetails();
		participantDto.setFirstName("firstName");
		participantDto.setLastName("lastName");
		participantDto.setGender("male");
		return participantDto;
	}

	private SessionDataBean getSessionDataBean() {
		SessionDataBean sessionDataBean = new SessionDataBean();
		sessionDataBean.setAdmin(true);
		sessionDataBean.setCsmUserId("1");
		sessionDataBean.setFirstName("admin");
		sessionDataBean.setIpAddress("127.0.0.1");
		sessionDataBean.setLastName("admin");
		sessionDataBean.setUserId(1L);
		sessionDataBean.setUserName("admin@admin.com");
		return sessionDataBean;
	}

	private Collection getCpeCollection(CollectionProtocol protocol) {
		Collection<CollectionProtocolEvent> cpes = new HashSet<CollectionProtocolEvent>();
		CollectionProtocolEvent event = new CollectionProtocolEvent();
		event.setActivityStatus("Active");
		event.setClinicalDiagnosis("Not Specified");
		event.setClinicalStatus("Not Specified");
		event.setCollectionPointLabel("visit1");
		event.setStudyCalendarEventPoint(0.0);
		event.setCollectionProtocol(protocol);
		event.setSpecimenRequirementCollection(getSpecimenRequirementColl(event));
		cpes.add(event);
		return cpes;
	}

	private Collection<SpecimenRequirement> getSpecimenRequirementColl(CollectionProtocolEvent event) {
		Collection<SpecimenRequirement> requirements = new HashSet<SpecimenRequirement>();
		SpecimenRequirement requirement = new SpecimenRequirement();
		requirement.setActivityStatus("Active");
		requirement.setCollectionComments("comments");
		requirement.setCollectionContainer("Not Specified");
		requirement.setCollectionProcedure("Not Specified");
		requirement.setCollectionProtocolEvent(event);
		requirement.setCollectionTimestamp(new Date());
		requirement.setCollector(getUser(1l));
		requirement.setInitialQuantity(1.0);
		requirement.setLineage("New");
		requirement.setPathologicalStatus("Not Specified");
		requirement.setReceivedComments("comments");
		requirement.setReceivedQuality("Not Specified");
		requirement.setReceivedTimestamp(new Date());
		requirement.setReceiver(getUser(1l));
		requirement.setSpecimenCharacteristics(null);
		requirement.setSpecimenClass("Fluid");
		requirement.setSpecimenType("Feces");
		requirement.setStorageType("Manual");
		requirements.add(requirement);
		return requirements;
	}

	private User getUser(Long userId) {
		User user = new User();
		user.setId(userId);
		user.setFirstName("firstName");
		user.setLastName("lastName");
		return user;
	}

	private List<SpecimenCollectionGroupInfo> getSCGSummaryList(Long cprId) {
		List<SpecimenCollectionGroupInfo> groupsInfo = new ArrayList<SpecimenCollectionGroupInfo>();
		SpecimenCollectionGroupInfo groupInfo = new SpecimenCollectionGroupInfo();
		groupInfo.setCollectionStatus("Collected");
		groupInfo.setName("test scg");
		groupInfo.setReceivedDate(new Date());
		groupInfo.setId(1l);
		groupsInfo.add(groupInfo);

		return groupsInfo;
	}

}
