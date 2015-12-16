
package com.krishagni.catissueplus.core.init;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.common.domain.ConfigSetting;
import com.krishagni.catissueplus.core.common.events.ConfigSettingDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.catissueplus.core.de.domain.QueryFolder;
import com.krishagni.catissueplus.core.de.domain.SavedQuery;
import com.krishagni.catissueplus.core.de.repository.DaoFactory;

public class ImportDefaultQueries implements InitializingBean {
	
	private static Log LOGGER = LogFactory.getLog(ImportDefaultQueries.class);

	private PlatformTransactionManager txnMgr;

	private UserDao userDao;

	private DaoFactory daoFactory;
	
	private com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory bioSpecimenDaoFactory;
	
	private ConfigurationService cfgService;
	
	private User sysUser;

	public void setTxnMgr(PlatformTransactionManager txnMgr) {
		this.txnMgr = txnMgr;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setBioSpecimenDaoFactory(
			com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory bioSpecimenDaoFactory) {
		this.bioSpecimenDaoFactory = bioSpecimenDaoFactory;
	}
	
	public void setCfgService(ConfigurationService cfgService) {
		this.cfgService = cfgService;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		TransactionTemplate txnTmpl = new TransactionTemplate(txnMgr);
		txnTmpl.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRED);
		txnTmpl.execute(new TransactionCallback<Void>() {

			@Override
			public Void doInTransaction(TransactionStatus status) {
				try {
					importQueries();
					return null;
				} catch (Exception e) {
					status.setRollbackOnly();
					throw new RuntimeException(e);
				}
			}
		});
	}

	private void importQueries() throws Exception {
		sysUser = userDao.getSystemUser();
		Resource[] resources = new PathMatchingResourcePatternResolver().getResources(QUERIES_DIRECTORY + "/**");

		Set<SavedQuery> queries = new HashSet<SavedQuery>();
		for (Resource resource : resources) {
			String filename = QUERIES_DIRECTORY + "/" + resource.getFilename();
			LOGGER.info("Importing query from file: " + filename);
			String newDigest = Utility.getResourceDigest(filename);
			byte[] content = IOUtils.toByteArray(resource.getURI());
			Map<String, Object> result = daoFactory.getSavedQueryDao().getQueryChangeLogDetails(filename);
			
			if (result == null) {
				SavedQuery query = insertQuery(filename, content, newDigest);
				if(query != null){
					queries.add(query);	
				}
				
				if(resource.getFilename().equals("SpecimenCatalog.json")) {
					UpdateConfiguration(query);
				}
			} else {
				Long queryId = ((Number)result.get("queryId")).longValue();
				String existingDigest = (String)result.get("md5Digest");

				if (existingDigest != null && existingDigest.equals(newDigest)) {
					LOGGER.info("No change found in file " + filename + " since last import");
					continue;
				}
				updateQuery(queryId, filename, content, newDigest);
			}
		}
		shareDefaultQueries(queries);
	}
	
	private void UpdateConfiguration(SavedQuery query) {
		User user = bioSpecimenDaoFactory.getUserDao().getSystemUser();
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null, null);
		SecurityContextHolder.getContext().setAuthentication(token);
		
		ConfigSettingDetail cfgDetail = new ConfigSettingDetail();
		cfgDetail.setModule("catalog");
		cfgDetail.setName("default_query");
		cfgDetail.setValue(query.getId().toString());
		RequestEvent<ConfigSettingDetail> req = new RequestEvent<ConfigSettingDetail>(cfgDetail); 
		ResponseEvent<ConfigSettingDetail> resp = cfgService.saveSetting(req);
		resp.throwErrorIfUnsuccessful();
	}

	private SavedQuery insertQuery(String filename, byte[] queryContent, String md5Digest) {
		SavedQuery savedQuery = new SavedQuery();
		try {
			savedQuery.setQueryDefJson(new String(queryContent), true);
			savedQuery.setCreatedBy(sysUser);
			savedQuery.setLastUpdated(new Date());
			savedQuery.setLastUpdatedBy(sysUser);
			daoFactory.getSavedQueryDao().saveOrUpdate(savedQuery);
			insertChangeLog(filename, md5Digest, "INSERTED", savedQuery.getId());
		} catch (Exception e) {
			LOGGER.error("Error saving query definition from file: " + filename, e);
		}
		return savedQuery;
	}

	private void updateQuery(Long queryId, String filename, byte[] queryContent, String md5Digest) {
		try {
			SavedQuery savedQuery = daoFactory.getSavedQueryDao().getQuery(queryId);
			if(savedQuery == null){
				savedQuery = new SavedQuery();
			}
			
			savedQuery.setQueryDefJson(new String(queryContent), true);
			daoFactory.getSavedQueryDao().saveOrUpdate(savedQuery);
			insertChangeLog(filename, md5Digest, "UPDATED", queryId);
		} catch (Exception e) {
			LOGGER.error("Error updating query " + queryId + " using definition from file: " + filename, e);
		}
	}

	private void insertChangeLog(String filename, String md5Digest, String status, Long id) {
		daoFactory.getSavedQueryDao().insertQueryChangeLog(filename, md5Digest, status, id);
	}

	private void shareDefaultQueries(Set<SavedQuery> queries) {
		QueryFolder folder = daoFactory.getQueryFolderDao().getByName(DEFAULT_QUERIES);
		if (folder == null) {
			folder = new QueryFolder();
			folder.setOwner(sysUser);
		}
		folder.getSavedQueries().addAll(queries);
		folder.setSharedWithAll(true);
		folder.setName(DEFAULT_QUERIES);
		daoFactory.getQueryFolderDao().saveOrUpdate(folder);
	}
	
	private static final String DEFAULT_QUERIES = "Default Queries";
	
	private static final String QUERIES_DIRECTORY = "/default-queries";

}
