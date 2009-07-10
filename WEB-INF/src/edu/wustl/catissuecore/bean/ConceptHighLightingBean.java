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
		return this.classificationName;
	}

	/**
	 * @param classificationNameParam the classificationName to set
	 */
	public void setClassificationName(String classificationNameParam)
	{
		this.classificationName = classificationNameParam;
	}

	/**
	 * @return the conceptName
	 */
	public String getConceptName()
	{
		return this.conceptName;
	}

	/**
	 * @param conceptNameParam the conceptName to set
	 */
	public void setConceptName(String conceptNameParam)
	{
		this.conceptName = conceptNameParam;
	}

	/**
	 * @return the endOffsets
	 */
	public String getEndOffsets()
	{
		return this.endOffsets;
	}

	/**
	 * @param endOffsetsParam the endOffsets to set
	 */
	public void setEndOffsets(String endOffsetsParam)
	{
		this.endOffsets = endOffsetsParam;
	}

	/**
	 * @return the startOffsets
	 */
	public String getStartOffsets()
	{
		return this.startOffsets;
	}

	/**
	 * @param startOffsetsParam the startOffsets to set
	 */
	public void setStartOffsets(String startOffsetsParam)
	{
		this.startOffsets = startOffsetsParam;
	}
}
