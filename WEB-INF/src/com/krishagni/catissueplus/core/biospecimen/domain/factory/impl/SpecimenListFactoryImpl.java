package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenList;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenListErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenListFactory;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListDetails;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenListCriteria;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.util.MessageUtil;

public class SpecimenListFactoryImpl implements SpecimenListFactory {
	private DaoFactory daoFactory;
	
	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public SpecimenList createSpecimenList(SpecimenListDetails details) {
		SpecimenList specimenList = new SpecimenList();
		
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		setSpecimenListAttributes(details, specimenList, false, ose);

		ose.checkAndThrow();
		return specimenList;
	}
	
	@Override
	public SpecimenList createSpecimenList(SpecimenList existing, SpecimenListDetails details) {
		SpecimenList specimenList = new SpecimenList();
		BeanUtils.copyProperties(existing, specimenList);
		
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		setSpecimenListAttributes(details, specimenList, true, ose);
		
		ose.checkAndThrow();
		return specimenList;
	}

	public SpecimenList createDefaultSpecimenList(User user) {
		SpecimenList specimenList = new SpecimenList();
		specimenList.setOwner(user);
		specimenList.setName(SpecimenList.getDefaultListName(user));
		specimenList.setDescription(MessageUtil.getInstance().getMessage("specimen_list_default_user_list"));
		specimenList.setCreatedOn(Calendar.getInstance().getTime());
		specimenList.setLastUpdatedOn(specimenList.getCreatedOn());
		return specimenList;
	}

	private void setSpecimenListAttributes(SpecimenListDetails details, SpecimenList specimenList, boolean partial, OpenSpecimenException ose) {
		if (specimenList.getId() == null && details.getId() != null) {
			specimenList.setId(details.getId()); 
		}
		
		setOwner(details, specimenList, partial , ose);
		setName(details, specimenList, partial , ose);
		setDescription(details, specimenList, partial, ose);
		setSpecimens(details, specimenList, partial, ose);
		setSharedUsers(details, specimenList, partial, ose);

		specimenList.setCreatedOn(Calendar.getInstance().getTime());
		specimenList.setLastUpdatedOn(specimenList.getCreatedOn());
	}
    
	private void setOwner(SpecimenListDetails details, SpecimenList specimenList, boolean partial, OpenSpecimenException ose) {
		if (partial && !details.isAttrModified("owner")) {
			return;
		}
		
		Long userId = details.getOwner() != null ? details.getOwner().getId() : null;
		
		if (userId == null) {
			ose.addError(SpecimenListErrorCode.OWNER_REQUIRED);			
		} else {
			User user = daoFactory.getUserDao().getById(userId);
			if (user == null) {
				ose.addError(SpecimenListErrorCode.OWNER_NOT_FOUND);
			} else {
				specimenList.setOwner(user);
			}
		}
	}

	private void setName(SpecimenListDetails details, SpecimenList specimenList, boolean partial, OpenSpecimenException ose) {
		if (partial && !details.isAttrModified("name")) {
			return;
		}
		
		String name = details.getName();
		if (StringUtils.isBlank(name)) {
			ose.addError(SpecimenListErrorCode.NAME_REQUIRED);
		} else {
			specimenList.setName(name);
		}		
	}

	private void setDescription(SpecimenListDetails details, SpecimenList specimenList, boolean partial, OpenSpecimenException ose) {
		if (partial && !details.isAttrModified("description")) {
			return;
		}

		specimenList.setDescription(details.getDescription());
	}

	private void setSpecimens(SpecimenListDetails details, SpecimenList specimenList, boolean partial, OpenSpecimenException ose) {
		if (partial && !details.isAttrModified("specimens")) {
			return;
		}
		
		List<String> labels = new ArrayList<String>();
		if (!CollectionUtils.isEmpty(details.getSpecimens())) {
			for (SpecimenInfo specimen : details.getSpecimens()) {
				labels.add(specimen.getLabel());
			}
		}
		
		if (labels != null && !labels.isEmpty()) {
			SpecimenListCriteria crit = new SpecimenListCriteria().labels(labels);			
			List<Specimen> specimens = daoFactory.getSpecimenDao().getSpecimens(crit);
			if (specimens.size() != labels.size()) {
				ose.addError(SpecimenListErrorCode.INVALID_LABELS);
			} else {
				specimenList.setSpecimens(new HashSet<Specimen>(specimens));
			}			
		} else {
			specimenList.getSpecimens().clear();
		}
	}
	
	private void setSharedUsers(SpecimenListDetails details, SpecimenList specimenList, boolean partial, OpenSpecimenException ose) {
		if (partial && !details.isAttrModified("sharedWith")) {
			return;
		}
		
		Long userId = details.getOwner() != null ? details.getOwner().getId() : null;
		
		List<Long> userIds = new ArrayList<Long>();
		if (!CollectionUtils.isEmpty(details.getSharedWith())) {
			for (UserSummary user : details.getSharedWith()) {
				if (user.getId().equals(userId)) {
					continue;
				}
				userIds.add(user.getId());
			}
		}
		
		if (userIds != null && !userIds.isEmpty()) {
			List<User> sharedUsers = daoFactory.getUserDao().getUsersByIds(userIds);
			if (sharedUsers.size() != userIds.size()) {
				ose.addError(SpecimenListErrorCode.INVALID_USERS_LIST);
			} else {
				specimenList.setSharedWith(new HashSet<User>(sharedUsers));
			}
		} else {
			specimenList.getSharedWith().clear();
		}
	}
}
