package edu.wustl.catissuecore.query;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: (c) Washington University, School of Medicine 2005
 * </p>
 * <p>
 * Company: Washington University, School of Medicine, St. Louis.
 * </p>
 * 
 * @author Aarti Sharma
 * @version 1.0
 */
public abstract class Query {
	/**
	 * Advanced query type constant
	 */
	public static final String ADVANCED_QUERY = "AdvancedQuery";

	/**
	 * Simple query type constant
	 */
	public static final String SIMPLE_QUERY = "SimpleQuery";

	/**
	 * Vector of DataElement objects that need to be selected in the output
	 */
	private Vector resultView = new Vector();

	/**
	 * Starting object from which all related objects can be part of the query
	 */
	protected String queryStartObject;

	/**
	 * Parent object of the queryStartObject i.e. the object from which the
	 * queryStartObject is derived
	 */
	private String parentOfQueryStartObject = new String();

	/**
	 * Set of tables that should be included in FROM part of query
	 */
	private Set tableSet = new HashSet();

	/**
	 * Object that forms the where part of query. This is SimpleConditionsImpl
	 * object in case of Simple query and AdvancedConditionsImpl object in case
	 * of Advanced query
	 */
	protected ConditionsImpl whereConditions;

	/**
	 * Suffix that is appended to all table aliases which is helpful in case of
	 * nested queries to differentiate between super query object from the
	 * subquery object of same type
	 */
	protected int tableSufix = 1;
	
	/**
	 * @return Returns the isParentDerivedSpecimen.
	 */
	public boolean isParentDerivedSpecimen() {
		return isParentDerivedSpecimen;
	}
	/**
	 * @param isParentDerivedSpecimen The isParentDerivedSpecimen to set.
	 */
	public void setParentDerivedSpecimen(boolean isParentDerivedSpecimen) {
		this.isParentDerivedSpecimen = isParentDerivedSpecimen;
	}
	/**
	 * @return Returns the levelOfParent.
	 */
	public int getLevelOfParent() {
		return levelOfParent;
	}
	/**
	 * @param levelOfParent The levelOfParent to set.
	 */
	public void setLevelOfParent(int levelOfParent) {
		this.levelOfParent = levelOfParent;
	}
	protected int levelOfParent = 0;
	
	protected boolean isParentDerivedSpecimen = false;
	
	private String activityStatusConditions = new String();

	/**
	 * Participant object constant
	 */
	public static final String PARTICIPANT = "Participant";

	public static final String COLLECTION_PROTOCOL_REGISTRATION = "CollectionProtReg";

	public static final String COLLECTION_PROTOCOL = "CollectionProtocol";

	public static final String COLLECTION_PROTOCOL_EVENT = "CollectionProtocolEvent";

	public static final String SPECIMEN_COLLECTION_GROUP = "SpecimenCollectionGroup";

	public static final String SPECIMEN = "Specimen";

	public static final String PARTICIPANT_MEDICAL_IDENTIFIER = "ParticipantMedicalId";

	public static final String INSTITUTION = "Institution";

	public static final String DEPARTMENT = "Department";

	public static final String CANCER_RESEARCH_GROUP = "CancerResearchGroup";

	public static final String USER = "User";

	public static final String ADDRESS = "Address";

	public static final String CSM_USER = "User";

	public static final String SITE = "Site";

	public static final String STORAGE_TYPE = "StorageType";

	public static final String SPECIMEN_CHARACTERISTICS = "SpecimenCharacteristics";

	public static final String STORAGE_CONTAINER_CAPACITY = "StorageContainerCapacity";

	public static final String BIO_HAZARD = "Biohazard";

	public static final String SPECIMEN_PROTOCOL = "SpecimenProtocol";

	public static final String COLLECTION_COORDINATORS = "CollectionCoordinators";

	public static final String SPECIMEN_REQUIREMENT = "SpecimenRequirement";

	public static final String COLLECTION_SPECIMEN_REQUIREMENT = "CollectionSpecimenRequirement";

	public static final String DISTRIBUTION_SPECIMEN_REQUIREMENT = "DistributionSpecReq";

	public static final String DISTRIBUTION_PROTOCOL = "DistributionProtocol";

	public static final String REPORTED_PROBLEM = "ReportedProblem";

	public static final String CELL_SPECIMEN_REQUIREMENT = "CellSpecimenRequirement";

	public static final String MOLECULAR_SPECIMEN_REQUIREMENT = "MolecularSpecimenRequirement";

	public static final String TISSUE_SPECIMEN_REQUIREMENT = "TissueSpecimenRequirement";

	public static final String SPECIMEN_EVENT_PARAMETERS = "SpecimenEventParameters";
	
	public static final String SPECIMEN_EVENT_PARAMETERS_APPEND = "S";

	public static final String FLUID_SPECIMEN_REQUIREMENT = "FluidSpecimenRequirement";

	public static final String STORAGE_CONTAINER = "StorageContainer";

	public static final String CHECKIN_CHECKOUT_EVENT_PARAMETER = "CheckinoutEventParam";

	public static final String CLINICAL_REPORT = "ClinicalReport";
	
	public static final String PARAM = "Param";

	/**
	 * This method executes the query string formed from getString method and
	 * creates a temporary table.
	 * @param isSecureExecute
	 *            TODO
	 * @param hasConditionOnIdentifiedField TODO
	 * @param columnIdsMap
	 * 
	 * @return Returns true in case everything is successful else false
	 */
	public List execute(SessionDataBean sessionDataBean,
			boolean isSecureExecute, Map queryResultObjectDataMap, boolean hasConditionOnIdentifiedField)
			throws DAOException {
		try {
			JDBCDAO dao = (JDBCDAO) DAOFactory.getDAO(Constants.JDBC_DAO);
			dao.openSession(null);
			String sql =  getString();
			Logger.out.debug("SQL************" + sql);
			List list = dao.executeQuery(sql, sessionDataBean,
					isSecureExecute,hasConditionOnIdentifiedField, queryResultObjectDataMap);
			dao.closeSession();
			return list;
		} catch (DAOException daoExp) {
			throw new DAOException(daoExp.getMessage(), daoExp);
		} catch (ClassNotFoundException classExp) {
			throw new DAOException(classExp.getMessage(), classExp);
		}
	}

	/**
	 * Adds the dataElement to result view.
	 * 
	 * @param dataElement -
	 *            Data Element to be added.
	 * @return - true (as per the general contract of Collection.add).
	 */
	public boolean addElementToView(DataElement dataElement) {
		return resultView.add(dataElement);
	}

	/**
	 * Adds the dataElement to result view
	 * 
	 * @param dataElement -
	 *            Data Element to be added
	 * @return - true (as per the general contract of Collection.add).
	 */
	public void addElementToView(int position, DataElement dataElement) {
		resultView.add(position, dataElement);
	}

	/**
	 * Returns the SQL representation of this query object
	 * 
	 * @return
	 */
	public String getString() {
		
		//Formatting is required only the first time
		if(this.queryStartObject.equals(Query.PARTICIPANT))
		{
			whereConditions.formatTree();
		}
		StringBuffer query = new StringBuffer();
		HashSet set = new HashSet();

		/**
		 * Forming SELECT part of the query.
		 */
		query.append("Select ");
		if (resultView.size() == 0) {
			query.append(" * ");
		} else {
			DataElement dataElement;
			String dataElementString;
			for (int i = 0; i < resultView.size(); i++) {
				dataElement = (DataElement) resultView.get(i);
				dataElementString = dataElement.toSQLString(tableSufix) + " "
						+ dataElement.getColumnNameString(tableSufix) + i;
				if (dataElementString.length() >= 20) {
					dataElementString = dataElement.toSQLString(tableSufix)
							+ " Column" + i;
				}
				//                set.add(dataElement.getTable());
				if (i != resultView.size() - 1) {

					query.append(dataElementString + " , ");
				} else {
					query.append(dataElementString + " ");
				}
			}
		}

		/**
		 * Forming FROM part of query
		 */
		set = (HashSet) getTableSet();
		//        HashSet relatedTables = new HashSet();
		//        Iterator it = set.iterator();
		//        while(it.hasNext())
		//        {
		//            relatedTables.addAll(getRelatedTables((String) it.next()));
		//        }
		//        set.addAll(relatedTables);
		//        it=set.iterator();
		//        Logger.out.debug("Tables in set are:");
		//        while(it.hasNext())
		//        {
		//            Logger.out.debug(it.next());
		//        }

		//	    HashSet set = this.getQueryObjects(this.queryStartObject);
		//	    for(int i = 0 ; i < resultView.size(); i++)
		//	    {
		//	        set.add(((DataElement)resultView.get(i)).getTable());
		//	    }

		set.addAll(getLinkingTables(set));
		Logger.out.debug("Set : " + set.toString());
		query.append("\nFROM ");
		
		String joinConditionString = this.getJoinConditionString(set);
		
		query.append(this.formFromString(set));

		/**
		 * Forming WHERE part of the query
		 */
		query.append("\nWHERE ");
		
		query.append(joinConditionString);
		activityStatusConditions = this.getActivityStatusConditions();
		if (activityStatusConditions != null)
		{
			query.append(" " + activityStatusConditions);
		}
		
		//        String whereConditionsString = whereConditions.getString(tableSufix);
		if (whereConditions.hasConditions()) {
			if (joinConditionString != null
					&& joinConditionString.length() != 0) {
				query.append(" " + Operator.AND + " (");
			}
			query.append(whereConditions.getString(tableSufix));
			if (joinConditionString != null
					&& joinConditionString.length() != 0) {
				query.append(" )");
			}
		}
		return query.toString();
	}

	/**
	 * @param set
	 * @return
	 */
	private Set getLinkingTables(HashSet set) {
		Set linkingTables = new HashSet();
		Iterator it = set.iterator();
		Table table;
		while(it.hasNext())
		{
			table = (Table) it.next();
			while(table.hasDifferentAlias())
			{
				Logger.out.debug("Linking table: "+table+" ---> "+table.getLinkingTable());
				table = table.getLinkingTable();
				linkingTables.add(table);
			}
			
		}
		Logger.out.debug("linking tables:"+linkingTables);
		return linkingTables;
	}
	/**
	 * This method returns set of all objects related to queryStartObject
	 * transitively
	 * 
	 * @param string -
	 *            Starting object to which all related objects should be found
	 * @return set of all objects related to queryStartObject transitively
	 */
	private HashSet getQueryObjects(String queryStartObject) {
		HashSet set = new HashSet();
		set.add(queryStartObject);
		Vector relatedObjectsCollection = (Vector) Client.relations
				.get(queryStartObject);
		if (relatedObjectsCollection == null) {
			return set;
		}

		set.addAll(relatedObjectsCollection);
		for (int i = 0; i < relatedObjectsCollection.size(); i++) {
			set
					.addAll(getQueryObjects((String) relatedObjectsCollection
							.get(i)));
		}

		//        while(queryStartObject != null)
		//        {
		//            set.add(queryStartObject);
		//            queryStartObject = (String) Client.relations.get(queryStartObject);
		//        }
		return set;
	}

	/**
	 * This method returns the Join Conditions string that joins all the
	 * tables/objects in the set. In case its a subquery relation with the
	 * superquery is also appended in this string
	 * 
	 * @param set -
	 *            objects in the query
	 * @return - string containing all join conditions
	 */
	private String getJoinConditionString(final HashSet set) {
		StringBuffer joinConditionString = new StringBuffer();
//		Object[] tablesArray = new String[set.size()];
//		Iterator it = set.iterator();
//		for(int i=0; it.hasNext(); i++)
//		{
//			tablesArray[i] = ((Table)it.next()).getTableName();
//		}
		
		Object[] tablesArray = set.toArray();
		RelationCondition relationCondition = null;

		//If subquery then join with the superquery
		if (tableSufix > 1) {
			
			//this maps the specimen to the one in the upper query
			//this is to support event queries with 'OR' condition
			if(this.getParentOfQueryStartObject().equals(Query.SPECIMEN_COLLECTION_GROUP))
			{
				relationCondition = new RelationCondition(new DataElement(Query.SPECIMEN,Constants.IDENTIFIER),
						new Operator(Operator.EQUAL),new DataElement(Query.SPECIMEN,Constants.IDENTIFIER));
			}
			else
			{
				relationCondition = (RelationCondition) Client.relationConditionsForRelatedTables
					.get(getJoinRelationWithParent());
			}

			if (relationCondition != null) {
				DataElement rightDataElement = new DataElement(relationCondition.getRightDataElement());
				DataElement leftDataElement = new DataElement(relationCondition.getLeftDataElement());
				
				//If its a relation between two specimen
				if(rightDataElement.getTable().getTableName().equals(Query.SPECIMEN)
						&& leftDataElement.getTable().getTableName().equals(Query.SPECIMEN))
				{
					if(isParentDerivedSpecimen)
					{
						Logger.out.debug("Parent is derived specimen");
						leftDataElement.setTable(new Table(Query.SPECIMEN,Query.SPECIMEN+levelOfParent+"L"));
						leftDataElement.setTable(new Table(Query.SPECIMEN,Query.SPECIMEN+levelOfParent+"L"));
						
					}
					else
					{
						Logger.out.debug("Parent is not derived specimen");
					}
				}
				
				tableSet.add(rightDataElement.getTableAliasName());
				joinConditionString.append(" "
						+ rightDataElement.toSQLString(
								tableSufix));
				joinConditionString.append(Operator.EQUAL);
				joinConditionString.append(leftDataElement.toSQLString(tableSufix - 1)
						+ " ");
				Logger.out.debug(rightDataElement.toSQLString(tableSufix)+Operator.EQUAL+leftDataElement.toSQLString(tableSufix - 1));
				
			}
		}

		//For all permutations of tables find the joining conditions
		Table table1;
		Table table2;
		for (int i = 0; i < tablesArray.length; i++) {
			table1 = (Table) tablesArray[i];
			if(table1.hasDifferentAlias())
			{
				relationCondition = (RelationCondition) Client.relationConditionsForRelatedTables
				.get(new Relation((String) table1.getLinkingTable().getTableName(),
						(String) table1.getTableName()));
				Logger.out.debug("***************relationCondition:"+relationCondition);
				if (relationCondition != null) {
					relationCondition = new RelationCondition(relationCondition);
					relationCondition.getLeftDataElement().setTable(table1.getLinkingTable());
					relationCondition.getRightDataElement().setTable(table1);
					Logger.out.debug(table1.getTableName() + " " +table1.getLinkingTable().getTableName()
							+ " " + relationCondition.toSQLString(tableSufix));
					if (joinConditionString.length() != 0) {
						joinConditionString.append(Operator.AND + " ");
					}
					joinConditionString.append(relationCondition
							.toSQLString(tableSufix));
					set.add(table1.getLinkingTable());
				}
				else
				{
					relationCondition = (RelationCondition) Client.relationConditionsForRelatedTables
					.get(new Relation((String) table1.getTableName(),
							(String) table1.getLinkingTable().getTableName()));
					Logger.out.debug("***************relationCondition:"+relationCondition);
					if (relationCondition != null) {
						relationCondition = new RelationCondition(relationCondition);
						relationCondition.getLeftDataElement().setTable(table1);
						relationCondition.getRightDataElement().setTable(table1.getLinkingTable());
						Logger.out.debug(table1.getTableName() + " " +table1.getLinkingTable().getTableName()
								+ " " + relationCondition.toSQLString(tableSufix));
						if (joinConditionString.length() != 0) {
							joinConditionString.append(Operator.AND + " ");
						}
						joinConditionString.append(relationCondition
								.toSQLString(tableSufix));
						set.add(table1.getLinkingTable());
					}
				}
				continue;
			}
			for (int j = i + 1; j < tablesArray.length; j++) {
				
				table2 = (Table) tablesArray[j];
				
				if(table2.hasDifferentAlias())
				{
					relationCondition = (RelationCondition) Client.relationConditionsForRelatedTables
					.get(new Relation((String) table2.getLinkingTable().getTableName(),
							(String) table2.getTableName()));
					Logger.out.debug("***************relationCondition:"+relationCondition);
					if (relationCondition != null) {
						relationCondition = new RelationCondition(relationCondition);
						relationCondition.getLeftDataElement().setTable(table2.getLinkingTable());
						relationCondition.getRightDataElement().setTable(table2);
						Logger.out.debug(table2.getTableName() + " " +table2.getLinkingTable().getTableName()
								+ " " + relationCondition.toSQLString(tableSufix));
						if (joinConditionString.length() != 0) {
							joinConditionString.append(Operator.AND + " ");
						}
						joinConditionString.append(relationCondition
								.toSQLString(tableSufix));
					}
					else
					{
						relationCondition = (RelationCondition) Client.relationConditionsForRelatedTables
						.get(new Relation((String) table2.getLinkingTable().getTableName(),
								(String) table2.getTableName()));
						Logger.out.debug("***************relationCondition:"+relationCondition);
						if (relationCondition != null) {
							relationCondition = new RelationCondition(relationCondition);
							relationCondition.getLeftDataElement().setTable(table2.getLinkingTable());
							relationCondition.getRightDataElement().setTable(table2);
							Logger.out.debug(table2.getTableName() + " " +table2.getLinkingTable().getTableName()
									+ " " + relationCondition.toSQLString(tableSufix));
							if (joinConditionString.length() != 0) {
								joinConditionString.append(Operator.AND + " ");
							}
							joinConditionString.append(relationCondition
									.toSQLString(tableSufix));
						}
					}
					continue;
				}
				
				relationCondition = (RelationCondition) Client.relationConditionsForRelatedTables
						.get(new Relation((String) table1.getTableName(),
								(String) table2.getTableName()));
				if (relationCondition != null) {
					Logger.out.debug(table1.getTableName() + " " + table2.getTableName()
							+ " " + relationCondition.toSQLString(tableSufix));
					if (joinConditionString.length() != 0) {
						joinConditionString.append(Operator.AND + " ");
					}
					joinConditionString.append(relationCondition
							.toSQLString(tableSufix));

				} else {
					relationCondition = (RelationCondition) Client.relationConditionsForRelatedTables
							.get(new Relation((String) table2.getTableName(),
									(String) table1.getTableName()));

					if (relationCondition != null) {
						Logger.out.debug(table2.getTableName() + " "
								+table1.getTableName() + " "
								+ relationCondition.toSQLString(tableSufix));
						if (joinConditionString.length() != 0) {
							joinConditionString.append(Operator.AND + " ");
						}
						joinConditionString.append(relationCondition
								.toSQLString(tableSufix));

					}
				}
			}
		}
		return joinConditionString.toString();
	}

	/**
	 * @return
	 */
	private Relation getJoinRelationWithParent() {
		Map JOIN_RELATION_MAP = new HashMap();
		JOIN_RELATION_MAP.put(new Relation(Query.PARTICIPANT,Query.COLLECTION_PROTOCOL),new Relation(Query.PARTICIPANT,Query.COLLECTION_PROTOCOL_REGISTRATION));
		JOIN_RELATION_MAP.put(new Relation(Query.COLLECTION_PROTOCOL,Query.SPECIMEN_COLLECTION_GROUP),new Relation(Query.COLLECTION_PROTOCOL_EVENT,Query.SPECIMEN_COLLECTION_GROUP));
		JOIN_RELATION_MAP.put(new Relation(Query.SPECIMEN_COLLECTION_GROUP,Query.SPECIMEN),new Relation(Query.SPECIMEN,Query.SPECIMEN));
		JOIN_RELATION_MAP.put(new Relation(Query.SPECIMEN,Query.SPECIMEN),new Relation(Query.SPECIMEN,Query.SPECIMEN));
		Logger.out.debug(this.getParentOfQueryStartObject()+" "+this.queryStartObject);
		return (Relation) JOIN_RELATION_MAP.get(new Relation(this.getParentOfQueryStartObject(),this.queryStartObject));
	}

	/**
	 * This method returns the string of table names in set that forms FROM part
	 * of query which forms the FROM part of the query
	 * 
	 * @param set -
	 *            set of tables
	 * @return A comma separated list of the tables in the set
	 */
	private String formFromString(final HashSet set) {
		
		if (tableSufix > 1) {
			RelationCondition relationCondition = (RelationCondition) Client.relationConditionsForRelatedTables
					.get(getJoinRelationWithParent());
			Logger.out.debug("*********relationCondition:"+relationCondition+"  "+getJoinRelationWithParent());
			
			set.add(new Table(relationCondition.getRightDataElement().getTableAliasName()));
		}
		
		StringBuffer fromString = new StringBuffer();
		Iterator it = set.iterator();
		
		Object tableAlias;
		Table table;
		while (it.hasNext()) {
			fromString.append(" ");
			table = (Table) it.next();
			Logger.out.debug(" Table name:"+table.getTableName());
			fromString
					.append(((String) Client.objectTableNames.get(table.getTableName()))
							.toUpperCase()
							+ " " + table.toSQLString() + tableSufix + " ");
			if (it.hasNext()) {
				fromString.append(",");
			}
		}
		fromString.append(" ");
		return fromString.toString();
	}

	public int getTableSufix() {
		return tableSufix;
	}

	public void setTableSufix(int tableSufix) {
		this.tableSufix = tableSufix;
	}

	public String getParentOfQueryStartObject() {
		return parentOfQueryStartObject;
	}

	public void setParentOfQueryStartObject(String parentOfQueryStartObject) {
		this.parentOfQueryStartObject = parentOfQueryStartObject;
	}

	/**
	 * @param resultView
	 *            The resultView to set.
	 */
	public void setResultView(Vector resultView) {
		this.resultView = resultView;
	}

	/**
	 * 
	 * @return
	 */
	public Vector getResultView() {
		return this.resultView;
	}

	public Set getRelatedTables(String aliasName) {
		List list = null;
		Set relatedTableNames = new HashSet();
		try {
			JDBCDAO dao = new JDBCDAO();
			dao.openSession(null);
			String sqlString = "SELECT tableData2.ALIAS_NAME from CATISSUE_QUERY_TABLE_DATA tableData2 "
					+ "join (SELECT CHILD_TABLE_ID FROM CATISSUE_TABLE_RELATION relationData,"
					+ "CATISSUE_QUERY_TABLE_DATA tableData "
					+ "where relationData.PARENT_TABLE_ID = tableData.TABLE_ID and tableData.ALIAS_NAME = '"
					+ aliasName
					+ "') as relatedTables  on relatedTables.CHILD_TABLE_ID = tableData2.TABLE_ID";
			list = dao.executeQuery(getString(), null, false, null);

			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				List row = (List) iterator.next();
				relatedTableNames.add(row.get(0));
			}

			dao.closeSession();
		} catch (DAOException daoExp) {
			Logger.out.debug("Could not obtain related tables. Exception:"
					+ daoExp.getMessage(), daoExp);
		} catch (ClassNotFoundException classExp) {
			Logger.out.debug("Could not obtain related tables. Exception:"
					+ classExp.getMessage(), classExp);
		}
		Logger.out.debug("Tables related to " + aliasName + " "
				+ relatedTableNames.toString());
		return relatedTableNames;
	}

	/**
	 * @return Returns the tableSet.
	 */
	public Set getTableNamesSet() {
		Set set = new HashSet();
		DataElement dataElement;
		if (resultView != null) {
			for (int i = 0; i < resultView.size(); i++) {
				dataElement = (DataElement) resultView.get(i);
				set.add(dataElement.getTableAliasName());

			}
		}
		if (whereConditions != null) {
			Set queryObjects = whereConditions.getQueryObjects();
			if (queryObjects != null) {
				Iterator it = queryObjects.iterator();
				Table table;
				while(it.hasNext())
				{
					table = (Table) it.next();
					set.add(table.getTableName());
				}
			}
		}
		if (this.queryStartObject != null) {
			set.add(this.queryStartObject);
		}
		if (tableSet != null) {
			
			set.addAll(tableSet);
		}
		Logger.out.debug("TableNamesSet:" + set);
		
		//REmoving all event parameter tables
		Iterator it = set.iterator();
		String tableName;
		Set newTableSet = new HashSet();
		while(it.hasNext())
		{
			tableName = (String) it.next();
			if(tableName.indexOf(Query.PARAM) == -1)
			{
				newTableSet.add(tableName);			
			}
		}
		
		Logger.out.debug("TableNamesSet after removing event parameter tables:" + newTableSet);
		
		return newTableSet;
	}
	
	/**
	 * @return Returns the tableSet.
	 */
	public Set getTableSet() {
		Set set = new HashSet();
		DataElement dataElement;
		if (resultView != null) {
			for (int i = 0; i < resultView.size(); i++) {
				dataElement = (DataElement) resultView.get(i);
				set.add(dataElement.getTable());

			}
		}
		Logger.out.debug("TableSet after adding resultview:" + set);
		
		if (whereConditions != null) {
			set.addAll(whereConditions.getQueryObjects());
		}
//		if (this.queryStartObject != null) {
//			set.add(new Table(this.queryStartObject,this.queryStartObject));
//		}
		if (tableSet != null) {
			
			Iterator it = tableSet.iterator();
			String tableName;
			while(it.hasNext())
			{
				tableName = (String) it.next();
				set.add(new Table(tableName,tableName));
			}
			
		}
		Logger.out.debug("Overall TableSet:" + set);
		return set;
	}

	/**
	 * @param tableSet
	 *            The tableSet to set.
	 */
	public void setTableSet(Set tableSet) {
		this.tableSet = tableSet;
	}

	public void showTableSet() {
		Iterator iterator1 = this.tableSet.iterator();
		while (iterator1.hasNext()) {
			Logger.out.debug("showTableSet............................"
					+ iterator1.next());
		}
	}

	/**
	 * This method returns all the column numbers in the query that belong to
	 * tableAlias and the related tables passed as parameter
	 * 
	 * @param tableAlias
	 * @param relatedTables
	 * @return
	 */
	public Vector getColumnIds(String tableAlias, Vector relatedTables) {
		Logger.out.debug(" tableAlias:" + tableAlias + " relatedTables:"
				+ relatedTables);
		if (relatedTables == null) {
			relatedTables = new Vector();
		}
		Vector columnIds = new Vector();
		DataElement dataElement;
		String dataElementTableName;
		String dataElementFieldName;
		for (int i = 0; i < resultView.size(); i++) {
			dataElement = (DataElement) resultView.get(i);
			dataElementTableName = dataElement.getTableAliasName();
			dataElementFieldName = dataElement.getField();

			//I
			if (dataElementTableName.equals(tableAlias)
					|| relatedTables.contains(dataElementTableName)) {
				if(dataElementTableName.equals(tableAlias) && (dataElementFieldName.equals(Constants.IDENTIFIER)|| 
						dataElementFieldName.equals(Constants.PARENT_SPECIMEN_ID_COLUMN)))
				{
					continue;
				}
				columnIds.add(new Integer(i + 1));
				Logger.out.debug("tableAlias:" + tableAlias + " columnId:"
						+ (i + 1));
			}
		}

		return columnIds;
	}

	/**
	 * This method returns all the column numbers in the query that belong to
	 * tableAlias and the related and are Identified Columns tables passed as
	 * parameter
	 * 
	 * @param tableAlias
	 * @param relatedTables
	 * @return
	 */
	public Vector getIdentifiedColumnIds(String tableAlias, Vector relatedTables) {
		Logger.out.debug(" tableAlias:" + tableAlias + " relatedTables:"
				+ relatedTables);
		if (relatedTables == null) {
			relatedTables = new Vector();
		}
		Vector columnIds = new Vector();
		DataElement dataElement;
		String dataElementTableName;
		String dataElementFieldName;
		Vector identifiedData;
		for (int i = 0; i < resultView.size(); i++) {
			dataElement = (DataElement) resultView.get(i);
			dataElementTableName = dataElement.getTableAliasName();
			dataElementFieldName = dataElement.getField();
			//I
			if (dataElementTableName.equals(tableAlias)
					|| relatedTables.contains(dataElementTableName)) {
				identifiedData = (Vector) Client.identifiedDataMap
						.get(dataElementTableName);
				Logger.out.debug("Table:" + dataElementTableName
						+ " Identified Data:" + identifiedData);
				if (identifiedData != null) {
					Logger.out.debug(" identifiedData not null..."
							+ identifiedData);
					Logger.out.debug(" dataElementFieldName:"
							+ dataElementFieldName);
					Logger.out
							.debug(" identifiedData.contains(dataElementFieldName)***** "
									+ identifiedData
											.contains(dataElementFieldName));
					if (identifiedData.contains(dataElementFieldName)) {
						Logger.out
								.debug(" identifiedData.contains(dataElementFieldName)***** "
										+ identifiedData
												.contains(dataElementFieldName));
						columnIds.add(new Integer(i + 1));
						Logger.out.debug("tableAlias:" + tableAlias
								+ " Identified column:" + dataElementFieldName
								+ " Identified columnId:" + (i + 1));
					}
				}

			}
		}

		return columnIds;
	}

	/**
	 * This method returns table alias -> identifier column id map of all the
	 * objects in tableAliasVector. In case there is no identifier column in the
	 * query for some table alias om the vector then that column is by default
	 * included in the resultant query
	 * 
	 * @param tableAliasVector
	 * @return
	 */
	public Map getIdentifierColumnIds(Vector tableAliasVector) {
		Logger.out.debug(" tableAliasVector:" + tableAliasVector);

		Map columnIdsMap = new HashMap();
		DataElement dataElement;
		String dataElementTableName;
		String dataElementFieldName;
		String tableAlias;
		DataElement identifierDataElement;

		for (int j = 0; j < tableAliasVector.size(); j++) {
			tableAlias = (String) tableAliasVector.get(j);
			for (int i = 0; i < resultView.size(); i++) {
				dataElement = (DataElement) resultView.get(i);
				dataElementTableName = dataElement.getTableAliasName();
				dataElementFieldName = dataElement.getField();

				if (dataElementTableName.equals(tableAlias)
						&& dataElementFieldName.equals(Constants.IDENTIFIER)) {
					columnIdsMap.put(tableAlias, new Integer(i + 1));
					Logger.out.debug("tableAlias:" + tableAlias
							+ " Identifier columnId:" + (i + 1));
					break;
				}
			}
			if (columnIdsMap.get(tableAlias) == null) {
				identifierDataElement = new DataElement(tableAlias,
						Constants.IDENTIFIER);
				this.resultView.add(identifierDataElement);
				columnIdsMap.put(tableAlias, new Integer(resultView.size()));
				Logger.out.debug("tableAlias:" + tableAlias
						+ " Added Identifier columnId:" + resultView.size());
			}
		}

		return columnIdsMap;
	}

	public Map getIdentifierColumnIds(Set tableAliasSet) {
		Vector tableAliasVector = new Vector();
		tableAliasVector.addAll(tableAliasSet);
		return getIdentifierColumnIds(tableAliasVector);
	}

	/**
	 * This method returns 'tableAlias.ColumnName' -> column id map of all the
	 * objects in select part of query. 
	 * 
	 * @return
	 */
	public Map getColumnIdsMap() {

		Map columnIdsMap = new HashMap();
		DataElement dataElement;
		String dataElementTableName;
		String dataElementFieldName;
		String tableAlias;
		DataElement identifierDataElement;

		for (int i = 0; i < resultView.size(); i++) {
			dataElement = (DataElement) resultView.get(i);
			dataElementTableName = dataElement.getTableAliasName();
			dataElementFieldName = dataElement.getField();

			columnIdsMap.put(dataElementTableName + "." + dataElementFieldName,
					new Integer(i + 1));
			Logger.out.debug("tableAlias:" + dataElementTableName + " "
					+ dataElementFieldName + " columnId:" + (i + 1));

		}
		return columnIdsMap;
	}

	/**
	 * @return Returns the whereConditions.
	 */
	public ConditionsImpl getWhereConditions() {
		return whereConditions;
	}
	/**
	 * @return Returns the activityStatusConditions.
	 */
	public String getActivityStatusConditions() {
		return activityStatusConditions;
	}
	/**
	 * @param activityStatusConditions The activityStatusConditions to set.
	 */
	public void setActivityStatusConditions(String activityStatusConditions) {
		this.activityStatusConditions = activityStatusConditions;
	}
	
	/**
	 * This method returns true if the query made 
	 * is on any of the identified fields else false 
	 * @return
	 */
	public boolean hasConditionOnIdentifiedField()
	{
		return whereConditions.hasConditionOnIdentifiedField();
	}
}