package edu.wustl.catissuecore.action;

import java.util.Collection;
import edu.wustl.cab2b.client.metadatasearch.MetadataSearch;
import edu.wustl.cab2b.common.beans.MatchedClass;
import edu.wustl.cab2b.common.util.Constants;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.client.metadatasearch.MetadataSearch;
import edu.wustl.cab2b.common.beans.MatchedClass;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.catissuecore.actionForm.AliquotForm;
import edu.wustl.catissuecore.actionForm.CategorySearchForm;
import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.SpecimenForm;
import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.annotations.AnnotationUtil;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryShoppingCartBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.querysuite.QueryShoppingCart;
import edu.wustl.catissuecore.util.querysuite.EntityCacheFactory;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.dbManager.DAOException;
import gate.security.Session;
import edu.wustl.common.bizlogic.IBizLogic;


public class NewShopingCartAction extends BaseAction {
	
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws DAOException,DynamicExtensionsSystemException, CheckedException {
		
		List<List<String>> cartnew = new ArrayList<List<String>>();
		HttpSession session = request.getSession();
		int count=0;
		int newCartSize=0;
		int oldCartSize=0;
		QueryShoppingCart queryShoppingCart =(QueryShoppingCart)session.getAttribute(edu.wustl.catissuecore.util.global.Constants.QUERY_SHOPPING_CART);
		QueryShoppingCartBizLogic queryShoppingCartBizLogic=new QueryShoppingCartBizLogic();
		List<AttributeInterface> oldAttributeList=new ArrayList<AttributeInterface>();
		List<AttributeInterface> cartAttributeList=new ArrayList<AttributeInterface>();
		List<String> columnList=new ArrayList<String>();
		String pageOf = request.getParameter(edu.wustl.catissuecore.util.global.Constants.PAGEOF);
		
		int[] searchTarget = prepareSearchTarget();
		int basedOn = 0;
		
		Set<EntityInterface> entityCollection = new HashSet<EntityInterface>();
		String[] searchString  ={"specimen"};
		Collection<AttributeInterface> attributeCollection=new ArrayList<AttributeInterface>();
		EntityCache cache = EntityCacheFactory.getInstance();
		MetadataSearch advancedSearch = new MetadataSearch(cache);
		MatchedClass matchedClass = advancedSearch.search(searchTarget, searchString, basedOn);
		entityCollection = matchedClass.getEntityCollection();
		List resultList = new ArrayList(entityCollection);
		for (int i = 0; i < resultList.size(); i++)
		{
			EntityInterface entity = (EntityInterface) resultList.get(i);
			String fullyQualifiedEntityName = entity.getName();
			if(fullyQualifiedEntityName.equals(Specimen.class.getName()))
			{
				attributeCollection=entity.getEntityAttributesForQuery();
				break;
			}
					
		}
		Iterator<AttributeInterface> attributreItr=attributeCollection.iterator();
		
		String[] selectColumnName = new String[attributeCollection.size()];
		for(int i=0;i<attributeCollection.size();i++)
		{
			
			AttributeInterface elem= attributreItr.next();
			String columnName=elem.getName().toString();
			selectColumnName[i]=columnName;
			columnList.add(columnName + " : " + "Specimen");
			cartAttributeList.add(elem);
		}
		
		if(queryShoppingCart==null)
		{
			queryShoppingCart=new QueryShoppingCart();
			queryShoppingCart.setCartAttributeList(cartAttributeList);
			queryShoppingCart.setColumnList(columnList);
			oldAttributeList=cartAttributeList;
			
		}
		else
		{	
			/*deleted the cart*/
			if(queryShoppingCart.isEmpty())
			{
				queryShoppingCart.setCartAttributeList(cartAttributeList);
				queryShoppingCart.setColumnList(columnList);
				oldAttributeList=cartAttributeList;
			}
			if(queryShoppingCart!=null&&queryShoppingCart.getCartAttributeList()!=null)
			{
				oldAttributeList = queryShoppingCart.getCartAttributeList();	
				oldCartSize=queryShoppingCart.getCart().size();
				
				if(queryShoppingCart.getCartAttributeList().get(0).getEntity().getName().equals(Specimen.class.getName()))
				{
					selectColumnName =new String[oldAttributeList.size()];
					selectColumnName=getManiputedColumnList(queryShoppingCart.getCartAttributeList());
					//cartAttributeList=new ArrayList<AttributeInterface>();
					cartAttributeList=oldAttributeList;
					
				}
				else 
				{
					selectColumnName=null;
				}
				
			}
		}
		
		if(selectColumnName!=null)
		{	
			cartnew=createListOfItems(form,selectColumnName,request);
	
			//Action Errors changed to Action Messages
			ActionMessages messages = new ActionMessages();
			int indexArray[] = queryShoppingCartBizLogic.getNewAttributeListIndexArray(oldAttributeList, cartAttributeList);
			if (indexArray != null)
			{

				count=queryShoppingCartBizLogic.add(queryShoppingCart, cartnew, null);
				newCartSize=queryShoppingCart.getCart().size();
				
				if (count >0)
				{
					if((cartnew.size()+oldCartSize-newCartSize)>0)
					{
						messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage
								("shoppingcart.duplicateObjError",count,
										cartnew.size()+oldCartSize-newCartSize));
						saveMessages(request, messages);
					}
					else
					{
						messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage
								("shoppingCart.addMessage",cartnew.size()));
						saveMessages(request, messages);
					}
					
				}
				else if(count ==0)
				{
					if((cartnew.size()+oldCartSize-newCartSize)>0)
					{
						messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage
								("shoppingcart.duplicateObjError",count,cartnew.size()+oldCartSize-newCartSize));
						saveMessages(request, messages);
					}
				}
	
			}
			else{
				addDifferentCartViewError(request);
			}
			
		}else{
				addDifferentCartViewError(request);
			}
		
		session.setAttribute(edu.wustl.catissuecore.util.global.Constants.QUERY_SHOPPING_CART,queryShoppingCart);
		return mapping.findForward(pageOf); 
	}
	
	private String[] getManiputedColumnList(
			List<AttributeInterface> oldAttributeList) {
		
		String[] selectColumnName = new String[oldAttributeList.size()];
		
		for (int i = 0; i < oldAttributeList.size(); i++) {
			
			AttributeInterface attributeInterface=oldAttributeList.get(i);
		
			selectColumnName[i]=attributeInterface.getName();
			
		}
		return selectColumnName;
	}

	/**
	 * @param form  object of ActionForm
	 * @param request 
	 * @return cart
	 * @throws DAOException Database related Exception
	 */
	private List<List<String>> createListOfItems(ActionForm form,String[] selectColumnName, HttpServletRequest request) throws DAOException {
		String objName=Specimen.class.getName();
		IBizLogic bizLogic=getBizLogic(objName);
		Object searchObjects = null;
		AliquotForm aliquotForm = new AliquotForm();
		SpecimenForm specimenForm=new SpecimenForm();
		
		List<List<String>> cartnew = new ArrayList<List<String>>();
		List<String> columnList=new ArrayList<String>();
		String[] whereColumnCondition = {"="};
		String[] whereColumnName = {"id"};
		
			 if(form instanceof AliquotForm)
			{
				aliquotForm = (AliquotForm) form;
				List<AbstractDomainObject> specimenList = aliquotForm.getSpecimenList();
				Iterator<AbstractDomainObject> it=specimenList.iterator();
			
				while (it.hasNext()) {
				
					List<String> columnList2=new ArrayList<String>();
					Specimen specimen=  (Specimen)it.next();
					Object[] whereColumnValue = {specimen.getId()};
					List ls1 =bizLogic.retrieve(objName, selectColumnName,whereColumnName,whereColumnCondition,whereColumnValue,null);
					columnList2=createList(ls1);
					cartnew.add(columnList2);
				}
			}
			 else if(form instanceof SpecimenForm)
			 {
				 	specimenForm = (SpecimenForm) form;
				 	Object[] whereColumnValue = {specimenForm.getId()};
				 	List ls= bizLogic.retrieve(objName, selectColumnName,whereColumnName,whereColumnCondition,whereColumnValue,null);
				 	columnList=createList(ls);
				 	cartnew.add(columnList);
			 }
			 else if(form instanceof ViewSpecimenSummaryForm)
			 {
				 	List ls=(List)request.getAttribute("specimenIdList");
				 	if(ls!=null)
				 	{
				 		Iterator itr=ls.iterator();
				 		while (itr.hasNext()) {
					
				 			List<String> columnList2=new ArrayList<String>();
				 			Object[] whereColumnValue = {Long.valueOf((itr.next()).toString())};
				 			List ls1 =bizLogic.retrieve(objName, selectColumnName,whereColumnName,whereColumnCondition,whereColumnValue,null);
				 			columnList2=createList(ls1);
				 			cartnew.add(columnList2);
				 		}
				 	}
			 }
			
		return cartnew;
		 
	}
	
	/**
	 * @param domainObjectName name of domain object
	 * @return
	 */
	private IBizLogic getBizLogic(String domainObjectName) 
	{
		BizLogicFactory factory = BizLogicFactory.getInstance();
		IBizLogic bizLogic = factory.getBizLogic(domainObjectName);
		return bizLogic;
	}
	
	/**
	 * @param obj Specimen Object
	 * @return
	 */
	private List<String>  createList(List list)
	{
		
		
			Object[] obj1 = (Object[]) list.get(0);
			String[] cartList=new String[obj1.length];
			for(int j=0;j<obj1.length;j++)
			{
				if(obj1[j]!=null)
				{	
					if(obj1[j] instanceof Date)
					{
						cartList[j]=Utility.parseDateToString((Date)obj1[j],  edu.wustl.common.util.global.Constants.DATE_PATTERN_MM_DD_YYYY);
					}
					else
					{
						cartList[j]=obj1[j].toString();
					}
				}
				else
					cartList[j]=null;
			
		}

		return Arrays.asList(cartList);
	}
	/**
	 * @param request object of HttpServletRequest
	 */
	private void addDifferentCartViewError(HttpServletRequest request)
	{
		String target;
		ActionErrors errors = new ActionErrors();
		ActionError error = new ActionError("shoppingcart.differentViewError");
		errors.add(ActionErrors.GLOBAL_ERROR, error);
		saveErrors(request, errors);
		target = new String(edu.wustl.catissuecore.util.global.Constants.DIFFERENT_VIEW_IN_CART);
	}
	
	private int[] prepareSearchTarget()
	{
		List<Integer> target = new ArrayList<Integer>();
		System.out.println();
		target.add(new Integer(Constants.CLASS));
		target.add(new Integer(Constants.ATTRIBUTE));
		target.add(new Integer(Constants.PV));
		int[] searchTarget = new int[target.size()];
		
		for (int i = 0; i < target.size(); i++)
		{
			searchTarget[i] = ((Integer) (target.get(i))).intValue();
		}
		return searchTarget;
	}
}
