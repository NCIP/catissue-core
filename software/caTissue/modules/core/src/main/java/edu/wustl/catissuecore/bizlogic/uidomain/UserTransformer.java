package edu.wustl.catissuecore.bizlogic.uidomain;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.UserForm;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;
import edu.wustl.common.bizlogic.UIDomainTransformer;
import edu.wustl.common.util.global.Status;

@InputUIRepOfDomain(UserForm.class)
public class UserTransformer implements UIDomainTransformer<UserForm, User> {

	public User createDomainObject(UserForm uiRepOfDomain) {
		InstanceFactory<User> instFact = DomainInstanceFactory.getInstanceFactory(User.class);
		User user = instFact.createObject();
		overwriteDomainObject(user, uiRepOfDomain);
		return user;
	}

	public void overwriteDomainObject(User domainObject, UserForm uiRepOfDomain) {
		// try
		// {
			if (SearchUtil.isNullobject(domainObject.getLastName())) {
				domainObject.setLastName(Constants.DOUBLE_QUOTES);
			}

			if (SearchUtil.isNullobject(domainObject.getFirstName())) {
				domainObject.setFirstName(Constants.DOUBLE_QUOTES);
			}

			if (SearchUtil.isNullobject(domainObject.getLoginName())) {
				domainObject.setLoginName(Constants.DOUBLE_QUOTES);
			}

			if (SearchUtil.isNullobject(domainObject.getEmailAddress())) {
				domainObject.setEmailAddress(Constants.DOUBLE_QUOTES);
			}

			if (SearchUtil.isNullobject(domainObject.getAddress())) {
				//domainObject.setAddress(new Address());
				InstanceFactory<Address> instFact = DomainInstanceFactory.getInstanceFactory(Address.class);
				domainObject.setAddress(instFact.createObject());
			}

			if (SearchUtil.isNullobject(domainObject.getInstitution())) {
				//domainObject.setInstitution(new Institution());
				InstanceFactory<Institution> instFact = DomainInstanceFactory.getInstanceFactory(Institution.class);
				domainObject.setInstitution(instFact.createObject());
			}

			if (SearchUtil.isNullobject(domainObject.getDepartment())) {
				//domainObject.setDepartment(new Department());
				InstanceFactory<Department> instFact = DomainInstanceFactory.getInstanceFactory(Department.class);
				domainObject.setDepartment(instFact.createObject());
			}

			if (SearchUtil.isNullobject(domainObject.getCancerResearchGroup())) {
				//domainObject.setCancerResearchGroup(new CancerResearchGroup());
				InstanceFactory<CancerResearchGroup> instFact = DomainInstanceFactory.getInstanceFactory(CancerResearchGroup.class);
				domainObject.setCancerResearchGroup(instFact.createObject());
			}

			if (SearchUtil.isNullobject(domainObject.getFirstTimeLogin())) {
				domainObject.setFirstTimeLogin(Boolean.TRUE);
			}


			if (uiRepOfDomain.getPageOf().equals(Constants.PAGE_OF_CHANGE_PASSWORD)) {
				uiRepOfDomain.setNewPassword(uiRepOfDomain.getNewPassword());
				uiRepOfDomain.setOldPassword(uiRepOfDomain.getOldPassword());
			} else {

				if (!uiRepOfDomain.getPageOf().equalsIgnoreCase("pageOfSignUp")) {
					final String[] siteIds = uiRepOfDomain.getSiteIds();
					if (siteIds != null && siteIds.length != 0) {
						final Collection<Site> newSiteCollection = new HashSet<Site> ();
						for (final String siteId : siteIds) {
							  InstanceFactory<Site> instFact = DomainInstanceFactory.getInstanceFactory(Site.class);
							final Site site =instFact.createObject();
							site.setId(Long.valueOf(siteId));
							newSiteCollection.add(site);
						}
						if(domainObject.getSiteCollection()==null)
						{
							domainObject.setSiteCollection(new HashSet<Site>());
						}
						domainObject.getSiteCollection().clear();
						domainObject.getSiteCollection().addAll(newSiteCollection);
					}
				}
				domainObject.setId(Long.valueOf(uiRepOfDomain.getId()));
				domainObject.setLoginName(uiRepOfDomain.getEmailAddress());
				domainObject.setLastName(uiRepOfDomain.getLastName());
				domainObject.setFirstName(uiRepOfDomain.getFirstName());
				domainObject.setEmailAddress(uiRepOfDomain.getEmailAddress());
				domainObject.setRoleId(uiRepOfDomain.getRole());
				domainObject.getInstitution().setId(Long.valueOf(uiRepOfDomain.getInstitutionId()));

				domainObject.getDepartment().setId(Long.valueOf(uiRepOfDomain.getDepartmentId()));
				domainObject.getCancerResearchGroup().setId(Long.valueOf(uiRepOfDomain.getCancerResearchGroupId()));
				if (Constants.PAGE_OF_USER_PROFILE.equals(uiRepOfDomain.getPageOf()) == Boolean.FALSE) {
					domainObject.setActivityStatus(uiRepOfDomain.getActivityStatus());
				}

				if (uiRepOfDomain.getPageOf().equals(Constants.PAGE_OF_SIGNUP))
				{
					domainObject.setStartDate(Calendar.getInstance().getTime());
				}

				if (!uiRepOfDomain.getPageOf().equals(Constants.PAGE_OF_SIGNUP) && !uiRepOfDomain.getPageOf().equals(Constants.PAGE_OF_USER_PROFILE))
				{
					domainObject.setComments(uiRepOfDomain.getComments());
				}

				if (uiRepOfDomain.getPageOf().equals(Constants.PAGE_OF_USER_ADMIN)
						&& uiRepOfDomain.getOperation().equals(Constants.ADD))
				{
					domainObject.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
					domainObject.setStartDate(Calendar.getInstance().getTime());
				}

				// Bug-1516: Jitendra
				if (uiRepOfDomain.getPageOf().equals(Constants.PAGE_OF_USER_ADMIN)
						&& uiRepOfDomain.getOperation().equals(Constants.EDIT))
				{
					uiRepOfDomain.setNewPassword(uiRepOfDomain.getNewPassword());
				}

				if (uiRepOfDomain.getPageOf().equals(Constants.PAGE_OF_APPROVE_USER))
				{
					if (uiRepOfDomain.getStatus().equals(Status.APPROVE_USER_APPROVE_STATUS.toString()))
					{
						domainObject.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
					}
					else if (uiRepOfDomain.getStatus().equals(Status.APPROVE_USER_REJECT_STATUS.toString()))
					{
						domainObject.setActivityStatus(Status.ACTIVITY_STATUS_REJECT.toString());
					}
					else
					{
						domainObject.setActivityStatus(Status.ACTIVITY_STATUS_PENDING.toString());
					}
				}

				domainObject.setRoleId(uiRepOfDomain.getRole());
				domainObject.getAddress().setStreet(uiRepOfDomain.getStreet());
				domainObject.getAddress().setCity(uiRepOfDomain.getCity());
				domainObject.getAddress().setState(uiRepOfDomain.getState());
				domainObject.getAddress().setCountry(uiRepOfDomain.getCountry());
				domainObject.getAddress().setZipCode(uiRepOfDomain.getZipCode());
				domainObject.getAddress().setPhoneNumber(uiRepOfDomain.getPhoneNumber());
				domainObject.getAddress().setFaxNumber(uiRepOfDomain.getFaxNumber());

				if (Constants.PAGE_OF_USER_ADMIN.equals(uiRepOfDomain.getPageOf()))
				{
					domainObject.setCsmUserId(uiRepOfDomain.getCsmUserId());
				}

			}

			if(uiRepOfDomain.isDirtyEditFlag()){
				domainObject.setDirtyEditFlag(new Boolean(true));
	        }else {
	        	domainObject.setDirtyEditFlag(new Boolean(false));
	        }
	        if(uiRepOfDomain.isRemoteManagedFlag()){
	        	domainObject.setRemoteManagedFlag(new Boolean(true));
	        }else {
	        	domainObject.setRemoteManagedFlag(new Boolean(false));
	        }
	        if(uiRepOfDomain.getRemoteId() !=0) {
	        	domainObject.setRemoteId(uiRepOfDomain.getRemoteId());
	        }else {
	        	domainObject.setRemoteId(null);
	        }

	}
}
