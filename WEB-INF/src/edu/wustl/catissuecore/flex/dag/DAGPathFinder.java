package edu.wustl.catissuecore.flex.dag;


import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.query.IPathFinder;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;
import edu.wustl.common.querysuite.metadata.path.ICuratedPath;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.queryengine.impl.CommonPathFinder;

public class DAGPathFinder implements IPathFinder {

	
	public List<IPath> getAllPossiblePaths(EntityInterface source, EntityInterface destination) {
		 
		CommonPathFinder pathFinder = new CommonPathFinder();
		List<IPath> allPossiblePaths = pathFinder.getAllPossiblePaths(source, destination);
		
		return allPossiblePaths;
	}
	
	
	public Set<ICuratedPath> autoConnect(Set<EntityInterface> arg0)
	{
		return new HashSet<ICuratedPath>();
	}


	public Set<ICuratedPath> getCuratedPaths(EntityInterface arg0, EntityInterface arg1)
	{
		return new HashSet<ICuratedPath>();
	}


	public Collection<AssociationInterface> getIncomingIntramodelAssociations(Long arg0)
	{
		return new Vector<AssociationInterface>();
	}


	public List<IInterModelAssociation> getInterModelAssociations(Long arg0)
	{
		return new Vector<IInterModelAssociation>();
	}


	

}
