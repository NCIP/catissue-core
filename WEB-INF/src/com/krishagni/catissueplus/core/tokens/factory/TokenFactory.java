
package com.krishagni.catissueplus.core.tokens.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.krishagni.catissueplus.core.tokens.LabelToken;

import edu.wustl.common.util.global.CommonServiceLocator;

public class TokenFactory {

	private static final String ERROR_IN_INITIALIZATION = "Error while initializing Token Properties";

	private static final String ERROR_IN_CLASS_INITIALIZATION = "Error while initializing Label generator class";

	private static final String LABEL_TOKEN_PROP_FILE_NAME = "Tokens.properties";

	private static Map<String, String> tokenMap;

	private boolean isToken(String tokenKey) {
		return tokenMap.containsKey(tokenKey);

	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private void initializeLableTokens() {
		try {
			final String absolutePath = CommonServiceLocator.getInstance().getPropDirPath() + File.separator
					+ LABEL_TOKEN_PROP_FILE_NAME;
			final InputStream inputStream = new FileInputStream(new File(absolutePath));
			Properties labelTokenProperties = new Properties();
			labelTokenProperties.load(inputStream);
			tokenMap = new HashMap(labelTokenProperties);
		}
		catch (Exception e) {
			new RuntimeException(ERROR_IN_INITIALIZATION, e);
		}
	}

	public String getTokenValue(String tokenKey, Object object) {
		try {
			if (tokenMap == null) {
				initializeLableTokens();
			}
			if (isToken(tokenKey)) {
				String className = tokenMap.get(tokenKey);
				LabelToken labelToken = (LabelToken) Class.forName(className).newInstance();
				return labelToken.getTokenValue(object);
			}
			else {
				return tokenKey;
			}

		}
		catch (Exception e) {
			new RuntimeException(ERROR_IN_CLASS_INITIALIZATION, e);
		}
		return null;
	}
}
