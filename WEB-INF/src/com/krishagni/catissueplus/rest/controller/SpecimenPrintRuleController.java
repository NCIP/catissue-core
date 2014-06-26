
package com.krishagni.catissueplus.rest.controller;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.printer.printRule.domain.factory.PrintRuleErrorCode;
import com.krishagni.catissueplus.core.printer.printRule.events.CreatePrintRuleEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.DeletePrintRuleEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.PatchPrintRuleEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.PrintRuleCreatedEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.PrintRuleDeletedEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.PrintRuleUpdatedEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.SpecimenPrintRuleDetails;
import com.krishagni.catissueplus.core.printer.printRule.events.UpdatePrintRuleEvent;
import com.krishagni.catissueplus.core.printer.printRule.services.PrintRuleService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("specimen/print-rules")
public class SpecimenPrintRuleController {

	private static String PATCH_PRINT_RULE = "patch print rule";

	@Autowired
	private PrintRuleService specimenPrintRuleSvc;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenPrintRuleDetails createSpecimenPrintRule(@RequestBody SpecimenPrintRuleDetails specimenPrintRuleDetails) {
		PrintRuleCreatedEvent resp = specimenPrintRuleSvc
				.createPrintRule(new CreatePrintRuleEvent(specimenPrintRuleDetails));
		if (resp.getStatus() == EventStatus.OK) {
			return (SpecimenPrintRuleDetails) resp.getPrintRuleDetails();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/name={ruleName}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public SpecimenPrintRuleDetails updateSpecimenPrintRuleByName(@PathVariable String ruleName,
			@RequestBody SpecimenPrintRuleDetails specimenPrintRuleDetails) {
		PrintRuleUpdatedEvent resp = specimenPrintRuleSvc
				.updatePrintRule(new UpdatePrintRuleEvent(specimenPrintRuleDetails, ruleName));
		if (resp.getStatus() == EventStatus.OK) {
			return (SpecimenPrintRuleDetails) resp.getPrintRuleDetails();
		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{ruleId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public SpecimenPrintRuleDetails updateSpecimenPrintRule(@PathVariable Long ruleId,
			@RequestBody SpecimenPrintRuleDetails specimenPrintRuleDetails) {
		PrintRuleUpdatedEvent resp = specimenPrintRuleSvc
				.updatePrintRule(new UpdatePrintRuleEvent(specimenPrintRuleDetails, ruleId));
		if (resp.getStatus() == EventStatus.OK) {
			return (SpecimenPrintRuleDetails) resp.getPrintRuleDetails();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/name={ruleName}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenPrintRuleDetails patchSpecimenPrintRuleByName(@PathVariable String ruleName,
			@RequestBody Map<String, Object> values) {
		PatchPrintRuleEvent event = new PatchPrintRuleEvent();
		event.setPrintRuleName(ruleName);
		event.setSessionDataBean(getSession());

		SpecimenPrintRuleDetails details = new SpecimenPrintRuleDetails();
		try {
			BeanUtils.populate(details, values);
		}
		catch (Exception e) {
			reportError(PrintRuleErrorCode.BAD_REQUEST, PATCH_PRINT_RULE);
		}
		details.setModifiedAttributes(new ArrayList<String>(values.keySet()));
		event.setPrintRuleDetails(details);

		PrintRuleUpdatedEvent response = specimenPrintRuleSvc.patchPrintRule(event);
		if (response.getStatus() == EventStatus.OK) {
			return (SpecimenPrintRuleDetails) response.getPrintRuleDetails();
		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.PATCH, value = "/{ruleId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenPrintRuleDetails patchSpecimenPrintRule(@PathVariable Long ruleId,
			@RequestBody Map<String, Object> values) {
		PatchPrintRuleEvent event = new PatchPrintRuleEvent();
		event.setPrintRuleId(ruleId);
		event.setSessionDataBean(getSession());

		SpecimenPrintRuleDetails details = new SpecimenPrintRuleDetails();
		try {
			BeanUtils.populate(details, values);
		}
		catch (Exception e) {
			reportError(PrintRuleErrorCode.BAD_REQUEST, PATCH_PRINT_RULE);
		}
		details.setModifiedAttributes(new ArrayList<String>(values.keySet()));
		event.setPrintRuleDetails(details);

		PrintRuleUpdatedEvent response = specimenPrintRuleSvc.patchPrintRule(event);
		if (response.getStatus() == EventStatus.OK) {
			return (SpecimenPrintRuleDetails) response.getPrintRuleDetails();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/name={ruleName}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String deleteSpecimenPrintRuleByName(@PathVariable String ruleName) {
		DeletePrintRuleEvent event = new DeletePrintRuleEvent();
		event.setPrintRuleName(ruleName);
		event.setSessionDataBean(getSession());
		PrintRuleDeletedEvent resp = specimenPrintRuleSvc.deletePrintRule(event);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getMessage();
		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{ruleId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String deleteSpecimenPrintRule(@PathVariable Long ruleId) {
		DeletePrintRuleEvent event = new DeletePrintRuleEvent();
		event.setPrintRuleId(ruleId);
		event.setSessionDataBean(getSession());
		PrintRuleDeletedEvent resp = specimenPrintRuleSvc.deletePrintRule(event);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getMessage();
		}
		return null;
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
