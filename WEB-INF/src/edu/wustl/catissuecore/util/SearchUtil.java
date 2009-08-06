/**
 * <p>Title: AdvanceQueryInterfaceAction Class>
 * <p>Description:	This class constructs the name-value bean of operators as per the given datatype.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Oct 25, 2005
 */

package edu.wustl.catissuecore.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.vo.HTMLField;
import edu.wustl.common.vo.SearchFieldData;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.simplequery.bizlogic.QueryBizLogic;
import edu.wustl.simplequery.query.Operator;

/**
 * @author aniruddha_phadnis
 */
public final  class SearchUtil
{
	
	/**
	 * creates a singleton object.
	 */
	private static SearchUtil searchUtil= new SearchUtil();
	
	/*
	 * Private constructor 
	 */
	private SearchUtil()
	{
			
	}
	/**
	 * returns the single object.
	 * @return AppUtility object.
	 */
	public static SearchUtil getInstance()
	{
		return searchUtil;
	}
	
	
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
     * Constant for multi select enum
     */
    public static final int DATATYPE_MULTI_ENUMERATED = 5;
    
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
	public static List<NameValueBean> getOperatorList(int dataType)
	{
		List<NameValueBean> operatorList = new ArrayList();
		operatorList.add(new NameValueBean(Constants.ANY,Constants.ANY));
		
		switch(dataType)
		{
			case DATATYPE_DATE:
			case DATATYPE_NUMERIC:		
				operatorList.add(new NameValueBean(Operator.EQUALS_CONDITION,Operator.EQUALS_CONDITION));
				operatorList.add(new NameValueBean(Operator.NOT_EQUALS_CONDITION,Operator.NOT_EQUALS_CONDITION));
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
				operatorList.add(new NameValueBean(Operator.EQUALS_CONDITION,Operator.EQUALS_CONDITION));
				operatorList.add(new NameValueBean(Operator.NOT_EQUALS_CONDITION,Operator.NOT_EQUALS_CONDITION));
				break;
				
			case DATATYPE_ENUMERATED:
				operatorList.add(new NameValueBean(Operator.EQUALS_CONDITION,Operator.EQUALS_CONDITION));
				operatorList.add(new NameValueBean(Operator.NOT_EQUALS_CONDITION,Operator.NOT_EQUALS_CONDITION));
				break;
			case DATATYPE_MULTI_ENUMERATED:
				operatorList.add(new NameValueBean(Operator.EQUALS_CONDITION,Operator.EQUALS_CONDITION));
				operatorList.add(new NameValueBean(Operator.NOT_EQUALS_CONDITION,Operator.NOT_EQUALS_CONDITION));
				operatorList.add(new NameValueBean(Operator.IN_CONDITION,Operator.IN_CONDITION));
				operatorList.add(new NameValueBean(Operator.NOT_IN_CONDITION,Operator.NOT_IN_CONDITION));
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
//		else if(objectName.equals("Specimen"))
//		{
//			link = "SpecimenAdvanceSearch.do?pageOf=pageOfSpecimenAdvanceSearch&selectedNode=";
//		}
		
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
	public static SearchFieldData initSearchUIData(String dataType,String msgKey, String aliasName,String colName,String id,
    		String opList,String valueList,String unitFieldKey)
    {
		//Initializing HTMLField class
    	HTMLField oprationField = new HTMLField(createKey(aliasName,colName,true),id+"Combo",opList);
    	HTMLField valueField = new HTMLField(createKey(aliasName,colName,false),id,valueList);
    	
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
	private static String createKey(String tableName,String fieldName,boolean isOperator)
    {
		//Checking whether the key is of operator field or not
		String key="";
    	if(isOperator)
    	{
    		key="value(Operator:" + tableName + ":" + fieldName +")";
    	}
    	else
    	{
    		key="value(" + tableName + ":" + fieldName + ")";
    	}
    	return key;
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
				searchFieldData = new SearchFieldData[9];
			    searchFieldData[0] = initSearchUIData(SearchUtil.STRING, "advanceQuery.collectionprotocol.lastname","User","LAST_NAME","lastName",Constants.STRING_OPERATORS,"","");
			    searchFieldData[1] = initSearchUIData(SearchUtil.STRING, "advanceQuery.collectionprotocol.firstname","User","FIRST_NAME","firstName",Constants.STRING_OPERATORS,"","");
			    searchFieldData[2] = initSearchUIData(SearchUtil.STRING,"collectionprotocol.protocoltitle","SpecimenProtocol","TITLE","title",Constants.STRING_OPERATORS,"","");
			    searchFieldData[3] = initSearchUIData(SearchUtil.STRING, "collectionprotocol.shorttitle","SpecimenProtocol","SHORT_TITLE","shortTitle",Constants.STRING_OPERATORS,"","");
			    searchFieldData[4] = initSearchUIData(SearchUtil.STRING, "collectionProtocolReg.participantProtocolID","CollectionProtReg","PROTOCOL_PARTICIPANT_ID","protocolParticipantId",Constants.STRING_OPERATORS,"","");
			    searchFieldData[5] = initSearchUIData(SearchUtil.DATE, "collectionProtocolReg.participantRegistrationDate","CollectionProtReg","REGISTRATION_DATE","regDate",Constants.DATE_NUMERIC_OPERATORS,"","");
			    searchFieldData[6] = initSearchUIData(SearchUtil.STRING, "collectionprotocol.irbid","SpecimenProtocol","IRB_IDENTIFIER","irbIdentifier",Constants.STRING_OPERATORS,"","");
			    searchFieldData[7] = initSearchUIData(SearchUtil.DATE, "advanceQuery.collectionprotocol.startdate","SpecimenProtocol","START_DATE","startDate",Constants.DATE_NUMERIC_OPERATORS,"","");
			    searchFieldData[8] = initSearchUIData(SearchUtil.DATE, "advanceQuery.collectionprotocol.enddate","SpecimenProtocol","END_DATE","endDate",Constants.DATE_NUMERIC_OPERATORS,"","");
			    break;
			    
			case Constants.PARTICIPANT_FORM_ID :
				searchFieldData = new SearchFieldData[12];
		        searchFieldData[0] = initSearchUIData(SearchUtil.STRING, "user.lastName","Participant","LAST_NAME","lastName",Constants.STRING_OPERATORS,"","");
		        searchFieldData[1] = initSearchUIData(SearchUtil.STRING,"user.firstName","Participant","FIRST_NAME","firstName",Constants.STRING_OPERATORS,"","");
		        searchFieldData[2] = initSearchUIData(SearchUtil.STRING, "participant.middleName","Participant","MIDDLE_NAME","middleName",Constants.STRING_OPERATORS,"","");
		        searchFieldData[3] = initSearchUIData(SearchUtil.DATE,   "participant.birthDate","Participant","BIRTH_DATE","birthDate",Constants.DATE_NUMERIC_OPERATORS,"","");		        
		        searchFieldData[4] = initSearchUIData(SearchUtil.DATE,   "participant.deathDate","Participant","DEATH_DATE","deathDate",Constants.DATE_NUMERIC_OPERATORS,"","");
		        searchFieldData[5] = initSearchUIData(SearchUtil.STRING,   "participant.vitalStatus","Participant","VITAL_STATUS","vitalStatus",Constants.STRING_OPERATORS,"","");		        
		        searchFieldData[6] = initSearchUIData(SearchUtil.STRING, "participant.gender","Participant","GENDER","gender",Constants.ENUMERATED_OPERATORS,Constants.GENDER_LIST,"");
		        searchFieldData[7] = initSearchUIData(SearchUtil.STRING, "participant.genotype","Participant","GENOTYPE","genotype",Constants.ENUMERATED_OPERATORS,Constants.GENOTYPE_LIST,"");
		        searchFieldData[8] = initSearchUIData(SearchUtil.STRING, "participant.race","Race","RACE_NAME","race",Constants.ENUMERATED_OPERATORS,Constants.RACELIST,"");
		        searchFieldData[9] = initSearchUIData(SearchUtil.STRING, "participant.ethnicity","Participant","ETHNICITY","ethnicity",Constants.ENUMERATED_OPERATORS,Constants.ETHNICITY_LIST,"");
		        searchFieldData[10] = initSearchUIData(SearchUtil.STRING, "participant.socialSecurityNumber","Participant","SOCIAL_SECURITY_NUMBER","ssn",Constants.STRING_OPERATORS,"","");
		        searchFieldData[11] = initSearchUIData(SearchUtil.STRING, "advanceQuery.specimenCollectionGroup.medicalRecordNumber","ParticipantMedicalId","MEDICAL_RECORD_NUMBER","medicalRecordNo",Constants.STRING_OPERATORS,"","");
		        break;
		        
			case Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID :
				searchFieldData = new SearchFieldData[6];
				searchFieldData[0] = initSearchUIData(SearchUtil.STRING, "specimenCollectionGroup.groupName","SpecimenCollectionGroup","NAME","groupName",Constants.STRING_OPERATORS,"","");
		        searchFieldData[1] = initSearchUIData(SearchUtil.STRING, "advanceQuery.specimenCollectionGroup.site","Site","NAME","siteName",Constants.STRING_OPERATORS,"","");//SITE_ID will be SITE_NAME
		        searchFieldData[2] = initSearchUIData(SearchUtil.NUMERIC,"specimenCollectionGroup.studyCalendarEventPoint","CollectionProtocolEvent","STUDY_CALENDAR_EVENT_POINT","studyCalendarEventPoint",Constants.DATE_NUMERIC_OPERATORS,"","");
		        searchFieldData[3] = initSearchUIData(SearchUtil.STRING,"specimenCollectionGroup.clinicalDiagnosis","SpecimenCollectionGroup","CLINICAL_DIAGNOSIS","clinicalDiagnosis",Constants.ENUMERATED_OPERATORS,Constants.CLINICAL_DIAGNOSIS_LIST,"");
		        searchFieldData[4] = initSearchUIData(SearchUtil.STRING,"specimenCollectionGroup.clinicalStatus","SpecimenCollectionGroup","CLINICAL_STATUS","clinicalStatus",Constants.ENUMERATED_OPERATORS,Constants.CLINICAL_STATUS_LIST,"");
		        searchFieldData[5] = initSearchUIData(SearchUtil.STRING,"specimenCollectionGroup.surgicalPathologyNumber","ClinicalReport","SURGICAL_PATHOLOGICAL_NUMBER","surgicalPathologyNo",Constants.STRING_OPERATORS,"","");
				break;
				
			case Constants.NEW_SPECIMEN_FORM_ID :
			    /* Aarti: Bug#1496- To allow query on initial quantity as well as available quantity 
			     * Added field data for available quantity*/
				searchFieldData = new SearchFieldData[10];
				searchFieldData[0] = initSearchUIData(SearchUtil.STRING, "specimen.type","Specimen","SPECIMEN_CLASS","className",Constants.ENUMERATED_OPERATORS,Constants.SPECIMEN_CLASS_LIST,"");
				searchFieldData[1] = initSearchUIData(SearchUtil.STRING,"specimen.subType","Specimen","TYPE","type",Constants.ENUMERATED_OPERATORS,Constants.SPECIMEN_TYPE_LIST,"");
				searchFieldData[2] = initSearchUIData(SearchUtil.STRING, "specimen.tissueSite","SpecimenCharacteristics","TISSUE_SITE","tissueSite",Constants.ENUMERATED_OPERATORS,Constants.TISSUE_SITE_LIST,"");
				searchFieldData[3] = initSearchUIData(SearchUtil.STRING,"specimen.tissueSide","SpecimenCharacteristics","TISSUE_SIDE","tissueSide",Constants.ENUMERATED_OPERATORS,Constants.TISSUE_SIDE_LIST,"");
				searchFieldData[4] = initSearchUIData(SearchUtil.STRING, "specimen.pathologicalStatus","Specimen","PATHOLOGICAL_STATUS","pathologicalStatus",Constants.ENUMERATED_OPERATORS,Constants.PATHOLOGICAL_STATUS_LIST,"");
				searchFieldData[5] = initSearchUIData(SearchUtil.NUMERIC, "specimen.concentration","Specimen","CONCENTRATION","concentration",Constants.DATE_NUMERIC_OPERATORS,"","");
				searchFieldData[6] = initSearchUIData(SearchUtil.NUMERIC, "specimen.availableQuantity","Specimen","AVAILABLE_QUANTITY","availableQuantity",Constants.DATE_NUMERIC_OPERATORS,"","");
				searchFieldData[7] = initSearchUIData(SearchUtil.NUMERIC, "specimen.quantity","Specimen","QUANTITY","quantity",Constants.DATE_NUMERIC_OPERATORS,"","");
				/**
				 * Name : Prafull
				 * Bug ID: 4171
				 * Patch ID: 4171_4
				 * Description: Adding Specimen.createdOn attribute in the Advance Search.
				 */
		        searchFieldData[3] = initSearchUIData(SearchUtil.DATE,   "specimen.createdDate","Specimen","CREATED_ON_DATE","createdDate",Constants.DATE_NUMERIC_OPERATORS,"","");		        
				searchFieldData[8] = initSearchUIData(SearchUtil.STRING, "specimen.biohazardType","Biohazard","TYPE","biohazardType",Constants.ENUMERATED_OPERATORS,Constants.BIOHAZARD_TYPE_LIST,"");
				searchFieldData[9] = initSearchUIData(SearchUtil.STRING, "specimen.biohazardName","Biohazard","NAME","biohazardName",Constants.STRING_OPERATORS,"","");
				/* END Bug#1496 */
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
		
		if(pageName.equals(Constants.COLLECTION_PROTOCOL))
		{
			searchFieldData = getSearchFieldData(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
		}
		else if(pageName.equals(Constants.PARTICIPANT))
		{
			searchFieldData = getSearchFieldData(Constants.PARTICIPANT_FORM_ID);
		}
		else if(pageName.equals(Constants.SPECIMEN_COLLECTION_GROUP))
		{
			searchFieldData = getSearchFieldData(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
		}
		else if(pageName.equals(Constants.SPECIMEN))
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
		
		return map;
	}
	
	public static int getFormId(String aliasName)
	{
		int formId = 0;
		if(aliasName.equals("Participant"))
		{
			formId = Constants.PARTICIPANT_FORM_ID;
		}
		else if(aliasName.equals("CollectionProtocol"))
		{
			formId = Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID;
		}
		else if(aliasName.equals("Specimen"))
		{
			formId = Constants.NEW_SPECIMEN_FORM_ID;
		}
		else if(aliasName.equals("SpecimenCollectionGroup"))
		{
			formId = Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID;
		}
		
		return formId;
	}
	
	public static String getColumnDisplayName(int formId,String tableName,String columnName)
	{
		SearchFieldData[] searchFieldData = getSearchFieldData(formId);
		String columnDisplayName = "";
		String colName="";
		if(searchFieldData != null)
		{
			String name = createKey(tableName,columnName,false);
						
			for(int i = 0; i < searchFieldData.length; i++)
			{
				String key = searchFieldData[i].getValueField().getName();
				if(key.equals(name))
				{
					columnDisplayName = searchFieldData[i].getLabelKey();
					break;
				}
			}
		}
		
		if(!columnDisplayName.equals(""))
		{
			ResourceBundle myResources =ResourceBundle.getBundle("ApplicationResources");
			colName = myResources.getString(columnDisplayName);
		}
		else 
		{
			colName = columnDisplayName;
		}
		return colName;
	}
	
	/**
	 * Returns a list of Name-Value beans which contains the table names with corresponding alias names.
	 * The list contains all the tables associated with Event Parameters only.
	 * @param bizLogic The object of class QueryBizLogic
	 * @return a list of table names.
	 */
	 public static List getEventParametersTables(QueryBizLogic bizLogic) throws DAOException,ClassNotFoundException
	 {
	  	Set tableSet = bizLogic.getAllTableNames("", Constants.ADVANCE_QUERY_TABLES);
	  	List newTableList = new ArrayList();
	  	
	  	//Adding SELECT Option
	  	newTableList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
	  	
	  	Iterator it = tableSet.iterator();
	  	
	  	while(it.hasNext())
	  	{
	  		NameValueBean bean = (NameValueBean) it.next();
	  		
	  		//Adding tables related to Event Parameters only.
	  		if(bean.getName().endsWith("Parameters") || bean.getName().endsWith("Parameter"))
	  		{
	  			newTableList.add(bean);
	  		}
	  	}
	  	
	  	return newTableList;
	}
	
	public static Map getEventParametersDisplayNames(QueryBizLogic bizLogic, List tableList) throws DAOException,ClassNotFoundException
	{
		HashMap displayNamesMap = new HashMap();
		
		//Extracting column names per table name & generating the map
    	for(int i=1;i<tableList.size();i++)
    	{
    		NameValueBean bean = (NameValueBean)tableList.get(i);
    		String aliasName = bean.getValue();
    		String tableDisplayName = bean.getName();
    		
    		List columnList = bizLogic.getColumnNames(aliasName);
    		
    		for(int j=0;j<columnList.size();j++)
    		{
    			NameValueBean colBean = (NameValueBean)columnList.get(j);
    			String columnName = colBean.getName();
    			
    			StringTokenizer tokenizer = new StringTokenizer(colBean.getValue(),".");
    			
    			tokenizer.nextToken();
    			String columnValue = tokenizer.nextToken();
    			
    			String key = aliasName + "." + columnValue;
    			String value = tableDisplayName + "." + columnName;
    			
    			displayNamesMap.put(key,value);
    		}
    	}
		
		return displayNamesMap;
	}
	
	
	/**
     * check not null
     * @param object object
     * @return boolean
     */

    public static boolean isNullobject(Object object)
    {	boolean flag = true;
	    if (object != null)
	    {
	       flag = false;
	    }
	    return flag;
    }


}