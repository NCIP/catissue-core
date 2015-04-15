package com.krishagni.catissueplus.rest.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.importer.events.ImportDetail;
import com.krishagni.catissueplus.core.importer.events.ImportJobDetail;
import com.krishagni.catissueplus.core.importer.services.ImportService;

@Controller
@RequestMapping("/import-jobs")
public class BulkObjectImportController {
	
	@Autowired
	private ImportService importSvc;

	@RequestMapping(method = RequestMethod.POST, value="/input-file")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody		
	public Map<String, String> uploadJobInputFile(@PathVariable("file") MultipartFile file) 
	throws IOException {
		RequestEvent<InputStream> req = new RequestEvent<InputStream>(file.getInputStream());
		ResponseEvent<String> resp = importSvc.uploadImportJobFile(req);
		resp.throwErrorIfUnsuccessful();
		
		return Collections.singletonMap("fileId", resp.getPayload());
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public ImportJobDetail createImportJob(@RequestBody ImportDetail detail) {
		RequestEvent<ImportDetail> req = new RequestEvent<ImportDetail>(detail);
		ResponseEvent<ImportJobDetail> resp = importSvc.importObjects(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
}
