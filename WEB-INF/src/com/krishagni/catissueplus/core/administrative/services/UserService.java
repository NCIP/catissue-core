
package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;

import com.krishagni.catissueplus.core.administrative.events.AnnouncementDetail;
import com.krishagni.catissueplus.core.administrative.events.InstituteDetail;
import com.krishagni.catissueplus.core.administrative.events.PasswordDetails;
import com.krishagni.catissueplus.core.administrative.events.UserDetail;
import com.krishagni.catissueplus.core.administrative.repository.UserListCriteria;
import com.krishagni.catissueplus.core.common.events.DeleteEntityOp;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.rbac.events.SubjectRoleDetail;

public interface UserService extends UserDetailsService, SAMLUserDetailsService {
	public ResponseEvent<List<UserSummary>> getUsers(RequestEvent<UserListCriteria> req);
	
	public ResponseEvent<Long> getUsersCount(RequestEvent<UserListCriteria> req);
	
	public ResponseEvent<UserDetail> getUser(RequestEvent<Long> req);

	public ResponseEvent<UserDetail> createUser(RequestEvent<UserDetail> req);

	public ResponseEvent<UserDetail> updateUser(RequestEvent<UserDetail> req);
	
	public ResponseEvent<UserDetail> patchUser(RequestEvent<UserDetail> req);

	public ResponseEvent<UserDetail> updateStatus(RequestEvent<UserDetail> req);

	public ResponseEvent<Boolean> resetPassword(RequestEvent<PasswordDetails> req);

	public ResponseEvent<Boolean> changePassword(RequestEvent<PasswordDetails> req);

	public ResponseEvent<Boolean> forgotPassword(RequestEvent<String> req);
	
	public ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<Long> req);

	public ResponseEvent<UserDetail> deleteUser(RequestEvent<DeleteEntityOp> req);

	public ResponseEvent<List<SubjectRoleDetail>> getCurrentUserRoles();

	public ResponseEvent<InstituteDetail> getInstitute(RequestEvent<Long> req);

	public ResponseEvent<Boolean> broadcastAnnouncement(RequestEvent<AnnouncementDetail> req);
}
