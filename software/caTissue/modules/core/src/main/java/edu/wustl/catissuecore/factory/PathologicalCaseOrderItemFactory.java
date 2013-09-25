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

import edu.wustl.catissuecore.domain.PathologicalCaseOrderItem;


public class PathologicalCaseOrderItemFactory implements InstanceFactory<PathologicalCaseOrderItem>
{
	private static PathologicalCaseOrderItemFactory pathologicalCaseOrderItemFactory;

	protected PathologicalCaseOrderItemFactory()
	{
		super();
	}

	public static synchronized PathologicalCaseOrderItemFactory getInstance()
	{
		if(pathologicalCaseOrderItemFactory==null){
			pathologicalCaseOrderItemFactory = new PathologicalCaseOrderItemFactory();
		}
		return pathologicalCaseOrderItemFactory;
	}
	public PathologicalCaseOrderItem createClone(PathologicalCaseOrderItem t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public PathologicalCaseOrderItem createObject()
	{
		PathologicalCaseOrderItem item= new PathologicalCaseOrderItem();
		initDefaultValues(item);
		return item;
	}

	public void initDefaultValues(PathologicalCaseOrderItem t)
	{
		// TODO Auto-generated method stub

	}

}
