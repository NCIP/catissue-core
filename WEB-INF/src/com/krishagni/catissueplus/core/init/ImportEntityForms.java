package com.krishagni.catissueplus.core.init;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.krishagni.catissueplus.core.common.util.Utility;

import krishagni.catissueplus.beans.FormContextBean;

public class ImportEntityForms extends ImportForms {
	private int order = 1;
	
	private Map<String, String> entityMap = new HashMap<String, String>();
	
	private String moduleName;
	
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		setCreateTable(true);
		super.afterPropertiesSet();
	}

	@Override 
	protected List<String> listFormFiles() 
	throws IOException {		
		BufferedReader reader = null;
		InputStream in = null;
		try {
			in = Utility.getResourceInputStream("com/krishagni/openspecimen/custom/" + moduleName + "/entity-forms/list.txt");
			reader = new BufferedReader(new InputStreamReader(in));
			
			String line = null;
			while ((line = reader.readLine()) != null) {
				String entityType = line.split("=")[0];
				String fileName = line.split("=")[1];
				entityMap.put(moduleName + "/entity-forms/" + fileName, entityType);
			}
			
			System.err.println("Forms: " + entityMap.keySet());
			return new ArrayList<String>(entityMap.keySet());
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(reader);
		}
	}

	@Override
	protected FormContextBean getFormContext(String formFile, Long formId) {
		String entityType = entityMap.get(formFile);
		FormContextBean formCtx = getDaoFactory().getFormDao().getFormContext(formId, -1L, entityType);
		if (formCtx == null) {
			formCtx = new FormContextBean();
		}

		formCtx.setContainerId(formId);
		formCtx.setCpId(-1L);
		formCtx.setEntityType(entityType);
		formCtx.setMultiRecord(false);
		formCtx.setSortOrder(order);
		formCtx.setSysForm(true);
		return formCtx;		
	}

	@Override
	protected void cleanup() {
		order = 1;
	}

}
