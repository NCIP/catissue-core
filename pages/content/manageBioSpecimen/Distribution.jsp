							<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.DistributionForm"%>
<%@ page import="java.util.List,java.util.Iterator"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>

<%!
	private String changeUnit(String specimenType)
	{
		if (specimenType == null)
			return "";
		if(specimenType.equals("Fluid"))
			return Constants.UNIT_ML;
		else if(specimenType.equals("Tissue"))
			return Constants.UNIT_GM;
		else if(specimenType.equals("Cell"))
			return Constants.UNIT_CC;
		else if(specimenType.equals("Molecular"))
			return Constants.UNIT_MG;
		else
			return " ";
			
	}
%>

<%
	//List itemList = (List)request.getAttribute(Constants.ITEMLIST);
	//ListIterator iterator=null;
	/*String [] cellSpecimenIdArray = (String [])request.getAttribute(Constants.CELL_SPECIMEN_ID_LIST);
	String [] fluidSpecimenIdArray = (String [])request.getAttribute(Constants.FLUID_SPECIMEN_ID_LIST);
	String [] molecularSpecimenIdArray = (String [])request.getAttribute(Constants.MOLECULAR_SPECIMEN_ID_LIST);
	String [] tissueSpecimenIdArray = (String [])request.getAttribute(Constants.TISSUE_SPECIMEN_ID_LIST);*/
	List specimenIdList = (List)request.getAttribute(Constants.SPECIMEN_ID_LIST);
	DistributionForm formBean = (DistributionForm)request.getAttribute("distributionForm");
	
%>
<head>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
	<script language="JavaScript">
		function onSpecimenIdChange(element)
		{	
			document.forms[0].idChange.value="true";
			setFormAction("<%=Constants.DISTRIBUTION_ACTION%>"+element.name);
			document.forms[0].submit();
		}
		//function to insert a row in the inner block
		function insRow(subdivtag)
		{
			var val = parseInt(document.forms[0].counter.value);
			val = val + 1;
			document.forms[0].counter.value = val;
			var sname = "";
			
			var r = new Array(); 
			r = document.getElementById(subdivtag).rows;
			var q = r.length;
			var x=document.getElementById(subdivtag).insertRow(q);
			
			// First Cell
			var spreqno=x.insertCell(0);
			spreqno.className="formSerialNumberField";
			var rowno=(q+1);
			//spreqno.innerHTML="" + sname;
			var identifier = "value(DistributedItem:" + rowno +"_systemIdentifier)";
			var cell1 = "<input type='hidden' name='" + identifier + "' value='' id='" + identifier + "'>";
			var className = "value(DistributedItem:" + rowno +"_Specimen_className)";
			var cell2 = "<input type='hidden' name='" + className + "' value='' id='" + className + "'>";
			
			
			spreqno.innerHTML="" + rowno+"." + cell1 + cell2 ;

			//Second Cell
			var spreqidentifier=x.insertCell(1);
			spreqidentifier.className="formField";
			sname="";

			var name = "value(DistributedItem:" + rowno + "_Specimen_systemIdentifier)";
			sname="<select name='" + name + "' size='1' class='formField' id='" + name + "' onchange='onSpecimenIdChange(this)'>";
			
			<%for(int i=0;i<specimenIdList.size();i++)
			{%>
				sname = sname + "<option value='<%=((NameValueBean)specimenIdList.get(i)).getValue()%>'><%=((NameValueBean)specimenIdList.get(i)).getName()%></option>";
			<%}%>
			sname = sname + "</select>";
			//var hiddenUnitName = "value(DistributedItem:" + (q+1) + "_unit)";
			//sname = sname + "<input type='hidden' name='" + hiddenUnitName + "' value='' id='" + hiddenUnitName + "'>";
			
			spreqidentifier.innerHTML="" + sname;
	
			//Third Cell
			var spreqtissueSite=x.insertCell(2);
			spreqtissueSite.className="formField";
			sname="";
		
			name = "value(DistributedItem:" + rowno + "_tissueSite)";
			sname= "";
			sname="<input type='text' name='" + name + "' class='formField' id='" + name + "'>";
			spreqtissueSite.innerHTML="" + sname;
			
			//Fourth Cell
			var spreqtissueSide=x.insertCell(3);
			spreqtissueSide.className="formField";
			sname="";
		
			name = "value(DistributedItem:" + rowno + "_tissueSide)";
			sname= "";
			sname="<input type='text' name='" + name + "'   class='formField' id='" + name + "'>";
			spreqtissueSide.innerHTML="" + sname;
			
			//Fifth Cell
			var spreqpathologicalStatus=x.insertCell(4);
			spreqpathologicalStatus.className="formField";
			sname="";
		
			name = "value(DistributedItem:" + rowno + "_pathologicalStatus)";
			sname= "";
			sname="<input type='text' name='" + name + "' class='formField' id='" + name + "'>";
			spreqpathologicalStatus.innerHTML="" + sname;
			
			//Sixh Cell
			var spreqquantity=x.insertCell(5);
			spreqquantity.className="formField";
			sname="";
		
			name = "value(DistributedItem:" + rowno + "_quantity)";
			sname= "";
			sname="<input type='text' name='" + name + "' size='30'  class='formFieldSized5' id='" + name + "'>";
			var unitName ="value(DistributedItem:"+rowno+"_unitSpan)";
			//var unitValue = changeUnit(document.forms[0].className.value)
			sname = sname + "&nbsp;<span id='"+unitName+"'>&nbsp;</span>";
			spreqquantity.innerHTML="" + sname;
			
			//Seventh Cell
			var checkb=x.insertCell(6);
			checkb.className="formField";
			checkb.colSpan=2;
			sname="";
			var name = "chk_"+ rowno;
			sname="<input type='checkbox' name='" + name +"' id='" + name +"' value='C'>"
			checkb.innerHTML=""+sname;	
		}
		function changeAction()
		{
			setFormAction("<%=Constants.DISTRIBUTION_REPORT_ACTION%>");
		}
		</script>
</head>


<%
        String operation = (String) request.getAttribute(Constants.OPERATION);
        String formName;

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
		Map map =null;
		if(obj != null && obj instanceof DistributionForm)
		{
			DistributionForm form = (DistributionForm)obj;
			noOfRows = form.getCounter();
			map = form.getValues();
		}
%>	
			
<html:errors/>
<html:form action="<%=Constants.DISTRIBUTION_ADD_ACTION%>">    
	<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">

	<!-- NEW distribution REGISTRATION BEGINS-->
	<tr>
	<td>
	
	<table summary="" cellpadding="3" cellspacing="0" border="0">
		<tr>
			<td><html:hidden property="operation" value="<%=operation%>"/></td>
			<td><html:hidden property="counter"/></td>
			<td><html:hidden property="systemIdentifier" /></td>
			<td><html:hidden property="idChange"/></td>
		</tr>
		
		<tr>
			 <td class="formMessage" colspan="3">* indicates a required field</td>
		</tr>

		<tr>
			 <td class="formTitle" height="20" colspan="3">
				<logic:equal name="operation" value="<%=Constants.ADD%>">
					<bean:message key="distribution.addTitle"/>
				</logic:equal>
				<logic:equal name="operation" value="<%=Constants.EDIT%>">
					<bean:message key="distribution.editTitle"/>
				</logic:equal>
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
					<bean:message key="eventparameters.distributed.by"/> 
				</label>	
			</td>	
			<td class="formField">
				<html:select property="userId" styleClass="formFieldSized" styleId="userId" size="1" >
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
				<html:text styleClass="formDateSized" size="35" styleId="dateOfEvent" property="dateOfEvent" />
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
					<bean:message key="eventparameters.time"/>&nbsp; 
					
				</label>
			</td>
			<td class="formField">
				<html:select property="timeInHours" styleClass="formFieldSized5" styleId="timeInHours" size="1">
					<html:options name="<%=Constants.HOURLIST%>" labelName="<%=Constants.HOURLIST%>" />
				</html:select>&nbsp;<bean:message key="eventparameters.timeinhours"/>&nbsp;
				<html:select property="timeInMinutes" styleClass="formFieldSized5" styleId="timeInMinutes" size="1">
					<html:options name="<%=Constants.MINUTESLIST%>" labelName="<%=Constants.MINUTESLIST%>" />
				</html:select>&nbsp;<bean:message key="eventparameters.timeinminutes"/>
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
	<table cellpadding="3" cellspacing="0" border="0">
		<tr rowspan = 4>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>									
		</tr>
	</table>
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="433">
<!--  Distributed Item begin here -->
				 <tr>
				     <td class="formTitle" height="20" colspan="5">
				     	<bean:message key="distribution.distributedItem"/>
				     </td>
				     <td class="formButtonField">
						<html:button property="addKeyValue" styleClass="actionButton" onclick="insRow('addMore')">
						<bean:message key="buttons.addMore"/>
						</html:button>
				    </td>
				    <td class="formButtonField" align="Right">
						<html:button property="deleteValue" styleClass="actionButton" onclick="deleteChecked('addMore','/catissuecore/Distribution.do?operation=<%=operation%>&pageOf=pageOfDistribution&status=true',document.forms[0].counter,'chk_')">
							<bean:message key="buttons.delete"/>
						</html:button>
					</td>
				    
				 </tr>
				 <tr>
				 	<td class="formSerialNumberLabel" width="5">
				     	#
				    </td>
					<td class="formRightSubTableTitle">
						<bean:message key="itemrecord.specimenId"/>
					</td>
					<td class="formLeftSubTableTitle">
						<bean:message key="specimen.tissueSite"/>
					</td>
					<td class="formRightSubTableTitle">
						<bean:message key="specimen.tissueSide"/>
					</td>
				    <td class="formRightSubTableTitle">
						<bean:message key="specimen.pathologicalStatus"/>
					</td>
					<td class="formRightSubTableTitle">
						<bean:message key="itemrecord.quantity"/>
					</td>
					<td class="formRightSubTableTitle">
						<label for="delete" align="center">
							<bean:message key="addMore.delete" />
						</label>
					</td>
				 </tr>
				 
				 <tbody id="addMore">
				<%
				for(int i=1;i<=noOfRows;i++)
				{
					String dIdentifier = "value(DistributedItem:"+i+"_systemIdentifier)";
					String itemName = "value(DistributedItem:"+i+"_Specimen_systemIdentifier)";
					String quantity = "value(DistributedItem:"+i+"_quantity)";
					String tissueSite = "value(DistributedItem:"+i+"_tissueSite)";
					String tissueSide = "value(DistributedItem:"+i+"_tissueSide)";
					String pathologicalStatus = "value(DistributedItem:"+i+"_pathologicalStatus)";					
					String unitSpan = "value(DistributedItem:"+i+"_unitSpan)";
					String className = "value(DistributedItem:"+i+"_Specimen_className)";
					String key = "DistributedItem:" + i + "_Specimen_className";
					//String unitKey = "DistributedItem:" + i + "_unit";
					//String unitProperty = "value(DistributedItem:"+i+"_unit)";
					//String fName = "onSpecimenTypeChange(this,'" + unitSpan + "','" + itemName + "','" + unitProperty + "')";
					String srKeyName = "DistributedItem:"+i+"_Specimen_systemIdentifier";
					//String idValue=(String)formBean.getValue(srKeyName);
					//String strUnitValue = ""+(String)formBean.getValue(unitProperty);
					String classValue = (String)formBean.getValue(key);
					String strUnitValue = changeUnit(classValue);
					String check = "chk_" + i;
				%>
				 <tr>
				 	<td class="formSerialNumberField" width="5"><%=i%>
					
				 	
				 	<html:hidden property="<%=dIdentifier%>" />	
				 	
					<input type="hidden" property="<%=className%>" id="<%=className%>" />
				 	</td>
				 	<td class="formField">
						<html:select property="<%=itemName%>" styleClass="formField" styleId="<%=itemName%>" size="1" onchange="onSpecimenIdChange(this)">
							<html:options collection="<%=Constants.SPECIMEN_ID_LIST%>" labelProperty="name" property="value"/>
						</html:select>
					</td>
				    <td class="formField">
				     	<html:text styleClass="formField"  styleId="<%=tissueSite%>" property="<%=tissueSite%>" readonly="true"/>
				    </td>
   				    <td class="formField">
				     	<html:text styleClass="formField" styleId="<%=tissueSide%>" property="<%=tissueSide%>" readonly="true"/>
				    </td>
   				    <td class="formField">
				     	<html:text styleClass="formField" styleId="<%=pathologicalStatus%>" property="<%=pathologicalStatus%>" readonly="true"/>
				    </td>
				    <td class="formField">
				     	<html:text styleClass="formFieldSized5" size="30" styleId="<%=quantity%>" property="<%=quantity%>" disabled="<%=readOnlyForAll%>" readonly="<%=readOnlyForAll%>"/>
				     	<span id="<%=unitSpan%>">&nbsp;<%=strUnitValue%></span>
				    </td>
				    <%
							String keyid = "DistributedItem:"+i+"_systemIdentifier";
							boolean bool = Utility.isPersistedValue(map,keyid);
							String condition = "";
							if(bool)
								condition = "disabled='disabled'";

						%>
						<td class="formField" width="5">
							<input type=checkbox name="<%=check %>" id="<%=check %>" <%=condition%>>		
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
				<%
					if (operation.equals(Constants.EDIT))
        			{
        		%>
					<td>
						<html:submit styleClass="actionButton" value="Report"/>
					</td>
			    <%
        			}
        		%>
					<td>
						<html:submit styleClass="actionButton" value="Submit" onclick="<%=changeAction%>" />
					</td>
					<td><html:reset styleClass="actionButton"/></td> 
				</tr>
			</table>
			<!-- action buttons end -->
			</td>
		</tr>
		
		</table>
		
	  </td>
	 </tr>

	 <!-- NEW Distribution ends-->
	 
	 </table>
</html:form>			
