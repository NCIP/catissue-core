<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="java.util.List,java.util.ListIterator"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.NewSpecimenForm"%>

<%
String bhIdArray [] = (String []) request.getAttribute(Constants.BIOHAZARD_ID_LIST);
String bhNameArray [] = (String []) request.getAttribute(Constants.BIOHAZARD_NAME_LIST);
String bhTypeArray [] = (String []) request.getAttribute(Constants.BIOHAZARD_TYPES_LIST);
%>
<head>
	<script language="JavaScript">
	
		var win = null;
		function NewWindow(mypage,myname,w,h,scroll)
		{
			LeftPosition = (screen.width) ? (screen.width-w)/2 : 0;
			TopPosition = (screen.height) ? (screen.height-h)/2 : 0;

			mypage=mypage+document.forms[0].startNumber.value;
			settings =
				'height='+h+',width='+w+',top='+TopPosition+',left='+LeftPosition+',scrollbars='+scroll+',resizable'
			win = open(mypage,myname,settings)
			if (win.opener == null)
				win.opener = self;
		}
	
		var idArray = new Array();
		var nameArray = new Array();
		var typeArray = new Array();
		
		<%
			if(bhIdArray != null && bhTypeArray != null && bhNameArray !=null)
			{
				for(int i=0;i<bhIdArray.length;i++)
				{
		%>
					idArray[<%=i%>] = "<%=bhIdArray[i]%>";
					nameArray[<%=i%>] = "<%=bhNameArray[i]%>";
					typeArray[<%=i%>] = "<%=bhTypeArray[i]%>";
		<%
				}
			}
		%>
		
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
		
		
		//ADD MORE FUNCTIONALITY FOR BIOHAZARDS
		function insBhRow(subdivtag)
		{
			var val = parseInt(document.forms[0].bhCounter.value);
			val = val + 1;
			document.forms[0].bhCounter.value = val;
			
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

			var name = "biohazardValue(Biohazard:" + (q+1) + "_type)";
			sname="<select name='" + name + "' size='1' class='formFieldSized15' id='" + name + "' onchange=onBiohazardTypeSelected(this)>";
			<%
					for(int i=0;i<Constants.BIOHAZARD_TYPE_ARRAY.length;i++)
					{
			%>
						sname = sname + "<option value='<%=Constants.BIOHAZARD_TYPE_ARRAY[i]%>'><%=Constants.BIOHAZARD_TYPE_ARRAY[i]%></option>";
			<%		}
			%>
			sname = sname + "</select>";
			spreqtype.innerHTML="" + sname;
		
			//Third Cellvalue()
			var spreqsubtype=x.insertCell(2);
			spreqsubtype.className="formField";
			sname="";

			name = "biohazardValue(Biohazard:" + (q+1) + "_systemIdentifier)";
			sname= "";
			sname="<select name='" + name + "' size='1' class='formFieldSized15' id='bhId" + (q+1) + "'>";
			sname = sname + "<option value='-1'><%=Constants.SELECT_OPTION%></option>";			
			sname = sname + "</select>";
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
			formName = Constants.SPECIMEN_EDIT_ACTION;
			readOnlyValue=true;
		}
		else
		{
			formName = Constants.SPECIMEN_ADD_ACTION;
			readOnlyValue=false;
		}

		if (operation.equals(Constants.VIEW))
		{
			readOnlyForAll=true;
		}

		String pageOf = (String)request.getAttribute(Constants.PAGEOF);

		Object obj = request.getAttribute("newSpecimenForm");
		int exIdRows=1;
		int bhRows=1;

		String unitSpecimen = "";
		if(obj != null && obj instanceof NewSpecimenForm)
		{
			NewSpecimenForm form = (NewSpecimenForm)obj;
			exIdRows = form.getExIdCounter();
			bhRows	 = form.getBhCounter();
			if(form.getUnit() != null)
				unitSpecimen = form.getUnit();
		}
%>

	<html:errors />

		<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
			
		   <html:form action="<%=Constants.SPECIMEN_ADD_ACTION%>">
		   
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
								<bean:message key="user.identifier"/>
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
						<td><html:hidden property="bhCounter"/></td>
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
				     <%String title = "specimen."+pageView+".title";%>
				     	<bean:message key="<%=title%>"/>
				     </td>
				 </tr>
				 <tr>
			     	<td class="formRequiredNotice" width="5">*</td>
				    <td class="formRequiredLabel">
						<label for="specimenCollectionGroupId">
							<bean:message key="specimen.specimenCollectionGroupId"/>
						</label>
					</td>
					<td class="formField" colspan="4">
			     		<html:select property="specimenCollectionGroupId" styleClass="formFieldSized15" styleId="specimenCollectionGroupId" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="specimenCollectionGroupIdList" labelName="specimenCollectionGroupIdList"/>		
						</html:select>
		        		<a href="SpecimenCollectionGroup.do?operation=add">
      						<bean:message key="app.addNew" />
   						</a>
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
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel">
						<label for="tissueSite">
							<bean:message key="specimen.tissueSite"/>
						</label>
					</td>
				     <td class="formField" colspan="4">
				     	<html:select property="tissueSite" styleClass="formFieldSized15" styleId="tissueSite" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="tissueSiteList" labelName="tissueSiteList"/>		
						</html:select>
						<a href="#"><img src="images\Tree.gif" border="0" width="24" height="18"></a>
		        	  </td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel">
						<label for="tissueSide">
							<bean:message key="specimen.tissueSide"/>
						</label>
					</td>
				     <td class="formField" colspan="4">
				     	<html:select property="tissueSide" styleClass="formFieldSized15" styleId="tissueSide" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="tissueSideList" labelName="tissueSideList"/>		
						</html:select>
		        	  </td>
				 </tr>
				 <tr>
				    <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				    </td>
				    <td class="formRequiredLabel">
						<label for="pathologicalStatus">
							<bean:message key="specimen.pathologicalStatus"/>
						</label>
					</td>
				    <td class="formField" colspan="4">
				     	<html:select property="pathologicalStatus" styleClass="formFieldSized15" styleId="pathologicalStatus" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="pathologicalStatusList" labelName="pathologicalStatusList"/>		
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
				     	<html:button property="mapButton" styleClass="actionButton" onclick="NewWindow('ShowFramedPage.do?pageOf=pageOfSpecimen&amp;storageType=','name','810','320','yes');return false">
							<bean:message key="buttons.showMap"/>
						</html:button>
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
				 
				 <tr>
				     <td class="formTitle" height="20" colspan="2">
				     	<bean:message key="specimen.biohazards"/>
				     </td>
				     <td class="formButtonField" colspan="2">
				     	<html:button property="addBiohazard" styleClass="actionButton" onclick="insBhRow('addBiohazardRow')">
				     		<bean:message key="buttons.addMore"/>
				     	</html:button>
				    </td>
				  </tr>
				  
				  <tr>
				 	<td class="formSerialNumberLabel" width="5">
				     	#
				    </td>
				    <td class="formLeftSubTableTitle">
						<bean:message key="biohazards.type"/>
					</td>
				    <td class="formRightSubTableTitle" colspan="2">
						<bean:message key="biohazards.name"/>
					</td>
				 </tr>
				 
				 <tbody id="addBiohazardRow">
				 	<%
				  	for(int i=1;i<=bhRows;i++)
				  	{
						String bhType = "biohazardValue(Biohazard:" + i + "_type)";
						String bhId	  = "biohazardValue(Biohazard:" + i + "_systemIdentifier)";
				  %>
					<tr>
					 	<td class="formSerialNumberField" width="5"><%=i%>.</td>
					    <td class="formField">
				     		<html:select property="<%=bhType%>" styleClass="formFieldSized15" styleId="<%=bhType%>" size="1" onchange="onBiohazardTypeSelected(this)">
								<html:options name="<%=Constants.BIOHAZARD_TYPE_LIST%>" labelName="<%=Constants.BIOHAZARD_TYPE_LIST%>" />
							</html:select>
				    	</td>
				    	<td class="formField">
				     		<html:select property="<%=bhId%>" styleClass="formFieldSized15" styleId="<%="bhId" + i%>" size="1">
								<html:option value="-1"><%=Constants.SELECT_OPTION%></html:option>
								<%
									String type = (String)request.getParameter(bhType);
									for(int x=0;x<bhIdArray.length;x++)
									{
										if(type!=null && type.equals(bhTypeArray[x]))
										{											
								%>
											<html:option value="<%=bhIdArray[x]%>"><%=bhNameArray[x]%></html:option>
								<%
										}
									}
								%>
							</html:select>
				    	</td>
					 </tr>
				  <% } %>
				 </tbody>
				 <!-- Bio-hazards End here -->	
				 
			 </table>		
		
			   

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