/**
 * <p>Title: EmbeddedEventParametersAction Class>
 * <p>Description:	This class initializes the fields in the EmbeddedEventParameters Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on Aug 5, 2005
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
		request.setAttribute(Constants.EMBEDDINGMEDIUMLIST, Constants.EMBEDDINGMEDIUMARRAY);
        
	}
}