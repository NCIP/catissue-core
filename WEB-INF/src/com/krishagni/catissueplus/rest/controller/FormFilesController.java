package com.krishagni.catissueplus.rest.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.de.events.FileDetail;
import com.krishagni.catissueplus.core.de.events.GetFileDetailOp;
import com.krishagni.catissueplus.core.de.services.FormService;

import edu.common.dynamicextensions.nutility.IoUtil;

@Controller
@RequestMapping("/form-files")
public class FormFilesController {
	@Autowired
	private FormService formSvc;
	
	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public FileDetail uploadFile(@PathVariable("file") MultipartFile file) {
		ResponseEvent<FileDetail> resp = formSvc.uploadFile(getRequestEvent(file));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void downloadFile(
			@RequestParam(value="formId", required=true) Long formId,
			@RequestParam(value="recordId", required=true) Long recordId,
			@RequestParam(value="ctrlName", required=true) String ctrlName,
			HttpServletResponse response) {
		
		GetFileDetailOp req = new GetFileDetailOp();
		req.setFormId(formId);
		req.setRecordId(recordId);
		req.setCtrlName(ctrlName);
		
		ResponseEvent<FileDetail> resp = formSvc.getFileDetail(getRequestEvent(req));
		resp.throwErrorIfUnsuccessful();

		FileDetail file = resp.getPayload();
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
	
	private <T> RequestEvent<T> getRequestEvent(T payload) {
		return new RequestEvent<T>(payload);				
	}	
}
