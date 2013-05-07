
package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.querysuite.QueryShoppingCart;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

/**
 * @author supriya_dankh Provides all the functionalities of the shopping cart
 *         such as Add ,Delete etc.
 */
public class QueryShoppingCartBizLogic extends CatissueDefaultBizLogic
{

	private transient final Logger logger = Logger.getCommonLogger(QueryShoppingCartBizLogic.class);

	/**
	 * @param datafromSelectedList : datafromSelectedList
	 * @return boolean
	 */
	public boolean checkIfHashed(List<String> datafromSelectedList)
	{
		for (final String listItem : datafromSelectedList)
		{
			if (listItem != Constants.HASHED_OUT)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @param cart : cart
	 * @param dataList : dataList
	 * @param keySet : keySet
	 * @return int
	 */
	public int add(QueryShoppingCart cart, List<List<String>> dataList, List<Integer> keySet)
	{
		int addCount = 0;

		if (keySet == null)
		{
			for (int i = 0; i < dataList.size(); i++)
			{
				if (!(cart.getCart().contains(dataList.get(i))))
				{
					cart.getCart().add(dataList.get(i));
					addCount++;
				}
			}
		}
		else
		{
			boolean dataIsHashed = false;
			for (final Integer index : keySet)
			{
				dataIsHashed = this.checkIfHashed(dataList.get(index));
				if ((!(cart.getCart().contains(dataList.get(index)))) && dataIsHashed)
				{
					cart.getCart().add(dataList.get(index));
					addCount++;
				}
				dataIsHashed = false;
			}
		}
		return addCount;
	}

	/**
	 * Delete a object in the shopping cart.
	 * @return int
	 * @param cart
	 *            a shopping cart object preset in session.
	 * @param keySet
	 *            Set of checkboxs of selected records to delete.
	 */
	public int delete(QueryShoppingCart cart, List<Integer> keySet)
	{
		final List<List<String>> removeList = new ArrayList<List<String>>();
		for (final Integer index : keySet)
		{
			removeList.add(cart.getCart().get(index));
		}
		cart.getCart().removeAll(removeList);
		if (cart.getCart().size() == 0)
		{
			cart.setCartAttributeList(null);
			cart.setColumnList(null);
		}
		return removeList.size();
	}

	/**
	 * Creates a List of records that user wants to export in a csv file.
	 * @return List
	 * @param cart
	 *            a shopping cart object preset in session.
	 * @param keySet
	 *            Set of checkboxs of selected records to export.
	 */
	public List<List<String>> export(QueryShoppingCart cart, List<Integer> keySet)
	{
		final List<List<String>> exportList = new ArrayList<List<String>>();
		exportList.add(cart.getColumnList());
		for (final Integer index : keySet)
		{
			exportList.add(cart.getCart().get(index));
		}

		return exportList;
	}

	/**
	 * Creates Entity Ids list.
	 * @return Set
	 * @param cart
	 *            Shopping cart.
	 * @param entityName
	 *            Name of Entity.
	 * @param chkBoxValues
	 *            List if checkbox indices.
	 */

	public Set<String> getEntityIdsList(QueryShoppingCart cart, List entityName,
			List<Integer> chkBoxValues)
	{
		final List<Integer> entityIdsColumnIndexList = this.getIdsColumnIndexList(cart
				.getCartAttributeList(), entityName);
		return this.getEntityList(cart, chkBoxValues, entityIdsColumnIndexList);
	}

	/**
	 * This method gets Entity list.
	 * @param cart QueryShoppingCart object
	 * @param chkBoxValues list of chkBoxValues
	 * @param entityIdsColumnIndexList list of entity IdsColumn Index.
	 * @return entityIdsList
	 */
	private Set<String> getEntityList(QueryShoppingCart cart, List<Integer> chkBoxValues,
			final List<Integer> entityIdsColumnIndexList)
	{
		final Set<String> entityIdsList = new LinkedHashSet<String>();
		final List<List<String>> dataList = cart.getCart();
		if (chkBoxValues != null)
		{
			for (final Integer index : chkBoxValues)
			{
				final List<String> record = dataList.get(index);
				for (int i = 0; i < entityIdsColumnIndexList.size(); i++)
				{
					final String data = record.get(entityIdsColumnIndexList.get(i));

					if (!(data.equals("")))
					{
						entityIdsList.add(data);
					}
				}
			}
		}
		else
		{
			for (final List<String> record : dataList)
			{
				for (int j = 0; j < entityIdsColumnIndexList.size(); j++)
				{
					final String data = record.get(entityIdsColumnIndexList.get(j));
					if (!(data.equals("")))
					{
						entityIdsList.add(data);
					}
				}
			}
		}

		return entityIdsList;
	}

	/**
	 *
	 * @param cart : cart
	 * @param entityName : entityName
	 * @param chkBoxValues : chkBoxValues
	 * @param attributeName : attributeName
	 * @return Set
	 */
	public Set<String> getEntityLabelsList(QueryShoppingCart cart, List entityName,
			List<Integer> chkBoxValues, String attributeName)
	{
		final List<Integer> entityIdsColumnIndexList = this.getLabelsColumnIndexList(cart
				.getCartAttributeList(), entityName, attributeName);
		return this.getEntityList(cart, chkBoxValues, entityIdsColumnIndexList);

	}

	/**
	 * @param entityIdsList : entityIdsList
	 * @return Set
	 * @throws BizLogicException BizLogic Exception
	 */
	public Set getListOfOrderItem(Set<String> entityIdsList) throws BizLogicException
	{
		final Set<String> orderIdsList = new LinkedHashSet<String>();
		final DAO dao = this.openDAOSession(null);
		try
		{
			final Iterator itr = entityIdsList.iterator();
			while (itr.hasNext())
			{
				final Long specimenId = Long.parseLong(itr.next().toString());
				if (this.isSpecimenValidToOrder(dao, specimenId))
				{
					orderIdsList.add(specimenId.toString());
				}

			}

		}
		catch (final NumberFormatException e)
		{
			this.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		finally
		{
			this.closeDAOSession(dao);
		}
		return orderIdsList;
	}

	/**
	 * To check specimen is valid or not for ordering
	 *
	 * @param dao : dao
	 * @param specimenId : specimenId
	 * @return boolean
	 * @throws BizLogicException : BizLogicException
	 */
	private boolean isSpecimenValidToOrder(DAO dao, Long specimenId) throws BizLogicException
	{
		boolean isSpecimenValid = true;
		try
		{
			final Specimen specimen = (Specimen) dao.retrieveById(Specimen.class.getName(),
					specimenId);

			if (specimen.getCollectionStatus() == null
					|| !specimen.getCollectionStatus()
							.equals(Constants.COLLECTION_STATUS_COLLECTED))
			{
				isSpecimenValid = false;
			}
			if (specimen.getSpecimenPosition() == null)
			{
				isSpecimenValid = false;
			}
			if (specimen.getAvailableQuantity().equals(new Double(0.0)))
			{
				isSpecimenValid = false;
			}
			if (specimen.getIsAvailable() != null && !specimen.getIsAvailable())
			{
				isSpecimenValid = false;
			}
		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return isSpecimenValid;
	}

	/**
	 * Creates Entity Ids column indices list .
	 *
	 * @param cartAttributeList
	 *            Shopping cart attribute list.
	 * @param entityName
	 *            Name of Entity.
	 * @return List of entity indices of entity ids present in cart.
	 */
	public List<Integer> getIdsColumnIndexList(List<AttributeInterface> cartAttributeList,
			List entityName)
	{
		final List<Integer> idIndexList = new ArrayList<Integer>();
		int counter = 0;
		for (final AttributeInterface attribute : cartAttributeList)
		{
			if ((attribute.getName().equals(Constants.ID))
					&& (entityName.contains(attribute.getEntity().getName())))
			{
				idIndexList.add(new Integer(counter));
			}
			counter++;
		}

		return idIndexList;
	}

	/**
	 * Creates Entity Ids column indices list.
	 * @param attributeName : attributeName
	 * @param cartAttributeList
	 *            Shopping cart attribute list.
	 * @param entityName
	 *            Name of Entity.
	 * @return List of entity indices of entity ids present in cart.
	 */
	public List<Integer> getLabelsColumnIndexList(List<AttributeInterface> cartAttributeList,
			List entityName, String attributeName)
	{
		final List<Integer> idIndexList = new ArrayList<Integer>();
		int counter = 0;
		for (final AttributeInterface attribute : cartAttributeList)
		{
			if ((attribute.getName().equals(attributeName))
					&& (entityName.contains(attribute.getEntity().getName())))
			{
				idIndexList.add(new Integer(counter));
			}
			counter++;
		}

		return idIndexList;
	}

	/**
	 * @param oldAttributeList
	 *            cart attribute list.
	 * @param attributeList
	 *            new Spreadsheet attribute list.
	 * @return int[]
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
				index = (oldAttributeList.indexOf(attributeList.get(i)));
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
		{
			indexArray = null;
		}
		return indexArray;
	}

}
