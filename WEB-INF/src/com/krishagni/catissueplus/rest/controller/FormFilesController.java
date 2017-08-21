package com.krishagni.catissueplus.rest.controller;

import java.io.File;

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
import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.catissueplus.core.de.events.FileDetail;
import com.krishagni.catissueplus.core.de.events.GetFileDetailOp;
import com.krishagni.catissueplus.core.de.services.FormService;

import edu.common.dynamicextensions.nutility.DeConfiguration;

@Controller
@RequestMapping("/form-files")
public class FormFilesController {
	@Autowired
	private FormService formSvc;
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public FileDetail uploadFile(@PathVariable("file") MultipartFile file) {
		return response(formSvc.uploadFile(request(file)));
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void downloadFile(
		@RequestParam(value = "formId", required = true)
		Long formId,

		@RequestParam(value = "recordId", required = true)
		Long recordId,

		@RequestParam(value = "ctrlName", required = true)
		String ctrlName,

		HttpServletResponse response) {
		
		GetFileDetailOp req = new GetFileDetailOp();
		req.setFormId(formId);
		req.setRecordId(recordId);
		req.setCtrlName(ctrlName);
		
		FileDetail file = response(formSvc.getFileDetail(request(req)));
		Utility.sendToClient(response, file.getFilename(), file.getContentType(), new File(file.getPath()));
	}

	@RequestMapping(method = RequestMethod.GET, value="/{fileId}")
	@ResponseStatus(HttpStatus.OK)
	public void downloadFile(
		@PathVariable("fileId")
		String fileId,

		@RequestParam(value = "contentType", required = false)
		String contentType,

		@RequestParam(value = "filename", required = false)
		String filename,

		HttpServletResponse response) {

		File file = new File(DeConfiguration.getInstance().fileUploadDir() + File.separator + fileId);
		if (!file.exists()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		Utility.sendToClient(response, filename, contentType, file);
	}

	private <T> RequestEvent<T> request(T payload) {
		return new RequestEvent<T>(payload);				
	}

	private <T> T response(ResponseEvent<T> resp) {
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
}
