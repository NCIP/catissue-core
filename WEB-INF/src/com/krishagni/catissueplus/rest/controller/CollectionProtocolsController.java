
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

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierOp;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierOp.OP;
import com.krishagni.catissueplus.core.biospecimen.events.ListCpCriteria;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

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
			@RequestParam(value = "chkPrivilege", required = false, defaultValue = "true")  
			boolean chkPrivlege,
			
			@RequestParam(value = "detailedList", required = false, defaultValue = "false") 
			boolean detailedList) {
		
		ListCpCriteria crit = new ListCpCriteria()
			.includePi(detailedList)
			.includeStat(detailedList);

		ResponseEvent<List<CollectionProtocolSummary>> resp = cpSvc.getProtocols(getRequest(crit));
		resp.throwErrorIfUnsuccessful();		
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public  CollectionProtocolDetail getCollectionProtocol(@PathVariable("id") Long cpId) {
		ResponseEvent<CollectionProtocolDetail> resp = cpSvc.getCollectionProtocol(getRequest(cpId));
		resp.throwErrorIfUnsuccessful();		
		return resp.getPayload();
	}	

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CollectionProtocolDetail createCollectionProtocol(@RequestBody CollectionProtocolDetail cp) {
		ResponseEvent<CollectionProtocolDetail> resp = cpSvc.createCollectionProtocol(getRequest(cp));
		resp.throwErrorIfUnsuccessful();		
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}/consent-tiers")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ConsentTierDetail> getConsentTiers(@PathVariable("id") Long cpId) {
		ResponseEvent<List<ConsentTierDetail>> resp = cpSvc.getConsentTiers(getRequest(cpId));
		resp.throwErrorIfUnsuccessful();		
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/{id}/consent-tiers")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public ConsentTierDetail addConsentTier(@PathVariable("id") Long cpId, @RequestBody ConsentTierDetail consentTier) {
		return performConsentTierOp(OP.ADD, cpId, consentTier);
	}

	@RequestMapping(method = RequestMethod.PUT, value="/{id}/consent-tiers/{tierId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public ConsentTierDetail updateConsentTier(
			@PathVariable("id") Long cpId,
			@PathVariable("tierId") Long tierId,
			@RequestBody ConsentTierDetail consentTier) {
		
		consentTier.setId(tierId);
		return performConsentTierOp(OP.UPDATE, cpId, consentTier);
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/{id}/consent-tiers/{tierId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public ConsentTierDetail removeConsentTier(
			@PathVariable("id") Long cpId,
			@PathVariable("tierId") Long tierId) {
		
		ConsentTierDetail consentTier = new ConsentTierDetail();
		consentTier.setId(tierId);
		return performConsentTierOp(OP.REMOVE, cpId, consentTier);		
	}
	
	private ConsentTierDetail performConsentTierOp(OP op, Long cpId, ConsentTierDetail consentTier) {
		ConsentTierOp req = new ConsentTierOp();		
		req.setConsentTier(consentTier);
		req.setCpId(cpId);
		req.setOp(op);
		
		ResponseEvent<ConsentTierDetail> resp = cpSvc.updateConsentTier(getRequest(req));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	private <T> RequestEvent<T> getRequest(T payload) {
		return new RequestEvent<T>(getSession(), payload);
	}
	
	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}	
}
