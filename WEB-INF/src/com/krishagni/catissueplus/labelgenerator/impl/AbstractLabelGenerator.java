
package com.krishagni.catissueplus.labelgenerator.impl;

import org.springframework.context.ApplicationContext;

import com.krishagni.catissueplus.core.common.CaTissueAppContext;
import com.krishagni.catissueplus.core.common.util.BucketPool;
import com.krishagni.catissueplus.labelgenerator.LabelGenerator;

public class AbstractLabelGenerator<T> implements LabelGenerator<T> {

	private static final String BUCKET_POOL = "bucketPool";

	@Override
	public String generateLabel(T object) {
		return null;
	}

	public String getUniqueId(String key) {
		ApplicationContext caTissueContext = CaTissueAppContext.getInstance();
		BucketPool bucketPool = (BucketPool) caTissueContext.getBean(BUCKET_POOL);
		return bucketPool.getNextValue(key).toString();
	}

}
