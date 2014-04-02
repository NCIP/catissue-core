
package com.krishagni.catissueplus.core.services.testdata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentResponseDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CreateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenCollGroupSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenCollectionGroupInfo;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
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
		Collection<ConsentTier> consentTierCollection = new HashSet<ConsentTier>();
		consentTierCollection.add(tier);
		protocol.setConsentTierCollection(consentTierCollection);
		return protocol;
	}

	public static CollectionProtocol getCpToReturnWithEmptyConsents() {
		Long cpId = 1l;
		CollectionProtocol protocol = getCollectionProtocol(cpId);
		Collection<ConsentTier> consentTierCollection = new HashSet<ConsentTier>();
		protocol.setConsentTierCollection(consentTierCollection);
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

	public static ReqSpecimenCollGroupSummaryEvent getScgListEvent() {
		ReqSpecimenCollGroupSummaryEvent event = new ReqSpecimenCollGroupSummaryEvent();
		event.setCprId(1l);
		return event;
	}

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
		registrationDetails.setParticipantDetail(getParticipantDto());
		registrationDetails.setResponseDetail(getConsentRespDetails(registrationDetails));
		return registrationDetails;
	}

	private static ConsentResponseDetail getConsentRespDetails(CollectionProtocolRegistrationDetail registrationDetails) {
		ConsentResponseDetail responseDetails = new ConsentResponseDetail();
		responseDetails.setConsentDate(new Date());
		responseDetails.setConsentUrl("www.google.com");
		responseDetails.setWitnessName("admin@admin.com");
		responseDetails.setConsentTierList(getConsentTierList());
		return responseDetails;
	}

	private static List<ConsentTierDetail> getConsentTierList() {
		ConsentTierDetail tierDetails = new ConsentTierDetail();
		tierDetails.setConsentStatment("statement1");
		tierDetails.setParticipantResponse("Yes");
		List<ConsentTierDetail> list = new ArrayList<ConsentTierDetail>();
		list.add(tierDetails);
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

	private static Collection getCpeCollection(CollectionProtocol protocol) {
		Collection<CollectionProtocolEvent> cpes = new HashSet<CollectionProtocolEvent>();
		CollectionProtocolEvent event = new CollectionProtocolEvent();
		event.setActivityStatus(ACTIVITY_STATUS_ACTIVE);
		event.setClinicalDiagnosis(NOT_SPECIFIED);
		event.setClinicalStatus(NOT_SPECIFIED);
		event.setCollectionPointLabel("visit1");
		event.setStudyCalendarEventPoint(0.0);
		event.setCollectionProtocol(protocol);
		event.setSpecimenRequirementCollection(getSpecimenRequirementColl(event));
		cpes.add(event);
		return cpes;
	}

	private static Collection<SpecimenRequirement> getSpecimenRequirementColl(CollectionProtocolEvent event) {
		Collection<SpecimenRequirement> requirements = new HashSet<SpecimenRequirement>();
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
	
	private static edu.wustl.catissuecore.domain.User getOldUser(Long userId) {
		edu.wustl.catissuecore.domain.User user = new edu.wustl.catissuecore.domain.User();
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

	public static List<SpecimenCollectionGroupInfo> getSCGSummaryList() {
		List<SpecimenCollectionGroupInfo> groupsInfo = new ArrayList<SpecimenCollectionGroupInfo>();
		SpecimenCollectionGroupInfo groupInfo = new SpecimenCollectionGroupInfo();
		groupInfo.setCollectionStatus("Collected");
		groupInfo.setName("test scg");
		groupInfo.setReceivedDate(new Date());
		groupInfo.setId(1l);
		groupsInfo.add(groupInfo);

		return groupsInfo;
	}

	public static Participant getParticipant() {
		Participant participant = new Participant();
		participant.setId(1l);
		return participant;
	}

	public static CollectionProtocol getCptoReturn() {
		Long cpId = 1l;
		CollectionProtocol protocol = getCollectionProtocol(cpId);
		protocol.setCollectionProtocolEventCollection(getCpeCollection(protocol));
		return protocol;
	}

	public static CreateRegistrationEvent getCprCreateEvent() {
		CreateRegistrationEvent reqEvent = new CreateRegistrationEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		reqEvent.setCprDetail(getRegistrationDetails(1l));
		return reqEvent;
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
