/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

package edu.wustl.catissuecore.query;

import java.util.HashMap;
import java.util.Vector;

public class Client
{
    /**
     * Map that stores the relation condition between two objects
     */
    public static HashMap relationConditionsForRelatedTables = new HashMap();
    
    /**
     * This stores the relation between objects where key is source object of the relation
     * and value is the target object of the relation
     * For example if Participant is the source and Accession is the target of a relation
     * then their is a value "Accession" associated with a key "Participant" in the map
     */
    public static HashMap relations = new HashMap();
    
    /**
     * This maps the objects with actual table names.
     */
    public static HashMap objectTableNames = new HashMap();
    
    public static void initialize()
    {
        setObjectTableNames();
        setRelationConditionsForRelatedTables();
        setRelations();
    }
    
    public static void setRelationConditionsForRelatedTables()
    {
        relationConditionsForRelatedTables.put(new Relation(Query.PARTICIPANT,Query.COLLECTION_PROTOCOL_REGISTRATION),new RelationCondition(new DataElement(Query.PARTICIPANT,"Identifier"), new Operator(Operator.EQUAL), new DataElement(Query.COLLECTION_PROTOCOL_REGISTRATION,"PARTICIPANT_ID")));
        relationConditionsForRelatedTables.put(new Relation(Query.COLLECTION_PROTOCOL, Query.COLLECTION_PROTOCOL_REGISTRATION),new RelationCondition(new DataElement(Query.COLLECTION_PROTOCOL,"Identifier"), new Operator(Operator.EQUAL), new DataElement(Query.COLLECTION_PROTOCOL_REGISTRATION,"COLLECTION_PROTOCOL_ID")));
        relationConditionsForRelatedTables.put(new Relation(Query.COLLECTION_PROTOCOL,Query.COLLECTION_PROTOCOL_EVENT),new RelationCondition(new DataElement(Query.COLLECTION_PROTOCOL,"Identifier"), new Operator(Operator.EQUAL), new DataElement(Query.COLLECTION_PROTOCOL_EVENT,"COLLECTION_PROTOCOL_ID")));
        relationConditionsForRelatedTables.put(new Relation(Query.COLLECTION_PROTOCOL_EVENT,Query.SPECIMEN_COLLECTION_GROUP),new RelationCondition(new DataElement(Query.COLLECTION_PROTOCOL_EVENT,"Identifier"), new Operator(Operator.EQUAL), new DataElement(Query.SPECIMEN_COLLECTION_GROUP,"COLLECTION_PROTOCOL_EVENT_ID")));
        relationConditionsForRelatedTables.put(new Relation(Query.SPECIMEN_COLLECTION_GROUP,Query.SPECIMEN),new RelationCondition(new DataElement(Query.SPECIMEN_COLLECTION_GROUP,"Identifier"), new Operator(Operator.EQUAL), new DataElement(Query.SPECIMEN,"SPECIMEN_COLLECTION_GROUP_ID")));
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
        relations.put(Query.COLLECTION_PROTOCOL_REGISTRATION,v3);
        relations.put(Query.COLLECTION_PROTOCOL,v);
        relations.put(Query.COLLECTION_PROTOCOL_EVENT,v4);
        relations.put(Query.SPECIMEN_COLLECTION_GROUP,v5);
    }
    
    public static void setObjectTableNames()
    {
        objectTableNames.put(Query.PARTICIPANT,"CATISSUE_PARTICIPANT");
        objectTableNames.put(Query.COLLECTION_PROTOCOL_REGISTRATION,"CATISSUE_COLLECTION_PROTOCOL_REGISTRATION");
        objectTableNames.put(Query.COLLECTION_PROTOCOL,"CATISSUE_COLLECTION_PROTOCOL");
        objectTableNames.put(Query.COLLECTION_PROTOCOL_EVENT,"CATISSUE_COLLECTION_PROTOCOL_EVENT");
        objectTableNames.put(Query.SPECIMEN_COLLECTION_GROUP,"CATISSUE_SPECIMEN_COLLECTION_GROUP");
        objectTableNames.put(Query.SPECIMEN,"CATISSUE_SPECIMEN");
    }
    
    public static void main(String[] args)
    {
        relationConditionsForRelatedTables.put(new Relation(Query.PARTICIPANT,Query.COLLECTION_PROTOCOL_REGISTRATION),new RelationCondition(new DataElement(Query.PARTICIPANT,"Identifier"), new Operator(Operator.EQUAL), new DataElement(Query.COLLECTION_PROTOCOL_REGISTRATION,"PARTICIPANT_ID")));
        relationConditionsForRelatedTables.put(new Relation(Query.COLLECTION_PROTOCOL, Query.COLLECTION_PROTOCOL_REGISTRATION),new RelationCondition(new DataElement(Query.COLLECTION_PROTOCOL,"Identifier"), new Operator(Operator.EQUAL), new DataElement(Query.COLLECTION_PROTOCOL_REGISTRATION,"COLLECTION_PROTOCOL_ID")));
        relationConditionsForRelatedTables.put(new Relation(Query.COLLECTION_PROTOCOL,Query.COLLECTION_PROTOCOL_EVENT),new RelationCondition(new DataElement(Query.COLLECTION_PROTOCOL,"Identifier"), new Operator(Operator.EQUAL), new DataElement(Query.COLLECTION_PROTOCOL_EVENT,"COLLECTION_PROTOCOL_ID")));
        relationConditionsForRelatedTables.put(new Relation(Query.COLLECTION_PROTOCOL_EVENT,Query.SPECIMEN_COLLECTION_GROUP),new RelationCondition(new DataElement(Query.COLLECTION_PROTOCOL_EVENT,"Identifier"), new Operator(Operator.EQUAL), new DataElement(Query.SPECIMEN_COLLECTION_GROUP,"COLLECTION_PROTOCOL_EVENT_ID")));
        relationConditionsForRelatedTables.put(new Relation(Query.SPECIMEN_COLLECTION_GROUP,Query.SPECIMEN),new RelationCondition(new DataElement(Query.SPECIMEN_COLLECTION_GROUP,"Identifier"), new Operator(Operator.EQUAL), new DataElement(Query.SPECIMEN,"SPECIMEN_COLLECTION_GROUP_ID")));
        
//        relations.put(Query.PARTICIPANT,Query.ACCESSION);
//        relations.put(Query.ACCESSION,Query.SPECIMEN);
//        relations.put(Query.SPECIMEN,Query.SEGMENT);
//        relations.put(Query.SEGMENT,Query.SAMPLE);
        
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
        relations.put(Query.COLLECTION_PROTOCOL_REGISTRATION,v3);
        relations.put(Query.COLLECTION_PROTOCOL,v);
        relations.put(Query.COLLECTION_PROTOCOL_EVENT,v4);
        relations.put(Query.SPECIMEN_COLLECTION_GROUP,v5);
        
        objectTableNames.put(Query.PARTICIPANT,"CATISSUE_PARTICIPANT");
        objectTableNames.put(Query.COLLECTION_PROTOCOL_REGISTRATION,"CATISSUE_COLLECTION_PROTOCOL_REGISTRATION");
        objectTableNames.put(Query.COLLECTION_PROTOCOL,"CATISSUE_COLLECTION_PROTOCOL");
        objectTableNames.put(Query.COLLECTION_PROTOCOL_EVENT,"CATISSUE_COLLECTION_PROTOCOL_EVENT");
        objectTableNames.put(Query.SPECIMEN_COLLECTION_GROUP,"CATISSUE_SPECIMEN_COLLECTION_GROUP");
        objectTableNames.put(Query.SPECIMEN,"CATISSUE_SPECIMEN");
        
        Query query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY,Query.PARTICIPANT);
        query.addElementToView(new DataElement(Query.PARTICIPANT,"IDENTIFIER"));
        query.addElementToView(new DataElement(Query.COLLECTION_PROTOCOL,"IDENTIFIER"));
        query.addElementToView(new DataElement(Query.SPECIMEN,"IDENTIFIER"));
        SimpleConditionsNode simpleConditionsNode = new SimpleConditionsNode(new Condition(
                									new DataElement("Participant","LAST_NAME"),
                									new Operator(Operator.EQUAL),"'SHARMA'"),
                									new Operator(Operator.AND));
        ((SimpleQuery)query).addCondition(simpleConditionsNode);
        simpleConditionsNode = new SimpleConditionsNode(new Condition(new DataElement("Participant","FIRST_NAME"),
                			   new Operator(Operator.EQUAL),"'aarti'"),new Operator(Operator.AND));
        ((SimpleQuery)query).addCondition(simpleConditionsNode);
        System.out.println(query.getString()+"\n\n");
        
//        query = QueryFactory.getInstance().newQuery(Query.ADVANCED_QUERY,Query.PARTICIPANT);
//        query.addElementToView(new DataElement(Query.PARTICIPANT,"IDENTIFIER"));
//        query.addElementToView(new DataElement(Query.COLLECTION_PROTOCOL,"IDENTIFIER"));
//        query.addElementToView(new DataElement(Query.SPECIMEN,"IDENTIFIER"));
//        
//        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
//        DefaultMutableTreeNode child1 ;
//        DefaultMutableTreeNode child2 ;
//        DefaultMutableTreeNode child3 ;
//        DefaultMutableTreeNode child4 ;
//        DefaultMutableTreeNode child5 ;
//        DefaultMutableTreeNode child6 ;
//        DefaultMutableTreeNode child7 ;
//        DefaultMutableTreeNode child8 ;
//        
//        AdvancedConditionsNode advancedConditionsNode =new AdvancedConditionsNode(Query.PARTICIPANT);
//        advancedConditionsNode.addConditionToNode(new Condition(new DataElement("Participant","gender"),
//                								  new Operator(Operator.EQUAL),"'Female'"));
//        advancedConditionsNode.setOperationWithChildCondition(new Operator(Operator.OR));
//        child1 = new DefaultMutableTreeNode(advancedConditionsNode);
//        
//        AdvancedConditionsNode advancedConditionsNode4 =new AdvancedConditionsNode(Query.COLLECTION_PROTOCOL); 
//        advancedConditionsNode4.setOperationWithChildCondition(new Operator(Operator.OR));
//        child4 = new DefaultMutableTreeNode(advancedConditionsNode4);
//        
//        AdvancedConditionsNode advancedConditionsNode5 =new AdvancedConditionsNode(Query.SPECIMEN); 
//        advancedConditionsNode5.addConditionToNode(new Condition(new DataElement(Query.SPECIMEN,"tissue_site"),new Operator(Operator.EQUAL),"'lung'"));
//        advancedConditionsNode5.addConditionToNode(new Condition(new DataElement(Query.SPECIMEN,"SPECIMEN_TYPE"),new Operator(Operator.EQUAL),"'tumor'"));
//        advancedConditionsNode5.setOperationWithChildCondition(new Operator(Operator.OR));
//        child5 = new DefaultMutableTreeNode(advancedConditionsNode5);
//        
//        AdvancedConditionsNode advancedConditionsNode6 =new AdvancedConditionsNode(Query.SPECIMEN); 
//        advancedConditionsNode6.addConditionToNode(new Condition(new DataElement(Query.SPECIMEN,"SPECIMEN_TYPE"),new Operator(Operator.EQUAL),"'non-malignant'"));
//        advancedConditionsNode6.setOperationWithChildCondition(new Operator(Operator.EXIST));
//        child6 = new DefaultMutableTreeNode(advancedConditionsNode6);
//        
//        AdvancedConditionsNode advancedConditionsNode7 =new AdvancedConditionsNode(Query.SPECIMEN); 
//        advancedConditionsNode7.addConditionToNode(new Condition(new DataElement(Query.SPECIMEN,"tissue_site"),new Operator(Operator.EQUAL),"'lung'"));
//        advancedConditionsNode7.addConditionToNode(new Condition(new DataElement(Query.SPECIMEN,"SPECIMEN_TYPE"),new Operator(Operator.EQUAL),"'tumor'"));
//        advancedConditionsNode7.setOperationWithChildCondition(new Operator(Operator.OR));
//        child7 = new DefaultMutableTreeNode(advancedConditionsNode7);
        
        
//        ((AdvancedQuery)query).addCondition(advancedConditionsNode);
//        AdvancedConditionsNode advancedConditionsNode2 =new AdvancedConditionsNode(Query.SPECIMEN); 
//        advancedConditionsNode2.addConditionToNode(new Condition(new DataElement("Sample","Type"),new Operator(Operator.EQUAL),"'RNA'"));
//        advancedConditionsNode2.addConditionToNode(new Condition(new DataElement("Sample","Quantity"),new Operator(Operator.GREATER_THAN),"5"));
//        child2 = new DefaultMutableTreeNode(advancedConditionsNode2);
//
//        //        System.out.println(((AdvancedQuery)query).addCondition(advancedConditionsNode,advancedConditionsNode2));
//        AdvancedConditionsNode advancedConditionsNode3 =new AdvancedConditionsNode(Query.SPECIMEN); 
//        advancedConditionsNode3.addConditionToNode(new Condition(new DataElement("Sample","Type"),new Operator(Operator.EQUAL),"'DNA'"));
//        advancedConditionsNode3.addConditionToNode(new Condition(new DataElement("Sample","Quantity"),new Operator(Operator.GREATER_THAN),"5"));
//        child3 = new DefaultMutableTreeNode(advancedConditionsNode3);
////        System.out.println(((AdvancedQuery)query).addCondition(advancedConditionsNode,advancedConditionsNode3));
//        
//        
//        AdvancedConditionsNode advancedConditionsNode8 =new AdvancedConditionsNode(Query.SPECIMEN); 
//        advancedConditionsNode8.addConditionToNode(new Condition(new DataElement("Sample","Type"),new Operator(Operator.EQUAL),"'DNA'"));
//        advancedConditionsNode8.addConditionToNode(new Condition(new DataElement("Sample","Quantity"),new Operator(Operator.GREATER_THAN),"5"));
//        child8 = new DefaultMutableTreeNode(advancedConditionsNode8);
        
//        root.add(child1);
//        child1.add(child4);
//        child4.add(child6);
//        child4.add(child7);
////        child7.add(child8);
////        child6.add(child2);
////        child6.add(child3);
//        
//        ((AdvancedConditionsImpl)((AdvancedQuery)query).whereConditions).setWhereCondition(root);
//        System.out.println(query.getString()+"\n\n");
    }
}