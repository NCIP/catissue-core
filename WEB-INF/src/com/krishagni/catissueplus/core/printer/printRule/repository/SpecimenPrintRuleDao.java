
package com.krishagni.catissueplus.core.printer.printRule.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.catissueplus.core.printer.printRule.domain.SpecimenPrintRule;

public interface SpecimenPrintRuleDao extends Dao<SpecimenPrintRule> {

	public SpecimenPrintRule getPrintRuleByName(String name);

	public boolean isUniquePrintRuleName(String name);

	public boolean isUniqueRule(String specClass, String specType, String workstationIP, String cpShortTitle,
			String loginName);

	public SpecimenPrintRule getPrintRule(Long id);

	public List<SpecimenPrintRule> getRules(int maxResults);

}
