/**
 * 
 */
package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.catissuecore.querysuite.QueryShoppingCart;

/**
 * @author supriya_dankh
 * Provides all the functionalities of the shopping cart such as Add ,Delete etc.
 */
public class QueryShoppingCartBizLogic extends CatissueDefaultBizLogic
{
	private transient Logger logger = Logger.getCommonLogger(QueryShoppingCartBizLogic.class);
	/**
	 * Adds a object in the shopping cart if the object is not present in cart.
	 * 
	 * @param cart a shopping cart object preset in session.
	 * @param dataList List of data that is suppose to add in shopping cart. 
	 * @param keySet Set of checkboxs of selected records.
	 * @return int  no of records added in the cart .
	 */
		
	public int add(QueryShoppingCart cart , List<List<String>> dataList ,List<Integer> keySet)
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
			for(Integer index:keySet)
			{
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
	public int delete(QueryShoppingCart cart  ,List<Integer> keySet)
	{ 
		List<List<String>> removeList = new ArrayList<List<String>>();
		for(Integer index : keySet)
		{
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
	public List<List<String>> export(QueryShoppingCart cart,List<Integer> keySet)
	{ 
		List<List<String>> exportList = new ArrayList<List<String>>();
		exportList.add(cart.getColumnList());
		for(Integer index: keySet)
		{
			exportList.add(cart.getCart().get(index));
		}
		
	    return exportList;
	}
	

	/**
	 * Creates Entity Ids list .
	 * 
     * @param cart Shopping cart.
     * @param entityName Name of Entity.
     * @param chkBoxValues List if checkbox indices.
     * @return List of entity ids present in cart if chkBoxValues null it will return all ids else only selected ids.
	 */
	
	public Set<String> getEntityIdsList(QueryShoppingCart cart,List entityName,List<Integer>chkBoxValues)
	{
		Set<String> entityIdsList = new HashSet<String>();
	    List<Integer> entityIdsColumnIndexList = getIdsColumnIndexList(cart.getCartAttributeList(),entityName);
        List<List<String>> dataList = cart.getCart();
        if(chkBoxValues!=null)
	    {
			    for(Integer index:chkBoxValues)
			    {
					List<String> record = dataList.get(index);
					for (int i = 0; i < entityIdsColumnIndexList.size(); i++)
					{
						String data = record.get(entityIdsColumnIndexList.get(i));
						
						if(!(data.equals("")))
						  entityIdsList.add(data);
					}
			    }
	       }
	       else
	       {
	        	for (List<String> record : dataList)
	    		{
	    			for (int j = 0; j < entityIdsColumnIndexList.size(); j++)
	    			{
	    				String data = record.get(entityIdsColumnIndexList.get(j));
						if (!(data.equals("")))
	    					entityIdsList.add(data);
	    			}
	    		}
	       }
	   
	    return entityIdsList;

	}
	
	public Set<String> getEntityLabelsList(QueryShoppingCart cart,List entityName,List<Integer>chkBoxValues,String attributeName)
	{
		Set<String> entityIdsList = new HashSet<String>();
	    List<Integer> entityIdsColumnIndexList = getLabelsColumnIndexList(cart.getCartAttributeList(),entityName,attributeName);
        List<List<String>> dataList = cart.getCart();
        if(chkBoxValues!=null)
	    {
			    for(Integer index:chkBoxValues)
			    {
					List<String> record = dataList.get(index);
					for (int i = 0; i < entityIdsColumnIndexList.size(); i++)
					{
						String data = record.get(entityIdsColumnIndexList.get(i));
						
						if(!(data.equals("")))
						  entityIdsList.add(data);
					}
			    }
	       }
	       else
	       {
	        	for (List<String> record : dataList)
	    		{
	    			for (int j = 0; j < entityIdsColumnIndexList.size(); j++)
	    			{
	    				String data = record.get(entityIdsColumnIndexList.get(j));
						if (!(data.equals("")))
	    					entityIdsList.add(data);
	    			}
	    		}
	       }
	   
	    return entityIdsList;

	}
	
	/**
	 * @param entityIdsList
	 * @return
	 */
	public Set getListOfOrderItem(Set<String> entityIdsList)throws BizLogicException
	{
		 Set<String> orderIdsList = new HashSet<String>();
		 DAO dao = openDAOSession(null);	
		 try
		   {
	    	  	Iterator itr = entityIdsList.iterator();
	    	   	while(itr.hasNext())
	    	   	{
	    	   		Long specimenId = Long.parseLong(itr.next().toString());
	    	   		if(isSpecimenValidToOrder(dao,specimenId))
	    	   		{
	    	   			orderIdsList.add(specimenId.toString());
	    	   		}
	    	   		
				}
		 
		   } catch (NumberFormatException e) {
			   logger.debug(e.getMessage(), e);
			   e.printStackTrace();
		   }finally
		   {
				closeDAOSession(dao);
		   }
		 return orderIdsList;
	}
	
	
	/**
	 * To check specimen is valid or not for ordering
	 * @param dao
	 * @param specimenId
	 * @return
	 * @throws BizLogicException
	 */
	private boolean isSpecimenValidToOrder(DAO dao ,Long specimenId) throws BizLogicException
	{
		boolean isSpecimenValid = true;
		try
		{
			Specimen specimen =(Specimen) dao.retrieveById(Specimen.class.getName(), specimenId);

			if(specimen.getCollectionStatus()==null || !specimen.getCollectionStatus().equals(Constants.COLLECTION_STATUS_COLLECTED))
			{
				isSpecimenValid = false;
			}
			if(specimen.getSpecimenPosition()==null)
			{
				isSpecimenValid = false;
			}
			if(specimen.getAvailableQuantity().equals(new Double(0.0)))
			{
				isSpecimenValid = false;
			}
			if(specimen.getIsAvailable()!=null && !specimen.getIsAvailable())
			{
				isSpecimenValid = false;
			}
		}
		catch(DAOException daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, "dao.error", "");
		}
		return isSpecimenValid;
	}
	
	
	/**
	 * Creates Entity Ids column indices list .
	 * 
     * @param cartAttributeList Shopping cart attribute list.
     * @param entityName Name of Entity.
     * @return List of entity indices of entity ids present in cart.
	 */
	public List<Integer> getIdsColumnIndexList(List<AttributeInterface> cartAttributeList,List entityName)
	{
		List<Integer> idIndexList = new ArrayList<Integer>();
		int i = 0;
		for (AttributeInterface attribute : cartAttributeList)
		{
			if ((attribute.getName().equals(Constants.ID))
					&& (entityName.contains(attribute.getEntity().getName())))
			{
				idIndexList.add(new Integer(i));
			}
			i++;
		}
		
		return idIndexList;
	}
	
	/**
	 * Creates Entity Ids column indices list .
	 * 
     * @param cartAttributeList Shopping cart attribute list.
     * @param entityName Name of Entity.
     * @return List of entity indices of entity ids present in cart.
	 */
	public List<Integer> getLabelsColumnIndexList(List<AttributeInterface> cartAttributeList,List entityName,String attributeName)
	{
		List<Integer> idIndexList = new ArrayList<Integer>();
		int i = 0;
		for (AttributeInterface attribute : cartAttributeList)
		{
			if ((attribute.getName().equals(attributeName))
					&& (entityName.contains(attribute.getEntity().getName())))
			{
				idIndexList.add(new Integer(i));
			}
			i++;
		}
		
		return idIndexList;
	}
	
	/**
	 * @param oldAttributeList cart attribute list.
	 * @param attributeList new Spreadsheet attribute list.
	 * @return
	 */
	public int[] getNewAttributeListIndexArray(List<AttributeInterface> oldAttributeList,
			List<AttributeInterface> attributeList)
	{
		int[] indexArray = new int[attributeList.size()];
		if (oldAttributeList.size() == attributeList.size())
		{
			int index;

			for (int i = 0; i < attributeList.size(); i++)
			{
				index = (oldAttributeList.indexOf((AttributeInterface) attributeList.get(i)));
				if (index == -1)
				{
					indexArray = null;
					break;
				}
				else
				{
					indexArray[i] = index;
				}
			}

		}
		else
			indexArray = null;
		return indexArray;
	}

	
}
