
package com.krishagni.catissueplus.core.services.testdata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentTier;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CprRegistrationDetails;
import com.krishagni.catissueplus.core.biospecimen.events.CreateBulkRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantRegistrationDetails;

import edu.wustl.common.beans.SessionDataBean;

public class CprTestData {

	private static final String NOT_SPECIFIED = "Not Specified";

	private static final String ACTIVITY_STATUS_DISABLED = "Disabled";

	private static final String ACTIVITY_STATUS_ACTIVE = "Active";

	private static final String PPID = "participant protocol identifier";

	public static DeleteRegistrationEvent getDeleteCprWithActiveChildren() {
		DeleteRegistrationEvent event = new DeleteRegistrationEvent();
		event.setId(1l);
		event.setIncludeChildren(false);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static CollectionProtocol getCpToReturnWithConsents() {
		Long cpId = 1l;
		CollectionProtocol protocol = getCollectionProtocol(cpId);
		ConsentTier tier = new ConsentTier();
		tier.setStatement("statement1");
		tier.setId(1l);
		
		ConsentTier tier1 = new ConsentTier();
		tier1.setStatement("statement2");
		tier1.setId(2l);
		
		ConsentTier tier2 = new ConsentTier();
		tier2.setStatement("statement3");
		tier2.setId(3l);
		
		Set<ConsentTier> consentTierCollection = new HashSet<ConsentTier>();
		consentTierCollection.add(tier);
		consentTierCollection.add(tier1);
		consentTierCollection.add(tier2);
		
		protocol.setConsentTier(consentTierCollection);
		return protocol;
	}

	public static CollectionProtocol getCpToReturnWithEmptyConsents() {
		Long cpId = 1l;
		CollectionProtocol protocol = getCollectionProtocol(cpId);
		Set<ConsentTier> consentTierCollection = new HashSet<ConsentTier>();
		protocol.setConsentTier(consentTierCollection);
		return protocol;
	}

	public static CreateRegistrationEvent getCprCreateEventEmptyPpid() {
		CreateRegistrationEvent reqEvent = new CreateRegistrationEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		CollectionProtocolRegistrationDetail details = getRegistrationDetails(1l);
		details.setPpid("");
		reqEvent.setCprDetail(details);
		return reqEvent;
	}

//	public static ReqSpecimenCollGroupSummaryEvent getScgListEvent() {
//		ReqSpecimenCollGroupSummaryEvent event = new ReqSpecimenCollGroupSummaryEvent();
//		event.setCprId(1l);
//		return event;
//	}

	public static DeleteRegistrationEvent getCprDeleteEvent() {
		DeleteRegistrationEvent event = new DeleteRegistrationEvent();
		event.setId(1l);
		event.setIncludeChildren(true);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static CollectionProtocolRegistration getCprToReturn() {
		CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
		cpr.setId(1l);
		cpr.setActive();
		cpr.setCollectionProtocol(getCollectionProtocol(1l));
		cpr.setScgCollection(getScgCollection(cpr));
		return cpr;
	}

	private static Collection<SpecimenCollectionGroup> getScgCollection(CollectionProtocolRegistration cpr) {
		Set<SpecimenCollectionGroup> scgColl = new HashSet<SpecimenCollectionGroup>();
		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg.setActive();
		scg.setBarcode("barcode");
		scg.setClinicalDiagnosis(NOT_SPECIFIED);
		scg.setClinicalStatus(NOT_SPECIFIED);
		scg.setCollectionComments("collectionComments");
		scg.setCollectionContainer(NOT_SPECIFIED);
		scg.setCollectionProcedure(NOT_SPECIFIED);
		scg.setCollectionProtocolRegistration(cpr);
		scg.setCollectionStatus("Complete");
		scg.setCollectionTimestamp(new Date());
		scg.setName("scg_name");
		scg.setSpecimenCollection(getSpecimenCollection(scg));
		scgColl.add(scg);
		return scgColl;
	}

	private static Set<Specimen> getSpecimenCollection(SpecimenCollectionGroup scg) {
		Set<Specimen> specimenColl = new HashSet<Specimen>();
		Specimen specimen = new Specimen();
		specimen.setActive();
		specimen.setSpecimenCollectionGroup(scg);
		specimenColl.add(specimen);
		return specimenColl;
	}

	public static CollectionProtocol getCollectionProtocol(Long cpId) {
		CollectionProtocol protocol = new CollectionProtocol();
		protocol.setId(cpId);
		protocol.setTitle("Test case CP");
		return protocol;
	}

	private static CollectionProtocolRegistrationDetail getRegistrationDetails(Long cpId) {
		CollectionProtocolRegistrationDetail registrationDetails = new CollectionProtocolRegistrationDetail();
		registrationDetails.setCpId(cpId);
		registrationDetails.setRegistrationDate(new Date());
		registrationDetails.setBarcode("barcode1");
		registrationDetails.setPpid("cpr_ppi");
		registrationDetails.setParticipant(getParticipantDto());
		registrationDetails.setConsentDetails(getConsentRespDetails(registrationDetails));
		return registrationDetails;
	}

	private static ConsentDetail getConsentRespDetails(CollectionProtocolRegistrationDetail registrationDetails) {
		ConsentDetail responseDetails = new ConsentDetail();
		responseDetails.setConsentSignatureDate(new Date());
		responseDetails.setConsentDocumentUrl("www.google.com");
		responseDetails.setWitnessName("admin@admin.com");
		responseDetails.setConsenTierStatements(getConsentTierList());
		return responseDetails;
	}

	private static List<ConsentTierDetail> getConsentTierList() {
		ConsentTierDetail tierDetail0 = new ConsentTierDetail();
		tierDetail0.setConsentStatment("statement1");
		tierDetail0.setParticipantResponse("Yes");
		
		ConsentTierDetail tierDetail1 = new ConsentTierDetail();
		tierDetail1.setConsentStatment("statement2");
		tierDetail1.setParticipantResponse("No");
		
		ConsentTierDetail tierDetail2 = new ConsentTierDetail();
		tierDetail2.setConsentStatment("statement3");
		tierDetail2.setParticipantResponse("Not Specified");
		List<ConsentTierDetail> list = new ArrayList<ConsentTierDetail>();
		list.add(tierDetail0);
		list.add(tierDetail1);
		list.add(tierDetail2);
		return list;
	}

	private static ParticipantDetail getParticipantDto() {
		ParticipantDetail participantDto = new ParticipantDetail();
		participantDto.setFirstName("firstName");
		participantDto.setLastName("lastName");
		participantDto.setGender("male");
		participantDto.setId(1l);
		return participantDto;
	}

	private static SessionDataBean getSessionDataBean() {
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

	private static Set getCpeCollection(CollectionProtocol protocol) {
		Set<CollectionProtocolEvent> cpes = new HashSet<CollectionProtocolEvent>();
		CollectionProtocolEvent event = new CollectionProtocolEvent();
		event.setActivityStatus(ACTIVITY_STATUS_ACTIVE);
		event.setClinicalDiagnosis(NOT_SPECIFIED);
		event.setClinicalStatus(NOT_SPECIFIED);
		event.setCollectionPointLabel("visit1");
		event.setStudyCalendarEventPoint(0.0);
		event.setCollectionProtocol(protocol);
		event.setSpecimenRequirements(getSpecimenRequirementColl(event));
		cpes.add(event);
		return cpes;
	}

	private static Set<SpecimenRequirement> getSpecimenRequirementColl(CollectionProtocolEvent event) {
		Set<SpecimenRequirement> requirements = new HashSet<SpecimenRequirement>();
		SpecimenRequirement requirement = new SpecimenRequirement();
		requirement.setActivityStatus(ACTIVITY_STATUS_ACTIVE);
		requirement.setCollectionComments("comments");
		requirement.setCollectionContainer(NOT_SPECIFIED);
		requirement.setCollectionProcedure(NOT_SPECIFIED);
		requirement.setCollectionProtocolEvent(event);
		requirement.setCollectionTimestamp(new Date());
		requirement.setCollector(getOldUser(1l));
		requirement.setInitialQuantity(1.0);
		requirement.setLineage("New");
		requirement.setPathologicalStatus(NOT_SPECIFIED);
		requirement.setReceivedComments("comments");
		requirement.setReceivedQuality(NOT_SPECIFIED);
		requirement.setReceivedTimestamp(new Date());
		requirement.setReceiver(getOldUser(1l));
		requirement.setTissueSide(NOT_SPECIFIED);
		requirement.setTissueSite(NOT_SPECIFIED);
		requirement.setSpecimenClass("Fluid");
		requirement.setSpecimenType("Feces");
		requirement.setStorageType("Manual");
		requirements.add(requirement);
		return requirements;
	}
	
	private static User getOldUser(Long userId) {
		User user = new User();
		user.setId(userId);
		user.setFirstName("firstName");
		user.setLastName("lastName");
		return user;
	}

	private static User getUser(Long userId) {
		User user = new User();
		user.setId(userId);
		user.setFirstName("firstName");
		user.setLastName("lastName");
		return user;
	}

//	public static List<SpecimenCollectionGroupInfo> getSCGSummaryList() {
//		List<SpecimenCollectionGroupInfo> groupsInfo = new ArrayList<SpecimenCollectionGroupInfo>();
//		SpecimenCollectionGroupInfo groupInfo = new SpecimenCollectionGroupInfo();
//		groupInfo.setCollectionStatus("Collected");
//		groupInfo.setName("test scg");
//		groupInfo.setReceivedDate(new Date());
//		groupInfo.setId(1l);
//		groupsInfo.add(groupInfo);
//
//		return groupsInfo;
//	}

	public static Participant getParticipant() {
		Participant participant = new Participant();
		participant.setId(1l);
		return participant;
	}

	public static CollectionProtocol getCptoReturn() {
		Long cpId = 1l;
		CollectionProtocol protocol = getCollectionProtocol(cpId);
		protocol.setCollectionProtocolEvents(getCpeCollection(protocol));
		return protocol;
	}

	public static CreateRegistrationEvent getCprCreateEvent() {
		CreateRegistrationEvent reqEvent = new CreateRegistrationEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		reqEvent.setCprDetail(getRegistrationDetails(1l));
		return reqEvent;
	}
	
	public static CreateBulkRegistrationEvent getCreateBulkRegEvent() {
		CreateBulkRegistrationEvent req = new CreateBulkRegistrationEvent();
		ParticipantRegistrationDetails regDetail = new ParticipantRegistrationDetails();
		regDetail = getRegDetails();
		regDetail.getRegistrationDetails().add(getCprRegistrationDetails());
		regDetail.getRegistrationDetails().add(getCprRegistrationDetails());
		req.setParticipantDetails(regDetail);
		req.setSessionDataBean(getSessionDataBean());
		return req;
	}
	
	private static ParticipantRegistrationDetails getRegDetails() {
		ParticipantRegistrationDetails reg = new ParticipantRegistrationDetails();
		ParticipantDetail p = getParticipantDto();
		reg.setFirstName(p.getFirstName());
		reg.setLastName(p.getLastName());
		reg.setPmis(p.getPmis());
		return reg;
	}
	
	public static CprRegistrationDetails getCprRegistrationDetails() {
		CprRegistrationDetails cpr = new CprRegistrationDetails();
		cpr.setCpId(1L);
		cpr.setCprId(3L);
		cpr.setPpId("default-test-ppid");
		cpr.setRegistrationDate(new Date());
		cpr.setConsentResponseDetail(getConsentRespDetails(null));
		return cpr;
	}

	public static CreateRegistrationEvent getCprCreateEventDuplicatePpid() {
		CreateRegistrationEvent reqEvent = new CreateRegistrationEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		reqEvent.setCprDetail(getRegistrationDetails(1L));
		return reqEvent;
	}

	public static CollectionProtocol getCollectionProtocol() {
		return getCollectionProtocol(1l);
	}

	public static User getUser() {
		return getUser(1l);
	}
}
