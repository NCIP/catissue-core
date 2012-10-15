package krishagni.catissueplus.mobile.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.catissuecore.bizlogic.SpecimenEventParametersBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.exception.DAOException;

public class TransferSpecimenAliquots  extends HttpServlet {
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
		
		JSONObject returnedJObject= new JSONObject();
		String msg;
		String successString;
		
		try{
			final SessionDataBean sessionData = (SessionDataBean) request.getSession().getAttribute(
					Constants.SESSION_DATA);
			if(sessionData != null){
			
				SpecimenEventParametersBizLogic bizLogic = new SpecimenEventParametersBizLogic();
				String pos1 = request.getParameter("positionX");
				String pos2 = request.getParameter("positionY");
				String specimenLabel = request.getParameter("specimenLable");
				String containerName = request.getParameter("containerName");
				
				msg = bizLogic.specimenEventTransferFromMobile(sessionData,specimenLabel,containerName,pos1,pos2);
				successString = "success";
			}else{
				msg = "";
				successString = "logout";
			}
		}catch(BizLogicException ex){
			msg = ex.getMsgValues();
			successString = "failure";
		}catch(DAOException ex){
			msg = ex.getMsgValues();
			successString = "failure";
		}
		
		try {
			returnedJObject.put("msg", msg);
			returnedJObject.put("success", successString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setContentType("application/json");
    	response.getWriter().write(returnedJObject.toString());

	    
		
	}
	

}
