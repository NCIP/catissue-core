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
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
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
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
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
				List arrayList = (ArrayList) session
						.getAttribute("DefineArrayFormObjects");
				Iterator arrayListItr = arrayList.iterator();
				while (arrayListItr.hasNext()) 
				{
					DefineArrayForm defineArrayFormObj = (DefineArrayForm) arrayListItr
							.next();
					orderToListArrayCollection.add(new NameValueBean(
							defineArrayFormObj.getArrayName(),
							defineArrayFormObj.getArrayName()));
				}
			}
			// Add the collection in request scope to be used in the
			// OrderItem.jsp
			request.setAttribute(Constants.ORDERTO_LIST_ARRAY,
					orderToListArrayCollection);

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
		List specimenClassList = CDEManager.getCDEManager()
				.getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_CLASS,
						null);

		request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);

		// Setting the specimen type list
		List specimenTypeList = CDEManager
				.getCDEManager()
				.getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_TYPE, null);
		request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);

		// Get the Specimen class and type from the cde
		CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(
				Constants.CDE_NAME_SPECIMEN_CLASS);
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
	private void setSiteAndStatus(HttpServletRequest request,
			OrderPathologyCaseForm pathology) 
	{

		NameValueBean bean = null;
		// Setting tissue site list
		List tissueSiteList = CDEManager.getCDEManager()
				.getPermissibleValueList(Constants.CDE_NAME_TISSUE_SITE, null);
		request.setAttribute(Constants.TISSUE_SITE_LIST, tissueSiteList);

		// Setting pathological status list
		List pathologicalStatusList = CDEManager.getCDEManager()
				.getPermissibleValueList(
						Constants.CDE_NAME_PATHOLOGICAL_STATUS, null);

		request.setAttribute(Constants.PATHOLOGICAL_STATUS_LIST,
				pathologicalStatusList);
	}

	/**
	 * @param request HttpServletRequest object
	 * @param pathology OrderPathologyCaseForm object
	 * @param orderForm OrderForm object
	 * @throws Exception object
	 */
	private void getProtocolName(HttpServletRequest request,
			OrderPathologyCaseForm pathology, OrderForm orderForm)
			throws Exception 
			{
		// to get the distribution protocol name
		DistributionBizLogic dao = (DistributionBizLogic) BizLogicFactory
				.getInstance().getBizLogic(Constants.DISTRIBUTION_FORM_ID);

		String sourceObjectName = DistributionProtocol.class.getName();
		String[] displayName = { "title" };
		String valueField = Constants.SYSTEM_IDENTIFIER;
		List protocolList = dao.getList(sourceObjectName, displayName,
				valueField, true);

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
		
//		List pathologicalCaseList = new ArrayList();
//		//retriving the id list from session.
//		if(request.getSession().getAttribute("pathologicalIdList") != null)
//		{
//			List idList = (List)request.getSession().getAttribute("pathologicalIdList");
//	    	
//			for(int i=0;i<idList.size();i++)
//			{
//				List pathologicalListFromDb = bizLogic.retrieve(IdentifiedSurgicalPathologyReport.class.getName(), "id", (String)idList.get(i));
//				IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport = (IdentifiedSurgicalPathologyReport)pathologicalListFromDb.get(0);
//				pathologicalCaseList.add(identifiedSurgicalPathologyReport);
//			}
//		}
//		return pathologicalCaseList;
				
			String sourceObjectName = IdentifiedSurgicalPathologyReport.class.getName();

			List pathologyCaseList = bizLogic.retrieve(sourceObjectName);
			return pathologyCaseList;

	}
}
