package edu.wustl.catissuecore.domain.pathology;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * Represents different logical sections of surgical pathology report.
 * @hibernate.class
 * table="CATISSUE_REPORT_SECTION"
 */

public class ReportSection extends AbstractDomainObject
{

	/**
	 * Text information of the report section.
	 */
	protected String documentFragment;
	/**
	 * End off set of the report section.
	 */
	protected Integer endOffSet;
	
	/**
	 * Name of the reporting section.
	 */
	protected String name;
	
	/**
	 * Start off set of reporting section. 
	 * 
	 */
	protected Integer startOffSet;
	
	/**
	 * Text content of the report.
	 */
	protected TextContent textContent;
	
	/**
	 * System generated unique ID.
	 */
	protected Long id;

	/**
    * @return system generated id
    * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
    * unsaved-value="null" generator-class="native" 
    * @hibernate.generator-param name="sequence" value="CATISSUE_REPORT_SECTION_SEQ"
    */
	public Long getId()
	{
		return id;
	}

	/**
	 * @param id sets system generated id
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * Constructor
	 */
	public ReportSection()
	{

	}

	
	/**
	 * @return text information of the report section.
     * @hibernate.property name="documentFragment"
     * type="string" column="DOCUMENT_FRAGMENT" 
     * length="1000"
     */
	public String getDocumentFragment()
	{
		return documentFragment;
	}
	/**
	 * @param documentFragment Sets the text information of the report section.
	 */
	public void setDocumentFragment(String documentFragment)
	{
		this.documentFragment = documentFragment;
	}

	/**
	 * @return the end off set of the report section.
     * @hibernate.property name="endOffSet"
     * type="integer" column="END_OFFSET" 
     * length="10"
     */
	public Integer getEndOffSet()
	{
		return endOffSet;
	}

	/**
	 * @param endOffSet sets the end off set of the report section.
	 */
	public void setEndOffSet(Integer endOffSet)
	{
		this.endOffSet = endOffSet;
	}

	/**
	 * @return the name of the report section.
     * @hibernate.property  name="name"
     * type="string" column="NAME" 
     * length="100"
     */
	public String getName()
	{
		return name;
	}
	
	/**
	 * @param name sets the name of the report section.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the start offset of the report section.
     * @hibernate.property name="startOffSet"
     * type="integer" column="START_OFFSET" 
     * length="10"
     */
	public Integer getStartOffSet()
	{
		return startOffSet;
	}

	/**
	 * @param startOffSet sets the startoff set of the report section.
	 */
	public void setStartOffSet(Integer startOffSet)
	{
		this.startOffSet = startOffSet;
	}
	/** 
	 * @return the text content.
	 * 	@hibernate.many-to-one 	name="textContent"
	 * 	class="edu.wustl.catissuecore.domain.pathology.TextContent"
	 * 	column="TEXT_CONTENT_ID" not-null="false"
	 */
	public TextContent getTextContent()
	{
		return textContent;
	}

	/**
	 * @param textContent sets the text content.
	 */
	public void setTextContent(TextContent textContent)
	{
		this.textContent = textContent;
	}

	
	public void setAllValues(IValueObject arg0) throws AssignDataException
	{
		// TODO Auto-generated method stub
		
	}

}