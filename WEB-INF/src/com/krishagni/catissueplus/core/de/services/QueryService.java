package com.krishagni.catissueplus.core.de.services;

import com.krishagni.catissueplus.core.de.events.SavedQueryDetailEvent;
import com.krishagni.catissueplus.core.de.events.ExecuteQueryEvent;
import com.krishagni.catissueplus.core.de.events.QueryExecutedEvent;
import com.krishagni.catissueplus.core.de.events.QuerySavedEvent;
import com.krishagni.catissueplus.core.de.events.QueryUpdatedEvent;
import com.krishagni.catissueplus.core.de.events.ReqSavedQueryDetailEvent;
import com.krishagni.catissueplus.core.de.events.ReqSavedQueriesSummaryEvent;
import com.krishagni.catissueplus.core.de.events.SaveQueryEvent;
import com.krishagni.catissueplus.core.de.events.SavedQueriesSummaryEvent;
import com.krishagni.catissueplus.core.de.events.UpdateQueryEvent;

public interface QueryService {	
	public SavedQueriesSummaryEvent getSavedQueries(ReqSavedQueriesSummaryEvent req);
	
	public SavedQueryDetailEvent getSavedQuery(ReqSavedQueryDetailEvent req);
	
	public QuerySavedEvent saveQuery(SaveQueryEvent req);
			
	public QueryUpdatedEvent updateQuery(UpdateQueryEvent req);
	
	public QueryExecutedEvent executeQuery(ExecuteQueryEvent req);	
}
