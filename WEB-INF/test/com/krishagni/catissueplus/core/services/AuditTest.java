/**
 * 
 */
package com.krishagni.catissueplus.core.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.krishagni.catissueplus.core.audit.domain.Audit;
import com.krishagni.catissueplus.core.audit.domain.factory.AuditFactory;
import com.krishagni.catissueplus.core.audit.domain.factory.impl.AuditFactoryImpl;
import com.krishagni.catissueplus.core.audit.events.AuditCreatedEvent;
import com.krishagni.catissueplus.core.audit.events.AuditDetail;
import com.krishagni.catissueplus.core.audit.events.CreateAuditEvent;
import com.krishagni.catissueplus.core.audit.repository.AuditDao;
import com.krishagni.catissueplus.core.audit.services.AuditService;
import com.krishagni.catissueplus.core.audit.services.impl.AuditServiceImpl;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.events.EventStatus;

public class AuditTest {

	@Mock
	private DaoFactory daoFactory;

	@Mock
	private AuditDao auditDao;

	private AuditFactory auditFactory;

	private AuditService auditService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(daoFactory.getAuditDao()).thenReturn(auditDao);
		auditService = new AuditServiceImpl();
		((AuditServiceImpl) auditService).setDaoFactory(daoFactory);
		auditFactory = new AuditFactoryImpl();
		((AuditServiceImpl) auditService).setAuditFactory(auditFactory);

	}

	@Test
	public void testForSuccessfulAuditCreation() {

		CreateAuditEvent createAuditEvent = getAuditEvent();
		AuditDetail auditDetail = createAuditEvent.getAuditDetail();

		AuditCreatedEvent response = auditService
				.insertAuditDetails(createAuditEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		AuditDetail createdAuditDetail = response.getAuditDetail();
		assertEquals(auditDetail.getObjectId(),
				createdAuditDetail.getObjectId());
		assertEquals(auditDetail.getObjectType(),
				createdAuditDetail.getObjectType());
		assertEquals(auditDetail.getIpAddress(),
				createdAuditDetail.getIpAddress());
		assertEquals(auditDetail.getUserId(), createdAuditDetail.getUserId());
	}

	@Test
	public void testAuditInsertWithServerErr() {

		CreateAuditEvent createAuditEvent = getAuditEvent();
		doThrow(new RuntimeException()).when(auditDao).saveOrUpdate(
				any(Audit.class));
		AuditCreatedEvent response = auditService
				.insertAuditDetails(createAuditEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	private CreateAuditEvent getAuditEvent() {
		CreateAuditEvent crAuditEvent = new CreateAuditEvent();

		AuditDetail auditDetail = new AuditDetail();
		auditDetail.setIpAddress("127.0.0.1");
		auditDetail.setObjectId(1L);
		auditDetail.setObjectType("PARTICIPANT");
		auditDetail.setOperation("ADD");
		auditDetail.setReasonForChange("Added new Participant");
		auditDetail.setUpdatedDate(Calendar.getInstance().getTime());
		auditDetail.setUserId(1L);
		crAuditEvent.setAuditDetail(auditDetail);
		return crAuditEvent;
	}
}
