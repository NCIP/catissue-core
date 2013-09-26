/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.cagrid.test;
import edu.yale.med.krauthammerlab.catissuecore.cagrid.test.CreateOrder;
import edu.yale.med.krauthammerlab.catissuecore.cagrid.test.DeserializationTest;
import edu.yale.med.krauthammerlab.catissuecore.cagrid.test.PopulatedAssociation;
import edu.yale.med.krauthammerlab.catissuecore.cagrid.test.RetrieveParticipant;
import edu.yale.med.krauthammerlab.catissuecore.cagrid.test.RetrieveSpecimen;
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
            //suite.addTestSuite(ParticipantConversionTest.class);
            //suite.addTestSuite(CollectionProtocolConversionTest.class);
            //suite.addTestSuite(CollectionProtocolRegistrationConversionTest.class);
            //suite.addTestSuite(WAPIUtilityTest.class);
            //suite.addTestSuite(OrderDetailsConversionTest.class);
            //suite.addTestSuite(SpecimenOrderItemTest.class);
            //suite.addTestSuite(ChemotherapyTest.class);

            //suite.addTestSuite(RetrieveParticipant.class);
            //suite.addTestSuite(RetrieveSpecimen.class);
            //suite.addTestSuite(DeserializationTest.class);
            suite.addTestSuite(CreateOrder.class);
            //suite.addTestSuite(PopulatedAssociation.class);
            return suite;
        }

        /**
         * Runs the test suite using the textual runner.
         */
        public static void main(String[] args) {
            junit.textui.TestRunner.run(suite());
        }

}
