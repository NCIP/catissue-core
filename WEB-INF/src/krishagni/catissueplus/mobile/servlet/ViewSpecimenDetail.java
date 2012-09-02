package krishagni.catissueplus.mobile.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.wustl.catissuecore.util.global.Constants;

public class ViewSpecimenDetail  extends HttpServlet {

	/**
	 *
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException,
			IOException
	{
		doPost(req, res);
	}

	
	/**
	 * This method is used to download files saved in database.
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException
	{

		org.json.JSONObject returnedJObject= new org.json.JSONObject();

		try{
			
			if(request.getParameter("label")!=null){
				final edu.wustl.common.beans.SessionDataBean sessionData = (edu.wustl.common.beans.SessionDataBean) request.getSession().getAttribute(
				Constants.SESSION_DATA);
				edu.wustl.dao.DAO dao = edu.wustl.catissuecore.util.global.AppUtility.openDAOSession(sessionData);
				edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic bizLogic = new edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic();

	    		returnedJObject.put("success", "false");
				
				krishagni.catissueplus.mobile.dto.SpecimenDTO obj = bizLogic.getSpecimenDTOFromLabel(dao, request.getParameter("label")); 
				returnedJObject.put("success", "true");
				returnedJObject.put("cpTitle", obj.getCpShortTitle());
				returnedJObject.put("partName", obj.getParticipantName());
				returnedJObject.put("eventPoint",obj.getEventPoint());
				returnedJObject.put("label",obj.getLabel());
				returnedJObject.put("classType", obj.getSpecimenType());
				returnedJObject.put("tissueSite", obj.getTissueSite());
				returnedJObject.put("availableQuantity", obj.getAvailableQuantity());
				if( obj.getContainerName()!=null){
				returnedJObject.put("container", obj.getContainerName()+" ("+ obj.getPositionDimensionOneString()+","+obj.getPositionDimensionTwoString()+")");
				}else{
					returnedJObject.put("container","Virtually Located ");
				}
				returnedJObject.put("pathologicalStatus", obj.getPathologicalStatus());
				
			}
			
			//response.getWriter().flush();
    	}catch(Exception e){
    		System.out.println("");
		}
		
		response.setContentType("application/json");
    	
		response.getWriter().write(returnedJObject.toString());

		
	}

}
