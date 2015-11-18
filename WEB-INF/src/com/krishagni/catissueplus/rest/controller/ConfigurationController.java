package com.krishagni.catissueplus.rest.controller;

import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.common.events.ConfigSettingDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;

@Controller
@RequestMapping("/config-settings")
public class ConfigurationController {
	
	@Autowired
	private ConfigurationService cfgSvc;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public List<ConfigSettingDetail> getConfigSettings(
			@RequestParam(value = "module", required = false) 
			String moduleName) {
		
		RequestEvent<String> req = new RequestEvent<String>(moduleName);
		ResponseEvent<List<ConfigSettingDetail>> resp = cfgSvc.getSettings(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public ConfigSettingDetail saveConfigSetting(@RequestBody ConfigSettingDetail detail) {
		RequestEvent<ConfigSettingDetail> req = new RequestEvent<ConfigSettingDetail>(detail);
		ResponseEvent<ConfigSettingDetail> resp = cfgSvc.saveSetting(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/locale")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Object> getLocaleSettings() {
		return cfgSvc.getLocaleSettings();
	}	
	
	@RequestMapping(method = RequestMethod.GET, value="/app-props")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Object> getAppProps() {
		return cfgSvc.getAppProps();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/welcome-video")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, String> getWelcomeVideoSettings() {
		return cfgSvc.getWelcomeVideoSettings();
	}
	
}
