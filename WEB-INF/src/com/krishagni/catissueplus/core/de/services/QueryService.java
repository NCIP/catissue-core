package com.krishagni.catissueplus.core.de.services;

import java.io.File;
import java.io.OutputStream;
import java.util.List;

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.de.events.*;

public interface QueryService {	
	ResponseEvent<SavedQueriesList> getSavedQueries(RequestEvent<ListSavedQueriesCriteria> req);

	ResponseEvent<SavedQueryDetail> getSavedQuery(RequestEvent<Long> req);

	ResponseEvent<SavedQueryDetail> saveQuery(RequestEvent<SavedQueryDetail> req);

	ResponseEvent<SavedQueryDetail> updateQuery(RequestEvent<SavedQueryDetail> req);

	ResponseEvent<Long> deleteQuery(RequestEvent<Long> req);

	//
	// query execution APIs
	//

	ResponseEvent<QueryExecResult> executeQuery(RequestEvent<ExecuteQueryEventOp> req);

	ResponseEvent<QueryExecResult> executeSavedQuery(RequestEvent<ExecuteSavedQueryOp> req);

	ResponseEvent<QueryDataExportResult> exportQueryData(RequestEvent<ExecuteQueryEventOp> req);

	ResponseEvent<File> getExportDataFile(RequestEvent<String> req);

	ResponseEvent<List<FacetDetail>> getFacetValues(RequestEvent<GetFacetValuesOp> req);

	//
	// folder related APIs
	//

	ResponseEvent<List<QueryFolderSummary>> getUserFolders(RequestEvent<?> req);

	ResponseEvent<QueryFolderDetails> getFolder(RequestEvent<Long> req);

	ResponseEvent<QueryFolderDetails> createFolder(RequestEvent<QueryFolderDetails> req);

	ResponseEvent<QueryFolderDetails> updateFolder(RequestEvent<QueryFolderDetails> req);

	ResponseEvent<Long> deleteFolder(RequestEvent<Long> req);

	ResponseEvent<SavedQueriesList> getFolderQueries(RequestEvent<ListFolderQueriesCriteria> req);

	ResponseEvent<List<SavedQuerySummary>> updateFolderQueries(RequestEvent<UpdateFolderQueriesOp> req);

	ResponseEvent<List<UserSummary>> shareFolder(RequestEvent<ShareQueryFolderOp> req);

	//
	// query audit logs related APIs
	//	
	ResponseEvent<QueryAuditLogsList> getAuditLogs(RequestEvent<ListQueryAuditLogsCriteria> req);

	ResponseEvent<QueryAuditLogDetail> getAuditLog(RequestEvent<Long> req);

	//
	// query export APIs
	//
	ResponseEvent<String> getQueryDef(RequestEvent<Long> req);

	//
	// internal use
	// 
	interface ExportProcessor {
		String filename();

		void headers(OutputStream out);
	}	

	QueryDataExportResult exportQueryData(ExecuteQueryEventOp opDetail, ExportProcessor processor);

	//
	// internal use
	// 
	String insertCustomQueryForms(String dirName) ;
}
