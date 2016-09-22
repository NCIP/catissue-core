package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.AssignPositionsOp;
import com.krishagni.catissueplus.core.administrative.events.ContainerHierarchyDetail;
import com.krishagni.catissueplus.core.administrative.events.ContainerQueryCriteria;
import com.krishagni.catissueplus.core.administrative.events.ContainerReplicationDetail;
import com.krishagni.catissueplus.core.administrative.events.ReservePositionsOp;
import com.krishagni.catissueplus.core.administrative.events.TenantDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerPositionDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerSummary;
import com.krishagni.catissueplus.core.administrative.events.StorageLocationSummary;
import com.krishagni.catissueplus.core.administrative.events.VacantPositionsOp;
import com.krishagni.catissueplus.core.administrative.repository.StorageContainerListCriteria;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.ExportedFileDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface StorageContainerService {
	public ResponseEvent<List<StorageContainerSummary>> getStorageContainers(RequestEvent<StorageContainerListCriteria> req);
	
	public ResponseEvent<Long> getStorageContainersCount(RequestEvent<StorageContainerListCriteria> req);
	
	public ResponseEvent<StorageContainerDetail> getStorageContainer(RequestEvent<ContainerQueryCriteria> req);
	
	public ResponseEvent<List<StorageContainerPositionDetail>> getOccupiedPositions(RequestEvent<Long> req);
	
	public ResponseEvent<StorageContainerDetail> createStorageContainer(RequestEvent<StorageContainerDetail> req);
	
	public ResponseEvent<StorageContainerDetail> updateStorageContainer(RequestEvent<StorageContainerDetail> req);
	
	public ResponseEvent<StorageContainerDetail> patchStorageContainer(RequestEvent<StorageContainerDetail> req);
	
	public ResponseEvent<Boolean> isAllowed(RequestEvent<TenantDetail> req);
	
	public ResponseEvent<ExportedFileDetail> exportMap(RequestEvent<ContainerQueryCriteria> req);
	
	public ResponseEvent<List<StorageContainerPositionDetail>> assignPositions(RequestEvent<AssignPositionsOp> req);
		
	public ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<Long> req);
	
	public ResponseEvent<StorageContainerDetail> deleteStorageContainer(RequestEvent<Long> req);
	
	public ResponseEvent<Boolean> replicateStorageContainer(RequestEvent<ContainerReplicationDetail> req);

	public ResponseEvent<List<StorageContainerSummary>> createContainerHierarchy(RequestEvent<ContainerHierarchyDetail> req);

	public ResponseEvent<List<StorageContainerSummary>> createMultipleContainers(RequestEvent<List<StorageContainerDetail>> req);

	//
	// Auto allocation and reservation
	//
	public ResponseEvent<List<StorageLocationSummary>> reservePositions(RequestEvent<ReservePositionsOp> req);

	public ResponseEvent<Integer> cancelReservation(RequestEvent<String> req);

	//
	// Mostly present to implement UI tree for faster access
	//

	//
	// Useful for displaying UI freezer tree
	// Gets all the sibling containers, ancestors and their siblings all the way up to root container.
	// For example: [F, [R1, R2 [B1, B2*, B3], R3, ... Rn] is the
	// result when input container ID is B2
	//
	public ResponseEvent<StorageContainerSummary> getAncestorsHierarchy(RequestEvent<ContainerQueryCriteria> req);

	public ResponseEvent<List<StorageContainerSummary>> getChildContainers(RequestEvent<ContainerQueryCriteria> req);

	public ResponseEvent<List<StorageLocationSummary>> getVacantPositions(RequestEvent<VacantPositionsOp> req);
}
