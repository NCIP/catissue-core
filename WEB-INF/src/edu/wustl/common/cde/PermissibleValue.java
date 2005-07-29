/*
 * Created on May 26, 2005
 *
 * Title : PermissibleValue Interface</p>
 * <p> This interface defines methods that can be used to access the information
 * about a PermissibleValue</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * 
 */
package edu.wustl.common.cde;

import java.util.Set;

/**
 * @author mandar_deshmukh
 *
 * <p> This interface defines methods that can be used to access the information
 * about a PermissibleValue</p>
 *
 */
public interface PermissibleValue 
{

	/**
	 * This method is used to get the conceptid of the PermissibleValue 
	 * @return returns a String object that contains the conceptid of the PermissibleValue 
	 */	
	String getConceptid();

	/**
	 * This method is used to get the Value of the PermissibleValue 
	 * @return returns a String object that contains the Value of the PermissibleValue 
	 */	
	String getValue();
	
	/**
	 * This method is used to get the defination of the PermissibleValue 
	 * @return returns a String object that contains the defination of the PermissibleValue 
	 */	
	String getDefination();
	
	/**
	 * This method is used to get the parent permissiblevalue of the PermissibleValue 
	 * @return returns a PermissibleValue object that contains the parent permissiblevalue 
	 * of the PermissibleValue 
	 */	
	PermissibleValue getParentPermissibleValue();
	
	/**
	 * This method is used to get the sub or child permissiblevalues of the PermissibleValue 
	 * @return returns a Set object that contains the sub permissible values 
	 * of the PermissibleValue 
	 */	
	Set getSubPermissibleValues();
	
} // PermissibleValue interface
