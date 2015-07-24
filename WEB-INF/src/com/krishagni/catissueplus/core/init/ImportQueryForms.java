package com.krishagni.catissueplus.core.init;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import krishagni.catissueplus.beans.FormContextBean;

import org.apache.commons.io.IOUtils;

import com.krishagni.catissueplus.core.common.util.Utility;

public class ImportQueryForms extends ImportForms {
	private int order = 1;
	
	@Override
	protected List<String> listFormFiles() 
	throws IOException {		
		BufferedReader reader = null;
		InputStream in = null;
		try {
			in = Utility.getResourceInputStream("/query-forms/list.txt");
			reader = new BufferedReader(new InputStreamReader(in));
			
			List<String> formFiles = new ArrayList<String>();			
			String file = null;
			while ((file = reader.readLine()) != null) {
				formFiles.add("/query-forms/" + file);
			}
			
			return formFiles;
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(reader);
		}		
	}

	@Override
	protected FormContextBean getFormContext(String formFile, Long formId) {
		FormContextBean formCtx = getDaoFactory().getFormDao().getFormContext(formId, -1L, "Query");
		if (formCtx == null) {
			formCtx = new FormContextBean();
		}

		formCtx.setContainerId(formId);
		formCtx.setCpId(-1L);
		formCtx.setEntityType("Query");
		formCtx.setMultiRecord(false);
		formCtx.setSortOrder(order++);
		formCtx.setSysForm(true);
		return formCtx;		
	}

	@Override
	protected void cleanup() {
		order = 1;
	}	
}