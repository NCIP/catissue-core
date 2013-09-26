/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry;
import edu.wustl.catissuecore.domain.deintegration.SpecimenRecordEntry;


public class SpecimenRecordEntryFactory implements InstanceFactory<SpecimenRecordEntry>
{

	private static SpecimenRecordEntryFactory specimenRecordEntryFactory;

	private SpecimenRecordEntryFactory()
	{
		super();
	}

	public static synchronized SpecimenRecordEntryFactory getInstance()
	{
		if(specimenRecordEntryFactory==null){
			specimenRecordEntryFactory = new SpecimenRecordEntryFactory();
		}
		return specimenRecordEntryFactory;
	}


	public SpecimenRecordEntry createClone(SpecimenRecordEntry t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public SpecimenRecordEntry createObject()
	{
		SpecimenRecordEntry entry = new SpecimenRecordEntry();
		initDefaultValues(entry);
		return entry;
	}

	public void initDefaultValues(SpecimenRecordEntry t)
	{
		// TODO Auto-generated method stub

	}
}
