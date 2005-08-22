/**
 * <p>Title: ShoppingCart Class>
 * <p>Description:  This class models the shopping cart of the user. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Apr 7, 2005
 */
package edu.wustl.catissuecore.query;

import java.util.Hashtable;

import edu.wustl.catissuecore.domain.AbstractDomainObject;

/**
 * This class models the shopping cart of the user.
 * @author aniruddha_phadnis
 */
public class ShoppingCart
{
	private static final long serialVersionUID = 1234567890L;
	
	/**
     * A shopping cart of the user.
     */
	protected Hashtable cart;
	
	//Default Constructor
	public ShoppingCart()
	{
		cart = new Hashtable();
	}
	
	/**
     * Returns a shopping cart of the user.
     * @return Hashtable a shopping cart of the user.
     * @see #setCart(Hashtable)
     */
	public Hashtable getCart()
	{
		return cart;
	}
	
	/**
     * Sets a shopping cart of the user.
     * @param cart a shopping cart of the user.
     * @see #getCart()
     */
	public void setCart(Hashtable cart)
	{
		this.cart = cart;
	}
	
	/**
     * Adds an object to the cart.
     * @param obj an object to be added to the cart.
     */
	public void add(AbstractDomainObject obj)
	{
		if(obj != null)
		{
			String key = String.valueOf(obj.getSystemIdentifier());
			cart.put(key,obj);
		}
	}
	
	/**
     * Removes an object from the cart.
     * @param key key of an object to be removed.
     */
	public void remove(String key)
	{
		if(key != null)
		{
			cart.remove(key);
		}
	}
}