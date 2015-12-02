package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.krishagni.catissueplus.core.administrative.domain.SpecimenRequest;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.SpecimenRequestErrorCode;
import com.krishagni.catissueplus.core.administrative.events.RequestListSpecimensDetail;
import com.krishagni.catissueplus.core.administrative.events.SpecimenRequestSummary;
import com.krishagni.catissueplus.core.administrative.services.SpecimenRequestService;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenList;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenListErrorCode;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.de.events.FormDataDetail;
import com.krishagni.catissueplus.core.de.events.FormType;
import com.krishagni.catissueplus.core.de.repository.FormDao;
import com.krishagni.catissueplus.core.de.services.FormService;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.napi.FormData;
import krishagni.catissueplus.beans.FormContextBean;

public class SpecimenRequestServiceImpl implements SpecimenRequestService {

	private DaoFactory daoFactory;

	private FormDao formDao;

	private FormService formSvc;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setFormDao(FormDao formDao) {
		this.formDao = formDao;
	}

	public void setFormSvc(FormService formSvc) {
		this.formSvc = formSvc;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<SpecimenRequestSummary>> createRequest(RequestEvent<RequestListSpecimensDetail> req) {
		try {
			RequestListSpecimensDetail detail = req.getPayload();

			SpecimenList list = daoFactory.getSpecimenListDao().getSpecimenList(detail.getListId());
			if (list == null) {
				return ResponseEvent.userError(SpecimenListErrorCode.NOT_FOUND);
			}

			User currentUser = AuthUtil.getCurrentUser();
			if (!list.getOwner().equals(currentUser) && !list.getSharedWith().contains(currentUser)) {
				return ResponseEvent.userError(SpecimenListErrorCode.ACCESS_NOT_ALLOWED);
			}

			List<SpecimenRequest> requests = createSpecimenRequests(list, detail.getRequestForms());
			return ResponseEvent.response(SpecimenRequestSummary.from(requests));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	private List<SpecimenRequest> createSpecimenRequests(SpecimenList list, List<Map<String, Object>> reqForms) {
		Map<Long, List<Specimen>> cpSpmns = getCpSpecimens(list);
		Map<Long, FormContextBean> cpReqForms = getCpRequestForms(cpSpmns.keySet());
		Map<Long, Container> formsCache = new HashMap<Long, Container>();

		List<SpecimenRequest> result = new ArrayList<SpecimenRequest>();
		for (Map.Entry<Long, List<Specimen>> spmns : cpSpmns.entrySet()) {
			FormContextBean cpReqFormCtxt = cpReqForms.get(spmns.getKey());
			if (cpReqFormCtxt == null) {
				cpReqFormCtxt = cpReqForms.get(-1L);
				if (cpReqFormCtxt == null) {
					String cpShortTitle = getCpShortTitle(spmns.getValue());
					throw OpenSpecimenException.serverError(SpecimenRequestErrorCode.FORM_NOT_CONFIGURED, cpShortTitle);
				}
			}

			Map<String, Object> reqForm = getRequestForm(reqForms, cpReqFormCtxt.getContainerId());
			if (reqForm == null) {
				String cpShortTitle = getCpShortTitle(spmns.getValue());
				throw OpenSpecimenException.userError(SpecimenRequestErrorCode.FORM_NOT_FILLED, cpShortTitle);
			}

			SpecimenRequest spmnReq = createSpecimenRequest(spmns.getKey(), spmns.getValue());

			Container form = formsCache.get(cpReqFormCtxt.getContainerId());
			if (form == null) {
				form = Container.getContainer(cpReqFormCtxt.getContainerId());
				formsCache.put(cpReqFormCtxt.getContainerId(), form);
			}

			reqForm.put("appData", getReqFormAppData(spmnReq, cpReqFormCtxt));
			saveRequestForm(form, reqForm);

			result.add(spmnReq);
		}

		return result;
	}

	private Map<Long, List<Specimen>> getCpSpecimens(SpecimenList list) {
		return daoFactory.getSpecimenListDao().getListCpSpecimens(list.getId());
	}

	private String getCpShortTitle(Collection<Specimen> specimens) {
		if (CollectionUtils.isEmpty(specimens)) {
			return "";
		}

		return specimens.iterator().next().getCollectionProtocol().getShortTitle();
	}

	private Map<Long, FormContextBean> getCpRequestForms(Collection<Long> cpIds) {
		//
		// Include the default request form as well
		//
		Set<Long> allCpIds = new HashSet<Long>(cpIds);
		allCpIds.add(-1L);

		//
		// Obtain all request form contexts from db
		//
		List<FormContextBean> formCtxts = formDao.getFormContexts(allCpIds, FormType.SPECIMEN_REQUEST_FORMS.getType());

		//
		// Create a map of CP request forms
		//
		Map<Long, FormContextBean> cpReqForms = new HashMap<Long, FormContextBean>();
		for (FormContextBean formCtxt : formCtxts) {
			cpReqForms.put(formCtxt.getCpId(), formCtxt);
		}

		return cpReqForms;
	}

	private Map<String, Object> getRequestForm(List<Map<String, Object>> reqForms, Long formId) {
		if (reqForms == null) {
			return null;
		}

		for (Map<String, Object> reqForm : reqForms) {
			Long containerId = ((Number)reqForm.get("containerId")).longValue();
			if (formId.equals(containerId)) {
				return reqForm;
			}
		}

		return null;
	}

	private SpecimenRequest createSpecimenRequest(Long cpId, List<Specimen> specimens) {
		CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getById(cpId);

		SpecimenRequest request = new SpecimenRequest();
		request.setCp(cp);
		request.setRequestor(AuthUtil.getCurrentUser());
		request.setDateOfRequest(Calendar.getInstance().getTime());
		request.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
		request.setSpecimens(new LinkedHashSet<Specimen>(specimens));

		daoFactory.getSpecimenRequestDao().saveOrUpdate(request);
		return request;
	}

	private Map<String, Object> getReqFormAppData(SpecimenRequest req, FormContextBean formCtxt) {
		Map<String, Object> appData = new HashMap<String, Object>();
		appData.put("formCtxtId", formCtxt.getIdentifier());
		appData.put("objectId", req.getId());
		return appData;
	}

	private void saveRequestForm(Container form, Map<String, Object> reqForm) {
		FormDataDetail detail = new FormDataDetail();
		detail.setFormId(form.getId());
		detail.setFormData(FormData.getFormData(form, reqForm));
		ResponseEvent<FormDataDetail> resp = formSvc.saveFormData(new RequestEvent<FormDataDetail>(detail));
		resp.throwErrorIfUnsuccessful();
	}
}