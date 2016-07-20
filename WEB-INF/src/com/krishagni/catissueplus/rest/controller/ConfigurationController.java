package com.krishagni.catissueplus.rest.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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

import com.krishagni.catissueplus.core.biospecimen.events.FileDetail;
import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.common.events.ConfigSettingDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.common.util.Utility;

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
		String moduleName,

		@RequestParam(value = "property", required = false)
		String propertyName) {

		if (StringUtils.isNotBlank(moduleName) && StringUtils.isNotBlank(propertyName)) {
			ConfigSettingDetail setting = response(cfgSvc.getSetting(request(Pair.make(moduleName, propertyName))));
			return Collections.singletonList(setting);
		} else {
			return response(cfgSvc.getSettings(request(moduleName)));
		}
	}

	@RequestMapping(method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public ConfigSettingDetail saveConfigSetting(@RequestBody ConfigSettingDetail detail) {
		return response(cfgSvc.saveSetting(request(detail)));
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

	@RequestMapping(method = RequestMethod.GET, value="/files")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public void downloadSettingFile(
			@RequestParam(value = "module", required = true)
			String moduleName,

			@RequestParam(value = "property", required = true)
			String propertyName,

			HttpServletResponse httpResp) throws IOException {

		FileDetail detail = cfgSvc.getFileDetail(moduleName, propertyName, null);
		if (detail == null || detail.getFileIn() == null) {
			return;
		}

		Utility.sendToClient(httpResp, detail.getFilename(), detail.getContentType(), detail.getFileIn());
	}

	@RequestMapping(method = RequestMethod.POST, value="/files")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String uploadSettingFile(@PathVariable("file") MultipartFile file)
	throws IOException {
		InputStream in = null;
		try {
			in = file.getInputStream();

			FileDetail detail = new FileDetail();
			detail.setFilename(file.getOriginalFilename());
			detail.setFileIn(in);

			ResponseEvent<String> resp = cfgSvc.uploadSettingFile(request(detail));
			resp.throwErrorIfUnsuccessful();
			return resp.getPayload();
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	public static <T> RequestEvent<T> request(T payload) {
		return new RequestEvent<T>(payload);
	}

	public static <T> T response(ResponseEvent<T> resp) {
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
}
