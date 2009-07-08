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

	/**
	 * List that will store attribute list of records added to shopping cart.
	 */
	private List<AttributeInterface> cartAttributeList;

	/**
	 * List of columns that should be displayed on cart.
	 */
	private List<String> columnList;

	/**
	 * List that will store actual data of records added to cart.
	 */
	private List<List<String>> cart;

	/**
	 * Constructor.
	 */
	public QueryShoppingCart()
	{
		this.cartAttributeList = new ArrayList<AttributeInterface>();
		this.cart = new ArrayList<List<String>>();
		this.columnList = new ArrayList<String>();
	}

	/**
	 * Returns list of attributes corrosponding to shopping cart of the user.
	 * @return List Attribute List.
	 * @see #setCartAttributeList(List)
	 */
	public List<AttributeInterface> getCartAttributeList()
	{
		return this.cartAttributeList;
	}

	/**
	 * This method checks cart is empty or not.
	 * @return empty or not.
	 */
	public boolean isEmpty()
	{
		return this.cart == null || this.cart.isEmpty();
	}

	/**
	 * Sets attribute list.
	 * @param cartAttributeList attribute list.
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
		return this.cart;
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

	/**
	 * This method gets Column List.
	 * @return columnList
	 */
	public List<String> getColumnList()
	{
		return this.columnList;
	}

	/**
	 * This method sets Column List.
	 * @param columnList column List.
	 */
	public void setColumnList(List<String> columnList)
	{
		this.columnList = columnList;
	}

}
