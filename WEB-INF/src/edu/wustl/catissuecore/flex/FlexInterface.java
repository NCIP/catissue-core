package edu.wustl.catissuecore.flex;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.PropertyConfigurator;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.EventParameters;
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
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Variables;
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
		System.out.println("str from flex client "+str);
		SpecimenBean sb = new SpecimenBean();
		sb.specimenLabel = "AA";
		return sb;
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
			String specimenKey = (String)itr1.next();
			specimenClassList.add(new NameValueBean(specimenKey, specimenKey));
		}
		return toStrList(specimenClassList);
	}
	
	public List<String> getFluidSpecimenTypePVList()
	{
		Map specimenTypeMap = getSpecimenTypeMap();
		List<NameValueBean> aList = (List)specimenTypeMap.get("Fluid");
		return toStrList(aList);
	}
	
	public List<String> getTissueSpecimenTypePVList()
	{
		Map specimenTypeMap = getSpecimenTypeMap();
		List<NameValueBean> aList =  (List)specimenTypeMap.get("Tissue");
		return toStrList(aList);
	}
	
	public List<String> getMolecularSpecimenTypePVList()
	{
		Map specimenTypeMap = getSpecimenTypeMap();
		List<NameValueBean> aList =  (List<NameValueBean>)specimenTypeMap.get("Molecular");
		return toStrList(aList);
	}
	
	public List<String> getCellSpecimenTypePVList()
	{
		Map specimenTypeMap = getSpecimenTypeMap();
		List<NameValueBean> aList = (List<NameValueBean>)specimenTypeMap.get("Cell");
		return toStrList(aList);
	}

	private Map getSpecimenTypeMap()
	{
		CDE specimenClassCDE = CDEManager.getCDEManager().getCDE("Specimen");
		Set setPV = specimenClassCDE.getPermissibleValues();
		Iterator itr = setPV.iterator();

		//List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList("Specimen", null);
		Map<String,List> subTypeMap = new HashMap<String,List>();
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
	
//----------------------------------------------------------------------------------------//	
	
	public String writeSpecimen(List<SpecimenBean> spBeanList)
	{
		Logger.out.debug("spBeanList size "+spBeanList.size());
		Map<String, String> msgMap = new HashMap<String, String>();
		for (SpecimenBean spBean : spBeanList)
		{
			String msg = writeSpecimen(spBean);
			Logger.out.debug("MSG "+spBean.specimenLabel+" : "+msg);
			msgMap.put(spBean.specimenLabel, msg);
		}
		return msgMap.toString();
	}

	private String writeSpecimen(SpecimenBean spBean)
	{
		System.out.println("SERVER writeSpecimen");
		Specimen sp = prepareSpecimen(spBean);
		String message = "ERROR";
		
		if(sp==null)
		{
			return message;
		}
		
		try
		{
			NewSpecimenBizLogic spBizLogic = (NewSpecimenBizLogic)BizLogicFactory.getInstance().getBizLogic(edu.wustl.catissuecore.util.global.Constants.NEW_SPECIMEN_FORM_ID);
			SessionDataBean sdb = new SessionDataBean();
			sdb.setUserId(1L);
			sdb.setUserName("admin@admin.com");
			spBizLogic.insert(sp, sdb, Constants.HIBERNATE_DAO);
			message = "SUCCESS";
		}
		catch(Exception ex)
		{
			message = ex.getMessage();
		}
		
		return message;
	}
	
	public SpecimenBean readSpecimen()
	{
		Logger.out.debug("SERVER readSpecimen");
		SpecimenBean sb = new SpecimenBean();
		return sb;
	}
	
	private Specimen getSpecimenInstance(String specimenClass)
	{
		System.out.println("specimenClass <"+specimenClass+">");
		Specimen sp = null;
		if(specimenClass.indexOf("Tissue")!=-1)
		{
			sp = new TissueSpecimen();
		}
		else if(specimenClass.indexOf("Fluid")!=-1)
		{
			sp = new FluidSpecimen();
		}
		else if(specimenClass.indexOf("Molecular")!=-1)
		{
			sp = new MolecularSpecimen();
		}
		else if(specimenClass.indexOf("Cell")!=-1)
		{
			sp = new CellSpecimen();
		}
		System.out.println("Returning basic specimen "+sp);
		return sp;
	}
	
	private Specimen prepareSpecimen(SpecimenBean spBean)
	{
		Specimen sp = getSpecimenInstance(spBean.specimenClass);
		if(sp==null)
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
		sp.setExternalIdentifierCollection(new HashSet<ExternalIdentifier>());
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
		sp.setSpecimenCollectionGroup(scg);
		
		Set<SpecimenEventParameters> eventSet = new HashSet<SpecimenEventParameters>();
		eventSet.add(getCollectionEventParameters());
		eventSet.add(getReceivedEventParameters());
		sp.setSpecimenEventCollection(eventSet);
				
		//StorageContainerBizLogic scBizLogic = (StorageContainerBizLogic)BizLogicFactory.getInstance().getBizLogic(edu.wustl.catissuecore.util.global.Constants.STORAGE_CONTAINER_FORM_ID);
		//scBizLogic.setNe
		
		sp.setStorageContainer(null);
		sp.setPositionDimensionOne(null);
		sp.setPositionDimensionTwo(null);
		System.out.println("Returning complete specimen");
		return sp;
	}

	private CollectionEventParameters getCollectionEventParameters()
	{
		CollectionEventParameters event = new CollectionEventParameters();
		setCommomParam(event);
		event.setCollectionProcedure("Not Specified");
		event.setContainer("Not Specified");
		return event;
	}
	
	private ReceivedEventParameters getReceivedEventParameters()
	{
		ReceivedEventParameters event = new ReceivedEventParameters();
		setCommomParam(event);
		event.setReceivedQuality("Not Specified");
		return event;
	}
	
	private void setCommomParam(EventParameters event)
	{
		event.setComment("");
		event.setTimestamp(new Date());
		User user = new User();
		user.setId(1L);
		event.setUser(user);
	}
}