package edu.wustl.catissuecore.query;

import java.util.HashSet;





/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */
public abstract class ConditionsImpl {

	public ConditionsImpl(){

	}

	/**
	 * Returns String representation of itself 
	 * @param tableSufix sufix for tables
	 * @return string representation
	 */
	public abstract String getString(int tableSufix);
	
//	//method that returns object names of conditions
//	//to form "FROM" part of query
//	public abstract HashSet getConditionObjects();

	public abstract HashSet getQueryObjects();
}