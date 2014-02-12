
package com.krishagni.catissueplus.core.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.krishagni.catissueplus.core.events.EventStatus;
import com.krishagni.catissueplus.core.events.participants.ParticipantDetails;
import com.krishagni.catissueplus.core.events.registration.AllRegistrationsSummaryEvent;
import com.krishagni.catissueplus.core.events.registration.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.events.registration.RegistrationUpdatedEvent;
import com.krishagni.catissueplus.core.events.registration.ReqRegistrationEvent;
import com.krishagni.catissueplus.core.events.registration.ReqRegistrationSummaryEvent;
import com.krishagni.catissueplus.core.events.registration.UpdateRegistrationEvent;
import com.krishagni.catissueplus.core.repository.CollectionProtocolRegistrationDao;
import com.krishagni.catissueplus.core.services.impl.CollectionProtocolServiceImpl;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.common.beans.SessionDataBean;

public class CollectionProtocolRegTest {

	@Mock
	private CollectionProtocolRegistrationDao collectionProtocolRegistrationDao;

	private CollectionProtocolService protocolService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		protocolService = new CollectionProtocolServiceImpl();
	}

	@Test
	public void testForSuccessfulRegistrationCreation() {
		Long cpId = 1l;
		ReqRegistrationEvent reqEvent = new ReqRegistrationEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		reqEvent.setCpId(cpId);
		reqEvent.setParticipantDetails(getParticipantDto());
//		when(collectionProtocolRegistrationDao.saveOrUpdate(getRegistration(reqEvent.getParticipantDetails(), reqEvent.getCpId())))
//				.thenReturn(getRegistration(reqEvent.getParticipantDetails(), reqEvent.getCpId()));
		protocolService.setRegistrationDao(collectionProtocolRegistrationDao);

		RegistrationCreatedEvent response = protocolService.createRegistration(reqEvent);
		assertNotNull("Response cannot be null", response);
		ParticipantDetails actualResult = response.getParticipantDetails();

		assertEquals(EventStatus.OK, response.getStatus());
		assertNotNull(actualResult);
		assertEquals(cpId, actualResult.getCpId());

	}

	@Test
	public void testForSuccessfulRegistrationUpdate() {
		Long cpId = 1l;
		UpdateRegistrationEvent reqEvent = new UpdateRegistrationEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		reqEvent.setParticipantDetails(getParticipantDto());
		when(collectionProtocolRegistrationDao.update(getRegistration(reqEvent.getParticipantDetails(), cpId))).thenReturn(
				getRegistration(reqEvent.getParticipantDetails(), cpId));
		protocolService.setRegistrationDao(collectionProtocolRegistrationDao);

		RegistrationUpdatedEvent response = protocolService.updateResgistration(reqEvent);
		assertNotNull("Response cannot be null", response);
		ParticipantDetails actualResult = response.getParticipantDetails();
		assertEquals(EventStatus.OK, response.getStatus());
		assertNotNull(actualResult);
		assertEquals(cpId, actualResult.getCpId());

	}

	@Test
	public void testForGetAllRegistrations() {
		Long cpId = 1l;
		ReqRegistrationSummaryEvent reqEvent = new ReqRegistrationSummaryEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		List<CollectionProtocolRegistration> expectedResult = getCprList(cpId);
		when(collectionProtocolRegistrationDao.getAllRegistrations(cpId)).thenReturn(expectedResult);
		protocolService.setRegistrationDao(collectionProtocolRegistrationDao);

		AllRegistrationsSummaryEvent response = protocolService.getAllRegistrations(reqEvent);
		assertNotNull("Response cannot be null", response);
		List<ParticipantDetails> actualResult = response.getParticipantDetails();
		assertEquals(EventStatus.OK, response.getStatus());
		assertNotNull(actualResult);
		assertEquals(expectedResult.size(), actualResult.size());

	}

	private List<CollectionProtocolRegistration> getCprList(Long cpId) {
		List<CollectionProtocolRegistration> cprList = new ArrayList<CollectionProtocolRegistration>();

		return cprList;
	}

	private CollectionProtocolRegistration getRegistration(ParticipantDetails participantDetails, Long cpId) {
		Participant participant = participantDetails.getDomain();
		CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
		cpr.setParticipant(participant);
		CollectionProtocol cp = new CollectionProtocol();
		cp.setId(cpId);
		cpr.setCollectionProtocol(cp);
		cpr.setProtocolParticipantIdentifier("cpr_ppi");
		return cpr;
	}

	private ParticipantDetails getParticipantDto() {
		ParticipantDetails participantDto = new ParticipantDetails();
		participantDto.setFirstName("firstName");
		participantDto.setLastName("lastName");
		participantDto.setGender("male");
		participantDto.setRegistrationDate(new Date());
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

}
