package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import krishagni.catissueplus.util.CommonUtil;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.dto.SCGSummaryDTO;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;


public class SaveSCGAjaxAction extends Action
{

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
			{
		    DAO dao = null;
		    Gson gson = new GsonBuilder().setDateFormat(ApplicationProperties.getValue("date.pattern")+" HH:mm").create();
		   List<String> errorList = new ArrayList<String>();
		   String responseString = "success";
		    try{
		         SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(Constants.SESSION_DATA);
		         dao = AppUtility.openDAOSession(sessionDataBean);
		         String json = request.getParameter("dataJSON");
	        
		         SCGSummaryDTO scgDTO = gson.fromJson(json, SCGSummaryDTO.class);
 		
		 		 SpecimenCollectionGroupBizLogic scgBizLogic = new SpecimenCollectionGroupBizLogic();
		 		 scgBizLogic.saveScgSummary(dao, scgDTO,errorList);
		 		dao.commit();
		 		}catch(ApplicationException ex){
		 			errorList.add(CommonUtil.getErrorMessage(ex, new SpecimenCollectionGroup(), "update"));
//		 			errorList.add(ex.getMsgValues());
		         }finally
		         {
		        	 AppUtility.closeDAOSession(dao);
		         }
		    if(!errorList.isEmpty())
	 		 {
	 			Iterator<String> itr = errorList.iterator();
	 			JSONArray jsonArray = new JSONArray();
	 			while(itr.hasNext())
	 			{
	 			
	 			JSONObject jsonobj = new JSONObject();
	 			jsonobj.put("msg",itr.next());
	 			jsonArray.put(jsonobj);
	 			}
	 			responseString = jsonArray.toString();
	 			 /*JsonElement element = gson.toJsonTree(errorList);
	 			JsonArray jsonArray = element.getAsJsonArray();
	 			responseString = jsonArray.getAsString();*/
	 		 }
		    response.setContentType("application/json");
	 		response.getWriter().write(responseString); 		            
		           return null;
			}
	
}
