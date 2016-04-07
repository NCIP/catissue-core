package com.krishagni.openspecimen.rde.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.openspecimen.rde.services.BarcodeParser;
import com.krishagni.openspecimen.rde.services.BarcodeTokenRegistrar;
import com.krishagni.openspecimen.rde.tokens.BarcodePart;
import com.krishagni.openspecimen.rde.tokens.BarcodeToken;

@Configurable
public class BarcodeParserImpl implements BarcodeParser {
	private Pattern bcPattern = Pattern.compile("%(.+?)%");
	
	private Pattern fnTokenPattern = Pattern.compile("(.+?)\\((.+?)\\)");

	private Map<String, CollectionProtocol> cpCache = new HashMap<String, CollectionProtocol>();
	
	@Autowired
	@Qualifier("barcodeTokenRegistrar")
	private BarcodeTokenRegistrar registrar;
	
	@Autowired
	private DaoFactory daoFactory;
		
	public void setBarcodeTokenRegistrar(BarcodeTokenRegistrar registrar) {
		this.registrar = registrar;
	}
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public List<BarcodePart> parseVisitBarcode(String barcode) {		
		List<BarcodePart> result = new ArrayList<BarcodePart>();
		BarcodePart cpCode = parseCpCode(barcode);
		result.add(cpCode);
		
		if (cpCode.getValue() == null) {
			return result;
		}
		
		CollectionProtocol cp = (CollectionProtocol)cpCode.getValue();		
		String visitNameFmt = cp.getVisitNameFormat();
		
		Map<String, Object> ctxMap = Collections.<String, Object>singletonMap("cp", cp);		
		Matcher matcher = bcPattern.matcher(visitNameFmt);
		int startIdx = 0;
		while (matcher.find()) {
			Pair<String, String[]> tokenArgs = getTokenNameArgs(matcher.group(1));
			BarcodeToken token = registrar.getToken(tokenArgs.first());
			if (token == null) {
				// TODO:
			}

			BarcodePart tpResult = null;
			if (startIdx < barcode.length()) {
				tpResult = token.parse(ctxMap, barcode, startIdx, tokenArgs.second());
				startIdx = tpResult.getEndIdx() + 1;
			} else {
				tpResult = new BarcodePart(token.getName());
			}

			result.add(tpResult);
		}
		
		return result;
	}

	private BarcodePart parseCpCode(String barcode) {
		String[] parts = barcode.split("-", 2);
		
		CollectionProtocol cp = null;
		if (!cpCache.containsKey(parts[0])) {
			cp = daoFactory.getCollectionProtocolDao().getCpByCode(parts[0]);
			cpCache.put(parts[0], cp);
		} else {
			cp = cpCache.get(parts[0]); 
		}
		
		BarcodePart result = new BarcodePart();
		result.setToken("CP_CODE");
		result.setCode(parts[0]);
		result.setStartIdx(0);
		result.setEndIdx(0); // do not consume CP code at beginning
		result.setValue(cp);
		if (cp != null) {
			result.setDisplayValue(cp.getShortTitle());
		}

		return result;		
	}

	private Pair<String, String[]> getTokenNameArgs(String input) {
		String tokenName = null;
		String[] args = null;

		Matcher fnMatcher = fnTokenPattern.matcher(input);
		if (fnMatcher.matches()) {
			tokenName = fnMatcher.group(1);
			args = fnMatcher.group(2).split(",");
		} else {
			tokenName = input;
		}

		return Pair.make(tokenName, args);
	}
}
