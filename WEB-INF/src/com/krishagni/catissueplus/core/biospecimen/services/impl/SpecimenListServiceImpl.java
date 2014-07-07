package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenList;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenListErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenListFactory;
import com.krishagni.catissueplus.core.biospecimen.events.CreateSpecimenListEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ListSpecimensEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ListSpecimensUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqListSpecimensEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenListDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ShareSpecimenListEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListDetails;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListSharedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListSummary;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenSummary;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateListSpecimensEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateSpecimenListEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenListService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.de.domain.QueryFolder;
import com.krishagni.catissueplus.core.de.events.QueryFolderSharedEvent;

public class SpecimenListServiceImpl implements SpecimenListService {
	
	private SpecimenListFactory specimenListFactory;
	
	private DaoFactory daoFactory;

	public SpecimenListFactory getSpecimenListFactory() {
		return specimenListFactory;
	}

	public void setSpecimenListFactory(SpecimenListFactory specimenListFactory) {
		this.specimenListFactory = specimenListFactory;
	}

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public SpecimenListsEvent getUserSpecimenLists(RequestEvent req) {
		try {
			Long userId = req.getSessionDataBean().getUserId();			
			List<SpecimenList> specimenLists = daoFactory.getSpecimenListDao().getUserSpecimenLists(userId);

			List<SpecimenListSummary> result = new ArrayList<SpecimenListSummary>();
			for (SpecimenList specimenList : specimenLists) {
				result.add(SpecimenListSummary.fromSpecimenList(specimenList));
			}
			
			return SpecimenListsEvent.ok(result);
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Internal Server Error";
			}
			
			return SpecimenListsEvent.serverError(message, e);
		}
	}

	@Override
	@PlusTransactional
	public SpecimenListDetailEvent getSpecimenList(ReqSpecimenListDetailEvent req) {
		try {
			Long listId = req.getListId();
			SpecimenList specimenList = daoFactory.getSpecimenListDao().getSpecimenList(listId);
			if (specimenList == null) {
				return SpecimenListDetailEvent.notFound(listId);
			}
			
			Long userId = req.getSessionDataBean().getUserId();
			if (!req.getSessionDataBean().isAdmin() && !specimenList.canUserAccess(userId)) {
				return SpecimenListDetailEvent.notAuthorized(listId);
			}
			
			return SpecimenListDetailEvent.ok(SpecimenListDetails.from(specimenList));			
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Internal Server Error";
			}
			return SpecimenListDetailEvent.serverError(message, e);			
		}
	}

	@Override
	@PlusTransactional
	public SpecimenListCreatedEvent createSpecimenList(CreateSpecimenListEvent req) {
		try {
			SpecimenListDetails listDetails = req.getListDetails();
			
			UserSummary owner = new UserSummary();
			owner.setId(req.getSessionDataBean().getUserId());
			listDetails.setOwner(owner);
			
			SpecimenList specimenList = specimenListFactory.createSpecimenList(listDetails);	
			daoFactory.getSpecimenListDao().saveOrUpdate(specimenList);
			return SpecimenListCreatedEvent.ok(SpecimenListDetails.from(specimenList));
		} catch (ObjectCreationException oce) {
			return SpecimenListCreatedEvent.badRequest(oce);
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Internal Server Error";
			}
			return SpecimenListCreatedEvent.serverError(message, e);
		}
	}

	@Override
	@PlusTransactional
	public SpecimenListUpdatedEvent updateSpecimenList(UpdateSpecimenListEvent req) {
		try {
			SpecimenListDetails listDetails = req.getListDetails();
			Long listId = listDetails.getId();
			if (listId == null) {
				return SpecimenListUpdatedEvent.badRequest(SpecimenListErrorCode.LIST_ID_REQUIRED);
			}
						
			SpecimenList existing = daoFactory.getSpecimenListDao().getSpecimenList(listId);
			if (existing == null) {
				return SpecimenListUpdatedEvent.badRequest(SpecimenListErrorCode.LIST_NOT_FOUND);
			}
			
			Long userId = req.getSessionDataBean().getUserId();
			if (!req.getSessionDataBean().isAdmin() && !existing.getOwner().getId().equals(userId)) {
				return SpecimenListUpdatedEvent.badRequest(SpecimenListErrorCode.NOT_UPDATE_AUTHORIZED);
			}
			
			UserSummary owner = new UserSummary();
			owner.setId(existing.getOwner().getId());
			listDetails.setOwner(owner);
			
			SpecimenList specimenList = specimenListFactory.createSpecimenList(listDetails);
			existing.update(specimenList);
			
			daoFactory.getSpecimenListDao().saveOrUpdate(specimenList);
			return SpecimenListUpdatedEvent.ok(SpecimenListDetails.from(specimenList));			
		} catch (ObjectCreationException oce) {
			return SpecimenListUpdatedEvent.badRequest(oce);
		} catch (Exception e) {		
			String message = e.getMessage();
			if (message == null) {
				message = "Internal Server Error";
			}
			return SpecimenListUpdatedEvent.serverError(message, e);
		}
	}

	@Override
	@PlusTransactional
	public ListSpecimensEvent getListSpecimens(ReqListSpecimensEvent req) {
		try {
			Long listId = req.getListId();
			SpecimenList specimenList = daoFactory.getSpecimenListDao().getSpecimenList(listId);			
			if (specimenList == null) {
				return ListSpecimensEvent.notFound(listId);
			}
			
			Long userId = req.getSessionDataBean().getUserId();
			if (!req.getSessionDataBean().isAdmin() && !specimenList.canUserAccess(userId)) {
				return ListSpecimensEvent.notAuthorized(listId);
			}
			
			return ListSpecimensEvent.ok(SpecimenSummary.from(specimenList.getSpecimens()));
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Internal Server Error";
			}
			return ListSpecimensEvent.serverError(message, e);
		}
	}

	@Override
	@PlusTransactional
	public ListSpecimensUpdatedEvent updateListSpecimens(UpdateListSpecimensEvent req) {
		try {
			Long listId = req.getListId();
			SpecimenList specimenList = daoFactory.getSpecimenListDao().getSpecimenList(listId);			
			if (specimenList == null) {
				return ListSpecimensUpdatedEvent.notFound(listId);
			}
			
			Long userId = req.getSessionDataBean().getUserId();
			if (!req.getSessionDataBean().isAdmin() && !specimenList.getOwner().getId().equals(userId)) {
				return ListSpecimensUpdatedEvent.notAuthorized(listId);
			}
			
			List<Specimen> specimens = null;
			List<String> labels = req.getSpecimens();
			
			if (labels == null || labels.isEmpty()) {
				specimens = new ArrayList<Specimen>();
			} else {
				specimens = daoFactory.getSpecimenDao().getSpecimensByLabel(labels);
			}
			
			switch (req.getOp()) {
				case ADD:
					specimenList.addSpecimens(specimens);
					break;
				
				case UPDATE:
					specimenList.updateSpecimens(specimens);
					break;
				
				case REMOVE:
					specimenList.removeSpecimens(specimens);
					break;				
			}
			
			daoFactory.getSpecimenListDao().saveOrUpdate(specimenList);
			
			List<SpecimenSummary> result = new ArrayList<SpecimenSummary>();
			for (Specimen specimen : specimens) {
				result.add(SpecimenSummary.from(specimen));
			}
			
			return ListSpecimensUpdatedEvent.ok(listId, result);
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Internal Server Error";
			}
			return ListSpecimensUpdatedEvent.serverError(message, e);
		}
	}

	@Override
	@PlusTransactional
	public SpecimenListSharedEvent shareSpecimenList(ShareSpecimenListEvent req) {
		try {
			Long listId = req.getListId();
			SpecimenList specimenList = daoFactory.getSpecimenListDao().getSpecimenList(listId);
			if (specimenList == null) {
				return SpecimenListSharedEvent.notFound(listId);
			}
			
			Long userId = req.getSessionDataBean().getUserId();
			if (!req.getSessionDataBean().isAdmin() && !specimenList.getOwner().getId().equals(userId)) {
				return SpecimenListSharedEvent.notAuthorized(listId);
			}
			
			List<User> users = null;
			List<Long> userIds = req.getUserIds();
			if (userIds == null || userIds.isEmpty()) {
				users = new ArrayList<User>();
			} else {
				users = daoFactory.getUserDao().getUsersById(userIds);
			}
			
			switch (req.getOp()) {
				case ADD:
					specimenList.addSharedUsers(users);
					break;
					
				case UPDATE:
					specimenList.updateSharedUsers(users);
					break;
					
				case REMOVE:
					specimenList.removeSharedUsers(users);
					break;					
			}
						
			daoFactory.getSpecimenListDao().saveOrUpdate(specimenList);			
			List<UserSummary> result = new ArrayList<UserSummary>();
			for (User user : specimenList.getSharedWith()) {
				result.add(UserSummary.fromUser(user));
			}
			
			return SpecimenListSharedEvent.ok(listId, result);
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Internal Server Error";
			}
			return SpecimenListSharedEvent.serverError(message, e);
		}
	}
}
