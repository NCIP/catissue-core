
package edu.wustl.catissuecore.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * @author janhavi_hasabnis
 */
public class CollectionProtocolEventBean implements Serializable
{

/**
 *
 */
	private static final long serialVersionUID = 8381530294059504778L;

	private String uniqueIdentifier;
	private Double studyCalenderEventPoint;
	private String collectionPointLabel;
	private String clinicalDiagnosis;
	private String clinicalStatus;
	private String collectionProcedure;
	private String collectionContainer;
	private String receivedQuality;
	private String collectedEventComments;
	private String receivedEventComments;
	private Map specimenRequirementbeanMap = new LinkedHashMap();
	private long id = -1;
	private long specimenCollRequirementGroupId = -1;
    /**
     * @return - studyCalenderEventPoint
     */
	public Double getStudyCalenderEventPoint()
	{
		return this.studyCalenderEventPoint;
	}
    /**
     * @param studyCalenderEventPointParam - studyCalenderEventPointParam
     */
	public void setStudyCalenderEventPoint(Double studyCalenderEventPointParam)
	{
		this.studyCalenderEventPoint = studyCalenderEventPointParam;
	}
	/**
	 * @return - collectionPointLabel
	 */
	public String getCollectionPointLabel()
	{
		return this.collectionPointLabel;
	}
	/**
	 * @param collectionPointLabelParam - collectionPointLabelParam
	 */
	public void setCollectionPointLabel(String collectionPointLabelParam)
	{
		this.collectionPointLabel = collectionPointLabelParam;
	}
	/**
	 * @return - clinicalDiagnosis
	 */
	public String getClinicalDiagnosis()
	{
		return this.clinicalDiagnosis;
	}
    /**
     * @param clinicalDiagnosisParam - clinicalDiagnosisParam
     */
	public void setClinicalDiagnosis(String clinicalDiagnosisParam)
	{
		this.clinicalDiagnosis = clinicalDiagnosisParam;
	}
    /**
     * @return - clinicalStatus
     */
	public String getClinicalStatus()
	{
		return this.clinicalStatus;
	}
	/**
	 * @param clinicalStatusParam - clinicalStatusParam
	 */
	public void setClinicalStatus(String clinicalStatusParam)
	{
		this.clinicalStatus = clinicalStatusParam;
	}
    /**
     * @return - collectionProcedure
     */
	public String getCollectionProcedure()
	{
		return this.collectionProcedure;
	}
	/**
	 * @param collectionProcedureParam - collectionProcedureParam
	 */
	public void setCollectionProcedure(String collectionProcedureParam)
	{
		this.collectionProcedure = collectionProcedureParam;
	}
	/**
	 * @return - collectionContainer
	 */
	public String getCollectionContainer()
	{
		return this.collectionContainer;
	}
	/**
	 * @param collectionContainerParam - collectionContainerParam
	 */
	public void setCollectionContainer(String collectionContainerParam)
	{
		this.collectionContainer = collectionContainerParam;
	}
	/**
	 * @return - receivedQuality
	 */
	public String getReceivedQuality()
	{
		return this.receivedQuality;
	}
	/**
	 * @param receivedQualityParam - receivedQualityParam
	 */
	public void setReceivedQuality(String receivedQualityParam)
	{
		this.receivedQuality = receivedQualityParam;
	}
	/**
	 * @return - collectedEventComments
	 */
	public String getCollectedEventComments()
	{
		return this.collectedEventComments;
	}
	/**
	 * @param collectedEventCommentsParam - collectedEventCommentsParam
	 */
	public void setCollectedEventComments(String collectedEventCommentsParam)
	{
		this.collectedEventComments = collectedEventCommentsParam;
	}
	/**
	 * @return - receivedEventComments
	 */
	public String getReceivedEventComments()
	{
		return this.receivedEventComments;
	}
	/**
	 * @param receivedEventCommentsParam - receivedEventCommentsParam
	 */
	public void setReceivedEventComments(String receivedEventCommentsParam)
	{
		this.receivedEventComments = receivedEventCommentsParam;
	}
	/**
	 * @return - Map
	 */
	public Map getSpecimenRequirementbeanMap()
	{
		if (this.specimenRequirementbeanMap == null)
		{
			this.specimenRequirementbeanMap = new LinkedHashMap();
		}
		return this.specimenRequirementbeanMap;
	}
	/**
	 * @param specimenRequirementbeanMapParam - specimenRequirementbeanMapParam
	 */
	public void setSpecimenRequirementbeanMap(Map specimenRequirementbeanMapParam)
	{
		this.specimenRequirementbeanMap = specimenRequirementbeanMapParam;
	}
	/**
	 * @param specimenRequirementBean - specimenRequirementBean
	 */
	public void addSpecimenRequirementBean(SpecimenRequirementBean specimenRequirementBean)
	{
		if (this.specimenRequirementbeanMap == null)
		{
			this.specimenRequirementbeanMap = new LinkedHashMap < String, SpecimenRequirementBean >();
		}
		this.specimenRequirementbeanMap.put( specimenRequirementBean.getUniqueIdentifier(),
				specimenRequirementBean );
	}
	/**
	 * @return - uniqueIdentifier
	 */
	public String getUniqueIdentifier()
	{
		return this.uniqueIdentifier;
	}
	/**
	 * @param uniqueIdentifierParam - uniqueIdentifierParam
	 */
	public void setUniqueIdentifier(String uniqueIdentifierParam)
	{
		this.uniqueIdentifier = uniqueIdentifierParam;
	}
	/**
	 * @return - serialVersionUID
	 */
	public static long getSerialVersionUID()
	{
		return serialVersionUID;
	}
	/**
	 * @return - id
	 */
	public long getId()
	{
		return this.id;
	}
	/**
	 * @param idParam - idParam
	 */
	public void setId(long idParam)
	{
		this.id = idParam;
	}
	/**
	 * @return - specimenCollRequirementGroupId
	 */
	public long getSpecimenCollRequirementGroupId()
	{
		return this.specimenCollRequirementGroupId;
	}
	/**
	 * @param specimenCollRequirementGroupIdParam - specimenCollRequirementGroupIdParam
	 */
	public void setSpecimenCollRequirementGroupId(long specimenCollRequirementGroupIdParam)
	{
		this.specimenCollRequirementGroupId = specimenCollRequirementGroupIdParam;
	}
		
		/**
	 * For SCG labeling,this will be exposed through API and not in the model.
	 */
	private String labelFormat;

    /**
	 * For SCG labeling,this will be exposed through API and not in the model.
	 */
	public String getLabelFormat()
	{
		return labelFormat;
	}
    /**
	 * For SCG labeling,this will be exposed through API and not in the model.
	 */
	public void setLabelFormat(String labelFormat)
	{
		this.labelFormat = labelFormat;
	}
}
