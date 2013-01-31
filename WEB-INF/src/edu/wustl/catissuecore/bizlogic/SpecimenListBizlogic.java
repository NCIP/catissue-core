package edu.wustl.catissuecore.bizlogic;

import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.tags.bizlogic.ITagBizlogic;
import edu.wustl.common.tags.dao.TagDAO;
import edu.wustl.common.tags.domain.Tag;
import edu.wustl.common.tags.domain.TagItem;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.query.util.global.AQConstants;

public class SpecimenListBizlogic implements ITagBizlogic
{
	/**
	 * Insert New Tag to the database.
	 * @param entityName from hbm file.
	 * @param label for new Tag.
	 * @param userId.
	 * @return tag identifier.
	 * @throws DAOException,BizLogicException.
	 */

 
	public long createNewTag(String entityName, String label, long userId) throws DAOException,
			BizLogicException
	{
		Tag tag = new Tag();
		tag.setLabel(label);
		tag.setUserId(userId);
		TagDAO tagDao = new TagDAO(entityName,userId);
		tagDao.insertTag(tag);
		return tag.getIdentifier();
	}

	/**
	 * Assign the Query to existing folder.
	 * @param entityName from hbm file.
	 * @param tagId.
	 * @param objId.
	 * @throws DAOException,BizLogicException.
	 */
 
	public void assignTag(String entityName, long tagId, long objId) throws DAOException,
			BizLogicException
	{
		Tag tag = new Tag();
		tag.setIdentifier(tagId);
		TagItem<AbstractSpecimen> tagItem = new TagItem<AbstractSpecimen>();
	 

		tagItem.setTag(tag);
		tagItem.setObjId(objId);

		TagDAO<AbstractSpecimen> tagDao = new TagDAO<AbstractSpecimen>(entityName,objId);
		tagDao.insertTagItem(tagItem);
	}

	/**
	 * Get list of Tags from the database.
	 * @param entityName from hbm file.
	 * @param obj Object to be inserted in database
	 * @throws DAOException,BizLogicException.
	 */
 
	public List<Tag> getTagList(String entityName, long userId) throws DAOException, BizLogicException
	{
		List<Tag> tagList = null;
		TagDAO tagDao = null;
		tagDao = new TagDAO(entityName,userId);
		tagList = tagDao.getTags();
		return tagList;
	}

 
	/**
	 * Get Tag object.
	 * @param entityName from hbm file.
	 * @param  tagId.
	 * @return Tag Object.
	 * @throws DAOException,BizLogicException.
	 */
 
	public Tag getTagById(String entityName, long tagId) throws DAOException, BizLogicException
	{
		TagDAO tagDao = new TagDAO(entityName,tagId);
		Tag tag = tagDao.getTagById(tagId);
		return tag;
	}

	/**
	 * Get the Set of TagItems.
	 * @param entityName from hbm file.
	 * @param tagId.
	 * @return Set<TagItem>.
	 * @throws DAOException,BizLogicException.
	 */
	public Set<TagItem> getTagItemByTagId(String entityName, long tagId) throws BizLogicException
	{
		TagDAO tagDao = new TagDAO(entityName,tagId);
		Set<TagItem> tagItem = tagDao.getTagItemBytagId(tagId);
		return tagItem;
	}

	 
	/**
	 * Delete the Tag from database.
	 * @param entityName from hbm file.
	 * @param tagId to retrieve TagItem Object and delete it from database.
	 * @throws DAOException,BizLogicException.
	 */
 
	public void deleteTag(String entityName, long tagId) throws DAOException, BizLogicException
	{
		TagDAO tagDao = new TagDAO(entityName,tagId);
		Tag tag = tagDao.getTagById(tagId);
		tagDao.deleteTag(tag);
	}

	/**
	 * Delete the Tag Item from database.
	 * @param entityName from hbm file.
	 * @param objId to retrieve TagItem Object and delete it from database.
	 * @throws DAOException,BizLogicException.
	 */

 
	public void deleteTagItem(String entityName, long itemId) throws DAOException,
			BizLogicException
	{
		TagDAO tagDAO = new TagDAO(entityName,itemId);
		TagItem tagItem = tagDAO.getTagItemById(itemId);
		tagDAO.deleteTagItem(tagItem);
	}

	/**
	 * Get the Tag Item from database for Tree Grid.
	 * @param entityName from hbm file.
	 * @param  tagId.
	 * @return Json Object.
	 * @throws DAOException,BizLogicException.
	 */
	public JSONObject getJSONObj(String entityName, long tagId) throws DAOException,
			BizLogicException
	{

		JSONObject arrayObj = new JSONObject(); 
		try
		{
			Set<TagItem> tagItemList = getTagItemByTagId(entityName, tagId);
			JSONArray treeData = new JSONArray();
			List objDetail = AppUtility.getObjDetails(tagId);
			
			int childCount = 0;
			for (Object object : objDetail) 
			{
					List list = (List)object;
					JSONObject obj = new JSONObject();
					obj.put(AQConstants.IDENTIFIER, Long.valueOf(list.get(0).toString()));
					obj.put(AQConstants.NAME, list.get(1).toString());
					childCount++;
					treeData.put(obj);
			}

			arrayObj.put(AQConstants.TREE_DATA, treeData);
			arrayObj.put(AQConstants.CHILDCOUNT, childCount);

		}
		catch (Exception e)
		{
			System.out.println(e.getStackTrace());
		}
		return arrayObj;

	}

	public void assignTag(String arg0, String arg1, long arg2, long arg3)
			throws DAOException, BizLogicException {
		// TODO Auto-generated method stub
		
	}

	public void createNewTag(String entityName, String newTagName, long ownerId, Set<Long> selectedUsers)
			throws DAOException, BizLogicException 
	{
		Tag tag = new Tag();
		tag.setLabel(newTagName);
		tag.setUserId(ownerId);
		tag.setSharedUserIds(selectedUsers);
		TagDAO tagDao = new TagDAO(entityName,ownerId);
		tagDao.insertTag(tag);
	}

	public void shareTags(String entityName, Set<Long> tagIdList, Set<Long> selectedUsers)
			throws DAOException, BizLogicException 
	{
		for(Long tagId : tagIdList) 
		{
			TagDAO tagDao = new TagDAO(entityName,tagId);
			Tag tag = getTagById(entityName, tagId);
			tag.getSharedUserIds().addAll(selectedUsers);
		    	tagDao.updateTag(tag);
		} 
	}
 
	  
}
