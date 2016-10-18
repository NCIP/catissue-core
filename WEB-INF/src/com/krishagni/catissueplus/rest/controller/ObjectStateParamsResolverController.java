package com.krishagni.catissueplus.rest.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.common.service.ObjectStateParamsResolver;
import com.krishagni.catissueplus.core.common.service.ObjectStateParamsResolverFactory;

@Controller
@RequestMapping("/object-state-params")
public class ObjectStateParamsResolverController {

	@Autowired
	ObjectStateParamsResolverFactory resolverFactory;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> getStateParams(
		@RequestParam(value = "objectName", required = true)
		String objectName,

		@RequestParam(value = "key", required = true)
		String key,

		@RequestParam(value = "value", required = true)
		Object value) {

		ObjectStateParamsResolver resolver = resolverFactory.getResolver(objectName);
		if (resolver == null) {
			return Collections.emptyMap();
		}

		return resolver.resolve(key, value);
	}
}
