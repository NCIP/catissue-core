package com.krishagni.catissueplus.core.de.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.de.repository.DaoFactory;
import com.krishagni.catissueplus.core.de.services.FormContextProcessor;
import com.krishagni.catissueplus.core.de.services.FormService;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.napi.FormEventsListener;
import edu.common.dynamicextensions.napi.FormEventsNotifier;
import krishagni.catissueplus.beans.FormContextBean;

//
// Used by DeObject. Any further use of this object needs to be carefully
// reviewed, as lot of internal details of this object are based on how
// DeObject works.
//

@Configurable
class FormInfoCache implements FormContextProcessor, FormEventsListener {
	@Autowired
	private FormService formService;

	@Autowired
	private DaoFactory daoFactory;

	//
	// Key is CP ID. For non CP specific, key is -1L
	//
	private Map<Long, ContextInfo> contextInfoMap = new HashMap<>();

	//
	// Key is form name
	//
	private Map<String, Container> formsCache = new HashMap<>();

	public FormInfoCache() {

	}

	@PostConstruct
	public void registerListeners() {
		FormEventsNotifier.getInstance().addListener(this);
		formService.addFormContextProc("*", this);
	}

	public String getFormName(Long cpId, String entityType) {
		ContextInfo ctxtInfo = getCpContextInfo(cpId);
		if (!ctxtInfo.hasFormName(entityType)) {
			synchronized (ctxtInfo) {
				String formName = null;
				Long formCtxtId = null;

				Pair<String, Long> formInfo = daoFactory.getFormDao().getFormNameContext(cpId, entityType);
				if (formInfo != null) {
					formName = formInfo.first();
					formCtxtId = formInfo.second();
				}

				ctxtInfo.addFormName(entityType, formName);
				ctxtInfo.addFormContext(entityType, formName, formCtxtId);
			}
		}

		return ctxtInfo.getFormName(entityType);

	}

	public Long getFormContext(Long cpId, String entityType, String formName) {
		ContextInfo ctxtInfo = getCpContextInfo(cpId);
		if (!ctxtInfo.hasFormContext(entityType, formName)) {
			Container form = getForm(formName);
			synchronized (ctxtInfo) {
				Long formCtx = daoFactory.getFormDao().getFormCtxtId(form.getId(), entityType, cpId);
				ctxtInfo.addFormContext(entityType, formName, formCtx);
			}
		}

		return ctxtInfo.getFormContext(entityType, formName);
	}

	public Container getForm(String formName) {
		Container form = formsCache.get(formName);
		if (form == null) {
			synchronized (formsCache) {
				form = Container.getContainer(formName);
				formsCache.put(formName, form);
			}
		}

		return form;
	}

	public Map<String, Object> getFormInfo(Long cpId, String entity) {
		String formName = getFormName(cpId, entity);
		if (StringUtils.isBlank(formName) && cpId != -1L) {
			cpId = -1L;
			formName = getFormName(cpId, entity);
		}

		if (StringUtils.isBlank(formName)) {
			return null;
		}

		Map<String, Object> formInfo = new HashMap<>();
		formInfo.put("formId", getForm(formName).getId());
		formInfo.put("formCtxtId", getFormContext(cpId, entity, formName));
		formInfo.put("formName", formName);
		return formInfo;
	}

	@Override
	public void onSaveOrUpdate(FormContextBean formCtxt) {
		removeCpFormContext(formCtxt);
	}

	@Override
	public void onRemove(FormContextBean formCtxt) {
		removeCpFormContext(formCtxt);
	}

	@Override
	public void onCreate(Container container) {
		formsCache.remove(container.getName());
	}

	@Override
	public void preUpdate(Container form) {
	}

	@Override
	public void onUpdate(Container container) {
		formsCache.remove(container.getName());
	}

	@Override
	public synchronized void onDelete(Container container) {
		formsCache.remove(container.getName());

		for (ContextInfo ctxtInfo : contextInfoMap.values()) {
			ctxtInfo.removeForm(container.getName());
		}
	}

	public void removeCpFormContext(FormContextBean formCtxt) {
		ContextInfo contextInfo = getCpContextInfo(formCtxt.getCpId());
		if (contextInfo == null) {
			return;
		}

		synchronized (contextInfo) {
			contextInfo.removeFormName(formCtxt.getEntityType());
			contextInfo.removeFormContext(formCtxt.getIdentifier());
		}
	}

	private ContextInfo getCpContextInfo(Long cpId) {
		if (!contextInfoMap.containsKey(cpId)) {
			synchronized (contextInfoMap) {
				if (!contextInfoMap.containsKey(cpId)) {
					contextInfoMap.put(cpId, new ContextInfo());
				}
			}
		}

		return contextInfoMap.get(cpId);
	}

	private static class ContextInfo {
		//
		// Key is entity. e.g. ParticipantExtension, SpecimenExtension etc
		// Value is name of form representing the extension
		//
		private Map<String, String> entityForms = new HashMap<>();

		//
		// Key is entity#form_name. e.g. ParticipantExtension#ParticipantCustomFields
		// Value is form context ID
		//
		private Map<String, Long> formContexts = new HashMap<>();

		public Map<String, String> getEntityForms() {
			return entityForms;
		}

		public void setEntityForms(Map<String, String> entityForms) {
			this.entityForms = entityForms;
		}

		public Map<String, Long> getFormContexts() {
			return formContexts;
		}

		public void setFormContexts(Map<String, Long> formContexts) {
			this.formContexts = formContexts;
		}

		public boolean hasFormName(String entityType) {
			return entityForms.containsKey(entityType);
		}

		public String getFormName(String entityType) {
			return entityForms.get(entityType);
		}

		public void addFormName(String entityType, String formName) {
			entityForms.put(entityType, formName);
		}

		public void removeFormName(String entityType) {
			entityForms.remove(entityType);
		}

		public void removeForm(String formName) {
			List<String> entities = new ArrayList<>();
			Iterator<Map.Entry<String, String>> iter = entityForms.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> entityFormName = iter.next();
				if (entityFormName.getValue() != null && entityFormName.getValue().equals(formName)) {
					entities.add(entityFormName.getKey() + "#" + formName);
					iter.remove();
				}
			}

			for (String entity : entities) {
				formContexts.remove(entity);
			}
		}

		public boolean hasFormContext(String entityType, String formName) {
			return formContexts.containsKey(getFormCtxtKey(entityType, formName));
		}

		public Long getFormContext(String entityType, String formName) {
			return formContexts.get(getFormCtxtKey(entityType, formName));
		}

		public void addFormContext(String entityType, String formName, Long ctxtId) {
			formContexts.put(getFormCtxtKey(entityType, formName), ctxtId);
		}

		private String getFormCtxtKey(String entityType, String formName) {
			return entityType + "#" + formName;
		}

		private void removeFormContext(Long formCtxtId) {
			Iterator<Map.Entry<String, Long>> iter = formContexts.entrySet().iterator();
			while (iter.hasNext()) {
				Long cachedId = iter.next().getValue();
				if (cachedId == null || cachedId.equals(formCtxtId)) {
					iter.remove();
				}
			}
		}
	}
}

