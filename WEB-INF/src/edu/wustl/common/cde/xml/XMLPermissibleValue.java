/*
 * Created on Jun 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.common.cde.xml;


/**
 * @author mandar_deshmukh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class XMLPermissibleValue
{
    String evsTerminology;
    String conceptCode;
    int depthOfHierarchyTree;
    
    /**
     * @param evsTerminology    database to connect
     * @param conceptCode		concept code of the Permissible value
     * @param depthOfHierarchyTree  depth of the tree to search
     */
    public XMLPermissibleValue(String evsTerminology,
    							String conceptCode,
								int depthOfHierarchyTree)
    {
        this.evsTerminology = evsTerminology;
        this.conceptCode  = conceptCode;
        this.depthOfHierarchyTree = depthOfHierarchyTree;
    } // constructor XMLPermissibleValue

    /**
     * Default constructor
     */
    public XMLPermissibleValue()
    {
    } //  XMLPermissibleValue

    
    
	/**
	 * @param conceptCode The conceptCode to set.
	 */
	public void setConceptCode(String conceptCode)
	{
		this.conceptCode = conceptCode;
	}
	
	/**
	 * @param depthOfHierarchyTree The depthOfHierarchyTree to set.
	 */
	public void setDepthOfHierarchyTree(int depthOfHierarchyTree)
	{
		this.depthOfHierarchyTree = depthOfHierarchyTree;
	}
	
	/**
	 * @param evsTerminology The evsTerminology to set.
	 */
	public void setEvsTerminology(String evsTerminology)
	{
		this.evsTerminology = evsTerminology;
	}
	
    /**
     * @return Returns the conceptCode.
     */
    public String getConceptCode()
    {
        return conceptCode;
    }
    
    /**
     * @return Returns the depthOfHierarchyTree.
     */
    public int getDepthOfHierarchyTree()
    {
        return depthOfHierarchyTree;
    }
    
    /**
     * @return Returns the evsTerminology.
     */
    public String getEvsTerminology()
    {
        return evsTerminology;
    }
    
} // XMLPermissibleValue
