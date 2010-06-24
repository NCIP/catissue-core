
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bean.GenericSpecimenVO;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.IdComparator;
import edu.wustl.catissuecore.util.SpecimenAutoStorageContainer;
import edu.wustl.catissuecore.util.SpecimenUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

// TODO: Auto-generated Javadoc
/**
 * The Class AnticipatorySpecimenViewAction.
 *
 * @author abhijit_naik
 */
public class AnticipatorySpecimenViewAction extends BaseAction
{

	/**
	 * .
	 */
	private static final String SPECIMEN_KEY_PREFIX = "S_";
	/**
	 * cpId.
	 */
	Long cpId = null;

	/**
	 * logger.
	 */
	private static final Logger LOGGER = Logger
			.getCommonLogger(AnticipatorySpecimenViewAction.class);
	/**
	 * asignedPositonSet.
	 */
	private Set asignedPositonSet = null;

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping
	 *            object of ActionMapping
	 * @param form : object of ActionForm
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
		String target = Constants.SUCCESS;
		try
		{
			dao = AppUtility.openDAOSession(sessionDataBean);
			this.asignedPositonSet = new HashSet();
			boolean isFromSpecimenEditPage = false;
			final SpecimenCollectionGroupForm specimenCollectionGroupForm = (SpecimenCollectionGroupForm) form;
			final HttpSession session = request.getSession();
			Long identifier = specimenCollectionGroupForm.getId();
			Long specimenId = null;
			final SpecimenAutoStorageContainer autoStorageContainer = new SpecimenAutoStorageContainer();
			final HashMap forwardToHashMap = (HashMap) request.getAttribute("forwardToHashMap");
			if (forwardToHashMap != null)
			{
				identifier = (Long) forwardToHashMap.get("specimenCollectionGroupId");

				if (identifier != null)
				{
					specimenCollectionGroupForm.setId(identifier);
				}

				specimenId = (Long) forwardToHashMap.get("specimenId");
				if (specimenId != null)
				{
					session.setAttribute(Constants.SPECIMENFORM, specimenId);
					isFromSpecimenEditPage = true;
				}

			}
			final SpecimenCollectionGroupBizLogic scgBizLogic = new SpecimenCollectionGroupBizLogic();
			session.setAttribute(Constants.SCGFORM, specimenCollectionGroupForm.getId());
			final SpecimenCollectionGroup specimencollectionGroup = scgBizLogic.getSCGFromId(identifier,
					sessionDataBean, true, dao);
			if (specimencollectionGroup.getActivityStatus().equalsIgnoreCase(
					Status.ACTIVITY_STATUS_DISABLED.toString()))
			{
				target = Status.ACTIVITY_STATUS_DISABLED.toString();
			}
			this.cpId = specimencollectionGroup.getCollectionProtocolRegistration()
					.getCollectionProtocol().getId();

			if (isFromSpecimenEditPage)
			{
				final Collection scgSpecimenList = specimencollectionGroup.getSpecimenCollection();
				this.getSpcimensToShowOnSummary(specimenId, scgSpecimenList, session,
						autoStorageContainer, dao);
			}
			else
			{
				this.addSCGSpecimensToSession(session, specimencollectionGroup,
						autoStorageContainer, dao);
			}
			request.setAttribute("RequestType",
					ViewSpecimenSummaryForm.REQUEST_TYPE_ANTICIPAT_SPECIMENS);
			autoStorageContainer.setCollectionProtocol(this.cpId);

			autoStorageContainer.setSpecimenStoragePositions(sessionDataBean);
			if (request.getParameter("target") != null)
			{
				target = request.getParameter("target");
			}

			autoStorageContainer.fillAllocatedPositionSet(this.asignedPositonSet);
			session.setAttribute("asignedPositonSet", this.asignedPositonSet);
		}
		finally
		{
			AppUtility.closeDAOSession(dao);
		}
		return mapping.findForward(target);
	}

	/**
	 * Add scg specimens to session.
	 * @param session : session
	 * @param specimencollectionGroup : specimencollectionGroup
	 * @throws DAOException : DAOException
	 * @throws BizLogicException : BizLogicException
	 */
	private void addSCGSpecimensToSession(HttpSession session,
			SpecimenCollectionGroup specimencollectionGroup,
			SpecimenAutoStorageContainer autoStorageContainer, DAO dao) throws DAOException,
			BizLogicException
	{
		final LinkedHashMap<String, CollectionProtocolEventBean> cpEventMap = new LinkedHashMap<String, CollectionProtocolEventBean>();

		final CollectionProtocolEventBean eventBean = new CollectionProtocolEventBean();

		eventBean.setUniqueIdentifier(String.valueOf(specimencollectionGroup.getId().longValue()));
		// sorting of specimen collection accoding to id
		final LinkedList<Specimen> specimenList = new LinkedList<Specimen>();
		final Iterator<Specimen> itr = specimencollectionGroup.getSpecimenCollection().iterator();

		while (itr.hasNext())
		{
			final Specimen specimen = itr.next();
			if (!Status.ACTIVITY_STATUS_DISABLED.toString().equals(specimen.getActivityStatus()))
			{
				specimenList.add(specimen);
			}
		}
		final Comparator spIdComp = new IdComparator();
		Collections.sort(specimenList, spIdComp);

		final Iterator itr2 = specimenList.iterator();
		final NewSpecimenBizLogic specBizLogic = new NewSpecimenBizLogic();
		while (itr2.hasNext())
		{
			final Specimen specimen = (Specimen) itr2.next();
			if (specimen.getChildSpecimenCollection() != null)
			{
				final long lastAliquotNo = specBizLogic.getTotalNoOfAliquotSpecimen(specimen
						.getId(), dao);
				final LinkedList<Specimen> childList = new LinkedList(specimen
						.getChildSpecimenCollection());
				Collections.sort(childList, spIdComp);
				this.setLabelInSpecimen(specimen.getId(), childList, lastAliquotNo);
			}
		}

		eventBean.setSpecimenRequirementbeanMap(this.getSpecimensMap(specimenList, this.cpId,
				autoStorageContainer));
		// eventBean.setSpecimenRequirementbeanMap(getSpecimensMap(
		// specimencollectionGroup.getSpecimenCollection(),cpId ));
		final String globalSpecimenId = "E" + eventBean.getUniqueIdentifier() + "_";
		cpEventMap.put(globalSpecimenId, eventBean);
		session.removeAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
		session.setAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP, cpEventMap);
	}

	/**
	 *
	 * @param specimenCollection : specimenCollection
	 * @param collectionProtocolId : collectionProtocolId
	 * @return LinkedHashMap : LinkedHashMap
	 * @throws DAOException : DAOException
	 */
	protected LinkedHashMap<String, GenericSpecimen> getSpecimensMap(
			Collection<Specimen> specimenCollection, long collectionProtocolId,
			SpecimenAutoStorageContainer autoStorageContainer) throws DAOException
	{
		LinkedHashMap<String, GenericSpecimen> specimenMap = new LinkedHashMap<String, GenericSpecimen>();

		final Iterator<Specimen> specimenIterator = specimenCollection.iterator();
		while (specimenIterator.hasNext())
		{
			final Specimen specimen = specimenIterator.next();
			if (specimen.getParentSpecimen() == null)
			{
				final GenericSpecimenVO specBean = this.getSpecimenBean(specimen, null);
				this.addToSpToAutoCont(specBean, autoStorageContainer);
				this.setChildren(specimen, specBean, autoStorageContainer);
				specBean.setUniqueIdentifier(SPECIMEN_KEY_PREFIX + specimen.getId());
				specBean.setCollectionProtocolId(collectionProtocolId);
				specimenMap = this.getOrderedMap(specimenMap, specimen.getId(), specBean,
						SPECIMEN_KEY_PREFIX);
			}

		}
		return specimenMap;
	}

	/**
	 *
	 * @param specimenCollection : specimenCollection
	 * @param specimenId : specimenId
	 * @param collectionProtocolId : collectionProtocolId
	 * @return LinkedHashMap < String , GenericSpecimen > : LinkedHashMap
	 * @throws DAOException : DAOException
	 */
	protected LinkedHashMap<String, GenericSpecimen> getSpecimensMap(Collection specimenCollection,
			long specimenId, long collectionProtocolId,
			SpecimenAutoStorageContainer autoStorageContainer) throws DAOException
	{
		final LinkedHashMap<String, GenericSpecimen> specimenMap = new LinkedHashMap<String, GenericSpecimen>();

		final Iterator specimenIterator = specimenCollection.iterator();
		while (specimenIterator.hasNext())
		{
			final Specimen specimen = (Specimen) specimenIterator.next();
			if (specimen.getId() != null && specimen.getId() == specimenId)
			{
				final GenericSpecimenVO specBean = this.getSpecimenBean(specimen, null);
				this.addToSpToAutoCont(specBean, autoStorageContainer);
				this.setChildren(specimen, specBean, autoStorageContainer);
				specBean.setUniqueIdentifier("S_" + specimen.getId());
				specBean.setCollectionProtocolId(collectionProtocolId);
				specimenMap.put("S_" + specimen.getId(), specBean);
			}

		}
		return specimenMap;
	}

	/**
	 *
	 * @param specimenMap : specimenMap
	 * @param identifier : id
	 * @param specBean : specBean
	 * @param prefix : prefix
	 * @return LinkedHashMap < String , GenericSpecimen > : LinkedHashMap
	 */
	private LinkedHashMap<String, GenericSpecimen> getOrderedMap(
			LinkedHashMap<String, GenericSpecimen> specimenMap, Long identifier,
			GenericSpecimenVO specBean, String prefix)
	{
		final LinkedHashMap<String, GenericSpecimen> orderedMap = new LinkedHashMap<String, GenericSpecimen>();
		final Object[] keyArray = specimenMap.keySet().toArray();
		for (final Object element : keyArray)
		{
			final String keyVal = (String) element;
			final String keyId = keyVal.substring(prefix.length());
			if (Long.parseLong(keyId) > identifier)
			{
				orderedMap.put(keyVal, specimenMap.get(keyVal));
				specimenMap.remove(keyVal);
			}
		}
		specimenMap.put(prefix + identifier, specBean);
		if (!orderedMap.isEmpty())
		{
			specimenMap.putAll(orderedMap);
		}
		return specimenMap;
	}

	/**
	 *
	 * @param specimen : specimen
	 * @param parentSpecimenVO : parentSpecimenVO
	 * @throws DAOException : DAOException
	 */
	protected void setChildren(Specimen specimen, GenericSpecimen parentSpecimenVO,
			SpecimenAutoStorageContainer autoStorageContainer) throws DAOException
	{

		final Collection<AbstractSpecimen> specimenChildren = specimen.getChildSpecimenCollection();
		final List<AbstractSpecimen> specimenChildrenCollection = new LinkedList<AbstractSpecimen>(
				specimenChildren);
		CollectionProtocolUtil.getSortedCPEventList(specimenChildrenCollection);

		if (specimenChildrenCollection == null || specimenChildrenCollection.isEmpty())
		{
			return;
		}

		final Iterator<AbstractSpecimen> iterator = specimenChildrenCollection.iterator();

		LinkedHashMap<String, GenericSpecimen> aliquotMap = new LinkedHashMap<String, GenericSpecimen>();
		LinkedHashMap<String, GenericSpecimen> derivedMap = new LinkedHashMap<String, GenericSpecimen>();

		while (iterator.hasNext())
		{
			final Specimen childSpecimen = (Specimen) iterator.next();
			final String lineage = childSpecimen.getLineage();

			final GenericSpecimenVO specimenBean = this.getSpecimenBean(childSpecimen, specimen
					.getLabel());
			// addToSpToAutoCont(specimenBean);
			this.setChildren(childSpecimen, specimenBean, autoStorageContainer);
			final String prefix = lineage + specimen.getId() + "_";
			specimenBean.setUniqueIdentifier(prefix + childSpecimen.getId());

			if (Constants.ALIQUOT.equals(childSpecimen.getLineage()))
			{
				aliquotMap = this.getOrderedMap(aliquotMap, childSpecimen.getId(), specimenBean,
						prefix);
			}
			else
			{
				derivedMap = this.getOrderedMap(derivedMap, childSpecimen.getId(), specimenBean,
						prefix);
			}
			specimenBean.setCollectionProtocolId(this.cpId);

		}
		if (aliquotMap != null && !aliquotMap.isEmpty())
		{

			final Iterator aliquotItr = aliquotMap.keySet().iterator();
			while (aliquotItr.hasNext())
			{
				final String key = (String) aliquotItr.next();
				final GenericSpecimenVO spec = (GenericSpecimenVO) aliquotMap.get(key);
				this.addToSpToAutoCont(spec, autoStorageContainer);

			}
		}

		if (derivedMap != null && !derivedMap.isEmpty())
		{
			final Iterator derivedItr = derivedMap.keySet().iterator();
			while (derivedItr.hasNext())
			{
				final String key = (String) derivedItr.next();
				final GenericSpecimenVO spec = (GenericSpecimenVO) derivedMap.get(key);
				this.addToSpToAutoCont(spec, autoStorageContainer);
			}
		}


//		parentSpecimenVO.setGenerateLabel(specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getGenerateLabel());
		parentSpecimenVO.setAliquotSpecimenCollection(aliquotMap);
		parentSpecimenVO.setDeriveSpecimenCollection(derivedMap);
	}

	/**
	 *
	 * @param specimen : specimen
	 * @param parentName : parentName
	 * @return GenericSpecimenVO : GenericSpecimenVO
	 * @throws DAOException : DAOException
	 */
	protected GenericSpecimenVO getSpecimenBean(Specimen specimen, String parentName)
			throws DAOException
	{
		final GenericSpecimenVO specimenDataBean = new GenericSpecimenVO();
		specimenDataBean.setBarCode(specimen.getBarcode());
		specimenDataBean.setClassName(specimen.getClassName());
		specimenDataBean.setDisplayName(specimen.getLabel());
		specimenDataBean.setPathologicalStatus(specimen.getPathologicalStatus());
		specimenDataBean.setId(specimen.getId().longValue());
		specimenDataBean.setParentName(parentName);
		if (specimen.getInitialQuantity() != null)
		{
			specimenDataBean.setQuantity(specimen.getInitialQuantity().toString());
		}

		specimenDataBean.setCheckedSpecimen(false);
		specimenDataBean.setPrintSpecimen(specimenDataBean.getPrintSpecimen());// Bug

		if (Constants.SPECIMEN_COLLECTED.equals(specimen.getCollectionStatus()))
		{
			specimenDataBean.setReadOnly(true);
		}
		specimenDataBean.setType(specimen.getSpecimenType());
		final SpecimenCharacteristics characteristics = specimen.getSpecimenCharacteristics();
		if (characteristics != null)
		{
			specimenDataBean.setTissueSide(characteristics.getTissueSide());
			specimenDataBean.setTissueSite(characteristics.getTissueSite());
		}
		String concentration = "";
		if ("Molecular".equals(specimen.getClassName()))
		{
			concentration = String.valueOf(((MolecularSpecimen) specimen)
					.getConcentrationInMicrogramPerMicroliter());
		}
		specimenDataBean.setConcentration(concentration);

		String storageType = null;

		if (specimen != null && specimen.getSpecimenPosition() != null)
		{
			final StorageContainer container = specimen.getSpecimenPosition().getStorageContainer();
			LOGGER.info("-----------Container while getting from domain--:" + container);
			specimenDataBean.setContainerId(String.valueOf(container.getId()));
			specimenDataBean.setSelectedContainerName(container.getName());
			specimenDataBean.setPositionDimensionOne(String.valueOf(specimen.getSpecimenPosition()
					.getPositionDimensionOne()));
			specimenDataBean.setPositionDimensionTwo(String.valueOf(specimen.getSpecimenPosition()
					.getPositionDimensionTwo()));
			specimenDataBean.setStorageContainerForSpecimen("Auto");
		}
		// Mandar : 18Aug08 to check for virtual specimens which are collected
		// after updating the initial storage types. START
		else if (specimen != null && specimen.getSpecimenPosition() == null
				&& "Collected".equals(specimen.getCollectionStatus()))
		{
			specimenDataBean.setStorageContainerForSpecimen("Virtual");
		} // //Mandar : 18Aug08 to check for virtual specimens which are
		// collected after updating the initial storage types. END
		else
		{
			storageType = this.getStorageType(specimen);
			specimenDataBean.setStorageContainerForSpecimen(storageType);

		}
		specimenDataBean.setGenerateLabel(isGenLabel(specimen));
		return specimenDataBean;
	}


	/**
	 * Checks if is gen label.
	 *
	 * @param objSpecimen the obj specimen
	 *
	 * @return true, if checks if is gen label
	 */
	private boolean isGenLabel(final Specimen objSpecimen)
	{
		boolean isGenLabelON = false;

		String lineage = objSpecimen.getLineage();
		String specimenLabelFormat = objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getSpecimenLabelFormat();
		String derLabelFormat = objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getDerivativeLabelFormat();
		String alLabelFomrat = objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getAliquotLabelFormat();

		if(isLabelFormatEmpty(objSpecimen))
		{
			isGenLabelON = false;
		}
		else if(isLabelFromatCPDefault(objSpecimen))
		{
			isGenLabelON = SpecimenUtil.isLblGenOnForCP(specimenLabelFormat, derLabelFormat, alLabelFomrat, lineage);
		}
		else if(!isLabelFromatCPDefault(objSpecimen))
		{
			isGenLabelON = true;
		}

		else if(objSpecimen.getSpecimenRequirement() == null)
		{
			isGenLabelON = SpecimenUtil.isLblGenOnForCP(specimenLabelFormat, derLabelFormat, alLabelFomrat, lineage);
		}
		return isGenLabelON;
	}

	/**
	 * @param objSpecimen
	 * @return
	 */
	private boolean isLabelFromatCPDefault(final Specimen objSpecimen)
	{
		return objSpecimen.getSpecimenRequirement() != null && !Validator.isEmpty(objSpecimen.getSpecimenRequirement().getLabelFormat()) && objSpecimen.getSpecimenRequirement().getLabelFormat().contains("%CP_DEFAULT%");
	}

	/**
	 * @param objSpecimen
	 * @return
	 */
	private boolean isLabelFormatEmpty(final Specimen objSpecimen)
	{
		return objSpecimen.getSpecimenRequirement() != null && Validator.isEmpty(objSpecimen.getSpecimenRequirement().getLabelFormat());
	}

	/**
	 *
	 * @param specimenDataBean : specimenDataBean
	 */
	private void addToSpToAutoCont(GenericSpecimenVO specimenDataBean,
			SpecimenAutoStorageContainer autoStorageContainer)
	{
		if ("Auto".equals(specimenDataBean.getStorageContainerForSpecimen()))
		{
			autoStorageContainer.addSpecimen(specimenDataBean, specimenDataBean.getClassName());
		}

	}

	/**
	 *
	 * @param specimen : specimen
	 * @return String : String
	 */
	private String getStorageType(Specimen specimen)
	{
		String storageType;
		final SpecimenRequirement reqSpecimen = specimen.getSpecimenRequirement();
		if (reqSpecimen == null)
		{
			storageType = "Virtual";
		}
		else
		{
			storageType = reqSpecimen.getStorageType();
		}
		return storageType;
	}

	/**
	 *
	 * @param session : session
	 * @param specimenId : specimenId
	 * @param specimenList : specimenList
	 * @throws DAOException : DAOException
	 * @throws BizLogicException : BizLogicException
	 */
	private void addSpecimensToSession(HttpSession session, Long specimenId, List specimenList,
			SpecimenAutoStorageContainer autoStorageContainer, DAO dao) throws DAOException,
			BizLogicException
	{
		final LinkedHashMap<String, CollectionProtocolEventBean> cpEventMap = new LinkedHashMap<String, CollectionProtocolEventBean>();
		final NewSpecimenBizLogic specBizLogic = new NewSpecimenBizLogic();
		final CollectionProtocolEventBean eventBean = new CollectionProtocolEventBean();

		eventBean.setUniqueIdentifier(String.valueOf(specimenId.longValue()));

		// sorting of specimen collection accoding to id

		final Comparator spIdComp = new IdComparator();
		Collections.sort(specimenList, spIdComp);

		// List specList = new LinkedList();
		long lastAliquotNo = specBizLogic.getTotalNoOfAliquotSpecimen(specimenId, dao);
		this.setLabelInSpecimen(specimenId, specimenList, lastAliquotNo);

		final Iterator itr2 = specimenList.iterator();
		while (itr2.hasNext())
		{
			final Specimen specimen = (Specimen) itr2.next();
			if (specimen.getChildSpecimenCollection() != null)
			{
				lastAliquotNo = specBizLogic.getTotalNoOfAliquotSpecimen(specimen.getId(), dao);
				final LinkedList<Specimen> childList = new LinkedList(specimen
						.getChildSpecimenCollection());
				Collections.sort(childList, spIdComp);
				this.setLabelInSpecimen(specimen.getId(), childList, lastAliquotNo);
			}

		}
		eventBean.setSpecimenRequirementbeanMap(this.getSpecimensMap(specimenList, specimenId,
				this.cpId, autoStorageContainer));

		final String globalSpecimenId = "E" + eventBean.getUniqueIdentifier() + "_";
		cpEventMap.put(globalSpecimenId, eventBean);
		session.removeAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
		session.setAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP, cpEventMap);
	}

	/**
	 *
	 * @param specimenId : specimenId
	 * @param scgSpecimenList : scgSpecimenList
	 * @param session : session
	 * @throws DAOException : DAOException
	 * @throws BizLogicException : BizLogicException
	 */
	private void getSpcimensToShowOnSummary(long specimenId, Collection scgSpecimenList,
			HttpSession session, SpecimenAutoStorageContainer autoStorageContainer, DAO dao)
			throws DAOException, BizLogicException
	{
		final List<Specimen> specimenList = new ArrayList<Specimen>();
		// Collection scgSpecimenList =
		// specimencollectionGroup.getSpecimenCollection();
		// getSpcimensToShowOnSummary(specimenId,scgSpecimenList);
		if (scgSpecimenList != null)
		{
			final Iterator itr = scgSpecimenList.iterator();
			while (itr.hasNext())
			{
				final Specimen specimen = (Specimen) itr.next();
				if (!Status.ACTIVITY_STATUS_DISABLED.toString()
						.equals(specimen.getActivityStatus()))
				{
					if (specimen.getId().equals(specimenId)
							|| (specimen.getParentSpecimen() != null && specimen
									.getParentSpecimen().getId().equals(specimenId)))
					{
						specimenList.add(specimen);
					}
				}
			}
		}
		this.addSpecimensToSession(session, specimenId, specimenList, autoStorageContainer, dao);
	}

	/**
	 *
	 * @param specimenId : specimenId
	 * @param specimenList : specimenList
	 * @param lastChildNo : lastChildNo
	 * @throws DAOException : DAOException
	 */
	public void setLabelInSpecimen(Long specimenId, Collection specimenList, long lastChildNo)
			throws DAOException
	{
		if (specimenList != null)
		{
			final Iterator itr = specimenList.iterator();
			while (itr.hasNext())
			{
				final Specimen specimen = (Specimen) itr.next();
				lastChildNo = setLabelForSpecimen(specimenId, lastChildNo, specimen);
			}
		}
	}

	/**
	 * Set Label For Specimen.
	 * @param specimenId Long
	 * @param lastChildNo Long
	 * @param specimen Specimen
	 * @return lastChildNo long
	 */
	private long setLabelForSpecimen(Long specimenId, long lastChildNo, final Specimen specimen)
	{
		if (specimen.getId().equals(specimenId)
				|| (specimen.getParentSpecimen() != null && specimen.getParentSpecimen()
						.getId().equals(specimenId)))
		{
			if (specimen.getLabel() == null || specimen.getLabel().equals(""))
			{
				if (specimen.getLineage().equals(Constants.ALIQUOT))
				{
					if(!isGenLabel(specimen))
					{
						if (specimen.getParentSpecimen().getLabel() != null)
						{
							specimen.setLabel(specimen.getParentSpecimen().getLabel() + "_"
									+ (++lastChildNo));
						}
					}
				}
			}
		}
		return lastChildNo;
	}
}
