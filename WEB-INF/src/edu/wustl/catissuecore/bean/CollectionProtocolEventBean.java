package edu.wustl.catissuecore.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;


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
	private long id=-1;
	
	public Double getStudyCalenderEventPoint()
	{
		return studyCalenderEventPoint;
	}
	
	public void setStudyCalenderEventPoint(Double studyCalenderEventPoint)
	{
		this.studyCalenderEventPoint = studyCalenderEventPoint;
	}
	
	public String getCollectionPointLabel()
	{
		return collectionPointLabel;
	}
	
	public void setCollectionPointLabel(String collectionPointLabel)
	{
		this.collectionPointLabel = collectionPointLabel;
	}
	
	public String getClinicalDiagnosis()
	{
		return clinicalDiagnosis;
	}
	
	public void setClinicalDiagnosis(String clinicalDiagnosis)
	{
		this.clinicalDiagnosis = clinicalDiagnosis;
	}
	
	public String getClinicalStatus()
	{
		return clinicalStatus;
	}
	
	public void setClinicalStatus(String clinicalStatus)
	{
		this.clinicalStatus = clinicalStatus;
	}

	
	public String getCollectionProcedure()
	{
		return collectionProcedure;
	}

	
	public void setCollectionProcedure(String collectionProcedure)
	{
		this.collectionProcedure = collectionProcedure;
	}

	
	public String getCollectionContainer()
	{
		return collectionContainer;
	}

	
	public void setCollectionContainer(String collectionContainer)
	{
		this.collectionContainer = collectionContainer;
	}

	
	public String getReceivedQuality()
	{
		return receivedQuality;
	}

	
	public void setReceivedQuality(String receivedQuality)
	{
		this.receivedQuality = receivedQuality;
	}

	
	public String getCollectedEventComments()
	{
		return collectedEventComments;
	}

	
	public void setCollectedEventComments(String collectedEventComments)
	{
		this.collectedEventComments = collectedEventComments;
	}

	
	public String getReceivedEventComments()
	{
		return receivedEventComments;
	}

	
	public void setReceivedEventComments(String receivedEventComments)
	{
		this.receivedEventComments = receivedEventComments;
	}

	
	public Map getSpecimenRequirementbeanMap()
	{
		if(specimenRequirementbeanMap==null)
			specimenRequirementbeanMap = new LinkedHashMap();
		return specimenRequirementbeanMap;
	}

	
	public void setSpecimenRequirementbeanMap(Map specimenRequirementbeanMap)
	{
		this.specimenRequirementbeanMap = specimenRequirementbeanMap;
	}

	public void addSpecimenRequirementBean(SpecimenRequirementBean specimenRequirementBean)
	{
		if(specimenRequirementbeanMap==null)
		{
			specimenRequirementbeanMap = new LinkedHashMap<String,SpecimenRequirementBean>();
		}
		specimenRequirementbeanMap.put(specimenRequirementBean.getUniqueIdentifier(), specimenRequirementBean);
	}

	
	public String getUniqueIdentifier()
	{
		return uniqueIdentifier;
	}

	
	public void setUniqueIdentifier(String uniqueIdentifier)
	{
		this.uniqueIdentifier = uniqueIdentifier;
	}
	
		public static long getSerialVersionUID()
	{
		return serialVersionUID;
	}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}	
}
