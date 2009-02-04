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
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.query.ShoppingCart;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * ShoppingCartBizLogic provides the shopping cart functionality.
 * @author aniruddha_phadnis
 */
public class ShoppingCartBizLogic extends DefaultBizLogic
{
	public void add(ShoppingCart cart,Object obj[]) throws DAOException,BizLogicException  
    {
		if(cart!=null && obj!=null)
		{
			List specimenObjectList = new ArrayList();
			for(int i=0;i<obj.length;i++)
			{
				List list = retrieve(Specimen.class.getName(),"id",obj[i].toString());
				
				if (list!=null && list.size()!= 0)
				{
					Specimen specimen = (Specimen)list.get(0);
					specimenObjectList.add(specimen);
				}
			}
			cart.addToMyList(specimenObjectList);
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
	        	
	        	cart.remove(str.substring(index).trim());
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
		        	String key = str.substring(index).trim();
					Specimen specimen = (Specimen)table.get(key);
					
					rowList.add(String.valueOf(specimen.getId()));
					rowList.add(specimen.getClassName());
					
					if(specimen.getType() != null)
						rowList.add(specimen.getType());
					else
						rowList.add("");
					
					rowList.add(specimen.getSpecimenCharacteristics().getTissueSite());
					rowList.add(specimen.getSpecimenCharacteristics().getTissueSide());
//					rowList.add(specimen.getSpecimenCharacteristics().getPathologicalStatus());
					
					shoppingCartList.add(rowList);
				}
			}
		}
		
		return shoppingCartList;
	}
}