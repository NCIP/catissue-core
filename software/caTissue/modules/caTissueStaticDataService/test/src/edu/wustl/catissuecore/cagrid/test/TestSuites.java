package edu.wustl.catissuecore.cagrid.test;
import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;

/**
 * @author Ion C. Olaru
 *         Date: 7/22/11 - 2:06 PM
 */
public class TestSuites {

    public static Test suite() {
            TestSuite suite = new TestSuite();
            suite.addTestSuite(ParticipantConversionTest.class);
            suite.addTestSuite(CollectionProtocolConversionTest.class);
            suite.addTestSuite(CollectionProtocolRegistrationConversionTest.class);
            suite.addTestSuite(WAPIUtilityTest.class);
            suite.addTestSuite(OrderDetailsConversionTest.class);
            suite.addTestSuite(SpecimenOrderItemTest.class);
            suite.addTestSuite(ChemotherapyTest.class);
            return suite;
        }

        /**
         * Runs the test suite using the textual runner.
         */
        public static void main(String[] args) {
            junit.textui.TestRunner.run(suite());
        }

}
