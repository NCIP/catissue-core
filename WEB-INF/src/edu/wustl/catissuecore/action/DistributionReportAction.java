package edu.wustl.catissuecore.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ConfigureResultViewForm;
import edu.wustl.catissuecore.actionForm.DistributionReportForm;
import edu.wustl.catissuecore.bizlogic.AbstractBizLogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.query.DataElement;
import edu.wustl.catissuecore.query.Operator;
import edu.wustl.catissuecore.query.Query;
import edu.wustl.catissuecore.query.QueryFactory;
import edu.wustl.catissuecore.query.SimpleConditionsNode;
import edu.wustl.catissuecore.query.SimpleQuery;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.util.ExportReport;
import edu.wustl.common.util.logger.Logger;


public class DistributionReportAction extends Action
{
	public String [] columnNames;
	public Vector setViewElements(String []selectedColumnsList) 
	{
			columnNames=new String[selectedColumnsList.length];
		    Vector vector = new Vector();
		    for(int i=0;i<selectedColumnsList.length;i++)
		    {
		    	StringTokenizer st= new StringTokenizer(selectedColumnsList[i],".");
		    	DataElement dataElement = new DataElement();
		    	while (st.hasMoreTokens())
				{
		    		dataElement.setTable(st.nextToken());
		    		
		    		dataElement.setField(st.nextToken());
		    		columnNames[i]=st.nextToken();
		    		Logger.out.debug("Selected column names in configuration "+columnNames[i]);
				}
		        vector.add(dataElement);
		    }
		    return vector;
		    
	}
    /**
     * Overrides the execute method of Action class.
     * */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
    	Long distributionId = (Long)request.getAttribute(Constants.DISTRIBUTION_ID);
    	Logger.out.debug("distributionId "+distributionId);
    	
    	ConfigureResultViewForm configForm = (ConfigureResultViewForm)form;
    	
    	if(distributionId!=null)
    		configForm.setDistributionId(distributionId);
    	else
    		distributionId = configForm.getDistributionId();
    	Logger.out.debug("distributionId "+distributionId);
    	AbstractBizLogic bizLogic = BizLogicFactory.getBizLogic(Constants.DISTRIBUTION_FORM_ID);
    	List list = bizLogic.retrieve(Distribution.class.getName(),Constants.SYSTEM_IDENTIFIER,distributionId);
    	Distribution dist = (Distribution) list.get(0);
    	
    	DistributionReportForm distributionReportForm = new DistributionReportForm();
    	distributionReportForm.setAllValues(dist);
    	
    	List listOfData = new ArrayList();
    	Collection distributedItemCollection = dist.getDistributedItemCollection();		
    	
    	String []specimenIds = new String[distributedItemCollection.size()];
    	int i=0;
    	Iterator itr = distributedItemCollection.iterator();
    	while(itr.hasNext())
    	{
    		DistributedItem item = (DistributedItem)itr.next();
    		Specimen specimen = item.getSpecimen();
    		Logger.out.debug("Specimen "+specimen);
    		Logger.out.debug("Specimen "+specimen.getSystemIdentifier());
    		specimenIds[i] = specimen.getSystemIdentifier().toString();
    		i++;
    	}
    	String action = configForm.getNextAction();
    	
    	Logger.out.debug("Configure/Default action "+action);
    	String selectedColumns[] = getSelectedColumns(action,configForm);
    	Logger.out.debug("Selected columns length"+selectedColumns.length);
    	if(selectedColumns.length!=0)
    	{
    		for(int k=0;k<selectedColumns.length;k++)
    		{
    			Logger.out.debug("Selected columns in configuration "+selectedColumns[k]);
    			
    		}
    	}
    	
    	for(int j=0;j<specimenIds.length;j++)
    	{
    		Collection simpleConditionNodeCollection = new ArrayList();
    		Query query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY, "Participant");
    		Logger.out.debug("Specimen ID" +specimenIds[j]);
    		SimpleConditionsNode simpleConditionsNode = new SimpleConditionsNode();
    		simpleConditionsNode.getCondition().setValue(specimenIds[j]);
    		simpleConditionsNode.getCondition().getDataElement().setTable("Specimen");
    		simpleConditionsNode.getCondition().getDataElement().setField("Identifier");
    		simpleConditionsNode.getCondition().getOperator().setOperator("=");
    		
    		
    		SimpleConditionsNode simpleConditionsNode1 = new SimpleConditionsNode();
    		simpleConditionsNode1.getCondition().getDataElement().setTable("DistributedItem");
    		simpleConditionsNode1.getCondition().getDataElement().setField("Distribution_Id");
    		simpleConditionsNode1.getCondition().getOperator().setOperator("=");
    		simpleConditionsNode1.getCondition().setValue(distributionId.toString());
    		simpleConditionsNode1.setOperator(new Operator("And"));
    		
    		simpleConditionNodeCollection.add(simpleConditionsNode1);
    		simpleConditionNodeCollection.add(simpleConditionsNode);
    		((SimpleQuery)query).addConditions(simpleConditionNodeCollection);
    		
    		Set tableSet = new HashSet();
    		tableSet.add("Participant");
    		tableSet.add("Specimen");
    		tableSet.add("CollectionProtocolRegistration");
    		tableSet.add("SpecimenCollectionGroup");
    		tableSet.add("DistributedItem");
    		query.setTableSet(tableSet);
    		
    		Vector vector = setViewElements(selectedColumns);
    		query.setResultView(vector);
    		
    		List list1 = query.execute();
    		Logger.out.debug("Size of the Data from the database" +list1.size());
    		Iterator listItr = list1.iterator();
    		while(listItr.hasNext())
    		{
    			Logger.out.debug("Data from the database according to the selected columns"+ listItr.next());
    		}
    		listOfData.add(list1);
    	}
    	
    	
    	
    	request.setAttribute("distributionReportForm", distributionReportForm);
    	request.setAttribute("columnNames", columnNames);
    	request.setAttribute("listOfData", listOfData);
    	//request.setAttribute("selectedColumns",selectedColumns);
    	
        if(!configForm.isReportAction())
        {
        	
        	configForm.setReportAction(true);
        	Logger.out.debug("configForm.isReportAction() "+configForm.isReportAction());
        	String filename = Variables.catissueHome+System.getProperty("file.separator")+"Temp.csv";
        	saveReport(distributionReportForm,listOfData,filename);
        	
        	sendFile(request,response,filename);
        }
		return (mapping.findForward("Success"));
}
    public String [] getSelectedColumns(String action,ConfigureResultViewForm form)
    {
    	if(("configure").equals(action))
    	{
    		String selectedColumns[] = form.getSelectedColumnNames();
    		Logger.out.debug("Selected columns length"+selectedColumns.length);
    		return selectedColumns;
    	}
    	else 
    	{
    		String selectedColumns[] ={"Specimen.IDENTIFIER.Specimen Identifier","Specimen.TYPE.Specimen Type","DistributedItem.QUANTITY.Specimen Quantity"};
    		form.setSelectedColumnNames(selectedColumns);
    		return selectedColumns;
    	}
    	
    }
    private List createList(String key,String value,List list)
    {
    	List newList = new ArrayList();
    	newList.add(ApplicationProperties.getValue(key));
    	newList.add(value);
    	list.add(newList);
    	return list;
    }
    public void saveReport(DistributionReportForm distributionReportForm,List listOfData,String fileName) throws IOException
    {
    	Logger.out.debug("Save action");
    	List distributionData = new ArrayList();
    	distributionData = createList("distribution.protocol",distributionReportForm.getDistributionProtocolTitle(),distributionData);
    	distributionData = createList("eventparameters.user",distributionReportForm.getUserName(),distributionData);
    	distributionData = createList("eventparameters.dateofevent",distributionReportForm.getDateOfEvent(),distributionData);
    	distributionData = createList("eventparameters.time",distributionReportForm.getTimeInHours()
    													+":"+distributionReportForm.getTimeInMinutes(),distributionData);
    	distributionData = createList("distribution.fromSite",distributionReportForm.getFromSite(),distributionData);
    	distributionData = createList("distribution.toSite",distributionReportForm.getToSite(),distributionData);
    	
    	distributionData = createList("eventparameters.comments",distributionReportForm.getComments(),distributionData);
    	
    	ExportReport report = new ExportReport(fileName);
    	report.writeData(distributionData);
    	List distributedItemsColumns = new ArrayList();
    	List columns = new ArrayList();
    	for(int k=0;k<columnNames.length;k++)
    	{
    		columns.add(columnNames[k]);
    		Logger.out.debug("Selected columns in save action "+columnNames[k]);
    	}
    	distributedItemsColumns.add(columns);
    	report.writeData(distributedItemsColumns);
    	Iterator listItr = listOfData.iterator();
    	Logger.out.debug("List of Data in save action "+listOfData);
    	while(listItr.hasNext())
    	{
    		report.writeData((List)listItr.next());
    	}
    	report.closeFile();
    	
    }
    public void sendFile(HttpServletRequest request, HttpServletResponse response,String filename)
    {
    	//String filename = null;
        //String fileName = null;
        StringBuffer sb = new StringBuffer();
//        HttpSession session = null;
        //indicates the invertigator id
        int invid;
        String prefix = null;
        try
        {
            
            //filename = Variables.absolutePathForDownloadRoot+"/"+request.getParameter("idno")+"/"+request.getParameter("dwnFile");
            //fileName = request.getParameter("dwnFile");
		    //prefix = Constants.Logger_Inv+ ": ";

            //Getting the DownLoad Bean object from the session object
            if ( filename != null && (false == (filename.length()==0)) )
            {
                File f = new File(filename);
                //Logger.log(prefix+"FileName to Download = "+filename,Logger.INFO);
                if (f.exists())
                {
                    response.setContentType("application/csv");
                    response
                            .setHeader("Content-Disposition",
                                    " filename=\""+filename+"\";");
                    response.setContentLength((int) f.length());

                    try
                    {
                        OutputStream os = response.getOutputStream();
                        FileInputStream stream = new FileInputStream(f);
                        BufferedInputStream bis = new BufferedInputStream(
                                stream);
                        InputStream is = new BufferedInputStream(bis);
                        int count;
                        byte buf[] = new byte[4096];
                        
                        while ((count = is.read(buf)) > -1) {
                            os.write(buf, 0, count);
                        }    
                        os.flush();
                        is.close();
                        os.close();
                    }
                    catch (Exception ex)
                    {
                        
                        throw new Exception(ex.getMessage());
                    }//end try/catch

                }
                else
                {
                    
                    throw new Exception("Sorry Cannot Download as fileName is null");
                }//end if
            }//end if    
            /*else
            {
                Logger.log(prefix+"FileName is null or has zero Length = "+filename,Logger.INFO);
            }*/
        }

        catch (Exception e)
        {
//            boolean success = (new File(filename)).delete();
            //Logger.log(prefix+"Sorry Cannot Download - Error Occured = "+e.getMessage(),Logger.WARNING);
//            if (!success)
//            {
//                // Deletion failed
//                Logger.log(prefix+"deletion failed for file  "+filename,Logger.INFO);
//             
//            }
        }

    
    }
}