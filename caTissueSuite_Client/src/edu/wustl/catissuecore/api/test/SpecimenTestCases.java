package edu.wustl.catissuecore.api.test;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import edu.wustl.catissuecore.domain.Aliquot;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

public class SpecimenTestCases extends CaTissueBaseTestCase
{
	/**
	 * AbstractDomainObject.
	 */
	AbstractDomainObject domainObject = null;

	/**
	 * Test to Update Collection Status Of Specimen.
	 */
	public void testUpdateCollectionStatusOfSpecimen()
	{
		try
		{
			Specimen sp = new Specimen();
			sp = (Specimen) TestCaseUtility.getObjectMap(Specimen.class);
			System.out.println("testUpdateCollectionStatusOfSpecimen Get Object Sp" + sp.getId());
			List spCollection = appService.getObjects(sp);
			sp = (Specimen) spCollection.get(0);
			System.out.println("Get Object Sp");
			sp.setCollectionStatus("Collected");
			//sp.setIsAvailable(true);
			sp.setExternalIdentifierCollection(null);
			System.out.println(sp + ": sp");
			sp = (Specimen) appService.updateObject(sp);
			System.out.println(sp + ": sp After Update");
			assertTrue(" Domain Object is successfully added ---->    Name:: " + sp, true);
			assertEquals(sp.getIsAvailable().booleanValue(),true);
		}
		catch (Exception e)
		{
			Logger.out.error("testUpdateCollectionStatusOfSpecimen" + e.getMessage(), e);
			System.out.println("SpecimenTestCases.testUpdateCollectionStatusOfSpecimen():" + e.getMessage());
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	}
	
	public void testUpdateSpecimenWithAvailableQtyGreaterThanInitialQty()
	{
		try
		{
			Specimen sp = new Specimen();
			sp = (Specimen) TestCaseUtility.getObjectMap(Specimen.class);
			sp.setInitialQuantity(100.0);
			sp.setAvailableQuantity(100000.0);
			sp.setIsAvailable(true);
			sp.setCollectionStatus("Collected");
			sp = (Specimen) appService.updateObject(sp);
			assertFalse("Specimen Object updated with  available quantity greater than initial Quantity.", true);
		}
		catch (Exception exp)
		{
			Logger.out.error("testUpdateSpecimenWithAvailableQtyGreaterThanInitialQty" + exp.getMessage(), exp);
			System.out.println("SpecimenTestCases.testUpdateSpecimenWithAvailableQtyGreaterThanInitialQty():" + exp.getMessage());
			exp.printStackTrace();
			assertTrue("Failed to update Specimen Object with  available quantity greater than initial Quantity.", true);
		}
	}

	/**
	 * Negative test case to update the activity status of specimen from disabled to active.
	 */
	public void testUpdateDisabledSpecimenToActive()
	{
		try
		{
			Specimen sp = new Specimen();
			sp = (Specimen) TestCaseUtility.getObjectMap(Specimen.class);
			System.out.println("testUpdateCollectionStatusOfSpecimen Get Object Sp" + sp.getId());
			List spCollection = appService.getObjects(sp);
			sp = (Specimen) spCollection.get(0);
			System.out.println("Get Object Sp");
			sp.setCollectionStatus("Collected");
			sp.setActivityStatus(Constants.DISABLED);
			//sp.setIsAvailable(true);
			sp.setExternalIdentifierCollection(null);
			System.out.println(sp + ": sp");
			sp = (Specimen) appService.updateObject(sp);
			System.out.println(sp + ": sp After Update");

			sp.setActivityStatus(Constants.ACTIVITY_STATUS_VALUES[1]);
			sp = (Specimen) appService.updateObject(sp);
			assertFalse("Domain Object updated", true);
		}
		catch (Exception e)
		{
			Logger.out.error("testUpdateCollectionStatusOfSpecimen" + e.getMessage(), e);
			System.out.println("SpecimenTestCases.testUpdateCollectionStatusOfSpecimen():" + e.getMessage());
			e.printStackTrace();
			assertTrue("Failed to update Domain Object's  activity status from disabled to active", true);
		}
	}

	/**
	 * Test Update Specimen With Consents.
	 */
	public void testUpdateSpecimenWithConsents()
	{
		try
		{
			Specimen sp = new Specimen();
			sp = (Specimen) TestCaseUtility.getObjectMap(Specimen.class);
			sp.setId(sp.getId());
			List spCollection = appService.getObjects(sp);
			sp = (Specimen) spCollection.get(0);
			sp.setCollectionStatus("Collected");
			sp.setIsAvailable(true);
			sp.setExternalIdentifierCollection(null);

			Collection consentTierStatusCollection = new HashSet();

			CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility.getObjectMap(CollectionProtocol.class);
			Collection consentTierCollection = collectionProtocol.getConsentTierCollection();
			Iterator consentTierItr = consentTierCollection.iterator();
			while (consentTierItr.hasNext())
			{
				ConsentTier consentTier = (ConsentTier) consentTierItr.next();
				ConsentTierStatus consentStatus = new ConsentTierStatus();
				consentStatus.setConsentTier(consentTier);
				consentStatus.setStatus("Yes");
				consentTierStatusCollection.add(consentStatus);
			}
			sp.setConsentTierStatusCollection(consentTierStatusCollection);
			System.out.println(sp + ": sp");
			sp = (Specimen) appService.updateObject(sp);
			System.out.println(sp + ": sp After Update");
			assertTrue(" Domain Object is successfully added ---->    Name:: " + sp, true);
		}
		catch (Exception e)
		{
			Logger.out.error("testUpdateSpecimenWithConsents" + e.getMessage(), e);
			System.out.println("SpecimenTestCases.testUpdateSpecimenWithConsents():" + e.getMessage());
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	}

	/**
	 * Test to Add Tissue Specimen.
	 */
	public void testAddTissueSpecimen()
	{
		try
		{
			TissueSpecimen specimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
			System.out.println("SpecimenTestCases.testAddTissueSpecimen(): " + scg);
			specimenObj.setSpecimenCollectionGroup(scg);
			Logger.out.info("Inserting domain object------->" + specimenObj);
			System.out.println("Before Creating Tissue Specimen");
			specimenObj = (TissueSpecimen) appService.createObject(specimenObj);
			TestCaseUtility.setObjectMap(specimenObj, TissueSpecimen.class);
			Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj.getId().toString());
			Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel());
			assertTrue(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel(), true);
		}
		catch (Exception e)
		{
			System.out.println("Exception thrown");
			System.out.println(e);
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	}

	/**
	 * Test to Add Tissue Specimen With Duplicate Label.
	 */
	public void testAddTissueSpecimenWithDuplicateLabel()
	{
		try
		{
			TissueSpecimen specimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
			System.out.println("SpecimenTestCases.testAddTissueSpecimen(): " + scg);
			specimenObj.setSpecimenCollectionGroup(scg);
			specimenObj.setLabel(((TissueSpecimen) TestCaseUtility.getObjectMap(TissueSpecimen.class)).getLabel());
			Logger.out.info("Inserting domain object------->" + specimenObj);
			System.out.println("Before Creating Tissue Specimen");
			specimenObj = (TissueSpecimen) appService.createObject(specimenObj);

			assertFalse("Lable generator is active so specimen creation wiht same label" + " should fail", true);
		}
		catch (Exception e)
		{
			assertTrue("Domain Object is successfully added ---->As Label generator is active", true);
			System.out.println("Exception thrown");
			System.out.println(e);
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();

		}
	}

	/**
	 * Test to Update Tissue Specimen With Duplicate Label.
	 */
	public void testUpdateTissueSpecimenWithDuplicateLabel()
	{
		try
		{
			TissueSpecimen specimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
			System.out.println("SpecimenTestCases.testAddTissueSpecimen(): " + scg);
			specimenObj.setSpecimenCollectionGroup(scg);

			Logger.out.info("Inserting domain object------->" + specimenObj);
			System.out.println("Before Creating Tissue Specimen");
			specimenObj = (TissueSpecimen) appService.createObject(specimenObj);
			specimenObj.setLabel(((TissueSpecimen) TestCaseUtility.getObjectMap(TissueSpecimen.class)).getLabel());
			specimenObj.setLabel(((TissueSpecimen) TestCaseUtility.getObjectMap(TissueSpecimen.class)).getLabel());
			specimenObj = (TissueSpecimen) appService.updateObject(specimenObj);

			assertFalse("Lable generator is active so specimen creation wiht same label " + "should fail", true);
		}
		catch (Exception e)
		{
			assertTrue("Domain Object is successfully added ---->As Label generator is " + "active", true);
			System.out.println("Exception thrown");
			System.out.println(e);
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	/**
	 * Test to Add Molecular Specimen.
	 */
	public void testAddMolecularSpecimen()
	{
		try
		{
			MolecularSpecimen specimenObj = (MolecularSpecimen) BaseTestCaseUtility.initMolecularSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
			System.out.println("SpecimenTestCases.testAddMolecularSpecimen(): " + scg);
			specimenObj.setSpecimenCollectionGroup(scg);
			Logger.out.info("Inserting domain object------->" + specimenObj);
			System.out.println("Before Creating Tissue Specimen");
			specimenObj = (MolecularSpecimen) appService.createObject(specimenObj);
			TestCaseUtility.setObjectMap(specimenObj, MolecularSpecimen.class);
			System.out.println("Afer Creating Tissue Specimen");
			Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj.getId().toString());
			Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel());
			assertTrue("Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel(), true);

		}
		catch (Exception e)
		{
			System.out.println("Exception thrown");
			System.out.println(e);
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	}

	/**
	 * Test to Add Cell Specimen.
	 */
	public void testAddCellSpecimen()
	{
		try
		{
			CellSpecimen specimenObj = (CellSpecimen) BaseTestCaseUtility.initCellSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
			specimenObj.setSpecimenCollectionGroup(scg);
			Logger.out.info("Inserting domain object------->" + specimenObj);
			System.out.println("Before Creating Tissue Specimen");
			specimenObj = (CellSpecimen) appService.createObject(specimenObj);
			TestCaseUtility.setObjectMap(specimenObj, CellSpecimen.class);
			System.out.println("Afer Creating Tissue Specimen");
			Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj.getId().toString());
			Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel());
			assertTrue("Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel(), true);
		}
		catch (Exception e)
		{
			System.out.println("Exception thrown");
			System.out.println(e);
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	}

	/**
	 * Test to Add Fluid Specimen.
	 */
	public void testAddFluidSpecimen()
	{
		try
		{
			FluidSpecimen specimenObj = (FluidSpecimen) BaseTestCaseUtility.initFluidSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
			specimenObj.setSpecimenCollectionGroup(scg);
			Logger.out.info("Inserting domain object------->" + specimenObj);
			System.out.println("Before Creating Tissue Specimen");
			specimenObj = (FluidSpecimen) appService.createObject(specimenObj);
			TestCaseUtility.setObjectMap(specimenObj, FluidSpecimen.class);
			System.out.println("Afer Creating Tissue Specimen");
			Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj.getId().toString());
			Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel());
			assertTrue("Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel(), true);

		}
		catch (Exception e)
		{
			System.out.println("Exception thrown");
			System.out.println(e);
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	}

	/**
	 * Test to Update Specimen With Barcode.
	 */
	public void testUpdateSpecimenWithBarcode()
	{
		String uniqueKey = UniqueKeyGeneratorUtil.getUniqueKey();
		MolecularSpecimen specimen = (MolecularSpecimen) TestCaseUtility.getObjectMap(MolecularSpecimen.class);
		specimen.setBarcode("barcode" + uniqueKey);
		specimen.setExternalIdentifierCollection(null);
		try
		{
			specimen = (MolecularSpecimen) appService.updateObject(specimen);
			System.out.println(specimen + ": specimen After Update");
			assertTrue(" Domain Object is successfully added ---->    Name:: " + specimen, true);
		}
		catch (Exception e)
		{
			Logger.out.error("testUpdateCollectionStatusOfSpecimen" + e.getMessage(), e);
			System.out.println("SpecimenTestCases.testUpdateCollectionStatusOfSpecimen():" + e.getMessage());
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}

	}

	/**
	 * Test to Update Specimen With Case Sensitive Barcode.
	 */
	public void testUpdateSpecimenWithCaseSensitiveBarcode()
	{
		String uniqueKey = UniqueKeyGeneratorUtil.getUniqueKey();
		MolecularSpecimen cellSpecimen = (MolecularSpecimen) TestCaseUtility.getObjectMap(MolecularSpecimen.class);
		cellSpecimen.setBarcode("specimen with barcode" + uniqueKey);
		cellSpecimen.setExternalIdentifierCollection(null);
		try
		{
			cellSpecimen = (MolecularSpecimen) appService.updateObject(cellSpecimen);
			System.out.println(cellSpecimen + ": specimen After Update");
			assertTrue(" Domain Object is successfully added ---->    Name:: " + cellSpecimen, true);
		}
		catch (Exception e)
		{
			Logger.out.error("testUpdateCollectionStatusOfSpecimen" + e.getMessage(), e);
			System.out.println("SpecimenTestCases.testUpdateCollectionStatusOfSpecimen():" + e.getMessage());
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
		TissueSpecimen specimen = (TissueSpecimen) TestCaseUtility.getObjectMap(TissueSpecimen.class);
		specimen.setBarcode("SPECIMEN WITH BARCODE" + uniqueKey);
		specimen.setExternalIdentifierCollection(null);
		try
		{
			specimen = (TissueSpecimen) appService.updateObject(specimen);
			System.out.println(specimen + ": specimen After Update");
			assertTrue(" Domain Object is successfully added ---->    Name:: " + specimen, true);
		}
		catch (Exception e)
		{
			Logger.out.error("testUpdateCollectionStatusOfSpecimen" + e.getMessage(), e);
			System.out.println("SpecimenTestCases.testUpdateCollectionStatusOfSpecimen():" + e.getMessage());
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	}

	/**
	 * Test Search Specimen With Barcode.
	 */
	public void testSearchSpecimenWithBarcode()
	{
		Specimen specimen = new Specimen();
		TissueSpecimen cachedSpecimen = (TissueSpecimen) TestCaseUtility.getObjectMap(TissueSpecimen.class);
		specimen.setBarcode(cachedSpecimen.getBarcode());
		Logger.out.info("searching domain object");
		try
		{
			List resultList = appService.search(Specimen.class, specimen);
			if (resultList != null)
			{
				if (resultList.size() == 1)
				{
					System.out.println(resultList.size());
					System.out.println("Case sensitive match is found of barcode is for Specimen");
					assertFalse("Case sensitive match is found of barcode is for Specimen", true);
				}
				else
				{
					assertTrue("All the  Specimen matched barcode found", true);
				}
			}
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Couldnot found Specimen", true);
		}
	}

	/**
	 * Test to Search Tissue Specimen.
	 */
	public void testSearchTissueSpecimen()
	{
		TissueSpecimen specimen = new TissueSpecimen();
		TissueSpecimen cachedSpecimen = (TissueSpecimen) TestCaseUtility.getObjectMap(TissueSpecimen.class);
		specimen.setId(cachedSpecimen.getId());
		Logger.out.info(" searching domain object");
		try
		{
			List resultList = appService.search(TissueSpecimen.class, specimen);
			for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();)
			{
				Specimen returnedspecimen = (Specimen) resultsIterator.next();
				System.out.println("here-->" + returnedspecimen.getLabel() + "Id:" + returnedspecimen.getId());
				Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedspecimen.getLabel());
			}
			assertTrue("Specimen found", true);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Couldnot found Specimen", true);
		}

	}

	/**
	 * Test to Search Specimen.
	 */
	public void testSearchSpecimen()
	{
		Specimen specimen = new Specimen();
		TissueSpecimen cachedSpecimen = (TissueSpecimen) TestCaseUtility.getObjectMap(TissueSpecimen.class);
		specimen.setId(cachedSpecimen.getId());
		Logger.out.info(" searching domain object");
		try
		{
			List resultList = appService.search(Specimen.class, specimen);
			for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();)
			{
				Specimen returnedspecimen = (Specimen) resultsIterator.next();
				System.out.println("here-->" + returnedspecimen.getLabel() + "Id:" + returnedspecimen.getId());
				Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedspecimen.getLabel());
			}
			assertTrue("Specimen found", true);
		}
		catch (Exception e)
		{
			System.out.println("SpecimenTestCases.testSearchSpecimen()" + e.getMessage());
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Couldnot found Specimen", true);
		}

	}

	/**
	 * Test to Update Tissue Specimen.
	 */
	public void testUpdateTissueSpecimen()
	{
		try
		{
			TissueSpecimen ts = (TissueSpecimen) TestCaseUtility.getObjectMap(TissueSpecimen.class);
			System.out.println("Specimen from map" + ts.getLabel());
			ts.setLabel("upadated TS" + UniqueKeyGeneratorUtil.getUniqueKey());
			ts.setIsAvailable(Boolean.TRUE);
			ts.setCollectionStatus("Collected");
			Collection externalIdentifierCollection = new HashSet();
			ExternalIdentifier externalIdentifier = new ExternalIdentifier();
			externalIdentifier.setName("eid" + UniqueKeyGeneratorUtil.getUniqueKey());
			externalIdentifier.setValue("" + UniqueKeyGeneratorUtil.getUniqueKey());
			externalIdentifier.setSpecimen(ts);
			externalIdentifierCollection.add(externalIdentifier);
			ts.setExternalIdentifierCollection(externalIdentifierCollection);
			ts = (TissueSpecimen) appService.updateObject(ts);
			Logger.out.info(" Domain Object is successfully updated ---->  :: " + ts.getLabel());
			assertTrue(" Domain Object is successfully added ---->    Name:: " + ts.getLabel(), true);
		}
		catch (Exception e)
		{
			System.out.println("SpecimenTestCases.testUpdateTissueSpecimen():" + e.getMessage());
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	}

	/**
	 * Test to Update Molecular Specimen.
	 */
	public void testUpdateMolecularSpecimen()
	{
		try
		{
			MolecularSpecimen ts = (MolecularSpecimen) TestCaseUtility.getObjectMap(MolecularSpecimen.class);
			System.out.println("Specimen from map" + ts.getLabel());
			ts.setLabel("upadated TS" + UniqueKeyGeneratorUtil.getUniqueKey());
			ts.setIsAvailable(Boolean.TRUE);
			ts.setCollectionStatus("Collected");
			Collection externalIdentifierCollection = new HashSet();
			ExternalIdentifier externalIdentifier = new ExternalIdentifier();
			externalIdentifier.setName("eid" + UniqueKeyGeneratorUtil.getUniqueKey());
			externalIdentifier.setValue(Constants.DOUBLE_QUOTES + UniqueKeyGeneratorUtil.getUniqueKey());
			externalIdentifier.setSpecimen(ts);
			externalIdentifierCollection.add(externalIdentifier);
			ts.setExternalIdentifierCollection(externalIdentifierCollection);
			ts = (MolecularSpecimen) appService.updateObject(ts);
			Logger.out.info(" Domain Object is successfully updated ---->  :: " + ts.getLabel());
			assertTrue(" Domain Object is successfully added ---->    Name:: " + ts.getLabel(), true);
		}
		catch (Exception e)
		{
			System.out.println("SpecimenTestCases.testUpdateMolecularSpecimen(): " + e.getMessage());
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	}

	/**
	 * Test to Verify Consent Response And Consent Status At SCG.
	 */
	public void testVerifyConsentResponseAndConsentStatusAtSCG()
	{
		System.out.println("Inside ConsentsVerificationTestCases:");
		CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
		try
		{
			cp = (CollectionProtocol) appService.createObject(cp);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			System.out.println("SpecimenTestCases.testVerifyConsentResponseAndConsentStatusAtSCG() 1" + e.getMessage());
			e.printStackTrace();
			assertFalse("Failed to create collection protocol", true);
		}
		System.out.println("CP:" + cp.getTitle());
		TestCaseUtility.setObjectMap(cp, CollectionProtocol.class);

		SpecimenCollectionGroup scg = (SpecimenCollectionGroup) createSCGWithConsents(cp);
		CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) TestCaseUtility
				.getObjectMap(CollectionProtocolRegistration.class);
		Collection consStatusCol = scg.getConsentTierStatusCollection();
		Collection consResponseCol = collectionProtocolRegistration.getConsentTierResponseCollection();

		Iterator consResItr = consResponseCol.iterator();
		Iterator consStatusItr = consStatusCol.iterator();

		ConsentTierStatus cs[] = new ConsentTierStatus[consStatusCol.size()];
		ConsentTierResponse rs[] = new ConsentTierResponse[consResponseCol.size()];
		int i = 0;
		System.out.println("Reached up to while");
		while (consStatusItr.hasNext())
		{
			cs[i] = (ConsentTierStatus) consStatusItr.next();
			rs[i] = (ConsentTierResponse) consResItr.next();
			i++;
		}

		for (int j = 0; j < cs.length; j++)
		{
			for (int k = 0; k < cs.length; k++)
			{
				if (cs[k].getConsentTier().getStatement().equals(rs[j].getConsentTier().getStatement()))
				{
					System.out.println("Statement:" + cs[k].getConsentTier().getStatement());
					assertEquals(cs[k].getStatus(), rs[j].getResponse());
				}
			}
		}

		TissueSpecimen ts = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		//		ts.setStorageContainer(null);
		ts.setSpecimenCollectionGroup(scg);
		ts.setLabel("TisSpec" + UniqueKeyGeneratorUtil.getUniqueKey());
		ts.setIsAvailable(Boolean.TRUE);
		System.out.println("Befor creating Tissue Specimen");
		try
		{
			ts = (TissueSpecimen) appService.createObject(ts);
			System.out.println("Spec:" + ts.getLabel());
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			System.out.println("SpecimenTestCases.testVerifyConsentResponseAndConsentStatusAtSCG()" + e.getMessage());
			e.printStackTrace();
			assertFalse("Failed to create", true);
		}
	}

	/**
	 * Test to Verify Consent Resopnse And Consent Status For Updated CP.
	 */
	public void testVerifyConsentResopnseAndConsentStatusForUpadatedCP()
	{
		System.out.println("Inside ConsentsVerificationTestCases:");
		CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
		try
		{
			cp = (CollectionProtocol) appService.createObject(cp);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			System.out.println("SpecimenTestCases.testVerifyConsentResopnseAndConsentStatusForUpadatedCP()" + e.getMessage());
			e.printStackTrace();
			assertFalse("Failed to create collection protocol", true);
		}
		System.out.println("CP:" + cp.getTitle());
		TestCaseUtility.setObjectMap(cp, CollectionProtocol.class);

		SpecimenCollectionGroup scg = (SpecimenCollectionGroup) createSCGWithConsents(cp);

		TissueSpecimen ts = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		//		ts.setStorageContainer(null);
		ts.setSpecimenCollectionGroup(scg);
		ts.setLabel("TisSpec" + UniqueKeyGeneratorUtil.getUniqueKey());
		ts.setIsAvailable(Boolean.TRUE);
		System.out.println("Befor creating Tissue Specimen");

		try
		{
			ts = (TissueSpecimen) appService.createObject(ts);
			System.out.println("Spec:" + ts.getLabel());
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			System.out.println("SpecimenTestCases.testVerifyConsentResopnseAndConsentStatusForUpadatedCP()" + e.getMessage());
			e.printStackTrace();
			assertFalse("Failed to create", true);
		}

		CollectionProtocol updatedCP = (CollectionProtocol) updateCP(cp);

		SpecimenCollectionGroup newSCG = (SpecimenCollectionGroup) createSCGWithConsents(updatedCP);

		TissueSpecimen ts1 = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		//		ts1.setStorageContainer(null);
		ts1.setSpecimenCollectionGroup(newSCG);
		ts1.setLabel("TisSpec" + UniqueKeyGeneratorUtil.getUniqueKey());
		ts1.setIsAvailable(Boolean.TRUE);
		System.out.println("Befor creating Tissue Specimen");

		try
		{
			ts = (TissueSpecimen) appService.createObject(ts1);
			System.out.println("Spec:" + ts.getLabel());
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to create", true);
		}

		Collection consStatusCol = newSCG.getConsentTierStatusCollection();
		CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) TestCaseUtility
				.getObjectMap(CollectionProtocolRegistration.class);
		Collection consResponseCol = collectionProtocolRegistration.getConsentTierResponseCollection();

		Iterator consResItr = consResponseCol.iterator();
		Iterator consStatusItr = consStatusCol.iterator();

		ConsentTierStatus cs[] = new ConsentTierStatus[consStatusCol.size()];
		ConsentTierResponse rs[] = new ConsentTierResponse[consResponseCol.size()];
		int i = 0;
		System.out.println("Reached up to while");
		while (consStatusItr.hasNext())
		{
			cs[i] = (ConsentTierStatus) consStatusItr.next();
			rs[i] = (ConsentTierResponse) consResItr.next();
			i++;
		}

		for (int j = 0; j < cs.length; j++)
		{
			for (int k = 0; k < cs.length; k++)
			{
				if (cs[k].getConsentTier().getStatement().equals(rs[j].getConsentTier().getStatement()))
				{
					System.out.println("Statements:" + cs[k].getConsentTier().getStatement());
					assertEquals(cs[k].getStatus(), rs[j].getResponse());
				}
			}
		}

	}

	/**
	 * Create SCG With Consents.
	 * @param cp CollectionProtocol
	 * @return SpecimenCollectionGroup
	 */
	public SpecimenCollectionGroup createSCGWithConsents(CollectionProtocol cp)
	{

		Participant participant = BaseTestCaseUtility.initParticipant();

		try
		{
			participant = (Participant) appService.createObject(participant);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to create collection protocol", true);
		}
		System.out.println("Participant:" + participant.getFirstName());
		CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
		collectionProtocolRegistration.setCollectionProtocol(cp);
		collectionProtocolRegistration.setParticipant(participant);
		collectionProtocolRegistration.setProtocolParticipantIdentifier("");
		collectionProtocolRegistration.setActivityStatus("Active");
		try
		{
			collectionProtocolRegistration.setRegistrationDate(Utility.parseDate("08/15/1975", Utility.datePattern("08/15/1975")));
			collectionProtocolRegistration.setConsentSignatureDate(Utility.parseDate("11/23/2006", Utility.datePattern("11/23/2006")));

		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		collectionProtocolRegistration.setSignedConsentDocumentURL("F:/doc/consentDoc.doc");
		User user = (User) TestCaseUtility.getObjectMap(User.class);
		//User user = new User();
		//user.setId(new Long(1));
		collectionProtocolRegistration.setConsentWitness(user);

		Collection consentTierResponseCollection = new LinkedHashSet();
		Collection consentTierCollection = new LinkedHashSet();
		consentTierCollection = cp.getConsentTierCollection();

		//			Iterator ConsentierItr = consentTierCollection.iterator();
		//
		//			ConsentTier c1= (ConsentTier) ConsentierItr.next();
		//			ConsentTierResponse r1 = new ConsentTierResponse();
		//			r1.setResponse("Yes");
		//			r1.setConsentTier(c1);
		//			consentTierResponseCollection.add(r1);
		//			ConsentTier c2= (ConsentTier) ConsentierItr.next();
		//			ConsentTierResponse r2 = new ConsentTierResponse();
		//			r2.setResponse("No");
		//			consentTierResponseCollection.add(r2);
		//			r2.setConsentTier(c2);
		//			ConsentTier c3= (ConsentTier) ConsentierItr.next();
		//			ConsentTierResponse r3 = new ConsentTierResponse();
		//			r3.setResponse("Yes");
		//			r3.setConsentTier(c3);
		//			consentTierResponseCollection.add(r3);
		Iterator consentTierItr = consentTierCollection.iterator();
		while (consentTierItr.hasNext())
		{
			ConsentTier consent = (ConsentTier) consentTierItr.next();
			ConsentTierResponse response = new ConsentTierResponse();
			response.setResponse("Yes");
			response.setConsentTier(consent);
			consentTierResponseCollection.add(response);
		}

		collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);

		System.out.println("Creating CPR");
		try
		{
			collectionProtocolRegistration = (CollectionProtocolRegistration) appService.createObject(collectionProtocolRegistration);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to register participant", true);
		}
		TestCaseUtility.setObjectMap(collectionProtocolRegistration, CollectionProtocolRegistration.class);

		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg = (SpecimenCollectionGroup) BaseTestCaseUtility.createSCG(collectionProtocolRegistration);
		Site site = (Site) TestCaseUtility.getObjectMap(Site.class);
		//Site site = new Site();
		//site.setId(new Long(1));

		scg.setSpecimenCollectionSite(site);
		scg.setName("New SCG" + UniqueKeyGeneratorUtil.getUniqueKey());
		scg = (SpecimenCollectionGroup) BaseTestCaseUtility.setEventParameters(scg);
		System.out.println("Creating SCG");
		try
		{
			scg = (SpecimenCollectionGroup) appService.createObject(scg);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to register participant", true);
		}
		return scg;
	}

	/**
	 * Update COllection Protocol.
	 * @param collectionProtocol CollectionProtocol
	 * @return CollectionProtocol
	 */
	public CollectionProtocol updateCP(CollectionProtocol collectionProtocol)
	{

		try
		{
			collectionProtocol = (CollectionProtocol) TestCaseUtility.getObjectMap(CollectionProtocol.class);
			Logger.out.info("updating domain object------->" + collectionProtocol);
			Collection ConCollection = collectionProtocol.getConsentTierCollection();
			ConsentTier c4 = new ConsentTier();
			c4.setStatement("consent for any research");
			ConCollection.add(c4);
			collectionProtocol.setConsentTierCollection(ConCollection);
			collectionProtocol = (CollectionProtocol) appService.updateObject(collectionProtocol);
			System.out.println("after updation" + collectionProtocol.getTitle());
			System.out.println("after updation" + collectionProtocol.getShortTitle());
			assertTrue("Domain object updated successfully", true);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			//assertFalse("Failed to update object",true);
			fail("Failed to update object");
		}
		return collectionProtocol;
	}

	/**
	 * Test to Verify Consents Withdrawn With Return Option.
	 */
	public void testVerifyConsentsWithdrawnWithReturnOption()
	{
		System.out.println("Inside ConsentsVerificationTestCases:");
		CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
		try
		{
			cp = (CollectionProtocol) appService.createObject(cp);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			System.out.println("SpecimenTestCases.testVerifyConsentsWithdrawnWithReturnOption()" + e.getMessage());
			e.printStackTrace();
			assertFalse("Failed to create collection protocol", true);
		}
		System.out.println("CP:" + cp.getTitle());
		Participant participant = BaseTestCaseUtility.initParticipant();

		try
		{
			participant = (Participant) appService.createObject(participant);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			System.out.println("SpecimenTestCases.testVerifyConsentsWithdrawnWithReturnOption() 2" + e.getMessage());
			e.printStackTrace();
			assertFalse("Failed to create collection protocol", true);
		}
		System.out.println("Participant:" + participant.getFirstName());
		CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
		collectionProtocolRegistration.setCollectionProtocol(cp);
		collectionProtocolRegistration.setParticipant(participant);
		collectionProtocolRegistration.setProtocolParticipantIdentifier("");
		collectionProtocolRegistration.setActivityStatus("Active");
		try
		{
			collectionProtocolRegistration.setRegistrationDate(Utility.parseDate("08/15/1975", Utility.datePattern("08/15/1975")));
			collectionProtocolRegistration.setConsentSignatureDate(Utility.parseDate("11/23/2006", Utility.datePattern("11/23/2006")));

		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		collectionProtocolRegistration.setSignedConsentDocumentURL("F:/doc/consentDoc.doc");
		User user = (User) TestCaseUtility.getObjectMap(User.class);
		collectionProtocolRegistration.setConsentWitness(user);

		Collection consentTierResponseCollection = new HashSet();
		Collection consentTierCollection = cp.getConsentTierCollection();
		Iterator consentTierItr = consentTierCollection.iterator();

		while (consentTierItr.hasNext())
		{
			ConsentTier consentTier = (ConsentTier) consentTierItr.next();
			ConsentTierResponse consentResponse = new ConsentTierResponse();
			consentResponse.setConsentTier(consentTier);
			consentResponse.setResponse("Yes");
			consentTierResponseCollection.add(consentResponse);
		}

		collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);

		collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);
		System.out.println("Creating CPR");
		try
		{
			collectionProtocolRegistration = (CollectionProtocolRegistration) appService.createObject(collectionProtocolRegistration);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to register participant", true);
		}

		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg = (SpecimenCollectionGroup) BaseTestCaseUtility.createSCG(collectionProtocolRegistration);
		Site site = (Site) TestCaseUtility.getObjectMap(Site.class);
		scg.setSpecimenCollectionSite(site);
		scg.setName("New SCG" + UniqueKeyGeneratorUtil.getUniqueKey());
		scg = (SpecimenCollectionGroup) BaseTestCaseUtility.setEventParameters(scg);
		System.out.println("Creating SCG");

		try
		{
			scg = (SpecimenCollectionGroup) appService.createObject(scg);

		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to rcreate SCG", true);
		}

		TissueSpecimen ts = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		//		ts.setStorageContainer(null);
		ts.setSpecimenCollectionGroup(scg);
		ts.setLabel("TisSpec" + UniqueKeyGeneratorUtil.getUniqueKey());
		System.out.println("Befor creating Tissue Specimen");

		try
		{
			ts = (TissueSpecimen) appService.createObject(ts);
		}
		catch (Exception e)
		{
			assertFalse("Failed to create specimen", true);
		}

		Collection consentTierCollection1 = cp.getConsentTierCollection();
		Iterator consentTierItr1 = consentTierCollection1.iterator();
		Collection newConStatusCol = new HashSet();
		Collection consentTierStatusCollection = scg.getConsentTierStatusCollection();

		Iterator conStatusItr = consentTierStatusCollection.iterator();
		ConsentTier c1 = (ConsentTier) consentTierItr1.next();
		ConsentTierStatus consentStatus1 = new ConsentTierStatus();
		consentStatus1.setStatus("Withdrawn");
		consentStatus1.setConsentTier(c1);
		newConStatusCol.add(consentStatus1);
		ConsentTier c2 = (ConsentTier) consentTierItr1.next();
		ConsentTierStatus consentStatus2 = new ConsentTierStatus();
		consentStatus2.setStatus("Withdrawn");
		consentStatus2.setConsentTier(c2);
		newConStatusCol.add(consentStatus2);
		ConsentTier c3 = (ConsentTier) consentTierItr1.next();
		ConsentTierStatus consentStatus3 = new ConsentTierStatus();
		consentStatus3.setStatus("Withdrawn");
		consentStatus3.setConsentTier(c3);
		newConStatusCol.add(consentStatus3);

		scg.setConsentTierStatusCollection(newConStatusCol);
		scg.setConsentWithdrawalOption("Return");
		scg.getCollectionProtocolRegistration().getCollectionProtocol().setId(
				collectionProtocolRegistration.getCollectionProtocol().getId());
		scg.getCollectionProtocolRegistration().setParticipant(participant);
		try
		{
			scg = (SpecimenCollectionGroup) appService.updateObject(scg);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to update SCG", true);
		}

	}

	/**
	 * Test to Verify Consent Withdrawn With Discard Option.
	 */
	public void testVerifyConsentWithdrawnWithDiscardOption()
	{
		System.out.println("Inside ConsentsVerificationTestCases:");
		CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
		try
		{
			cp = (CollectionProtocol) appService.createObject(cp);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to create collection protocol", true);
		}
		System.out.println("CP:" + cp.getTitle());
		Participant participant = BaseTestCaseUtility.initParticipant();

		try
		{
			participant = (Participant) appService.createObject(participant);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to create collection protocol", true);
		}
		System.out.println("Participant:" + participant.getFirstName());
		CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
		collectionProtocolRegistration.setCollectionProtocol(cp);
		collectionProtocolRegistration.setParticipant(participant);
		collectionProtocolRegistration.setProtocolParticipantIdentifier("");
		collectionProtocolRegistration.setActivityStatus("Active");
		try
		{
			collectionProtocolRegistration.setRegistrationDate(Utility.parseDate("08/15/1975", Utility.datePattern("08/15/1975")));
			collectionProtocolRegistration.setConsentSignatureDate(Utility.parseDate("11/23/2006", Utility.datePattern("11/23/2006")));

		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		collectionProtocolRegistration.setSignedConsentDocumentURL("F:/doc/consentDoc.doc");
		User user = (User) TestCaseUtility.getObjectMap(User.class);
		collectionProtocolRegistration.setConsentWitness(user);

		Collection consentTierResponseCollection = new HashSet();
		Collection consentTierCollection = cp.getConsentTierCollection();
		Iterator consentTierItr = consentTierCollection.iterator();

		while (consentTierItr.hasNext())
		{
			ConsentTier consentTier = (ConsentTier) consentTierItr.next();
			ConsentTierResponse consentResponse = new ConsentTierResponse();
			consentResponse.setConsentTier(consentTier);
			consentResponse.setResponse("Yes");
			consentTierResponseCollection.add(consentResponse);
		}

		collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);

		collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);
		System.out.println("Creating CPR");
		try
		{
			collectionProtocolRegistration = (CollectionProtocolRegistration) appService.createObject(collectionProtocolRegistration);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to register participant", true);
		}

		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg = (SpecimenCollectionGroup) BaseTestCaseUtility.createSCG(collectionProtocolRegistration);
		Site site = (Site) TestCaseUtility.getObjectMap(Site.class);
		scg.setSpecimenCollectionSite(site);
		scg.setName("New SCG" + UniqueKeyGeneratorUtil.getUniqueKey());
		scg = (SpecimenCollectionGroup) BaseTestCaseUtility.setEventParameters(scg);
		System.out.println("Creating SCG");

		try
		{
			scg = (SpecimenCollectionGroup) appService.createObject(scg);

		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to rcreate SCG", true);
		}

		TissueSpecimen ts = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		//		ts.setStorageContainer(null);
		ts.setSpecimenCollectionGroup(scg);
		ts.setLabel("TisSpec" + UniqueKeyGeneratorUtil.getUniqueKey());
		System.out.println("Befor creating Tissue Specimen");

		try
		{
			ts = (TissueSpecimen) appService.createObject(ts);
		}
		catch (Exception e)
		{
			assertFalse("Failed to create specimen", true);
		}

		Collection consentTierCollection1 = cp.getConsentTierCollection();
		Iterator consentTierItr1 = consentTierCollection1.iterator();
		Collection newConStatusCol = new HashSet();
		Collection consentTierStatusCollection = scg.getConsentTierStatusCollection();

		Iterator conStatusItr = consentTierStatusCollection.iterator();
		ConsentTier c1 = (ConsentTier) consentTierItr1.next();
		ConsentTierStatus consentStatus1 = new ConsentTierStatus();
		consentStatus1.setStatus("Withdrawn");
		consentStatus1.setConsentTier(c1);
		newConStatusCol.add(consentStatus1);
		ConsentTier c2 = (ConsentTier) consentTierItr1.next();
		ConsentTierStatus consentStatus2 = new ConsentTierStatus();
		consentStatus2.setStatus("Withdrawn");
		consentStatus2.setConsentTier(c2);
		newConStatusCol.add(consentStatus2);
		ConsentTier c3 = (ConsentTier) consentTierItr1.next();
		ConsentTierStatus consentStatus3 = new ConsentTierStatus();
		consentStatus3.setStatus("Withdrawn");
		consentStatus3.setConsentTier(c3);
		newConStatusCol.add(consentStatus3);

		scg.setConsentTierStatusCollection(newConStatusCol);
		scg.setConsentWithdrawalOption("Discard");
		scg.getCollectionProtocolRegistration().getCollectionProtocol().setId(
				collectionProtocolRegistration.getCollectionProtocol().getId());
		scg.getCollectionProtocolRegistration().setParticipant(participant);
		try
		{
			scg = (SpecimenCollectionGroup) appService.updateObject(scg);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to update SCG", true);
		}

	}

	/**
	 * Change consent status at specimen level(No,No,No).
	 * All child specimen have consent status as specimen and SCG have status as (Yes,Yes,Yes)
	 */
	public void testVerifyConsentResponseChangeWithSpecimenChange()
	{
		CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
		cp.setShortTitle("cp_SpecimenChange_final");
		try
		{
			cp = (CollectionProtocol) appService.createObject(cp);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to create collection protocol", true);
		}
		SpecimenCollectionGroup scg = createSCGWithConsents(cp);
		System.out.println("created scg with 3 consents having yes response");
		TissueSpecimen ts = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		ts.setSpecimenCollectionGroup(scg);
		ts.setLineage("New");
		ts.setLabel("TisSpec_" + UniqueKeyGeneratorUtil.getUniqueKey());
		System.out.println("Befor creating Tissue Specimen");
		try
		{
			ts = (TissueSpecimen) appService.createObject(ts);
		}
		catch (Exception e)
		{
			assertFalse("Failed to create specimen parent", true);
		}
		TissueSpecimen childSpecimen1 = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		childSpecimen1.setParentSpecimen(ts);
		childSpecimen1.setConsentTierStatusCollection(ts.getConsentTierStatusCollection());
		childSpecimen1.setLabel("TisSpec_child_" + UniqueKeyGeneratorUtil.getUniqueKey());
		childSpecimen1.setLineage("Aliquot");
		childSpecimen1.setSpecimenCollectionGroup(scg);//bug 12073 and 12074
		System.out.println("Befor creating Tissue Specimen");

		try
		{
			childSpecimen1 = (TissueSpecimen) appService.createObject(childSpecimen1);
		}
		catch (Exception e)
		{
			assertFalse("Failed to create specimen child1", true);
		}

		Collection consentTierStatusCollection = ts.getConsentTierStatusCollection();

		Iterator consentTierItr1 = consentTierStatusCollection.iterator();
		ConsentTierStatus c1 = (ConsentTierStatus) consentTierItr1.next();
		c1.setStatus("No");
		ConsentTierStatus c2 = (ConsentTierStatus) consentTierItr1.next();
		c2.setStatus("No");
		ConsentTierStatus c3 = (ConsentTierStatus) consentTierItr1.next();
		c3.setStatus("No");

		ts.setApplyChangesTo("ApplyAll");

		Collection externalIdentifierCollection = new HashSet();
		ExternalIdentifier externalIdentifier = new ExternalIdentifier();
		externalIdentifier.setName("Specimen 1 ext id_");
		externalIdentifier.setValue("12");
		externalIdentifierCollection.add(externalIdentifier);
		ts.setExternalIdentifierCollection(externalIdentifierCollection);
		try
		{
			ts = (TissueSpecimen) appService.updateObject(ts);
			System.out.println("after tissuespecimen update in testVerifyConsentResponseChangeWithSpecimenChange()");
		}
		catch (Exception e)
		{
			System.out.println("SpecimenTestCases.testVerifyConsentResponseChangeWithSpecimenChange()");
			e.printStackTrace();
			assertFalse("Failed to update TissueSpecimen after changing consents " + e.getMessage(), true);
		}

	}

	/**
	 * Create CPR with all consent responses as Yes
	 * Change CPR response to No
	 * All SCG and Specimens have same consent status as CPR.
	 */
	public void testVerifyConsentResponseChangeWithCPRChange()
	{
		CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
		cp.setShortTitle("cp_CPRChange");
		try
		{
			cp = (CollectionProtocol) appService.createObject(cp);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to create collection protocol", true);
		}

		Participant participant = BaseTestCaseUtility.initParticipant();

		try
		{
			participant = (Participant) appService.createObject(participant);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to create collection protocol", true);
		}
		System.out.println("Participant:" + participant.getFirstName());
		CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
		collectionProtocolRegistration.setCollectionProtocol(cp);
		collectionProtocolRegistration.setParticipant(participant);
		collectionProtocolRegistration.setProtocolParticipantIdentifier("");
		collectionProtocolRegistration.setActivityStatus("Active");
		try
		{
			collectionProtocolRegistration.setRegistrationDate(Utility.parseDate("08/15/1975", Utility.datePattern("08/15/1975")));
			collectionProtocolRegistration.setConsentSignatureDate(Utility.parseDate("11/23/2006", Utility.datePattern("11/23/2006")));

		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		collectionProtocolRegistration.setSignedConsentDocumentURL("F:/doc/consentDoc.doc");
		User user = (User) TestCaseUtility.getObjectMap(User.class);
		collectionProtocolRegistration.setConsentWitness(user);

		Collection consentTierResponseCollection = new LinkedHashSet();
		Collection consentTierCollection = new LinkedHashSet();
		consentTierCollection = cp.getConsentTierCollection();
		Iterator consentTierItr = consentTierCollection.iterator();
		while (consentTierItr.hasNext())
		{
			ConsentTier consent = (ConsentTier) consentTierItr.next();
			ConsentTierResponse response = new ConsentTierResponse();
			response.setResponse("Yes");
			response.setConsentTier(consent);
			consentTierResponseCollection.add(response);
		}

		collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);

		System.out.println("Creating CPR");
		try
		{
			collectionProtocolRegistration = (CollectionProtocolRegistration) appService.createObject(collectionProtocolRegistration);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to register participant", true);
		}
		TestCaseUtility.setObjectMap(collectionProtocolRegistration, CollectionProtocolRegistration.class);

		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg = (SpecimenCollectionGroup) BaseTestCaseUtility.createSCG(collectionProtocolRegistration);
		Site site = (Site) TestCaseUtility.getObjectMap(Site.class);
		scg.setSpecimenCollectionSite(site);
		scg.setName("New SCG" + UniqueKeyGeneratorUtil.getUniqueKey());
		scg = (SpecimenCollectionGroup) BaseTestCaseUtility.setEventParameters(scg);
		System.out.println("Creating SCG");
		try
		{
			scg = (SpecimenCollectionGroup) appService.createObject(scg);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to register participant", true);
		}

		TissueSpecimen ts = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		ts.setSpecimenCollectionGroup(scg);
		ts.setLabel("TisSpec" + UniqueKeyGeneratorUtil.getUniqueKey());
		System.out.println("Befor creating Tissue Specimen");

		try
		{
			ts = (TissueSpecimen) appService.createObject(ts);
			System.out.println("after tissuespecimen update in testVerifyConsentResponseChangeWithCPRChange()");
		}
		catch (Exception e)
		{
			System.out.println("TestingTestcases.testVerifyConsentResponseChangeWithCPRChange()");
			assertFalse("Failed to create specimen parent", true);
		}
		Collection consentResponse = collectionProtocolRegistration.getConsentTierResponseCollection();
		Iterator it = consentResponse.iterator();
		while (it.hasNext())
		{
			ConsentTierResponse response = (ConsentTierResponse) it.next();
			response.setResponse("No");
		}
		try
		{
			collectionProtocolRegistration = (CollectionProtocolRegistration) appService.updateObject(collectionProtocolRegistration);
			System.out.println("after collectionProtocolRegistration update in testVerifyConsentResponseChangeWithCPRChange()");

		}
		catch (Exception e)
		{
			System.out.println("TestingTestcases.testVerifyConsentResponseChangeWithCPRChange()");
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to update CollectionProtocolRegistration", true);
		}

	}

	/**
	 * Add consent status at SCG level(Yes,Yes,Yes).
	 * Change consent status at SCG level(Yes,No,Not Specified) with ApplyAll
	 * All Specimens have same consent status as SCG(Yes,No,Not Specified).
	 */
	public void testVerifyConsentResponseChangeWithSCGChangeApplyAll()
	{
		CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
		cp.setShortTitle("cp_SCGChangeApplyAll");
		try
		{
			cp = (CollectionProtocol) appService.createObject(cp);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to create collection protocol", true);
		}
		SpecimenCollectionGroup scg = createSCGWithConsents(cp);
		TissueSpecimen ts = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		ts.setSpecimenCollectionGroup(scg);
		ts.setLabel("TisSpec" + UniqueKeyGeneratorUtil.getUniqueKey());
		System.out.println("Befor creating Tissue Specimen");

		try
		{
			ts = (TissueSpecimen) appService.createObject(ts);
		}
		catch (Exception e)
		{
			assertFalse("Failed to create specimen parent", true);
		}

		TissueSpecimen childSpecimen1 = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		childSpecimen1.setParentSpecimen(ts);
		childSpecimen1.setSpecimenCollectionGroup(scg);
		childSpecimen1.setLineage("Aliquot");
		childSpecimen1.setLabel("TisSpec_child" + UniqueKeyGeneratorUtil.getUniqueKey());
		System.out.println("Befor creating Tissue Specimen");

		try
		{
			childSpecimen1 = (TissueSpecimen) appService.createObject(childSpecimen1);
		}
		catch (Exception e)
		{
			assertFalse("Failed to create specimen child1", true);
		}
		//change scg responsees
		Collection newConStatusCol = new LinkedHashSet();
		Collection consentTierStatusCollection = scg.getConsentTierStatusCollection();

		Iterator conStatusItr = consentTierStatusCollection.iterator();
		ConsentTierStatus consentStatus1 = (ConsentTierStatus) conStatusItr.next();
		consentStatus1.setStatus("Yes");
		newConStatusCol.add(consentStatus1);
		ConsentTierStatus consentStatus2 = (ConsentTierStatus) conStatusItr.next();
		consentStatus2.setStatus("No");
		newConStatusCol.add(consentStatus2);
		ConsentTierStatus consentStatus3 = (ConsentTierStatus) conStatusItr.next();
		consentStatus3.setStatus("Not Specified");
		newConStatusCol.add(consentStatus3);

		scg.setConsentTierStatusCollection(newConStatusCol);
		scg.setApplyChangesTo("ApplyAll");
		scg.getCollectionProtocolRegistration().getCollectionProtocol().setId(
				scg.getCollectionProtocolRegistration().getCollectionProtocol().getId());
		scg.getCollectionProtocolRegistration().setParticipant(
				scg.getCollectionProtocolRegistration().getParticipant());

		try
		{
			scg = (SpecimenCollectionGroup) appService.updateObject(scg);
			System.out.println("after scg update in testVerifyConsentResponseChangeWithSCGChangeApplyAll()");
		}
		catch (Exception e)
		{
			System.out.println("TestingTestcases.testVerifyConsentResponseChangeWithSCGChangeApplyAll()");
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to update SCG", true);
		}

	}

	/**
	 * Add consent status at SCG level(Yes,Yes,Yes).
	 * Change consent status of second child specimen(No,No,No)
	 * Change consent status at SCG level(Yes,No,Not Specified)
	 * Then consent status of specimen and first child specimen - Yes,No,Not Specified
	 * consent status of second child specimen - No,No,No
	 *
	 */
	public void testVerifyConsentResponseChangeWithConflictOption()
	{
		CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
		cp.setShortTitle("cp_ConflictOption");
		try
		{
			cp = (CollectionProtocol) appService.createObject(cp);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to create collection protocol", true);
		}
		SpecimenCollectionGroup scg = createSCGWithConsents(cp);
		TissueSpecimen ts = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		ts.setSpecimenCollectionGroup(scg);
		ts.setLabel("TisSpec_test" + UniqueKeyGeneratorUtil.getUniqueKey());
		System.out.println("Befor creating Tissue Specimen");

		try
		{
			ts = (TissueSpecimen) appService.createObject(ts);
		}
		catch (Exception e)
		{
			assertFalse("Failed to create specimen parent", true);
		}

		TissueSpecimen childSpecimen1 = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		childSpecimen1.setParentSpecimen(ts);
		childSpecimen1.setSpecimenCollectionGroup(scg);
		childSpecimen1.setLineage("Aliquot");
		Double quantity = new Double(2.0);
		childSpecimen1.setInitialQuantity(quantity);
		childSpecimen1.setAvailableQuantity(quantity);
		childSpecimen1.setLabel("TisSpec_child1" + UniqueKeyGeneratorUtil.getUniqueKey());
		System.out.println("Befor creating Tissue Specimen");

		try
		{
			childSpecimen1 = (TissueSpecimen) appService.createObject(childSpecimen1);
		}
		catch (Exception e)
		{
			assertFalse("Failed to create specimen child1", true);
		}

		TissueSpecimen childSpecimen2 = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		childSpecimen2.setParentSpecimen(ts);
		childSpecimen2.setSpecimenCollectionGroup(scg);
		childSpecimen2.setLineage("Aliquot");
		childSpecimen2.setInitialQuantity(quantity);
		childSpecimen2.setAvailableQuantity(quantity);
		childSpecimen2.setLabel("TisSpec_child2" + UniqueKeyGeneratorUtil.getUniqueKey());
		System.out.println("Befor creating Tissue Specimen");

		try
		{
			childSpecimen2 = (TissueSpecimen) appService.createObject(childSpecimen2);
		}
		catch (Exception e)
		{
			assertFalse("Failed to create specimen child2", true);
		}
		Collection consentTierResponseCollectionChild2 = new HashSet();
		Collection consentTierCollectionChild2 = cp.getConsentTierCollection();
		Iterator consentTierItrChild2 = consentTierCollectionChild2.iterator();

		while (consentTierItrChild2.hasNext())
		{
			ConsentTier consentTier = (ConsentTier) consentTierItrChild2.next();
			ConsentTierStatus consentStatus = new ConsentTierStatus();
			consentStatus.setConsentTier(consentTier);
			consentStatus.setStatus("No");
			consentTierResponseCollectionChild2.add(consentStatus);
		}
		childSpecimen2.setConsentTierStatusCollection(consentTierResponseCollectionChild2);

		Collection externalIdentifierCollection = new HashSet();
		ExternalIdentifier externalIdentifier = new ExternalIdentifier();
		externalIdentifier.setName("Specimen 1 ext id");
		externalIdentifier.setValue("11");
		externalIdentifierCollection.add(externalIdentifier);
		childSpecimen2.setExternalIdentifierCollection(externalIdentifierCollection);
		try
		{
			childSpecimen2 = (TissueSpecimen) appService.updateObject(childSpecimen2);
		}
		catch (Exception e)
		{
			assertFalse("Failed to update specimen childSpecimen2" + e.getMessage(), true);
		}
		System.out.println("after child update");
		//change scg responsees
		Collection newConStatusCol = new HashSet();
		Collection consentTierStatusCollection = scg.getConsentTierStatusCollection();

		Iterator conStatusItr = consentTierStatusCollection.iterator();
		ConsentTierStatus consentStatus1 = (ConsentTierStatus) conStatusItr.next();
		consentStatus1.setStatus("Yes");
		newConStatusCol.add(consentStatus1);
		ConsentTierStatus consentStatus2 = (ConsentTierStatus) conStatusItr.next();
		consentStatus2.setStatus("No");
		newConStatusCol.add(consentStatus2);
		ConsentTierStatus consentStatus3 = (ConsentTierStatus) conStatusItr.next();
		consentStatus3.setStatus("Not Specified");
		newConStatusCol.add(consentStatus3);

		scg.setConsentTierStatusCollection(newConStatusCol);
		scg.setApplyChangesTo("Apply");
		scg.getCollectionProtocolRegistration().getCollectionProtocol().setId(
				scg.getCollectionProtocolRegistration().getCollectionProtocol().getId());
		scg.getCollectionProtocolRegistration().setParticipant(
				scg.getCollectionProtocolRegistration().getParticipant());
		try
		{
			scg = (SpecimenCollectionGroup) appService.updateObject(scg);
			System.out.println("after scg update in testVerifyConsentResponseChangeWithConflictOption()");
		}
		catch (Exception e)
		{
			System.out.println("TestingTestcases.testVerifyConsentResponseChangeWithConflictOption() after scg update");
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to update SCG", true);
		}

	}

	/**
	 * Withdraw all cosnents at Registration(CPR).
	 * All SCG and specimen will be disabled.
	 */
	public void testVerifyConsentsWithdrawnWithDiscardOption()
	{
		CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
		cp.setShortTitle("cp_DiscardOption");
		try
		{
			cp = (CollectionProtocol) appService.createObject(cp);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to create collection protocol", true);
		}
		System.out.println("CP:" + cp.getTitle());
		Participant participant = BaseTestCaseUtility.initParticipant();
		try
		{
			participant = (Participant) appService.createObject(participant);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to create collection protocol", true);
		}
		System.out.println("Participant:" + participant.getFirstName());
		CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
		collectionProtocolRegistration.setCollectionProtocol(cp);
		collectionProtocolRegistration.setParticipant(participant);
		collectionProtocolRegistration.setProtocolParticipantIdentifier("");
		collectionProtocolRegistration.setActivityStatus("Active");
		try
		{
			collectionProtocolRegistration.setRegistrationDate(Utility.parseDate("08/15/1975", Utility.datePattern("08/15/1975")));
			collectionProtocolRegistration.setConsentSignatureDate(Utility.parseDate("11/23/2006", Utility.datePattern("11/23/2006")));

		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		collectionProtocolRegistration.setSignedConsentDocumentURL("F:/doc/consentDoc.doc");
		User user = (User) TestCaseUtility.getObjectMap(User.class);
		collectionProtocolRegistration.setConsentWitness(user);

		Collection consentTierResponseCollection = new HashSet();
		Collection consentTierCollection = cp.getConsentTierCollection();
		Iterator consentTierItr = consentTierCollection.iterator();

		while (consentTierItr.hasNext())
		{
			ConsentTier consentTier = (ConsentTier) consentTierItr.next();
			ConsentTierResponse consentResponse = new ConsentTierResponse();
			consentResponse.setConsentTier(consentTier);
			consentResponse.setResponse("Yes");
			consentTierResponseCollection.add(consentResponse);
		}
		collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);
		System.out.println("Creating CPR");
		try
		{
			collectionProtocolRegistration = (CollectionProtocolRegistration) appService.createObject(collectionProtocolRegistration);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to register participant", true);
		}

		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg = (SpecimenCollectionGroup) BaseTestCaseUtility.createSCG(collectionProtocolRegistration);
		Site site = (Site) TestCaseUtility.getObjectMap(Site.class);
		scg.setSpecimenCollectionSite(site);
		scg.setName("New SCG" + UniqueKeyGeneratorUtil.getUniqueKey());
		scg = (SpecimenCollectionGroup) BaseTestCaseUtility.setEventParameters(scg);
		System.out.println("Creating SCG");

		try
		{
			scg = (SpecimenCollectionGroup) appService.createObject(scg);

		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to rcreate SCG", true);
		}

		TissueSpecimen ts = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		ts.setSpecimenCollectionGroup(scg);
		ts.setLabel("TisSpec" + UniqueKeyGeneratorUtil.getUniqueKey());
		System.out.println("Befor creating Tissue Specimen");

		try
		{
			ts = (TissueSpecimen) appService.createObject(ts);
		}
		catch (Exception e)
		{
			assertFalse("Failed to create specimen", true);
		}

		Collection consentTierCollection1 = collectionProtocolRegistration.getConsentTierResponseCollection();
		Iterator consentTierItr1 = consentTierCollection1.iterator();

		ConsentTierResponse c1 = (ConsentTierResponse) consentTierItr1.next();
		c1.setResponse("Withdrawn");
		ConsentTierResponse c2 = (ConsentTierResponse) consentTierItr1.next();
		c2.setResponse("Withdrawn");
		ConsentTierResponse c3 = (ConsentTierResponse) consentTierItr1.next();
		c3.setResponse("Withdrawn");

		collectionProtocolRegistration.setConsentWithdrawalOption("Discard");
		//collectionProtocolRegistration.getCollectionProtocol().setId(collectionProtocolRegistration.getId());
		collectionProtocolRegistration.setParticipant(participant);
		try
		{
			collectionProtocolRegistration = (CollectionProtocolRegistration) appService.updateObject(collectionProtocolRegistration);
			System.out.println("after collectionProtocolRegistration update in testVerifyConsentsWithdrawnWithDiscardOption()");

		}
		catch (Exception e)
		{
			System.out.println("TestingTestcases.testVerifyConsentsWithdrawnWithDiscardOption()");
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to update CollectionProtocolRegistration", true);
		}

	}

	/**
	 * Withdraw all cosnents at SCG level.
	 * All SCG and specimen will be disabled.
	 */
	public void testVerifyConsentsWithdrawnWithDiscardOptionAtSCG()
	{
		CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
		cp.setShortTitle("cp_DiscardOptionAtSCG");
		try
		{
			cp = (CollectionProtocol) appService.createObject(cp);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to create collection protocol", true);
		}
		System.out.println("CP:" + cp.getTitle());
		SpecimenCollectionGroup scg = createSCGWithConsents(cp);

		TissueSpecimen ts = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		ts.setSpecimenCollectionGroup(scg);
		ts.setLabel("TisSpec" + UniqueKeyGeneratorUtil.getUniqueKey());
		System.out.println("Befor creating Tissue Specimen");

		try
		{
			ts = (TissueSpecimen) appService.createObject(ts);
		}
		catch (Exception e)
		{
			assertFalse("Failed to create specimen", true);
		}

		Collection newConStatusCol = new HashSet();
		Collection consentTierStatusCollection = scg.getConsentTierStatusCollection();

		Iterator conStatusItr = consentTierStatusCollection.iterator();
		ConsentTierStatus consentStatus1 = (ConsentTierStatus) conStatusItr.next();
		consentStatus1.setStatus("Withdrawn");
		newConStatusCol.add(consentStatus1);
		ConsentTierStatus consentStatus2 = (ConsentTierStatus) conStatusItr.next();
		consentStatus2.setStatus("Withdrawn");
		newConStatusCol.add(consentStatus2);
		ConsentTierStatus consentStatus3 = (ConsentTierStatus) conStatusItr.next();
		consentStatus3.setStatus("Withdrawn");
		newConStatusCol.add(consentStatus3);

		scg.setConsentTierStatusCollection(newConStatusCol);

		scg.setConsentTierStatusCollection(newConStatusCol);
		scg.setConsentWithdrawalOption("Discard");
		scg.getCollectionProtocolRegistration().getCollectionProtocol().setId(
				scg.getCollectionProtocolRegistration().getCollectionProtocol().getId());
		scg.getCollectionProtocolRegistration().setParticipant(
				scg.getCollectionProtocolRegistration().getParticipant());
		try
		{
			scg = (SpecimenCollectionGroup) appService.updateObject(scg);
			System.out.println("after scg update in testVerifyConsentsWithdrawnWithDiscardOptionAtSCG()");
		}
		catch (Exception e)
		{
			System.out.println("TestingTestcases.testVerifyConsentsWithdrawnWithDiscardOptionAtSCG()");
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to update SCG", true);
		}
	}

	/**
	 * Test to Check Label Of Child A Specimens.
	 */
	public void testCheckLabelOfChildASpecimens()
	{
		try
		{
			TissueSpecimen specimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);

			//SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
			//scg.setId(new Long(36));
			System.out.println("SpecimenTestCases.testAddTissueSpecimen(): " + scg);
			specimenObj.setSpecimenCollectionGroup(scg);
			specimenObj.setCollectionStatus("Collected");
			Logger.out.info("Inserting domain object------->" + specimenObj);
			System.out.println("Before Creating Tissue Specimen");
			specimenObj.setLabel(null);
			specimenObj = (TissueSpecimen) appService.createObject(specimenObj);

			TissueSpecimen childSpecimen1 = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			childSpecimen1.setParentSpecimen(specimenObj);
			childSpecimen1.setLabel(null);
			childSpecimen1.setLineage("Aliquot");
			childSpecimen1.setSpecimenCollectionGroup(scg);//bug 12073 and 12074
			System.out.println("Befor creating Tissue Specimen");

			try
			{
				childSpecimen1 = (TissueSpecimen) appService.createObject(childSpecimen1);
			}
			catch (Exception e)
			{
				assertFalse("Failed to create specimen child1", true);
			}

			TissueSpecimen childSpecimen2 = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			childSpecimen2.setParentSpecimen(specimenObj);
			childSpecimen2.setLabel(null);
			childSpecimen2.setLineage("Aliquot");
			childSpecimen2.setSpecimenCollectionGroup(scg);//bug 12073 and 12074
			System.out.println("Befor creating Tissue Specimen");

			try
			{
				childSpecimen2 = (TissueSpecimen) appService.createObject(childSpecimen2);
			}
			catch (Exception e)
			{
				assertFalse("Failed to create specimen child1", true);
			}

			System.out.println("Parent label" + specimenObj.getLabel());
			System.out.println("Child Specimen label" + childSpecimen1.getLabel());
			System.out.println("Child Specimen label" + childSpecimen2.getLabel());
			if (childSpecimen1.getLabel().equals(specimenObj.getLabel() + "_1") && childSpecimen2.getLabel().equals(specimenObj.getLabel() + "_2"))
			{
				assertTrue(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel(), true);
			}
		}
		catch (Exception e)
		{
			System.out.println("Exception thrown");
			System.out.println(e);
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	}

	/**
	 * Test to Add Child Specimen On Searched Parent.
	 */
	public void testAddChildSpecimenOnSearchedParent()
	{
		try
		{
			TissueSpecimen specimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);

			//SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
			//scg.setId(new Long(36));

			specimenObj.setSpecimenCollectionGroup(scg);
			specimenObj.setCollectionStatus("Collected");
			specimenObj = (TissueSpecimen) appService.createObject(specimenObj);

			TissueSpecimen childSpecimen1 = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			childSpecimen1.setParentSpecimen(specimenObj);
			childSpecimen1.setLineage("Aliquot");
			childSpecimen1.setSpecimenCollectionGroup(scg);//bug 12073 and 12074
			childSpecimen1 = (TissueSpecimen) appService.createObject(childSpecimen1);
			System.out.println("Child label is " + childSpecimen1.getLabel());
		}

		catch (Exception e)
		{
			System.out.println(e.getMessage());
			assertFalse("Failed to create specimen child1", true);
		}

	}

	/**
	 * Test to Change Specimen Activity Status From Active To Closed.
	 */
	public void testChangeSpecimenActivityStatusFromActiveToClosed()
	{
		try
		{
			TissueSpecimen specimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
			System.out.println("SpecimenTestCases.testAddTissueSpecimen(): " + scg);
			specimenObj.setSpecimenCollectionGroup(scg);
			Logger.out.info("Inserting domain object------->" + specimenObj);
			System.out.println("Before Creating Tissue Specimen");
			specimenObj.setCollectionStatus("Collected");
			specimenObj = (TissueSpecimen) appService.createObject(specimenObj);
			specimenObj.setActivityStatus("Closed");
			specimenObj = (TissueSpecimen) appService.updateObject(specimenObj);
			assertTrue("Domain Object is successfully updated. " + "Activity status updated From Active To Closed." + specimenObj.getLabel(), true);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			assertFalse("Failed to update activity status from active to closed of specimen", true);
		}
	}

	/**
	 * Test to Change Specimen Activity Status From Closed To Active.
	 */
	public void testChangeSpecimenActivityStatusFromClosedToActive()
	{
		try
		{
			TissueSpecimen specimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
			System.out.println("SpecimenTestCases.testAddTissueSpecimen(): " + scg);
			specimenObj.setSpecimenCollectionGroup(scg);
			Logger.out.info("Inserting domain object------->" + specimenObj);
			System.out.println("Before Creating Tissue Specimen");
			specimenObj.setCollectionStatus("Collected");
			specimenObj = (TissueSpecimen) appService.createObject(specimenObj);
			specimenObj.setActivityStatus("Closed");
			specimenObj = (TissueSpecimen) appService.updateObject(specimenObj);
			specimenObj.setActivityStatus("Active");
			specimenObj = (TissueSpecimen) appService.updateObject(specimenObj);
			assertTrue("Domain Object is successfully updated. " + "Activity status updated From Closed To Active." + specimenObj.getLabel(), true);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			assertFalse("Failed to update activity status from closed to active of specimen", true);
		}
	}
	
	 public void testEditSiteUserCPAssociation() {
		Logger.out.info("updating domain object site ------->");
		try {
			ExcelTestCaseUtility.shiftSpecimenInSCG();
			assertTrue("Domain object successfully updated ---->", true);
		} catch (Exception e) {
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			assertFalse("failed to update Object", true);
		}
	}
}
