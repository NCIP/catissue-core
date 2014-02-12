
package com.krishagni.catissueplus.core.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.krishagni.catissueplus.core.events.EventStatus;
import com.krishagni.catissueplus.core.events.participants.CreateParticipantEvent;
import com.krishagni.catissueplus.core.events.participants.ParticipantCreatedEvent;
import com.krishagni.catissueplus.core.events.participants.ParticipantDetailsEvent;
import com.krishagni.catissueplus.core.events.participants.ParticipantDetails;
import com.krishagni.catissueplus.core.events.participants.ParticipantSummary;
import com.krishagni.catissueplus.core.events.participants.ParticipantUpdatedEvent;
import com.krishagni.catissueplus.core.events.participants.ReqParticipantDetailEvent;
import com.krishagni.catissueplus.core.events.participants.ReqParticipantsSummaryEvent;
import com.krishagni.catissueplus.core.events.participants.UpdateParticipantEvent;
import com.krishagni.catissueplus.core.events.registration.AllParticipantsSummaryEvent;
import com.krishagni.catissueplus.core.repository.ParticipantDao;
import com.krishagni.catissueplus.core.services.impl.ParticipantServiceImpl;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.common.beans.SessionDataBean;

public class ParticipantTest {

	@Mock
	ParticipantDao participantDao;

	ParticipantService participantService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		participantService = new ParticipantServiceImpl();
	}

	@Test
	public void testForSuccessfulParticipantCreation() {

		CreateParticipantEvent reqEvent = new CreateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetails participantDto = new ParticipantDetails();
		participantDto.setActivityStatus("Active");
		participantDto.setFirstName("firstName");
		participantDto.setLastName("lastName");
		participantDto.setGender("male");
		reqEvent.setParticipantDto(participantDto);

		Participant participant = participantDto.getDomain();
//		when(participantDao.save(participant)).thenReturn(participant);
//		participantService.setParticipantDao(participantDao);
		ParticipantCreatedEvent response = participantService.createParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		ParticipantDetails createdParticipantDto = response.getParticipantDto();
		assertEquals(participantDto.getFirstName(), createdParticipantDto.getFirstName());

	}

	@Test
	public void testForSuccessfulParticipantUpdate() {

		UpdateParticipantEvent reqEvent = new UpdateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetails participantDto = new ParticipantDetails();
		participantDto.setActivityStatus("Active");
		participantDto.setFirstName("firstName");
		participantDto.setLastName("lastName");
		participantDto.setGender("male");
		reqEvent.setParticipantDto(participantDto);

		Participant participant = participantDto.getDomain();
//		when(participantDao.save(participant)).thenReturn(participant);
//		participantService.setParticipantDao(participantDao);
		ParticipantUpdatedEvent response = participantService.updateParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		ParticipantDetails createdParticipant = response.getParticipantDto();
		assertEquals(participantDto.getFirstName(), createdParticipant.getFirstName());

	}

	@Test
	public void testGetParticipant() {

		Long participantId = 1L;
		ReqParticipantDetailEvent reqEvent = new ReqParticipantDetailEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		reqEvent.setParticipantId(participantId);

		when(participantDao.getParticipant(participantId)).thenReturn(getParticipant(participantId));
		participantService.setParticipantDao(participantDao);
		ParticipantDetailsEvent response = participantService.getParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		ParticipantDetails retrievedParticipant = response.getParticipantDto();
		assertEquals(participantId, retrievedParticipant.getId());

	}

	@Test
	public void testGetParticipantList() {

		ReqParticipantsSummaryEvent reqEvent = new ReqParticipantsSummaryEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		List<Participant> expectedResults = getParticipantsList();
		when(participantDao.getAllParticipants()).thenReturn(getParticipantsList());
		participantService.setParticipantDao(participantDao);
		AllParticipantsSummaryEvent response = participantService.getallParticipants(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		List<ParticipantSummary> actualResults = response.getParticipantsSummary();
		assertNotNull(actualResults);
		assertEquals(expectedResults.size(), actualResults.size());

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

	private Participant getParticipant(Long id) {
		Participant participant = new Participant();
		participant.setId(id);
		return participant;
	}

	private List<Participant> getParticipantsList() {
		Participant participant1 = new Participant();
		participant1.setId(1L);
		participant1.setFirstName("firstName1");
		participant1.setLastName("lastName1");
		Participant participant2 = new Participant();
		participant2.setId(2L);
		participant2.setFirstName("firstName2");
		participant2.setLastName("lastName2");
		List<Participant> participants = new ArrayList<Participant>();
		participants.add(participant1);
		participants.add(participant2);
		return participants;
	}
}
