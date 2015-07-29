package com.krishagni.openspecimen.custom.sgh.services.impl;


import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;

import com.krishagni.catissueplus.core.biospecimen.services.DocumentDeIdentifier;

public class SprDeIdentifier implements DocumentDeIdentifier {
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public String deIdentify(String report, Map<String, Object> contextMap) {
		report = deIdentifyText(report);
		return report;
	}

	private String deIdentifyText(String report) {
		List<Object[]> list = sessionFactory.getCurrentSession().createSQLQuery(GET_DEIDENTIFICATION_TEXT).list();

		StringBuilder regexPhi = new StringBuilder();
		StringBuilder regexDeleteNextLine = new StringBuilder();
		StringBuilder regexDeleteLine = new StringBuilder();
		StringBuilder regexDeIdentifyWord = new StringBuilder();
		
		for (Object[] obj : list) {
			String searchText = (String) obj[0];
			boolean deleteLine = (obj[1] != null) ? (Boolean) obj[1] : false;
			boolean deleteNextLine = (obj[2] != null) ? (Boolean) obj[2] : false;
			if (deleteNextLine) {
				if (searchText.equals("Primary Provider:")) {
					addOr(regexPhi);
					regexPhi.append(searchText);
				} else {
					addOr(regexDeleteNextLine); 
					regexDeleteNextLine.append(searchText);
				}
			} else if (deleteLine) {
				addOr(regexDeleteLine);
				regexDeleteLine.append(searchText);
			} else {
				addOr(regexDeIdentifyWord);
				regexDeIdentifyWord.append(searchText);
			}
		}
	
		report = replaceString(report, REPLACE_5_LINE_REGX, regexPhi, true);
		report = replaceString(report, REPLACE_2_LINE_REGX, regexDeleteNextLine, true);
		report = replaceString(report, REPLACE_1_LINE_REGX, regexDeleteLine, true);
		report = replaceString(report, REPLACE_WORD, regexDeIdentifyWord, false);
		return report;
	}
	
	private String replaceString(String input, String regexFmt, StringBuilder regexText, boolean delete) {
		if (regexText.length() <= 0) {
			return input;
		}
		regexFmt = String.format(regexFmt, regexText);
		String replacement = delete ? StringUtils.EMPTY : REPLACEMENT_STRING;
		return input.replaceAll(regexFmt, replacement);
	}

	private void addOr(StringBuilder cond) {
		if (cond.length() > 0) {
			cond.append("|");
		}
	}
	
	private static final String GET_DEIDENTIFICATION_TEXT = "select string_content, delete_line, delete_next_line from catissue_deidentified_text";
	
	private static final String REPLACEMENT_STRING = "XXX";
	
	private static final String REPLACE_5_LINE_REGX = "(?i).*?(%s)" + 
			"((.*?\\n.*?\\n.*?\\n.*?\\n.*?\\n)|(.*?\\n.*?\\n.*?\\n.*?\\n)|(.*?\\n.*?\\n.*?\\n)|(.*?\\n.*?\\n)|(.*?\\n)|(\\b))";
	
	private static final String REPLACE_2_LINE_REGX = "(?i).*?(%s)((.*?\\n.*?\\n)|(.*?\\n)|(\\b))";
	
	private static final String REPLACE_1_LINE_REGX = "(?i).*?(%s)((.*?\\n)|(\\b))";
	
	private static final String REPLACE_WORD = "(?i).*?(%s)\\b";
	
}
