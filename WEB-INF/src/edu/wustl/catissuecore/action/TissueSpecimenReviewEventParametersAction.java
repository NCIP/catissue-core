/**
 * <p>Title: TissueSpecimenReviewEventParametersAction Class>
 * <p>Description:	This class initializes the fields in the TissueSpecimenReviewEventParameters Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on Aug 31, 2005
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.util.global.Constants;


/**
 * @author mandar_deshmukh
 * This class initializes the fields in the TissueSpecimenReviewEventParameters Add/Edit webpage.
 * 
 */
public class TissueSpecimenReviewEventParametersAction extends SpecimenEventParametersAction
{
	protected void setRequestParameters(HttpServletRequest request) throws Exception
	{
//		set array of histological quality
		request.setAttribute(Constants.HISTOLOGICALQUALITYLIST , Constants.HISTOLOGICALQUALITYARRAY );
	}
}
