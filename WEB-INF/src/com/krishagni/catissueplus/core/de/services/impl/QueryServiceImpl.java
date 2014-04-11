package com.krishagni.catissueplus.core.de.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.de.domain.AqlBuilder;
import com.krishagni.catissueplus.core.de.domain.SavedQuery;
import com.krishagni.catissueplus.core.de.events.SavedQueryDetailEvent;
import com.krishagni.catissueplus.core.de.events.ExecuteQueryEvent;
import com.krishagni.catissueplus.core.de.events.QueryExecutedEvent;
import com.krishagni.catissueplus.core.de.events.QuerySavedEvent;
import com.krishagni.catissueplus.core.de.events.SavedQuerySummary;
import com.krishagni.catissueplus.core.de.events.QueryUpdatedEvent;
import com.krishagni.catissueplus.core.de.events.ReqSavedQueryDetailEvent;
import com.krishagni.catissueplus.core.de.events.ReqSavedQueriesSummaryEvent;
import com.krishagni.catissueplus.core.de.events.SaveQueryEvent;
import com.krishagni.catissueplus.core.de.events.SavedQueriesSummaryEvent;
import com.krishagni.catissueplus.core.de.events.SavedQueryDetail;
import com.krishagni.catissueplus.core.de.events.UpdateQueryEvent;
import com.krishagni.catissueplus.core.de.repository.DaoFactory;
import com.krishagni.catissueplus.core.de.services.QueryService;
import com.krishagni.catissueplus.core.de.services.SavedQueryErrorCode;

import edu.common.dynamicextensions.query.Query;
import edu.common.dynamicextensions.query.QueryParserException;
import edu.common.dynamicextensions.query.QueryResultData;
import edu.wustl.common.beans.SessionDataBean;

public class QueryServiceImpl implements QueryService {
	private static final String cpForm = "CollectionProtocol";

	private static final String cprForm = "CollectionProtocolRegistration";

	private static final String dateFormat = "MM/dd/yyyy";

	private DaoFactory daoFactory;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public SavedQueriesSummaryEvent getSavedQueries(ReqSavedQueriesSummaryEvent req) {
		try {
			if (req.getStartAt() < 0 || req.getMaxRecords() <= 0) {
				String msg = SavedQueryErrorCode.INVALID_PAGINATION_FILTER.message();
				return SavedQueriesSummaryEvent.badRequest(msg, null);
			}

			List<SavedQuery> queries = daoFactory.getSavedQueryDao().getQueries(req.getStartAt(), req.getMaxRecords());
			return SavedQueriesSummaryEvent.ok(toQuerySummaryList(queries));
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Internal Server Error";
			}
			return SavedQueriesSummaryEvent.serverError(message, e);
		}
	}
	
	@Override
	@PlusTransactional
	public SavedQueryDetailEvent getSavedQuery(ReqSavedQueryDetailEvent req) {
		try {
			SavedQuery savedQuery = daoFactory.getSavedQueryDao().getQuery(req.getQueryId());				
			return SavedQueryDetailEvent.ok(SavedQueryDetail.fromSavedQuery(savedQuery));
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Internal Server Error";
			}
			return SavedQueryDetailEvent.serverError(message, e);
		}
	}
	
	@Override
	@PlusTransactional
	public QuerySavedEvent saveQuery(SaveQueryEvent req) {
		try {
			SavedQueryDetail queryDetail = req.getSavedQueryDetail();
			if (queryDetail.getId() != null) {
				return QuerySavedEvent.badRequest(SavedQueryErrorCode.QUERY_ID_FOUND.message(), null);
			}
			
			Query query = Query.createQuery();
			query.wideRows(true).ic(true).dateFormat(dateFormat).compile(cprForm, getAql(queryDetail));
			
			SavedQuery savedQuery = getSavedQuery(req.getSessionDataBean(), queryDetail);			
			daoFactory.getSavedQueryDao().saveOrUpdate(savedQuery);
			return QuerySavedEvent.ok(SavedQueryDetail.fromSavedQuery(savedQuery));
		} catch (QueryParserException qpe) {
			return QuerySavedEvent.badRequest(qpe.getMessage(), qpe);
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Internal Server Error";
			}
			return QuerySavedEvent.serverError(message, e);
		}
	}

	@Override
	@PlusTransactional
	public QueryUpdatedEvent updateQuery(UpdateQueryEvent req) {
		try {
			SavedQueryDetail queryDetail = req.getSavedQueryDetail();

			if (queryDetail.getId() == null) {
				return QueryUpdatedEvent.badRequest(SavedQueryErrorCode.QUERY_ID_NOT_FOUND.message(), null);
			}

			Query query = Query.createQuery();
			query.wideRows(true).ic(true).dateFormat(dateFormat).compile(cprForm, getAql(queryDetail));
									
			SavedQuery savedQuery = getSavedQuery(req.getSessionDataBean(), queryDetail);
			SavedQuery existing = daoFactory.getSavedQueryDao().getQuery(queryDetail.getId());
			existing.update(savedQuery);
			
			daoFactory.getSavedQueryDao().saveOrUpdate(existing);			
			return QueryUpdatedEvent.ok(SavedQueryDetail.fromSavedQuery(existing));
		} catch (QueryParserException qpe) {
			return QueryUpdatedEvent.badRequest(qpe.getMessage(), qpe);
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Internal Server Error";
			}
			return QueryUpdatedEvent.serverError(message, e);
		}
	}
	
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


	
	private List<SavedQuerySummary> toQuerySummaryList(List<SavedQuery> queries) {
		List<SavedQuerySummary> querySummaries = new ArrayList<SavedQuerySummary>();
		
		for (SavedQuery savedQuery : queries) {
			querySummaries.add(SavedQuerySummary.fromSavedQuery(savedQuery));
		}

		return querySummaries;
	}
	
	private SavedQuery getSavedQuery(SessionDataBean sdb, SavedQueryDetail detail) {
		User user = new User();
		user.setId(sdb.getUserId());

		SavedQuery savedQuery = new SavedQuery();
		savedQuery.setTitle(detail.getTitle());
		savedQuery.setSelectList(detail.getSelectList());
		savedQuery.setFilters(detail.getFilters());
		savedQuery.setQueryExpression(detail.getQueryExpression());		
		if (detail.getId() == null) {
			savedQuery.setCreatedBy(user);
		}
		
		savedQuery.setLastUpdatedBy(user);
		savedQuery.setLastUpdated(Calendar.getInstance().getTime());
		return savedQuery;
	}
	
	private String getAql(SavedQueryDetail queryDetail) {
		return AqlBuilder.getInstance().getQuery(				
				queryDetail.getSelectList(), 
				queryDetail.getFilters(), 
				queryDetail.getQueryExpression());
		
	}
}
