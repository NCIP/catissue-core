import edu.wustl.catissuecore.domain.util.PropertiesLoader;
import junit.framework.TestCase;

/**
 * @author Ion C. Olaru
 *         Date: 8/29/11 - 2:48 PM
 */
public class PropertiesLoaderTest extends TestCase {
    public void testSuperUser() {
        assertEquals("admin@admin.com", PropertiesLoader.getCaTissueSuperUserUsername());
        assertEquals("Aa_111111", PropertiesLoader.getCaTissueSuperUserPassword());
    }
}
