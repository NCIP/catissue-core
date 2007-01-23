
package edu.wustl.catissuecore.action.querysuite;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.action.BaseAppletAction;
import edu.wustl.catissuecore.bizlogic.querysuite.CreateQueryObjectBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.querysuite.factory.SqlGeneratorFactory;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.util.logger.Logger;

/**
 * This action is a applet action called from DiagrammaticViewApplet class when user clicks on seach button of AddLimits.jsp.
 * This class gets IQuery Object from the applet and also generates sql out of itwith the help of CreateQueryObjectBizLogic.
 * This sql is then fired and the results are stored in session so that then they can be seen on ViewSeachResults jsp screen.
 * An emplty map is sent back to the applet. 
 * @author deepti_shelar
 */
public class ViewSearchResultsAction extends BaseAppletAction
{
	/**
	 * This method gets the input strings from the DiagrammaticViewApplet class.
	 * A call to CreateQueryObjectBizLogic returns map which holds list of the result data for the rules added by user.
	 * @param mapping mapping
	 * @param form form
	 * @param request request
	 * @param response response
	 * @throws Exception Exception
	 * @return ActionForward actionForward
	 */
	public ActionForward initData(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		Map inputDataMap = (Map) request.getAttribute(Constants.INPUT_APPLET_DATA);
		if (inputDataMap != null && !inputDataMap.isEmpty())
		{
			IQuery query = (IQuery) inputDataMap.get("queryObject");
			String sql = SqlGeneratorFactory.getInstance().generateSQL(query);
			System.out.println("**********************sql:  "+sql);
			sql= "Select ParticipantMedicalIdentif_1.MEDICAL_RECORD_NUMBER From CATISSUE_PART_MEDICAL_ID ParticipantMedicalIdentif_1  left join CATISSUE_PARTICIPANT Participant_2 on (ParticipantMedicalIdentif_1.participant_id=Participant_2.Identifier)  Where (ParticipantMedicalIdentif_1.MEDICAL_RECORD_NUMBER is NOT NULL) And(Participant_2.GENDER like 'm%')";
			sql = "Select User_1.ACTIVITY_STATUS, User_1.STATUS_COMMENT, User_1.LOGIN_NAME, User_1.START_DATE, User_1.EMAIL_ADDRESS, User_1.FIRST_NAME, User_1.CSM_USER_ID, User_1.LAST_NAME From CATISSUE_USER User_1 left join CATISSUE_INSTITUTION Institution_2 on (User_1.INSTITUTION_ID=Institution_2.IDENTIFIER) Where (User_1.FIRST_NAME like 'a%') And(Institution_2.NAME is NOT NULL)";
			Logger.out.info(sql);
			CreateQueryObjectBizLogic bizLogic = new CreateQueryObjectBizLogic();
			Map outputData = bizLogic.fireQuery(sql);
			request.getSession().setAttribute("treeData", outputData.get("treeData"));
			request.getSession().setAttribute(Constants.SPREADSHEET_DATA_LIST, outputData.get(Constants.SPREADSHEET_DATA_LIST));
			request.getSession().setAttribute(Constants.SPREADSHEET_COLUMN_LIST, outputData.get(Constants.SPREADSHEET_COLUMN_LIST));;
			Map ruleDetailsMap = new HashMap();
			writeMapToResponse(response, ruleDetailsMap);
		}
		return null;
	}

	/**
	 * This is a overloaded method to call the actions method set bt applet class.
	 * @param methodName String
	 * @param mapping ActionMapping
	 * @param form form
	 * @param request request
	 * @param response response
	 * @throws Exception Exception
	 * @return ActionForward actionForward
	 */
	protected ActionForward invokeMethod(String methodName, ActionMapping mapping, ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		if (methodName.trim().length() > 0)
		{
			Method method = getMethod(methodName, this.getClass());
			if (method != null)
			{
				Object args[] = {mapping, form, request, response};
				return (ActionForward) method.invoke(this, args);
			}
			else
				return null;
		}
		return null;
	}
}
