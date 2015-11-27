package com.krishagni.catissueplus.core.de.repository;

public interface DaoFactory {
	public FormDao getFormDao();
	
	public SavedQueryDao getSavedQueryDao();
	
	public QueryFolderDao getQueryFolderDao();
	
	public QueryAuditLogDao getQueryAuditLogDao();

	public CpCatalogSettingDao getCpCatalogSettingDao();
}
