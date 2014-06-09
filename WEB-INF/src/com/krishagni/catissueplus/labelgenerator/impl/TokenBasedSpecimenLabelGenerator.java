
package com.krishagni.catissueplus.labelgenerator.impl;

import java.util.StringTokenizer;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.labelgenerator.LabelGenerator;

public class TokenBasedSpecimenLabelGenerator implements LabelGenerator<Specimen> {

	private TokenFactory tokenFactory;

	public TokenFactory getTokenFactory() {
		return tokenFactory;
	}

	public void setTokenFactory(TokenFactory tokenFactory) {
		this.tokenFactory = tokenFactory;
	}

	@Override
	public String generateLabel(Specimen specimen) {

		String specimenLabelFormat = specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration()
				.getCollectionProtocol().getSpecimenLabelFormat();
		if (specimenLabelFormat != null) {
			StringTokenizer tokens = new StringTokenizer(specimenLabelFormat, "%");
			StringBuffer label = new StringBuffer();
			while (tokens.hasMoreElements()) {
				String tokenKey = (String) tokens.nextElement();
				label.append(tokenFactory.getTokenValue(tokenKey, specimen));
			}
			return label.toString();
		}
		return null;
	}
}
