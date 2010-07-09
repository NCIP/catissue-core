///**
// *<p>Title: </p>
// *<p>Description:  </p>
// *<p>Copyright:TODO</p>
// *@author
// *@version 1.0
// */
//package edu.wustl.catissuecore.testcase.bizlogic;
//
//import edu.wustl.catissuecore.domain.Participant;
//import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
//import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
//
//
//
//public class ParticipantEmpiTestCase  extends CaTissueSuiteBaseTest
//{
//    String empiId="";
//    public void testCreateParticipantWithEMPI()
//    {
//        try
//        {
//            Participant participant = BaseTestCaseUtility
//                    .initParticipantWithCPR();
//            empiId=UniqueKeyGeneratorUtil.getUniqueKey();
//            participant.setEmpiId(empiId);
//            participant = (Participant) appService.createObject(participant);
//            System.out.println("Object created successfully:::"+participant.getLastName());
//            assertTrue("Object added successfully", true);
//        }
//        catch (Exception e)
//        {
//            System.out.println("ParticipantTestCases.testCreateParticipantWithEMPI()"
//                            + e.getMessage());
//            e.printStackTrace();
//            assertFalse(e.getMessage(), true);
//        }
//    }
//
//  /*  public void testRegisterParticipantWithEMPI()
//    {
//        try
//        {
//            Participant participant = BaseTestCaseUtility
//                    .initParticipantWithCPR();
//            participant.setEmpiId(empiId);
//            Long cpid =((CollectionProtocol)TestCaseUtility.getObjectMap(CollectionProtocol.class)).getId();
//            appService.registerParticipant( participant, cpid, "admin@admin.com");
//            System.out.println("Object created successfully:::"+participant.getLastName());
//            assertTrue("Object added successfully", true);
//        }
//        catch (Exception e)
//        {
//            System.out
//                    .println("ParticipantTestCases.testRegisterParticipantWithEMPI()"
//                            + e.getMessage());
//            e.printStackTrace();
//            assertFalse("could not register participatn", true);
//        }
//    }
//
//    public void testRegisterNewParticipantWithEMPI()
//    {
//        try
//        {
//            Participant participant = BaseTestCaseUtility
//                    .initParticipantWithCPR();
//            participant.setEmpiId(UniqueKeyGeneratorUtil.getUniqueKey());
//            Long cpid =((CollectionProtocol)TestCaseUtility.getObjectMap(CollectionProtocol.class)).getId();
//            appService.registerParticipant( participant, cpid, "admin@admin.com");
//            System.out.println("Object created successfully:::"+participant.getEmpiId());
//            assertTrue("participant registered ", true);
//        }
//        catch (Exception e)
//        {
//            System.out.println("ParticipantTestCases.testRegisterNewParticipantWithEMPI()"
//                            + e.getMessage());
//            e.printStackTrace();
//            assertFalse("could not add object", true);
//        }
//    }
//    */
//}
