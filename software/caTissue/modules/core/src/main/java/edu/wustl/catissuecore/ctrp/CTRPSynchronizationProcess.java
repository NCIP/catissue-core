package edu.wustl.catissuecore.ctrp;

import java.util.List;

import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.bizlogic.InstitutionBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;

/**
 * This class synchronizes remotely managed elements in caTissue with CTRP data
 * on a configurable frequency.
 * 
 * @author Ravi Batchu
 * 
 */
public final class CTRPSynchronizationProcess {

	private static final Logger logger = Logger
			.getCommonLogger(CTRPSynchronizationProcess.class);
	private COPPAServiceClient coppaServiceClient;
	private boolean enabled = true;

	public void process() {
		if (isEnabled())
			try {
				processSync();
			} catch (Exception e) {
				logger.error(e.toString(), e);
			}
	}

	private void processSync() throws BizLogicException, Exception {
		processSyncInstitutions();
		processSyncUsers();
		processSyncProtocols();
	}

	private void processSyncInstitutions() throws BizLogicException, Exception {
		coppaServiceClient = new COPPAServiceClient();
		InstitutionBizLogic institutionBizLogic = new InstitutionBizLogic();
		List<Institution> syncList = institutionBizLogic
				.getRemoteSyncInstitutions();
		logger.debug("Remote Institutions Sync List Size:" + syncList.size());
		int updateCount = 0;
		for (Institution localInstitution : syncList) {
			Institution remoteInstitution = coppaServiceClient
					.getRemoteInstituionById(localInstitution.getRemoteId()
							+ "");
			if (!COPPAUtil.compareAndSyncData(localInstitution,
					remoteInstitution)) {
				// Update local entity only if there is change in COPPA
				logger.debug("Updating Local Entity with remote data:"
						+ remoteInstitution.getName());
				institutionBizLogic.update(localInstitution);
				updateCount++;
			}// end -if
		}// end-for
		logger.info("Number of Institutions Syncrhonized from CTRP:"
				+ updateCount);
	}

	private void processSyncUsers() throws BizLogicException, Exception {
		coppaServiceClient = new COPPAServiceClient();
		UserBizLogic userBizLogic = new UserBizLogic();
		SessionDataBean sessionDataBean = new SessionDataBean();
		sessionDataBean.setAdmin(true);
		sessionDataBean.setCsmUserId("1");
		sessionDataBean.setUserId(new Long("1"));
		List<User> syncList = userBizLogic.getRemoteSyncUsers();
		logger.debug("Remote Users Sync List Size:" + syncList.size());
		int updateCount = 0;
		final String applicationName = CommonServiceLocator.getInstance()
				.getAppName();
		DAO dao = null;
		dao = DAOConfigFactory.getInstance().getDAOFactory(applicationName)
				.getDAO();

		for (User localUser : syncList) {
			User remoteUser = coppaServiceClient.getRemoteUserById(localUser
					.getRemoteId() + "");
			if (!COPPAUtil.compareAndSyncData(localUser, remoteUser)) {
				// Update local entity only if there is change in COPPA
				remoteUser.setId(localUser.getId());
				logger.debug("Updating Local Entity with remote data:"
						+ remoteUser.getFirstName());
				dao.openSession(sessionDataBean);
				dao.update(localUser);
				dao.update(localUser.getAddress());
				dao.commit();
				dao.closeSession();
				// userBizLogic.update(localUser, null, null, sessionDataBean);
				updateCount++;
			}// end -if
		}// end-for
		logger.info("Number of Users Syncrhonized from CTRP:" + updateCount);
	}

	private void processSyncProtocols() throws BizLogicException, Exception {
		coppaServiceClient = new COPPAServiceClient();
		CollectionProtocolBizLogic collectionProtocolBizLogic = new CollectionProtocolBizLogic();
		List<CollectionProtocol> syncList = collectionProtocolBizLogic
				.getRemoteSyncCollectionProtocols();
		int updateCount = 0;
		logger.debug("Remote Protocols Sync List Size:" + syncList.size());
		SessionDataBean sessionDataBean = new SessionDataBean();
		sessionDataBean.setAdmin(true);
		sessionDataBean.setCsmUserId("1");
		sessionDataBean.setUserId(new Long("1"));
		final String applicationName = CommonServiceLocator.getInstance()
				.getAppName();
		DAO dao = null;
		dao = DAOConfigFactory.getInstance().getDAOFactory(applicationName)
				.getDAO();
		for (CollectionProtocol localCollectionProtocol : syncList) {
			CollectionProtocol remoteCollectionProtocol = coppaServiceClient
					.getRemoteProtocolById(localCollectionProtocol
							.getRemoteId() + "");
			if (!COPPAUtil.compareAndSyncData(localCollectionProtocol,
					remoteCollectionProtocol)) {
				// Update local entity only if there is change in COPPA
				logger.debug("Updating Local Entity with remote data:"
						+ remoteCollectionProtocol.getTitle());
				// dao.openSession(sessionDataBean);
				// dao.update(localCollectionProtocol);
				// dao.commit();
				// dao.closeSession();
				collectionProtocolBizLogic.update(localCollectionProtocol,
						null, null, sessionDataBean);
				updateCount++;
			}// end -if
		}// end-for
		logger.info("Number of CollectionProtocols Syncrhonized from CTRP:"
				+ updateCount);
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
