package edu.wustl.catissuecore.applet.ui.querysuite;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.query.IPathFinder;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.AppletServerCommunicator;
import edu.wustl.catissuecore.applet.model.AppletModelInterface;
import edu.wustl.catissuecore.applet.model.BaseAppletModel;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;
import edu.wustl.common.querysuite.metadata.path.ICuratedPath;
import edu.wustl.common.querysuite.metadata.path.IPath;
/**
 * Returns all the possible paths between the source entity and destination entity.
 * @author deepti_shelar
 *
 */
public class PathFinderAppletServerCommunicator implements IPathFinder
{

	String serverURL = "";
	/**
	 * Constructor which set server url;
	 * @param serverURL
	 */
	PathFinderAppletServerCommunicator (String serverURL)
	{
		this.serverURL = serverURL ;
	}


	/**
	 * Returns all the possible paths between the source entity and destination entity.
	 * @param source source entity reference.
	 * @param destination Target Entity reference.
	 */
	public List<IPath> getAllPossiblePaths(EntityInterface source, EntityInterface destination)
	{
		List<IPath> paths = null;
		String urlString = serverURL + AppletConstants.PATH_FINDER + "?" +AppletConstants.APPLET_ACTION_PARAM_NAME + "=initData";
		BaseAppletModel appletModel = new BaseAppletModel();
		Map inputMap = new HashMap(); 
		inputMap.put(AppletConstants.SRC_ENTITY, source);
		inputMap.put(AppletConstants.DEST_ENTITY, destination);
		appletModel.setData(inputMap);
		try
		{
			AppletModelInterface outputModel = AppletServerCommunicator.doAppletServerCommunication(urlString, appletModel);
			paths = (List<IPath>)outputModel.getData().get(AppletConstants.PATHS);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Exception while getting all paths:", e);
		}
		catch (ClassNotFoundException e)
		{
			throw new RuntimeException("Exception while getting all paths:", e);
		}
		return paths;
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
