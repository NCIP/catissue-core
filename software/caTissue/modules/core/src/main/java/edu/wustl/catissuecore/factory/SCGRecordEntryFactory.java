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

import edu.wustl.catissuecore.domain.deintegration.SCGRecordEntry;


public class SCGRecordEntryFactory implements InstanceFactory<SCGRecordEntry>
{

	private static SCGRecordEntryFactory scgRecordEntryFactory;

	private SCGRecordEntryFactory()
	{
		super();
	}

	public static synchronized SCGRecordEntryFactory getInstance()
	{
		if(scgRecordEntryFactory==null){
			scgRecordEntryFactory = new SCGRecordEntryFactory();
		}
		return scgRecordEntryFactory;
	}


	public SCGRecordEntry createClone(SCGRecordEntry t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public SCGRecordEntry createObject()
	{
		SCGRecordEntry entry = new SCGRecordEntry();
		initDefaultValues(entry);
		return entry;
	}

	public void initDefaultValues(SCGRecordEntry t)
	{
		// TODO Auto-generated method stub

	}

}
