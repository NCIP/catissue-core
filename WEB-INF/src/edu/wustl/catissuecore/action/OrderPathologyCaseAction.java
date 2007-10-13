
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.DefineArrayForm;
import edu.wustl.catissuecore.actionForm.OrderForm;
import edu.wustl.catissuecore.actionForm.OrderPathologyCaseForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.DistributionBizLogic;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.SurgicalPathologyReport;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * Action for ordering specimen
 * 
 * @author deepti_phadnis
 * 
 */
public class OrderPathologyCaseAction extends BaseAction
{

	/**
	 * @param mapping ActionMapping object
	 * @param form ActionForm object
	 * @param request HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @return ActionForward object 
	 * @throws Exception object
	 */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		OrderPathologyCaseForm pathology = (OrderPathologyCaseForm) form;
		HttpSession session = request.getSession();
		pathology.setTypeOfCase("derivative");

		if (session.getAttribute("OrderForm") != null)
		{
			OrderForm orderForm = (OrderForm) session.getAttribute("OrderForm");
			pathology.setOrderForm(orderForm);
			if (orderForm.getDistributionProtocol() != null)
			{
				getProtocolName(request, pathology, orderForm);
			}

			Collection pathologyCase;
			pathologyCase = (List) getDataFromDatabase(request);
			request.setAttribute("pathologyCase", pathologyCase);
			setClassAndTypeInList(request);
			setSiteAndStatus(request, pathology);

			List orderToListArrayCollection = new ArrayList();
			orderToListArrayCollection.add(new NameValueBean("None", "None"));

			if (session.getAttribute("DefineArrayFormObjects") != null)
			{
				List arrayList = (ArrayList) session.getAttribute("DefineArrayFormObjects");
				Iterator arrayListItr = arrayList.iterator();
				while (arrayListItr.hasNext())
				{
					DefineArrayForm defineArrayFormObj = (DefineArrayForm) arrayListItr.next();
					orderToListArrayCollection.add(new NameValueBean(defineArrayFormObj.getArrayName(), defineArrayFormObj.getArrayName()));
				}
			}
			// Add the collection in request scope to be used in the
			// OrderItem.jsp
			request.setAttribute(Constants.ORDERTO_LIST_ARRAY, orderToListArrayCollection);

			request.setAttribute("typeOf", "pathologyCase");
			request.setAttribute("OrderPathologyCaseForm", pathology);
			return mapping.findForward("success");
		}
		else
		{
			return mapping.findForward("failure");
		}
	}

	/**
	 * function for setting the class and type 
	 * @param request HttpServletRequest object 
	 */
	private void setClassAndTypeInList(HttpServletRequest request)
	{
		// Setting specimen class list
		List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_CLASS, null);

		request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);

		// Setting the specimen type list
		List specimenTypeList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_TYPE, null);
		request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);

		// Get the Specimen class and type from the cde
		CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(Constants.CDE_NAME_SPECIMEN_CLASS);
		Set setPV = specimenClassCDE.getPermissibleValues();
		Iterator itr = setPV.iterator();

		specimenClassList = new ArrayList();
		Map subTypeMap = new HashMap();
		specimenClassList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));

		while (itr.hasNext())
		{
			List innerList = new ArrayList();
			Object obj = itr.next();
			PermissibleValue pv = (PermissibleValue) obj;
			String tmpStr = pv.getValue();
			specimenClassList.add(new NameValueBean(tmpStr, tmpStr));

			Set list1 = pv.getSubPermissibleValues();
			Iterator itr1 = list1.iterator();
			innerList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));
			while (itr1.hasNext())
			{
				Object obj1 = itr1.next();
				PermissibleValue pv1 = (PermissibleValue) obj1;
				// set specimen type
				String tmpInnerStr = pv1.getValue();
				innerList.add(new NameValueBean(tmpInnerStr, tmpInnerStr));
			}
			subTypeMap.put(pv.getValue(), innerList);
		} // class and values set

		//specimenClassList.remove(1);

		// sets the Class list
		request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);

		// set the map to subtype
		request.setAttribute(Constants.SPECIMEN_TYPE_MAP, subTypeMap);

	}

	/**
	 * @param request HttpServletRequest object
	 * @param pathology OrderPathologyCaseForm object
	 */
	private void setSiteAndStatus(HttpServletRequest request, OrderPathologyCaseForm pathology)
	{

		NameValueBean bean = null;
		// Setting tissue site list
		List tissueSiteList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_TISSUE_SITE, null);
		request.setAttribute(Constants.TISSUE_SITE_LIST, tissueSiteList);

		// Setting pathological status list
		List pathologicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_PATHOLOGICAL_STATUS, null);

		request.setAttribute(Constants.PATHOLOGICAL_STATUS_LIST, pathologicalStatusList);
	}

	/**
	 * @param request HttpServletRequest object
	 * @param pathology OrderPathologyCaseForm object
	 * @param orderForm OrderForm object
	 * @throws Exception object
	 */
	private void getProtocolName(HttpServletRequest request, OrderPathologyCaseForm pathology, OrderForm orderForm) throws Exception
	{
		// to get the distribution protocol name
		DistributionBizLogic dao = (DistributionBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.DISTRIBUTION_FORM_ID);

		String sourceObjectName = DistributionProtocol.class.getName();
		String[] displayName = {"title"};
		String valueField = Constants.SYSTEM_IDENTIFIER;
		List protocolList = dao.getList(sourceObjectName, displayName, valueField, true);

		request.setAttribute(Constants.DISTRIBUTIONPROTOCOLLIST, protocolList);

		for (int i = 0; i < protocolList.size(); i++)
		{
			NameValueBean obj = (NameValueBean) protocolList.get(i);

			if (orderForm.getDistributionProtocol().equals(obj.getValue()))
			{
				pathology.setDistrbutionProtocol(obj.getName());
			}
		}
	}

	/**
	 * @param request HttpServletRequest object
	 * @return List of Pathology Case objects
	 */
	private List getDataFromDatabase(HttpServletRequest request) throws DAOException
	{
		// to get data from database when specimen id is given
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.NEW_PATHOLOGY_FORM_ID);
		List pathologicalCaseList = new ArrayList();
		//retriving the id list from session.
		String [] className = {IdentifiedSurgicalPathologyReport.class.getName(),DeidentifiedSurgicalPathologyReport.class.getName(),SurgicalPathologyReport.class.getName()};
		int [] size = new int[3];
		
		if(request.getSession().getAttribute(Constants.PATHALOGICAL_CASE_ID) != null)
		{
			size[0] = getList(request, Constants.PATHALOGICAL_CASE_ID , className[0], pathologicalCaseList, bizLogic);
		}
		if(request.getSession().getAttribute(Constants.DEIDENTIFIED_PATHALOGICAL_CASE_ID) != null)
		{
			size[1] = size[0] + getList(request , Constants.DEIDENTIFIED_PATHALOGICAL_CASE_ID ,className[1], pathologicalCaseList, bizLogic);
		}
		if(request.getSession().getAttribute(Constants.SURGICAL_PATHALOGY_CASE_ID) != null)
		{
			size[2] = size[1] + getList(request, Constants.SURGICAL_PATHALOGY_CASE_ID , className[2], pathologicalCaseList, bizLogic);
		}
		
		if (pathologicalCaseList != null && !pathologicalCaseList.isEmpty())
		{
			for(int i = 0 ; i < pathologicalCaseList.size() ; i++)
			{
				String[] selectColName = {"specimenCollectionGroup"};
				String[] whereColName = {Constants.SYSTEM_IDENTIFIER};
				String[] whereColCond = {"="};
				String sourceObjectName;
				SurgicalPathologyReport identifiedSurPathReport = (SurgicalPathologyReport) pathologicalCaseList.get(i);
				Object[] whereColVal = {identifiedSurPathReport.getId()};
				if(i<size[0])
				{
					sourceObjectName = className[0];
				}
				else if(i>=size[0] && i<size[1])
				{
					sourceObjectName = className[1];
				}
				else
				{
					sourceObjectName = className[2];
				}
				List specimenCollList = bizLogic.retrieve(sourceObjectName,selectColName,whereColName,whereColCond,whereColVal,Constants.AND_JOIN_CONDITION);
				if(specimenCollList != null && !specimenCollList.isEmpty())
				{
					SpecimenCollectionGroup specimenColGroup = (SpecimenCollectionGroup) specimenCollList.get(0);
					identifiedSurPathReport.setSpecimenCollectionGroup(specimenColGroup);
				}
			}
		}
		
		return pathologicalCaseList;
	}
	
	private int getList(HttpServletRequest request , String attr , String className , List pathologicalCaseList, IBizLogic bizLogic)throws DAOException
	{
		String columnName="id";
		List idList = (List)request.getSession().getAttribute(attr);
		int size = idList.size();
		for(int i=0;i<idList.size();i++)
		{
			List pathologicalListFromDb = bizLogic.retrieve(className, columnName, (String)idList.get(i));
			pathologicalCaseList.add(pathologicalListFromDb.get(0));
		}
		return size;
	}
}
