
package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.events.DeleteUserOp;
import com.krishagni.catissueplus.core.administrative.events.ListUserCriteria;
import com.krishagni.catissueplus.core.administrative.events.PasswordDetails;
import com.krishagni.catissueplus.core.administrative.events.UserDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public interface UserService {
	public ResponseEvent<List<UserSummary>> getUsers(RequestEvent<ListUserCriteria> req);
	
	public ResponseEvent<UserDetail> getUser(RequestEvent<Long> req);

	public ResponseEvent<UserDetail> createUser(RequestEvent<UserDetail> req);

	public ResponseEvent<UserDetail> updateUser(RequestEvent<UserDetail> req);

	public ResponseEvent<Boolean> resetPassword(RequestEvent<PasswordDetails> req);

	public ResponseEvent<Boolean> changePassword(RequestEvent<PasswordDetails> req);

	public ResponseEvent<Boolean> forgotPassword(RequestEvent<String> req);

	public ResponseEvent<Map<String, List>> deleteUser(RequestEvent<DeleteUserOp> req);
}
