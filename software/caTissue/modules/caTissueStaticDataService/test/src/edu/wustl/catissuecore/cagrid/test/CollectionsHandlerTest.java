package edu.wustl.catissuecore.cagrid.test;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.StudyFormContext;
import edu.wustl.catissuecore.domain.util.CollectionsHandler;
import junit.framework.TestCase;
import org.hibernate.LazyInitializationException;

import java.util.*;

/**
 * @author Ion C. Olaru
 * Date: 1/18/12
 */
public class CollectionsHandlerTest extends TestCase {
    List collectionInstance;
    CollectionsHandler ch;
    byte collectionSize = 3;
    Set objectCache = new HashSet();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        collectionInstance = new ArrayList();

        for (byte i=0; i<collectionSize; i++) {
            CollectionProtocol cp = new edu.wustl.catissuecore.domain.mock.CollectionProtocol();
            cp.setGridGrouperPrivileges(null);
            cp.setStudyFormContextCollection(null);
            cp.setId(90L + i);
            cp.setActivityStatus("Active");
            cp.setSiteCollection(new HashSet<Site>());

            Site site = new edu.wustl.catissuecore.domain.mock.Site();
            site.setAssignedSiteUserCollection(null);
            site.setId(100L);
            cp.getSiteCollection().add(site);

            collectionInstance.add(cp);
        }
    }

    public void testOneObject() {
        edu.wustl.catissuecore.domain.mock.CollectionProtocol cp = (edu.wustl.catissuecore.domain.mock.CollectionProtocol)collectionInstance.get(0);
        CollectionsHandler.handleObject(cp, objectCache);
        assertNotNull(cp.getStudyFormContextCollection());
        assertNotNull(cp.getGridGrouperPrivileges());
        assertEquals(0, cp.getStudyFormContextCollection().size());
        assertEquals(0, cp.getGridGrouperPrivileges().size());
    }

    public void testOneObjectWithNestedCollections() {
        edu.wustl.catissuecore.domain.mock.CollectionProtocol cp = (edu.wustl.catissuecore.domain.mock.CollectionProtocol)collectionInstance.get(0);
        CollectionsHandler.handleObject(cp, objectCache);
        assertNotNull(cp.getStudyFormContextCollection());
        assertNotNull(cp.getGridGrouperPrivileges());
        assertEquals(0, cp.getStudyFormContextCollection().size());
        assertEquals(0, cp.getGridGrouperPrivileges().size());

        assertNotNull(cp.getSiteCollection());
        assertTrue(cp.getSiteCollection().size() == 1);

        Site site = (Site)cp.getSiteCollection().toArray()[0];

        assertNotNull(site);
        assertNotNull(site.getAssignedSiteUserCollection());
        assertEquals(0, site.getAssignedSiteUserCollection().size());
    }

    public void testOneObjectWithCircularReferences() {
        edu.wustl.catissuecore.domain.mock.CollectionProtocol cp = (edu.wustl.catissuecore.domain.mock.CollectionProtocol)collectionInstance.get(0);
        cp.setChildCollectionProtocolCollection(new HashSet<CollectionProtocol>(1));
        cp.getChildCollectionProtocolCollection().add(cp);
        CollectionsHandler.handleObject(cp, objectCache);
    }
}
