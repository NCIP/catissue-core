/**
 * 
 */
package edu.wustl.catissuecore.bizlogic.querysuite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.catissuecore.querysuite.QueryShoppingCart;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.querysuite.queryobject.impl.metadata.QueryOutputTreeAttributeMetadata;
import edu.wustl.common.querysuite.queryobject.impl.metadata.SelectedColumnsMetadata;

/**
 * @author supriya_dankh
 * Provides all the functionalities of the shopping cart such as Add ,Delete etc.
 */
public class QueryShoppingCartBizLogic 
{
	
	/**
	 * Adds a object in the shopping cart if the object is not present in cart.
	 * 
	 * @param cart a shopping cart object preset in session.
	 * @param dataList List of data that is suppose to add in shopping cart. 
	 * @param keySet Set of checkboxs of selected records.
	 * @return int  no of records added in the cart .
	 */
		
	public int add(QueryShoppingCart cart , List<List<String>> dataList ,Set keySet)
	{   
		int addCount = 0 ;
		
		if(keySet==null)
		{
			for (int i = 0; i < dataList.size(); i++) 
			{				
				if(!(cart.getCart().contains(dataList.get(i))))
				{
					cart.getCart().add(dataList.get(i));
					addCount++;
				}
				
			 }
		}
			
		else 
		{
			for(Iterator itr = keySet.iterator();itr.hasNext();)
			{
				Object obj = itr.next();
				int index = getIndex(obj);
				if(!(cart.getCart().contains(dataList.get(index))))
				{
					cart.getCart().add(dataList.get(index));
					addCount++;
				}
						    			
			}
		}
					
		return addCount;
		
	}
	
	/**
	 * Delete a object in the shopping cart.
	 * 
	 * @param cart a shopping cart object preset in session.
     * @param keySet Set of checkboxs of selected records to delete.
	 */
	public int delete(QueryShoppingCart cart  ,Set keySet)
	{ 
		List<List<String>> removeList = new ArrayList<List<String>>();
		for(Iterator itr = keySet.iterator();itr.hasNext();)
		{
			Object obj = itr.next();
			int index = getIndex(obj);
			removeList.add(cart.getCart().get(index));
	    }
		cart.getCart().removeAll(removeList);
		if(cart.getCart().size()==0)
		{
			cart.setCartAttributeList(null);
			cart.setColumnList(null);
		}
		return removeList.size();
	}
	
	/**
	 * Creates a List of records that user wants to export in a csv file.
	 * 
	 * @param cart a shopping cart object preset in session.
     * @param keySet Set of checkboxs of selected records to export.
	 */
	public List<List<String>> export(QueryShoppingCart cart,Set keySet)
	{ 
		List<List<String>> exportList = new ArrayList<List<String>>();
		exportList.add(cart.getColumnList());
		for(Iterator itr = keySet.iterator();itr.hasNext();)
		{
			Object obj = itr.next();
			int index = getIndex(obj);
			exportList.add(cart.getCart().get(index));
		}
		
	    return exportList;
	}
	
	/**
	 * Separates a index of checkbox present in object obj.
	 * 
     * @param obj.
     * @return index.
	 */
	public int getIndex(Object obj)
	{
		String str = obj.toString();
    	StringTokenizer strTokens = new StringTokenizer(str,"_");
    	strTokens.nextToken();
    	int index = Integer.parseInt(strTokens.nextToken());
		return index;
	}
	
	/**
	 * Creates specimen order list .
	 * 
     * @param dataList List of cart data.
     * @param keySet list of selected checkbox values.
     * @param specimenIdIndex array of specimen id indices in attributelist.
     * @return List of specimen ids present in cart.
	 */
	public List<String> createSpecimenOrderingList(List<List<String>>dataList,Set keySet,List specimenIdIndex)
	{
	    List<String> specimenIdsList = new ArrayList<String>();
		for(Iterator itr = keySet.iterator();itr.hasNext();)
		{
			Object obj = itr.next();
			int index = getIndex(obj);
			List<String> record = dataList.get(index);
			for (int i = 0; i < specimenIdIndex.size(); i++)
			{
				if(!(specimenIdsList.contains(record.get((Integer)specimenIdIndex.get(i)))))
				  specimenIdsList.add(record.get((Integer)specimenIdIndex.get(i)));
			}
		}
		return specimenIdsList;
	}
	
}
