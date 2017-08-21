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

public class ImportSpecimenEventForms extends ImportForms {
	private Map<String, Boolean[]> eventFormsInfo = new HashMap<String, Boolean[]>();

	@Override
	protected Collection<String> listFormFiles() throws IOException {
		BufferedReader reader = null;
		InputStream in = null;
		try {
			in = Utility.getResourceInputStream("/spe-forms/list.csv");
			reader = new BufferedReader(new InputStreamReader(in));
			
			eventFormsInfo.clear();
			
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}
				
				//
				// Note: Since this file is created by developers and not end users,
				// we do not do any kind of validations here
				//				
				String[] tokens = line.split(","); 
				Boolean[] info = {
					Boolean.parseBoolean(tokens[1].trim()), /* is system event */ 
					Boolean.parseBoolean(tokens[2].trim())  /* is multi-record */
				};
				eventFormsInfo.put("/spe-forms/" + tokens[0].trim(), info);
			}
			
			return eventFormsInfo.keySet();
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(reader);
		}		
	}

	@Override
	protected boolean isSysForm(String formFile) {
		return eventFormsInfo.get(formFile)[0];
	}

	@Override
	protected FormContextBean getFormContext(String formFile, Long formId) {
		FormContextBean formCtx = getDaoFactory().getFormDao().getFormContext(formId, -1L, "SpecimenEvent");
		if (formCtx == null) {
			formCtx = new FormContextBean();
		}
		
		Boolean[] eventInfo = eventFormsInfo.get(formFile);
		 
		formCtx.setContainerId(formId);
		formCtx.setCpId(-1L);
		formCtx.setEntityType("SpecimenEvent");
		formCtx.setSysForm(eventInfo[0]);
		formCtx.setMultiRecord(eventInfo[1]);
		formCtx.setSortOrder(null);		
		return formCtx;		
	}
	
	@Override
	protected void cleanup() {
		eventFormsInfo.clear();		
	}
}
