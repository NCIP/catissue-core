/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

package edu.wustl.catissuecore.query;

import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.PropertyConfigurator;

import edu.wustl.catissuecore.bizlogic.QueryBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.query.AbstractClient;
import edu.wustl.common.util.logger.Logger;

public class Client extends AbstractClient
{
    public static void initialize()
    {
    	QueryBizLogic.initializeQueryData();
//        objectTableNames = QueryBizLogic.getQueryObjectNameTableNameMap();
//        relationConditionsForRelatedTables = QueryBizLogic.getRelationData();
    }

    public static void setRelationConditionsForRelatedTables()
    {
        relationConditionsForRelatedTables.put(new Relation(Query.PARTICIPANT,
                Query.COLLECTION_PROTOCOL_REGISTRATION), new RelationCondition(
                new DataElement(Query.PARTICIPANT, "Identifier"), new Operator(
                        Operator.EQUAL), new DataElement(
                        Query.COLLECTION_PROTOCOL_REGISTRATION,
                        "PARTICIPANT_ID")));
        relationConditionsForRelatedTables.put(new Relation(Query.PARTICIPANT,
                Query.PARTICIPANT_MEDICAL_IDENTIFIER),
                new RelationCondition(new DataElement(Query.PARTICIPANT,
                        "Identifier"), new Operator(Operator.EQUAL),
                        new DataElement(Query.PARTICIPANT_MEDICAL_IDENTIFIER,
                                "PARTICIPANT_ID")));
        relationConditionsForRelatedTables.put(new Relation(
                Query.COLLECTION_PROTOCOL,
                Query.COLLECTION_PROTOCOL_REGISTRATION), new RelationCondition(
                new DataElement(Query.COLLECTION_PROTOCOL, "Identifier"),
                new Operator(Operator.EQUAL), new DataElement(
                        Query.COLLECTION_PROTOCOL_REGISTRATION,
                        "COLLECTION_PROTOCOL_ID")));
        relationConditionsForRelatedTables.put(new Relation(
                Query.COLLECTION_PROTOCOL, Query.COLLECTION_PROTOCOL_EVENT),
                new RelationCondition(new DataElement(
                        Query.COLLECTION_PROTOCOL, "Identifier"), new Operator(
                        Operator.EQUAL), new DataElement(
                        Query.COLLECTION_PROTOCOL_EVENT,
                        "COLLECTION_PROTOCOL_ID")));
        relationConditionsForRelatedTables.put(new Relation(
                Query.COLLECTION_PROTOCOL_EVENT,
                Query.SPECIMEN_COLLECTION_GROUP), new RelationCondition(
                new DataElement(Query.COLLECTION_PROTOCOL_EVENT, "Identifier"),
                new Operator(Operator.EQUAL), new DataElement(
                        Query.SPECIMEN_COLLECTION_GROUP,
                        "COLLECTION_PROTOCOL_EVENT_ID")));
        relationConditionsForRelatedTables
                .put(new Relation(Query.SPECIMEN_COLLECTION_GROUP,
                        Query.SPECIMEN),
                        new RelationCondition(new DataElement(
                                Query.SPECIMEN_COLLECTION_GROUP, "Identifier"),
                                new Operator(Operator.EQUAL), new DataElement(
                                        Query.SPECIMEN,
                                        "SPECIMEN_COLLECTION_GROUP_ID")));
        relationConditionsForRelatedTables.put(new Relation(Query.DEPARTMENT,
                Query.USER), new RelationCondition(new DataElement(
                Query.DEPARTMENT, "Identifier"), new Operator(Operator.EQUAL),
                new DataElement(Query.USER, "DEPARTMENT_ID")));
        relationConditionsForRelatedTables.put(new Relation(Query.INSTITUTION,
                Query.USER), new RelationCondition(new DataElement(
                Query.INSTITUTION, "Identifier"), new Operator(Operator.EQUAL),
                new DataElement(Query.USER, "INSTITUTION_ID")));
        relationConditionsForRelatedTables.put(new Relation(
                Query.CANCER_RESEARCH_GROUP, Query.USER),
                new RelationCondition(new DataElement(
                        Query.CANCER_RESEARCH_GROUP, "Identifier"),
                        new Operator(Operator.EQUAL), new DataElement(
                                Query.USER, "CANCER_RESEARCH_GROUP_ID")));
        relationConditionsForRelatedTables.put(new Relation(Query.ADDRESS,
                Query.USER), new RelationCondition(new DataElement(
                Query.ADDRESS, "Identifier"), new Operator(Operator.EQUAL),
                new DataElement(Query.USER, "ADDRESS_ID")));
        relationConditionsForRelatedTables.put(new Relation(Query.CSM_USER,
                Query.USER), new RelationCondition(new DataElement(
                Query.CSM_USER, "USER_ID"), new Operator(Operator.EQUAL),
                new DataElement(Query.USER, "Identifier")));
        relationConditionsForRelatedTables.put(new Relation(Query.USER,
                Query.SITE), new RelationCondition(new DataElement(Query.USER,
                "Identifier"), new Operator(Operator.EQUAL), new DataElement(
                Query.SITE, "USER_ID")));
        relationConditionsForRelatedTables.put(new Relation(Query.ADDRESS,
                Query.SITE), new RelationCondition(new DataElement(
                Query.ADDRESS, "Identifier"), new Operator(Operator.EQUAL),
                new DataElement(Query.SITE, "ADDRESS_ID")));
        relationConditionsForRelatedTables.put(new Relation(
                Query.STORAGE_CONTAINER_CAPACITY, Query.STORAGE_CONTAINER),
                new RelationCondition(new DataElement(
                        Query.STORAGE_CONTAINER_CAPACITY, "Identifier"),
                        new Operator(Operator.EQUAL), new DataElement(
                                Query.STORAGE_CONTAINER,
                                "STORAGE_CONTAINER_CAPACITY_ID")));
        relationConditionsForRelatedTables
                .put(new Relation(Query.SPECIMEN_PROTOCOL,
                        Query.COLLECTION_PROTOCOL), new RelationCondition(
                        new DataElement(Query.SPECIMEN_PROTOCOL, "Identifier"),
                        new Operator(Operator.EQUAL), new DataElement(
                                Query.COLLECTION_PROTOCOL, "Identifier")));
        relationConditionsForRelatedTables.put(new Relation(Query.USER,
                Query.SPECIMEN_PROTOCOL), new RelationCondition(
                new DataElement(Query.USER, "Identifier"), new Operator(
                        Operator.EQUAL), new DataElement(
                        Query.SPECIMEN_PROTOCOL, "PRINCIPAL_INVESTIGATOR_ID")));
        relationConditionsForRelatedTables.put(new Relation(Query.USER,
                Query.COLLECTION_COORDINATORS), new RelationCondition(
                new DataElement(Query.USER, "Identifier"), new Operator(
                        Operator.EQUAL), new DataElement(
                        Query.COLLECTION_COORDINATORS, "USER_ID")));
        relationConditionsForRelatedTables.put(new Relation(
                Query.COLLECTION_PROTOCOL, Query.COLLECTION_COORDINATORS),
                new RelationCondition(new DataElement(
                        Query.COLLECTION_PROTOCOL, "Identifier"), new Operator(
                        Operator.EQUAL),
                        new DataElement(Query.COLLECTION_COORDINATORS,
                                "COLLECTION_PROTOCOL_ID")));
        relationConditionsForRelatedTables.put(new Relation(
                Query.SPECIMEN_REQUIREMENT,
                Query.COLLECTION_SPECIMEN_REQUIREMENT), new RelationCondition(
                new DataElement(Query.SPECIMEN_REQUIREMENT, "Identifier"),
                new Operator(Operator.EQUAL), new DataElement(
                        Query.COLLECTION_SPECIMEN_REQUIREMENT,
                        "SPECIMEN_REQUIREMENT_ID")));
        relationConditionsForRelatedTables.put(new Relation(
                Query.COLLECTION_PROTOCOL_EVENT,
                Query.COLLECTION_SPECIMEN_REQUIREMENT), new RelationCondition(
                new DataElement(Query.COLLECTION_PROTOCOL_EVENT, "Identifier"),
                new Operator(Operator.EQUAL), new DataElement(
                        Query.COLLECTION_SPECIMEN_REQUIREMENT,
                        "COLLECTION_PROTOCOL_EVENT_ID")));
        relationConditionsForRelatedTables.put(new Relation(
                Query.SPECIMEN_PROTOCOL, Query.DISTRIBUTION_PROTOCOL),
                new RelationCondition(new DataElement(Query.SPECIMEN_PROTOCOL,
                        "Identifier"), new Operator(Operator.EQUAL),
                        new DataElement(Query.DISTRIBUTION_PROTOCOL,
                                "Identifier")));
        relationConditionsForRelatedTables.put(new Relation(
                Query.SPECIMEN_REQUIREMENT,
                Query.DISTRIBUTION_SPECIMEN_REQUIREMENT),
                new RelationCondition(new DataElement(
                        Query.SPECIMEN_REQUIREMENT, "Identifier"),
                        new Operator(Operator.EQUAL), new DataElement(
                                Query.DISTRIBUTION_SPECIMEN_REQUIREMENT,
                                "SPECIMEN_REQUIREMENT_ID")));
        relationConditionsForRelatedTables.put(new Relation(
                Query.DISTRIBUTION_PROTOCOL,
                Query.DISTRIBUTION_SPECIMEN_REQUIREMENT),
                new RelationCondition(new DataElement(
                        Query.DISTRIBUTION_PROTOCOL, "Identifier"),
                        new Operator(Operator.EQUAL), new DataElement(
                                Query.DISTRIBUTION_SPECIMEN_REQUIREMENT,
                                "DISTRIBUTION_PROTOCOL_ID")));
        relationConditionsForRelatedTables.put(new Relation(
                Query.CELL_SPECIMEN_REQUIREMENT,
                Query.SPECIMEN_REQUIREMENT),
                new RelationCondition(new DataElement(
                        Query.CELL_SPECIMEN_REQUIREMENT, "Identifier"),
                        new Operator(Operator.EQUAL), new DataElement(
                                Query.SPECIMEN_REQUIREMENT,
                                "Identifier")));
        relationConditionsForRelatedTables.put(new Relation(
                Query.MOLECULAR_SPECIMEN_REQUIREMENT,
                Query.SPECIMEN_REQUIREMENT),
                new RelationCondition(new DataElement(
                        Query.MOLECULAR_SPECIMEN_REQUIREMENT, "Identifier"),
                        new Operator(Operator.EQUAL), new DataElement(
                                Query.SPECIMEN_REQUIREMENT,
                                "Identifier")));
        relationConditionsForRelatedTables.put(new Relation(
                Query.TISSUE_SPECIMEN_REQUIREMENT,
                Query.SPECIMEN_REQUIREMENT),
                new RelationCondition(new DataElement(
                        Query.TISSUE_SPECIMEN_REQUIREMENT, "Identifier"),
                        new Operator(Operator.EQUAL), new DataElement(
                                Query.SPECIMEN_REQUIREMENT,
                                "Identifier")));
        relationConditionsForRelatedTables.put(new Relation(
                Query.FLUID_SPECIMEN_REQUIREMENT,
                Query.SPECIMEN_REQUIREMENT),
                new RelationCondition(new DataElement(
                        Query.FLUID_SPECIMEN_REQUIREMENT, "Identifier"),
                        new Operator(Operator.EQUAL), new DataElement(
                                Query.SPECIMEN_REQUIREMENT,
                                "Identifier")));
        relationConditionsForRelatedTables.put(new Relation(
                Query.STORAGE_TYPE,
                Query.STORAGE_CONTAINER),
                new RelationCondition(new DataElement(
                        Query.STORAGE_TYPE, "Identifier"),
                        new Operator(Operator.EQUAL), new DataElement(
                                Query.STORAGE_CONTAINER,
                                "STORAGE_TYPE_ID")));
        relationConditionsForRelatedTables.put(new Relation(
                Query.SITE,
                Query.STORAGE_CONTAINER),
                new RelationCondition(new DataElement(
                        Query.SITE, "Identifier"),
                        new Operator(Operator.EQUAL), new DataElement(
                                Query.STORAGE_CONTAINER,
                                "SITE_ID")));
        relationConditionsForRelatedTables.put(new Relation(
                Query.STORAGE_CONTAINER_CAPACITY,
                Query.STORAGE_CONTAINER),
                new RelationCondition(new DataElement(
                        Query.STORAGE_CONTAINER_CAPACITY, "Identifier"),
                        new Operator(Operator.EQUAL), new DataElement(
                                Query.STORAGE_CONTAINER,
                                "STORAGE_CONTAINER_CAPACITY_ID")));
        
        relationConditionsForRelatedTables.put(new Relation(
                Query.COLLECTION_PROTOCOL,
                Query.SPECIMEN),
                new RelationCondition(new DataElement(
                        Query.COLLECTION_PROTOCOL, "Identifier"),
                        new Operator(Operator.EQUAL), new DataElement(
                                Query.SPECIMEN,
                                "COLLECTION_PROTOCOL_ID")));
        

    }

    public static void setRelations()
    {
        Vector v = new Vector();
        v.add(Query.COLLECTION_PROTOCOL_EVENT);
        Vector v2 = new Vector();
        v2.add(Query.COLLECTION_PROTOCOL_REGISTRATION);
        Vector v3 = new Vector();
        v3.add(Query.COLLECTION_PROTOCOL);
        Vector v4 = new Vector();
        v4.add(Query.SPECIMEN_COLLECTION_GROUP);
        Vector v5 = new Vector();
        v5.add(Query.SPECIMEN);

        relations.put(Query.PARTICIPANT, v2);
        relations.put(Query.COLLECTION_PROTOCOL_REGISTRATION, v3);
        relations.put(Query.COLLECTION_PROTOCOL, v);
        relations.put(Query.COLLECTION_PROTOCOL_EVENT, v4);
        relations.put(Query.SPECIMEN_COLLECTION_GROUP, v5);
    }

    public static void setObjectTableNames()
    {
        objectTableNames.put(Query.PARTICIPANT, "CATISSUE_PARTICIPANT");
        objectTableNames.put(Query.PARTICIPANT_MEDICAL_IDENTIFIER,
                "catissue_participant_medical_identifier");
        objectTableNames.put(Query.COLLECTION_PROTOCOL_REGISTRATION,
                "CATISSUE_COLLECTION_PROTOCOL_REGISTRATION");
        objectTableNames.put(Query.COLLECTION_PROTOCOL,
                "CATISSUE_COLLECTION_PROTOCOL");
        objectTableNames.put(Query.COLLECTION_PROTOCOL_EVENT,
                "CATISSUE_COLLECTION_PROTOCOL_EVENT");
        objectTableNames.put(Query.SPECIMEN_COLLECTION_GROUP,
                "CATISSUE_SPECIMEN_COLLECTION_GROUP");
        objectTableNames.put(Query.SPECIMEN, "CATISSUE_SPECIMEN");
        objectTableNames.put(Query.DEPARTMENT, "CATISSUE_DEPARTMENT");
        objectTableNames.put(Query.INSTITUTION, "catissue_institution");
        objectTableNames.put(Query.CANCER_RESEARCH_GROUP,
                "catissue_cancer_research_group");
        objectTableNames.put(Query.USER, "catissue_user");
        objectTableNames.put(Query.CSM_USER, "csm_user");
        objectTableNames.put(Query.ADDRESS, "catissue_address");
        objectTableNames.put(Query.SITE, "catissue_site");
        objectTableNames.put(Query.STORAGE_TYPE, "catissue_storage_type");
        objectTableNames.put(Query.STORAGE_CONTAINER_CAPACITY,
                "catissue_storage_container_capacity");
        objectTableNames.put(Query.BIO_HAZARD, "catissue_biohazard");
        objectTableNames.put(Query.SPECIMEN_PROTOCOL,
                "catissue_specimen_protocol");
        objectTableNames.put(Query.COLLECTION_COORDINATORS,
                "catissue_collection_coordinators");
        objectTableNames.put(Query.SPECIMEN_REQUIREMENT,
                "catissue_specimen_requirement");
        objectTableNames.put(Query.COLLECTION_SPECIMEN_REQUIREMENT,
                "catissue_collection_specimen_requirement");
        objectTableNames.put(Query.DISTRIBUTION_PROTOCOL,
                "catissue_distribution_protocol");
        objectTableNames.put(Query.DISTRIBUTION_SPECIMEN_REQUIREMENT,
                "catissue_distribution_specimen_requirement");
        objectTableNames.put(Query.REPORTED_PROBLEM,
                "catissue_reported_problem");
        objectTableNames.put(Query.CELL_SPECIMEN_REQUIREMENT,
        "catissue_cell_specimen_requirement");
        objectTableNames.put(Query.MOLECULAR_SPECIMEN_REQUIREMENT,
        "catissue_molecular_specimen_requirement");
        objectTableNames.put(Query.TISSUE_SPECIMEN_REQUIREMENT,
        "catissue_tissue_specimen_requirement");
        objectTableNames.put(Query.FLUID_SPECIMEN_REQUIREMENT,
        "catissue_fluid_specimen_requirement");
        objectTableNames.put(Query.STORAGE_CONTAINER,
        "catissue_storage_container");
    }

    public static void main(String[] args)
    {
    	Variables.catissueHome = System.getProperty("user.dir");
		Logger.out = org.apache.log4j.Logger.getLogger("");
		PropertyConfigurator.configure(Variables.catissueHome+"\\WEB-INF\\src\\"+"ApplicationResources.properties");
		
		System.out.println("here");
        initialize();
        //        relations.put(Query.PARTICIPANT,Query.ACCESSION);
        //        relations.put(Query.ACCESSION,Query.SPECIMEN);
        //        relations.put(Query.SPECIMEN,Query.SEGMENT);
        //        relations.put(Query.SEGMENT,Query.SAMPLE);
        
        relationConditionsForRelatedTables.put(new Relation(
                Query.COLLECTION_PROTOCOL,
                Query.SPECIMEN),
                new RelationCondition(new DataElement(
                        Query.COLLECTION_PROTOCOL, "Identifier"),
                        new Operator(Operator.EQUAL), new DataElement(
                                Query.SPECIMEN,
                                "COLLECTION_PROTOCOL_ID")));

        Query query;
//        SimpleConditionsNode simpleConditionsNode;
//
//        query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY,
//                Query.PARTICIPANT);
//        query
//                .addElementToView(new DataElement(Query.PARTICIPANT,
//                        "IDENTIFIER"));
//        query.addElementToView(new DataElement(
//                Query.PARTICIPANT_MEDICAL_IDENTIFIER, "IDENTIFIER"));
//        //        query.addElementToView(new DataElement(Query.COLLECTION_PROTOCOL,"IDENTIFIER"));
//        //        query.addElementToView(new DataElement(Query.SPECIMEN,"IDENTIFIER"));
//        simpleConditionsNode = new SimpleConditionsNode(new Condition(
//                new DataElement("Participant", "LAST_NAME"), new Operator(
//                        Operator.EQUAL), "'SHARMA'"),
//                new Operator(Operator.AND));
//        ((SimpleQuery) query).addCondition(simpleConditionsNode);
//        simpleConditionsNode = new SimpleConditionsNode(new Condition(
//                new DataElement("Participant", "FIRST_NAME"), new Operator(
//                        Operator.EQUAL), "'aarti'"), new Operator(Operator.AND));
//        ((SimpleQuery) query).addCondition(simpleConditionsNode);
//
//        simpleConditionsNode = new SimpleConditionsNode(new Condition(
//                new DataElement(Query.COLLECTION_PROTOCOL_REGISTRATION,
//                        "REGISTRATION_DATE"), new Operator(Operator.EQUAL),
//                "'2005-08-16'"), new Operator(Operator.AND));
//        ((SimpleQuery) query).addCondition(simpleConditionsNode);
//
//        System.out.println("Query:\n" + query.getString());
//
//        /**
//         * Query for user
//         */
//        /*---------------------------------------------------------------- */
//        query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY,
//                Query.USER);
//        query.addElementToView(new DataElement(Query.CSM_USER, "FIRST_NAME"));
//        query.addElementToView(new DataElement(Query.INSTITUTION, "NAME"));
//        simpleConditionsNode = new SimpleConditionsNode(new Condition(
//                new DataElement(Query.CSM_USER, "LAST_NAME"), new Operator(
//                        Operator.EQUAL), "'SHARMA'"),
//                new Operator(Operator.AND));
//        ((SimpleQuery) query).addCondition(simpleConditionsNode);
//        System.out.println("\nQuery:\n" + query.getString());
//
//        /*---------------------------------------------------------------- */
//
//        query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY,
//                Query.USER);
//        query.addElementToView(new DataElement(Query.CSM_USER, "FIRST_NAME"));
//        query.addElementToView(new DataElement(Query.INSTITUTION, "NAME"));
//        query.addElementToView(new DataElement(Query.DEPARTMENT, "NAME"));
//        query.addElementToView(new DataElement(Query.CANCER_RESEARCH_GROUP,
//                "NAME"));
//        simpleConditionsNode = new SimpleConditionsNode(new Condition(
//                new DataElement(Query.ADDRESS, "COUNTRY"), new Operator(
//                        Operator.EQUAL), "'United States'"), new Operator(
//                Operator.AND));
//        ((SimpleQuery) query).addCondition(simpleConditionsNode);
//        System.out.println("\nQuery:\n" + query.getString());
//
//        /*---------------------------------------------------------------- */
//
//        /**
//         * Query for institution
//         */
//        query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY,
//                Query.INSTITUTION);
//        query.addElementToView(new DataElement(Query.INSTITUTION, "NAME"));
//        simpleConditionsNode = new SimpleConditionsNode(new Condition(
//                new DataElement(Query.INSTITUTION, "NAME"), new Operator(
//                        Operator.EQUAL), "'as'"), new Operator(Operator.AND));
//        ((SimpleQuery) query).addCondition(simpleConditionsNode);
//        System.out.println("\nQuery:\n" + query.getString());
//
//        /*---------------------------------------------------------------- */
//
//        /**
//         * Query for institution
//         */
//        query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY,
//                Query.DEPARTMENT);
//        query.addElementToView(new DataElement(Query.DEPARTMENT, "NAME"));
//        simpleConditionsNode = new SimpleConditionsNode(new Condition(
//                new DataElement(Query.DEPARTMENT, "NAME"), new Operator(
//                        Operator.EQUAL), "'AAA'"), new Operator(Operator.AND));
//        ((SimpleQuery) query).addCondition(simpleConditionsNode);
//        System.out.println("\nQuery:\n" + query.getString());
//
//        /*---------------------------------------------------------------- */
//
//        /**
//         * Query for cancer research group
//         */
//        query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY,
//                Query.CANCER_RESEARCH_GROUP);
//        query.addElementToView(new DataElement(Query.CANCER_RESEARCH_GROUP,
//                "NAME"));
//        simpleConditionsNode = new SimpleConditionsNode(new Condition(
//                new DataElement(Query.CANCER_RESEARCH_GROUP, "NAME"),
//                new Operator(Operator.EQUAL), "'crg1'"), new Operator(
//                Operator.AND));
//        ((SimpleQuery) query).addCondition(simpleConditionsNode);
//        System.out.println("\nQuery:\n" + query.getString());
//
//        /*---------------------------------------------------------------- */
//
//        /**
//         * Query for site
//         */
//        query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY,
//                Query.SITE);
//        query.addElementToView(new DataElement(Query.SITE, "NAME"));
//        //        query.addElementToView(new DataElement(Query.CSM_USER,"first_name"));
//        query.addElementToView(new DataElement(Query.ADDRESS, "COUNTRY"));
//        simpleConditionsNode = new SimpleConditionsNode(new Condition(
//                new DataElement(Query.SITE, "TYPE"), new Operator(
//                        Operator.EQUAL), "'Hospital'"), new Operator(
//                Operator.AND));
//        ((SimpleQuery) query).addCondition(simpleConditionsNode);
//        System.out.println("\nQuery:\n" + query.getString());
//
//        /*---------------------------------------------------------------- */
//
//        /**
//         * Query for storage type
//         */
//        query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY,
//                Query.STORAGE_TYPE);
//        query.addElementToView(new DataElement(Query.STORAGE_TYPE, "TYPE"));
//        query.addElementToView(new DataElement(
//                Query.STORAGE_CONTAINER_CAPACITY, "ONE_DIMENSION_LABEL"));
//        simpleConditionsNode = new SimpleConditionsNode(new Condition(
//                new DataElement(Query.STORAGE_TYPE, "TYPE"), new Operator(
//                        Operator.EQUAL), "'Freezer1'"), new Operator(
//                Operator.AND));
//        ((SimpleQuery) query).addCondition(simpleConditionsNode);
//        System.out.println("\nQuery:\n" + query.getString());
//
//        /*---------------------------------------------------------------- */
//
//        /**
//         * Query for bio hazard
//         */
//        query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY,
//                Query.BIO_HAZARD);
//        query.addElementToView(new DataElement(Query.BIO_HAZARD, "NAME"));
//        simpleConditionsNode = new SimpleConditionsNode(new Condition(
//                new DataElement(Query.BIO_HAZARD, Constants.ACTIVITY_STATUS_COLUMN), new Operator(
//                        Operator.EQUAL), "'"+Constants.ACTIVITY_STATUS_CLOSED+"'"), new Operator(
//                Operator.AND));
//        ((SimpleQuery) query).addCondition(simpleConditionsNode);
//        System.out.println("\nQuery:\n" + query.getString());
//        
//        
//        /*---------------------------------------------------------------- */
//
//        /**
//         * Query for collection protocol
//         */
//        query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY,
//                Query.COLLECTION_PROTOCOL);
//        query.addElementToView(new DataElement(Query.SPECIMEN_PROTOCOL, "TITLE"));
//        query.addElementToView(new DataElement(Query.USER, "identifier"));
//        query.addElementToView(new DataElement(Query.CSM_USER, "first_name"));
//        query.addElementToView(new DataElement(Query.COLLECTION_PROTOCOL_EVENT, "CLINICAL_STATUS"));
//        query.addElementToView(new DataElement(Query.SPECIMEN_REQUIREMENT, "SPECIMEN_TYPE"));
//        simpleConditionsNode = new SimpleConditionsNode(new Condition(
//                new DataElement(Query.SPECIMEN_REQUIREMENT, "SPECIMEN_TYPE"), new Operator(
//                        Operator.EQUAL), "'Blood'"), new Operator(
//                Operator.AND));
//        ((SimpleQuery) query).addCondition(simpleConditionsNode);
//        System.out.println("\nQuery:\n" + query.getString());
//
//                simpleConditionsNode = new SimpleConditionsNode(new Condition(new DataElement(Query.SPECIMEN,"TYPE"),
//          			   new Operator(Operator.EQUAL),"'Fluid'"),new Operator(Operator.AND));
//                 ((SimpleQuery)query).addCondition(simpleConditionsNode);

                query = QueryFactory.getInstance().newQuery(Query.ADVANCED_QUERY,Query.PARTICIPANT);
                query.addElementToView(new DataElement(Query.PARTICIPANT,"IDENTIFIER"));
                query.addElementToView(new DataElement(Query.COLLECTION_PROTOCOL,"IDENTIFIER"));
                query.addElementToView(new DataElement(Query.SPECIMEN_COLLECTION_GROUP,"IDENTIFIER"));
                query.addElementToView(new DataElement(Query.SPECIMEN,"PARENT_SPECIMEN_ID"));
                query.addElementToView(new DataElement(Query.SPECIMEN,"IDENTIFIER"));
                
                DefaultMutableTreeNode root = new DefaultMutableTreeNode();
                DefaultMutableTreeNode child1 ;
                DefaultMutableTreeNode child2 ;
                DefaultMutableTreeNode child3 ;
                DefaultMutableTreeNode child4 ;
                DefaultMutableTreeNode child5 ;
                DefaultMutableTreeNode child6 ;
                DefaultMutableTreeNode child7 ;
                DefaultMutableTreeNode child8 ;
                
                AdvancedConditionsNode advancedConditionsNode =new AdvancedConditionsNode(Query.PARTICIPANT);
                advancedConditionsNode.addConditionToNode(new Condition(new DataElement("Participant","gender",Constants.FIELD_TYPE_VARCHAR),
                        								  new Operator(Operator.EQUAL),"'Female'"));
                advancedConditionsNode.setOperationWithChildCondition(new Operator(Operator.OR));
                child1 = new DefaultMutableTreeNode(advancedConditionsNode);
                
                AdvancedConditionsNode advancedConditionsNode4 =new AdvancedConditionsNode(Query.COLLECTION_PROTOCOL); 
                advancedConditionsNode4.setOperationWithChildCondition(new Operator(Operator.OR));
                child4 = new DefaultMutableTreeNode(advancedConditionsNode4);
                
                AdvancedConditionsNode advancedConditionsNode5 =new AdvancedConditionsNode(Query.SPECIMEN_COLLECTION_GROUP); 
//                advancedConditionsNode5.addConditionToNode(new Condition(new DataElement(Query.SPECIMEN,"tissue_site"),new Operator(Operator.EQUAL),"'lung'"));
//                advancedConditionsNode5.addConditionToNode(new Condition(new DataElement(Query.SPECIMEN,"SPECIMEN_TYPE"),new Operator(Operator.EQUAL),"'tumor'"));
                advancedConditionsNode5.setOperationWithChildCondition(new Operator(Operator.OR));
                child5 = new DefaultMutableTreeNode(advancedConditionsNode5);
                
                AdvancedConditionsNode advancedConditionsNode6 =new AdvancedConditionsNode(Query.SPECIMEN); 
//                advancedConditionsNode6.addConditionToNode(new Condition(new DataElement(Query.SPECIMEN,"SPECIMEN_TYPE"),new Operator(Operator.EQUAL),"'non-malignant'"));
                advancedConditionsNode6.setOperationWithChildCondition(new Operator(Operator.EXIST));
                child6 = new DefaultMutableTreeNode(advancedConditionsNode6);
                
                AdvancedConditionsNode advancedConditionsNode7 =new AdvancedConditionsNode(Query.SPECIMEN); 
                advancedConditionsNode7.addConditionToNode(new Condition(new DataElement(Query.SPECIMEN,"tissue_site",Constants.FIELD_TYPE_TEXT),new Operator(Operator.EQUAL),"lung"));
                advancedConditionsNode7.addConditionToNode(new Condition(new DataElement(Query.SPECIMEN,"SPECIMEN_TYPE",Constants.FIELD_TYPE_TEXT),new Operator(Operator.EQUAL),"'tumor'"));
                advancedConditionsNode7.setOperationWithChildCondition(new Operator(Operator.OR));
                child7 = new DefaultMutableTreeNode(advancedConditionsNode7);

                ((AdvancedQuery)query).addCondition(advancedConditionsNode);
                AdvancedConditionsNode advancedConditionsNode2 =new AdvancedConditionsNode(Query.SPECIMEN); 
                advancedConditionsNode2.addConditionToNode(new Condition(new DataElement("Sample","Type"),new Operator(Operator.EQUAL),"'RNA'"));
                advancedConditionsNode2.addConditionToNode(new Condition(new DataElement("Sample","Quantity"),new Operator(Operator.GREATER_THAN),"5"));
                child2 = new DefaultMutableTreeNode(advancedConditionsNode2);
        
                //        System.out.println(((AdvancedQuery)query).addCondition(advancedConditionsNode,advancedConditionsNode2));
                AdvancedConditionsNode advancedConditionsNode3 =new AdvancedConditionsNode(Query.SPECIMEN); 
                advancedConditionsNode3.addConditionToNode(new Condition(new DataElement("Sample","Type"),new Operator(Operator.EQUAL),"'DNA'"));
                advancedConditionsNode3.addConditionToNode(new Condition(new DataElement("Sample","Quantity"),new Operator(Operator.GREATER_THAN),"5"));
                child3 = new DefaultMutableTreeNode(advancedConditionsNode3);
        //        System.out.println(((AdvancedQuery)query).addCondition(advancedConditionsNode,advancedConditionsNode3));
                
                
                AdvancedConditionsNode advancedConditionsNode8 =new AdvancedConditionsNode(Query.SPECIMEN); 
                advancedConditionsNode8.addConditionToNode(new Condition(new DataElement("Sample","Type"),new Operator(Operator.EQUAL),"'DNA'"));
                advancedConditionsNode8.addConditionToNode(new Condition(new DataElement("Sample","Quantity"),new Operator(Operator.GREATER_THAN),"5"));
                child8 = new DefaultMutableTreeNode(advancedConditionsNode8);

                root.add(child1);
                child1.add(child4);
                child4.add(child5);
                child5.add(child6);
                child5.add(child7);
        //        child7.add(child8);
        //        child6.add(child2);
        //        child6.add(child3);
                
                ((AdvancedConditionsImpl)((AdvancedQuery)query).whereConditions).setWhereCondition(root);
                System.out.println("\n\n"+query.getString()+"\n\n");
    }
   
}