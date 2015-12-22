package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.util.CollectionUtils;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenList;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenListErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenListFactory;
import com.krishagni.catissueplus.core.biospecimen.events.ListSpecimensDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ShareSpecimenListOp;
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

	private static final Pattern DEF_LIST_NAME_PATTERN = Pattern.compile("\\$\\$\\$\\$user_\\d+");

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
			User currentUser = AuthUtil.getCurrentUser();
			List<SpecimenList> specimenLists = new ArrayList<SpecimenList>();
			if (AuthUtil.isAdmin()){
				specimenLists = daoFactory.getSpecimenListDao().getSpecimenLists();
			} else {
				specimenLists = daoFactory.getSpecimenListDao().getUserSpecimenLists(currentUser.getId());
			}

			List<SpecimenListSummary> result = new ArrayList<SpecimenListSummary>();
			SpecimenList defUserList = null;
			for (SpecimenList specimenList : specimenLists) {
				if (specimenList.isDefaultList(currentUser)) {
					defUserList = specimenList;
					continue;
				}

				result.add(SpecimenListSummary.fromSpecimenList(specimenList));
			}

			if (defUserList == null) {
				defUserList = createDefaultList(currentUser, 0L);
			}

			result.add(0, SpecimenListSummary.fromSpecimenList(defUserList));
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
			User currentUser = AuthUtil.getCurrentUser();

			SpecimenList specimenList = null;
			if (listId == 0) {
				specimenList = getDefaultList(currentUser);
			} else {
				specimenList = daoFactory.getSpecimenListDao().getSpecimenList(listId);
			}

			if (specimenList == null) {
				return ResponseEvent.userError(SpecimenListErrorCode.NOT_FOUND, listId);
			}
			

			if (!AuthUtil.isAdmin() && !specimenList.canUserAccess(currentUser.getId())) {
				return ResponseEvent.userError(SpecimenListErrorCode.ACCESS_NOT_ALLOWED);
			}
			
			List<Specimen> readAccessSpecimens = getReadAccessSpecimens(specimenList.getId(), null);
			return ResponseEvent.response(SpecimenListDetails.from(specimenList, readAccessSpecimens));
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
			ensureUniqueName(specimenList);
			ensureValidSpecimensAndUsers(listDetails, specimenList, siteCpPairs);
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
		return updateSpecimenList(req, false);
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenListDetails> patchSpecimenList(RequestEvent<SpecimenListDetails> req) {
		return updateSpecimenList(req, true);
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenListDetails> deleteSpecimenList(RequestEvent<Long> req) {
		try {
			Long listId = req.getPayload();
			SpecimenList existing = daoFactory.getSpecimenListDao().getSpecimenList(listId);
			if (existing == null) {
				return ResponseEvent.userError(SpecimenListErrorCode.NOT_FOUND, listId);
			}
			
			Long userId = AuthUtil.getCurrentUser().getId();
			if (!AuthUtil.isAdmin() && !existing.getOwner().getId().equals(userId)) {
				return ResponseEvent.userError(SpecimenListErrorCode.ACCESS_NOT_ALLOWED);
			}

			existing.delete();
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
			User currentUser = AuthUtil.getCurrentUser();

			SpecimenList specimenList = null;
			if (listId == 0) {
				specimenList = daoFactory.getSpecimenListDao().getDefaultSpecimenList(currentUser.getId());
			} else {
				specimenList = daoFactory.getSpecimenListDao().getSpecimenList(listId);
			}

			if (specimenList == null) {
				if (listId != 0) {
					return ResponseEvent.userError(SpecimenListErrorCode.NOT_FOUND, listId);
				} else {
					return ResponseEvent.response(ListSpecimensDetail.from(Collections.<Specimen>emptyList(), 0L));
				}
			}

			if (!AuthUtil.isAdmin() && !specimenList.canUserAccess(currentUser.getId())) {
				return ResponseEvent.userError(SpecimenListErrorCode.ACCESS_NOT_ALLOWED);
			}
			
			Long specimensCount = daoFactory.getSpecimenListDao().getListSpecimensCount(listId);
			List<Pair<Long, Long>> siteCpPairs = AccessCtrlMgr.getInstance().getReadAccessSpecimenSiteCps();
			if (siteCpPairs != null && siteCpPairs.isEmpty()) {
				return ResponseEvent.response(ListSpecimensDetail.from(specimensCount));
			}
			
			List<Specimen> readAccessSpecimens = getReadAccessSpecimens(listId, siteCpPairs);
			return ResponseEvent.response(ListSpecimensDetail.from(readAccessSpecimens, specimensCount));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<ListSpecimensDetail>  updateListSpecimens(RequestEvent<UpdateListSpecimensOp> req) {
		try {
			User currentUser = AuthUtil.getCurrentUser();
			UpdateListSpecimensOp opDetail = req.getPayload();
			Long listId = opDetail.getListId();

			SpecimenList specimenList = null;
			if (listId == 0) {
				specimenList = getDefaultList(currentUser);
			} else {
				specimenList = daoFactory.getSpecimenListDao().getSpecimenList(listId);
			}

			if (specimenList == null) {
				return ResponseEvent.userError(SpecimenListErrorCode.NOT_FOUND, listId);
			}

			if (!AuthUtil.isAdmin() && !specimenList.canUserAccess(currentUser.getId())) {
				return ResponseEvent.userError(SpecimenListErrorCode.ACCESS_NOT_ALLOWED);
			}
			
			List<Specimen> specimens = null;
			List<String> labels = opDetail.getSpecimens();
			List<Pair<Long, Long>> siteCpPairs = AccessCtrlMgr.getInstance().getReadAccessSpecimenSiteCps();
			
			if (labels == null || labels.isEmpty()) {
				specimens = new ArrayList<Specimen>();
			} else {
				ensureValidSpecimens(labels, siteCpPairs);
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
			
			Long specimensCount = daoFactory.getSpecimenListDao().getListSpecimensCount(listId);
			List<Specimen> readAccessSpecimens = getReadAccessSpecimens(listId, siteCpPairs);
			return ResponseEvent.response(ListSpecimensDetail.from(readAccessSpecimens, specimensCount));
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
			User currentUser = AuthUtil.getCurrentUser();
			ShareSpecimenListOp opDetail = req.getPayload();
			Long listId = opDetail.getListId();

			SpecimenList specimenList = null;
			if (listId == 0) {
				specimenList = getDefaultList(currentUser, null);
			} else {
				specimenList = daoFactory.getSpecimenListDao().getSpecimenList(listId);
			}

			if (specimenList == null) {
				return ResponseEvent.userError(SpecimenListErrorCode.NOT_FOUND, listId);
			}

			if (!AuthUtil.isAdmin() && !specimenList.canUserAccess(currentUser.getId())) {
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

	private SpecimenList createDefaultList(User user, Long id) {
		SpecimenList list = specimenListFactory.createDefaultSpecimenList(user);
		list.setId(id);
		return list;
	}

	private SpecimenList getDefaultList(User user) {
		return getDefaultList(user, 0L);
	}

	private SpecimenList getDefaultList(User user, Long id) {
		SpecimenList specimenList = daoFactory.getSpecimenListDao().getDefaultSpecimenList(user.getId());
		if (specimenList == null) {
			specimenList = createDefaultList(user, id);
		}

		return specimenList;
	}

	@PlusTransactional
	private ResponseEvent<SpecimenListDetails> updateSpecimenList(RequestEvent<SpecimenListDetails> req, boolean partial) {
		try {
			User currentUser = AuthUtil.getCurrentUser();
			SpecimenListDetails listDetails = req.getPayload();

			SpecimenList existing = null;
			if (listDetails.getId() == 0) {
				existing = getDefaultList(currentUser, null);
				listDetails.setName(existing.getName());
			} else {
				existing = daoFactory.getSpecimenListDao().getSpecimenList(listDetails.getId());
			}

			if (existing == null) {
				return ResponseEvent.userError(SpecimenListErrorCode.NOT_FOUND, listDetails.getId());
			}

			if (!AuthUtil.isAdmin() && !existing.canUserAccess(currentUser.getId())) {
				return ResponseEvent.userError(SpecimenListErrorCode.ACCESS_NOT_ALLOWED);
			}
			
			UserSummary owner = new UserSummary();
			owner.setId(existing.getOwner().getId());
			listDetails.setOwner(owner);
			
			SpecimenList specimenList = null;
			if (partial) {
				specimenList = specimenListFactory.createSpecimenList(existing, listDetails);
			} else {
				specimenList = specimenListFactory.createSpecimenList(listDetails);
			}
			
			ensureUniqueName(existing, specimenList);
			ensureValidSpecimensAndUsers(listDetails, specimenList, null);
			existing.update(specimenList);
			daoFactory.getSpecimenListDao().saveOrUpdate(existing);
			
			List<Specimen> readAccessSpecimens = getReadAccessSpecimens(specimenList.getId(), null);
			return ResponseEvent.response(SpecimenListDetails.from(existing, readAccessSpecimens));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private List<Specimen> getReadAccessSpecimens(Long specimenListId, List<Pair<Long, Long>> siteCpPairs) {
		return getReadAccessSpecimens(specimenListId, null, siteCpPairs);
	}
	
	private List<Specimen> getReadAccessSpecimens(Long specimenListId, List<String> specimenLabels, List<Pair<Long, Long>> siteCpPairs) {
		if (siteCpPairs == null) {
			siteCpPairs = AccessCtrlMgr.getInstance().getReadAccessSpecimenSiteCps();
		}
		
		if (siteCpPairs != null && siteCpPairs.isEmpty()) {
			return Collections.<Specimen> emptyList();
		}
		
		SpecimenListCriteria crit = new SpecimenListCriteria()
			.specimenListId(specimenListId)	
			.labels(specimenLabels)
			.siteCps(siteCpPairs);
		
		return daoFactory.getSpecimenDao().getSpecimens(crit);
	}
	
	private void ensureValidSpecimensAndUsers(SpecimenListDetails details, SpecimenList specimenList, List<Pair<Long, Long>> siteCpPairs) {
		if (details.isAttrModified("specimens")) {
			ensureValidSpecimens(specimenList, siteCpPairs);
		}
		
		if (details.isAttrModified("sharedWith")){
			ensureValidUsers(specimenList, siteCpPairs);
		}
	}
	
	private void ensureValidSpecimens(SpecimenList specimenList, List<Pair<Long, Long>> siteCpPairs) {
		if (CollectionUtils.isEmpty(specimenList.getSpecimens())) {
			return;
		}
		
		List<String> labels = new ArrayList<String>();
		for (Specimen specimen : specimenList.getSpecimens()) {
			labels.add(specimen.getLabel());
		}
		
		ensureValidSpecimens(labels, siteCpPairs);
	}
	
	private void ensureValidSpecimens(List<String> specimenLabels,  List<Pair<Long, Long>> siteCpPairs) {
		List<Specimen> specimens = getReadAccessSpecimens(null, specimenLabels, siteCpPairs);
		if (specimens.size() != specimenLabels.size()) {
			throw OpenSpecimenException.userError(SpecimenListErrorCode.INVALID_LABELS);
		}
	}
	
	private void ensureValidUsers(SpecimenList specimenList, List<Pair<Long, Long>> siteCpPairs) {
		if (CollectionUtils.isEmpty(specimenList.getSharedWith())) {
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
	
	private void ensureUniqueName(SpecimenList existingList, SpecimenList newList) {
		if (existingList != null && existingList.getName().equals(newList.getName())) {
			return;
		}
		
		ensureUniqueName(newList);
	}
	
	private void ensureUniqueName(SpecimenList newList) {
		String newListName = newList.getName();

		SpecimenList list = daoFactory.getSpecimenListDao().getSpecimenListByName(newListName);
		if  (list != null) {
			throw OpenSpecimenException.userError(SpecimenListErrorCode.DUP_NAME, newListName);
		}

		if (DEF_LIST_NAME_PATTERN.matcher(newListName).matches()) {
			throw OpenSpecimenException.userError(SpecimenListErrorCode.DUP_NAME, newListName);
		}
	}
}