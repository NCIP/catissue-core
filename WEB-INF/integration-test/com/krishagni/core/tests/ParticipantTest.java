package com.krishagni.core.tests;

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
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantMatchedEvent;
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
	@DatabaseSetup("ParticipantTest.generic.initial.xml")
	@DatabaseTearDown("ParticipantTest.generic.teardown.xml")
	public void matchParticpantBySsn() {
		MatchParticipantEvent req = ParticipantTestData.getMatchParticipantEvent();
		req.getParticipantDetail().setSsn("333-22-4444");
		ParticipantMatchedEvent resp = participantSvc.getMatchingParticipants(req);
		
		Assert.assertEquals(true, resp.isSuccess());
		Assert.assertEquals("ssn", resp.getMatchedAttr());
		Assert.assertEquals((Long)1L, resp.getParticipants().get(0).getId());
	}
	
	@Test
	@DatabaseSetup("ParticipantTest.generic.initial.xml")
	@DatabaseTearDown("ParticipantTest.generic.teardown.xml")
	public void matchParticpantByEmpi() {
		MatchParticipantEvent req = ParticipantTestData.getMatchParticipantEvent();
		req.getParticipantDetail().setEmpi("dummy-empi-id");
		
		ParticipantMatchedEvent resp = participantSvc.getMatchingParticipants(req);
		
		Assert.assertEquals(true, resp.isSuccess());
		Assert.assertEquals("empi", resp.getMatchedAttr());
		Assert.assertEquals((Long)1L, resp.getParticipants().get(0).getId());
	}
	
	@Test
	@DatabaseSetup("ParticipantTest.generic.initial.xml")
	@DatabaseTearDown("ParticipantTest.generic.teardown.xml")
	public void matchParticpantByPmi() {
		MatchParticipantEvent req = ParticipantTestData.getMatchParticipantEvent();
		req.getParticipantDetail().setPmis(ParticipantTestData.getPmi("SITE1", "MRN1"));
		
		ParticipantMatchedEvent resp = participantSvc.getMatchingParticipants(req);
		
		Assert.assertEquals(true, resp.isSuccess());
		Assert.assertEquals("pmi", resp.getMatchedAttr());
		Assert.assertEquals((Long)1L, resp.getParticipants().get(0).getId());
	}
	
	@Test
	@DatabaseSetup("ParticipantTest.generic.initial.xml")
	@DatabaseTearDown("ParticipantTest.generic.teardown.xml")
	public void matchParticpantByLnameAndDob() {
		MatchParticipantEvent req = ParticipantTestData.getMatchParticipantEvent();
		req.getParticipantDetail().setLastName("default_last_name");
		req.getParticipantDetail().setBirthDate(CprTestData.getDate(21, 10, 2012));
		ParticipantMatchedEvent resp = participantSvc.getMatchingParticipants(req);
		
		Assert.assertEquals(true, resp.isSuccess());
		Assert.assertEquals("lnameAndDob", resp.getMatchedAttr());
		Assert.assertEquals((Long)1L, resp.getParticipants().get(0).getId());
	}
	
	/*
	 * Get Participant API Tests
	 */
	@Test
	@DatabaseSetup("ParticipantTest.generic.initial.xml")
	@DatabaseTearDown("ParticipantTest.generic.teardown.xml")
	public void getParticipantTest() {
		ReqParticipantDetailEvent req = ParticipantTestData.getReqParticipantDetailEvent();
		ParticipantDetailEvent resp = participantSvc.getParticipant(req);
		
		Assert.assertEquals(true, resp.isSuccess());
		Assert.assertEquals((Long)1L, resp.getParticipantDetail().getId());
	}
	
	@Test
	public void getParticipantTestParticipantNotFound() {
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
	
}
