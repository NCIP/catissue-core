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
