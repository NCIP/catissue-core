package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenList;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenListErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenListFactory;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListDetails;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.UserSummary;

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
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		SpecimenList specimenList = new SpecimenList();
		
		Long userId = details.getOwner() != null ? details.getOwner().getId() : null;
		setOwner(specimenList, userId , ose);
		setName(specimenList, details.getName(), ose);
		
		List<String> labels = new ArrayList<String>();
		if (!CollectionUtils.isEmpty(details.getSpecimens())) {
			for (SpecimenDetail specimen : details.getSpecimens()) {
				labels.add(specimen.getLabel());
			}			
		}
		setSpecimens(specimenList, labels, ose);
		
		List<Long> sharedUsers = new ArrayList<Long>();		
		if (!CollectionUtils.isEmpty(details.getSharedWith())) {
			for (UserSummary user : details.getSharedWith()) {
				if (user.getId().equals(userId)) {
					continue;
				}
				sharedUsers.add(user.getId());
			}			
		}
		setSharedUsers(specimenList, sharedUsers, ose);

		ose.checkAndThrow();
		return specimenList;
	}
	
	private void setOwner(SpecimenList specimenList, Long userId, OpenSpecimenException ose) {
		if (userId == null) {
			ose.addError(SpecimenListErrorCode.OWNER_REQUIRED);			
		} else {
			User user = daoFactory.getUserDao().getUser(userId);
			if (user == null) {
				ose.addError(SpecimenListErrorCode.OWNER_NOT_FOUND);
			} else {
				specimenList.setOwner(user);
			}						
		}
	}
	
	private void setName(SpecimenList list, String name, OpenSpecimenException ose) {
		if (StringUtils.isBlank(name)) {
			ose.addError(SpecimenListErrorCode.NAME_REQUIRED);
		} else {
			list.setName(name);
		}		
	}
	
	private void setSpecimens(SpecimenList list, List<String> labels, OpenSpecimenException ose) {
		if (labels != null && !labels.isEmpty()) {
			List<Specimen> specimens = daoFactory.getSpecimenDao().getSpecimensByLabel(labels);
			if (specimens.size() != labels.size()) {
				ose.addError(SpecimenListErrorCode.INVALID_LABELS);
			} else {
				list.setSpecimens(new HashSet<Specimen>(specimens));
			}			
		} else {
			list.getSpecimens().clear();
		}
	}
	
	private void setSharedUsers(SpecimenList specimenList, List<Long> userIds, OpenSpecimenException ose) {
		if (userIds != null && !userIds.isEmpty()) {
			List<User> sharedUsers = daoFactory.getUserDao().getUsersById(userIds);
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
