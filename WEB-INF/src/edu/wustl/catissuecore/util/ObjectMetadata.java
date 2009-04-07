package edu.wustl.catissuecore.util;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.dto.CollectionProtocolDTO;
import edu.wustl.catissuecore.dto.UserDTO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.dao.util.HibernateMetaData;

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
		String tableName = "";
		if(obj instanceof AbstractDomainObject)
		{
			tableName = HibernateMetaData.getTableName(obj.getClass()).toLowerCase();
		}
		else if(obj instanceof UserDTO)
		{
			UserDTO userDto = (UserDTO)obj;
			User user = userDto.getUser();
			tableName = HibernateMetaData.getTableName(user.getClass()).toLowerCase();
		}
		else if(obj instanceof CollectionProtocolDTO)
		{
			CollectionProtocolDTO CpDto = (CollectionProtocolDTO) obj;
			CollectionProtocol collectionProtocol = CpDto.getCollectionProtocol();
			tableName = HibernateMetaData.getTableName(collectionProtocol.getClass()).toLowerCase();
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
