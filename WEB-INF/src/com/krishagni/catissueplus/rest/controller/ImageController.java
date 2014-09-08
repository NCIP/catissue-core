
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
import com.krishagni.catissueplus.core.administrative.events.GetImageEvent;
import com.krishagni.catissueplus.core.administrative.events.GotImageEvent;
import com.krishagni.catissueplus.core.administrative.events.ImageCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.ImageDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.ImageDetails;
import com.krishagni.catissueplus.core.administrative.events.ImagePatchDetails;
import com.krishagni.catissueplus.core.administrative.events.ImageUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchImageEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateImageEvent;
import com.krishagni.catissueplus.core.administrative.services.ImageService;

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

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ImageDetails getBiohazard(@PathVariable Long id) {
		GetImageEvent event = new GetImageEvent();
		event.setId(id);
		GotImageEvent resp = imagesvc.getImage(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDetails();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/eqpImageId={eqpImageId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ImageDetails getBiohazard(@PathVariable String eqpImageId) {
		GetImageEvent event = new GetImageEvent();
		event.setEqpImageId(eqpImageId);
		GotImageEvent resp = imagesvc.getImage(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDetails();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ImageDetails createImage(@RequestBody ImageDetails details) {
		CreateImageEvent reqEvent = new CreateImageEvent();
		reqEvent.setDetails(details);
		reqEvent.setSessionDataBean(getSession());
		ImageCreatedEvent resp = imagesvc.createImage(reqEvent);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDetails();
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
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDetails();
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

		ImageUpdatedEvent resp = imagesvc.patchImage(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDetails();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String deleteImage(@PathVariable Long id) {
		DeleteImageEvent reqEvent = new DeleteImageEvent();
		reqEvent.setId(id);
		reqEvent.setSessionDataBean(getSession());
		ImageDeletedEvent resp = imagesvc.deleteImage(reqEvent);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getMessage();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/eqpImageId={eqpImageId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String deleteImage(@PathVariable String eqpImageId) {
		DeleteImageEvent reqEvent = new DeleteImageEvent();
		reqEvent.setEqpImageId(eqpImageId);
		reqEvent.setSessionDataBean(getSession());
		ImageDeletedEvent resp = imagesvc.deleteImage(reqEvent);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getMessage();
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
