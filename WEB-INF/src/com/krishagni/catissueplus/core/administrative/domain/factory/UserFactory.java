
package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.events.UserDetails;

public interface UserFactory {

	public User createUser(UserDetails details);

	public User patchUser(User oldUser, UserDetails details);

}
