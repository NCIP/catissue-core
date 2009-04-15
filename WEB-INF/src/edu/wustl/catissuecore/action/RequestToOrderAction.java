/*
 * Copyright: Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 */

package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Permissions;
import edu.wustl.security.manager.SecurityManagerFactory;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;
import flex.messaging.io.ArrayList;
import gov.nih.nci.security.authorization.domainobjects.Role;

/**
 * <p>Title: RequestToOrderAction Class>
 * <p>Description:	This class initializes the fields of jsp page to request the bio-specimens.</p>
 * @author deepti_phadnis
 * @version 1.00
 */
public class RequestToOrderAction extends BaseAction
{

	private transient Logger logger = Logger.getCommonLogger(RequestToOrderAction.class);
	/**
	 * Method to initialize the fields required on the UI pages.
	 * @param mapping Struts's ActionMapping
	 * @param form Referance to associated actionform with action and jsp
	 * @param request Referance to HTTP request 
	 * @param response Referance to HTTP response
	 * @return Returns the path of the mapped JSP/Action to which page will be forwared after sucessful execution of action
	 * @throws Exception throws exception in case of any logical or runtime error.
	 */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//Sets the Distribution Protocol Id List.
		SessionDataBean sessionLoginInfo = getSessionData(request);
		Long loggedInUserID = sessionLoginInfo.getUserId();
		long csmUserId = new Long(sessionLoginInfo.getCsmUserId()).longValue();
		Role role = SecurityManagerFactory.getSecurityManager().getUserRole(csmUserId);

		List distributionProtocolList = loadDistributionProtocol(loggedInUserID, role.getName(),
				sessionLoginInfo);
		request.setAttribute(Constants.DISTRIBUTIONPROTOCOLLIST, distributionProtocolList);
		return mapping.findForward("requestOrderPage");
	}

	/**
	 * This method loads the title as Name and id as value of distribution protocol from database 
	 * and return the namevalue bean of ditribution protocol for a given PI.
	 * @param piID User id of PI for which all the distribution protocol is to be loaded.
	 * @return Returns the list of namevalue bean of ditribution protocol for a given PI.
	 * @throws BizLogicException 
	 * @throws DAOException Throws DAOException if any database releated error occures
	 **/
	private List loadDistributionProtocol(final Long piID, String roleName,
			SessionDataBean sessionDataBean) throws BizLogicException, DAOException
	{
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		List distributionProtocolList = new ArrayList();

		String sourceObjectName = DistributionProtocol.class.getName();
		String[] displayName = {"title"};
		String valueFieldCol = Constants.ID;
		DAO dao = null;

		String[] whereColNames = {Status.ACTIVITY_STATUS.toString()};
		String[] whereColCond = {"!="};
		Object[] whereColVal = {Status.ACTIVITY_STATUS_CLOSED.toString()};
		String separatorBetweenFields = "";

		// checking for the role. if role is admin / supervisor then show all the distribution protocols.
		if (roleName.equals(Constants.ADMINISTRATOR) || roleName.equals(Constants.SUPERVISOR))
		{
			distributionProtocolList = bizLogic.getList(sourceObjectName, displayName,
					valueFieldCol, whereColNames, whereColCond, whereColVal,
					Constants.AND_JOIN_CONDITION, separatorBetweenFields, true);
		}
		else
		{
			String[] whereColumnName = {"principalInvestigator.id",
					Status.ACTIVITY_STATUS.toString()};
			String[] colCondition = {"=", "!="};
			Object[] whereColumnValue = {piID, Status.ACTIVITY_STATUS_CLOSED.toString()};
			String joinCondition = Constants.AND_JOIN_CONDITION;
			boolean isToExcludeDisabled = true;

			//Get data from database
			distributionProtocolList = bizLogic.getList(sourceObjectName, displayName,
					valueFieldCol, whereColumnName, colCondition, whereColumnValue, joinCondition,
					separatorBetweenFields, isToExcludeDisabled);
		}

		// Fix for bug #9543 - start
		// Check for Distribution privilege & if privilege present, show all DP's in DP list
		if (!roleName.equals(Constants.ADMINISTRATOR) && sessionDataBean != null)
		{
			HashSet<Long> siteIds = new HashSet<Long>();
			HashSet<Long> cpIds = new HashSet<Long>();
			boolean hasDistributionPrivilege = false;

			try
			{
				dao = DAOConfigFactory.getInstance().getDAOFactory(Constants.APPLICATION_NAME)
						.getDAO();
				dao.openSession(null);
				User user = (User) dao.retrieveById(User.class.getName(), sessionDataBean
						.getUserId());
				Collection<Site> siteCollection = user.getSiteCollection();
				Collection<CollectionProtocol> cpCollection = user.getAssignedProtocolCollection();

				// Scientist
				if (siteCollection == null || siteCollection.isEmpty())
				{
					return distributionProtocolList;
				}
				for (Site site : siteCollection)
				{
					siteIds.add(site.getId());
				}
				if (cpCollection != null)
				{
					for (CollectionProtocol cp : cpCollection)
					{
						cpIds.add(cp.getId());
					}
				}

				hasDistributionPrivilege = checkDistributionPrivilege(sessionDataBean, siteIds,
						cpIds);

				if (hasDistributionPrivilege)
				{
					distributionProtocolList = bizLogic.getList(sourceObjectName, displayName,
							valueFieldCol, whereColNames, whereColCond, whereColVal,
							Constants.AND_JOIN_CONDITION, separatorBetweenFields, true);
				}
			}
			finally
			{
				dao.closeSession();
			}
		}

		// Fix for bug #9543 - end

		return distributionProtocolList;
	}

	private boolean checkDistributionPrivilege(SessionDataBean sessionDataBean,
			HashSet<Long> siteIds, HashSet<Long> cpIds)
	{
		boolean hasDistributionPrivilege = false;
		try
		{
			String objectId = Site.class.getName();
			PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(
					sessionDataBean.getUserName());

			for (Long siteId : siteIds)
			{
				if (privilegeCache.hasPrivilege(objectId + "_" + siteId, Permissions.DISTRIBUTION))
				{
					return true;
				}
			}
			objectId = CollectionProtocol.class.getName();
			for (Long cpId : cpIds)
			{
				boolean temp = privilegeCache.hasPrivilege(objectId + "_" + cpId,
						Permissions.DISTRIBUTION);
				if (temp)
				{
					return true;
				}
				hasDistributionPrivilege = AppUtility.checkForAllCurrentAndFutureCPs(
						Permissions.DISTRIBUTION, sessionDataBean, cpId.toString());
			}
		}
		catch (SMException e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}
		return hasDistributionPrivilege;
	}
}