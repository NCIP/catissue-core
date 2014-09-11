package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenList;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenListErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenListFactory;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListDetails;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenSummary;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
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
		ObjectCreationException oce = new ObjectCreationException();
		SpecimenList specimenList = new SpecimenList();
		
		Long userId = details.getOwner() != null ? details.getOwner().getId() : null;
		setOwner(specimenList, userId , oce);
		setName(specimenList, details.getName(), oce);
		
		List<String> labels = new ArrayList<String>();
		if (!CollectionUtils.isEmpty(details.getSpecimens())) {
			for (SpecimenSummary specimen : details.getSpecimens()) {
				labels.add(specimen.getLabel());
			}			
		}
		setSpecimens(specimenList, labels, oce);
		
		List<Long> sharedUsers = new ArrayList<Long>();		
		if (!CollectionUtils.isEmpty(details.getSharedWith())) {
			for (UserSummary user : details.getSharedWith()) {
				if (user.getId().equals(userId)) {
					continue;
				}
				sharedUsers.add(user.getId());
			}			
		}
		setSharedUsers(specimenList, sharedUsers, oce);
		
		if (oce.hasErrors()) {
			throw oce;
		}
				
		return specimenList;
	}
	
	private void setOwner(SpecimenList specimenList, Long userId, ObjectCreationException oce) {
		if (userId == null) {
			oce.addError(SpecimenListErrorCode.INVALID_USER_ID, "user-id");			
		} else {
			User user = daoFactory.getUserDao().getUser(userId);
			if (user == null) {
				oce.addError(SpecimenListErrorCode.INVALID_USER_ID, "user-id");
			} else {
				specimenList.setOwner(user);
			}						
		}
	}
	
	private void setName(SpecimenList list, String name, ObjectCreationException oce) {
		if (StringUtils.isBlank(name)) {
			oce.addError(SpecimenListErrorCode.INVALID_LIST_NAME, "specimen-list-name");
		} else {
			list.setName(name);
		}		
	}
	
	private void setSpecimens(SpecimenList list, List<String> labels, ObjectCreationException oce) {
		if (labels != null && !labels.isEmpty()) {
			List<Specimen> specimens = daoFactory.getSpecimenDao().getSpecimensByLabel(labels);
			if (specimens.size() != labels.size()) {
				oce.addError(SpecimenListErrorCode.INVALID_SPECIMEN_LABELS, "specimen-labels");
			} else {
				list.setSpecimens(new HashSet<Specimen>(specimens));
			}			
		} else {
			list.getSpecimens().clear();
		}
	}
	
	private void setSharedUsers(SpecimenList specimenList, List<Long> userIds, ObjectCreationException oce) {
		if (userIds != null && !userIds.isEmpty()) {
			List<User> sharedUsers = daoFactory.getUserDao().getUsersById(userIds);
			if (sharedUsers.size() != userIds.size()) {
				oce.addError(SpecimenListErrorCode.INVALID_USER_ID, "user-ids");
			} else {
				specimenList.setSharedWith(new HashSet<User>(sharedUsers));
			}
		} else {
			specimenList.getSharedWith().clear();
		}
	}
}
