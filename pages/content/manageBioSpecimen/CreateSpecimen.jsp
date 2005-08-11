<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="java.util.List,java.util.ListIterator"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.CreateSpecimenForm"%>

<head>
	<script language="JavaScript">
	
		function onTypeChange(element)
		{
			var unit = document.getElementById("unitSpan");
			var unitSpecimen = "";
			document.forms[0].concentration.disabled = true;
			
			if(element.value == "Tissue")
			{
				unitSpecimen = "<%=Constants.UNIT_GM%>";
				document.forms[0].unit.value = "<%=Constants.UNIT_GM%>";
			}
			else if(element.value == "Fluid")
			{
				unitSpecimen = "<%=Constants.UNIT_ML%>";
				document.forms[0].unit.value = "<%=Constants.UNIT_ML%>";
			}
			else if(element.value == "Cell")
			{
				unitSpecimen = "<%=Constants.UNIT_CC%>";
				document.forms[0].unit.value = "<%=Constants.UNIT_CC%>";
			}
			else if(element.value == "Molecular")
			{
				unitSpecimen = "<%=Constants.UNIT_MG%>";
				document.forms[0].unit.value = "<%=Constants.UNIT_MG%>";
				document.forms[0].concentration.disabled = false;
			}
			
			unit.innerHTML = unitSpecimen;
		}
		
		function onBiohazardTypeSelected(element)
		{ 
			var i = (element.name).indexOf("_");
			var comboNo = (element.name).substring(i-1,i);
			var comboToRefresh = "bhId" + comboNo;
			ele = document.getElementById(comboToRefresh);
			//To Clear the Combo Box
			ele.options.length = 0;
			
			ele.options[0] = new Option('-- Select --','-1');
			var j=1;
			//Populating the corresponding Combo Box
			for(i=0;i<idArray.length;i++)
			{
				if(typeArray[i] == element.value)
				{
					ele.options[j++] = new Option(nameArray[i],idArray[i]);
				}
			}
		}
	
		//ADD MORE -------- EXTERNAL IDENTIFIER
		function insExIdRow(subdivtag)
		{
			var val = parseInt(document.forms[0].exIdCounter.value);
			val = val + 1;
			document.forms[0].exIdCounter.value = val;
			
			var r = new Array(); 
			r = document.getElementById(subdivtag).rows;
			var q = r.length;
			var rowno = q + 1;
			var x=document.getElementById(subdivtag).insertRow(q);
		
			// First Cell
			var spreqno=x.insertCell(0);
			spreqno.className="formSerialNumberField";
			sname=(q+1);
			spreqno.innerHTML="" + rowno + ".";
		
			//Second Cell
			var spreqtype=x.insertCell(1);
			spreqtype.className="formField";
			spreqtype.colSpan=1;
			sname="";
			
			var name = "externalIdentifierValue(ExternalIdentifier:" + rowno +"_name)";
			sname="<input type='text' name='" + name + "' class='formFieldSized15' id='" + name + "'>";      
		
		
			spreqtype.innerHTML="" + sname;
		
			//Third Cell
			var spreqsubtype=x.insertCell(2);
			spreqsubtype.className="formField";
			sname="";
		
			name = "externalIdentifierValue(ExternalIdentifier:" + rowno +"_value)";
			sname= "";
			
			sname="<input type='text' name='" + name + "' class='formFieldSized15' id='" + name + "'>"   
		
			spreqsubtype.innerHTML="" + sname;
		}
		
	</script>
</head>

<% 
		String operation = (String)request.getAttribute(Constants.OPERATION);
		String formName,pageView=operation,editViewButton="buttons."+Constants.EDIT;
		boolean readOnlyValue=false,readOnlyForAll=false;

		if(operation.equals(Constants.EDIT))
		{
			editViewButton="buttons."+Constants.VIEW;
			formName = Constants.CREATE_SPECIMEN_EDIT_ACTION;
			readOnlyValue=true;
		}
		else
		{
			formName = Constants.CREATE_SPECIMEN_ADD_ACTION;
			readOnlyValue=false;
		}

		if (operation.equals(Constants.VIEW))
		{
			readOnlyForAll=true;
		}

		String pageOf = (String)request.getAttribute(Constants.PAGEOF);

		Object obj = request.getAttribute("createSpecimenForm");
		int exIdRows=1;

		String unitSpecimen = "";
		if(obj != null && obj instanceof CreateSpecimenForm)
		{
			CreateSpecimenForm form = (CreateSpecimenForm)obj;
			exIdRows = form.getExIdCounter();
			if(form.getUnit() != null)
				unitSpecimen = form.getUnit();
		}
%>

	<html:errors />

		<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
			
		   <html:form action="<%=Constants.CREATE_SPECIMEN_ADD_ACTION%>">
		   
		   <logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
		   	<tr>
    		    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0">
		   			<tr>
				  	<td align="right" colspan="3">
					<%
						String changeAction = "setFormAction('MakeParticipantEditable.do?"+Constants.EDITABLE+"="+!readOnlyForAll+"')";
				 	%>
					<!-- action buttons begins -->
					<table cellpadding="4" cellspacing="0" border="0">
						<tr>
						   	<td>
						   		<html:submit styleClass="actionButton" onclick="<%=changeAction%>">
						   			<bean:message key="<%=editViewButton%>"/>
						   		</html:submit>
						   	</td>
							<td>
								<html:reset styleClass="actionButton">
									<bean:message key="buttons.export"/>
								</html:reset>
							</td>
						</tr>
					</table>
					<!-- action buttons end -->
				  </td>
				  </tr>
				</table>
			   </td>
			</tr>
			</logic:equal>		 
			
			<!-- If operation is equal to edit or search but,the page is for query the identifier field is not shown -->
			<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
				<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
			<!-- ENTER IDENTIFIER BEGINS-->	
			  <br/>	
  	    	  <tr>
    		    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0">
			 	 
				  <tr>
				     <td class="formTitle" height="20" colspan="3">
				     	<bean:message key="user.searchTitle"/>
				     </td>
				  </tr>
				  
				  <tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="identifier">
								<bean:message key="specimen.identifier"/>
							</label>
						</td>
					    <td class="formField">
					    	<html:text styleClass="formFieldSized15" size="30" styleId="identifier" property="identifier" readonly="<%=readOnlyForAll%>"/>
					    </td>
				  </tr>	

				 <%
					String changeAction = "setFormAction('"+Constants.PARTICIPANT_SEARCH_ACTION+"');setOperation('"+Constants.SEARCH+"');";
				 %>
 
				  <tr>
				   <td align="right" colspan="3">
					 <table cellpadding="4" cellspacing="0" border="0">
						 <tr>
						    <td>
						    	<html:submit styleClass="actionButton" value="Search" onclick="<%=changeAction%>"/>
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
			  </logic:notEqual>
			  
			   	
			  <!-- NEW SPECIMEN REGISTRATION BEGINS-->
	    	  <tr>
			    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0" width="600">
				 <tr>
					<td>
						<html:hidden property="<%=Constants.OPERATION%>" value="<%=operation%>"/>
						<td><html:hidden property="exIdCounter"/></td>
					</td>
				 </tr>
				 
				<logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
				 <tr>
					<td>
						<html:hidden property="sysmtemIdentifier"/>
					</td>
				 </tr>
				</logic:equal>

				<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.SEARCH%>">
					<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">
				 		<tr>
				     		<td class="formMessage" colspan="3">* indicates a required field</td>
				 		</tr>
				 	</logic:notEqual>
				 <tr>
				     <td class="formTitle" height="20" colspan="6">
				     <%String title = "createSpecimen."+pageView+".title";%>
				     	<bean:message key="<%=title%>"/>
				     </td>
				 </tr>
				 <tr>
			     	<td class="formRequiredNotice" width="5">*</td>
				    <td class="formRequiredLabel">
						<label for="specimenCollectionGroupId">
							<bean:message key="createSpecimen.parent"/>
						</label>
					</td>
					<td class="formField" colspan="4">
			     		<html:select property="parentSpecimenId" styleClass="formFieldSized10" styleId="parentSpecimenId" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="<%=Constants.PARENT_SPECIMEN_ID_LIST%>" labelName="<%=Constants.PARENT_SPECIMEN_ID_LIST%>"/>
						</html:select>
		        	</td>
				 </tr>
				 
				 <tr>
				 	<td class="formRequiredNotice" width="5">*</td>
				    <td class="formRequiredLabel">
				     	<label for="className">
				     		<bean:message key="specimen.type"/>
				     	</label>
				    </td>
				    <td class="formField" colspan="4">
				     	<html:select property="className" styleClass="formFieldSized15" styleId="className" size="1" disabled="<%=readOnlyForAll%>" onchange="onTypeChange(this)">
							<html:options name="specimenTypeList" labelName="specimenTypeList"/>		
						</html:select>
		        	</td>
				 </tr>
				 
				 <tr>
				    <td class="formRequiredNotice" width="5">*</td>
				    <td class="formRequiredLabel">
				     	<label for="type">
				     		<bean:message key="specimen.subType"/>
				     	</label>
				    </td>
				    <td class="formField" colspan="4">
				     	<html:select property="type" styleClass="formFieldSized15" styleId="type" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="specimenSubTypeList" labelName="specimenSubTypeList"/>		
						</html:select>
		        	</td>
				 </tr>
				 
				 <tr>
			     	<td class="formRequiredNotice" width="5">
				     	&nbsp;
				    </td>
				    <td class="formLabel">
						<label for="concentration">
							<bean:message key="specimen.concentration"/>
						</label>
					</td>
				    <%
						if(unitSpecimen.equals(Constants.UNIT_MG))
						{
					%>
				    		<td class="formField" colspan="4">
				     			<html:text styleClass="formFieldSized15" size="30" styleId="concentration" property="concentration" readonly="<%=readOnlyForAll%>" disabled="false"/>
				   			</td>
				    <%
						}
						else
						{
					%>
							<td class="formField" colspan="4">
				     			<html:text styleClass="formFieldSized15" size="30" styleId="concentration" property="concentration" readonly="<%=readOnlyForAll%>" disabled="true"/>
				    		</td>
					<%
						}
					%>
				 </tr>
				 <tr>
			     	<td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				    </td>
				    <td class="formRequiredLabel">
						<label for="quantity">
							<bean:message key="specimen.quantity"/>
						</label>
					</td>
				    <td class="formField" colspan="4">
				     	<html:text styleClass="formFieldSized15" size="30" styleId="quantity" property="quantity" readonly="<%=readOnlyForAll%>"/>
				     	<span id="unitSpan"><%=unitSpecimen%></span>
				     	<html:hidden property="unit"/>
				    </td>
				 </tr>
				 
				 <tr>
			     	<td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				    </td>
				    <td class="formRequiredLabel">
						<label for="comments">
							<bean:message key="specimen.comments"/>
						</label>
					</td>
				    <td class="formField" colspan="4">
				    	<html:textarea styleClass="formFieldSized15" rows="3" styleId="comments" property="comments" readonly="<%=readOnlyForAll%>"/>
				    </td>
				 </tr>
				 
				 <tr>
				     <td class="formTitle" height="20" colspan="2">
				     	<bean:message key="specimen.storage"/>
				     </td>
				     <td class="formButtonField" colspan="4">
				     	<html:submit styleClass="actionButton">
							<bean:message key="buttons.showMap"/>
						</html:submit>
				    </td>
				  </tr>
 				  <tr>
			     	<td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				    </td>
				    <td class="formRequiredLabel">
						<label for="storageContainer">
							<bean:message key="specimen.storageContainer"/>
						</label>
					</td>
				    <td class="formField" colspan="2">
				     	<html:text styleClass="formFieldSized15" size="30" styleId="storageContainer" property="storageContainer" readonly="<%=readOnlyForAll%>"/>
				    </td>
				 </tr>
				 <tr>
			     	<td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				    </td>
				    <td class="formRequiredLabel">
						<label for="positionDimensionOne">
							<bean:message key="specimen.positionDimensionOne"/>
						</label>
					</td>
				    <td class="formField" colspan="2">
				     	<html:text styleClass="formFieldSized15" size="30" styleId="positionDimensionOne" property="positionDimensionOne" readonly="<%=readOnlyForAll%>"/>
				    </td>
				 </tr>
			 
				 <tr>
			     	<td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				    </td>
				    <td class="formRequiredLabel">
						<label for="positionDimensionTwo">
							<bean:message key="specimen.positionDimensionTwo"/>
						</label>
					</td>
				    <td class="formField" colspan="2">
				     	<html:text styleClass="formFieldSized15" size="30" styleId="positionDimensionTwo" property="positionDimensionTwo" readonly="<%=readOnlyForAll%>"/>
				    </td>
				 </tr>
		 
				 <tr>
				     <td class="formTitle" height="20" colspan="2">
				     	<bean:message key="specimen.externalIdentifier"/>
				     </td>
				     <td class="formButtonField" colspan="2">
				     	<html:button property="addExId" styleClass="actionButton" onclick="insExIdRow('addExternalIdentifier')">
				     		<bean:message key="buttons.addMore"/>
				     	</html:button>
				    </td>
				 </tr>
				 
				 	<tr>
					 	<td class="formSerialNumberLabel" width="5">
					     	#
					    </td>
					    <td class="formLeftSubTableTitle">
							<bean:message key="externalIdentifier.name"/>
						</td>
					    <td class="formRightSubTableTitle" colspan="2">
							<bean:message key="externalIdentifier.value"/>
						</td>
					 </tr>
				  <tbody id="addExternalIdentifier">
				  <%
				  	for(int i=1;i<=exIdRows;i++)
				  	{
						String exName = "externalIdentifierValue(ExternalIdentifier:" + i + "_name)";
						String exValue = "externalIdentifierValue(ExternalIdentifier:" + i + "_value)";
				  %>
					<tr>
					 	<td class="formSerialNumberField" width="5"><%=i%>.</td>
					    <td class="formField">
				     		<html:text styleClass="formFieldSized15" styleId="<%=exName%>" property="<%=exName%>" readonly="<%=readOnlyForAll%>"/>
				    	</td>
				    	<td class="formField">
				     		<html:text styleClass="formFieldSized15" styleId="<%=exValue%>" property="<%=exValue%>" readonly="<%=readOnlyForAll%>"/>
				    	</td>
					 </tr>
				  <% } %>
				 </tbody>
				 </table>
			 <!-- Bio-hazards End here -->

 			   	 <logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">		
				 	<tr>
				  		<td align="right" colspan="4">
							<%
								String changeAction = "setFormAction('"+formName+"')";
				 			%>
							<!-- action buttons begins -->
							<table cellpadding="4" cellspacing="0" border="0">
								<tr>
						   			<td>
						   				<html:submit styleClass="actionButton" onclick="<%=changeAction%>">
						   					<bean:message key="buttons.submit"/>
						   				</html:submit>
						   			</td>
									<td>
										<html:reset styleClass="actionButton">
											<bean:message key="buttons.reset"/>
										</html:reset>
									</td> 
									<td>
										<html:reset styleClass="actionButton">
											<bean:message key="buttons.moreSpecimen"/>
										</html:reset>
									</td>
								</tr>
							</table>
							<!-- action buttons end -->
				  		</td>
				 	</tr>
				 </logic:notEqual>
				 
				</logic:notEqual>
				
			 
			 <!-- NEW SPECIMEN REGISTRATION ends-->
			 </html:form>
</table>