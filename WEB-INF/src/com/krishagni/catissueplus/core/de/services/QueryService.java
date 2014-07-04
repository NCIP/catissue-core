package com.krishagni.catissueplus.core.de.services;

import com.krishagni.catissueplus.core.de.events.*;

public interface QueryService {	
	public SavedQueriesSummaryEvent getSavedQueries(ReqSavedQueriesSummaryEvent req);
	
	public SavedQueryDetailEvent getSavedQuery(ReqSavedQueryDetailEvent req);
	
	public QuerySavedEvent saveQuery(SaveQueryEvent req);
			
	public QueryUpdatedEvent updateQuery(UpdateQueryEvent req);

	public QueryDeletedEvent deleteQuery(DeleteQueryEvent req);
	
	public QueryExecutedEvent executeQuery(ExecuteQueryEvent req);
	
	public QueryDataExportedEvent exportQueryData(ExportQueryDataEvent req);
	
	public ExportDataFileEvent getExportDataFile(ReqExportDataFileEvent req);
	
	//
	// folder related APIs
	//
	
	public QueryFoldersEvent getUserFolders(ReqQueryFoldersEvent req);
	
	public QueryFolderDetailEvent getFolder(ReqQueryFolderDetailEvent req);
	
	public QueryFolderCreatedEvent createFolder(CreateQueryFolderEvent req);
	
	public QueryFolderUpdatedEvent updateFolder(UpdateQueryFolderEvent req);
	
	public QueryFolderDeletedEvent deleteFolder(DeleteQueryFolderEvent req);
	
	public FolderQueriesEvent getFolderQueries(ReqFolderQueriesEvent req);
	
	public FolderQueriesUpdatedEvent updateFolderQueries(UpdateFolderQueriesEvent req);
	
	public QueryFolderSharedEvent shareFolder(ShareQueryFolderEvent req);
	
	//
	// query audit logs related APIs
	//	
	public QueryAuditLogsEvent getAuditLogs(ReqQueryAuditLogsEvent req);
	
	public QueryAuditLogEvent getAuditLog(ReqQueryAuditLogEvent req);
	
	//
	// query export APIs
	//
	public QueryDefEvent getQueryDef(ReqQueryDefEvent req);
}
