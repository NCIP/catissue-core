/**
 * <p>Title: ConceptHighLightingBean Class>
 * <p>Description:	This class contains attributes to display on ViewSurgicalPathologyReport.jsp Page in Concept Highlighting part</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on March 16,2007
 */
package edu.wustl.catissuecore.bean;


/**
 * @author ashish_gupta
 *
 */
public class ConceptHighLightingBean
{
	private String conceptName;
	private String startOffsets;
	private String endOffsets;
	private String classificationName;
	
	/**
	 * @return the classificationName
	 */
	public String getClassificationName()
	{
		return classificationName;
	}
	
	/**
	 * @param classificationName the classificationName to set
	 */
	public void setClassificationName(String classificationName)
	{
		this.classificationName = classificationName;
	}
	
	/**
	 * @return the conceptName
	 */
	public String getConceptName()
	{
		return conceptName;
	}
	
	/**
	 * @param conceptName the conceptName to set
	 */
	public void setConceptName(String conceptName)
	{
		this.conceptName = conceptName;
	}
	
	/**
	 * @return the endOffsets
	 */
	public String getEndOffsets()
	{
		return endOffsets;
	}
	
	/**
	 * @param endOffsets the endOffsets to set
	 */
	public void setEndOffsets(String endOffsets)
	{
		this.endOffsets = endOffsets;
	}
	
	/**
	 * @return the startOffsets
	 */
	public String getStartOffsets()
	{
		return startOffsets;
	}
	
	/**
	 * @param startOffsets the startOffsets to set
	 */
	public void setStartOffsets(String startOffsets)
	{
		this.startOffsets = startOffsets;
	}
}
