package edu.wustl.catissuecore.api.test;

import java.text.ParseException;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
//import sun.security.krb5.internal.crypto.e;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Participant;
//import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Race;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenObjectFactory;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.Utility;
//import edu.wustl.common.util.global.Constants;
//import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.common.util.HQLCriteria;
//import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
//import gov.nih.nci.system.comm.client.ClientSession;

public class DebounDataMigration_old {

private static int rowNo = 1;


static ApplicationService appService = null;

public void setCPRAndSCG(String excel[][]) throws Exception{

	appService = ApplicationServiceProvider.getApplicationService();
	String cpShortTitle = excel[rowNo][0];
	String collectedDate = excel[rowNo][6];
	String collSite = excel[rowNo][9];
	
	String query = null;
	HQLCriteria hqlcri = null;
	
	CollectionProtocol collectionProtocol = new CollectionProtocol();
	collectionProtocol.setShortTitle(cpShortTitle);
	System.out.println("cpShortTitle "+ cpShortTitle);
	List<?> resultList1 = null;	
	resultList1 = appService.search(CollectionProtocol.class, collectionProtocol);
	if(resultList1.size() > 0) {
		System.out.println("No of CP retrived from DB "+ resultList1.size());
		collectionProtocol = (CollectionProtocol)resultList1.get(0);
	}
	
	if(collectionProtocol.getId() != null ) {
		Participant participant  = null;
		System.out.println(" rowNo "+ rowNo + " excel.length "+ excel.length);
		while(rowNo < excel.length -1){
    	   try {
				System.out.println("inside while rowNo "+ rowNo);
				String studyLabel = excel[rowNo][10].trim();
				participant  = null;
				Collection<Specimen> specList = null; 
				Collection<SpecimenCollectionGroup> scgList = new ArrayList(); 
				SpecimenCollectionGroup scg = null;
				String pPI = excel[rowNo][1].trim();
				if(pPI.contains("-")) {
					System.out.println("pPI hase -");
					pPI = pPI.split("-")[1];
				}
				System.out.println("ppi "+ pPI);
				CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();;
				collectionProtocolRegistration.setProtocolParticipantIdentifier(pPI);
				// searching if CPR and Participant already registered
				List cprResultList = appService.search(CollectionProtocolRegistration.class, collectionProtocolRegistration);
				Iterator cprResultIterator = cprResultList.iterator();
				System.out.println("No of CPR retrived "+ cprResultList.size());
				while(cprResultIterator.hasNext()) {
					 System.out.println("cpr found ");
					 collectionProtocolRegistration = (CollectionProtocolRegistration)cprResultIterator.next();
				     System.out.println("cp in cpr " + collectionProtocolRegistration.getCollectionProtocol().getShortTitle());
				     if(collectionProtocolRegistration.getCollectionProtocol().equals(collectionProtocol)) {
				    	 participant =  collectionProtocolRegistration.getParticipant();
				    	 System.out.println("matching participant found " +participant.getLastName() +" " +  participant.getFirstName());
	                 	 query = "select specimenCollectionGroup from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as specimenCollectionGroup where "+
		               		"specimenCollectionGroup.collectionProtocolRegistration.participant.id= " + participant.getId() +
               				" and specimenCollectionGroup.collectionProtocolRegistration.collectionProtocol.id= " + collectionProtocol.getId() +
               				" and specimenCollectionGroup.collectionProtocolEvent.collectionPointLabel = '"  + studyLabel +"'";
               
	                 	  System.out.println("specimen query "+ query);
		             	  hqlcri = new HQLCriteria(query);
		             	  scgList = appService.query(hqlcri, Specimen.class.getName());
		             	  System.out.println("No of scgs  " + scgList .size()); 
		             	  SpecimenCollectionGroup specimenCollectioGroup = null;
	                  	  Iterator<SpecimenCollectionGroup> scgItr = scgList.iterator();
		             	 a: while(scgItr.hasNext()) { // This should iterat atleast one due to anticipated SCG
	     						  specimenCollectioGroup = (SpecimenCollectionGroup)scgItr.next();
	                  	          System.out.println("scg label "+specimenCollectioGroup.getCollectionProtocolEvent().getCollectionPointLabel());
	                  		   
	                  	        query = "select specimen from edu.wustl.catissuecore.domain.Specimen as specimen where " +
                		    	  "specimen.specimenCollectionGroup.collectionProtocolRegistration.participant.id= " + participant.getId() +
                		    	  " and specimen.specimenCollectionGroup.collectionProtocolRegistration.collectionProtocol.id= " + collectionProtocol.getId() +
                		    	  " and specimen.specimenCollectionGroup.id = "  + specimenCollectioGroup.getId()  ; 
             
                		    	  System.out.println("specimen query "+ query);
                		    	  hqlcri = new HQLCriteria(query);
                		    	  specList = appService.query(hqlcri, Specimen.class.getName());
                		    	  System.out.println("No of specimen "+ specList.size());
	                  	          if(specimenCollectioGroup.getCollectionStatus().equals("Complete")) {
	                  	        	  System.out.println("scg completed");
	                  		    	  Iterator<Specimen> spcItr = specList.iterator();
	                  				  while(spcItr.hasNext()){ 
	                  					   	Specimen specimen = (Specimen)spcItr.next();
	                  					   //	System.out.println("specimen.getLineage() "+specimen.getLineage()); 
	                  					   	if(specimen.getLineage().equals("New")){
	                  							String parentLabelXLS = excel[rowNo][11].trim().split("-")[1]+"P";
	                  							System.out.println("parent Label "+ specimen.getLabel());
	                  							if(specimen.getCollectionStatus().equals("Collected") && specimen.getLabel().equals(parentLabelXLS)) {
	                  								System.out.println("New specimen collected");
	                  								scg = specimenCollectioGroup;
	                  								break a; 
	                  							}
	                  					 	}
	                  				  }
	                  		      } else {  // same participant having diffrent anticipated SCG
	                  		    	 System.out.println("same participant having anticipated SCG");
	                  		    	  scg = specimenCollectioGroup;
	           					      System.out.println("scgSpcList "+ specList);

	                  		      }
		             	  }
		             	 if (scg == null) {
					   		  System.out.println("create new SCG");
             		    	  scg = addSCGWithNameAndEvents(collectionProtocolRegistration, specimenCollectioGroup.getCollectionProtocolEvent(), participant, excel);
             		    	  System.out.println("New scg created");
             		    	  specList = null;
						  }
	                    break;
				     }
			    }
			
			
			    if(participant == null){ // participant add
			    	 try {
				       participant = initParticipant(excel)	; 
				       collectionProtocolRegistration = initCollectionProtocolRegistration(participant, collectionProtocol, excel);
					   Collection collectionProtocolRegistrationCollection = new HashSet();
					   collectionProtocolRegistrationCollection.add(collectionProtocolRegistration);
					   participant.setCollectionProtocolRegistrationCollection(collectionProtocolRegistrationCollection);
				       
	            	   participant = (Participant) appService.createObject(participant); 
	     			   System.out.println("Participant Added .. id is "+ participant.getId()); 
	     			  // SpecimenCollectionGroup scg = null;
	     			   Collection cprColl = participant.getCollectionProtocolRegistrationCollection();
	     			   System.out.println("No of cpr retreived from participant "+ cprColl.size());
	     			   Iterator<CollectionProtocolRegistration> cprItr = cprColl.iterator();
	     			   while (cprItr.hasNext()) {
	     				   CollectionProtocolRegistration cpr = (CollectionProtocolRegistration)cprItr.next();
	     				   Collection scgCollection  = cpr.getSpecimenCollectionGroupCollection();
	     				
	     				   Iterator<SpecimenCollectionGroup> scgItr = scgCollection.iterator();
	     			       System.out.println("scgCollection.size() "+ scgCollection.size());
	     				   while(scgItr.hasNext()) {
	     					   scg = (SpecimenCollectionGroup)scgItr.next();
	     					   System.out.println("studyLabel "+ studyLabel);
	     					   System.out.println("CollectionPointLabel() "+ scg.getCollectionProtocolEvent().getCollectionPointLabel());
	     					   if(scg.getCollectionProtocolEvent().getCollectionPointLabel().equals(studyLabel))
	     					   		break;
	     					   else 
	     					      scg = null;
	     				   }
	     			   }
			    	 }  
		    	 catch (Exception e) {
		    		 System.out.println("Exception in create Participant "+ participant.getLastName() + " "+ participant.getFirstName() +" "+e.getMessage());
		    		 System.err.println("Exception in create Participant "+ participant.getLastName() + " "+ participant.getFirstName() +" "+e.getMessage());
		    		 e.printStackTrace();
		    	 } 
			    } 
			 
			    if(specList == null && scg != null) {
			    	  System.out.println("specList == null && scg != null");
				   	   specList = scg.getSpecimenCollection();
					   System.out.println("specList.size() "+ specList.size());
			   	}
			    
		  if(specList.size() >= 0) {
			   System.out.println("scgSpcList.size() "+ specList.size());

			  String tissueSite = excel[rowNo][14];
			  String pathoStatus = excel[rowNo][15];
	
		      Iterator<Specimen> spcItr = specList.iterator();
			  while(spcItr.hasNext()){ 
				   	Specimen specimen = (Specimen)spcItr.next();
				   	System.out.println("specimen.getLineage() "+specimen.getLineage()); 
				   	if(specimen.getLineage().equals("New")){
				   		//String parentLabel = excel[rowNo][11].trim();
				   		//if(parentLabel.concat("-")) 
						String parentLabel = excel[rowNo][11].trim().split("-")[1]+"P";
						System.out.println("parentLabel "+ parentLabel);
						specimen.setLabel(parentLabel);
						specimen.setInitialQuantity(Double.parseDouble("3"));
						setSpecimenEvents(specimen, excel, rowNo);
						specimen.setCollectionStatus("Collected");
	
						SpecimenCharacteristics specimenCharacteristics = specimen.getSpecimenCharacteristics();
						specimenCharacteristics.setTissueSide("Not Specified");
						specimenCharacteristics.setTissueSite(tissueSite);
						specimen.setSpecimenCharacteristics(specimenCharacteristics);
				   	
						specimen.setPathologicalStatus(pathoStatus);
						
						System.out.println("searching site");
						Site site = createSite(excel); 
						scg  = specimen.getSpecimenCollectionGroup();
						if (scg != null) {
						   scg.setSpecimenCollectionSite(site);
						   scg.setCollectionStatus("Complete");
						   
						   scg.setClinicalStatus("Not Specified");
						   scg.setClinicalDiagnosis("Not Specified");
						   setEventParameters(scg, excel, rowNo);
						   
						   appService.updateObject(scg);
						   System.out.println("scg updated");
					   }
					  
						//System.out.println("scg.getSpecimenEventParametersCollection().size()  "+ scg.getSpecimenEventParametersCollection().size());
						specimen.setSpecimenCollectionGroup(scg);
						/* collDate is commented while inegrating with nightly bcz it should be in the format MM-dd-yyyy **/
						//Date timestamp = EventsUtil.setTimeStamp(excel[rowNo][6],"15","45");
						Date timestamp = EventsUtil.setTimeStamp("08-15-1975","15","45");
						specimen.setCreatedOn(timestamp);
						
						appService.updateObject(specimen);
				        System.out.println("New Specimen updated");
				        Collection<AbstractSpecimen> childSpecColl = null;
				        query = "select specimen from edu.wustl.catissuecore.domain.Specimen as specimen where " +
				            "  specimen.parentSpecimen.id = " + specimen.getId();
				        
				       System.out.println("childSpecimen query "+ query);
				  	   hqlcri = new HQLCriteria(query);
				  	   specList = appService.query(hqlcri, Specimen.class.getName());
				  	   System.out.println("No of Childspecimens  " + specList.size());  
				        
						Iterator<Specimen> childItr = specList.iterator();
						//int tempRowNo = rowNo;
						while(childItr.hasNext()){
							Specimen drvSpec = (Specimen)childItr.next();
							String specimenClass = excel[rowNo][12];
							String specimenType = excel[rowNo][13];
							System.out.println("drv spc "+ drvSpec.getSpecimenClass() + " "+ drvSpec.getSpecimenType() );
							System.out.println("excel "+specimenClass +" "+ specimenType );
							if(drvSpec.getLineage().equals("Derived") && 
									drvSpec.getSpecimenClass().equals(specimenClass) &&
									drvSpec.getSpecimenType().equals(specimenType)) {
									String specimenLable = excel[rowNo][11];
									String drvInitQuantityExcel = excel[rowNo][16].trim();
							   
									drvSpec.setLabel(specimenLable);
						   	   
									drvSpec.setSpecimenCharacteristics(drvSpec.getParentSpecimen().getSpecimenCharacteristics());
									setSpecimenEvents(drvSpec, excel, rowNo);
						       
									drvSpec.setPathologicalStatus(pathoStatus);
									if(drvInitQuantityExcel.contains("ml")) {
										drvInitQuantityExcel = drvInitQuantityExcel.split("ml")[0].trim();
								    }
									
									double quantityDO = drvSpec.getInitialQuantity();
									drvSpec.setInitialQuantity(quantityDO + Double.parseDouble(drvInitQuantityExcel));
							     
									drvSpec.setCollectionStatus("Collected");
	//									specimenDomainCollection.add(drvSpec);	
									drvSpec.setCreatedOn(((Specimen)drvSpec.getParentSpecimen()).getCreatedOn());
									appService.updateObject(drvSpec);
							        System.out.println("drvSpec updated");
							        
							        int noOfAliquotsInExcel = (int)Double.valueOf(excel[rowNo][17].trim()).doubleValue();
							        System.out.println("noOfAliquotsInExcel "+ noOfAliquotsInExcel);
								   	Double quantityPerAliquot = (Double.parseDouble(drvInitQuantityExcel)/noOfAliquotsInExcel) ;
								   	
								   	query = "select specimen from edu.wustl.catissuecore.domain.Specimen as specimen where " +
						            "  specimen.parentSpecimen.id = " + drvSpec.getId() + " and specimen.collectionStatus != 'Collected'" ;
						        
								   	System.out.println("childSpecimen query "+ query);
								   	hqlcri = new HQLCriteria(query);
								   	specList = appService.query(hqlcri, Specimen.class.getName());
								   	System.out.println("No of Childspecimens in drv which are not colllected " + specList.size());  	
								   	
									Iterator<Specimen> childItr2 = specList.iterator();
								//	int aliquoteNo = 0;
									
									query = "select specimen from edu.wustl.catissuecore.domain.Specimen as specimen where " +
						            "  specimen.parentSpecimen.id = " + drvSpec.getId() + " and specimen.collectionStatus = 'Collected'" ;
						        
								   	System.out.println("childSpecimen query "+ query);
								   	hqlcri = new HQLCriteria(query);
								     Collection<Specimen> collSpecList = appService.query(hqlcri, Specimen.class.getName());
								     int aliquoteNo = collSpecList.size();
								   	 System.out.println("No of Childspecimens in drv which are colllected " + collSpecList.size());  	
									
									int i = 1;
									for( i = 1; i <= noOfAliquotsInExcel && childItr2.hasNext(); i++) {
										Specimen aliquotSpec = (Specimen)childItr2.next();
									    if(aliquotSpec.getLineage().equals("Aliquot")) {
										   aliquoteNo++;
										   aliquotSpec.setCollectionStatus("Collected");
										   aliquotSpec.setLabel(aliquotSpec.getParentSpecimen().getLabel()+"_"+aliquoteNo);
									       setSpecimenEvents(aliquotSpec, excel, rowNo);
									       
									   	   System.out.println("aliquot quantityPerAliquot  "+ quantityPerAliquot);
									   	   aliquotSpec.setInitialQuantity(quantityPerAliquot);
										   
									       String storageContainer = excel[rowNo][18];
									       if (storageContainer == "Virtual") {
									    	   System.out.println("Storage container is Virtual");
									    	   aliquotSpec.setSpecimenPosition(null);
									       } else {
										       try{
											       StorageContainer sc = new StorageContainer();
											       System.out.println("storageContainer "+ storageContainer);
												   sc.setName(storageContainer);
												   SpecimenPosition specPos = new SpecimenPosition();
												   specPos.setStorageContainer(sc);
												   specPos.setSpecimen(aliquotSpec);
												   aliquotSpec.setSpecimenPosition(specPos);
												   System.out.println("storageContainer in aliquote"+ aliquotSpec.getSpecimenPosition().getStorageContainer().getName());
	
										   	    }catch (Exception e){
											   		   System.out.println("Exception while allocation storage container for aliquot "+ storageContainer);
											   		   System.err.println("Exception while allocation storage container for aliquot "+ storageContainer);
											   		   e.printStackTrace();
										   	   }
									       }
										   aliquotSpec.setSpecimenCharacteristics(aliquotSpec.getParentSpecimen().getSpecimenCharacteristics());
	//										   specimenDomainCollection.add(aliquotSpec);
										   aliquotSpec.setCreatedOn(((Specimen)aliquotSpec.getParentSpecimen()).getCreatedOn());
										 try {
										   appService.updateObject(aliquotSpec);
										 }catch(Exception e){
											   System.out.println("Exception in updating aliquot so fixing virtual storage container");
											   System.err.println("Exception in updating aliquot so fixing virtual storage container ");
											   e.printStackTrace();
											   aliquotSpec.setSpecimenPosition(null);  // if storage container is full
											   appService.updateObject(aliquotSpec);
										 }
										   System.out.println("Aliquote updated ");
										   query = "select specimenCollectionGroup from edu.wustl.catissuecore.domain.Specimen as specimen where " +
								            "  specimen.id = " + aliquotSpec.getId();
									   }
					  } //for ends 
									//if(i < noOfAliquotsInExcel) {
										if(specList.size() < noOfAliquotsInExcel) {
										int noOfAlqTobeCreated = noOfAliquotsInExcel - specList.size();
										System.out.println("more aliquots to be created "+ noOfAlqTobeCreated);
										getAliquotsSpecimen(drvSpec, excel, rowNo, noOfAlqTobeCreated, quantityPerAliquot,aliquoteNo);
									}
							}
						}
				   	}
			   	} 
		    }
		  	
	       }catch (Exception ex)
			{
				System.out.println("Exception in  setCPRAndSCG for participant " + participant.getLastName() + " "+ participant.getFirstName());
				System.err.println("Exception in  setCPRAndSCG for participant " + participant.getLastName() + "" + participant.getFirstName());
				ex.printStackTrace();
				throw ex;
			}
	   	rowNo++;
		} // while ended
			System.out.println("done rowNo "+ rowNo);
	  
	} // if ended
}


public SpecimenCollectionGroup addSCGWithNameAndEvents(CollectionProtocolRegistration cpr, CollectionProtocolEvent cllectionProtocolEvent, Participant participant, String[][] excel) throws Exception
{

	System.out.println(" in addSCGWithNameAndEvents ");
	SpecimenCollectionGroup specimenCollectionGroup = new SpecimenCollectionGroup();
	specimenCollectionGroup =( SpecimenCollectionGroup) createSCG(cllectionProtocolEvent);
	if (specimenCollectionGroup != null) {
		Site site = createSite(excel); 
		specimenCollectionGroup.setSpecimenCollectionSite(site);
		String scgName="scg added through api"+UniqueKeyGeneratorUtil.getUniqueKey();
		specimenCollectionGroup.setName(scgName);
		specimenCollectionGroup.setCollectionProtocolRegistration(cpr);
		System.out.println("set cpr in scg");
		setEventParameters(specimenCollectionGroup, excel, rowNo);
		try{
			specimenCollectionGroup = (SpecimenCollectionGroup) appService.createObject(specimenCollectionGroup);
		}catch (Exception e){
			System.out.println("Exception in addSCGWithNameAndEvents");
			System.err.println("Exception in addSCGWithNameAndEvents");
         	e.printStackTrace();
		}
	}
	return specimenCollectionGroup;
	

}




private Site createSite(String[][] excel) throws Exception{
	
	String collSite = excel[rowNo][9];
	Site site = new Site();
	site.setName(collSite);
	List<?> resultList1 = null;
    try
	{
		resultList1 = appService.search(Site.class, site);
		if(resultList1.size() > 0) {
			System.out.println("No of Sites retrived from DB "+ resultList1.size());
			site = (Site)resultList1.get(0);
			System.out.println("site got");
		}
	} catch (Exception e1)
	{
		System.out.println("Exception in searching site");
		System.err.println("Exception in searching site");
		e1.printStackTrace();
		throw e1;
	}
	return site;
}

private void setEventParameters(SpecimenCollectionGroup sprObj, String excel[][], int row)
{
	String collCotainer = excel[row][8];
	String collProcedure = excel[row][7];
	String collDate = excel[row][6];
	
	System.out.println("Inside setEventParameters for SCG");
	Collection specimenEventParametersCollection = new HashSet();
	CollectionEventParameters collectionEventParameters = new CollectionEventParameters();
	ReceivedEventParameters receivedEventParameters = new ReceivedEventParameters();
	collectionEventParameters.setCollectionProcedure(collProcedure);
	collectionEventParameters.setComment("");
	collectionEventParameters.setContainer(collCotainer);
	try{
		//Date date = new SimpleDateFormat("mm-dd-yyyy").parse(collDate);
		System.out.println("collDate "+ collDate);
		/* collDate is commented while inegrating with nightly bcz it should be in the format MM-dd-yyyy  **/
		//Date timestamp = EventsUtil.setTimeStamp(collDate,"15","45");
		Date timestamp = EventsUtil.setTimeStamp("08-15-1975","15","45");
		collectionEventParameters.setTimestamp(timestamp);
		System.out.println("set collDate "+ collDate);
	}catch(Exception e){
		System.out.println("Exception while setting colection date in setEventParameters");
		System.err.println("Exception while setting colection date in setEventParameters");
		e.printStackTrace();
	}
	
	
	User user = getUser("abrink@pathology.wustl.edu");
	collectionEventParameters.setUser(user);	
	collectionEventParameters.setSpecimenCollectionGroup(sprObj);	
	
	//Received Events		
	receivedEventParameters.setComment("");
	User receivedUser = getUser("abrink@pathology.wustl.edu");
	receivedEventParameters.setUser(receivedUser);
	receivedEventParameters.setReceivedQuality("Not Specified");	

	/* collDate is commented while inegrating with nightly bcz it should be in the format MM-dd-yyyy **/
	//Date receivedTimestamp = EventsUtil.setTimeStamp(collDate,"15","45");
	Date receivedTimestamp = EventsUtil.setTimeStamp("08-15-1975","15","45");
	receivedEventParameters.setTimestamp(receivedTimestamp);		
	receivedEventParameters.setSpecimenCollectionGroup(sprObj);
	specimenEventParametersCollection.add(collectionEventParameters);
	specimenEventParametersCollection.add(receivedEventParameters);
	sprObj.setSpecimenEventParametersCollection(specimenEventParametersCollection);
}
private  void setSpecimenEvents(Specimen specimen,  String excel[][], int row) throws Exception

{
	System.out.println("In setSpecimenEvents");	
	String collCotainer = excel[row][8];
	String collProcedure = excel[row][7];
	String collDate = excel[row][6];
	
    Collection<SpecimenEventParameters> specimenEventCollection = new LinkedHashSet<SpecimenEventParameters>();
    CollectionEventParameters collectionEvent = null;
	ReceivedEventParameters receivedEvent = null;

	
    Collection evColl = specimen.getSpecimenEventCollection();
   // System.out.println( " evColl.size() "+ evColl.size());
   // if(specimen.getLineage()!="Aliquot") {
	if(evColl != null && evColl.size() != 0) {
		System.out.println("got eventColl from specimen ");
		Iterator itr  = evColl.iterator();
		while(itr.hasNext()) {
			SpecimenEventParameters spEvPar =	(SpecimenEventParameters)itr.next();
			if(spEvPar instanceof  CollectionEventParameters ) {
				System.out.println("got CollectionEventParameters");
				collectionEvent =(CollectionEventParameters) spEvPar;
			}
			else if(spEvPar instanceof  ReceivedEventParameters ) {
				System.out.println("got ReceivedEventParameters");
				receivedEvent =(ReceivedEventParameters) spEvPar;
			}
		}
	}
   // }
	
    if(collectionEvent == null) 
		collectionEvent = new CollectionEventParameters();
  
    User collectionEventUser = getUser("abrink@pathology.wustl.edu");
    collectionEvent.setUser(collectionEventUser);
	

    if(!specimen.getLineage().equals("New")){
		System.out.println(" in setSpecimenEventsnot a new specimen");
    	Collection parentSpcEvColl = specimen.getParentSpecimen().getSpecimenEventCollection();
    	
		if(parentSpcEvColl != null) {
			Iterator itr  = parentSpcEvColl.iterator();
			while(itr.hasNext()) {
				SpecimenEventParameters spEvPar =	(SpecimenEventParameters)itr.next();
				if(spEvPar instanceof  CollectionEventParameters ) {
					collectionEvent.setCollectionProcedure(((CollectionEventParameters) spEvPar).getCollectionProcedure());
					collectionEvent.setContainer(((CollectionEventParameters) spEvPar).getContainer());
				}
		     }
		}
	} else {
		System.out.println("in else ");
		collectionEvent.setCollectionProcedure(collProcedure);
		collectionEvent.setContainer(collCotainer);
	}
  
  try
	{
	 /*collDate is commented while inegrating with nightly bcz it should be in the format MM-dd-yyyy **/
	//Date timestamp = EventsUtil.setTimeStamp(collDate,"15","45");
	  Date timestamp = EventsUtil.setTimeStamp("08-15-1975","15","45");
	  collectionEvent.setTimestamp(timestamp);
	  System.out.println("set collDate "+ collDate);				
	}
	catch (Exception e1)
	{
		System.out.println("exception in setSpecimenEvents");
		e1.printStackTrace();
	}
	
	collectionEvent.setSpecimen(specimen);
	specimenEventCollection.add(collectionEvent);
	
    //setting received event values
	
	if(receivedEvent == null)
		receivedEvent = new ReceivedEventParameters();
     
	    receivedEvent.setReceivedQuality("Not Specified"); // c ltr
		User receivedEventUser = getUser("abrink@pathology.wustl.edu");
		receivedEvent.setUser(receivedEventUser);
   // }
	    
	try
	{
		/* collDate is commented while inegrating with nightly bcz it should be in the format MM-dd-yyyy  **/
		//Date receivedTimestamp = EventsUtil.setTimeStamp(collDate,"15","45");
		Date receivedTimestamp = EventsUtil.setTimeStamp("08-15-1975","15","45");
		receivedEvent.setTimestamp(receivedTimestamp);
	}
	catch (Exception e)
	{
		System.out.println("exception in setSpecimenEvents");
		e.printStackTrace();
	}
	receivedEvent.setSpecimen(specimen);
		specimenEventCollection.add(receivedEvent); 
    	specimen.setSpecimenEventCollection(specimenEventCollection);
    	
    	 evColl = specimen.getSpecimenEventCollection();
			if(evColl != null) {
				System.out.println("got eventColl from specimen ");
				Iterator itr  = evColl.iterator();
				while(itr.hasNext()) {
					SpecimenEventParameters spEvPar =	(SpecimenEventParameters)itr.next();
					if(spEvPar instanceof  CollectionEventParameters ) {
					System.out.println("in setting event "+	((CollectionEventParameters) spEvPar).getUser().getFirstName());
					}
					else if(spEvPar instanceof  ReceivedEventParameters ) {
						//ReceivedEventParameters) spEvPar;
					}
				}
			}

}


public  Participant initParticipant(String excel[][]) 
{
	String lastName = excel[rowNo][2];
	String firstName = excel[rowNo][3];
	String middleName = excel[rowNo][4];
	String dOB = excel[rowNo][5];
	
	Participant participant = new Participant();
	participant.setLastName(lastName);
	participant.setFirstName(firstName);
	System.out.println("middleName "+ middleName);
	if(middleName != "") {
		System.out.println("setting middle name");
		participant.setMiddleName(middleName);
	}
	//participant.setBirthDate(new Date(dOB));
	 try
    {   System.out.println("dob "+ dOB); 
        //dOB = dOB.replace("-", "\");
        System.out.println(dOB);
        
        Date timestamp = EventsUtil.setTimeStamp(dOB,"15","45");
        participant.setBirthDate(timestamp);
        
        
		//participant.setBirthDate(new SimpleDateFormat("MM-dd-yyyy").parse(dOB));
		System.out.println("birthdate set");
    }
    catch (Exception e)
    {
        System.out.println("exception in initParticipant in DOB");  
        System.err.println("exception in initParticipant in DOB");  
        e.printStackTrace();
    }
	
	participant.setGender("Unspecified");
	participant.setEthnicity("Unknown");
	participant.setSexGenotype("XX");
	
	Collection raceCollection = new HashSet();
	Race race = new Race();
	race.setRaceName("White");
	race.setParticipant(participant);
	raceCollection.add(race);
	
	race = new Race();
	race.setRaceName("Unknown");
	race.setParticipant(participant);
	raceCollection.add(race);
	
	/*raceCollection.add("White");
	raceCollection.add("Asian");*/
	participant.setRaceCollection(raceCollection);
	participant.setActivityStatus("Active");
	participant.setEthnicity("Hispanic or Latino");
	System.out.println("Participant initiated successfully -->Name:"+participant.getFirstName()+" "+participant.getLastName());
	return participant;
}

public  CollectionProtocolRegistration initCollectionProtocolRegistration(Participant participant, CollectionProtocol cp, String excel[][])
{
	//Logger.configure("");
	String pPI = excel[rowNo][1];
	String colldate = excel[rowNo][6];
	
	if(pPI.contains("-")) {
		System.out.println("pPI has -");
		pPI = pPI.split("-")[1];
	}
    System.out.println("pPI "+ pPI);
	CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();

	collectionProtocolRegistration.setCollectionProtocol(cp);
    collectionProtocolRegistration.setParticipant(participant);
    collectionProtocolRegistration.setProtocolParticipantIdentifier(pPI);
	collectionProtocolRegistration.setActivityStatus("Active");
	try
	{
		//collectionProtocolRegistration.setRegistrationDate(Utility.parseDate(colldate.replace('/', '-'), "M-d-yyyy"));
        /*collDate is commented while inegrating with nightly bcz it should be in the format MM-dd-yyyy  **/
		//Date timestamp = EventsUtil.setTimeStamp(colldate,"15","45");
        Date timestamp = EventsUtil.setTimeStamp("08-15-1975","15","45");
        collectionProtocolRegistration.setRegistrationDate(timestamp);
		
	}
	catch (Exception e)
	{
		System.out.println("Exception in initCollectionProtocolRegistration" );
		System.err.println("Exception in initCollectionProtocolRegistration" );
		e.printStackTrace();
	}
	
	//Setting Consent Tier Responses.
	try
	{
		collectionProtocolRegistration.setConsentSignatureDate(Utility.parseDate("11/23/2006",Utility.datePattern("11/23/2006")));
	}
	catch (ParseException e)
	{			
		System.err.println("exception in initCPR in Date");
    	System.out.println("exception in initCPR in Date");
		e.printStackTrace();
	}
	// pratha collectionProtocolRegistration.setSignedConsentDocumentURL("F:/doc/consentDoc.doc");
	User user = getUser("abrink@pathology.wustl.edu");
	collectionProtocolRegistration.setConsentWitness(user);
	
	Collection consentTierResponseCollection = new HashSet();
	Collection consentTierCollection = cp.getConsentTierCollection();
	Iterator consentTierItr = consentTierCollection.iterator();
	while(consentTierItr.hasNext())
	{
		ConsentTier consentTier = (ConsentTier)consentTierItr.next();
		ConsentTierResponse consentResponse = new ConsentTierResponse();
		consentResponse.setConsentTier(consentTier);
		consentResponse.setResponse("Yes");
		consentTierResponseCollection.add(consentResponse);
	}
	collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);	
	System.out.println("calling createSCG in initCollectionProtocolRegistration");
	SpecimenCollectionGroup specimenCollectionGroup = createSCG(collectionProtocolRegistration);
	
	Collection specimenCollectionGroupCollection = new HashSet<SpecimenCollectionGroup>(); 
	collectionProtocolRegistration.setSpecimenCollectionGroupCollection(specimenCollectionGroupCollection);
	
	Collection collectionProtocolRegistrationCollection = new HashSet();

	
	System.out.println("CPR initiated");
	return collectionProtocolRegistration;
}

public  SpecimenCollectionGroup createSCG(CollectionProtocolRegistration collectionProtocolRegistration)
{
	Map<Specimen, List<Specimen>> specimenMap = new LinkedHashMap<Specimen, List<Specimen>>();
	SpecimenCollectionGroup specimenCollectionGroup = null;
	try 
	{
		Collection collectionProtocolEventCollection = collectionProtocolRegistration.getCollectionProtocol().getCollectionProtocolEventCollection();
		Iterator collectionProtocolEventIterator = collectionProtocolEventCollection.iterator();
		//User user = (User)TestCaseUtility.getObjectMap(User.class);
		User user = getUser("abrink@pathology.wustl.edu");
		while(collectionProtocolEventIterator.hasNext())
		{
			CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent)collectionProtocolEventIterator.next();
			specimenCollectionGroup = new SpecimenCollectionGroup(collectionProtocolEvent);
			specimenCollectionGroup.setCollectionProtocolRegistration(collectionProtocolRegistration);
			specimenCollectionGroup.setConsentTierStatusCollectionFromCPR(collectionProtocolRegistration);
			
			LabelGenerator specimenCollectionGroupLableGenerator = LabelGeneratorFactory.getInstance("speicmenCollectionGroupLabelGeneratorClass");
			specimenCollectionGroupLableGenerator.setLabel(specimenCollectionGroup);
			
			Collection cloneSpecimenCollection = new LinkedHashSet();
			Collection<SpecimenRequirement> specimenCollection = collectionProtocolEvent.getSpecimenRequirementCollection();
			if(specimenCollection != null && !specimenCollection.isEmpty())
			{
				Iterator itSpecimenCollection = specimenCollection.iterator();
				while(itSpecimenCollection.hasNext())
				{
					SpecimenRequirement reqSpecimen = (SpecimenRequirement)itSpecimenCollection.next();
					if(reqSpecimen.getLineage().equalsIgnoreCase("new"))
					{
						Specimen cloneSpecimen = getCloneSpecimen(specimenMap, reqSpecimen,null,specimenCollectionGroup,user);
						LabelGenerator specimenLableGenerator = LabelGeneratorFactory.getInstance("specimenLabelGeneratorClass");
						specimenLableGenerator.setLabel(cloneSpecimen);
						cloneSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);
						cloneSpecimenCollection.add(cloneSpecimen);
					}
				}
			}
			
			specimenCollectionGroup.setSpecimenCollection(cloneSpecimenCollection);
		}
	}catch(Exception e)
	{
		System.err.println("Exception in create SCG");
		System.out.println("Exception in create SCG");
		e.printStackTrace();
	}
		return specimenCollectionGroup;
}


public  SpecimenCollectionGroup createSCG(CollectionProtocolEvent collectionProtocolEvent)
{
	Map<Specimen, List<Specimen>> specimenMap = new LinkedHashMap<Specimen, List<Specimen>>();
	SpecimenCollectionGroup specimenCollectionGroup = null;
	try 
	{
		User user = getUser("abrink@pathology.wustl.edu");
		
		specimenCollectionGroup = new SpecimenCollectionGroup(collectionProtocolEvent);
		System.out.println("scg label "+specimenCollectionGroup.getCollectionProtocolEvent().getCollectionPointLabel());
		specimenCollectionGroup.setCollectionProtocolRegistration(collectionProtocolEvent.getCollectionProtocolRegistration());
		
		LabelGenerator specimenCollectionGroupLableGenerator = LabelGeneratorFactory.getInstance("speicmenCollectionGroupLabelGeneratorClass");
		specimenCollectionGroupLableGenerator.setLabel(specimenCollectionGroup);
		
		Collection cloneSpecimenCollection = new LinkedHashSet();
		Collection<SpecimenRequirement> specimenCollection = collectionProtocolEvent.getSpecimenRequirementCollection();
		if(specimenCollection != null && !specimenCollection.isEmpty())
		{
			Iterator itSpecimenCollection = specimenCollection.iterator();
			while(itSpecimenCollection.hasNext())
			{
				SpecimenRequirement reqSpecimen = (SpecimenRequirement)itSpecimenCollection.next();
				if(reqSpecimen.getLineage().equalsIgnoreCase("new"))
				{
					Specimen cloneSpecimen = getCloneSpecimen(specimenMap, reqSpecimen,null,specimenCollectionGroup,user);
					LabelGenerator specimenLableGenerator = LabelGeneratorFactory.getInstance("specimenLabelGeneratorClass");
					specimenLableGenerator.setLabel(cloneSpecimen);
					cloneSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);
					cloneSpecimenCollection.add(cloneSpecimen);
				}
			}
		}
		
		specimenCollectionGroup.setSpecimenCollection(cloneSpecimenCollection);
	}catch(Exception e)
	{
		System.out.println("Exception in create SCG");
		System.err.println("Exception in create SCG");
		e.printStackTrace();
	}
	return specimenCollectionGroup;
}


private static Specimen getCloneSpecimen(Map<Specimen, List<Specimen>> specimenMap, SpecimenRequirement reqSpecimen, Specimen pSpecimen, SpecimenCollectionGroup specimenCollectionGroup, User user)
{
	Collection childrenSpecimen = new LinkedHashSet<Specimen>(); 
	Specimen newSpecimen = null;
	try 
	{
		newSpecimen = (Specimen) new SpecimenObjectFactory()
			.getDomainObject(reqSpecimen.getClassName(),reqSpecimen);
	}
	catch (AssignDataException e1) 
	{
		System.out.println("Exception in getCloneSpecimen");
		System.err.println("Exception in getCloneSpecimen");
		e1.printStackTrace();
		return null;
	}	
	newSpecimen.setParentSpecimen(pSpecimen);
	newSpecimen.setDefaultSpecimenEventCollection(user.getId());
	newSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);
	newSpecimen.setConsentTierStatusCollectionFromSCG(specimenCollectionGroup);
	if (newSpecimen.getParentSpecimen()== null)
	{
		specimenMap.put(newSpecimen, new ArrayList<Specimen>());
	}
	else
	{
		specimenMap.put(newSpecimen, null);
	}
	
	Collection childrenSpecimenCollection = reqSpecimen.getChildSpecimenCollection();
	if(childrenSpecimenCollection != null && !childrenSpecimenCollection.isEmpty())
	{
    	Iterator<SpecimenRequirement> it = childrenSpecimenCollection.iterator();
    	while(it.hasNext())
    	{
    		SpecimenRequirement childReqSpecimen = it.next();
    		Specimen newchildSpecimen = getCloneSpecimen(specimenMap, childReqSpecimen,newSpecimen, specimenCollectionGroup, user);
    		childrenSpecimen.add(newchildSpecimen);
    		newSpecimen.setChildSpecimenCollection(childrenSpecimen);
    	}
	}
	return newSpecimen;
}


private User getUser(String loginName){
	
	User user = new User();
	user.setLoginName(loginName);
	List<?> resultList1 = new LinkedList ();
	try {
	List resultList = appService.search(User.class,user);
	if(resultList.size() > 0){
		user = (User)resultList.get(0);
	}
	
	}catch (Exception e) {
		System.out.println("Exception in getUser");
		System.err.println("Exception in getUser");
		e.printStackTrace();
	}
	
	return user;
}


 public  void getAliquotsSpecimen(Specimen parentSpecimen , String excel[][],int row, int aliquotCount, Double quantityPerAliquot, int aliquoteNo )throws Exception
{  

	//Collection specColl = new LinkedHashSet();
	System.out.println("In Aliquot Specimen");

	String noOfAliquotes = excel[row][17].trim();
	String specimenType = excel[rowNo][13];
	String storageContainer = excel[rowNo][18];
	
	System.out.println("aliquot count "+ aliquotCount);
	System.out.println(" quantityPerAliquot "+ quantityPerAliquot);
	
	try
	{
		for(int i = 1; i <= aliquotCount; i++)
		{
			System.out.println(" i "+ i);
			Specimen specimen = new Specimen();
			//specimen = createSpecimen(specimen, parentSpecimen, excel, row, lineage);
		    System.out.println("parentSpecimen.getSpecimenClass() " +parentSpecimen.getSpecimenClass());
			if(parentSpecimen.getSpecimenClass().equals("Tissue"))
			{
				specimen = new TissueSpecimen();
			}
			else if(parentSpecimen.getSpecimenClass().equals("Fluid"))
			{
				specimen = new FluidSpecimen();
				specimen.setSpecimenClass("Fluid");
			}
			else if(parentSpecimen.getSpecimenClass().equals("Cell"))
			{
				specimen = new CellSpecimen();
			}
			else if(parentSpecimen.getSpecimenClass().equals("Molecular"))
			{
				specimen = new MolecularSpecimen();
			}
			specimen.setSpecimenType(parentSpecimen.getSpecimenType());;
			specimen.setParentSpecimen(parentSpecimen);
            specimen.setLineage("Aliquot");
            setSpecimenEvents(specimen, excel, row);

			System.out.println("in getAliquotsSpecimen after calling setSpecimenEvents");
			specimen.setInitialQuantity(quantityPerAliquot);
			specimen.setAvailableQuantity(quantityPerAliquot);
			specimen.setNoOfAliquots(0); // c ltr
		    specimen.setSpecimenCharacteristics(parentSpecimen.getSpecimenCharacteristics());
		    System.out.println("aliquoteNo "+ aliquoteNo);
			System.out.println("aliquoteNo "+ aliquoteNo);
			specimen.setLabel(parentSpecimen.getLabel()+"_"+ ++aliquoteNo);
		    StorageContainer sc = new StorageContainer();
		    sc.setName(storageContainer);
		    SpecimenPosition specPos = new SpecimenPosition();
			specPos.setStorageContainer(sc);
			specPos.setSpecimen(specimen);
			//specPos.setPositionDimensionOne(new Integer(2));
			//specPos.setPositionDimensionTwo(new Integer(2));
			specimen.setSpecimenPosition(specPos);
			specimen.setIsAvailable(new Boolean(true));
			specimen.setActivityStatus("Active");
			specimen.setPathologicalStatus(parentSpecimen.getPathologicalStatus());
		    
			System.out.println("in getAliquotsSpecimen aliquot type " + specimen.getSpecimenType());
			System.out.println("parentSpecimen "+ parentSpecimen);
			specimen.setSpecimenCollectionGroup(parentSpecimen.getSpecimenCollectionGroup());
			System.out.println(" in getAliquots parentSpecimen.getSpecimenCollectionGroup  "+ parentSpecimen.getSpecimenCollectionGroup());
		   try {
			    appService.createObject(specimen);
				 }catch(Exception e){
					   System.out.println("Exception in creating aliquot so fixing virtual storage container");
					   System.err.println("Exception in creating aliquot so fixing virtual storage container ");
					   e.printStackTrace();
					   specimen.setSpecimenPosition(null);  // if storage container is full
					   //appService.updateObject(specimen);
				 }
		    System.out.println("Aliquote created ");
			
		}
	}
	catch(ArrayIndexOutOfBoundsException ex)
	{
		System.out.println("Exception in getChildSpecimenMap"+ex);
		System.err.println("Exception in getChildSpecimenMap"+ex);

	}
	
	//return specColl;
} 


} //class cclosed

