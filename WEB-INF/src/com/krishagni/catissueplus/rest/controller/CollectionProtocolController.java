
package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.events.collectionprotocols.AllCollProtocolsSummaryEvent;
import com.krishagni.catissueplus.events.collectionprotocols.CollectionProtocolDetail;
import com.krishagni.catissueplus.events.collectionprotocols.CollectionProtocolDetailEvent;
import com.krishagni.catissueplus.events.collectionprotocols.CollectionProtocolInfo;
import com.krishagni.catissueplus.events.collectionprotocols.ReqCollProtocolDetailEvent;
import com.krishagni.catissueplus.events.collectionprotocols.ReqCollProtocolsSummaryEvent;
import com.krishagni.catissueplus.events.participants.ParticipantInfo;
import com.krishagni.catissueplus.events.participants.ParticipantsSummaryEvent;
import com.krishagni.catissueplus.events.participants.ReqParticipantsSummaryEvent;
import com.krishagni.catissueplus.service.CollectionProtocolService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/ng/collection-protocols")
public class CollectionProtocolController {

	@Autowired
	private CollectionProtocolService collectionProtocolService;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<CollectionProtocolInfo> getCollectionProtocolList() {
		ReqCollProtocolsSummaryEvent event = new ReqCollProtocolsSummaryEvent();
		event.setSessionDataBean((SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA));
		AllCollProtocolsSummaryEvent result = collectionProtocolService.getCollectionProtocolList(event);
		return result.getCollectionProtocolsInfo();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CollectionProtocolDetail getCollectionProtocolList(@PathVariable("id") Long id) {
		ReqCollProtocolDetailEvent event = new ReqCollProtocolDetailEvent();
		event.setId(id);
		event.setSessionDataBean((SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA));
		CollectionProtocolDetailEvent result = collectionProtocolService.getCollectionProtocol(event);
		return result.getCollectionProtocolDetail();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/participants")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ParticipantInfo> getParticipants(@PathVariable("id") Long cpId,
			@RequestParam(value = "query", required = false, defaultValue = "") String searchStr) {
		ReqParticipantsSummaryEvent event = new ReqParticipantsSummaryEvent();
		event.setCpId(cpId);
		event.setSearchString(searchStr);
		event.setSessionDataBean((SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA));
		ParticipantsSummaryEvent result = collectionProtocolService.getRegisteredParticipantList(event);
		return result.getParticipantsInfo();
	}

}
