package com.krishagni.catissueplus.core.init;

import java.io.File;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.de.ui.StorageContainerControlFactory;
import com.krishagni.catissueplus.core.de.ui.StorageContainerMapper;
import com.krishagni.catissueplus.core.de.ui.UserControlFactory;
import com.krishagni.catissueplus.core.de.ui.UserFieldMapper;

import edu.common.dynamicextensions.domain.nui.factory.ControlManager;
import edu.common.dynamicextensions.nutility.DEApp;
import edu.wustl.dynamicextensions.formdesigner.mapper.ControlMapper;

public class DeInitializer implements InitializingBean {
	private PlatformTransactionManager transactionManager;
	
	private ConfigurationService cfgSvc;
	
	private DataSource dataSource;
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	public void setCfgSvc(ConfigurationService cfgSvc) {
		this.cfgSvc = cfgSvc;		 
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		String dateFormat = cfgSvc.getStrSetting("common", "date_format", "MM-dd-yyyy");
		String timeFormat = cfgSvc.getStrSetting("common", "time_format", "HH:mm");
		String dataDir = cfgSvc.getStrSetting("common", "data_dir", ".");
		
		String dir = new StringBuilder(dataDir).append(File.separator)
			.append("de-file-data").append(File.separator)
			.toString();
		
		File dirFile = new File(dir);
		if (!dirFile.exists()) {
			if (!dirFile.mkdirs()) {
				throw new RuntimeException("Error couldn't create directory for storing de file data");
			}
		}
					
		DEApp.init(dataSource, transactionManager, dir, dateFormat, timeFormat);
		ControlManager.getInstance().registerFactory(UserControlFactory.getInstance());			
		ControlMapper.getInstance().registerControlMapper("userField", new UserFieldMapper());
		
		ControlManager.getInstance().registerFactory(StorageContainerControlFactory.getInstance());
		ControlMapper.getInstance().registerControlMapper("storageContainer", new StorageContainerMapper());		
	}
}
