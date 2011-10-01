package edu.wustl.catissuecore.gridgrouper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.wustl.catissuecore.domain.CPGridGrouperPrivilege;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.dao.DAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.migrator.util.Utility;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;
import edu.wustl.security.privilege.PrivilegeUtility;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.User;

public class GridGroupSync {
	
	private static final Log LOG = LogFactory.getLog(GridGroupSync.class);
	
	public void syncSingleUser(String loginName) throws Exception {
		final List<List<String>> userList = getMigratedUsers(loginName);
        sync (userList);		
	}
	
	public void syncAllMigratedUsers() throws Exception {
		final List<List<String>> userList = getMigratedUsers(null);
        sync (userList);
        
	}
	private void sync(List<List<String>> userList) throws Exception {
        if (userList == null) {
        	return;
        }
        
        String identity = "";
        Map<String , String> filteredUserGroups = null;
        User user = null;
        UserProvisioningManager userProvisioningManager = new PrivilegeUtility().getUserProvisioningManager();

		
        for (List<String> userResult:userList) {
        	identity = userResult.get(0).toString();
        	
        	filteredUserGroups = GridGrouperUtil.getFilteredUserGroups(identity,null);
        	Collection<String> filteredGroupIds = filteredUserGroups.values();

        	user = userProvisioningManager.getUser(userResult.get(1).toString());
        	String deleteUserGroupAssociationQuery = "delete from csm_user_group where user_id = ? and group_id in (select group_id from csm_group where group_desc = '"+CPGridGrouperPrivilege.GRID_GROUP_DESC+"')";
        	removeGroups(user.getUserId() , deleteUserGroupAssociationQuery);
        	//List<String> groupsToDelete = getGroupsToDelete(user.getUserId()+"");
        	//for (String groupId:groupsToDelete) {
        	//	userProvisioningManager.removeUserFromGroup(groupId, user.getUserId()+"");
        	//}
        	
        	userProvisioningManager.addGroupsToUser(user.getUserId()+"", filteredGroupIds.toArray(new String[0]));
        	LOG.debug("Updated Groups for User : " + identity + " : " + Arrays.toString(filteredUserGroups.keySet().toArray(new String[0])));
        	
        	/* 
        	 remove users from collection protocol association 
        	 we are not suppose to remove all CPs for the user , user may have been assigned to some CPs with local sites . so remove only CPs which are assigned to user by grid groups 
        	 */
        	//this list give the CPs assigned with local sites 
       	    List<String> cpIdsAssignedUsingLocalSites = getCpIdsAssignedUsingLocalSites(userResult.get(2).toString());
        	// delete from catissue_user_cp table only if cpid not in this list 
        	deleteFromUserCP(userResult.get(2).toString(),cpIdsAssignedUsingLocalSites);

        	
        	// find CPs for user ...  
        	// get cps from catissue_cp_grid_prvg for the filtered groups ..
        	if (filteredUserGroups.keySet().size() > 0) {
        		List<String> collectionProtocols = getCpIdsForFilteredGroups(filteredUserGroups.keySet());

	        	for (String cpId:collectionProtocols) {
	        		insertUserCP(userResult.get(2).toString(),cpId);
	        	}
	        	LOG.debug("Updated CPs for User : " + identity + " : " + Arrays.toString(collectionProtocols.toArray(new String[0])));
        	}
        	
        	PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
        	PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(user.getLoginName());
			privilegeCache.refresh();
			LOG.debug("SYNC SUCCESS..");
        }
        
	}

	private List<String> getCpIdsForFilteredGroups(Set<String> groupNames) throws Exception {
		List<String> resultList = new ArrayList<String>();
		if (groupNames.size() == 0) {
			return resultList;
		}
		List<String> groupNamesWithQts = new ArrayList<String>();
		
		for (String str:groupNames) {
			groupNamesWithQts.add("'"+str+"'");
		}
			
		String cpSql = "select distinct(collection_protocol_id) from catissue_cp_grid_prvg where status = 'ACTIVE' and group_name in ("+Arrays.toString(groupNamesWithQts.toArray()).replace("[", "").replace("]", "")+")";
		
		@SuppressWarnings(value = { "unchecked" })
        List<List<String>> cpList = Utility.executeQueryUsingDataSource(cpSql, null ,false, "WUSTLKey");
	     if (cpList != null) {
	        for (List<String> cp:cpList) {
	        	resultList.add(cp.get(0).toString());
	        }
	     }
        return resultList;
	}

	private List<List<String>> getMigratedUsers(String loginName) throws Exception {
		String userSql = "select identity , cmu.login_name , identifier from csm_migrate_user cmu  ,catissue_user cu where cu.login_name=cmu.login_name";
		if (loginName != null) {
			userSql = userSql + " and cmu.login_name = '"+loginName+"'";
		}
		@SuppressWarnings(value = { "unchecked" })
        List<List<String>> userList = Utility.executeQueryUsingDataSource(userSql, null ,false, "WUSTLKey");
        return userList;
	}
	
	private List<String> getCpIdsAssignedUsingLocalSites(String cpId) throws Exception {
		
		List<String> resultList = new ArrayList<String>();

			
		String cpSql = "select distinct(scp.collection_protocol_id) from CATISSUE_SITE_CP scp , CATISSUE_USER_CP ucp where ucp.collection_protocol_id = scp.collection_protocol_id and ucp.user_id = " + cpId;
		@SuppressWarnings(value = { "unchecked" })
        List<List<String>> cpList = Utility.executeQueryUsingDataSource(cpSql, null ,false, "WUSTLKey");
        if (cpList != null) {
	        for (List<String> cp:cpList) {
	        	resultList.add(cp.get(0).toString());
	        }
        }
        return resultList;
	}
	
	private List<String> getGroupsToDelete(String userId) throws Exception {
		
		List<String> resultList = new ArrayList<String>();

			
		String cpSql = "select group_id from csm_user_group where user_id = "+userId +"and group_id in (select group_id from csm_group where group_desc = 'GRID_GROUP')";
		@SuppressWarnings(value = { "unchecked" })
        List<List<String>> cpList = Utility.executeQueryUsingDataSource(cpSql, null ,false, "WUSTLKey");
        if (cpList != null) {
	        for (List<String> cp:cpList) {
	        	resultList.add(cp.get(0).toString());
	        }
        }
        return resultList;
	}
	

	
	
	private void insertUserCP(String userId , String cpId)
	throws ApplicationException
	{

			JDBCDAO jdbcDAO = null;
			jdbcDAO = AppUtility.openJDBCSession();
			
			PreparedStatement statement = null;
			try
			{
				
				statement = jdbcDAO.getPreparedStatement("insert into catissue_user_cp values (? ,?)");

					statement.setLong(1, Long.valueOf(userId));
					statement.setLong(2, Long.valueOf(cpId));
					statement.executeUpdate();
					//statement.clearParameters();

				jdbcDAO.commit();
			}
			catch (final SQLException excep)
			{
				//ignore if contraint violation
				LOG.error("Collection Protocol :" +cpId+ " already exists for userId : " +userId + " in catissue_user_table .. we can ignore this error " + excep.getMessage());
				//excep.printStackTrace();
			}
			finally
			{
				AppUtility.closeJDBCSession(jdbcDAO);
				try
				{
					statement.close();
				}
				catch (final SQLException e)
				{
					throw new ApplicationException(ErrorKey.getErrorKey("SQL.exp"), e,
					"GridGroupSync");
				}
			}

	}
	
	private void deleteFromUserCP(String userId , List<String> cpIds)
	throws ApplicationException
	{

			if (cpIds.size() == 0) {
				return;
			}
			JDBCDAO jdbcDAO = null;
			jdbcDAO = AppUtility.openJDBCSession();
			
			PreparedStatement statement = null;
			try
			{
				
				String delSql = "delete from catissue_user_cp where user_id=? and collection_protocol_id not in ("+ Arrays.toString(cpIds.toArray()).replace("[", "").replace("]", "") +")";
				statement = jdbcDAO.getPreparedStatement(delSql);

					statement.setLong(1, Long.valueOf(userId));
					
					statement.executeUpdate();
					//statement.clearParameters();

				jdbcDAO.commit();
			}
			catch (final SQLException excep)
			{
				//ignore if contraint violation
				excep.printStackTrace();
			}
			finally
			{
				AppUtility.closeJDBCSession(jdbcDAO);
				try
				{
					statement.close();
				}
				catch (final SQLException e)
				{
					throw new ApplicationException(ErrorKey.getErrorKey("SQL.exp"), e,
					"GridGroupSync");
				}
			}

	}
	
	
	
	private void removeGroups(Long userId , String deleteQuery)
	throws ApplicationException
	{

			JDBCDAO jdbcDAO = null;
			jdbcDAO = AppUtility.openJDBCSession();
			
			PreparedStatement statement = null;
			try
			{
				
				statement = jdbcDAO.getPreparedStatement(deleteQuery);

					statement.setLong(1, userId);
					statement.executeUpdate();
					//statement.clearParameters();

				jdbcDAO.commit();
			}
			catch (final SQLException excep)
			{
				LOG.error("Failed to delete groups for user : " + userId + " from CSM_GROUP table " + excep.getMessage());
				throw new ApplicationException(ErrorKey.getErrorKey("SQL.exp"), excep,
				"GridGroupSync");
			}
			finally
			{
				AppUtility.closeJDBCSession(jdbcDAO);
				try
				{
					statement.close();
				}
				catch (final SQLException e)
				{
					throw new ApplicationException(ErrorKey.getErrorKey("SQL.exp"), e,
					"GridGroupSync");
				}
			}

	}
	
	private <T> T getObjectById (Class<T> t , Long id ) throws ApplicationException
	{
			DAO dao = null;
			
			
			Object obj = null;
			try
			{
				
				dao = AppUtility.openDAOSession(null);
				obj = dao.retrieveById(t.getName(), id) ;

			}
			catch (final Exception excep)
			{
				throw new ApplicationException(ErrorKey.getErrorKey("SQL.exp"), excep,
				"GridGroupSync");
			}
			finally
			{
				AppUtility.closeDAOSession(dao);

			}
			
			return (T)obj;

	}

	

		 
	public static void main (String ... args) {
		GridGroupSync sync = new GridGroupSync();
		try {
			CollectionProtocol c = sync.getObjectById(CollectionProtocol.class, Long.valueOf("100"));
			System.out.println(c.getClass().getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
