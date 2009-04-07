/*
 * Created on Nov 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.AdvanceQueryBizlogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.querysuite.bizlogic.QueryBizLogic;
import edu.wustl.common.querysuite.queryobject.impl.Condition;
import edu.wustl.common.querysuite.queryobject.impl.Query;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.QuerySessionData;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.simplequery.bizlogic.SimpleQueryBizLogic;
import edu.wustl.simplequery.query.DataElement;
import edu.wustl.simplequery.query.Operator;
import edu.wustl.simplequery.query.QueryFactory;
import edu.wustl.simplequery.query.Table;

/**
 * @author poornima_govindrao
 *
 * Action which forms the Advance query object and 
 * creates a Temporary table with the search results
 */
public class AdvanceSearchResultsAction extends BaseAction
{
	/**
	 * Overrides the execute method of Action class.
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 * @throws Exception generic exception
	 */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		//Query Start object
		String aliasName = Constants.PARTICIPANT;
		String pageOf=Constants.PAGEOF_QUERY_RESULTS;
		AdvanceQueryBizlogic advBizLogic = (AdvanceQueryBizlogic)BizLogicFactory.getInstance().getBizLogic(Constants.ADVANCE_QUERY_INTERFACE_ID);
		String target = Constants.SUCCESS;
		//Get the advance query root object from session 
		HttpSession session = request.getSession();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)session.getAttribute(Constants.ADVANCED_CONDITIONS_ROOT);
		//Bug 2004:Make a copy of the original query root object so that any modifications to the tree object does 
		//not reflect when query is redefined.
		DefaultMutableTreeNode originalQueryObject =new DefaultMutableTreeNode();
		copy(root,originalQueryObject);
		if(originalQueryObject.getChildCount()==0)
		{
			ActionErrors errors = new ActionErrors();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"advanceQuery.noConditions"));
			saveErrors(request, errors);
			pageOf=Constants.PAGEOF_ADVANCE_QUERY_INTERFACE;
			target = Constants.FAILURE;
		}
		else
		{
			//Create Advance Query object
			Query query = QueryFactory.getInstance().newQuery(Query.ADVANCED_QUERY, aliasName);
			
			//Set the root object as the where conditions
			((AdvancedConditionsImpl)((AdvancedQuery)query).getWhereConditions()).setWhereCondition(originalQueryObject);
			//Set activity Status conditions to filter data which are disabled 
			Vector tablesVector = new Vector();
			tablesVector.add(Query.PARTICIPANT);
			tablesVector.add(Query.SPECIMEN_PROTOCOL);
			tablesVector.add(Query.COLLECTION_PROTOCOL_REGISTRATION);
			tablesVector.add(Query.SPECIMEN_COLLECTION_GROUP);
			tablesVector.add(Query.SPECIMEN);
			String activityStatusConditions = advBizLogic.formActivityStatusConditions(tablesVector,query.getTableSufix());
			query.setActivityStatusConditions(activityStatusConditions);
			
			//Set the table set for join Condition 
			Set tableSet = new HashSet();
			advBizLogic.setTables(originalQueryObject,tableSet);
			//setTablesDownTheHeirarchy(tableSet);
			query.setTableSet(tableSet);
			
			List columnNames = new ArrayList();
			SimpleQueryBizLogic bizLogic = new SimpleQueryBizLogic(); 
			
			//Set attribute type in the DataElement	
			setAttributeType(originalQueryObject);
			
			//Set Identifier of Participant,Collection Protocol, Specimen Collection Group and Specimen if not existing in the resultView
			tablesVector.remove(Query.SPECIMEN_PROTOCOL);
			tablesVector.add(Query.COLLECTION_PROTOCOL);
			query.getIdentifierColumnIds(tablesVector);
			
			//Set tables for Configuration.
			if(query.getTableNamesSet().contains(Query.BIO_HAZARD))
			{
				tablesVector.add(Query.BIO_HAZARD);
			}
			Object[] selectedTables = tablesVector.toArray();
			session.setAttribute(Constants.TABLE_ALIAS_NAME,selectedTables);
			
			Logger.out.debug("tableSet from query before setting resultview :"+query.getTableNamesSet());
			//Set the result view for Advance Search
			Vector selectDataElements = bizLogic.getSelectDataElements(null,new ArrayList(tablesVector), columnNames, false);

			query.setResultView(selectDataElements);
			
			Map queryResultObjectDataMap = new HashMap();

			SimpleQueryBizLogic simpleQueryBizLogic = new SimpleQueryBizLogic();
			
			simpleQueryBizLogic.createQueryResultObjectData(query.getTableNamesSet(),
											queryResultObjectDataMap,query);
			simpleQueryBizLogic.addObjectIdentifierColumnsToQuery(
											queryResultObjectDataMap, query);

			simpleQueryBizLogic.setDependentIdentifiedColumnIds(
											queryResultObjectDataMap, query);
			
			Map columnIdsMap = query.getColumnIdsMap();
			session.setAttribute(Constants.COLUMN_ID_MAP,columnIdsMap);
			Logger.out.debug("map of column ids:"+columnIdsMap+":"+columnIdsMap.size());
			
			SessionDataBean sessionData = getSessionData(request);
			//Temporary table name with userID attached
			String tempTableName = Constants.QUERY_RESULTS_TABLE+"_"+sessionData.getUserId();
			
			QueryBizLogic queryBizLogic = (QueryBizLogic)AbstractBizLogicFactory.getBizLogic(
	            	ApplicationProperties.getValue("app.bizLogicFactory"),
					"getBizLogic", Constants.QUERY_INTERFACE_ID);
			//Create temporary table with the data from the Advance Query Search 
			String sql = query.getString();
			queryBizLogic.insertQuery(sql,getSessionData(request));
//			String sql = "Select Participant2.IDENTIFIER  Column0 , Participant2.LAST_NAME  Column1 , Participant2.FIRST_NAME  Column2 , Participant2.MIDDLE_NAME  Column3 , Participant2.BIRTH_DATE  Column4 , Participant2.DEATH_DATE  Column5 , Participant2.VITAL_STATUS  Column6 , Participant2.GENDER  Column7 , Participant2.GENOTYPE  Column8 , Participant2.RACE  Column9 , Participant2.ETHNICITY  Column10 , Participant2.SOCIAL_SECURITY_NUMBER  Column11 , ParticipantMedicalId1.MEDICAL_RECORD_NUMBER  Column12 , CollectionProtReg1.IDENTIFIER  Column13 , SpecimenProtocol1.TITLE  Column14 , CollectionProtReg1.PROTOCOL_PARTICIPANT_ID  Column15 , CollectionProtReg1.REGISTRATION_DATE  Column16 , Participant2.LAST_NAME  Column17 , Participant2.FIRST_NAME  Column18 , Participant2.MIDDLE_NAME  Column19 , Participant2.BIRTH_DATE  Column20 , SpecimenCollectionGroup1.IDENTIFIER  Column21 , SpecimenCollectionGroup1.CLINICAL_DIAGNOSIS  Column22 , SpecimenCollectionGroup1.CLINICAL_STATUS  Column23 , SpecimenProtocol1.TITLE  Column24 , CollectionProtocolEvent1.STUDY_CALENDAR_EVENT_POINT  Column25 , Site1.NAME  Column26 , CollectionProtReg1.PROTOCOL_PARTICIPANT_ID  Column27 , ClinicalReport1.SURGICAL_PATHOLOGICAL_NUMBER  Column28 , ClinicalReport1.PARTICIPENT_MEDI_IDENTIFIER_ID  Column29 , Participant2.LAST_NAME  Column30 , Participant2.FIRST_NAME  Column31 , Specimen1.IDENTIFIER  Column32 , Specimen1.SPECIMEN_COLLECTION_GROUP_ID  Column33 , Specimen1.PARENT_SPECIMEN_ID  Column34 , Specimen1.TYPE  Column35 , Specimen1.POSITION_DIMENSION_ONE  Column36 , Specimen1.POSITION_DIMENSION_TWO  Column37 , Specimen1.BARCODE  Column38 , Specimen1.QUANTITY  Column39 , Specimen1.AVAILABLE_QUANTITY  Column40 , Specimen1.CONCENTRATION  Column41 , SpecimenCharacteristics1.TISSUE_SITE  Column42 , SpecimenCharacteristics1.TISSUE_SIDE  Column43 , SpecimenCharacteristics1.PATHOLOGICAL_STATUS  Column44 , ExternalIdentifier1.NAME  Column45 , ExternalIdentifier1.VALUE  Column46 , CollectionProtocol1.IDENTIFIER  Column47 , User1.LAST_NAME  Column48 , User1.FIRST_NAME  Column49 , SpecimenProtocol1.TITLE  Column50 , SpecimenProtocol1.SHORT_TITLE  Column51 , SpecimenProtocol1.IRB_IDENTIFIER  Column52 , SpecimenProtocol1.START_DATE  Column53 , SpecimenProtocol1.END_DATE  Column54 , SpecimenProtocol1.ENROLLMENT  Column55 , SpecimenProtocol1.DESCRIPTION_URL  Column56 , CollectionProtocolEvent1.CLINICAL_STATUS  Column57 , CollectionProtocolEvent1.STUDY_CALENDAR_EVENT_POINT  Column58 , SpecimenRequirement1.SPECIMEN_TYPE  Column59 , SpecimenRequirement1.TISSUE_SITE  Column60 , SpecimenRequirement1.PATHOLOGY_STATUS  Column61 , SpecimenRequirement1.QUANTITY  Column62 , CollectionSpecReq1.COLLECTION_PROTOCOL_EVENT_ID  Column63 , Site1.IDENTIFIER  Column64 , User1.IDENTIFIER  Column65 "+ 
//					"FROM  CATISSUE_COLL_SPECIMEN_REQ CollectionSpecReq1 , CATISSUE_SPECIMEN_REQUIREMENT SpecimenRequirement1 ,  CATISSUE_SPECIMEN_PROTOCOL SpecimenProtocol1 ,  CATISSUE_COLL_PROT_EVENT CollectionProtocolEvent1 , CATISSUE_SITE Site1 , CATISSUE_CLINICAL_REPORT ClinicalReport1 ,  CATISSUE_SPECIMEN_CHAR SpecimenCharacteristics1 , CATISSUE_EXTERNAL_IDENTIFIER ExternalIdentifier1 , CATISSUE_USER User1,CATISSUE_SPECIMEN Specimen1 ,  (CATISSUE_PARTICIPANT Participant2) left join (  CATISSUE_COLL_PROT_REG CollectionProtReg1,CATISSUE_PART_MEDICAL_ID ParticipantMedicalId1  ) on  (Participant2.IDENTIFIER   =  CollectionProtReg1.PARTICIPANT_ID AND Participant2.IDENTIFIER   =  ParticipantMedicalId1.PARTICIPANT_ID ) left join (CATISSUE_COLLECTION_PROTOCOL CollectionProtocol1,CATISSUE_SPECIMEN_COLL_GROUP SpecimenCollectionGroup1) on ( CollectionProtReg1.IDENTIFIER   =  SpecimenCollectionGroup1.COLLECTION_PROTOCOL_REG_ID  AND CollectionProtocol1.IDENTIFIER   =  CollectionProtReg1.COLLECTION_PROTOCOL_ID ) where " +
//					"SpecimenRequirement1.IDENTIFIER   =  CollectionSpecReq1.SPECIMEN_REQUIREMENT_ID  AND CollectionProtocolEvent1.IDENTIFIER   =  CollectionSpecReq1.COLLECTION_PROTOCOL_EVENT_ID    AND  SpecimenProtocol1.IDENTIFIER   =  CollectionProtocol1.IDENTIFIER  AND User1.IDENTIFIER   =  SpecimenProtocol1.PRINCIPAL_INVESTIGATOR_ID  AND CollectionProtocolEvent1.IDENTIFIER   =  SpecimenCollectionGroup1.COLLECTION_PROTOCOL_EVENT_ID  AND Site1.IDENTIFIER   =  SpecimenCollectionGroup1.SITE_ID  AND ClinicalReport1.IDENTIFIER   =  SpecimenCollectionGroup1.CLINICAL_REPORT_ID  AND SpecimenCollectionGroup1.IDENTIFIER   =  Specimen1.SPECIMEN_COLLECTION_GROUP_ID  AND CollectionProtocol1.IDENTIFIER   =  CollectionProtocolEvent1.COLLECTION_PROTOCOL_ID  AND SpecimenCharacteristics1.IDENTIFIER   =  Specimen1.SPECIMEN_CHARACTERISTICS_ID  AND Specimen1.IDENTIFIER   =  ExternalIdentifier1.SPECIMEN_ID and Participant2.ACTIVITY_STATUS != 'Disabled'  AND SpecimenProtocol1.ACTIVITY_STATUS != 'Disabled'  AND CollectionProtReg1.ACTIVITY_STATUS != 'Disabled'  AND SpecimenCollectionGroup1.ACTIVITY_STATUS != 'Disabled'  AND Specimen1.ACTIVITY_STATUS != 'Disabled'  AND (( " +
//					"(Participant2.IDENTIFIER  > 0  ) ) )";
			Logger.out.debug("no. of tables in tableSet after table created"+query.getTableNamesSet().size()+":"+query.getTableNamesSet());
			Logger.out.debug("Advance Query Sql"+sql);
			/**
			 * Name: Prafull
			 * Description: Query performance issue. Instead of saving complete query results in session, resultd will be fetched for each result page navigation.
			 * object of class QuerySessionData will be saved in session, which will contain the required information for query execution while navigating through query result pages.
			 */
			boolean hasConditionOnIdentifiedField = query.hasConditionOnIdentifiedField();
			int noOfRecords  = advBizLogic.createTempTable(sql,tempTableName,sessionData,queryResultObjectDataMap, hasConditionOnIdentifiedField);
			
			if (noOfRecords!=0)
			{
				//Set the columnDisplayNames in session
				session.setAttribute(Constants.COLUMN_DISPLAY_NAMES,columnNames);
				
				//Remove configured columns from session for previous query in same session
				session.setAttribute(Constants.CONFIGURED_SELECT_COLUMN_LIST,null);
				session.setAttribute(Constants.CONFIGURED_COLUMN_DISPLAY_NAMES,null);
				session.setAttribute(Constants.CONFIGURED_COLUMN_NAMES,null);
				session.removeAttribute(Constants.SPECIMENT_VIEW_ATTRIBUTE);
				//Remove the spreadsheet column display names from session
				session.setAttribute(Constants.SPREADSHEET_COLUMN_LIST,null);
				
				//Remove selected node from session
				session.setAttribute(Constants.SELECTED_NODE,null);
				
				//Remove select columnList from Session
				session.setAttribute(Constants.SELECT_COLUMN_LIST,null);
				
				/**
				 * Name: Prafull
				 * Description: Query performance issue. Instead of saving complete query results in session, resultd will be fetched for each result page navigation.
				 * object of class QuerySessionData will be saved in session, which will contain the required information for query execution while navigating through query result pages.
				 */
				int recordsPerPage; 
				String recordsPerPageSessionValue = (String)session.getAttribute(Constants.RESULTS_PER_PAGE);
				if (recordsPerPageSessionValue==null)
				{
						recordsPerPage = Integer.parseInt(XMLPropertyHandler.getValue(Constants.RECORDS_PER_PAGE_PROPERTY_NAME));
						session.setAttribute(Constants.RESULTS_PER_PAGE, recordsPerPage+"");
				}
				else
					recordsPerPage = new Integer(recordsPerPageSessionValue).intValue();
				// saving required query data in Session so that can be used later on while navigating through result pages using pagination.
				QuerySessionData querySessionData = new QuerySessionData();
				querySessionData.setQueryResultObjectDataMap(queryResultObjectDataMap);
				querySessionData.setSecureExecute(false);
				querySessionData.setHasConditionOnIdentifiedField(hasConditionOnIdentifiedField);
				querySessionData.setRecordsPerPage(recordsPerPage);
				
				session.setAttribute(Constants.QUERY_SESSION_DATA, querySessionData);
				/**
				 * Name : Aarti Sharma
				 * Bug ID: 4359
				 * Description: Setting hyperlinkColumnMap to null so that no hyperlinks appear in spreadsheet of advance query interface 
				 */
				session.setAttribute(Constants.HYPERLINK_COLUMN_MAP, null);
			}
			else
			{
				ActionErrors errors = new ActionErrors();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"advanceQuery.noRecordsFound"));
				saveErrors(request, errors);
				pageOf=Constants.PAGEOF_ADVANCE_QUERY_INTERFACE;
				target = Constants.SIMPLE_QUERY_NO_RESULTS;
			}
		}
		
		request.setAttribute(Constants.PAGEOF, pageOf);
		return mapping.findForward(target);
	}

	/**
	 * This method Parse the root and set the attribute type in the DataElement
	 * @param tree object of DefaultMutableTreeNode
	 * @throws Exception generic exception
	 */
	private void setAttributeType(DefaultMutableTreeNode tree) throws Exception
	{
		DefaultMutableTreeNode child;
		int childCount = tree.getChildCount();
		Logger.out.debug("childCount"+childCount);
		for(int i=0;i<childCount;i++)
		{
			child = (DefaultMutableTreeNode)tree.getChildAt(i);
			AdvancedConditionsNode advNode = (AdvancedConditionsNode)child.getUserObject();
			Vector conditions = advNode.getObjectConditions();
			Iterator conditionsItr = conditions.iterator();
			while(conditionsItr.hasNext())
			{
				Condition condition = (Condition)conditionsItr.next();
				String columnName = condition.getDataElement().getField();
				String aliasName = condition.getDataElement().getTableAliasName();
				QueryBizLogic bizLogic = new QueryBizLogic();//(QueryBizLogic)BizLogicFactory.getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
				StringTokenizer tableTokens = new StringTokenizer(aliasName,".");
				if(tableTokens.countTokens()==2)
				{
					aliasName = tableTokens.nextToken();
					tableTokens.nextToken();
				}

				String attributeType = bizLogic.getAttributeType(columnName,aliasName);
				condition.getDataElement().setFieldType(attributeType);
			}
			setAttributeType(child);
		}
	}

	/**
	 * Method to deep copy the DefaultMutableTreeNode object
	 * @param oldCopy old object of DefaultMutableTreeNode
	 * @param newCopy updated object of DefaultMutableTreeNode
	 */
	private void copy(DefaultMutableTreeNode oldCopy,DefaultMutableTreeNode newCopy)
	{
		DefaultMutableTreeNode child = new DefaultMutableTreeNode();
		if(oldCopy != null)
		{
			int childCount = oldCopy.getChildCount();
			for(int i=0;i<childCount;i++)
			{
				child = (DefaultMutableTreeNode)oldCopy.getChildAt(i);
				AdvancedConditionsNode advNode = (AdvancedConditionsNode)child.getUserObject();
				AdvancedConditionsNode newAdvNode = new AdvancedConditionsNode(new String(advNode.getObjectName()));
				Vector conditions = advNode.getObjectConditions();
				Operator opWithChild = advNode.getOperationWithChildCondition();
				Vector newConditions = new Vector();
				Iterator itr1 = conditions.iterator();
				while(itr1.hasNext())
				{
					Condition con = (Condition)itr1.next();
					DataElement data = con.getDataElement();
					DataElement newData = new DataElement(new Table(new String(data.getTableAliasName())),
							new String(data.getField()));
					Condition newCon = new Condition(newData,new Operator(con.getOperator()),new String(con.getValue()));
					newConditions.add(newCon);
				}
				newAdvNode.setObjectConditions(newConditions);
				newAdvNode.setOperationWithChildCondition(new Operator(opWithChild));
				DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(newAdvNode);
				newCopy.add(newChild);
				copy(child,newChild);
			}
		}
	}
}