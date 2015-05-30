package com.krishagni.catissueplus.core.biospecimen.services;

import java.util.Map;

public interface DocumentDeIdentifier {

	public String deIdentify(String data, Map<String, Object> contextMap);
}
