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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.cde.CDEManager;

/**
 * This class initializes the fields in the EmbeddedEventParameters Add/Edit webpage.
 
 */
public class EmbeddedEventParametersAction extends SpecimenEventParametersAction
{
	protected void setRequestParameters(HttpServletRequest request) throws Exception
	{
		//set array of EmbeddingMedium
		List embeddingMediumList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_EMBEDDING_MEDIUM,null);
    	request.setAttribute(Constants.EMBEDDING_MEDIUM_LIST, embeddingMediumList);
	}
}