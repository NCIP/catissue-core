package com.krishagni.catissueplus.core.common.access;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.Operation;
import com.krishagni.catissueplus.core.common.events.Resource;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.rbac.common.errors.RbacErrorCode;
import com.krishagni.rbac.domain.Subject;
import com.krishagni.rbac.domain.SubjectAccess;
import com.krishagni.rbac.domain.SubjectRole;
import com.krishagni.rbac.repository.DaoFactory;
import com.krishagni.rbac.service.RbacService;

@Configurable
public class AccessCtrlMgr {

	@Autowired
	private RbacService rbacService;
	
	@Autowired
	private DaoFactory daoFactory;
	
	@Autowired
	private UserDao userDao;
	
	private static AccessCtrlMgr instance;
	
	private AccessCtrlMgr() {
		
	}
	
	public static AccessCtrlMgr getInstance() {
		if (instance == null) {
			instance = new AccessCtrlMgr();
		}
		
		return instance;
	}
	
	public void ensureUserIsAdmin() {
		User user = AuthUtil.getCurrentUser();
		
		if (!user.isAdmin()) {
			throw OpenSpecimenException.userError(RbacErrorCode.ADMIN_RIGHTS_REQUIRED);
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////
	//                                                                                  //
	//          User object access control helper methods                               //
	//                                                                                  //
	//////////////////////////////////////////////////////////////////////////////////////
	public void ensureCreateUserRights(User user) {
		ensureUserObjectRights(user, Operation.CREATE);
	}
	
	public void ensureUpdateUserRights(User user) {
		ensureUserObjectRights(user, Operation.UPDATE);
	}
	
	public void ensureDeleteUserRights(User user) {
		ensureUserObjectRights(user, Operation.DELETE);
	}
	
	private void ensureUserObjectRights(User user, Operation op) {
		if (AuthUtil.isAdmin()) {
			return;
		}
		
		Set<Site> sites = getSites(Resource.USER, op);
		for (Site site : sites) {
			if (site.getInstitute().equals(user.getInstitute())) {
				return;
			}
		}
		
		throw OpenSpecimenException.userError(RbacErrorCode.ACCESS_DENIED);		
	}

	//////////////////////////////////////////////////////////////////////////////////////
	//                                                                                  //
	//          Distribution Protocol object access control helper methods              //
	//                                                                                  //
	//////////////////////////////////////////////////////////////////////////////////////
	public void ensureReadDpRights() {
		if (AuthUtil.isAdmin()) {
			return;
		} 
		
		User user = AuthUtil.getCurrentUser();
		Operation[] ops = {Operation.CREATE, Operation.UPDATE};
		if (!canUserPerformOp(user.getId(), Resource.ORDER, ops)) {
			throw OpenSpecimenException.userError(RbacErrorCode.ACCESS_DENIED);
		}		
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	//                                                                                  //
	//          Collection Protocol object access control helper methods                //
	//                                                                                  //
	//////////////////////////////////////////////////////////////////////////////////////
	public Set<Long> getReadableCpIds() {
		if (AuthUtil.isAdmin()) {
			return null;
		}
		
		Long userId = AuthUtil.getCurrentUser().getId();
		String resource = Resource.CP.getName();
		String[] ops = {Operation.READ.getName()};
		List<SubjectAccess> accessList = daoFactory.getSubjectDao().getAccessList(userId, resource, ops);
		
		Set<Long> readableCpIds = new HashSet<Long>();
		Set<Long> cpOfSites = new HashSet<Long>();
		for (SubjectAccess access : accessList) {
			if (access.getSite() != null && access.getCollectionProtocol() != null) {
				readableCpIds.add(access.getCollectionProtocol().getId());
			} else if (access.getSite() != null) {
				cpOfSites.add(access.getSite().getId());
			} else {
				Set<Site> sites = getUserInstituteSites(userId);
				for (Site site : sites) {
					cpOfSites.add(site.getId());
				}
				break;
			}
		}
		
		if (!cpOfSites.isEmpty()) {
			readableCpIds.addAll(daoFactory.getCollectionProtocolDao().getCpIdsBySiteIds(cpOfSites));
		}
		
		return readableCpIds;
	}
	
	public void ensureCreateCpRights(CollectionProtocol cp) {
		ensureCpObjectRights(cp, Operation.CREATE);
	}
	
	public void ensureReadCpRights(CollectionProtocol cp) {
		ensureCpObjectRights(cp, Operation.READ);
	}

	public void ensureUpdateCpRights(CollectionProtocol cp) {
		ensureCpObjectRights(cp, Operation.UPDATE);
	}

	public void ensureDeleteCpRights(CollectionProtocol cp) {
		ensureCpObjectRights(cp, Operation.DELETE);
	}
	
	private void ensureCpObjectRights(CollectionProtocol cp, Operation op) {
		if (AuthUtil.isAdmin()) {
			return;
		}
		
		Long userId = AuthUtil.getCurrentUser().getId();		
		String resource = Resource.CP.getName();
		String[] ops = {op.getName()};
		
		boolean allowed = false;
		List<SubjectAccess> accessList = daoFactory.getSubjectDao().getAccessList(userId, resource, ops);		
		for (SubjectAccess access : accessList) {
			Site accessSite = access.getSite();
			CollectionProtocol accessCp = access.getCollectionProtocol();
			
			if (accessSite != null && accessCp != null && accessCp.equals(cp)) {
				//
				// Specific CP
				//
				allowed = true;
			} else if (accessSite != null && accessCp == null && cp.getRepositories().contains(accessSite)) {
				//
				// TODO: 
				// Current implementation is at least one site is CP repository. We do not check whether permission is
				// for all CP repositories.
				//
				// All CPs of a site
				//
				allowed = true;
			} else if (accessSite == null && accessCp == null) {
				//
				// All CPs of all sites of user institute
				//
				Set<Site> instituteSites = getUserInstituteSites(userId);
				if (CollectionUtils.containsAny(instituteSites, cp.getRepositories())) {
					allowed = true;
				}
			}
			
			if (allowed) {
				break;
			}
		}
		
		if (!allowed) {
			throw OpenSpecimenException.userError(RbacErrorCode.ACCESS_DENIED);
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////
	//                                                                                  //
	//          Participant object access control helper methods                        //
	//                                                                                  //
	//////////////////////////////////////////////////////////////////////////////////////
	public static class ParticipantReadAccess {
		public boolean admin;
		
		public Set<Long> siteIds;
		
		public boolean phiAccess;		
	}
	
	public ParticipantReadAccess getParticipantReadAccess(Long cpId) {
		ParticipantReadAccess result = new ParticipantReadAccess();
		result.phiAccess = true;
		
		if (AuthUtil.isAdmin()) {
			result.admin = true;
			return result;
		}
		
		Long userId = AuthUtil.getCurrentUser().getId();
		String resource = Resource.PARTICIPANT.getName();
		String[] ops = {Operation.READ.getName()};
		List<SubjectAccess> accessList = daoFactory.getSubjectDao().getAccessList(userId, cpId, resource, ops);
		if (accessList.isEmpty()) {
			resource = Resource.PARTICIPANT_DEID.getName();
			accessList = daoFactory.getSubjectDao().getAccessList(userId, cpId, resource, ops);
			result.phiAccess = false;
		}
		
		Set<Long> siteIds = new HashSet<Long>();		
		for (SubjectAccess access : accessList) {
			Site accessSite = access.getSite();
			if (accessSite != null) {
				siteIds.add(accessSite.getId());
			} else if (accessSite == null) {
				Set<Site> sites = getUserInstituteSites(userId);
				for (Site site : sites) {
					siteIds.add(site.getId());
				}
				break;
			}
		}

		result.siteIds = siteIds;
		return result;
	}
	
	public boolean ensureCreateCprRights(CollectionProtocolRegistration cpr) {
		return ensureCprObjectRights(cpr, Operation.CREATE);
	}

	public boolean ensureReadCprRights(CollectionProtocolRegistration cpr) {
		return ensureCprObjectRights(cpr, Operation.READ);
	}

	public boolean ensureUpdateCprRights(CollectionProtocolRegistration cpr) {
		return ensureCprObjectRights(cpr, Operation.UPDATE);
	}

	public boolean ensureDeleteCprRights(CollectionProtocolRegistration cpr) {
		return ensureCprObjectRights(cpr, Operation.DELETE);
	}
	
	private boolean ensureCprObjectRights(CollectionProtocolRegistration cpr, Operation op) {
		if (AuthUtil.isAdmin()) {
			return true;
		}
		
		Long userId = AuthUtil.getCurrentUser().getId();
		boolean phiAccess = true;
		String resource = Resource.PARTICIPANT.getName();
		String[] ops = {op.getName()};
		
		boolean allowed = false;
		Long cpId = cpr.getCollectionProtocol().getId();		
		List<SubjectAccess> accessList = daoFactory.getSubjectDao().getAccessList(userId, cpId, resource, ops);		
		if (accessList.isEmpty() && op == Operation.READ) {
			phiAccess = false;
			resource = Resource.PARTICIPANT_DEID.getName();
			accessList = daoFactory.getSubjectDao().getAccessList(userId, cpId, resource, ops);
		}
		
		if (accessList.isEmpty()) {
			throw OpenSpecimenException.userError(RbacErrorCode.ACCESS_DENIED);
		}
		
		Set<Site> mrnSites = cpr.getParticipant().getMrnSites();
		if (mrnSites.isEmpty()) {
			return phiAccess;
		}
		
		for (SubjectAccess access : accessList) {
			Site accessSite = access.getSite();
			if (accessSite != null && mrnSites.contains(accessSite)) { // Specific site
				allowed = true;
			} else if (accessSite == null) { // All user institute sites
				Set<Site> instituteSites = getUserInstituteSites(userId);
				if (CollectionUtils.containsAny(instituteSites, mrnSites)) {
					allowed = true;
				}
			}
			
			if (allowed) {
				break;
			}
		}
		
		if (!allowed) {
			throw OpenSpecimenException.userError(RbacErrorCode.ACCESS_DENIED);
		}
		
		return phiAccess;
	}

	//////////////////////////////////////////////////////////////////////////////////////
	//                                                                                  //
	//          Visit and Specimen object access control helper methods                 //
	//                                                                                  //
	//////////////////////////////////////////////////////////////////////////////////////
	public void ensureCreateOrUpdateVisitRights(Visit visit) {
		ensureVisitAndSpecimenObjectRights(visit.getRegistration(), Operation.UPDATE);
	}

	public void ensureReadVisitRights(Visit visit) {
		ensureReadVisitRights(visit.getRegistration());
	}
	
	public void ensureReadVisitRights(CollectionProtocolRegistration cpr) {
		ensureVisitAndSpecimenObjectRights(cpr, Operation.READ);
	}
		
	public void ensureDeleteVisitRights(Visit visit) {
		ensureVisitAndSpecimenObjectRights(visit.getRegistration(), Operation.DELETE);
	}
	
	public void ensureCreateOrUpdateSpecimenRights(Specimen specimen) {
		ensureVisitAndSpecimenObjectRights(specimen.getRegistration(), Operation.UPDATE);
	}
	
	public void ensureReadSpecimenRights(Specimen specimen) {
		ensureReadSpecimenRights(specimen.getRegistration());
	}
	
	public void ensureReadSpecimenRights(CollectionProtocolRegistration cpr) {
		ensureVisitAndSpecimenObjectRights(cpr, Operation.READ);
	}
	
	public void ensureDeleteSpecimenRights(Specimen specimen) {
		ensureVisitAndSpecimenObjectRights(specimen.getRegistration(), Operation.DELETE);
	}
		
	private void ensureVisitAndSpecimenObjectRights(CollectionProtocolRegistration cpr, Operation op) {
		if (AuthUtil.isAdmin()) {
			return;
		}
		
		Long userId = AuthUtil.getCurrentUser().getId();
		String resource = Resource.VISIT_N_SPECIMEN.getName();
		String[] ops = null;
		if (op == Operation.CREATE || op == Operation.UPDATE) {
			ops = new String[]{Operation.CREATE.getName(), Operation.UPDATE.getName()};
		} else {
			ops = new String[]{op.getName()};
		}
		
		boolean allowed = false;
		Long cpId = cpr.getCollectionProtocol().getId();		
		List<SubjectAccess> accessList = daoFactory.getSubjectDao().getAccessList(userId, cpId, resource, ops);		
		if (accessList.isEmpty()) {
			throw OpenSpecimenException.userError(RbacErrorCode.ACCESS_DENIED);
		}
		
		Set<Site> mrnSites = cpr.getParticipant().getMrnSites();
		if (mrnSites.isEmpty()) {
			return;
		}
		
		for (SubjectAccess access : accessList) {
			Site accessSite = access.getSite();
			if (accessSite != null && mrnSites.contains(accessSite)) { // Specific site
				allowed = true;
			} else if (accessSite == null) { // All user institute sites
				Set<Site> instituteSites = getUserInstituteSites(userId);
				if (CollectionUtils.containsAny(instituteSites, mrnSites)) {
					allowed = true;
				}
			}
			
			if (allowed) {
				break;
			}
		}
		
		if (!allowed) {
			throw OpenSpecimenException.userError(RbacErrorCode.ACCESS_DENIED);
		}
	}
	
	public Set<Site> getRoleAssignedSites() {
		User user = AuthUtil.getCurrentUser();		
		Subject subject = daoFactory.getSubjectDao().getById(user.getId());
				
		Set<Site> results = new HashSet<Site>();
		boolean allSites = false;
		for (SubjectRole role : subject.getRoles()) {
			if (role.getSite() == null) {
				allSites = true;
				break;
			}
			
			results.add(role.getSite());
		}
		
		if (allSites) {
			results.clear();
			results.addAll(getUserInstituteSites(user.getId()));
		}

		return results;
	}
	
	private Set<Site> getSites(Resource resource, Operation operation) {
		User user = AuthUtil.getCurrentUser();
		String[] ops = {operation.getName()};
		List<SubjectAccess> accessList = daoFactory.getSubjectDao().getAccessList(user.getId(), resource.getName(), ops);
		
		Set<Site> results = new HashSet<Site>();
		boolean allSites = false;
		for (SubjectAccess access : accessList) {
			if (access.getSite() == null) {
				allSites = true;
				break;
			}
			
			results.add(access.getSite());
		}
		
		if (allSites) {
			results.clear();
			results.addAll(getUserInstituteSites(user.getId()));
		}
		
		return results;
	}
	

	private Set<Site> getUserInstituteSites(Long userId) {
		User user = userDao.getById(userId); 
		return user.getInstitute().getSites();		
	}
	
	private boolean canUserPerformOp(Long userId, Resource resource, Operation[] operations) {
		List<String> ops = new ArrayList<String>();
		for (Operation operation : operations) {
			ops.add(operation.getName());
		}
		
		return daoFactory.getSubjectDao().canUserPerformOps(
				userId, 
				resource.getName(), 
				ops.toArray(new String[0]));
	}	
}
