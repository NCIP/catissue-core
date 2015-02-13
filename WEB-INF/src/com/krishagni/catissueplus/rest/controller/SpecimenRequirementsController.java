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

import com.krishagni.catissueplus.core.biospecimen.domain.AliquotSpecimensRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.DerivedSpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenRequirementDetail;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("specimen-requirements")
public class SpecimenRequirementsController {

	@Autowired
	private CollectionProtocolService cpSvc;
	
	@Autowired
	private HttpServletRequest httpServletRequest;		

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenRequirementDetail> getRequirements(
			@RequestParam(value = "eventId", required = true) 
			Long eventId) {
		
		ResponseEvent<List<SpecimenRequirementDetail>> resp = cpSvc.getSpecimenRequirments(getRequest(eventId));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenRequirementDetail addRequirement(@RequestBody SpecimenRequirementDetail requirement) {
		ResponseEvent<SpecimenRequirementDetail> resp = cpSvc.addSpecimenRequirement(getRequest(requirement));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();		
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/{id}/aliquots")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenRequirementDetail> createAliquots(
			@PathVariable("id") Long parentSrId,
			@RequestBody AliquotSpecimensRequirement requirement) {
		
		requirement.setParentSrId(parentSrId);
		ResponseEvent<List<SpecimenRequirementDetail>> resp = cpSvc.createAliquots(getRequest(requirement));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();		
	}

	@RequestMapping(method = RequestMethod.POST, value="/{id}/derived")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenRequirementDetail createDerived(
			@PathVariable("id") Long parentSrId,
			@RequestBody DerivedSpecimenRequirement requirement) {
		
		requirement.setParentSrId(parentSrId);
		ResponseEvent<SpecimenRequirementDetail> resp = cpSvc.createDerived(getRequest(requirement));
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
