package edu.wustl.catissuecore.cacore;

import edu.wustl.catissuecore.client.CaCoreAppServicesDelegator;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Race;
import edu.wustl.catissuecore.domain.Site;
import gov.nih.nci.system.dao.DAOException;
import gov.nih.nci.system.dao.Request;
import gov.nih.nci.system.dao.Response;
import gov.nih.nci.system.dao.orm.ORMDAOImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

public class CatissueCoreDAOImpl extends ORMDAOImpl {

	private static final String BEANS_JAR_REGEXP = ".*beans.jar";

	private static final Logger LOGGER = Logger.getLogger(CatissueCoreDAOImpl.class.getName());

	private final CaCoreAppServicesDelegator appService = new CaCoreAppServicesDelegator();

	protected CaCoreAppServicesDelegator getApplicationServiceDelegator() {
		return appService;
	}

	/**
	 * Method which provides all the class names on which client can query.
	 *
	 * @return classNames list of supported class names. (non-Javadoc)
	 * @see gov.nih.nci.system.dao.DAO#getAllClassNames()
	 */
	public List<String> getAllClassNames() {
		List<String> classNames = new ArrayList<String>();
		try {
			classNames = getClassNames();
		} catch (Exception e) {
			LOGGER.error("Error occured while retrieving class names:\n"
					+ e.getMessage(), e);
		}
		return classNames;
	}

	public Response query(Request request) throws DAOException {
		String userName = CaTissueCoreAuthenticationUtil
				.retreiveUsernameFromSecurityContext();
		Response response = super.query(request);
		try {
			List<Object> filteredResult = appService.searchFilter(userName,
					(List<?>) response.getResponse());

			response.setResponse(filteredResult);
			response.setRowCount(filteredResult.size());
		} catch (Exception e) {
			throw new DAOException("Error occured while filtering the result.",
					e);
		}
		return response;
	}

	/**
	 * Method to get supported class names.
	 *
	 * @return classNames List of class names
	 * @throws Exception
	 *             generic Exception
	 */
	private static List<String> getClassNames() throws Exception {
		JarFile beanJarFile = null;
		// Get the URLs
		java.net.URL[] urls = ((java.net.URLClassLoader) Thread.currentThread()
				.getContextClassLoader()).getURLs();

		int count = 0;
		// search for beans jar
		for (URL url : urls) {
			if (url.getFile().matches(BEANS_JAR_REGEXP)) {
				beanJarFile = new JarFile(url.getFile());
				count++;
			}
		}

		if (beanJarFile == null) {
			LOGGER
					.warn(BEANS_JAR_REGEXP
							+ " could not be located conventionally via the context ClassLoader. Perhaps, we're deployed in exploded format. Trying to scan the /WEB-INF/lib folder...");
			try {
				for (URL url : urls) {
					final File file = new File(url.getPath());
					if (file.exists() && file.isDirectory()
							&& file.getName().equals("classes")) {
						File lib = new File(file.getParentFile(), "lib");
						if (lib.exists() && lib.isDirectory()) {
							File[] jars = lib.listFiles();
							for (File jar : jars) {
								if (jar.getName().matches(BEANS_JAR_REGEXP)) {
									LOGGER.warn("Found potential match: "
											+ jar.getAbsolutePath());
									beanJarFile = new JarFile(jar);
									count++;
								}
							}
						}
					}
				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}

		if (beanJarFile == null) {
			throw new FileNotFoundException("Could not locate the bean jar");
		}
		if (count > 1) {
			throw new FileNotFoundException("Found more than one bean jar");
		}

		// get all classes from beans jar
		List<String> classNames = new ArrayList<String>();
		Enumeration<JarEntry> jarFile = beanJarFile.entries();
		while (jarFile.hasMoreElements()) {
			JarEntry jarEntry = jarFile.nextElement();
			if (!jarEntry.isDirectory()) {
				String name = jarEntry.getName();
				if (name.endsWith(".class")) {
					String klassName = name.replace('/', '.').substring(0,
							name.lastIndexOf('.'));
					if (!Class.forName(klassName).isInterface()) {
						classNames.add(klassName);
					}
				}
			}
		}

		classNames.add(AbstractSpecimen.class.getName());
		classNames.add(CollectionProtocol.class.getName());
		classNames.add(CollectionProtocolRegistration.class.getName());
		classNames.add(ParticipantMedicalIdentifier.class.getName());
		classNames.add(Participant.class.getName());
		classNames.add(Race.class.getName());
		classNames.add(Site.class.getName());

		return classNames;
	}

}
