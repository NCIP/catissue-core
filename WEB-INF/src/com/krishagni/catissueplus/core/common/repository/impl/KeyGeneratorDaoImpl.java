
package com.krishagni.catissueplus.core.common.repository.impl;

import org.hibernate.Query; 

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.repository.KeyGeneratorDao;
import com.krishagni.catissueplus.core.common.util.KeyGenerator;

public class KeyGeneratorDaoImpl extends AbstractDao<KeyGenerator> implements KeyGeneratorDao {
	private static final String FQN = KeyGenerator.class.getName();

	private static final String GET_VALUE_BY_KEY = FQN + ".getValueByKey";
	@Override
	public KeyGenerator getKeyGeneratorValue(String  key) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_VALUE_BY_KEY);
		query.setString("key", key);
		return query.list().isEmpty() ? null : (KeyGenerator) query.list().get(0);
	}

	public void saveOrUpdate(KeyGenerator keyGenerator) {

		sessionFactory.getCurrentSession().saveOrUpdate(keyGenerator);
	}
}
