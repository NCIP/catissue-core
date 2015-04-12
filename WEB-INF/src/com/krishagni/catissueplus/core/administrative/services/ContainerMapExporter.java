package com.krishagni.catissueplus.core.administrative.services;

import java.io.File;
import java.io.IOException;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;

public interface ContainerMapExporter {
	public File exportToFile(StorageContainer container) 
	throws IOException;
}
