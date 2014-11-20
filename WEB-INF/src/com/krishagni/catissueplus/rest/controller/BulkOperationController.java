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
import com.krishagni.catissueplus.bulkoperator.events.BulkOperationsEvent;
import com.krishagni.catissueplus.bulkoperator.events.BulkRecordsImportedEvent;
import com.krishagni.catissueplus.bulkoperator.events.JobDetail;
import com.krishagni.catissueplus.bulkoperator.events.JobsDetailEvent;
import com.krishagni.catissueplus.bulkoperator.events.LogFileContentEvent;
import com.krishagni.catissueplus.bulkoperator.events.ReqBulkOperationsEvent;
import com.krishagni.catissueplus.bulkoperator.events.ReqJobsDetailEvent;
import com.krishagni.catissueplus.bulkoperator.events.ReqLogFileContentEvent;
import com.krishagni.catissueplus.bulkoperator.events.BulkImportRecordsEvent;
import com.krishagni.catissueplus.bulkoperator.services.BulkOperationService;
import com.krishagni.catissueplus.core.common.events.EventStatus;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/bulk-operations")
public class BulkOperationController {

	//
	// TODO: Changed name to make it small and crisp
	//
	@Autowired
	private HttpServletRequest httpReq;

	@Autowired
	private BulkOperationService boService;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<BulkOperationDetail> getOperations() {
		ReqBulkOperationsEvent req = new ReqBulkOperationsEvent();
		BulkOperationsEvent resp = boService.getBulkOperations(req);
		
		//
		// TODO: Changed this for better error handling
		// 
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getOperations();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/jobs")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<JobDetail> getJobs(
			@RequestParam(value = "start", required = false, defaultValue = "0") int start,
			@RequestParam(value = "max", required = false, defaultValue = "50") int max) {
		ReqJobsDetailEvent req = new ReqJobsDetailEvent();
		req.setMaxRecords(max);
		req.setStartAt(start);
		req.setSessionDataBean(getSession());
		
		JobsDetailEvent resp = boService.getJobs(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getJobs();
	}
	
	//
	// TODO: uploadFile changed to importRecords
	//
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Long importRecords(@RequestParam("name") String operationName, @RequestParam("csvFile") MultipartFile csvFile) throws IOException {
		BulkImportRecordsEvent req = new BulkImportRecordsEvent();
		req.setSessionDataBean(getSession());
		req.setOperationName(operationName);
		
		File fileIn = File.createTempFile("bo-input-", ".csv");		
		csvFile.transferTo(fileIn);
		req.setFileIn(fileIn);
		
		BulkRecordsImportedEvent resp = boService.bulkImportRecords(req);
		
		fileIn.delete();
		if (resp.isSuccess()) {
			resp.raiseException();
		}
		
		//
		// TODO: I think this should be a proper job detail object
		//
		return resp.getJobId();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/logs")
    public void downloadLogFile(@PathVariable("id") Long jobId, HttpServletResponse httpServletResponse) {
		ReqLogFileContentEvent req = new ReqLogFileContentEvent();
		req.setJobId(jobId);
		req.setSessionDataBean(getSession());
		
		LogFileContentEvent resp = boService.getLogFileContent(req);
		
		if (resp.getStatus() == EventStatus.OK) {
			doDownload(resp, httpServletResponse);
		}		
	}
	
	private void doDownload(LogFileContentEvent resp, HttpServletResponse httpServletResponse) {
		try {
			httpServletResponse.setContentType("application/download");
			httpServletResponse.setHeader("Content-Disposition", "attachment;filename=\""	+ resp.getFileName() + "\";");
			httpServletResponse.setContentLength(resp.getLogFileContents().length);
			OutputStream outputStream = httpServletResponse.getOutputStream();
			outputStream.write(resp.getLogFileContents());
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private SessionDataBean getSession() {
		return (SessionDataBean) httpReq.getSession().getAttribute(
				Constants.SESSION_DATA);
	}
}
