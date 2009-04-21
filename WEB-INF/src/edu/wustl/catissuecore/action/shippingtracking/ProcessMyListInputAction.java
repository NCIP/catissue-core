package edu.wustl.catissuecore.action.shippingtracking;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.shippingtracking.BaseShipmentForm;
import edu.wustl.common.action.SecureAction;
/**
 * this class implements the action for MyList inputs.
 */
public class ProcessMyListInputAction extends SecureAction
{
	/**
	 * the method implements the MyList input action.
	 * @param mapping object of ActionMapping class.
	 * @param form object of ActionForm class.
	 * @param request object of HttpServletRequest class.
	 * @param response object of HttpServletResponse class.
	 * @return forward mapping.
	 */
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		if(form instanceof BaseShipmentForm)
		{
			BaseShipmentForm shipmentForm=(BaseShipmentForm)form;
			if(request.getSession().getAttribute(edu.wustl.catissuecore.util.global.Constants.SPECIMEN_LABELS_LIST)!=null)
			{
				List<String> specimenLabels = (List<String>)request.getSession().getAttribute(edu.wustl.catissuecore.util.global.Constants.SPECIMEN_LABELS_LIST);
				//if(specimenLabels.size()>0)
				if(!(specimenLabels.isEmpty()))
				{
					setSpecimenLabels(shipmentForm,specimenLabels);
				}
			}
			if(request.getSession().getAttribute(edu.wustl.catissuecore.util.global.Constants.CONTAINER_NAMES_LIST)!=null)
			{
				List<String> containerNames = (List<String>)request.getSession().getAttribute(edu.wustl.catissuecore.util.global.Constants.CONTAINER_NAMES_LIST);
				//if(containerNames.size()>0)
				if(!(containerNames.isEmpty()))
				{
					setContainerNames(shipmentForm,containerNames);
				}
			}
			shipmentForm.setOperation(edu.wustl.catissuecore.util.global.Constants.ADD);
		}
		return mapping.findForward(edu.wustl.catissuecore.util.global.Constants.SUCCESS);
	}
	/**
	 * sets the container names.
	 * @param shipmentForm form in which container names are to be set.
	 * @param containerNames list of container names.
	 */
	private void setContainerNames(BaseShipmentForm shipmentForm, List<String> containerNames)
	{
		shipmentForm.setContainerLabelChoice("ContainerLabel");
		int containerCounter=0;
		for (String containerName : containerNames)
		{
			containerCounter++;
			shipmentForm.setContainerDetails("containerLabel_"+containerCounter,containerName);
		}
		shipmentForm.setContainerCounter(containerCounter);
	}
	/**
	 * sets the specimen labels.
	 * @param shipmentForm form in which labels are to be set.
	 * @param specimenLabels list of labels.
	 */
	private void setSpecimenLabels(BaseShipmentForm shipmentForm, List<String> specimenLabels)
	{
		shipmentForm.setSpecimenLabelChoice("SpecimenLabel");
		int specimenCounter=0;
		for (String specimenLabel : specimenLabels)
		{
			specimenCounter++;
			shipmentForm.setSpecimenDetails("specimenLabel_"+specimenCounter, specimenLabel);
		}
		shipmentForm.setSpecimenCounter(specimenCounter);
	}
}
