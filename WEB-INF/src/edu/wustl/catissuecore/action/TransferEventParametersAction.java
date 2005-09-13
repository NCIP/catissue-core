/**
 * <p>Title: TransferEventParametersAction Class>
 * <p>Description:	This class initializes the fields in the TransferEventParameters Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on Aug 05, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.bizlogic.AbstractBizLogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * @author mandar_deshmukh
 * This class initializes the fields in the TransferEventParameters Add/Edit webpage.
 */
public class TransferEventParametersAction extends SpecimenEventParametersAction
{

	protected void setRequestParameters(HttpServletRequest request)
	{
		super.setRequestParameters(request);

		try
		{
			String[] displayNameFields = {"number"};
			String valueField = "systemIdentifier";
			AbstractBizLogic dao = BizLogicFactory.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);

			//Sets the TO and FROM StorageContainerList attribute to be used in the Add/Edit User Page.
			String sourceObjectName = StorageContainer.class.getName();
			List fromContainerList = dao.getList(sourceObjectName, displayNameFields, valueField);
			request.setAttribute(Constants.FROMCONTAINERLIST, fromContainerList);

			List toContainerList = dao.getList(sourceObjectName, displayNameFields, valueField);
			request.setAttribute(Constants.TOCONTAINERLIST, toContainerList);
		}
		catch (Exception exc)
		{
			Logger.out.error(exc.getMessage());
		}
	}
}