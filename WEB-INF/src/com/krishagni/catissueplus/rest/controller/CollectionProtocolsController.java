
package com.krishagni.catissueplus.rest.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
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
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierOp;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierOp.OP;
import com.krishagni.catissueplus.core.biospecimen.events.CopyCpOpDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CpQueryCriteria;
import com.krishagni.catissueplus.core.biospecimen.events.CpWorkflowCfgDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CpWorkflowCfgDetail.WorkflowDetail;
import com.krishagni.catissueplus.core.biospecimen.events.FileDetail;
import com.krishagni.catissueplus.core.biospecimen.events.MergeCpDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.CpListCriteria;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.events.DeleteEntityOp;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.EntityDeleteResp;
import com.krishagni.catissueplus.core.common.events.Operation;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.Resource;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.catissueplus.core.de.events.CpCatalogSettingDetail;
import com.krishagni.catissueplus.core.de.events.FormSummary;
import com.krishagni.catissueplus.core.de.events.SavedQuerySummary;
import com.krishagni.catissueplus.core.de.services.CatalogService;
import com.krishagni.catissueplus.core.de.services.FormService;
import com.krishagni.catissueplus.core.query.Column;
import com.krishagni.catissueplus.core.query.ListConfig;
import com.krishagni.catissueplus.core.query.ListDetail;
import com.krishagni.catissueplus.core.query.ListGenerator;

import edu.common.dynamicextensions.nutility.IoUtil;

@Controller
@RequestMapping("/collection-protocols")
public class CollectionProtocolsController {

	@Autowired
	private CollectionProtocolService cpSvc;
	
	@Autowired
	private FormService formSvc;

	@Autowired
	private CatalogService catalogSvc;

	@Autowired
	private ListGenerator listGenerator;

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
			
			@RequestParam(value = "repositoryName", required = false)
			String repositoryName,
			
			@RequestParam(value = "startAt", required = false, defaultValue = "0") 
			int startAt,
			
			@RequestParam(value = "maxResults", required = false, defaultValue = "100")
			int maxResults,
			
			@RequestParam(value = "detailedList", required = false, defaultValue = "false") 
			boolean detailedList) {
		
		CpListCriteria crit = new CpListCriteria()
			.query(searchStr)
			.title(title)
			.piId(piId)
			.repositoryName(repositoryName)
			.includePi(detailedList)
			.includeStat(detailedList)
			.startAt(startAt)
			.maxResults(maxResults);

		ResponseEvent<List<CollectionProtocolSummary>> resp = cpSvc.getProtocols(getRequest(crit));
		resp.throwErrorIfUnsuccessful();		
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/count")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Long> getCollectionProtocolsCount(
			@RequestParam(value = "query", required = false)
			String searchStr,
			
			@RequestParam(value = "title", required = false)
			String title,
			
			@RequestParam(value = "piId", required = false)
			Long piId,
			
			@RequestParam(value = "repositoryName", required = false)
			String repositoryName) {
		
		CpListCriteria crit = new CpListCriteria()
			.query(searchStr)
			.title(title)
			.piId(piId)
			.repositoryName(repositoryName);
		
		ResponseEvent<Long> resp = cpSvc.getProtocolsCount(getRequest(crit));
		resp.throwErrorIfUnsuccessful();
		return Collections.singletonMap("count", resp.getPayload());
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
		cp.setSopDocumentName(null);
		cp.setSopDocumentUrl(null);

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
		RequestEvent<CollectionProtocolDetail> req = new RequestEvent<CollectionProtocolDetail>(cp);
		ResponseEvent<CollectionProtocolDetail> resp = cpSvc.importCollectionProtocol(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}/sop-document")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public void downloadSopDocument(@PathVariable("id") Long cpId, HttpServletResponse httpResp)
	throws IOException {
		ResponseEvent<File> resp = cpSvc.getSopDocument(getRequest(cpId));
		resp.throwErrorIfUnsuccessful();

		File file = resp.getPayload();
		String fileName = file.getName().split("_", 2)[1];
		Utility.sendToClient(httpResp, fileName, file);
	}

	@RequestMapping(method = RequestMethod.POST, value="/sop-documents")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String uploadSopDocument(@PathVariable("file") MultipartFile file)
	throws IOException {
		InputStream in = null;
		try {
			in = file.getInputStream();

			FileDetail detail = new FileDetail();
			detail.setFilename(file.getOriginalFilename());
			detail.setFileIn(in);

			ResponseEvent<String> resp = cpSvc.uploadSopDocument(getRequest(detail));
			resp.throwErrorIfUnsuccessful();
			return resp.getPayload();
		} finally {
			IOUtils.closeQuietly(in);
		}
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
	
	@RequestMapping(method = RequestMethod.POST, value="/{cpId}/copy")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public CollectionProtocolDetail copyCollectionProtocol(
			@PathVariable("cpId") 
			Long cpId,
			
			@RequestBody 
			CollectionProtocolDetail cpDetail) {
		
		CopyCpOpDetail opDetail = new CopyCpOpDetail();
		opDetail.setCpId(cpId);
		opDetail.setCp(cpDetail);

		ResponseEvent<CollectionProtocolDetail> resp = cpSvc.copyCollectionProtocol(getRequest(opDetail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/consents-waived")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CollectionProtocolDetail updateConsentsWaived(@PathVariable Long id, @RequestBody Map<String, String> props) {
		CollectionProtocolDetail cp = new  CollectionProtocolDetail();
		cp.setId(id);
		cp.setConsentsWaived(Boolean.valueOf(props.get("consentsWaived")));
		
		ResponseEvent<CollectionProtocolDetail> resp = cpSvc.updateConsentsWaived(getRequest(cp));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}/dependent-entities")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<DependentEntityDetail> getCpDependentEntities(@PathVariable Long id) {
		ResponseEvent<List<DependentEntityDetail>> resp = cpSvc.getCpDependentEntities(getRequest(id));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public EntityDeleteResp<CollectionProtocolDetail> deleteCollectionProtocol(
			@PathVariable Long id,
			
			@RequestParam(value = "forceDelete", required = false, defaultValue = "false") 
			boolean forceDelete) {
		
		DeleteEntityOp crit = new DeleteEntityOp();
		crit.setId(id);
		crit.setForceDelete(forceDelete);
		
		ResponseEvent<EntityDeleteResp<CollectionProtocolDetail>> resp = cpSvc.deleteCollectionProtocol(getRequest(crit));
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
			@PathVariable("id") 
			Long cpId,
			
			@PathVariable("tierId") 
			Long tierId,
			
			@RequestBody 
			ConsentTierDetail consentTier) {
		
		consentTier.setId(tierId);
		return performConsentTierOp(OP.UPDATE, cpId, consentTier);
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/{id}/consent-tiers/{tierId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public ConsentTierDetail removeConsentTier(
			@PathVariable("id") 
			Long cpId,
			
			@PathVariable("tierId") 
			Long tierId) {
		
		ConsentTierDetail consentTier = new ConsentTierDetail();
		consentTier.setId(tierId);
		return performConsentTierOp(OP.REMOVE, cpId, consentTier);		
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}/consent-tiers/{tierId}/dependent-entities")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public List<DependentEntityDetail> getConsentDependentEntities(
			@PathVariable("id") 
			Long cpId,
			
			@PathVariable("tierId") 
			Long tierId) {
		ConsentTierDetail consentTierDetail = new ConsentTierDetail();
		consentTierDetail.setCpId(cpId);
		consentTierDetail.setId(tierId);
		
		ResponseEvent<List<DependentEntityDetail>> resp = cpSvc.getConsentDependentEntities(getRequest(consentTierDetail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/workflows")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody		
	public CpWorkflowCfgDetail getWorkflowCfg(@PathVariable("id") Long cpId) {
		ResponseEvent<CpWorkflowCfgDetail> resp = cpSvc.getWorkflows(new RequestEvent<>(cpId));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="/{id}/workflows")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody		
	public CpWorkflowCfgDetail saveWorkflowCfg(@PathVariable("id") Long cpId, @RequestBody List<WorkflowDetail> workflows) {
		CpWorkflowCfgDetail input = new CpWorkflowCfgDetail();
		input.setCpId(cpId);
		
		for (WorkflowDetail workflow : workflows) {
			input.getWorkflows().put(workflow.getName(), workflow);
		}
		
		ResponseEvent<CpWorkflowCfgDetail> resp = cpSvc.saveWorkflows(new RequestEvent<>(input));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	//
	// Catalog settings
	//
	@RequestMapping(method = RequestMethod.GET, value="/{id}/catalog-query")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SavedQuerySummary getCatalogQuery(@PathVariable("id") Long cpId) {
		CollectionProtocolSummary cp = new CollectionProtocolSummary();
		cp.setId(cpId);

		RequestEvent<CollectionProtocolSummary> req = new RequestEvent<CollectionProtocolSummary>(cp);
		ResponseEvent<SavedQuerySummary> resp = catalogSvc.getCpCatalogQuery(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/catalog-settings")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CpCatalogSettingDetail getCatalogSetting(@PathVariable("id") Long cpId) {
		CollectionProtocolSummary cp = new CollectionProtocolSummary();
		cp.setId(cpId);

		RequestEvent<CollectionProtocolSummary> req = new RequestEvent<CollectionProtocolSummary>(cp);
		ResponseEvent<CpCatalogSettingDetail> resp = catalogSvc.getCpSetting(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.PUT, value="/{id}/catalog-settings")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CpCatalogSettingDetail updateCatalogSetting(
			@PathVariable("id")
			Long cpId,

			@RequestBody
			CpCatalogSettingDetail detail) {

		CollectionProtocolSummary cp = new CollectionProtocolSummary();
		cp.setId(cpId);
		detail.setCp(cp);

		RequestEvent<CpCatalogSettingDetail> req = new RequestEvent<CpCatalogSettingDetail>(detail);
		ResponseEvent<CpCatalogSettingDetail> resp = catalogSvc.saveCpSetting(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/{id}/catalog-settings")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CpCatalogSettingDetail deleteCatalogSetting(@PathVariable("id") Long cpId) {
		CollectionProtocolSummary cp = new CollectionProtocolSummary();
		cp.setId(cpId);

		RequestEvent<CollectionProtocolSummary> req = new RequestEvent<CollectionProtocolSummary>(cp);
		ResponseEvent<CpCatalogSettingDetail> resp = catalogSvc.deleteCpSetting(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	//
	// For UI work
	//
	@RequestMapping(method = RequestMethod.GET, value="/byop")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody			
	public List<CollectionProtocolSummary> getCpListByOp(
			@RequestParam(value = "resource", required = true)
			String resourceName,
			
			@RequestParam(value = "op", required = true) 
			String opName,
			
			@RequestParam(value = "siteName", required = false)
			String[] siteNames,
			
			@RequestParam(value = "title", required = false)
			String searchTitle)	 {
				
		List<String> inputSiteList = Collections.emptyList();
		if (siteNames != null) {
			inputSiteList = Arrays.asList(siteNames);
		}
		
		Resource resource = Resource.fromName(resourceName);
		Operation op = Operation.fromName(opName);
		
		List<CollectionProtocolSummary> emptyList = Collections.<CollectionProtocolSummary>emptyList();
		ResponseEvent<List<CollectionProtocolSummary>> resp = new ResponseEvent<List<CollectionProtocolSummary>>(emptyList);
		if (resource == Resource.PARTICIPANT && op == Operation.CREATE) {
			 resp = cpSvc.getRegisterEnabledCps(inputSiteList, searchTitle);			
		}
		
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value="/extension-form")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Object> getForm() {
		return formSvc.getExtensionInfo(-1L, CollectionProtocol.EXTN);
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/forms")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<FormSummary> getForms(
			@PathVariable("id")
			Long cpId,

			@RequestParam(value = "entityType", required = true)
			String[] entityTypes) {
		return formSvc.getEntityForms(cpId, entityTypes);
	}

	@RequestMapping(method = RequestMethod.POST, value="/merge")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public MergeCpDetail mergeCollectionProtocol(@RequestBody MergeCpDetail mergeDetail) {
		ResponseEvent<MergeCpDetail> resp = cpSvc.mergeCollectionProtocols(getRequest(mergeDetail));
		resp.throwErrorIfUnsuccessful();

		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/list-config")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ListConfig getListConfig(
			@PathVariable("id")
			Long cpId,

			@RequestParam(value = "listName", required = true)
			String listName) {

		Map<String, Object> listCfgReq = new HashMap<>();
		listCfgReq.put("cpId", cpId);
		listCfgReq.put("listName", listName);

		ResponseEvent<ListConfig> resp = cpSvc.getCpListCfg(new RequestEvent<>(listCfgReq));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/expression-values")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Collection<Object> getExpressionValues(
			@PathVariable("id")
			Long cpId,

			@RequestParam(value = "expr", required = true)
			String expr,

			@RequestParam(value = "searchTerm", required = false, defaultValue = "")
			String searchTerm) {

		return listGenerator.getExpressionValues(cpId, expr, searchTerm);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{id}/specimens")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ListDetail getSpecimens(
			@PathVariable("id")
			Long cpId,

			@RequestParam(value = "startAt", required = false, defaultValue = "0")
			int startAt,

			@RequestParam(value = "maxResults", required = false, defaultValue = "100")
			int maxResults,

			@RequestBody
			List<Column> filters) {

		Map<String, Object> listReq = new HashMap<>();
		listReq.put("cpId", cpId);
		listReq.put("startAt", startAt);
		listReq.put("maxResults", maxResults);
		listReq.put("filters", filters);

		ResponseEvent<ListDetail> resp = cpSvc.getCpSpecimens(getRequest(listReq));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{id}/participants")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ListDetail getParticipants(
			@PathVariable("id")
			Long cpId,

			@RequestParam(value = "startAt", required = false, defaultValue = "0")
			int startAt,

			@RequestParam(value = "maxResults", required = false, defaultValue = "100")
			int maxResults,

			@RequestBody
			List<Column> filters) {

		Map<String, Object> listReq = new HashMap<>();
		listReq.put("cpId", cpId);
		listReq.put("startAt", startAt);
		listReq.put("maxResults", maxResults);
		listReq.put("filters", filters);

		ResponseEvent<ListDetail> resp = cpSvc.getCpParticipants(getRequest(listReq));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
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
		return new RequestEvent<T>(payload);
	}
}
