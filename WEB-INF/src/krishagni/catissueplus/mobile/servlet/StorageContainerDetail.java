package krishagni.catissueplus.mobile.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.wustl.catissuecore.util.global.Constants;
import org.json.JSONObject;
import org.json.JSONArray;
import krishagni.catissueplus.mobile.dto.StoragePositionDTO;
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
		
		try{
			
			if(request.getParameter("containerName")!=null){
				final edu.wustl.common.beans.SessionDataBean sessionData = (edu.wustl.common.beans.SessionDataBean) request.getSession().getAttribute(
				Constants.SESSION_DATA);
				edu.wustl.dao.DAO dao = edu.wustl.catissuecore.util.global.AppUtility.openDAOSession(sessionData);
				edu.wustl.catissuecore.bizlogic.StorageContainerForSpecimenBizLogic bizLogic = new edu.wustl.catissuecore.bizlogic.StorageContainerForSpecimenBizLogic();
				String containerName = request.getParameter("containerName");
				StoragePositionDTO[][] storageContainer = bizLogic.getPositionDetailsFromContainer(containerName,dao);
				returnedJObject.put("success", "false");
				JSONArray array = new JSONArray();
				for(int i = 0;i < storageContainer.length;i++){
					JSONArray rowarray = new JSONArray();
					for(int j = 0 ;j < storageContainer[i].length;j++){
						JSONObject obj = new JSONObject();
						StoragePositionDTO dto = storageContainer[i][j];
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
				returnedJObject.put("containerMap",array.toString());
				
			}
			
			//response.getWriter().flush();
		}catch(Exception e){
			System.out.println("");
		}
		
		response.setContentType("application/json");
		
		response.getWriter().write(returnedJObject.toString());
		
	
	}


}
