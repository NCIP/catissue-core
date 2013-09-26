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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry;


public class ParticipantSingleAPITestcase extends AbstractCaCoreApiTestCasesWithRegularAuthentication
{
	
//	public void testSearchPartDE()
//	{
//		Participant participant = new Participant();
//		ParticipantRecordEntry entry = new ParticipantRecordEntry();
//		AlcoholHealthAnnotation alcoAnnot = new AlcoholHealthAnnotation();
//		alcoAnnot.setDrinksPerWeek(3L);
//		alcoAnnot.setParticipantRecordEntry_AlcoholHealthAnnotation(entry);
//		entry.setParticipant(participant);
//		entry.setAlcoholHealthAnnotationCollection(new HashSet(Arrays.asList(alcoAnnot)));
//		participant.setParticipantRecordEntryCollection(new HashSet(Arrays.asList(entry)));
//		
//		List<Participant> participants = searchByExample(Participant.class, participant);
//		for (Participant part : participants) 
//		{
//			printObject(part, part.getClass());
//		}
//	}
//	
//	private void printObject(Object obj, Class klass) throws Exception {
//		System.out.println("Printing "+ klass.getName());
//		Method[] methods = klass.getMethods();
//		for(Method method:methods)
//		{
//			if(method.getName().startsWith("get") && !method.getName().equals("getClass"))
//			{
//				System.out.print("\t"+method.getName().substring(3)+":");
//				Object val = method.invoke(obj, (Object[])null);
//				if(val instanceof java.util.Set)
//					System.out.println("size="+((Collection)val).size());
//				else
//					System.out.println(val);
//			}
//		}
//	}
	
}
