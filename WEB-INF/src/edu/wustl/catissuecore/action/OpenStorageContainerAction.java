package edu.wustl.catissuecore.action;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.bean.StorageContainerBean;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

public class OpenStorageContainerAction extends BaseAction{
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String operation=request.getParameter("operation");
		HttpSession session = request.getSession();
		StorageContainerForm storageContainerForm = (StorageContainerForm)form;
		StorageContainerBean storageContainerBean=null;
		String target=Constants.SUCCESS;
		String pageOf=request.getParameter(Constants.PAGE_OF);
		
		if("viewMapTab".equals(pageOf))
		{
			 target=pageOf;
			 String id = request.getParameter(Constants.SYSTEM_IDENTIFIER);
			 String activityStatus =request.getParameter(Constants.ACTIVITY_STATUS);
			 request.setAttribute(Constants.SYSTEM_IDENTIFIER, id);
			 request.setAttribute(Constants.ACTIVITY_STATUS,activityStatus);
		}
		if(Constants.ADD.equals(operation))
		{
			session.removeAttribute(Constants.STORAGE_CONTAINER_SESSION_BEAN);
		}
		else
		{				
				storageContainerBean = new StorageContainerBean();
		}
		if(storageContainerForm.getId() != 0 && !"pageOfStorageType".equals(pageOf))
		{
			storageContainerBean.setBarcode(storageContainerForm.getBarcode());
			storageContainerBean.setCheckedButton(storageContainerForm.getCheckedButton());
			storageContainerBean.setCollectionIds(storageContainerForm.getCollectionIds());
			storageContainerBean.setContainerId(storageContainerForm.getContainerId());
			storageContainerBean.setContainerName(storageContainerForm.getContainerName());
			storageContainerBean.setDefaultTemperature(storageContainerForm.getDefaultTemperature());
			storageContainerBean.setHoldsSpecimenArrTypeIds(storageContainerForm.getHoldsSpecimenArrTypeIds());
			storageContainerBean.setHoldsSpecimenClassTypes(storageContainerForm.getHoldsSpecimenClassTypes());
			storageContainerBean.setHoldsStorageTypeIds(storageContainerForm.getHoldsStorageTypeIds());
			storageContainerBean.setTypeId(storageContainerForm.getTypeId());
			storageContainerBean.setTypeName(storageContainerForm.getTypeName());
			storageContainerBean.setId(storageContainerForm.getId());
			storageContainerBean.setParentContainerId(storageContainerForm.getParentContainerId());
			storageContainerBean.setPos1(storageContainerForm.getPos1());
			storageContainerBean.setPos2(storageContainerForm.getPos2());
			storageContainerBean.setPositionDimensionOne(storageContainerForm.getPositionDimensionOne());
			storageContainerBean.setPositionDimensionTwo(storageContainerForm.getPositionDimensionTwo());
			storageContainerBean.setOneDimensionCapacity(storageContainerForm.getOneDimensionCapacity());
			storageContainerBean.setTwoDimensionCapacity(storageContainerForm.getTwoDimensionCapacity());
			storageContainerBean.setOneDimensionLabel(storageContainerForm.getOneDimensionLabel());
			storageContainerBean.setTwoDimensionLabel(storageContainerForm.getTwoDimensionLabel());
			storageContainerBean.setSiteId(storageContainerForm.getSiteId());
			storageContainerBean.setSiteName(storageContainerForm.getSiteName());
			storageContainerBean.setSiteForParentContainer(storageContainerForm.getSiteForParentContainer());
			storageContainerBean.setParentContainerSelected(storageContainerForm.getParentContainerSelected());
		}
		if("pageOfStorageType".equals(pageOf))
		{
			  Long storageTypeIdentifier=(Long)request.getAttribute(Constants.SYSTEM_IDENTIFIER);
			  session.setAttribute("storageTypeIdentifier", storageTypeIdentifier);
			  session.setAttribute("isPageFromStorageType",Constants.YES);
		      session.setAttribute("forwardToHashMap", (HashMap) request.getAttribute("forwardToHashMap"));
		}
		/*
		  storageContainerBean.setCheckedButton(storageContainerForm.getCheckedButton());
		  storageContainerBean.setCollectionIds(storageContainerForm.getCollectionIds());
		  storageContainerBean.setContainerName(storageContainerForm.getContainerName());
		  storageContainerBean.setHoldsSpecimenArrTypeIds(storageContainerForm.getHoldsSpecimenArrTypeIds());
		  storageContainerBean.setHoldsSpecimenClassTypes(storageContainerForm.getHoldsSpecimenClassTypes());
		  storageContainerBean.setHoldsStorageTypeIds(storageContainerForm.getHoldsStorageTypeIds());
		  storageContainerBean.setOneDimensionCapacity(storageContainerForm.getOneDimensionCapacity());
	      storageContainerBean.setTwoDimensionCapacity(storageContainerForm.getTwoDimensionCapacity());
		  storageContainerBean.setOneDimensionLabel(storageContainerForm.getOneDimensionLabel());
	      storageContainerBean.setTwoDimensionLabel(storageContainerForm.getTwoDimensionLabel());
		*/
		session.setAttribute(Constants.STORAGE_CONTAINER_SESSION_BEAN, storageContainerBean);
		request.setAttribute(Constants.OPERATION,operation);
		return mapping.findForward(target);
	}

}
