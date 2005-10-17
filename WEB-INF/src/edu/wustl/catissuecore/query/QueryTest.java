package edu.wustl.catissuecore.query;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
 public class QueryTest {
	 	public Vector element = new Vector();
	 	int temp =0;
	 	//public static void main(String args[]){
	 	public Vector getTreeElement(){
	 	Query query;
      	query = QueryFactory.getInstance().newQuery(Query.ADVANCED_QUERY,Query.PARTICIPANT);
      	query.addElementToView(new DataElement(Query.PARTICIPANT,"IDENTIFIER"));
      	query.addElementToView(new DataElement(Query.COLLECTION_PROTOCOL,"IDENTIFIER"));
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
        advancedConditionsNode.addConditionToNode(new Condition(new DataElement("Participant","gender"),new Operator(Operator.EQUAL),"'Female'"));
        advancedConditionsNode.setOperationWithChildCondition(new Operator(Operator.OR));
        child1 = new DefaultMutableTreeNode(advancedConditionsNode);
        
        AdvancedConditionsNode advancedConditionsNode4 =new AdvancedConditionsNode(Query.COLLECTION_PROTOCOL); 
        advancedConditionsNode4.setOperationWithChildCondition(new Operator(Operator.OR));
        child4 = new DefaultMutableTreeNode(advancedConditionsNode4);
        
        AdvancedConditionsNode advancedConditionsNode5 =new AdvancedConditionsNode(Query.SPECIMEN); 
        advancedConditionsNode5.addConditionToNode(new Condition(new DataElement(Query.SPECIMEN,"tissue_site"),new Operator(Operator.EQUAL),"'lung'"));
        advancedConditionsNode5.addConditionToNode(new Condition(new DataElement(Query.SPECIMEN,"SPECIMEN_TYPE"),new Operator(Operator.EQUAL),"'tumor'"));
        advancedConditionsNode5.setOperationWithChildCondition(new Operator(Operator.OR));
        child5 = new DefaultMutableTreeNode(advancedConditionsNode5);
        
        AdvancedConditionsNode advancedConditionsNode6 =new AdvancedConditionsNode(Query.SPECIMEN); 
        advancedConditionsNode6.addConditionToNode(new Condition(new DataElement(Query.SPECIMEN,"SPECIMEN_TYPE"),new Operator(Operator.EQUAL),"'non-malignant'"));
        advancedConditionsNode6.setOperationWithChildCondition(new Operator(Operator.EXIST));
        child6 = new DefaultMutableTreeNode(advancedConditionsNode6);
        
        AdvancedConditionsNode advancedConditionsNode7 =new AdvancedConditionsNode(Query.SPECIMEN); 
        advancedConditionsNode7.addConditionToNode(new Condition(new DataElement(Query.SPECIMEN,"tissue_site"),new Operator(Operator.EQUAL),"'lung'"));
        advancedConditionsNode7.addConditionToNode(new Condition(new DataElement(Query.SPECIMEN,"SPECIMEN_TYPE"),new Operator(Operator.EQUAL),"'tumor'"));
        advancedConditionsNode7.setOperationWithChildCondition(new Operator(Operator.OR));
        child7 = new DefaultMutableTreeNode(advancedConditionsNode7);

        ((AdvancedQuery)query).addCondition(advancedConditionsNode);
        AdvancedConditionsNode advancedConditionsNode2 =new AdvancedConditionsNode(Query.SPECIMEN); 
        advancedConditionsNode2.addConditionToNode(new Condition(new DataElement("Sample","Type"),new Operator(Operator.EQUAL),"'RNA'"));
        advancedConditionsNode2.addConditionToNode(new Condition(new DataElement("Sample","Quantity"),new Operator(Operator.GREATER_THAN),"5"));
        child2 = new DefaultMutableTreeNode(advancedConditionsNode2);

               System.out.println(((AdvancedQuery)query).addCondition(advancedConditionsNode,advancedConditionsNode2));
        AdvancedConditionsNode advancedConditionsNode3 =new AdvancedConditionsNode(Query.SPECIMEN); 
        advancedConditionsNode3.addConditionToNode(new Condition(new DataElement("Sample","Type"),new Operator(Operator.EQUAL),"'DNA'"));
        advancedConditionsNode3.addConditionToNode(new Condition(new DataElement("Sample","Quantity"),new Operator(Operator.GREATER_THAN),"5"));
        child3 = new DefaultMutableTreeNode(advancedConditionsNode3);
        System.out.println(((AdvancedQuery)query).addCondition(advancedConditionsNode,advancedConditionsNode3));
        
        
        AdvancedConditionsNode advancedConditionsNode8 =new AdvancedConditionsNode(Query.SPECIMEN); 
        advancedConditionsNode8.addConditionToNode(new Condition(new DataElement("Sample","Type"),new Operator(Operator.EQUAL),"'DNA'"));
        advancedConditionsNode8.addConditionToNode(new Condition(new DataElement("Sample","Quantity"),new Operator(Operator.GREATER_THAN),"5"));
        child8 = new DefaultMutableTreeNode(advancedConditionsNode8);

        root.add(child1);
        child1.add(child4);
        child4.add(child6);
        child4.add(child7);
        child7.add(child8);
        child6.add(child2);
        child6.add(child3);
        QueryTest test = new QueryTest();
        Vector tree = test.arrangeTree(root,0);
        
        for(int i = 0; i < tree.size(); i++){
        	System.out.println("element at "+i+"---> "+tree.get(i));
        }
       
        return tree;
       // ((AdvancedConditionsImpl)((AdvancedQuery)query).whereConditions).setWhereCondition(root);
        //System.out.println(query.getString()+"\n\n");
	 }
	 	public Vector arrangeTree(DefaultMutableTreeNode node,int count){
			//int temp = count + 1;
			temp++;
			//System.out.println("temp-->"+temp);
			
			//if(!node.isLeaf()){
				for(int i = 0; i < node.getChildCount();i++){
					
					DefaultMutableTreeNode n = (DefaultMutableTreeNode)node.getChildAt(i);
					AdvancedConditionsNode advConditionNode = (AdvancedConditionsNode)n.getUserObject();
					Vector v = advConditionNode.getObjectConditions();
					//System.out.println("size-->"+v.size());
					String str = "";
					Condition con = null;
					for(int k = 0; k < v.size(); k++){
						con = (Condition)v.get(k);
						DataElement data = con.getDataElement();
			        	Operator op = con.getOperator();
			        	if(k == 0)
			        		str = temp + "|" + count + "|" +data.getTable()+": "+data.getField()+ " "+op.getOperator() + " "+con.getValue();
			        	else
			        		str = str +" "+"AND"+" "+data.getField()+" "+op.getOperator() + " "+con.getValue();
			        	
					}
					
				if(con == null){
					str = temp + "|" + count + "|"+advConditionNode.getObjectName()+": "+"ANY";
				}
				//System.out.println("str-->"+str);
				element.add(str);
				
					arrangeTree(n,temp);
	        }

			
			
		//}
				
			return element;
 }
//	 	public Vector getTreeElement(){
//	 		//System.out.println("element--"+element.size());
//	 		return element;
//	 	}
 }
 
