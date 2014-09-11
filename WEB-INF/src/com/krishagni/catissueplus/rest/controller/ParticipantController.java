
package com.krishagni.catissueplus.rest.controller;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
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

import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.CreateParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.MatchParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantMatchedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantPatchDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.PatchParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SubRegistrationDetailsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationInfo;
import com.krishagni.catissueplus.core.biospecimen.events.ReqParticipantDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSubRegistrationDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.services.ParticipantService;
import com.krishagni.catissueplus.core.common.events.EventStatus;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/participants")
public class ParticipantController {

	private static final String PATCH_PARTICIPANT = "patch participant";

	@Autowired
	private HttpServletRequest httpServletRequest;

	@Autowired
	private ParticipantService participantSvc;

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ParticipantDetail getParticipantById(@PathVariable("id") Long participantId) {
		ReqParticipantDetailEvent event = new ReqParticipantDetailEvent();
		event.setParticipantId(participantId);

		ParticipantDetailEvent resp = participantSvc.getParticipant(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getParticipantDetail();
	}
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/registrations")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<RegistrationInfo> getRegistrationsforParticipant(@PathVariable("id") Long participantId,
			@RequestParam(value = "cpId", required = false, defaultValue = "") String cpId) {
		ReqSubRegistrationDetailEvent event = new ReqSubRegistrationDetailEvent();
		event.setParticipantId(participantId);
		event.setCpId(StringUtils.isBlank(cpId)?null:Long.valueOf(cpId));

		SubRegistrationDetailsEvent resp = participantSvc.getSubRegistrationDetails(event);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getRegistrationInfo();
		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/SubCpRegistrations")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<RegistrationInfo> getSubCPRegistrationsforParticipant(@PathVariable("id") Long participantId,
			@RequestParam(value = "cpId", required = true, defaultValue = "") String cpId) {
		ReqSubRegistrationDetailEvent event = new ReqSubRegistrationDetailEvent();
		event.setParticipantId(participantId);
		event.setCpId(StringUtils.isBlank(cpId)?null:Long.valueOf(cpId));

		SubRegistrationDetailsEvent resp = participantSvc.getSubRegistrationDetails(event);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getRegistrationInfo();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ParticipantDetail createParticipant(@RequestBody ParticipantDetail participantDetail) {
		CreateParticipantEvent event = new CreateParticipantEvent();
		event.setSessionDataBean(getSession());
		event.setParticipantDetail(participantDetail);
		ParticipantCreatedEvent resp = participantSvc.createParticipant(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getParticipantDetail();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ParticipantDetail updateParticipant(@PathVariable Long id, @RequestBody ParticipantDetail participantDetail) {
		UpdateParticipantEvent event = new UpdateParticipantEvent();
		event.setParticipantId(id);
		event.setSessionDataBean(getSession());
		event.setParticipantDetail(participantDetail);
		ParticipantUpdatedEvent resp = participantSvc.updateParticipant(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getParticipantDetail();
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ParticipantDetail patchParticipant(@PathVariable Long id, @RequestBody Map<String, Object> values) {
		PatchParticipantEvent event = new PatchParticipantEvent();
		ParticipantPatchDetail detail = new ParticipantPatchDetail();
		try {
			BeanUtils.populate(detail, values);
		}
		catch (Exception e) {
			reportError(ParticipantErrorCode.BAD_REQUEST, PATCH_PARTICIPANT);
		}
		detail.setModifiedAttributes(new ArrayList<String>(values.keySet()));
		event.setParticipantDetail(detail);
		event.setId(id);
		event.setSessionDataBean(getSession());
		ParticipantUpdatedEvent resp = participantSvc.patchParticipant(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getParticipantDetail();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String delete(@PathVariable Long id,
			@RequestParam(value = "includeChildren", required = false, defaultValue = "false") String includeChildren) {
		DeleteParticipantEvent event = new DeleteParticipantEvent();
		event.setSessionDataBean(getSession());
		event.setIncludeChildren(Boolean.valueOf(includeChildren));
		event.setId(id);
		ParticipantDeletedEvent resp = participantSvc.delete(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getMessage();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/matchParticipants")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ParticipantDetail> getMatchedParticipants(@RequestBody ParticipantDetail participantDetail) {
		MatchParticipantEvent event = new MatchParticipantEvent();
		event.setSessionDataBean(getSession());
		event.setParticipantDetail(participantDetail);
		ParticipantMatchedEvent resp = participantSvc.getMatchingParticipants(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getMatchingParticipants();
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
