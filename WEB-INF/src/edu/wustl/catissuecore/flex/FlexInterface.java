
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

	/**
	 * Constructor.
	 * @throws Exception : Exception
	 */
	public FlexInterface() throws Exception
	{
	}

	/**
	 * @param str.
	 * @return SpecimenBean object
	 */
	public SpecimenBean say(String str)
	{
		SpecimenBean sb = new SpecimenBean();
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
		session = flex.messaging.FlexContext.getHttpRequest().getSession();
		SessionDataBean sdb = (SessionDataBean) session.getAttribute(Constants.SESSION_DATA);
		SpecimenBean spBean = new SpecimenBean();

		EventParamtersBean collEvBean = new EventParamtersBean();
		EventParamtersBean recEvBean = new EventParamtersBean();
		spBean.collectionEvent = collEvBean;
		spBean.receivedEvent = recEvBean;

		spBean.collectionEvent.userName = sdb.getLastName() + ", " + sdb.getFirstName();
		spBean.receivedEvent.userName = sdb.getLastName() + ", " + sdb.getFirstName();
		spBean.comment = "";

		try
		{
			if (Constants.ADD.equals(mode)
					&& edu.wustl.catissuecore.util.global.Constants.NEW_SPECIMEN_TYPE
							.equals(parentType))
			{
				if (parentName != null)
				{
					SpecimenCollectionGroup scg = getSpecimenCollGrp(parentName);
					if (scg != null)
					{
						SpecimenCollectionGroupBizLogic bizLogic = new SpecimenCollectionGroupBizLogic();
						Collection eventColl = (Collection) bizLogic.retrieveAttribute(
								SpecimenCollectionGroup.class.getName(), scg.getId(),
								"elements(specimenEventParametersCollection)");
						if (eventColl != null && !eventColl.isEmpty())
						{
							Iterator itr = eventColl.iterator();
							while (itr.hasNext())
							{
								SpecimenEventParameters event = (SpecimenEventParameters) itr
										.next();
								String[] selectColName = {"user"};
								String[] whereColName = {"id"};
								String[] whereColCond = {"="};
								Object[] whereColVal = {event.getId()};
								List list = bizLogic.retrieve(SpecimenEventParameters.class
										.getName(), selectColName, whereColName, whereColCond,
										whereColVal, Constants.AND_JOIN_CONDITION);
								logger.info("List:" + list);
								if (list != null && !list.isEmpty())
								{
									User user = (User) list.get(0);
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
		catch (Exception e)
		{
			logger.debug(e.getMessage(), e);
		}

		return spBean;
	}

	/**
	 * @param nvBeanList.
	 * @return list
	 */
	private List<String> toStrList(List<NameValueBean> nvBeanList)
	{
		List<String> strList = new ArrayList<String>();
		for (NameValueBean bean : nvBeanList)
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
		List<NameValueBean> aList = CDEManager.getCDEManager().getPermissibleValueList(
				"Tissue Side", null);
		return toStrList(aList);
	}

	/**
	 * @return List.
	 * @throws BizLogicException :BizLogicException
	 */
	public List<String> getTissueSitePVList() throws BizLogicException
	{
		List<NameValueBean> aList = AppUtility.tissueSiteList();
		return toStrList(aList);
	}

	/**
	 * @return list of Pathological Status permissible values.
	 */
	public List<String> getPathologicalStatusPVList()
	{
		List<NameValueBean> aList = CDEManager.getCDEManager().getPermissibleValueList(
				"Pathological Status", null);
		return toStrList(aList);
	}

	/**
	 * @return List.
	 */
	public List<String> getSpecimenClassStatusPVList()
	{
		Map<String, List<NameValueBean>> specimenTypeMap = AppUtility.getSpecimenTypeMap();
		Set<String> specimenKeySet = specimenTypeMap.keySet();
		List<NameValueBean> specimenClassList = new ArrayList<NameValueBean>();

		Iterator itr1 = specimenKeySet.iterator();
		while (itr1.hasNext())
		{
			String specimenKey = (String) itr1.next();
			specimenClassList.add(new NameValueBean(specimenKey, specimenKey));
		}
		return toStrList(specimenClassList);
	}

	/**
	 * @return List.
	 */
	public List<String> getFluidSpecimenTypePVList()
	{
		Map specimenTypeMap = AppUtility.getSpecimenTypeMap();
		List<NameValueBean> aList = (List) specimenTypeMap.get("Fluid");
		return toStrList(aList);
	}

	/**
	 * @return List.
	 */
	public List<String> getTissueSpecimenTypePVList()
	{
		Map specimenTypeMap = AppUtility.getSpecimenTypeMap();
		List<NameValueBean> aList = (List) specimenTypeMap.get("Tissue");
		return toStrList(aList);
	}

	/**
	 * @return List.
	 */
	public List<String> getMolecularSpecimenTypePVList()
	{
		Map specimenTypeMap = AppUtility.getSpecimenTypeMap();
		List<NameValueBean> aList = (List<NameValueBean>) specimenTypeMap.get("Molecular");
		return toStrList(aList);
	}

	/**
	 * @return List.
	 */
	public List<String> getCellSpecimenTypePVList()
	{
		Map specimenTypeMap = AppUtility.getSpecimenTypeMap();
		List<NameValueBean> aList = (List<NameValueBean>) specimenTypeMap.get("Cell");
		return toStrList(aList);
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
		UserBizLogic userBizLogic = new UserBizLogic();
		List userList = userBizLogic.getUsers(Constants.ADD);
		return toStrList(userList);

	}

	/**
	 * @return List
	 */
	public List getProcedureList()
	{
		List procedureList = CDEManager.getCDEManager().getPermissibleValueList(
				edu.wustl.catissuecore.util.global.Constants.CDE_NAME_COLLECTION_PROCEDURE, null);
		return toStrList(procedureList);
	}

	/**
	 * @return List
	 */
	public List getContainerList()
	{
		List containerList = CDEManager.getCDEManager().getPermissibleValueList(
				edu.wustl.catissuecore.util.global.Constants.CDE_NAME_CONTAINER, null);
		return toStrList(containerList);
	}

	/**
	 * @return List
	 */
	public List getReceivedQualityList()
	{
		List qualityList = CDEManager.getCDEManager().getPermissibleValueList(
				edu.wustl.catissuecore.util.global.Constants.CDE_NAME_RECEIVED_QUALITY, null);
		return toStrList(qualityList);
	}

	/**
	 * @return List
	 */
	public List getBiohazardTypeList()
	{
		List biohazardList = CDEManager.getCDEManager().getPermissibleValueList(
				edu.wustl.catissuecore.util.global.Constants.CDE_NAME_BIOHAZARD, null);
		return toStrList(biohazardList);

	}

	/**
	 * @return List
	 */
	public List getBiohazardNameList()
	{
		List<List> biohazardNameList = new ArrayList<List>();
		try
		{
			IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic) factory
					.getBizLogic(edu.wustl.catissuecore.util.global.Constants.NEW_SPECIMEN_FORM_ID);
			List biohazardTypeList = getBiohazardTypeList();
			Iterator biohazardTypeItr = biohazardTypeList.iterator();
			while (biohazardTypeItr.hasNext())
			{
				List<String> nameList = new ArrayList<String>();
				nameList.add(Constants.SELECT_OPTION);
				//NameValueBean nvb = (NameValueBean) biohazardTypeItr.next();
				String type = (String) biohazardTypeItr.next();

				String[] selectColNames = {"name"};
				String[] whereColName = {"type"};
				String[] whereColCond = {"="};
				Object[] whereColVal = {type};
				List list = bizLogic.retrieve(Biohazard.class.getName(), selectColNames,
						whereColName, whereColCond, whereColVal, Constants.AND_JOIN_CONDITION);
				if (list != null && list.size() > 0)
				{
					Iterator iterator = list.iterator();
					while (iterator.hasNext())
					{
						String name = (String) iterator.next();
						nameList.add(name);
					}
				}
				biohazardNameList.add(nameList);
			}
		}
		catch (BizLogicException e)
		{
			logger.debug("Error mesg :" + e.getMessage());
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
		LinkedHashMap<String, GenericSpecimen> viewSpecimenMap = new LinkedHashMap<String, GenericSpecimen>();
		String message = "ERROR";

		SessionDataBean sdb = (SessionDataBean) session.getAttribute(Constants.SESSION_DATA);
		SpecimenAutoStorageContainer speicmenAutoStorageCont = new SpecimenAutoStorageContainer();
		int i = 1;
		try
		{
			for (SpecimenBean spBean : spBeanList)
			{
				SpecimenDataBean specimenDataBean = prepareGenericSpecimen(spBean,
						speicmenAutoStorageCont);
				specimenDataBean = getStorageContainers(specimenDataBean, specimenDataBean
						.getCollectionProtocolId(), speicmenAutoStorageCont);
				specimenDataBean.uniqueId = "" + i;
				viewSpecimenMap.put(specimenDataBean.getUniqueIdentifier(), specimenDataBean);
				i++;

			}
			speicmenAutoStorageCont.setCollectionProtocolSpecimenStoragePositions(sdb);
			session.setAttribute(
					edu.wustl.catissuecore.util.global.Constants.SPECIMEN_LIST_SESSION_MAP,
					viewSpecimenMap);

			message = "SUCCESS";
		}
		catch (Exception ex)
		{
			message = ex.getMessage();
			logger.debug(message, ex);
		}

		return message;
	}

	/**
	 * @param spBeanList : SpecimenBean object.
	 * @return String
	 */
	public String writeSpecimen1(List<SpecimenBean> spBeanList)
	{
		LinkedHashSet<Specimen> specimenHashSet = new LinkedHashSet<Specimen>();
		String message = "ERROR";
		for (SpecimenBean spBean : spBeanList)
		{
			Specimen specimen = prepareSpecimen(spBean);

			specimenHashSet.add(specimen);
		}
		try
		{
			session.setAttribute(
					edu.wustl.catissuecore.util.global.Constants.SPECIMEN_LIST_SESSION_MAP,
					specimenHashSet);

			message = "SUCCESS";
		}
		catch (Exception ex)
		{
			message = ex.getMessage();
			logger.debug(message, ex);
		}

		return message;
	}

	/**
	 * @param spBeanList : SpecimenBean object.
	 * @return String
	 */
	public String editSpecimen(List<SpecimenBean> spBeanList)
	{
		session = flex.messaging.FlexContext.getHttpRequest().getSession();
		LinkedHashMap specimenMap = (LinkedHashMap) session
				.getAttribute(edu.wustl.catissuecore.util.global.Constants.SPECIMEN_LIST_SESSION_MAP);
		Iterator itr = specimenMap.values().iterator();

		SpecimenAutoStorageContainer speicmenAutoStorageCont = new SpecimenAutoStorageContainer();

		while (itr.hasNext())
		{
			SpecimenDataBean spDataBean = (SpecimenDataBean) itr.next();
			if (spDataBean != null)
			{
				for (SpecimenBean spBean : spBeanList)
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
							spDataBean
									.setExternalIdentifierCollection(getExternalIdentifierColl(spBean.exIdColl));
						}

						if (spBean.biohazardColl != null && !spBean.biohazardColl.isEmpty())
						{
							spDataBean
									.setBiohazardCollection(getBiohazardColl(spBean.biohazardColl));
						}

						try
						{
							if (spBean.derivedColl != null && !spBean.derivedColl.isEmpty())
							{
								int i = 1;
								LinkedHashMap<String, GenericSpecimen> derivedMap = new LinkedHashMap<String, GenericSpecimen>();

								for (SpecimenBean derivedBean : spBean.derivedColl)
								{
									derivedBean.parentName = spBean.specimenLabel;
									derivedBean.parentType = edu.wustl.catissuecore.util.global.Constants.DERIVED_SPECIMEN_TYPE;
									SpecimenDataBean derivedDataBean = prepareGenericSpecimen(
											derivedBean, speicmenAutoStorageCont);
									derivedDataBean = getStorageContainers(derivedDataBean,
											derivedDataBean.getCollectionProtocolId(),
											speicmenAutoStorageCont);
									derivedDataBean.uniqueId = "d" + i;
									derivedMap.put("d" + i, derivedDataBean);
									i++;

								}
								spDataBean.setDeriveSpecimenCollection(derivedMap);
							}
						}
						catch (ApplicationException e)
						{
							logger.debug(e.getMessage(), e);
						}
					}

				}

			}

		}

		try
		{
			SessionDataBean sdb = (SessionDataBean) session.getAttribute(Constants.SESSION_DATA);
			speicmenAutoStorageCont.setCollectionProtocolSpecimenStoragePositions(sdb);
		}
		catch (ApplicationException e)
		{
			logger.debug(e.getMessage(), e);
		}

		/*LinkedHashSet<Specimen> specimenSet = new LinkedHashSet<Specimen>();
		 if (spBeanList != null && spBeanList.size() > 0)
		 {

		 for (SpecimenBean spBean : spBeanList)
		 {
		 Specimen specimen = prepareSpecimen(spBean);
		 specimenSet.add(specimen);
		 }
		 NewSpecimenBizLogic spBizLogic = (NewSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(
		 edu.wustl.catissuecore.util.global.Constants.NEW_SPECIMEN_FORM_ID);
		 HttpSession session = flex.messaging.FlexContext.getHttpRequest().getSession();
		 SessionDataBean sdb = (SessionDataBean) session.getAttribute(Constants.SESSION_DATA);*/

		/*try
		 {
		 //	spBizLogic.bulkUpdateSpecimens(specimenSet, sdb);
		 }
		 catch (DAOException e)
		 {
		 return e.getMessage();
		 }*/

		//}
		return "success:Specimens Updated Successfully";
	} /*private String writeSpecimen(SpecimenBean spBean)
	 {
	 System.out.println("SERVER writeSpecimen");
	 Specimen sp = prepareSpecimen(spBean);
	 String message = "ERROR";

	 if (sp == null)
	 {
	 return message;
	 }

	 try
	 {

	 HttpSession session = flex.messaging.FlexContext.getHttpRequest().getSession();
	 SessionDataBean sdb1 = (SessionDataBean) session.getAttribute(Constants.SESSION_DATA);
	 IBizLogic bizLogic = AbstractBizLogicFactory.getBizLogic(ApplicationProperties.getValue("app.bizLogicFactory"), "getBizLogic",
	 edu.wustl.catissuecore.util.global.Constants.NEW_SPECIMEN_FORM_ID);
	 NewSpecimenBizLogic spBizLogic = (NewSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(
	 edu.wustl.catissuecore.util.global.Constants.NEW_SPECIMEN_FORM_ID);
	 SessionDataBean sdb = new SessionDataBean();
	 sdb.setUserId(1L);
	 sdb.setUserName("admin@admin.com");

	 bizLogic.insert(sp, sdb, 0);
	 message = "SUCCESS";
	 }
	 catch (Exception ex)
	 {
	 message = ex.getMessage();
	 }

	 return message;
	 }*/

	/**
	 * @return SpecimenBean object.
	 */
	public SpecimenBean readSpecimen()
	{
		logger.info("SERVER readSpecimen");
		SpecimenBean sb = new SpecimenBean();
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
		List<SpecimenBean> list = new ArrayList<SpecimenBean>();
		session = flex.messaging.FlexContext.getHttpRequest().getSession();
		HashSet specimenIdList = (HashSet) session.getAttribute("specimenId");
		LinkedHashMap<String, GenericSpecimen> viewSpecimenMap = new LinkedHashMap<String, GenericSpecimen>();
		if (specimenIdList != null)
		{
			IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic) factory
					.getBizLogic(edu.wustl.catissuecore.util.global.Constants.NEW_SPECIMEN_FORM_ID);

			int i = 1;
			Iterator itr = specimenIdList.iterator();
			while (itr.hasNext())
			{
				String specimenId = (itr.next()).toString();
				Object object = bizLogic.retrieve(Specimen.class.getName(), Long
						.valueOf(specimenId));
				//Object object = bizLogic.retrieve(Specimen.class.getName(), new Long(specimenId));
				if (object != null)
				{
					Specimen specimen = (Specimen) object;
					Collection exIdColl = (Collection) bizLogic.retrieveAttribute(Specimen.class
							.getName(), specimen.getId(), "elements(externalIdentifierCollection)");
					specimen.setExternalIdentifierCollection(exIdColl);
					SpecimenCollectionGroup scg = getSpecimenCollGrpForSpecimen(specimenId);
					specimen.setSpecimenCollectionGroup(scg);
					specimen = setStorageContForSpecimen(specimen);
					SpecimenBean sb = prepareSpecimenBean(specimen, Constants.EDIT);
					list.add(sb);
					SpecimenDataBean sdb = prepareGenericSpecimen(specimen);
					sdb.setUniqueIdentifier("" + i);
					viewSpecimenMap.put("" + i, sdb);
					i++;
				}
			}
		}
		if (session
				.getAttribute(edu.wustl.catissuecore.util.global.Constants.SPECIMEN_LIST_SESSION_MAP) != null)
		{
			session
					.removeAttribute(edu.wustl.catissuecore.util.global.Constants.SPECIMEN_LIST_SESSION_MAP);
		}
		session.setAttribute(
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
		SpecimenBean sb = new SpecimenBean();
		sb.mode = mode;

		if (specimen.getId() != null)
			sb.spID = specimen.getId();
		if (specimen.getLabel() != null)
			sb.specimenLabel = specimen.getLabel();
		if (specimen.getBarcode() != null)
			sb.specimenBarcode = specimen.getBarcode();
		if (specimen.getLineage() != null)
			sb.lineage = specimen.getLineage();
		if (specimen.getClassName() != null)
			sb.specimenClass = specimen.getClassName();
		if (specimen.getSpecimenType() != null)
			sb.specimenType = specimen.getSpecimenType();
		if (specimen.getPathologicalStatus() != null)
			sb.pathologicalStatus = specimen.getPathologicalStatus();
		if (specimen.getInitialQuantity() != null)
			sb.quantity = specimen.getInitialQuantity();
		if (specimen.getCreatedOn() != null)
			sb.creationDate = specimen.getCreatedOn();

		SpecimenCharacteristics characteristic = specimen.getSpecimenCharacteristics();
		if (characteristic != null)
		{
			if (characteristic.getTissueSide() != null)
				sb.tissueSide = characteristic.getTissueSide();
			if (characteristic.getTissueSite() != null)
				sb.tissueSite = characteristic.getTissueSite();
		}
		if (specimen.getComment() != null)
			sb.comment = specimen.getComment();
		if (specimen.getBiohazardCollection() != null)
		{
			//sb.biohazardColl = new ArrayList(specimen.getBiohazardCollection());
			sb.biohazardColl = getBiohazardBeanCollection(specimen.getBiohazardCollection());
		}
		if (specimen.getExternalIdentifierCollection() != null)
		{
			//	sb.exIdColl = new ArrayList(specimen.getExternalIdentifierCollection());
			sb.exIdColl = getExternalIdentiferBeanCollection(specimen
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
		List<BiohazardBean> biozardBeanColl = new ArrayList<BiohazardBean>();
		if (biohazardColl != null)
		{
			Iterator biohazardItr = biohazardColl.iterator();
			while (biohazardItr.hasNext())
			{
				Biohazard biohazard = (Biohazard) biohazardItr.next();
				BiohazardBean biohazardBean = new BiohazardBean();
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
		List<ExternalIdentifierBean> externalIdentiferBeanColl = new ArrayList<ExternalIdentifierBean>();
		if (externalIdentiferColl != null)
		{
			Iterator externalIdItr = externalIdentiferColl.iterator();
			while (externalIdItr.hasNext())
			{
				ExternalIdentifier externalIdentifer = (ExternalIdentifier) externalIdItr.next();
				ExternalIdentifierBean externalIdBean = new ExternalIdentifierBean();
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

	/*private Specimen prepareSpecimen(SpecimenBean spBean)
	 {
	 Specimen sp = getSpecimenInstance(spBean.specimenClass);
	 if (sp == null)
	 {
	 return null;
	 }
	 sp.setType(spBean.specimenType);

	 sp.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
	 sp.setAvailable(true);
	 Quantity qt = new Quantity();
	 qt.setValue(spBean.quantity);
	 sp.setInitialQuantity(qt);
	 sp.setAvailableQuantity(qt);
	 sp.setBarcode(spBean.specimenBarcode);
	 sp.setBiohazardCollection(new HashSet<Biohazard>());
	 sp.setChildrenSpecimen(new HashSet<Specimen>());
	 sp.setComment("");
	 sp.setCreatedOn(new Date());
	 //sp.setExternalIdentifierCollection(new HashSet<ExternalIdentifier>());
	 if (spBean.exIdColl != null && !spBean.exIdColl.isEmpty())
	 {
	 sp.setExternalIdentifierCollection(getExternalIdentifierColl(spBean.exIdColl));
	 }

	 //sp.setExternalIdentifierCollection(spBean.exIdColl);

	 
	 if (spBean.biohazardColl != null && !spBean.biohazardColl.isEmpty())
	 {
	 sp.setBiohazardCollection(getBiohazardColl(spBean.biohazardColl));
	 }
	 sp.setLabel(spBean.specimenLabel);
	 //sp.setLineage("New");
	 sp.setParentSpecimen(null);
	 sp.setPathologicalStatus(spBean.pathologicalStatus);

	 SpecimenCharacteristics specimenCharacteristics = new SpecimenCharacteristics();
	 specimenCharacteristics.setTissueSide(spBean.tissueSide);
	 specimenCharacteristics.setTissueSite(spBean.tissueSite);
	 sp.setSpecimenCharacteristics(specimenCharacteristics);

	 SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
	 scg.setId(1L);
	 scg.setName("scg1");
	 sp.setSpecimenCollectionGroup(scg);

	 Set<SpecimenEventParameters> eventSet = new HashSet<SpecimenEventParameters>();
	 CollectionEventParameters collectionEvent = getCollectionEventParameters(spBean.collectionEvent);
	 collectionEvent.setSpecimen(sp);
	 eventSet.add(collectionEvent);

	 ReceivedEventParameters receEvent = getReceivedEventParameters(spBean.receivedEvent);
	 receEvent.setSpecimen(sp);
	 eventSet.add(receEvent);
	 sp.setSpecimenEventCollection(eventSet);

	 //Collection biohazardColl = getBiohazardColl(spBean.biohazardColl);
	 //StorageContainerBizLogic scBizLogic = (StorageContainerBizLogic)BizLogicFactory.getInstance().getBizLogic(edu.wustl.catissuecore.util.global.Constants.STORAGE_CONTAINER_FORM_ID);
	 //scBizLogic.setNe

	 sp.setStorageContainer(null);
	 sp.setPositionDimensionOne(null);
	 sp.setPositionDimensionTwo(null);
	 System.out.println("Returning complete specimen");
	 return sp;
	 }*/

	/**
	 * @param spBean.
	 * @param speicmenAutoStorageCont
	 * @return SpecimenDataBean object
	 * @throws ApplicationException
	 */
	private SpecimenDataBean prepareGenericSpecimen(SpecimenBean spBean,
			SpecimenAutoStorageContainer speicmenAutoStorageCont) throws ApplicationException
	{
		SpecimenDataBean specimenDataBean = new SpecimenDataBean();
		Specimen corresSpecimen = new Specimen();
		corresSpecimen.setCreatedOn(spBean.creationDate);
		specimenDataBean.setCorresSpecimen(corresSpecimen);
		specimenDataBean.setType(spBean.specimenType);
		specimenDataBean.setStorageContainerForSpecimen(spBean.storage);
		//specimenDataBean.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		//sp.setAvailable(true);
		specimenDataBean.setQuantity(String.valueOf(spBean.quantity));
		if (edu.wustl.catissuecore.util.global.Constants.MOLECULAR.equals(spBean.specimenClass))
		{
			if (spBean.concentration != null && spBean.concentration.toString() != "")
			{
				specimenDataBean.setConcentration(String.valueOf(spBean.concentration));
			}
		}
		//sp.setAvailableQuantity(qt);
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
		/*sp.setBiohazardCollection(new HashSet<Biohazard>());
		 sp.setChildrenSpecimen(new HashSet<Specimen>());*/
		specimenDataBean.setComment(spBean.comment);
		//sp.setCreatedOn(new Date());
		specimenDataBean.setExternalIdentifierCollection(new HashSet<ExternalIdentifier>());
		if (spBean.exIdColl != null && !spBean.exIdColl.isEmpty())
		{
			specimenDataBean
					.setExternalIdentifierCollection(getExternalIdentifierColl(spBean.exIdColl));
		}

		if (spBean.biohazardColl != null && !spBean.biohazardColl.isEmpty())
		{
			specimenDataBean.setBiohazardCollection(getBiohazardColl(spBean.biohazardColl));
		}
		if (Variables.isSpecimenLabelGeneratorAvl)
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
		/*SpecimenCharacteristics specimenCharacteristics = new SpecimenCharacteristics();
		 specimenCharacteristics.setTissueSide(spBean.tissueSide);
		 specimenCharacteristics.setTissueSite(spBean.tissueSite);
		 sp.setSpecimenCharacteristics(specimenCharacteristics);*/

		/*if (spBean.scgName != null)
		 {
		 specimenDataBean.setSpecimenCollectionGroup(getSpecimenCollGrp(spBean.scgName));
		 }*/
		CollectionProtocol cp = null;
		if (edu.wustl.catissuecore.util.global.Constants.NEW_SPECIMEN_TYPE
				.equals(spBean.parentType))
		{
			SpecimenCollectionGroup scg = getSpecimenCollGrp(spBean.parentName);
			specimenDataBean.setLineage(edu.wustl.catissuecore.util.global.Constants.NEW_SPECIMEN);
			if (scg != null)
			{
				specimenDataBean.setSpecimenCollectionGroup(scg);
				if (scg.getId() != null)
					cp = CollectionProtocolUtil.getCollectionProtocolForSCG(scg.getId().toString());
			}

		}
		else
		{
			Specimen parentSp = getParentSpecimen(spBean.parentName);
			specimenDataBean
					.setLineage(edu.wustl.catissuecore.util.global.Constants.DERIVED_SPECIMEN);
			if (parentSp != null)
			{
				specimenDataBean.setParentSpecimen(parentSp);
				specimenDataBean.setSpecimenCollectionGroup((SpecimenCollectionGroup) parentSp
						.getSpecimenCollectionGroup());
				if (parentSp.getId() != null)
					cp = CollectionProtocolUtil.getCollectionProtocolForSCG(parentSp
							.getSpecimenCollectionGroup().getId().toString());
			}
		}

		if (cp != null && cp.getId() != null)
			specimenDataBean.setCollectionProtocolId(cp.getId());

		/*scg.setId(1L);
		 scg.setName("scg1");
		 sp.setSpecimenCollectionGroup(scg);*/

		if (edu.wustl.catissuecore.util.global.Constants.NEW_SPECIMEN_TYPE
				.equals(spBean.parentType))
		{
			Set<SpecimenEventParameters> eventSet = new HashSet<SpecimenEventParameters>();
			CollectionEventParameters collectionEvent = getCollectionEventParameters(spBean.collectionEvent);
			//collectionEvent.setSpecimen(sp);
			eventSet.add(collectionEvent);

			ReceivedEventParameters receEvent = getReceivedEventParameters(spBean.receivedEvent);
			//receEvent.setSpecimen(sp);
			eventSet.add(receEvent);

			specimenDataBean.setSpecimenEventCollection(eventSet);
		}
		/*specimenDataBean.setStorageContainer(null);
		 sp.setPositionDimensionOne(null);
		 sp.setPositionDimensionTwo(null);*/

		if (spBean.derivedColl != null)
		{
			LinkedHashMap<String, GenericSpecimen> derivedMap = new LinkedHashMap<String, GenericSpecimen>();
			Iterator itr = spBean.derivedColl.iterator();
			int i = 1;
			while (itr.hasNext())
			{
				SpecimenBean derivedBean = (SpecimenBean) itr.next();

				derivedBean.collectionEvent = spBean.collectionEvent;
				derivedBean.receivedEvent = spBean.receivedEvent;
				derivedBean.parentType = edu.wustl.catissuecore.util.global.Constants.DERIVED_SPECIMEN_TYPE;
				SpecimenDataBean derivedDataBean = prepareGenericSpecimen(derivedBean,
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
		//specimenDataBean = getStorageContainers(specimenDataBean, specimenDataBean.getCollectionProtocolId(), speicmenAutoStorageCont);
		return specimenDataBean;
	}

	/**
	 * @param sp
	 * @return SpecimenDataBean object.
	 */
	private SpecimenDataBean prepareGenericSpecimen(Specimen sp)
	{
		SpecimenDataBean specimenDataBean = new SpecimenDataBean();
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
		Specimen parentSpecimen = (Specimen) sp.getParentSpecimen();
		specimenDataBean.setParentSpecimen(parentSpecimen);
		specimenDataBean.setPathologicalStatus(sp.getPathologicalStatus());

		specimenDataBean.setTissueSide(sp.getSpecimenCharacteristics().getTissueSide());
		specimenDataBean.setTissueSite(sp.getSpecimenCharacteristics().getTissueSite());
		specimenDataBean.setLineage(sp.getLineage());
		specimenDataBean.setSpecimenCollectionGroup((SpecimenCollectionGroup) sp
				.getSpecimenCollectionGroup());
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
		//specimenDataBean = getStorageContainers(specimenDataBean, specimenDataBean.getCollectionProtocolId(), speicmenAutoStorageCont);
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
			speicmenAutoStorageCont.addSpecimen(specimenDataBean, specimenDataBean.getClassName(),
					collectionProtocolId);
		if (specimenDataBean.getDeriveSpecimenCollection() != null
				&& !specimenDataBean.getDeriveSpecimenCollection().isEmpty())
		{
			Collection<GenericSpecimen> deriveSpColl = specimenDataBean
					.getDeriveSpecimenCollection().values();
			for (GenericSpecimen sp : deriveSpColl)
			{
				if (sp.getStorageContainerForSpecimen().equals("Auto"))
					speicmenAutoStorageCont
							.addSpecimen(sp, sp.getClassName(), collectionProtocolId);
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
		Specimen specimen = getSpecimenInstance(spBean.specimenClass);
		specimen.setSpecimenType(spBean.specimenType);
		specimen.setId(spBean.spID);
		specimen.setCreatedOn(spBean.creationDate);
		//specimenDataBean.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		//sp.setAvailable(true);
		Double qt = new Double(spBean.quantity);
		specimen.setInitialQuantity(qt);
		specimen.setAvailableQuantity(qt);
		if (edu.wustl.catissuecore.util.global.Constants.MOLECULAR.equals(spBean.specimenClass))
		{
			((MolecularSpecimen) specimen).setConcentrationInMicrogramPerMicroliter(Double
					.valueOf(spBean.getConcentration()));

		}

		//specimen.setClassName(spBean.specimenClass);
		specimen.setBarcode(spBean.specimenBarcode);

		specimen.setComment(spBean.comment);

		//sp.setCreatedOn(new Date());
		specimen.setExternalIdentifierCollection(new HashSet<ExternalIdentifier>());
		if (spBean.exIdColl != null && !spBean.exIdColl.isEmpty())
		{
			specimen.setExternalIdentifierCollection(getExternalIdentifierColl(spBean.exIdColl));
		}

		if (spBean.biohazardColl != null && !spBean.biohazardColl.isEmpty())
		{
			specimen.setBiohazardCollection(getBiohazardColl(spBean.biohazardColl));
		}
		specimen.setLabel(spBean.specimenLabel);

		//specimenDataBean.setParentSpecimen(null);
		specimen.setPathologicalStatus(spBean.pathologicalStatus);

		SpecimenCharacteristics specimenCharacteristics = new SpecimenCharacteristics();
		specimenCharacteristics.setTissueSide(spBean.tissueSide);
		specimenCharacteristics.setTissueSite(spBean.tissueSite);
		specimen.setSpecimenCharacteristics(specimenCharacteristics);

		if (spBean.derivedColl != null)
		{
			LinkedHashSet<AbstractSpecimen> derivedSpecimenSet = new LinkedHashSet<AbstractSpecimen>();
			Iterator itr = spBean.derivedColl.iterator();
			int i = 1;
			while (itr.hasNext())
			{
				SpecimenBean derivedBean = (SpecimenBean) itr.next();
				derivedBean.collectionEvent = spBean.collectionEvent;
				derivedBean.receivedEvent = spBean.receivedEvent;
				Specimen derivedSp = prepareSpecimen(derivedBean);
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
		HashSet<ExternalIdentifier> exIdSet = new HashSet<ExternalIdentifier>();
		Iterator itr = exIdColl.iterator();
		while (itr.hasNext())
		{
			ExternalIdentifierBean exBean = (ExternalIdentifierBean) itr.next();
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
			ExternalIdentifier ex = new ExternalIdentifier();
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
		HashSet<Biohazard> biohazardSet = new HashSet<Biohazard>();
		Iterator itr = biohazardColl.iterator();
		while (itr.hasNext())
		{
			BiohazardBean biohazardBean = (BiohazardBean) itr.next();
			if (!biohazardBean.getName().equals(Constants.SELECT_OPTION)
					&& !biohazardBean.getType().equals(Constants.SELECT_OPTION))
			{
				Long id = getBiohazardIdentifier(biohazardBean.getType(), biohazardBean.getName());
				Biohazard biohazard = new Biohazard();
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
		String sourceObjName = Biohazard.class.getName();
		String[] selectColName = {"id"};
		String[] whereColName = {"type", "name"};
		String[] whereColCond = {"=", "="};
		Object[] whereColVal = {type, name};
		BiohazardBizLogic bizLogic = new BiohazardBizLogic();
		try
		{
			List list = bizLogic.retrieve(sourceObjName, selectColName, whereColName, whereColCond,
					whereColVal, Constants.AND_JOIN_CONDITION);
			Iterator itr = list.iterator();
			while (itr.hasNext())
			{
				Long id = (Long) itr.next();
				return id;
			}

		}
		catch (BizLogicException e)
		{
			logger.debug("Error whioe getting biohazard Id:" + e.getMessage());
		}

		return null;
	}

	/**
	 * @param scgName.
	 * @return SpecimenCollectionGroup object
	 */
	private SpecimenCollectionGroup getSpecimenCollGrp(String scgName)
	{

		String sourceObjName = SpecimenCollectionGroup.class.getName();

		String[] whereColName = {"name"};
		String[] whereColCond = {"="};
		Object[] whereColVal = {scgName};
		SpecimenCollectionGroupBizLogic bizLogic = new SpecimenCollectionGroupBizLogic();
		try
		{
			List list = bizLogic.retrieve(sourceObjName, whereColName, whereColCond, whereColVal,
					Constants.AND_JOIN_CONDITION);
			Iterator itr = list.iterator();
			while (itr.hasNext())
			{
				SpecimenCollectionGroup scg = (SpecimenCollectionGroup) itr.next();
				return scg;
			}
			//SpecimenCollectionGroup scg1 = 
		}
		catch (BizLogicException e)
		{
			logger.debug("Error whioe getting scg :" + e.getMessage());
		}

		return null;

	}

	/**
	 * @param spId.
	 * @return SpecimenCollectionGroup object.
	 */
	private SpecimenCollectionGroup getSpecimenCollGrpForSpecimen(String spId)
	{

		String sourceObjName = Specimen.class.getName();
		String[] selectColName = {"specimenCollectionGroup"};
		String[] whereColName = {"id"};
		String[] whereColCond = {"="};
		Object[] whereColVal = {Long.parseLong(spId)};
		SpecimenCollectionGroupBizLogic bizLogic = new SpecimenCollectionGroupBizLogic();
		try
		{
			List list = bizLogic.retrieve(sourceObjName, selectColName, whereColName, whereColCond,
					whereColVal, Constants.AND_JOIN_CONDITION);
			Iterator itr = list.iterator();
			while (itr.hasNext())
			{
				SpecimenCollectionGroup scg = (SpecimenCollectionGroup) itr.next();
				return scg;
			}
			//SpecimenCollectionGroup scg1 = 
		}
		catch (BizLogicException e)
		{
			logger.debug("Error whioe getting scg :" + e.getMessage());
		}

		return null;

	}

	/**
	 * @param specimen.
	 * @return  Specimen object
	 */
	private Specimen setStorageContForSpecimen(Specimen specimen)
	{

		String sourceObjName = Specimen.class.getName();
		String[] selectColName = {"specimenPosition.storageContainer"};
		String[] whereColName = {"id"};
		String[] whereColCond = {"="};
		Object[] whereColVal = {specimen.getId()};
		NewSpecimenBizLogic bizLogic = new NewSpecimenBizLogic();
		try
		{
			List list = bizLogic.retrieve(sourceObjName, selectColName, whereColName, whereColCond,
					whereColVal, Constants.OR_JOIN_CONDITION);
			if (!list.isEmpty())
			{
				StorageContainer storageCont = (StorageContainer) list.get(0);
				specimen.getSpecimenPosition().setStorageContainer(storageCont);
				return specimen;
			}
		}

		catch (BizLogicException e)
		{
			logger.debug("Error whioe getting attributes for sp :" + e.getMessage());
		}

		return specimen;

	}

	/**
	 * @param parentName.
	 * @return Specimen object
	 */
	private Specimen getParentSpecimen(String parentName)
	{

		String sourceObjName = Specimen.class.getName();
		String[] selectColVal = {"id", "specimenCollectionGroup"};
		String[] whereColName = {"label"};
		String[] whereColCond = {"="};
		Object[] whereColVal = {parentName};
		NewSpecimenBizLogic bizLogic = new NewSpecimenBizLogic();
		try
		{
			List list = bizLogic.retrieve(sourceObjName, selectColVal, whereColName, whereColCond,
					whereColVal, Constants.AND_JOIN_CONDITION);
			if (list != null && !list.isEmpty())
			{
				Iterator itr = list.iterator();
				while (itr.hasNext())
				{
					Object[] obj = (Object[]) list.get(0);
					Long id = (Long) obj[0];
					SpecimenCollectionGroup scg = (SpecimenCollectionGroup) obj[1];
					Specimen specimen = new Specimen();
					specimen.setId(id);
					specimen.setSpecimenCollectionGroup(scg);
					return specimen;
				}
			}
		}
		catch (BizLogicException e)
		{
			logger.debug("Error whioe getting specimen :" + e.getMessage());
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
		CollectionEventParameters event = new CollectionEventParameters();
		//setCommomParam(event);
		event.setTimestamp(getTimeStamp(collectionEvent.eventdDate, collectionEvent.eventHour,
				collectionEvent.eventMinute));
		event.setCollectionProcedure(collectionEvent.collectionProcedure);
		event.setContainer(collectionEvent.container);
		event.setComment(collectionEvent.comment);
		User user = getUser(collectionEvent.userName);
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
		ReceivedEventParameters event = new ReceivedEventParameters();
		event.setTimestamp(getTimeStamp(receivedEvent.eventdDate, receivedEvent.eventHour,
				receivedEvent.eventMinute));
		User user = getUser(receivedEvent.userName);
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
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
		calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
		return calendar.getTime();

	}

	/*private void setCommomParam(EventParameters event)
	 {
	 event.setComment("");
	 event.setTimestamp(new Date());
	 User user = new User();
	 user.setId(1L);
	 event.setUser(user);
	 }*/

	/**
	 * @param userName.
	 * @return User object
	 * @throws BizLogicException
	 */
	private User getUser(String userName) throws BizLogicException
	{
		User user = new User();
		int index = userName.indexOf(",");
		String lastName = userName.substring(0, index);
		String firstName = userName.substring(index + 2);
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		UserBizLogic userBizLogic = (UserBizLogic) factory.getBizLogic(Constants.USER_FORM_ID);
		String sourceObjName = User.class.getName();
		String[] whereColName = {Constants.LASTNAME, Constants.FIRSTNAME};
		String[] whereColCond = {"=", "="};
		Object[] whereColVal = {lastName, firstName};

		List list = userBizLogic.retrieve(sourceObjName, whereColName, whereColCond, whereColVal,
				Constants.AND_JOIN_CONDITION);
		if (list != null && !list.isEmpty())
			user = (User) list.get(0);
		return user;

	}

	//--------------DAG-----------------------------
	public void restoreQueryObject()
	{
		if (dagPanel == null)
		{
			this.initFlexInterface();
		}
		else
		{
			dagPanel.restoreQueryObject();
		}
	}

	/**
	 * Add Nodes in define Result view
	 * @param nodesStr
	 * @return
	 */
	public DAGNode addNodeToView(String nodesStr)
	{
		DAGNode dagNode = dagPanel.addNodeToOutPutView(nodesStr);
		return dagNode;
	}

	/**
	 * Repaints DAG
	 * @return
	 */
	public Map<String, Object> repaintDAG()
	{
		return dagPanel.repaintDAG();
	}

	public int getSearchResult()
	{
		return dagPanel.search();
	}

	/**
	 * create DAG Node
	 * @param strToCreateQueryObject
	 * @param entityName
	 */
	public DAGNode createNode(String strToCreateQueryObject, String entityName)
	{
		DAGNode dagNode = dagPanel.createQueryObject(strToCreateQueryObject, entityName, "Add");
		return dagNode;
	}

	/**
	 * @param expressionId
	 * @return
	 */
	public String getLimitUI(int expressionId)
	{
		Map map = dagPanel.editAddLimitUI(expressionId);
		String htmlStr = (String) map.get(DAGConstant.HTML_STR);
		IExpression expression = (IExpression) map.get(DAGConstant.EXPRESSION);
		dagPanel.setExpression(expression);
		return htmlStr;
	}

	/**
	 * Edit Node
	 * @param strToCreateQueryObject
	 * @param entityName
	 * @return
	 */
	public DAGNode editNode(String strToCreateQueryObject, String entityName)
	{
		DAGNode dagNode = dagPanel.createQueryObject(strToCreateQueryObject, entityName, "Edit");
		return dagNode;
	}

	/**
	 * Checks validity of nodes for query
	 * @param linkedNodeList
	 * @return True if both nodes have any of the attribute as Date, else False 
	 */
	public boolean checkIfNodesAreValid(List<DAGNode> linkedNodeList)
	{
		boolean areNodesValid = false;

		areNodesValid = dagPanel.checkForValidAttributes(linkedNodeList);

		return areNodesValid;
	}

	/**
	 * @param linkedNodeList.
	 * @return boolean
	 */
	public boolean checkIfSingleNodeValid(List<DAGNode> linkedNodeList)
	{
		boolean isNodeValid = false;
		isNodeValid = dagPanel.checkForNodeValidAttributes(linkedNodeList.get(0));
		return isNodeValid;
	}

	/**
	 * @param linkedNodeList.
	 * @return Map 
	 */
	public Map getSingleNodeQueryDate(List<DAGNode> linkedNodeList)
	{
		DAGNode sourceNode = linkedNodeList.get(0);
		Map singleNodeDataMap = dagPanel.getSingleNodeQueryData(sourceNode.getExpressionId(),
				sourceNode.getNodeName());

		return singleNodeDataMap;
	}

	/**
	 * @param customNode
	 * @return
	 */
	public Map getSingleNodeEditData(SingleNodeCustomFormulaNode customNode)
	{
		Map singleNodeDataMap = dagPanel.getSingleNodeQueryData(customNode.getNodeExpressionId(),
				customNode.getEntityName());
		return singleNodeDataMap;
	}

	/*public Map retrieveQueryData(List<DAGNode> linkedNodeList)
	{
		DAGNode sourceNode = linkedNodeList.get(0);
		DAGNode destinationNode = linkedNodeList.get(1);
		Map queryDataMap = dagPanel.getQueryData(sourceNode.getExpressionId(), destinationNode
				.getExpressionId(), sourceNode.getNodeName(), destinationNode.getNodeName());
		return queryDataMap;
	}

	public Map retrieveEditQueryData(CustomFormulaNode customNode)
	{
		Map queryDataMap = dagPanel.getQueryData(customNode.getFirstNodeExpId(), customNode
				.getSecondNodeExpId(), customNode.getFirstNodeName(), customNode
				.getSecondNodeName());
		return queryDataMap;
	}*/

	/**
	 * @param nodeID
	 */
	public void removeCustomFormula(String nodeID)
	{
		//System.out.println("In remove custom formula");
		dagPanel.removeCustomFormula(nodeID);
	}

	/**
	 * Deletes node from output view
	 * @param expId
	 */
	public void deleteFromView(int expId)
	{
		dagPanel.deleteExpressionFormView(expId);
	}

	/**
	 * Adds node to output view
	 * @param expId
	 */
	public void addToView(int expId)
	{
		dagPanel.addExpressionToView(expId);
	}

	/**
	 * Deletes node from DAG
	 * @param expId
	 */
	public void deleteNode(int expId)
	{
		dagPanel.deleteExpression(expId);//delete Expression 
	}

	/**
	 * Gets path List between nodes
	 * @param linkedNodeList
	 * @return
	 */
	private List<IPath> getPathList(List<DAGNode> linkedNodeList)
	{
		DAGNode sourceNode = linkedNodeList.get(0);
		DAGNode destinationNode = linkedNodeList.get(1);
		List<IPath> pathsList = dagPanel.getPaths(sourceNode, destinationNode);
		return pathsList;
	}

	/**
	 * Gets association(path) between 2 nodes
	 * @param linkedNodeList
	 * @return
	 */
	public List getpaths(List<DAGNode> linkedNodeList)
	{
		List<IPath> pathsList = getPathList(linkedNodeList);
		List<DAGPath> pathsListStr = new ArrayList<DAGPath>();
		for (int i = 0; i < pathsList.size(); i++)
		{
			Path p = (Path) pathsList.get(i);
			DAGPath path = new DAGPath();
			path.setToolTip(DAGPanel.getPathDisplayString(pathsList.get(i)));
			path.setId(Long.valueOf(p.getPathId()).toString());
			pathsListStr.add(path);
		}
		return pathsListStr;
	}

	/**
	 * Gets association(path) between 2 nodes
	 * @param linkedNodeList
	 * @return
	 */
	public CustomFormulaNode formTemporalQuery(CustomFormulaNode customFormulaNode, String operation)
	{
		return dagPanel.formTemporalQuery(customFormulaNode, operation);
	}

	public SingleNodeCustomFormulaNode formSingleNodeFormula(SingleNodeCustomFormulaNode node,
			String operation)
	{
		return dagPanel.formSingleNodeFormula(node, operation);
	}

	/**
	 * Links 2 nodes
	 * @param linkedNodeList
	 * @param selectedPaths
	 */

	public List<DAGPath> linkNodes(List<DAGNode> linkedNodeList, List<DAGPath> selectedPaths)
	{
		DAGNode sourceNode = linkedNodeList.get(0);
		DAGNode destinationNode = linkedNodeList.get(1);
		List<IPath> pathsList = getPathList(linkedNodeList);
		List<IPath> selectedList = new ArrayList<IPath>();
		for (int j = 0; j < selectedPaths.size(); j++)
		{
			for (int i = 0; i < pathsList.size(); i++)
			{
				IPath path = pathsList.get(i);
				String pathStr = Long.valueOf(path.getPathId()).toString();
				DAGPath dagPath = selectedPaths.get(j);
				String pathId = dagPath.getId();
				if (pathStr.equals(pathId))
				{
					selectedList.add(path);
					break;
				}

			}
		}
		return dagPanel.linkNode(sourceNode, destinationNode, selectedList);
	}

	/**
	 * Deletes associaton between 2 nodes
	 * @param linkedNodeList
	 * @param linkName
	 */
	public void deleteLink(List<DAGNode> linkedNodeList, String linkName)
	{
		dagPanel.deletePath(linkName, linkedNodeList);
	}

	/**
	 * Sets logical operator set from UI
	 * @param node
	 * @param operandIndex
	 * @param operator
	 */
	public void setLogicalOperator(DAGNode node, int operandIndex, String operator)
	{
		int parentExpId = node.getExpressionId();
		dagPanel.updateLogicalOperator(parentExpId, operandIndex, operator);
	}

	/**
	 *Initalises DAG 
	 *
	 */
	public void initFlexInterface()
	{
		queryObject = new ClientQueryBuilder();
		IPathFinder pathFinder = new CommonPathFinder();
		dagPanel = new DAGPanel(pathFinder);
		dagPanel.setQueryObject(queryObject);
	}

	private IClientQueryBuilderInterface queryObject = null;
	private DAGPanel dagPanel = null;
	private HttpSession session = null;

	//----- END DAG -------	
	//	Methods added by Baljeet
	/**
	 * This method retrieves the List if all Collection Protocols
	 * @return The cp List.
	 */
	public List getCpList()
	{

		List<CpAndParticipentsBean> cpList = new ArrayList<CpAndParticipentsBean>();
		//Getting the CP List 
		List cpColl;
		try
		{
			// added by Geeta for removing the cache
			HttpSession session = null;
			session = flex.messaging.FlexContext.getHttpRequest().getSession();
			SessionDataBean sessionDataBean = (SessionDataBean) session
					.getAttribute(Constants.SESSION_DATA);
			CpBasedViewBizLogic cpBizLogic = new CpBasedViewBizLogic();
			cpColl = cpBizLogic.getCollectionProtocolCollection(sessionDataBean);
			Collections.sort(cpColl);
			//Converting From NameValueBean to CpAndParticipentsBean
			Iterator itr = cpColl.iterator();
			while (itr.hasNext())
			{
				CpAndParticipentsBean cpBean = new CpAndParticipentsBean();
				NameValueBean bean = (NameValueBean) itr.next();
				cpBean.setName(bean.getName());
				cpBean.setValue(bean.getValue());

				//Adding CpAndParticipentsBean to cpList
				cpList.add(cpBean);
			}
		}
		catch (ApplicationException e)
		{
			logger.debug(e.getMessage(), e);
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
		session = flex.messaging.FlexContext.getHttpRequest().getSession();
		session.setAttribute("cpTitle", cpTitle);
		List<CpAndParticipentsBean> participantsList = new ArrayList<CpAndParticipentsBean>();
		
			// Removed the cp based cache : Geeta
			CpBasedViewBizLogic cpBizLogic = new CpBasedViewBizLogic();
			participantsList = cpBizLogic.getRegisteredParticipantInfoCollection(Long
					.parseLong(cpId));
		
		
		//Sorting the participants
		ParticipantComparator partComp = new ParticipantComparator();
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
			SpecimenCollectionGroupBizLogic bizlogic = new SpecimenCollectionGroupBizLogic();
			str = bizlogic.getSCGTreeForCPBasedView(Long.parseLong(cpId), Long.parseLong(pId));
			//return str;
		}
		catch (Exception e)
		{
			logger.debug("Error while getting tree date :" + e.getMessage(), e);
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
			SpecimenCollectionGroupBizLogic bizLogic = new SpecimenCollectionGroupBizLogic();
			CollectionProtocolRegistration cpr = bizLogic.chkParticipantRegisteredToCP(Long
					.valueOf(pId), Long.valueOf(cpId));
			if (cpr == null)
			{
				Long parentCPId = null;
				// Get the parent Id of cpId;
				String hql = "select cp.parentCollectionProtocol.id from "
						+ CollectionProtocol.class.getName() + " as cp where cp.id = " + cpId;
				List parentCpIdList = AppUtility.executeQuery(hql);
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
					List cpList = AppUtility.executeQuery(hql);
					if (cpList != null && !cpList.isEmpty())
						return Boolean.valueOf(true);
				}
			}
		}
		return Boolean.valueOf(false);
	}
}
