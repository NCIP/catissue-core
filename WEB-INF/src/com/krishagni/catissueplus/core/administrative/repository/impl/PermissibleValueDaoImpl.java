package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.repository.PermissibleValueDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;


public class PermissibleValueDaoImpl extends AbstractDao<PermissibleValue> implements PermissibleValueDao {

	@Override
	public PermissibleValue getPermissibleValue(Long id) {
		return (PermissibleValue) sessionFactory.getCurrentSession().get(PermissibleValue.class, id);
	}

	@Override
	@SuppressWarnings(value = {"unchecked"})
	public Boolean isUniqueValueInAttribute(String value, String attribute) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_PV_BY_VALUE_AND_NAME);
		query.setString("value", value);
		query.setString("attribute", attribute);
		List<PermissibleValue> result = query.list();
		return result.isEmpty() ? true : false;
	}
	
	@Override
	@SuppressWarnings(value = {"unchecked"})
	public PermissibleValue getPvByValueAndAttribute(String value, String attribute) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_PV_BY_VALUE_AND_NAME);
		query.setString("value", value);
		query.setString("attribute", attribute);
		List<PermissibleValue> result = query.list();
		return result.isEmpty() ? null : result.get(0);
	}

	@Override
	@SuppressWarnings(value = {"unchecked"})
	public Boolean isUniqueConceptCode(String conceptCode) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_PV_BY_CONCEPT_CODE);
		query.setString("conceptCode", conceptCode);
		List<User> result = query.list();
		return result.isEmpty() ? true : false;
	}
	
	@Override
	@SuppressWarnings(value = {"unchecked"})
	public List<PermissibleValue> getAllPVsByAttribute(String attribute, String searchString, int maxResult) {
		boolean isSearchTermSpecified = !StringUtils.isBlank(searchString);
   
		String queryAttribute = GET_PVS_BY_NAME;
		if (isSearchTermSpecified) {
			queryAttribute = GET_PVS_BY_NAME_AND_SEARCH_TERM;
		}
		Query query = sessionFactory.getCurrentSession().getNamedQuery(queryAttribute);
		query.setMaxResults(maxResult);
		query.setString("attribute", attribute);
		if (isSearchTermSpecified) {
			query.setString("searchTerm", searchString.toLowerCase() + "%");
		}
		return query.list();
	}
	
	@Override
	@SuppressWarnings(value = {"unchecked"})
	public List<String> getAllValuesByAttribute(String attribute) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_VALUES_BY_NAME);
		query.setString("attribute", attribute);
		return query.list();
	}
	
	@Override
	@SuppressWarnings(value = {"unchecked"})
	public Boolean isPvAvailable(String attribute, String parentValue, String value) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_PV_BY_NAME_VALUE_PARRENT);
		query.setString("attribute", attribute);
		query.setString("value", value);
		query.setString("parentValue", parentValue);
		List<User> result = query.list();
		return result.isEmpty() ? false : true;
	}
	
	private static final String FQN = PermissibleValue.class.getName();

	private static final String GET_PV_BY_VALUE_AND_NAME = FQN + ".getPVByValueAndAttribute";
	
	private static final String GET_PV_BY_CONCEPT_CODE = FQN + ".getPVByConceptCode";
	
	private static final String GET_PVS_BY_NAME = FQN + ".getPVsByAttribute";
	
	private static final String GET_VALUES_BY_NAME = FQN + ".getValuesByAttribute";

	private static final String GET_PV_BY_NAME_VALUE_PARRENT = FQN + ".getPvByAttributeAndValueAndParent";
	
	private static final String GET_PVS_BY_NAME_AND_SEARCH_TERM = FQN+ ".getPVsByAttributeAndSearchTerm";
}
