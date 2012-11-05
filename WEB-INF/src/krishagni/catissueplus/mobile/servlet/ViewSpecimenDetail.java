package krishagni.catissueplus.mobile.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.exception.DAOException;

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
		String errorMsg = "";

		try{
			final edu.wustl.common.beans.SessionDataBean sessionData = (edu.wustl.common.beans.SessionDataBean) request.getSession().getAttribute(
					Constants.SESSION_DATA);
			if(sessionData != null){
				
				if(request.getParameter("label")!=null){
					edu.wustl.dao.DAO dao = edu.wustl.catissuecore.util.global.AppUtility.openDAOSession(sessionData);
					edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic bizLogic = new edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic();
	
		    		returnedJObject.put("success", "false");
					
					krishagni.catissueplus.mobile.dto.SpecimenDTO obj = bizLogic.getSpecimenDTOFromLabel(dao, request.getParameter("label")); 
					returnedJObject.put("success", "true");
					returnedJObject.put("cpTitle", obj.getCpShortTitle());
					returnedJObject.put("partName", obj.getParticipantName()+" ("+obj.getProtocol_participant_id()+")");
					returnedJObject.put("eventPoint",obj.getEventPoint());
					returnedJObject.put("label",obj.getLabel());
					returnedJObject.put("classType", obj.getSpecimenClass()+"( "+obj.getSpecimenType()+")");
					returnedJObject.put("tissueSite", obj.getTissueSite()+" ("+obj.getTissueSide()+")");
					returnedJObject.put("availableQuantity", obj.getAvailableQuantity());
					if( obj.getContainerName()!=null){
					returnedJObject.put("container", obj.getContainerName()+" ("+ obj.getPositionDimensionOneString()+","+obj.getPositionDimensionTwoString()+")");
					}else{
						returnedJObject.put("container","Virtually Located ");
					}
					returnedJObject.put("pathologicalStatus", obj.getPathologicalStatus());
					
				}
			}else{
				returnedJObject.put("success", "logout");
			}
			
			//response.getWriter().flush();
    	}catch(DAOException e){
    		errorMsg = Constants.INVALID_LABEL_BARCODE;
    	}catch(BizLogicException e){
			errorMsg = Constants.INVALID_LABEL_BARCODE;
    	}catch(Exception e){
    		errorMsg = Constants.INVALID_LABEL_BARCODE;
		}
		
		try{ 
			returnedJObject.put("errMsg", errorMsg);
		}catch(JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setContentType("application/json");
    	
		response.getWriter().write(returnedJObject.toString());

		
	}

}
