package com.krishagni.catissueplus.core.de.events;

import org.springframework.web.multipart.MultipartFile;

public class UploadFileEvent {

	private MultipartFile file;

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public MultipartFile getFile () {
		return file;
	}
}
