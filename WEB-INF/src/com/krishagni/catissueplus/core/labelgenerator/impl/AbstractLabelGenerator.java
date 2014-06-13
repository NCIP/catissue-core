
package com.krishagni.catissueplus.core.labelgenerator.impl;

import java.util.StringTokenizer;

import com.krishagni.catissueplus.core.labelgenerator.LabelGenerator;
import com.krishagni.catissueplus.core.tokens.factory.TokenFactory;

public class AbstractLabelGenerator<T> implements LabelGenerator<T> {

	private static final String BUCKET_POOL = "bucketPool";

	private TokenFactory tokenFactory;

	public void setTokenFactory(TokenFactory tokenFactory) {
		this.tokenFactory = tokenFactory;
	}

	/*	public String getUniqueId(String key) {
			ApplicationContext caTissueContext = CaTissueAppContext.getInstance();
			BucketPool bucketPool = (BucketPool) caTissueContext.getBean(BUCKET_POOL);
			return bucketPool.getNextValue(key).toString();
		}*/

	@Override
	public String generateLabel(String labelFormat, T object) {
		StringTokenizer tokens = new StringTokenizer(labelFormat, "%");
		StringBuffer label = new StringBuffer();
		while (tokens.hasMoreElements()) {
			String tokenKey = (String) tokens.nextElement();
			label.append(tokenFactory.getTokenValue(tokenKey, object));
		}
		return label.toString();
	}

}
