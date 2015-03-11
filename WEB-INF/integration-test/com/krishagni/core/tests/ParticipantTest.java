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
import com.krishagni.catissueplus.core.biospecimen.events.MatchedParticipants;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.PmiDetail;
import com.krishagni.catissueplus.core.biospecimen.services.ParticipantService;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.core.common.ApplicationContextConfigurer;
import com.krishagni.core.common.TestUtils;
import com.krishagni.core.common.WebContextLoader;
import com.krishagni.core.tests.testdata.CommonUtils;
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
	
	private <T> RequestEvent<T> getRequest(T payload) {
		return CommonUtils.getRequest(payload);
	}
	
	/*
	 * Match Participant API Test 
	 */
	
	@Test
	@DatabaseSetup("participant-test/generic-initial.xml")
	@DatabaseTearDown("participant-test/generic-teardown.xml")
	public void matchParticpantBySsn() {
		ParticipantDetail input = ParticipantTestData.getParticipant();
		input.setSsn("333-22-4444");
		
		ResponseEvent<MatchedParticipants> resp = participantSvc.getMatchingParticipants(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals("ssn", resp.getPayload().getMatchedAttr());
		Assert.assertEquals((Long)1L, resp.getPayload().getParticipants().get(0).getId());
	}
	
	@Test
	@DatabaseSetup("participant-test/generic-initial.xml")
	@DatabaseTearDown("participant-test/generic-teardown.xml")
	public void matchParticpantByEmpi() {
		ParticipantDetail input = ParticipantTestData.getParticipant();
		input.setEmpi("dummy-empi-id");
		
		ResponseEvent<MatchedParticipants> resp = participantSvc.getMatchingParticipants(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals("empi", resp.getPayload().getMatchedAttr());
		Assert.assertEquals((Long)1L, resp.getPayload().getParticipants().get(0).getId());
	}
	
	@Test
	@DatabaseSetup("participant-test/generic-initial.xml")
	@DatabaseTearDown("participant-test/generic-teardown.xml")
	public void matchParticpantByPmi() {
		ParticipantDetail input = ParticipantTestData.getParticipant();
		input.setPmis(ParticipantTestData.getPmi("SITE1", "MRN1"));
		
		ResponseEvent<MatchedParticipants> resp = participantSvc.getMatchingParticipants(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals("pmi", resp.getPayload().getMatchedAttr());
		Assert.assertEquals((Long)1L, resp.getPayload().getParticipants().get(0).getId());
	}
	
	@Test
	@DatabaseSetup("participant-test/generic-initial.xml")
	@DatabaseTearDown("participant-test/generic-teardown.xml")
	public void matchParticpantByLnameAndDob() {
		ParticipantDetail input = ParticipantTestData.getParticipant();
		input.setLastName("default_last_name");
		input.setBirthDate(CommonUtils.getDate(21, 10, 2000));
		
		ResponseEvent<MatchedParticipants> resp = participantSvc.getMatchingParticipants(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals("lnameAndDob", resp.getPayload().getMatchedAttr());
		Assert.assertEquals((Long)1L, resp.getPayload().getParticipants().get(0).getId());
	}
	
	/*
	 * Get Participant API Tests
	 */
	@Test
	@DatabaseSetup("participant-test/generic-initial.xml")
	@DatabaseTearDown("participant-test/generic-teardown.xml")
	public void getParticipantTest() {
		ResponseEvent<ParticipantDetail> resp = participantSvc.getParticipant(getRequest(1L));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		ParticipantDetail participant = resp.getPayload();
		Assert.assertNotNull("Participant detail object should not be null!", participant );
		Assert.assertEquals(new Long(1), participant.getId());
		Assert.assertEquals("Firstname mismatch", "default_first_name", participant.getFirstName());
		Assert.assertEquals("LastName mismatch", "default_last_name", participant.getLastName());
		Assert.assertEquals("Birthdate mismatch", CommonUtils.getDate(21,10,2000), participant.getBirthDate());
		Assert.assertEquals("Deathdate mismatch", CommonUtils.getDate(21,10,2012), participant.getDeathDate());
		Assert.assertEquals("SSN mismatch", "333-22-4444", participant.getSsn());
		Assert.assertEquals("Gender mismatch" , "MALE", participant.getGender());
		Assert.assertEquals("Genotype mismatch" , "XX", participant.getSexGenotype());
		Assert.assertEquals("Ethnicity mismatch" , "Hispanic or Latino", participant.getEthnicity());
		Assert.assertEquals("Empi-id mismatch" , "dummy-empi-id", participant.getEmpi());
		Assert.assertEquals("Vital status mismatch", "Dead" , participant.getVitalStatus());
		
		List<PmiDetail> pmis = participant.getPmis();
		Assert.assertEquals("Pmi count mismatch" , new Integer(2), new Integer(pmis.size()));
		
		for (PmiDetail pmi : pmis) { 
			if ("SITE1".equals(pmi.getSiteName())) {
				Assert.assertEquals("Mrn mismatch" , "MRN1" , pmi.getMrn());
			} else if ("SITE2".equals(pmi.getSiteName())) {
				Assert.assertEquals("Mrn mismatch" , "MRN2" , pmi.getMrn());
			} else {
				Assert.fail("Site: " + pmi.getSiteName() + " was not expected.");
			}
		}
		
		Set<String> race = participant.getRaces();
		Assert.assertEquals(new Integer(2), new Integer(race.size()));
		Assert.assertEquals("Race 'Asian' not found!", true, race.contains("Asian"));
		Assert.assertEquals("Race 'Asian' not found!", true, race.contains("American Indian or Alaska Native"));
	}
	
	@Test
	public void getNonExistingParticipant() {
		ResponseEvent<ParticipantDetail> resp = participantSvc.getParticipant(getRequest(1L));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, ParticipantErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	public void getParticipantTestInvalidParticipantId() {
		ResponseEvent<ParticipantDetail> resp = participantSvc.getParticipant(getRequest(-1L));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, ParticipantErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("participant-test/get-disabled-paticipant-initial.xml")
	@DatabaseTearDown("participant-test/generic-teardown.xml")
	public void getDisabledPaticipant() {
		ResponseEvent<ParticipantDetail> resp = participantSvc.getParticipant(getRequest(1L));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, ParticipantErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
}
