package com.krishagni.catissueplus.core.de.domain.factory.impl;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.de.domain.QueryFolder;
import com.krishagni.catissueplus.core.de.domain.SavedQuery;
import com.krishagni.catissueplus.core.de.domain.factory.QueryFolderFactory;
import com.krishagni.catissueplus.core.de.events.QueryFolderDetails;
import com.krishagni.catissueplus.core.de.events.SavedQuerySummary;
import com.krishagni.catissueplus.core.de.repository.DaoFactory;
import com.krishagni.catissueplus.core.de.services.SavedQueryErrorCode;

public class QueryFolderFactoryImpl implements QueryFolderFactory {
	private DaoFactory daoFactory;
	
	private UserDao userDao;

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

	@Override
	public QueryFolder createQueryFolder(QueryFolderDetails details) {
		ObjectCreationException oce = new ObjectCreationException();
		QueryFolder queryFolder = new QueryFolder();
		
		Long userId = details.getOwner() != null ? details.getOwner().getId() : null;
		setOwner(queryFolder, userId , oce);
		setName(queryFolder, details.getName(), oce);
		
		List<Long> queryIds = new ArrayList<Long>();
		for (SavedQuerySummary query : details.getQueries()) {
			queryIds.add(query.getId());
		}
		setQueries(queryFolder, queryIds, oce);
		
		queryFolder.setSharedWithAll(details.isSharedWithAll());		
		List<Long> sharedUsers = new ArrayList<Long>();		
		if (!details.isSharedWithAll()) {
			for (UserSummary user : details.getSharedWith()) {
				if (user.getId().equals(userId)) {
					continue;
				}
				sharedUsers.add(user.getId());
			}			
		}
		setSharedUsers(queryFolder, sharedUsers, oce);
		
		if (oce.hasErrors()) {
			throw oce;
		}
				
		return queryFolder;
	}
	
	private void setOwner(QueryFolder folder, Long userId, ObjectCreationException oce) {
		if (userId == null) {
			oce.addError(SavedQueryErrorCode.INVALID_USER_ID, "user-id");			
		} else {
			User user = userDao.getUser(userId);
			if (user == null) {
				oce.addError(SavedQueryErrorCode.INVALID_USER_ID, "user-id");
			} else {
				folder.setOwner(user);
			}						
		}		
	}
	
	private void setName(QueryFolder folder, String name, ObjectCreationException oce) {
		if (StringUtils.isBlank(name)) {
			oce.addError(SavedQueryErrorCode.INVALID_FOLDER_NAME, "query-folder-name");
		} else {
			folder.setName(name);
		}		
	}
	
	private void setQueries(QueryFolder folder, List<Long> queryIds, ObjectCreationException oce) {
		if (queryIds != null && !queryIds.isEmpty()) {
			List<SavedQuery> queries = daoFactory.getSavedQueryDao().getQueriesByIds(queryIds);
			if (queries.size() != queryIds.size()) {
				oce.addError(SavedQueryErrorCode.QUERIES_NOT_ACCESSIBLE, "query-ids");
			} else {
				folder.setSavedQueries(new HashSet<SavedQuery>(queries));
			}			
		} else {
			folder.getSavedQueries().clear();
		}
	}
	
	private void setSharedUsers(QueryFolder folder, List<Long> userIds, ObjectCreationException oce) {
		if (userIds != null && !userIds.isEmpty()) {
			List<User> sharedUsers = userDao.getUsersById(userIds);
			if (sharedUsers.size() != userIds.size()) {
				oce.addError(SavedQueryErrorCode.INVALID_SHARE_ACCESS_DETAILS, "user-ids");
			} else {
				folder.setSharedWith(new HashSet<User>(sharedUsers));
			}
		} else {
			folder.getSharedWith().clear();
		}
	}
}
