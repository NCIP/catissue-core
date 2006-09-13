/**
 * <p>Title: QuickEventsAction Class</p>
 * <p>Description:  This class initializes the atributes required for the QuickEvents webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 3, 2006
 */
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.IBizLogic;



/**
 * Created : 03-July-2006
 * @author mandar_deshmukh
 *
 * This class initializes the atributes required for the QuickEvents webpage.
 */
public class QuickEventsAction extends BaseAction {

    /**
     * Overrides the execute method of Action class.
     * Sets the various fields in QuickEvents webpage.
     * */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
//		DefaultBizLogic bizLogic = BizLogicFactory.getDefaultBizLogic();
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
   		String [] fields = {Constants.SYSTEM_IDENTIFIER};
     /*   List specimenList = bizLogic.getList(Specimen.class.getName(), fields, Constants.SYSTEM_IDENTIFIER, true); 	 	
  	 	request.setAttribute(Constants.SPECIMEN_ID_LIST, specimenList); */
  	 	
  	 	request.setAttribute(Constants.EVENT_PARAMETERS_LIST,Constants.EVENT_PARAMETERS);
  	 	
  	 	String pageOf = Constants.SUCCESS;
  	 	
        return mapping.findForward(pageOf );
    }
}
