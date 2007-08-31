package edu.wustl.catissuecore.flex;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.PropertyConfigurator;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.metadata.path.Path;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.impl.Rule;
import edu.wustl.cab2b.client.ui.query.ClientQueryBuilder;
import edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface;
import edu.wustl.cab2b.client.ui.query.IPathFinder;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.querysuite.GenerateHtmlForAddLimitsBizLogic;
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
import edu.wustl.catissuecore.flex.dag.DAGNode;
import edu.wustl.catissuecore.flex.dag.DAGPanel;
import edu.wustl.catissuecore.flex.dag.DAGPath;
import edu.wustl.catissuecore.flex.dag.DAGPathFinder;
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
		Variables.applicationHome = System.getProperty("user.dir");
		Logger.out = org.apache.log4j.Logger.getLogger("");
		PropertyConfigurator.configure(Variables.applicationHome+"\\WEB-INF\\src\\"+"ApplicationResources.properties");
		
		Logger.out.debug("here");
		CDEManager.init();
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
	
	public List<SpecimenBean> readSpecimenList()
	{
		List<SpecimenBean> list = new ArrayList<SpecimenBean>();
		
		list.add(readSpecimen());
		list.add(readSpecimen());
		list.add(readSpecimen());
		
		return list;
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
	//--------------DAG-----------------------------
	
	public List<DAGNode> repaintCreateDAG()
	{
		return dagPanel.repaintCreateDAG(session);
	}
	public String getSearchResult()
	{
	
		return dagPanel.search(session);
	}
	/**
	 * Set i/p from flex
	 * @param strToCreateQueryObject
	 * @param entityName
	 */
	public void setNode(String strToCreateQueryObject,String entityName)
    {
	//TODO Write code to refresh node list when new http request arrives.
//		 Add  node each time  and Append it list of node to keep track of nodes
		// Delete node should remove node from list required to idenetify node uniquely 
		// Should remove any assocation
		DAGNode dagNode = dagPanel.createQueryObject(strToCreateQueryObject, entityName, session,queryObject,"Add");
		nodeList.add(dagNode);
		
	}
	
	public String getLimitUI(int expressionId)
	{
		
		Map map =dagPanel.editAddLimitUI(expressionId);
		String htmlStr = (String)map.get("HTMLSTR");
		IExpression expression = (IExpression)map.get("EXPRESSION");
		dagPanel.setExpression(expression);
		System.out.println("[ " +htmlStr +" ]");
		return htmlStr;
	}
	
	public String editNode(String strToCreateQueryObject,String entityName)
	{
		String conditionStr	= null;
		System.out.println("strToCreateQueryObject ==>"+strToCreateQueryObject);
		System.out.println("entityName====>"+entityName);
		DAGNode dagNode = dagPanel.createQueryObject(strToCreateQueryObject, entityName, session,queryObject,"Edit");
		System.out.println("dagnode  id "+dagNode.getExpressionId());
		for(int i=0;i<nodeList.size();i++)
		{
			DAGNode node  = nodeList.get(i);
			System.out.println("node  id====> "+node.getExpressionId());
			if(node.equals(dagNode))
			{
				nodeList.remove(i);
				nodeList.add(i,dagNode);
				break;
			}
		}
		for  (int i=0;i<5;i++)
			System.out.println(i);
		conditionStr = dagNode.getToolTip();
		System.out.println("conditionStr==="+conditionStr);
		return conditionStr;
	}
	
	/**
	 * GetLastNode
	 * @return
	 */
	public DAGNode getLastNode()
	{
		int lastIndex =0;
		if(!nodeList.isEmpty())
		{
			lastIndex= (nodeList.size()-1);
		}
		return nodeList.get(lastIndex);
	}
	
	public void deleteNode(String nodeName)
	{
		for(int i=0;i<nodeList.size();i++)
		{
			DAGNode dagNode = nodeList.get(i);
			if(dagNode.getNodeName().equals(nodeName));
			{
				nodeList.remove(i);
				break;
			}
		}
	}
	
	public List getpaths(List<DAGNode> linkedNodeList)
	{
		sourceNode = linkedNodeList.get(0);
		destinationNode = linkedNodeList.get(1);
		pathsList=dagPanel.getPaths(sourceNode, destinationNode);
		List<DAGPath> pathsListStr = new ArrayList<DAGPath>();
		for(int i=0;i<pathsList.size();i++)
		{
			Path p =(Path) pathsList.get(i);
			DAGPath path = new DAGPath();
			path.setName(DAGPanel.getPathDisplayString(pathsList.get(i)));
			path.setId(new Long(p.getPathId()));
			pathsListStr.add(path);
		}
		return pathsListStr;
	}
	
	public void linkNodes(List<DAGPath> selectedPaths)
	{
		try {
			List<IPath> selectedList = new ArrayList<IPath>();
			for(int j=0;j<selectedPaths.size();j++)
			{
				for(int i=0; i<pathsList.size();i++)
				{
					Path path =(Path) pathsList.get(i);
					long pathId = selectedPaths.get(j).getId().longValue();
					if(path.getPathId()==pathId)
					{
						selectedList.add(path);
						break;
					}
					
				}
			}
			dagPanel.linkNode(sourceNode,destinationNode,selectedList);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setLogicalOperator(DAGNode node,int operandIndex,String operator)
	{
		int parentExpId = node.getExpressionId();
		System.out.println("parentExpId =="+parentExpId+"==operandIndex=="+operandIndex+"==operator=="+operator );
		dagPanel.updateLogicalOperator(parentExpId, operandIndex, operator);
		
	}

	public void initFlexInterface()
	{
		nodeList = new ArrayList<DAGNode>();
		queryObject = new ClientQueryBuilder();
		IPathFinder pathFinder = new DAGPathFinder();
		dagPanel = new DAGPanel(pathFinder);
		dagPanel.setQueryObject(queryObject);
		session= flex.messaging.FlexContext.getHttpRequest().getSession();
	}
	private DAGNode sourceNode= null;
	private DAGNode destinationNode = null; 
	private	IClientQueryBuilderInterface queryObject;
	private List<DAGNode> nodeList;
	private List<IPath> pathsList;
	private DAGPanel dagPanel;
	private HttpSession session = null;
	
	
	
}