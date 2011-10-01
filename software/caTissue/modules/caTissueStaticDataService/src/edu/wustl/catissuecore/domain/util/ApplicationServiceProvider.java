package edu.wustl.catissuecore.domain.util;

import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.proxy.ApplicationServiceProxy;
import gov.nih.nci.system.security.acegi.providers.GroupNameAuthenticationToken;
import gov.nih.nci.system.security.acegi.providers.UsernameAuthenticationToken;
import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationManager;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.providers.x509.X509AuthenticationToken;
import org.aopalliance.aop.Advice;
import org.globus.gsi.GlobusCredential;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.InputStreamResource;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.Map;

/**
 * User: Ion C. Olaru
 * Date: 9/9/11- 9:22 PM
 */
public class ApplicationServiceProvider {

    private static ApplicationContext ctx = new ClassPathXmlApplicationContext("edu/wustl/catissuecore/domain/application-config-client.xml");

    private static String DEFAULT_SERVICE = "ServiceInfo";

    public static ApplicationService getApplicationService() throws Exception {
        return getApplicationService(DEFAULT_SERVICE, null, null, null);
    }

    public static ApplicationService getApplicationService(String username, String password) throws Exception {
        if (username == null || password == null || username.trim().length() == 0 || password.trim().length() == 0)
            throw new Exception("Cannot authenticate user, please pass username and password");
        return getApplicationService(DEFAULT_SERVICE, null, username, password);
    }

    public static ApplicationService getApplicationService(String service) throws Exception {
        return getApplicationService(service, null, null, null);
    }

    public static ApplicationService getApplicationService(String service, String username, String password) throws Exception {
        if (username == null || password == null || username.trim().length() == 0 || password.trim().length() == 0)
            throw new Exception("Cannot authenticate user");
        return getApplicationService(service, null, username, password);
    }

    public static ApplicationService getApplicationServiceFromUrl(String url) throws Exception {
        return getApplicationService(DEFAULT_SERVICE, url, null, null);
    }

    public static ApplicationService getApplicationServiceFromUrl(String url, String username, String password) throws Exception {
        if (username == null || password == null || username.trim().length() == 0 || password.trim().length() == 0)
            throw new Exception("Cannot authenticate user");
        return getApplicationService(DEFAULT_SERVICE, url, username, password);
    }

    public static ApplicationService getApplicationServiceFromUrl(String url, String service) throws Exception {
        return getApplicationService(service, url, null, null);
    }

    public static ApplicationService getApplicationServiceFromUrl(String url, String service, String username, String password) throws Exception {
        if (username == null || password == null || username.trim().length() == 0 || password.trim().length() == 0)
            throw new Exception("Cannot authenticate user");
        return getApplicationService(service, url, username, password);
    }

    @SuppressWarnings("unchecked")
    public static ApplicationService getApplicationService(Collection<String> groups) throws Exception {
        ApplicationService as = getApplicationServiceForGroups(DEFAULT_SERVICE, null, groups);
        return as;
    }

    @SuppressWarnings("unchecked")
    public static ApplicationService getApplicationService(String service, Collection<String> groups) throws Exception {
        ApplicationService as = getApplicationServiceForGroups(service, null, groups);
        return as;

    }

    @SuppressWarnings("unchecked")
    public static ApplicationService getApplicationServiceFromUrl(String service, String url, Collection<String> groups) throws Exception {
        ApplicationService as = getApplicationServiceForGroups(service, url, groups);
        return as;
    }

    public static ApplicationService getApplicationServiceForUser(String username) throws Exception {
        ApplicationService as = getApplicationServiceForUsername(DEFAULT_SERVICE, null, username);
        return as;
    }

    public static ApplicationService getApplicationServiceForUser(String service, String username) throws Exception {
        ApplicationService as = getApplicationServiceForUsername(service, null, username);
        return as;

    }

    public static ApplicationService getApplicationServiceForUserFromUrl(String service, String url, String username) throws Exception {
        ApplicationService as = getApplicationServiceForUsername(service, url, username);
        return as;
    }

    public static ApplicationService getApplicationService(GlobusCredential cred) throws Exception {
        return getApplicationService(DEFAULT_SERVICE, null, cred);
    }

    @SuppressWarnings("unchecked")
    private static ApplicationService getApplicationService(String service, String url, GlobusCredential cred) throws Exception {
        if (url != null && !url.startsWith("https://")) {
            throw new Exception("Url should be using secured protocol to use Globus Credentials");
        }
        if (cred == null) {
            throw new Exception("Credentials can not be null");
        }
        cred.verify();
        X509AuthenticationToken auth = new X509AuthenticationToken(cred.getIdentityCertificate());
        auth.setDetails(cred);
        return getApplicationService(service, url, auth);
    }

    @SuppressWarnings("unchecked")
    private static ApplicationService getApplicationServiceForGroups(String service, String url, Collection<String> groups) throws Exception {
        if (url != null) {
            throw new Exception("Url security feature for groups is not supported");
        }
        if (groups == null || groups.size() == 0) {
            throw new Exception("User Groups Collection cannot be empty");
        }

        Authentication auth = new GroupNameAuthenticationToken(groups);
        return getApplicationService(service, url, auth);
    }

    private static ApplicationService getApplicationServiceForUsername(String service, String url, String username) throws Exception {
        if (url != null) {
            throw new Exception("Url security feature for username is not supported");
        }
        if (username == null || username.trim().length() == 0) {
            throw new Exception("Username cannot be empty");
        }

        Authentication auth = new UsernameAuthenticationToken(username.trim());
        return getApplicationService(service, url, auth);
    }

    @SuppressWarnings("unchecked")
    public static ApplicationService getApplicationService(String service, String url, String username, String password) throws Exception {
        Boolean secured = username != null && password != null;

        Authentication auth = null;
        if (secured)
            auth = new UsernamePasswordAuthenticationToken(username, password);

        return getApplicationService(service, url, auth);
    }

    private static ApplicationService getApplicationService(String service, String url, Authentication auth) throws Exception {
        Boolean secured = auth != null;
        ApplicationContext context = getApplicationContext(service, url, secured);
        Map<String, Object> serviceInfoMap = (Map<String, Object>) context.getBean(service);
        ApplicationService as = (ApplicationService) serviceInfoMap.get("APPLICATION_SERVICE_BEAN");

        if (!secured) {
            setApplicationService(as, null);
        } else {
            AuthenticationManager am = (AuthenticationManager) serviceInfoMap.get("AUTHENTICATION_SERVICE_BEAN");

            try {
                auth = am.authenticate(auth);
                setApplicationService(as, auth);
            } catch (Exception e) {
                String message = "Error authenticating user:";
                throw new ApplicationException(message, e);
            }
        }
        return as;
    }


    private static void setApplicationService(ApplicationService as, Authentication auth) {
        if (as instanceof org.springframework.aop.framework.Advised) {
            org.springframework.aop.framework.Advised proxy = (org.springframework.aop.framework.Advised) as;
            for (Advisor advisor : proxy.getAdvisors()) {
                Advice advice = advisor.getAdvice();
                if (advice instanceof ApplicationServiceProxy) {
                    ApplicationServiceProxy asp = (ApplicationServiceProxy) advice;
                    asp.setApplicationService(as);
                    asp.setAuthentication(auth);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static ApplicationContext getApplicationContext(String service, String url, Boolean secured) throws Exception {
        if (service == null || service.trim().length() == 0)
            throw new Exception("Name of the service can not be empty");

        Map<String, Object> serviceInfoMap = (Map<String, Object>) ctx.getBean(service);

        if (serviceInfoMap == null)
            throw new Exception("Change the configuration file!!!");

        if (url == null) {
            validateContext(serviceInfoMap, ctx, secured);
            return ctx;
        } else {
            String serviceInfo = (String) serviceInfoMap.get("APPLICATION_SERVICE_CONFIG");
            ;

            //URL_KEY must be present if the user is trying to use the url to reach the service
            if (serviceInfo == null || serviceInfo.indexOf("URL_KEY") < 0)
                throw new Exception("Change the configuration file!!!");

            serviceInfo = serviceInfo.replace("URL_KEY", url);

            //Prepare in memory configuration from the information retrieved of the configuration file
            String xmlFileString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE beans PUBLIC \"-//SPRING//DTD BEAN//EN\" \"http://www.springframework.org/dtd/spring-beans.dtd\"><beans>" + serviceInfo + "</beans>";
            GenericApplicationContext context = new GenericApplicationContext();
            XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(context);
            xmlReader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE);
            InputStreamResource inputStreamResource = new InputStreamResource(new ByteArrayInputStream(xmlFileString.getBytes()));
            xmlReader.loadBeanDefinitions(inputStreamResource);
            context.refresh();

            Map<String, Object> newServiceInfoMap = (Map<String, Object>) ctx.getBean(service);

            validateContext(newServiceInfoMap, context, secured);

            return context;
        }
    }

    private static void validateContext(Map<String, Object> serviceInfoMap, ApplicationContext ctx, Boolean secured) throws Exception {
        ApplicationService as = null;
        AuthenticationManager am = null;

        as = (ApplicationService) serviceInfoMap.get("APPLICATION_SERVICE_BEAN");

        if (as == null)
            throw new Exception("Change the configuration file!!!");

        if (!secured)
            return;

        am = (AuthenticationManager) serviceInfoMap.get("AUTHENTICATION_SERVICE_BEAN");
        if (am == null)
            throw new Exception("Change the configuration file!!!");

    }
}