/**
 * <p>Title: ShoppingCartHDAO Class>
 * <p>Description:	ShoppingCartBizLogic provides the shopping cart functionality.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 21, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.simplequery.query.ShoppingCart;

/**
 * ShoppingCartBizLogic provides the shopping cart functionality.
 * @author aniruddha_phadnis
 */
public class ShoppingCartBizLogic extends CatissueDefaultBizLogic
{
	private transient Logger logger = Logger.getCommonLogger(ShoppingCartBizLogic.class);
	public void add(ShoppingCart cart,Object obj[]) throws BizLogicException
    {
		try
		{
			if(cart!=null && obj!=null)
			{
				for(int i=0;i<obj.length;i++)
				{
					Object object = retrieve(Specimen.class.getName(), new Long(obj[i].toString()));

					if (object != null)
					{
						Specimen specimen = (Specimen) object;
						cart.add(specimen);
					}
				}
			}
		}
		catch(ApplicationException appExp)
		{
			logger.debug(appExp.getMessage(), appExp);
			throw getBizLogicException(appExp, appExp.getErrorKeyName(),appExp.getMsgValues());
		}

	}
    
	public ShoppingCart delete(ShoppingCart cart,Object obj[])
    {
		if(cart!=null && obj!=null)
		{
			for(int i=0;i<obj.length;i++)
			{
	        	String str = obj[i].toString();
	        	int index = str.indexOf("_") + 1;
	        	/**
	        	 * Name: Vijay Pande
	        	 * Key was not generated properly to remove object from map.
	        	 * Objects re stored in the map on the basis of the objectId hence id is retrieved from from String and set it as key.
	        	 */
	        	String key=(str.substring(index).trim());
	        	cart.remove(key);
			}
		}
		
		return cart;
    }
    
//	public void deleteCartFile(String fileName)
//    {
//		try
//		{
//			fileName = fileName + ".txt";
//			File f = new File(Variables.catissueHome,fileName);
//			f.delete();
//		}
//		catch(Exception e)
//		{
//			 Logger.out.error(e.getMessage(), e);
//		}
//	}
	
	public List export(ShoppingCart cart,Object obj[],String fileName)
    {
		List shoppingCartList = null;
		
		if(cart != null)
		{
			Hashtable table = cart.getCart();
			
			if(table != null && table.size() != 0)
			{
				shoppingCartList = new ArrayList();
				
				List rowList = new ArrayList();
				
				rowList.add("Identifier");
				rowList.add("Type");
				rowList.add("Subtype");
				rowList.add("Tissue Site");
				rowList.add("Tissue Side");
				rowList.add("Pathological Status");
				
				shoppingCartList.add(rowList);
				
				for(int i=0;i<obj.length;i++)
				{
					rowList = new ArrayList();
					
					String str = obj[i].toString();
					int index = str.indexOf("_") + 1;
					//resolved bug# 4385
					//int index1 = str.indexOf(" |");
					String key = str.substring(index).trim();
					Specimen specimen = (Specimen)table.get(key);
					
					rowList.add(String.valueOf(specimen.getId()));
					rowList.add(specimen.getClassName());
					
					if(specimen.getSpecimenType() != null)
						rowList.add(specimen.getSpecimenType());
					else
						rowList.add("");
					
					rowList.add(specimen.getSpecimenCharacteristics().getTissueSite());
					rowList.add(specimen.getSpecimenCharacteristics().getTissueSide());
					
					//Bug #4554 :kalpana
					rowList.add(specimen.getPathologicalStatus());
					//end of bug #4554
//					rowList.add(specimen.getSpecimenCharacteristics().getPathologicalStatus());
					
					shoppingCartList.add(rowList);
				}
			}
		}
		
		return shoppingCartList;
	}
}