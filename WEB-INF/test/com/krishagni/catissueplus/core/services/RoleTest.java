
package com.krishagni.catissueplus.core.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.privileges.domain.Role;
import com.krishagni.catissueplus.core.privileges.domain.factory.PrivilegeErrorCode;
import com.krishagni.catissueplus.core.privileges.domain.factory.RoleFactory;
import com.krishagni.catissueplus.core.privileges.domain.factory.impl.RoleFactoryImpl;
import com.krishagni.catissueplus.core.privileges.events.CreateRoleEvent;
import com.krishagni.catissueplus.core.privileges.events.RoleCreatedEvent;
import com.krishagni.catissueplus.core.privileges.events.RoleDetails;
import com.krishagni.catissueplus.core.privileges.events.RoleUpdatedEvent;
import com.krishagni.catissueplus.core.privileges.events.UpdateRoleEvent;
import com.krishagni.catissueplus.core.privileges.repository.PrivilegeDao;
import com.krishagni.catissueplus.core.privileges.repository.RoleDao;
import com.krishagni.catissueplus.core.privileges.services.RoleService;
import com.krishagni.catissueplus.core.privileges.services.impl.RoleServiceImpl;
import com.krishagni.catissueplus.core.services.testdata.RoleTestData;

public class RoleTest {

	@Mock
	private DaoFactory daoFactory;

	@Mock
	RoleDao roleDao;

	@Mock
	PrivilegeDao privilegeDao;

	private RoleFactory roleFactory;

	private RoleService roleService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		when(daoFactory.getRoleDao()).thenReturn(roleDao);
		when(daoFactory.getPrivilegeDao()).thenReturn(privilegeDao);

		roleService = new RoleServiceImpl();
		((RoleServiceImpl) roleService).setDaoFactory(daoFactory);
		roleFactory = new RoleFactoryImpl();
		((RoleFactoryImpl) roleFactory).setDaoFactory(daoFactory);
		((RoleServiceImpl) roleService).setRoleFactory(roleFactory);
		when(roleDao.getRoleByName(anyString())).thenReturn(null);
		when(privilegeDao.getPrivilegeByName(anyString())).thenReturn(RoleTestData.getPrivilege(1l));
	}

	@Test
	public void testForSuccessfulRoleCreation() {
		CreateRoleEvent reqEvent = RoleTestData.getCreateRoleEvent();
		RoleCreatedEvent response = roleService.createRole(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(reqEvent.getRoleDetails().getName(), response.getRoleDetails().getName());

	}

	@Test
	public void testRoleCreationWithDuplicateRoleName() {
		when(roleDao.getRoleByName(anyString())).thenReturn(RoleTestData.getRole(1l));
		CreateRoleEvent reqEvent = RoleTestData.getCreateRoleEvent();
		RoleCreatedEvent response = roleService.createRole(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(RoleTestData.ROLE_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(PrivilegeErrorCode.DUPLICATE_ROLE_NAME.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testRoleCreationWithEmptyRoleName() {
		CreateRoleEvent reqEvent = RoleTestData.getCreateRoleEventWithEmptyRoleName();
		RoleCreatedEvent response = roleService.createRole(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(RoleTestData.ROLE_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(PrivilegeErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testRoleCreationWithServerErr() {
		CreateRoleEvent reqEvent = RoleTestData.getCreateRoleEvent();

		doThrow(new RuntimeException()).when(roleDao).saveOrUpdate(any(Role.class));
		RoleCreatedEvent response = roleService.createRole(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testForSuccessfulRoleUpdate() {
		when(roleDao.getRole(anyLong())).thenReturn(RoleTestData.getRole(1L));
		UpdateRoleEvent reqEvent = RoleTestData.getUpdateRoleEvent();

		RoleUpdatedEvent response = roleService.updateRole(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		RoleDetails createdRole = response.getRoleDetails();
		assertEquals(reqEvent.getRoleDetails().getName(), createdRole.getName());
	}

	@Test
	public void testForInvalidRoleUpdate() {
		when(roleDao.getRole(anyLong())).thenReturn(null);
		UpdateRoleEvent reqEvent = RoleTestData.getUpdateRoleEvent();

		RoleUpdatedEvent response = roleService.updateRole(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}

	@Test
	public void testGetAllRoles() {
		when(roleDao.getAllRoles()).thenReturn(RoleTestData.getRoles());
		List<RoleDetails> roleDetails = roleService.getAllRoles();
		assertNotNull("response cannot be null", roleDetails);
	}
	
	@Test
	public void testRoleUpdateWithServerErr() {
		when(roleDao.getRole(anyLong())).thenReturn(RoleTestData.getRole(1L));
		UpdateRoleEvent reqEvent = RoleTestData.getUpdateRoleEvent();

		doThrow(new RuntimeException()).when(roleDao).saveOrUpdate(any(Role.class));
		RoleUpdatedEvent response = roleService.updateRole(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}
}
