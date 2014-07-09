
package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.events.AllUsersEvent;
import com.krishagni.catissueplus.core.administrative.events.CloseUserEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.DisableUserEvent;
import com.krishagni.catissueplus.core.administrative.events.ForgotPasswordEvent;
import com.krishagni.catissueplus.core.administrative.events.GetUserEvent;
import com.krishagni.catissueplus.core.administrative.events.PasswordForgottenEvent;
import com.krishagni.catissueplus.core.administrative.events.PasswordUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.PasswordValidatedEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchUserEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllUsersEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdatePasswordEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.UserClosedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserDisabledEvent;
import com.krishagni.catissueplus.core.administrative.events.UserUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.ValidatePasswordEvent;

public interface UserService {

	public AllUsersEvent getAllUsers(ReqAllUsersEvent req);

	public UserCreatedEvent createUser(CreateUserEvent event);

	public UserUpdatedEvent updateUser(UpdateUserEvent event);

	public UserClosedEvent closeUser(CloseUserEvent event);

	public PasswordUpdatedEvent setPassword(UpdatePasswordEvent updatePasswordEvent);

	public PasswordUpdatedEvent changePassword(UpdatePasswordEvent updatePasswordEvent);

	public PasswordForgottenEvent forgotPassword(ForgotPasswordEvent forgotPasswordEvent);

	public PasswordValidatedEvent validatePassword(ValidatePasswordEvent event);

	public UserUpdatedEvent patchUser(PatchUserEvent event);

	public GetUserEvent getUser(Long userId);

	public UserDisabledEvent deleteUser(DisableUserEvent event);

}
