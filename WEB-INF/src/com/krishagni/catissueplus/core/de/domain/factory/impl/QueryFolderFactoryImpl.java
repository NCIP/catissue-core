package com.krishagni.catissueplus.core.de.domain.factory.impl;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
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
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		QueryFolder queryFolder = new QueryFolder();
		
		Long userId = details.getOwner() != null ? details.getOwner().getId() : null;
		setOwner(queryFolder, userId , ose);
		setName(queryFolder, details.getName(), ose);
		
		List<Long> queryIds = new ArrayList<Long>();
		for (SavedQuerySummary query : details.getQueries()) {
			queryIds.add(query.getId());
		}
		setQueries(queryFolder, queryIds, ose);
		
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
		setSharedUsers(queryFolder, sharedUsers, ose);

		ose.checkAndThrow();
		return queryFolder;
	}
	
	private void setOwner(QueryFolder folder, Long userId, OpenSpecimenException ose) {
		if (userId == null) {
			ose.addError(UserErrorCode.NOT_FOUND);			
		} else {
			User user = userDao.getById(userId);
			if (user == null) {
				ose.addError(UserErrorCode.NOT_FOUND);
			} else {
				folder.setOwner(user);
			}						
		}		
	}
	
	private void setName(QueryFolder folder, String name, OpenSpecimenException ose) {
		if (StringUtils.isBlank(name)) {
			ose.addError(SavedQueryErrorCode.FOLDER_NAME_REQUIRED);
		} else {
			folder.setName(name);
		}		
	}
	
	private void setQueries(QueryFolder folder, List<Long> queryIds, OpenSpecimenException ose) {
		if (queryIds != null && !queryIds.isEmpty()) {
			List<SavedQuery> queries = daoFactory.getSavedQueryDao().getQueriesByIds(queryIds);
			if (queries.size() != queryIds.size()) {
				ose.addError(SavedQueryErrorCode.QUERIES_NOT_ACCESSIBLE);
			} else {
				folder.setSavedQueries(new HashSet<SavedQuery>(queries));
			}			
		} else {
			folder.getSavedQueries().clear();
		}
	}
	
	private void setSharedUsers(QueryFolder folder, List<Long> userIds, OpenSpecimenException ose) {
		if (userIds != null && !userIds.isEmpty()) {
			List<User> sharedUsers = userDao.getUsersByIds(userIds);
			if (sharedUsers.size() != userIds.size()) {
				ose.addError(SavedQueryErrorCode.INVALID_SHARE_ACCESS_DETAILS);
			} else {
				folder.setSharedWith(new HashSet<User>(sharedUsers));
			}
		} else {
			folder.getSharedWith().clear();
		}
	}
}
