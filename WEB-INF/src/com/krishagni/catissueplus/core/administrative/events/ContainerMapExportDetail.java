package com.krishagni.catissueplus.core.administrative.events;

import java.io.File;

public class ContainerMapExportDetail {
	private String name;
	
	private File file;
	
	public ContainerMapExportDetail(String name, File file) {
		this.name = name;
		this.file = file;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
