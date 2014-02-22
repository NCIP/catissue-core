package com.krishagni.catissueplus.core.de.services.impl;

import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.de.events.ExecuteQueryEvent;
import com.krishagni.catissueplus.core.de.events.QueryExecutedEvent;
import com.krishagni.catissueplus.core.de.services.QueryService;

import edu.common.dynamicextensions.query.Query;
import edu.common.dynamicextensions.query.QueryParserException;
import edu.common.dynamicextensions.query.QueryResultData;

public class QueryServiceImpl implements QueryService {
	private static final String cpForm = "CollectionProtocol";
	
	private static final String cprForm = "CollectionProtocolRegistration";
	
	private static final String dateFormat = "MM/dd/yyyy";

	@Override
	@PlusTransactional
	public QueryExecutedEvent executeQuery(ExecuteQueryEvent req) {
		try {
			Query query = Query.createQuery();
			query.wideRows(req.isWideRows()).ic(true).dateFormat(dateFormat).compile(cprForm, req.getAql());
			QueryResultData queryResult = query.getData();			
			return QueryExecutedEvent.ok(queryResult.getColumnLabels(), queryResult.getRows());
		} catch (QueryParserException qpe) {
			return QueryExecutedEvent.badRequest(qpe.getMessage(), qpe);
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Internal Server Error";
			}
			return QueryExecutedEvent.serverError(message, e);			
		}
	}
}
