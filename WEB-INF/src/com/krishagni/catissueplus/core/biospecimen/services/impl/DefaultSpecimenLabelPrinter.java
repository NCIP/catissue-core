package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenLabelPrintJob;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenLabelPrintJobItem;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenLabelPrintJobItem.Status;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenLabelPrinter;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplTokenRegistrar;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.service.ConfigChangeListener;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.common.util.AuthUtil;

public class DefaultSpecimenLabelPrinter implements SpecimenLabelPrinter, InitializingBean, ConfigChangeListener {
	private static final Logger logger = Logger.getLogger(DefaultSpecimenLabelPrinter.class);
	
	private List<SpecimenLabelPrintRule> rules = new ArrayList<SpecimenLabelPrintRule>();
	
	private DaoFactory daoFactory;
	
	private ConfigurationService cfgSvc;
	
	private LabelTmplTokenRegistrar printLabelTokensRegistrar;
	
	private MessageSource messageSource;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setCfgSvc(ConfigurationService cfgSvc) {
		this.cfgSvc = cfgSvc;
	}

	public void setPrintLabelTokensRegistrar(LabelTmplTokenRegistrar printLabelTokensRegistrar) {
		this.printLabelTokensRegistrar = printLabelTokensRegistrar;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public SpecimenLabelPrintJob print(List<Specimen> specimens) {		
		try {
			String ipAddr = AuthUtil.getRemoteAddr();
			User currentUser = AuthUtil.getCurrentUser();
			
			SpecimenLabelPrintJob job = new SpecimenLabelPrintJob();
			job.setSubmissionDate(Calendar.getInstance().getTime());
			job.setSubmittedBy(currentUser);
	
			for (Specimen specimen : specimens) {				
				boolean found = false;
				for (SpecimenLabelPrintRule rule : rules) {
					if (!rule.isApplicableFor(specimen, currentUser, ipAddr)) {
						continue;
					}
					
					SpecimenLabelPrintJobItem item = new SpecimenLabelPrintJobItem();
					item.setJob(job);
					item.setPrinterName(rule.getPrinterName());
					item.setSpecimen(specimen);
					item.setStatus(Status.QUEUED);
					item.setLabelType(rule.getLabelType());
					item.setData(rule.formatPrintData(specimen));
						
					job.getItems().add(item);
					found = true;
					break;
				}
				
				if (!found) {
					logger.warn("No print rule matched specimen: " + specimen.getLabel());
				}
			}
			
			if (job.getItems().isEmpty()) {
				return null;				
			}
			
			daoFactory.getSpecimenLabelPrintJobDao().saveOrUpdate(job);			
			return job;
		} catch (Exception e) {
			e.printStackTrace();
			throw OpenSpecimenException.serverError(e);			
		}
	}	

	@Override
	public void afterPropertiesSet() throws Exception {
		reloadRules();
		cfgSvc.registerChangeListener(LABEL_PRINT_MODULE, this);
	}
	
	@Override
	public void onConfigChange(String name, String value) {
		reloadRules();
	}		
		
	private void reloadRules() {
		String rulesFile = cfgSvc.getStrSetting(LABEL_PRINT_MODULE, RULES_FILE_CFG_NAME, (String)null);
		if (StringUtils.isBlank(rulesFile)) {
			return;
		}
		
		boolean classpath = false;
		if (rulesFile.startsWith("classpath:")) {
			rulesFile = rulesFile.substring(10);
			classpath = true;
		}
				
		List<SpecimenLabelPrintRule> rules = new ArrayList<SpecimenLabelPrintRule>();
		
		BufferedReader reader = null;
		try {
			if (classpath) {
				reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(rulesFile)));
			} else {
				reader = new BufferedReader(new FileReader(rulesFile));
			}
						
			String ruleLine = null;
			while ((ruleLine = reader.readLine()) != null) {
				SpecimenLabelPrintRule rule = parseRule(ruleLine);
				if (rule == null) {
					continue;
				}
				
				rules.add(rule);
			}
			
			this.rules = rules;
		} catch (Exception e) {
			logger.error("Error loading rules from file: " + rulesFile, e);
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}
	
	private SpecimenLabelPrintRule parseRule(String ruleLine) {
		try {
			if (ruleLine.startsWith("#")) {
				return null;
			}
			
			String[] ruleLineFields = ruleLine.split("\\s+");
			if (ruleLineFields == null || ruleLineFields.length != 7) {
				logger.error("Invalid rule: " + ruleLine);
				return null;
			}
			
			SpecimenLabelPrintRule rule = new SpecimenLabelPrintRule();
			rule.setSpecimenClass(ruleLineFields[0]);
			rule.setSpecimenType(ruleLineFields[1]);
			rule.setUserLogin(ruleLineFields[2]);
			
			if (!ruleLineFields[3].equals("*")) {
				rule.setIpAddressMatcher(new IpAddressMatcher(ruleLineFields[3]));
			}			
			rule.setLabelType(ruleLineFields[4]);
			
			String[] labelTokens = ruleLineFields[5].split(",");			
			boolean badTokens = false;			
			
			List<LabelTmplToken> tokens = new ArrayList<LabelTmplToken>();
			for (String labelToken : labelTokens) {
				LabelTmplToken token = printLabelTokensRegistrar.getToken(labelToken);
				if (token == null) {
					logger.error("Unknown token: " + token);
					badTokens = true;
					break;
				}
				
				tokens.add(token);
			}
			
			if (badTokens) {
				return null;
			}
			
			rule.setDataTokens(tokens);
			rule.setPrinterName(ruleLineFields[6]);
			rule.setMessageSource(messageSource);
			return rule;
		} catch (Exception e) {
			logger.error("Error parsing rule: " + ruleLine, e);
		}
		
		return null;
	}
		
	private static final String LABEL_PRINT_MODULE = "label_printer";
	
	private static final String RULES_FILE_CFG_NAME = "rules_file";

}
