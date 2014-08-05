
package com.krishagni.catissueplus.core.printer.printRule.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.printer.printRule.domain.SpecimenPrintRule;
import com.krishagni.catissueplus.core.printer.printRule.domain.factory.PrintRuleErrorCode;
import com.krishagni.catissueplus.core.printer.printRule.domain.factory.impl.SpecimenPrintRuleFactoryImpl;
import com.krishagni.catissueplus.core.printer.printRule.events.CreatePrintRuleEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.DeletePrintRuleEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.GetAllPrintRulesEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.GetPrintRuleEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.PatchPrintRuleEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.PrintRuleCreatedEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.PrintRuleDeletedEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.PrintRuleGotEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.PrintRuleUpdatedEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.ReqAllPrintRulesEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.SpecimenPrintRuleDetails;
import com.krishagni.catissueplus.core.printer.printRule.events.SpecimenPrintRulePatchDetails;
import com.krishagni.catissueplus.core.printer.printRule.events.UpdatePrintRuleEvent;
import com.krishagni.catissueplus.core.printer.printRule.services.PrintRuleService;

public class SpecimenPrintRuleServiceImpl implements PrintRuleService {

	private static final String PRINT_RULE_NAME = "print rule name";

	private DaoFactory daoFactory;

	private SpecimenPrintRuleFactoryImpl specimenPrintRuleFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setSpecimenPrintRuleFactory(SpecimenPrintRuleFactoryImpl specimenPrintRuleFactory) {
		this.specimenPrintRuleFactory = specimenPrintRuleFactory;
	}

	@Override
	@PlusTransactional
	public PrintRuleCreatedEvent createPrintRule(CreatePrintRuleEvent event) {
		try {
			SpecimenPrintRule specimenPrintRule = specimenPrintRuleFactory
					.createSpecimenPrintRule((SpecimenPrintRuleDetails) event.getDetails());
			ObjectCreationException exceptionHandler = new ObjectCreationException();
			ensureUniqueName(specimenPrintRule.getName(), exceptionHandler);
			ensureUniqueRule(specimenPrintRule, exceptionHandler);
			exceptionHandler.checkErrorAndThrow();
			daoFactory.getSpecimenPrintRuleDao().saveOrUpdate(specimenPrintRule);
			return PrintRuleCreatedEvent.ok(SpecimenPrintRuleDetails.fromDomain(specimenPrintRule));
		}
		catch (ObjectCreationException ce) {
			return PrintRuleCreatedEvent.invalidRequest(PrintRuleErrorCode.ERRORS.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return PrintRuleCreatedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public PrintRuleUpdatedEvent updatePrintRule(UpdatePrintRuleEvent event) {
		try {
			SpecimenPrintRule oldSpecimenPrintRule = null;
			String printRuleName = event.getPrintRuleName();
			if (printRuleName != null) {
				oldSpecimenPrintRule = daoFactory.getSpecimenPrintRuleDao().getPrintRuleByName(printRuleName);
			}
			else {
				oldSpecimenPrintRule = daoFactory.getSpecimenPrintRuleDao().getPrintRule(event.getPrintRuleId());
			}

			if (oldSpecimenPrintRule == null) {
				return PrintRuleUpdatedEvent.notFound();
			}
			SpecimenPrintRule specimenPrintRule = specimenPrintRuleFactory
					.createSpecimenPrintRule((SpecimenPrintRuleDetails) event.getPrintRuleDetails());

			ObjectCreationException exceptionHandler = new ObjectCreationException();
			checkChangeInPrintName(oldSpecimenPrintRule.getName(), specimenPrintRule.getName(), exceptionHandler);
			checkChangeInRule(oldSpecimenPrintRule, specimenPrintRule, exceptionHandler);
			exceptionHandler.checkErrorAndThrow();

			oldSpecimenPrintRule.update(specimenPrintRule);
			daoFactory.getSpecimenPrintRuleDao().saveOrUpdate(oldSpecimenPrintRule);
			return PrintRuleUpdatedEvent.ok(SpecimenPrintRuleDetails.fromDomain(oldSpecimenPrintRule));
		}
		catch (ObjectCreationException ce) {
			return PrintRuleUpdatedEvent.invalidRequest(PrintRuleErrorCode.ERRORS.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return PrintRuleUpdatedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public PrintRuleDeletedEvent deletePrintRule(DeletePrintRuleEvent event) {
		try {
			SpecimenPrintRule specimenPrintRule = null;
			String printRuleName = event.getPrintRuleName();
			if (printRuleName != null) {
				specimenPrintRule = daoFactory.getSpecimenPrintRuleDao().getPrintRuleByName(printRuleName);
			}
			else {
				specimenPrintRule = daoFactory.getSpecimenPrintRuleDao().getPrintRule(event.getPrintRuleId());
			}

			if (specimenPrintRule == null) {
				return PrintRuleDeletedEvent.notFound();
			}
			daoFactory.getSpecimenPrintRuleDao().delete(specimenPrintRule);
			return PrintRuleDeletedEvent.ok();
		}
		catch (Exception e) {
			return PrintRuleDeletedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public PrintRuleUpdatedEvent patchPrintRule(PatchPrintRuleEvent event) {
		try {
			SpecimenPrintRule oldSpecimenPrintRule = null;
			String printRuleName = event.getPrintRuleName();
			if (printRuleName != null) {
				oldSpecimenPrintRule = daoFactory.getSpecimenPrintRuleDao().getPrintRuleByName(printRuleName);
			}
			else {
				oldSpecimenPrintRule = daoFactory.getSpecimenPrintRuleDao().getPrintRule(event.getPrintRuleId());
			}
			if (oldSpecimenPrintRule == null) {
				return PrintRuleUpdatedEvent.notFound();
			}
			SpecimenPrintRule specimenPrintRule = specimenPrintRuleFactory.patchSpecimenPrintRule(oldSpecimenPrintRule,
					(SpecimenPrintRulePatchDetails) event.getPrintRuleDetails());

			if (((SpecimenPrintRulePatchDetails) event.getPrintRuleDetails()).isNameModified()) {
				ObjectCreationException exceptionHandler = new ObjectCreationException();
				ensureUniqueName(specimenPrintRule.getName(), exceptionHandler);
				exceptionHandler.checkErrorAndThrow();
			}

			daoFactory.getSpecimenPrintRuleDao().saveOrUpdate(specimenPrintRule);
			return PrintRuleUpdatedEvent.ok(SpecimenPrintRuleDetails.fromDomain(specimenPrintRule));
		}
		catch (ObjectCreationException ce) {
			return PrintRuleUpdatedEvent.invalidRequest(PrintRuleErrorCode.ERRORS.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return PrintRuleUpdatedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public GetAllPrintRulesEvent getPrintAllRules(ReqAllPrintRulesEvent event) {
		List<SpecimenPrintRule> ruleList = daoFactory.getSpecimenPrintRuleDao().getRules(event.getMaxResults());
		List<SpecimenPrintRuleDetails> details = new ArrayList<SpecimenPrintRuleDetails>();
		for (SpecimenPrintRule rule : ruleList) {
			details.add(SpecimenPrintRuleDetails.fromDomain(rule));
		}
		return GetAllPrintRulesEvent.ok(details);
	}

	@Override
	@PlusTransactional
	public PrintRuleGotEvent getPrintRule(GetPrintRuleEvent event) {
		try {
			SpecimenPrintRule printRule = null;
			if (event.getName() != null) {
				printRule = daoFactory.getSpecimenPrintRuleDao().getPrintRuleByName(event.getName());
				if (printRule == null) {
					return PrintRuleGotEvent.notFound(event.getName());
				}
			}
			else {
				printRule = daoFactory.getSpecimenPrintRuleDao().getPrintRule(event.getId());
				if (printRule == null) {
					return PrintRuleGotEvent.notFound(event.getId());
				}
			}
			return PrintRuleGotEvent.ok(SpecimenPrintRuleDetails.fromDomain(printRule));
		}
		catch (Exception e) {
			return PrintRuleGotEvent.serverError(e);
		}
	}

	private void ensureUniqueRule(SpecimenPrintRule specimenPrintRule, ObjectCreationException exceptionHandler) {
		if (!daoFactory.getSpecimenPrintRuleDao().isUniqueRule(specimenPrintRule.getSpecimenClass(),
				specimenPrintRule.getSpecimenType(), specimenPrintRule.getWorkstationIP(), specimenPrintRule.getLoginName(),
				specimenPrintRule.getCpShortTitle())) {
			exceptionHandler.addError(PrintRuleErrorCode.DUPLICATE_PRINT_RULE, PRINT_RULE_NAME);
		}
	}

	private void ensureUniqueName(String name, ObjectCreationException exceptionHandler) {
		if (!daoFactory.getSpecimenPrintRuleDao().isUniquePrintRuleName(name)) {
			exceptionHandler.addError(PrintRuleErrorCode.DUPLICATE_PRINT_RULE, PRINT_RULE_NAME);
		}
	}

	private void checkChangeInRule(SpecimenPrintRule oldSpecimenPrintRule, SpecimenPrintRule specimenPrintRule,
			ObjectCreationException exceptionHandler) {
		if (!oldSpecimenPrintRule.getSpecimenClass().equals(specimenPrintRule.getSpecimenClass())
				|| !oldSpecimenPrintRule.getSpecimenType().equals(specimenPrintRule.getSpecimenType())
				|| !oldSpecimenPrintRule.getWorkstationIP().equals(specimenPrintRule.getWorkstationIP())
				|| !oldSpecimenPrintRule.getCpShortTitle().equals(specimenPrintRule.getCpShortTitle())
				|| !oldSpecimenPrintRule.getLoginName().equals(specimenPrintRule.getLoginName())) {
			ensureUniqueRule(specimenPrintRule, exceptionHandler);
		}

	}

	private void checkChangeInPrintName(String oldPrintRuleName, String newPrintRuleName,
			ObjectCreationException exceptionHandler) {
		if (!oldPrintRuleName.equals(newPrintRuleName)) {
			ensureUniqueName(newPrintRuleName, exceptionHandler);
		}
	}

}
