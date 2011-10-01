import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;

/**
 * @author Ion C. Olaru
 * Date: 7/14/11
 * Time: 5:35 PM
 */
public abstract class BaseConversionTest extends TestCase {
    protected Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();
    private static Logger log = Logger.getLogger(BaseConversionTest.class);

    public abstract void testWsToDomain();
    public abstract void testDomainToWs();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        log.debug("SETUP:");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        log.debug("DONE.");
    }
}
