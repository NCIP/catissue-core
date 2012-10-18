package krishagni.catissueplus.mobile.servlet;

import java.io.IOException;
import java.util.Calendar;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import krishagni.catissueplus.mobile.dto.AliquotsDetailsDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.catissuecore.bizlogic.AliquotBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import krishagni.catissueplus.mobile.dto.SpecimenDTO;


public class CreateAliquot  extends HttpServlet  {
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
		String msg = "";
		try{
			final SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(
					Constants.SESSION_DATA);
			if(sessionDataBean != null){
				
	
				AliquotsDetailsDTO aliquotDetailObj = getAliqoutDetail(request);
				AliquotBizLogic bizLogic = new AliquotBizLogic();
				returnedJObject.put("success", "false");
				List<SpecimenDTO> list;
				if(aliquotDetailObj.isBasedOnCP()){
					list =  bizLogic.createAliquotSpecimenBasedOnCp(aliquotDetailObj, sessionDataBean);
				} else {
					list = bizLogic.createAliquotSpecimen(aliquotDetailObj,sessionDataBean);
				}
				JSONObject commonDetail = new JSONObject();
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
				
				
				
				returnedJObject.put("success", "true");
			}else{
				msg = "";
				returnedJObject.put("success", "logout");
			}
			
		}catch(BizLogicException e){
			msg = e.getMsgValues();
		} catch(ApplicationException e){
			msg = e.getMsgValues();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		}
		
		try {
			returnedJObject.put("msg", msg);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.setContentType("application/json");
    	
		response.getWriter().write(returnedJObject.toString());


	}
	
	public AliquotsDetailsDTO getAliqoutDetail(HttpServletRequest request){
		AliquotsDetailsDTO obj = new AliquotsDetailsDTO();
		obj.setContainerName(request.getParameter("containerName"));
		obj.setParentSpecimenLabel( request.getParameter("specimenLable"));
		if(request.getParameter("basedOnCp") == null || "no".equals(request.getParameter("basedOnCp"))){
			obj.setBasedOnCP(false);
		}else{
			obj.setBasedOnCP(true);
		}
		
		if(request.getParameter("noOfAliquots")!=null){
			obj.setNoOfAliquots(Integer.parseInt(request.getParameter("noOfAliquots")));
		}else{
			obj.setNoOfAliquots(0);
		}
		if(request.getParameter("quantity") != null && !"".equals(request.getParameter("quantity").trim())){
			obj.setQuantityPerAliquot(Double.parseDouble(request.getParameter("quantity")));
		}else{
			obj.setQuantityPerAliquot(0.0);
		}
		
		obj.setStartingStoragePositionX(request.getParameter("positionX"));
		obj.setCreatedDate(Calendar.getInstance().getTime());
		obj.setStartingStoragePositionY(request.getParameter("positionY"));
		if("checked".equals(request.getParameter("disposeCheck"))){
			obj.setDisposeParentCheck(true);
		}else{
			obj.setDisposeParentCheck(false);
		}
		if("checked".equals(request.getParameter("printLabel"))){
			obj.setPrintLabel(true);
		}else{
			obj.setPrintLabel(false);
		}
			
		return obj;
	}
		
}
