package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

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
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenListsCriteria;
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
import com.krishagni.catissueplus.core.common.util.CsvFileWriter;
import com.krishagni.catissueplus.core.common.util.CsvWriter;
import com.krishagni.catissueplus.core.common.util.MessageUtil;


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
	public ResponseEvent<List<SpecimenListSummary>> getSpecimenLists(RequestEvent<SpecimenListsCriteria> req) {
		try {
			SpecimenListsCriteria crit = addSpecimenListsCriteria(req.getPayload());
			return ResponseEvent.response(daoFactory.getSpecimenListDao().getSpecimenLists(crit));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Long> getSpecimenListsCount(RequestEvent<SpecimenListsCriteria> req) {
		try {
			SpecimenListsCriteria crit = addSpecimenListsCriteria(req.getPayload());
			return ResponseEvent.response(daoFactory.getSpecimenListDao().getSpecimenListsCount(crit));
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
			return ResponseEvent.response(SpecimenListDetails.from(existing, null));
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

			Integer specimensCount = null;
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
			List<Long> ids = opDetail.getSpecimens();
			List<Pair<Long, Long>> siteCpPairs = AccessCtrlMgr.getInstance().getReadAccessSpecimenSiteCps();
			
			if (CollectionUtils.isEmpty(ids)) {
				specimens = new ArrayList<>();
			} else {
				ensureValidSpecimens(ids, siteCpPairs);
				specimens = daoFactory.getSpecimenDao().getSpecimens(new SpecimenListCriteria().ids(ids));
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
			
			daoFactory.getSpecimenListDao().saveOrUpdate(specimenList, true);

			List<Specimen> readAccessSpecimens = getReadAccessSpecimens(specimenList.getId(), siteCpPairs);
			return ResponseEvent.response(ListSpecimensDetail.from(readAccessSpecimens, specimenList.size()));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<ListSpecimensDetail> addChildSpecimens(RequestEvent<Long> req) {
		try {
			SpecimenList list = getSpecimenList(req.getPayload(), null);
			int listSize = list.size();

			List<Pair<Long, Long>> siteCpPairs = AccessCtrlMgr.getInstance().getReadAccessSpecimenSiteCps();
			List<Specimen> accessibleSpmns = getReadAccessSpecimens(list.getId(), siteCpPairs);

			List<Specimen> addedSpmns = new ArrayList<>();
			addChildSpecimens(list, accessibleSpmns, addedSpmns);
			accessibleSpmns.addAll(addedSpmns);
			return ResponseEvent.response(ListSpecimensDetail.from(accessibleSpmns, addedSpmns.size() + listSize));
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
			return ResponseEvent.response(exportSpecimenList(list, SpecimenList.groupByAncestors(specimens)));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	private SpecimenListsCriteria addSpecimenListsCriteria(SpecimenListsCriteria crit) {
		if (!AuthUtil.isAdmin()) {
			crit.userId(AuthUtil.getCurrentUser().getId());
		}

		return crit;
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
			daoFactory.getSpecimenListDao().saveOrUpdate(existing, true);
			
			List<Specimen> readAccessSpecimens = getReadAccessSpecimens(existing.getId(), null);
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
			if (listId != 0) {
				list = daoFactory.getSpecimenListDao().getSpecimenList(listId);
			} else {
				list = getDefaultList(AuthUtil.getCurrentUser(), null);
			}
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

	private List<Specimen> getReadAccessSpecimens(Long specimenListId, List<Long> specimenIds, List<Pair<Long, Long>> siteCpPairs) {
		if (siteCpPairs == null) {
			siteCpPairs = AccessCtrlMgr.getInstance().getReadAccessSpecimenSiteCps();
		}

		if (siteCpPairs != null && siteCpPairs.isEmpty()) {
			return Collections.<Specimen>emptyList();
		}

		SpecimenListCriteria crit = new SpecimenListCriteria()
			.specimenListId(specimenListId)
			.ids(specimenIds)
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
		
		List<Long> ids = specimenList.getSpecimens().stream().map(Specimen::getId).collect(Collectors.toList());
		ensureValidSpecimens(ids, siteCpPairs);
	}
	
	private void ensureValidSpecimens(List<Long> specimenIds,  List<Pair<Long, Long>> siteCpPairs) {
		List<Specimen> specimens = getReadAccessSpecimens(null, specimenIds, siteCpPairs);
		if (specimens.size() != specimenIds.size()) {
			throw OpenSpecimenException.userError(SpecimenListErrorCode.INVALID_SPECIMENS);
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

	private void addChildSpecimens(SpecimenList list, List<Specimen> specimens, List<Specimen> added) {
		for (Specimen specimen : specimens) {
			List<Specimen> childSpmns = specimen.getChildCollection()
				.stream().filter(childSpmn -> childSpmn.isCollected() && !list.contains(childSpmn))
				.collect(Collectors.toList());
			list.addSpecimens(childSpmns);
			added.addAll(childSpmns);

			addChildSpecimens(list, childSpmns, added);
		}
	}

	private ExportedFileDetail exportSpecimenList(SpecimenList list, Collection<Specimen> specimens) {
		String listName = list.getName();
		FileWriter fileWriter = null;
		CsvWriter csvWriter = null;
		File dataFile = null;

		try {
			if (list.isDefaultList()) {
				listName = list.getOwner().formattedName() + " " + getMsg(LIST_DEFAULT);
			}

			File dataDir = new File(ConfigUtil.getInstance().getDataDir());
			dataFile = File.createTempFile("specimen-list-", ".csv", dataDir);
			fileWriter = new FileWriter(dataFile);
			csvWriter = CsvFileWriter.createCsvFileWriter(fileWriter);

			csvWriter.writeNext(new String[] { getMsg(LIST_NAME), listName});
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

		return new ExportedFileDetail(listName, dataFile);
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

	private static final String LIST_DEFAULT   = "specimen_list_default_user_list";

	private static final String SPMN_LABEL     = "specimen_label";

	private static final String SPMN_CLASS     = "specimen_class";

	private static final String SPMN_TYPE      = "specimen_type";

	private static final String SPMN_PATHOLOGY = "specimen_pathology";

	private static final String SPMN_CP        = "specimen_cp_short";

	private static final String SPMN_QTY       = "specimen_quantity";

	private static final String SPMN_LOC       = "specimen_location";

	private static final String SPMN_LINEAGE   = "specimen_lineage";
}
