package com.krishagni.catissueplus.core.administrative.services.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import com.krishagni.catissueplus.core.administrative.domain.ContainerType;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerFactory;
import com.krishagni.catissueplus.core.administrative.events.AssignPositionsOp;
import com.krishagni.catissueplus.core.administrative.events.ContainerHierarchyDetail;
import com.krishagni.catissueplus.core.administrative.events.ContainerQueryCriteria;
import com.krishagni.catissueplus.core.administrative.events.ContainerReplicationDetail;
import com.krishagni.catissueplus.core.administrative.events.ContainerReplicationDetail.DestinationDetail;
import com.krishagni.catissueplus.core.administrative.events.ReservePositionsOp;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerPositionDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerSummary;
import com.krishagni.catissueplus.core.administrative.events.StorageLocationSummary;
import com.krishagni.catissueplus.core.administrative.events.TenantDetail;
import com.krishagni.catissueplus.core.administrative.events.VacantPositionsOp;
import com.krishagni.catissueplus.core.administrative.repository.StorageContainerListCriteria;
import com.krishagni.catissueplus.core.administrative.services.ContainerMapExporter;
import com.krishagni.catissueplus.core.administrative.services.ContainerSelectionStrategy;
import com.krishagni.catissueplus.core.administrative.services.ContainerSelectionStrategyFactory;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTaskManager;
import com.krishagni.catissueplus.core.administrative.services.StorageContainerService;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenResolver;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.RollbackTransaction;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.ErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.ExportedFileDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.LabelGenerator;
import com.krishagni.catissueplus.core.common.service.ObjectStateParamsResolver;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class StorageContainerServiceImpl implements StorageContainerService, ObjectStateParamsResolver, InitializingBean {
	private DaoFactory daoFactory;
	
	private StorageContainerFactory containerFactory;
	
	private ContainerMapExporter mapExporter;

	private LabelGenerator nameGenerator;

	private SpecimenResolver specimenResolver;

	private ContainerSelectionStrategyFactory selectionStrategyFactory;

	private ScheduledTaskManager taskManager;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public StorageContainerFactory getContainerFactory() {
		return containerFactory;
	}

	public void setContainerFactory(StorageContainerFactory containerFactory) {
		this.containerFactory = containerFactory;
	}
	
	public void setMapExporter(ContainerMapExporter mapExporter) {
		this.mapExporter = mapExporter;
	}

	public void setNameGenerator(LabelGenerator nameGenerator) {
		this.nameGenerator = nameGenerator;
	}

	public void setSpecimenResolver(SpecimenResolver specimenResolver) {
		this.specimenResolver = specimenResolver;
	}

	public void setSelectionStrategyFactory(ContainerSelectionStrategyFactory selectionStrategyFactory) {
		this.selectionStrategyFactory = selectionStrategyFactory;
	}

	public void setTaskManager(ScheduledTaskManager taskManager) {
		this.taskManager = taskManager;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<StorageContainerSummary>> getStorageContainers(RequestEvent<StorageContainerListCriteria> req) {
		try {			
			StorageContainerListCriteria crit = addContainerListCriteria(req.getPayload());
			List<StorageContainer> containers = daoFactory.getStorageContainerDao().getStorageContainers(crit);
			List<StorageContainerSummary> result = StorageContainerSummary.from(containers, crit.includeChildren());
			return ResponseEvent.response(result);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Long> getStorageContainersCount(RequestEvent<StorageContainerListCriteria> req) {
		try {
			StorageContainerListCriteria crit = addContainerListCriteria(req.getPayload());
			return ResponseEvent.response(daoFactory.getStorageContainerDao().getStorageContainersCount(crit));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<StorageContainerDetail> getStorageContainer(RequestEvent<ContainerQueryCriteria> req) {
		try {		
			StorageContainer container = getContainer(req.getPayload());						
			AccessCtrlMgr.getInstance().ensureReadContainerRights(container);
			return ResponseEvent.response(StorageContainerDetail.from(container));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<StorageContainerPositionDetail>> getOccupiedPositions(RequestEvent<Long> req) {
		try {
			StorageContainer container = getContainer(req.getPayload(), null);
			AccessCtrlMgr.getInstance().ensureReadContainerRights(container);
			return ResponseEvent.response(StorageContainerPositionDetail.from(container.getOccupiedPositions()));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<StorageContainerDetail> createStorageContainer(RequestEvent<StorageContainerDetail> req) {
		try {
			StorageContainerDetail input = req.getPayload();			
			StorageContainer container = containerFactory.createStorageContainer(input);
			AccessCtrlMgr.getInstance().ensureCreateContainerRights(container);
			
			ensureUniqueConstraints(null, container);
			container.validateRestrictions();			
			daoFactory.getStorageContainerDao().saveOrUpdate(container, true);
			return ResponseEvent.response(StorageContainerDetail.from(container));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<StorageContainerDetail> updateStorageContainer(RequestEvent<StorageContainerDetail> req) {
		return updateStorageContainer(req, false);
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<StorageContainerDetail> patchStorageContainer(RequestEvent<StorageContainerDetail> req) {
		return updateStorageContainer(req, true);
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> isAllowed(RequestEvent<TenantDetail> req) {
		try {
			TenantDetail detail = req.getPayload();

			StorageContainer container = getContainer(detail.getContainerId(), detail.getContainerName());
			AccessCtrlMgr.getInstance().ensureReadContainerRights(container);
			
			CollectionProtocol cp = new CollectionProtocol();
			cp.setId(detail.getCpId());
			String specimenClass = detail.getSpecimenClass();
			String type = detail.getSpecimenType();
			boolean isAllowed = container.canContainSpecimen(cp, specimenClass, type);

			if (!isAllowed) {
				return ResponseEvent.userError(
						StorageContainerErrorCode.CANNOT_HOLD_SPECIMEN, 
						container.getName(), 
						Specimen.getDesc(specimenClass, type));
			} else {
				return ResponseEvent.response(isAllowed);
			}
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ExportedFileDetail> exportMap(RequestEvent<ContainerQueryCriteria> req) {
		try {
			StorageContainer container = getContainer(req.getPayload());						
			AccessCtrlMgr.getInstance().ensureReadContainerRights(container);
			File file = mapExporter.exportToFile(container);
			return ResponseEvent.response(new ExportedFileDetail(container.getName(), file));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<StorageContainerPositionDetail>> assignPositions(RequestEvent<AssignPositionsOp> req) {
		try {
			AssignPositionsOp op = req.getPayload();
			StorageContainer container = getContainer(op.getContainerId(), op.getContainerName());
			
			List<StorageContainerPosition> positions = new ArrayList<StorageContainerPosition>();
			for (StorageContainerPositionDetail posDetail : op.getPositions()) {
				positions.add(createPosition(container, posDetail, op.getVacateOccupant()));
			}
			
			container.assignPositions(positions, op.getVacateOccupant());
			daoFactory.getStorageContainerDao().saveOrUpdate(container, true);
			return ResponseEvent.response(StorageContainerPositionDetail.from(container.getOccupiedPositions()));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<Long> req) {
		try {
			StorageContainer existing = getContainer(req.getPayload(), null);
			return ResponseEvent.response(existing.getDependentEntities());
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<StorageContainerDetail> deleteStorageContainer(RequestEvent<Long> req) {
		try {
			StorageContainer existing = getContainer(req.getPayload(), null);
			AccessCtrlMgr.getInstance().ensureDeleteContainerRights(existing);
			existing.delete();
			return ResponseEvent.response(StorageContainerDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> replicateStorageContainer(RequestEvent<ContainerReplicationDetail> req) {
		try {
			ContainerReplicationDetail replDetail = req.getPayload();
			StorageContainer srcContainer = getContainer(
					replDetail.getSourceContainerId(), 
					replDetail.getSourceContainerName(), 
					StorageContainerErrorCode.SRC_ID_OR_NAME_REQ);
			
			for (DestinationDetail dest : replDetail.getDestinations()) {
				replicateContainer(srcContainer, dest);
			}

			return ResponseEvent.response(true);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<StorageContainerSummary>> createContainerHierarchy(RequestEvent<ContainerHierarchyDetail> req) {
		ContainerHierarchyDetail input = req.getPayload();
		List<StorageContainer> containers = new ArrayList<>();

		try {
			StorageContainer container = containerFactory.createStorageContainer("dummyName", input);
			AccessCtrlMgr.getInstance().ensureCreateContainerRights(container);
			container.validateRestrictions();

			for (int i = 1; i <= input.getNumOfContainers(); i++) {
				StorageContainer cloned = null;
				if (i == 1) {
					cloned = container;
				} else {
					cloned = container.copy();
					setPosition(cloned);
				}

				generateName(cloned);
				ensureUniqueConstraints(null, cloned);
				
				createContainerHierarchy(cloned.getType().getCanHold(), cloned);
				daoFactory.getStorageContainerDao().saveOrUpdate(cloned);
				containers.add(cloned);
			}
			
			return ResponseEvent.response(StorageContainerDetail.from(containers));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<StorageContainerSummary>> createMultipleContainers(RequestEvent<List<StorageContainerDetail>> req) {
		try {
			List<StorageContainerSummary> result = new ArrayList<>();

			for (StorageContainerDetail detail : req.getPayload()) {
				if (StringUtils.isNotBlank(detail.getTypeName()) || detail.getTypeId() != null) {
					detail.setName("dummy");
				}

				StorageContainer container = containerFactory.createStorageContainer(detail);
				AccessCtrlMgr.getInstance().ensureCreateContainerRights(container);
				if (container.getType() != null) {
					generateName(container);
				}

				ensureUniqueConstraints(null, container);
				container.validateRestrictions();
				daoFactory.getStorageContainerDao().saveOrUpdate(container);
				result.add(StorageContainerSummary.from(container));
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
	public ResponseEvent<List<StorageLocationSummary>> reservePositions(RequestEvent<ReservePositionsOp> req) {
		long t1 = System.currentTimeMillis();
		try {
			ReservePositionsOp op = req.getPayload();
			if (StringUtils.isNotBlank(op.getReservationToCancel())) {
				cancelReservation(new RequestEvent<>(op.getReservationToCancel()));
			}

			String reservationId = UUID.randomUUID().toString();
			Date reservationTime = Calendar.getInstance().getTime();

			Long cpId = op.getCpId();
			CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getById(cpId);
			if (cp == null) {
				throw OpenSpecimenException.userError(CpErrorCode.NOT_FOUND, cpId);
			}

			if (StringUtils.isBlank(cp.getContainerSelectionStrategy())) {
				return ResponseEvent.response(Collections.emptyList());
			}

			ContainerSelectionStrategy strategy = selectionStrategyFactory.getStrategy(cp.getContainerSelectionStrategy());
			if (strategy == null) {
				throw OpenSpecimenException.userError(CpErrorCode.INV_CONT_SEL_STRATEGY, cp.getContainerSelectionStrategy());
			}

			List<StorageContainerPosition> reservedPositions = new ArrayList<>();
			for (TenantDetail detail : op.getTenants()) {
				detail.setCpId(cpId);

				boolean allAllocated = false;
				while (!allAllocated) {
					long t2 = System.currentTimeMillis();
					StorageContainer container = strategy.getContainer(detail, cp.getAliquotsInSameContainer());
					if (container == null) {
						ResponseEvent<List<StorageLocationSummary>> resp = new ResponseEvent<>(Collections.emptyList());
						resp.setRollback(true);
						return resp;
					}

					int numPositions = detail.getNumOfAliquots();
					if (numPositions <= 0) {
						numPositions = 1;
					}

					while (numPositions != 0) {
						StorageContainerPosition pos = container.nextAvailablePosition(true);
						if (pos == null) {
							break;
						}

						pos.setReservationId(reservationId);
						pos.setReservationTime(reservationTime);
						container.addPosition(pos);
						reservedPositions.add(pos);
						--numPositions;
					}

					if (numPositions == 0) {
						allAllocated = true;
					} else {
						detail.setNumOfAliquots(numPositions);
					}

					System.err.println("***** Allocation time: " + (System.currentTimeMillis() - t2) + " ms");
				}
			}

			return ResponseEvent.response(StorageLocationSummary.from(reservedPositions));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		} finally {
			System.err.println("***** Call time: " + (System.currentTimeMillis() - t1) + " ms");
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Integer> cancelReservation(RequestEvent<String> req) {
		try {
			int vacatedPositions = daoFactory.getStorageContainerDao()
				.deleteReservedPositions(Collections.singletonList(req.getPayload()));
			return ResponseEvent.response(vacatedPositions);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<StorageContainerSummary> getAncestorsHierarchy(RequestEvent<ContainerQueryCriteria> req) {
		try {
			StorageContainer container = getContainer(req.getPayload());
			AccessCtrlMgr.getInstance().ensureReadContainerRights(container);

			StorageContainerSummary summary = null;
			if (container.getParentContainer() == null) {
				summary = new StorageContainerSummary();
				summary.setId(container.getId());
				summary.setName(container.getName());
				summary.setNoOfRows(container.getNoOfRows());
				summary.setNoOfColumns(container.getNoOfColumns());
			} else {
				summary = daoFactory.getStorageContainerDao().getAncestorsHierarchy(container.getId());
			}

			return ResponseEvent.response(summary);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<StorageContainerSummary>> getChildContainers(RequestEvent<ContainerQueryCriteria> req) {
		try {
			StorageContainer container = getContainer(req.getPayload());
			AccessCtrlMgr.getInstance().ensureReadContainerRights(container);
			return ResponseEvent.response(
				daoFactory.getStorageContainerDao().getChildContainers(container.getId(), container.getNoOfColumns()));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@RollbackTransaction
	public ResponseEvent<List<StorageLocationSummary>> getVacantPositions(RequestEvent<VacantPositionsOp> req) {
		try {
			VacantPositionsOp detail = req.getPayload();
			StorageContainer container = getContainer(detail.getContainerId(), detail.getContainerName());
			AccessCtrlMgr.getInstance().ensureReadContainerRights(container);

			int numPositions = detail.getRequestedPositions();
			if (numPositions <= 0) {
				numPositions = 1;
			}

			List<StorageContainerPosition> vacantPositions = new ArrayList<>();
			for (int i = 0; i < numPositions; ++i) {
				StorageContainerPosition position = null;
				if (i == 0) {
					if (StringUtils.isNotBlank(detail.getStartRow()) && StringUtils.isNotBlank(detail.getStartColumn())) {
						position = container.nextAvailablePosition(detail.getStartRow(), detail.getStartColumn());
					} else if (detail.getStartPosition() > 0) {
						position = container.nextAvailablePosition(detail.getStartPosition());
					} else {
						position = container.nextAvailablePosition();
					}
				} else {
					position = container.nextAvailablePosition(true);
				}

				if (position == null) {
					throw OpenSpecimenException.userError(StorageContainerErrorCode.NO_FREE_SPACE, container.getName());
				}

				container.addPosition(position);
				vacantPositions.add(position);
			}

			return ResponseEvent.response(
				vacantPositions.stream()
					.map(StorageLocationSummary::from)
					.collect(Collectors.toList()));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	public String getObjectName() {
		return "container";
	}

	@Override
	@PlusTransactional
	public Map<String, Object> resolve(String key, Object value) {
		if (key.equals("id")) {
			value = Long.valueOf(value.toString());
		}

		return daoFactory.getStorageContainerDao().getContainerIds(key, value);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		taskManager.scheduleWithFixedDelay(
			new Runnable() {
				@Override
				@PlusTransactional
				public void run() {
					try {
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.MINUTE, -5);
						daoFactory.getStorageContainerDao().deleteReservedPositionsOlderThan(cal.getTime());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, 5
		);
	}

	private StorageContainerListCriteria addContainerListCriteria(StorageContainerListCriteria crit) {
		Set<Long> siteIds = AccessCtrlMgr.getInstance().getReadAccessContainerSites();
		if (siteIds != null && siteIds.isEmpty()) {
			throw OpenSpecimenException.userError(RbacErrorCode.ACCESS_DENIED);
		}

		if (CollectionUtils.isNotEmpty(crit.cpIds())) {
			//
			// TODO: what if cp site IDs is empty because of invalid cp ids
			// return error
			//
			Set<Long> cpSiteIds = new HashSet<Long>(daoFactory.getCollectionProtocolDao().getSiteIdsByCpIds(crit.cpIds()));	
			if (siteIds == null) {
				siteIds = cpSiteIds;
			} else {
				siteIds =new HashSet<Long>(CollectionUtils.intersection(siteIds, cpSiteIds));
			}
		}

		if (siteIds != null) {
			crit.siteIds(siteIds);
		}

		return crit;
	}

	private StorageContainer getContainer(ContainerQueryCriteria crit) {
		return getContainer(crit.getId(), crit.getName());
	}
	
	private StorageContainer getContainer(Long id, String name) {
		return getContainer(id, name, StorageContainerErrorCode.ID_OR_NAME_REQ);
	}
	
	private StorageContainer getContainer(Long id, String name, ErrorCode requiredErrCode) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		StorageContainer container = null;
		if (id != null) {
			container = daoFactory.getStorageContainerDao().getById(id);
			if (container == null) {
				ose.addError(StorageContainerErrorCode.NOT_FOUND, id);
			}
		} else if (StringUtils.isNotBlank(name)) {
			container = daoFactory.getStorageContainerDao().getByName(name);
			if (container == null) {
				ose.addError(StorageContainerErrorCode.NOT_FOUND, name);
			}
		} else if (requiredErrCode != null) {
			ose.addError(requiredErrCode);
		}
		
		ose.checkAndThrow();
		return container;
	}

	private ResponseEvent<StorageContainerDetail> updateStorageContainer(RequestEvent<StorageContainerDetail> req, boolean partial) {
		try {
			StorageContainerDetail input = req.getPayload();			
			StorageContainer existing = getContainer(input.getId(), input.getName());			
			AccessCtrlMgr.getInstance().ensureUpdateContainerRights(existing);			
			
			input.setId(existing.getId());
			StorageContainer container = null;
			if (partial) {
				container = containerFactory.createStorageContainer(existing, input);
			} else {
				container = containerFactory.createStorageContainer(input); 
			}
			
			ensureUniqueConstraints(existing, container);			
			existing.update(container);			
			daoFactory.getStorageContainerDao().saveOrUpdate(existing, true);
			return ResponseEvent.response(StorageContainerDetail.from(existing));			
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}		
	}
	
	private void ensureUniqueConstraints(StorageContainer existing, StorageContainer newContainer) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		if (!isUniqueName(existing, newContainer)) {
			ose.addError(StorageContainerErrorCode.DUP_NAME, newContainer.getName());
		}
		
		if (!isUniqueBarcode(existing, newContainer)) {
			ose.addError(StorageContainerErrorCode.DUP_BARCODE);
		}
		
		ose.checkAndThrow();
	}
	
	private boolean isUniqueName(StorageContainer existingContainer, StorageContainer newContainer) {
		if (existingContainer != null && existingContainer.getName().equals(newContainer.getName())) {
			return true;
		}
		
		return isUniqueName(newContainer.getName());
	}
		
	private boolean isUniqueName(String name) {
		StorageContainer container = daoFactory.getStorageContainerDao().getByName(name);
		return container == null;
	}
	
	private boolean isUniqueBarcode(StorageContainer existingContainer, StorageContainer newContainer) {
		if (StringUtils.isBlank(newContainer.getBarcode())) {
			return true;
		}
		
		if (existingContainer != null && newContainer.getBarcode().equals(existingContainer.getBarcode())) {
			return true;
		}
		
		StorageContainer container = daoFactory.getStorageContainerDao().getByBarcode(newContainer.getBarcode());
		return container == null;
	}
	
	private StorageContainerPosition createPosition(StorageContainer container, StorageContainerPositionDetail pos, boolean vacateOccupant) {
		if (StringUtils.isBlank(pos.getPosOne()) ^ StringUtils.isBlank(pos.getPosTwo())) {
			throw OpenSpecimenException.userError(StorageContainerErrorCode.INVALID_POSITIONS);
		}
		
		String entityType = pos.getOccuypingEntity();
		if (StringUtils.isBlank(entityType)) {
			throw OpenSpecimenException.userError(StorageContainerErrorCode.INVALID_ENTITY_TYPE, "none");
		}
		
		if (StringUtils.isBlank(pos.getOccupyingEntityName()) && pos.getOccupyingEntityId() == null) {
			throw OpenSpecimenException.userError(StorageContainerErrorCode.OCCUPYING_ENTITY_ID_OR_NAME_REQUIRED);
		}
		
		if (entityType.equalsIgnoreCase("specimen")) {
			return createSpecimenPosition(container, pos, vacateOccupant);
		} else if (entityType.equalsIgnoreCase("container")) {
			return createChildContainerPosition(container, pos);
		}
		
		throw OpenSpecimenException.userError(StorageContainerErrorCode.INVALID_ENTITY_TYPE, entityType);
	}
	
	private StorageContainerPosition createSpecimenPosition(
			StorageContainer container,
			StorageContainerPositionDetail pos,
			boolean vacateOccupant) {


		Specimen specimen = getSpecimen(pos);
		AccessCtrlMgr.getInstance().ensureCreateOrUpdateSpecimenRights(specimen, false);
		
		StorageContainerPosition position = null;
		if (StringUtils.isBlank(pos.getPosOne()) || StringUtils.isBlank(pos.getPosTwo())) {
			position = container.createVirtualPosition();
			position.setOccupyingSpecimen(specimen);
			return position;
		}

		if (!container.canContain(specimen)) {
			throw OpenSpecimenException.userError(
					StorageContainerErrorCode.CANNOT_HOLD_SPECIMEN, 
					container.getName(), 
					specimen.getLabelOrDesc());
		}
		
		if (!container.canSpecimenOccupyPosition(specimen.getId(), pos.getPosOne(), pos.getPosTwo(), vacateOccupant)) {
			throw OpenSpecimenException.userError(StorageContainerErrorCode.NO_FREE_SPACE, container.getName());
		}
		
		position = container.createPosition(pos.getPosOne(), pos.getPosTwo());
		position.setOccupyingSpecimen(specimen);
		return position;		
	}

	private Specimen getSpecimen(StorageContainerPositionDetail pos) {
		return specimenResolver.getSpecimen(
			pos.getOccupyingEntityId(),
			pos.getCpShortTitle(),
			pos.getOccupyingEntityName()
		);
	}
	
	private StorageContainerPosition createChildContainerPosition(
			StorageContainer container, 
			StorageContainerPositionDetail pos) {
		
		StorageContainer childContainer = getContainer(pos.getOccupyingEntityId(), pos.getOccupyingEntityName());
		AccessCtrlMgr.getInstance().ensureUpdateContainerRights(childContainer);
		if (!container.canContain(childContainer)) {
			throw OpenSpecimenException.userError(
					StorageContainerErrorCode.CANNOT_HOLD_CONTAINER, 
					container.getName(), 
					childContainer.getName());
		}
		
		if (!container.canContainerOccupyPosition(childContainer.getId(), pos.getPosOne(), pos.getPosTwo())) {
			throw OpenSpecimenException.userError(StorageContainerErrorCode.NO_FREE_SPACE, container.getName());
		}
		
		StorageContainerPosition position = container.createPosition(pos.getPosOne(), pos.getPosTwo());
		position.setOccupyingContainer(childContainer);
		return position;
	}
	
	private void replicateContainer(StorageContainer srcContainer, DestinationDetail dest) {
		StorageContainerDetail detail = new StorageContainerDetail();
		detail.setName(dest.getName());
		detail.setSiteName(dest.getSiteName());
		
		StorageLocationSummary storageLocation = new StorageLocationSummary();
		storageLocation.setId(dest.getParentContainerId());
		storageLocation.setName(dest.getParentContainerName());
		storageLocation.setPositionX(dest.getPosOne());
		storageLocation.setPositionY(dest.getPosTwo());
		storageLocation.setPosition(dest.getPosition());
		detail.setStorageLocation(storageLocation);
		
		StorageContainer replica = containerFactory.createStorageContainer(getContainerCopy(srcContainer), detail);
		AccessCtrlMgr.getInstance().ensureCreateContainerRights(replica);
		
		ensureUniqueConstraints(null, replica);
		replica.validateRestrictions();
		daoFactory.getStorageContainerDao().saveOrUpdate(replica);
		
		if (replica.getParentContainer() != null && replica.getPosition() != null) {
			replica.getParentContainer().addPosition(replica.getPosition());
		}
	}

	private void createContainerHierarchy(ContainerType containerType, StorageContainer parentContainer) {
		if (containerType == null) {
			return;
		}
		
		StorageContainer container = containerFactory.createStorageContainer("dummyName", containerType, parentContainer);
		int noOfContainers = parentContainer.getNoOfRows() * parentContainer.getNoOfColumns();
		for (int i = 1; i <= noOfContainers; i++) {
			StorageContainer cloned = null;
			if (i == 1) {
				cloned = container;
			} else {
				cloned = container.copy();
				setPosition(cloned);
			}

			generateName(cloned);
			parentContainer.addChildContainer(cloned);
			createContainerHierarchy(containerType.getCanHold(), cloned);
		}
	}

	private void generateName(StorageContainer container) {
		ContainerType type = container.getType();
		String name = nameGenerator.generateLabel(type.getNameFormat(), container);
		if (StringUtils.isBlank(name)) {
			throw OpenSpecimenException.userError(
				StorageContainerErrorCode.INCORRECT_NAME_FMT,
				type.getNameFormat(),
				type.getName());
		}

		container.setName(name);
	}

	private void setPosition(StorageContainer container) {
		StorageContainer parentContainer = container.getParentContainer();
		if (parentContainer == null) {
			return;
		}
		
		StorageContainerPosition position = parentContainer.nextAvailablePosition(true);
		if (position == null) {
			throw OpenSpecimenException.userError(StorageContainerErrorCode.NO_FREE_SPACE, parentContainer.getName());
		} 
		
		position.setOccupyingContainer(container);
		container.setPosition(position);
	}

	private StorageContainer getContainerCopy(StorageContainer source) {
		StorageContainer copy = new StorageContainer();
		copy.setTemperature(source.getTemperature());
		copy.setNoOfColumns(source.getNoOfColumns());
		copy.setNoOfRows(source.getNoOfRows());
		copy.setColumnLabelingScheme(source.getColumnLabelingScheme());
		copy.setRowLabelingScheme(source.getRowLabelingScheme());
		copy.setComments(source.getComments());
		copy.setAllowedSpecimenClasses(new HashSet<String>(source.getAllowedSpecimenClasses()));		
		copy.setAllowedSpecimenTypes(new HashSet<String>(source.getAllowedSpecimenTypes()));
		copy.setAllowedCps(new HashSet<CollectionProtocol>(source.getAllowedCps()));
		copy.setCompAllowedSpecimenClasses(copy.computeAllowedSpecimenClasses());
		copy.setCompAllowedSpecimenTypes(copy.computeAllowedSpecimenTypes());
		copy.setCompAllowedCps(copy.computeAllowedCps());
		copy.setStoreSpecimenEnabled(source.isStoreSpecimenEnabled());
		copy.setCreatedBy(AuthUtil.getCurrentUser());
		return copy;
	}
}
