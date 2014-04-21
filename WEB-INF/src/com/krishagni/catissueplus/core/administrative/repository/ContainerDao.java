package com.krishagni.catissueplus.core.administrative.repository;

import com.krishagni.catissueplus.core.common.repository.Dao;

import edu.wustl.catissuecore.domain.StorageContainer;


public interface ContainerDao extends Dao<StorageContainer>{

	StorageContainer getContainer(String containerName);

}
