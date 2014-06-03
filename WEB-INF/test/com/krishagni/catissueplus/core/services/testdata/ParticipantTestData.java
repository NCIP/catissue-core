package com.krishagni.catissueplus.core.services.testdata;

import java.util.ArrayList;  
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.ParticipantMedicalIdentifier;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.events.CreateParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantMedicalIdentifierNumberDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ReqParticipantDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateParticipantEvent;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import edu.wustl.common.beans.SessionDataBean;


public class ParticipantTestData {
	
	private static final String NOT_SPECIFIED = "Not Specified";

	public static DeleteParticipantEvent getParticipantDeleteEventWithoutChildren() {
		DeleteParticipantEvent event = new DeleteParticipantEvent();
		event.setId(1l);
		event.setIncludeChildren(false);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static CreateParticipantEvent getParticipantCreateEvent() {
		CreateParticipantEvent reqEvent = new CreateParticipantEvent();
		reqEvent.setParticipantDetail(getParticipantDetails());
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetail participantDetails = getParticipantDetails();
		reqEvent.setParticipantDetail(participantDetails);
		return reqEvent;
	}

	public static CreateParticipantEvent getParticipantCreateEventInvalidSSN() {
		CreateParticipantEvent reqEvent = new CreateParticipantEvent();
		reqEvent.setParticipantDetail(getParticipantDetailsWithInvalidSSN());
		reqEvent.setSessionDataBean(getSessionDataBean());
		return reqEvent;
	}

	public static CreateParticipantEvent getParticipantCreateWithFutureDate() {
		CreateParticipantEvent reqEvent = new CreateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetail participantDto = getParticipantDetails();
		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.DAY_OF_YEAR, 1);
		Date tomorrow = calendar.getTime();
		participantDto.setBirthDate(tomorrow);
		reqEvent.setParticipantDetail(participantDto);
		return reqEvent;
	}

	public static CreateParticipantEvent getParticipantCreatEventPastDeathDate() {
		CreateParticipantEvent reqEvent = new CreateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetail participantDto = getParticipantDetails();
		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.DAY_OF_YEAR, -1);
		Date yesterday = calendar.getTime();
		participantDto.setDeathDate(yesterday);
		reqEvent.setParticipantDetail(participantDto);
		return reqEvent;
	}

	public static CreateParticipantEvent getParticipantCreateInvalidDeathDate() {
		CreateParticipantEvent reqEvent = new CreateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetail participantDto = getParticipantDetails();
		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.DAY_OF_YEAR, 1);
		Date tomorrow = calendar.getTime();
		participantDto.setDeathDate(tomorrow);
		participantDto.setVitalStatus("Alive");
		reqEvent.setParticipantDetail(participantDto);
		return reqEvent;
	}

	public static CreateParticipantEvent getParticipantCreateEventInvalidSite() {
		return getParticipantCreateEvent();
	}

	public static CreateParticipantEvent getParticipantCreateEmptyMrn() {
		CreateParticipantEvent reqEvent = new CreateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetail participantDto = getParticipantDetails();
		participantDto.setPmiCollection(getMrnWithEmptyNumber());
		reqEvent.setParticipantDetail(participantDto);
		return reqEvent;
	}

	public static CreateParticipantEvent getParticipantCreateInvalidGender() {
		CreateParticipantEvent reqEvent = new CreateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetail participantDto = getParticipantDetails();
		participantDto.setGender("mal");

		reqEvent.setParticipantDetail(participantDto);
		return reqEvent;
	}

	public static CreateParticipantEvent getParticipantCreateInvalidRace() {
		CreateParticipantEvent reqEvent = new CreateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetail participantDto = getParticipantDetails();
		Set<String> race = new HashSet<String>();
		race.add("melanin");
		participantDto.setRace(race);

		reqEvent.setParticipantDetail(participantDto);
		return reqEvent;
	}

	public static CreateParticipantEvent getParticipantCreateDuplicateSsn() {
		CreateParticipantEvent reqEvent = new CreateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetail participantDto = getParticipantDetails();

		reqEvent.setParticipantDetail(participantDto);
		return reqEvent;
	}

	public static UpdateParticipantEvent getParticipantUpdateEvent() {
		UpdateParticipantEvent reqEvent = new UpdateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetail participantDto = getParticipantDetails();
		participantDto.setId(1L);
		participantDto.setActivityStatus("Active");
		participantDto.setFirstName("firstName");
		participantDto.setLastName("lastName");
		participantDto.setGender("male");
		reqEvent.setParticipantDetail(participantDto);
		return reqEvent;
	}

	public static ReqParticipantDetailEvent getRequestParticipantEvent() {
		ReqParticipantDetailEvent reqEvent = new ReqParticipantDetailEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		reqEvent.setParticipantId(1l);
		return reqEvent;
	}

	public static UpdateParticipantEvent getParticipantUpdateInvalidSsn() {
		UpdateParticipantEvent reqEvent = new UpdateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetail participantDto = getParticipantDetails();
		participantDto.setId(1L);
		participantDto.setActivityStatus("Active");
		participantDto.setFirstName("firstName");
		participantDto.setLastName("lastName");
		participantDto.setGender("male");
		participantDto.setSsn("222-3-333");
		reqEvent.setParticipantDetail(participantDto);
		return reqEvent;
	}

	public static UpdateParticipantEvent getUpdateParticipantNewSsn() {
		UpdateParticipantEvent reqEvent = new UpdateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetail participantDto = getParticipantDetails();
		participantDto.setId(1L);
		participantDto.setActivityStatus("Active");
		participantDto.setFirstName("firstName");
		participantDto.setLastName("lastName");
		participantDto.setGender("male");
		participantDto.setSsn("222-32-1333");
		reqEvent.setParticipantDetail(participantDto);
		return reqEvent;
	}

	public static Participant getUpdatedParticipantWithNewSsn() {
		Participant participant = getParticipant(1L);
		String newSsn = "111-21-4315";
		participant.setSocialSecurityNumber(newSsn);
		return participant;
	}

	public static Participant getUpdateParticipant() {
		Participant participant = getParticipant(1L);
		ParticipantMedicalIdentifier pmi = new ParticipantMedicalIdentifier();
		pmi.setMedicalRecordNumber("234r5");
		pmi.setSite(getSite("some test site"));
		pmi.setParticipant(participant);
		participant.getPmiCollection().put(pmi.getSite().getName(), pmi);
		return participant;
	}

	private static ParticipantDetail getParticipantDetailsWithInvalidSSN() {
		ParticipantDetail detail = getParticipantDetails();
		detail.setSsn("22-43-546");
		return detail;
	}

	public static Participant getParticipantToDelete() {
		Participant participantToDelete = getParticipant(1l);
		participantToDelete.setCprCollection(getCprCollection());
		return participantToDelete;
	}

	public static DeleteParticipantEvent getParticipantDeleteEvent() {
		DeleteParticipantEvent event = new DeleteParticipantEvent();
		event.setId(1l);
		event.setIncludeChildren(true);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}
	private static Map<String, CollectionProtocolRegistration> getCprCollection() {
		Map<String, CollectionProtocolRegistration> cprColl = new HashMap<String, CollectionProtocolRegistration>();
		CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
		cpr.setActive();
		cpr.setBarcode("barcode");
		cpr.setCollectionProtocol(getCollectionProtocol());
		cpr.setProtocolParticipantIdentifier("ppid1");
		cpr.setRegistrationDate(new Date());
		cpr.setScgCollection(getscgCollection(cpr));
		cprColl.put(cpr.getCollectionProtocol().getTitle(), cpr);
		return cprColl;
	}

	private static CollectionProtocol getCollectionProtocol() {
		CollectionProtocol protocol = new CollectionProtocol();
		protocol.setTitle("Test case CP");
		return protocol;
	}

	private static Collection<SpecimenCollectionGroup> getscgCollection(CollectionProtocolRegistration cpr) {
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

	private static Participant getParticipant(Long id) {
		Participant participant = new Participant();
		participant.setId(id);
		participant.setFirstName("firstName1");
		participant.setLastName("lastName1");
		return participant;
	}

	private static  ParticipantDetail getParticipantDetails() {
		ParticipantDetail details = new ParticipantDetail();
		details.setActivityStatus("Active");
		details.setFirstName("firstName");
		details.setLastName("lastName");
		details.setGender("male");
		details.setBirthDate(new Date());
		details.setSsn("123-34-3654");
		details.setVitalStatus("Death");

		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.DAY_OF_YEAR, 1);
		Date tomorrow = calendar.getTime();
		details.setDeathDate(tomorrow);

		details.setEthnicity("Not Reported");
		Set<String> race = new HashSet<String>();
		race.add("Asian");
		details.setRace(race);
		ParticipantMedicalIdentifierNumberDetail medicalRecordNumberDetail = new ParticipantMedicalIdentifierNumberDetail();
		medicalRecordNumberDetail.setMrn("234");
		medicalRecordNumberDetail.setSiteName("siteName");
		List<ParticipantMedicalIdentifierNumberDetail> mrns = new ArrayList<ParticipantMedicalIdentifierNumberDetail>();
		mrns.add(medicalRecordNumberDetail);
		details.setPmiCollection(mrns);
		return details;
	}

	public static Site getSite(String string) {
		Site site = new Site();
		site.setName(string);
		return site;
	}

	private static List<ParticipantMedicalIdentifierNumberDetail> getMrnWithEmptyNumber() {
		ParticipantMedicalIdentifierNumberDetail medicalRecordNumberDetail = new ParticipantMedicalIdentifierNumberDetail();
		medicalRecordNumberDetail.setMrn("");
		medicalRecordNumberDetail.setSiteName("siteName");
		List<ParticipantMedicalIdentifierNumberDetail> mrns = new ArrayList<ParticipantMedicalIdentifierNumberDetail>();
		mrns.add(medicalRecordNumberDetail);
		return mrns;
	}

	public static CreateParticipantEvent getParticipantCreateEventDuplicateMrn() {
		CreateParticipantEvent event = new CreateParticipantEvent();
		ParticipantDetail detail = getParticipantDetails();

//		ParticipantMedicalIdentifierNumberDetail medicalRecordNumberDetail = new ParticipantMedicalIdentifierNumberDetail();
//		medicalRecordNumberDetail.setMrn("234");
//		medicalRecordNumberDetail.setSiteName("newSiteName");
//		detail.getPmiCollection().add(medicalRecordNumberDetail);
		event.setParticipantDetail(detail);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}
}
