/**
 * <p>Title: ShoppingCartAction Class>
 * <p>Description:	This class initializes the fields of ShoppingCart.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 18, 2005
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ShoppingCartForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.ShoppingCartBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.query.ShoppingCart;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;

public class ShoppingCartAction extends Action//BaseAction
{
    /**
     * Overrides the execute method of Action class.
     * Initializes the various fields in ShoppingCart.jsp Page.
     * */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        //Gets the value of the operation parameter.
        String operation = (String)request.getParameter(Constants.OPERATION);
        
        HttpSession session = request.getSession(true);
        ShoppingCart cart = (ShoppingCart)session.getAttribute(Constants.SHOPPING_CART);
        ShoppingCartBizLogic bizLogic = (ShoppingCartBizLogic)BizLogicFactory.getBizLogic(Constants.SHOPPING_CART_FORM_ID);
        ShoppingCartForm shopForm = (ShoppingCartForm)form;
        
        if(cart == null)
        {
        	cart = new ShoppingCart();
        }
        
        if(operation == null)
        {
        	try
			{
        		List specimenList = bizLogic.retrieve(Specimen.class.getName());
        		Iterator it = specimenList.iterator();
        		
        		while(it.hasNext())
        		{
        			Specimen specimen = (Specimen)it.next();
        			cart.add(specimen);
        		}
        		
        		session.setAttribute(Constants.SHOPPING_CART,cart);
			}
        	catch(Exception e)
			{
        		e.printStackTrace();
			}
        }
        else
        {
	        if(operation.equals(Constants.ADD))
	        {
	        	//bizLogic.add(cart);
	        }
	        else if(operation.equals(Constants.DELETE))
	        {
	        	Map map = shopForm.getValues();
	        	Object obj[] = map.keySet().toArray();
	        	
	        	session.setAttribute(Constants.SHOPPING_CART,bizLogic.delete(cart,obj));
	        }
	        else if(operation.equals(Constants.EXPORT))
	        {
	        	String fileName = session.getId() + ".txt";
	        	bizLogic.export(cart,Variables.catissueHome,fileName);
	        	String path = "/" + fileName;
	        	return new ActionForward(path);
	        }
        }
        
        //Sets the operation attribute to be used in the Add/Edit Shopping Cart Page. 
        request.setAttribute(Constants.OPERATION, operation);
        
        return mapping.findForward(Constants.SUCCESS);
    }
}