package com.krishagni.catissueplus.core.importer.services;

public interface ImportListener {
	public void success();

	public void fail(Throwable t);
}
