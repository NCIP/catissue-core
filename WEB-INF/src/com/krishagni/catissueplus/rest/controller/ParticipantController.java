//
//package com.krishagni.catissueplus.rest.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//import com.krishagni.catissueplus.events.ReadEvent;
//import com.krishagni.catissueplus.events.RequestReadEvent;
//import com.krishagni.catissueplus.events.key.ParticipantSearchKey;
//import com.krishagni.catissueplus.events.participants.ParticipantInfo;
//import com.krishagni.catissueplus.service.ParticipantService;
//
//@Controller
//@RequestMapping("/participants")
//public class ParticipantController
//{
//
//	@Autowired
//	private ParticipantService participantService;
//
//	@RequestMapping(method = RequestMethod.GET, value = "/{cpId}/{queryString}")
//	@ResponseStatus(HttpStatus.OK)
//	@ResponseBody
//	public List<ParticipantInfo> getParticipantsInfoList(Long cpId, String queryString)
//	{
//		ParticipantSearchKey participantSearchKey = new ParticipantSearchKey();
//		participantSearchKey.setCollectionProtocolId(cpId);
//		participantSearchKey.setQueryString(queryString);
//		RequestReadEvent<ParticipantSearchKey> event = new RequestReadEvent<ParticipantSearchKey>();
//		event.setKey(participantSearchKey);
//		ReadEvent<List<ParticipantInfo>> result = participantService.getParticipantInfoList(event);
//		return result.getPayload();
//	}
//
//	@RequestMapping(method = RequestMethod.GET, value = "/{cpId}")
//	@ResponseStatus(HttpStatus.OK)
//	@ResponseBody
//	public List<ParticipantInfo> getParticipantsInfoList(Long cpId)
//	{
//		return getParticipantsInfoList(cpId, null);
//	}
//
//}
