package com.krishagni.catissueplus.core.biospecimen.services;

public interface DocumentDeIdentifier {

	String deIdentify(String data, Long visitId);
}
