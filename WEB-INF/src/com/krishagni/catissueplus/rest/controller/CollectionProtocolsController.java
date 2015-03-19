
package com.krishagni.catissueplus.rest.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierOp;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierOp.OP;
import com.krishagni.catissueplus.core.biospecimen.events.CpQueryCriteria;
import com.krishagni.catissueplus.core.biospecimen.repository.CpListCriteria;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

import edu.common.dynamicextensions.nutility.IoUtil;
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
			@RequestParam(value = "query", required = false) 
			String searchStr,
			
			@RequestParam(value = "title", required = false)
			String title,
			
			@RequestParam(value = "piId", required = false)
			Long piId,
			
			@RequestParam(value = "startAt", required = false, defaultValue = "0") 
			int startAt,
			
			@RequestParam(value = "maxResults", required = false, defaultValue = "100") 
			int maxResults,
			
			@RequestParam(value = "chkPrivilege", required = false, defaultValue = "true")  
			boolean chkPrivlege,
			
			@RequestParam(value = "detailedList", required = false, defaultValue = "false") 
			boolean detailedList) {
		
		CpListCriteria crit = new CpListCriteria()
			.query(searchStr)
			.title(title)
			.piId(piId)
			.includePi(detailedList)
			.includeStat(detailedList)
			.startAt(startAt)
			.maxResults(maxResults);

		ResponseEvent<List<CollectionProtocolSummary>> resp = cpSvc.getProtocols(getRequest(crit));
		resp.throwErrorIfUnsuccessful();		
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public  CollectionProtocolDetail getCollectionProtocol(@PathVariable("id") Long cpId) {
		CpQueryCriteria crit = new CpQueryCriteria();
		crit.setId(cpId);
		
		ResponseEvent<CollectionProtocolDetail> resp = cpSvc.getCollectionProtocol(getRequest(crit));
		resp.throwErrorIfUnsuccessful();		
		return resp.getPayload();
	}	
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/definition")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public void getCpDefFile(@PathVariable("id") Long cpId, HttpServletResponse response) 
	throws JsonProcessingException {
		CpQueryCriteria crit = new CpQueryCriteria();
		crit.setId(cpId);
		crit.setFullObject(true);
		
		ResponseEvent<CollectionProtocolDetail> resp = cpSvc.getCollectionProtocol(getRequest(crit));
		resp.throwErrorIfUnsuccessful();
		
		CollectionProtocolDetail cp = resp.getPayload();
		ObjectMapper mapper = new ObjectMapper();
		FilterProvider filters = new SimpleFilterProvider().addFilter("withoutId", SimpleBeanPropertyFilter.serializeAllExcept("id"));		
		String def = mapper.writer(filters).withDefaultPrettyPrinter().writeValueAsString(cp);
		
		response.setContentType("application/json");
		response.setHeader("Content-Disposition", "attachment;filename=CpDef_" + cpId + ".json");
			
		InputStream in = null;
		try {
			in = new ByteArrayInputStream(def.getBytes());
			IoUtil.copy(in, response.getOutputStream());
		} catch (IOException e) {
			throw new RuntimeException("Error sending file", e);
		} finally {
			IoUtil.close(in);
		}				
	}

	@RequestMapping(method = RequestMethod.POST, value="/definition")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody		
	public CollectionProtocolDetail importCpDef(@PathVariable("file") MultipartFile file) 
	throws IOException {
		CollectionProtocolDetail cp = new ObjectMapper().readValue(file.getBytes(), CollectionProtocolDetail.class);
		RequestEvent<CollectionProtocolDetail> req = new RequestEvent<CollectionProtocolDetail>(getSession(), cp);
		ResponseEvent<CollectionProtocolDetail> resp = cpSvc.importCollectionProtocol(req);
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
	
	@RequestMapping(method = RequestMethod.PUT, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CollectionProtocolDetail updateCollectionProtocol(@RequestBody CollectionProtocolDetail cp) {
		ResponseEvent<CollectionProtocolDetail> resp = cpSvc.updateCollectionProtocol(getRequest(cp));
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
