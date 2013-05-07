package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.wustl.catissuecore.storage.StorageContainerGridObject;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;


public class StoragePositionAjaxAction extends SecureAction
{
	/**
	 * This function creates the json object of the information required about container 
	 * and send it back as response.
	 * @param mapping object of ActionMapping class.
	 * @param form object of ActionForm class.
	 * @param request object of HttpServletRequest class.
	 * @param response object of HttpServletResponse class.
	 * @return forward mapping.
	 * @throws Exception if some problem occurs.
	 */
	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
    {
		String containerName = request.getParameter(Constants.CONTAINER_NAME);
		if("null".equals(containerName))
		{
			containerName = (String) request.getAttribute(Constants.CONTAINER_NAME);
		}
		StorageContainerGridObject storageContainerGridObject = null;
		storageContainerGridObject = new StorageContainerGridObject();
		storageContainerGridObject=StorageContainerUtil.getContainerDetails(containerName);
		ActionErrors errors = new ActionErrors();
		if(null==storageContainerGridObject)
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.specimen.storageContainerEditBox"));
		}
		
		if(storageContainerGridObject!=null)
		{
			boolean[][] availablePositions=storageContainerGridObject.getAvailablePositions();
			JSONArray array = new JSONArray();
			JSONArray titleArray = new JSONArray();
			JSONObject dimensionOne = new JSONObject();
			JSONObject dimensionTwo = new JSONObject();
			
			
			for(int i=0;i<storageContainerGridObject.getOneDimensionCapacity();i++)
			{
				dimensionOne.put(String.valueOf(i+1), AppUtility.getPositionValue(storageContainerGridObject.getOneDimensionLabellingScheme(), i+1));
			}
			for(int i=0;i<storageContainerGridObject.getTwoDimensionCapacity();i++)
			{
				dimensionTwo.put(String.valueOf(i+1), AppUtility.getPositionValue(storageContainerGridObject.getTwoDimensionLabellingScheme(), i+1));
			}
			for(int i=1;i<=storageContainerGridObject.getOneDimensionCapacity();i++)
			{
				JSONArray rowarray = new JSONArray();
				for(int j=1;j<=storageContainerGridObject.getTwoDimensionCapacity();j++)
				{
					JSONObject obj = new JSONObject();
					obj.put(j+"",  availablePositions[i][j]);
					rowarray.put(obj);
				}
				array.put(rowarray);
			}
			
			for(int i=1;i<=storageContainerGridObject.getOneDimensionCapacity();i++)
			{
				JSONArray rowarray = new JSONArray();
				for(int j=1;j<=storageContainerGridObject.getTwoDimensionCapacity();j++)
				{
					JSONObject obj = new JSONObject();
					obj.put(j+"",  AppUtility.getPositionValue(storageContainerGridObject.getOneDimensionLabellingScheme(),i)+","+AppUtility.getPositionValue(storageContainerGridObject.getTwoDimensionLabellingScheme(),j));
					rowarray.put(obj);
				}
				titleArray.put(rowarray);
			}
			
			JSONObject dimensionOneCapacity = new JSONObject();
			JSONObject dimensionTwoCapacity = new JSONObject();
			dimensionOneCapacity.put("dimensionOneCapacity", storageContainerGridObject.getOneDimensionCapacity());
			dimensionTwoCapacity.put("dimensionTwoCapacity", storageContainerGridObject.getTwoDimensionCapacity());
			
			JSONObject returnedJObject= new JSONObject();
			
		
			returnedJObject.put("dimensionOne",dimensionOne.toString());
			returnedJObject.put("dimensionTwo",dimensionTwo.toString());
			returnedJObject.put("dimensionOneLabel",storageContainerGridObject.getOneDimensionLabel().toString());
			returnedJObject.put("dimensionTwoLabel",storageContainerGridObject.getTwoDimensionLabel().toString());
			returnedJObject.put("containerMap",array.toString());
			returnedJObject.put("titleMap",titleArray.toString());
			returnedJObject.put("dimensionOneCapacity", storageContainerGridObject.getOneDimensionCapacity());
			returnedJObject.put("dimensionTwoCapacity", storageContainerGridObject.getTwoDimensionCapacity());
			returnedJObject.put("pos1ControlName",dimensionOne.toString());
			returnedJObject.put("pos2ControlName",dimensionTwo.toString());
			
			returnedJObject.put("pos1ControlName",request.getSession().getAttribute(Constants.POS1));
			returnedJObject.put("pos2ControlName",request.getSession().getAttribute(Constants.POS2));
			returnedJObject.put("controlName",request.getSession().getAttribute("controlName"));
			returnedJObject.put("pageOf",request.getSession().getAttribute(Constants.PAGEOF));
			response.setContentType("application/json");
			
			response.getWriter().write(returnedJObject.toString());
		}
		return null;

	}

}
