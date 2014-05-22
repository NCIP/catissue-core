package com.krishagni.catissueplus.core.de.services;

import com.krishagni.catissueplus.core.de.events.CreateQueryFolderEvent;
import com.krishagni.catissueplus.core.de.events.DeleteQueryFolderEvent;
import com.krishagni.catissueplus.core.de.events.ExecuteQueryEvent;
import com.krishagni.catissueplus.core.de.events.ExportDataFileEvent;
import com.krishagni.catissueplus.core.de.events.ExportQueryDataEvent;
import com.krishagni.catissueplus.core.de.events.FolderQueriesEvent;
import com.krishagni.catissueplus.core.de.events.FolderQueriesUpdatedEvent;
import com.krishagni.catissueplus.core.de.events.QueryAuditLogsEvent;
import com.krishagni.catissueplus.core.de.events.QueryDataExportedEvent;
import com.krishagni.catissueplus.core.de.events.QueryFolderDetailEvent;
import com.krishagni.catissueplus.core.de.events.QueryFoldersEvent;
import com.krishagni.catissueplus.core.de.events.QueryExecutedEvent;
import com.krishagni.catissueplus.core.de.events.QueryFolderCreatedEvent;
import com.krishagni.catissueplus.core.de.events.QueryFolderDeletedEvent;
import com.krishagni.catissueplus.core.de.events.QueryFolderSharedEvent;
import com.krishagni.catissueplus.core.de.events.QueryFolderUpdatedEvent;
import com.krishagni.catissueplus.core.de.events.QuerySavedEvent;
import com.krishagni.catissueplus.core.de.events.QueryUpdatedEvent;
import com.krishagni.catissueplus.core.de.events.ReqExportDataFileEvent;
import com.krishagni.catissueplus.core.de.events.ReqFolderQueriesEvent;
import com.krishagni.catissueplus.core.de.events.ReqQueryFolderDetailEvent;
import com.krishagni.catissueplus.core.de.events.ReqQueryFoldersEvent;
import com.krishagni.catissueplus.core.de.events.ReqSavedQueriesSummaryEvent;
import com.krishagni.catissueplus.core.de.events.ReqQueryAuditLogEvent;
import com.krishagni.catissueplus.core.de.events.ReqQueryAuditLogsEvent;
import com.krishagni.catissueplus.core.de.events.ReqSavedQueryDetailEvent;
import com.krishagni.catissueplus.core.de.events.SaveQueryEvent;
import com.krishagni.catissueplus.core.de.events.SavedQueriesSummaryEvent;
import com.krishagni.catissueplus.core.de.events.QueryAuditLogEvent;
import com.krishagni.catissueplus.core.de.events.SavedQueryDetailEvent;
import com.krishagni.catissueplus.core.de.events.ShareQueryFolderEvent;
import com.krishagni.catissueplus.core.de.events.UpdateFolderQueriesEvent;
import com.krishagni.catissueplus.core.de.events.UpdateQueryEvent;
import com.krishagni.catissueplus.core.de.events.UpdateQueryFolderEvent;

public interface QueryService {	
	public SavedQueriesSummaryEvent getSavedQueries(ReqSavedQueriesSummaryEvent req);
	
	public SavedQueryDetailEvent getSavedQuery(ReqSavedQueryDetailEvent req);
	
	public QuerySavedEvent saveQuery(SaveQueryEvent req);
			
	public QueryUpdatedEvent updateQuery(UpdateQueryEvent req);
	
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
}
