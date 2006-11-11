/*
 * Created on Oct 3, 2006
 * 
 */
package edu.wustl.catissuecore.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.AliquotForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.IBizLogic;


/**
 * @author jitendra_agrawal
 */
public class AliquotSummaryAction extends BaseAction
{

	/**
	 * This method will get call from Aliquot Summary page. 
	 * Overrides the execute method of Action class.
	 */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{		
		AliquotForm aliquotForm = (AliquotForm) form;
		String noOfAliquouts = aliquotForm.getNoOfAliquots();
		Map aliquotMap = aliquotForm.getAliquotMap();
		aliquotMap.put("noOfAliquots",noOfAliquouts);
		String target = Constants.FAILURE;
		if(aliquotForm.getForwardTo() != null && aliquotForm.getForwardTo().equals("sameCollectionGroup"))
		{
			String labelKey = "Specimen:1_label";
			String label = (String) aliquotMap.get(labelKey);
			String sourceObjectName = Specimen.class.getName();
			String[] whereColumnName = new String[1];
			Object[] whereColumnValue = new Object[1];		
			whereColumnName[0] = Constants.SYSTEM_LABEL;
			whereColumnValue[0] = label;
			String[] whereColumnCondition = {"="};
			String joinCondition = null;
			IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
			List list = bizLogic.retrieve(sourceObjectName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);

			/**
			 *  If list is not empty, set the Parent specimen Id and forward to success. 
			 *  If list is null or empty, forward to failure.
			 */
			if (list != null && !list.isEmpty())
			{
				Object obj = list.get(0);
				Specimen specimen = (Specimen) obj;
				Map forwardToHashMap = new HashMap();
				forwardToHashMap.put("specimenCollectionGroupId", specimen.getSpecimenCollectionGroup().getId().toString());
				request.setAttribute("forwardToHashMap", forwardToHashMap);
				target= aliquotForm.getForwardTo();
			}
			else
			{
				target = Constants.FAILURE;
			}
		}
		else if(aliquotForm.getForwardTo() != null && aliquotForm.getForwardTo().equals("distribution"))
		{		
			request.setAttribute("forwardToHashMap", aliquotMap);
			target= aliquotForm.getForwardTo();
		}
		
		return mapping.findForward(target);
	}
}
