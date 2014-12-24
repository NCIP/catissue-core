
package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.biospecimen.events.AllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRespEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.biospecimen.events.CreateCollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqAllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqCollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/collection-protocols")
public class CollectionProtocolsController {

	@Autowired
	private CollectionProtocolService cpSvc;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<CollectionProtocolSummary> getCollectionProtocols(
			@RequestParam(value = "chkPrivilege", required = false, defaultValue = "true")  boolean chkPrivlege,
			@RequestParam(value = "detailedList", required = false, defaultValue = "false") boolean detailedList) {
		
		ReqAllCollectionProtocolsEvent req = new ReqAllCollectionProtocolsEvent();
		req.setSessionDataBean(getSession());
		req.setChkPrivileges(chkPrivlege);
		req.setIncludePi(detailedList);
		req.setIncludeStats(detailedList);

		AllCollectionProtocolsEvent resp = cpSvc.getAllProtocols(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		return resp.getCpList();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public  CollectionProtocolDetail getCollectionProtocol(@PathVariable("id") Long cpId) {
		ReqCollectionProtocolEvent req = new ReqCollectionProtocolEvent();
		req.setCpId(cpId);
		req.setSessionDataBean(getSession());
		
		CollectionProtocolRespEvent resp = cpSvc.getCollectionProtocol(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
				
		return resp.getCp();
	}	

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CollectionProtocolDetail createCollectionProtocol(@RequestBody CollectionProtocolDetail cp) {
		CreateCollectionProtocolEvent req = new CreateCollectionProtocolEvent();
		req.setCp(cp);
		req.setSessionDataBean(getSession());

		CollectionProtocolCreatedEvent resp = cpSvc.createCollectionProtocol(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}

		return resp.getCp();
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}	
}
