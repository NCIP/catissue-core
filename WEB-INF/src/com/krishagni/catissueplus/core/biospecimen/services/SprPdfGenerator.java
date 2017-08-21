package com.krishagni.catissueplus.core.biospecimen.services;

import java.io.File;
import java.util.Map;

public interface SprPdfGenerator {
	public File generate(File file, Map<String, Object> contextMap);
}
