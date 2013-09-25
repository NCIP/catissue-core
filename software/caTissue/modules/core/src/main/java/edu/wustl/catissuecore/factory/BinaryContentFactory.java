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

import edu.wustl.catissuecore.domain.pathology.BinaryContent;


public class BinaryContentFactory implements InstanceFactory<BinaryContent>
{
	private static BinaryContentFactory binaryContentFactory;

	private BinaryContentFactory()
	{
		super();
	}

	public static synchronized BinaryContentFactory getInstance()
	{
		if(binaryContentFactory==null){
			binaryContentFactory = new BinaryContentFactory();
		}
		return binaryContentFactory;
	}

	public BinaryContent createClone(BinaryContent t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public BinaryContent createObject()
	{
		BinaryContent binaryContent=new BinaryContent();
		initDefaultValues(binaryContent);
		return binaryContent;
	}

	public void initDefaultValues(BinaryContent t)
	{
		// TODO Auto-generated method stub

	}

}
