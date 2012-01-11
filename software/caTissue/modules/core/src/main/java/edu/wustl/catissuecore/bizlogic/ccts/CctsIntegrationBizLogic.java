/**
 * 
 */
package edu.wustl.catissuecore.bizlogic.ccts;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;  
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;

import edu.duke.cabig.c3pr.webservice.subjectregistration.EnrollSubjectNewRequest;
import edu.duke.cabig.c3pr.webservice.subjectregistration.EnrollSubjectNewResponse;
import edu.duke.cabig.c3pr.webservice.subjectregistration.SubjectRegistration;
import edu.duke.cabig.c3pr.webservice.subjectregistration.TakeSubjectOffStudyRequest;
import edu.duke.cabig.c3pr.webservice.subjectregistration.TakeSubjectOffStudyResponse;
import edu.wustl.catissuecore.bizlogic.ICollectionProtocolBizLogic;
import edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic;
import edu.wustl.catissuecore.bizlogic.IParticipantBizLogic;
import edu.wustl.catissuecore.bizlogic.IUserBizLogic;
import edu.wustl.catissuecore.dao.DataQueueDAO;
import edu.wustl.catissuecore.dao.NotificationDAO;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.ccts.DataQueue;
import edu.wustl.catissuecore.domain.ccts.Notification;
import edu.wustl.catissuecore.domain.ccts.ProcessingStatus;
import edu.wustl.catissuecore.util.MessagesHolder;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.lookup.DefaultLookupResult;
import edu.wustl.common.participant.utility.ParticipantManagerUtility;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.patientLookUp.util.PatientLookupException;

/**
 * This class contains business operations for CCTS integration.
 * 
 * @author Denis G. Krylov
 * 
 */
@Transactional
public class CctsIntegrationBizLogic implements ICctsIntegrationBizLogic,
		ApplicationContextAware {

	private NotificationDAO notificationDAO;

	private DataQueueDAO dataQueueDAO;

	private IParticipantBizLogic participantBizLogic;

	private ICollectionProtocolRegistrationBizLogic cprBizLogic;

	private ICollectionProtocolBizLogic cpBizLogic;

	private IUserBizLogic userBizLogic;

	private List<IDataConverter> dataConverters;

	private List<IDomainObjectComparator> domainObjectComparators;

	private IRegistrationConverter registrationConverter;

	private IParticipantConverter participantConverter;

	private ApplicationContext applicationContext;

	private String serviceClientBeanId;

	private boolean cctsEnabled;

	private MessageSource messageSource;

	private static final ISO21090Helper h = null;

	private static final Logger logger = Logger
			.getCommonLogger(CctsIntegrationBizLogic.class);

	/**
	 * @return the notificationDAO
	 */
	public final NotificationDAO getNotificationDAO() {
		return notificationDAO;
	}

	/**
	 * @param notificationDAO
	 *            the notificationDAO to set
	 */
	public final void setNotificationDAO(NotificationDAO notificationDAO) {
		this.notificationDAO = notificationDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.catissuecore.bizlogic.ccts.ICctsIntegrationBizLogic#
	 * getNotifications(int, int)
	 */
	@Transactional(readOnly = true)
	public Collection<Notification> getNotifications(int firstResult,
			int maxResults) {
		return notificationDAO.getNotifications(firstResult, maxResults);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.catissuecore.bizlogic.ccts.ICctsIntegrationBizLogic#
	 * getNotificationCount()
	 */
	@Transactional(readOnly = true)
	public int getNotificationCount() {
		return notificationDAO.getCountByExample(new Notification());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.catissuecore.bizlogic.ccts.ICctsIntegrationBizLogic#
	 * getNotificationById(int)
	 */
	@Override
	@Transactional(readOnly = true)
	public Notification getNotificationById(long id) {
		return notificationDAO.getById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.catissuecore.bizlogic.ccts.ICctsIntegrationBizLogic#
	 * pickNotificationForProcessing()
	 */
	@Override
	public Notification pickNotificationForProcessing() {
		return notificationDAO.pickNotificationForProcessing();
	}

	@Override
	public void updateNotification(Notification notification) {
		notificationDAO.update(notification);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.catissuecore.bizlogic.ccts.ICctsIntegrationBizLogic#
	 * addDataQueueItem(edu.wustl.catissuecore.domain.ccts.DataQueue)
	 */
	@Override
	public void addDataQueueItem(DataQueue dataQueueItem) {
		dataQueueDAO.save(dataQueueItem);
	}

	/**
	 * @return the dataQueueDAO
	 */
	public final DataQueueDAO getDataQueueDAO() {
		return dataQueueDAO;
	}

	/**
	 * @param dataQueueDAO
	 *            the dataQueueDAO to set
	 */
	public final void setDataQueueDAO(DataQueueDAO dataQueueDAO) {
		this.dataQueueDAO = dataQueueDAO;
	}

	@Override
	public int getPendingDataQueueItemsCount() {
		return dataQueueDAO.getPendingIncomingDataQueueItemsCount();
	}

	@Override
	public DataQueue pickDataQueueItemForProcessing() {
		final DataQueue item = dataQueueDAO
				.pickIncomingDataQueueItemForProcessing();
		if (item != null) {
			linkQueueItemToDomainObject(item);
			if (item.getParticipant() != null) {
				dataQueueDAO.initialize(item.getParticipant());
			}
			if (item.getRegistration() != null) {
				dataQueueDAO.initialize(item.getRegistration());
				participantBizLogic.init(item.getRegistration());
			}
		}
		return item;
	}

	@Override
	public void linkQueueItemToDomainObject(DataQueue item) {
		String gridId = item.getNotification().getObjectIdValue();
		if (StringUtils.isNotBlank(gridId)) {
			if (item.isParticipantRelated() && item.getParticipant() == null) {
				Participant participant;
				try {
					participant = participantBizLogic
							.getParticipantByGridId(gridId);
				} catch (BizLogicException e) {
					throw new RuntimeException(e);
				}
				if (participant == null) {
					// could not establish a link by grid id. Let's try
					// participant's biz identifier: MRN
					try {
						participant = participantBizLogic
								.getParticipantByPMI(convertParticipant(item,
										null)
										.getParticipantMedicalIdentifierCollection());
					} catch (Exception e) {
						logger.error(e);
					}
				}
				if (participant != null) {
					item.setParticipant(participant);
					dataQueueDAO.update(item);
				}
			}
			if (item.isRegistrationRelated() && item.getRegistration() == null) {
				CollectionProtocolRegistration cpr;
				try {
					cpr = cprBizLogic.getRegistrationByGridId(gridId);
				} catch (BizLogicException e) {
					throw new RuntimeException(e);
				}
				if (cpr == null) {
					try {
						final CollectionProtocolRegistration convertedCPR = convertRegistration(
								item, null);
						cpr = cprBizLogic.findRegistration(
								convertedCPR.getCollectionProtocol(),
								convertedCPR.getParticipant());
					} catch (Exception e) {
						logger.error(e);
					}
				}
				if (cpr != null) {
					item.setRegistration(cpr);
					dataQueueDAO.update(item);
				}
			}
		}
	}

	/**
	 * @return the participantBizLogic
	 */
	public final IParticipantBizLogic getParticipantBizLogic() {
		return participantBizLogic;
	}

	/**
	 * @param participantBizLogic
	 *            the participantBizLogic to set
	 */
	public final void setParticipantBizLogic(
			IParticipantBizLogic participantBizLogic) {
		this.participantBizLogic = participantBizLogic;
	}

	@Override
	public List<IDomainObjectComparisonResult> getParticipantComparison(
			DataQueue queueItem, IErrorsReporter errorsReporter) {
		List<IDomainObjectComparisonResult> list = new ArrayList();
		try {
			if (queueItem.isParticipantRelated()) {
				Participant newParticipant = convertParticipant(queueItem,
						errorsReporter);
				Participant oldParticipant = queueItem.getParticipant() != null ? queueItem
						.getParticipant() : new Participant();
				createParticipantComparison(list, newParticipant,
						oldParticipant);
			}
			if (queueItem.isRegistrationRelated()) {
				CollectionProtocolRegistration cpr = convertRegistration(
						queueItem, errorsReporter);
				Participant newParticipant = cpr.getParticipant();
				Participant oldParticipant = cpr.getParticipant().getId() != null ? participantBizLogic
						.getParticipantById(cpr.getParticipant().getId())
						: new Participant();
				createParticipantComparison(list, newParticipant,
						oldParticipant);
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
			if (errorsReporter != null) {
				errorsReporter
						.error("An internal processing error has occurred. It is advised to either reject this item or contact your system administrator for support.");
			}
		}
		return list;
	}

	@Override
	public List<IDomainObjectComparisonResult> getRegistrationComparison(
			DataQueue queueItem, IErrorsReporter errorsReporter) {
		List<IDomainObjectComparisonResult> list = new ArrayList<IDomainObjectComparisonResult>();
		try {
			if (queueItem.isRegistrationRelated()) {
				CollectionProtocolRegistration newCpr = convertRegistration(
						queueItem, errorsReporter);
				validateRegistration(newCpr, errorsReporter);
				CollectionProtocolRegistration oldCpr = queueItem
						.getRegistration() != null ? queueItem
						.getRegistration()
						: new CollectionProtocolRegistration();
				createRegistrationComparison(list, newCpr, oldCpr);
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
			if (errorsReporter != null) {
				errorsReporter
						.error("An internal processing error has occurred. It is advised to either reject this item or contact your system administrator for support.");
			}
		}
		return list;
	}

	private void createRegistrationComparison(
			List<IDomainObjectComparisonResult> list,
			CollectionProtocolRegistration newCpr,
			CollectionProtocolRegistration oldCpr) {
		for (IDomainObjectComparator comparator : getDomainObjectComparators()) {
			if (comparator.supports(CollectionProtocolRegistration.class)) {
				list.addAll(comparator.compare(oldCpr, newCpr));
			}
		}
	}

	/**
	 * @param list
	 * @param newParticipant
	 * @param oldParticipant
	 */
	private void createParticipantComparison(
			List<IDomainObjectComparisonResult> list,
			Participant newParticipant, Participant oldParticipant) {
		for (IDomainObjectComparator comparator : getDomainObjectComparators()) {
			if (comparator.supports(Participant.class)) {
				list.addAll(comparator.compare(oldParticipant, newParticipant));
			}
		}
	}

	@Override
	public boolean isEligibleForAutoProcessing(DataQueue queueItem) {
		final MutableBoolean ok = new MutableBoolean(true);
		final IErrorsReporter errorsReporter = new IErrorsReporter() {
			@Override
			public void error(String msg) {
				ok.setValue(false);
			}
		};

		if (queueItem.isParticipantRelated()) {
			convertParticipant(queueItem, errorsReporter);
			if (!getParticipantMatchingResults(queueItem, null).isEmpty()) {
				ok.setValue(false);
			}
		} else if (queueItem.isRegistrationRelated()) {
			CollectionProtocolRegistration cpr = convertRegistration(queueItem,
					errorsReporter);
			if (ok.booleanValue()) {
				// no errors during conversion. But we need to perform
				// additional
				// validation
				validateRegistration(cpr, errorsReporter);
			}

		}
		return ok.booleanValue();
	}

	private void validateRegistration(CollectionProtocolRegistration cpr,
			IErrorsReporter errorsReporter) {
		Participant p = cpr.getParticipant();
		if (p.getId() == null) {
			if (!getParticipantMatchingResults(p, null).isEmpty()) {
				errorsReporter
						.error("Potential matches among the existing participants have been found.");
			}
		}

		try {
			// if this is a new registration, we need to validate it for
			// duplicate PPI, for example.
			if (cprBizLogic.findRegistration(cpr.getCollectionProtocol(),
					cpr.getParticipant()) == null)
				cprBizLogic.validateCPR(cpr);
		} catch (BizLogicException e) {
			errorsReporter.error(e.getMessage());
		}
	}

	@Override
	public CollectionProtocolRegistration convertRegistration(
			DataQueue queueItem, final IErrorsReporter iErrorsReporter) {
		CollectionProtocolRegistration base = queueItem.getRegistration() != null ? queueItem
				.getRegistration() : new CollectionProtocolRegistration();
		CollectionProtocolRegistration baseCopy = new CollectionProtocolRegistration();
		try {
			org.springframework.beans.BeanUtils.copyProperties(base, baseCopy);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		convertRegistration(queueItem, iErrorsReporter, baseCopy);
		return baseCopy;

	}

	private void convertRegistration(DataQueue queueItem,
			IErrorsReporter errorsReporter,
			CollectionProtocolRegistration baseCopy) {
		for (IDataConverter<AbstractDomainObject> converter : getDataConverters()) {
			if (CollectionProtocolRegistration.class.equals(converter
					.supports(queueItem))) {
				converter.convert(baseCopy, queueItem, errorsReporter);
			}
		}

	}

	private Participant convertParticipant(DataQueue queueItem,
			IErrorsReporter errorsReporter) {
		Participant base = queueItem.getParticipant() != null ? queueItem
				.getParticipant() : new Participant();
		Participant baseCopy = new Participant();
		try {
			PropertyUtils.copyProperties(baseCopy, base);    
			baseCopy.setRaceCollection(new LinkedHashSet());
			if (base.getRaceCollection() != null)
				baseCopy.getRaceCollection().addAll(base.getRaceCollection());

			baseCopy.setParticipantMedicalIdentifierCollection(new LinkedHashSet());
			if (base.getParticipantMedicalIdentifierCollection() != null)
				baseCopy.getParticipantMedicalIdentifierCollection().addAll(
						base.getParticipantMedicalIdentifierCollection());

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		convertParticipant(queueItem, errorsReporter, baseCopy);
		return baseCopy;
	}

	/**
	 * @param queueItem
	 * @param errorsReporter
	 * @param baseCopy
	 */
	private void convertParticipant(DataQueue queueItem,
			IErrorsReporter errorsReporter, Participant baseCopy) {
		for (IDataConverter<AbstractDomainObject> converter : getDataConverters()) {
			if (Participant.class.equals(converter.supports(queueItem))) {
				converter.convert(baseCopy, queueItem, errorsReporter);
			}
		}
	}

	/**
	 * @return the dataConverters
	 */
	public final List<IDataConverter> getDataConverters() {
		return dataConverters;
	}

	/**
	 * @param dataConverters
	 *            the dataConverters to set
	 */
	public final void setDataConverters(List<IDataConverter> dataConverters) {
		this.dataConverters = dataConverters;
	}

	/**
	 * @return the domainObjectComparators
	 */
	public final List<IDomainObjectComparator> getDomainObjectComparators() {
		return domainObjectComparators;
	}

	/**
	 * @param domainObjectComparators
	 *            the domainObjectComparators to set
	 */
	public final void setDomainObjectComparators(
			List<IDomainObjectComparator> domainObjectComparators) {
		this.domainObjectComparators = domainObjectComparators;
	}

	@Override
	public void rejectQueueItem(DataQueue item) {
		item.setProcessingStatus(ProcessingStatus.REJECTED);
		dataQueueDAO.update(item);
	}

	@Override
	public void acceptQueueItem(DataQueue queueItem, String userName)
			throws BizLogicException {
		acceptQueueItem(queueItem, userName, null);
	}

	@Override
	public void addDataQueueItemWithAutoProcess(DataQueue dataQueue) {
		for (DataQueue item : dataQueueDAO.getPendingDataQueueItems(dataQueue
				.getNotification().getObjectIdValue())) {
			rejectQueueItem(item);
		}
		addDataQueueItem(dataQueue);
		try {
			autoProcessDataQueue();
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
	}

	@Override
	public List<DefaultLookupResult> getParticipantMatchingResults(
			DataQueue queueItem, IErrorsReporter errorsReporter) {
		List<DefaultLookupResult> list = new ArrayList<DefaultLookupResult>();
		// only run participant matching for 'new' participants.
		if (queueItem.isParticipantRelated()
				&& queueItem.getParticipant() == null) {
			final Participant participant = convertParticipant(queueItem, null);
			list = getParticipantMatchingResults(participant, errorsReporter);
		}
		if (queueItem.isRegistrationRelated()
				&& queueItem.getRegistration() == null) {
			CollectionProtocolRegistration cpr = convertRegistration(queueItem,
					null);
			if (cpr.getParticipant().getId() == null) {
				list = getParticipantMatchingResults(cpr.getParticipant(),
						errorsReporter);
			}
		}
		return list;
	}

	private List<DefaultLookupResult> getParticipantMatchingResults(
			Participant participant, IErrorsReporter errorsReporter) {
		List<DefaultLookupResult> list = new ArrayList<DefaultLookupResult>();
		if (ParticipantManagerUtility.isCallToLookupLogicNeeded(participant)) {
			try {
				list.addAll(ParticipantManagerUtility
						.getListOfMatchingParticipants(participant, null, -1L));
			} catch (PatientLookupException e) {
				logger.error(e.toString(), e);
				if (errorsReporter != null) {
					errorsReporter
							.error("Unable to run participant matching algorithm due to an internal error: "
									+ e.getMessage());
				}
			}
		}

		return list;
	}

	@Override
	public void acceptQueueItem(DataQueue queueItem, String userName,
			Participant forceParticipant) throws BizLogicException {

		try {
			RegistrationPersistenceListener.suspendForCurrentThread();

			if (queueItem.isParticipantRelated()) {
				acceptParticipantRelatedItem(queueItem, userName,
						forceParticipant);
			}
			if (queueItem.isRegistrationRelated()) {
				acceptRegistrationRelatedItem(queueItem, userName,
						forceParticipant);
			}
			queueItem.setProcessingStatus(ProcessingStatus.COMPLETED);
			dataQueueDAO.update(queueItem);
		} finally {
			RegistrationPersistenceListener.resumeForCurrentThread();
		}
	}

	private void acceptRegistrationRelatedItem(DataQueue queueItem,
			String userName, Participant forceParticipant)
			throws BizLogicException {
		CollectionProtocolRegistration cpr = convertRegistration(queueItem,
				null);
		CollectionProtocol cp = cpr.getCollectionProtocol();
		Participant p;

		// first, store the participant's data
		if (forceParticipant == null) {
			saveOrUpdateParticipant(userName, cpr.getParticipant());
			p = cpr.getParticipant();
		} else {
			p = forceParticipant;
		}

		// let's try to see if there is an existing registration of this
		// participant on this protocol already.
		CollectionProtocolRegistration existingCpr = queueItem
				.getRegistration();
		if (existingCpr == null) {
			existingCpr = cprBizLogic.findRegistration(cp, p);
		}

		CollectionProtocolRegistration cprToSave;
		if (existingCpr != null) {
			// updating an existing registration is easy.
			participantBizLogic.init(existingCpr);
			existingCpr.setActivityStatus(cpr.getActivityStatus());
			existingCpr
					.setConsentSignatureDate(cpr.getConsentSignatureDate() != null ? cpr
							.getConsentSignatureDate() : existingCpr
							.getConsentSignatureDate());
			existingCpr.setGridId(cpr.getGridId());
			existingCpr.setParticipant(p);
			existingCpr.setProtocolParticipantIdentifier(cpr
					.getProtocolParticipantIdentifier());
			existingCpr.setRegistrationDate(cpr.getRegistrationDate());
			cprToSave = existingCpr;
		} else {
			cpr.setParticipant(p);
			cprToSave = cpr;
		}
		dataQueueDAO.clear();
		cprBizLogic.saveOrUpdateRegistration(cprToSave, userName);
		queueItem.setRegistration(cprToSave);

	}

	/**
	 * @param queueItem
	 * @param userName
	 * @param forceParticipant
	 * @throws RuntimeException
	 * @throws BizLogicException
	 */
	private void acceptParticipantRelatedItem(DataQueue queueItem,
			String userName, Participant forceParticipant)
			throws RuntimeException, BizLogicException {
		if (forceParticipant != null) {
			queueItem.setParticipant(forceParticipant);
		}
		Participant participant = null;
		if (queueItem.getParticipant() == null) {
			participant = new Participant();
			convertParticipant(queueItem, null, participant);
		} else {
			participant = queueItem.getParticipant();
			try {
				participant = participantBizLogic
						.getParticipantById(participant.getId());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			queueItem.setParticipant(participant);
			convertParticipant(queueItem, null, participant);
		}
		saveOrUpdateParticipant(userName, participant);
		if (queueItem.getParticipant() == null) {
			queueItem.setParticipant(participant);
		}
	}

	/**
	 * @param userName
	 * @param participant
	 * @throws BizLogicException
	 */
	private void saveOrUpdateParticipant(String userName,
			Participant participant) throws BizLogicException {
		// for reasons which i don't well understand,
		// edu.wustl.catissuecore.util.ApiSearchUtil.setParticipantMedicalIdentifierDefault(ParticipantMedicalIdentifier)
		// is adding an empty Site, and that causes Hibernate
		// transient object exception. For this reason, I'm removing all
		// nullified PMI
		// records from the collection here.
		CollectionUtils.filter(
				participant.getParticipantMedicalIdentifierCollection(),
				new Predicate() {
					@Override
					public boolean evaluate(Object arg0) {
						ParticipantMedicalIdentifier pmi = (ParticipantMedicalIdentifier) arg0;
						return !(pmi.getSite() == null && pmi
								.getMedicalRecordNumber() == null);
					}
				});

		participantBizLogic.saveOrUpdateParticipant(participant, userName);
	}

	/**
	 * @return the userBizLogic
	 */
	public final IUserBizLogic getUserBizLogic() {
		return userBizLogic;
	}

	/**
	 * @param userBizLogic
	 *            the userBizLogic to set
	 */
	public final void setUserBizLogic(IUserBizLogic userBizLogic) {
		this.userBizLogic = userBizLogic;
	}

	@Override
	public synchronized void autoProcessDataQueue() throws BizLogicException {
		User user = userBizLogic.getAdminUser();
		if (user != null) {
			logger.info("Auto-processing launched on behalf of: "
					+ user.getEmailAddress());
			// be careful with infinite loops!
			int safetyBarrier = 0;
			while (getPendingDataQueueItemsCount() > 0 && safetyBarrier < 10000) {
				safetyBarrier++;
				DataQueue queueItem = pickDataQueueItemForProcessing();
				if (isEligibleForAutoProcessing(queueItem)) {
					logger.info("Automatically accepting a data queue item: "
							+ queueItem);
					acceptQueueItem(queueItem, user.getLoginName());
				} else {
					logger.info("Unable to auto-process data queue item: "
							+ queueItem);
					break;
				}
			}
		} else {
			logger.error("Unable to auto-process data queue, because admin user is not found.");
		}
	}

	/**
	 * @return the cprBizLogic
	 */
	public ICollectionProtocolRegistrationBizLogic getCprBizLogic() {
		return cprBizLogic;
	}

	/**
	 * @param cprBizLogic
	 *            the cprBizLogic to set
	 */
	public void setCprBizLogic(
			ICollectionProtocolRegistrationBizLogic cprBizLogic) {
		this.cprBizLogic = cprBizLogic;
	}

	@Override
	public boolean isAuthorized(SessionDataBean sessionDataBean,
			DataQueue queueItem) throws BizLogicException {
		if (queueItem != null) {
			if (queueItem.isParticipantRelated()) {
				Participant p = convertParticipant(queueItem, null);
				if (p.getId() != null && p.getId() > 0) {
					return participantBizLogic.hasPrivilegeToView(
							Participant.class.getName(), p.getId(),
							sessionDataBean);
				}
			} else if (queueItem.isRegistrationRelated()) {
				CollectionProtocolRegistration cpr = convertRegistration(
						queueItem, null);
				CollectionProtocol cp = cpr.getCollectionProtocol();
				Participant p = cpr.getParticipant();
				if (p != null
						&& p.getId() != null
						&& p.getId() > 0
						&& !participantBizLogic.hasPrivilegeToView(
								Participant.class.getName(), p.getId(),
								sessionDataBean)) {
					return false;
				}
				if (cp != null
						&& cp.getId() != null
						&& cp.getId() > 0
						&& (!cpBizLogic.hasPrivilegeToView(
								CollectionProtocol.class.getName(), cp.getId(),
								sessionDataBean) || !cpBizLogic.isAuthorized(
								sessionDataBean, cp, "REGISTRATION"))) {
					return false;
				}

			}
		}
		// if this participant does not exist in caTissue locally, we can't
		// really check permissions on him/her
		// so we will return true if the user has at least Registration
		// permission on any protocol.
		return userBizLogic.hasRegistrationPermission(sessionDataBean);
	}

	/**
	 * @return the cpBizLogic
	 */
	public ICollectionProtocolBizLogic getCpBizLogic() {
		return cpBizLogic;
	}

	/**
	 * @param cpBizLogic
	 *            the cpBizLogic to set
	 */
	public void setCpBizLogic(ICollectionProtocolBizLogic cpBizLogic) {
		this.cpBizLogic = cpBizLogic;
	}

	@Override
	public boolean isAuthorized(SessionDataBean sessionDataBean)
			throws BizLogicException {
		return isAuthorized(sessionDataBean, pickDataQueueItemForProcessing());
	}

	private EnrollSubjectNewRequest prepareEnrollSubjectNewRequest(
			CollectionProtocolRegistration cpr) {
		CollectionProtocol cp;
		try {
			cp = cpBizLogic.retrieveB(CollectionProtocol.class.getName(), cpr
					.getCollectionProtocol().getId());
		} catch (BizLogicException e) {
			throw new RuntimeException(e);
		}
		cpr.setCollectionProtocol(cp);

		EnrollSubjectNewRequest request = new EnrollSubjectNewRequest();
		request.setSiteIdentifier(registrationConverter
				.convertToOrganizationIdentifier(cp.getIrbSite()));
		request.setStudyIdentifier(registrationConverter
				.convertToDocumentIdentifier(cp));
		request.setStudySubject(registrationConverter.convert(cpr).getValue());
		request.setSubjectIdentifier(participantConverter.convertPMI(getPMI(cpr
				.getParticipant())));
		return request;
	}

	private ParticipantMedicalIdentifier getPMI(Participant p) {
		if (p != null
				&& CollectionUtils.isNotEmpty(p
						.getParticipantMedicalIdentifierCollection())) {
			return p.getParticipantMedicalIdentifierCollection().iterator()
					.next();
		}
		return null;
	}

	/**
	 * @return the registrationConverter
	 */
	public IRegistrationConverter getRegistrationConverter() {
		return registrationConverter;
	}

	/**
	 * @param registrationConverter
	 *            the registrationConverter to set
	 */
	public void setRegistrationConverter(
			IRegistrationConverter registrationConverter) {
		this.registrationConverter = registrationConverter;
	}

	/**
	 * @return the participantConverter
	 */
	public IParticipantConverter getParticipantConverter() {
		return participantConverter;
	}

	/**
	 * @param participantConverter
	 *            the participantConverter to set
	 */
	public void setParticipantConverter(
			IParticipantConverter participantConverter) {
		this.participantConverter = participantConverter;
	}

	@Override
	public void enrollStudySubject(CollectionProtocolRegistration cpr) {
		if (cctsEnabled)
			try {
				EnrollSubjectNewRequest request = prepareEnrollSubjectNewRequest(cpr);
				SubjectRegistration service = getSubjectRegistration();
				EnrollSubjectNewResponse response = service
						.enrollStudySubject(request);
				MessagesHolder
						.addInformationalMessage(getMessageSource()
								.getMessage(
										"RegistrationService.enrollStudySubject.info.success",
										null, null));
			} catch (BeanCreationException e) {
				MessagesHolder
						.addErrorMessage(getMessageSource()
								.getMessage(
										"RegistrationService.enrollStudySubject.error.connectivity",
										null, null));
				logger.error(e.getMessage(), e);
				throw new ProcessingException(e.getMessage(), e);
			} catch (Exception e) {
				if (ExceptionUtils.getRootCause(e) instanceof ConnectException
						|| ExceptionUtils.getRootCause(e) instanceof SocketTimeoutException)
					MessagesHolder
							.addErrorMessage(getMessageSource()
									.getMessage(
											"RegistrationService.enrollStudySubject.error.connectivity",
											null, null));
				else
					MessagesHolder
							.addErrorMessage(getMessageSource()
									.getMessage(
											"RegistrationService.enrollStudySubject.error.generic",
											new Object[] { e.getMessage() },
											null));
				logger.error(e.getMessage(), e);
				throw new ProcessingException(e.getMessage(), e);
			}
	}

	/**
	 * @return
	 */
	private SubjectRegistration getSubjectRegistration() {
		return (SubjectRegistration) applicationContext
				.getBean(serviceClientBeanId);
	}

	/**
	 * @param applicationContext
	 *            the applicationContext to set
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * @return the serviceClientBeanId
	 */
	public String getServiceClientBeanId() {
		return serviceClientBeanId;
	}

	/**
	 * @param serviceClientBeanId
	 *            the serviceClientBeanId to set
	 */
	public void setServiceClientBeanId(String serviceClientBeanId) {
		this.serviceClientBeanId = serviceClientBeanId;
	}

	/**
	 * @return the cctsEnabled
	 */
	public boolean isCctsEnabled() {
		return cctsEnabled;
	}

	/**
	 * @param cctsEnabled
	 *            the cctsEnabled to set
	 */
	public void setCctsEnabled(boolean cctsEnabled) {
		this.cctsEnabled = cctsEnabled;
	}

	@Override
	public void sendToRegistrationService(CollectionProtocolRegistration cpr,
			Object[] oldState, Object[] newState) {
		if (!cctsEnabled)
			return;
		// Make sure the registration is complete before sending it out.
		// Also, check that grid id is null, which indicates the registration
		// did not come from C3PR itself.
		// Furthermore, skip this registration if its state hasn't really
		// changed
		if (cpr.getCollectionProtocol() != null && cpr.getParticipant() != null
				&& cpr.getActivityStatus() != null && cpr.getGridId() == null
				&& !Arrays.equals(oldState, newState)) {
			Utils.restoreDefaultHttpsURLHandler();
			try {
				if (edu.wustl.common.util.global.Status.ACTIVITY_STATUS_ACTIVE
						.toString().equalsIgnoreCase(cpr.getActivityStatus())) {
					// Active registration. Invoke "enrollStudySubject".
					enrollStudySubject(cpr);
				} else {
					// Take the subject off the study.
					takeSubjectOffStudy(cpr);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private void takeSubjectOffStudy(CollectionProtocolRegistration cpr) {
		if (cctsEnabled)
			try {
				TakeSubjectOffStudyRequest request = prepareTakeSubjectOffStudyRequest(cpr);
				SubjectRegistration service = getSubjectRegistration();
				TakeSubjectOffStudyResponse response = service
						.takeSubjectOffStudy(request);
				MessagesHolder
						.addInformationalMessage(getMessageSource()
								.getMessage(
										"RegistrationService.takeSubjectOffStudy.info.success",
										null, null));
			} catch (BeanCreationException e) {
				MessagesHolder
						.addErrorMessage(getMessageSource()
								.getMessage(
										"RegistrationService.enrollStudySubject.error.connectivity",
										null, null));
				logger.error(e.getMessage(), e);
				throw new ProcessingException(e.getMessage(), e);
			} catch (Exception e) {
				if (ExceptionUtils.getRootCause(e) instanceof ConnectException
						|| ExceptionUtils.getRootCause(e) instanceof SocketTimeoutException)
					MessagesHolder
							.addErrorMessage(getMessageSource()
									.getMessage(
											"RegistrationService.enrollStudySubject.error.connectivity",
											null, null));
				else
					MessagesHolder
							.addErrorMessage(getMessageSource()
									.getMessage(
											"RegistrationService.enrollStudySubject.error.generic",
											new Object[] { e.getMessage() },
											null));
				logger.error(e.getMessage(), e);
				throw new ProcessingException(e.getMessage(), e);
			}

	}

	private TakeSubjectOffStudyRequest prepareTakeSubjectOffStudyRequest(
			CollectionProtocolRegistration cpr) {
		TakeSubjectOffStudyRequest request = new TakeSubjectOffStudyRequest();
		request.setOffStudyDate(h.TSDateTime(new Date()));
		request.setStudySubjectIdentifier(registrationConverter
				.convertToSubjectIdentifier(
						cpr.getProtocolParticipantIdentifier(),
						cpr.getCollectionProtocol()));
		return request;
	}

	/**
	 * @return the messageSource
	 */
	public MessageSource getMessageSource() {
		return messageSource;
	}

	/**
	 * @param messageSource
	 *            the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
