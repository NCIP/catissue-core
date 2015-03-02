package com.krishagni.catissueplus.core.common.service;

import java.util.Map;

public interface TemplateService {
	public String render(String templateName, Map<String, String> properties);
}
