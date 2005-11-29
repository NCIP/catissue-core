/**
 * <p>Title: AdvanceQueryInterfaceAction Class>
 * <p>Description:	This class constructs the name-value bean of operators as per the given datatype.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Oct 25, 2005
 */

package edu.wustl.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.query.Operator;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.vo.HTMLField;
import edu.wustl.catissuecore.vo.SearchFieldData;
import edu.wustl.common.beans.NameValueBean;

/**
 * @author aniruddha_phadnis
 */
public class SearchUtil
{
	/**
     * Constant for datatype DATE
     */
    public static final int DATATYPE_DATE = 1;
    
    /**
     * Constant for datatype NUMERIC
     */
    public static final int DATATYPE_NUMERIC = 2;
        
    /**
     * Constant for datatype STRING
     */
    public static final int DATATYPE_STRING = 3;
    
    /**
     * Constant for datatype STRING
     */
    public static final int DATATYPE_ENUMERATED = 4;
    
    /**
     * Constant for datatype STRING
     */
    public static final String STRING = "String";
    
    /**
     * Constant for datatype DATE
     */
    public static final String DATE = "Date";
    
    /**
     * Constant for datatype NUMERIC
     */
    public static final String NUMERIC = "Numeric";
    
    
	//Function returns the list of operators as per the datatype.
	public static List getOperatorList(int dataType)
	{
		List operatorList = new ArrayList();
		operatorList.add(new NameValueBean(Constants.ANY,Constants.ANY));
		
		switch(dataType)
		{
			case DATATYPE_DATE:
			case DATATYPE_NUMERIC:		
				operatorList.add(new NameValueBean(Operator.EQUALS_CONDITION,Operator.EQUAL));
				operatorList.add(new NameValueBean(Operator.NOT_EQUALS_CONDITION,Operator.NOT_EQUALS));
				operatorList.add(new NameValueBean(Operator.LESS_THAN,Operator.LESS_THAN));
				operatorList.add(new NameValueBean(Operator.LESS_THAN_OR_EQUALS,Operator.LESS_THAN_OR_EQUALS));
				operatorList.add(new NameValueBean(Operator.GREATER_THAN,Operator.GREATER_THAN));
				operatorList.add(new NameValueBean(Operator.GREATER_THAN_OR_EQUALS,Operator.GREATER_THAN_OR_EQUALS));
				operatorList.add(new NameValueBean(Operator.BETWEEN,Operator.BETWEEN));
				operatorList.add(new NameValueBean(Operator.NOT_BETWEEN,Operator.NOT_BETWEEN));
				break;
			
			case DATATYPE_STRING:
				operatorList.add(new NameValueBean(Operator.STARTS_WITH,Operator.STARTS_WITH));
				operatorList.add(new NameValueBean(Operator.ENDS_WITH,Operator.ENDS_WITH));
				operatorList.add(new NameValueBean(Operator.CONTAINS,Operator.CONTAINS));
				operatorList.add(new NameValueBean(Operator.EQUALS_CONDITION,Operator.EQUAL));
				operatorList.add(new NameValueBean(Operator.NOT_EQUALS_CONDITION,Operator.NOT_EQUALS));
				break;
				
			case DATATYPE_ENUMERATED:
				operatorList.add(new NameValueBean(Operator.EQUALS_CONDITION,Operator.EQUAL));
				operatorList.add(new NameValueBean(Operator.NOT_EQUALS_CONDITION,Operator.NOT_EQUALS));
				break;
		}
		
		return operatorList;
	}
	
	public static String getLink(String objectName)
	{
		String link = "";
		
		if(objectName.equals("Participant"))
		{
			link = "ParticipantAdvanceSearch.do?pageOf=pageOfParticipantAdvanceSearch&selectedNode=";
		}
		else if(objectName.equals("CollectionProtocol"))
		{
			link = "CollectionProtocolAdvanceSearch.do?pageOf=pageOfCollectionProtocolAdvanceSearch&selectedNode=";
		}
		else if(objectName.equals("Specimen"))
		{
			link = "SpecimenAdvanceSearch.do?pageOf=pageOfSpecimenAdvanceSearch&selectedNode=";
		}
		else if(objectName.equals("SpecimenCollectionGroup"))
		{
			link = "SpecimenCollectionGroupAdvanceSearch.do?pageOf=pageOfSpecimenCollectionGroupAdvanceSearch&selectedNode=";
		}
		else if(objectName.equals("Specimen"))
		{
			link = "SpecimenAdvanceSearch.do?pageOf=pageOfSpecimenAdvanceSearch&selectedNode=";
		}
		
		return link;
	}
	
	/**
	 * Used for initializing HTMLField class which set the value of each field(text,comboBox etc)
	 * @param dataType  Datatype of field
	 * @param msgKey  Label of row
	 * @param tblName  Used for creating key of field
	 * @param colName  Represents name of field
	 * @param id  Represents id of field
	 * @param opList  For populating value of comboBox
	 * @param valueList  For populating value in case of comboBox in 3rd column of table
	 * @param unitFieldKey  Used for specimen page only 
	 * @return  instance of SearchFieldData
	 */	
	public static SearchFieldData initSearchUIData(String dataType,String msgKey, String tblName,String colName,String id,
    		String opList,String valueList,String unitFieldKey)
    {
		//Initializing HTMLField class
    	HTMLField oprationField = new HTMLField(createKey(tblName,colName,true,dataType),id+"Combo",opList);
    	HTMLField valueField = new HTMLField(createKey(tblName,colName,false,dataType),id,valueList);
    	
    	//Function Name used according to dataType of field
    	String funcName = "";
    	
    	//Condition for diffent function name for enabing/disabling next field
    	if( dataType.equals(SearchUtil.DATE) || (dataType).equals(SearchUtil.NUMERIC))
    	{
    		//Passing 3 parameters out of this two are representing id of textbox
			funcName = "onDateOperatorChange(this,'" + id + "1','" + id + "2')";
    	}
    	else
    	{
    		//passing id of field from where function is called and next field's id
    		funcName = "onOperatorChange('" + (id+"Combo") + "','" + id +"')";
    	}
    	
    	return new SearchFieldData(dataType, msgKey ,oprationField,valueField,funcName,unitFieldKey);
    }
	
	/**
	 * Used for creating key of field which is further used for mapping
	 * @param tableName Used for creating key of field
	 * @param fieldName Represents name of field
	 * @param isOperator Checking whether the key is of operator field or not
	 * @param dataType Datatype of field
	 * @return
	 */	
	private static String createKey(String tableName,String fieldName,boolean isOperator,String dataType)
    {
		//Checking whether the key is of operator field or not
    	if(isOperator)
    	{
    		return "value(Operator:" + tableName + ":" + fieldName +")";
    	}
    	else
    	{
    		return "value(" + tableName + ":" + fieldName + ")";
    	}
    }
	
	/**
	 * Returns an array of objects, of SearchFieldData, that is required to construct
	 * the advanced search pages. 
	 * @param formId The unique identifier of a formbean
	 * @return an array of objects of SearchFieldData.
	 */
	public static SearchFieldData[] getSearchFieldData(int formId)
	{
		SearchFieldData[] searchFieldData = null;
		
		switch(formId)
		{
			case Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID :
				searchFieldData = new SearchFieldData[8];
		    	searchFieldData[0] = initSearchUIData(SearchUtil.STRING, "collectionprotocol.principalinvestigator","SpecimenProtocol","PRINCIPAL_INVESTIGATOR_ID","principalInvestigator",Constants.ENUMERATED_OPERATORS,Constants.USERLIST,"");
		    	searchFieldData[1] = initSearchUIData(SearchUtil.STRING,"collectionprotocol.protocoltitle","SpecimenProtocol","TITLE","title",Constants.STRING_OPERATORS,"","");
		    	searchFieldData[2] = initSearchUIData(SearchUtil.STRING, "collectionprotocol.shorttitle","SpecimenProtocol","SHORT_TITLE","shortTitle",Constants.STRING_OPERATORS,"","");
		    	searchFieldData[3] = initSearchUIData(SearchUtil.STRING, "collectionprotocol.irbid","SpecimenProtocol","IRB_IDENTIFIER","irbIdentifier",Constants.STRING_OPERATORS,"","");
		    	searchFieldData[4] = initSearchUIData(SearchUtil.DATE, "collectionprotocol.startdate","SpecimenProtocol","START_DATE","startDate",Constants.DATE_NUMERIC_OPERATORS,"","");
		    	searchFieldData[5] = initSearchUIData(SearchUtil.DATE, "collectionprotocol.enddate","SpecimenProtocol","END_DATE","endDate",Constants.DATE_NUMERIC_OPERATORS,"","");
		    	searchFieldData[6] = initSearchUIData(SearchUtil.STRING, "collectionProtocolReg.participantProtocolID","CollectionProtocolRegistration","PROTOCOL_PARTICIPANT_IDENTIFIER","protocolParticipantId",Constants.STRING_OPERATORS,"","");
		    	searchFieldData[7] = initSearchUIData(SearchUtil.DATE, "collectionProtocolReg.participantRegistrationDate","CollectionProtocolRegistration","REGISTRATION_DATE","regDate",Constants.DATE_NUMERIC_OPERATORS,"","");
	    		break;
			case Constants.PARTICIPANT_FORM_ID :
				searchFieldData = new SearchFieldData[9];
		        searchFieldData[0] = initSearchUIData(SearchUtil.STRING, "user.lastName","Participant","LAST_NAME","lastName",Constants.STRING_OPERATORS,"","");
		        searchFieldData[1] = initSearchUIData(SearchUtil.STRING,"user.firstName","Participant","FIRST_NAME","firstName",Constants.STRING_OPERATORS,"","");
		        searchFieldData[2] = initSearchUIData(SearchUtil.STRING, "participant.middleName","Participant","MIDDLE_NAME","middleName",Constants.STRING_OPERATORS,"","");
		        searchFieldData[3] = initSearchUIData(SearchUtil.DATE,   "participant.birthDate","Participant","BIRTH_DATE","birthDate",Constants.DATE_NUMERIC_OPERATORS,"","");
		        searchFieldData[4] = initSearchUIData(SearchUtil.STRING, "participant.gender","Participant","GENDER","gender",Constants.ENUMERATED_OPERATORS,Constants.GENDER_LIST,"");
		        searchFieldData[5] = initSearchUIData(SearchUtil.STRING, "participant.genotype","Participant","GENOTYPE","genotype",Constants.ENUMERATED_OPERATORS,Constants.GENOTYPE_LIST,"");
		        searchFieldData[6] = initSearchUIData(SearchUtil.STRING, "participant.race","Participant","RACE","race",Constants.ENUMERATED_OPERATORS,Constants.RACELIST,"");
		        searchFieldData[7] = initSearchUIData(SearchUtil.STRING, "participant.ethnicity","Participant","ETHNICITY","ethnicity",Constants.ENUMERATED_OPERATORS,Constants.ETHNICITY_LIST,"");
		        searchFieldData[8] = initSearchUIData(SearchUtil.STRING, "participant.socialSecurityNumber","Participant","SOCIAL_SECURITY_NUMBER","ssn",Constants.STRING_OPERATORS,"","");
		        break;
			case Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID :
				searchFieldData = new SearchFieldData[6];
		        searchFieldData[0] = initSearchUIData(SearchUtil.STRING, "specimenCollectionGroup.site","SpecimenCollectionGroup","SITE_ID","siteName",Constants.STRING_OPERATORS,"","");//SITE_ID will be SITE_NAME
		        searchFieldData[1] = initSearchUIData(SearchUtil.DATE,"specimenCollectionGroup.studyCalendarEventPoint","CollectionProtocolEvent","STUDY_CALENDAR_EVENT_POINT","studyCalendarEventPoint",Constants.DATE_NUMERIC_OPERATORS,"","");
		        searchFieldData[2] = initSearchUIData(SearchUtil.STRING,"specimenCollectionGroup.clinicalDiagnosis","SpecimenCollectionGroup","CLINICAL_DIAGNOSIS","clinicalDiagnosis",Constants.ENUMERATED_OPERATORS,Constants.CLINICAL_DIAGNOSIS_LIST,"");
		        searchFieldData[3] = initSearchUIData(SearchUtil.STRING,"specimenCollectionGroup.clinicalStatus","SpecimenCollectionGroup","CLINICAL_STATUS","clinicalStatus",Constants.ENUMERATED_OPERATORS,Constants.CLINICAL_STATUS_LIST,"");
		        searchFieldData[4] = initSearchUIData(SearchUtil.STRING,"specimenCollectionGroup.medicalRecordNumber","ParticipantMedicalIdentifier","MEDICAL_RECORD_NUMBER","medicalRecordNo",Constants.STRING_OPERATORS,"","");
		        searchFieldData[5] = initSearchUIData(SearchUtil.STRING,"specimenCollectionGroup.surgicalPathologyNumber","ClinicalReport","SURGICAL_PATHOLOGICAL_NUMBER","surgicalPathologyNo",Constants.STRING_OPERATORS,"","");
				break;
			case Constants.NEW_SPECIMEN_FORM_ID :
				searchFieldData = new SearchFieldData[9];
				searchFieldData[0] = initSearchUIData(SearchUtil.STRING, "specimen.type","Specimen","CLASS_NAME","className",Constants.ENUMERATED_OPERATORS,Constants.SPECIMEN_CLASS_LIST,"");
				searchFieldData[1] = initSearchUIData(SearchUtil.STRING,"specimen.subType","Specimen","TYPE","type",Constants.ENUMERATED_OPERATORS,Constants.SPECIMEN_TYPE_LIST,"");
				searchFieldData[2] = initSearchUIData(SearchUtil.STRING, "specimen.tissueSite","SpecimenCharacteristics","TISSUE_SITE","tissueSite",Constants.ENUMERATED_OPERATORS,Constants.TISSUE_SITE_LIST,"");
				searchFieldData[3] = initSearchUIData(SearchUtil.STRING,"specimen.tissueSide","SpecimenCharacteristics","TISSUE_SIDE","tissueSide",Constants.ENUMERATED_OPERATORS,Constants.TISSUE_SIDE_LIST,"");
				searchFieldData[4] = initSearchUIData(SearchUtil.DATE, "specimen.pathologicalStatus","SpecimenCharacteristics","PATHOLOGICAL_STATUS","pathologicalStatus",Constants.ENUMERATED_OPERATORS,Constants.PATHOLOGICAL_STATUS_LIST,"");
				searchFieldData[5] = initSearchUIData(SearchUtil.NUMERIC, "specimen.concentration","Specimen","CONCENTRATION","concentration",Constants.DATE_NUMERIC_OPERATORS,"","");
				searchFieldData[6] = initSearchUIData(SearchUtil.NUMERIC, "specimen.quantity","Specimen","QUANTITY","quantity",Constants.DATE_NUMERIC_OPERATORS,"","");
				searchFieldData[7] = initSearchUIData(SearchUtil.STRING, "specimen.biohazardType","Biohazard","TYPE","biohazardType",Constants.ENUMERATED_OPERATORS,Constants.BIOHAZARD_TYPE_LIST,"");
				searchFieldData[8] = initSearchUIData(SearchUtil.DATE, "specimen.biohazardName","Biohazard","NAME","biohazardName",Constants.STRING_OPERATORS,"","");
		}
		
		return searchFieldData;
	}
	
	/**
	 * Returns a map of Resource Bundle keys & datatypes for a particular page
	 * @param pageName The name of a advanced search page
	 * @return an array of objects of SearchFieldData.
	 */
	public static Map getResourceBundleMap(String pageName)
	{
		SearchFieldData[] searchFieldData = null;
		
		if(pageName.equals("CollectionProtocol"))
		{
			searchFieldData = getSearchFieldData(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
		}
		else if(pageName.equals("Participant"))
		{
			searchFieldData = getSearchFieldData(Constants.PARTICIPANT_FORM_ID);
		}
		else if(pageName.equals("SpecimenCollectionGroup"))
		{
			searchFieldData = getSearchFieldData(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
		}
		else if(pageName.equals("Specimen"))
		{
			searchFieldData = getSearchFieldData(Constants.NEW_SPECIMEN_FORM_ID);
		}

		HashMap map = new HashMap();
		
		for(int i=0;i<searchFieldData.length;i++)
		{
			String key = searchFieldData[i].getValueField().getName().trim();
			key = key.substring(key.indexOf("(")+1,key.length()-1);
			
			String labelName = searchFieldData[i].getLabelKey();
			String dataType = searchFieldData[i].getDataType();
			
			map.put(key,new NameValueBean(labelName,dataType));
		}
		
//		/******** TO BE REMOVED LATER *****************/
//		map = new HashMap();
//		map.put("Participant:RACE",new NameValueBean("participant.race","String"));
//		map.put("Participant:GENOTYPE",new NameValueBean("participant.genotype","String"));
//		map.put("Participant:MIDDLE_NAME",new NameValueBean("participant.genotype","String"));
//		map.put("Participant:FIRST_NAME",new NameValueBean("user.firstName","String"));
//		map.put("Participant:ETHNICITY",new NameValueBean("participant.ethnicity","String"));
//		map.put("Participant:GENDER",new NameValueBean("participant.gender","String"));
//		map.put("Participant:LAST_NAME",new NameValueBean("user.lastName","String"));
//		map.put("Participant:SOCIAL_SECURITY_NUMBER",new NameValueBean("participant.socialSecurityNumber","String"));
//		map.put("Participant:BIRTH_DATE",new NameValueBean("participant.birthDate","Date"));
//		/******** TO BE REMOVED LATER *****************/
		
		return map;
	}
}