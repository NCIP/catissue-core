
package com.krishagni.catissueplus.rest.controller;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.events.CreateImageEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteImageEvent;
import com.krishagni.catissueplus.core.administrative.events.ImageCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.ImageDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.ImageDetails;
import com.krishagni.catissueplus.core.administrative.events.ImagePatchDetails;
import com.krishagni.catissueplus.core.administrative.events.ImageUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchImageEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateImageEvent;
import com.krishagni.catissueplus.core.administrative.services.ImageService;
import com.krishagni.catissueplus.core.common.events.EventStatus;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/images")
public class ImageController {

	private static final String PATCH_IMAGE = "patch image";

	@Autowired
	private HttpServletRequest httpServletRequest;

	@Autowired
	private ImageService imagesvc;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ImageDetails createImage(@RequestBody ImageDetails details) {
		CreateImageEvent reqEvent = new CreateImageEvent();
		reqEvent.setDetails(details);
		reqEvent.setSessionDataBean(getSession());
		ImageCreatedEvent response = imagesvc.createImage(reqEvent);
		if (response.getStatus() == EventStatus.OK) {
			return response.getDetails();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ImageDetails updateImage(@PathVariable Long id, @RequestBody ImageDetails details) {
		UpdateImageEvent event = new UpdateImageEvent();
		event.setId(id);
		event.setDetails(details);
		event.setSessionDataBean(getSession());
		ImageUpdatedEvent resp = imagesvc.updateImage(event);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getDetails();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ImageDetails PatchImage(@PathVariable Long id, @RequestBody Map<String, Object> values) {
		PatchImageEvent event = new PatchImageEvent();
		event.setId(id);
		event.setSessionDataBean(getSession());

		ImagePatchDetails details = new ImagePatchDetails();
		try {
			BeanUtils.populate(details, values);
		}
		catch (Exception e) {
			reportError(SiteErrorCode.BAD_REQUEST, PATCH_IMAGE);
		}
		details.setModifiedAttributes(new ArrayList<String>(values.keySet()));
		event.setDetails(details);

		ImageUpdatedEvent response = imagesvc.patchImage(event);
		if (response.getStatus() == EventStatus.OK) {
			return response.getDetails();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String deleteImage(@PathVariable Long id) {
		DeleteImageEvent reqEvent = new DeleteImageEvent();
		reqEvent.setId(id);
		reqEvent.setSessionDataBean(getSession());
		ImageDeletedEvent response = imagesvc.deleteImage(reqEvent);
		if (response.getStatus() == EventStatus.OK) {
			return response.getMessage();
		}
		return null;
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
