package edu.wustl.catissuecore.gridgrouper;

import edu.wustl.catissuecore.util.GridPropertyFileReader;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.migrator.util.Utility;
import gov.nih.nci.cacoresdk.util.GridAuthenticationClient;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier;
import gov.nih.nci.cagrid.gridgrouper.bean.MemberDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.MemberFilter;
import gov.nih.nci.cagrid.gridgrouper.bean.MembershipType;
import gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier;
import gov.nih.nci.cagrid.gridgrouper.client.GridGrouperClient;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.GridGrouperRuntimeFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.GroupNotFoundFault;
import gov.nih.nci.logging.api.util.StringUtils;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.axis.types.URI.MalformedURIException;
import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;

public class GridGrouperUtil {

	private static GlobusCredential globusCredentials = null;
	private static GridGrouperClient client = null;
	private static Logger LOG = Logger.getLogger(GridGrouperUtil.class);
	
	/**************************************
	 * This method is used to intialize the client if client is null and update
	 * the client if globus credential's life time is near to expire. The
	 * threshold is managed using GG_TIMEOUT_LIMIT in GridGrouperConstant interface.
	 */
	public static void syncClient() {
		Properties defaultProps = GridPropertyFileReader.configuredProperties();
		Properties serviceUrls = GridPropertyFileReader.serviceUrls();
		
		String userName = defaultProps.getProperty(GridGrouperConstant.GG_USER_NAME_KEY);
		String password = defaultProps.getProperty(GridGrouperConstant.GG_PASSWORD_KEY);
		String dorianUrl = serviceUrls.getProperty("cagrid.master.dorian.service.url");
		String gridGrouperUrl = serviceUrls.getProperty("cagrid.master.gridgrouper.service.url");
		
		if (!StringUtils.isBlank(userName)
				&& !StringUtils.isBlank(password)
				&& !StringUtils.isBlank(dorianUrl)
				&& !StringUtils.isBlank(gridGrouperUrl)) {
				try {
					globusCredentials = GridAuthenticationClient.authenticate(dorianUrl, dorianUrl, userName, password);
				} catch (Exception e) {
					LOG.error(GridGrouperConstant.GLOBUS_INIT_ERROR, e);
				}
				if (globusCredentials != null) {
					try {
						client = new GridGrouperClient(gridGrouperUrl,globusCredentials);
					} catch (MalformedURIException e) {
						LOG.error(GridGrouperConstant.GG_URL_ERROR, e);
					} catch (RemoteException e) {
						LOG.error(GridGrouperConstant.GG_REMOTE_ERROR, e);
					}
				}
		}
	}
	
	
	public static GroupDescriptor[] getGroups(String stemName) throws Exception {
		syncClient();
		GroupDescriptor[] groups = client.getChildGroups(getStemIdentifier(stemName));
		return groups;
	}
	
	public static boolean isMemberOfGroup(String member , String groupName) throws GridGrouperRuntimeFault, GroupNotFoundFault, RemoteException  {
		syncClient();
		return client.isMemberOf(getGroupIdentifier(groupName), member, MemberFilter.All);

	}
	
	public static MemberDescriptor[] getMembers(String groupName) throws Exception {
		syncClient();
		MemberDescriptor[] members = client.getMembers(getGroupIdentifier(groupName), MemberFilter.All);
		return members;
	}
	
	private static StemIdentifier getStemIdentifier(String stemName) {
		StemIdentifier id = new StemIdentifier();
		id.setStemName(stemName);
		return id;
	}
	public static GroupIdentifier getGroupIdentifier(String groupName) {
		GroupIdentifier id = new GroupIdentifier();
		id.setGroupName(groupName);
		return id;
	}
	public static GroupDescriptor getGroupByIdentifier(GroupIdentifier groupIdentifier) throws GridGrouperRuntimeFault, GroupNotFoundFault, RemoteException {
		return client.getGroup(groupIdentifier);
	}
	public static GroupDescriptor[] getUserGroups(String  memberIdentity) throws  RemoteException {
		syncClient();
		GroupDescriptor[] groups = client.getMembersGroups(memberIdentity, MembershipType.Any);
		return groups;
	}
	
	public static Map<String, List<String>> getStemAndGroups() throws Exception{
		Map<String, List<String>> map= new HashMap<String, List<String>>();
		for(String stem : getStems()){
			List<String> groups = new ArrayList<String>();
			for(GroupDescriptor gd : getGroups(stem)){
				groups.add(gd.getName());
			}
			map.put(stem, groups);
		}
		return map;
	}


	/*
	 *  get all grid grouper group names which are provisioned in catissue
	 */
	private static List<String> getGroupNamesFromDB() throws Exception{
		List<String> caTissuegroups = new ArrayList<String>();
		String groupSql = "select distinct(group_name) from catissue_cp_grid_prvg cgp ";
		List<List<String>> groupList = Utility.executeQueryUsingDataSource(groupSql, null ,false, "WUSTLKey");
		if (groupList != null) {
			for (List<String> result:groupList) {
				caTissuegroups.add(result.get(0).toString());
	        }
		}
		return caTissuegroups;
	}
	
	/*
	 * 	get all grouper group names and csm group id for that group
	 */
	public static Map<String , String> getGridGroupInfoFromDB() throws ApplicationException {
		Map<String , String> groups = newHashMap();
		String groupSql = "select distinct(cgp.group_name) , cg.group_id from catissue_cp_grid_prvg cgp , csm_group cg where  cgp.group_name = cg.group_name";
		List<List<String>> groupList = Utility.executeQueryUsingDataSource(groupSql, null ,false, "WUSTLKey");
        if (groupList != null) {
			for (List<String> result:groupList) {
	        	groups.put(result.get(0).toString() , result.get(1).toString());
	        }
        }
        return groups;
	}
	
	/*
	 * get grouper groups of a user , and filter them to fetch only ones provisioned in catissue .. with map of  group name and csm group id . 
	 * csmUserName can be NULL
	 */
	public static  Map<String , String> getFilteredUserGroups (String memberIdentity , String csmUserName) throws Exception {
		Map<String , String> filteredUserGroups = newHashMap();
		if (StringUtils.isBlank(memberIdentity) && !StringUtils.isBlank(csmUserName)) {
			String sql = "select identity from csm_migrate_user where login_name = ?"  ;
    		List<ColumnValueBean> parameters = new ArrayList<ColumnValueBean>();
    		ColumnValueBean groupNameBean = new ColumnValueBean(csmUserName);
    		parameters.add(groupNameBean);
			
			List<List<String>> userList = Utility.executeQueryUsingDataSource(sql, parameters ,false, "WUSTLKey");
			if (userList != null) {
				memberIdentity = userList.get(0).get(0).toString();
	        }
		}
		if ( StringUtils.isBlank(memberIdentity)  ) {
			return filteredUserGroups;
		}
		Map<String , String> groupsFromDB = getGridGroupInfoFromDB();
		
		Set<String> groupNames = groupsFromDB.keySet();
		for (String groupName:groupNames) {
			try {
				if (GridGrouperUtil.isMemberOfGroup(memberIdentity, groupName))  filteredUserGroups.put(groupName, groupsFromDB.get(groupName));
			} catch (GroupNotFoundFault gnf) {
				LOG.error("Grid Group not found in grid grouper : " + groupName);
				// group doesnt exist anymore in grid grouper that means this group is gone from grid group.
				
			}
		}
		return filteredUserGroups;
	}
	
	/*
	 * 	get all cps for groups in catissue_cp_grid_prvg table 
	 */
	public static List<String> getCPsForUserGroups(Set<String> gridGroupNames) throws ApplicationException {
		List<String> cpIds = new ArrayList<String>();
		
		Set<String> gridGroupNamesWithQoutes = new HashSet<String>();
		for (String str:gridGroupNames) {
			gridGroupNamesWithQoutes.add("'"+str+"'");
		}
		
		String sql = "select collection_protocol_id from catissue_cp_grid_prvg where group_name in("+Arrays.toString(gridGroupNamesWithQoutes.toArray(new String[0])).replace("[", "").replace("]", "")+")";
		List<List<String>> rList = Utility.executeQueryUsingDataSource(sql, null ,false, "WUSTLKey");
        if (rList != null) {
			for (List<String> r:rList) {
				cpIds.add(r.get(0).toString());
	        }
        }
        return cpIds;
	}
	
	public static List<List<String>> getCsmUserIds(MemberDescriptor[] members) throws ApplicationException  {
		List<List<String>> resultList = new ArrayList<List<String>>();
		if (members !=null) {
			List<String> memberIdentities = new ArrayList<String>();
    		for (int j=0 ;j<members.length; j++) {
    			MemberDescriptor member = members[j];
    			if (member.getSubjectId() != null) {
    				memberIdentities.add("'"+member.getSubjectId()+"'");
    			}
    		}
    		//List<ColumnValueBean> parameters = new ArrayList<ColumnValueBean>();
    		//ColumnValueBean groupNameBean = new ColumnValueBean(memberIdentities);
    		//parameters.add(groupNameBean);
    		String queryStr = "select u.user_id , cu.identifier from csm_user u , csm_migrate_user mu , catissue_user cu where cu.csm_user_id = u.user_id and mu.login_name = u.login_name and mu.identity in ("+Arrays.toString(memberIdentities.toArray()).replace("[","").replace("]", "")+") ";
    		System.out.println(queryStr);
    		resultList = Utility.executeQueryUsingDataSource(queryStr, null,false, "WUSTLKey");
		}
		return resultList;
	}
	
	
	private static <K,V> Map<K,V> newHashMap() {
		return new HashMap<K, V>();
	}
	
	
	public static boolean isIdentityPresentInGridGrouper(String gridUserName) throws Exception {
		List<String> dbGroups = getGroupNamesFromDB();
		boolean flag = false;
		Map<String, List<String>> gridGrouperMap = getStemAndGroups();
		for(String stem : gridGrouperMap.keySet()){
			for(String group : gridGrouperMap.get(stem)){
				if (!dbGroups.contains(group)) {
					continue;
				}
				System.out.println(group);
				MemberDescriptor [] members = getMembers(group);
	    		if (members !=null) {
	        		for (int j=0 ;j<members.length; j++) {
	        			MemberDescriptor member = members[j];
	        			String userName = getUserName(member.getSubjectId());

	        			if (userName.equals(gridUserName)) {
	        				return true;
	        			}
	        		}
	    		}
			}
		}
		return flag;
	}

	
	public static List<String> getStems() throws Exception{
		Properties defaultProps = GridPropertyFileReader.configuredProperties();
		String ggStems = defaultProps.getProperty(GridGrouperConstant.GG_STEMS_KEY);
		return Arrays.asList(ggStems.split(";"));
	}
	
	private static String getUserName(String subjectId) {
		String[] tokens = subjectId.split("/");
		String cn = tokens[5];
		String[] cnTokens = cn.split("=");
		return cnTokens[1];
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		System.out.println(GridGrouperUtil.isMemberOfGroup("/O=caBIG/OU=caGrid/OU=Training/OU=Dorian/CN=jdoe01", "Bootcamp:bootcamp"));
		MemberDescriptor [] members = GridGrouperUtil.getMembers("Bootcamp:bootcamp");
		if (members !=null) {
    		for (int j=0 ;j<members.length; j++) {
    			MemberDescriptor member = members[j];
    			//member.
    			//System.out.println("--" + member.getSubjectId());
    		}
		}
		
		//GridGrouperUtil.sync2();
		//GlobusCredential credentials = GridGrouperUtil.authenticate(GridGrouperUtil.USER_NAME, GridGrouperUtil.USER_PASSWORD);
		//System.out.println(credentials.getIdentity());
		/*
		Map<String, List<String>> gridGrouperMap = GridGrouperUtil.getStemAndGroups();
		for(String stem : gridGrouperMap.keySet()){
			for(String group : gridGrouperMap.get(stem)){
				System.out.println(group);
				MemberDescriptor [] members = GridGrouperUtil.getMembers(group);
	    		if (members !=null) {
	        		for (int j=0 ;j<members.length; j++) {
	        			MemberDescriptor member = members[j];
	        			//member.
	        			System.out.println("--" + member.getSubjectId());
	        		}
	    		}
			}
		}*/
		//System.out.println(GridGrouperUtil.isUserPresentInGridGrouper("lewis.j.frey"));
		//System.out.println(GridGrouperUtil.isIdentityPresentInGridGrouper("jdoe01"));
		//System.out.println(GridGrouperUtil.isUserPresentInGridGrouper("/O=caBIG/OU=caGrid/OU=Training/OU=National Cancer Institute/CN=akkalas"));
	}
}
