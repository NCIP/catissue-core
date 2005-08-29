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
	String [] cellSpecimenIdArray = (String [])request.getAttribute(Constants.CELL_SPECIMEN_ID_LIST);
	String [] fluidSpecimenIdArray = (String [])request.getAttribute(Constants.FLUID_SPECIMEN_ID_LIST);
	String [] molecularSpecimenIdArray = (String [])request.getAttribute(Constants.MOLECULAR_SPECIMEN_ID_LIST);
	String [] tissueSpecimenIdArray = (String [])request.getAttribute(Constants.TISSUE_SPECIMEN_ID_LIST);
	DistributionForm formBean = (DistributionForm)request.getAttribute("distributionForm");
%>
<head>
	<script language="JavaScript">
		
		function onSpecimenTypeChange(element,unitSpan,idCombo,hiddenProperty)
		{
			comboToRefresh = document.getElementById(idCombo);
			//To Clear the Combo Box
			comboToRefresh.options.length = 0;
			var unit = document.getElementById(unitSpan);
			var unitSpecimen = "";
			var unitProperty = document.getElementById(hiddenProperty);
			
			if(element.value == "Tissue")
			{
				unitSpecimen = "<%=Constants.UNIT_GM%>";
				unitProperty.value = "<%=Constants.UNIT_GM%>";
				<%
					if(tissueSpecimenIdArray != null && tissueSpecimenIdArray.length != 0)
					{
						for(int i=0;i<tissueSpecimenIdArray.length;i++)
						{
				%>
							comboToRefresh.options[<%=i%>] = new Option('<%=tissueSpecimenIdArray[i]%>','<%=tissueSpecimenIdArray[i]%>');
				<%
						}
					}
				%>
			}
			else if(element.value == "Fluid")
			{
				unitSpecimen = "<%=Constants.UNIT_ML%>";
				unitProperty.value = "<%=Constants.UNIT_ML%>";
				<%
					if(fluidSpecimenIdArray != null && fluidSpecimenIdArray.length != 0)
					{
						for(int i=0;i<fluidSpecimenIdArray.length;i++)
						{
				%>
							comboToRefresh.options[<%=i%>] = new Option('<%=fluidSpecimenIdArray[i]%>','<%=fluidSpecimenIdArray[i]%>');
				<%
						}
					}
				%>
			}
			else if(element.value == "Cell")
			{
				unitSpecimen = "<%=Constants.UNIT_CC%>";
				unitProperty.value = "<%=Constants.UNIT_CC%>";
				<%
					if(cellSpecimenIdArray != null && cellSpecimenIdArray.length != 0)
					{
						for(int i=0;i<cellSpecimenIdArray.length;i++)
						{
				%>
							comboToRefresh.options[<%=i%>] = new Option('<%=cellSpecimenIdArray[i]%>','<%=cellSpecimenIdArray[i]%>');
				<%
						}
					}
				%>
			}
			else if(element.value == "Molecular")
			{
				unitSpecimen = "<%=Constants.UNIT_MG%>";
				unitProperty.value = "<%=Constants.UNIT_MG%>";
				<%
					if(molecularSpecimenIdArray != null && molecularSpecimenIdArray.length != 0)
					{
						for(int i=0;i<molecularSpecimenIdArray.length;i++)
						{
				%>
							comboToRefresh.options[<%=i%>] = new Option('<%=molecularSpecimenIdArray[i]%>','<%=molecularSpecimenIdArray[i]%>');
				<%
						}
					}
				%>
			}
			else
			{
				comboToRefresh.options[0] = new Option('<%=Constants.SELECT_OPTION%>','<%=Constants.SELECT_OPTION%>');
			}
			
			unit.innerHTML = unitSpecimen;
		}
		
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
			var spreqspecimen=x.insertCell(1);
			spreqspecimen.className="formField";
			sname="";
			var unitName = "value(DistributedItem:" + (q+1) + "_unitSpan)";
			var className = "value(DistributedItem:"+ (q+1) +"_Specimen_className)";
			sname= "";
			var name = "value(DistributedItem:" + (q+1) + "_Specimen_systemIdentifier)";
			var hiddenUnitName = "value(DistributedItem:" + (q+1) + "_unit)";
			var fName = "onSpecimenTypeChange(this,'" + unitName + "','" + name + "','" + hiddenUnitName + "')";
			sname="<select name='" + className + "' size='1' class='formFieldSized10' id='" + className + "' onChange=" + fName + ">";
			<%
				for(int i=0;i<Constants.SPECIMEN_TYPE_VALUES.length;i++)
				{
			%>
						sname = sname + "<option value='<%=Constants.SPECIMEN_TYPE_VALUES[i]%>'><%=Constants.SPECIMEN_TYPE_VALUES[i]%></option>";
			<%
				}
			%>
			sname = sname + "</select><input type='hidden' name='" + hiddenUnitName + "' value='' id='" + hiddenUnitName + "'>";
			spreqspecimen.innerHTML="" + sname;
			
			//Third Cell
			var spreqtype=x.insertCell(2);
			spreqtype.className="formField";
			sname="";

			//var name = "value(DistributedItem:" + (q+1) + "_Specimen_systemIdentifier)";
			sname="<select name='" + name + "' size='1' class='formFieldSized10' id='" + name + "'>";
			sname = sname + "<option value='<%=Constants.SELECT_OPTION%>'><%=Constants.SELECT_OPTION%></option>";
			sname = sname + "</select>";
			spreqtype.innerHTML="" + sname;
			
			//Fourth Cellvalue()
			var spreqsubtype=x.insertCell(3);
			spreqsubtype.className="formField";
			sname="";
		
			name = "value(DistributedItem:" + (q+1) + "_quantity)";
			sname= "";
			sname="<input type='text' name='" + name + "' size='30'  class='formFieldSized10' id='" + name + "'>";
			sname = sname + "&nbsp;<span id='" + unitName + "'>&nbsp;</span>";
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
<html:form action="<%=Constants.DISTRIBUTION_ADD_ACTION%>">    
	<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">

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
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="eventparameters.dateofevent"/> 
				</label>
			</td>
			<td class="formField">
				 <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
				<html:text styleClass="formDateSized" size="35" styleId="dateOfEvent" property="dateOfEvent" readonly="true"/>
				<a href="javascript:show_calendar('distributionForm.dateOfEvent',null,null,'MM-DD-YYYY');">
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
	</table>
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="433">
<!--  Distributed Item begin here -->
				 <tr>
				     <td class="formTitle" height="20" colspan="3">
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
						<bean:message key="distribution.specimenType"/>
					</td>
					<td class="formRightSubTableTitle">
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
					String unitSpan = "value(DistributedItem:"+i+"_unitSpan)";
					String className = "value(DistributedItem:"+i+"_Specimen_className)";
					String key = "DistributedItem:" + i + "_Specimen_className";
					String unitKey = "DistributedItem:" + i + "_unit";
					String unitProperty = "value(DistributedItem:"+i+"_unit)";
					String fName = "onSpecimenTypeChange(this,'" + unitSpan + "','" + itemName + "','" + unitProperty + "')";
					
					String temp = String.valueOf(formBean.getValue(unitKey));
					
					if(temp.equals("null"))
						temp = "";
				%>
				 <tr>
				 	<td class="formSerialNumberField" width="5"><%=i%>.
				 	<%--html:hidden property="<%=unitProperty%>"/--%>
				 	<input type="hidden" property="<%=unitProperty%>" id="<%=unitProperty%>" />
				 	</td>
				 	<td class="formField">
				     	<html:select property="<%=className%>" styleClass="formFieldSized10" styleId="<%=className%>" size="1" disabled="<%=readOnlyForAll%>" onchange="<%=fName%>">
							<html:options name="<%=Constants.SPECIMEN_CLASS_LIST%>" labelName="<%=Constants.SPECIMEN_CLASS_LIST%>"/>
						</html:select>
		        	</td>
		        	
				    <td class="formField">
						<html:select property="<%=itemName%>" styleClass="formFieldSized10" styleId="<%=itemName%>" size="1" disabled="<%=readOnlyForAll%>">
							<%
								String keyValue = (String)formBean.getValue(key);

								if(keyValue != null)
								{
								if(keyValue.equals("Cell"))
								{
							%>
								<html:options name="<%=Constants.CELL_SPECIMEN_ID_LIST%>" labelName="<%=Constants.CELL_SPECIMEN_ID_LIST%>" />
							<%
								}
								else if(keyValue.equals("Fluid"))
								{
							%>
								<html:options name="<%=Constants.FLUID_SPECIMEN_ID_LIST%>" labelName="<%=Constants.FLUID_SPECIMEN_ID_LIST%>" />
							<%
								}
								else if(keyValue.equals("Molecular"))
								{
							%>
								<html:options name="<%=Constants.MOLECULAR_SPECIMEN_ID_LIST%>" labelName="<%=Constants.MOLECULAR_SPECIMEN_ID_LIST%>" />
							<%
								}
								else if(keyValue.equals("Tissue"))
								{
							%>
								<html:options name="<%=Constants.TISSUE_SPECIMEN_ID_LIST%>" labelName="<%=Constants.TISSUE_SPECIMEN_ID_LIST%>" />
							<%
								}
								else
								{
							%>
								<html:option value="<%=Constants.SELECT_OPTION%>"><%=Constants.SELECT_OPTION%></html:option>
							<%
								}
								}
								else
								{
							%>
								<html:option value="<%=Constants.SELECT_OPTION%>"><%=Constants.SELECT_OPTION%></html:option>
							<%
								}
							%>
						</html:select>
					</td>
				    <td class="formField">
				     	<html:text styleClass="formFieldSized10" size="30" styleId="<%=quantity%>" property="<%=quantity%>" readonly="<%=readOnlyForAll%>"/>
				     	<span id="<%=unitSpan%>">&nbsp;<%=temp%></span>
				    </td>
				 </tr>
				 <%
				}
				%>
				 </tbody>
				 
			
			<!-- Distributed item End here -->
<!-- buttons -->
		<tr>
		  <td align="right" colspan="6">
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
	 
	 </table>
</html:form>			
