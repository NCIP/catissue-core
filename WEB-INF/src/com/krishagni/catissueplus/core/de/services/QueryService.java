package com.krishagni.catissueplus.core.de.services;

import java.io.File;
import java.io.OutputStream;
import java.util.List;

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.de.events.*;

public interface QueryService {	
	public ResponseEvent<SavedQueriesList> getSavedQueries(RequestEvent<ListSavedQueriesCriteria> req);
	
	public ResponseEvent<SavedQueryDetail> getSavedQuery(RequestEvent<Long> req);
	
	public ResponseEvent<SavedQueryDetail> saveQuery(RequestEvent<SavedQueryDetail> req);
			
	public ResponseEvent<SavedQueryDetail> updateQuery(RequestEvent<SavedQueryDetail> req);

	public ResponseEvent<Long> deleteQuery(RequestEvent<Long> req);
	
	//
	// query execution APIs
	//

	public ResponseEvent<QueryExecResult> executeQuery(RequestEvent<ExecuteQueryEventOp> req);
	
	public ResponseEvent<QueryDataExportResult> exportQueryData(RequestEvent<ExecuteQueryEventOp> req);
	
	public ResponseEvent<File> getExportDataFile(RequestEvent<String> req);
	
	public ResponseEvent<List<FacetDetail>> getFacetValues(RequestEvent<GetFacetValuesOp> req);

	//
	// folder related APIs
	//
	
	public ResponseEvent<List<QueryFolderSummary>> getUserFolders(RequestEvent<?> req);
	
	public ResponseEvent<QueryFolderDetails> getFolder(RequestEvent<Long> req);
	
	public ResponseEvent<QueryFolderDetails> createFolder(RequestEvent<QueryFolderDetails> req);
	
	public ResponseEvent<QueryFolderDetails> updateFolder(RequestEvent<QueryFolderDetails> req);
	
	public ResponseEvent<Long> deleteFolder(RequestEvent<Long> req);
	
	public ResponseEvent<SavedQueriesList> getFolderQueries(RequestEvent<ListFolderQueriesCriteria> req);
	
	public ResponseEvent<List<SavedQuerySummary>> updateFolderQueries(RequestEvent<UpdateFolderQueriesOp> req);
	
	public ResponseEvent<List<UserSummary>> shareFolder(RequestEvent<ShareQueryFolderOp> req);
	
	//
	// query audit logs related APIs
	//	
	public ResponseEvent<QueryAuditLogsList> getAuditLogs(RequestEvent<ListQueryAuditLogsCriteria> req);
	
	public ResponseEvent<QueryAuditLogDetail> getAuditLog(RequestEvent<Long> req);
	
	//
	// query export APIs
	//
	public ResponseEvent<String> getQueryDef(RequestEvent<Long> req);
	
	//
	// internal use
	// 
	public interface ExportProcessor {
		public void headers(OutputStream out);
	}	

	public QueryDataExportResult exportQueryData(ExecuteQueryEventOp opDetail, ExportProcessor processor);
	
	//
	// internal use
	// 
	public String insertCustomQueryForms(String dirName) ;
	
}
