
package com.krishagni.catissueplus.core.services.testdata;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.printer.printRule.domain.SpecimenPrintRule;
import com.krishagni.catissueplus.core.printer.printRule.events.CreatePrintRuleEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.DeletePrintRuleEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.PatchPrintRuleEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.SpecimenPrintRuleDetails;
import com.krishagni.catissueplus.core.printer.printRule.events.UpdatePrintRuleEvent;
import com.krishagni.catissueplus.core.privileges.domain.Privilege;
import com.krishagni.catissueplus.core.privileges.domain.factory.PrivilegeType;

import edu.wustl.common.beans.SessionDataBean;

public class SpecimenPrintRuleTestData {

	public static final String PRINT_RULE_NAME = "print rule name";
	
	public static final String SPECIMEN_CLASS = "specimen class";

	public static final String PRINTER_NAME = "printer name";

	public static final String LABEL_TYPE = "label type";

	public static final Object SPECIMEN_TYPE = "specimen type";

	public static final Object DATA_ON_LABEL = "data on label";

	private static final String PATCH_PRINT_RULE = "patch print rule";

	public static final Object WORKSTATION_IP = "workstation ip";

	public static SessionDataBean getSessionDataBean() {
		SessionDataBean sessionDataBean = new SessionDataBean();
		sessionDataBean.setAdmin(true);
		sessionDataBean.setCsmUserId("1");
		sessionDataBean.setFirstName("admin");
		sessionDataBean.setIpAddress("127.0.0.1");
		sessionDataBean.setLastName("admin");
		sessionDataBean.setUserId(1L);
		sessionDataBean.setUserName("admin@admin.com");
		return sessionDataBean;
	}

	public static CreatePrintRuleEvent getCreatePrintRuleEventWithEmptyPrintRuleName() {
		CreatePrintRuleEvent event = getCreatePrintRuleEvent();
		SpecimenPrintRuleDetails details = (SpecimenPrintRuleDetails) event.getDetails();
		details.setName(null);
		event.setDetails(details);
		return event;
	}

	public static CreatePrintRuleEvent getCreatePrintRuleEvent() {
		CreatePrintRuleEvent event = new CreatePrintRuleEvent(null);
		event.setSessionDataBean(getSessionDataBean());
		SpecimenPrintRuleDetails details = getPrintRuleDetails();
		event.setDetails(details);
		return event;
	}

	private static SpecimenPrintRuleDetails getPrintRuleDetails() {
		SpecimenPrintRuleDetails details = new SpecimenPrintRuleDetails();
		details.setName("My PrintRule");
		details.setDataOnLabel(getDataLabels());
		details.setPrinterName("tbw");
		details.setSpecimenClass("Tissue");
		details.setSpecimenType("Cell");
		details.setWorkstationIP("192.168.2.1");
		details.setLabelType(getLabelTypes());
		return details;
	}

	private static Set<String> getLabelTypes() {
		Set<String> labelTypes = new HashSet<String>();
		labelTypes.add("Cap+Slide");
		return labelTypes;
	}

	private static Set<String> getDataLabels() {
		Set<String> dataLabels = new HashSet<String>();
		dataLabels.add("CP Title");
		dataLabels.add("Specimen type");
		dataLabels.add("Created On");
		return dataLabels;
	}

	public static SpecimenPrintRule getPrintRule(long id) {
		SpecimenPrintRule printRule = new SpecimenPrintRule();
		printRule.setId(id);
		printRule.setName("My PrintRule");
		printRule.setDataOnLabel(" Label,  type,CP title,Created On");
		printRule.setPrinterName("tbw");
		printRule.setSpecimenClass("Tissue");
		printRule.setSpecimenType("Cell");
		printRule.setWorkstationIP("Any");
		printRule.setLabelType("Slide");
		return printRule;
	}

	public static UpdatePrintRuleEvent getUpdatePrintRuleEvent() {
		UpdatePrintRuleEvent event = new UpdatePrintRuleEvent(new SpecimenPrintRuleDetails(), "My Rule");
		event.setSessionDataBean(getSessionDataBean());
		SpecimenPrintRuleDetails details = getPrintRuleDetails();
		event.setPrintRuleDetails(details);
		return event;
	}
	
	public static UpdatePrintRuleEvent getUpdatePrintRuleEventById() {
		UpdatePrintRuleEvent event = new UpdatePrintRuleEvent(new SpecimenPrintRuleDetails(), 1l);
		event.setSessionDataBean(getSessionDataBean());
		SpecimenPrintRuleDetails details = getPrintRuleDetails();
		event.setPrintRuleDetails(details);
		return event;
	}

	public static Privilege getPrivilege(long id) {
		Privilege privilege = new Privilege();
		privilege.setId(id);
		privilege.setName(PrivilegeType.DISTRIBUTION.value());
		return privilege;
	}

	public static CreatePrintRuleEvent getCreatePrintRuleEventWithEmptyPrinterName() {
		CreatePrintRuleEvent event = getCreatePrintRuleEvent();
		((SpecimenPrintRuleDetails) event.getDetails()).setPrinterName(null);
		return event;
	}

	public static DeletePrintRuleEvent getDeletePrintRuleEvent() {
		DeletePrintRuleEvent event = new DeletePrintRuleEvent();
		return event;
	}
	
	public static DeletePrintRuleEvent getDeletePrintRuleEventByName() {
		DeletePrintRuleEvent event = new DeletePrintRuleEvent();
		event.setPrintRuleName("My Rule");
		return event;
	}

	public static CreatePrintRuleEvent getCreatePrintRuleEventWithEmptySpecimenClass() {
		CreatePrintRuleEvent event = getCreatePrintRuleEvent();
		((SpecimenPrintRuleDetails) event.getDetails()).setSpecimenClass(null);
		return event;
	}

	public static CreatePrintRuleEvent getCreatePrintRuleEventWithEmptySpecimenType() {
		CreatePrintRuleEvent event = getCreatePrintRuleEvent();
		((SpecimenPrintRuleDetails) event.getDetails()).setSpecimenType(null);
		return event;
	}

	public static CreatePrintRuleEvent getCreatePrintRuleEventWithEmptyLabelType() {
		CreatePrintRuleEvent event = getCreatePrintRuleEvent();
		((SpecimenPrintRuleDetails) event.getDetails()).setLabelType(new HashSet<String>());
		return event;
	}

	public static CreatePrintRuleEvent getCreatePrintRuleEventWithEmptyDataOnLabel() {
		CreatePrintRuleEvent event = getCreatePrintRuleEvent();
		((SpecimenPrintRuleDetails) event.getDetails()).setDataOnLabel(new HashSet<String>());
		return event;
	}

	public static CreatePrintRuleEvent getCreatePrintRuleEventWithAnySpecimenClass() {
		CreatePrintRuleEvent event = getCreatePrintRuleEvent();
		((SpecimenPrintRuleDetails) event.getDetails()).setSpecimenClass("Any");
		return event;
	}

	public static CreatePrintRuleEvent getCreatePrintRuleEventWithAnySpecimenType() {
		CreatePrintRuleEvent event = getCreatePrintRuleEvent();
		((SpecimenPrintRuleDetails) event.getDetails()).setSpecimenType("any");
		return event;
	}

	public static CreatePrintRuleEvent getCreatePrintRuleEventWithEmptyWorkstationIP() {
		CreatePrintRuleEvent event = getCreatePrintRuleEvent();
		((SpecimenPrintRuleDetails) event.getDetails()).setWorkstationIP(null);
		return event;
	}

	public static CreatePrintRuleEvent getCreatePrintRuleEventWithAnyWorkstationIP() {
		CreatePrintRuleEvent event = getCreatePrintRuleEvent();
		((SpecimenPrintRuleDetails) event.getDetails()).setWorkstationIP("any");
		return event;
	}

	public static CreatePrintRuleEvent getCreatePrintRuleEventWithInvalidLabelType() {
		CreatePrintRuleEvent event = getCreatePrintRuleEvent();
		Set<String> labelTypes = new HashSet<String>();
		labelTypes.add("sasa");
		((SpecimenPrintRuleDetails) event.getDetails()).setLabelType(labelTypes);
		return event;
	}

	public static PatchPrintRuleEvent getPatchData() {
		PatchPrintRuleEvent event = new PatchPrintRuleEvent();
		event.setPrintRuleId(1l);
		SpecimenPrintRuleDetails details = new SpecimenPrintRuleDetails();
		try {
			BeanUtils.populate(details, getPrintRulePatchAttributes());
		}
		catch (Exception e) {
			reportError(UserErrorCode.BAD_REQUEST, PATCH_PRINT_RULE);
		}
		details.setModifiedAttributes(new ArrayList<String>(getPrintRulePatchAttributes().keySet()));
		event.setPrintRuleDetails(details);
		return event;
	}

	private static Map<String, Object> getPrintRulePatchAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("specimenClass", "Fluid");
		attributes.put("specimenType", "any");
		attributes.put("labelType", getLabelTypes());
		attributes.put("dataOnLabel", getDataLabels());
		attributes.put("printerName", "t1");
		attributes.put("name", "My Print Rule");
		attributes.put("workstationIP", "192.168.2.2");
		return attributes;
	}

	public static PatchPrintRuleEvent nonPatchData() {
		PatchPrintRuleEvent event = new PatchPrintRuleEvent();
		event.setPrintRuleId(1l);
		SpecimenPrintRuleDetails details = new SpecimenPrintRuleDetails();
		event.setPrintRuleDetails(details);
		return event;
	}

	public static PatchPrintRuleEvent getPatchDataWithName() {
		PatchPrintRuleEvent event = getPatchData();
		event.setPrintRuleName("My Print Rule");
		return event;
	}

	public static CreatePrintRuleEvent getCreatePrintRuleEventWithInvalidWorkstationIP() {
		CreatePrintRuleEvent event = getCreatePrintRuleEvent();
		((SpecimenPrintRuleDetails) event.getDetails()).setWorkstationIP("192.2.");
		return event;
	}

	public static UpdatePrintRuleEvent getUpdatePrintRuleEventForChangeInRuleAndName() {
		UpdatePrintRuleEvent event = new UpdatePrintRuleEvent(getPrintRuleDetails(), 1l);
		event.setSessionDataBean(getSessionDataBean());
		SpecimenPrintRuleDetails details = ((SpecimenPrintRuleDetails)event.getPrintRuleDetails());
		details.setName("My Print Rule 1");
		details.setSpecimenClass("Cell");
		details.setSpecimenType("Blood");
		details.setWorkstationIP("Any");
		event.setPrintRuleDetails(details);
		return event;
	}

	public static UpdatePrintRuleEvent getUpdatePrintRuleEventForChangeSpecimenClassAndName() {
		UpdatePrintRuleEvent event = getUpdatePrintRuleEvent();
		SpecimenPrintRuleDetails details = ((SpecimenPrintRuleDetails) event.getPrintRuleDetails());
		details.setName("New_print_rule");
		details.setSpecimenClass("Tissue");
		event.setPrintRuleDetails(details);
		return event;
	}

	public static UpdatePrintRuleEvent getUpdatePrintRuleEventForChangeSpecimenTypeAndName() {
		UpdatePrintRuleEvent event = getUpdatePrintRuleEvent();
		SpecimenPrintRuleDetails details = ((SpecimenPrintRuleDetails) event.getPrintRuleDetails());
		details.setName("New_print_rule");
		details.setSpecimenType("MyType");
		event.setPrintRuleDetails(details);
		return event;
	}

	public static UpdatePrintRuleEvent getUpdatePrintRuleEventForChangeWorkstationIPAndName() {
		UpdatePrintRuleEvent event = getUpdatePrintRuleEvent();
		SpecimenPrintRuleDetails details = ((SpecimenPrintRuleDetails) event.getPrintRuleDetails());
		details.setName("New_print_rule");
		details.setWorkstationIP("192.168.2.2");
		event.setPrintRuleDetails(details);
		return event;
	}
}
