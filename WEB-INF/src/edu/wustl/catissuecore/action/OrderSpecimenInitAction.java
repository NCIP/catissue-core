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
import edu.wustl.common.action.BaseAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.DefineArrayForm;
import edu.wustl.catissuecore.actionForm.OrderSpecimenForm;
import edu.wustl.catissuecore.actionForm.OrderForm;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.DistributionBizLogic;
import edu.wustl.catissuecore.domain.Specimen;



/**Action for ordering specimen
 * @author deepti_phadnis
 * 
 */
public class  OrderSpecimenInitAction  extends BaseAction
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
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
		OrderSpecimenForm spec = (OrderSpecimenForm) form;
        HttpSession session = request.getSession();
        spec.setTypeOfSpecimen("existingSpecimen");
	    
        if(session.getAttribute("OrderForm")!=null)
        {
        	OrderForm orderForm=(OrderForm)session.getAttribute("OrderForm");
	        spec.setOrderForm(orderForm);
		    if(orderForm.getDistributionProtocol()!=null)
	    	{
	    		getProtocolName(request,spec,orderForm);
	    	}
	        
		    Collection specimen;
		    specimen=(List)getDataFromDatabase(request);
	    	try
		    {
		        request.setAttribute("specimen",specimen);
		        
		        //Setting specimen class list	
		        List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_CLASS, null);
				request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);
	
				//Setting the specimen type list
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
	
				// sets the Class list
				request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);
	
				// set the map to subtype
				request.setAttribute(Constants.SPECIMEN_TYPE_MAP, subTypeMap);
	
				//default checked on existing specimen
				spec.setTypeOfSpecimen("existingSpecimen");
	
		    }
	    	
		    catch(Exception excp)
		    {
		    	Logger.out.error(excp.getMessage(),excp); 
		    }
		    
		    request.setAttribute("typeOf","specimen");
		    request.setAttribute("OrderSpecimenForm", spec);
		    
		    List orderToListArrayCollection = new ArrayList();
		    orderToListArrayCollection.add(new NameValueBean("None","None"));
		    
		    if(session.getAttribute("DefineArrayFormObjects") != null)
		    {
		    	List arrayList = (ArrayList)session.getAttribute("DefineArrayFormObjects");
		    	Iterator arrayListItr = arrayList.iterator();
		    	while(arrayListItr.hasNext())
		    	{
		    		DefineArrayForm defineArrayFormObj = (DefineArrayForm) arrayListItr.next();
		    		orderToListArrayCollection.add(new NameValueBean(defineArrayFormObj.getArrayName(),defineArrayFormObj.getArrayName()));
		    	}
		    }
		    //Add the collection in request scope to be used in the OrderItem.jsp
		    request.setAttribute(Constants.ORDERTO_LIST_ARRAY,orderToListArrayCollection);	
		    
			return mapping.findForward("success");
        }
        else
        {
        	return mapping.findForward("failure");
        }
   	}


	
	
	
	
	/** function for getting protocol name
	 * @param request HttpServletRequest object
	 * @param spec OrderSpecimenForm object
	 * @param orderForm OrderForm object
	 * @throws Exception object
	 */
	private void getProtocolName(HttpServletRequest request,OrderSpecimenForm spec,OrderForm orderForm) throws Exception
	{
    	//to get the distribution protocol name
		DistributionBizLogic dao = (DistributionBizLogic) BizLogicFactory.getInstance()
        .getBizLogic(Constants.DISTRIBUTION_FORM_ID);
		
    	String sourceObjectName = DistributionProtocol.class.getName();
		String[] displayName = { "title" };
		String valueField = Constants.SYSTEM_IDENTIFIER;
		List protocolList = dao.getList(sourceObjectName, displayName,
		valueField, true);
		
		request.setAttribute(Constants.DISTRIBUTIONPROTOCOLLIST, protocolList);

		
		for(int i=0;i<protocolList.size();i++)
		{
			NameValueBean obj=(NameValueBean)protocolList.get(i);
			
			if(orderForm.getDistributionProtocol().equals(obj.getValue()))
			{
				spec.setDistrbutionProtocol(obj.getName());
			}
		}
	}
	
	/** function for getting data from database
	 * @param request HttpServletRequest object
	 * @return List of specimen objects
	 */
	private List getDataFromDatabase(HttpServletRequest request)
	{
		//to get data from database when specimen id is given
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
		HttpSession session = request.getSession(true);
		
		try
    	{
    		
	    	String sourceObjectName = Specimen.class.getName();
	    	String columnName="id";
	    	
			List valueField=(List)session.getAttribute("specimenId");
	    	List specimen=new ArrayList();
	    	if(valueField != null && valueField.size() >0)
	    	{
				for(int i=0;i<valueField.size();i++)
				{
					List specimenList = bizLogic.retrieve(sourceObjectName, columnName, (String)valueField.get(i));
					Specimen speclist=(Specimen)specimenList.get(0);
					specimen.add(speclist);
				}
	    	}
			return specimen;
    	}
    	catch(DAOException e)
    	{
    		Logger.out.error(e.getMessage(), e);
    		return null;
    	}
	}
}
    


