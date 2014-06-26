
package com.krishagni.catissueplus.core.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.printer.printRule.domain.SpecimenPrintRule;
import com.krishagni.catissueplus.core.printer.printRule.domain.factory.PrintRuleErrorCode;
import com.krishagni.catissueplus.core.printer.printRule.domain.factory.impl.SpecimenPrintRuleFactoryImpl;
import com.krishagni.catissueplus.core.printer.printRule.events.CreatePrintRuleEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.DeletePrintRuleEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.PatchPrintRuleEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.PrintRuleCreatedEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.PrintRuleDeletedEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.PrintRuleUpdatedEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.SpecimenPrintRuleDetails;
import com.krishagni.catissueplus.core.printer.printRule.events.UpdatePrintRuleEvent;
import com.krishagni.catissueplus.core.printer.printRule.repository.SpecimenPrintRuleDao;
import com.krishagni.catissueplus.core.printer.printRule.services.PrintRuleService;
import com.krishagni.catissueplus.core.printer.printRule.services.impl.SpecimenPrintRuleServiceImpl;
import com.krishagni.catissueplus.core.services.testdata.SpecimenPrintRuleTestData;

public class SpecimenPrintRuleTest {

	@Mock
	private DaoFactory daoFactory;

	@Mock
	SpecimenPrintRuleDao printRuleDao;

	private SpecimenPrintRuleFactoryImpl specimenPrintRuleFactory;

	private PrintRuleService printRuleService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(daoFactory.getSpecimenPrintRuleDao()).thenReturn(printRuleDao);
		printRuleService = new SpecimenPrintRuleServiceImpl();
		((SpecimenPrintRuleServiceImpl) printRuleService).setDaoFactory(daoFactory);
		specimenPrintRuleFactory = new SpecimenPrintRuleFactoryImpl();
		((SpecimenPrintRuleServiceImpl) printRuleService).setSpecimenPrintRuleFactory(specimenPrintRuleFactory);
		when(printRuleDao.getPrintRuleByName(anyString())).thenReturn(SpecimenPrintRuleTestData.getPrintRule(1l));
		when(printRuleDao.getPrintRule(anyLong())).thenReturn(SpecimenPrintRuleTestData.getPrintRule(1l));
		when(printRuleDao.isUniquePrintRuleName(anyString())).thenReturn(Boolean.TRUE);
		when(printRuleDao.isUniqueRule(anyString(), anyString(), anyString())).thenReturn(Boolean.TRUE);
	}

	@Test
	public void testForSuccessfulPrintRuleCreation() {
		CreatePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getCreatePrintRuleEvent();
		PrintRuleCreatedEvent response = printRuleService.createPrintRule(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(((SpecimenPrintRuleDetails)reqEvent.getDetails()).getName(), ((SpecimenPrintRuleDetails)response.getPrintRuleDetails()).getName()); 
	}

	@Test
	public void testPrintRuleCreationWithDuplicatePrintRuleName() {
		when(printRuleDao.isUniquePrintRuleName(anyString())).thenReturn(Boolean.FALSE);
		CreatePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getCreatePrintRuleEvent();
		PrintRuleCreatedEvent response = printRuleService.createPrintRule(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(SpecimenPrintRuleTestData.PRINT_RULE_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(PrintRuleErrorCode.DUPLICATE_PRINT_RULE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testPrintRuleCreationWithEmptyPrintRuleName() {
		CreatePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getCreatePrintRuleEventWithEmptyPrintRuleName();
		PrintRuleCreatedEvent response = printRuleService.createPrintRule(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SpecimenPrintRuleTestData.PRINT_RULE_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(PrintRuleErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testPrintRuleCreationWithEmptyPrinterName() {
		CreatePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getCreatePrintRuleEventWithEmptyPrinterName();
		PrintRuleCreatedEvent response = printRuleService.createPrintRule(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SpecimenPrintRuleTestData.PRINTER_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(PrintRuleErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testPrintRuleCreationWithNullSpecimenClass() {
		CreatePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getCreatePrintRuleEventWithEmptySpecimenClass();
		PrintRuleCreatedEvent response = printRuleService.createPrintRule(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(((SpecimenPrintRuleDetails)reqEvent.getDetails()).getName(), ((SpecimenPrintRuleDetails)response.getPrintRuleDetails()).getName());
	}
	
	@Test
	public void testPrintRuleCreationWithAnySpecimenClass() {
		CreatePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getCreatePrintRuleEventWithAnySpecimenClass();
		PrintRuleCreatedEvent response = printRuleService.createPrintRule(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(((SpecimenPrintRuleDetails)reqEvent.getDetails()).getName(), ((SpecimenPrintRuleDetails)response.getPrintRuleDetails()).getName());
	}
	
	@Test
	public void testPrintRuleCreationWithNullWorkstationIP() {
		CreatePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getCreatePrintRuleEventWithEmptyWorkstationIP();
		PrintRuleCreatedEvent response = printRuleService.createPrintRule(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(((SpecimenPrintRuleDetails)reqEvent.getDetails()).getName(), ((SpecimenPrintRuleDetails)response.getPrintRuleDetails()).getName());
	}
	
	@Test
	public void testPrintRuleCreationWithAnyWorkstationIP() {
		CreatePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getCreatePrintRuleEventWithAnyWorkstationIP();
		PrintRuleCreatedEvent response = printRuleService.createPrintRule(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(((SpecimenPrintRuleDetails)reqEvent.getDetails()).getName(), ((SpecimenPrintRuleDetails)response.getPrintRuleDetails()).getName());
	}
	
	@Test
	public void testPrintRuleCreationWithInvalidWorkstationIP() {
		CreatePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getCreatePrintRuleEventWithInvalidWorkstationIP();
		PrintRuleCreatedEvent response = printRuleService.createPrintRule(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SpecimenPrintRuleTestData.WORKSTATION_IP, response.getErroneousFields()[0].getFieldName());
		assertEquals(PrintRuleErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testPrintRuleCreationWithNullSpecimenType() {
		CreatePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getCreatePrintRuleEventWithEmptySpecimenType();
		PrintRuleCreatedEvent response = printRuleService.createPrintRule(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(((SpecimenPrintRuleDetails)reqEvent.getDetails()).getName(), ((SpecimenPrintRuleDetails)response.getPrintRuleDetails()).getName());
	}
	
	@Test
	public void testPrintRuleCreationWithAnySpecimenType() {
		CreatePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getCreatePrintRuleEventWithAnySpecimenType();
		PrintRuleCreatedEvent response = printRuleService.createPrintRule(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(((SpecimenPrintRuleDetails)reqEvent.getDetails()).getName(), ((SpecimenPrintRuleDetails)response.getPrintRuleDetails()).getName());
	}
	
	@Test
	public void testPrintRuleCreationWithEmptyLabelType() {
		CreatePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getCreatePrintRuleEventWithEmptyLabelType();
		PrintRuleCreatedEvent response = printRuleService.createPrintRule(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SpecimenPrintRuleTestData.LABEL_TYPE, response.getErroneousFields()[0].getFieldName());
		assertEquals(PrintRuleErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testPrintRuleCreationWithInvalidLabelType() {
		CreatePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getCreatePrintRuleEventWithInvalidLabelType();
		PrintRuleCreatedEvent response = printRuleService.createPrintRule(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SpecimenPrintRuleTestData.LABEL_TYPE, response.getErroneousFields()[0].getFieldName());
		assertEquals(PrintRuleErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testPrintRuleCreationWithEmptyDataOnLabel() {
		CreatePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getCreatePrintRuleEventWithEmptyDataOnLabel();
		PrintRuleCreatedEvent response = printRuleService.createPrintRule(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SpecimenPrintRuleTestData.DATA_ON_LABEL, response.getErroneousFields()[0].getFieldName());
		assertEquals(PrintRuleErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testPrintRuleDuplicatePrintRule() {
		CreatePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getCreatePrintRuleEvent();
		when(printRuleDao.isUniquePrintRuleName(anyString())).thenReturn(Boolean.FALSE);
		PrintRuleCreatedEvent response = printRuleService.createPrintRule(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(PrintRuleErrorCode.DUPLICATE_PRINT_RULE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testPrintRuleCreationWithServerErr() {
		CreatePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getCreatePrintRuleEvent();
		doThrow(new RuntimeException()).when(printRuleDao).saveOrUpdate(any(SpecimenPrintRule.class));
		PrintRuleCreatedEvent response = printRuleService.createPrintRule(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testForSuccessfulPrintRuleUpdate() {
		when(printRuleDao.getPrintRuleByName(anyString())).thenReturn(SpecimenPrintRuleTestData.getPrintRule(1L));
		UpdatePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getUpdatePrintRuleEvent();

		PrintRuleUpdatedEvent response = printRuleService.updatePrintRule(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		SpecimenPrintRuleDetails createdPrintRule = (SpecimenPrintRuleDetails) response.getPrintRuleDetails();
		assertEquals(((SpecimenPrintRuleDetails)reqEvent.getPrintRuleDetails()).getName(), createdPrintRule.getName());
	}
	
	@Test
	public void testForPrintRuleUpdateWithChangeInSpecimenClass() {
		when(printRuleDao.isUniqueRule(anyString(), anyString(), anyString())).thenReturn(Boolean.FALSE);
		UpdatePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getUpdatePrintRuleEventForChangeSpecimenClassAndName();

		PrintRuleUpdatedEvent response = printRuleService.updatePrintRule(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(PrintRuleErrorCode.DUPLICATE_PRINT_RULE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testForPrintRuleUpdateWithChangeInSpecimenType() {
		when(printRuleDao.isUniqueRule(anyString(), anyString(), anyString())).thenReturn(Boolean.FALSE);
		UpdatePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getUpdatePrintRuleEventForChangeSpecimenTypeAndName();

		PrintRuleUpdatedEvent response = printRuleService.updatePrintRule(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(PrintRuleErrorCode.DUPLICATE_PRINT_RULE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testForPrintRuleUpdateWithChangeInWorkstationIP() {
		when(printRuleDao.isUniqueRule(anyString(), anyString(), anyString())).thenReturn(Boolean.FALSE);
		UpdatePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getUpdatePrintRuleEventForChangeWorkstationIPAndName();

		PrintRuleUpdatedEvent response = printRuleService.updatePrintRule(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(PrintRuleErrorCode.DUPLICATE_PRINT_RULE.message(), response.getErroneousFields()[0].getErrorMessage());

	}

	@Test
	public void testForInvalidPrintRuleUpdate() {
		when(printRuleDao.getPrintRuleByName(anyString())).thenReturn(null);
		UpdatePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getUpdatePrintRuleEvent();
		PrintRuleUpdatedEvent response = printRuleService.updatePrintRule(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}

	@Test
	public void testPrintRuleUpdateWithServerErr() {
		when(printRuleDao.getPrintRuleByName(anyString())).thenReturn(SpecimenPrintRuleTestData.getPrintRule(1L));
		UpdatePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getUpdatePrintRuleEvent();
		doThrow(new RuntimeException()).when(printRuleDao).saveOrUpdate(any(SpecimenPrintRule.class));
		PrintRuleUpdatedEvent response = printRuleService.updatePrintRule(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}
	
	@Test
	public void testSuccessfulPrintRuleDelete() {
		DeletePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getDeletePrintRuleEvent();
		SpecimenPrintRule printRuleToDelete = SpecimenPrintRuleTestData.getPrintRule(1l);
		when(printRuleDao.getPrintRuleByName(anyString())).thenReturn(printRuleToDelete);
		PrintRuleDeletedEvent response = printRuleService.deletePrintRule(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}
	
	@Test
	public void testSuccessfulPrintRuleDeleteByName() {
		DeletePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getDeletePrintRuleEventByName();
		SpecimenPrintRule printRuleToDelete = SpecimenPrintRuleTestData.getPrintRule(1l);
		when(printRuleDao.getPrintRuleByName(anyString())).thenReturn(printRuleToDelete);
		PrintRuleDeletedEvent response = printRuleService.deletePrintRule(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}
	
	@Test
	public void testPrintRuleDeleteWithServerErr() {
		doThrow(new RuntimeException()).when(printRuleDao).delete(any(SpecimenPrintRule.class));
		DeletePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getDeletePrintRuleEvent();
		SpecimenPrintRule printRuleToDelete = SpecimenPrintRuleTestData.getPrintRule(1l);
		when(printRuleDao.getPrintRuleByName(anyString())).thenReturn(printRuleToDelete);
		PrintRuleDeletedEvent response = printRuleService.deletePrintRule(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}
	
	@Test
	public void testForInvalidPrintRuleDelete() {
		when(printRuleDao.getPrintRuleByName(anyString())).thenReturn(null);
		DeletePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getDeletePrintRuleEventByName();
		PrintRuleDeletedEvent response = printRuleService.deletePrintRule(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}
	
	@Test
	public void testForSuccessfulPrintUpdateWithName() {
		UpdatePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getUpdatePrintRuleEvent();
		PrintRuleUpdatedEvent response = printRuleService.updatePrintRule(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		SpecimenPrintRuleDetails createdRule = (SpecimenPrintRuleDetails) response.getPrintRuleDetails();
		assertEquals(((SpecimenPrintRuleDetails) reqEvent.getPrintRuleDetails()).getName(), createdRule.getName());
	}
	
	@Test
	public void testForSuccessfulPrintUpdateWithId() {
		UpdatePrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getUpdatePrintRuleEventById();
		PrintRuleUpdatedEvent response = printRuleService.updatePrintRule(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		SpecimenPrintRuleDetails createdRule = (SpecimenPrintRuleDetails) response.getPrintRuleDetails();
		assertEquals(((SpecimenPrintRuleDetails) reqEvent.getPrintRuleDetails()).getName(), createdRule.getName());
	}

	@Test
	public void testSuccessfullPatchPritnRule() {
		PatchPrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getPatchData();
		PrintRuleUpdatedEvent response = printRuleService.patchPrintRule(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}
	
	@Test
	public void testSuccessfullPatchPritnRuleByName() {
		PatchPrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getPatchDataWithName();
		PrintRuleUpdatedEvent response = printRuleService.patchPrintRule(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}
	
	@Test
	public void testPatchPrintRuleWithInvalidAttribute() {
		when(daoFactory.getSpecimenPrintRuleDao().isUniquePrintRuleName(anyString())).thenReturn(false);
		PatchPrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getPatchData();
		PrintRuleUpdatedEvent response = printRuleService.patchPrintRule(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals("Please resolve the highlighted errors.", response.getMessage());
	}
	
	@Test
	public void testPatchPrintRuleInvalidRule() {
		when(daoFactory.getSpecimenPrintRuleDao().getPrintRule(anyLong())).thenReturn(null);
		PatchPrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getPatchData();
		PrintRuleUpdatedEvent response = printRuleService.patchPrintRule(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}
	
	@Test
	public void testPatchPrintRule() {
		PatchPrintRuleEvent reqEvent = SpecimenPrintRuleTestData.nonPatchData();
		PrintRuleUpdatedEvent response = printRuleService.patchPrintRule(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}
	
	@Test
	public void testPrintRulePatchWithServerErr() {
		doThrow(new RuntimeException()).when(printRuleDao).saveOrUpdate(any(SpecimenPrintRule.class));
		PatchPrintRuleEvent reqEvent = SpecimenPrintRuleTestData.getPatchData();
		PrintRuleUpdatedEvent response = printRuleService.patchPrintRule(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}
}
