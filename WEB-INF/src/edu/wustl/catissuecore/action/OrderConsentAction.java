package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.OrderBizLogic;
import edu.wustl.catissuecore.dto.ConsentDTO;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.dao.HibernateDAO;

public class OrderConsentAction extends BaseAction
{

	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		HibernateDAO dao = null;
		try
		{
			dao=(HibernateDAO) AppUtility.openDAOSession(null);
			OrderBizLogic orderBizLogic=new OrderBizLogic();
			String labelsString=request.getParameter("specimenLabels");
			List<String> specimenLabels=new ArrayList<String>();
			if(labelsString!=null || labelsString!=null)
			{
				specimenLabels.addAll(Arrays.asList(request.getParameter("specimenLabels").split(",")));
			}
			
			Collection<ConsentDTO> consents=orderBizLogic.getConsentDetails(specimenLabels, dao);
			request.setAttribute("ConsentDTOs", consents);

		}
	    finally
	    {
	    	dao.closeSession();
	    }
		return mapping.findForward(Constants.SUCCESS);// ViewAll
	}
}

