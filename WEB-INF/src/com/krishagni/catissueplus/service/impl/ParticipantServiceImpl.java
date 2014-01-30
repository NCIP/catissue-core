//
//package com.krishagni.catissueplus.service.impl;
//
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//
//import com.krishagni.catissueplus.annotation.PlusTransactional;
//import com.krishagni.catissueplus.dao.ParticipantDao;
//import com.krishagni.catissueplus.dto.ParticipantDto;
//import com.krishagni.catissueplus.events.CreateEvent;
//import com.krishagni.catissueplus.events.CreatedEvent;
//import com.krishagni.catissueplus.events.ReadEvent;
//import com.krishagni.catissueplus.events.RequestReadEvent;
//import com.krishagni.catissueplus.events.key.ParticipantSearchKey;
//import com.krishagni.catissueplus.events.participants.ParticipantInfo;
//import com.krishagni.catissueplus.service.ParticipantService;
//
//import edu.wustl.catissuecore.domain.Participant;
//import edu.wustl.common.util.global.Validator;
//
//@Service(value="ParticipantServiceImpl")
//public class ParticipantServiceImpl implements ParticipantService
//{
//
//	private ParticipantDao participantDao;
//
//	public ParticipantDao getParticipantDao()
//	{
//		return participantDao;
//	}
//
//	public void setParticipantDao(ParticipantDao participantDao)
//	{
//		this.participantDao = participantDao;
//	}
//
//	@Override
//	public CreatedEvent<ParticipantDto> save(CreateEvent<ParticipantDto> createParticipantEvent)
//	{
//		participantDao.save(createParticipantEvent.getObject().getParticipant());
//		return null;
//	}
//
//	@Override
//	@PlusTransactional
//	public ReadEvent<List<ParticipantInfo>> getParticipantInfoList(
//			RequestReadEvent<ParticipantSearchKey> participantReadEvent)
//	{
//		ParticipantSearchKey participantSearchKey = participantReadEvent.getKey();
//		List<ParticipantInfo> participantInfoList;
//		if (Validator.isEmpty(participantSearchKey.getQueryString()))
//		{
//			participantInfoList = participantDao.getParticipantsInfoList(participantSearchKey.getCollectionProtocolId());
//		}
//		else
//		{
//			participantInfoList = participantDao.getParticipantsInfoList(participantSearchKey.getCollectionProtocolId(),
//					participantSearchKey.getQueryString());
//		}
//		return ReadEvent.ok(participantInfoList);
//	}
//
//	@Override
//	public ReadEvent<ParticipantDto> getParticipant(RequestReadEvent<Long> participantReadEvent)
//	{
//		Participant participant = participantDao.getParticipant(participantReadEvent.getKey());
//		ParticipantDto participantDto = new ParticipantDto(participant);
//		return ReadEvent.ok(participantDto);
//	}
//
//}
