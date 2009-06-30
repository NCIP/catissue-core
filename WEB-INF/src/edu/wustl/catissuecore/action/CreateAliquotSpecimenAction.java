
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.AliquotForm;
import edu.wustl.catissuecore.bizlogic.OrderBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;

/**
 * @author renuka_bajpai
 *
 */
public class CreateAliquotSpecimenAction extends BaseAction
{

	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String specimenId = request.getParameter("specimenId");
		Specimen specimen = null;
		if (specimenId != null)
		{
			IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			OrderBizLogic orderBizLogic = (OrderBizLogic) factory
					.getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
			specimen = (Specimen) orderBizLogic.getSpecimenObject(Long.parseLong(specimenId));

		}
		System.out.println("");
		AliquotForm aliquotForm = (AliquotForm) form;
		aliquotForm.setSpecimenLabel(specimen.getLabel());
		aliquotForm.setNoOfAliquots("1");
		aliquotForm.setForwardTo("orderDetails");
		return mapping.findForward("aliquotSpecimen");
	}

}
