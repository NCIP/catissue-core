package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.AssignPositionsOp;
import com.krishagni.catissueplus.core.administrative.events.ContainerQueryCriteria;
import com.krishagni.catissueplus.core.administrative.events.ContainerReplicationDetail;
import com.krishagni.catissueplus.core.administrative.events.PositionTenantDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerPositionDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerSummary;
import com.krishagni.catissueplus.core.administrative.repository.StorageContainerListCriteria;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.ExportedFileDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface StorageContainerService {
	public ResponseEvent<List<StorageContainerSummary>> getStorageContainers(RequestEvent<StorageContainerListCriteria> req);
	
	public ResponseEvent<StorageContainerDetail> getStorageContainer(RequestEvent<ContainerQueryCriteria> req);
	
	public ResponseEvent<List<StorageContainerPositionDetail>> getOccupiedPositions(RequestEvent<Long> req);
	
	public ResponseEvent<StorageContainerDetail> createStorageContainer(RequestEvent<StorageContainerDetail> req);
	
	public ResponseEvent<StorageContainerDetail> updateStorageContainer(RequestEvent<StorageContainerDetail> req);
	
	public ResponseEvent<StorageContainerDetail> patchStorageContainer(RequestEvent<StorageContainerDetail> req);
	
	public ResponseEvent<Boolean> isAllowed(RequestEvent<PositionTenantDetail> req);
	
	public ResponseEvent<ExportedFileDetail> exportMap(RequestEvent<ContainerQueryCriteria> req);
	
	public ResponseEvent<List<StorageContainerPositionDetail>> assignPositions(RequestEvent<AssignPositionsOp> req);
		
	public ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<Long> req);
	
	public ResponseEvent<StorageContainerDetail> deleteStorageContainer(RequestEvent<Long> req);
	
	public ResponseEvent<Boolean> replicateStorageContainer(RequestEvent<ContainerReplicationDetail> req);
}
