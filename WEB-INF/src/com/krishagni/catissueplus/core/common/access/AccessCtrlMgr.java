package com.krishagni.catissueplus.core.common.access;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.biospecimen.ConfigParams;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CprErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitErrorCode;
import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.Operation;
import com.krishagni.catissueplus.core.common.events.Resource;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.rbac.common.errors.RbacErrorCode;
import com.krishagni.rbac.domain.Subject;
import com.krishagni.rbac.domain.SubjectAccess;
import com.krishagni.rbac.domain.SubjectRole;
import com.krishagni.rbac.repository.DaoFactory;

@Configurable
public class AccessCtrlMgr {

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

		if (user.isAdmin() && op != Operation.READ) {
			throw OpenSpecimenException.userError(RbacErrorCode.ADMIN_RIGHTS_REQUIRED);
		}
		
		if (!canUserPerformOp(AuthUtil.getCurrentUser().getId(), Resource.USER, new Operation[] {op})) {
			throw OpenSpecimenException.userError(RbacErrorCode.ACCESS_DENIED);
		}
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
	
	public void ensureReadDPRights(DistributionProtocol dp) {
		if (AuthUtil.isAdmin()) {
			return;
		}
		
		Set<Site> userSites = getSites(Resource.ORDER, new Operation[]{Operation.CREATE, Operation.UPDATE});
		if (CollectionUtils.intersection(userSites, dp.getDistributingSites()).isEmpty()) {
			throw OpenSpecimenException.userError(RbacErrorCode.ACCESS_DENIED);
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////
	//                                                                                  //
	//          Collection Protocol object access control helper methods                //
	//                                                                                  //
	//////////////////////////////////////////////////////////////////////////////////////
	public Set<Long> getReadableCpIds() {
		return getEligibleCpIds(Resource.CP.getName(), Operation.READ.getName(), null);
	}

	public Set<Long> getRegisterEnabledCpIds(List<String> siteNames) {
		return getEligibleCpIds(Resource.PARTICIPANT.getName(), Operation.CREATE.getName(), siteNames);
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
			} else if (accessSite == null && (accessCp == null || accessCp.equals(cp))) {
				//
				// All CPs or specific CP 
				//
				
				allowed = true;
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
		
		if (!isAccessRestrictedBasedOnMrn()) {
			return result;
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
			}
		}

		result.siteIds = siteIds;
		return result;
	}

	public boolean ensureCreateCprRights(Long cprId) {
		return ensureCprObjectRights(cprId, Operation.CREATE);
	}

	public boolean ensureCreateCprRights(CollectionProtocolRegistration cpr) {
		return ensureCprObjectRights(cpr, Operation.CREATE);
	}

	public void ensureReadCprRights(Long cprId) {
		ensureCprObjectRights(cprId, Operation.READ);
	}

	public boolean ensureReadCprRights(CollectionProtocolRegistration cpr) {
		return ensureCprObjectRights(cpr, Operation.READ);
	}

	public void ensureUpdateCprRights(Long cprId) {
		ensureCprObjectRights(cprId, Operation.UPDATE);
	}

	public boolean ensureUpdateCprRights(CollectionProtocolRegistration cpr) {
		return ensureCprObjectRights(cpr, Operation.UPDATE);
	}

	public void ensureDeleteCprRights(Long cprId) {
		ensureCprObjectRights(cprId, Operation.DELETE);
	}

	public boolean ensureDeleteCprRights(CollectionProtocolRegistration cpr) {
		return ensureCprObjectRights(cpr, Operation.DELETE);
	}

	private boolean ensureCprObjectRights(Long cprId, Operation op) {
		CollectionProtocolRegistration cpr = daoFactory.getCprDao().getById(cprId);
		if (cpr == null) {
			throw OpenSpecimenException.userError(CprErrorCode.NOT_FOUND);
		}

		return ensureCprObjectRights(cpr, op);
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
		
		if (!isAccessRestrictedBasedOnMrn()) {
			return true;
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
	public void ensureCreateOrUpdateVisitRights(Long visitId) {
		ensureVisitObjectRights(visitId, Operation.UPDATE);
	}

	public void ensureCreateOrUpdateVisitRights(Visit visit) {
		ensureVisitAndSpecimenObjectRights(visit.getRegistration(), Operation.UPDATE);
	}

	public void ensureReadVisitRights(Long visitId) {
		ensureVisitObjectRights(visitId, Operation.READ);
	}

	public void ensureReadVisitRights(Visit visit) {
		ensureReadVisitRights(visit.getRegistration());
	}

	public void ensureReadVisitRights(CollectionProtocolRegistration cpr) {
		ensureVisitAndSpecimenObjectRights(cpr, Operation.READ);
	}

	public void ensureDeleteVisitRights(Long visitId) {
		ensureVisitObjectRights(visitId, Operation.DELETE);
	}

	public void ensureDeleteVisitRights(Visit visit) {
		ensureVisitAndSpecimenObjectRights(visit.getRegistration(), Operation.DELETE);
	}

	public void ensureCreateOrUpdateSpecimenRights(Long specimenId) {
		ensureSpecimenObjectRights(specimenId, Operation.UPDATE);
	}

	public void ensureCreateOrUpdateSpecimenRights(Specimen specimen) {
		ensureVisitAndSpecimenObjectRights(specimen.getRegistration(), Operation.UPDATE);
	}

	public void ensureReadSpecimenRights(Long specimenId) {
		ensureSpecimenObjectRights(specimenId, Operation.READ);
	}

	public void ensureReadSpecimenRights(Specimen specimen) {
		ensureReadSpecimenRights(specimen.getRegistration());
	}

	public void ensureReadSpecimenRights(CollectionProtocolRegistration cpr) {
		ensureVisitAndSpecimenObjectRights(cpr, Operation.READ);
	}

	public void ensureDeleteSpecimenRights(Long specimenId) {
		ensureSpecimenObjectRights(specimenId, Operation.DELETE);
	}

	public void ensureDeleteSpecimenRights(Specimen specimen) {
		ensureVisitAndSpecimenObjectRights(specimen.getRegistration(), Operation.DELETE);
	}

	public List<Pair<Long, Long>> getReadAccessSpecimenSiteCps() {
		if (AuthUtil.isAdmin()) {
			return null;
		}

		String[] ops = {Operation.READ.getName()};
		Set<Pair<Long, Long>> siteCpPairs = getVisitAndSpecimenSiteCps(ops);
		siteCpPairs.addAll(getDistributionOrderSiteCps(ops));

		Set<Long> sitesOfAllCps = new HashSet<Long>();
		List<Pair<Long, Long>> result = new ArrayList<Pair<Long, Long>>();
		for (Pair<Long, Long> siteCp : siteCpPairs) {
			if (siteCp.second() == null) {
				sitesOfAllCps.add(siteCp.first());
				result.add(siteCp);
			}
		}


		for (Pair<Long, Long> siteCp : siteCpPairs) {
			if (sitesOfAllCps.contains(siteCp.first())) {
				continue;
			}

			result.add(siteCp);
		}

		return result;
	}

	private void ensureVisitObjectRights(Long visitId, Operation op) {
		Visit visit = daoFactory.getVisitDao().getById(visitId);
		if (visit == null) {
			throw OpenSpecimenException.userError(VisitErrorCode.NOT_FOUND);
		}

		ensureVisitAndSpecimenObjectRights(visit.getRegistration(), op);
	}

	private void ensureSpecimenObjectRights(Long specimenId, Operation op) {
		Specimen specimen = daoFactory.getSpecimenDao().getById(specimenId);
		if (specimen == null) {
			throw OpenSpecimenException.userError(SpecimenErrorCode.NOT_FOUND, specimenId);
		}

		ensureVisitAndSpecimenObjectRights(specimen.getRegistration(), op);
	}

	private void ensureVisitAndSpecimenObjectRights(CollectionProtocolRegistration cpr, Operation op) {
		if (AuthUtil.isAdmin()) {
			return;
		}

		String[] ops = null;
		if (op == Operation.CREATE || op == Operation.UPDATE) {
			ops = new String[]{Operation.CREATE.getName(), Operation.UPDATE.getName()};
		} else {
			ops = new String[]{op.getName()};
		}
		ensureSprOrVisitAndSpecimenObjectRights(cpr, Resource.VISIT_N_SPECIMEN, ops);
	}

	private Set<Pair<Long, Long>> getVisitAndSpecimenSiteCps(String[] ops) {
		Long userId = AuthUtil.getCurrentUser().getId();
		String resource = Resource.VISIT_N_SPECIMEN.getName();

		List<SubjectAccess> accessList = daoFactory.getSubjectDao().getAccessList(userId, resource, ops);
		Set<Pair<Long, Long>> siteCpPairs = new HashSet<Pair<Long, Long>>();
		for (SubjectAccess access : accessList) {
			Set<Site> sites = null;
			if (access.getSite() != null) {
				sites = Collections.singleton(access.getSite());
			} else {
				sites = getUserInstituteSites(userId);
			}

			Long cpId = null;
			if (access.getCollectionProtocol() != null) {
				cpId = access.getCollectionProtocol().getId();
			}

			for (Site site : sites) {
				siteCpPairs.add(Pair.make(site.getId(), cpId));
			}
		}

		return siteCpPairs;
	}

	//////////////////////////////////////////////////////////////////////////////////////
	//                                                                                  //
	//          Storage container object access control helper methods                  //
	//                                                                                  //
	//////////////////////////////////////////////////////////////////////////////////////
	public Set<Long> getReadAccessContainerSites() {
		if (AuthUtil.isAdmin()) {
			return null;
		}

		Set<Site> sites = getSites(Resource.STORAGE_CONTAINER, Operation.READ);
		Set<Long> result = new HashSet<Long>();
		for (Site site : sites) {
			result.add(site.getId());
		}

		return result;
	}

	public void ensureCreateContainerRights(StorageContainer container) {
		ensureStorageContainerObjectRights(container, Operation.CREATE);
	}

	public void ensureCreateContainerRights(Site containerSite) {
		ensureStorageContainerObjectRights(containerSite, Operation.CREATE);
	}

	public void ensureReadContainerRights(StorageContainer container) {
		ensureStorageContainerObjectRights(container, Operation.READ);
	}

	public void ensureUpdateContainerRights(StorageContainer container) {
		ensureStorageContainerObjectRights(container, Operation.UPDATE);
	}

	public void ensureDeleteContainerRights(StorageContainer container) {
		ensureStorageContainerObjectRights(container, Operation.DELETE);
	}

	private void ensureStorageContainerObjectRights(StorageContainer container, Operation op) {
		if (AuthUtil.isAdmin()) {
			return;
		}

		ensureStorageContainerObjectRights(container.getSite(), op);
	}

	private void ensureStorageContainerObjectRights(Site containerSite, Operation op) {
		if (AuthUtil.isAdmin()) {
			return;
		}

		Long userId = AuthUtil.getCurrentUser().getId();
		String resource = Resource.STORAGE_CONTAINER.getName();
		String[] ops = {op.getName()};

		boolean allowed = false;
		List<SubjectAccess> accessList = daoFactory.getSubjectDao().getAccessList(userId, resource, ops);
		if (accessList.isEmpty()) {
			throw OpenSpecimenException.userError(RbacErrorCode.ACCESS_DENIED);
		}

		for (SubjectAccess access : accessList) {
			Site accessSite = access.getSite();
			if (accessSite != null && accessSite.equals(containerSite)) { // Specific site
				allowed = true;
			} else if (accessSite == null) { // All user institute sites
				Set<Site> instituteSites = getUserInstituteSites(userId);
				if (instituteSites.contains(containerSite)) {
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
	//          Distribution order access control helper methods                        //
	//                                                                                  //
	//////////////////////////////////////////////////////////////////////////////////////
	
	public Set<Long> getReadAccessDistributionOrderSites() {
		if (AuthUtil.isAdmin()) {
			return null;
		}
		
		return Utility.<Set<Long>>collect(getSites(Resource.ORDER, Operation.READ), "id", true);
	}
	
	public boolean canCreateUpdateDistributionOrder() {
		if (AuthUtil.isAdmin()) {
			return true;
		}
		
		Long userId = AuthUtil.getCurrentUser().getId();
		return canUserPerformOp(userId, Resource.ORDER, new Operation[] {Operation.CREATE, Operation.UPDATE});
	}
	
	public Set<Long> getCreateUpdateAccessDistributionOrderSites() {
		if (AuthUtil.isAdmin()) {
			return null;
		}
		
		return Utility.<Set<Long>>collect(getSites(Resource.ORDER, new Operation[]{Operation.CREATE, Operation.UPDATE}), "id", true);
	}
	
	@SuppressWarnings("unchecked")
	public Set<Long> getDistributionOrderAllowedSites(DistributionProtocol dp) {
		Set<Site> allowedSites = null;
		if (AuthUtil.isAdmin()) {
			allowedSites = dp.getDistributingSites();
		} else {
			Set<Site> userSites = getSites(Resource.ORDER, new Operation[]{Operation.CREATE, Operation.UPDATE});
			allowedSites = new HashSet<Site>(CollectionUtils.intersection(userSites, dp.getDistributingSites()));
		}
		
		return Utility.<Set<Long>>collect(allowedSites, "id", true);
	}

	public void ensureCreateDistributionOrderRights(DistributionOrder order) {
		ensureDistributionOrderObjectRights(order, Operation.CREATE);
	}

	public void ensureReadDistributionOrderRights(DistributionOrder order) {
		ensureDistributionOrderObjectRights(order, Operation.READ);
	}

	public void ensureUpdateDistributionOrderRights(DistributionOrder order) {
		ensureDistributionOrderObjectRights(order, Operation.UPDATE);
	}

	public void ensureDeleteDistributionOrderRights(DistributionOrder order) {
		ensureDistributionOrderObjectRights(order, Operation.DELETE);
	}
	
	private void ensureDistributionOrderObjectRights(DistributionOrder order, Operation operation) {
		if (AuthUtil.isAdmin()) {
			return;
		}
		
		if (CollectionUtils.intersection(
				getSites(Resource.ORDER, operation),
				order.getDistributionProtocol().getDistributingSites()).isEmpty()) {
			
			throw OpenSpecimenException.userError(RbacErrorCode.ACCESS_DENIED);
		}
	}

	private Set<Pair<Long, Long>> getDistributionOrderSiteCps(String[] ops) {
		Long userId = AuthUtil.getCurrentUser().getId();
		String resource = Resource.ORDER.getName();

		List<SubjectAccess> accessList = daoFactory.getSubjectDao().getAccessList(userId, resource, ops);
		Set<Pair<Long, Long>> siteCpPairs = new HashSet<Pair<Long, Long>>();
		for (SubjectAccess access : accessList) {
			Set<Site> sites = null;
			if (access.getSite() != null) {
				sites = access.getSite().getInstitute().getSites();
			} else {
				sites = getUserInstituteSites(userId);
			}

			for (Site site : sites) {
				siteCpPairs.add(Pair.make(site.getId(), (Long) null));
			}
		}

		return siteCpPairs;
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
	
	public Set<Site> getSites(Resource resource, Operation op) {
		return getSites(resource, new Operation[]{op});
	}

	public Set<Site> getSites(Resource resource, Operation[] operations) {
		User user = AuthUtil.getCurrentUser();
		String[] ops = new String[operations.length];
		for(int i = 0; i < operations.length; i++ ) {
			ops[i] = operations[i].getName();
		}
		
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
	
	public Set<Long> getEligibleCpIds(String resource, String op, List<String> siteNames) {
		if (AuthUtil.isAdmin()) {
			return null;
		}

		Long userId = AuthUtil.getCurrentUser().getId();
		String[] ops = {op};

		List<SubjectAccess> accessList = null;
		if (CollectionUtils.isEmpty(siteNames)) {
			accessList = daoFactory.getSubjectDao().getAccessList(userId, resource, ops);
		} else {
			accessList = daoFactory.getSubjectDao().getAccessList(userId, resource, ops, siteNames.toArray(new String[0]));
		}

		Set<Long> cpIds = new HashSet<Long>();
		Set<Long> cpOfSites = new HashSet<Long>();
		for (SubjectAccess access : accessList) {
			if (access.getSite() != null && access.getCollectionProtocol() != null) {
				cpIds.add(access.getCollectionProtocol().getId());
			} else if (access.getSite() != null) {
				cpOfSites.add(access.getSite().getId());
			} else if (access.getCollectionProtocol() != null) {
				cpIds.add(access.getCollectionProtocol().getId());
			} else  {
				Collection<Site> sites = getUserInstituteSites(userId);
				for (Site site : sites) {
					if (CollectionUtils.isEmpty(siteNames) || siteNames.contains(site.getName())) {
						cpOfSites.add(site.getId());
					}
				}
			}
		}

		if (!cpOfSites.isEmpty()) {
			cpIds.addAll(daoFactory.getCollectionProtocolDao().getCpIdsBySiteIds(cpOfSites));
		}

		return cpIds;
	}

	private Set<Site> getUserInstituteSites(Long userId) {
		return getUserInstitute(userId).getSites();
	}

	private Institute getUserInstitute(Long userId) {
		User user = userDao.getById(userId);
		return user.getInstitute();
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

	//////////////////////////////////////////////////////////////////////////////////////
	//                                                                                  //
	//          Surgical pathology report access control helper methods                 //
	//                                                                                  //
	//////////////////////////////////////////////////////////////////////////////////////

	public void ensureCreateOrUpdateSprRights(Visit visit) {
		ensureSprObjectRights(visit, Operation.UPDATE);
	}

	public void ensureDeleteSprRights(Visit visit) {
		ensureSprObjectRights(visit, Operation.DELETE);
	}

	public void ensureReadSprRights(Visit visit) {
		ensureSprObjectRights(visit, Operation.READ);
	}

	public void ensureLockSprRights(Visit visit) {
		ensureSprObjectRights(visit, Operation.LOCK);
	}

	public void ensureUnlockSprRights(Visit visit) {
		ensureSprObjectRights(visit, Operation.UNLOCK);
	}

	private void ensureSprObjectRights(Visit visit, Operation op) {
		if (AuthUtil.isAdmin()) {
			return;
		}

		if (op == Operation.LOCK || op == Operation.UNLOCK) {
			ensureCreateOrUpdateVisitRights(visit);
		} else {
			ensureVisitObjectRights(visit.getId(), op);
		}
		CollectionProtocolRegistration cpr = visit.getRegistration();
		String[] ops = {op.getName()};
		ensureSprOrVisitAndSpecimenObjectRights(cpr, Resource.SURGICAL_PATHOLOGY_REPORT, ops);
	}

	private void ensureSprOrVisitAndSpecimenObjectRights(CollectionProtocolRegistration cpr, Resource resource, String[] ops) {
		Long userId = AuthUtil.getCurrentUser().getId();
		Long cpId = cpr.getCollectionProtocol().getId();
		List<SubjectAccess> accessList = daoFactory.getSubjectDao().getAccessList(userId, cpId, resource.getName(), ops);
		if (accessList.isEmpty()) {
			throw OpenSpecimenException.userError(RbacErrorCode.ACCESS_DENIED);
		}

		if (!isAccessRestrictedBasedOnMrn()) {
			return;
		}
		
		Set<Site> mrnSites = cpr.getParticipant().getMrnSites();
		if (mrnSites.isEmpty()) {
			return;
		}

		boolean allowed = false;
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

	//////////////////////////////////////////////////////////////////////////////////////
	//                                                                                  //
	//          Scheduled Job object access control helper methods              //
	//                                                                                  //
	//////////////////////////////////////////////////////////////////////////////////////
	public void ensureReadScheduledJobRights() {
		Operation[] ops = {Operation.READ};
		ensureScheduledJobRights(ops);
	}

	public void ensureRunJobRights() {
		Operation[] ops = {Operation.READ};
		ensureScheduledJobRights(ops);
	}

	public void ensureCreateScheduledJobRights() {
		Operation[] ops = {Operation.CREATE};
		ensureScheduledJobRights(ops);
	}

	public void ensureUpdateScheduledJobRights() {
		Operation[] ops = {Operation.UPDATE};
		ensureScheduledJobRights(ops);
	}

	public void ensureDeleteScheduledJobRights() {
		Operation[] ops = {Operation.DELETE};
		ensureScheduledJobRights(ops);
	}

	public void ensureScheduledJobRights(Operation[] ops) {
		if (AuthUtil.isAdmin()) {
			return;
		}

		User user = AuthUtil.getCurrentUser();
		if (!canUserPerformOp(user.getId(), Resource.SCHEDULED_JOB, ops)) {
			throw OpenSpecimenException.userError(RbacErrorCode.ACCESS_DENIED);
		}
	}
	
	private Boolean isAccessRestrictedBasedOnMrn() {
		return ConfigUtil.getInstance().getBoolSetting(
				ConfigParams.MODULE,
				ConfigParams.MRN_RESTRICTION_ENABLED, 
				false);
	}
}
