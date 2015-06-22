package com.krishagni.catissueplus.core.de.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.de.domain.QueryFolder;
import com.krishagni.catissueplus.core.de.repository.QueryFolderDao;

public class QueryFolderDaoImpl extends AbstractDao<QueryFolder> implements QueryFolderDao {

	private static final String FQN = QueryFolder.class.getName();
	
	private static final String GET_QUERY_FOLDERS_BY_USER = FQN + ".getQueryFoldersByUser";
	
	private static final String GET_FOLDER_BY_NAME = FQN + ".getQueryFolderByName";

	@SuppressWarnings("unchecked")
	@Override
	public List<QueryFolder> getUserFolders(Long userId) { 
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_QUERY_FOLDERS_BY_USER);
		query.setLong("userId", userId);
		return query.list();
	}

	@Override
	public QueryFolder getQueryFolder(Long folderId) {
		return (QueryFolder) sessionFactory.getCurrentSession().get(QueryFolder.class, folderId);
	}
	
	@Override
	public void deleteFolder(QueryFolder folder) {
		sessionFactory.getCurrentSession().delete(folder);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public QueryFolder getByName(String name) { 
		List<QueryFolder> folders = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_FOLDER_BY_NAME)
				.setString("name", name)
				.list();
		return folders.isEmpty() ? null : folders.iterator().next();
	}
}