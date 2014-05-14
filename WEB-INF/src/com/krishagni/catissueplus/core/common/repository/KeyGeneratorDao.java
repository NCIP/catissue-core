
package com.krishagni.catissueplus.core.common.repository;

import com.krishagni.catissueplus.core.common.util.KeyGenerator;

public interface KeyGeneratorDao extends Dao<KeyGenerator> {

	public KeyGenerator getKeyGeneratorValue(String key);

	public void saveOrUpdate(KeyGenerator keyGenerator);

}
