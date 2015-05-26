package com.krishagni.catissueplus.core.biospecimen.events;

import org.springframework.web.multipart.MultipartFile;

public class ConsentFormDetail {
	private Long cprId;
	
	private MultipartFile file;

	public Long getCprId() {
		return cprId;
	}

	public void setCprId(Long cprId) {
		this.cprId = cprId;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
}
