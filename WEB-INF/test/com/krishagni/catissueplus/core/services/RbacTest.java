package com.krishagni.catissueplus.core.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.services.testdata.RbacTestData;
import com.krishagni.rbac.common.errors.RbacErrorCode;
import com.krishagni.rbac.domain.Group;
import com.krishagni.rbac.domain.Operation;
import com.krishagni.rbac.domain.Permission;
import com.krishagni.rbac.domain.Resource;
import com.krishagni.rbac.domain.Role;
import com.krishagni.rbac.domain.Subject;
import com.krishagni.rbac.events.AccessCheckedEvent;
import com.krishagni.rbac.events.AddPermissionEvent;
import com.krishagni.rbac.events.AllGroupRolesEvent;
import com.krishagni.rbac.events.AllOperationsEvent;
import com.krishagni.rbac.events.AllPermissionsEvent;
import com.krishagni.rbac.events.AllResourcesEvent;
import com.krishagni.rbac.events.AllRolesEvent;
import com.krishagni.rbac.events.AllSubjectRolesEvent;
import com.krishagni.rbac.events.CheckAccessEvent;
import com.krishagni.rbac.events.DeleteOperationEvent;
import com.krishagni.rbac.events.DeletePermissionEvent;
import com.krishagni.rbac.events.DeleteResourceEvent;
import com.krishagni.rbac.events.DeleteRoleEvent;
import com.krishagni.rbac.events.ListAllEvent;
import com.krishagni.rbac.events.OperationDeletedEvent;
import com.krishagni.rbac.events.OperationDetails;
import com.krishagni.rbac.events.OperationSavedEvent;
import com.krishagni.rbac.events.PermissionAddedEvent;
import com.krishagni.rbac.events.PermissionDeletedEvent;
import com.krishagni.rbac.events.PermissionDetails;
import com.krishagni.rbac.events.ReqAllGroupRolesEvent;
import com.krishagni.rbac.events.ReqAllSubjectRolesEvent;
import com.krishagni.rbac.events.ResourceDeletedEvent;
import com.krishagni.rbac.events.ResourceDetails;
import com.krishagni.rbac.events.ResourceSavedEvent;
import com.krishagni.rbac.events.RoleDeletedEvent;
import com.krishagni.rbac.events.RoleSavedEvent;
import com.krishagni.rbac.events.SaveOperationEvent;
import com.krishagni.rbac.events.SaveResourceEvent;
import com.krishagni.rbac.events.SaveRoleEvent;
import com.krishagni.rbac.repository.DaoFactory;
import com.krishagni.rbac.repository.GroupDao;
import com.krishagni.rbac.repository.OperationDao;
import com.krishagni.rbac.repository.PermissionDao;
import com.krishagni.rbac.repository.ResourceDao;
import com.krishagni.rbac.repository.RoleDao;
import com.krishagni.rbac.repository.SubjectDao;
import com.krishagni.rbac.service.RbacService;
import com.krishagni.rbac.service.impl.RbacServiceImpl;

public class RbacTest {

	@Mock
	private DaoFactory daoFactory;
	
	@Mock 
	private ResourceDao resourceDao;
	
	@Mock 
	private OperationDao operationDao;
	
	@Mock
	private PermissionDao permissionDao;
	
	@Mock
	private RoleDao roleDao;
	
	@Mock
	private SubjectDao subjectDao;
	
	@Mock
	private GroupDao groupDao;
	
	private RbacService rbacSvc;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		rbacSvc = new RbacServiceImpl();
		((RbacServiceImpl) rbacSvc).setDaoFactory(daoFactory);
		
		when(daoFactory.getOperationDao()).thenReturn(operationDao);
		when(daoFactory.getResourceDao()).thenReturn(resourceDao);
		when(daoFactory.getPermissionDao()).thenReturn(permissionDao);
		when(daoFactory.getRoleDao()).thenReturn(roleDao);
		when(daoFactory.getSubjectDao()).thenReturn(subjectDao);
		when(daoFactory.getGroupDao()).thenReturn(groupDao);
	}
	
	//
	// - Resources Tests
	//
	
	@Test
	public void createResourceTest() {
		SaveResourceEvent req = new SaveResourceEvent();
		ResourceDetails rd = new ResourceDetails();
		rd.setName("default");
		req.setResource(rd);
		final Long resourceId = new Long(1);
		
		doReturn(null).when(resourceDao).getResourceByName(any(String.class));
		
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Resource r = (Resource) invocation.getArguments()[0];
				r.setId(resourceId);
				return null;
			}
		})
		.when(resourceDao)
		.saveOrUpdate((any(Resource.class)));
		
		ResourceSavedEvent res = rbacSvc.saveResource(req);
		
		assertEquals(EventStatus.OK, res.getStatus());
		assertEquals(resourceId, res.getResource().getId());
		assertEquals(rd.getName(), res.getResource().getName());
	}
	
	@Test
	public void createResourceTestNullNameExpectBadRequest() {
		SaveResourceEvent req = new SaveResourceEvent();
		ResourceDetails rd = new ResourceDetails();
		rd.setName(null);
		req.setResource(rd);
		final Long resourceId = new Long(1);
		
		doReturn(null).when(resourceDao).getResourceByName(any(String.class));
		
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Resource r = (Resource) invocation.getArguments()[0];
				r.setId(resourceId);
				return null;
			}
		})
		.when(resourceDao)
		.saveOrUpdate((any(Resource.class)));
		
		ResourceSavedEvent res = rbacSvc.saveResource(req);
		
		assertEquals(EventStatus.BAD_REQUEST, res.getStatus());
		assertEquals(RbacErrorCode.INVALID_RESOURCE_NAME.message(), res.getMessage());
	}
	
	@Test
	public void createResourceTestEmptyNameExpectBadRequest() {
		SaveResourceEvent req = new SaveResourceEvent();
		ResourceDetails rd = new ResourceDetails();
		rd.setName("");
		req.setResource(rd);
		final Long resourceId = new Long(1);
		
		doReturn(null).when(resourceDao).getResourceByName(any(String.class));
		
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Resource r = (Resource) invocation.getArguments()[0];
				r.setId(resourceId);
				return null;
			}
		})
		.when(resourceDao)
		.saveOrUpdate((any(Resource.class)));
		
		ResourceSavedEvent res = rbacSvc.saveResource(req);
		
		assertEquals(EventStatus.BAD_REQUEST, res.getStatus());
		assertEquals(RbacErrorCode.INVALID_RESOURCE_NAME.message(), res.getMessage());
	}
	
	@Test
	public void createResourceTestNullResourceDetailsExpectBadRequest() {
		SaveResourceEvent req = new SaveResourceEvent();
		final Long resourceId = new Long(1);
		
		doReturn(null).when(resourceDao).getResourceByName(any(String.class));
		
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Resource r = (Resource) invocation.getArguments()[0];
				r.setId(resourceId);
				return null;
			}
		})
		.when(resourceDao)
		.saveOrUpdate((any(Resource.class)));
		
		ResourceSavedEvent res = rbacSvc.saveResource(req);
		
		assertEquals(EventStatus.BAD_REQUEST, res.getStatus());
		assertEquals(RbacErrorCode.INVALID_RESOURCE_NAME.message(), res.getMessage());
	}
	
	@Test
	public void createResourceTestRuntimeExceptionExpectNullResponse() {
		SaveResourceEvent req = new SaveResourceEvent();
		ResourceDetails rd = new ResourceDetails();
		rd.setName("default");
		req.setResource(rd);
		
		doReturn(null).when(resourceDao).getResourceByName(any(String.class));
		
		String errmsg = "Some mysql error";
		doThrow(new RuntimeException(errmsg))
		.when(resourceDao)
		.saveOrUpdate((any(Resource.class)));
		
		ResourceSavedEvent res = rbacSvc.saveResource(req);
		
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, res.getStatus());
		assertEquals(errmsg, res.getMessage());
	}
	
	@Test
	public void getAllResources() {
		Long resourceId = new Long(1);
		
		Resource r1 = new Resource();
		r1.setId(resourceId);
		r1.setName("specimen");
		
		Resource r2 = new Resource();
		r2.setId(resourceId+1);
		r2.setName("scg");
		
		List<Resource> resources = new ArrayList<Resource>();
		resources.add(r1);
		resources.add(r2);
		
		doReturn(resources).when(resourceDao).getAllResources();
		
		AllResourcesEvent res = rbacSvc.getAllResources(new ListAllEvent());
		
		assertEquals(EventStatus.OK, res.getStatus());
		assertEquals(resourceId, res.getResources().get(0).getId());
		assertEquals(new Long(resourceId+1), res.getResources().get(1).getId());
	}
	
	@Test
	public void getAllResourcesRuntimeExceptionExpectServerError() {
		String errmsg = "Some mysql exception";
		doThrow(new RuntimeException(errmsg)).when(resourceDao).getAllResources();
		
		AllResourcesEvent res = rbacSvc.getAllResources(new ListAllEvent());
		
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, res.getStatus());
		assertEquals(errmsg, res.getMessage());
	}
	
	@Test
	public void deleteResourceTest() {
		String resourceName = "specimen";
		
		DeleteResourceEvent req = new DeleteResourceEvent();
		req.setResourceName(resourceName);
		
		Resource resource = new Resource();
		resource.setName(resourceName);
		resource.setId(new Long(1));
		
		doReturn(resource).when(resourceDao).getResourceByName(resourceName);
		
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				return null;
			}
		})
		.when(resourceDao)
		.delete((any(Resource.class)));
		
		ResourceDeletedEvent res = rbacSvc.deleteResource(req);
		
		assertEquals(EventStatus.OK, res.getStatus());
		assertEquals(resourceName, res.getResource().getName());
	}
	
	@Test
	public void deleteResourceTestResourceNotFound() {
		String resourceName = "specimen";
		
		DeleteResourceEvent req = new DeleteResourceEvent();
		req.setResourceName(resourceName);
		
		doReturn(null).when(resourceDao).getResourceByName(resourceName);
		
		ResourceDeletedEvent res = rbacSvc.deleteResource(req);
		
		assertEquals(EventStatus.NOT_FOUND, res.getStatus());
	}
	
	//
	// -- Operation tests
	//
	
	@Test
	public void createOperationTest() {
		SaveOperationEvent req = new SaveOperationEvent();
		OperationDetails rd = new OperationDetails();
		rd.setName("default");
		req.setOperation(rd);
		final Long operationId = new Long(1);
		
		doReturn(null).when(operationDao).getOperationByName(any(String.class));
		
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Operation r = (Operation) invocation.getArguments()[0];
				r.setId(operationId);
				return null;
			}
		})
		.when(operationDao)
		.saveOrUpdate((any(Operation.class)));
		
		OperationSavedEvent res = rbacSvc.saveOperation(req);
		
		assertEquals(EventStatus.OK, res.getStatus());
		assertEquals(operationId, res.getOperation().getId());
		assertEquals(rd.getName(), res.getOperation().getName());
	}
	
	@Test
	public void createOperationTestNullNameExpectBadRequest() {
		SaveOperationEvent req = new SaveOperationEvent();
		OperationDetails rd = new OperationDetails();
		rd.setName(null);
		req.setOperation(rd);

		OperationSavedEvent res = rbacSvc.saveOperation(req);
		
		assertEquals(EventStatus.BAD_REQUEST, res.getStatus());
		assertEquals(RbacErrorCode.INVALID_OPERATION_NAME.message(), res.getMessage());
	}
	
	@Test
	public void createOperationTestEmptyNameExpectBadRequest() {
		SaveOperationEvent req = new SaveOperationEvent();
		OperationDetails rd = new OperationDetails();
		rd.setName("");
		req.setOperation(rd);
		final Long operationId = new Long(1);
		
		doReturn(null).when(operationDao).getOperationByName(any(String.class));
		
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Operation r = (Operation) invocation.getArguments()[0];
				r.setId(operationId);
				return null;
			}
		})
		.when(operationDao)
		.saveOrUpdate((any(Operation.class)));
		
		OperationSavedEvent res = rbacSvc.saveOperation(req);
		
		assertEquals(EventStatus.BAD_REQUEST, res.getStatus());
		assertEquals(RbacErrorCode.INVALID_OPERATION_NAME.message(), res.getMessage());
	}
	
	@Test
	public void createOperationTestNullOperationDetailsExpectBadRequest() {
		SaveOperationEvent req = new SaveOperationEvent();
		final Long operationId = new Long(1);
		
		doReturn(null).when(operationDao).getOperationByName(any(String.class));
		
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Operation r = (Operation) invocation.getArguments()[0];
				r.setId(operationId);
				return null;
			}
		})
		.when(operationDao)
		.saveOrUpdate((any(Operation.class)));
		
		OperationSavedEvent res = rbacSvc.saveOperation(req);
		
		assertEquals(EventStatus.BAD_REQUEST, res.getStatus());
		assertEquals(RbacErrorCode.INVALID_OPERATION_NAME.message(), res.getMessage());
	}
	
	@Test
	public void createOperationTestRuntimeExceptionExpectNullResponse() {
		SaveOperationEvent req = new SaveOperationEvent();
		OperationDetails rd = new OperationDetails();
		rd.setName("default");
		req.setOperation(rd);
		
		doReturn(null).when(operationDao).getOperationByName(any(String.class));
		
		String errmsg = "Some mysql error";
		doThrow(new RuntimeException(errmsg))
		.when(operationDao)
		.saveOrUpdate((any(Operation.class)));
		
		OperationSavedEvent res = rbacSvc.saveOperation(req);
		
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, res.getStatus());
		assertEquals(errmsg, res.getMessage());
	}
	
	@Test
	public void getAllOperations() {
		Long operationId = new Long(1);
		
		Operation r1 = new Operation();
		r1.setId(operationId);
		r1.setName("specimen");
		
		Operation r2 = new Operation();
		r2.setId(operationId+1);
		r2.setName("scg");
		
		List<Operation> operations = new ArrayList<Operation>();
		operations.add(r1);
		operations.add(r2);
		
		doReturn(operations).when(operationDao).getAllOperations();
		
		AllOperationsEvent res = rbacSvc.getAllOperations(new ListAllEvent());
		
		assertEquals(EventStatus.OK, res.getStatus());
		assertEquals(operationId, res.getOperations().get(0).getId());
		assertEquals(new Long(operationId+1), res.getOperations().get(1).getId());
	}
	
	@Test
	public void getAllOperationsRuntimeExceptionExpectServerError() {
		String errmsg = "Some mysql exception";
		doThrow(new RuntimeException(errmsg)).when(operationDao).getAllOperations();
		
		AllOperationsEvent res = rbacSvc.getAllOperations(new ListAllEvent());
		
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, res.getStatus());
		assertEquals(errmsg, res.getMessage());
	}
	
	@Test
	public void deleteOperationTest() {
		String operationName = "OPERATION";
		
		DeleteOperationEvent req = new DeleteOperationEvent();
		req.setOperationName(operationName);
		
		Operation operation = new Operation();
		operation.setName(operationName);
		operation.setId(new Long(1));
		
		doReturn(operation).when(operationDao).getOperationByName(operationName);
		
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				return null;
			}
		})
		.when(operationDao)
		.delete((any(Operation.class)));
		
		OperationDeletedEvent res = rbacSvc.deleteOperation(req);
		
		assertEquals(EventStatus.OK, res.getStatus());
		assertEquals(operationName, res.getOperation().getName());
	}
	
	@Test
	public void deleteOperationTestOperationNotFound() {
		String operationName = "specimen";
		
		DeleteOperationEvent req = new DeleteOperationEvent();
		req.setOperationName(operationName);
		
		doReturn(null).when(operationDao).getOperationByName(operationName);
		
		OperationDeletedEvent res = rbacSvc.deleteOperation(req);
		
		assertEquals(EventStatus.NOT_FOUND, res.getStatus());
	}
	
	@Test
	public void getAllPermissionsTest() {
		Permission p1 = new Permission();
		Resource r1 = new Resource();
		r1.setName("Specimen");
		r1.setId(new Long(1));
		p1.setResource(r1);
		
		Operation o1 = new Operation();
		o1.setName("Derive");
		o1.setId(new Long(1));
		p1.setOperation(o1);
		p1.setId(new Long(1));
		
		Permission p2 = new Permission();
		Resource r2 = new Resource();
		r2.setName("CPR");
		r2.setId(new Long(2));
		p2.setResource(r2);
		
		Operation o2 = new Operation();
		o2.setName("Save");
		o2.setId(new Long(2));
		p2.setOperation(o2);
		p2.setId(new Long(2));
		
		List<Permission> permissions = new ArrayList<Permission>();
		permissions.add(p1);
		permissions.add(p2);
		
		doReturn(permissions).when(permissionDao).getAllPermissions();
		
		AllPermissionsEvent res = rbacSvc.getAllPermissions(new ListAllEvent());
		
		assertEquals(EventStatus.OK, res.getStatus());
		assertEquals(new Long(1), res.getPermissions().get(0).getId());
		assertEquals(new Long(2), res.getPermissions().get(1).getId());
	}
	
	@Test
	public void getAllPermissionsTestRuntimeExceptionExpectInternalError() {
		String msg = "Some mysql error";
		doThrow(new RuntimeException(msg)).when(permissionDao).getAllPermissions();
		
		AllPermissionsEvent res = rbacSvc.getAllPermissions(new ListAllEvent());
		
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, res.getStatus());
		assertEquals(msg, res.getMessage());
	}
	
	@Test
	public void createPermissionTest() {
		Resource r1 = new Resource();
		r1.setName("SPECIMEN");
		r1.setId(new Long(1));
		
		Operation o1 = new Operation();
		o1.setName("DERIVE");
		o1.setId(new Long(1));
		
		final Long permissionId = new Long(1);
		Permission p1 = new Permission();
		p1.setId(new Long(1));
		p1.setResource(r1);
		p1.setOperation(o1);
		
		doReturn(r1).when(resourceDao).getResourceByName(any(String.class));
		doReturn(o1).when(operationDao).getOperationByName(any(String.class));
		doReturn(null).when(permissionDao).getPermission(r1.getName(), o1.getName());
		
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Permission p = (Permission) invocation.getArguments()[0];
				p.setId(permissionId);
				return null;
			}
		})
		.when(permissionDao)
		.saveOrUpdate((any(Permission.class)));
		
		AddPermissionEvent req = new AddPermissionEvent();
		PermissionDetails pds = new PermissionDetails();
		pds.setOperationName(o1.getName());
		pds.setResourceName(r1.getName());
		req.setPermission(pds);
		PermissionAddedEvent res = rbacSvc.addPermission(req);
		
		assertEquals(EventStatus.OK, res.getStatus());
		assertEquals(p1.getId(), res.getPermission().getId());
	}
	
	@Test
	public void createPermissionTestResourceNameEmptyExpectBadRequest() {
		AddPermissionEvent req = new AddPermissionEvent();
		PermissionDetails pds = new PermissionDetails();
		pds.setOperationName("default");
		pds.setResourceName("");
		req.setPermission(pds);
		PermissionAddedEvent res = rbacSvc.addPermission(req);
		
		assertEquals(EventStatus.BAD_REQUEST, res.getStatus());
		assertEquals(RbacErrorCode.INVALID_RESOURCE_NAME.message(), res.getMessage());
	}
	
	@Test
	public void createPermissionTestOperationNameEmptyExpectBadRequest() {
		AddPermissionEvent req = new AddPermissionEvent();
		PermissionDetails pds = new PermissionDetails();
		pds.setOperationName("");
		pds.setResourceName("default");
		req.setPermission(pds);
		PermissionAddedEvent res = rbacSvc.addPermission(req);
		
		assertEquals(EventStatus.BAD_REQUEST, res.getStatus());
		assertEquals(RbacErrorCode.INVALID_OPERATION_NAME.message(), res.getMessage());
	}
	
	@Test
	public void createPermissionTestResourceNotFoundExpectNotFoundErrorMsg() {
		Resource r1 = new Resource();
		r1.setName("SPECIMEN");
		r1.setId(new Long(1));
		
		Operation o1 = new Operation();
		o1.setName("DERIVE");
		o1.setId(new Long(1));
		
		Permission p1 = new Permission();
		p1.setId(new Long(1));
		p1.setResource(r1);
		p1.setOperation(o1);
		
		doReturn(null).when(resourceDao).getResourceByName(any(String.class));
		
		AddPermissionEvent req = new AddPermissionEvent();
		PermissionDetails pds = new PermissionDetails();
		pds.setOperationName(o1.getName());
		pds.setResourceName(r1.getName());
		req.setPermission(pds);
		PermissionAddedEvent res = rbacSvc.addPermission(req);
		
		assertEquals(EventStatus.BAD_REQUEST, res.getStatus());
		assertEquals(RbacErrorCode.RESOURCE_NOT_FOUND.message(),res.getMessage());
	}
	
	@Test
	public void createPermissionTestOperationNotFoundExpectNotFoundErrorMsg() {
		Resource r1 = new Resource();
		r1.setName("SPECIMEN");
		r1.setId(new Long(1));
		
		Operation o1 = new Operation();
		o1.setName("DERIVE");
		o1.setId(new Long(1));
		
		Permission p1 = new Permission();
		p1.setId(new Long(1));
		p1.setResource(r1);
		p1.setOperation(o1);
		
		doReturn(r1).when(resourceDao).getResourceByName(any(String.class));
		doReturn(null).when(operationDao).getOperationByName(any(String.class));
		
		AddPermissionEvent req = new AddPermissionEvent();
		PermissionDetails pds = new PermissionDetails();
		pds.setOperationName(o1.getName());
		pds.setResourceName(r1.getName());
		req.setPermission(pds);
		PermissionAddedEvent res = rbacSvc.addPermission(req);
		
		assertEquals(EventStatus.BAD_REQUEST, res.getStatus());
		assertEquals(RbacErrorCode.OPERATION_NOT_FOUND.message(), res.getMessage());
	}
	
	@Test
	public void createPermissionTestDuplicatePermissionExpectBadRequest() {
		Resource r1 = new Resource();
		r1.setName("SPECIMEN");
		r1.setId(new Long(1));
		
		Operation o1 = new Operation();
		o1.setName("DERIVE");
		o1.setId(new Long(1));
		
		final Long permissionId = new Long(1);
		Permission p1 = new Permission();
		p1.setId(new Long(1));
		p1.setResource(r1);
		p1.setOperation(o1);
		
		doReturn(r1).when(resourceDao).getResourceByName(any(String.class));
		doReturn(o1).when(operationDao).getOperationByName(any(String.class));
		doReturn(p1).when(permissionDao).getPermission(r1.getName(), o1.getName());
		
		AddPermissionEvent req = new AddPermissionEvent();
		PermissionDetails pds = new PermissionDetails();
		pds.setOperationName(o1.getName());
		pds.setResourceName(r1.getName());
		req.setPermission(pds);
		PermissionAddedEvent res = rbacSvc.addPermission(req);
		
		assertEquals(EventStatus.BAD_REQUEST, res.getStatus());
		assertEquals(RbacErrorCode.DUPLICATE_PERMISSION.message(), res.getMessage());
	}
	
	@Test
	public void createPermissionTestRuntimeExceptionOnSaveOrUpdate() {
		Resource r1 = new Resource();
		r1.setName("SPECIMEN");
		r1.setId(new Long(1));
		
		Operation o1 = new Operation();
		o1.setName("DERIVE");
		o1.setId(new Long(1));
		
		final Long permissionId = new Long(1);
		Permission p1 = new Permission();
		p1.setId(new Long(1));
		p1.setResource(r1);
		p1.setOperation(o1);
		
		doReturn(r1).when(resourceDao).getResourceByName(any(String.class));
		doReturn(o1).when(operationDao).getOperationByName(any(String.class));
		doReturn(null).when(permissionDao).getPermission(r1.getName(), o1.getName());
		
		String msg = "Some mysql error";
		doThrow(new RuntimeException(msg))
		.when(permissionDao)
		.saveOrUpdate((any(Permission.class)));
		
		AddPermissionEvent req = new AddPermissionEvent();
		PermissionDetails pds = new PermissionDetails();
		pds.setOperationName(o1.getName());
		pds.setResourceName(r1.getName());
		req.setPermission(pds);
		PermissionAddedEvent res = rbacSvc.addPermission(req);
		
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, res.getStatus());
		assertEquals(msg, res.getMessage());
	}
	
	@Test
	public void deletePermissionTest() {
		Resource r1 = new Resource();
		r1.setName("SPECIMEN");
		r1.setId(new Long(1));
		
		Operation o1 = new Operation();
		o1.setName("DERIVE");
		o1.setId(new Long(1));
		
		final Long permissionId = new Long(1);
		Permission p1 = new Permission();
		p1.setId(permissionId);
		p1.setResource(r1);
		p1.setOperation(o1);
		
		doReturn(p1).when(permissionDao).getPermission(r1.getName(), o1.getName());
		
		DeletePermissionEvent req = new DeletePermissionEvent();
		PermissionDetails pd = new PermissionDetails();
		pd.setResourceName(r1.getName());
		pd.setOperationName(o1.getName());
		
		req.setPermissionDetails(pd);
		
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				return null;
			}
		})
		.when(permissionDao)
		.delete((any(Permission.class)));
		
		PermissionDeletedEvent res = rbacSvc.deletePermission(req);
		
		assertEquals(EventStatus.OK, res.getStatus());
		assertEquals(permissionId , res.getPermissionDetails().getId());
	}
	
	@Test
	public void deletePermissionTestInvalidOperationNameExpectBadRequest() {
		Resource r1 = new Resource();
		r1.setName("SPECIMEN");
		r1.setId(new Long(1));
		
		Operation o1 = new Operation();
		o1.setName("DERIVE");
		o1.setId(new Long(1));
		
		final Long permissionId = new Long(1);
		Permission p1 = new Permission();
		p1.setId(permissionId);
		p1.setResource(r1);
		p1.setOperation(o1);
		
		doReturn(p1).when(permissionDao).getPermission(r1.getName(), o1.getName());
		
		DeletePermissionEvent req = new DeletePermissionEvent();
		PermissionDetails pd = new PermissionDetails();
		pd.setResourceName(r1.getName());
		pd.setOperationName("");
		
		req.setPermissionDetails(pd);
		
		PermissionDeletedEvent res = rbacSvc.deletePermission(req);
		
		assertEquals(EventStatus.BAD_REQUEST, res.getStatus());
		assertEquals(RbacErrorCode.INVALID_OPERATION_NAME.message(), res.getMessage());
	}
	
	@Test
	public void deletePermissionTestInvalidResourceNameExpectBadRequest() {
		Resource r1 = new Resource();
		r1.setName("SPECIMEN");
		r1.setId(new Long(1));
		
		Operation o1 = new Operation();
		o1.setName("DERIVE");
		o1.setId(new Long(1));
		
		final Long permissionId = new Long(1);
		Permission p1 = new Permission();
		p1.setId(permissionId);
		p1.setResource(r1);
		p1.setOperation(o1);
		
		doReturn(p1).when(permissionDao).getPermission(r1.getName(), o1.getName());
		
		DeletePermissionEvent req = new DeletePermissionEvent();
		PermissionDetails pd = new PermissionDetails();
		pd.setResourceName("");
		pd.setOperationName(o1.getName());
		
		req.setPermissionDetails(pd);
		
		PermissionDeletedEvent res = rbacSvc.deletePermission(req);
		
		assertEquals(EventStatus.BAD_REQUEST, res.getStatus());
		assertEquals(RbacErrorCode.INVALID_RESOURCE_NAME.message(), res.getMessage());
	}
	

	@Test
	public void deletePermissionTestPermissionNotFoundExpectBadRequest() {
		Resource r1 = new Resource();
		r1.setName("SPECIMEN");
		r1.setId(new Long(1));
		
		Operation o1 = new Operation();
		o1.setName("DERIVE");
		o1.setId(new Long(1));
		
		final Long permissionId = new Long(1);
		Permission p1 = new Permission();
		p1.setId(permissionId);
		p1.setResource(r1);
		p1.setOperation(o1);
		
		doReturn(null).when(permissionDao).getPermission(r1.getName(), o1.getName());
		
		DeletePermissionEvent req = new DeletePermissionEvent();
		PermissionDetails pd = new PermissionDetails();
		pd.setResourceName(r1.getName());
		pd.setOperationName(o1.getName());
		
		req.setPermissionDetails(pd);

		PermissionDeletedEvent res = rbacSvc.deletePermission(req);
		
		assertEquals(EventStatus.BAD_REQUEST, res.getStatus());
		assertEquals(RbacErrorCode.PERMISSIONS_NOT_FOUND.message() , res.getMessage());
	}
	
	@Test
	public void deletePermissionTestNullPermissionDetailsExpectBadRequest() {
		DeletePermissionEvent req = new DeletePermissionEvent();
		req.setPermissionDetails(null);
		
		PermissionDeletedEvent res = rbacSvc.deletePermission(req);
		
		assertEquals(EventStatus.BAD_REQUEST, res.getStatus());
		assertEquals(RbacErrorCode.INVALID_RESOURCE_NAME.message() , res.getMessage());
	}
	
	@Test
	public void getAllRolesTest() {
		Role r1 = RbacTestData.getRole(new Long(1), "admin");
		Role r2 = RbacTestData.getRole(new Long(2), "super admin");
		
		List<Role> rs = new ArrayList<Role>();
		rs.add(r1);
		rs.add(r2);
		
		doReturn(rs).when(roleDao).getAllRoles();
		
		AllRolesEvent res = rbacSvc.getAllRoles(new ListAllEvent());
		
		assertEquals(EventStatus.OK, res.getStatus());
		assertEquals(new Long(1), res.getRoles().get(0).getId());
		assertEquals(new Long(2), res.getRoles().get(1).getId());
	}
	
	@Test
	public void getAllRolesTestRuntimeExceptionExpectInternalServerError() {
		String msg = "Some sql error";
		doThrow(new RuntimeException(msg)).when(roleDao).getAllRoles();
		
		AllRolesEvent res = rbacSvc.getAllRoles(new ListAllEvent());
		
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, res.getStatus());
		assertEquals(msg, res.getMessage());
	}
	
	@Test
	public void saveRoleTest() {
		final Long roleId = new Long(1);
		Role r1 = RbacTestData.getRole(roleId, "admin");
		
		Permission p = new Permission();
		p.setResource(r1.getAcl().iterator().next().getResource());
		p.setOperation(r1.getAcl().iterator().next().getOperations().iterator().next().getOperation());
		p.setId(roleId);
		
		doReturn(RbacTestData.getResource()).when(resourceDao).getResourceByName(any(String.class));
		doReturn(RbacTestData.getOperation()).when(operationDao).getOperationByName(any(String.class));
		doReturn(null).when(roleDao).getRoleByName(any(String.class));
		doReturn(p).when(permissionDao).getPermission(any(String.class), any(String.class));
		
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Role r = (Role) invocation.getArguments()[0];
				r.setId(roleId);
				return null;
			}
		})
		.when(roleDao)
		.saveOrUpdate((any(Role.class)));
		
		SaveRoleEvent req = new SaveRoleEvent();
		req.setRole(RbacTestData.getRoleDetails(roleId, "admin"));
		
		RoleSavedEvent res = rbacSvc.saveRole(req);
		
		assertEquals(EventStatus.OK, res.getStatus());
		assertEquals(roleId, res.getRole().getId());
	}
	
	@Test
	public void saveRoleTestNullRoleDetailsExpectBadRequest() {
		
		SaveRoleEvent req = new SaveRoleEvent();
		req.setRole(null);
		
		RoleSavedEvent res = rbacSvc.saveRole(req);
		
		assertEquals(EventStatus.BAD_REQUEST, res.getStatus());
		assertEquals(RbacErrorCode.INVALID_ROLE_NAME.message(), res.getMessage());
	}
	
	@Test
	public void saveRoleTestEmptyRoleNameExpectBadRequest() {
		
		SaveRoleEvent req = new SaveRoleEvent();
		req.setRole(RbacTestData.getRoleDetails(new Long(1), ""));
		
		RoleSavedEvent res = rbacSvc.saveRole(req);
		
		assertEquals(EventStatus.BAD_REQUEST, res.getStatus());
		assertEquals(RbacErrorCode.INVALID_ROLE_NAME.message(), res.getMessage());
	}
	
	@Test
	public void saveRoleTestRuntimeExceptionExpectServerError() {
		final Long roleId = new Long(1);
		Role r = RbacTestData.getRole(roleId, "admin");
		Permission p = new Permission();
		p.setResource(r.getAcl().iterator().next().getResource());
		p.setOperation(r.getAcl().iterator().next().getOperations().iterator().next().getOperation());
		p.setId(roleId);
		
		doReturn(RbacTestData.getResource()).when(resourceDao).getResourceByName(any(String.class));
		doReturn(RbacTestData.getOperation()).when(operationDao).getOperationByName(any(String.class));
		doReturn(p).when(permissionDao).getPermission(any(String.class), any(String.class));
		doReturn(null).when(roleDao).getRoleByName(any(String.class));
		
		String msg = "Sql error";
		doThrow(new RuntimeException(msg))
		.when(roleDao)
		.saveOrUpdate((any(Role.class)));
		
		SaveRoleEvent req = new SaveRoleEvent();
		req.setRole(RbacTestData.getRoleDetails(roleId, "admin"));
		
		RoleSavedEvent res = rbacSvc.saveRole(req);
		
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, res.getStatus());
		assertEquals(msg, res.getMessage());
	}
	
	@Test
	public void deleteRoleTest() {
		Role r1 = RbacTestData.getRole(new Long(1), "admin");
		
		doReturn(r1).when(roleDao).getRoleByName("admin");

		DeleteRoleEvent req = new DeleteRoleEvent();
		req.setRoleName("admin");
		
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				return null;
			}
		})
		.when(roleDao)
		.delete((any(Role.class)));
		
		RoleDeletedEvent res = rbacSvc.deleteRole(req);
		
		assertEquals(EventStatus.OK, res.getStatus());
		assertEquals(new Long(1), res.getRole().getId());
	}
	
	@Test
	public void deleteRoleTestRuntimeExceptionExpectServerError() {
		Role r1 = RbacTestData.getRole(new Long(1), "admin");
		
		doReturn(r1).when(roleDao).getRoleByName("admin");

		DeleteRoleEvent req = new DeleteRoleEvent();
		req.setRoleName("admin");
		
		String msg = "Sql error";
		doThrow(new RuntimeException(msg))
		.when(roleDao)
		.delete((any(Role.class)));
		
		RoleDeletedEvent res = rbacSvc.deleteRole(req);
		
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, res.getStatus());
		assertEquals(msg, res.getMessage());
	}
	
	@Test
	public void getAllSubjectRolesTest() {
		Long subjectId = (long)1;
		Subject s1 = RbacTestData.getSubject(subjectId);
		
		doReturn(s1).when(subjectDao).getSubject(subjectId);
		
		ReqAllSubjectRolesEvent req = new ReqAllSubjectRolesEvent();
		req.setSubjectId(subjectId);
		
		AllSubjectRolesEvent res = rbacSvc.getAllSubjectRoles(req);
		
		assertEquals(EventStatus.OK, res.getStatus());
		assertEquals(subjectId, res.getSubject().getId());		
	}
	
	@Test
	public void getAllSubjectRolesTestNullSubjectId() {
		ReqAllSubjectRolesEvent req = new ReqAllSubjectRolesEvent();
		req.setSubjectId(null);
		
		AllSubjectRolesEvent res = rbacSvc.getAllSubjectRoles(req);
		
		assertEquals(EventStatus.BAD_REQUEST, res.getStatus());
		assertEquals(RbacErrorCode.INVALID_SUBJECT_DETAILS.message(), res.getMessage());		
	}
	
	@Test
	public void getAllSubjectRolesTestRuntimeExceptionExpectInternalServerError() {
		String msg = "Sql error";
		Long subjectId = (long)1;
		doThrow(new RuntimeException(msg)).when(subjectDao).getSubject(subjectId);
		
		ReqAllSubjectRolesEvent req = new ReqAllSubjectRolesEvent();
		req.setSubjectId(subjectId);
		
		AllSubjectRolesEvent res = rbacSvc.getAllSubjectRoles(req);
		
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, res.getStatus());
		assertEquals(msg, res.getMessage());		
	}
	
	public void getAllGroupRolesTest() {
		Long GroupId = (long)1;
		Group s1 = RbacTestData.getGroup(GroupId);
		
		doReturn(s1).when(groupDao).getGroup(GroupId);
		
		ReqAllGroupRolesEvent req = new ReqAllGroupRolesEvent();
		req.setGroupId(GroupId);
		
		AllGroupRolesEvent res = rbacSvc.getAllGroupRoles(req);
		
		assertEquals(EventStatus.OK, res.getStatus());
		assertEquals(GroupId, res.getGroup().getId());		
	}
	
	@Test
	public void getAllGroupRolesTestNullGroupId() {
		ReqAllGroupRolesEvent req = new ReqAllGroupRolesEvent();
		req.setGroupId(null);
		
		AllGroupRolesEvent res = rbacSvc.getAllGroupRoles(req);
		
		assertEquals(EventStatus.BAD_REQUEST, res.getStatus());
		assertEquals(RbacErrorCode.INVALID_GROUP_DETAILS.message(), res.getMessage());		
	}
	
	@Test
	public void getAllGroupRolesTestRuntimeExceptionExpectInternalServerError() {
		String msg = "Sql error";
		Long GroupId = (long)1;
		doThrow(new RuntimeException(msg)).when(groupDao).getGroup(GroupId);
		
		ReqAllGroupRolesEvent req = new ReqAllGroupRolesEvent();
		req.setGroupId(GroupId);
		
		AllGroupRolesEvent res = rbacSvc.getAllGroupRoles(req);
		
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, res.getStatus());
		assertEquals(msg, res.getMessage());		
	}
	
	@Test
	public void userAccessTest() {
		CheckAccessEvent req = new CheckAccessEvent();
		req.setDsoId((long)1);
		req.setGroupId((long)2);
		req.setOperationName("all");
		req.setResourceInstanceId((long)123);
		req.setResourceName("specimen");
		req.setSubjectId((long)3);
		
		doReturn(true).when(subjectDao).canUserAccess(any(Map.class));
		
		AccessCheckedEvent res = rbacSvc.checkAccess(req);
		
		assertEquals(EventStatus.OK, res.getStatus());
		assertEquals(AccessCheckedEvent.AccessSettings.CAN_ACCESS, res.getAccessSettings());
	}
	
	@Test
	public void userAccessTestDeniedForSubject() {
		CheckAccessEvent req = new CheckAccessEvent();
		req.setDsoId((long)1);
		req.setGroupId((long)2);
		req.setOperationName("all");
		req.setResourceInstanceId((long)123);
		req.setResourceName("specimen");
		req.setSubjectId((long)3);
		
		doReturn(false).when(subjectDao).canUserAccess(any(Map.class));
		doReturn(false).when(groupDao).canUserAccess(any(Map.class));
		
		AccessCheckedEvent res = rbacSvc.checkAccess(req);
		
		assertEquals(EventStatus.OK, res.getStatus());
		assertEquals(AccessCheckedEvent.AccessSettings.ACCESS_DENIED, res.getAccessSettings());
	}
	
	@Test
	public void userAccessTestHasGroupAccess() {
		CheckAccessEvent req = new CheckAccessEvent();
		req.setDsoId((long)1);
		req.setGroupId((long)2);
		req.setOperationName("all");
		req.setResourceInstanceId((long)123);
		req.setResourceName("specimen");
		req.setSubjectId((long)3);
		
		doReturn(false).when(subjectDao).canUserAccess(any(Map.class));
		doReturn(true).when(groupDao).canUserAccess(any(Map.class));
		
		AccessCheckedEvent res = rbacSvc.checkAccess(req);
		
		assertEquals(EventStatus.OK, res.getStatus());
		assertEquals(AccessCheckedEvent.AccessSettings.CAN_ACCESS, res.getAccessSettings());
	}
	
	@Test
	public void userAccessTestRuntimeException() {
		CheckAccessEvent req = new CheckAccessEvent();
		req.setDsoId((long)1);
		req.setGroupId((long)2);
		req.setOperationName("all");
		req.setResourceInstanceId((long)123);
		req.setResourceName("specimen");
		req.setSubjectId((long)3);
		
		String msg = "Sql error";
		doThrow(new RuntimeException(msg)).when(subjectDao).canUserAccess(any(Map.class));
		
		AccessCheckedEvent res = rbacSvc.checkAccess(req);
		
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, res.getStatus());
		assertEquals(msg, res.getMessage());
	}
	
	@Test
	public void userAccessTestRuntimeExceptionOnGroupAccess() {
		CheckAccessEvent req = new CheckAccessEvent();
		req.setDsoId((long)1);
		req.setGroupId((long)2);
		req.setOperationName("all");
		req.setResourceInstanceId((long)123);
		req.setResourceName("specimen");
		req.setSubjectId((long)3);
		
		String msg = "Sql error";
		doReturn(false).when(subjectDao).canUserAccess(any(Map.class));
		doThrow(new RuntimeException(msg)).when(groupDao).canUserAccess(any(Map.class));
		
		AccessCheckedEvent res = rbacSvc.checkAccess(req);
		
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, res.getStatus());
		assertEquals(msg, res.getMessage());
	}
}