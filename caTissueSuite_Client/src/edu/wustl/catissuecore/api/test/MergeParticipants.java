package edu.wustl.catissuecore.api.test;

import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.Status;
import edu.wustl.dao.JDBCDAO;
import gov.nih.nci.common.util.HQLCriteria;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.system.comm.client.ClientSession;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.StringTokenizer;

public class MergeParticipants
{
	static ApplicationService appService = null;
	
	public void mergePart(String fileName) throws Exception {
		 
	      System.out.println("In mergePart");
		  Long partId1 = null;
		  Long partId2 = null;
		  File file = new File(fileName);
		  BufferedReader bufRdr = new BufferedReader(new FileReader(file));
		  String line = null;
		  String [][] partInfo = new String [70][10];
		 
		  int row = 0;
		  
		  bufRdr.readLine();
		  while((line = bufRdr.readLine()) != null)
		  { 
			System.out.println("line  "+line);
			try {
				 StringTokenizer st = new StringTokenizer(line,",");
				 int col = 0;
				 while (st.hasMoreTokens())
				 {
					 partInfo[row][col] = st.nextToken();
					 col++;
				 }
			//	 System.out.println("part Id "+ partInfo[row][0] + " "+ partInfo[row][1]);
				 partId1 = Long.parseLong(partInfo[row][0]);
				 partId2 = Long.parseLong(partInfo[row][1]);
				 System.out.println("part Id "+ partId1 + " "+ partId2);
				 compareDetails(partId1, partId2);
				 row++;
			}catch (Exception e) {
				System.out.println("Exception in while ");
				 System.out.println("part Id "+ partInfo[row][0] + " "+ partInfo[row][1]);
				e.printStackTrace();
			}
		  }
			 //close the file
			 bufRdr.close();
		}

	 public void compareDetails(Long partId1, Long partId2) throws Exception {
		
		List detailsList1 =  getDeatils(partId1);
		List detailsList2 =  getDeatils(partId2);		
		System.out.println("scgList1 "+ detailsList1.size());
		System.out.println("scgList2 "+ detailsList2.size());
		Long scgId2 = null;
		Long cprId2 = null;
		if(detailsList1.size() > 0 && detailsList2.size() > 0) {
			Iterator detailsItr2 = detailsList2.iterator();
			while (detailsItr2.hasNext())
			{
				Object[] scgObj2 = (Object[])detailsItr2.next();
				 cprId2 = (Long)scgObj2[0];
				Long cpeId2 = (Long)scgObj2[1];
				String collStatus2 = scgObj2[2].toString();
				String collDate2 = scgObj2[3].toString();
				String ppi2 = scgObj2[4].toString();
				 scgId2 = (Long)scgObj2[5];
				Long siteId2 = (Long)scgObj2[6];
				
				System.out.println("cprId2 "+ cprId2 + " cpeId2 "+ cpeId2 + " collStatus2 "+  collStatus2 + " collDate2 "+ collDate2 + " ppi2 "+ ppi2);
				Iterator detailsItr1 = detailsList1.iterator();
				while (detailsItr1.hasNext())
				{
					Object[] scgObj1 = (Object[])detailsItr1.next();
					Long cprId1 = (Long)scgObj1[0];
					Long cpeId1 = (Long)scgObj1[1];
					String collStatus1 = scgObj1[2].toString();
					String collDate1 = scgObj1[3].toString();
					String ppi1 = scgObj1[4].toString();
					Long scgId1 = (Long)scgObj1[5];
					Long siteId1 = (Long)scgObj1[6];
					
					System.out.println("cprId1 "+ cprId1 + " cpeId1 "+ cpeId1+ " collStatus1 "+  collStatus1 +" collDate1 "+ collDate1 );				
					if(cpeId2.equals(cpeId1)){
						
						System.out.println("Same eve id");					
						if(collStatus2.equals("Complete") && collStatus1.equals("Complete")) {
							
							System.out.println("both are collected");
							if(collDate2.equals(collDate1)) {
								
								System.out.println("same colldate them move specimen in scg1");
								moveSpecimen(scgId2, scgId1);
								System.out.println("Specimens moved successfully");
								
								
							} else {
								System.out.println("different coll date move scg2 in cpr2");
								moveSCG(scgId2, cprId1);
								System.out.println("SCG moved successfully");
							}
						}
					} else {
						System.out.println("different eve");
						if(collStatus2.equals("Complete")){
							
							System.out.println("different eve2 so move scg2 in cpr1");
							moveSCG(scgId2, cprId1);
							System.out.println("SCG moved successfully");
							
						}
					}
				}
			}
			 disableParticipant(partId2, cprId2, scgId2);
		} else {
			System.out.println("Either of participant details are not found ");
		}
	 }

	 public void moveSpecimen(Long scgId2, Long scgId1) throws Exception {
//		 try {
//				String query = null;
//				HQLCriteria hqlcri = null;				
//				query = "update edu.wustl.catissuecore.domain.Specimen as specimen" +
//						" set specimen.specimenCollectionGroup.id = " + scgId1 +
//						" where specimen.specimenCollectionGroup.id = "+ scgId2;
//				
//				hqlcri = new HQLCriteria(query);
//				List list  = appService.query(hqlcri, SpecimenCollectionGroup.class.getName());  	
//				System.out.println("number of specimens updated "+ list.size());		 
//			}catch (Exception ex){
//			ex.printStackTrace();
//			}
//		 
		List<Specimen> speList = getSpecimenTomove(scgId2);
		if (speList != null){
			Iterator<Specimen> speItr = speList.iterator();
			while (speItr.hasNext())
			{
				Specimen specimen = (Specimen) speItr.next();
				updateSpecimen(specimen, scgId1 );
				
			}
		}
	}
	 public void updateSpecimen(Specimen specimen, Long scgId) {
		 
		 SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		 scg.setId(scgId);
		 specimen.setSpecimenCollectionGroup(scg);
		 try{
			appService.updateObject(specimen);
			}catch (Exception e) {
				System.out.println("exception in update specimen");
				e.printStackTrace();
			}
	 }
	 
	 public List<Specimen> getSpecimenTomove(Long scgId) throws Exception{
		 
		 List<Specimen> specList = null;
		 try {
			String query = null;
			HQLCriteria hqlcri = null;				
			query = "select specimen from edu.wustl.catissuecore.domain.Specimen as specimen" +					
					" where specimen.specimenCollectionGroup.id = "+ scgId;
			
			hqlcri = new HQLCriteria(query);
			specList  = appService.query(hqlcri, Specimen.class.getName());  	
			System.out.println("number of specimens to be updated "+ specList.size());		 
		}catch (Exception ex){
		ex.printStackTrace();
		}
		return specList;
	 }
	 
	 
	 public void moveSCG(Long scgId, Long cprId) throws Exception{
			
			SpecimenCollectionGroup scg = getSCG(scgId);//			
//			System.out.println(scg.getCollectionProtocolRegistration().getId());
//			System.out.println(scg.getCollectionProtocolEvent().getId());
//			System.out.println(scg.getCollectionStatus());
//			Iterator eventParamItr = scg.getSpecimenEventParametersCollection().iterator();
//			while(eventParamItr.hasNext()){
//				SpecimenEventParameters specEveparam = (SpecimenEventParameters)eventParamItr.next();
//				if(specEveparam instanceof CollectionEventParameters) {
//					System.out.println(specEveparam.getTimestamp());
//				}
//			}
			System.out.println("in moveScg ");
			scg.setId(scgId);
			CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
			cpr.setId(cprId);								
			scg.setConsentTierStatusCollection(null);
			scg.setCollectionProtocolRegistration(cpr);			
			appService.updateObject(scg);
			System.out.println(" Scg moved  ");
	 }
	 
	 public SpecimenCollectionGroup getSCG(long scgId){
		 
		 SpecimenCollectionGroup scg = null;	
		 try {
				String query = null;
				HQLCriteria hqlcri = null;				
				query = "select specimenCollectionGroup " +
						" from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as specimenCollectionGroup" +
						" where specimenCollectionGroup.id= " + scgId ;
				System.out.println("query in getSCG() "+query );
				
				hqlcri = new HQLCriteria(query);
				List list  = appService.query(hqlcri, SpecimenCollectionGroup.class.getName());  	
		  	    scg = (SpecimenCollectionGroup)list.get(0);
		 	}catch (Exception ex){
				ex.printStackTrace();
			}
		 	
		 	return scg;
	 }
	 
	 
	 public List getDeatils(Long partId) {
		 List list = null;	
		 try {
				String query = null;
				HQLCriteria hqlcri = null;				
				query = "select collectionEventParameters.specimenCollectionGroup.collectionProtocolRegistration.id, collectionEventParameters.specimenCollectionGroup.collectionProtocolEvent.id, " +
				" collectionEventParameters.specimenCollectionGroup.collectionStatus, collectionEventParameters.timestamp, " +
				" collectionEventParameters.specimenCollectionGroup.collectionProtocolRegistration.protocolParticipantIdentifier," +
				" collectionEventParameters.specimenCollectionGroup.id, collectionEventParameters.specimenCollectionGroup.specimenCollectionSite.id " +
				" from edu.wustl.catissuecore.domain.CollectionEventParameters as collectionEventParameters" +
				" where collectionEventParameters.specimenCollectionGroup.collectionProtocolRegistration.participant.id= " + partId ;
				
				hqlcri = new HQLCriteria(query);
		  	   list = appService.query(hqlcri, CollectionEventParameters.class.getName());  	
		  	 
		 	}catch (Exception ex){
				ex.printStackTrace();
			}
		 	
		 	return list;
		 }	 

	 public void disableParticipant(Long partId, Long cprId, Long scgId) throws Exception{
		 
		CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
		try {
			String query = null;
			HQLCriteria hqlcri = null;				
			query = "select collectionProtocolRegistration" +
					" from edu.wustl.catissuecore.domain.CollectionProtocolRegistration as collectionProtocolRegistration" +
					" where collectionProtocolRegistration.id= " + cprId ;
			
			hqlcri = new HQLCriteria(query);
			List list  = appService.query(hqlcri, CollectionProtocolRegistration.class.getName());  
			System.out.println("cpr list.size() "+list.size());
			if(list.size() > 0){
		  	    cpr = (CollectionProtocolRegistration)list.get(0);
		  	    System.out.println("here ");
		  	    cpr.setActivityStatus("Disabled");
		  	    cpr.setParticipant(null);
				appService.updateObject(cpr);
				System.out.println("cpr disabled");
				Participant participant = new Participant();
				participant.setId(partId);			 
			    participant.setActivityStatus("Disabled");	
			    //participant.setCollectionProtocolRegistrationCollection(new LinkedHashSet<CollectionProtocolRegistration>());
				appService.updateObject(participant);
				 System.out.println("participant disabled");
			}
			
	 	}catch (Exception ex){
	 		System.out.println("Exception in disableParticipant ");
			ex.printStackTrace();
		}			
		
//	 	 try{
//		 	Participant participant = new Participant();
//			participant.setId(partId);			 
//		    participant.setActivityStatus("Disabled");	
//		    participant.setCollectionProtocolRegistrationCollection(new LinkedHashSet<CollectionProtocolRegistration>());
//			appService.updateObject(participant);
//			 System.out.println("participant disabled");
//			}catch (Exception e) {
//				System.out.println("Exception in update participant");
//				e.printStackTrace();				
//			}
 }
	 

	public static void setUp(){
		
		System.setProperty("javax.net.ssl.trustStore", "D:/Main/pcatissue/jboss-4.2.2.GA/server/default/conf/jbosskey.keystore");
		appService = ApplicationServiceProvider.getApplicationService();
		ClientSession cs = ClientSession.getInstance();
		try
		{ 
			cs.startSession("catissue_admin@persistent.co.in", "Login^78");
		} 	
		catch (Exception ex) 
		{ 
			System.out.println(ex.getMessage()); 
			ex.printStackTrace();
			System.exit(1);
		}		
	}

	public static void main(String arg[]){
		try {
			System.out
					.println("---------IN MergeParticipants.Main-----------");
			System.out.println("user.dir  " + System.getProperty("user.dir"));
			String excelFilePath = System.getProperty("user.dir")
					+ "/excelFiles/mergePart.csv";
			setUp();
			new MergeParticipants().mergePart(excelFilePath);
			System.out.println("---------END MergeParticipants.Main-----------");
		} catch (Exception e) {
			System.out.println("Exception in MergeParticipants");
			System.err.println("Exception in MergeParticipants");
			e.printStackTrace();
		}
	}
	
}
