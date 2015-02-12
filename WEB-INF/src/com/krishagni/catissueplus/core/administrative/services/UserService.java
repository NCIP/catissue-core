
package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.ListUserCriteria;
import com.krishagni.catissueplus.core.administrative.events.PasswordDetails;
import com.krishagni.catissueplus.core.administrative.events.UserDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public interface UserService {

	public ResponseEvent<List<UserSummary>> getAllUsers(RequestEvent<ListUserCriteria> req);

	public ResponseEvent<UserDetail> createUser(RequestEvent<UserDetail> req);

	public ResponseEvent<UserDetail> updateUser(RequestEvent<UserDetail> event);

	public ResponseEvent<UserDetail> closeUser(RequestEvent<Long> req);

	public ResponseEvent<Boolean> resetPassword(RequestEvent<PasswordDetails> req);

	public ResponseEvent<Boolean> changePassword(RequestEvent<PasswordDetails> req);

	public ResponseEvent<Boolean> forgotPassword(RequestEvent<String> req);

	public ResponseEvent<Boolean> validatePassword(RequestEvent<String> req);

	public ResponseEvent<UserDetail> getUser(RequestEvent<Long> userId);

	public ResponseEvent<UserDetail> deleteUser(RequestEvent<Long> userId);

}
