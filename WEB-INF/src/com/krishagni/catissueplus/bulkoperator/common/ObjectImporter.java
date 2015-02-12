package com.krishagni.catissueplus.bulkoperator.common;

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface ObjectImporter<T> {
	public ResponseEvent<T> importObject(RequestEvent<T> req);

}
