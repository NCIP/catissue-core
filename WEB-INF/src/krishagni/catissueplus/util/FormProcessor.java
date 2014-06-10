package krishagni.catissueplus.util;

import java.util.Collections;

import org.springframework.context.ApplicationContext;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.common.CaTissueAppContext;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.de.events.AddFormContextsEvent;
import com.krishagni.catissueplus.core.de.events.FormContextDetail;
import com.krishagni.catissueplus.core.de.events.FormContextsAddedEvent;
import com.krishagni.catissueplus.core.de.services.FormService;

import edu.common.dynamicextensions.nutility.FormPostProcessor;

public class FormProcessor implements FormPostProcessor {

	@Override
	public void process(Long containerId, Integer sortOrder) {
		
		ApplicationContext caTissueContext = CaTissueAppContext.getInstance();
		FormService formSvc = (FormService) caTissueContext.getBean("formSvc");

		FormContextDetail ctxt = new FormContextDetail();
		CollectionProtocolSummary cp = new CollectionProtocolSummary();
		cp.setId(-1L);
		
		AddFormContextsEvent req = new AddFormContextsEvent();
		ctxt.setCollectionProtocol(cp);
		ctxt.setFormId(containerId);
		ctxt.setLevel("Query");
		ctxt.setMultiRecord(false); 
		ctxt.setSortOrder(sortOrder);
		
		req.setFormContexts(Collections.singletonList(ctxt));
		FormContextsAddedEvent resp = formSvc.addFormContexts(req);
		
		if (resp.getStatus() != EventStatus.OK) {
			throw new RuntimeException("Exception occurred in processing of the form with id : " + containerId);
		}
	}
}
