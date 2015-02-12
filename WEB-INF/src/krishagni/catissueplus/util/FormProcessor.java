package krishagni.catissueplus.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.de.events.FormContextDetail;
import com.krishagni.catissueplus.core.de.events.FormSummary;
import com.krishagni.catissueplus.core.de.events.FormType;
import com.krishagni.catissueplus.core.de.events.RemoveFormContextOp;
import com.krishagni.catissueplus.core.de.services.FormService;

import edu.common.dynamicextensions.nutility.FormPostProcessor;

public class FormProcessor implements FormPostProcessor {

	@Override
	public void process(Long containerId, Integer sortOrder) {
		
		ApplicationContext appCtx = OpenSpecimenAppCtxProvider.getAppCtx();
		FormService formSvc = (FormService) appCtx.getBean("formSvc");

		CollectionProtocolSummary cp = new CollectionProtocolSummary();
		cp.setId(-1L);
		
		FormContextDetail ctxt = new FormContextDetail();				
		ctxt.setCollectionProtocol(cp);
		ctxt.setFormId(containerId);
		ctxt.setLevel("Query");
		ctxt.setMultiRecord(false); 
		ctxt.setSortOrder(sortOrder);
		
		RequestEvent<List<FormContextDetail>> req = new RequestEvent<List<FormContextDetail>>();
		req.setPayload(Collections.singletonList(ctxt));
		ResponseEvent<List<FormContextDetail>> resp = formSvc.addFormContexts(req);
		resp.throwErrorIfUnsuccessful();		
	}

	@Override
	public List<Long> getQueryForms() {
		ApplicationContext appCtx = OpenSpecimenAppCtxProvider.getAppCtx();
		FormService formSvc = (FormService) appCtx.getBean("formSvc");
		
		FormType type = FormType.QUERY_FORMS;
		RequestEvent<FormType> req = new RequestEvent<FormType>();
		req.setPayload(type);
				
		ResponseEvent<List<FormSummary>> resp = formSvc.getForms(req);
		resp.throwErrorIfUnsuccessful();

		List<Long> queryForms = new ArrayList<Long>();
		for (FormSummary form: resp.getPayload()) {
			queryForms.add(form.getFormId());
		}
		
		return queryForms;
	}

	@Override
	public void deleteQueryForm(Long formId) {
		ApplicationContext appCtx = OpenSpecimenAppCtxProvider.getAppCtx();
		FormService formSvc = (FormService) appCtx.getBean("formSvc");
		
		RemoveFormContextOp opDetail = new RemoveFormContextOp();
		opDetail.setFormId(formId);
		opDetail.setFormType(FormType.QUERY_FORMS);
		
		RequestEvent<RemoveFormContextOp> req = new RequestEvent<RemoveFormContextOp>();
		req.setPayload(opDetail);
				
		ResponseEvent<Boolean> resp = formSvc.removeFormContext(req);
		resp.throwErrorIfUnsuccessful();		
	}
}
