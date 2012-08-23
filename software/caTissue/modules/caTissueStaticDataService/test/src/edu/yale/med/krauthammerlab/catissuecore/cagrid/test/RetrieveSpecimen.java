package edu.yale.med.krauthammerlab.catissuecore.cagrid.test;

import java.io.IOException;


import org.cagrid.cql2.CQLQuery;
import org.cagrid.cql2.results.CQLQueryResults;
import org.jdom.DataConversionException;
import org.jdom.Element;
import org.jdom.JDOMException;

import edu.wustl.catissuecore.domain.util.UniqueKeyGenerator;
import edu.wustl.catissuecore.domain.ws.AbstractSpecimenSpecimenCharacteristics;
import edu.wustl.catissuecore.domain.ws.CellSpecimen;
import edu.wustl.catissuecore.domain.ws.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ws.FluidSpecimen;
import edu.wustl.catissuecore.domain.ws.MolecularSpecimen;
import edu.wustl.catissuecore.domain.ws.Specimen;
import edu.wustl.catissuecore.domain.ws.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.ws.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.ws.SpecimenSpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.ws.TissueSpecimen;

public class RetrieveSpecimen extends TestBase {

    protected void compareSpecimens(Specimen specimen, Element specElement) throws DataConversionException {
        assertTrue(specElement.getAttribute("id").getLongValue() == specimen.getIdentifier());
        assertTrue("\""+specElement.getAttributeValue("barcode")+"\" == \""+specimen.getBarcode()+"\"",
                   specElement.getAttributeValue("barcode").equals(specimen.getBarcode()));
        assertTrue(specElement.getAttributeValue("label").equals(specimen.getLabel()));
    }

    /**
     * Test that a fluid specimen, specimen collection group, participant, registration, 
     * and collection protocol can be created via the WAPI 
     * and that the specimen can be retrieved and has all the same data as the original submission.
     * @throws Throwable
     */
    public void testCreateAndRetrieveFluidSpecimen() throws Throwable {
        CollectionProtocolRegistration reg = createParticipantAndCPR();
        SpecimenCollectionGroup scg = createSpecimenCollectionGroup(reg);

        Specimen specimen = insertSpecimen(scg,FluidSpecimen.class, "Fluid","Serum");
        System.out.println(String.format("--> Specimen inserted: %d", specimen.getIdentifier()));
        
        CQLQuery q = createSpecimenQuery(specimen,"edu.wustl.catissuecore.domain.FluidSpecimen");
        CQLQueryResults results = client.executeQuery(q);
        assertTrue("Has one result.", results.getObjectResult().length == 1);
        Element objectResult = getResultsElement(results);
        assertTrue(objectResult.getChildren().size() == 1);
        Element specElement = (Element) objectResult.getChildren().get(0);
        compareSpecimens(specimen,specElement);
    }

    /**
     * Test that a cell specimen, specimen collection group, participant, registration, 
     * and collection protocol can be created via the WAPI 
     * and that the specimen can be retrieved and has all the same data as the original submission.
     * @throws Throwable
     */
    public void testCreateAndRetrieveCellSpecimen() throws Throwable {
        CollectionProtocolRegistration reg = createParticipantAndCPR();
        SpecimenCollectionGroup scg = createSpecimenCollectionGroup(reg);

        Specimen specimen = insertSpecimen(scg,CellSpecimen.class,"Cell","Cryopreserved Cells");
        System.out.println(String.format("--> Specimen inserted: %d", specimen.getIdentifier()));
        
        CQLQuery q = createSpecimenQuery(specimen,"edu.wustl.catissuecore.domain.CellSpecimen");
        CQLQueryResults results = client.executeQuery(q);
        assertTrue("Has one result.", results.getObjectResult().length == 1);
        Element objectResult = getResultsElement(results);
        assertTrue(objectResult.getChildren().size() == 1);
        Element specElement = (Element) objectResult.getChildren().get(0);
        compareSpecimens(specimen,specElement);
    }

    /**
     * Test that a tissue specimen, specimen collection group, participant, registration, 
     * and collection protocol can be created via the WAPI 
     * and that the specimen can be retrieved and has all the same data as the original submission.
     * @throws Throwable
     */
    public void testCreateAndRetrieveTissueSpecimen() throws Throwable {
        CollectionProtocolRegistration reg = createParticipantAndCPR();
        SpecimenCollectionGroup scg = createSpecimenCollectionGroup(reg);

        Specimen specimen = insertSpecimen(scg,TissueSpecimen.class,"Tissue","Frozen Tissue");
        System.out.println(String.format("--> Specimen inserted: %d", specimen.getIdentifier()));
        
        CQLQuery q = createSpecimenQuery(specimen,"edu.wustl.catissuecore.domain.TissueSpecimen");
        CQLQueryResults results = client.executeQuery(q);
        assertTrue("Has one result.", results.getObjectResult().length == 1);
        Element objectResult = getResultsElement(results);
        assertTrue(objectResult.getChildren().size() == 1);
        Element specElement = (Element) objectResult.getChildren().get(0);
        compareSpecimens(specimen,specElement);
    }


    /**
     * Test that a molecular specimen, specimen collection group, participant, registration, 
     * and collection protocol can be created via the WAPI 
     * and that the specimen can be retrieved and has all the same data as the original submission.
     * @throws Throwable
     */
    public void testCreateAndRetrieveMolecularSpecimen() throws Throwable {
        CollectionProtocolRegistration reg = createParticipantAndCPR();
        SpecimenCollectionGroup scg = createSpecimenCollectionGroup(reg);

        Specimen specimen = insertSpecimen(scg,MolecularSpecimen.class,"Molecular","DNA");
        System.out.println(String.format("--> Specimen inserted: %d", specimen.getIdentifier()));
        
        CQLQuery q = createSpecimenQuery(specimen,"edu.wustl.catissuecore.domain.MolecularSpecimen");
        CQLQueryResults results = client.executeQuery(q);
        assertTrue("Has one result.", results.getObjectResult().length == 1);
        Element objectResult = getResultsElement(results);
        assertTrue(objectResult.getChildren().size() == 1);
        Element specElement = (Element) objectResult.getChildren().get(0);
        compareSpecimens(specimen,specElement);
    }
    
    /**
     * and collection protocol can be created via the WAPI 
     * Test that a molecular specimen, specimen collection group, participant, registration, 
     * and that the specimen can be retrieved by searching for the abstract specimen type 
     * and has all the same data as the original submission.
     * @throws Throwable
     */
    public void testCreateAndRetrieveViaAbstractSpecimen() throws Throwable {
        CollectionProtocolRegistration reg = createParticipantAndCPR();
        SpecimenCollectionGroup scg = createSpecimenCollectionGroup(reg);

        Specimen specimen = insertSpecimen(scg,MolecularSpecimen.class,"Molecular","DNA");
        System.out.println(String.format("AbstractSpecimenSearchTest Specimen inserted: %d", specimen.getIdentifier()));
        
        CQLQuery q = createSpecimenQuery(specimen,"edu.wustl.catissuecore.domain.AbstractSpecimen");
        System.out.println(serialize(q));
        CQLQueryResults results = client.executeQuery(q);
        System.out.println(serialize(results));
        assertTrue("Has result object",results.getObjectResult() != null);
        assertTrue("Has one result.", results.getObjectResult().length == 1);
        Element objectResult = getResultsElement(results);
        assertTrue(objectResult.getChildren().size() == 1);
        Element specElement = (Element) objectResult.getChildren().get(0);
        compareSpecimens(specimen,specElement);
    }
    
    /**
     * Test that a cell specimen, specimen collection group, participant, registration, 
     * and collection protocol can be created via the WAPI 
     * and that the specimen is returned from the WAPI with the GSID of that specimen.
     * @throws Throwable
     */
    public void testGetGSIDBackFromWritableAPI() throws Throwable {
        CollectionProtocolRegistration reg = createParticipantAndCPR();
        SpecimenCollectionGroup scg = createSpecimenCollectionGroup(reg);

        Specimen specimen = insertSpecimen(scg,CellSpecimen.class,"Cell","Cryopreserved Cells");
        System.out.println(String.format("--> Specimen inserted: %d", specimen.getIdentifier()));
        
        CQLQuery q = createSpecimenQuery(specimen,"edu.wustl.catissuecore.domain.CellSpecimen");
        CQLQueryResults results = client.executeQuery(q);
        assertTrue("Has one result.", results.getObjectResult().length == 1);
        Element objectResult = getResultsElement(results);
        assertTrue(objectResult.getChildren().size() == 1);
        Element specElement = (Element) objectResult.getChildren().get(0);        
        final String gsid = specElement.getAttributeValue("globalSpecimenIdentifier");
        assertNotNull(gsid);
        assertTrue(gsid.matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
    }
    
    /**
     * Test that a specimen cannot be created with non-permissible values in the specimen type,
     * or that the specimen cannot have mismatched specimen types with regard to the specimen class.
     * @throws Throwable
     */
    public void testCreateAndRetrieveInvalidSpecimen() throws Throwable {
        CollectionProtocolRegistration reg = createParticipantAndCPR();
        SpecimenCollectionGroup scg = createSpecimenCollectionGroup(reg);

        try {
            Specimen s = insertSpecimen(scg,FluidSpecimen.class,"Fluid","Bilbo");
            fail("Successfully created an invalid specimen with class of Cell and type of Bilbo, id="+s.getIdentifier());
        } catch (Exception e) {
            System.out.println("Unsuccessfully added an invalid specimen with a class of Cell and type of Bilbo.");
            System.out.println(e.getMessage());
        }

        try {
            Specimen s = insertSpecimen(scg,FluidSpecimen.class,"Fluid","Cryopreserved Cells");
            fail("Successfully created an invalid specimen with class of Fluid and type of Cryopreserved Cells, id="+s.getIdentifier());
        } catch (Exception e) {
            System.out.println("Unsuccessfully added an invalid specimen with a class of Cell and type of Bilbo.");
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Test that a specimen cannot be created with non-permissible values in the specimen class,
     * or that the specimen cannot have mismatched java class with regard to the specimen class.
     * @throws Throwable
     */
    public void testCreateInvalidSpecimenClass() throws Throwable {
        CollectionProtocolRegistration reg = createParticipantAndCPR();
        SpecimenCollectionGroup scg = createSpecimenCollectionGroup(reg);

        try {
            Specimen s = insertSpecimen(scg,CellSpecimen.class,"Bilbo","Cryopreserved Cells");
            fail("Successfully created an invalid specimen with class of Bilbo, id="+s.getIdentifier());
        } catch (Exception e) {
            System.out.println("Unsuccessfully added an invalid specimen with a class of Cell and type of Bilbo.");
            System.out.println(e.getMessage());
        }

        try {
            Specimen s = insertSpecimen(scg,CellSpecimen.class,"Fluid","Whole Blood");
            fail("Successfully created an invalid specimen with java class of CellSpecimen, a class of Fluid and type of Cryopreserved Cells, id="+s.getIdentifier());
        } catch (Exception e) {
            System.out.println("Unsuccessfully added an invalid specimen with a class of Cell and type of Bilbo.");
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Test that a specimen cannot be created without an associated specimen collection group.
     * @throws Throwable
     */
    public void testCreateSpecimenNoSCG() throws Throwable {
        try {
            String key = UniqueKeyGenerator.getKey();
            // Specimen
            Specimen s = new CellSpecimen();
     
            // attributes
            s.setSpecimenClass("Cell");
            s.setSpecimenType("Cryopreserved Cells");
            s.setCollectionStatus("Collected");
            s.setActivityStatus("Active");
            s.setPathologicalStatus("Not Specified");
            s.setBarcode("Bar Code XYZ - " + key);
            s.setLabel("Label XYZ - " + key);
            s.setInitialQuantity(10.0);
            s.setAvailableQuantity(10.0);
            s.setIsAvailable(true);
     
            // specimen characteristics
            s.setSpecimenCharacteristics(new AbstractSpecimenSpecimenCharacteristics());
            s.getSpecimenCharacteristics().setSpecimenCharacteristics(new SpecimenCharacteristics());
            s.getSpecimenCharacteristics().getSpecimenCharacteristics().setTissueSide("Left");
            s.getSpecimenCharacteristics().getSpecimenCharacteristics().setTissueSite("Not Specified");
     
            Specimen sResult = (Specimen)client.insert(s);
            s.setIdentifier(sResult.getIdentifier());
            s.setGlobalSpecimenIdentifier(sResult.getGlobalSpecimenIdentifier());

            fail("Successfully created an invalid specimen with null Specimen Collection Group, id="+s.getIdentifier());
        } catch (Exception e) {
            System.out.println("Unsuccessfully added an invalid specimen with a null Specimen Collection Group.");
            System.out.println(e.getMessage());
        }
    }
    
}
