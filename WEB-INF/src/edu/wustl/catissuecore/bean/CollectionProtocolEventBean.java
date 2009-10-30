
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
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 8381530294059504778L;
	/**
	 * uniqueIdentifier.
	 */
	private String uniqueIdentifier;
	/**
	 * studyCalenderEventPoint.
	 */
	private Double studyCalenderEventPoint;
	/**
	 * collectionPointLabel.
	 */
	private String collectionPointLabel;
	/**
	 * clinicalDiagnosis.
	 */
	private String clinicalDiagnosis;
	/**
	 * clinicalStatus.
	 */
	private String clinicalStatus;
	/**
	 * collectionProcedure.
	 */
	private String collectionProcedure;
	/**
	 * collectionContainer.
	 */
	private String collectionContainer;
	/**
	 * receivedQuality.
	 */
	private String receivedQuality;
	/**
	 * collectedEventComments.
	 */
	private String collectedEventComments;
	/**
	 * receivedEventComments.
	 */
	private String receivedEventComments;
	/**
	 * specimenRequirementbeanMap.
	 */
	private Map specimenRequirementbeanMap = new LinkedHashMap();
	/**
	 * id.
	 */
	private long id = -1;
	/**
	 * specimenCollRequirementGroupId.
	 */
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
			this.specimenRequirementbeanMap = new LinkedHashMap<String, SpecimenRequirementBean>();
		}
		this.specimenRequirementbeanMap.put(specimenRequirementBean.getUniqueIdentifier(),
				specimenRequirementBean);
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
	 * @return String
	 */
	public String getLabelFormat()
	{
		return this.labelFormat;
	}

	/**
	 * For SCG labeling,this will be exposed through API and not in the model.
	 * @param labelFormat String.
	 */
	public void setLabelFormat(String labelFormat)
	{
		this.labelFormat = labelFormat;
	}
}
