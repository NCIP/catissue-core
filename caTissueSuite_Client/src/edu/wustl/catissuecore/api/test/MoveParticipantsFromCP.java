package edu.wustl.catissuecore.api.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.SpecimenRequirementBean;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Status;
import gov.nih.nci.common.util.HQLCriteria;

public class MoveParticipantsFromCP extends CaTissueBaseTestCase{
	
	private String oldCps[] = {"MDCP", "HASD", "ACS","ADRC"};
	
	

	public CollectionProtocol getCP(String cpShortTitle) throws Exception {

		CollectionProtocol collectionProtocol = new CollectionProtocol();
		collectionProtocol.setShortTitle(cpShortTitle);
		System.out.println("cpShortTitle " + cpShortTitle);
		List<?> resultList1 = null;
		try {
			resultList1 = appService.search(CollectionProtocol.class,
					collectionProtocol);
			System.out.println("No of CP retrived from DB "
					+ resultList1.size());
			if (resultList1.size() > 0) {
				collectionProtocol = (CollectionProtocol) resultList1.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return collectionProtocol;
	}


	public void transferToCp() throws Exception {
		CollectionProtocol newCp = getCP("cp_mdcp");
		for (int i = 0; i < 1; i++) {
			CollectionProtocol oldCp = getCP(oldCps[i]);
			//Collection cprColl = oldCp.getCollectionProtocolRegistrationCollection();
			//System.out.println("cpr in oldCp "+ cprColl.size());
			getAllCPRHavingOneSCG(newCp, oldCp);
			/*Iterator cprResultIterator = cprColl.iterator();
			while (cprResultIterator.hasNext()) {
				CollectionProtocolRegistration cpr = (CollectionProtocolRegistration) cprResultIterator
						.next();
				cpr.setCollectionProtocol(newCp);
				CollectionProtocolRegistration updatedCpr = (CollectionProtocolRegistration)appService.updateObject(cpr);
				System.out.println("cpr updated ");
				break; 
				// System.out.println("CollectionProtocolEvent "+
				// collectionProtocolRegistration
				// .getSpecimenCollectionGroupCollection().size());
			}*/
			
		}
		
	}
	
	
	public void getAllCPRHavingOneSCG(CollectionProtocol newCp, CollectionProtocol oldCp)
		throws Exception {
	try {
		CollectionProtocol cp = new CollectionProtocol();
		System.out.println("oldCp id "+ oldCp.getId());
		System.out.println("newCp id "+ newCp.getId());
		cp.setId(oldCp.getId());
		CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
		collectionProtocolRegistration.setCollectionProtocol(cp);
		
		List cprResultList = appService.search(
				CollectionProtocolRegistration.class,
				collectionProtocolRegistration);
		Iterator cprResultIterator = cprResultList.iterator();
		System.out.println("No of CPR retrived " + cprResultList.size());
		while (cprResultIterator.hasNext()) {
			collectionProtocolRegistration = (CollectionProtocolRegistration) cprResultIterator
					.next();
			System.out.println("cpr id "+collectionProtocolRegistration.getId() );
			System.out.println("participant name "+ collectionProtocolRegistration.getParticipant().getLastName() + " "+ collectionProtocolRegistration.getParticipant().getFirstName());
			collectionProtocolRegistration.setConsentTierResponseCollection(null);
			collectionProtocolRegistration.setCollectionProtocol(newCp);
			System.out.println("updating ");
			//CollectionProtocolRegistration updatedCpr = (CollectionProtocolRegistration)appService.updateObject(collectionProtocolRegistration);
			System.out.println("cpr updated ");
			break;
			
		
			
		}
	} catch (Exception ex) {
		ex.printStackTrace();
		throw ex;
	}

}
}
