/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.api.testcases;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArrayContent;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.system.applicationservice.ApplicationException;

public class ScientistAsNonPIorPCTestCases extends AbstractCaCoreApiTestCasesWithRegularAuthentication {

	public ScientistAsNonPIorPCTestCases() {
		loginName = PropertiesLoader.getNonPIPCScientistUsername();
		password = PropertiesLoader.getNonPIPCScientistPassword();
	}

	public void testBulkFluidSpecimen() {
		DetachedCriteria criteria = DetachedCriteria.forClass(FluidSpecimen.class);
		criteria.add(Property.forName("id").isNotNull());

		List<Object> result = null;
		try {
			result = getApplicationService().query(criteria);

			boolean failed = false;
			for (Object o : result) {
				if (!(o instanceof FluidSpecimen)) {
					failed = true;
					break;
				}
			}

			if (failed || result.size() > 2697) {
				assertFalse(
						"Failed to retrieve Fuild Specimens for condition of id not null",
						true);
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
			assertFalse(
					"Failed to retrieve Fuild Specimens for condition of id not null",
					true);
		}
	}

	public void testGetParticipantForCP() {
		List<Object> result = null;
		try {
			result = getApplicationService().query(
					CqlUtility.getParticipantsForCP(PropertiesLoader
							.getCPTitleOfParticipantForNonPIPCScientist()));

			for (Object o : result) {
				Participant p = (Participant) o;
				if ((p.getFirstName() == null || p.getFirstName().isEmpty())
						&& (p.getLastName() == null || p.getLastName()
								.isEmpty())
						&& (p.getMiddleName() == null || p.getMiddleName()
								.isEmpty())
						&& (p.getSocialSecurityNumber() == null || p
								.getSocialSecurityNumber().isEmpty())
						&& p.getBirthDate() == null && p.getDeathDate() == null) {
				} else {
					assertFalse(
							"Failed to filter the PHI data of Participants",
							true);
					break;
				}
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
			assertFalse("Failed to retrieve Participants", true);
		} catch (ClassCastException e) {
			assertFalse("Failed to retrieve Participants", true);
		}
	}

	public void testSpecimenMalePPI() {
		List<Object> result = null;
		String ppi = PropertiesLoader.getPPIForSpecimen();
		try {
			result = getApplicationService().query(
					CqlUtility.getSpecimenMalePPI(ppi));

			for (Object o : result) {
				if (o instanceof Specimen) {
					Specimen specimen = (Specimen) o;
					SpecimenCollectionGroup scg = specimen
							.getSpecimenCollectionGroup();
					CollectionProtocolRegistration cpr = scg
							.getCollectionProtocolRegistration();
					if (!ppi.equals(cpr.getProtocolParticipantIdentifier())
							|| specimen.getCreatedOn() != null) {
						assertFalse(
								"Failed to retrieve Specimens of males with CPR having PPI value as "
										+ ppi + " having masked PHI data.",
								true);
						break;
					}
				}
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
			assertFalse(
					"Failed to retrieve Specimens of males with CPR having PPI value as "
							+ ppi + " having masked PHI data.", true);
		}
	}
	
	 public void testSearchSpecimenCollectionGroupWithScientist()
	    {
	    	SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
	    	scg.setId(2l);
	    	try{
	        scg = searchById(SpecimenCollectionGroup.class,scg);
	        System.out.println(scg);

	    	SpecimenCollectionGroup scga = new SpecimenCollectionGroup();
	    	String barcode = scg.getBarcode();
	    	scga.setBarcode(barcode);
	    	
	    	List<SpecimenCollectionGroup> spList = searchByExample(SpecimenCollectionGroup.class, scga);
	    	if(spList != null)
	    	{
	    		assertEquals(1, spList.size());
	    		for (SpecimenCollectionGroup specimen2 : spList) 
	    		{
	    			assertEquals(barcode, specimen2.getBarcode());
				}
	    		
	    	}
	    	else
	    		assertFalse(
						"Failed to retrieve SCG with barcode : "+barcode,
						true);
	    	} catch (ApplicationException e) {
				e.printStackTrace();
				assertFalse(
						"Failed to retrieve Fuild Specimens for condition of id not null",
						true);
			}
	    	
	    }
	 
	 public void testSearchMolecularSpecimenWithScientistLogin()
	    {
	    	DetachedCriteria criteria = DetachedCriteria.forClass(MolecularSpecimen.class);
			criteria.add(Property.forName("id").isNotNull());

			List<Object> result = null;
			try {
				result = getApplicationService().query(criteria);

				boolean failed = false;
				for (Object o : result) {
					if (!(o instanceof MolecularSpecimen)) {
						failed = true;
						break;
					}
				}

				if (failed || result.size() < 1) {
					assertFalse(
							"Failed to retrieve MolecularSpecimen for condition of id not null",
							true);
				}
			} catch (ApplicationException e) {
				e.printStackTrace();
				assertFalse(
						"Failed to retrieve MolecularSpecimen for condition of id not null",
						true);
			}
	    	
	    }
	 
	 public void testSearchCellSpecimenWithScientistLogin()
	    {
	    	DetachedCriteria criteria = DetachedCriteria.forClass(CellSpecimen.class);
			criteria.add(Property.forName("id").isNotNull());

			List<Object> result = null;
			try {
				result = getApplicationService().query(criteria);

				boolean failed = false;
				for (Object o : result) {
					if (!(o instanceof CellSpecimen)) {
						failed = true;
						break;
					}
				}

				if (failed || result.size() < 1) {
					assertFalse(
							"Failed to retrieve CellSpecimen  for condition of id not null",
							true);
				}
			} catch (ApplicationException e) {
				e.printStackTrace();
				assertFalse(
						"Failed to retrieve CellSpecimen  for condition of id not null",
						true);
			}
	    	
	    }
	 
	 public void testSearchFluidSpecimenWithScientistLogin()
	    {
	    	DetachedCriteria criteria = DetachedCriteria.forClass(FluidSpecimen.class);
			criteria.add(Property.forName("id").isNotNull());

			List<Object> result = null;
			try {
				result = getApplicationService().query(criteria);

				boolean failed = false;
				for (Object o : result) {
					if (!(o instanceof FluidSpecimen)) {
						failed = true;
						break;
					}
				}

				if (failed || result.size() < 1) {
					assertFalse(
							"Failed to retrieve FluidSpecimen  for condition of id not null",
							true);
				}
			} catch (ApplicationException e) {
				e.printStackTrace();
				assertFalse(
						"Failed to retrieve FluidSpecimen  for condition of id not null",
						true);
			}
	    	
	    }
	 
	 public void testSearchTissueSpecimenWithScientistLogin()
	    {
	    	DetachedCriteria criteria = DetachedCriteria.forClass(TissueSpecimen.class);
			criteria.add(Property.forName("id").isNotNull());

			List<Object> result = null;
			try {
				result = getApplicationService().query(criteria);

				boolean failed = false;
				for (Object o : result) {
					if (!(o instanceof TissueSpecimen)) {
						failed = true;
						break;
					}
				}

				if (failed || result.size() < 1) {
					assertFalse(
							"Failed to retrieve TissueSpecimen  for condition of id not null",
							true);
				}
			} catch (ApplicationException e) {
				e.printStackTrace();
				assertFalse(
						"Failed to retrieve TissueSpecimen  for condition of id not null",
						true);
			}
	    	
	    }
	 

		public void testParticipantWithScientistLogin() throws ApplicationException {
			Participant participant = new Participant();
			participant.setFirstName("John");

			List<Participant> result = null;
			try {
				log.info("searching domain object");
				result = searchByExample(Participant.class, participant);
				log.info("result  "+result);
				if(result!=null && result.size()>0)
				{
					log.info("Scientist able to search participant by name");
					throw new ApplicationException();
				}
			} catch (ApplicationException e) {
				e.printStackTrace();
				assertFalse("Scientist able to search participants", true);
			}
			
		}
		
		public void testParticipantScientistLoginForGivenPMI() throws ApplicationException {
			Participant participant = new Participant();
			ParticipantMedicalIdentifier pmi=new ParticipantMedicalIdentifier();
			pmi.setMedicalRecordNumber("123");
			
			Site site=new Site();
			site.setName("In Transit");
			pmi.setSite(site);
			Collection<ParticipantMedicalIdentifier> pmiCollection =new HashSet<ParticipantMedicalIdentifier>();
			pmiCollection.add(pmi);
			participant.setParticipantMedicalIdentifierCollection(pmiCollection);

			List<Participant> result = null;
			try {
				log.info("searching domain object");
				result = searchByExample(Participant.class, participant);
				if(result!=null && result.size()>0)
				{
					log.info("Scientist able to search participant by given PMI");
					throw new ApplicationException();
				}
			} catch (ApplicationException e) {
				e.printStackTrace();
				assertFalse("Scientist able to search participants", true);
			}
		}
		
		public void testSpecimenArrayContentWithScientistLogin()
		{
			try
			{
				SpecimenArrayContent sac = new SpecimenArrayContent();
				sac.setId(new Long(1));
				List sacCollection = searchByExample(SpecimenArrayContent.class,sac);
				System.out.println("Total SpecimenArrayContent Count:"+sacCollection.size());
				Iterator itr = sacCollection.iterator();
				while(itr.hasNext())
				{
					SpecimenArrayContent spe = (SpecimenArrayContent)itr.next();
					if(spe.getSpecimen().getCreatedOn()!=null)
					{
						fail("SpecimenArrayContent ->Specimen PHI data is visible to scientist");
					}
					System.out.println("SpecimenArrayContent->Specimen Created on :"+spe.getSpecimen().getCreatedOn());
				}
			}
			 catch(Exception e)
			 {
				 System.out
						.println("ScientistRoleTestCases.testSpecimenArrayContentWithScientistLogin()"+e.getMessage());
				 e.printStackTrace();
				 assertFalse("Test failed. to search SpecimenArrayContent", true);
			 }
		}
		
		/**
		  * Search  CPR and check if PHI data is visible
		  *
		  */
		  public void testSearchProtocolRegistrationWithScientistLogin()
		  {
			try
			{
				CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
				cpr.setId(new Long(1));
				List cprList = searchByExample(CollectionProtocolRegistration.class, cpr);
				System.out.println("Size : "+cprList.size());
				for(int i=0;i<cprList.size();i++)
				{
					CollectionProtocolRegistration returnedReg = (CollectionProtocolRegistration)cprList.get(i);
					if(returnedReg.getRegistrationDate()!=null||returnedReg.getSignedConsentDocumentURL()!=null||
							returnedReg.getConsentSignatureDate()!=null)
					{
						fail("CollectionProtocolRegistration PHI data is visible to scientist");
					}
					 System.out.println("CPR->RegistrationDate :"+ returnedReg.getRegistrationDate());
				     System.out.println("CPR->ConsentSignatureDate :"+ returnedReg.getConsentSignatureDate());
				     System.out.println("CPR->SignedConsentDocumentURL:"+ returnedReg.getSignedConsentDocumentURL());
				}
			 }
			 catch(Exception e){
				 System.out
						.println("ScientistRoleTestCases.testSearchProtocolRegistrationWithScientistLogin() "+e.getMessage());
			     Logger.out.error(e.getMessage(),e);
				 e.printStackTrace();
				 assertFalse("Test failed. to search SpecimenCollectionGroup", true);
			 }
		  }
}
