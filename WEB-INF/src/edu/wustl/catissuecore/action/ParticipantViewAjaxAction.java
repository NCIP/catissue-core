package edu.wustl.catissuecore.action;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.dao.DAO;

public class ParticipantViewAjaxAction extends DispatchAction{

	public ActionForward getSpecimenLabel(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
	
		String scgId = request.getParameter("scgId");
		NewSpecimenBizLogic specBizlogic = new NewSpecimenBizLogic();
		SessionDataBean sessionData =  (SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
		String specimenLabels = "";
		DAO dao = null;
		try{
			dao = AppUtility.openDAOSession(sessionData);
	    if(scgId!=null)
	    {
		 specimenLabels = getSpecimenLabelJson(specBizlogic.getSpecimeLables(dao,Long.valueOf(scgId)));
	    }
		}finally
		 {
		    if(dao!=null)
		    {
			  dao.closeSession();	
		    }
		 }
	    response.setContentType("application/json");
	    PrintWriter out = response.getWriter();
	    out.print(specimenLabels);
	    out.flush();
		return null;
	}
	
	/**
	 * @param innerDataArray
	 * @param speicmens
	 * @throws JSONException
	 */
	private String getSpecimenLabelJson(List<NameValueBean> speicmens)
			throws JSONException
	{
		JSONObject mainJsonObject = new JSONObject();
		JSONArray innerDataArray = new JSONArray();
		//Gson gson = new Gson();
		//gson.toJson(speicmens);
		for(NameValueBean specimen:speicmens )
		{			
		        JSONObject innerJsonObject = new JSONObject();
				
				innerJsonObject.put("text",specimen.getName());
				innerJsonObject.put("value",specimen.getValue());
				innerDataArray.put(innerJsonObject);
		 }
		return innerDataArray.toString();
	}
	
}
