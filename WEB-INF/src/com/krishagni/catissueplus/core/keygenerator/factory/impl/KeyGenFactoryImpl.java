
package com.krishagni.catissueplus.core.keygenerator.factory.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.util.KeyGenFactory;
import com.krishagni.catissueplus.core.common.util.KeyGenerator;

public class KeyGenFactoryImpl implements KeyGenFactory{

	private DaoFactory daoFactory;

	@Autowired
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@PlusTransactional
	public synchronized Long getValueByKey(String key , String keyType) {
		KeyGenerator keyGenerator = daoFactory.getKeyGeneratorDao().getKeyGeneratorValue(key);
		if (keyGenerator == null) {
			keyGenerator = new KeyGenerator();
			keyGenerator.setKey(key);
			keyGenerator.setValue(keyGenerator.increment());
			keyGenerator.setKeyType(keyType);
			daoFactory.getKeyGeneratorDao().saveOrUpdate(keyGenerator);
		}
		else {
			keyGenerator.setValue(keyGenerator.increment());
			daoFactory.getKeyGeneratorDao().saveOrUpdate(keyGenerator);
		}
		return keyGenerator.getValue();
	}

}
