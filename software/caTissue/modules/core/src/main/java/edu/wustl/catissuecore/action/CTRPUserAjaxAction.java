package edu.wustl.catissuecore.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.catissuecore.ctrp.COPPAServiceClient;
import edu.wustl.catissuecore.ctrp.COPPAUtil;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.coppa.po.Person;

public class CTRPUserAjaxAction extends SecureAction {
	private transient final Logger logger = Logger
			.getCommonLogger(CTRPUserAjaxAction.class);

	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		final IFactory factory = AbstractFactoryConfig.getInstance()
				.getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.USER_FORM_ID);
		String pageOperation = request.getParameter("pageOperation");
		COPPAServiceClient coppaClient = new COPPAServiceClient();
		JSONObject responseJsonObject = new JSONObject();
		if ("SearchRemote".equalsIgnoreCase(pageOperation)) {
			String emailAddress = request.getParameter("emailAddress");
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			User searchUser = new User();
			Person[] userList = null;
			// searchUser.setEmailAddress(ctrpEntityForm.getEntityName());
			searchUser.setEmailAddress(emailAddress);
			searchUser.setFirstName(firstName);
			searchUser.setLastName(lastName);
			userList = coppaClient.searchPerson(searchUser);
			responseJsonObject = constructJSONResponse(userList);
		} else if ("GetRemoteById".equalsIgnoreCase(pageOperation)) {
			String remoteId = request.getParameter("remoteId");
			Person user = coppaClient.getPersonById(remoteId);
			Person[] userList = new Person[1];
			userList[0] = user;
			responseJsonObject = constructJSONResponse(userList);
			coppaClient.getRemoteInstitutionsByUserId(remoteId);

		} else if ("GetInstitutions".equalsIgnoreCase(pageOperation)) {
			// Get Remote Institutions related to person
			String remoteId = request.getParameter("remoteId");
			String userId = request.getParameter("userId");
			Institution defaultInstitution = null;
			if (AppUtility.isNotEmpty(userId)
					&& ((new Long(userId).longValue()) != 0)) {
				User user = (User) bizLogic.retrieve(User.class.getName(),
						new Long(userId).longValue());
				defaultInstitution = user.getInstitution();
			}
			List<Institution> comboInstList = new ArrayList<Institution>();
			List<Institution> localInstList = bizLogic
					.retrieve(Institution.class.getName());

			if (defaultInstitution != null) {
				// Add the associated institution as top of the combobox for it
				// to be selected by default
				comboInstList.add(defaultInstitution);
				ListIterator<Institution> localInstListIter = localInstList
						.listIterator();
				// Remove associated institution from local list as it is
				// already
				// added to top
				while (localInstListIter.hasNext()) {
					Institution inst = localInstListIter.next();

					if (defaultInstitution.getId().equals(inst.getId())) {
						localInstListIter.remove();
						break;
					}
				}
			}

			List<Institution> remoteInstList = null;

			if (COPPAUtil.isCOPPAEnabled() && AppUtility.isNotEmpty(remoteId)
					&& (!"0".equalsIgnoreCase(remoteId))) {
				remoteInstList = coppaClient
						.getRemoteInstitutionsByUserId(remoteId);
			}
			// Get all available local institutions related to person
			// String sourceObjectName = Institution.class.getName();
			// String[] displayNameFields = { Constants.NAME };
			// String valueField = Constants.SYSTEM_IDENTIFIER;
			// List<NameValueBean> localInstList = bizLogic.getList(
			// sourceObjectName, displayNameFields, valueField, false);

			// Scan remote and local lists to remove common institutions
			if (remoteInstList != null) {
				ListIterator<Institution> remoteInstListIter = remoteInstList
						.listIterator();
				while (remoteInstListIter.hasNext()) {
					Institution remoteInst = remoteInstListIter.next();
					ListIterator<Institution> localInstListIter = localInstList
							.listIterator();
					while (localInstListIter.hasNext()) {
						Institution inst = localInstListIter.next();

						if (remoteInst.getRemoteId().equals(inst.getRemoteId())) {
							// Set caTissue Institution id to matching remote
							// institution
							remoteInst.setId(inst.getId());
							// Remove it from local institution list to avoid
							// duplicate display
							localInstListIter.remove();
						}
					}// for all local institutions list
					if ((defaultInstitution != null)
							&& (defaultInstitution.getRemoteId() != null)
							&& (defaultInstitution.getRemoteId()
									.equals(remoteInst.getRemoteId()))) {
						remoteInstListIter.remove();
					}
				}// for related remote insts list
			}// end if remote list not null
			if (AppUtility.isNotEmpty(remoteInstList)) {
				comboInstList.addAll(remoteInstList);
			}
			if (AppUtility.isNotEmpty(localInstList)) {
				comboInstList.addAll(localInstList);
			}
			responseJsonObject = constructJSONComboResponse(comboInstList,
					COPPAUtil.isCOPPAEnabled());

		} else {
			responseJsonObject.put("error", "Unsupported pageOperation:"
					+ pageOperation);
		}
		response.setContentType("text/javascript");
		final PrintWriter out = response.getWriter();
		out.write(responseJsonObject.toString());
		logger.debug("JSON Inst Response:" + responseJsonObject);
		return null;
	}

	private JSONObject constructJSONResponse(Person[] userList)
			throws JSONException, Exception {
		JSONObject mainJsonObject = new JSONObject();
		JSONArray jsonUserRecords = new JSONArray();
		int totalCount = 0;
		if (userList != null && (userList.length > 0)) {
			totalCount = userList.length;
			for (Person user : userList) {
				JSONObject jsonUserObject = new JSONObject();
				jsonUserObject.put("id", COPPAUtil.getRemoteIdentifier(user));
				jsonUserObject.put("firstName", COPPAUtil.getFirstName(user));
				jsonUserObject.put("lastName", COPPAUtil.getLastName(user));
				jsonUserObject.put("emailAddress",
						COPPAUtil.getEmailAddress(user));
				jsonUserObject.put("street", COPPAUtil.getStreetAddress(user));
				jsonUserObject.put("city", COPPAUtil.getCity(user));
				// jsonUserObject.put("state", "Maryland");
				jsonUserObject.put("state",
						getFullStateName(COPPAUtil.getState(user)));
				jsonUserObject.put("phoneNumber",
						COPPAUtil.getTelephoneNumber(user));
				jsonUserObject.put("faxNumber", COPPAUtil.getFaxNumber(user));
				jsonUserObject.put("zipCode", COPPAUtil.getZipCode(user));
				jsonUserObject.put("country", COPPAUtil.getCountry(user));
				jsonUserRecords.put(jsonUserObject);
			}// end for userList
		}// if userList not null
		mainJsonObject.put("totalCount", totalCount);
		mainJsonObject.put("row", jsonUserRecords);
		return mainJsonObject;
	}

	private String getFullStateName(String stateCode) throws Exception {
		ResourceBundle rb = ResourceBundle
				.getBundle(Constants.STATE_CODE_MAPPING_RESOURCES);
		String stateName = stateCode;
		if (rb != null) {
			stateName = rb.getString(stateCode);
			NameValueBean stateNVB = new NameValueBean();
			stateNVB.setName(stateName);
			stateNVB.setValue(stateName);
			List<NameValueBean> stateList = CDEManager.getCDEManager()
					.getPermissibleValueList(Constants.CDE_NAME_STATE_LIST,
							null);
			if (!stateList.contains(stateNVB)) {
				stateName = Constants.STATE_OTHER;
			}
		}
		return stateName;
	}

	private JSONObject constructJSONComboResponse(List<Institution> instList,
			boolean isCoppaEnabled) throws JSONException, Exception {
		JSONObject mainJsonObject = new JSONObject();
		JSONArray jsonUserRecords = new JSONArray();
		int comboId = 0;

		if (AppUtility.isNotEmpty(instList)) {
			for (Institution inst : instList) {
				// if (inst.getValue().equalsIgnoreCase("-1")) {
				// // Skip --Select-- row in User page
				// continue;
				// }
				// if (jsonUserRecords.toString().indexOf(
				// "\"" + inst.getName() + "\"") > -1) {
				// // Skip - if remote institution is already created locally
				// continue;
				// }
				JSONObject jsonUserObject = new JSONObject();
				jsonUserObject.put("id", comboId++);
				jsonUserObject.put("localInstId", inst.getId());
				jsonUserObject.put("name", inst.getName());
				if ((inst.getRemoteManagedFlag() != null)
						&& inst.getRemoteManagedFlag() && isCoppaEnabled) {
					jsonUserObject.put("remoteFlag", "Y");
					jsonUserObject.put("remoteInstId", inst.getRemoteId());
				} else {
					jsonUserObject.put("remoteFlag", "N");
					jsonUserObject.put("remoteInstId", 0);
				}
				jsonUserRecords.put(jsonUserObject);
			}// end for instList
		}// if instList not null
		mainJsonObject.put("totalCount", jsonUserRecords.length());
		mainJsonObject.put("row", jsonUserRecords);
		return mainJsonObject;
	}
}
