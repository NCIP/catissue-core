package com.krishagni.core.tests;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.MatchParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantMatchedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantMedicalIdentifierNumberDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ReqParticipantDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.services.ParticipantService;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.core.common.ApplicationContextConfigurer;
import com.krishagni.core.common.TestUtils;
import com.krishagni.core.common.WebContextLoader;
import com.krishagni.core.tests.testdata.CprTestData;
import com.krishagni.core.tests.testdata.ParticipantTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ApplicationContextConfigurer.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@WebAppConfiguration
public class ParticipantTest {
	@Resource
    private WebApplicationContext webApplicationContext;
	
	@Autowired
	private ParticipantService participantSvc;

	@Autowired
	private ApplicationContext ctx;
	
	/*
	 * Match Participant API Test 
	 */
	@Test
	@DatabaseSetup("participant-test/generic-initial.xml")
	@DatabaseTearDown("participant-test/generic-teardown.xml")
	public void matchParticpantBySsn() {
		MatchParticipantEvent req = ParticipantTestData.getMatchParticipantEvent();
		req.getParticipantDetail().setSsn("333-22-4444");
		ParticipantMatchedEvent resp = participantSvc.getMatchingParticipants(req);
		
		Assert.assertEquals(true, resp.isSuccess());
		Assert.assertEquals("ssn", resp.getMatchedAttr());
		Assert.assertEquals((Long)1L, resp.getParticipants().get(0).getId());
	}
	
	@Test
	@DatabaseSetup("participant-test/generic-initial.xml")
	@DatabaseTearDown("participant-test/generic-teardown.xml")
	public void matchParticpantByEmpi() {
		MatchParticipantEvent req = ParticipantTestData.getMatchParticipantEvent();
		req.getParticipantDetail().setEmpi("dummy-empi-id");
		
		ParticipantMatchedEvent resp = participantSvc.getMatchingParticipants(req);
		
		Assert.assertEquals(true, resp.isSuccess());
		Assert.assertEquals("empi", resp.getMatchedAttr());
		Assert.assertEquals((Long)1L, resp.getParticipants().get(0).getId());
	}
	
	@Test
	@DatabaseSetup("participant-test/generic-initial.xml")
	@DatabaseTearDown("participant-test/generic-teardown.xml")
	public void matchParticpantByPmi() {
		MatchParticipantEvent req = ParticipantTestData.getMatchParticipantEvent();
		req.getParticipantDetail().setPmis(ParticipantTestData.getPmi("SITE1", "MRN1"));
		
		ParticipantMatchedEvent resp = participantSvc.getMatchingParticipants(req);
		
		Assert.assertEquals(true, resp.isSuccess());
		Assert.assertEquals("pmi", resp.getMatchedAttr());
		Assert.assertEquals((Long)1L, resp.getParticipants().get(0).getId());
	}
	
	@Test
	@DatabaseSetup("participant-test/generic-initial.xml")
	@DatabaseTearDown("participant-test/generic-teardown.xml")
	public void matchParticpantByLnameAndDob() {
		MatchParticipantEvent req = ParticipantTestData.getMatchParticipantEvent();
		req.getParticipantDetail().setLastName("default_last_name");
		req.getParticipantDetail().setBirthDate(CprTestData.getDate(21, 10, 2000));
		ParticipantMatchedEvent resp = participantSvc.getMatchingParticipants(req);
		
		Assert.assertEquals(true, resp.isSuccess());
		Assert.assertEquals("lnameAndDob", resp.getMatchedAttr());
		Assert.assertEquals((Long)1L, resp.getParticipants().get(0).getId());
	}
	
	/*
	 * Get Participant API Tests
	 */
	@Test
	@DatabaseSetup("participant-test/generic-initial.xml")
	@DatabaseTearDown("participant-test/generic-teardown.xml")
	public void getParticipantTest() {
		ReqParticipantDetailEvent req = ParticipantTestData.getReqParticipantDetailEvent();
		ParticipantDetailEvent resp = participantSvc.getParticipant(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		ParticipantDetail participant = resp.getParticipantDetail();
		Assert.assertNotNull("Participant detail object should not be null!", participant );
		Assert.assertEquals(new Long(1), participant.getId());
		Assert.assertEquals("Firstname mismatch", "default_first_name", participant.getFirstName());
		Assert.assertEquals("LastName mismatch", "default_last_name", participant.getLastName());
		Assert.assertEquals("Birthdate mismatch", CprTestData.getDate(21,10,2000), participant.getBirthDate());
		Assert.assertEquals("Deathdate mismatch", CprTestData.getDate(21,10,2012), participant.getDeathDate());
		Assert.assertEquals("SSN mismatch", "333-22-4444", participant.getSsn());
		Assert.assertEquals("Gender mismatch" , "MALE", participant.getGender());
		Assert.assertEquals("Genotype mismatch" , "XX", participant.getSexGenotype());
		Assert.assertEquals("Ethnicity mismatch" , "Hispanic or Latino", participant.getEthnicity());
		Assert.assertEquals("Empi-id mismatch" , "dummy-empi-id", participant.getEmpi());
		Assert.assertEquals("Vital status mismatch", "Dead" , participant.getVitalStatus());
		
		List<ParticipantMedicalIdentifierNumberDetail> pmis = participant.getPmis();
		Assert.assertEquals("Pmi count mismatch" , new Integer(2), new Integer(pmis.size()));
		
		for (ParticipantMedicalIdentifierNumberDetail pmi : pmis) { 
			if ("SITE1".equals(pmi.getSiteName())) {
				Assert.assertEquals("Mrn mismatch" , "MRN1" , pmi.getMrn());
			} else if ("SITE2".equals(pmi.getSiteName())) {
				Assert.assertEquals("Mrn mismatch" , "MRN2" , pmi.getMrn());
			} else {
				Assert.fail("Site: " + pmi.getSiteName() + " was not expected.");
			}
		}
		
		Set<String> race = participant.getRace();
		Assert.assertEquals(new Integer(2), new Integer(race.size()));
		Assert.assertEquals("Race 'Asian' not found!", true, race.contains("Asian"));
		Assert.assertEquals("Race 'Asian' not found!", true, race.contains("American Indian or Alaska Native"));
	}
	
	@Test
	public void getNonExistingParticipant() {
		ReqParticipantDetailEvent req = ParticipantTestData.getReqParticipantDetailEvent();
		ParticipantDetailEvent resp = participantSvc.getParticipant(req);
		
		Assert.assertEquals(EventStatus.NOT_FOUND, resp.getStatus());
		Assert.assertEquals((Long)1L, resp.getParticipantId());
	}
	
	@Test
	public void getParticipantTestNullParticipantId() {
		ReqParticipantDetailEvent req = ParticipantTestData.getReqParticipantDetailEvent();
		req.setParticipantId(null);
		
		ParticipantDetailEvent resp = participantSvc.getParticipant(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ParticipantErrorCode.INVALID_ATTR_VALUE, "participant-id"));
	}
	
	@Test
	@DatabaseSetup("participant-test/get-disabled-paticipant-initial.xml")
	@DatabaseTearDown("participant-test/generic-teardown.xml")
	public void getDisabledPaticipant() {
		ReqParticipantDetailEvent req = ParticipantTestData.getReqParticipantDetailEvent();
		ParticipantDetailEvent resp = participantSvc.getParticipant(req);
		
		Assert.assertEquals(EventStatus.NOT_FOUND, resp.getStatus());
		Assert.assertEquals(req.getParticipantId(), resp.getParticipantId());
	}
	
}
