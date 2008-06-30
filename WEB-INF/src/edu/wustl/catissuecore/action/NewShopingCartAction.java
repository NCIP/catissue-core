package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.actionForm.AliquotForm;
import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.SpecimenForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryShoppingCartBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.querysuite.QueryShoppingCart;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.dbManager.DAOException;
import gate.security.Session;
import edu.wustl.common.bizlogic.IBizLogic;

/**
 * @author chitra_garg
 *
 */
public class NewShopingCartAction extends BaseAction {
	
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws DAOException,DynamicExtensionsSystemException {
		
		List<List<String>> cartnew = new ArrayList<List<String>>();
		HttpSession session = request.getSession();
		int count=0;
		int newCartSize=0;
		int oldCartSize=0;
		QueryShoppingCart queryShoppingCart =new QueryShoppingCart();
		QueryShoppingCartBizLogic queryShoppingCartBizLogic=new QueryShoppingCartBizLogic();
		List<AttributeInterface> oldAttributeList=new ArrayList<AttributeInterface>();
		List<AttributeInterface> cartAttributeList=new ArrayList<AttributeInterface>();
		List<String> columnList=new ArrayList<String>();
		String pageOf = request.getParameter(Constants.PAGEOF);
		
		EntityManager entityManager=(EntityManager) EntityManager.getInstance();
		EntityInterface entity=(EntityInterface) entityManager.getEntityByName(new Specimen().getClass().getName());
		Collection<AttributeInterface> attributeCollection=((Entity) entity).getAttributeCollection();
	
		Iterator<AttributeInterface> attributreItr=attributeCollection.iterator();
	
		while (attributreItr.hasNext()) {
			AttributeInterface elem= attributreItr.next();
			String columnName=elem.getName().toString();
			columnList.add(columnName + " : " + "Specimen");
			cartAttributeList.add(elem);
			
		}

		if(session.getAttribute(Constants.QUERY_SHOPPING_CART)!=null)
		{
			queryShoppingCart =(QueryShoppingCart)session.getAttribute(Constants.QUERY_SHOPPING_CART);
			oldAttributeList = queryShoppingCart.getCartAttributeList();
			oldCartSize=queryShoppingCart.getCart().size();
		}
		else
		{
			queryShoppingCart.setCartAttributeList(cartAttributeList);
			queryShoppingCart.setColumnList(columnList);
			oldAttributeList=cartAttributeList;
			
		}
			cartnew=createListOfItems(form);
			
			ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
			if(errors==null)
			{
				 errors=new ActionErrors();
			}
			
			int indexArray[] = queryShoppingCartBizLogic.getNewAttributeListIndexArray(oldAttributeList, cartAttributeList);
			if (indexArray != null)
			{

				count=queryShoppingCartBizLogic.add(queryShoppingCart, cartnew, null);
				newCartSize=queryShoppingCart.getCart().size();
				
				if (count >0)
				{
					if((cartnew.size()+oldCartSize-newCartSize)>0)
					{
						ActionError actionError =new ActionError("shoppingcart.duplicateObjError",count,cartnew.size()+oldCartSize-newCartSize);
						errors.add(ActionErrors.GLOBAL_ERROR, actionError);
						saveErrors(request, errors);
					}
					else
					{
						ActionError actionError =new ActionError("shoppingCart.addMessage",cartnew.size());
						errors.add(ActionErrors.GLOBAL_ERROR, actionError);
						saveErrors(request, errors);
					}
					
				}
				else if(count ==0)
				{
					if((cartnew.size()+oldCartSize-newCartSize)>0)
					{
						ActionError actionError =new ActionError("shoppingcart.duplicateObjError",count,cartnew.size()+oldCartSize-newCartSize);
						errors.add(ActionErrors.GLOBAL_ERROR, actionError);
						saveErrors(request, errors);
					}
				}
	
			}else{
				addDifferentCartViewError(request);
			}
		
		session.setAttribute(Constants.QUERY_SHOPPING_CART,queryShoppingCart);
		return mapping.findForward(pageOf); 
	}
	
	/**
	 * @param form  object of ActionForm
	 * @return cart
	 * @throws DAOException Database related Exception
	 */
	private List<List<String>> createListOfItems(ActionForm form) throws DAOException {
		String objName=new Specimen().getClass().getName();
		IBizLogic bizLogic=getBizLogic(objName);
		Object searchObjects = null;
		AliquotForm aliquotForm = new AliquotForm();
		SpecimenForm specimenForm=new SpecimenForm();
		
		List<List<String>> cartnew = new ArrayList<List<String>>();
		List<String> columnList=new ArrayList<String>();
	
			 if(form instanceof AliquotForm)
			{
				aliquotForm = (AliquotForm) form;
				List<AbstractDomainObject> specimenList = aliquotForm.getSpecimenList();
				Iterator<AbstractDomainObject> it=specimenList.iterator();
			
				while (it.hasNext()) {
					List<String> columnList2=new ArrayList<String>();
					Specimen specimen=  (Specimen)it.next();
					searchObjects =bizLogic.retrieve(objName, specimen.getId());
					columnList2=createList(searchObjects);
					cartnew.add(columnList2);
				}
			}
			 else
			 {
				 	specimenForm = (SpecimenForm) form;
				 	searchObjects =bizLogic.retrieve(objName, specimenForm.getId());
				 	columnList=createList(searchObjects);
				 	cartnew.add(columnList);
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
	private List<String>  createList(Object obj)
	{
		Specimen specimen=(Specimen)obj;
		List<String> cartList=new ArrayList<String>();
		cartList.add(specimen.getCollectionStatus());
		if(specimen.getSpecimenPosition()!=null)
		{
			cartList.add(specimen.getSpecimenPosition().getPositionDimensionTwo().toString());
		}
		else 
		{
			cartList.add(null);
		}
		cartList.add(String.valueOf(specimen.getId()));
		cartList.add(specimen.getInitialQuantity().toString());
		cartList.add(specimen.getAvailableQuantity().toString());
		if(specimen.getSpecimenPosition()!=null)
		{
			cartList.add(specimen.getSpecimenPosition().getPositionDimensionOne().toString());
		}
		else 
		{
			cartList.add(null);
		}
		cartList.add(specimen.getComment());
		cartList.add(specimen.getLineage());
		cartList.add(specimen.getActivityStatus());
		cartList.add(specimen.getLabel());
		cartList.add(specimen.getSpecimenType());
		if(specimen.getCreatedOn()!=null)
		{
			cartList.add(specimen.getCreatedOn().toString());
		}
		else 
		{
			cartList.add(null);
		}
		cartList.add(specimen.getBarcode());
		cartList.add(null);//specimenForm.getIsCollectionProtocolRequirement().toString());//TO DO is cp reuired
		cartList.add(specimen.getAvailable().toString());//TO DO is available
		cartList.add(specimen.getPathologicalStatus());
		return cartList;
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
		target = new String(Constants.DIFFERENT_VIEW_IN_CART);
	}
}
