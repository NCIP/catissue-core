package com.krishagni.catissueplus.core.de.services;

import com.krishagni.catissueplus.core.de.events.ExecuteQueryEvent;
import com.krishagni.catissueplus.core.de.events.QueryExecutedEvent;



public interface QueryService {
	public QueryExecutedEvent executeQuery(ExecuteQueryEvent req);
}
