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

import edu.duke.cabig.c3pr.webservice.subjectmanagement.SubjectManagement;
import edu.duke.cabig.c3pr.webservice.subjectmanagement.SubjectManagementService;

/**
 * @author Denis G. Krylov
 * 
 */
public final class SubjectManagementFactoryBean extends
		ServiceClientFactoryBean {

	@Override
	public Object getObject() throws Exception {
		final QName qname = new QName(
				"http://enterpriseservices.nci.nih.gov/SubjectManagementService",
				"SubjectManagementService");
		SubjectManagementService service = new SubjectManagementService(
				new URL(getWsdlLocation()),
				qname);		
		ServiceDelegate delegate = getProvider().createServiceDelegate(new URL(getWsdlLocation()),
				qname,
                SubjectManagementService.class);
		
		final Field field = ReflectionUtils.findField(SubjectManagementService.class, "delegate");
		ReflectionUtils.makeAccessible(field);
		ReflectionUtils.setField(field, service, delegate);
		
		service.setHandlerResolver(createHandlerResolver());
		SubjectManagement port = service.getSubjectManagement();
		return preparePort((BindingProvider) port);
	}

	@Override
	public Class getObjectType() {
		return SubjectManagement.class;
	}

}
