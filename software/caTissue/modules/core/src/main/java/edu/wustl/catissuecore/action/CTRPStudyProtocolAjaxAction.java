package edu.wustl.catissuecore.action;

import java.io.PrintWriter;
import java.util.ArrayList;
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
import edu.wustl.catissuecore.ctrp.COPPAUserTransformer;
import edu.wustl.catissuecore.ctrp.COPPAUtil;
import edu.wustl.catissuecore.domain.CollectionProtocol;
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
import gov.nih.nci.coppa.services.pa.StudyProtocol;

public class CTRPStudyProtocolAjaxAction extends SecureAction {
	private transient final Logger logger = Logger
			.getCommonLogger(CTRPStudyProtocolAjaxAction.class);

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
			String title = request.getParameter("title");
			CollectionProtocol searchCollectionProtocol = new CollectionProtocol();
			StudyProtocol[] protocolList = null;
			// searchUser.setEmailAddress(ctrpEntityForm.getEntityName());
			searchCollectionProtocol.setTitle(title);
			protocolList = coppaClient
					.searchSutdyProtocol(searchCollectionProtocol);
			responseJsonObject = constructJSONResponse(protocolList);
		} else if ("GetRemoteById".equalsIgnoreCase(pageOperation)) {
			String remoteId = request.getParameter("remoteId");
			StudyProtocol studyProtocol = coppaClient
					.getSutdyProtocolById(remoteId);
			Person principalInvestigator = coppaClient
					.getPrincipalInvestigatorByStudyProtocolId(remoteId);
			responseJsonObject = constructDetailJSONResponse(studyProtocol,
					principalInvestigator);
			coppaClient.getRemoteInstitutionsByUserId(remoteId);

		} else if ("GetUsers".equalsIgnoreCase(pageOperation)) {

			// Get Remote Institutions related to person
			String remoteId = request.getParameter("remoteId");
			String protocolId = request.getParameter("protocolId");
			User defaultPI = null;
			User piRemoteUser = null;
			if (AppUtility.isNotEmpty(protocolId)
					&& ((new Long(protocolId).longValue()) != 0)) {
				CollectionProtocol protocol = (CollectionProtocol) bizLogic
						.retrieve(CollectionProtocol.class.getName(), new Long(
								protocolId).longValue());
				defaultPI = protocol.getPrincipalInvestigator();
			}
			List<User> comboUserList = new ArrayList<User>();
			List<User> localInstList = bizLogic.retrieve(User.class.getName());

			if (defaultPI != null) {
				// Add the associated institution as top of the combobox for it
				// to be selected by default
				comboUserList.add(defaultPI);
				ListIterator<User> localInstListIter = localInstList
						.listIterator();
				// Remove associated institution from local list as it is
				// already
				// added to top
				while (localInstListIter.hasNext()) {
					User inst = localInstListIter.next();

					if (defaultPI.getId().equals(inst.getId())) {
						localInstListIter.remove();
						break;
					}
				}
			} else {
				// default pi null
				if (COPPAUtil.isCOPPAEnabled()
						&& AppUtility.isNotEmpty(remoteId)
						&& (!"0".equalsIgnoreCase(remoteId))) {
					Person piRemotePerson = coppaClient
							.getPrincipalInvestigatorByStudyProtocolId(""
									+ protocolId);
					piRemoteUser = new COPPAUserTransformer()
							.transform(piRemotePerson);
				}
			}
			if ((defaultPI == null) && (piRemoteUser != null)) {
				comboUserList.add(piRemoteUser);
				// Scan remote and local lists to remove common institutions
				ListIterator<User> localInstListIter = localInstList
						.listIterator();
				while (localInstListIter.hasNext()) {
					User inst = localInstListIter.next();

					if (piRemoteUser.getRemoteId().equals(inst.getRemoteId())) {
						// Set caTissue User id to matching remote
						// user
						piRemoteUser.setId(inst.getId());
						// Remove it from local institution list to avoid
						// duplicate display
						localInstListIter.remove();
					}
				}// for all local institutions list
			} // end if piRemoteUser null
			if (AppUtility.isNotEmpty(localInstList)) {
				comboUserList.addAll(localInstList);
			}
			responseJsonObject = constructJSONComboResponse(comboUserList,
					COPPAUtil.isCOPPAEnabled());

		} else {
			responseJsonObject.put("error", "Unsupported pageOperation:"
					+ pageOperation);
		}
		response.setContentType("text/javascript");
		final PrintWriter out = response.getWriter();
		out.write(responseJsonObject.toString());
		logger.debug("JSON User Response:" + responseJsonObject);
		return null;
	}

	private JSONObject constructJSONResponse(StudyProtocol[] protocolList)
			throws JSONException, Exception {
		JSONObject mainJsonObject = new JSONObject();
		JSONArray jsonUserRecords = new JSONArray();
		int totalCount = 0;
		if (protocolList != null && (protocolList.length > 0)) {
			totalCount = protocolList.length;
			for (StudyProtocol studyProtocol : protocolList) {
				JSONObject jsonUserObject = new JSONObject();
				jsonUserObject.put("id",
						COPPAUtil.getRemoteIdentifier(studyProtocol));
				jsonUserObject.put("title", COPPAUtil.getTitle(studyProtocol));
				jsonUserObject.put("shortTitle",
						COPPAUtil.getShortTitle(studyProtocol));
				jsonUserObject.put("startDate",
						COPPAUtil.getStartDateStr(studyProtocol));
				jsonUserRecords.put(jsonUserObject);
			}// end for protocolList
		}// if protocolList not null
		mainJsonObject.put("totalCount", totalCount);
		mainJsonObject.put("row", jsonUserRecords);
		return mainJsonObject;
	}

	private JSONObject constructDetailJSONResponse(StudyProtocol studyProtocol,
			Person person) throws JSONException, Exception {
		JSONObject mainJsonObject = new JSONObject();
		JSONArray jsonUserRecords = new JSONArray();
		int totalCount = 0;
		if (studyProtocol != null) {
			totalCount = 1;
			JSONObject jsonUserObject = new JSONObject();
			jsonUserObject.put("id",
					COPPAUtil.getRemoteIdentifier(studyProtocol));
			jsonUserObject.put("title", COPPAUtil.getTitle(studyProtocol));
			jsonUserObject.put("shortTitle",
					COPPAUtil.getShortTitle(studyProtocol));
			jsonUserObject.put("startDate",
					COPPAUtil.getStartDateStr(studyProtocol));
			jsonUserObject.put("principalInvestigator",
					COPPAUtil.getLastName(person));
			jsonUserRecords.put(jsonUserObject);
		}// if studyProtocol not null
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

	private JSONObject constructJSONComboResponse(List<User> instList,
			boolean isCoppaEnabled) throws JSONException, Exception {
		JSONObject mainJsonObject = new JSONObject();
		JSONArray jsonUserRecords = new JSONArray();
		int comboId = 0;

		if (AppUtility.isNotEmpty(instList)) {
			for (User inst : instList) {
				JSONObject jsonUserObject = new JSONObject();
				jsonUserObject.put("id", comboId++);
				jsonUserObject.put("localInstId", inst.getId());
				jsonUserObject.put("name",
						inst.getFirstName() + " " + inst.getLastName());
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
