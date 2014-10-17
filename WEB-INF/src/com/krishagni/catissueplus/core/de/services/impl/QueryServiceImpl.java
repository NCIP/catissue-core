package com.krishagni.catissueplus.core.de.services.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.email.EmailHandler;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.de.domain.AqlBuilder;
import com.krishagni.catissueplus.core.de.domain.QueryAuditLog;
import com.krishagni.catissueplus.core.de.domain.QueryFolder;
import com.krishagni.catissueplus.core.de.domain.SavedQuery;
import com.krishagni.catissueplus.core.de.domain.factory.QueryFolderFactory;
import com.krishagni.catissueplus.core.de.events.CreateQueryFolderEvent;
import com.krishagni.catissueplus.core.de.events.DeleteQueryEvent;
import com.krishagni.catissueplus.core.de.events.DeleteQueryFolderEvent;
import com.krishagni.catissueplus.core.de.events.ExecuteQueryEvent;
import com.krishagni.catissueplus.core.de.events.ExportDataFileEvent;
import com.krishagni.catissueplus.core.de.events.ExportQueryDataEvent;
import com.krishagni.catissueplus.core.de.events.FolderQueriesEvent;
import com.krishagni.catissueplus.core.de.events.FolderQueriesUpdatedEvent;
import com.krishagni.catissueplus.core.de.events.QueryAuditLogDetail;
import com.krishagni.catissueplus.core.de.events.QueryAuditLogEvent;
import com.krishagni.catissueplus.core.de.events.QueryAuditLogSummary;
import com.krishagni.catissueplus.core.de.events.QueryAuditLogsEvent;
import com.krishagni.catissueplus.core.de.events.QueryDataExportedEvent;
import com.krishagni.catissueplus.core.de.events.QueryDefEvent;
import com.krishagni.catissueplus.core.de.events.QueryDeletedEvent;
import com.krishagni.catissueplus.core.de.events.QueryExecutedEvent;
import com.krishagni.catissueplus.core.de.events.QueryFolderCreatedEvent;
import com.krishagni.catissueplus.core.de.events.QueryFolderDeletedEvent;
import com.krishagni.catissueplus.core.de.events.QueryFolderDetailEvent;
import com.krishagni.catissueplus.core.de.events.QueryFolderDetails;
import com.krishagni.catissueplus.core.de.events.QueryFolderSharedEvent;
import com.krishagni.catissueplus.core.de.events.QueryFolderSummary;
import com.krishagni.catissueplus.core.de.events.QueryFolderUpdatedEvent;
import com.krishagni.catissueplus.core.de.events.QueryFoldersEvent;
import com.krishagni.catissueplus.core.de.events.QuerySavedEvent;
import com.krishagni.catissueplus.core.de.events.QueryUpdatedEvent;
import com.krishagni.catissueplus.core.de.events.ReqExportDataFileEvent;
import com.krishagni.catissueplus.core.de.events.ReqFolderQueriesEvent;
import com.krishagni.catissueplus.core.de.events.ReqQueryAuditLogEvent;
import com.krishagni.catissueplus.core.de.events.ReqQueryAuditLogsEvent;
import com.krishagni.catissueplus.core.de.events.ReqQueryDefEvent;
import com.krishagni.catissueplus.core.de.events.ReqQueryFolderDetailEvent;
import com.krishagni.catissueplus.core.de.events.ReqQueryFoldersEvent;
import com.krishagni.catissueplus.core.de.events.ReqSavedQueriesSummaryEvent;
import com.krishagni.catissueplus.core.de.events.ReqSavedQueryDetailEvent;
import com.krishagni.catissueplus.core.de.events.SaveQueryEvent;
import com.krishagni.catissueplus.core.de.events.SavedQueriesSummaryEvent;
import com.krishagni.catissueplus.core.de.events.SavedQueryDetail;
import com.krishagni.catissueplus.core.de.events.SavedQueryDetailEvent;
import com.krishagni.catissueplus.core.de.events.SavedQuerySummary;
import com.krishagni.catissueplus.core.de.events.ShareQueryFolderEvent;
import com.krishagni.catissueplus.core.de.events.UpdateFolderQueriesEvent;
import com.krishagni.catissueplus.core.de.events.UpdateQueryEvent;
import com.krishagni.catissueplus.core.de.events.UpdateQueryFolderEvent;
import com.krishagni.catissueplus.core.de.repository.DaoFactory;
import com.krishagni.catissueplus.core.de.repository.QueryAuditLogDao;
import com.krishagni.catissueplus.core.de.repository.SavedQueryDao;
import com.krishagni.catissueplus.core.de.services.QueryService;
import com.krishagni.catissueplus.core.de.services.SavedQueryErrorCode;
import com.krishagni.catissueplus.core.privileges.PrivilegeType;
import com.krishagni.catissueplus.core.privileges.services.PrivilegeService;

import edu.common.dynamicextensions.query.Query;
import edu.common.dynamicextensions.query.QueryParserException;
import edu.common.dynamicextensions.query.QueryResponse;
import edu.common.dynamicextensions.query.QueryResultCsvExporter;
import edu.common.dynamicextensions.query.QueryResultData;
import edu.common.dynamicextensions.query.QueryResultExporter;
import edu.common.dynamicextensions.query.QueryResultScreener;
import edu.common.dynamicextensions.query.ResultColumn;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.CommonServiceLocator;

public class QueryServiceImpl implements QueryService {
	private static final String cpForm = "CollectionProtocol";

	private static final String cprForm = "Participant";

	private static final String dateFormat = CommonServiceLocator.getInstance().getDatePattern();

	private static final String timeFormat = CommonServiceLocator.getInstance().getTimePattern();
	
	private static final int EXPORT_THREAD_POOL_SIZE = getThreadPoolSize();
	
	private static final int ONLINE_EXPORT_TIMEOUT_SECS = 30;
	
	private static final String EXPORT_DATA_DIR = getExportDataDir();
	
	private static ExecutorService exportThreadPool = Executors.newFixedThreadPool(EXPORT_THREAD_POOL_SIZE);
		
	private DaoFactory daoFactory;

	private UserDao userDao;
	
	private QueryFolderFactory queryFolderFactory;
	
	private PrivilegeService privilegeSvc;
	
	static {
		initExportFileCleaner();
	}

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	

	public QueryFolderFactory getQueryFolderFactory() {
		return queryFolderFactory;
	}

	public void setQueryFolderFactory(QueryFolderFactory queryFolderFactory) {
		this.queryFolderFactory = queryFolderFactory;
	}

	public PrivilegeService getPrivilegeSvc() {
		return privilegeSvc;
	}

	public void setPrivilegeSvc(PrivilegeService privilegeSvc) {
		this.privilegeSvc = privilegeSvc;
	}

	@Override
	@PlusTransactional
	public SavedQueriesSummaryEvent getSavedQueries(ReqSavedQueriesSummaryEvent req) {
		try {
			if (req.getStartAt() < 0 || req.getMaxRecords() <= 0) {
				String msg = SavedQueryErrorCode.INVALID_PAGINATION_FILTER.message();
				return SavedQueriesSummaryEvent.badRequest(msg, null);
			}

			Long userId = req.getSessionDataBean().getUserId();
			List<SavedQuerySummary> queries = daoFactory.getSavedQueryDao().getQueries(
							userId, 
							req.getStartAt(), req.getMaxRecords(), 
							req.getSearchString());
			
			Long count = null;
			if (req.isCountReq()) {
				count = daoFactory.getSavedQueryDao().getQueriesCount(userId, req.getSearchString());
			}
			
			return SavedQueriesSummaryEvent.ok(queries, count);
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

			Query.createQuery().wideRows(true).ic(true)
					.dateFormat(dateFormat).timeFormat(timeFormat)
					.compile(cprForm, getAql(queryDetail));
			SavedQuery savedQuery = getSavedQuery(req.getSessionDataBean(), queryDetail);
			daoFactory.getSavedQueryDao().saveOrUpdate(savedQuery);
			return QuerySavedEvent.ok(SavedQueryDetail.fromSavedQuery(savedQuery));
		} catch (QueryParserException qpe) {
			return QuerySavedEvent.badRequest(qpe.getMessage(), qpe);
		} catch (IllegalArgumentException iae) {
			return QuerySavedEvent.badRequest(iae.getMessage(), iae);		
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

			Query.createQuery().wideRows(true).ic(true)
					.dateFormat(dateFormat).timeFormat(timeFormat)
					.compile(cprForm, getAql(queryDetail));
			SavedQuery savedQuery = getSavedQuery(req.getSessionDataBean(), queryDetail);
			SavedQuery existing = daoFactory.getSavedQueryDao().getQuery(queryDetail.getId());
			existing.update(savedQuery);

			daoFactory.getSavedQueryDao().saveOrUpdate(existing);	
			return QueryUpdatedEvent.ok(SavedQueryDetail.fromSavedQuery(existing));
		} catch (QueryParserException qpe) {
			return QueryUpdatedEvent.badRequest(qpe.getMessage(), qpe);
		} catch (IllegalArgumentException iae) {
			return QueryUpdatedEvent.badRequest(iae.getMessage(), iae);
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
	public QueryDeletedEvent deleteQuery(DeleteQueryEvent req) {
		try {
			Long queryId = req.getQueryId();
			SavedQuery query = daoFactory.getSavedQueryDao().getQuery(queryId);
			if (query == null) {
				return QueryDeletedEvent.notFound(queryId);
			}

			boolean isAdmin = req.getSessionDataBean().isAdmin();
			Long userId = req.getSessionDataBean().getUserId();
			if (!isAdmin && !query.getCreatedBy().getId().equals(userId)) {
				return QueryDeletedEvent.notAuthorized(queryId);
			}

			query.setDeletedOn(Calendar.getInstance().getTime());
			daoFactory.getSavedQueryDao().saveOrUpdate(query);
			return QueryDeletedEvent.ok(queryId);
		} catch (Exception e) {
			return QueryDeletedEvent.serverError(e.getMessage(), e);
		}
	}

	@Override
	@PlusTransactional
	public QueryExecutedEvent executeQuery(ExecuteQueryEvent req) {
		QueryResultData queryResult = null;
		
		try {
			SessionDataBean sdb = req.getSessionDataBean();
			boolean countQuery = req.getRunType().equals("Count");
			
			Query query = Query.createQuery()
					.wideRows(req.isWideRows()).ic(true)
					.dateFormat(dateFormat).timeFormat(timeFormat);
			query.compile(
					cprForm, 
					getAqlWithCpIdInSelect(sdb, countQuery, req.getAql()), 
					getRestriction(sdb, req.getCpId()));
			
			QueryResponse resp = query.getData();
			insertAuditLog(req, resp);
			
			queryResult = resp.getResultData();
			queryResult.setScreener(new QueryResultScreenerImpl(sdb, countQuery));
			
			Integer[] indices = null;
			if (req.getIndexOf() != null && !req.getIndexOf().isEmpty()) {
				indices = queryResult.getColumnIndices(req.getIndexOf());
			}
			
			return QueryExecutedEvent.ok(
					queryResult.getColumnLabels(), queryResult.getStringifiedRows(), 
					queryResult.getDbRowsCount(), indices);
		} catch (QueryParserException qpe) {
			return QueryExecutedEvent.badRequest(qpe.getMessage(), qpe);
		} catch (IllegalArgumentException iae) {
			return QueryExecutedEvent.badRequest(iae.getMessage(), iae);
		} catch (IllegalAccessError iae) {
			return QueryExecutedEvent.notAuthorized(iae.getMessage(), iae);
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Internal Server Error";
			}
			return QueryExecutedEvent.serverError(message, e);
		} finally {
			if (queryResult != null) {
				try {
					queryResult.close();
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
		}
	}

	@Override
	@PlusTransactional
	public QueryDataExportedEvent exportQueryData(ExportQueryDataEvent req) {
		try {
			SessionDataBean sdb = req.getSessionDataBean();
			boolean countQuery = req.getRunType().equals("Count");
			
			
			Query query = Query.createQuery();
			query.wideRows(req.isWideRows())
				.ic(true)
				.dateFormat(dateFormat).timeFormat(timeFormat)
				.compile(
						cprForm, 
						getAqlWithCpIdInSelect(sdb, countQuery, req.getAql()), 
						getRestriction(sdb, req.getCpId()));
			
			String filename = UUID.randomUUID().toString();
			boolean completed = exportData(filename, query, req);
			return QueryDataExportedEvent.ok(filename, completed);
		} catch (QueryParserException qpe) {
			return QueryDataExportedEvent.badRequest(qpe.getMessage(), qpe);		
		} catch (IllegalArgumentException iae) {
			return QueryDataExportedEvent.badRequest(iae.getMessage(), iae);
		} catch (IllegalAccessError iae) {
			return QueryDataExportedEvent.notAuthorized(iae.getMessage(), iae);
		} catch (Exception e) {
			return QueryDataExportedEvent.serverError("Error exporting data", e);
		}
	}
	
	@Override
	public ExportDataFileEvent getExportDataFile(ReqExportDataFileEvent req) {
		String fileId = req.getFileId();
		try {
			String path = EXPORT_DATA_DIR + File.separator + fileId;
			File f = new File(path);
			if (f.exists()) {
				return ExportDataFileEvent.ok(fileId, f);
			} else {
				return ExportDataFileEvent.notFound(fileId);
			}
		} catch (Exception e) {
			return ExportDataFileEvent.serverError(fileId, e.getMessage(), e);
		}
	}
		
	@Override
	@PlusTransactional
	public QueryFoldersEvent getUserFolders(ReqQueryFoldersEvent req) {
		try {
			Long userId = req.getSessionDataBean().getUserId();			
			List<QueryFolder> queryFolders = daoFactory.getQueryFolderDao().getUserFolders(userId);

			List<QueryFolderSummary> result = new ArrayList<QueryFolderSummary>();
			for (QueryFolder folder : queryFolders) {
				result.add(QueryFolderSummary.fromQueryFolder(folder));
			}
			
			return QueryFoldersEvent.ok(result);
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Internal Server Error";
			}
			return QueryFoldersEvent.serverError(message, e);
		}
	}
	
	@Override
	@PlusTransactional
	public QueryFolderDetailEvent getFolder(ReqQueryFolderDetailEvent req) {
		try {
			Long folderId = req.getFolderId();
			QueryFolder folder = daoFactory.getQueryFolderDao().getQueryFolder(folderId);
			if (folder == null) {
				return QueryFolderDetailEvent.notFound(folderId);
			}
			
			Long userId = req.getSessionDataBean().getUserId();
			if (!req.getSessionDataBean().isAdmin() && !folder.canUserAccess(userId)) {
				return QueryFolderDetailEvent.notAuthorized(folderId);
			}
			
			return QueryFolderDetailEvent.ok(QueryFolderDetails.fromQueryFolder(folder));			
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Internal Server Error";
			}
			return QueryFolderDetailEvent.serverError(message, e);			
		}
	}	
	
	
	@Override
	@PlusTransactional
	public QueryFolderCreatedEvent createFolder(CreateQueryFolderEvent req) {
		try {
			QueryFolderDetails folderDetails = req.getFolderDetails();
			
			UserSummary owner = new UserSummary();
			owner.setId(req.getSessionDataBean().getUserId());
			folderDetails.setOwner(owner);
			
			QueryFolder queryFolder = queryFolderFactory.createQueryFolder(folderDetails);	
			
			daoFactory.getQueryFolderDao().saveOrUpdate(queryFolder);
			
			if (!queryFolder.getSharedWith().isEmpty()) {
				sendFolderSharedEmail(queryFolder.getOwner(), queryFolder, queryFolder.getSharedWith());
			}			
			return QueryFolderCreatedEvent.ok(QueryFolderDetails.fromQueryFolder(queryFolder));
		} catch (ObjectCreationException oce) {
			return QueryFolderCreatedEvent.badRequest(oce);
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Internal Server Error";
			}
			return QueryFolderCreatedEvent.serverError(message, e);
		}
	}

	@Override
	@PlusTransactional
	public QueryFolderUpdatedEvent updateFolder(UpdateQueryFolderEvent req) {
		try {
			QueryFolderDetails folderDetails = req.getFolderDetails();
			Long folderId = folderDetails.getId();
			if (folderId == null) {
				return QueryFolderUpdatedEvent.badRequest(SavedQueryErrorCode.FOLDER_ID_REQUIRED);
			}
						
			QueryFolder existing = daoFactory.getQueryFolderDao().getQueryFolder(folderId);
			if (existing == null) {
				return QueryFolderUpdatedEvent.badRequest(SavedQueryErrorCode.FOLDER_DOESNT_EXISTS);
			}
			
			Long userId = req.getSessionDataBean().getUserId();
			if (!req.getSessionDataBean().isAdmin() && !existing.getOwner().getId().equals(userId)) {
				return QueryFolderUpdatedEvent.badRequest(SavedQueryErrorCode.USER_NOT_AUTHORISED);
			}
			
			UserSummary owner = new UserSummary();
			owner.setId(existing.getOwner().getId());
			folderDetails.setOwner(owner);
			
			QueryFolder queryFolder = queryFolderFactory.createQueryFolder(folderDetails);
			Set<User> newUsers = new HashSet<User>(queryFolder.getSharedWith());
			newUsers.removeAll(existing.getSharedWith());
			existing.update(queryFolder);
			
			daoFactory.getQueryFolderDao().saveOrUpdate(existing);
			
			if (!newUsers.isEmpty()) {
				User user = userDao.getUser(userId);
				sendFolderSharedEmail(user, queryFolder, newUsers);
			}
			return QueryFolderUpdatedEvent.ok(QueryFolderDetails.fromQueryFolder(existing));			
		} catch (ObjectCreationException oce) {
			return QueryFolderUpdatedEvent.badRequest(oce);
		} catch (Exception e) {		
			String message = e.getMessage();
			if (message == null) {
				message = "Internal Server Error";
			}
			return QueryFolderUpdatedEvent.serverError(message, e);
		}
	}

	@Override
	@PlusTransactional
	public QueryFolderDeletedEvent deleteFolder(DeleteQueryFolderEvent req) {
		try {
			Long folderId = req.getFolderId();
			if (folderId == null) {
				return QueryFolderDeletedEvent.notFound(folderId);
			}

			QueryFolder existing = daoFactory.getQueryFolderDao().getQueryFolder(folderId);
			if (existing == null) {
				return QueryFolderDeletedEvent.notFound(folderId);								
			}
			
			Long userId = req.getSessionDataBean().getUserId();
			if (!req.getSessionDataBean().isAdmin() && !existing.getOwner().getId().equals(userId)) {
				return QueryFolderDeletedEvent.notAuthorized(folderId);
			}
			
			daoFactory.getQueryFolderDao().deleteFolder(existing);
			return QueryFolderDeletedEvent.ok(folderId);
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Internal Server Error";
			}
			return QueryFolderDeletedEvent.serverError(message, e);
		}
	}
	
	@Override
	@PlusTransactional
	public FolderQueriesEvent getFolderQueries(ReqFolderQueriesEvent req) {
		try {
			Long folderId = req.getFolderId();
			QueryFolder folder = daoFactory.getQueryFolderDao().getQueryFolder(folderId);			
			if (folder == null) {
				return FolderQueriesEvent.notFound(folderId);
			}
			
			Long userId = req.getSessionDataBean().getUserId();
			if (!req.getSessionDataBean().isAdmin() && !folder.canUserAccess(userId)) {
				return FolderQueriesEvent.notAuthorized(folderId);
			}
			
			List<SavedQuerySummary> queries = daoFactory.getSavedQueryDao().getQueriesByFolderId(
					folderId, 
					req.getStartAt(), 
					req.getMaxRecords(),
					req.getSearchString());
			
			Long count = null;
			if (req.isCountReq()) {
				count = daoFactory.getSavedQueryDao().getQueriesCountByFolderId(folderId, req.getSearchString());
			}
			
			return FolderQueriesEvent.ok(queries, count);
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Internal Server Error";
			}
			return FolderQueriesEvent.serverError(message, e);
		}
	}
	
	@Override
	@PlusTransactional
	public FolderQueriesUpdatedEvent updateFolderQueries(UpdateFolderQueriesEvent req) {
		try {
			Long folderId = req.getFolderId();
			QueryFolder queryFolder = daoFactory.getQueryFolderDao().getQueryFolder(folderId);			
			if (queryFolder == null) {
				return FolderQueriesUpdatedEvent.notFound(folderId);
			}
			
			Long userId = req.getSessionDataBean().getUserId();
			if (!req.getSessionDataBean().isAdmin() && !queryFolder.getOwner().getId().equals(userId)) {
				return FolderQueriesUpdatedEvent.notAuthorized(folderId);
			}
			
			List<SavedQuery> savedQueries = null;
			List<Long> queryIds = req.getQueries();
			
			if (queryIds == null || queryIds.isEmpty()) {
				savedQueries = new ArrayList<SavedQuery>();
			} else {
				savedQueries = daoFactory.getSavedQueryDao().getQueriesByIds(queryIds);
			}
			
			switch (req.getOp()) {
				case ADD:
					queryFolder.addQueries(savedQueries);
					break;
				
				case UPDATE:
					queryFolder.updateQueries(savedQueries);
					break;
				
				case REMOVE:
					queryFolder.removeQueries(savedQueries);
					break;				
			}
			
			daoFactory.getQueryFolderDao().saveOrUpdate(queryFolder);			
			List<SavedQuerySummary> result = new ArrayList<SavedQuerySummary>();
			for (SavedQuery query : savedQueries) {
				result.add(SavedQuerySummary.fromSavedQuery(query));
			}
			
			return FolderQueriesUpdatedEvent.ok(folderId, result);
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Internal Server Error";
			}
			return FolderQueriesUpdatedEvent.serverError(message, e);
		}
	}
	
	@Override
	@PlusTransactional
	public QueryFolderSharedEvent shareFolder(ShareQueryFolderEvent req) {
		try {
			Long folderId = req.getFolderId();
			QueryFolder queryFolder = daoFactory.getQueryFolderDao().getQueryFolder(folderId);
			if (queryFolder == null) {
				return QueryFolderSharedEvent.notFound(folderId);
			}
			
			Long userId = req.getSessionDataBean().getUserId();
			if (!req.getSessionDataBean().isAdmin() && !queryFolder.getOwner().getId().equals(userId)) {
				return QueryFolderSharedEvent.notAuthorized(folderId);
			}
			
			
			List<User> users = null;
			List<Long> userIds = req.getUserIds();
			if (userIds == null || userIds.isEmpty()) {
				users = new ArrayList<User>();
			} else {
				users = userDao.getUsersById(userIds);
			}
			
			Collection<User> newUsers = null; 
			switch (req.getOp()) {
				case ADD:
					newUsers = queryFolder.addSharedUsers(users);
					break;
					
				case UPDATE:
					newUsers = queryFolder.updateSharedUsers(users);
					break;
					
				case REMOVE:
					queryFolder.removeSharedUsers(users);
					break;					
			}
											
			daoFactory.getQueryFolderDao().saveOrUpdate(queryFolder);			
			List<UserSummary> result = new ArrayList<UserSummary>();
			for (User user : queryFolder.getSharedWith()) {
				result.add(UserSummary.fromUser(user));
			}
			
			if (newUsers != null && !newUsers.isEmpty()) {
				User user = userDao.getUser(userId);
				sendFolderSharedEmail(user, queryFolder, newUsers);
			}
			
			return QueryFolderSharedEvent.ok(folderId, result);
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Internal Server Error";
			}
			return QueryFolderSharedEvent.serverError(message, e);
		}
	}
	
    @Override
    @PlusTransactional
    public QueryAuditLogsEvent getAuditLogs(ReqQueryAuditLogsEvent req){
		try {
			SessionDataBean session = req.getSessionDataBean();			
			Long userId = session.getUserId();
			
			Long savedQueryId = req.getSavedQueryId();
			int startAt = req.getStartAt() < 0 ? 0 : req.getStartAt();
			int maxRecs = req.getMaxRecords() < 0 ? 0 : req.getMaxRecords();

			QueryAuditLogDao logDao = daoFactory.getQueryAuditLogDao();
			List<QueryAuditLogSummary> auditLogs = null;
			Long count = null;
			if (savedQueryId == null || savedQueryId == -1) {
				if (!session.isAdmin()) {
					return QueryAuditLogsEvent.forbidden();
				}
				
				switch (req.getType()) {
					case ALL:
						auditLogs = logDao.getAuditLogs(startAt, maxRecs);
						if (req.isCountReq()) {
							count = logDao.getAuditLogsCount();
						}
						break;
						
					case LAST_24:
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.DAY_OF_MONTH, -1);
						Date intervalSt = cal.getTime();						
						Date intervalEnd = Calendar.getInstance().getTime();
						auditLogs = logDao.getAuditLogs(intervalSt, intervalEnd, startAt, maxRecs);
						if (req.isCountReq()) {
							count = logDao.getAuditLogsCount(intervalSt, intervalEnd);
						}						
						break;
				}
								
			} else {
				auditLogs = logDao.getAuditLogs(savedQueryId, userId, startAt, maxRecs);
			}
			
			return QueryAuditLogsEvent.ok(auditLogs, count);
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Internal Server Error";
			}

			return QueryAuditLogsEvent.serverError(message, e);
		}
    }
    
    @Override
    @PlusTransactional
    public QueryAuditLogEvent getAuditLog(ReqQueryAuditLogEvent req) {
		try {
			Long auditLogId = req.getAuditLogId();
			QueryAuditLog queryAuditLog = daoFactory.getQueryAuditLogDao().getAuditLog(auditLogId);
			return QueryAuditLogEvent.ok(QueryAuditLogDetail.from(queryAuditLog));
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Internal Server Error";
			}

			return QueryAuditLogEvent.serverError(message, e);
		}
    }
    
	@Override
	@PlusTransactional
	public QueryDefEvent getQueryDef(ReqQueryDefEvent req) {
		try {
			SavedQueryDao queryDao = daoFactory.getSavedQueryDao();
			
			Long queryId = req.getQueryId();			
			SavedQuery query = queryDao.getQuery(queryId);			
			if (query == null) {
				return QueryDefEvent.notFound(queryId);
			}
			
			Long userId = req.getSessionDataBean().getUserId();
			if (!query.getCreatedBy().getId().equals(userId) && 
					!queryDao.isQuerySharedWithUser(queryId, userId)) {
				return QueryDefEvent.forbidden(queryId);
			}
			
			String queryDef = query.getQueryDefJson(true);
			return QueryDefEvent.ok(queryId, queryDef);			
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Internal Server Error";				
			}
			
			return QueryDefEvent.serverError(message, e);
		}
	}
         
	private SavedQuery getSavedQuery(SessionDataBean sdb, SavedQueryDetail detail) {
		User user = new User();
		user.setId(sdb.getUserId());
		
		SavedQuery savedQuery = new SavedQuery();		
		savedQuery.setTitle(detail.getTitle());
		savedQuery.setCpId(detail.getCpId());
		savedQuery.setSelectList(detail.getSelectList());
		savedQuery.setFilters(detail.getFilters());
		savedQuery.setQueryExpression(detail.getQueryExpression());
		if (detail.getId() == null) {
			savedQuery.setCreatedBy(user);
		}
		
		savedQuery.setLastUpdatedBy(user);
		savedQuery.setLastUpdated(Calendar.getInstance().getTime());
		savedQuery.setReporting(detail.getReporting());
		return savedQuery;
	}


	private String getAql(SavedQueryDetail queryDetail) {
		return AqlBuilder.getInstance().getQuery(
				queryDetail.getSelectList(),
				queryDetail.getFilters(),
				queryDetail.getQueryExpression());
	}
	
	private boolean exportData(final String filename, final Query query, final ExportQueryDataEvent req) 
	throws ExecutionException, InterruptedException, CancellationException {
		Future<Boolean> result = exportThreadPool.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {				
				QueryResultExporter exporter = new QueryResultCsvExporter();
				String path = EXPORT_DATA_DIR + File.separator + filename;
				
				Transaction txn = startTxn();
				QueryResponse resp = exporter.export(
						path, query, new QueryResultScreenerImpl(req.getSessionDataBean(), false));
				try {
					insertAuditLog(req, resp);
					sendEmail();
					txn.commit();
				} catch (Exception e) {
					if (txn != null) {
						txn.rollback();
					}
				}
                
				return true;
			}
			
			private void sendEmail() {
				try {
					User user = userDao.getUser(req.getSessionDataBean().getUserId());
					
					SavedQuery savedQuery = null;
					Long queryId = req.getSavedQueryId();
					if (queryId != null) {
						savedQuery = daoFactory.getSavedQueryDao().getQuery(queryId);
					}					
					EmailHandler.sendQueryDataExportedEmail(user, savedQuery, filename);					
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
		});
		
		try {
			return result.get(ONLINE_EXPORT_TIMEOUT_SECS, TimeUnit.SECONDS);
		} catch (TimeoutException te) {
			return false;
		}		
	}
	
	private String getRestriction(SessionDataBean sdb, Long cpId) {
		if (sdb.isAdmin()) {
			if (cpId != null && cpId != -1) {
				return cpForm + ".id = " + cpId;
			}
		} else {
			List<Long> cpIds = privilegeSvc.getCpList(sdb.getUserId(), PrivilegeType.READ.value());
			if (cpIds == null || cpIds.isEmpty()) {
				throw new IllegalAccessError("User does not have access to any CP");
			}
			
			if (cpId != null && cpId != -1) {
				if (cpIds.contains(cpId)) {
					return cpForm + ".id = " + cpId; 
				}
				
				throw new IllegalAccessError("Access to cp is not permitted: " + cpId);
			} else {
				List<String> restrictions = new ArrayList<String>();
				
				int startIdx = 0, numCpIds = cpIds.size();
				int chunkSize = 999;
				while (startIdx < numCpIds) {
					int endIdx = startIdx + chunkSize;
					if (endIdx > numCpIds) {
						endIdx = numCpIds;
					}
					
					restrictions.add(getCpIdRestriction(cpIds.subList(startIdx, endIdx)));
					startIdx = endIdx;
				}
				
				return "(" + StringUtils.join(restrictions, " or ") + ")";
			}
		}
		
		return null;
	}
	
	private String getCpIdRestriction(List<Long> cpIds) {
		return new StringBuilder(cpForm)
			.append(".id in (")
			.append(StringUtils.join(cpIds, ", "))
			.append(")")
			.toString();
	}
			
	private String getAqlWithCpIdInSelect(SessionDataBean sdb, boolean isCount, String aql) {
		if (sdb.isAdmin() || isCount) {
			return aql;
		} else {
			String afterSelect = aql.trim().substring(6);
			return "select " + cpForm + ".id, " + afterSelect;
		}
	}
	
	private static int getThreadPoolSize() {
		String poolSize = XMLPropertyHandler.getValue("query.exportThreadPoolSize");
		if (poolSize == null || poolSize.isEmpty()) {
			return 5;
		}
		
		return Integer.parseInt(poolSize);
	}
	
	private static String getExportDataDir() {
		String dir = new StringBuilder(XMLPropertyHandler.getValue("appserver.home.dir")).append(File.separator)
			.append("os-data").append(File.separator)
			.append("query-exported-data").append(File.separator)
			.toString();
		
		File dirFile = new File(dir);
		if (!dirFile.exists()) {
			if (!dirFile.mkdirs()) {
				throw new RuntimeException("Error couldn't create directory for exporting query data");
			}
		}
		
		return dir;
	}

	private void insertAuditLog(ExecuteQueryEvent req, QueryResponse resp) {
		User user = new User();
		user.setId(req.getSessionDataBean().getUserId());

		QueryAuditLog auditLog = new QueryAuditLog();
		auditLog.setQueryId(req.getSavedQueryId());
		auditLog.setRunBy(user);
		auditLog.setTimeOfExecution(resp.getTimeOfExecution());
		auditLog.setTimeToFinish(resp.getExecutionTime());
		auditLog.setRunType(req.getRunType());
		auditLog.setSql(resp.getSql());
		daoFactory.getQueryAuditLogDao().saveOrUpdate(auditLog);
	}
	
	private Transaction startTxn() {
		AbstractDao<?> dao = (AbstractDao<?>)daoFactory.getQueryAuditLogDao();
		Session session = dao.getSessionFactory().getCurrentSession();
		Transaction txn = session.getTransaction();
		if (txn == null || !txn.isActive()) {
			txn = session.beginTransaction();			
		}
		
		return txn;
	}
		
	private class QueryResultScreenerImpl implements QueryResultScreener {
		private SessionDataBean sdb;
		
		private boolean countQuery;
		
		private Map<Long, Boolean> phiAccessMap = new HashMap<Long, Boolean>();
		
		private static final String mask = "##########";
		
		public QueryResultScreenerImpl(SessionDataBean sdb, boolean countQuery) {
			this.sdb = sdb;
			this.countQuery = countQuery;
		}

		@Override
		public List<ResultColumn> getScreenedResultColumns(List<ResultColumn> preScreenedResultCols) {
			if (sdb.isAdmin() || this.countQuery) {
				return preScreenedResultCols;
			}
			
			List<ResultColumn> result = new ArrayList<ResultColumn>(preScreenedResultCols);
			result.remove(0);
			return result;
		}

		@Override
		public Object[] getScreenedRowData(List<ResultColumn> preScreenedResultCols, Object[] rowData) {
			if (sdb.isAdmin() || this.countQuery || rowData.length == 0) {
				return rowData;
			}
						
			Long cpId = ((Number)rowData[0]).longValue();
			Object[] screenedData = ArrayUtils.remove(rowData, 0);
			
			Boolean phiAccess = phiAccessMap.get(cpId);
			if (phiAccess == null) {
				phiAccess = privilegeSvc.hasPrivilege(sdb.getUserId(), cpId, PrivilegeType.PHI_ACCESS.value());
				phiAccess = phiAccess != null && phiAccess.equals(true);
				phiAccessMap.put(cpId, phiAccess);
			}
			
			if (phiAccess) {
				return screenedData;
			}
			
			int i = 0; 
			boolean first = true;
			for (ResultColumn col : preScreenedResultCols) {
				if (first) {
					first = false;
					continue;
				}
				
				if (col.isPhi()) {
					screenedData[i] = mask;
				}
				
				++i;
			}
			
			return screenedData;
		}		
	}
	
	private static void initExportFileCleaner() {
		long currentTime = Calendar.getInstance().getTime().getTime();
		
		Calendar nextDayStart = Calendar.getInstance();
		nextDayStart.set(Calendar.HOUR, 0);
		nextDayStart.set(Calendar.MINUTE, 0);
		nextDayStart.set(Calendar.SECOND, 0);
		nextDayStart.set(Calendar.MILLISECOND, 0);
		nextDayStart.add(Calendar.DATE, 1);
				
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
				new Runnable() {					
					@Override
					public void run() {
						try {
							Calendar cal = Calendar.getInstance();
							cal.add(Calendar.DAY_OF_MONTH, -30);
							long deleteBeforeTime = cal.getTimeInMillis();
							
							File dir = new File(EXPORT_DATA_DIR);							
							for (File file : dir.listFiles()) {								
								if (file.lastModified() <= deleteBeforeTime) {
									file.delete();
								}
							}
						} catch (Exception e) {
							
						}						
					}
				},
				nextDayStart.getTimeInMillis() - currentTime, 
				24 * 60 * 60 * 1000, 
				TimeUnit.MILLISECONDS);						
	}
	
	private void sendFolderSharedEmail(User user, QueryFolder folder, Collection<User> sharedUsers) {
		for (User sharedWith : sharedUsers) {
			EmailHandler.sendQueryFolderSharedEmail(user, folder, sharedWith);
		}		
	}
}