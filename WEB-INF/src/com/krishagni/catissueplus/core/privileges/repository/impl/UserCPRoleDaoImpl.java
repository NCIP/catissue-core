
package com.krishagni.catissueplus.core.privileges.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.privileges.domain.UserCPRole;
import com.krishagni.catissueplus.core.privileges.events.UserPrivDetail;
import com.krishagni.catissueplus.core.privileges.repository.UserCPRoleDao;

import edu.wustl.catissuecore.domain.User;

public class UserCPRoleDaoImpl extends AbstractDao<UserCPRole> implements UserCPRoleDao {

	@Override
	@SuppressWarnings(value = {"unchecked"})
	public List<UserCPRole> getCPUserRoleByCpAndUser(Long cpId, Long userId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_USERCPROLE_BY_CP_AND_USER);
		query.setLong("cpId", cpId);
		query.setLong("userId", userId);
		return query.list();
	}


	@Override
	public List<Long> getCpIdsByUserId(Long userId) {
//		String sql = "(select cp.identifier from catissue_collection_protocol cp inner join catissue_site_cp cp_site on " +
//				"cp_site.collection_protocol_id = cp.identifier inner join catissue_site_users site_user on " +
//				"site_user.site_id = cp_site.site_ID where site_user.user_id = :userId) union " +
//				"(select cp.identifier from catissue_collection_protocol cp inner join catissue_user_cp user_cp on " +
//				"user_cp.collection_protocol_id = cp.identifier where user_cp.user_id = :userId)";
//		
//		String allCps = "select collection_protocol_id from CATISSUE_COLL_COORDINATORS where user_id = :userId"+
//		" union select collection_protocol_id from CATISSUE_USER_CP where user_id = :userId";
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_CP_IDS_BY_USER_ID_CONST);
		query.setLong("userId", userId);
		List list = query.list();
		List<Long> result = new ArrayList<Long>();
		for (Object object : list) {
			result.add(Long.valueOf(object.toString()));
		}
		return result;
	}

	@Override
	public List<Long> getSiteIdsByUserId(Long userId) {
//		String sql = "(select cp.identifier from catissue_collection_protocol cp inner join catissue_site_cp cp_site on " +
//				"cp_site.collection_protocol_id = cp.identifier inner join catissue_site_users site_user on " +
//				"site_user.site_id = cp_site.site_ID where site_user.user_id = :userId) union " +
//				"(select cp.identifier from catissue_collection_protocol cp inner join catissue_user_cp user_cp on " +
//				"user_cp.collection_protocol_id = cp.identifier where user_cp.user_id = :userId)";
		
		String allSites = "select user.siteCollection.id from "+ User.class.getName()+" user where user.id=:userId";
		Query query = sessionFactory.getCurrentSession().createQuery(allSites);
		query.setLong("userId", userId);
		return query.list();
	}


	@Override
	public List<Long> getAllCpIds() {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_ALL_CP_IDS_CONST);
		List result = query.list(); 
		List<Long> res = new ArrayList<Long>();
		for (Object object : result) {
			res.add(Long.valueOf(object.toString()));
		}
		return res;
	}


	@Override
	public List<Long> getCpIdBySiteId(Long siteId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_CP_IDS_BY_SITE_ID_CONST);
		query.setParameter("siteId", siteId);
		List result = query.list();
		List<Long> res = new ArrayList<Long>();
		for (Object object : result) {
			res.add(Long.valueOf(object.toString()));
		}
		return res;
	}
	
	@Override
	public List<Long> getSiteIdsByCpId(Long cpId){
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SITEIDS_BY_CP_ID_CONST);
		query.setParameter("cpId", cpId);
		List result = query.list();
		List<Long> res = new ArrayList<Long>();
		for (Object object : result) {
			res.add(Long.valueOf(object.toString()));
		}
		return res;
	}
	
	@Override
	public UserPrivDetail getUserPrivDetail(Long userId){
		UserPrivDetail detail = new UserPrivDetail();
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_USER_PRIV_DETAIL_CONST);
		query.setParameter("userId", userId);
		List result = query.list();
		List<Long> res = new ArrayList<Long>();
		Object[] obj = (Object[])result.get(0);
		detail.setCsmUserId(Long.valueOf(obj[0].toString()));
		detail.setLoginName((obj[1].toString()));
		return detail;
	}
	
	private static final String FQN = UserCPRole.class.getName();

	private static final String GET_USERCPROLE_BY_CP_AND_USER = FQN + ".getUserCPRoleByCpAndUser";

	private static final String GET_CPS_BY_USER_PRIVELEGE_CONST = FQN + ".getCPsByUserAndPrivConstant";
	
	private static final String GET_USER_PRIV_DETAIL_CONST = FQN + ".getUserPrivDetail";
	
	private static final String GET_SITEIDS_BY_CP_ID_CONST = FQN + ".getSiteIdsByCPId";
	
	private static final String GET_CP_IDS_BY_SITE_ID_CONST = FQN + ".getCPIdBySiteId";
	
	private static final String GET_ALL_CP_IDS_CONST = FQN + ".getAllCpIds";
	
	private static final String GET_CP_IDS_BY_USER_ID_CONST = FQN + ".getCPIdsByUserId";
	
	
	
	
}
