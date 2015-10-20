package com.krishagni.catissueplus.core.init;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import krishagni.catissueplus.beans.FormContextBean;

import org.apache.commons.io.IOUtils;

import com.krishagni.catissueplus.core.common.util.Utility;

public class ImportDeForms extends ImportForms  {
	private Map<String, String> formsInfo = new HashMap<String, String>();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		setCreateTable(true);
		super.afterPropertiesSet();
	}
	
	@Override
	protected Collection<String> listFormFiles() 
	throws IOException {		
		BufferedReader reader = null;
		InputStream in = null;
		try {
			in = Utility.getResourceInputStream("de-forms/list.csv");
			
			formsInfo.clear();
			
			if (in != null) {
				reader = new BufferedReader(new InputStreamReader(in));
				String line = null;
				while ((line = reader.readLine()) != null) {
					String[] tokens = line.split(",");
					formsInfo.put("de-forms/" + tokens[0].trim(), tokens[1].trim());
				}
			}
			
			return formsInfo.keySet();
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(reader);
		}		
	}

	@Override
	protected FormContextBean getFormContext(String formFile, Long formId) {
		String entityType = formsInfo.get(formFile);
		FormContextBean formCtx = getDaoFactory().getFormDao().getFormContext(formId, -1L, entityType);
		if (formCtx == null) {
			formCtx = new FormContextBean();
		}

		formCtx.setContainerId(formId);
		formCtx.setCpId(-1L);
		formCtx.setEntityType(entityType);
		formCtx.setMultiRecord(false);
		formCtx.setSortOrder(null);
		formCtx.setSysForm(true);
		return formCtx;
	}

	@Override
	protected void cleanup() {
		formsInfo.clear();
	}	
}
