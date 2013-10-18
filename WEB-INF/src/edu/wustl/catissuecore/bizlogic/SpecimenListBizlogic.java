package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.tags.bizlogic.ITagBizlogic;
import edu.wustl.common.tags.dao.TagDAO;
import edu.wustl.common.tags.domain.Tag;
import edu.wustl.common.tags.domain.TagItem;
import edu.wustl.common.tags.util.TagUtil;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.query.util.global.AQConstants;
import edu.wustl.query.util.global.UserCache;
import gov.nih.nci.security.authorization.domainobjects.User;

public class SpecimenListBizlogic implements ITagBizlogic
{
	private static final Logger LOGGER = Logger.getCommonLogger(SpecimenListBizlogic.class);
	
	private static final String GET_SPECIMEN_IDS_FROM_LABEL = "SELECT s.id FROM Specimen s WHERE s.label IN (:labels)";
	
	private static final String GET_SPECIMEN_IDS_FROM_BARCODE = "SELECT s.id FROM Specimen s WHERE s.barcode IN (:barcodes)";
	/**
	 * Insert New Tag to the database.
	 * @param entityName from hbm file.
	 * @param label for new Tag.
	 * @param userId.
	 * @return tag identifier.
	 * @throws DAOException,BizLogicException.
	 */
	public long createNewTag(String label, Long userId) throws DAOException,
			BizLogicException
	{
		TagDAO tagDao = null;
		try
		{
			Tag tag = new Tag();
			tag.setLabel(label);
			tag.setUserId(userId);
			tagDao = new TagDAO(Constants.ENTITY_SPECIMEN_TAG);
			tagDao.insertTag(tag);
			tagDao.commit();
			return tag.getIdentifier();
		}
		catch (DAOException e)
		{
			throw new BizLogicException(e);
		}
		finally
		{
			if(tagDao != null)
			{	
				tagDao.closeSession();
			}
		}	
	}
	
	/**
	 * Assign the Query to existing folder.
	 * @param entityName from hbm file.
	 * @param tagId.
	 * @param objId.
	 * @throws DAOException,BizLogicException.
	 */
	public void assignTag(Long tagId, Long objId) throws DAOException,
			BizLogicException
	{
		TagDAO<AbstractSpecimen> tagDao = null;
		try
		{
			tagDao = new TagDAO<AbstractSpecimen>(Constants.ENTITY_SPECIMEN_TAGITEM);
			boolean alreadyAssigned = tagDao.isTagItemAlreadyAssigned(tagId, objId);
			if (! alreadyAssigned)
			{
				Tag tag = new Tag();
				tag.setIdentifier(tagId);
				TagItem<AbstractSpecimen> tagItem = new TagItem<AbstractSpecimen>();
		
				tagItem.setTag(tag);
				tagItem.setObjId(objId);
	
				tagDao.insertTagItem(tagItem);
			}
			tagDao.commit();
		}
		catch (DAOException e)
		{
			throw new BizLogicException(e);
		}
		finally
		{
			if(tagDao != null)
			{	
				tagDao.closeSession();
			}
		}	
	}
	
	/**
	 * Get list of Tags from the database.
	 * @param entityName from hbm file.
	 * @param obj Object to be inserted in database
	 * @throws DAOException,BizLogicException.
	 */
	public List<Tag> getTagList(SessionDataBean sessionBean) throws DAOException, BizLogicException
	{
		TagDAO tagDao = null;
		List<Tag> tagList = null;
		try
		{	 
			tagDao = new TagDAO(Constants.ENTITY_SPECIMEN_TAG);
			tagList = tagDao.getTags(sessionBean);
			return tagList;
		}
		catch (DAOException e)
		{
			throw new BizLogicException(e);
		}
		finally
		{
			if(tagDao != null)
			{	
				tagDao.closeSession();
			}
		}	
	}

 
	/**
	 * Get Tag object.
	 * @param entityName from hbm file.
	 * @param  tagId.
	 * @return Tag Object.
	 * @throws DAOException,BizLogicException.
	 */
	public Tag getTagById(Long tagId) throws DAOException, BizLogicException
	{	TagDAO tagDao = null;
		try
		{
			tagDao = new TagDAO(Constants.ENTITY_SPECIMEN_TAG);
			return tagDao.getTagById(tagId);
		}
		catch (DAOException e)
		{
			throw new BizLogicException(e);
		}
		finally
		{
			if(tagDao != null)
			{	
				tagDao.closeSession();
			}
		}	
	}

	/**
	 * Get the Set of TagItems.
	 * @param entityName from hbm file.
	 * @param tagId.
	 * @return Set<TagItem>.
	 * @throws DAOException,BizLogicException.
	 */
	public Set<TagItem> getTagItemByTagId(Long tagId) throws BizLogicException
	{
		TagDAO tagDao = null;
		Set<TagItem> tagItem = new HashSet<TagItem>();
		try
		{
			tagDao = new TagDAO(Constants.ENTITY_SPECIMEN_TAG);
			tagItem = tagDao.getTagItemBytagId(tagId);
		}
		catch (BizLogicException e)
		{
			LOGGER.error("Error occured while getting queries", e);
		}
		finally
		{
			if(tagDao != null)
			{	
				tagDao.closeSession();
			}
		}	
		return tagItem;
	}
	 
	/**
	 * Delete the Tag from database.
	 * @param entityName from hbm file.
	 * @param tagId to retrieve TagItem Object and delete it from database.
	 * @throws DAOException,BizLogicException.
	 */
	public void deleteTag(SessionDataBean sessionBean,Long tagId) throws DAOException, BizLogicException
	{
		TagDAO tagDao = null;
		try
		{
			tagDao = new TagDAO(Constants.ENTITY_SPECIMEN_TAG); 
			Tag tag = tagDao.getTagById(tagId);
			if(tag.getUserId() == sessionBean.getUserId()){
				tagDao.deleteTag(tag);
			} else {
				tag.getSharedUserIds().remove(Long.parseLong(sessionBean.getCsmUserId()));
				tagDao.updateTag(tag);
			}
			tagDao.commit();
		}
		catch (DAOException e)
		{
			throw new BizLogicException(e);
		}
		finally
		{
			if(tagDao != null)
			{	
				tagDao.closeSession();
			}
		}	
	}
	
	/**
	 * Delete the Tag Item from database.
	 * @param entityName from hbm file.
	 * @param objId to retrieve TagItem Object and delete it from database.
	 * @throws DAOException,BizLogicException.
	 */
	public void deleteTagItem(Long tagItemId, Long userId) throws DAOException,
			BizLogicException
	{	TagDAO tagDAO = null;
		try
		{ 
			tagDAO = new TagDAO(Constants.ENTITY_SPECIMEN_TAGITEM);
			TagItem tagItem = tagDAO.getTagItemById(tagItemId);
			Tag tag = tagItem.getTag(); 
			tagDAO.deleteTagItem(tagItem); 
			tagDAO.commit();
		}
		catch (DAOException e)
		{
			throw new BizLogicException(e);
		}
		finally
		{
			if(tagDAO != null)
			{	
				tagDAO.closeSession();
			}
		}	
	}
	
	/**
	 * Get the Tag Item from database for Tree Grid.
	 * @param entityName from hbm file.
	 * @param  tagId.
	 * @return Json Object.
	 * @throws DAOException,BizLogicException.
	 */
	public JSONObject getJSONObj(Long tagId) throws DAOException,
			BizLogicException
	{

		JSONObject arrayObj = new JSONObject(); 
		try
		{
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

	/**
	 * share Tags to users.
	 * @param Set<Long> selectedUsers.
	 * @param Set<Long> tagIdSet.
	 * @throws DAOException.
	 */
	public void shareTags(SessionDataBean sessionBean, Set<Long> tagIdSet, Set<Long> csmUserIds)
			throws DAOException, BizLogicException 
	{
		List<Tag> specimens = new ArrayList<Tag>();
		for(Long tagId : tagIdSet) 
		{
			TagDAO tagDao = null;
			try
			{
				tagDao = new TagDAO(Constants.ENTITY_SPECIMEN_TAG);
				Tag tag = tagDao.getTagById(tagId);
				Long csmUserId = Long.parseLong(sessionBean.getCsmUserId());
				if(csmUserIds.contains(csmUserId)){
					csmUserIds.remove(csmUserId);
				}
				tag.getSharedUserIds().addAll(csmUserIds); 
				tagDao.updateTag(tag);
				tagDao.commit();
				specimens.add(tag);
			}
			catch (DAOException e)
			{
				throw new BizLogicException(e);
			}
			finally
			{
				if(tagDao != null)
				{	
					tagDao.closeSession();
				}
			}	
		}
		try {	
			if (!specimens.isEmpty()){
				User user = UserCache.getUser(sessionBean.getCsmUserId().toString());
				Set<User> users = getUsersFromCsmIds(csmUserIds);
				TagUtil.sendSharedTagEmailNotification(user, specimens, 
					users, Constants.SHARE_SPECIMEN_LIST_EMAIL_TMPL);
			}
		} catch (Exception e) {
			LOGGER.error("Error while sending email for query folder",e);
		} 
	}
	
	public Set<User> getUsersFromCsmIds(Set<Long> csmUserIds) 
			throws BizLogicException
	{
		Set<User> users = new HashSet<User>();		
		for(Long sUserId : csmUserIds){
			User user = UserCache.getUser(sUserId.toString()); 
			users.add(user);	
		}
		return users;
	}
	/**
	 * delete TagItems from object Ids.
	 * @param List<Long> objIds.
	 * @param Long tagId.
	 * @param Long userId.
	 * @throws DAOException.
	 */
	public void deleteTagItemsFromObjIds(List<Long> specimenIds, Long tagId, Long userId)
			throws DAOException, BizLogicException 
	{
		TagDAO tagDAO = null;
		try
		{
			Tag tag = getTagById(tagId);
			tagDAO = new TagDAO(Constants.ENTITY_SPECIMEN_TAGITEM);	
			tagDAO.deleteTagItemsFromObjIds(specimenIds, tagId);
			tagDAO.commit();
		}
		catch (DAOException e)
		{
			throw new BizLogicException(e);
		}  
		finally
		{
			if(tagDAO != null)
			{	
				tagDAO.closeSession();
			}
		}	
	}	 
	
	public List<Long> getSpecimenIdsFromLabel(List<String> labels) throws DAOException, BizLogicException {
		DAO dao = null;
		List<Long> list = null;
		String appName = CommonServiceLocator.getInstance().getAppName();
		IDAOFactory daofactory = DAOConfigFactory.getInstance().getDAOFactory(appName);
		try 
		{
			SessionDataBean sessionDataBean = new SessionDataBean();		
			dao = daofactory.getDAO();
			dao.openSession(sessionDataBean);
			List<ColumnValueBean> parameters = new ArrayList<ColumnValueBean>(); 
			parameters.add(new ColumnValueBean("labels", labels));
			list = ((HibernateDAO)dao).executeParamHQL(GET_SPECIMEN_IDS_FROM_LABEL, parameters);
		}
		catch (DAOException e)
		{
			throw new BizLogicException(e);
		} finally {
			dao.closeSession();
		}
		
		return list;
	}
	
	public List<Long> getSpecimenIdsFromBarcode(List<String> barcodes) throws DAOException, BizLogicException {
		DAO dao = null;
		List<Long> list = null;
		String appName = CommonServiceLocator.getInstance().getAppName();
		IDAOFactory daofactory = DAOConfigFactory.getInstance().getDAOFactory(appName);
		try 
		{
			SessionDataBean sessionDataBean = new SessionDataBean();		
			dao = daofactory.getDAO();
			dao.openSession(sessionDataBean);
			List<ColumnValueBean> parameters = new ArrayList<ColumnValueBean>(); 
			parameters.add(new ColumnValueBean("barcodes", barcodes));
			list = ((HibernateDAO)dao).executeParamHQL(GET_SPECIMEN_IDS_FROM_BARCODE, parameters);
		}
		catch (DAOException e)
		{
			throw new BizLogicException(e);
		} finally {
			dao.closeSession();
		}
		
		return list;
	}
}
