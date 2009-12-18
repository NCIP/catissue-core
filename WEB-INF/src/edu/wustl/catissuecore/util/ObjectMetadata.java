package edu.wustl.catissuecore.util;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.dto.CollectionProtocolDTO;
import edu.wustl.catissuecore.dto.UserDTO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.util.HibernateMetaData;
import edu.wustl.dao.util.HibernateMetaDataFactory;

/**
 * 
 * @author pooja_deshpande
 *
 */
public class ObjectMetadata implements titli.controller.interfaces.ObjectMetadataInterface
{
	/**
	 * Return the table name of the corresponding object
	 * @param obj The object whose table name is to be retrieved
	 * @return tableName Table name of the obj
	 * 
	 */
	public String getTableName(Object obj)
	{
		HibernateMetaData hibernateMetaData;
		String tableName = "";
		try {
			hibernateMetaData = HibernateMetaDataFactory.getHibernateMetaData
			(CommonServiceLocator.getInstance().getAppName());


			if(obj instanceof AbstractDomainObject)
			{

				tableName = hibernateMetaData.getTableName(obj.getClass()).toLowerCase();
			}
			else if(obj instanceof UserDTO)
			{
				UserDTO userDto = (UserDTO)obj;
				User user = userDto.getUser();
				tableName = hibernateMetaData.getTableName(user.getClass()).toLowerCase();
			}
			else if(obj instanceof CollectionProtocolDTO)
			{
				CollectionProtocolDTO CpDto = (CollectionProtocolDTO) obj;
				CollectionProtocol collectionProtocol = CpDto.getCollectionProtocol();
				tableName = hibernateMetaData.getTableName(collectionProtocol.getClass()).toLowerCase();
			}
		} catch (DAOException e)
		{
			e.printStackTrace();
		}
		return tableName;
	}
	
	/**
	 * Returns the identifier of the corresponding object
	 * @param obj The object whose identifier is to be retrieved
	 * @return id The unique identifier
	 */
	public String getUniqueIdentifier(Object obj)
	{
		String id = null;
		if(obj instanceof AbstractDomainObject)
		{
			id= ((AbstractDomainObject) obj).getId().toString();
		}
		else if(obj instanceof UserDTO)
		{
			UserDTO userDto = (UserDTO)obj;
			User user = userDto.getUser();
			id= user.getId().toString();
		}
		else if(obj instanceof CollectionProtocolDTO)
		{
			CollectionProtocolDTO CpDto = (CollectionProtocolDTO) obj;
			CollectionProtocol collectionProtocol = CpDto.getCollectionProtocol();
			
			id=  collectionProtocol.getId().toString();
		}
		
		return id;
	}
}
