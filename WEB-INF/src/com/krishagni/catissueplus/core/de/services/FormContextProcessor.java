package com.krishagni.catissueplus.core.de.services;

import krishagni.catissueplus.beans.FormContextBean;

public interface FormContextProcessor {
	public void onSaveOrUpdate(FormContextBean formCtxt);

	public void onRemove(FormContextBean formCtxt);
}