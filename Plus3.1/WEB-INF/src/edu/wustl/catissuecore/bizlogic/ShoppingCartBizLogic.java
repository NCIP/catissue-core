/**
 * <p>
 * Title: ShoppingCartHDAO Class>
 * <p>
 * Description: ShoppingCartBizLogic provides the shopping cart functionality.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00 Created on Jul 21, 2005
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

	/**
	 * Logger object. 
	 */
	private transient final Logger logger = Logger.getCommonLogger(ShoppingCartBizLogic.class);

	/**
	 * @param cart : cart 
	 * @param obj : obj
	 * @throws BizLogicException : BizLogicException
	 */
	public void add(ShoppingCart cart, Object obj[]) throws BizLogicException
	{
		try
		{
			if (cart != null && obj != null)
			{
				for (final Object element : obj)
				{
					final Object object = this.retrieve(Specimen.class.getName(),
							new Long(element.toString()));

					if (object != null)
					{
						final Specimen specimen = (Specimen) object;
						cart.add(specimen);
					}
				}
			}
		}
		catch (final ApplicationException appExp)
		{
			this.logger.error(appExp.getMessage(), appExp);
			appExp.printStackTrace();
			throw this
					.getBizLogicException(appExp, appExp.getErrorKeyName(), appExp.getMsgValues());
		}

	}

	/**
	 * @param cart : cart
	 * @param obj : obj
	 * @return ShoppingCart
	 */
	public ShoppingCart delete(ShoppingCart cart, Object obj[])
	{
		if (cart != null && obj != null)
		{
			for (final Object element : obj)
			{
				final String str = element.toString();
				final int index = str.indexOf("_") + 1;
				/**
				 * Name: Vijay Pande Key was not generated properly to remove
				 * object from map. Objects re stored in the map on the basis of
				 * the objectId hence id is retrieved from from String and set
				 * it as key.
				 */
				final String key = (str.substring(index).trim());
				cart.remove(key);
			}
		}

		return cart;
	}

	// public void deleteCartFile(String fileName)
	// {
	// try
	// {
	// fileName = fileName + ".txt";
	// File f = new File(Variables.catissueHome,fileName);
	// f.delete();
	// }
	// catch(Exception e)
	// {
	// Logger.out.error(e.getMessage(), e);
	// }
	// }
	/**
	 * @param cart : cart
	 * @param fileName : fileName
	 * @return List
	 */
	public List export(ShoppingCart cart, Object obj[], String fileName)
	{
		List shoppingCartList = null;

		if (cart != null)
		{
			final Hashtable table = cart.getCart();

			if (table != null && table.size() != 0)
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

				for (final Object element : obj)
				{
					rowList = new ArrayList();

					final String str = element.toString();
					final int index = str.indexOf("_") + 1;
					// resolved bug# 4385
					// int index1 = str.indexOf(" |");
					final String key = str.substring(index).trim();
					final Specimen specimen = (Specimen) table.get(key);

					rowList.add(String.valueOf(specimen.getId()));
					rowList.add(specimen.getClassName());

					if (specimen.getSpecimenType() != null)
					{
						rowList.add(specimen.getSpecimenType());
					}
					else
					{
						rowList.add("");
					}

					rowList.add(specimen.getSpecimenCharacteristics().getTissueSite());
					rowList.add(specimen.getSpecimenCharacteristics().getTissueSide());

					// Bug #4554 :kalpana
					rowList.add(specimen.getPathologicalStatus());
					// end of bug #4554
					// rowList.add(specimen.getSpecimenCharacteristics().
					// getPathologicalStatus());

					shoppingCartList.add(rowList);
				}
			}
		}

		return shoppingCartList;
	}
}