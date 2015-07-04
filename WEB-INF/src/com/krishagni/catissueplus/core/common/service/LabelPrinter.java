package com.krishagni.catissueplus.core.common.service;

import java.util.List;

import com.krishagni.catissueplus.core.common.domain.LabelPrintJob;

public interface LabelPrinter<T> {
	public LabelPrintJob print(List<T> objects, int numCopies);
}
