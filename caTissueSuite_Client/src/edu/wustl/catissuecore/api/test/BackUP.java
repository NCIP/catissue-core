package edu.wustl.catissuecore.api.test;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import gov.nih.nci.common.util.HQLCriteria;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

public class BackUP  extends CaTissueBaseTestCase {
	
	
	/*public  void createCP() throws Exception {
		System.out.println("---------IN MoveParticipantsFromCP.createCP-----------");
		CollectionProtocol oldCp = getCP("MDCP");
		CollectionProtocol collectionProtocol = initCollectionProtocol(oldCp);
		collectionProtocol.setShortTitle("22test");
		collectionProtocol.setTitle("22test");
		collectionProtocol = (CollectionProtocol) appService.createObject(collectionProtocol);
		System.out.println("Collection Protocol created "+ collectionProtocol.getShortTitle());
	}
	
	public  CollectionProtocol initCollectionProtocol(CollectionProtocol oldcp) {
		CollectionProtocol collectionProtocol = new CollectionProtocol();
		collectionProtocol.setConsentTierCollection(oldcp.getConsentTierCollection());
		collectionProtocol.setAliquotInSameContainer(oldcp.getAliquotInSameContainer());
		collectionProtocol.setDescriptionURL(oldcp.getDescriptionURL());
		collectionProtocol.setActivityStatus(oldcp.getActivityStatus());
		collectionProtocol.setEndDate(oldcp.getEndDate());
		collectionProtocol.setEnrollment(oldcp.getEnrollment());
		collectionProtocol.setIrbIdentifier(oldcp.getIrbIdentifier());
		collectionProtocol.setEnrollment(oldcp.getEnrollment());

		try {
			collectionProtocol.setStartDate(oldcp.getStartDate());

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		if (oldcp.getId() != null) {
			Collection specimenCollection = new LinkedHashSet<SpecimenRequirement>();
			System.out.println("CollectionProtocolEvent size "
					+ oldcp.getCollectionProtocolEventCollection().size());
			Iterator<CollectionProtocolEvent> cpeItr = oldcp
					.getCollectionProtocolEventCollection().iterator();
			while (cpeItr.hasNext()) {
				CollectionProtocolEvent oldCpe = (CollectionProtocolEvent) cpeItr
						.next();
				System.out.println("CollectionProtocolEvent "+ oldCpe.getCollectionPointLabel());
				if(oldCpe.getCollectionPointLabel().equals("Lumber Puncture" )) {
					System.out.println("Lumber Puncture");
					CollectionProtocolEvent newCpe = new CollectionProtocolEvent();
					setCollectionProtocolEvent(oldCpe, newCpe);
					Collection collectionProtocolEventList = new LinkedHashSet();
					collectionProtocolEventList.add(newCpe);
					collectionProtocol.setCollectionProtocolEventCollection(collectionProtocolEventList); 
				}
			}
		} 
		collectionProtocol.setPrincipalInvestigator(oldcp.getPrincipalInvestigator());
		collectionProtocol.setCoordinatorCollection(oldcp.getCoordinatorCollection());

		return collectionProtocol;
	}

	public static void setCollectionProtocolEvent(CollectionProtocolEvent oldCpe, CollectionProtocolEvent newCpe) {
		
		newCpe.setCollectionPointLabel(oldCpe.getCollectionPointLabel());
		newCpe.setStudyCalendarEventPoint(oldCpe.getStudyCalendarEventPoint());
		newCpe.setClinicalStatus(oldCpe.getClinicalStatus());
		newCpe.setActivityStatus(oldCpe.getActivityStatus());
		newCpe.setClinicalDiagnosis(oldCpe.getClinicalDiagnosis());
		Collection specimenCollection = new LinkedHashSet<SpecimenRequirement>();
		try {
			String query = null;
			HQLCriteria hqlcri = null;
			System.out.println("oldCpe.getId() "+ oldCpe.getId());
			query = "select specimenRequirement from edu.wustl.catissuecore.domain.SpecimenRequirement"
					+ " as specimenRequirement where "
					+ " specimenRequirement.collectionProtocolEvent.id= " + oldCpe.getId();
			hqlcri = new HQLCriteria(query);
			List sreqList = appService.query(hqlcri,
					CollectionProtocolEvent.class.getName());
			if (sreqList != null) {
				System.out.println("sreqList.size()"+ sreqList.size() );
				Iterator<SpecimenRequirement> sreqItr =	sreqList.iterator();
				while(sreqItr.hasNext()) {
					SpecimenRequirement sreq = (SpecimenRequirement)sreqItr.next();
					System.out.println("sreq "+ sreq.getLineage());
					sreq.setId(null);
					specimenCollection.add(sreq);
				}
				System.out.println("specimenCollection "+ specimenCollection.size());
				newCpe.setSpecimenRequirementCollection(specimenCollection);
				System.out.println("newCpe created ");
			}
		} catch (Exception ex) {
			System.out.println("Exception in getCpe");
			System.err.println("Exception in getCpe");
			ex.printStackTrace();
			//throw ex;
		}
		/*CollectionProtocolEventBean cpEventBean = new CollectionProtocolEventBean();
		SpecimenRequirementBean specimenRequirementBean = createSpecimenBean();
		cpEventBean.addSpecimenRequirementBean(specimenRequirementBean);
		Map specimenMap = (Map) cpEventBean.getSpecimenRequirementbeanMap();
		if (specimenMap != null && !specimenMap.isEmpty()) {
			specimenCollection = edu.wustl.catissuecore.util.CollectionProtocolUtil
					.getReqSpecimens(specimenMap.values(), null,
							newCpe);
		}
		newCpe.setSpecimenRequirementCollection(specimenCollection);*/
//	}

}
