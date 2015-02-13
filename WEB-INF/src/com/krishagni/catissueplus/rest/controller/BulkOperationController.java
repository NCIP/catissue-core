package com.krishagni.catissueplus.rest.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

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

import com.krishagni.catissueplus.bulkoperator.events.BulkOperationDetail;
import com.krishagni.catissueplus.bulkoperator.events.ImportRecordsOpDetail;
import com.krishagni.catissueplus.bulkoperator.events.JobDetail;
import com.krishagni.catissueplus.bulkoperator.events.ListJobsCriteria;
import com.krishagni.catissueplus.bulkoperator.events.LogFileContent;
import com.krishagni.catissueplus.bulkoperator.services.BulkOperationService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/bulk-operations")
public class BulkOperationController {

	@Autowired
	private HttpServletRequest httpReq;

	@Autowired
	private BulkOperationService boService;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<BulkOperationDetail> getOperations() {
		RequestEvent<?> req = new RequestEvent<Object>();
		req.setSessionDataBean(getSession());
		
		ResponseEvent<List<BulkOperationDetail>> resp = boService.getBulkOperations(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/jobs")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<JobDetail> getJobs(
			@RequestParam(value = "start", required = false, defaultValue = "0") int start,
			@RequestParam(value = "max", required = false, defaultValue = "50") int max) {
		
		ListJobsCriteria listCrit = new ListJobsCriteria()
			.startAt(start)
			.maxResults(max);
		
		RequestEvent<ListJobsCriteria> req = new RequestEvent<ListJobsCriteria>(getSession(), listCrit);				
		ResponseEvent<List<JobDetail>> resp = boService.getJobs(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String importRecords(
			@RequestParam("name") String operationName, 
			@RequestParam("csvFile") MultipartFile csvFile) 
	throws IOException {

		File fileIn = File.createTempFile("bo-input-", ".csv");		
		csvFile.transferTo(fileIn);
		
		ImportRecordsOpDetail detail = new ImportRecordsOpDetail();
		detail.setOperationName(operationName);
		detail.setFileIn(fileIn);
		
		RequestEvent<ImportRecordsOpDetail> req = new RequestEvent<ImportRecordsOpDetail>(getSession(), detail);		
		ResponseEvent<String> resp = boService.bulkImportRecords(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/logs")
    public void downloadLogFile(@PathVariable("id") Long jobId, HttpServletResponse httpResp) {

		RequestEvent<Long> req = new RequestEvent<Long>(getSession(), jobId);
		ResponseEvent<LogFileContent> resp = boService.getLogFileContent(req);
		resp.throwErrorIfUnsuccessful();

		sendFile(resp.getPayload(), httpResp);
	}
	
	private void sendFile(LogFileContent resp, HttpServletResponse httpResp) {
		try {
			httpResp.setContentType("application/download");
			httpResp.setHeader("Content-Disposition", "attachment;filename=\""	+ resp.getFileName() + "\";");
			httpResp.setContentLength(resp.getLogFileContents().length);
			OutputStream outputStream = httpResp.getOutputStream();
			outputStream.write(resp.getLogFileContents());
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private SessionDataBean getSession() {
		return (SessionDataBean) httpReq.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
