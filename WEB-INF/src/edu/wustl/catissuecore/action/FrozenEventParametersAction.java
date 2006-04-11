/**
 * <p>Title: FrozenEventParametersAction Class>
 * <p>Description:	This class initializes the fields in the FrozenEventParameters Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 28, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.cde.CDEManager;

/**
 * This class initializes the fields in the FrozenEventParameters Add/Edit webpage.
 * @author mandar deshmukh
 */
public class FrozenEventParametersAction extends SpecimenEventParametersAction
{
	protected void setRequestParameters(HttpServletRequest request) throws Exception
	{
		//set array of methods
        List methodList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_METHOD,null);
    	request.setAttribute(Constants.METHOD_LIST, methodList);
	}
}