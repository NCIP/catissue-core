<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="java.util.Map,java.util.List,java.util.ListIterator"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ConfigureResultViewForm"%>
<%@ page import="java.util.*"%>

<head>
<%
    HashMap tableDataMap = (HashMap) request.getAttribute(Constants.TABLE_COLUMN_DATA_MAP);
    Set tableNamesList = tableDataMap.keySet();
%>
<SCRIPT LANGUAGE="JavaScript">
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
		    			String values = tableNameVal+"."+columnData.getValue()+"."+columnData.getName();
		    			columnNamesArray = columnNamesArray + "\"" + values + "\"";
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
	  			deleteOption(theSelFrom, i);
    		}
		}
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
	  if(obj.options[obj.options.selectedIndex].index > 0)
	  {
	    current = obj.options[obj.options.selectedIndex].text;
	    reverse = obj.options[obj.options[obj.options.selectedIndex].index-1].text;
	    obj.options[obj.options.selectedIndex].text = reverse;
	    obj.options[obj.options[obj.options.selectedIndex].index-1].text = current;
	    self.focus();
	    obj.options.selectedIndex--;
	  }
	}
	
	function moveDown(obj)
	{
	  var currernt;
	  var next;
	  if(obj.options[obj.options.selectedIndex].index != obj.length-1)
	  {
	    current = obj.options[obj.options.selectedIndex].text;
	    next = obj.options[obj.options[obj.options.selectedIndex].index+1].text;
	    obj.options[obj.options.selectedIndex].text =  next;
	    obj.options[obj.options[obj.options.selectedIndex].index+1].text = current;
	    self.focus();
	    obj.options.selectedIndex++;
	  }
	}
</script>
<%
    ConfigureResultViewForm form = (ConfigureResultViewForm)request.getAttribute("configureResultViewForm");
%>
</head>
<html:errors/>
<html:form action="<%=Constants.DISTRIBUTION_REPORT_ACTION%>">
    <table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="1000">
		<tr>
		    <td>
			 	<table summary="" cellpadding="3" cellspacing="0" border="0">
					<tr>
						<td align="right" colspan="3">
							<html:hidden property="nextAction" value="configure"/>
						</td>
						<td align="right" colspan="3">
							<html:hidden property="distributionId"/>
						</td>
				  	</tr>
				</table>
			<td>
		</tr>
		
		<tr>
			<td>
				<table summary="" cellpadding="3" cellspacing="0" border="0" >
				<tr>	
					<td class="formTitle" height="20" colspan="10">
            			<bean:message key="configure.result"/>
            		</td>    
        		</tr>
        		<tr>
            		<td class="formRequiredLabel">
                		<label for="tableName">
                    		<bean:message key="table.names"/>
               			</label>
            		</td>
            		<td class="formField" colspan="4">
                	<%
                	pageContext.setAttribute(Constants.TABLE_NAMES_LIST, tableNamesList);
                	%>
                
                	<html:select property="tableName" styleClass="formFieldSized15"  size="1" styleId="tableName" onchange="onTypeChange(this)">
                 		<html:options collection="<%=Constants.TABLE_NAMES_LIST%>" labelProperty="name" property="value"/>   
	                </html:select>
    		        </td>
        		</tr>
        		<tr>
            		<td class="formRequiredLabel">
                		<label for="columnNames">
                    		<bean:message key="column.names"/>
                		</label>
            		</td>				    
		            <td class="formField" colspan="4">
		            <!-- --------------------------------------- -->
		            <%
		                String tableName = (String)form.getTableName();
		                List columnNamesList = (List)tableDataMap.get(tableName);
		                if(columnNamesList == null)
		                {
		                    columnNamesList = new ArrayList();
		                    columnNamesList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
		                }
		                pageContext.setAttribute(Constants.COLUMN_NAMES_LIST, columnNamesList);
		            %>
		            <!-- ----------------------------------------->
		                <html:select property="columnNames" styleClass="formFieldSized"  styleId="columnNames" size="10"  multiple="true">
	                    	<html:options collection="<%=Constants.COLUMN_NAMES_LIST%>" labelProperty="name" property="value"/>
		                </html:select>
			            </td>
			            <td align="center" valign="middle">
			                <html:button styleClass="actionButton" property="shiftRight" value="-->" styleClass="actionButton" styleId ="shiftRight" onclick="moveOptionsRight(this.form.columnNames, this.form.selectedColumnNames);" /><br />
			                <html:button styleClass="actionButton" property="shiftLeft" value="<--" styleClass="actionButton" styleId ="shiftLeft" onclick="moveOptionsLeft(this.form.selectedColumnNames, this.form.columnNames);" />
			            </td>
			            <td class="formField">
			                <html:select property="selectedColumnNames" styleClass="formFieldSized" styleId="selectedColumnNames" size="10" multiple="true">
			                    
			                    
			                </html:select>
			            </td>
			            <td align="center" valign="middle">
			                <html:button styleClass="actionButton" property="shiftUp" value="Up" styleClass="actionButton" styleId ="shiftUp" onclick="moveUp(this.form.selectedColumnNames);"/><br/><br/>
			                <html:button styleClass="actionButton" property="shiftDown" value="Down" styleClass="actionButton" styleId ="shiftDown" onclick="moveDown(this.form.selectedColumnNames)" />
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
	   					<html:submit styleClass = "actionButton" onclick = "selectOptions(this.form.selectedColumnNames);">
	   						<bean:message key="buttons.submit"/>
	   					</html:submit>
	   				</td>
					<td>
						<html:reset styleClass="actionButton">
							<bean:message key="buttons.reset"/>
						</html:reset>
					</td> 
				
				</tr>
			</table>
<!-- action buttons ends -->			
            </td>
        </tr>
    </table>
</html:form>




