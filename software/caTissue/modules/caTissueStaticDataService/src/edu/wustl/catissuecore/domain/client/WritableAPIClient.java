package edu.wustl.catissuecore.domain.client;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.GlobusCredentialException;

import java.rmi.RemoteException;

/**
 * @author Ion C. Olaru
 * Tests the Writable API operations
 *
 * */
public class WritableAPIClient extends Catissue_cacoreClient  {

    public WritableAPIClient(String url) throws MalformedURIException, RemoteException {
        super(url);
    }

    public WritableAPIClient(String url, GlobusCredential proxy) throws MalformedURIException, RemoteException {
        super(url, proxy);
    }

    public WritableAPIClient(EndpointReferenceType epr) throws MalformedURIException, RemoteException {
        super(epr);
    }

    public WritableAPIClient(EndpointReferenceType epr, GlobusCredential proxy) throws MalformedURIException, RemoteException {
        super(epr, proxy);
    }

    /**
     *
     * When ran from IDE specify the location of client-config.wssd
     * VM params: -Daxis.ClientConfigFile=C:\caTissue\software\caTissue\modules\caTissueStaticDataService\src\edu\wustl\catissuecore\domain\client\client-config.wsdd
     * Program params: url https://10.10.10.222:9443/wsrf/services/cagrid/Catissue_cacore CreateParticipant
     * */
    public static void main(String[] args) {
        System.out.println("Running the Grid Service Client");
        String url = args[1];
        System.out.println("Service URL: " + url);
        try {
            ClientRunAll.client = new Catissue_cacoreClient(url, ClientRunAll.getGlobusCredential());
        } catch (MalformedURIException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (GlobusCredentialException e) {
            e.printStackTrace();
        }
        runMain(args);
    }

    public static void runMain(String[] args) {

        /**
         * Collection Protocol Event Id
         * */
        long cpeId = 0;

        /**
         * Collection Protocol Registration Id
         * */
        long cprId = 0;

        /**
         * Storage Container Id
         * */
        long scId = 0;

        /**
         * Specimen Characteristics Id
         * */
        long sCharId = 0;

        /**
         * Specimen Id
         * */
        long spcId = 0;

        /**
         * Distribution Protocol Id
         * */
        long dpId = 0;

        /**
         * Order Id
         * */
        long oId = 0;

        /**
         * Order Item Id
         * */
        long oiId = 0;

        /**
         * Distributed Item Id
         * */
        long diId = 0;

        try {
            if (args[2].equals("CreateChemotherapy")) {
                ClientRunAll.createChemotherapy();
            }

            if (args[2].equals("CreateInstitution")) {
                ClientRunAll.createInstitution();
            }

            if (args[2].equals("CreateParticipant")) {
                ClientRunAll.createParticipantAndCPR();
                // ClientRunAll.testCreateParticipant();
            }

            if (args[2].equals("CreateSpecimen")) {
                if (args.length < 7) {
                    System.out.println("Please pass the CollectionProtocolEvent Id as -D CPE=#");
                    System.out.println("Please pass the CollectionProtocolRegistration Id as -D CPR=#");
                    System.out.println("Please pass the Specimen Container Id as -D SC=#");
                    System.out.println("Please pass the Speciemn Characteristics Id as -D SCHAR=#");
                } else {
                    cpeId = (Integer.parseInt(args[3]));
                    cprId = (Integer.parseInt(args[4]));
                    scId = (Integer.parseInt(args[5]));
                    sCharId = (Integer.parseInt(args[6]));
                }
                System.out.println("Collection Protocol Event Id: " + cpeId);
                System.out.println("Collection Protocol Registration Id: " + cprId);
                System.out.println("Specimen Container Id: " + scId);
                System.out.println("Speciemn Characteristics Id: " + sCharId);

                ClientRunAll.createSpecimenCollectionGroupWithSpecimens(cpeId, cprId, scId, sCharId);
            }

            if (args[2].equals("CreateOrder")) {
                if (args.length < 4) {
                    System.out.println("Please pass the Specimen Id as -D CPC=#");
                } else {
                    spcId = (Integer.parseInt(args[3]));
                }
                System.out.println("Specimen Id: " + spcId);

                ClientRunAll.placeAnOrder(spcId);
            }

            if (args[2].equals("UpdateOrder")) {
                if (args.length < 7) {
                    System.out.println("Please pass the Specimen Id as -D CPC=#");
                    System.out.println("Please pass the Distribution Protocol Id as -D DP=#");
                    System.out.println("Please pass the Order Id as -D O=#");
                    System.out.println("Please pass the Order Item Id as -D OI=#");
                } else {
                    spcId = (Integer.parseInt(args[3]));
                    dpId = (Integer.parseInt(args[4]));
                    oId = (Integer.parseInt(args[5]));
                    oiId = (Integer.parseInt(args[6]));
                }
                System.out.println("Specimen Id: " + spcId);
                System.out.println("Distribution Protocol Id: " + dpId);
                System.out.println("Order Id: " + oId);
                System.out.println("Order Item Id: " + oiId);

                ClientRunAll.updateOrderToPending(spcId, dpId, oId, oiId);
            }

            if (args[2].equals("DistributeOrder")) {
                if (args.length < 7) {
                    System.out.println("Please pass the Specimen Id as -D CPC=#");
                    System.out.println("Please pass the Distribution Protocol Id as -D DP=#");
                    System.out.println("Please pass the Order Id as -D O=#");
                    System.out.println("Please pass the Order Item Id as -D OI=#");
                } else {
                    spcId = (Integer.parseInt(args[3]));
                    dpId = (Integer.parseInt(args[4]));
                    oId = (Integer.parseInt(args[5]));
                    oiId = (Integer.parseInt(args[6]));
                    try {
                        diId = (Integer.parseInt(args[7]));
                    } catch (NumberFormatException e) {
                        System.out.println("Distribution Item id was not passed.");
                    }
                }
                System.out.println("Specimen Id: " + spcId);
                System.out.println("Distribution Protocol Id: " + dpId);
                System.out.println("Order Id: " + oId);
                System.out.println("Order Item Id: " + oiId);
                System.out.println("Distribution Item Id: " + diId);

                if (diId == 0)
                    ClientRunAll.distributeOrder(spcId, dpId, oId, oiId);
                else
                    ClientRunAll.distributeOrder(spcId, dpId, oId, oiId, diId);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

}
