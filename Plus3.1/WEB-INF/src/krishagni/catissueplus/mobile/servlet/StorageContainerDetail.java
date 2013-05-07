package krishagni.catissueplus.mobile.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import krishagni.catissueplus.mobile.dto.StoragePositionDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.catissuecore.storage.StorageContainerGridObject;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
public class StorageContainerDetail  extends HttpServlet {
	
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
			returnedJObject.put("success", "false");
			final edu.wustl.common.beans.SessionDataBean sessionData = (edu.wustl.common.beans.SessionDataBean) request.getSession().getAttribute(
					Constants.SESSION_DATA);
			if(sessionData != null){
				
			if(request.getParameter("containerName")!=null){
				edu.wustl.dao.DAO dao = edu.wustl.catissuecore.util.global.AppUtility.openDAOSession(sessionData);
				String containerName = request.getParameter("containerName");
				StorageContainerGridObject scGridObject = StorageContainerUtil.getContainerDetails(containerName,dao);
				StoragePositionDTO[][] storageContainer = scGridObject.getPositionDetails();
				
				String oneDimensionLabellingScheme=(String) scGridObject.getOneDimensionLabellingScheme();
				String twoDimensionLabellingScheme=(String) scGridObject.getTwoDimensionLabellingScheme();
				
			
				JSONArray array = new JSONArray();
				JSONObject dimensionOne = new JSONObject();
				JSONObject dimensionTwo = new JSONObject();
				for(int i = 0;i < storageContainer.length;i++){
					JSONArray rowarray = new JSONArray();
					dimensionOne.put(String.valueOf(i+1), AppUtility.getPositionValue(oneDimensionLabellingScheme, i+1));
					for(int j = 0 ;j < storageContainer[i].length;j++){
						JSONObject obj = new JSONObject();
						StoragePositionDTO dto = storageContainer[i][j];
						dimensionTwo.put(String.valueOf(j+1), AppUtility.getPositionValue(twoDimensionLabellingScheme, j+1));
						
						if(dto != null){
							obj.put(j+"",  true);
							if(dto.getSpecimenDTO()!=null){
								obj.put("specimenLabel",dto.getSpecimenDTO().getLabel());
								obj.put("cpShortTitle",dto.getSpecimenDTO().getCpShortTitle());
								obj.put("type", dto.getSpecimenDTO().getSpecimenType());
								obj.put("availabelQuantity", dto.getSpecimenDTO().getAvailableQuantity());
							}else{
								obj.put("childContainerId",dto.getChildContainerId());
								obj.put("childContainerName",dto.getChildContainerLabel());
								obj.put("childContainerCapacityX",dto.getChildContainerPosxCap());
								obj.put("childContainerCapacityY",dto.getChildContainerPosyCap());
								
							}
							obj.put("containerId",dto.getContainerId());
							obj.put("containerName",dto.getContainerName());
						}else{
							obj.put(j+"",  false);
						}
						rowarray.put(obj);
					}
					array.put(rowarray);
					
				}
				
				
				returnedJObject.put("posx",storageContainer.length);
				returnedJObject.put("posy",storageContainer[1].length);
				returnedJObject.put("success", "success");
				returnedJObject.put("dimensionOne",dimensionOne.toString());
				returnedJObject.put("dimensionTwo",dimensionTwo.toString());
				returnedJObject.put("occupiedPositions",scGridObject.getOccupiedPositons());
				returnedJObject.put("capacity",(scGridObject.getOneDimensionCapacity()*scGridObject.getTwoDimensionCapacity()));
				returnedJObject.put("oneLabel",scGridObject.getOneDimensionLabel());
				returnedJObject.put("twoLabel", scGridObject.getTwoDimensionLabel());
				returnedJObject.put("containerType",scGridObject.getType());
				
				returnedJObject.put("containerMap",array.toString());
			}else{
				msg = "";
				returnedJObject.put("success", "logout");
				
			}
				
			}
			
			//response.getWriter().flush();
		}catch(Exception e){
			msg = "Please Check Container Name or Barcode.";
			System.out.println("");
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


}
