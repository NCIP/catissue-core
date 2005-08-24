<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.DistributionForm"%>
<%@ page import="java.util.List,java.util.ListIterator"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>

<%
	List itemList = (List)request.getAttribute(Constants.ITEMLIST);
	ListIterator iterator=null;
%>
<head>
	<script language="JavaScript">
		//function to insert a row in the inner block
		function insRow(subdivtag)
		{
			var val = parseInt(document.forms[0].counter.value);
			val = val + 1;
			document.forms[0].counter.value = val;
			
			var r = new Array(); 
			r = document.getElementById(subdivtag).rows;
			var q = r.length;
			var x=document.getElementById(subdivtag).insertRow(q);
			
			// First Cell
			var spreqno=x.insertCell(0);
			spreqno.className="formSerialNumberField";
			sname=(q+1);
			spreqno.innerHTML="" + sname;

			//Second Cell
			var spreqtype=x.insertCell(1);
			spreqtype.className="formField";
			sname="";

			var name = "value(DistributedItem:" + (q+1) + "_Specimen_systemIdentifier)";
			sname="<select name='" + name + "' size='1' class='formFieldSized15' id='" + name + "'>";
			<%
				if(itemList!=null)
				{
					iterator = itemList.listIterator();
					while(iterator.hasNext())
					{
						NameValueBean bean = (NameValueBean)iterator.next();
			%>
						sname = sname + "<option value='<%=bean.getValue()%>'><%=bean.getName()%></option>";
			<%		}
				}
			%>
			sname = sname + "</select>";
			spreqtype.innerHTML="" + sname;
		
			//Third Cellvalue()
			var spreqsubtype=x.insertCell(2);
			spreqsubtype.className="formField";
			sname="";
		
			name = "value(DistributedItem:" + (q+1) + "_quantity)";
			sname= "";
			sname="<input type='text' name='" + name + "' size='30'  class='formFieldSized15' id='" + name + "'>";
			spreqsubtype.innerHTML="" + sname;
		}
	</script>
</head>


<%
        String operation = (String) request.getAttribute(Constants.OPERATION);
        String formName;
        String searchFormName = new String(Constants.DISTRIBUTION_SEARCH_ACTION);
		boolean readOnlyValue=false,readOnlyForAll=false;
       
        if (operation.equals(Constants.EDIT))
        {
            formName = Constants.DISTRIBUTION_EDIT_ACTION;
            readOnlyValue = true;
        }
        else
        {
            formName = Constants.DISTRIBUTION_ADD_ACTION;
            readOnlyValue = false;
        }
		
		String pageOf = (String)request.getAttribute(Constants.PAGEOF);

		Object obj = request.getAttribute("distributionForm");
		int noOfRows=1;

		if(obj != null && obj instanceof DistributionForm)
		{
			DistributionForm form = (DistributionForm)obj;
			noOfRows = form.getCounter();
		}
%>	
			
<html:errors/>
    
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">

<html:form action="<%=Constants.DISTRIBUTION_ADD_ACTION%>">

	<logic:notEqual name="operation" value="<%=Constants.ADD%>">  
	<!-- ENTER IDENTIFIER BEGINS-->	
	<br/>	
	<tr>
		<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0">
				<tr>
					<td class="formTitle" height="20" colspan="3">
						<bean:message key="distribution.searchTitle"/>
					</td>
				</tr>	
		  
				<tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel">
						<label for="systemIdentifier">
							<bean:message key="eventparameters.systemIdentifier"/>
						</label>
					</td>
					<td class="formField">
						<html:text styleClass="formFieldSized" size="30" styleId="systemIdentifier" property="systemIdentifier"/>
					</td>
				</tr>	
				<%
					String changeAction = "setFormAction('" + searchFormName
							  + "');setOperation('" + Constants.SEARCH + "');";
				%>
				<tr>
					<td align="right" colspan="3">
					<table cellpadding="4" cellspacing="0" border="0">
						<tr>
							<td>
								<html:submit styleClass="actionButton" value="Search" onclick="<%=changeAction%>" />
							</td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<!-- ENTER IDENTIFIER ENDS-->	
	</logic:notEqual>

			  
			   	
			  
	<!-- NEW distribution REGISTRATION BEGINS-->
	<tr>
	<td>
	
	<table summary="" cellpadding="3" cellspacing="0" border="0">
		<tr>
			<td><html:hidden property="operation" value="<%=operation%>"/></td>
			<td><html:hidden property="counter"/></td>
		</tr>

		<logic:notEqual name="operation" value="<%=Constants.SEARCH%>">
		<tr>
			 <td class="formMessage" colspan="3">* indicates a required field</td>
		</tr>

		<tr>
			 <td class="formTitle" height="20" colspan="3">
				<bean:message key="distribution.title"/>
			 </td>
		</tr>

		<!-- Name of the distribution -->
<!-- Distribution Protocol Id -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="distribution.protocol"/> 
				</label>	
			</td>	
			<td class="formField">
				<html:select property="distributionProtocolId" styleClass="formFieldSized" styleId="distributionProtocolId" size="1">
					<html:options collection="<%=Constants.DISTRIBUTIONPROTOCOLLIST%>" labelProperty="name" property="value"/>
				</html:select>
			</td>	
		</tr>

<!-- User -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="eventparameters.user"/> 
				</label>	
			</td>	
			<td class="formField">
				<html:select property="userId" styleClass="formFieldSized" styleId="userId" size="1">
					<html:options collection="<%=Constants.USERLIST%>" labelProperty="name" property="value"/>
				</html:select>
			</td>	
		</tr>

<!-- date -->		
		<tr>
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formLabel">
				<label for="type">
					<bean:message key="eventparameters.dateofevent"/> 
				</label>
			</td>
			<td class="formField">
				 <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
				<html:text styleClass="formDateSized" size="35" styleId="dateOfEvent" property="dateOfEvent" readonly="true"/>
				<a href="javascript:show_calendar('distributionForm.dateOfEvent','','','MM-DD-YYYY');">
					<img src="images\calendar.gif" width=24 height=22 border=0>
				</a>
			</td>
		</tr>

<!-- hours & minutes -->		
		<tr>
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formLabel">
				<label for="type">
					<bean:message key="eventparameters.timeinhours"/>&nbsp; 
					<bean:message key="eventparameters.timeinminutes"/> 
				</label>
			</td>
			<td class="formField">
				<html:select property="timeInHours" styleClass="formFieldSized5" styleId="timeInHours" size="1">
					<html:options name="<%=Constants.HOURLIST%>" labelName="<%=Constants.HOURLIST%>" />
				</html:select>&nbsp;
				<html:select property="timeInMinutes" styleClass="formFieldSized5" styleId="timeInMinutes" size="1">
					<html:options name="<%=Constants.MINUTESLIST%>" labelName="<%=Constants.MINUTESLIST%>" />
				</html:select>
			</td>
		</tr>
<!-- fromSite -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="distribution.fromSite"/> 
				</label>
			</td>
			<td class="formField">
				<html:select property="fromSite" styleClass="formFieldSized" styleId="fromSite" size="1">
					<html:options collection="<%=Constants.FROMSITELIST%>" labelProperty="name" property="value"/>
				</html:select>

			</td>
		</tr>
<!-- toSite -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="distribution.toSite"/> 
				</label>
			</td>
			<td class="formField">
				<html:select property="toSite" styleClass="formFieldSized" styleId="toSite" size="1">
					<html:options collection="<%=Constants.TOSITELIST%>" labelProperty="name" property="value"/>
				</html:select>

			</td>
		</tr>				

<!-- comments -->		
		<tr>
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formLabel">
				<label for="type">
					<bean:message key="eventparameters.comments"/> 
				</label>
			</td>
			<td class="formField">
				<html:textarea styleClass="formFieldSized"  styleId="comments" property="comments" />
			</td>
		</tr>
<!--  Distributed Item begin here -->
				 <tr>
				     <td class="formTitle" height="20" colspan="2">
				     	<bean:message key="distribution.distributedItem"/>
				     </td>
				     <td class="formButtonField">
						<html:button property="addKeyValue" styleClass="actionButton" onclick="insRow('addMore')">
						<bean:message key="buttons.addMore"/>
						</html:button>
				    </td>
				  </tr>
				 <tr>
				 	<td class="formSerialNumberLabel" width="5">
				     	#
				    </td>
				    <td class="formLeftSubTableTitle">
						<bean:message key="itemrecord.specimenId"/>
					</td>
				    <td class="formRightSubTableTitle">
						<bean:message key="itemrecord.quantity"/>
					</td>
				 </tr>
				 
				 <tbody id="addMore">
				<%
				for(int i=1;i<=noOfRows;i++)
				{
					String itemName = "value(DistributedItem:"+i+"_Specimen_systemIdentifier)";
					String quantity = "value(DistributedItem:"+i+"_quantity)";
				%>
				 <tr>
				 	<td class="formSerialNumberField" width="5"><%=i%>.</td>
				    <td class="formField">
						<html:select property="<%=itemName%>" styleClass="formFieldSized15" styleId="<%=itemName%>" size="1" disabled="<%=readOnlyForAll%>">
							<html:options collection="<%=Constants.ITEMLIST%>" labelProperty="name" property="value"/>		
						</html:select>
					</td>
				    <td class="formField">
				     	<html:text styleClass="formFieldSized15" size="30" styleId="<%=quantity%>" property="<%=quantity%>" readonly="<%=readOnlyForAll%>"/>
				    </td>
				 </tr>
				 <%
				}
				%>
				 </tbody>
				 
			
			<!-- Distributed item End here -->
<!-- buttons -->
		<tr>
		  <td align="right" colspan="3">
			<!-- action buttons begins -->
			<%
        		String changeAction = "setFormAction('" + formName + "');";
			%> 
			<table cellpadding="4" cellspacing="0" border="0">
				<tr>
					<td>
						<html:submit styleClass="actionButton" value="Submit" onclick="<%=changeAction%>" />
					</td>
					<td><html:reset styleClass="actionButton"/></td> 
				</tr>
			</table>
			<!-- action buttons end -->
			</td>
		</tr>

		</logic:notEqual>
		</table>
		
	  </td>
	 </tr>

	 <!-- NEW Distribution ends-->
	 
	 </html:form>
 </table>
			
