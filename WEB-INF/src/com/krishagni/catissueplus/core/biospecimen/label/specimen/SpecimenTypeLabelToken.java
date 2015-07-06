package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.biospecimen.ConfigParams;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.service.ConfigChangeListener;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.common.util.Utility;

public class SpecimenTypeLabelToken extends AbstractSpecimenLabelToken implements InitializingBean, ConfigChangeListener {

	@Autowired
	private ConfigurationService cfgSvc;

	private Map<String, String> spmnAbbrTypeMap = new HashMap<String, String>();
	
	public void setCfgSvc(ConfigurationService cfgSvc) {
		this.cfgSvc = cfgSvc;
	}

	public SpecimenTypeLabelToken() {
		this.name = "SP_TYPE";
	}

	@Override
	public String getLabel(Specimen specimen) {
		return spmnAbbrTypeMap.get(specimen.getSpecimenType());
	}

	@Override
	public void onConfigChange(String name, String value) {
		if (!name.equals(ConfigParams.SPECIMEN_TYPE_ABBR_MAP)) {
			return;
		}

		loadSpmnTypeAbbrMap();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		loadSpmnTypeAbbrMap();
		cfgSvc.registerChangeListener(ConfigParams.MODULE, this);
	}

	private void loadSpmnTypeAbbrMap() {
		InputStream defIn = null;
		InputStream custIn = null;
				
		try {
			defIn = Utility.getResourceInputStream(defSpmnTypeAbbrMapFile);
			if (defIn == null) {
				throw OpenSpecimenException.serverError(new RuntimeException("Couldn't open default specimen type mapping file"));
			}
			
			spmnAbbrTypeMap.putAll(getAbbrMap(defIn));
			
			String customMappingFile = cfgSvc.getStrSetting(
					ConfigParams.MODULE, 
					ConfigParams.SPECIMEN_TYPE_ABBR_MAP, 
					"");
			
			if (StringUtils.isBlank(customMappingFile)) {
				return;
			}
			
			if (customMappingFile.startsWith("classpath:")) {
				custIn = Utility.getResourceInputStream(customMappingFile.substring(10));
			} else {
				custIn = new FileInputStream(customMappingFile);
			}
			
			spmnAbbrTypeMap.putAll(getAbbrMap(custIn));			
		} catch (Exception e) {
			logger.error("Error loading specimen type abbreviation map", e);
			throw OpenSpecimenException.serverError(e);
		} finally {
			IOUtils.closeQuietly(defIn);
			IOUtils.closeQuietly(custIn);
		}		
	}

	private Map<String, String> getAbbrMap(InputStream in) {
		BufferedReader reader = null;
		Map<String, String> result = new HashMap<String, String>();
		
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}
				
				String[] fields = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				if (fields.length != 2) {
					logger.error("Invalid input line: " + line + ". Ignoring");
					continue;
				}
				
				String type = fields[0].trim().replaceAll("\"", "");
				String abbr = fields[1].trim().replaceAll("\"", "");
				
				if (StringUtils.isEmpty(type)) {
					logger.error("Invalid input line: " + line + ". Type is empty. Ignoring");
					continue;
				}
				
				if (StringUtils.isEmpty(abbr)) {
					logger.error("Invalid input line: " + line + ". Abbreviation is empty. Ignoring");					
					continue;
					
				}
				
				result.put(type, abbr);
			}
			
			return result;
		} catch (Exception e) {
			logger.error("Error loading specimen type abbreviation map", e);
			throw OpenSpecimenException.serverError(e);
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}
	
	private static final Logger logger = Logger.getLogger(SpecimenTypeLabelToken.class);
	
	private static final String defSpmnTypeAbbrMapFile = "/com/krishagni/catissueplus/core/biospecimen/specimen-type-abbr.csv";	
}