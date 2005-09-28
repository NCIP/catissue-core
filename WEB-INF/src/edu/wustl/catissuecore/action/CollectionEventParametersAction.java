/**
 * <p>Title: CollectionEventParametersAction Class>
 * <p>Description:	This class initializes the fields in the CollectionEventParameters Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on Aug 04, 2005
 */
package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.cde.CDEManager;


/**
 * @author mandar_deshmukh
 *
 * This class initializes the fields in the CollectionEventParameters Add/Edit webpage.
 */
public class CollectionEventParametersAction extends SpecimenEventParametersAction
{
	protected void setRequestParameters(HttpServletRequest request)
	{
		super.setRequestParameters(request);
		
		// set the procedure lists
		List procedureList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_COLLECTION_PROCEDURE,null);
    	request.setAttribute(Constants.PROCEDURELIST, procedureList);
	    
	    // set the container lists
    	List containerList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_CONTAINER,null);
    	request.setAttribute(Constants.CONTAINERLIST, containerList);
	}
}
