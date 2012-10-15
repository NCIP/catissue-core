package krishagni.catissueplus.mobile.servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.exception.DAOException;

public class CatissueMobileAppCommonServlet  extends HttpServlet {
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
		JSONArray returnedJObjectArray = new JSONArray();
		
		String successString="";
		try{
			final SessionDataBean sessionData = (SessionDataBean) request.getSession().getAttribute(
					Constants.SESSION_DATA);
			if(sessionData != null){
				
				StorageContainerBizLogic bizLogic = new StorageContainerBizLogic();
				Map<Long,String> map = bizLogic.getStorageContainers();
				final Iterator<Long> iterator = map.keySet().iterator();
				while(iterator.hasNext()){
					JSONObject tempObj= new JSONObject();
					Long key = iterator.next();
					tempObj.put("value",key);
					tempObj.put("label",map.get(key));
					returnedJObjectArray.put(tempObj);
				}
				successString = "success";
			}else{
				successString = "logout";
			}
			
		}catch(BizLogicException ex){
			
		}catch(DAOException ex){
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
		}
		
		try {
			returnedJObject.put("success", successString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		response.setContentType("application/json");
		response.getWriter().write(returnedJObjectArray.toString());

	    
		
	}
	

}

