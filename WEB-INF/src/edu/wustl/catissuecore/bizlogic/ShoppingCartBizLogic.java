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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.query.ShoppingCart;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * ShoppingCartBizLogic provides the shopping cart functionality.
 * @author aniruddha_phadnis
 */
public class ShoppingCartBizLogic extends DefaultBizLogic
{
	public void add(ShoppingCart cart,Object obj[]) throws DAOException
    {
		if(cart!=null && obj!=null)
		{
			for(int i=0;i<obj.length;i++)
			{
				List list = retrieve(Specimen.class.getName(),"systemIdentifier",obj[i].toString());
				
				if (list!=null && list.size()!= 0)
				{
					Specimen specimen = (Specimen)list.get(0);
					cart.add(specimen);
				}
			}
		}
    }
    
	public ShoppingCart delete(ShoppingCart cart,Object obj[])
    {
		if(cart!=null && obj!=null)
		{
			for(int i=0;i<obj.length;i++)
			{
	        	String str = obj[i].toString();
	        	int index = str.indexOf(":") + 1;
	        	
	        	cart.remove(str.substring(index));
			}
		}
		
		return cart;
    }
    
	public void deleteCartFile(String fileName)
    {
		try
		{
			fileName = fileName + ".txt";
			File f = new File(Variables.catissueHome,fileName);
			f.delete();
		}
		catch(Exception e)
		{
			 Logger.out.error(e.getMessage(), e);
		}
	}
	
	public void export(ShoppingCart cart,String directory,String fileName)
    {
		FileWriter writer = null;
		try
		{
			File file = new File(directory,fileName);
			writer = new FileWriter(file);
			
			if(cart != null)
			{
				Hashtable table = cart.getCart();
				
				if(table != null && table.size() != 0)
				{
					Enumeration enum = table.keys();
					String fieldSeparator = "\t";
					int i=1;
					
					writer.write("#");
					writer.write(fieldSeparator);
					writer.write("IDENTIFIER");
					writer.write(fieldSeparator);
					writer.write("TYPE");
					writer.write(fieldSeparator);
					writer.write("SUBTYPE");
					writer.write(fieldSeparator);
					writer.write("TISSUE SITE");
					writer.write(fieldSeparator);
					writer.write("TISSUE SIDE");
					writer.write(fieldSeparator);
					writer.write("PATHOLOGICAL STATUS");
					writer.write(fieldSeparator);
					
					while(enum.hasMoreElements())
					{
						String key = (String)enum.nextElement();
						Specimen specimen = (Specimen)table.get(key);
						
						writer.write("\n");
						writer.write(i + ".");
						writer.write(fieldSeparator);
						writer.write(String.valueOf(specimen.getSystemIdentifier()));
						writer.write(fieldSeparator);
						writer.write(specimen.getClassName());
						writer.write(fieldSeparator);
						writer.write(specimen.getType());
						writer.write(fieldSeparator);
						writer.write(specimen.getSpecimenCharacteristics().getTissueSite());
						writer.write(fieldSeparator);
						writer.write(specimen.getSpecimenCharacteristics().getTissueSide());
						writer.write(fieldSeparator);
						writer.write(specimen.getSpecimenCharacteristics().getPathologicalStatus());
						i++;
					}
				}
			}
		}
		catch(IOException ie)
		{
			 Logger.out.error(ie.getMessage(), ie);
		}
		catch(Exception e)
		{
			 Logger.out.error(e.getMessage(), e);
		}
		finally
		{
			try
			{
				writer.close();
			}
			catch(IOException ioe)
			{
				 Logger.out.error(ioe.getMessage(), ioe);
			}
		}
	}
}