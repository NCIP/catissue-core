package com.krishagni.catissueplus.core.administrative.repository;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.common.repository.Dao;



public interface ContainerDao extends Dao<StorageContainer>{
	StorageContainer getContainer(String containerName);
}
