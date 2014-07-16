package com.krishagni.catissueplus.rest.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.de.events.FileDetail;
import com.krishagni.catissueplus.core.de.events.FileDetailEvent;
import com.krishagni.catissueplus.core.de.events.FileUploadedEvent;
import com.krishagni.catissueplus.core.de.events.ReqFileDetailEvent;
import com.krishagni.catissueplus.core.de.events.UploadFileEvent;
import com.krishagni.catissueplus.core.de.services.FormService;

import edu.common.dynamicextensions.nutility.IoUtil;

@Controller
@RequestMapping("/form-files")
public class FormFilesController {
	@Autowired
	private FormService formSvc;
	
	@RequestMapping(method = RequestMethod.POST,  produces="text/plain")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public String uploadFile(@PathVariable("file") MultipartFile file) {
		UploadFileEvent req = new UploadFileEvent();
		req.setFile(file);
		
		FileUploadedEvent resp = formSvc.uploadFile(req);
		if (resp.getStatus() == EventStatus.OK) {
			return new Gson().toJson(resp.getFile());
		}

		return null;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void downloadFile(
			@RequestParam(value="formId", required=true) Long formId,
			@RequestParam(value="recordId", required=true) Long recordId,
			@RequestParam(value="ctrlName", required=true) String ctrlName,
			HttpServletResponse response) {
		
		ReqFileDetailEvent req = new ReqFileDetailEvent();
		req.setFormId(formId);
		req.setRecordId(recordId);
		req.setCtrlName(ctrlName);
		
		FileDetailEvent resp = formSvc.getFileDetail(req);
		if (resp.getStatus() != EventStatus.OK) {
			return;
		}
		
		FileDetail file = resp.getFileDetail();
		response.setContentType(file.getContentType());
		response.setHeader("Content-Disposition", "attachment;filename=" + file.getFilename());
			
		InputStream in = null;
		try {
			in = new FileInputStream(file.getPath());
			IoUtil.copy(in, response.getOutputStream());
		} catch (IOException e) {
			throw new RuntimeException("Error sending file", e);
		} finally {
			IoUtil.close(in);
		}
	}
}
