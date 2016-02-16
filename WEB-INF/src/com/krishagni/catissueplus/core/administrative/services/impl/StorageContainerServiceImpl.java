package com.krishagni.catissueplus.core.administrative.services.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerFactory;
import com.krishagni.catissueplus.core.administrative.events.AssignPositionsOp;
import com.krishagni.catissueplus.core.administrative.events.ContainerQueryCriteria;
import com.krishagni.catissueplus.core.administrative.events.ContainerReplicationDetail;
import com.krishagni.catissueplus.core.administrative.events.ContainerReplicationDetail.DestinationDetail;
import com.krishagni.catissueplus.core.administrative.events.PositionTenantDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerPositionDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerSummary;
import com.krishagni.catissueplus.core.administrative.events.StorageLocationSummary;
import com.krishagni.catissueplus.core.administrative.repository.StorageContainerListCriteria;
import com.krishagni.catissueplus.core.administrative.services.ContainerMapExporter;
import com.krishagni.catissueplus.core.administrative.services.StorageContainerService;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.ErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.ExportedFileDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.ObjectStateParamsResolver;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class StorageContainerServiceImpl implements StorageContainerService, ObjectStateParamsResolver {
	private DaoFactory daoFactory;
	
	private StorageContainerFactory containerFactory;
	
	private ContainerMapExporter mapExporter;

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

	@Override
	@PlusTransactional
	public ResponseEvent<List<StorageContainerSummary>> getStorageContainers(RequestEvent<StorageContainerListCriteria> req) {
		try {			
			StorageContainerListCriteria crit = req.getPayload();
			Set<Long> siteIds = AccessCtrlMgr.getInstance().getReadAccessContainerSites();
			if (siteIds != null && siteIds.isEmpty()) {
				return ResponseEvent.userError(RbacErrorCode.ACCESS_DENIED);
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
			Long containerId = req.getPayload();			
			StorageContainer container = daoFactory.getStorageContainerDao().getById(containerId);
			if (container == null) {
				return ResponseEvent.userError(StorageContainerErrorCode.NOT_FOUND);
			}
			
			AccessCtrlMgr.getInstance().ensureReadContainerRights(container);
			Set<StorageContainerPosition> positions = container.getOccupiedPositions();
			return ResponseEvent.response(StorageContainerPositionDetail.from(positions));
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
	public ResponseEvent<Boolean> isAllowed(RequestEvent<PositionTenantDetail> req) {
		try {
			PositionTenantDetail detail = req.getPayload();
			StorageContainer container = null;
			
			if (detail.getContainerId() != null) {
				container = daoFactory.getStorageContainerDao().getById(detail.getContainerId());
			} else if (StringUtils.isNotBlank(detail.getContainerName())) {
				container = daoFactory.getStorageContainerDao().getByName(detail.getContainerName());
			}
			
			if (container == null) {
				return ResponseEvent.userError(StorageContainerErrorCode.NOT_FOUND);
			}
			
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
			StorageContainer existing = daoFactory.getStorageContainerDao().getById(req.getPayload());
			if (existing == null) {
				return ResponseEvent.userError(StorageContainerErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(existing.getDependentEntities());
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<StorageContainerDetail> deleteStorageContainer(RequestEvent<Long> req) {
		try {
			StorageContainer existing = daoFactory.getStorageContainerDao().getById(req.getPayload());
			if (existing == null) {
				return ResponseEvent.userError(StorageContainerErrorCode.NOT_FOUND);
			}
			
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
		
		String entityName = pos.getOccupyingEntityName();
		Long entityId = pos.getOccupyingEntityId();
		if (StringUtils.isBlank(entityName) && entityId == null) {
			throw OpenSpecimenException.userError(StorageContainerErrorCode.OCCUPYING_ENTITY_ID_OR_NAME_REQUIRED);
		}
		
		if (entityType.equalsIgnoreCase("specimen")) {
			return createSpecimenPosition(container, pos, entityId, entityName, vacateOccupant);
		} else if (entityType.equalsIgnoreCase("container")) {
			return createChildContainerPosition(container, pos, entityId, entityName);
		}
		
		throw OpenSpecimenException.userError(StorageContainerErrorCode.INVALID_ENTITY_TYPE, entityType);
	}
	
	private StorageContainerPosition createSpecimenPosition(
			StorageContainer container, 
			StorageContainerPositionDetail pos, 
			Long specimenId, 
			String label,
			boolean vacateOccupant) {
		
		Specimen specimen = getSpecimen(specimenId, label);
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
	
	private Specimen getSpecimen(Long specimenId, String label) {
		Specimen specimen = null;

		Object key = null;
		if (specimenId != null) {
			key = specimenId;
			specimen = daoFactory.getSpecimenDao().getById(specimenId);
		} else if (StringUtils.isNotBlank(label)) {
			key = label;
			specimen = daoFactory.getSpecimenDao().getByLabel(label);
		}

		if (key == null) {
			throw OpenSpecimenException.userError(SpecimenErrorCode.LABEL_REQUIRED);
		}
		
		if (specimen == null) {
			throw OpenSpecimenException.userError(SpecimenErrorCode.NOT_FOUND, key);
		}

		return specimen;
	}
	
	private StorageContainerPosition createChildContainerPosition(
			StorageContainer container, 
			StorageContainerPositionDetail pos, 
			Long containerId, 
			String containerName) {
		
		StorageContainer childContainer = getContainer(containerId, containerName);
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
