/**
 * 
 */
package edu.wustl.catissuecore.querysuite;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * @author supriya_dankh
 * This class models the query wizard shopping cart of user. 
 */
public class QueryShoppingCart 
{
	//List that will store attribute list of records added to shopping cart.
	List<AttributeInterface> cartAttributeList;
	
	//List of columns that should be displayed on cart.
	List<String> columnList;
	
	//List that will store actual data of records added to cart.
	List<List<String>> cart;
	
	public QueryShoppingCart()
	{
		cartAttributeList = new ArrayList<AttributeInterface>();
		cart = new ArrayList<List<String>>();
		columnList = new ArrayList<String>();
	}

	/**
     * Returns list of atrributes currosponding to shopping cart of the user.
     * @return List Attribute List.
     * @see #setCartAttributeList(List)
     */
	public List<AttributeInterface> getCartAttributeList() 
	{
		return cartAttributeList;
	}

	public boolean isEmpty()
	{
		return cart==null || cart.isEmpty();
	}
	/**
     * Sets atrribute list.
     * @param cartAttributeList atrribute list.
     * @see #getCartAttributeList()
     */
	public void setCartAttributeList(List<AttributeInterface> cartAttributeList) 
	{
		this.cartAttributeList = cartAttributeList;
	}

	/**
     * Returns list a shopping cart of the user.
     * @return List a shopping cart.
     * @see #setCart(List)
     */
	public List<List<String>> getCart() 
	{
		return cart;
	}

	/**
     * Sets a shopping cart of the user.
     * @param cart a shopping cart of the user.
     * @see #getCart()
     */
	public void setCart(List<List<String>> cart) 
	{
		this.cart = cart;
	}

	public List<String> getColumnList() 
	{
		return columnList;
	}

	public void setColumnList(List<String> columnList)
	{
		this.columnList = columnList;
	} 
	
	

}
