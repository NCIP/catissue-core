package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;
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
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.ExportedFileDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.common.util.MessageUtil;

import au.com.bytecode.opencsv.CSVWriter;

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
			SpecimenList specimenList = getSpecimenList(req.getPayload(), null);
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
			SpecimenList existing = getSpecimenList(req.getPayload(), null);
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
	public ResponseEvent<ListSpecimensDetail> getListSpecimens(RequestEvent<SpecimenListCriteria> req) {
		try {
			SpecimenListCriteria crit = req.getPayload();
			Long listId = crit.specimenListId();

			SpecimenList specimenList = getSpecimenList(listId, null);

			Long specimensCount = null;
			if (crit.includeStat()) {
				specimensCount = daoFactory.getSpecimenListDao().getListSpecimensCount(listId);
				crit.includeStat(false);
			}

			List<Pair<Long, Long>> siteCpPairs = AccessCtrlMgr.getInstance().getReadAccessSpecimenSiteCps();
			if (siteCpPairs != null && siteCpPairs.isEmpty()) {
				return ResponseEvent.response(ListSpecimensDetail.from(specimensCount));
			}

			List<Specimen> specimens = daoFactory.getSpecimenDao().getSpecimens(crit.siteCps(siteCpPairs));
			return ResponseEvent.response(ListSpecimensDetail.from(specimens, specimensCount));
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
			UpdateListSpecimensOp opDetail = req.getPayload();
			SpecimenList specimenList = getSpecimenList(opDetail.getListId(), null);

			List<Specimen> specimens = null;
			List<String> labels = opDetail.getSpecimens();
			List<Pair<Long, Long>> siteCpPairs = AccessCtrlMgr.getInstance().getReadAccessSpecimenSiteCps();
			
			if (labels == null || labels.isEmpty()) {
				specimens = new ArrayList<Specimen>();
			} else {
				ensureValidSpecimens(labels, siteCpPairs);
				specimens = daoFactory.getSpecimenDao().getSpecimens(new SpecimenListCriteria().labels(labels));
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
			
			Long specimensCount = daoFactory.getSpecimenListDao().getListSpecimensCount(specimenList.getId());
			List<Specimen> readAccessSpecimens = getReadAccessSpecimens(specimenList.getId(), siteCpPairs);
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
			ShareSpecimenListOp opDetail = req.getPayload();
			SpecimenList specimenList = getSpecimenList(opDetail.getListId(), null);

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

	@Override
	@PlusTransactional
	public ResponseEvent<ExportedFileDetail> exportSpecimenList(RequestEvent<EntityQueryCriteria> req) {
		try {
			EntityQueryCriteria crit = req.getPayload();
			SpecimenList list = getSpecimenList(crit.getId(), crit.getName());
			List<Specimen> specimens = getReadAccessSpecimens(list.getId(), null);
			return ResponseEvent.response(exportSpecimenList(list, specimens));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@PlusTransactional
	private ResponseEvent<SpecimenListDetails> updateSpecimenList(RequestEvent<SpecimenListDetails> req, boolean partial) {
		try {
			SpecimenListDetails listDetails = req.getPayload();
			SpecimenList existing = getSpecimenList(listDetails.getId(), null);

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
			
			List<Specimen> readAccessSpecimens = getReadAccessSpecimens(specimenList.getId(), null);
			return ResponseEvent.response(SpecimenListDetails.from(existing, readAccessSpecimens));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	private SpecimenList getSpecimenList(Long listId, String listName) {
		SpecimenList list = null;
		Object key = null;

		if (listId != null) {
			list = daoFactory.getSpecimenListDao().getSpecimenList(listId);
			key = listId;
		} else if (StringUtils.isNotBlank(listName)) {
			list = daoFactory.getSpecimenListDao().getSpecimenListByName(listName);
			key = listName;
		}

		if (list == null) {
			throw OpenSpecimenException.userError(SpecimenListErrorCode.NOT_FOUND, key);
		}

		Long userId = AuthUtil.getCurrentUser().getId();
		if (!AuthUtil.isAdmin() && !list.canUserAccess(userId)) {
			throw OpenSpecimenException.userError(SpecimenListErrorCode.ACCESS_NOT_ALLOWED);
		}

		return list;
	}

	private List<Specimen> getReadAccessSpecimens(Long specimenListId, List<Pair<Long, Long>> siteCpPairs) {
		return getReadAccessSpecimens(specimenListId, null, siteCpPairs);
	}
	
	private List<Specimen> getReadAccessSpecimens(Long specimenListId, List<String> specimenLabels, List<Pair<Long, Long>> siteCpPairs) {
		if (siteCpPairs == null) {
			siteCpPairs = AccessCtrlMgr.getInstance().getReadAccessSpecimenSiteCps();
		}

		if (siteCpPairs != null && siteCpPairs.isEmpty()) {
			return Collections.<Specimen>emptyList();
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
		SpecimenList list = daoFactory.getSpecimenListDao().getSpecimenListByName(newList.getName());
		if  (list != null) {
			throw OpenSpecimenException.userError(SpecimenListErrorCode.DUP_NAME, newList.getName());
		}
	}

	private ExportedFileDetail exportSpecimenList(SpecimenList list, Collection<Specimen> specimens) {
		FileWriter fileWriter = null;
		CSVWriter csvWriter = null;
		File dataFile = null;

		try {
			File dataDir = new File(ConfigUtil.getInstance().getDataDir());
			dataFile = File.createTempFile("specimen-list-", ".csv", dataDir);
			fileWriter = new FileWriter(dataFile);
			csvWriter = new CSVWriter(fileWriter);

			csvWriter.writeNext(new String[] { getMsg(LIST_NAME), list.getName()});
			csvWriter.writeNext(new String[] { getMsg(LIST_DESC), list.getDescription()});
			csvWriter.writeNext(new String[0]);

			csvWriter.writeNext(getHeaderRow());
			for (Specimen specimen : specimens) {
				csvWriter.writeNext(getDataRow(specimen));
			}

			csvWriter.flush();
		} catch (Exception e) {
			if (dataFile != null) {
				dataFile.delete();
			}

			throw new RuntimeException("Error exporting specimen list", e);
		} finally {
			IOUtils.closeQuietly(fileWriter);
			IOUtils.closeQuietly(csvWriter);
		}

		return new ExportedFileDetail(list.getName(), dataFile);
	}

	private String[] getHeaderRow() {
		return new String[] {
			getMsg(SPMN_LABEL),
			getMsg(SPMN_CP),
			getMsg(SPMN_LINEAGE),
			getMsg(SPMN_CLASS),
			getMsg(SPMN_TYPE),
			getMsg(SPMN_PATHOLOGY),
			getMsg(SPMN_LOC),
			getMsg(SPMN_QTY)
		};
	}

	private String[] getDataRow(Specimen specimen) {
		String availableQty = "";
		if (specimen.getAvailableQuantity() != null) {
			availableQty = specimen.getAvailableQuantity().stripTrailingZeros().toString();
		}

		String location = "";
		StorageContainerPosition position = specimen.getPosition();
		if (position != null) {
			location = position.getContainer().getName();
			location += ": (" + position.getPosTwo() + ", " + position.getPosOne() + ")";
		}

		return new String[] {
			specimen.getLabel(),
			specimen.getCollectionProtocol().getShortTitle(),
			specimen.getLineage(),
			specimen.getSpecimenClass(),
			specimen.getSpecimenType(),
			specimen.getPathologicalStatus(),
			location,
			availableQty
		};
	}

	private String getMsg(String code) {
		return MessageUtil.getInstance().getMessage(code);
	}

	private static final String LIST_NAME      = "specimen_list_name";

	private static final String LIST_DESC      = "specimen_list_description";

	private static final String SPMN_LABEL     = "specimen_label";

	private static final String SPMN_CLASS     = "specimen_class";

	private static final String SPMN_TYPE      = "specimen_type";

	private static final String SPMN_PATHOLOGY = "specimen_pathology";

	private static final String SPMN_CP        = "specimen_cp_short";

	private static final String SPMN_QTY       = "specimen_quantity";

	private static final String SPMN_LOC       = "specimen_location";

	private static final String SPMN_LINEAGE   = "specimen_lineage";
}
