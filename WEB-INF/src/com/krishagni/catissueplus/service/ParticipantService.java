//
//package com.krishagni.catissueplus.service;
//
//import java.util.List;
//
//import com.krishagni.catissueplus.dto.ParticipantDto;
//import com.krishagni.catissueplus.events.CreateEvent;
//import com.krishagni.catissueplus.events.CreatedEvent;
//import com.krishagni.catissueplus.events.ReadEvent;
//import com.krishagni.catissueplus.events.RequestReadEvent;
//import com.krishagni.catissueplus.events.key.ParticipantSearchKey;
//import com.krishagni.catissueplus.events.participants.ParticipantInfo;
//
//public interface ParticipantService
//{
//
//	public CreatedEvent<ParticipantDto> save(CreateEvent<ParticipantDto> createCPEvent);
//
//	public ReadEvent<List<ParticipantInfo>> getParticipantInfoList(
//			RequestReadEvent<ParticipantSearchKey> participantReadEvent);
//
//	public ReadEvent<ParticipantDto> getParticipant(RequestReadEvent<Long> participantReadEvent);
//}
