
package com.krishagni.catissueplus.rest.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.notification.events.NotifiedRegistrationDetail;
import com.krishagni.catissueplus.core.notification.services.CatissueNotificationService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

	@Autowired
	private HttpServletRequest httpServletRequest;

	@Autowired
	private CatissueNotificationService catissueNotificationSvc;

	@RequestMapping(method = RequestMethod.POST, value = "/registration")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CollectionProtocolRegistrationDetail registerParticipant(@RequestBody NotifiedRegistrationDetail registrationDetails) {
		RequestEvent<NotifiedRegistrationDetail> req = new RequestEvent<NotifiedRegistrationDetail>(getSession(), registrationDetails);
		ResponseEvent<CollectionProtocolRegistrationDetail> resp = catissueNotificationSvc.registerParticipant(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();		
	}

//	@RequestMapping(method = RequestMethod.PUT, value= "/registration")
//	@ResponseStatus(HttpStatus.OK)
//	@ResponseBody
//	public RegistrationUpdatedEvent updateParticipantRegistartion(
//			@RequestBody NotifiedRegistrationDetail registrationDetails) {
//		RegisterParticipantEvent event = new RegisterParticipantEvent();
//		event.setSessionDataBean(getSession());
//		event.setRegistrationDetails(registrationDetails);
//		return catissueNotificationSvc.updateParticipantRegistartion(event);
//	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}

}
