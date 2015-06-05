package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenList;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenListErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenListFactory;
import com.krishagni.catissueplus.core.biospecimen.events.ListSpecimensDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ShareSpecimenListOp;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListDetails;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListSummary;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateListSpecimensOp;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenListCriteria;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenListService;
import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.util.AuthUtil;

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
	public ResponseEvent<List<SpecimenListSummary>> getUserSpecimenLists(RequestEvent<?> req) {
		try {
			Long userId = AuthUtil.getCurrentUser().getId();
			List<SpecimenList> specimenLists = new ArrayList<SpecimenList>();
			if (AuthUtil.isAdmin()){
				specimenLists = daoFactory.getSpecimenListDao().getSpecimenLists();
			} else {
				specimenLists = daoFactory.getSpecimenListDao().getUserSpecimenLists(userId);
			}

			List<SpecimenListSummary> result = new ArrayList<SpecimenListSummary>();
			for (SpecimenList specimenList : specimenLists) {
				result.add(SpecimenListSummary.fromSpecimenList(specimenList));
			}
			
			return ResponseEvent.response(result);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenListDetails> getSpecimenList(RequestEvent<Long> req) {
		try {
			Long listId = req.getPayload();
			SpecimenList specimenList = daoFactory.getSpecimenListDao().getSpecimenList(listId);
			if (specimenList == null) {
				return ResponseEvent.userError(SpecimenListErrorCode.NOT_FOUND);
			}
			
			Long userId = AuthUtil.getCurrentUser().getId();
			if (!AuthUtil.isAdmin() && !specimenList.canUserAccess(userId)) {
				return ResponseEvent.userError(SpecimenListErrorCode.ACCESS_NOT_ALLOWED);
			}
			
			List<Specimen> accessibleSpecimens = getAccessibleSpecimens(specimenList.getId(), null);
			return ResponseEvent.response(SpecimenListDetails.from(specimenList, accessibleSpecimens));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);			
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenListDetails> createSpecimenList(RequestEvent<SpecimenListDetails> req) {
		try {
			SpecimenListDetails listDetails = req.getPayload();
			
			List<Pair<Long, Long>> siteCpPairs = AccessCtrlMgr.getInstance().getReadAccessSpecimenSiteCps();
			if (siteCpPairs != null && siteCpPairs.isEmpty()) {
				return ResponseEvent.userError(SpecimenListErrorCode.ACCESS_NOT_ALLOWED);
			}
			
			UserSummary owner = new UserSummary();
			owner.setId(AuthUtil.getCurrentUser().getId());
			listDetails.setOwner(owner);
			
			SpecimenList specimenList = specimenListFactory.createSpecimenList(listDetails);
			ensureValidSpecimensAndUsers(specimenList, siteCpPairs);
			daoFactory.getSpecimenListDao().saveOrUpdate(specimenList);
			return ResponseEvent.response(SpecimenListDetails.from(specimenList));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenListDetails> updateSpecimenList(RequestEvent<SpecimenListDetails> req) {
		try {
			SpecimenListDetails listDetails = req.getPayload();
			Long listId = listDetails.getId();
			if (listId == null) {
				return ResponseEvent.userError(SpecimenListErrorCode.NOT_FOUND);
			}
						
			SpecimenList existing = daoFactory.getSpecimenListDao().getSpecimenList(listId);
			if (existing == null) {
				return ResponseEvent.userError(SpecimenListErrorCode.NOT_FOUND);
			}
			
			Long userId = AuthUtil.getCurrentUser().getId();
			if (!AuthUtil.isAdmin() && !existing.getOwner().getId().equals(userId)) {
				return ResponseEvent.userError(SpecimenListErrorCode.ACCESS_NOT_ALLOWED);
			}
			
			UserSummary owner = new UserSummary();
			owner.setId(existing.getOwner().getId());
			listDetails.setOwner(owner);
			
			SpecimenList specimenList = specimenListFactory.createSpecimenList(listDetails);
			ensureValidSpecimensAndUsers(specimenList, null);
			existing.update(specimenList);
			
			daoFactory.getSpecimenListDao().saveOrUpdate(existing);
			return ResponseEvent.response(SpecimenListDetails.from(existing));			
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenListDetails> deleteSpecimenList(RequestEvent<Long> req) {
		try {
			SpecimenList existing = daoFactory.getSpecimenListDao().getSpecimenList(req.getPayload());
			if (existing == null) {
				return ResponseEvent.userError(SpecimenListErrorCode.NOT_FOUND);
			}
			
			Long userId = AuthUtil.getCurrentUser().getId();
			if (!AuthUtil.isAdmin() && !existing.getOwner().getId().equals(userId)) {
				return ResponseEvent.userError(SpecimenListErrorCode.ACCESS_NOT_ALLOWED);
			}
			
			existing.setDeletedOn(Calendar.getInstance().getTime());
			daoFactory.getSpecimenListDao().saveOrUpdate(existing);
			return ResponseEvent.response(SpecimenListDetails.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<ListSpecimensDetail> getListSpecimens(RequestEvent<Long> req) {
		try {
			Long listId = req.getPayload();
			SpecimenList specimenList = daoFactory.getSpecimenListDao().getSpecimenList(listId);			
			if (specimenList == null) {
				return ResponseEvent.userError(SpecimenListErrorCode.NOT_FOUND);
			}
			
			Long userId = AuthUtil.getCurrentUser().getId();
			if (!AuthUtil.isAdmin() && !specimenList.canUserAccess(userId)) {
				return ResponseEvent.userError(SpecimenListErrorCode.ACCESS_NOT_ALLOWED);
			}
			
			Long specimensCount = daoFactory.getSpecimenListDao().getListSpecimensCount(listId);
			List<Pair<Long, Long>> siteCpPairs = AccessCtrlMgr.getInstance().getReadAccessSpecimenSiteCps();
			if (siteCpPairs != null && siteCpPairs.isEmpty()) {
				return ResponseEvent.response(ListSpecimensDetail.from(specimensCount));
			}
			
			List<Specimen> accessibleSpecimens = getAccessibleSpecimens(listId, siteCpPairs);
			return ResponseEvent.response(ListSpecimensDetail.from(accessibleSpecimens, specimensCount));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<SpecimenDetail>>  updateListSpecimens(RequestEvent<UpdateListSpecimensOp> req) {
		try {
			UpdateListSpecimensOp opDetail = req.getPayload();
			
			Long listId = opDetail.getListId();
			SpecimenList specimenList = daoFactory.getSpecimenListDao().getSpecimenList(listId);			
			if (specimenList == null) {
				return ResponseEvent.userError(SpecimenListErrorCode.NOT_FOUND);
			}
			
			Long userId = AuthUtil.getCurrentUser().getId();
			if (!AuthUtil.isAdmin() && !specimenList.getOwner().getId().equals(userId)) {
				return ResponseEvent.userError(SpecimenListErrorCode.ACCESS_NOT_ALLOWED);
			}
			
			List<Specimen> specimens = null;
			List<String> labels = opDetail.getSpecimens();
			
			if (labels == null || labels.isEmpty()) {
				specimens = new ArrayList<Specimen>();
			} else {
				ensureValidSpecimens(labels, null);
				specimens = daoFactory.getSpecimenDao()
						.getSpecimens(new SpecimenListCriteria().labels(labels));
			}
			
			switch (opDetail.getOp()) {
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
			
			List<SpecimenDetail> result = new ArrayList<SpecimenDetail>();
			for (Specimen specimen : specimenList.getSpecimens()) {
				result.add(SpecimenDetail.from(specimen));
			}
			
			return ResponseEvent.response(result);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<UserSummary>> shareSpecimenList(RequestEvent<ShareSpecimenListOp> req) {
		try {
			ShareSpecimenListOp opDetail = req.getPayload();
			
			Long listId = opDetail.getListId();
			SpecimenList specimenList = daoFactory.getSpecimenListDao().getSpecimenList(listId);
			if (specimenList == null) {
				return ResponseEvent.userError(SpecimenListErrorCode.NOT_FOUND);
			}
			
			Long userId = AuthUtil.getCurrentUser().getId();
			if (!AuthUtil.isAdmin() && !specimenList.getOwner().getId().equals(userId)) {
				return ResponseEvent.userError(SpecimenListErrorCode.ACCESS_NOT_ALLOWED);
			}
			
			List<User> users = null;
			List<Long> userIds = opDetail.getUserIds();
			if (userIds == null || userIds.isEmpty()) {
				users = new ArrayList<User>();
			} else {
				ensureValidUsers(userIds);
				users = daoFactory.getUserDao().getUsersByIds(userIds);
			}
			
			switch (opDetail.getOp()) {
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
				result.add(UserSummary.from(user));
			}
			
			return ResponseEvent.response(result);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private List<Specimen> getAccessibleSpecimens(Long specimenListId, List<Pair<Long, Long>> siteCpPairs) {
		if (siteCpPairs == null) {
			siteCpPairs = AccessCtrlMgr.getInstance().getReadAccessSpecimenSiteCps();
		}
		
		SpecimenListCriteria crit = new SpecimenListCriteria()
			.specimenListId(specimenListId)
			.siteCps(siteCpPairs);
		
		return daoFactory.getSpecimenDao().getSpecimens(crit);
	}
	
	private void ensureValidSpecimensAndUsers(SpecimenList specimenList, List<Pair<Long, Long>> siteCpPairs) {
		ensureValidSpecimens(specimenList, siteCpPairs);
		ensureValidUsers(specimenList, siteCpPairs);
	}
	
	private void ensureValidSpecimens(SpecimenList specimenList, List<Pair<Long, Long>> siteCpPairs) {
		if (!CollectionUtils.isEmpty(specimenList.getSpecimens())) {
			return;
		}
		
		List<String> labels = new ArrayList<String>();
		for (Specimen specimen : specimenList.getSpecimens()) {
			labels.add(specimen.getLabel());
		}
		
		ensureValidSpecimens(labels, siteCpPairs);
	}
	
	private void ensureValidSpecimens(List<String> specimenLabels,  List<Pair<Long, Long>> siteCpPairs) {
		if (siteCpPairs == null) {
			siteCpPairs = AccessCtrlMgr.getInstance().getReadAccessSpecimenSiteCps();
		}
		
		List<Specimen> specimens = daoFactory.getSpecimenDao()
				.getSpecimens(new SpecimenListCriteria().labels(specimenLabels).siteCps(siteCpPairs));
		
		if (specimens.size() != specimenLabels.size()) {
			throw OpenSpecimenException.userError(SpecimenListErrorCode.INVALID_LABELS);
		}
		
	}
	
	private void ensureValidUsers(SpecimenList specimenList, List<Pair<Long, Long>> siteCpPairs) {
		if (!CollectionUtils.isEmpty(specimenList.getSharedWith())) {
			return;
		}
		
		Long userId = specimenList.getOwner().getId();
		List<Long> sharedUsers = new ArrayList<Long>();
		for (User user : specimenList.getSharedWith()) {
			if (user.getId().equals(userId)) {
				continue;
			}
			sharedUsers.add(user.getId());
		}
		
		ensureValidUsers(sharedUsers);
	}
	
	private void ensureValidUsers(List<Long> userIds) {
		Long instituteId = null;
		if (!AuthUtil.isAdmin()) {
			User user = daoFactory.getUserDao().getById(AuthUtil.getCurrentUser().getId());
			instituteId = user.getInstitute().getId();
		}
		
		List<User> users = daoFactory.getUserDao().getUsersByIdsAndInstitute(userIds, instituteId);
		if (userIds.size() != users.size()) {
			throw OpenSpecimenException.userError(SpecimenListErrorCode.INVALID_USERS_LIST);
		}
	}
}
