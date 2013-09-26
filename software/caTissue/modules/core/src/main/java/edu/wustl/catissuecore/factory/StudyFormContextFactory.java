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

import java.util.HashSet;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.StudyFormContext;


public class StudyFormContextFactory implements InstanceFactory<StudyFormContext>
{
	private static StudyFormContextFactory studyFormContextFactory;

	protected StudyFormContextFactory()
	{
		super();
	}

	public static synchronized StudyFormContextFactory getInstance()
	{
		if(studyFormContextFactory==null){
			studyFormContextFactory = new StudyFormContextFactory();
		}
		return studyFormContextFactory;
	}
	public StudyFormContext createClone(StudyFormContext t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public StudyFormContext createObject()
	{
		StudyFormContext studyFormContext=new StudyFormContext();
		initDefaultValues(studyFormContext);
		return studyFormContext;
	}

	public void initDefaultValues(StudyFormContext obj)
	{
		obj.setCollectionProtocolCollection(new HashSet<CollectionProtocol>());
	}

}
