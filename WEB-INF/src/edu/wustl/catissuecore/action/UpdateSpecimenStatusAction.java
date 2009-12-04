
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenObjectFactory;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.SpecimenDetailsTagUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.JDBCDAO;

/**
 * @author renuka_bajpai
 */
public class UpdateSpecimenStatusAction extends BaseAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger
			.getCommonLogger(UpdateSpecimenStatusAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
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
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final SessionDataBean sessionDataBean = this.getSessionData(request);
		DAO dao = null;
		final ViewSpecimenSummaryForm specimenSummaryForm = (ViewSpecimenSummaryForm) form;
		try
		{
			dao = AppUtility.openDAOSession(sessionDataBean);
			final String eventId = specimenSummaryForm.getEventId();

			final HttpSession session = request.getSession();
			final NewSpecimenBizLogic bizLogic = new NewSpecimenBizLogic();

			final LinkedHashSet specimenDomainCollection = this
					.getSpecimensToSave(eventId, session);

			//bizLogic.updaupdateAnticipatorySpecimens(specimenDomainCollection,
			// sessionDataBean);
			if (specimenDomainCollection != null && specimenDomainCollection.size() > 0)
			{
				final Iterator<Specimen> spcItr = specimenDomainCollection.iterator();
				final Date timeStamp = this.getTimeStamp(spcItr.next());
				this.setCreatedOnDate(specimenDomainCollection, timeStamp);
				bizLogic.update(specimenDomainCollection, specimenDomainCollection, 0,
						sessionDataBean);
			}
			final Object obj = session.getAttribute("SCGFORM");

			// 11July08 : Mandar : For GenericSpecimen
			SpecimenDetailsTagUtil.setAnticipatorySpecimenDetails(request, specimenSummaryForm,
					false);
			// added this to disable collected checkboxes

			if (request.getParameter("target") != null
					&& request.getParameter("target").equals("viewSummary"))
			{
				final ActionMessages actionMessages = new ActionMessages();
				actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"object.add.successOnly", "Specimens"));
				specimenSummaryForm.setShowbarCode(true);
				specimenSummaryForm.setShowLabel(true);
				this.saveMessages(request, actionMessages);
				specimenSummaryForm.setReadOnly(true);
				// bug 12959
				request.setAttribute("readOnly", true);

			}
			if (specimenSummaryForm.getPrintCheckbox() != null
					&& specimenSummaryForm.getPrintCheckbox().equals("true"))
			{
				// By Falguni Sachde
				// Code Reviewer:Abhijit Naik
				// Bug :6569 : In case of collected SCG ,the
				// specimenDomainCollection not contains all specimen.
				// To get all specimen related with give SCG ,query with SCG id
				// and get SpecimenCollection
				if (obj == null)
				{
					this.logger.fatal("SCG id is null failed to execute" + " print of scg -"
							+ "UpdateSpecimenStatusAction");
				}
				else
				{
					final HashSet specimenCollection = this.getSpecimensToPrint((Long) obj,
							sessionDataBean, dao);
					// bug 11169
					// Set specimenprintCollection =
					// specimenSummaryForm.getSpecimenPrintList();
					// Set domainObjSet = this.getSpecimensFromGenericSpecimens(
					// specimenprintCollection);

					final HashMap forwardToPrintMap = new HashMap();
					forwardToPrintMap.put("printAntiSpecimen", this.getSpecimensToPrint(
							specimenSummaryForm, specimenCollection));
					request.setAttribute("forwardToPrintMap", forwardToPrintMap);
					request.setAttribute("AntiSpecimen", "1");
					// bug 12141 start
					if (specimenSummaryForm.getForwardTo() != null
							&& !specimenSummaryForm.getForwardTo().equals(
									Constants.ADD_MULTIPLE_SPECIMEN_TO_CART))
					{
						return mapping.findForward(Constants.PRINT_ANTICIPATORY_SPECIMENS);
					}
					// bug 12141 end
				}
			}

			if (specimenSummaryForm.getForwardTo() != null
					&& specimenSummaryForm.getForwardTo().equals(
							Constants.ADD_MULTIPLE_SPECIMEN_TO_CART))
			{
				//bug 13164 start
				if (request.getParameter(Constants.IS_SCG_SUBMIT) != null
						&& (request.getParameter(Constants.IS_SCG_SUBMIT)
								.equals(Constants.SCG_SUBMIT)))
				{
					final HashSet specimenprintCollection = this.getSpecimensToPrint((Long) obj,
							sessionDataBean, dao);

					final Iterator iter = specimenprintCollection.iterator();
					final List specimenIdList = new ArrayList();
					while (iter.hasNext())
					{
						specimenIdList.add(((Specimen) iter.next()).getId());
					}
					request.setAttribute(Constants.SPECIMEN_ID_LIST, specimenIdList);
				}
				else
				{
					request.setAttribute(Constants.SPECIMEN_ID_LIST, this
							.getSpecimenIdList(specimenSummaryForm));
				}
				//bug 13164 end
				this.saveToken(request);
				// bug 12141 start
				if (specimenSummaryForm.getPrintCheckbox() != null
						&& specimenSummaryForm.getPrintCheckbox().equals("true"))
				{
					return mapping.findForward(Constants.ADD_MULTIPLE_SPECIMEN_TO_CART_AND_PRINT);
				}
				else
				{
					return mapping.findForward(Constants.ADD_MULTIPLE_SPECIMEN_TO_CART);
				}
				// bug 12141 end
			}

			return mapping.findForward(Constants.SUCCESS);
		}
		catch (final ApplicationException exception)
		{
			exception.printStackTrace();
			this.logger.error(exception.getMessage(), exception);
			String errorMsg = exception.getCustomizedMsg();
			if(errorMsg==null)
			{
				errorMsg =exception.getMessage();
			}
			this.logger.debug(errorMsg, exception);
			// 11July08 : Mandar : For GenericSpecimen
			SpecimenDetailsTagUtil.setAnticipatorySpecimenDetails(request, specimenSummaryForm,
					false);
			// Suman-For bug #8228
			String s = "";
			if (errorMsg.equals(
					"Failed to update multiple specimen Stroage location already in use")
					||errorMsg.equals(
							"Failed to update multiple specimen"
									+ " Either Storagecontainer is full!"
									+ " or it cannot accomodate all the" + " specimens.")
					|| errorMsg.equals(
							"Failed to update multiple specimen"
									+ " Storagecontainer information not" + " found!"))
			{
				this.clearSCLocation(specimenSummaryForm);
				s = "Please allocate a different container to the"
						+ " specimens shown below with empty container"
						+ " names as the container you specified has" + " insufficient space";
			}
			else
			{
				s = errorMsg;
			}
			final ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("errors.item", s));
			this.saveErrors(request, actionErrors);
			this.saveToken(request);
			final String pageOf = request.getParameter(Constants.PAGE_OF);
			if (pageOf != null)
			{
				request.setAttribute(Constants.PAGE_OF, pageOf);
			}
			return mapping.findForward(Constants.FAILURE);
		}
		finally
		{
			AppUtility.closeDAOSession(dao);
		}
	}

	private List getSpecimenIdList(ViewSpecimenSummaryForm specimenSummaryForm)
	{
		final List specimenIdList = new ArrayList();
		specimenSummaryForm.getEventId();
		final List<GenericSpecimen> genericSpecimensList = new ArrayList<GenericSpecimen>();
		genericSpecimensList.addAll(specimenSummaryForm.getSpecimenList());
		genericSpecimensList.addAll(specimenSummaryForm.getAliquotList());
		genericSpecimensList.addAll(specimenSummaryForm.getDerivedList());
		for (final GenericSpecimen gSpecimen : genericSpecimensList)
		{
			specimenIdList.add(gSpecimen.getId());
		}
		return specimenIdList;
	}

	/**
	 *
	 * @param specimenSummaryForm : specimenSummaryForm
	 * @param specimenDomainCollection : specimenDomainCollection
	 * @return Set < Specimen > : Set < Specimen >
	 */
	private Set<Specimen> getSpecimensToPrint(ViewSpecimenSummaryForm specimenSummaryForm,
			HashSet specimenDomainCollection)
	{
		final Set specimenprintCollection = specimenSummaryForm.getSpecimenPrintList();
		final Iterator it = specimenprintCollection.iterator();
		final Set<Specimen> specimensToPrint = new LinkedHashSet<Specimen>();
		while (it.hasNext())
		{
			final Object obj = it.next();
			if (obj instanceof GenericSpecimen)
			{
				final GenericSpecimen gSpecimen = (GenericSpecimen) obj;
				final Iterator itr = specimenDomainCollection.iterator();
				while (itr.hasNext())
				{
					final Specimen specimen = (Specimen) itr.next();
					//if label and barcode generation is off then label will be null.
					// thus added this check
					//bug 13026
					if (specimen.getId().toString() != null && gSpecimen.getId() != -1)
					{
						if (specimen.getId().longValue() == gSpecimen.getId())
						{
							specimensToPrint.add(specimen);
						}
					}
					else if (specimen.getLabel() != null && gSpecimen.getDisplayName() != null)
					{
						if (specimen.getLabel().equals(gSpecimen.getDisplayName()))
						{
							specimensToPrint.add(specimen);
						}
					}
				}
			}
		}
		return specimensToPrint;
	}

	/**
	 *
	 * @param specimenSummaryForm : specimenSummaryForm
	 * @throws Exception : Exception
	 */
	private void clearSCLocation(ViewSpecimenSummaryForm specimenSummaryForm) throws Exception
	{
		final List<GenericSpecimen> specimenList = specimenSummaryForm.getSpecimenList();
		final List<GenericSpecimen> aliquotList = specimenSummaryForm.getAliquotList();
		final List<GenericSpecimen> derivedList = specimenSummaryForm.getDerivedList();
		final List<String> allocatedPositions = new ArrayList<String>();
		int freeSizeofContainer = 0;
		final StorageContainerBizLogic scBizLogic = new StorageContainerBizLogic();
		JDBCDAO jdbcDao = null;
		try
		{
			jdbcDao = AppUtility.openJDBCSession();

			for (final GenericSpecimen spec : specimenList)
			{
				freeSizeofContainer = scBizLogic.getCountofFreeLocationOfContainer(jdbcDao,
						spec.getContainerId(), spec.getSelectedContainerName()).intValue();
				//freeSizeofContainer = StorageContainerUtil.getCountofFreeLocationOfContainer(conId, conName);

			}

			int tempContainerSize = this.checkList(specimenList, allocatedPositions,
					freeSizeofContainer, scBizLogic, jdbcDao);
			tempContainerSize = this.checkList(aliquotList, allocatedPositions, tempContainerSize,
					scBizLogic, jdbcDao);
			this.checkList(derivedList, allocatedPositions, tempContainerSize, scBizLogic, jdbcDao);
		}
		catch (final Exception e)
		{
			this.logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw new BizLogicException(null, e, e.getMessage());
		}
		finally
		{
			try
			{
				AppUtility.closeJDBCSession(jdbcDao);
			}
			catch (final ApplicationException e)
			{
				this.logger.error(e.getMessage(),e);
				e.printStackTrace();
				throw new BizLogicException(e.getErrorKey(), e, e.getMsgValues());
			}
		}
	}

	/**
	 *
	 * @param gs : gs
	 * @param allocatedPositions : allocatedPositions
	 * @param containerSize : containerSize
	 * @return int : int
	 */
	public int checkList(List<GenericSpecimen> gs, List<String> allocatedPositions,
			int containerSize, StorageContainerBizLogic scBizLogic, JDBCDAO jdbcDAO)
			throws BizLogicException
	{
		for (final GenericSpecimen spec : gs)
		{
			final String positionOne = spec.getPositionDimensionOne();
			final String positionTwo = spec.getPositionDimensionTwo();
			final String containerName = spec.getStorageContainerForSpecimen();
			final String key = containerName + ":" + positionOne + "," + positionTwo;
			if (positionOne != "" && positionTwo != "")
			{
				if (!(scBizLogic.isPositionAvailable(jdbcDAO, spec.getContainerId(), spec
						.getSelectedContainerName(), positionOne, positionTwo))
						|| allocatedPositions.contains(key))
				{
					spec.setPositionDimensionOne("");
					spec.setPositionDimensionTwo("");
					spec.setStorageContainerForSpecimen("");
					spec.setSelectedContainerName("");
				}
				else
				{
					allocatedPositions.add(key);
				}
			}
			else
			{
				if (containerSize >= 1)
				{
					allocatedPositions.add(key);
				}
				else
				{
					spec.setPositionDimensionOne("");
					spec.setPositionDimensionTwo("");
					spec.setStorageContainerForSpecimen("");
					spec.setSelectedContainerName("");
				}
			}
			containerSize = containerSize - 1;
		}
		return containerSize;
	}

	// end bug 8228 - Suman

	/**
	 * @param eventId : eventId
	 * @param session : session
	 * @return LinkedHashSet : LinkedHashSet
	 * @throws ApplicationException : ApplicationException
	 */
	protected LinkedHashSet getSpecimensToSave(String eventId, HttpSession session)
			throws ApplicationException
	{
		final Map collectionProtocolEventMap = (Map) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);

		final CollectionProtocolEventBean eventBean = (CollectionProtocolEventBean) collectionProtocolEventMap
				.get(eventId);

		final LinkedHashMap specimenMap = (LinkedHashMap) eventBean.getSpecimenRequirementbeanMap();

		final Collection specimenCollection = specimenMap.values();
		final Iterator iterator = specimenCollection.iterator();

		final LinkedHashSet specimenDomainCollection = new LinkedHashSet();
		while (iterator.hasNext())
		{
			final GenericSpecimen specimenVO = (GenericSpecimen) iterator.next();
			Specimen specimen = null;
			if (!specimenVO.getReadOnly() && specimenVO.getCheckedSpecimen())
			{
				specimen = this.createSpecimenDomainObject(specimenVO);
				specimen
						.setChildSpecimenCollection(this.getChildrenSpecimens(specimenVO, specimen));
				specimenDomainCollection.add(specimen);
			}
			else
			{
				specimenDomainCollection.addAll(this.getChildrenSpecimens(specimenVO, specimen));
			}
		}
		return specimenDomainCollection;
	}

	/**
	 *
	 * @param specimenVO : specimenVO
	 * @param parentSpecimen : parentSpecimen
	 * @return Collection : Collection
	 * @throws ApplicationException : ApplicationException
	 */
	private Collection getChildrenSpecimens(GenericSpecimen specimenVO, Specimen parentSpecimen)
			throws ApplicationException
	{
		final LinkedHashSet childrenSpecimens = new LinkedHashSet();
		final LinkedHashMap aliquotMap = specimenVO.getAliquotSpecimenCollection();

		if (aliquotMap != null && !aliquotMap.isEmpty())
		{
			final Collection aliquotCollection = aliquotMap.values();
			final Iterator iterator = aliquotCollection.iterator();
			while (iterator.hasNext())
			{
				final GenericSpecimen aliquotSpecimen = (GenericSpecimen) iterator.next();
				Specimen specimen = null;
				if (!aliquotSpecimen.getReadOnly()&& aliquotSpecimen.getCheckedSpecimen())
				{
					specimen = this.createSpecimenDomainObject(aliquotSpecimen);
					specimen.setParentSpecimen(parentSpecimen);
					specimen.setChildSpecimenCollection(this.getChildrenSpecimens(aliquotSpecimen,
							specimen));
					childrenSpecimens.add(specimen);
				}
				else
				{
					childrenSpecimens.addAll(this.getChildrenSpecimens(aliquotSpecimen, specimen));
				}
			}
		}

		final LinkedHashMap derivedMap = specimenVO.getDeriveSpecimenCollection();

		if (derivedMap != null && !derivedMap.isEmpty())
		{
			final Collection aliquotCollection = derivedMap.values();
			final Iterator iterator = aliquotCollection.iterator();
			while (iterator.hasNext())
			{
				final GenericSpecimen derivedSpecimen = (GenericSpecimen) iterator.next();
				Specimen specimen = null;
				if (!derivedSpecimen.getReadOnly() && derivedSpecimen.getCheckedSpecimen())
				{
					specimen = this.createSpecimenDomainObject(derivedSpecimen);
					specimen.setParentSpecimen(parentSpecimen);
					specimen.setChildSpecimenCollection(this.getChildrenSpecimens(derivedSpecimen,
							specimen));
					childrenSpecimens.add(specimen);
				}
				else
				{
					childrenSpecimens.addAll(this.getChildrenSpecimens(derivedSpecimen, specimen));
				}
			}
		}
		return childrenSpecimens;
	}

	/**
	 * @param specimenVO : specimenVO
	 * @return Specimen : Specimen
	 * @throws ApplicationException
	 *             Application Exception
	 */
	protected Specimen createSpecimenDomainObject(GenericSpecimen specimenVO)
			throws ApplicationException
	{

		Specimen specimen;
		try
		{
			specimen = (Specimen) new SpecimenObjectFactory().getDomainObject(specimenVO
					.getClassName());
		}
		catch (final AssignDataException e1)
		{
			this.logger.error(e1.getMessage(), e1);
			e1.printStackTrace();
			return null;
		}

		if (Constants.MOLECULAR.equals(specimenVO.getClassName()))
		{
			Double concentration = null;
			try
			{
				concentration = new Double(specimenVO.getConcentration());
			}
			catch (final Exception exception)
			{
				this.logger.error(exception.getMessage(), exception);
				exception.printStackTrace();
				concentration = new Double(0);
			}
			((MolecularSpecimen) specimen).setConcentrationInMicrogramPerMicroliter(concentration);
		}
		final Long id = this.getSpecimenId(specimenVO);
		specimen.setId(id);
		specimen.setSpecimenClass(specimenVO.getClassName());
		specimen.setSpecimenType(specimenVO.getType());
		if ("".equals(specimenVO.getDisplayName()))
		{
			specimen.setLabel(null);
		}
		else
		{
			specimen.setLabel(specimenVO.getDisplayName());
		}

		specimen.setBarcode(specimenVO.getBarCode());

		/* bug 6015 vaishali khandelwal */

		/* end bug 6015 */

		final String initialQuantity = specimenVO.getQuantity();
		if (initialQuantity != null)
		{
			if (!initialQuantity.equals(""))
			{
				specimen.setInitialQuantity(new Double(initialQuantity));
			}
		}
		if (specimenVO.getCheckedSpecimen())
		{
			specimen.setCollectionStatus(Constants.SPECIMEN_COLLECTED);
			specimen.setAvailableQuantity(new Double(initialQuantity));
			if ((specimen.getAvailableQuantity() != null && specimen.getAvailableQuantity()
					.doubleValue() > 0))
			{
				specimen.setIsAvailable(Boolean.TRUE);
			}
		}
		else
		{
			specimen.setCollectionStatus("Pending");
			// Mandar : 25July08: ------- start ------------
			specimen.setAvailableQuantity(new Double(0));
			// Mandar : 25July08: ------- end ------------
		}

		if ("Virtual".equals(specimenVO.getStorageContainerForSpecimen()))
		{
			// specimen.setStorageContainer(null);
			specimen.setSpecimenPosition(null);
		}
		else
		{
			this.setStorageContainer(specimenVO, specimen);
		}

		return specimen;
	}

	/**
	 * @param specimenVO : specimenVO
	 * @param specimen : specimen
	 * @throws ApplicationException : ApplicationException
	 */
	private void setStorageContainer(GenericSpecimen specimenVO, Specimen specimen)
			throws ApplicationException
	{

		final String pos1 = specimenVO.getPositionDimensionOne();
		final String pos2 = specimenVO.getPositionDimensionTwo();

		if (!specimenVO.getCheckedSpecimen())
		{
			specimenVO.setPositionDimensionOne(String.valueOf(CollectionProtocolUtil
					.getStorageTypeValue(specimenVO.getStorageContainerForSpecimen())));

			return;
		}
		SpecimenPosition specPos = specimen.getSpecimenPosition();

		if (specPos == null)
		{
			specPos = new SpecimenPosition();
		}
		if (pos1 != null)
		{
			try
			{
				specPos.setPositionDimensionOne(Integer.parseInt(pos1));
			}
			catch (final NumberFormatException exception)
			{
				this.logger.error(exception.getMessage(), exception);
				specPos.setPositionDimensionOne(null);
			}
		}
		if (pos2 != null)
		{
			try
			{
				specPos.setPositionDimensionTwo(Integer.parseInt(pos2));
			}
			catch (final NumberFormatException exception)
			{
				this.logger.error(exception.getMessage(), exception);
				specPos.setPositionDimensionTwo(null);
			}
		}
		final StorageContainer storageContainer = new StorageContainer();
		specPos.setSpecimen(specimen);
		specPos.setStorageContainer(storageContainer);
		specimen.setSpecimenPosition(specPos);

		final String containerId = specimenVO.getContainerId();

		if (containerId != null && containerId.trim().length() > 0)
		{
			storageContainer.setId(new Long(containerId));
		}
		if (specimenVO.getSelectedContainerName() == null
				|| specimenVO.getSelectedContainerName().trim().length() == 0)
		{
			// ErrorKey errorKey = ErrorKey.getErrorKey("action.error");
			if(specimenVO.getDisplayName()==null)
			{
				specimenVO.setDisplayName("");
			}
			throw AppUtility.getApplicationException(null, "spec.storage.missing", specimenVO
					.getDisplayName());
		}
		storageContainer.setName(specimenVO.getSelectedContainerName());
		// specimen.setStorageContainer(storageContainer);
	}

	/**
	 * @param specimenVO : specimenVO
	 * @return Long : Long
	 */
	private Long getSpecimenId(GenericSpecimen specimenVO)
	{
		final long uniqueId = specimenVO.getId();
		if (uniqueId <= 0)
		{
			return null;
		}
		final Long id = new Long(uniqueId);
		return id;
	}

	/**
	 * @param scgId : scgId
	 * @param sessionDataBean : sessionDataBean
	 * @return HashSet : HashSet
	 * @throws BizLogicException : BizLogicException
	 */
	protected HashSet getSpecimensToPrint(Long scgId, SessionDataBean sessionDataBean, DAO dao)
			throws BizLogicException
	{

		final SpecimenCollectionGroupBizLogic bizLogic = new SpecimenCollectionGroupBizLogic();
		final SpecimenCollectionGroup objSCG = bizLogic.getSCGFromId(scgId, sessionDataBean, true,
				dao);
		final HashSet specimenCollection = new HashSet(objSCG.getSpecimenCollection());

		return specimenCollection;

	}

	/**
	 *
	 * @param specimenColl : specimenColl
	 * @param timeStamp : timeStamp
	 */
	private void setCreatedOnDate(Collection<AbstractSpecimen> specimenColl, Date timeStamp)
	{
		try
		{
			final Iterator<AbstractSpecimen> spcItr = specimenColl.iterator();
			while (spcItr.hasNext())
			{
				final Specimen specimen = (Specimen) spcItr.next();
				if ((specimen != null)
						&& (Constants.COLLECTION_STATUS_COLLECTED.equals(specimen
								.getCollectionStatus())))
				{
					specimen.setCreatedOn(timeStamp);
					final Collection<AbstractSpecimen> childSpecColl = specimen
							.getChildSpecimenCollection();
					if (childSpecColl != null && childSpecColl.size() > 0)
					{
						this.setCreatedOnDate(childSpecColl, timeStamp);
					}
				}
			}
		}
		catch (final Exception e)
		{
			this.logger.error(e.getMessage(),e);
			e.printStackTrace();
		}
	}

	/**
	 * @param specimen
	 *            : specimen
	 * @return Date : Date
	 */
	private Date getTimeStamp(Specimen specimen)
	{
		Date timeStamp = null;
		try
		{
			final String query = "select collectionEventParameters.timestamp"
					+ " from edu.wustl.catissuecore.domain.CollectionEventParameters"
					+ " as collectionEventParameters where "
					+ " collectionEventParameters.specimenCollectionGroup.id"
					+ " = (select specimen.specimenCollectionGroup.id"
					+ " from edu.wustl.catissuecore.domain.Specimen as" + " specimen where "
					+ "specimen.id = " + specimen.getId() + ")";

			final List<Date> list = new DefaultBizLogic().executeQuery(query);
			final Iterator<Date> itr = list.iterator();
			while (itr.hasNext())
			{
				timeStamp = itr.next();
			}
		}
		catch (final Exception e)
		{
			this.logger.error(e.getMessage(),e);
			e.printStackTrace();
		}
		return timeStamp;
	}

}