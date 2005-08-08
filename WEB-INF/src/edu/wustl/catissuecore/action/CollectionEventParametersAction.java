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

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.util.global.Constants;


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
	    request.setAttribute(Constants.PROCEDURELIST, Constants.PROCEDUREARRAY);
	    
	    // set the container lists
	    request.setAttribute(Constants.CONTAINERLIST, Constants.CONTAINERARRAY);
	}
}
