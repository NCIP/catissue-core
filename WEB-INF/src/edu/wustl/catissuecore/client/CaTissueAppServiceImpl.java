
package edu.wustl.catissuecore.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.*;
import org.springframework.context.ApplicationContext;

import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.de.events.AddRecordEntryEvent;
import com.krishagni.catissueplus.core.de.events.RecordEntryEventAdded;
import com.krishagni.catissueplus.core.de.services.FormService;

import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.FormDataManager;
import edu.common.dynamicextensions.napi.impl.FormDataManagerImpl;
import edu.wustl.bulkoperator.appservice.AbstractBulkOperationAppService;
import edu.wustl.bulkoperator.metadata.HookingInformation;
import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;

public class CaTissueAppServiceImpl extends AbstractBulkOperationAppService {

	private CaCoreAppServicesDelegator appService;

	private String userName;

	public CaTissueAppServiceImpl(boolean isAuthenticationRequired, String userName, String password)
	throws Exception {
		super(isAuthenticationRequired, userName, password);
	}

	@Override
	public void authenticate(String userName, String password) throws BulkOperationException {
		try	{
			if (isAuthRequired && password != null) {
				if (!appService.delegateLogin(userName, password)) {
					throw new BulkOperationException(
							"Could not login with given username/password.Please check the credentials");
				}
			}
			this.userName = userName;
		} catch (Exception appExp) {
			throw new BulkOperationException(appExp.getMessage(), appExp);
		}
	}

	@Override
	public void initialize(String userName, String password) throws BulkOperationException {
		appService = new CaCoreAppServicesDelegator();
		authenticate(userName, password);
	}

	@Override
	public void deleteObject(Object arg0) throws BulkOperationException {
	}

	@Override
	protected Object insertObject(Object domainObject) throws Exception {
		try {
			Object returnedObject = appService.delegateAdd(userName, domainObject);
			return returnedObject;
		} catch (ApplicationException appExp) {
			throw appExp;
		} catch (Exception exp) {
			throw exp;
		}
	}

	@Override
	protected Object searchObject(Object str) throws Exception {
		Object returnedObject = null;
		try {
			String hql = (String) str;
			List result = AppUtility.executeQuery(hql);

			if (!result.isEmpty()) {
				returnedObject = result.get(0);
			}
		} catch (Exception appExp) {
			throw new Exception(appExp.getMessage(), appExp);
		}
		return returnedObject;
	}

	@Override
	protected Object updateObject(Object domainObject) throws Exception {
		try {
			Object returnedObject = appService.delegateEdit(userName, domainObject);
			return returnedObject;
		} catch (ApplicationException appExp) {
			throw appExp;
		} catch (Exception exp) {
			throw exp;
		}
	}

	public Long insertDEObject(String formName, final Map<String, Object> dataValue, HookingInformation recEntryInfo)
	throws Exception {
		Long recordId = null;
		Container c = Container.getContainer(formName);
		FormData formData = getFormData(c, dataValue);
		final SessionDataBean sessionDataBean = recEntryInfo.getSessionDataBean();

		UserContext ctxt =  new UserContext() {
			@Override
			public String getUserName() {
				return sessionDataBean.getUserName();
			}

			@Override
			public Long getUserId() {
				return sessionDataBean.getUserId();
			}

			@Override
			public String getIpAddress() {
				return sessionDataBean.getIpAddress();
			}
		};
		FormDataManager formDataManager = new FormDataManagerImpl(false);
		recordId = formDataManager.saveOrUpdateFormData(ctxt, formData);
		hookStaticDynExtObj(recEntryInfo, c.getId(), recordId);
		return recordId;
	}

	private FormData getFormData(Container c, Map<String, Object> dataValue) {
		FormData formData = new FormData(c);
		for (Control ctrl : c.getControlsMap().values()) {
			if (ctrl instanceof SubFormControl) {
				SubFormControl sfCtrl = (SubFormControl) ctrl;
                String sfKey = new StringBuilder(c.getName()).append("->")
                                .append(sfCtrl.getSubContainer().getName()).toString();
                List<Map<String, Object>> sfDataValueList = (List<Map<String, Object>>) dataValue.get(sfKey);
				
				if (sfDataValueList == null) {
					formData.addFieldValue(new ControlValue(sfCtrl, null));
					continue;
				}
				
				List<FormData> subFormsData = new ArrayList<FormData>();
				for (Map<String, Object> sfDataValue : sfDataValueList) {
                    subFormsData.add(getFormData(sfCtrl.getSubContainer(), sfDataValue));
				}
				formData.addFieldValue(new ControlValue(sfCtrl, subFormsData));
				continue;
			}

            Object value = null;
            if (ctrl instanceof MultiSelectControl) {
                List<Map<String, String>> msList = (List<Map<String, String>>) dataValue.get(ctrl.getUserDefinedName());
                value = new String[msList.size()];
                int i = 0;
                String[] msVal = new String[msList.size()];
                for (Map<String, String> msMap : msList) {
                    msVal[i++] = msMap.get(ctrl.getUserDefinedName());
                }
                value = msVal;
            } else {
                value = dataValue.get(ctrl.getUserDefinedName());
            }
			if (value != null) {
				formData.addFieldValue(new ControlValue(ctrl, value));
			} else {
				formData.addFieldValue(new ControlValue(ctrl, null));
			}
		}
		return formData;
	}

	protected List<Object> hookStaticDynExtObj(HookingInformation recEntryInfo, Long containerId, Long recordId)
	throws Exception {
		ApplicationContext caTissueContext = OpenSpecimenAppCtxProvider.getAppCtx();
		FormService formSvc = (FormService) caTissueContext.getBean("formSvc");
		Map<String,Object> recIntegrationInfo = recEntryInfo.getDataHookingInformation();

		AddRecordEntryEvent req = new AddRecordEntryEvent();
		req.setSessionDataBean(recEntryInfo.getSessionDataBean());
		req.setRecIntegrationInfo(recIntegrationInfo);
		req.setContainerId(containerId);
		req.setRecordId(recordId);

		RecordEntryEventAdded resp = formSvc.addRecordEntry(req);

		return null;
	}

	@Override
	protected Long hookStaticDynExtObject(Object arg0) throws Exception {
		return null;
	}
}