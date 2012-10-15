package krishagni.catissueplus.mobile.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import krishagni.catissueplus.mobile.dto.SpecimenDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.catissuecore.bizlogic.AliquotBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;

public class DisplayAction  extends HttpServlet {

	
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
		try{
			final SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(
					Constants.SESSION_DATA);
			String type = request.getParameter("type");
			returnedJObject.put("success", "false");
			if("displayAliquots".equals(type)){
				AliquotBizLogic bizLogic = new AliquotBizLogic();
				String parentSpecimenLabel = request.getParameter("label");
				JSONObject commonDetail = new JSONObject();
				List<SpecimenDTO> list = bizLogic.getAliquotDetail(parentSpecimenLabel, sessionDataBean);
				if(list!=null && list.size()>0){
					SpecimenDTO dtoObj = list.get(0);
					commonDetail.put("pathologicalStatus", dtoObj.getPathologicalStatus());
					commonDetail.put("tissueSide",dtoObj.getTissueSide());
					commonDetail.put("tissueSite",dtoObj.getTissueSite());
					commonDetail.put("type",dtoObj.getSpecimenType());
					
					JSONArray aliquotArray = new JSONArray();
					for(int i = 0; i < list.size(); i ++){
						dtoObj = list.get(i);
						JSONObject obj = new JSONObject();
						obj.put("label",dtoObj.getLabel());
						obj.put("contDetail",dtoObj.getContainerName()+ " ("+dtoObj.getPositionDimensionOneString()+","+dtoObj.getPositionDimensionTwoString()+") ");
						aliquotArray.put(obj);
					}
					returnedJObject.put("commonDetail",commonDetail.toString());
					returnedJObject.put("aliquotList",aliquotArray.toString());
				}
			} 
			returnedJObject.put("success", "true");
			
		}catch(ApplicationException e){
			e.getMsgValues();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setContentType("application/json");
   	
		response.getWriter().write(returnedJObject.toString());


	}

	
}
