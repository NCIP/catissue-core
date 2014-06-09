
package com.krishagni.catissueplus.core.common.util;

import java.util.HashMap;
import java.util.Map;

import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.events.MaxCurrentValueDto;

public class BucketPool {

	private static Map<String, MaxCurrentValueDto> bucketPool = new HashMap<String, MaxCurrentValueDto>();

	private static Long MaxKeyValue = 5L;

	private static BucketPool bucketPoolObj;

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	private BucketPool() {

	}

	public static BucketPool getInstance() {
		if (bucketPoolObj != null) {
			bucketPoolObj = new BucketPool();
		}
		return bucketPoolObj;

	}

	public Long getNextValue(String key) {
		Long value = null;
		if (bucketPool.containsKey(key)) {
			MaxCurrentValueDto maxCurrentValueDto = bucketPool.get(key);
			updateBucketPool(key, maxCurrentValueDto);
			value = maxCurrentValueDto.getCurrentValue();
		}
		else {
			value = addKey(key);
		}
		return value;
	}

	private void updateBucketPool(String key, MaxCurrentValueDto maxCurrentValueDto) {

		if (maxCurrentValueDto.getCurrentValue().equals(maxCurrentValueDto.getMaxValue())) {
			KeyGenerator keyGenerator = daoFactory.getKeyGeneratorDao().getKeyGeneratorValue(key);
			keyGenerator.setValue(keyGenerator.getValue() + MaxKeyValue);
			daoFactory.getKeyGeneratorDao().saveOrUpdate(keyGenerator);
			maxCurrentValueDto.setMaxValue(keyGenerator.getValue());
			maxCurrentValueDto.setCurrentValue((keyGenerator.getValue() - MaxKeyValue) + 1);
			bucketPool.put(key, maxCurrentValueDto);
		}
		else {
			maxCurrentValueDto.incrementCurrentValue();
		}
	}

	private Long addKey(String key) {
		Long value = null;
		KeyGenerator keyGenerator = daoFactory.getKeyGeneratorDao().getKeyGeneratorValue(key);
		if (keyGenerator == null) {
			keyGenerator = new KeyGenerator();
			keyGenerator.setKey(key);
			keyGenerator.setValue(MaxKeyValue);
			keyGenerator.setKeyType(key);
			daoFactory.getKeyGeneratorDao().saveOrUpdate(keyGenerator);

			MaxCurrentValueDto maxCurrentValueDto = new MaxCurrentValueDto(1L, MaxKeyValue);
			bucketPool.put(key, maxCurrentValueDto);
			value = maxCurrentValueDto.getCurrentValue();
		}
		else {
			keyGenerator.setValue(keyGenerator.getValue() + MaxKeyValue);
			value = (keyGenerator.getValue() - MaxKeyValue) + 1;
			daoFactory.getKeyGeneratorDao().saveOrUpdate(keyGenerator);

			MaxCurrentValueDto maxCurrentValueDto = new MaxCurrentValueDto(value, keyGenerator.getValue());
			bucketPool.put(key, maxCurrentValueDto);
		}
		return value;

	}
}
