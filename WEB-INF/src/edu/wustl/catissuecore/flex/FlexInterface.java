
package edu.wustl.catissuecore.flex;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import edu.emory.mathcs.backport.java.util.Collections;
import edu.wustl.cab2b.client.ui.query.ClientQueryBuilder;
import edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface;
import edu.wustl.cab2b.client.ui.query.IPathFinder;
import edu.wustl.catissuecore.bean.CpAndParticipentsBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bean.SpecimenDataBean;
import edu.wustl.catissuecore.bizlogic.BiohazardBizLogic;
import edu.wustl.catissuecore.bizlogic.CpBasedViewBizLogic;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.ParticipantComparator;
import edu.wustl.catissuecore.util.SpecimenAutoStorageContainer;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.query.impl.CommonPathFinder;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.metadata.path.Path;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.query.flex.dag.CustomFormulaNode;
import edu.wustl.query.flex.dag.DAGConstant;
import edu.wustl.query.flex.dag.DAGNode;
import edu.wustl.query.flex.dag.DAGPanel;
import edu.wustl.query.flex.dag.DAGPath;
import edu.wustl.query.flex.dag.SingleNodeCustomFormulaNode;

/**
 * Flex interface class for displaying the collection protocols and participants.
 */
public class FlexInterface
{

	/**
	 * Logger object.
	 */
	private final static transient Logger logger = Logger.getCommonLogger(FlexInterface.class);
	private HttpSession session = null;
	private boolean isToInitializeSession = true;

	private boolean generateLabel = false;
	/**
	 * Constructor.
	 * @throws Exception : Exception
	 */
	public FlexInterface() throws Exception
	{
	}

	public void setSessionInitializationFalse()
	{
		isToInitializeSession = false;
	}
	/**
	 * @param str.
	 * @return SpecimenBean object
	 */
	public SpecimenBean say(String str)
	{
		final SpecimenBean sb = new SpecimenBean();
		sb.specimenLabel = "AA";
		return sb;
	}

	/**
	 * @param mode.
	 * @param parentType
	 * @param parentName
	 * @return SpecimenBean object
	 * @throws DAOException
	 */
	public SpecimenBean initFlexInterfaceForMultipleSp(String mode, String parentType,
			String parentName) throws DAOException
	{
		if(isToInitializeSession)
		{
			this.session = flex.messaging.FlexContext.getHttpRequest().getSession();
		}
		final SessionDataBean sdb = (SessionDataBean) this.session
				.getAttribute(Constants.SESSION_DATA);
		final SpecimenBean spBean = new SpecimenBean();

		final EventParamtersBean collEvBean = new EventParamtersBean();
		final EventParamtersBean recEvBean = new EventParamtersBean();
		spBean.collectionEvent = collEvBean;
		spBean.receivedEvent = recEvBean;

		spBean.collectionEvent.userName = sdb.getLastName() + ", " + sdb.getFirstName();
		spBean.receivedEvent.userName = sdb.getLastName() + ", " + sdb.getFirstName();
		spBean.comment = "";

		SpecimenCollectionGroup scg = null;
		try
		{
			if (Constants.ADD.equals(mode)
					&& edu.wustl.catissuecore.util.global.Constants.NEW_SPECIMEN_TYPE
							.equals(parentType))
			{
				if (parentName != null)
				{
					 scg = this.getSpecimenCollGrp(parentName);
					if (scg != null)
					{
						final SpecimenCollectionGroupBizLogic bizLogic = new SpecimenCollectionGroupBizLogic();
						final Collection eventColl = (Collection) bizLogic.retrieveAttribute(
								SpecimenCollectionGroup.class.getName(), scg.getId(),
								"elements(specimenEventParametersCollection)");
						if (eventColl != null && !eventColl.isEmpty())
						{
							final Iterator itr = eventColl.iterator();
							while (itr.hasNext())
							{
								final SpecimenEventParameters event = (SpecimenEventParameters) itr
										.next();
								final String[] selectColName = {"user"};
								final String[] whereColName = {"id"};
								final String[] whereColCond = {"="};
								final Object[] whereColVal = {event.getId()};
								final List list = bizLogic.retrieve(SpecimenEventParameters.class
										.getName(), selectColName, whereColName, whereColCond,
										whereColVal, Constants.AND_JOIN_CONDITION);
								logger.info("List:" + list);
								if (list != null && !list.isEmpty())
								{
									final User user = (User) list.get(0);
									event.setUser(user);
								}
								if (event instanceof CollectionEventParameters)
								{
									collEvBean.copy(event);
								}
								else if (event instanceof ReceivedEventParameters)
								{
									recEvBean.copy(event);
								}
							}
						}
					}
				}
			}
		}
		catch (final Exception e)
		{
			FlexInterface.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

		if(scg != null)
		{
			generateLabel=scg.getCollectionProtocolRegistration().getCollectionProtocol().getGenerateLabel();
		}

		return spBean;
	}

	/**
	 * @param nvBeanList.
	 * @return list
	 */
	private List<String> toStrList(List<NameValueBean> nvBeanList)
	{
		final List<String> strList = new ArrayList<String>();
		for (final NameValueBean bean : nvBeanList)
		{
			strList.add(bean.getName());
		}
		return strList;
	}

	/**
	 * @return list.
	 */
	public List<String> getTissueSidePVList()
	{
		final List<NameValueBean> aList = CDEManager.getCDEManager().getPermissibleValueList(
				"Tissue Side", null);
		return this.toStrList(aList);
	}

	/**
	 * @return List.
	 * @throws BizLogicException :BizLogicException
	 */
	public List<String> getTissueSitePVList() throws BizLogicException
	{
		final List<NameValueBean> aList = AppUtility.tissueSiteList();
		return this.toStrList(aList);
	}

	/**
	 * @return list of Pathological Status permissible values.
	 */
	public List<String> getPathologicalStatusPVList()
	{
		final List<NameValueBean> aList = CDEManager.getCDEManager().getPermissibleValueList(
				"Pathological Status", null);
		return this.toStrList(aList);
	}

	/**
	 * @return List.
	 */
	public List<String> getSpecimenClassStatusPVList()
	{
		final Map<String, List<NameValueBean>> specimenTypeMap = AppUtility.getSpecimenTypeMap();
		final Set<String> specimenKeySet = specimenTypeMap.keySet();
		final List<NameValueBean> specimenClassList = new ArrayList<NameValueBean>();

		final Iterator itr1 = specimenKeySet.iterator();
		while (itr1.hasNext())
		{
			final String specimenKey = (String) itr1.next();
			specimenClassList.add(new NameValueBean(specimenKey, specimenKey));
		}
		return this.toStrList(specimenClassList);
	}

	/**
	 * @return List.
	 */
	public List<String> getFluidSpecimenTypePVList()
	{
		final Map specimenTypeMap = AppUtility.getSpecimenTypeMap();
		final List<NameValueBean> aList = (List) specimenTypeMap.get("Fluid");
		return this.toStrList(aList);
	}

	/**
	 * @return List.
	 */
	public List<String> getTissueSpecimenTypePVList()
	{
		final Map specimenTypeMap = AppUtility.getSpecimenTypeMap();
		final List<NameValueBean> aList = (List) specimenTypeMap.get("Tissue");
		return this.toStrList(aList);
	}

	/**
	 * @return List.
	 */
	public List<String> getMolecularSpecimenTypePVList()
	{
		final Map specimenTypeMap = AppUtility.getSpecimenTypeMap();
		final List<NameValueBean> aList = (List<NameValueBean>) specimenTypeMap.get("Molecular");
		return this.toStrList(aList);
	}

	/**
	 * @return List.
	 */
	public List<String> getCellSpecimenTypePVList()
	{
		final Map specimenTypeMap = AppUtility.getSpecimenTypeMap();
		final List<NameValueBean> aList = (List<NameValueBean>) specimenTypeMap.get("Cell");
		return this.toStrList(aList);
	}

	/**
	 * @return List.
	 */
	public List getSpecimenTypeStatusPVList()
	{
		return CDEManager.getCDEManager().getPermissibleValueList("Specimen Type", null);
	}

	/**
	 * @return List.
	 */
	public List getSCGList()
	{
		return null;
	}

	/**
	 * @return List.
	 * @throws BizLogicException : BizLogicException
	 */
	public List getUserList() throws BizLogicException
	{
		final UserBizLogic userBizLogic = new UserBizLogic();
		final List userList = userBizLogic.getUsers(Constants.ADD);
		return this.toStrList(userList);

	}

	/**
	 * @return List
	 */
	public List getProcedureList()
	{
		final List procedureList = CDEManager.getCDEManager().getPermissibleValueList(
				edu.wustl.catissuecore.util.global.Constants.CDE_NAME_COLLECTION_PROCEDURE, null);
		return this.toStrList(procedureList);
	}

	/**
	 * @return List
	 */
	public List getContainerList()
	{
		final List containerList = CDEManager.getCDEManager().getPermissibleValueList(
				edu.wustl.catissuecore.util.global.Constants.CDE_NAME_CONTAINER, null);
		return this.toStrList(containerList);
	}

	/**
	 * @return List
	 */
	public List getReceivedQualityList()
	{
		final List qualityList = CDEManager.getCDEManager().getPermissibleValueList(
				edu.wustl.catissuecore.util.global.Constants.CDE_NAME_RECEIVED_QUALITY, null);
		return this.toStrList(qualityList);
	}

	/**
	 * @return List
	 */
	public List getBiohazardTypeList()
	{
		final List biohazardList = CDEManager.getCDEManager().getPermissibleValueList(
				edu.wustl.catissuecore.util.global.Constants.CDE_NAME_BIOHAZARD, null);
		return this.toStrList(biohazardList);

	}

	/**
	 * @return List
	 */
	public List getBiohazardNameList()
	{
		final List<List> biohazardNameList = new ArrayList<List>();
		try
		{
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic) factory
					.getBizLogic(edu.wustl.catissuecore.util.global.Constants.NEW_SPECIMEN_FORM_ID);
			final List biohazardTypeList = this.getBiohazardTypeList();
			final Iterator biohazardTypeItr = biohazardTypeList.iterator();
			while (biohazardTypeItr.hasNext())
			{
				final List<String> nameList = new ArrayList<String>();
				nameList.add(Constants.SELECT_OPTION);
				//NameValueBean nvb = (NameValueBean) biohazardTypeItr.next();
				final String type = (String) biohazardTypeItr.next();

				final String[] selectColNames = {"name"};
				final String[] whereColName = {"type"};
				final String[] whereColCond = {"="};
				final Object[] whereColVal = {type};
				final List list = bizLogic.retrieve(Biohazard.class.getName(), selectColNames,
						whereColName, whereColCond, whereColVal, Constants.AND_JOIN_CONDITION);
				if (list != null && list.size() > 0)
				{
					final Iterator iterator = list.iterator();
					while (iterator.hasNext())
					{
						final String name = (String) iterator.next();
						nameList.add(name);
					}
				}
				biohazardNameList.add(nameList);
			}
		}
		catch (final BizLogicException e)
		{
			FlexInterface.logger.error("Error mesg :" + e.getMessage(),e);
			e.printStackTrace();
		}

		return biohazardNameList;

	}

	/**
	 * @param spBeanList : SpecimenBean object.
	 * @return String.
	 */
	public String writeSpecimen(List<SpecimenBean> spBeanList)
	{
		logger.debug("spBeanList size " + spBeanList.size());
		//Map<String, String> msgMap = new HashMap<String, String>();
		//LinkedHashMap<Specimen,List> specimenMap = new LinkedHashMap<Specimen,List>();
		final LinkedHashMap<String, GenericSpecimen> viewSpecimenMap = new LinkedHashMap<String, GenericSpecimen>();
		String message = "ERROR";

		final SessionDataBean sdb = (SessionDataBean) this.session
				.getAttribute(Constants.SESSION_DATA);
		final SpecimenAutoStorageContainer speicmenAutoStorageCont = new SpecimenAutoStorageContainer();
		int i = 1;
		try
		{
			for (final SpecimenBean spBean : spBeanList)
			{
				SpecimenDataBean specimenDataBean = this.prepareGenericSpecimen(spBean,
						speicmenAutoStorageCont);
				specimenDataBean = this.getStorageContainers(specimenDataBean, specimenDataBean
						.getCollectionProtocolId(), speicmenAutoStorageCont);
				specimenDataBean.uniqueId = "" + i;
				viewSpecimenMap.put(specimenDataBean.getUniqueIdentifier(), specimenDataBean);
				i++;

			}
			speicmenAutoStorageCont.setCollectionProtocolSpecimenStoragePositions(sdb);
			this.session.setAttribute(
					edu.wustl.catissuecore.util.global.Constants.SPECIMEN_LIST_SESSION_MAP,
					viewSpecimenMap);

			message = "SUCCESS";
		}
		catch (final Exception ex)
		{
			message = ex.getMessage();
			FlexInterface.logger.error(message, ex);
			ex.printStackTrace();
		}

		return message;
	}

	/**
	 * @param spBeanList : SpecimenBean object.
	 * @return String
	 */
	public String writeSpecimen1(List<SpecimenBean> spBeanList)
	{
		final LinkedHashSet<Specimen> specimenHashSet = new LinkedHashSet<Specimen>();
		String message = "ERROR";
		for (final SpecimenBean spBean : spBeanList)
		{
			final Specimen specimen = this.prepareSpecimen(spBean);

			specimenHashSet.add(specimen);
		}
		try
		{
			this.session.setAttribute(
					edu.wustl.catissuecore.util.global.Constants.SPECIMEN_LIST_SESSION_MAP,
					specimenHashSet);

			message = "SUCCESS";
		}
		catch (final Exception ex)
		{
			message = ex.getMessage();
			FlexInterface.logger.error(message, ex);
			ex.printStackTrace();
		}

		return message;
	}

	/**
	 * @param spBeanList : SpecimenBean object.
	 * @return String
	 */
	public String editSpecimen(List<SpecimenBean> spBeanList)
	{
		if(isToInitializeSession)
		{
			this.session = flex.messaging.FlexContext.getHttpRequest().getSession();
		}
		final LinkedHashMap specimenMap = (LinkedHashMap) this.session
				.getAttribute(edu.wustl.catissuecore.util.global.Constants.SPECIMEN_LIST_SESSION_MAP);
		final Iterator itr = specimenMap.values().iterator();

		final SpecimenAutoStorageContainer speicmenAutoStorageCont = new SpecimenAutoStorageContainer();

		while (itr.hasNext())
		{
			final SpecimenDataBean spDataBean = (SpecimenDataBean) itr.next();
			if (spDataBean != null)
			{
				for (final SpecimenBean spBean : spBeanList)
				{
					if (spDataBean.getId() == spBean.spID.longValue())
					{
						if (spBean.specimenLabel != null
								&& !spBean.specimenLabel.equals(spDataBean.getLabel()))
						{
							spDataBean.setLabel(spBean.specimenLabel);
						}
						if (spBean.specimenBarcode != null
								&& !spBean.specimenBarcode.equals(spDataBean.getBarCode()))
						{
							spDataBean.setBarCode(spBean.specimenBarcode);
						}
						if (spDataBean.getCorresSpecimen() != null && spBean.creationDate != null)
						{
							spDataBean.getCorresSpecimen().setCreatedOn(spBean.creationDate);
						}
						if (spBean.quantity != null
								&& !spBean.quantity.toString().equals(spDataBean.getQuantity()))
						{
							spDataBean.setQuantity(spBean.quantity.toString());
						}
						if (spBean.concentration != null
								&& !spBean.concentration.toString().equals(
										spDataBean.getConcentration()))
						{
							spDataBean.setConcentration(spBean.concentration.toString());
						}
						if (spBean.comment != null
								&& !spBean.comment.equals(spDataBean.getComment()))
						{
							spDataBean.setComment(spBean.comment);
						}
						if (spBean.pathologicalStatus != null
								&& !spBean.pathologicalStatus.equals(spDataBean
										.getPathologicalStatus()))
						{
							spDataBean.setPathologicalStatus(spBean.pathologicalStatus);
						}
						if (spBean.tissueSite != null
								&& !spBean.tissueSite.equals(spDataBean.getTissueSite()))
						{
							spDataBean.setTissueSite(spBean.tissueSite);
						}
						if (spBean.tissueSide != null
								&& !spBean.tissueSide.equals(spDataBean.getTissueSide()))
						{
							spDataBean.setTissueSide(spBean.tissueSide);
						}

						if (spBean.exIdColl != null && !spBean.exIdColl.isEmpty())
						{
							spDataBean.setExternalIdentifierCollection(this
									.getExternalIdentifierColl(spBean.exIdColl));
						}

						if (spBean.biohazardColl != null && !spBean.biohazardColl.isEmpty())
						{
							spDataBean.setBiohazardCollection(this
									.getBiohazardColl(spBean.biohazardColl));
						}

						try
						{
							if (spBean.derivedColl != null && !spBean.derivedColl.isEmpty())
							{
								int i = 1;
								final LinkedHashMap<String, GenericSpecimen> derivedMap = new LinkedHashMap<String, GenericSpecimen>();

								for (final SpecimenBean derivedBean : spBean.derivedColl)
								{
									derivedBean.parentName = spBean.specimenLabel;
									derivedBean.parentType = edu.wustl.catissuecore.util.global.Constants.DERIVED_SPECIMEN_TYPE;
									SpecimenDataBean derivedDataBean = this.prepareGenericSpecimen(
											derivedBean, speicmenAutoStorageCont);
									derivedDataBean = this.getStorageContainers(derivedDataBean,
											derivedDataBean.getCollectionProtocolId(),
											speicmenAutoStorageCont);
									derivedDataBean.uniqueId = "d" + i;
									derivedMap.put("d" + i, derivedDataBean);
									i++;

								}
								spDataBean.setDeriveSpecimenCollection(derivedMap);
							}
						}
						catch (final ApplicationException e)
						{
							FlexInterface.logger.error(e.getMessage(), e);
							e.printStackTrace();
						}
					}

				}

			}

		}

		try
		{
			final SessionDataBean sdb = (SessionDataBean) this.session
					.getAttribute(Constants.SESSION_DATA);
			speicmenAutoStorageCont.setCollectionProtocolSpecimenStoragePositions(sdb);
		}
		catch (final ApplicationException e)
		{
			FlexInterface.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return "success:Specimens Updated Successfully";
	}

	/**
	 * @return SpecimenBean object.
	 */
	public SpecimenBean readSpecimen()
	{
		logger.info("SERVER readSpecimen");
		final SpecimenBean sb = new SpecimenBean();
		sb.specimenLabel = "tp";
		sb.tissueSite = "VULVA";
		sb.specimenClass = "Molecular";
		sb.specimenType = "DNA";
		return sb;
	}

	/**
	 * @return List of specimens.
	 * @throws BizLogicException : BizLogicException
	 */
	public List<SpecimenBean> readSpecimenList() throws BizLogicException
	{
		final List<SpecimenBean> list = new ArrayList<SpecimenBean>();
		if(isToInitializeSession)
		{
			this.session = flex.messaging.FlexContext.getHttpRequest().getSession();
		}
		final HashSet specimenIdList = (HashSet) this.session.getAttribute("specimenId");
		final LinkedHashMap<String, GenericSpecimen> viewSpecimenMap = new LinkedHashMap<String, GenericSpecimen>();
		if (specimenIdList != null)
		{
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic) factory
					.getBizLogic(edu.wustl.catissuecore.util.global.Constants.NEW_SPECIMEN_FORM_ID);

			int i = 1;
			final Iterator itr = specimenIdList.iterator();
			while (itr.hasNext())
			{
				final String specimenId = (itr.next()).toString();
				final Object object = bizLogic.retrieve(Specimen.class.getName(), Long
						.valueOf(specimenId));
				//Object object = bizLogic.retrieve(Specimen.class.getName(), new Long(specimenId));
				if (object != null)
				{
					Specimen specimen = (Specimen) object;
					final Collection exIdColl = (Collection) bizLogic.retrieveAttribute(
							Specimen.class.getName(), specimen.getId(),
							"elements(externalIdentifierCollection)");
					specimen.setExternalIdentifierCollection(exIdColl);
					final SpecimenCollectionGroup scg = this
							.getSpecimenCollGrpForSpecimen(specimenId);
					specimen.setSpecimenCollectionGroup(scg);
					specimen = this.setStorageContForSpecimen(specimen);
					final SpecimenBean sb = this.prepareSpecimenBean(specimen, Constants.EDIT);
					list.add(sb);
					final SpecimenDataBean sdb = this.prepareGenericSpecimen(specimen);
					sdb.setUniqueIdentifier("" + i);
					viewSpecimenMap.put("" + i, sdb);
					i++;
				}
			}
		}
		if (this.session
				.getAttribute(edu.wustl.catissuecore.util.global.Constants.SPECIMEN_LIST_SESSION_MAP) != null)
		{
			this.session
					.removeAttribute(edu.wustl.catissuecore.util.global.Constants.SPECIMEN_LIST_SESSION_MAP);
		}
		this.session.setAttribute(
				edu.wustl.catissuecore.util.global.Constants.SPECIMEN_LIST_SESSION_MAP,
				viewSpecimenMap);
		return list;
	}

	/**
	 * @param specimen.
	 * @param mode
	 * @return SpecimenBean object
	 */
	private SpecimenBean prepareSpecimenBean(Specimen specimen, String mode)
	{
		final SpecimenBean sb = new SpecimenBean();
		sb.mode = mode;

		if (specimen.getId() != null)
		{
			sb.spID = specimen.getId();
		}
		if (specimen.getLabel() != null)
		{
			sb.specimenLabel = specimen.getLabel();
		}
		if (specimen.getBarcode() != null)
		{
			sb.specimenBarcode = specimen.getBarcode();
		}
		if (specimen.getLineage() != null)
		{
			sb.lineage = specimen.getLineage();
		}
		if (specimen.getClassName() != null)
		{
			sb.specimenClass = specimen.getClassName();
		}
		if (specimen.getSpecimenType() != null)
		{
			sb.specimenType = specimen.getSpecimenType();
		}
		if (specimen.getPathologicalStatus() != null)
		{
			sb.pathologicalStatus = specimen.getPathologicalStatus();
		}
		if (specimen.getInitialQuantity() != null)
		{
			sb.quantity = specimen.getInitialQuantity();
		}
		if (specimen.getCreatedOn() != null)
		{
			sb.creationDate = specimen.getCreatedOn();
		}

		final SpecimenCharacteristics characteristic = specimen.getSpecimenCharacteristics();
		if (characteristic != null)
		{
			if (characteristic.getTissueSide() != null)
			{
				sb.tissueSide = characteristic.getTissueSide();
			}
			if (characteristic.getTissueSite() != null)
			{
				sb.tissueSite = characteristic.getTissueSite();
			}
		}
		if (specimen.getComment() != null)
		{
			sb.comment = specimen.getComment();
		}
		if (specimen.getBiohazardCollection() != null)
		{
			//sb.biohazardColl = new ArrayList(specimen.getBiohazardCollection());
			sb.biohazardColl = this.getBiohazardBeanCollection(specimen.getBiohazardCollection());
		}
		if (specimen.getExternalIdentifierCollection() != null)
		{
			//	sb.exIdColl = new ArrayList(specimen.getExternalIdentifierCollection());
			sb.exIdColl = this.getExternalIdentiferBeanCollection(specimen
					.getExternalIdentifierCollection());
		}
		if (specimen.getClassName().equalsIgnoreCase("Molecular"))
		{
			sb.concentration = ((MolecularSpecimen) specimen)
					.getConcentrationInMicrogramPerMicroliter();
		}

		return sb;
	}

	/**
	 * @param biohazardColl .
	 * @return list of BiohazardBeanCollection
	 */
	private List getBiohazardBeanCollection(Collection biohazardColl)
	{
		final List<BiohazardBean> biozardBeanColl = new ArrayList<BiohazardBean>();
		if (biohazardColl != null)
		{
			final Iterator biohazardItr = biohazardColl.iterator();
			while (biohazardItr.hasNext())
			{
				final Biohazard biohazard = (Biohazard) biohazardItr.next();
				final BiohazardBean biohazardBean = new BiohazardBean();
				biohazardBean.setId(biohazard.getId());
				biohazardBean.setName(biohazard.getName());
				biohazardBean.setType(biohazard.getType());
				biozardBeanColl.add(biohazardBean);
			}
		}
		return biozardBeanColl;
	}

	/**
	 * @param externalIdentiferColl.
	 * @return list of ExternalIdentiferBeanCollection
	 */
	private List getExternalIdentiferBeanCollection(Collection externalIdentiferColl)
	{
		final List<ExternalIdentifierBean> externalIdentiferBeanColl = new ArrayList<ExternalIdentifierBean>();
		if (externalIdentiferColl != null)
		{
			final Iterator externalIdItr = externalIdentiferColl.iterator();
			while (externalIdItr.hasNext())
			{
				final ExternalIdentifier externalIdentifer = (ExternalIdentifier) externalIdItr
						.next();
				final ExternalIdentifierBean externalIdBean = new ExternalIdentifierBean();
				externalIdBean.setId(externalIdentifer.getId());
				externalIdBean.setName(externalIdentifer.getName());
				externalIdBean.setValue(externalIdentifer.getValue());
				externalIdentiferBeanColl.add(externalIdBean);
			}
		}
		return externalIdentiferBeanColl;
	}

	/**
	 * @param specimenClass.
	 * @return Specimen object
	 */
	private Specimen getSpecimenInstance(String specimenClass)
	{
		Specimen sp = null;
		if (specimenClass.indexOf("Tissue") != -1)
		{
			sp = new TissueSpecimen();
		}
		else if (specimenClass.indexOf("Fluid") != -1)
		{
			sp = new FluidSpecimen();
		}
		else if (specimenClass.indexOf("Molecular") != -1)
		{
			sp = new MolecularSpecimen();
		}
		else if (specimenClass.indexOf("Cell") != -1)
		{
			sp = new CellSpecimen();
		}

		return sp;
	}

	/**
	 * @param spBean.
	 * @param speicmenAutoStorageCont
	 * @return SpecimenDataBean object
	 * @throws ApplicationException
	 */
	private SpecimenDataBean prepareGenericSpecimen(SpecimenBean spBean,
			SpecimenAutoStorageContainer speicmenAutoStorageCont) throws ApplicationException
	{
		final SpecimenDataBean specimenDataBean = new SpecimenDataBean();
		final Specimen corresSpecimen = new Specimen();
		corresSpecimen.setCreatedOn(spBean.creationDate);
		specimenDataBean.setCorresSpecimen(corresSpecimen);
		specimenDataBean.setType(spBean.specimenType);
		specimenDataBean.setStorageContainerForSpecimen(spBean.storage);
		specimenDataBean.setQuantity(String.valueOf(spBean.quantity));
		if(!Validator.isEmpty(spBean.specimenLabel) && spBean.specimenLabel.equals("AutoGenerated"))
		{
			specimenDataBean.setGenerateLabel(true);
		}
		if (edu.wustl.catissuecore.util.global.Constants.MOLECULAR.equals(spBean.specimenClass))
		{
			if (spBean.concentration != null && spBean.concentration.toString() != "")
			{
				specimenDataBean.setConcentration(String.valueOf(spBean.concentration));
			}
		}
		specimenDataBean.setClassName(spBean.specimenClass);
		if (Variables.isSpecimenBarcodeGeneratorAvl)
		{
			specimenDataBean.setBarCode(null);
			specimenDataBean.setShowBarcode(false);
			spBean.specimenBarcode = "";
		}
		else
		{
			specimenDataBean.setBarCode(spBean.specimenBarcode);
			specimenDataBean.setShowBarcode(true);
		}
		specimenDataBean.setComment(spBean.comment);
		specimenDataBean.setExternalIdentifierCollection(new HashSet<ExternalIdentifier>());
		if (spBean.exIdColl != null && !spBean.exIdColl.isEmpty())
		{
			specimenDataBean.setExternalIdentifierCollection(this
					.getExternalIdentifierColl(spBean.exIdColl));
		}

		if (spBean.biohazardColl != null && !spBean.biohazardColl.isEmpty())
		{
			specimenDataBean.setBiohazardCollection(this.getBiohazardColl(spBean.biohazardColl));
		}
		if (generateLabel)
		{
			specimenDataBean.setLabel(null);
			specimenDataBean.setShowLabel(false);
			spBean.specimenLabel = "";
		}
		else
		{
			specimenDataBean.setLabel(spBean.specimenLabel);
			specimenDataBean.setShowLabel(true);
		}

		specimenDataBean.setParentSpecimen(null);
		specimenDataBean.setPathologicalStatus(spBean.pathologicalStatus);

		specimenDataBean.setTissueSide(spBean.tissueSide);
		specimenDataBean.setTissueSite(spBean.tissueSite);

		CollectionProtocol cp = null;
		if (edu.wustl.catissuecore.util.global.Constants.NEW_SPECIMEN_TYPE
				.equals(spBean.parentType))
		{
			final SpecimenCollectionGroup scg = this.getSpecimenCollGrp(spBean.parentName);
			specimenDataBean.setLineage(edu.wustl.catissuecore.util.global.Constants.NEW_SPECIMEN);
			if (scg != null)
			{
				specimenDataBean.setSpecimenCollectionGroup(scg);
				if (scg.getId() != null)
				{
					cp = CollectionProtocolUtil.getCollectionProtocolForSCG(scg.getId().toString());
				}
			}

		}
		else
		{
			final Specimen parentSp = this.getParentSpecimen(spBean.parentName);
			specimenDataBean
					.setLineage(edu.wustl.catissuecore.util.global.Constants.DERIVED_SPECIMEN);
			if (parentSp != null)
			{
				specimenDataBean.setParentSpecimen(parentSp);
				specimenDataBean.setSpecimenCollectionGroup(parentSp.getSpecimenCollectionGroup());
				if (parentSp.getId() != null)
				{
					cp = CollectionProtocolUtil.getCollectionProtocolForSCG(parentSp
							.getSpecimenCollectionGroup().getId().toString());
				}
			}
		}

		if (cp != null && cp.getId() != null)
		{
			specimenDataBean.setCollectionProtocolId(cp.getId());
		}

		if (edu.wustl.catissuecore.util.global.Constants.NEW_SPECIMEN_TYPE
				.equals(spBean.parentType))
		{
			final Set<SpecimenEventParameters> eventSet = new HashSet<SpecimenEventParameters>();
			final CollectionEventParameters collectionEvent = this
					.getCollectionEventParameters(spBean.collectionEvent);
			eventSet.add(collectionEvent);

			final ReceivedEventParameters receEvent = this
					.getReceivedEventParameters(spBean.receivedEvent);
			eventSet.add(receEvent);

			specimenDataBean.setSpecimenEventCollection(eventSet);
		}
		if (spBean.derivedColl != null)
		{
			final LinkedHashMap<String, GenericSpecimen> derivedMap = new LinkedHashMap<String, GenericSpecimen>();
			final Iterator itr = spBean.derivedColl.iterator();
			int i = 1;
			while (itr.hasNext())
			{
				final SpecimenBean derivedBean = (SpecimenBean) itr.next();

				derivedBean.collectionEvent = spBean.collectionEvent;
				derivedBean.receivedEvent = spBean.receivedEvent;
				derivedBean.parentType = edu.wustl.catissuecore.util.global.Constants.DERIVED_SPECIMEN_TYPE;
				final SpecimenDataBean derivedDataBean = this.prepareGenericSpecimen(derivedBean,
						speicmenAutoStorageCont);
				derivedDataBean.setCollectionProtocolId(specimenDataBean.getCollectionProtocolId());
				derivedDataBean
						.setLineage(edu.wustl.catissuecore.util.global.Constants.DERIVED_SPECIMEN);
				derivedDataBean.setUniqueIdentifier("d" + i);
				derivedDataBean.setParentName(spBean.specimenLabel);
				derivedMap.put("d" + i, derivedDataBean);
				i++;
			}
			specimenDataBean.setDeriveSpecimenCollection(derivedMap);
		}
		return specimenDataBean;
	}

	/**
	 * @param sp
	 * @return SpecimenDataBean object.
	 */
	private SpecimenDataBean prepareGenericSpecimen(Specimen sp)
	{
		final SpecimenDataBean specimenDataBean = new SpecimenDataBean();
		specimenDataBean.setId(sp.getId());
		specimenDataBean.setType(sp.getSpecimenType());
		specimenDataBean.setStorageContainerForSpecimen("Auto");
		specimenDataBean.setCorresSpecimen(sp);
		specimenDataBean.setQuantity(String.valueOf(sp.getInitialQuantity()));
		specimenDataBean.setClassName(sp.getClassName());
		specimenDataBean.setBarCode(sp.getBarcode());
		specimenDataBean.setLineage(sp.getLineage());
		specimenDataBean.setComment(sp.getComment());
		specimenDataBean.setExternalIdentifierCollection(new HashSet<ExternalIdentifier>());
		specimenDataBean.setExternalIdentifierCollection(sp.getExternalIdentifierCollection());
		specimenDataBean.setBiohazardCollection(sp.getBiohazardCollection());
		specimenDataBean.setLabel(sp.getLabel());
		final Specimen parentSpecimen = (Specimen) sp.getParentSpecimen();
		specimenDataBean.setParentSpecimen(parentSpecimen);
		specimenDataBean.setPathologicalStatus(sp.getPathologicalStatus());

		specimenDataBean.setTissueSide(sp.getSpecimenCharacteristics().getTissueSide());
		specimenDataBean.setTissueSite(sp.getSpecimenCharacteristics().getTissueSite());
		specimenDataBean.setLineage(sp.getLineage());
		specimenDataBean.setSpecimenCollectionGroup(sp.getSpecimenCollectionGroup());
		specimenDataBean.setSpecimenEventCollection(sp.getSpecimenEventCollection());
		if (sp.getClassName().equalsIgnoreCase("Molecular"))
		{
			specimenDataBean.setConcentration(((MolecularSpecimen) sp)
					.getConcentrationInMicrogramPerMicroliter().toString());
		}
		if (sp.getSpecimenPosition() != null
				&& sp.getSpecimenPosition().getStorageContainer() != null)
		{
			specimenDataBean.setSelectedContainerName(sp.getSpecimenPosition()
					.getStorageContainer().getName());
			if (sp.getSpecimenPosition() != null)
			{
				specimenDataBean.setPositionDimensionOne(sp.getSpecimenPosition()
						.getPositionDimensionOne().toString());
				specimenDataBean.setPositionDimensionTwo(sp.getSpecimenPosition()
						.getPositionDimensionTwo().toString());
			}
		}
		else
		{
			specimenDataBean.setStorageContainerForSpecimen("Virtual");
		}
		return specimenDataBean;
	}

	/**
	 * @param specimenDataBean.
	 * @param collectionProtocolId
	 * @param speicmenAutoStorageCont
	 * @return SpecimenDataBean object
	 */
	private SpecimenDataBean getStorageContainers(SpecimenDataBean specimenDataBean,
			Long collectionProtocolId, SpecimenAutoStorageContainer speicmenAutoStorageCont)
	{

		if (specimenDataBean.getStorageContainerForSpecimen().equals("Auto"))
		{
			speicmenAutoStorageCont.addSpecimen(specimenDataBean, specimenDataBean.getClassName(),
					collectionProtocolId);
		}
		if (specimenDataBean.getDeriveSpecimenCollection() != null
				&& !specimenDataBean.getDeriveSpecimenCollection().isEmpty())
		{
			final Collection<GenericSpecimen> deriveSpColl = specimenDataBean
					.getDeriveSpecimenCollection().values();
			for (final GenericSpecimen sp : deriveSpColl)
			{
				if (sp.getStorageContainerForSpecimen().equals("Auto"))
				{
					speicmenAutoStorageCont
							.addSpecimen(sp, sp.getClassName(), collectionProtocolId);
				}
			}
		}
		return specimenDataBean;
	}

	/**
	 * @param spBean.
	 * @return Specimen object
	 */
	private Specimen prepareSpecimen(SpecimenBean spBean)
	{
		final Specimen specimen = this.getSpecimenInstance(spBean.specimenClass);
		specimen.setSpecimenType(spBean.specimenType);
		specimen.setId(spBean.spID);
		specimen.setCreatedOn(spBean.creationDate);
		//specimenDataBean.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		//sp.setAvailable(true);
		final Double qt = new Double(spBean.quantity);
		specimen.setInitialQuantity(qt);
		specimen.setAvailableQuantity(qt);
		if (edu.wustl.catissuecore.util.global.Constants.MOLECULAR.equals(spBean.specimenClass))
		{
			((MolecularSpecimen) specimen).setConcentrationInMicrogramPerMicroliter(Double
					.valueOf(spBean.getConcentration()));

		}

		specimen.setBarcode(spBean.specimenBarcode);

		specimen.setComment(spBean.comment);

		specimen.setExternalIdentifierCollection(new HashSet<ExternalIdentifier>());
		if (spBean.exIdColl != null && !spBean.exIdColl.isEmpty())
		{
			specimen.setExternalIdentifierCollection(this
					.getExternalIdentifierColl(spBean.exIdColl));
		}

		if (spBean.biohazardColl != null && !spBean.biohazardColl.isEmpty())
		{
			specimen.setBiohazardCollection(this.getBiohazardColl(spBean.biohazardColl));
		}
		specimen.setLabel(spBean.specimenLabel);

		specimen.setPathologicalStatus(spBean.pathologicalStatus);

		final SpecimenCharacteristics specimenCharacteristics = new SpecimenCharacteristics();
		specimenCharacteristics.setTissueSide(spBean.tissueSide);
		specimenCharacteristics.setTissueSite(spBean.tissueSite);
		specimen.setSpecimenCharacteristics(specimenCharacteristics);

		if (spBean.derivedColl != null)
		{
			final LinkedHashSet<AbstractSpecimen> derivedSpecimenSet = new LinkedHashSet<AbstractSpecimen>();
			final Iterator itr = spBean.derivedColl.iterator();
			int i = 1;
			while (itr.hasNext())
			{
				final SpecimenBean derivedBean = (SpecimenBean) itr.next();
				derivedBean.collectionEvent = spBean.collectionEvent;
				derivedBean.receivedEvent = spBean.receivedEvent;
				final Specimen derivedSp = this.prepareSpecimen(derivedBean);
				derivedSp.setLineage(edu.wustl.catissuecore.util.global.Constants.DERIVED_SPECIMEN);
				derivedSp.setSpecimenCharacteristics(specimen.getSpecimenCharacteristics());
				derivedSpecimenSet.add(derivedSp);
				i++;
			}
			specimen.setLineage(edu.wustl.catissuecore.util.global.Constants.DERIVED_SPECIMEN);
			specimen.setChildSpecimenCollection(derivedSpecimenSet);
		}
		return specimen;
	}

	/**
	 * @param exIdColl.
	 * @return HashSet of ExternalIdentifierCollection
	 */
	private HashSet getExternalIdentifierColl(Collection exIdColl)
	{
		final HashSet<ExternalIdentifier> exIdSet = new HashSet<ExternalIdentifier>();
		final Iterator itr = exIdColl.iterator();
		while (itr.hasNext())
		{
			final ExternalIdentifierBean exBean = (ExternalIdentifierBean) itr.next();
			if ((exBean.getName() == null || exBean.getName().equals(""))
					&& (exBean.getValue() == null || exBean.getValue().equals("")))
			{
				continue;
			}
			if (exBean.getId() == -1)
			{
				exBean.setId(null);
			}
			else
			{
				exBean.setId(exBean.getId());
			}
			final ExternalIdentifier ex = new ExternalIdentifier();
			ex.setId(exBean.getId());
			ex.setName(exBean.getName());
			ex.setValue(exBean.getValue());
			exIdSet.add(ex);
		}
		return exIdSet;
	}

	/**
	 * @param biohazardColl.
	 * @return HashSet of BiohazardCollection
	 */
	private HashSet getBiohazardColl(Collection biohazardColl)
	{
		final HashSet<Biohazard> biohazardSet = new HashSet<Biohazard>();
		final Iterator itr = biohazardColl.iterator();
		while (itr.hasNext())
		{
			final BiohazardBean biohazardBean = (BiohazardBean) itr.next();
			if (!biohazardBean.getName().equals(Constants.SELECT_OPTION)
					&& !biohazardBean.getType().equals(Constants.SELECT_OPTION))
			{
				final Long id = this.getBiohazardIdentifier(biohazardBean.getType(), biohazardBean
						.getName());
				final Biohazard biohazard = new Biohazard();
				biohazard.setId(id);
				biohazard.setType(biohazardBean.getType());
				biohazard.setName(biohazardBean.getName());
				biohazardSet.add(biohazard);
			}
		}
		return biohazardSet;
	}

	/**
	 * @param type.
	 * @param name
	 * @return Long :BiohazardIdentifier
	 */
	private Long getBiohazardIdentifier(String type, String name)
	{
		final String sourceObjName = Biohazard.class.getName();
		final String[] selectColName = {"id"};
		final String[] whereColName = {"type", "name"};
		final String[] whereColCond = {"=", "="};
		final Object[] whereColVal = {type, name};
		final BiohazardBizLogic bizLogic = new BiohazardBizLogic();
		try
		{
			final List list = bizLogic.retrieve(sourceObjName, selectColName, whereColName,
					whereColCond, whereColVal, Constants.AND_JOIN_CONDITION);
			final Iterator itr = list.iterator();
			while (itr.hasNext())
			{
				final Long id = (Long) itr.next();
				return id;
			}

		}
		catch (final BizLogicException e)
		{
			FlexInterface.logger.error("Error whioe getting biohazard Id:" + e.getMessage(),e);
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @param scgName.
	 * @return SpecimenCollectionGroup object
	 */
	private SpecimenCollectionGroup getSpecimenCollGrp(String scgName)
	{

		final String sourceObjName = SpecimenCollectionGroup.class.getName();

		final String[] whereColName = {"name"};
		final String[] whereColCond = {"="};
		final Object[] whereColVal = {scgName};
		final SpecimenCollectionGroupBizLogic bizLogic = new SpecimenCollectionGroupBizLogic();
		try
		{
			final List list = bizLogic.retrieve(sourceObjName, whereColName, whereColCond,
					whereColVal, Constants.AND_JOIN_CONDITION);
			final Iterator itr = list.iterator();
			while (itr.hasNext())
			{
				final SpecimenCollectionGroup scg = (SpecimenCollectionGroup) itr.next();
				return scg;
			}
			//SpecimenCollectionGroup scg1 =
		}
		catch (final BizLogicException e)
		{
			FlexInterface.logger.error("Error whioe getting scg :" + e.getMessage(),e);
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * @param spId.
	 * @return SpecimenCollectionGroup object.
	 */
	private SpecimenCollectionGroup getSpecimenCollGrpForSpecimen(String spId)
	{

		final String sourceObjName = Specimen.class.getName();
		final String[] selectColName = {"specimenCollectionGroup"};
		final String[] whereColName = {"id"};
		final String[] whereColCond = {"="};
		final Object[] whereColVal = {Long.parseLong(spId)};
		final SpecimenCollectionGroupBizLogic bizLogic = new SpecimenCollectionGroupBizLogic();
		try
		{
			final List list = bizLogic.retrieve(sourceObjName, selectColName, whereColName,
					whereColCond, whereColVal, Constants.AND_JOIN_CONDITION);
			final Iterator itr = list.iterator();
			while (itr.hasNext())
			{
				final SpecimenCollectionGroup scg = (SpecimenCollectionGroup) itr.next();
				return scg;
			}
			//SpecimenCollectionGroup scg1 =
		}
		catch (final BizLogicException e)
		{
			FlexInterface.logger.error("Error whioe getting scg :" + e.getMessage(),e);
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * @param specimen.
	 * @return  Specimen object
	 */
	private Specimen setStorageContForSpecimen(Specimen specimen)
	{

		final String sourceObjName = Specimen.class.getName();
		final String[] selectColName = {"specimenPosition.storageContainer"};
		final String[] whereColName = {"id"};
		final String[] whereColCond = {"="};
		final Object[] whereColVal = {specimen.getId()};
		final NewSpecimenBizLogic bizLogic = new NewSpecimenBizLogic();
		try
		{
			final List list = bizLogic.retrieve(sourceObjName, selectColName, whereColName,
					whereColCond, whereColVal, Constants.OR_JOIN_CONDITION);
			if (!list.isEmpty())
			{
				final StorageContainer storageCont = (StorageContainer) list.get(0);
				specimen.getSpecimenPosition().setStorageContainer(storageCont);
				return specimen;
			}
		}

		catch (final BizLogicException e)
		{
			FlexInterface.logger.error("Error whioe getting attributes for sp :" + e.getMessage(),e);
			e.printStackTrace();
		}

		return specimen;

	}

	/**
	 * @param parentName.
	 * @return Specimen object
	 */
	private Specimen getParentSpecimen(String parentName)
	{

		final String sourceObjName = Specimen.class.getName();
		final String[] selectColVal = {"id", "specimenCollectionGroup"};
		final String[] whereColName = {"label"};
		final String[] whereColCond = {"="};
		final Object[] whereColVal = {parentName};
		final NewSpecimenBizLogic bizLogic = new NewSpecimenBizLogic();
		try
		{
			final List list = bizLogic.retrieve(sourceObjName, selectColVal, whereColName,
					whereColCond, whereColVal, Constants.AND_JOIN_CONDITION);
			if (list != null && !list.isEmpty())
			{
				final Iterator itr = list.iterator();
				while (itr.hasNext())
				{
					final Object[] obj = (Object[]) list.get(0);
					final Long id = (Long) obj[0];
					final SpecimenCollectionGroup scg = (SpecimenCollectionGroup) obj[1];
					final Specimen specimen = new Specimen();
					specimen.setId(id);
					specimen.setSpecimenCollectionGroup(scg);
					return specimen;
				}
			}
		}
		catch (final BizLogicException e)
		{
			FlexInterface.logger.error("Error whioe getting specimen :" + e.getMessage(),e);
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * @param collectionEvent.
	 * @return CollectionEventParameters object
	 * @throws BizLogicException
	 */
	private CollectionEventParameters getCollectionEventParameters(
			EventParamtersBean collectionEvent) throws BizLogicException
	{
		final CollectionEventParameters event = new CollectionEventParameters();
		//setCommomParam(event);
		event.setTimestamp(this.getTimeStamp(collectionEvent.eventdDate, collectionEvent.eventHour,
				collectionEvent.eventMinute));
		event.setCollectionProcedure(collectionEvent.collectionProcedure);
		event.setContainer(collectionEvent.container);
		event.setComment(collectionEvent.comment);
		final User user = this.getUser(collectionEvent.userName);
		event.setUser(user);
		return event;
	}

	/**
	 * @param receivedEvent.
	 * @return ReceivedEventParameters object
	 * @throws BizLogicException
	 */
	private ReceivedEventParameters getReceivedEventParameters(EventParamtersBean receivedEvent)
			throws BizLogicException
	{
		final ReceivedEventParameters event = new ReceivedEventParameters();
		event.setTimestamp(this.getTimeStamp(receivedEvent.eventdDate, receivedEvent.eventHour,
				receivedEvent.eventMinute));
		final User user = this.getUser(receivedEvent.userName);
		event.setUser(user);
		event.setComment(receivedEvent.comment);
		//setCommomParam(event);
		event.setReceivedQuality(receivedEvent.receivedQuality);
		return event;
	}

	/**
	 * @param date.
	 * @param hour
	 * @param minute
	 * @return Date object
	 */
	private Date getTimeStamp(Date date, String hour, String minute)
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
		calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
		return calendar.getTime();

	}

	/**
	 * @param userName.
	 * @return User object
	 * @throws BizLogicException
	 */
	private User getUser(String userName) throws BizLogicException
	{
		User user = new User();
		final int index = userName.indexOf(",");
		final String lastName = userName.substring(0, index);
		final String firstName = userName.substring(index + 2);
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final UserBizLogic userBizLogic = (UserBizLogic) factory
				.getBizLogic(Constants.USER_FORM_ID);
		final String sourceObjName = User.class.getName();
		final String[] whereColName = {Constants.LASTNAME, Constants.FIRSTNAME};
		final String[] whereColCond = {"=", "="};
		final Object[] whereColVal = {lastName, firstName};

		final List list = userBizLogic.retrieve(sourceObjName, whereColName, whereColCond,
				whereColVal, Constants.AND_JOIN_CONDITION);
		if (list != null && !list.isEmpty())
		{
			user = (User) list.get(0);
		}
		return user;

	}



	//	Methods added by Baljeet
	/**
	 * This method retrieves the List if all Collection Protocols
	 * @return The cp List.
	 */
	public List getCpList()
	{

		final List<CpAndParticipentsBean> cpList = new ArrayList<CpAndParticipentsBean>();
		//Getting the CP List
		List cpColl;
		try
		{
			// added by Geeta for removing the cache
			if(isToInitializeSession)
			{
				this.session = flex.messaging.FlexContext.getHttpRequest().getSession();
			}
			final SessionDataBean sessionDataBean = (SessionDataBean) session
					.getAttribute(Constants.SESSION_DATA);
			final CpBasedViewBizLogic cpBizLogic = new CpBasedViewBizLogic();
			cpColl = cpBizLogic.getCollectionProtocolCollection(sessionDataBean);
			Collections.sort(cpColl);
			//Converting From NameValueBean to CpAndParticipentsBean
			final Iterator itr = cpColl.iterator();
			while (itr.hasNext())
			{
				final CpAndParticipentsBean cpBean = new CpAndParticipentsBean();
				final NameValueBean bean = (NameValueBean) itr.next();
				cpBean.setName(bean.getName());
				cpBean.setValue(bean.getValue());

				//Adding CpAndParticipentsBean to cpList
				cpList.add(cpBean);
			}
		}
		catch (final ApplicationException e)
		{
			FlexInterface.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return cpList;
	}

	/**
	 * This method retrieves the List of participants associated with a cp
	 * @param cpId :Collection protocol Id
	 * @return the list of Participants
	 */
	public List getParticipantsList(String cpId, String cpTitle) throws ApplicationException
	{
		//Setting the cp title in session
		if(isToInitializeSession)
		{
			this.session = flex.messaging.FlexContext.getHttpRequest().getSession();
		}
		this.session.setAttribute("cpTitle", cpTitle);
		List<CpAndParticipentsBean> participantsList = new ArrayList<CpAndParticipentsBean>();

		// Removed the cp based cache : Geeta
		final CpBasedViewBizLogic cpBizLogic = new CpBasedViewBizLogic();
		participantsList = cpBizLogic.getRegisteredParticipantInfoCollection(Long.parseLong(cpId));

		//Sorting the participants
		final ParticipantComparator partComp = new ParticipantComparator();
		Collections.sort(participantsList, partComp);
		return participantsList;
	}

	/**
	 * This mehtod returns the XML String for generating tree
	 * @param cpId : Selcted Collection Protocol ID
	 * @param pId : Selected Participant Id
	 * @return : The XML String for tree data
	 * @throws Exception
	 */
	public String getTreeData(String cpId, String pId)
	{
		//System.out.println("In get tree data method & cpID is:"+cpId);
		String str = null;
		try
		{
			final SpecimenCollectionGroupBizLogic bizlogic = new SpecimenCollectionGroupBizLogic();
			str = bizlogic.getSCGTreeForCPBasedView(Long.parseLong(cpId), Long.parseLong(pId));
			//return str;
		}
		catch (final Exception e)
		{
			FlexInterface.logger.error("Error while getting tree date :" + e.getMessage(), e);
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * @param cpId.
	 * @param pId
	 * @return boolean
	 * @throws Exception
	 */
	public Boolean chkArmShifting(String cpId, String pId) throws Exception
	{

		if (cpId != null && pId != null)
		{
			final SpecimenCollectionGroupBizLogic bizLogic = new SpecimenCollectionGroupBizLogic();
			final CollectionProtocolRegistration cpr = bizLogic.chkParticipantRegisteredToCP(Long
					.valueOf(pId), Long.valueOf(cpId));
			if (cpr == null)
			{
				Long parentCPId = null;
				// Get the parent Id of cpId;
				String hql = "select cp.parentCollectionProtocol.id from "
						+ CollectionProtocol.class.getName() + " as cp where cp.id = " + cpId;
				final List parentCpIdList = AppUtility.executeQuery(hql);
				if (parentCpIdList != null && !parentCpIdList.isEmpty())
				{
					parentCPId = (Long) parentCpIdList.get(0);
				}
				if (parentCPId != null)
				{
					hql = "select cpr.collectionProtocol.id from "
							+ CollectionProtocolRegistration.class.getName() + " as cpr where "
							+ "cpr.participant.id = " + pId + " and cpr.collectionProtocol.type= '"
							+ edu.wustl.catissuecore.util.global.Constants.ARM_CP_TYPE
							+ "' and cpr.collectionProtocol.parentCollectionProtocol.id = "
							+ parentCPId.toString() + " and cpr.collectionProtocol.id !=" + cpId; //Check if there are other arms registered for participant;
					final List cpList = AppUtility.executeQuery(hql);
					if (cpList != null && !cpList.isEmpty())
					{
						return Boolean.valueOf(true);
					}
				}
			}
		}
		return Boolean.valueOf(false);
	}
}
