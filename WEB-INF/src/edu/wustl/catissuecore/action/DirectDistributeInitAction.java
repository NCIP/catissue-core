
package edu.wustl.catissuecore.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.OrderBiospecimenArrayForm;
import edu.wustl.catissuecore.actionForm.OrderForm;
import edu.wustl.catissuecore.actionForm.OrderPathologyCaseForm;
import edu.wustl.catissuecore.actionForm.OrderSpecimenForm;
import edu.wustl.catissuecore.bizlogic.OrderBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.SurgicalPathologyReport;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Permissions;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;

/**
 * @author renuka_bajpai
 */
public class DirectDistributeInitAction extends BaseAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger
			.getCommonLogger(DirectDistributeInitAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final String typeOf = request.getParameter("typeOf");
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final OrderBizLogic orderBizLogic = (OrderBizLogic) factory
				.getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);

		final SessionDataBean sessionData = this.getSessionData(request);
		final PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
		final PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(sessionData
				.getUserName());

		final User user = this.getUser(sessionData.getUserName(), sessionData.getUserId());
		final List siteIdsList = (List) orderBizLogic.getUserSitesWithDistributionPrev(user,
				privilegeCache);
		boolean isValidTodistribute = false;

		if (typeOf.equals(Constants.SPECIMEN_ORDER_FORM_TYPE))
		{
			final OrderSpecimenForm orderSpecForm = (OrderSpecimenForm) form;
			final List specimenCollection = (List) orderBizLogic
					.getSpecimenDataFromDatabase(request);
			isValidTodistribute = this.isValidToDistributeSpecimenCheckPviOnSite(
					specimenCollection, siteIdsList);
			if (!isValidTodistribute)
			{
				isValidTodistribute = this.isValidToDistributeSpecimenCheckPviOnCP(
						specimenCollection, privilegeCache, sessionData);
			}
			// Collections.sort(specimenCollection, new SpecimenComparator());
			orderSpecForm.setValues(this.putValueInSpecimenMap(specimenCollection));
			final OrderForm orderFrom = (OrderForm) request.getSession().getAttribute("OrderForm");
			orderSpecForm.setOrderForm(orderFrom);
			orderSpecForm.setPageOf("specimen");
		}
		else if (typeOf.equals(Constants.ARRAY_ORDER_FORM_TYPE))
		{

			final OrderBiospecimenArrayForm orderArrayForm = (OrderBiospecimenArrayForm) form;
			final List specimenArrayCollection = (List) orderBizLogic
					.getSpecimenArrayDataFromDatabase(request);
			isValidTodistribute = this.isValidToDistributeSpecArray(specimenArrayCollection,
					siteIdsList);
			orderArrayForm.setValues(this.putValueInArrayMap(specimenArrayCollection));
			// Obtain OrderForm instance from the session.
			final OrderForm orderFrom = (OrderForm) request.getSession().getAttribute("OrderForm");
			orderArrayForm.setOrderForm(orderFrom);
			orderArrayForm.setPageOf("specimenArray");
		}
		else
		{
			final OrderPathologyCaseForm pathologyForm = (OrderPathologyCaseForm) form;
			final List pathologyCollection = (List) orderBizLogic
					.getPathologyDataFromDatabase(request);
			isValidTodistribute = this.isValidToDistributePathoCase(pathologyCollection,
					privilegeCache, sessionData);
			pathologyForm.setValues(this.putValueInPathologyMap(pathologyCollection));
			final OrderForm orderFrom = (OrderForm) request.getSession().getAttribute("OrderForm");
			pathologyForm.setOrderForm(orderFrom);
			pathologyForm.setPageOf("pathologyCase");
		}
		if (!orderBizLogic.isSuperAdmin(user) && !isValidTodistribute)
		{
			final ActionErrors errors = new ActionErrors();
			final ActionError error = new ActionError("access.denied.to.distribute");
			errors.add(ActionErrors.GLOBAL_ERROR, error);
			this.saveErrors(request, errors);
			return mapping.findForward("failure");
		}

		return mapping.findForward("success");
	}

	/**
	 * @param userName
	 *            : userName
	 * @param userId
	 *            : userId
	 * @return User : User
	 * @throws ApplicationException
	 *             : ApplicationException
	 */
	private User getUser(String userName, Long userId) throws ApplicationException
	{
		DAO dao = null;
		try
		{
			dao = AppUtility.openDAOSession(null);
			final User user = (User) dao.retrieveById(User.class.getName(), userId);
			return user;
		}
		finally
		{
			AppUtility.closeDAOSession(dao);
		}

	}

	/**
	 *
	 * @param specimenCollection : specimenCollection
	 * @param siteIdsList : siteIdsList
	 * @return boolean : boolean
	 */
	private boolean isValidToDistributeSpecimenCheckPviOnSite(List specimenCollection,
			List siteIdsList)
	{
		boolean isValidToDistribute = true;

		final Iterator<Specimen> specItr = specimenCollection.iterator();

		while (specItr.hasNext())
		{
			final Specimen specimen = specItr.next();
			// specimen.getSpecimenCollectionGroup().
			// getCollectionProtocolRegistration
			// ().getCollectionProtocol().getId()
			final SpecimenPosition specimenPosition = specimen.getSpecimenPosition();
			if (specimenPosition != null)
			{
				if (!siteIdsList.contains(specimenPosition.getStorageContainer().getSite().getId()))
				{
					isValidToDistribute = false;
					break;
				}
			}
		}

		return isValidToDistribute;
	}

	/**
	 *
	 * @param specimenCollection : specimenCollection
	 * @param privilegeCache : privilegeCache
	 * @param sessionDataBean : sessionDataBean
	 * @return boolean : boolean
	 * @throws SMException : SMException
	 */
	private boolean isValidToDistributeSpecimenCheckPviOnCP(List specimenCollection,
			PrivilegeCache privilegeCache, SessionDataBean sessionDataBean) throws SMException
	{
		boolean isValidToDistribute = true;

		final Iterator<Specimen> specItr = specimenCollection.iterator();

		while (specItr.hasNext())
		{
			final Specimen specimen = specItr.next();
			final Long cpId = specimen.getSpecimenCollectionGroup()
					.getCollectionProtocolRegistration().getCollectionProtocol().getId();
			final String objectId = Constants.COLLECTION_PROTOCOL_CLASS_NAME + "_" + cpId;
			boolean isAuthorized = privilegeCache.hasPrivilege(objectId,
					Variables.privilegeDetailsMap.get(Constants.DISTRIBUTE_SPECIMENS));
			if (!isAuthorized)
			{
				isAuthorized = AppUtility.checkForAllCurrentAndFutureCPs(Permissions.DISTRIBUTION,
						sessionDataBean, cpId.toString());
			}

			if (!isAuthorized)
			{
				isValidToDistribute = false;
				break;
			}

		}

		return isValidToDistribute;
	}

	/**
	 *
	 * @param specArrayCollection : specArrayCollection
	 * @param siteIdsList : siteIdsList
	 * @return boolean : boolean
	 */
	private boolean isValidToDistributeSpecArray(List specArrayCollection, List siteIdsList)
	{
		boolean isValidToDistribute = true;

		final Iterator<SpecimenArray> specArrayItr = specArrayCollection.iterator();

		while (specArrayItr.hasNext())
		{
			final SpecimenArray specimenArray = specArrayItr.next();

			final StorageContainer storageContainer = (StorageContainer) specimenArray
					.getLocatedAtPosition().getParentContainer();
			if (!siteIdsList.contains(storageContainer.getSite().getId()))
			{
				isValidToDistribute = false;
				break;
			}
		}

		return isValidToDistribute;
	}

	/**
	 *
	 * @param pathologyReports : pathologyReports
	 * @param privilegeCache : privilegeCache
	 * @param sessionDataBean : sessionDataBean
	 * @return boolean : boolean
	 * @throws ApplicationException : ApplicationException
	 */
	private boolean isValidToDistributePathoCase(List pathologyReports,
			PrivilegeCache privilegeCache, SessionDataBean sessionDataBean)
			throws ApplicationException
	{
		boolean isValidToDistribute = true;

		final Iterator pathologyReportsIter = pathologyReports.iterator();

		try
		{
			while (pathologyReportsIter.hasNext())
			{
				final SurgicalPathologyReport surgPathReports = (SurgicalPathologyReport) pathologyReportsIter
						.next();
				final SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) surgPathReports
						.getSpecimenCollectionGroup();

				if (specimenCollectionGroup != null)
				{

					final Long cpId = specimenCollectionGroup.getCollectionProtocolRegistration()
							.getCollectionProtocol().getId();
					final String objectId = Constants.COLLECTION_PROTOCOL_CLASS_NAME + "_" + cpId;
					boolean isAuthorized = privilegeCache.hasPrivilege(objectId,
							Variables.privilegeDetailsMap.get(Constants.DISTRIBUTE_SPECIMENS));
					if (!isAuthorized)
					{
						isAuthorized = AppUtility.checkForAllCurrentAndFutureCPs(
								Permissions.DISTRIBUTION, sessionDataBean, cpId.toString());
					}

					if (!isAuthorized)
					{
						isValidToDistribute = false;
						break;
					}
				}
			}
		}
		catch (final SMException smExp)
		{
			this.logger.error(smExp.getMessage(), smExp);
			throw AppUtility.getApplicationException(smExp, "sm.operation.error",
					"Error in checking has privilege");
		}

		return isValidToDistribute;
	}

	/**
	 * @param specimenCollection
	 *            : specimenCollection
	 * @return Map : Map
	 */
	private Map putValueInSpecimenMap(List specimenCollection)
	{
		final Map specimenMap = new LinkedHashMap();
		final Iterator specCollIter = specimenCollection.iterator();
		int counter = 0;
		// boolean isValidToDistribute = false;
		while (specCollIter.hasNext())
		{
			final Specimen specimen = (Specimen) specCollIter.next();
			specimenMap.put("OrderSpecimenBean:" + counter + "_specimenId", specimen.getId()
					.toString());

			// confirm abt this what is name here TODO
			specimenMap.put("OrderSpecimenBean:" + counter + "_specimenName", specimen.getLabel()
					.toString());
			specimenMap.put("OrderSpecimenBean:" + counter + "_availableQuantity", specimen
					.getAvailableQuantity().toString());
			specimenMap.put("OrderSpecimenBean:" + counter + "_requestedQuantity", specimen
					.getAvailableQuantity().toString());
			specimenMap.put("OrderSpecimenBean:" + counter + "_description", "");

			// look for this TODO
			specimenMap.put("OrderSpecimenBean:" + counter + "_unitRequestedQuantity", "");

			specimenMap.put("OrderSpecimenBean:" + counter + "_specimenClass", specimen
					.getClassName());
			specimenMap.put("OrderSpecimenBean:" + counter + "_specimenType", specimen
					.getSpecimenType());
			specimenMap.put("OrderSpecimenBean:" + counter + "_isDerived", "false");

			specimenMap.put("OrderSpecimenBean:" + counter + "_checkedToRemove", "");
			specimenMap.put("OrderSpecimenBean:" + counter + "_typeOfItem", "specimen");
			specimenMap.put("OrderSpecimenBean:" + counter + "_distributionSite", "");
			specimenMap.put("OrderSpecimenBean:" + counter + "_arrayName", "None");
			counter++;
		}

		return specimenMap;
	}

	/**
	 * @param specimenArrays
	 *            : specimenArrays
	 * @return Map : Map
	 */
	private Map putValueInArrayMap(List specimenArrays)
	{
		final Map arrayMap = new HashMap();
		final Iterator specArrayCollIter = specimenArrays.iterator();
		int counter = 0;
		while (specArrayCollIter.hasNext())
		{
			final SpecimenArray specimenArray = (SpecimenArray) specArrayCollIter.next();
			arrayMap.put("OrderSpecimenBean:" + counter + "_specimenId", specimenArray.getId()
					.toString());
			arrayMap.put("OrderSpecimenBean:" + counter + "_specimenName", specimenArray.getName());
			arrayMap.put("OrderSpecimenBean:" + counter + "_availableQuantity", "");
			arrayMap.put("OrderSpecimenBean:" + counter + "_requestedQuantity", "0.0");
			arrayMap.put("OrderSpecimenBean:" + counter + "_description", "");
			arrayMap.put("OrderSpecimenBean:" + counter + "_unitRequestedQuantity", "");
			arrayMap.put("OrderSpecimenBean:" + counter + "_isDerived", "false");
			arrayMap.put("OrderSpecimenBean:" + counter + "_typeOfItem", "specimenArray");
			arrayMap.put("OrderSpecimenBean:" + counter + "_arrayName", "None");
			arrayMap.put("OrderSpecimenBean:" + counter + "_distributionSite", "");
			counter++;
		}
		return arrayMap;

	}

	/**
	 * @param pathologyReports
	 *            : pathologyReports
	 * @return Map : Map
	 */
	private Map putValueInPathologyMap(List pathologyReports)
	{
		final Map pathologyMap = new HashMap();
		final Iterator pathologyReportsIter = pathologyReports.iterator();
		int counter = 0;
		while (pathologyReportsIter.hasNext())
		{
			final SurgicalPathologyReport surgPathReports = (SurgicalPathologyReport) pathologyReportsIter
					.next();
			pathologyMap.put("OrderSpecimenBean:" + counter + "_specimenId", surgPathReports
					.getId().toString());
			pathologyMap.put("OrderSpecimenBean:" + counter + "_specimenName", surgPathReports
					.getSpecimenCollectionGroup().getSurgicalPathologyNumber());
			pathologyMap.put("OrderSpecimenBean:" + counter + "_availableQuantity", "");
			pathologyMap.put("OrderSpecimenBean:" + counter + "_requestedQuantity", "1");
			pathologyMap.put("OrderSpecimenBean:" + counter + "_description", "");
			pathologyMap.put("OrderSpecimenBean:" + counter + "_specimenCollectionGroup",
					surgPathReports.getSpecimenCollectionGroup().getId().toString());
			pathologyMap
					.put("OrderSpecimenBean:" + counter + "_collectionProtocol", surgPathReports
							.getSpecimenCollectionGroup().getCollectionProtocolRegistration()
							.getCollectionProtocol().getTitle());

			pathologyMap.put("OrderSpecimenBean:" + counter + "_isDerived", "true");
			pathologyMap.put("OrderSpecimenBean:" + counter + "_specimenClass", "Tissue");
			pathologyMap
					.put("OrderSpecimenBean:" + counter + "_specimenType", "Fixed Tissue Block");
			pathologyMap.put("OrderSpecimenBean:" + counter + "_unitRequestedQuantity", "count");
			pathologyMap.put("OrderSpecimenBean:" + counter + "_typeOfItem", "pathologyCase");
			pathologyMap.put("OrderSpecimenBean:" + counter + "_arrayName", "None");
			pathologyMap.put("OrderSpecimenBean:" + counter + "_pathologicalStatus", "");
			pathologyMap.put("OrderSpecimenBean:" + counter + "_tissueSite", "");
			counter++;

		}
		return pathologyMap;

	}

}
