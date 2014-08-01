
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

import com.krishagni.catissueplus.core.administrative.events.AddPvEvent;
import com.krishagni.catissueplus.core.administrative.events.AllPvsEvent;
import com.krishagni.catissueplus.core.administrative.events.DeletePvEvent;
import com.krishagni.catissueplus.core.administrative.events.EditPvEvent;
import com.krishagni.catissueplus.core.administrative.events.GetAllPVsEvent;
import com.krishagni.catissueplus.core.administrative.events.PermissibleValueDetails;
import com.krishagni.catissueplus.core.administrative.events.PvAddedEvent;
import com.krishagni.catissueplus.core.administrative.events.PvDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.PvEditedEvent;
import com.krishagni.catissueplus.core.administrative.events.PvInfo;
import com.krishagni.catissueplus.core.administrative.services.PermissibleValueService;
import com.krishagni.catissueplus.core.common.events.EventStatus;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/permissible-values")
public class PermissibleValueController {

	@Autowired
	private PermissibleValueService pvSvc;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public PermissibleValueDetails createPermissleValue(@RequestBody PermissibleValueDetails details) {
		AddPvEvent event = new AddPvEvent(details);
		event.setSessionDataBean(getSession());
		PvAddedEvent resp = pvSvc.createPermissibleValue(event);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getDetails();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public PermissibleValueDetails updatePermissibleValue(@PathVariable Long id,
			@RequestBody PermissibleValueDetails details) {
		EditPvEvent event = new EditPvEvent(details, id);
		event.setSessionDataBean(getSession());
		PvEditedEvent resp = pvSvc.updatePermissibleValue(event);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getDetails();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String deletePermissibleValue(@PathVariable Long id) {
		DeletePvEvent event = new DeletePvEvent();
		event.setId(id);
		event.setSessionDataBean(getSession());
		PvDeletedEvent resp = pvSvc.deletePermissibleValue(event);

		if (resp.getStatus() == EventStatus.OK) {
			return resp.getMessage();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/attribute={attribute}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<PvInfo> getPermissibleValue(@PathVariable String attribute,
			@RequestParam(value = "searchString", required = false, defaultValue = "") String searchStr,
			@RequestParam(value = "maxResults", required = false, defaultValue = "1000") String maxResults) {
		GetAllPVsEvent event = new GetAllPVsEvent();
		event.setAttribute(attribute);
		event.setSearchString(searchStr);
		event.setMaxResult(Integer.parseInt(maxResults));
		event.setSessionDataBean(getSession());
		AllPvsEvent resp = pvSvc.getPermissibleValues(event);

		if (resp.getStatus() == EventStatus.OK) {
			return resp.getPvs();
		}
		return null;
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
