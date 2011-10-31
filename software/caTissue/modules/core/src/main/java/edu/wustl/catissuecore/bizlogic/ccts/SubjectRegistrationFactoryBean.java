/**
 * 
 */
package edu.wustl.catissuecore.bizlogic.ccts;

import java.lang.reflect.Field;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.spi.ServiceDelegate;

import org.springframework.util.ReflectionUtils;

import edu.duke.cabig.c3pr.webservice.subjectregistration.SubjectRegistration;
import edu.duke.cabig.c3pr.webservice.subjectregistration.SubjectRegistrationService;

/**
 * @author Denis G. Krylov
 * 
 */
public final class SubjectRegistrationFactoryBean extends
		ServiceClientFactoryBean {

	@Override
	public Object getObject() throws Exception {
		final QName qname = new QName(
				"http://enterpriseservices.nci.nih.gov/SubjectRegistrationService",
				"SubjectRegistrationService");
		SubjectRegistrationService service = new SubjectRegistrationService(
				new URL(getWsdlLocation()),
				qname);		
		ServiceDelegate delegate = getProvider().createServiceDelegate(new URL(getWsdlLocation()),
				qname,
				SubjectRegistrationService.class);
		
		final Field field = ReflectionUtils.findField(SubjectRegistrationService.class, "delegate");
		ReflectionUtils.makeAccessible(field);
		ReflectionUtils.setField(field, service, delegate);		
		
		service.setHandlerResolver(createHandlerResolver());
		SubjectRegistration port = service.getSubjectRegistration();
		return preparePort((BindingProvider) port);
	}

	@Override
	public Class getObjectType() {
		return SubjectRegistration.class;
	}

}
