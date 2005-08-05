/**
 * <p>Title: FrozenEventParametersAction Class>
 * <p>Description:	This class initializes the fields in the FrozenEventParameters Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Jyoti Singh
 * @version 1.00
 * Created on Aug 2, 2005
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.util.global.Constants;

/**
 * This class initializes the fields in the EmbeddedEventParameters Add/Edit webpage.
 
 */
public class EmbeddedEventParametersAction extends SpecimenEventParametersAction
{
	protected void setRequestParameters(HttpServletRequest request)
	{
		super.setRequestParameters(request);
		
		//set array of EmbeddingMedium
		//Sets the hourList attribute to be used in the Add/Edit EmbeddedEventParameters Page.
		request.setAttribute(Constants.EMBEDDINGMEDIUMLIST, Constants.EMBEDDINGMEDIUMARRAY);
        
	}
}