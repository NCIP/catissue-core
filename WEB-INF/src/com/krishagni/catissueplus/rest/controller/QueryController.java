package com.krishagni.catissueplus.rest.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.de.events.ExecuteQueryEvent;
import com.krishagni.catissueplus.core.de.events.ExportDataFileEvent;
import com.krishagni.catissueplus.core.de.events.ExportQueryDataEvent;
import com.krishagni.catissueplus.core.de.events.QueryDataExportedEvent;
import com.krishagni.catissueplus.core.de.events.QueryExecutedEvent;
import com.krishagni.catissueplus.core.de.events.ReqExportDataFileEvent;
import com.krishagni.catissueplus.core.de.services.QueryService;

import edu.common.dynamicextensions.nutility.IoUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/query")
public class QueryController {
	
	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@Autowired
	private QueryService querySvc;
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public QueryExecutedEvent executeQuery(@RequestBody ExecuteQueryEvent req) {
		req.setSessionDataBean(getSession());
		return querySvc.executeQuery(req);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/export")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public QueryDataExportedEvent exportQueryData(@RequestBody ExportQueryDataEvent req) {
		req.setSessionDataBean(getSession());
		return querySvc.exportQueryData(req);
	}	
	
	@RequestMapping(method = RequestMethod.GET, value="/export")
	@ResponseStatus(HttpStatus.OK)
	public void downloadExportDataFile(
			@RequestParam(value="fileId", required=true) String fileId,
			HttpServletResponse response) {
		
		ReqExportDataFileEvent req = new ReqExportDataFileEvent();
		req.setFileId(fileId);
		
		ExportDataFileEvent resp = querySvc.getExportDataFile(req);
		if (resp.getStatus() != EventStatus.OK) {
			return;
		}
		
		File file = resp.getFile();
		response.setContentType("text/csv;");
		response.setHeader("Content-Disposition", "attachment;filename=QueryResults.csv");
			
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			IoUtil.copy(in, response.getOutputStream());
		} catch (IOException e) {
			throw new RuntimeException("Error sending file", e);
		} finally {
			IoUtil.close(in);
		}
	}
	
	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(
				Constants.SESSION_DATA);
	}
}
																																																																																																																																						