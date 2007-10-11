
package edu.wustl.catissuecore.flex;

import java.util.ArrayList;
import java.util.Calendar;
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

import javax.servlet.http.HttpSession;

import edu.emory.mathcs.backport.java.util.Collections;
import edu.wustl.cab2b.client.ui.query.ClientQueryBuilder;
import edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface;
import edu.wustl.cab2b.client.ui.query.IPathFinder;
import edu.wustl.catissuecore.bean.CpAndParticipentsBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bean.SpecimenDataBean;
import edu.wustl.catissuecore.bizlogic.BiohazardBizLogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.flex.dag.DAGConstant;
import edu.wustl.catissuecore.flex.dag.DAGNode;
import edu.wustl.catissuecore.flex.dag.DAGPanel;
import edu.wustl.catissuecore.flex.dag.DAGPath;
import edu.wustl.catissuecore.util.ParticipantRegistrationCacheManager;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.metadata.path.Path;
import edu.wustl.common.querysuite.queryengine.impl.CommonPathFinder;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;


public class FlexInterface
{
	public FlexInterface() throws Exception
	{
		//		Variables.applicationHome = System.getProperty("user.dir");
		//		Logger.out = org.apache.log4j.Logger.getLogger("");
		//		PropertyConfigurator.configure(Variables.applicationHome+"\\WEB-INF\\src\\"+"ApplicationResources.properties");
		//		
		//		Logger.out.debug("here");
		//		CDEManager.init();
	}

	public SpecimenBean say(String str)
	{
		System.out.println("str from flex client " + str);
		SpecimenBean sb = new SpecimenBean();
		sb.specimenLabel = "AA";
		return sb;
	}

	public SpecimenBean initFlexInterfaceForMultipleSp()
	{
		session = flex.messaging.FlexContext.getHttpRequest().getSession();
		SessionDataBean sdb = (SessionDataBean) session.getAttribute(Constants.SESSION_DATA);
		SpecimenBean spBean = new SpecimenBean();
		spBean.collectionEvent.userName = sdb.getLastName() + ", " + sdb.getFirstName();
		spBean.receivedEvent.userName = sdb.getLastName() + ", " + sdb.getFirstName();
		spBean.comment = "My comment vis ";
		return spBean;
	}

	private List<String> toStrList(List<NameValueBean> nvBeanList)
	{
		List<String> strList = new ArrayList<String>();
		for (NameValueBean bean : nvBeanList)
		{
			strList.add(bean.getName());
		}
		return strList;
	}

	public List<String> getTissueSidePVList()
	{
		List<NameValueBean> aList = CDEManager.getCDEManager().getPermissibleValueList("Tissue Side", null);
		return toStrList(aList);
	}

	public List<String> getTissueSitePVList()
	{
		List<NameValueBean> aList = CDEManager.getCDEManager().getPermissibleValueList("Tissue Site", null);
		return toStrList(aList);
	}

	public List<String> getPathologicalStatusPVList()
	{
		List<NameValueBean> aList = CDEManager.getCDEManager().getPermissibleValueList("Pathological Status", null);
		return toStrList(aList);
	}

	public List<String> getSpecimenClassStatusPVList()
	{
		Map specimenTypeMap = getSpecimenTypeMap();
		Set specimenKeySet = specimenTypeMap.keySet();
		List<NameValueBean> specimenClassList = new ArrayList<NameValueBean>();

		Iterator itr1 = specimenKeySet.iterator();
		while (itr1.hasNext())
		{
			String specimenKey = (String) itr1.next();
			specimenClassList.add(new NameValueBean(specimenKey, specimenKey));
		}
		return toStrList(specimenClassList);
	}

	public List<String> getFluidSpecimenTypePVList()
	{
		Map specimenTypeMap = getSpecimenTypeMap();
		List<NameValueBean> aList = (List) specimenTypeMap.get("Fluid");
		return toStrList(aList);
	}

	public List<String> getTissueSpecimenTypePVList()
	{
		Map specimenTypeMap = getSpecimenTypeMap();
		List<NameValueBean> aList = (List) specimenTypeMap.get("Tissue");
		return toStrList(aList);
	}

	public List<String> getMolecularSpecimenTypePVList()
	{
		Map specimenTypeMap = getSpecimenTypeMap();
		List<NameValueBean> aList = (List<NameValueBean>) specimenTypeMap.get("Molecular");
		return toStrList(aList);
	}

	public List<String> getCellSpecimenTypePVList()
	{
		Map specimenTypeMap = getSpecimenTypeMap();
		List<NameValueBean> aList = (List<NameValueBean>) specimenTypeMap.get("Cell");
		return toStrList(aList);
	}

	private Map getSpecimenTypeMap()
	{
		CDE specimenClassCDE = CDEManager.getCDEManager().getCDE("Specimen");
		Set setPV = specimenClassCDE.getPermissibleValues();
		Iterator itr = setPV.iterator();

		//List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList("Specimen", null);
		Map<String, List> subTypeMap = new HashMap<String, List>();
		//specimenClassList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));

		while (itr.hasNext())
		{
			List<NameValueBean> innerList = new ArrayList<NameValueBean>();
			Object obj = itr.next();
			PermissibleValue pv = (PermissibleValue) obj;
			//String tmpStr = pv.getValue();
			//specimenClassList.add(new NameValueBean(tmpStr, tmpStr));

			Set list1 = pv.getSubPermissibleValues();
			Iterator itr1 = list1.iterator();
			innerList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));

			while (itr1.hasNext())
			{
				Object obj1 = itr1.next();
				PermissibleValue pv1 = (PermissibleValue) obj1;
				//Setting Specimen Type
				String tmpInnerStr = pv1.getValue();
				innerList.add(new NameValueBean(tmpInnerStr, tmpInnerStr));
			}

			subTypeMap.put(pv.getValue(), innerList);
		}
		//System.out.println("subTypeMap "+subTypeMap);
		return subTypeMap;
	}

	public List getSpecimenTypeStatusPVList()
	{
		return CDEManager.getCDEManager().getPermissibleValueList("Specimen Type", null);
	}

	public List getSCGList()
	{
		return null;
	}

	public List getUserList() throws DAOException
	{
		UserBizLogic userBizLogic = new UserBizLogic();
		List userList = userBizLogic.getUsers(Constants.ADD);
		return toStrList(userList);

	}

	public List getProcedureList()
	{
		List procedureList = CDEManager.getCDEManager().getPermissibleValueList(
				edu.wustl.catissuecore.util.global.Constants.CDE_NAME_COLLECTION_PROCEDURE, null);
		return toStrList(procedureList);
	}

	public List getContainerList()
	{
		List containerList = CDEManager.getCDEManager()
				.getPermissibleValueList(edu.wustl.catissuecore.util.global.Constants.CDE_NAME_CONTAINER, null);
		return toStrList(containerList);
	}

	public List getReceivedQualityList()
	{
		List qualityList = CDEManager.getCDEManager().getPermissibleValueList(edu.wustl.catissuecore.util.global.Constants.CDE_NAME_RECEIVED_QUALITY,
				null);
		return toStrList(qualityList);
	}

	public List getBiohazardTypeList()
	{
		List biohazardList = CDEManager.getCDEManager()
				.getPermissibleValueList(edu.wustl.catissuecore.util.global.Constants.CDE_NAME_BIOHAZARD, null);
		return toStrList(biohazardList);

	}

	public List getBiohazardNameList()
	{
		List<List> biohazardNameList = new ArrayList<List>();
		try
		{
			NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(
					edu.wustl.catissuecore.util.global.Constants.NEW_SPECIMEN_FORM_ID);
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
				List list = bizLogic.retrieve(Biohazard.class.getName(), selectColNames, whereColName, whereColCond, whereColVal,
						Constants.AND_JOIN_CONDITION);
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
		catch (DAOException e)
		{
			Logger.out.debug("Error mesg :" + e.getMessage());
		}

		return biohazardNameList;

	}

	//----------------------------------------------------------------------------------------//	

	public String writeSpecimen(List<SpecimenBean> spBeanList)
	{
		Logger.out.debug("spBeanList size " + spBeanList.size());
		//Map<String, String> msgMap = new HashMap<String, String>();
		//LinkedHashMap<Specimen,List> specimenMap = new LinkedHashMap<Specimen,List>();
		LinkedHashMap<String, GenericSpecimen> viewSpecimenMap = new LinkedHashMap<String, GenericSpecimen>();
		String message = "ERROR";

		//SessionDataBean sdb2 = (SessionDataBean) session.getAttribute(Constants.SESSION_DATA);
		int i = 1;
		for (SpecimenBean spBean : spBeanList)
		{
			/*	String msg = writeSpecimen(spBean);
			 Logger.out.debug("MSG " + spBean.specimenLabel + " : " + msg);
			 msgMap.put(spBean.specimenLabel, msg);*/
			SpecimenDataBean specimenDataBean = prepareGenericSpecimen(spBean);

			//spBean.spID = new Long(i);
			specimenDataBean.uniqueId = "" + i;
			viewSpecimenMap.put(specimenDataBean.getUniqueIdentifier(), specimenDataBean);
			i++;

		}
		try
		{
			//SessionDataBean sdb1 = (SessionDataBean) session.getAttribute(Constants.SESSION_DATA);
			/*IBizLogic bizLogic = AbstractBizLogicFactory.getBizLogic(ApplicationProperties.getValue("app.bizLogicFactory"), "getBizLogic",
			 edu.wustl.catissuecore.util.global.Constants.NEW_SPECIMEN_FORM_ID);
			 SessionDataBean sdb = new SessionDataBean();
			 sdb.setUserId(1L);
			 sdb.setUserName("admin@admin.com");

			 bizLogic.insert(specimenMap, sdb, Constants.HIBERNATE_DAO);*/
			//session.setAttribute("specimenMap",specimenMap);
			session.setAttribute(edu.wustl.catissuecore.util.global.Constants.SPECIMEN_LIST_SESSION_MAP, viewSpecimenMap);

			message = "SUCCESS";
		}
		catch (Exception ex)
		{
			message = ex.getMessage();

		}

		return message;
	}


	
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
			session.setAttribute(edu.wustl.catissuecore.util.global.Constants.SPECIMEN_LIST_SESSION_MAP, specimenHashSet);

			message = "SUCCESS";
		}
		catch (Exception ex)
		{
			message = ex.getMessage();

		}

		return message;
	}

public String editSpecimen(List<SpecimenBean> spBeanList)
	{
		LinkedHashSet<Specimen> specimenSet = new LinkedHashSet<Specimen>();
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
			SessionDataBean sdb = (SessionDataBean) session.getAttribute(Constants.SESSION_DATA);

			try
			{
				spBizLogic.bulkUpdateSpecimens(specimenSet, sdb);
			}
			catch (DAOException e)
			{
				return e.getMessage();
			}

		}
		return "Specimens Updated Successfully";
	}

	/*private String writeSpecimen(SpecimenBean spBean)
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

	 bizLogic.insert(sp, sdb, Constants.HIBERNATE_DAO);
	 message = "SUCCESS";
	 }
	 catch (Exception ex)
	 {
	 message = ex.getMessage();
	 }

	 return message;
	 }*/

	public SpecimenBean readSpecimen()
	{
		Logger.out.debug("SERVER readSpecimen");
		SpecimenBean sb = new SpecimenBean();
		sb.specimenLabel = "tp";
		sb.tissueSite = "VULVA";
		sb.specimenClass = "Molecular";
		sb.specimenType = "DNA";
		return sb;
	}

	public List<SpecimenBean> readSpecimenList() throws DAOException
	{
		List<SpecimenBean> list = new ArrayList<SpecimenBean>();
		// getting specimen list from session
		/*List specimenIdList = new ArrayList();
		 specimenIdList.add("66");
		 specimenIdList.add("89");
		 specimenIdList.add("4");
		 specimenIdList.add("1");
		 specimenIdList.add("3");
		 specimenIdList.add("5");
		 specimenIdList.add("47");*/
		session = flex.messaging.FlexContext.getHttpRequest().getSession();
		List specimenIdList = (List) session.getAttribute("specimenId");
		if (specimenIdList != null)
		{
			NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(
					edu.wustl.catissuecore.util.global.Constants.NEW_SPECIMEN_FORM_ID);
			Iterator itr = specimenIdList.iterator();
			while (itr.hasNext())
			{
				String specimenId = (String) itr.next();
				List specimenList = bizLogic.retrieve(Specimen.class.getName(), "id", specimenId);
				if (specimenList != null)
				{
					Specimen specimen = (Specimen) specimenList.get(0);
					Collection exIdColl = (Collection) bizLogic.retrieveAttribute(Specimen.class.getName(), specimen.getId(),
							"elements(externalIdentifierCollection)");
					specimen.setExternalIdentifierCollection(exIdColl);
					SpecimenBean sb = prepareSpecimenBean(specimen, Constants.EDIT);
					list.add(sb);

				}

			}

			/*list = new ArrayList<SpecimenBean>();
			 SpecimenBean sb = readSpecimen();
			 sb.specimenClass = "Tissue";
			 list.add(sb);
			 list.add(readSpecimen());
			 list.add(readSpecimen());
			 
			 list.add(sb);
			 list.add(readSpecimen());
			 list.add(readSpecimen());
			 */
		}
		return list;
	}

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
		if (specimen.getClassName() != null)
			sb.specimenClass = specimen.getClassName();
		if (specimen.getType() != null)
			sb.specimenType = specimen.getType();
		if (specimen.getPathologicalStatus() != null)
			sb.pathologicalStatus = specimen.getPathologicalStatus();
		if (specimen.getInitialQuantity() != null)
			sb.quantity = specimen.getInitialQuantity().getValue();
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
			sb.biohazardColl = new ArrayList(specimen.getBiohazardCollection());
		if (specimen.getExternalIdentifierCollection() != null)
			sb.exIdColl = new ArrayList(specimen.getExternalIdentifierCollection());

		return sb;
	}

	private Specimen getSpecimenInstance(String specimenClass)
	{
		System.out.println("specimenClass <" + specimenClass + ">");
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
		System.out.println("Returning basic specimen " + sp);
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

	private SpecimenDataBean prepareGenericSpecimen(SpecimenBean spBean)
	{
		SpecimenDataBean specimenDataBean = new SpecimenDataBean();

		specimenDataBean.setType(spBean.specimenType);
		specimenDataBean.setStorageContainerForSpecimen(spBean.storage);	
		//specimenDataBean.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		//sp.setAvailable(true);
		specimenDataBean.setQuantity(String.valueOf(spBean.quantity));
		//sp.setAvailableQuantity(qt);
		specimenDataBean.setClassName(spBean.specimenClass);
		specimenDataBean.setBarCode(spBean.specimenBarcode);
		/*sp.setBiohazardCollection(new HashSet<Biohazard>());
		 sp.setChildrenSpecimen(new HashSet<Specimen>());*/
		specimenDataBean.setComment(spBean.comment);
		//sp.setCreatedOn(new Date());
		specimenDataBean.setExternalIdentifierCollection(new HashSet<ExternalIdentifier>());
		if (spBean.exIdColl != null && !spBean.exIdColl.isEmpty())
		{
			specimenDataBean.setExternalIdentifierCollection(getExternalIdentifierColl(spBean.exIdColl));
		}

		if (spBean.biohazardColl != null && !spBean.biohazardColl.isEmpty())
		{
			specimenDataBean.setBiohazardCollection(getBiohazardColl(spBean.biohazardColl));
		}
		specimenDataBean.setLabel(spBean.specimenLabel);

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
		if (edu.wustl.catissuecore.util.global.Constants.NEW_SPECIMEN_TYPE.equals(spBean.parentType))
		{
			specimenDataBean.setSpecimenCollectionGroup(getSpecimenCollGrp(spBean.parentName));
		}
		else
		{
			specimenDataBean.setParentSpecimen(getParentSpecimen(spBean.parentName));
		}

		/*scg.setId(1L);
		 scg.setName("scg1");
		 sp.setSpecimenCollectionGroup(scg);*/

		Set<SpecimenEventParameters> eventSet = new HashSet<SpecimenEventParameters>();
		CollectionEventParameters collectionEvent = getCollectionEventParameters(spBean.collectionEvent);
		//collectionEvent.setSpecimen(sp);
		eventSet.add(collectionEvent);

		ReceivedEventParameters receEvent = getReceivedEventParameters(spBean.receivedEvent);
		//receEvent.setSpecimen(sp);
		eventSet.add(receEvent);

		specimenDataBean.setSpecimenEventCollection(eventSet);

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

				SpecimenDataBean derivedDataBean = prepareGenericSpecimen(derivedBean);
				derivedDataBean.setUniqueIdentifier(""+i);	
				derivedMap.put("d1" + i, derivedDataBean);
				i++;
			}
			specimenDataBean.setDeriveSpecimenCollection(derivedMap);
		}
		System.out.println("Returning complete specimen");
		return specimenDataBean;
	}

	/* prepare specimen for edit multiple specimen */
	private Specimen prepareSpecimen(SpecimenBean spBean)
	{
		Specimen specimen = getSpecimenInstance(spBean.specimenClass);
		if (edu.wustl.catissuecore.util.global.Constants.ADD.equals(spBean.mode))
		{
			specimen.setParentSpecimen(null);
			if (edu.wustl.catissuecore.util.global.Constants.NEW_SPECIMEN_TYPE.equals(spBean.parentType))
			{
				specimen.setSpecimenCollectionGroup(getSpecimenCollGrp(spBean.parentName));
			}
			else
			{
				specimen.setParentSpecimen(getParentSpecimen(spBean.parentName));
			}
			
			specimen.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
			specimen.setCollectionStatus(edu.wustl.catissuecore.util.global.Constants.SPECIMEN_COLLECTED);
			specimen.setAvailable(Boolean.TRUE);
			specimen.setStorageContainer(null);
			specimen.setLineage(edu.wustl.catissuecore.util.global.Constants.NEW_SPECIMEN);
		}
		
		
		specimen.setType(spBean.specimenType);
		specimen.setId(spBean.spID);
		specimen.setCreatedOn(spBean.creationDate);
		//specimenDataBean.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		//sp.setAvailable(true);
		Quantity qt = new Quantity();
		qt.setValue(spBean.quantity);
		specimen.setInitialQuantity(qt);
		specimen.setAvailableQuantity(qt);
		//if(edu.wustl.catissuecore.util.global.Constants.MOLECULAR.equals(spBean.specimenClass))

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

		if (!edu.wustl.catissuecore.util.global.Constants.EDIT.equals(spBean.mode))
		{
			Set<SpecimenEventParameters> eventSet = new HashSet<SpecimenEventParameters>();
			CollectionEventParameters collectionEvent = getCollectionEventParameters(spBean.collectionEvent);
			collectionEvent.setSpecimen(specimen);
			eventSet.add(collectionEvent);

			ReceivedEventParameters receEvent = getReceivedEventParameters(spBean.receivedEvent);
			receEvent.setSpecimen(specimen);
			eventSet.add(receEvent);

			specimen.setSpecimenEventCollection(eventSet);
		}

		if (spBean.derivedColl != null)
		{
			LinkedHashSet<Specimen> derivedSpecimenSet = new LinkedHashSet<Specimen>();
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
			specimen.setChildrenSpecimen(derivedSpecimenSet);
		}
		System.out.println("Returning complete specimen");
		return specimen;
	}

	/* finish */

	private HashSet getExternalIdentifierColl(Collection exIdColl)
	{
		HashSet<ExternalIdentifier> exIdSet = new HashSet<ExternalIdentifier>();
		Iterator itr = exIdColl.iterator();
		while (itr.hasNext())
		{
			ExternalIdentifier ex = (ExternalIdentifier) itr.next();
			if ((ex.getName() == null || ex.getName().equals("")) && (ex.getValue() == null || ex.getValue().equals("")))
			{
				continue;
			}

			ex.setId(ex.getId());
			exIdSet.add(ex);
		}
		return exIdSet;
	}

	private HashSet getBiohazardColl(Collection biohazardColl)
	{
		HashSet<Biohazard> biohazardSet = new HashSet<Biohazard>();
		Iterator itr = biohazardColl.iterator();
		while (itr.hasNext())
		{
			Biohazard biohazard = (Biohazard) itr.next();
			if (!biohazard.getName().equals(Constants.SELECT_OPTION) && !biohazard.getType().equals(Constants.SELECT_OPTION))
			{
				Long id = getBiohazardIdentifier(biohazard.getType(), biohazard.getName());
				biohazard.setId(id);
				biohazardSet.add(biohazard);
			}
		}
		return biohazardSet;
	}

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
			List list = bizLogic.retrieve(sourceObjName, selectColName, whereColName, whereColCond, whereColVal, Constants.AND_JOIN_CONDITION);
			Iterator itr = list.iterator();
			while (itr.hasNext())
			{
				Long id = (Long) itr.next();
				return id;
			}

		}
		catch (DAOException e)
		{
			Logger.out.debug("Error whioe getting biohazard Id:" + e.getMessage());
			System.out.println("Error whioe getting biohazard Id:" + e.getMessage());

		}

		return null;
	}

	private SpecimenCollectionGroup getSpecimenCollGrp(String scgName)
	{

		String sourceObjName = SpecimenCollectionGroup.class.getName();

		String[] whereColName = {"name"};
		String[] whereColCond = {"="};
		Object[] whereColVal = {scgName};
		SpecimenCollectionGroupBizLogic bizLogic = new SpecimenCollectionGroupBizLogic();
		try
		{
			List list = bizLogic.retrieve(sourceObjName, whereColName, whereColCond, whereColVal, Constants.AND_JOIN_CONDITION);
			Iterator itr = list.iterator();
			while (itr.hasNext())
			{
				SpecimenCollectionGroup scg = (SpecimenCollectionGroup) itr.next();
				return scg;
			}
			//SpecimenCollectionGroup scg1 = 
		}
		catch (DAOException e)
		{
			Logger.out.debug("Error whioe getting scg :" + e.getMessage());
			System.out.println("Error whioe getting scg:" + e.getMessage());

		}

		return null;

	}

	private Specimen getParentSpecimen(String parentName)
	{

		String sourceObjName = Specimen.class.getName();

		String[] whereColName = {"label"};
		String[] whereColCond = {"="};
		Object[] whereColVal = {parentName};
		NewSpecimenBizLogic bizLogic = new NewSpecimenBizLogic();
		try
		{
			List list = bizLogic.retrieve(sourceObjName, whereColName, whereColCond, whereColVal, Constants.AND_JOIN_CONDITION);
			Iterator itr = list.iterator();
			while (itr.hasNext())
			{
				Specimen specimen = (Specimen) itr.next();
				return specimen;
			}
			//SpecimenCollectionGroup scg1 = 
		}
		catch (DAOException e)
		{
			Logger.out.debug("Error whioe getting specimen :" + e.getMessage());
			System.out.println("Error whioe getting specimen :" + e.getMessage());

		}

		return null;

	}

	private CollectionEventParameters getCollectionEventParameters(EventParamtersBean collectionEvent)
	{
		CollectionEventParameters event = new CollectionEventParameters();
		//setCommomParam(event);
		event.setTimestamp(getTimeStamp(collectionEvent.eventdDate, collectionEvent.eventHour, collectionEvent.eventMinute));
		event.setCollectionProcedure(collectionEvent.collectionProcedure);
		event.setContainer(collectionEvent.container);
		event.setComment(collectionEvent.comment);
		User user = new User();
		user.setId(1L);
		event.setUser(user);
		return event;
	}

	private ReceivedEventParameters getReceivedEventParameters(EventParamtersBean receivedEvent)
	{
		ReceivedEventParameters event = new ReceivedEventParameters();
		event.setTimestamp(getTimeStamp(receivedEvent.eventdDate, receivedEvent.eventHour, receivedEvent.eventMinute));
		User user = new User();
		user.setId(1L);
		event.setUser(user);
		event.setComment(receivedEvent.comment);
		//setCommomParam(event);
		event.setReceivedQuality(receivedEvent.receivedQuality);
		return event;
	}

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
	public List<DAGNode> repaintDAG()
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
	 * 
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
			path.setId(new Long(p.getPathId()).toString());
			pathsListStr.add(path);
		}
		return pathsListStr;
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
				String pathStr = new Long(path.getPathId()).toString();
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

		//Getting the instance of participantRegistrationCacheManager
		ParticipantRegistrationCacheManager participantRegCacheManager = new ParticipantRegistrationCacheManager();

		//Getting the CP List 
		List cpColl = participantRegCacheManager.getCPDetailCollection();
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
		return cpList;
	}

	/**
	 * This method retrieves the List of participants associated with a cp
	 * @param cpId :Collection protocol Id
	 * @return the list of Participants
	 */
	public List getParticipantsList(String cpId, String cpTitle)
	{
		//Setting the cp title in session
		session = flex.messaging.FlexContext.getHttpRequest().getSession();
		session.setAttribute("cpTitle", cpTitle);
		List<CpAndParticipentsBean> participantsList = new ArrayList<CpAndParticipentsBean>();

		//Getting the instance of participantRegistrationCacheManager
		ParticipantRegistrationCacheManager participantRegCacheManager = new ParticipantRegistrationCacheManager();

		//getting the list of participants from cache for particular CP.
		List participantNamesWithId = participantRegCacheManager.getParticipantNames(Long.parseLong(cpId));

		//Values in participantNamesWithID will be in format (ID:lastName firstName) 
		//tokenize the value and create nameValueBean with name as (lastName firstName) and value as participantId 
		//and store in the list

		//List participantColl = new ArrayList();
		if (participantNamesWithId != null && participantNamesWithId.size() > 0)
		{
			Iterator itr = participantNamesWithId.iterator();
			while (itr.hasNext())
			{
				String participantIdAndName = (String) itr.next();
				int index = participantIdAndName.indexOf(":");
				Long Id = null;
				String name = "";
				Id = new Long(participantIdAndName.substring(0, index));
				name = participantIdAndName.substring(index + 1);

				participantsList.add(new CpAndParticipentsBean(name, Id.toString()));
				//participantColl.add(new NameValueBean(name, Id));
			}
		}
		else
		{
			System.out.println("The participants list is empty");
		}
		//Sorting the participants
		//Collections.sort(participantsList);
		return participantsList;
	}

	/**
	 * This mehtod returns the XML String for generating tree 
	 * @param cpId : Selcted Collection Protocol ID
	 * @param pId : Selected Participant Id
	 * @return : The XML String for tree data 
	 * @throws Exception
	 */
	public String getTreeData(String cpId, String pId) throws Exception
	{
		//System.out.println("In get tree data method & cpID is:"+cpId);
		SpecimenCollectionGroupBizLogic bizlogic = new SpecimenCollectionGroupBizLogic();
		String str = bizlogic.getSCGTreeForCPBasedView(Long.parseLong(cpId), Long.parseLong(pId));
		return str;
	}

}