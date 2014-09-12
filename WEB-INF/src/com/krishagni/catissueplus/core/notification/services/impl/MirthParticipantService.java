
package com.krishagni.catissueplus.core.notification.services.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.ApplicationContext;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.notification.events.NotificationResponse;
import com.krishagni.catissueplus.core.notification.events.NotificationResponse.Status;
import com.krishagni.catissueplus.core.notification.events.SubjectDto;
import com.krishagni.catissueplus.core.notification.services.CrudService;

import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.velocity.VelocityManager;

public class MirthParticipantService implements CrudService {

	private static final transient Logger logger = Logger.getCommonLogger(MirthParticipantService.class);

	private static CrudService mirthParticipantSvc = null;

	private static String SUBJECT_TEMPLATE = "SubjectTemplate.vm";

	public static CrudService getInstance() {
		if (mirthParticipantSvc == null) {
			mirthParticipantSvc = new MirthParticipantService();
		}
		return mirthParticipantSvc;
	}

	@Override
	public NotificationResponse insert(Object domainObj) {
		SubjectDto subject = populateSubjectDto(domainObj);
		subject.setOperation("Add");
		return sendSubject(subject);
	}

	@Override
	public NotificationResponse update(Object domainObj) {
		SubjectDto subject = populateSubjectDto(domainObj);
		subject.setOperation("Update");
		return sendSubject(subject);
	}

	private SubjectDto populateSubjectDto(Object domainObj) {
		Participant participant = (Participant) domainObj;
		SubjectDto subject = new SubjectDto();
		subject.setGender(participant.getGender());
		Date dob = participant.getBirthDate();
		subject.setBirthDate(dob);

		Map<String, CollectionProtocolRegistration> cprCollection = participant.getCprCollection();
		for (Entry<String, CollectionProtocolRegistration> entry : cprCollection.entrySet()) {

			CollectionProtocolRegistration cpr = entry.getValue();
			Date regDate = cpr.getRegistrationDate();
			subject.setEnrollmentDate(regDate);
			subject.setLabel(cpr.getProtocolParticipantIdentifier().toString());
			Long cpId = cpr.getCollectionProtocol().getId();
			ApplicationContext appCtx = OpenSpecimenAppCtxProvider.getAppCtx();
			MirthApplicationService mirthAppSvc = (MirthApplicationService) appCtx.getBean("mirthAppService");

			String studyId = mirthAppSvc.getMappedStudyId(cpId, "Mirth");
			subject.setStudyId(studyId);
		}
		return subject;
	}

	public NotificationResponse sendSubject(SubjectDto subject) {
		URL url;
		HttpURLConnection connection = null;
		NotificationResponse notifResponse = new NotificationResponse();
		try {
			// Create connection
			String mirthUrl = XMLPropertyHandler.getValue("MirthUrl");
			url = new URL(mirthUrl);

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/xml");
			List<SubjectDto> subjects = new ArrayList<SubjectDto>();
			subjects.add(subject);

			VelocityManager vm = VelocityManager.getInstance();
			String xml = vm.evaluate(subjects, SUBJECT_TEMPLATE);

			connection.setDoOutput(true);

			connection.setRequestProperty("Content-Length", "" + Integer.toString(xml.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(xml);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
			}
			rd.close();
			String status = response.toString();

			if (Status.SUCCESS.toString().equalsIgnoreCase(status)) {
				return NotificationResponse.success("Subject Created Suceessfully");
			}
			else {
				return NotificationResponse.fail("Error while inserting data into Mirth database");
			}

		}
		catch (IOException ioException) {
			return NotificationResponse.fail("Can not connect mirth application");
		}
		catch (Exception e) {
			logger.error("Error while sending http request to mirth of Participant with PPId " + subject.getLabel());
			return NotificationResponse.fail(e.getMessage());
		}
		finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

	}

	@Override
	public NotificationResponse delete(Object domainObj) {
		// TODO Auto-generated method stub
		return null;
	}
}
