
package com.krishagni.catissueplus.core.printer.printRule.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.printer.printRule.domain.SpecimenPrintRule;
import com.krishagni.catissueplus.core.printer.printRule.repository.SpecimenPrintRuleDao;

public class SpecimenPrintRuleDaoImpl extends AbstractDao<SpecimenPrintRule> implements SpecimenPrintRuleDao {

	private static final String FQN = SpecimenPrintRule.class.getName();

	private static final String GET_PRINT_RULE_BY_NAME = FQN + ".getPrintRuleByName";

	private static final String GET_PRINT_RULE_BY_SPECCLASS_SPECTYPE_IP = FQN
			+ ".getPrintRuleBySpecimenClassAndSpecimenTypeAndIP";

	private static final String GET_PRINT_RULES = FQN + ".getPrintRules";
	
	@Override
	@SuppressWarnings(value = {"unchecked"})
	public SpecimenPrintRule getPrintRuleByName(String name) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_PRINT_RULE_BY_NAME);
		query.setString("name", name);
		List<SpecimenPrintRule> result = query.list();
		if (!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public boolean isUniquePrintRuleName(String name) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_PRINT_RULE_BY_NAME);
		query.setString("name", name);
		return query.list().isEmpty() ? true : false;
	}

	@Override
	public boolean isUniqueRule(String specClass, String specType, String workstationIP) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_PRINT_RULE_BY_SPECCLASS_SPECTYPE_IP);
		query.setString("specimenClass", specClass);
		query.setString("specimenType", specType);
		query.setString("workstationIP", workstationIP);
		return query.list().isEmpty() ? true : false;
	}
	
	@Override
	@SuppressWarnings(value = {"unchecked"})
	public List<SpecimenPrintRule> getRules() {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_PRINT_RULES);
		return query.list();
	}

	@Override
	public SpecimenPrintRule getPrintRule(Long id) {
		return (SpecimenPrintRule) sessionFactory.getCurrentSession().get(SpecimenPrintRule.class, id);
	}

}
