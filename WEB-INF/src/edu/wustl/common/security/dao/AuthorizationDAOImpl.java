/*
 * Created on Oct 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.common.security.dao;

import gov.nih.nci.security.authorization.ObjectPrivilegeMap;
import gov.nih.nci.security.authorization.domainobjects.Application;
import gov.nih.nci.security.authorization.domainobjects.Privilege;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.util.StringUtilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

/**
 * @author aarti_sharma
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class AuthorizationDAOImpl extends
		gov.nih.nci.security.dao.AuthorizationDAOImpl {

	static final Logger log = edu.wustl.common.util.logger.Logger.out;

	private SessionFactory sf = null;


	private String typeOfAccess = "MIXED";
	/**
	 * @param sf
	 * @param applicationContextName
	 */
	public AuthorizationDAOImpl(SessionFactory sf, String applicationContextName) {
		super(sf, applicationContextName);
		this.sf = sf;
		
	}

	public Collection getPrivilegeMap(String userName, Collection pEs)
			throws CSException {
		ArrayList result = new ArrayList();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		boolean test = false;
		Session s = null;

		Connection cn = null;

		if (StringUtilities.isBlank(userName)) {
			throw new CSException("userName can't be null!");
		}
		if (pEs == null) {
			throw new CSException(
					"protection elements collection can't be null!");
		}
		if (pEs.size() == 0) {
			return result;
		}

		try {

			s = sf.openSession();

			cn = s.connection();

			StringBuffer stbr = new StringBuffer();
			stbr.append("select distinct(p.privilege_name)");
			stbr.append(" from csm_protection_group pg,");
			stbr.append(" csm_protection_element pe,");
			stbr.append(" csm_pg_pe pgpe,");
			stbr.append(" csm_user_group_role_pg ugrpg,");
			stbr.append(" csm_user u,");
			stbr.append(" csm_group g,");
			stbr.append(" csm_user_group ug,");
			stbr.append(" csm_role_privilege rp,");
			stbr.append(" csm_privilege p ");
			stbr
					.append(" where pgpe.protection_group_id = pg.protection_group_id");
			stbr
					.append(" and pgpe.protection_element_id = pe.protection_element_id");
			stbr.append(" and pe.object_id= ?");
			
			stbr.append(" and pe.attribute=?");
			stbr
					.append(" and pg.protection_group_id = ugrpg.protection_group_id ");
			stbr.append(" and (( ugrpg.group_id = g.group_id");
			stbr.append("       and ug.user_id = u.user_id)");
			stbr.append("       or ");
			stbr.append("     (ugrpg.user_id = u.user_id))");
			stbr.append(" and u.login_name=?");
			stbr.append(" and ugrpg.role_id = rp.role_id ");
			stbr.append(" and rp.privilege_id = p.privilege_id");
			
			StringBuffer stbr2 = new StringBuffer();
			stbr2.append("select distinct(p.privilege_name)");
			stbr2.append(" from csm_protection_group pg,");
			stbr2.append(" csm_protection_element pe,");
			stbr2.append(" csm_pg_pe pgpe,");
			stbr2.append(" csm_user_group_role_pg ugrpg,");
			stbr2.append(" csm_user u,");
			stbr2.append(" csm_group g,");
			stbr2.append(" csm_user_group ug,");
			stbr2.append(" csm_role_privilege rp,");
			stbr2.append(" csm_privilege p ");
			stbr2
					.append(" where pgpe.protection_group_id = pg.protection_group_id");
			stbr2
					.append(" and pgpe.protection_element_id = pe.protection_element_id");
			stbr2.append(" and pe.object_id= ?");
			
			stbr2.append(" and pe.attribute IS NULL");
			stbr2
					.append(" and pg.protection_group_id = ugrpg.protection_group_id ");
			stbr2.append(" and (( ugrpg.group_id = g.group_id");
			stbr2.append("       and ug.user_id = u.user_id)");
			stbr2.append("       or ");
			stbr2.append("     (ugrpg.user_id = u.user_id))");
			stbr2.append(" and u.login_name=?");
			stbr2.append(" and ugrpg.role_id = rp.role_id ");
			stbr2.append(" and rp.privilege_id = p.privilege_id");

			String sql = stbr.toString();
			pstmt = cn.prepareStatement(sql);
			
			String sql2 = stbr2.toString();
			pstmt2 = cn.prepareStatement(sql2);

			Iterator it = pEs.iterator();
			while (it.hasNext()) {
				ProtectionElement pe = (ProtectionElement) it.next();
				ArrayList privs = new ArrayList();
				if (pe.getObjectId() != null) {
					
					if (pe.getAttribute() != null) {
						pstmt.setString(1, pe.getObjectId());
						pstmt.setString(2, pe.getAttribute());
						pstmt.setString(3, userName);
						rs = pstmt.executeQuery();
					} else {
						pstmt2.setString(1, pe.getObjectId());
						pstmt2.setString(2, userName);
						rs = pstmt2.executeQuery();
					}
					
				}

				

				while (rs.next()) {
					String priv = rs.getString(1);
					Privilege p = new Privilege();
					p.setName(priv);
					privs.add(p);
				}
				rs.close();
				ObjectPrivilegeMap opm = new ObjectPrivilegeMap(pe, privs);
				result.add(opm);
			}

			pstmt.close();

		} catch (Exception ex) {
			if (log.isDebugEnabled())
				log.debug("Failed to get privileges for " + userName + "|"
						+ ex.getMessage());
			throw new CSException("Failed to get privileges for " + userName
					+ "|" + ex.getMessage(), ex);
		} finally {
			try {

				s.close();
				rs.close();
				pstmt.close();
			} catch (Exception ex2) {
				if (log.isDebugEnabled())
					log
							.debug("Authorization|||getPrivilegeMap|Failure|Error in Closing Session |"
									+ ex2.getMessage());
			}
		}

		return result;
	}

}