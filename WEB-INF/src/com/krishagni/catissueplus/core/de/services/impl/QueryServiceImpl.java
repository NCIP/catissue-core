package com.krishagni.catissueplus.core.de.services.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
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
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.service.EmailService;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.catissueplus.core.de.domain.AqlBuilder;
import com.krishagni.catissueplus.core.de.domain.QueryAuditLog;
import com.krishagni.catissueplus.core.de.domain.QueryFolder;
import com.krishagni.catissueplus.core.de.domain.SavedQuery;
import com.krishagni.catissueplus.core.de.domain.factory.QueryFolderFactory;
import com.krishagni.catissueplus.core.de.events.ExecuteQueryEventOp;
import com.krishagni.catissueplus.core.de.events.ListFolderQueriesCriteria;
import com.krishagni.catissueplus.core.de.events.ListQueryAuditLogsCriteria;
import com.krishagni.catissueplus.core.de.events.ListSavedQueriesCriteria;
import com.krishagni.catissueplus.core.de.events.QueryAuditLogDetail;
import com.krishagni.catissueplus.core.de.events.QueryAuditLogSummary;
import com.krishagni.catissueplus.core.de.events.QueryAuditLogsList;
import com.krishagni.catissueplus.core.de.events.QueryDataExportResult;
import com.krishagni.catissueplus.core.de.events.QueryExecResult;
import com.krishagni.catissueplus.core.de.events.QueryFolderDetails;
import com.krishagni.catissueplus.core.de.events.QueryFolderSummary;
import com.krishagni.catissueplus.core.de.events.SavedQueriesList;
import com.krishagni.catissueplus.core.de.events.SavedQueryDetail;
import com.krishagni.catissueplus.core.de.events.SavedQuerySummary;
import com.krishagni.catissueplus.core.de.events.ShareQueryFolderOp;
import com.krishagni.catissueplus.core.de.events.UpdateFolderQueriesOp;
import com.krishagni.catissueplus.core.de.repository.DaoFactory;
import com.krishagni.catissueplus.core.de.repository.QueryAuditLogDao;
import com.krishagni.catissueplus.core.de.repository.SavedQueryDao;
import com.krishagni.catissueplus.core.de.services.QueryService;
import com.krishagni.catissueplus.core.de.services.SavedQueryErrorCode;

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
	
	private static String QUERY_DATA_EXPORTED_EMAIL_TMPL = "query_export_data";
	
	private static String SHARE_QUERY_FOLDER_EMAIL_TMPL = "query_share_query_folder";

	private static final String dateFormat = CommonServiceLocator.getInstance().getDatePattern();

	private static final String timeFormat = CommonServiceLocator.getInstance().getTimePattern();
	
	private static final int EXPORT_THREAD_POOL_SIZE = getThreadPoolSize();
	
	private static final int ONLINE_EXPORT_TIMEOUT_SECS = 30;
	
	private static final String EXPORT_DATA_DIR = getExportDataDir();
	
	private static ExecutorService exportThreadPool = Executors.newFixedThreadPool(EXPORT_THREAD_POOL_SIZE);
		
	private DaoFactory daoFactory;

	private UserDao userDao;
	
	private QueryFolderFactory queryFolderFactory;
	
	private EmailService emailService;
	
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

	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<SavedQueriesList> getSavedQueries(RequestEvent<ListSavedQueriesCriteria> req) {
		try {
			ListSavedQueriesCriteria crit = req.getPayload();
			
			if (crit.startAt() < 0 || crit.maxResults() <= 0) {
				return ResponseEvent.userError(SavedQueryErrorCode.INVALID_PAGINATION_FILTER);
			}

			Long userId = AuthUtil.getCurrentUser().getId();
			List<SavedQuerySummary> queries = daoFactory.getSavedQueryDao().getQueries(
							userId, 
							crit.startAt(),
							crit.maxResults(),
							crit.query());
			
			Long count = null;
			if (crit.countReq()) {
				count = daoFactory.getSavedQueryDao().getQueriesCount(userId, crit.query());
			}
			
			return ResponseEvent.response(SavedQueriesList.create(queries, count));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<SavedQueryDetail> getSavedQuery(RequestEvent<Long> req) {
		try {
			SavedQuery savedQuery = daoFactory.getSavedQueryDao().getQuery(req.getPayload());
			if (savedQuery == null) {
				return ResponseEvent.userError(SavedQueryErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(SavedQueryDetail.fromSavedQuery(savedQuery));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<SavedQueryDetail> saveQuery(RequestEvent<SavedQueryDetail> req) {
		try {
			SavedQueryDetail queryDetail = req.getPayload();
			queryDetail.setId(null);

			Query.createQuery().wideRows(true).ic(true)
					.dateFormat(dateFormat).timeFormat(timeFormat)
					.compile(cprForm, getAql(queryDetail));
			SavedQuery savedQuery = getSavedQuery(req.getSessionDataBean(), queryDetail);
			daoFactory.getSavedQueryDao().saveOrUpdate(savedQuery);
			return ResponseEvent.response(SavedQueryDetail.fromSavedQuery(savedQuery));
		} catch (QueryParserException qpe) {
			return ResponseEvent.userError(SavedQueryErrorCode.MALFORMED);
		} catch (IllegalArgumentException iae) {
			return ResponseEvent.userError(SavedQueryErrorCode.MALFORMED);		
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}	
	}

	@Override
	@PlusTransactional
	public ResponseEvent<SavedQueryDetail> updateQuery(RequestEvent<SavedQueryDetail> req) {
		try {
			SavedQueryDetail queryDetail = req.getPayload();

			Query.createQuery().wideRows(true).ic(true)
					.dateFormat(dateFormat).timeFormat(timeFormat)
					.compile(cprForm, getAql(queryDetail));
			SavedQuery savedQuery = getSavedQuery(req.getSessionDataBean(), queryDetail);
			SavedQuery existing = daoFactory.getSavedQueryDao().getQuery(queryDetail.getId());
			existing.update(savedQuery);

			daoFactory.getSavedQueryDao().saveOrUpdate(existing);	
			return ResponseEvent.response(SavedQueryDetail.fromSavedQuery(savedQuery));
		} catch (QueryParserException qpe) {
			return ResponseEvent.userError(SavedQueryErrorCode.MALFORMED);
		} catch (IllegalArgumentException iae) {
			return ResponseEvent.userError(SavedQueryErrorCode.MALFORMED);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Long> deleteQuery(RequestEvent<Long> req) {
		try {
			Long queryId = req.getPayload();
			SavedQuery query = daoFactory.getSavedQueryDao().getQuery(queryId);
			if (query == null) {
				return ResponseEvent.userError(SavedQueryErrorCode.NOT_FOUND);
			}

			boolean isAdmin = req.getSessionDataBean().isAdmin();
			Long userId = req.getSessionDataBean().getUserId();
			if (!isAdmin && !query.getCreatedBy().getId().equals(userId)) {
				return ResponseEvent.userError(SavedQueryErrorCode.OP_NOT_ALLOWED);
			}

			query.setDeletedOn(Calendar.getInstance().getTime());
			daoFactory.getSavedQueryDao().saveOrUpdate(query);
			return ResponseEvent.response(queryId);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<QueryExecResult> executeQuery(RequestEvent<ExecuteQueryEventOp> req) {
		QueryResultData queryResult = null;
		
		try {
			ExecuteQueryEventOp opDetail = req.getPayload();
			SessionDataBean sdb = req.getSessionDataBean();
			boolean countQuery = opDetail.getRunType().equals("Count");
			
			Query query = Query.createQuery()
					.wideRows(opDetail.isWideRows()).ic(true)
					.dateFormat(dateFormat).timeFormat(timeFormat);
			query.compile(
					cprForm, 
					getAqlWithCpIdInSelect(sdb, countQuery, opDetail.getAql()), 
					getRestriction(sdb, opDetail.getCpId()));
			
			QueryResponse resp = query.getData();
			insertAuditLog(req, resp);
			
			queryResult = resp.getResultData();
			queryResult.setScreener(new QueryResultScreenerImpl(sdb, countQuery));
			
			Integer[] indices = null;
			if (opDetail.getIndexOf() != null && !opDetail.getIndexOf().isEmpty()) {
				indices = queryResult.getColumnIndices(opDetail.getIndexOf());
			}
			
			return ResponseEvent.response(QueryExecResult.create(
					queryResult.getColumnLabels(), queryResult.getStringifiedRows(), 
					queryResult.getDbRowsCount(), indices));
		} catch (QueryParserException qpe) {
			return ResponseEvent.userError(SavedQueryErrorCode.MALFORMED);
		} catch (IllegalArgumentException iae) {
			return ResponseEvent.userError(SavedQueryErrorCode.MALFORMED);
		} catch (IllegalAccessError iae) {
			return ResponseEvent.userError(SavedQueryErrorCode.OP_NOT_ALLOWED);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
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
	public ResponseEvent<QueryDataExportResult> exportQueryData(RequestEvent<ExecuteQueryEventOp> req) {
		try {
			ExecuteQueryEventOp opDetail = req.getPayload();
			SessionDataBean sdb = req.getSessionDataBean();
			boolean countQuery = opDetail.getRunType().equals("Count");
			
			
			Query query = Query.createQuery();
			query.wideRows(opDetail.isWideRows())
				.ic(true)
				.dateFormat(dateFormat).timeFormat(timeFormat)
				.compile(
						cprForm, 
						getAqlWithCpIdInSelect(sdb, countQuery, opDetail.getAql()), 
						getRestriction(sdb, opDetail.getCpId()));
			
			String filename = UUID.randomUUID().toString();
			boolean completed = exportData(filename, query, req);
			return ResponseEvent.response(QueryDataExportResult.create(filename, completed));
		} catch (QueryParserException qpe) {
			return ResponseEvent.userError(SavedQueryErrorCode.MALFORMED);		
		} catch (IllegalArgumentException iae) {
			return ResponseEvent.userError(SavedQueryErrorCode.MALFORMED);
		} catch (IllegalAccessError iae) {
			return ResponseEvent.userError(SavedQueryErrorCode.OP_NOT_ALLOWED);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	public ResponseEvent<File> getExportDataFile(RequestEvent<String> req) {
		String fileId = req.getPayload();
		try {
			String path = EXPORT_DATA_DIR + File.separator + fileId;
			File f = new File(path);
			if (f.exists()) {
				return ResponseEvent.response(f);
			} else {
				return ResponseEvent.userError(SavedQueryErrorCode.EXPORT_DATA_FILE_NOT_FOUND);
			}
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
		
	@Override
	@PlusTransactional
	public ResponseEvent<List<QueryFolderSummary>> getUserFolders(RequestEvent<?> req) {
		try {
			Long userId = req.getSessionDataBean().getUserId();			
			List<QueryFolder> queryFolders = daoFactory.getQueryFolderDao().getUserFolders(userId);

			List<QueryFolderSummary> result = new ArrayList<QueryFolderSummary>();
			for (QueryFolder folder : queryFolders) {
				result.add(QueryFolderSummary.fromQueryFolder(folder));
			}
			
			return ResponseEvent.response(result);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<QueryFolderDetails> getFolder(RequestEvent<Long> req) {
		try {
			Long folderId = req.getPayload();
			QueryFolder folder = daoFactory.getQueryFolderDao().getQueryFolder(folderId);
			if (folder == null) {
				return ResponseEvent.userError(SavedQueryErrorCode.FOLDER_NOT_FOUND);
			}
			
			Long userId = req.getSessionDataBean().getUserId();
			if (!req.getSessionDataBean().isAdmin() && !folder.canUserAccess(userId)) {
				return ResponseEvent.userError(SavedQueryErrorCode.OP_NOT_ALLOWED);
			}
			
			return ResponseEvent.response(QueryFolderDetails.fromQueryFolder(folder));			
		} catch (Exception e) {
			return ResponseEvent.serverError(e);			
		}
	}	
	
	
	@Override
	@PlusTransactional
	public ResponseEvent<QueryFolderDetails> createFolder(RequestEvent<QueryFolderDetails> req) {
		try {
			QueryFolderDetails folderDetails = req.getPayload();
			
			UserSummary owner = new UserSummary();
			owner.setId(req.getSessionDataBean().getUserId());
			folderDetails.setOwner(owner);
			
			QueryFolder queryFolder = queryFolderFactory.createQueryFolder(folderDetails);	
			
			daoFactory.getQueryFolderDao().saveOrUpdate(queryFolder);
			
			if (!queryFolder.getSharedWith().isEmpty()) {
				sendFolderSharedEmail(queryFolder.getOwner(), queryFolder, queryFolder.getSharedWith());
			}			
			return ResponseEvent.response(QueryFolderDetails.fromQueryFolder(queryFolder));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<QueryFolderDetails> updateFolder(RequestEvent<QueryFolderDetails> req) {
		try {
			QueryFolderDetails folderDetails = req.getPayload();
			Long folderId = folderDetails.getId();
			if (folderId == null) {
				return ResponseEvent.userError(SavedQueryErrorCode.FOLDER_NOT_FOUND);
			}
						
			QueryFolder existing = daoFactory.getQueryFolderDao().getQueryFolder(folderId);
			if (existing == null) {
				return ResponseEvent.userError(SavedQueryErrorCode.FOLDER_NOT_FOUND);
			}
			
			Long userId = req.getSessionDataBean().getUserId();
			if (!req.getSessionDataBean().isAdmin() && !existing.getOwner().getId().equals(userId)) {
				return ResponseEvent.userError(SavedQueryErrorCode.OP_NOT_ALLOWED);
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
				User user = userDao.getById(userId);
				sendFolderSharedEmail(user, queryFolder, newUsers);
			}
			return ResponseEvent.response(QueryFolderDetails.fromQueryFolder(existing));			
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Long> deleteFolder(RequestEvent<Long> req) {
		try {
			Long folderId = req.getPayload();
			if (folderId == null) {
				return ResponseEvent.userError(SavedQueryErrorCode.FOLDER_NOT_FOUND);
			}

			QueryFolder existing = daoFactory.getQueryFolderDao().getQueryFolder(folderId);
			if (existing == null) {
				return ResponseEvent.userError(SavedQueryErrorCode.FOLDER_NOT_FOUND);								
			}
			
			Long userId = req.getSessionDataBean().getUserId();
			if (!req.getSessionDataBean().isAdmin() && !existing.getOwner().getId().equals(userId)) {
				return ResponseEvent.userError(SavedQueryErrorCode.OP_NOT_ALLOWED);
			}
			
			daoFactory.getQueryFolderDao().deleteFolder(existing);
			return ResponseEvent.response(folderId);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<SavedQueriesList> getFolderQueries(RequestEvent<ListFolderQueriesCriteria> req) {
		try {
			ListFolderQueriesCriteria crit = req.getPayload();
			Long folderId = crit.folderId();
			QueryFolder folder = daoFactory.getQueryFolderDao().getQueryFolder(folderId);			
			if (folder == null) {
				return ResponseEvent.userError(SavedQueryErrorCode.FOLDER_NOT_FOUND);
			}
			
			Long userId = req.getSessionDataBean().getUserId();
			if (!req.getSessionDataBean().isAdmin() && !folder.canUserAccess(userId)) {
				return ResponseEvent.userError(SavedQueryErrorCode.OP_NOT_ALLOWED);
			}
			
			List<SavedQuerySummary> queries = daoFactory.getSavedQueryDao().getQueriesByFolderId(
					folderId, 
					crit.startAt(),
					crit.maxResults(),
					crit.query());
			
			Long count = null;
			if (crit.countReq()) {
				count = daoFactory.getSavedQueryDao().getQueriesCountByFolderId(folderId, crit.query());
			}
			
			return ResponseEvent.response(SavedQueriesList.create(queries, count));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<SavedQuerySummary>> updateFolderQueries(RequestEvent<UpdateFolderQueriesOp> req) {
		try {
			UpdateFolderQueriesOp opDetail = req.getPayload();
			
			Long folderId = opDetail.getFolderId();
			QueryFolder queryFolder = daoFactory.getQueryFolderDao().getQueryFolder(folderId);			
			if (queryFolder == null) {
				return ResponseEvent.userError(SavedQueryErrorCode.FOLDER_NOT_FOUND);
			}
			
			Long userId = req.getSessionDataBean().getUserId();
			if (!req.getSessionDataBean().isAdmin() && !queryFolder.getOwner().getId().equals(userId)) {
				return ResponseEvent.userError(SavedQueryErrorCode.OP_NOT_ALLOWED);
			}
			
			List<SavedQuery> savedQueries = null;
			List<Long> queryIds = opDetail.getQueries();
			
			if (queryIds == null || queryIds.isEmpty()) {
				savedQueries = new ArrayList<SavedQuery>();
			} else {
				savedQueries = daoFactory.getSavedQueryDao().getQueriesByIds(queryIds);
			}
			
			switch (opDetail.getOp()) {
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
			for (SavedQuery query : queryFolder.getSavedQueries()) {
				result.add(SavedQuerySummary.fromSavedQuery(query));
			}
			
			return ResponseEvent.response(result);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<UserSummary>> shareFolder(RequestEvent<ShareQueryFolderOp> req) {
		try {
			ShareQueryFolderOp opDetail = req.getPayload();
			
			Long folderId = opDetail.getFolderId();
			QueryFolder queryFolder = daoFactory.getQueryFolderDao().getQueryFolder(folderId);
			if (queryFolder == null) {
				return ResponseEvent.userError(SavedQueryErrorCode.FOLDER_NOT_FOUND);
			}
			
			Long userId = req.getSessionDataBean().getUserId();
			if (!req.getSessionDataBean().isAdmin() && !queryFolder.getOwner().getId().equals(userId)) {
				return ResponseEvent.userError(SavedQueryErrorCode.OP_NOT_ALLOWED);
			}
			
			
			List<User> users = null;
			List<Long> userIds = opDetail.getUserIds();
			if (userIds == null || userIds.isEmpty()) {
				users = new ArrayList<User>();
			} else {
				users = userDao.getUsersByIds(userIds);
			}
			
			Collection<User> newUsers = null; 
			switch (opDetail.getOp()) {
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
				result.add(UserSummary.from(user));
			}
			
			if (newUsers != null && !newUsers.isEmpty()) {
				User user = userDao.getById(userId);
				sendFolderSharedEmail(user, queryFolder, newUsers);
			}
			
			return ResponseEvent.response(result);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
    @Override
    @PlusTransactional
    public ResponseEvent<QueryAuditLogsList> getAuditLogs(RequestEvent<ListQueryAuditLogsCriteria> req){
		try {
			SessionDataBean session = req.getSessionDataBean();			
			Long userId = session.getUserId();
			
			ListQueryAuditLogsCriteria crit = req.getPayload();
			Long savedQueryId = crit.savedQueryId();
			int startAt = crit.startAt() < 0 ? 0 : crit.startAt();
			int maxRecs = crit.maxResults() < 0 ? 0 : crit.maxResults();

			QueryAuditLogDao logDao = daoFactory.getQueryAuditLogDao();
			List<QueryAuditLogSummary> auditLogs = null;
			Long count = null;
			if (savedQueryId == null || savedQueryId == -1) {
				if (!session.isAdmin()) {
					return ResponseEvent.userError(SavedQueryErrorCode.OP_NOT_ALLOWED);
				}
				
				switch (crit.type()) {
					case ALL:
						auditLogs = logDao.getAuditLogs(startAt, maxRecs);
						if (crit.countReq()) {
							count = logDao.getAuditLogsCount();
						}
						break;
						
					case LAST_24:
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.DAY_OF_MONTH, -1);
						Date intervalSt = cal.getTime();						
						Date intervalEnd = Calendar.getInstance().getTime();
						auditLogs = logDao.getAuditLogs(intervalSt, intervalEnd, startAt, maxRecs);
						if (crit.countReq()) {
							count = logDao.getAuditLogsCount(intervalSt, intervalEnd);
						}						
						break;
				}
								
			} else {
				auditLogs = logDao.getAuditLogs(savedQueryId, userId, startAt, maxRecs);
			}
			
			return ResponseEvent.response(QueryAuditLogsList.create(auditLogs, count));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
    }
    
    @Override
    @PlusTransactional
    public ResponseEvent<QueryAuditLogDetail> getAuditLog(RequestEvent<Long> req) {
		try {
			Long auditLogId = req.getPayload();
			QueryAuditLog queryAuditLog = daoFactory.getQueryAuditLogDao().getAuditLog(auditLogId);
			return ResponseEvent.response(QueryAuditLogDetail.from(queryAuditLog));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
    }
    
	@Override
	@PlusTransactional
	public ResponseEvent<String> getQueryDef(RequestEvent<Long> req) {
		try {
			SavedQueryDao queryDao = daoFactory.getSavedQueryDao();
			
			Long queryId = req.getPayload();			
			SavedQuery query = queryDao.getQuery(queryId);			
			if (query == null) {
				return ResponseEvent.userError(SavedQueryErrorCode.NOT_FOUND);
			}
			
			Long userId = req.getSessionDataBean().getUserId();
			if (!query.getCreatedBy().getId().equals(userId) && 
					!queryDao.isQuerySharedWithUser(queryId, userId)) {
				return ResponseEvent.userError(SavedQueryErrorCode.OP_NOT_ALLOWED);
			}
			
			String queryDef = query.getQueryDefJson(true);
			return ResponseEvent.response(queryDef);			
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
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
	
	private boolean exportData(final String filename, final Query query, final RequestEvent<ExecuteQueryEventOp> req) 
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
					User user = userDao.getById(req.getSessionDataBean().getUserId());
					
					SavedQuery savedQuery = null;
					Long queryId = req.getPayload().getSavedQueryId();
					if (queryId != null) {
						savedQuery = daoFactory.getSavedQueryDao().getQuery(queryId);
					}					
					sendQueryDataExportedEmail(user, savedQuery, filename);
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
			List<Long> cpIds = Collections.emptyList(); //privilegeSvc.getCpList(sdb.getUserId(), PrivilegeType.READ.value());
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

	private void insertAuditLog(RequestEvent<ExecuteQueryEventOp> req, QueryResponse resp) {
		User user = new User();
		user.setId(req.getSessionDataBean().getUserId());

		QueryAuditLog auditLog = new QueryAuditLog();
		auditLog.setQueryId(req.getPayload().getSavedQueryId());
		auditLog.setRunBy(user);
		auditLog.setTimeOfExecution(resp.getTimeOfExecution());
		auditLog.setTimeToFinish(resp.getExecutionTime());
		auditLog.setRunType(req.getPayload().getRunType());
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
				phiAccess = true; //privilegeSvc.hasPrivilege(sdb.getUserId(), cpId, PrivilegeType.PHI_ACCESS.value());
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
	
	private void sendQueryDataExportedEmail(User user, SavedQuery query, String filename) {
		String title = query != null ? query.getTitle() : "Unsaved query";
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("user", user);
		props.put("query", query);
		props.put("filename", filename);
		props.put("appUrl", Utility.getAppUrl());
		props.put("$subject", new String[] {title});
		
		emailService.sendEmail(QUERY_DATA_EXPORTED_EMAIL_TMPL, new String[] {user.getEmailAddress()}, props);
	}
	
	private void sendFolderSharedEmail(User user, QueryFolder folder, Collection<User> sharedUsers) {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("user", user);
		props.put("folder", folder);
		props.put("appUrl", Utility.getAppUrl());
		
		for (User sharedWith : sharedUsers) {
			props.put("sharedWith", sharedWith);
			emailService.sendEmail(SHARE_QUERY_FOLDER_EMAIL_TMPL, new String[] {sharedWith.getEmailAddress()}, props);
		}		
	}
}