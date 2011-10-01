/**
 * 
 */
package edu.wustl.catissuecore.bizlogic.ccts;

import java.io.StringWriter;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import edu.duke.cabig.c3pr.webservice.common.AdvanceSearchCriterionParameter;
import edu.duke.cabig.c3pr.webservice.common.DSETAdvanceSearchCriterionParameter;
import edu.duke.cabig.c3pr.webservice.iso21090.CD;
import edu.duke.cabig.c3pr.webservice.iso21090.DSETST;
import edu.duke.cabig.c3pr.webservice.iso21090.ST;
import edu.duke.cabig.c3pr.webservice.subjectregistration.QuerySubjectRegistrationRequest;
import edu.duke.cabig.c3pr.webservice.subjectregistration.QuerySubjectRegistrationResponse;
import edu.duke.cabig.c3pr.webservice.subjectregistration.StudySubject;
import edu.duke.cabig.c3pr.webservice.subjectregistration.SubjectRegistration;
import edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic;
import edu.wustl.catissuecore.bizlogic.IParticipantBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ccts.DataQueue;
import edu.wustl.catissuecore.domain.ccts.EventType;
import edu.wustl.catissuecore.domain.ccts.Notification;
import edu.wustl.catissuecore.domain.ccts.ProcessingStatus;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;

/**
 * Fetches a collection protocol registration information from the Subject
 * Registration service, processes it, and stores in the database, if need be.
 * 
 * @author Denis G. Krylov
 * 
 */
public final class RegistrationEventProcessor implements ICctsEventProcessor,
		ApplicationContextAware {

	private static final Logger logger = Logger
			.getCommonLogger(RegistrationEventProcessor.class);

	private String serviceClientBeanId;

	private IParticipantBizLogic participantBizLogic;

	private ICctsIntegrationBizLogic integrationBizLogic;

	private ApplicationContext applicationContext;

	private ICollectionProtocolRegistrationBizLogic cprBizLogic;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.wustl.catissuecore.bizlogic.ccts.ICctsEventProcessor#process(java
	 * .lang.String)
	 */
	@Override
	public String process(String gridId, Notification notification)
			throws ProcessingException {
		StudySubject reg = findRegistrationByGridId(gridId);
		final String payload = convertToXML(reg);
		if (reg != null && StringUtils.isNotEmpty(payload)) {
			addToDataQueue(payload, notification, gridId);
		} else {
			logger.error("Attempt to retrieve a StudySubject from C3PR using Grid ID of "
					+ gridId
					+ " went through fine, but did not return any data. Therefore, an entry in the data queue will not be created.");
		}
		return payload;
	}

	private void addToDataQueue(String payload, Notification notification,
			String gridId) {
		// see if a participant with the given grid id already exists.
		try {
			CollectionProtocolRegistration cpr = cprBizLogic
					.getRegistrationByGridId(gridId);
			DataQueue dataQueue = new DataQueue();
			dataQueue.setPayload(StringUtils.left(payload, 65535));
			dataQueue.setDateTime(new Date());
			dataQueue.setNotification(notification);
			dataQueue.setProcessingStatus(ProcessingStatus.PENDING);
			dataQueue.setRegistration(cpr);
			dataQueue.setIncoming(true);
			integrationBizLogic.addDataQueueItemWithAutoProcess(dataQueue);
		} catch (BizLogicException e) {
			logger.error(e.getMessage(), e);
			throw new ProcessingException(e);
		}

	}

	private String convertToXML(StudySubject reg) {
		try {
			JAXBContext jc = JAXBContext.newInstance(StudySubject.class,
					CD.class);
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			JAXBElement<StudySubject> jaxbElement = new JAXBElement<StudySubject>(
					new QName(Constants.NES_SUBJ_REG_NS, "studySubject"),
					StudySubject.class, reg);
			final StringWriter sw = new StringWriter();
			m.marshal(jaxbElement, sw);
			return sw.toString();
		} catch (Exception e) {
			logger.error(e.toString(), e);
			return StringUtils.EMPTY;
		}
	}

	/**
	 * @param gridId
	 * 
	 */
	private StudySubject findRegistrationByGridId(String gridId) {
		StudySubject reg = null;
		QuerySubjectRegistrationRequest advancedQuerySubjectRequest = new QuerySubjectRegistrationRequest();
		DSETAdvanceSearchCriterionParameter parameters = new DSETAdvanceSearchCriterionParameter();

		AdvanceSearchCriterionParameter param = new AdvanceSearchCriterionParameter();
		final ST attrName = new ST();
		attrName.setValue("gridId");
		param.setAttributeName(attrName);

		final ST objName = new ST();
		objName.setValue("edu.duke.cabig.c3pr.domain.StudySubject");
		param.setObjectName(objName);

		final CD predicate = new CD();
		predicate.setCode("=");
		param.setPredicate(predicate);

		final DSETST values = new DSETST();
		ST value = new ST();
		value.setValue(gridId);
		values.getItem().add(value);
		param.setValues(values);

		parameters.getItem().add(param);
		advancedQuerySubjectRequest.setParameters(parameters);

		try {
			QuerySubjectRegistrationResponse advancedQuerySubjectResponse = getSubjectRegistration()
					.querySubjectRegistration(advancedQuerySubjectRequest);
			if (advancedQuerySubjectResponse.getStudySubjects() != null
					&& CollectionUtils.isNotEmpty(advancedQuerySubjectResponse
							.getStudySubjects().getItem())) {
				reg = advancedQuerySubjectResponse.getStudySubjects().getItem()
						.get(0);
				logger.debug("Found study subject: "
						+ ToStringBuilder.reflectionToString(reg));
			}
		} catch (Exception e) {
			throw new ProcessingException(e);
		}
		return reg;
	}

	/**
	 * @return
	 */
	private SubjectRegistration getSubjectRegistration() {
		return (SubjectRegistration) applicationContext
				.getBean(serviceClientBeanId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.wustl.catissuecore.bizlogic.ccts.ICctsEventProcessor#supports(edu
	 * .wustl.catissuecore.domain.ccts.EventType)
	 */
	@Override
	public boolean supports(EventType eventType) {
		return eventType == EventType.SUBJECT_REGISTRATION;
	}

	/**
	 * @return the serviceClientBeanId
	 */
	public final String getServiceClientBeanId() {
		return serviceClientBeanId;
	}

	/**
	 * @param serviceClientBeanId
	 *            the serviceClientBeanId to set
	 */
	public final void setServiceClientBeanId(String serviceClientBeanId) {
		this.serviceClientBeanId = serviceClientBeanId;
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		this.applicationContext = ctx;
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

	/**
	 * @return the integrationBizLogic
	 */
	public final ICctsIntegrationBizLogic getIntegrationBizLogic() {
		return integrationBizLogic;
	}

	/**
	 * @param integrationBizLogic
	 *            the integrationBizLogic to set
	 */
	public final void setIntegrationBizLogic(
			ICctsIntegrationBizLogic integrationBizLogic) {
		this.integrationBizLogic = integrationBizLogic;
	}

	/**
	 * @return the cprBizLogic
	 */
	public final ICollectionProtocolRegistrationBizLogic getCprBizLogic() {
		return cprBizLogic;
	}

	/**
	 * @param cprBizLogic
	 *            the cprBizLogic to set
	 */
	public final void setCprBizLogic(
			ICollectionProtocolRegistrationBizLogic cprBizLogic) {
		this.cprBizLogic = cprBizLogic;
	}

}
