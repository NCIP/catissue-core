package com.krishagni.catissueplus.core.common.service.impl;

import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.krishagni.catissueplus.core.common.service.TemplateService;

public class TemplateServiceImpl implements TemplateService {
	private VelocityEngine velocityEngine;
	
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public String render(String templateName, Map<String, Object> properties) {
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateName, properties);
	}
}
