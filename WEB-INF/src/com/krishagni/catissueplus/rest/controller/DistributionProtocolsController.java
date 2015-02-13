//
//package com.krishagni.catissueplus.rest.controller;
//
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetail;
//import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolQueryCriteria;
//import com.krishagni.catissueplus.core.administrative.repository.DpListCriteria;
//import com.krishagni.catissueplus.core.administrative.services.DistributionProtocolService;
//import com.krishagni.catissueplus.core.common.events.RequestEvent;
//import com.krishagni.catissueplus.core.common.events.ResponseEvent;
//
//import edu.wustl.catissuecore.util.global.Constants;
//import edu.wustl.common.beans.SessionDataBean;
//
//@Controller
//@RequestMapping("/distribution-protocols")
//public class DistributionProtocolsController {
//
//	@Autowired
//	private DistributionProtocolService distributionProtocolSvc;
//
//	@Autowired
//	private HttpServletRequest httpServletRequest;
//
//	@RequestMapping(method = RequestMethod.GET)
//	@ResponseStatus(HttpStatus.OK)
//	@ResponseBody
//	public List<DistributionProtocolDetail> getDistributionProtocols(
//			@RequestParam(value = "maxResults", required = false, defaultValue = "1000") 
//			int maxResults) {
//		
//		DpListCriteria crit = new DpListCriteria().maxResults(maxResults);		
//		ResponseEvent<List<DistributionProtocolDetail>> resp = distributionProtocolSvc.getDistributionProtocols(getRequest(crit));
//		resp.throwErrorIfUnsuccessful();
//		return resp.getPayload();
//	}
//
//	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
//	@ResponseStatus(HttpStatus.OK)
//	@ResponseBody
//	public DistributionProtocolDetail getDistributionProtocol(@PathVariable Long id) {
//		ResponseEvent<DistributionProtocolDetail> resp = distributionProtocolSvc.getDistributionProtocol(getRequest(id));
//		resp.throwErrorIfUnsuccessful();
//		return resp.getPayload();
//	}
//
//	@RequestMapping(method = RequestMethod.POST)
//	@ResponseStatus(HttpStatus.OK)
//	@ResponseBody
//	public DistributionProtocolDetail createDistributionProtocol(@RequestBody DistributionProtocolDetail dp) {
//		ResponseEvent<DistributionProtocolDetail> resp = distributionProtocolSvc.createDistributionProtocol(getRequest(dp));
//		resp.throwErrorIfUnsuccessful();
//		return resp.getPayload();
//	}
//
//	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
//	@ResponseBody
//	@ResponseStatus(HttpStatus.OK)
//	public DistributionProtocolDetail updateDistributionProtocol(
//			@PathVariable 
//			Long id,
//			
//			@RequestBody 
//			DistributionProtocolDetail dp) {
//		dp.setId(id);
//		
//		ResponseEvent<DistributionProtocolDetail> resp = distributionProtocolSvc.updateDistributionProtocol(getRequest(dp));
//		resp.throwErrorIfUnsuccessful();
//		return resp.getPayload();
//	}
//
//	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
//	@ResponseBody
//	@ResponseStatus(HttpStatus.OK)
//	public DistributionProtocolDetail deleteDistributionProtocol(@PathVariable Long id) {
//		ResponseEvent<DistributionProtocolDetail> resp = distributionProtocolSvc.deleteDistributionProtocol(getRequest(id));
//		resp.throwErrorIfUnsuccessful();
//		return resp.getPayload();
//	}
//
//	private <T> RequestEvent<T> getRequest(T payload) {
//		return new RequestEvent<T>(getSession(), payload);
//	}
//	
//	private SessionDataBean getSession() {
//		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
//	}
//}