package edu.wustl.catissuecore.ctrp;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URL;
import java.net.URLClassLoader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.coppa.po.Organization;
import gov.nih.nci.coppa.po.Person;
import gov.nih.nci.coppa.services.pa.StudyProtocol;

public class COPPAServiceClientTestCase {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private transient final Logger logger = Logger
			.getCommonLogger(COPPAServiceClientTestCase.class);

	@Test
	public void testOrganizationSearch() throws Exception {
		try {
			COPPAServiceClient client = new COPPAServiceClient();
			String keyword = "Hospital";
			Organization[] results = client.searchOrganization(keyword);
			if (results != null && results.length > 0) {
				assertTrue(true);
			} else {
				logger.error("No search results for Organization. Keyword:"
						+ keyword);
				fail();
			}

			for (Organization o : results) {
				logger.debug("organization:"
						+ o.getName().getPart().get(0).getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testPersonSearch() throws Exception {
		try {
			COPPAServiceClient client = new COPPAServiceClient();
			String firstName = "Sirisha";
			String lastName = "Kilambi";
			User user = new User();
			user.setFirstName(firstName);
			user.setLastName(lastName);

			Person[] results = client.searchPerson(user);
			if (results != null && results.length > 0) {
				assertTrue(true);
			} else {
				logger.error("No search results for Organization. Keyword:"
						+ user.getFirstName() + " " + user.getLastName());
				fail();
			}

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testStudyProtocolSearch() throws Exception {
		try {
			COPPAServiceClient client = new COPPAServiceClient();
			CollectionProtocol cp = new CollectionProtocol();
			String title = "ctgov is set to false 2nd via grid 3.3 test1";
			cp.setTitle(title);
			StudyProtocol[] results = client.searchSutdyProtocol(cp);
			if (results != null && results.length > 0) {
				assertTrue(true);
			} else {
				logger.error("No search results for Study Protocol. Keyword:"
						+ cp.getTitle());
				fail();
			}

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testGetSutdyProtocolById() throws Exception {
		try {
			COPPAServiceClient client = new COPPAServiceClient();
			String coppaProtocolId = "135568";
			StudyProtocol result = client.getSutdyProtocolById(coppaProtocolId);
			if (result != null) {
				assertTrue(true);
			} else {
				logger.error("No study protocol found for id:"
						+ coppaProtocolId);
				fail();
			}

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testGetPrincipalInvestigatorByStudyProtocolId() throws Exception {
		try {
			COPPAServiceClient client = new COPPAServiceClient();
			String coppaProtocolId = "135568";
			Person result = client.getPrincipalInvestigatorByStudyProtocolId(coppaProtocolId);
			if (result != null) {
				assertTrue(true);
			} else {
				logger.error("No study protocol found for id:"
						+ coppaProtocolId);
				fail();
			}

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}}
