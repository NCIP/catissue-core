package edu.wustl.catissuecore.api.test;

import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.Participant;


public class ParticipantEdit extends CaTissueBaseTestCase {
	
	private int rowNo = 1;
	
//	private void parriciapantEdit(String[][] excel ) throws Exception {
//		
//		while(rowNo < excel.length) {
//
//			String participantId = excel[rowNo][0];
//			if(participantId != null && participantId != ""){
//				participantId = participantId.trim();
//			}
//			
//			String pPI = excel[rowNo][1];
//			if(pPI != null && pPI != ""){
//				pPI = pPI.trim();
//			}
//			
//			Participant participanr = getParticipant(new Long(participantId));
//			
//			String vitalStatus = excel[rowNo][2];
//			if(vitalStatus != null && vitalStatus != ""){
//				vitalStatus = vitalStatus.trim();
//			}
//			
//			updateParticipant(participanr, vitalStatus);
//			rowNo++;
//			
//		} 
//		
//	}
	
//	private void updateParticipant(Participant participant, String vitalStatus) throws Exception {
//		
//		participant.setVitalStatus(vitalStatus);
//		Participant updatedParticipant = appService.update(participant);
//		System.out.println("Participant updated with vital status "+ updatedParticipant.getVitalStatus());
//		
//	}
	
	private Participant getParticipant(Long partId) throws Exception{
		
		Participant participant = new Participant();
		participant.setId(new Long(partId));

		try
		{
			List resultList = appService.search(Participant.class, participant);
			System.out.println("No of Participant retrived " + resultList.size());
			if (resultList.size() == 0){
				participant.setId(null);
			}
			else 
			{
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();)
				{
					participant = (Participant) resultsIterator.next();
					System.out.println(" Domain Object is successfully Found ---->  :: "
							+ participant.getLastName() + " " + participant.getFirstName());
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("ParticipantTestCases.testSearchParticipant()" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return participant;
		
	}
	
	
}