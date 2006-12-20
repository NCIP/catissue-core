<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="java.util.Map,java.util.List,java.util.ListIterator"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ConfigureResultViewForm"%>
<%@ page import="edu.wustl.catissuecore.actionForm.AdvanceSearchForm"%>
<%@ page import="edu.wustl.common.actionForm.SimpleQueryInterfaceForm"%>
<%@ page import="java.util.*"%>

<head>
<%
    HashMap tableDataMap = (HashMap) request.getAttribute(Constants.TABLE_COLUMN_DATA_MAP);
    Set tableNamesList = tableDataMap.keySet();
    String pageOf = (String)request.getAttribute("pageOf");
    String callAction = new String();
    String selectedColumns[] = null;
    String [] columnNames=null;


    if(pageOf.equals(Constants.PAGEOF_SIMPLE_QUERY_INTERFACE))
	{
		callAction=Constants.SIMPLE_SEARCH_AFTER_CONFIGURE_ACTION+"?pageOf="+Constants.PAGEOF_SIMPLE_QUERY_INTERFACE;
	    SimpleQueryInterfaceForm form = (SimpleQueryInterfaceForm)request.getAttribute("simpleQueryInterfaceForm");
	    selectedColumns = form.getSelectedColumnNames();

	}
	else if(pageOf.equals(Constants.PAGEOF_DISTRIBUTION))
	{
		callAction=Constants.DISTRIBUTION_REPORT_ACTION;
		ConfigureResultViewForm form = (ConfigureResultViewForm)request.getAttribute("configureResultViewForm");
		selectedColumns = form.getSelectedColumnNames();

	}
	else if(pageOf.equals(Constants.PAGE_OF_DISTRIBUTION_CP_QUERY))
	{
		callAction=Constants.CP_QUERY_DISTRIBUTION_REPORT_ACTION;
		ConfigureResultViewForm form = (ConfigureResultViewForm)request.getAttribute("configureResultViewForm");
		selectedColumns = form.getSelectedColumnNames();

	}

	else if(pageOf.equals(Constants.PAGEOF_QUERY_RESULTS))
	{
		callAction = Constants.CONFIGURE_ADVANCED_SEARCH_RESULTS_ACTION;
		AdvanceSearchForm form = (AdvanceSearchForm)request.getAttribute("advanceSearchForm");
		selectedColumns = form.getSelectedColumnNames();

	}
    if(selectedColumns!=null)
    {	    
    	columnNames=new String[selectedColumns.length];
    	for(int i=0;i<selectedColumns.length;i++)
    	{
    		//Split the string which is in the form TableAlias.columnNames.columnDisplayNames
    		StringTokenizer st= new StringTokenizer(selectedColumns[i],".");
    		while (st.hasMoreTokens())
    		{
    			st.nextToken();
    			st.nextToken();
    			columnNames[i]=st.nextToken();
	    		if(st.hasMoreTokens())
	    			st.nextToken();

    		}
    	}
    }
%>
<SCRIPT LANGUAGE="JavaScript">
	//var pageOf = <%=pageOf%>;
    var size = <%=(tableNamesList.size()-1)%>;
    var tableDataArray=new Array(size);
	<%
	    	int count=0;
	    	Iterator tableDataIterator = tableNamesList.iterator();
	    	while(tableDataIterator.hasNext())
	    	{
	    		NameValueBean keyObj = ((NameValueBean)tableDataIterator.next());
	    		
	    		String tableName = keyObj.getName();
	    		String tableNameVal = keyObj.getValue();
	    		
	    		if(!tableName.equals(Constants.SELECT_OPTION))
	    		{
		    		String columnDisplayNamesArray = "";
		    		String columnNamesArray = "";		    		
		    		List subList = (List)tableDataMap.get(keyObj);
		    		
		    		int subListSize = subList.size();
		    		for(int listSize=0;listSize<subListSize;listSize++ )
		    		{
		    			NameValueBean columnData = (NameValueBean)subList.get(listSize);
		    			//String columnName = columnData.getValue();
		    			columnDisplayNamesArray = columnDisplayNamesArray + "\"" + columnData.getName() + "\"";
		    			//String values = columnData.getValue();
		    			columnNamesArray = columnNamesArray + "\"" + columnData.getValue() + "\"";
		    			if(listSize != subListSize - 1)
		    			{
			    			columnDisplayNamesArray = columnDisplayNamesArray + ",";   
			    			columnNamesArray = columnNamesArray + ",";   
			    		}
		    		}
	%>
					tableDataArray[<%=count%>]=new Array(3);
					tableDataArray[<%=count%>][0]="<%=tableNameVal%>";
					tableDataArray[<%=count%>][1]=new Array(<%=columnDisplayNamesArray%>);
					tableDataArray[<%=count%>][2]=new Array(<%=columnNamesArray%>);					
	<%	    		
					count++;
				}
	    	}	
	%>
	    
    function addOption(theSel, theText, theValue)
    {
	    var newOpt = new Option(theText, theValue);
	    var selLength = theSel.length;
	    var exists="false";
	    for(var i=0;i<selLength; i++)
	    {
			if(theSel.options[i].value==theValue)
				{
					exists="true";
					break;
				}
	    }
	    if(exists=="false")
		    theSel.options[selLength] = newOpt;
	}
	
    function deleteOption(theSel, theIndex)
    { 
	    var selLength = theSel.length;
	    if(selLength>0)
	    {
	    theSel.options[theIndex] = null;
	    }
   	}
	
    function moveOptionsRight(theSelFrom, theSelTo)
    {
  
	    var selLength = theSelFrom.length;
	    var selectedText = new Array();
	    var selectedValues = new Array();
	    var selectedCount = 0;
	  
	    var i;
	  
	    // Find the selected Options in reverse order
	    // and delete them from the 'from' Select.
	    for(i=selLength-1; i>=0; i--)
	    {
		    if(theSelFrom.options[i].selected)
		    {
			    selectedText[selectedCount] = theSelFrom.options[i].text;
			    selectedValues[selectedCount] = theSelFrom.options[i].value;
			    //deleteOption(theSelFrom, i);
			    selectedCount++;
		    }
	    }
	  
	    // Add the selected text/values in reverse order.
	    // This will add the Options to the 'to' Select
	    // in the same order as they were in the 'from' Select.
	    for(i=selectedCount-1; i>=0; i--)
	    {
	    	addOption(theSelTo, selectedText[i], selectedValues[i]);
	    }
	    if(selectedCount==0)
			alert("Please select column name.");
	    
    }
   	function moveOptionsLeft(theSelFrom, theSelTo)
	{
		var selLength = theSelFrom.length;
		var selectedCount = 0;
		var i;
		for(i=selLength-1; i>=0; i--)
		{
		    if(theSelFrom.options[i].selected)
		    {
		    	selectedCount++;
	  			deleteOption(theSelFrom, i);
    		}
		}
		if(selectedCount==0)
			alert("Please select column name.");
	}
    function typeChange(namesArray,valuesArray)
    { 
	    var columnsList = "columnNames";
	    ele = document.getElementById(columnsList);
	    //To Clear the Combo Box
	    ele.options.length = 0;
				
	    //ele.options[0] = new Option('-- Select --','-1');
	    var j=0;
	    //Populating the corresponding Combo Box
	    for(i=0;i<namesArray.length;i++)
	    {
	    	ele.options[j++] = new Option(namesArray[i],valuesArray[i]);
	    }
    }

    function onTypeChange(element)
    {
    	
    	for(i=0;i<tableDataArray.length;i++)
    	{
    		if(element.value == tableDataArray[i][0])
    		{
    			var namesArray = tableDataArray[i][1];
    			var valuesArray = tableDataArray[i][2];
    			break;
    		}
    	}
    	typeChange(namesArray,valuesArray);
    }
    function selectOptions(element)
	{
		for(i=0;i<element.length;i++) 
		{
			element.options[i].selected=true;
		}
	}
	function moveUp(obj)
	{
	  var currernt;
	  var reverse;
	  var currerntValue;
	  var reverseValue;
	  
	  if(obj.options[obj.options.selectedIndex].index > 0)
	  {
	    current = obj.options[obj.options.selectedIndex].text;
	    currentValue = obj.options[obj.options.selectedIndex].value;
	    reverse = obj.options[obj.options[obj.options.selectedIndex].index-1].text;
	    reverseValue = obj.options[obj.options[obj.options.selectedIndex].index-1].value;
	    obj.options[obj.options.selectedIndex].text = reverse;
	    obj.options[obj.options.selectedIndex].value = reverseValue;
	    obj.options[obj.options[obj.options.selectedIndex].index-1].text = current;
	    obj.options[obj.options[obj.options.selectedIndex].index-1].value = currentValue;
	    self.focus();
	    obj.options.selectedIndex--;
	  }
	}
	
	function moveDown(obj)
	{
	  var currernt;
	  var reverse;
	  var currerntValue;
	  var reverseValue;
	  if(obj.options[obj.options.selectedIndex].index != obj.length-1)
	  {
	    current = obj.options[obj.options.selectedIndex].text;
	    currentValue = obj.options[obj.options.selectedIndex].value;
	    reverse = obj.options[obj.options[obj.options.selectedIndex].index+1].text;
	    reverseValue = obj.options[obj.options[obj.options.selectedIndex].index+1].value;
	    obj.options[obj.options.selectedIndex].text = reverse;
	    obj.options[obj.options.selectedIndex].value = reverseValue;
	    obj.options[obj.options[obj.options.selectedIndex].index+1].text = current;
	    obj.options[obj.options[obj.options.selectedIndex].index+1].value = currentValue;
	    self.focus();
	    obj.options.selectedIndex++;
	  }
	}
	function onClickAction(action)
	{
		selectOptions(document.forms[0].selectedColumnNames);
		document.forms[0].action = action;
		if(action == "AdvanceSearchResults.do")
		{
			document.forms[0].target = "_parent";
		}
		document.forms[0].submit();
	}
	
	function onSubmit(theSelTo)
	{		
		if(theSelTo.length==0)
		{
			alert("We need to add atleast one column to define view");
		}
		else
		{
			var action = "<%=callAction%>";			
			onClickAction(action);
		}
	}
	
</script>
<!-- Mandar : 434 : for tooltip -->
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
</head>
<html:errors/>
<html:form action="<%=callAction%>">
    <table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="1000">
		<tr>
		    <td>
			 	<table summary="" cellpadding="3" cellspacing="0" border="0">
					<tr>
						<logic:equal name="pageOf" value="<%=Constants.PAGEOF_DISTRIBUTION%>">
							<td align="right" colspan="3">
								<html:hidden property="nextAction" value="configure"/>

							</td>
						
							<td align="right" colspan="3">
								<html:hidden property="distributionId"/>
								<html:hidden property="pageOf" value="<%=pageOf%>"/>
							</td>
						</logic:equal>

						<logic:equal name="pageOf" value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
							<html:hidden property="counter"/>
						</logic:equal>
						<logic:equal name="pageOf" value="<%=Constants.PAGE_OF_DISTRIBUTION_CP_QUERY%>">
							<td align="right" colspan="3">
								<html:hidden property="nextAction" value="configure"/>
							</td>
						
							<td align="right" colspan="3">
								<html:hidden property="distributionId"/>
								<html:hidden property="pageOf" value="<%=pageOf%>"/>
							</td>
						</logic:equal>
				  	</tr>
				</table>
			<td>
		</tr>
		
		<tr>
			<td>
				<table summary="" cellpadding="3" cellspacing="0" border="1" >
				<tr>	
					<td class="formTitle" height="20" colspan="10">
            			<bean:message key="configure.result"/>
            		</td>    
        		</tr>
        		<tr>
            		<td class="formRequiredLabel" align="left">
                		<label for="tableName">
                    		<bean:message key="table.names"/>
               			</label>
            		</td>
            		<td class="formField" colspan="4">
                	<%
                	pageContext.setAttribute(Constants.TABLE_NAMES_LIST, tableNamesList);
                	%>
<!-- Mandar : 434 : for tooltip -->                
                	<html:select property="tableName" styleClass="formFieldSized15"  size="1" styleId="tableName" onchange="onTypeChange(this)"
					 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
                		<html:option value="-1"><%=Constants.SELECT_OPTION%></html:option>
                 		<html:options collection="<%=Constants.TABLE_NAMES_LIST%>" labelProperty="name" property="value"/>   
	                </html:select>
    		        </td>
        		</tr>
        		<tr>
            		<td class="formRequiredLabel" align="left">
                		<label for="columnNames">
                    		<bean:message key="column.names"/>
                		</label>
            		</td>				    
		            <td class="formField" colspan="4">
		            <!-- --------------------------------------- -->
		            <%
		                /*String tableName = (String)form.getTableName();
		                List columnNamesList = (List)tableDataMap.get(tableName);
		                if(columnNamesList == null)
		                {
		                    columnNamesList = new ArrayList();
		                    columnNamesList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
		                }
		                pageContext.setAttribute(Constants.COLUMN_NAMES_LIST, columnNamesList);*/
		            %>
		            <!-- ----------------------------------------->
<!-- Mandar : 434 : for tooltip -->
		                <html:select property="columnNames" styleClass="formFieldSized"  styleId="columnNames" size="10"  multiple="true"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
	                    	
		                </html:select>
			            </td>
			            <td align="center" valign="middle">
			                <html:button styleClass="actionButton" property="shiftRight" styleClass="actionButton" styleId ="shiftRight" onclick="moveOptionsRight(this.form.columnNames, this.form.selectedColumnNames);">
			                	<bean:message key="buttons.addToView"/>
			                </html:button>
			                <br/><br/>
			                <html:button styleClass="actionButton" property="shiftLeft" styleClass="actionButton" styleId ="shiftLeft" onclick="moveOptionsLeft(this.form.selectedColumnNames, this.form.columnNames);" >
				                <bean:message key="buttons.deleteFromView"/>
				            </html:button>  
			            </td>
			            <td class="formField">
<!-- Mandar : 434 : for tooltip -->
			                <html:select property="selectedColumnNames" styleClass="formFieldSized" styleId="selectedColumnNames" size="10" multiple="true"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<% if(selectedColumns!=null)
						      {
						    	for(int i=0;i<selectedColumns.length;i++) {
			                %>
			                	<option value="<%=selectedColumns[i]%>"><%=columnNames[i]%></option>  
			                <%
			                	}
			                  }
			                %>  

			                </html:select>
			            </td>
			            <td align="center" valign="middle">
			                <html:button styleClass="actionButton" property="shiftUp" styleClass="actionButton" styleId ="shiftUp" onclick="moveUp(this.form.selectedColumnNames);">
			                	<bean:message key="buttons.up"/>
				            </html:button>  <br/><br/>
			                
			                <html:button styleClass="actionButton" property="shiftDown" styleClass="actionButton" styleId ="shiftDown" onclick="moveDown(this.form.selectedColumnNames)" >
			                	<bean:message key="buttons.down"/>
				            </html:button> 
			            </td>
		        	</tr>

       			</table>
      		</td>
      	</tr>
      	<tr>
	  		<td>
<!-- action buttons begins -->
			<table cellpadding="4" cellspacing="0" border="0">
				<tr>
	   				<td>
	   					<%String nAction =  "onClickAction('"+callAction+"')";%>
	   					<html:button styleClass="actionButton" property="configButton" onclick = "onSubmit(this.form.selectedColumnNames);" >
	   						<bean:message key="buttons.submit"/>
	   					</html:button>
	   				</td>
					<!--td>
						<html:reset styleClass="actionButton">
							<bean:message key="buttons.reset"/>
						</html:reset>
					</td--> 
				
				</tr>
			</table>
<!-- action buttons ends -->			
            </td>
        </tr>
    </table>
</html:form>




