<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="java.util.List,java.util.ListIterator"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.CreateSpecimenForm"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="java.util.*"%>


<%@ include file="/pages/content/common/SpecimenCommonScripts.jsp" %>
<head>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
	<script language="JavaScript">
		var win = null;
		function NewWindow(mypage,myname,w,h,scroll)
		{
			LeftPosition = (screen.width) ? (screen.width-w)/2 : 0;
			TopPosition = (screen.height) ? (screen.height-h)/2 : 0;

			settings =
				'height='+h+',width='+w+',top='+TopPosition+',left='+LeftPosition+',scrollbars='+scroll+',resizable'
			win = open(mypage,myname,settings)
			if (win.opener == null)
				win.opener = self;
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
			var x=document.getElementById(subdivtag).insertRow(0);
		
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
			sname="<input type='text' name='" + name + "' maxlength='50'  class='formFieldSized10' id='" + name + "'>";      
		
		
			spreqtype.innerHTML="" + sname;
		
			//Third Cell
			var spreqsubtype=x.insertCell(2);
			spreqsubtype.className="formField";
			spreqsubtype.colSpan=1;
			sname="";
		
			name = "externalIdentifierValue(ExternalIdentifier:" + rowno +"_value)";
			sname= "";
			
			sname="<input type='text' name='" + name + "' maxlength='50'  class='formFieldSized10' id='" + name + "'>"   
		
			spreqsubtype.innerHTML="" + sname;
			
			//Fourth Cell
			var checkb=x.insertCell(3);
			checkb.className="formField";
			checkb.colSpan=2;
			sname="";
			var name = "chk_"+ rowno;
			sname="<input type='checkbox' name='" + name +"' id='" + name +"' value='C'>"
			checkb.innerHTML=""+sname;
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
		
		CreateSpecimenForm form = null;
		String unitSpecimen = "";
		Map map = null;
		if(obj != null && obj instanceof CreateSpecimenForm)
		{
			form = (CreateSpecimenForm)obj;
			map = form.getExternalIdentifier();
			exIdRows = form.getExIdCounter();
			if(form.getUnit() != null)
				unitSpecimen = form.getUnit();
		}
		System.out.println("SPID :--- : " +(String)request.getAttribute("parentSpecimenId"));
%>

	<html:errors />
   <html:form action="<%=Constants.CREATE_SPECIMEN_ADD_ACTION%>">
		<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="500">
		   
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
			
			  <!-- NEW SPECIMEN REGISTRATION BEGINS-->
	    	  <tr>
			    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0" width="500">
				 <tr>
					<td>
						<html:hidden property="<%=Constants.OPERATION%>" value="<%=operation%>"/>
						<td><html:hidden property="exIdCounter"/></td>
					</td>
				 </tr>
				 <tr>
					<td>
						<html:hidden property="positionInStorageContainer" />
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
				    <td class="formRequiredLabel" width="175">
						<label for="specimenCollectionGroupId">
							<bean:message key="createSpecimen.parent"/>
						</label>
					</td>
					<td class="formField" colspan="2">
			     		<html:select property="parentSpecimenId" styleClass="formFieldSized10" styleId="parentSpecimenId" size="1" disabled="<%=readOnlyForAll%>">
							<html:options collection="<%=Constants.PARENT_SPECIMEN_ID_LIST%>" labelProperty="name" property="value"/>
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
				    <td class="formField" colspan="2">
				     	<html:select property="className" styleClass="formFieldSized15" styleId="className" size="1" disabled="<%=readOnlyForAll%>" onchange="onTypeChange(this)">
							<html:options collection="<%=Constants.SPECIMEN_CLASS_LIST%>" labelProperty="name" property="value"/>
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
				    <td class="formField" colspan="2">
				    <!-- --------------------------------------- -->
				    <%
								String classValue = (String)form.getClassName();
								specimenTypeList = (List)specimenTypeMap.get(classValue);
								
								boolean subListEnabled = false;
						
								if(classValue != null && classValue.equals("Cell"))
									subListEnabled = true;
								
								
								if(specimenTypeList == null)
								{
									specimenTypeList = new ArrayList();
									specimenTypeList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
								}
								pageContext.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
								String subTypeFunctionName ="onSubTypeChangeUnit('className',this,'unitSpan')"; 
					%>
				    <!-- --------------------------------------- -->
				     	<html:select property="type" styleClass="formFieldSized15" styleId="type" 
				     	 size="1" disabled="<%=subListEnabled%>"
				     	 onchange="<%=subTypeFunctionName%>" >
							<html:options collection="<%=Constants.SPECIMEN_TYPE_LIST%>" labelProperty="name" property="value"/>
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
				    		<td class="formField" colspan="2">
				     			<html:text styleClass="formFieldSized15" size="30" styleId="concentration" property="concentration" readonly="<%=readOnlyForAll%>" disabled="false"/>
				   			</td>
				    <%
						}
						else
						{
					%>
							<td class="formField" colspan="2">
				     			<html:text styleClass="formFieldSized15" size="30" maxlength="10"  styleId="concentration" property="concentration" readonly="<%=readOnlyForAll%>" disabled="true"/>
				     			&nbsp;<bean:message key="specimen.concentrationUnit"/>
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
				    <td class="formField" colspan="2">
				     	<html:text styleClass="formFieldSized15" size="30" maxlength="10"  styleId="quantity" property="quantity" readonly="<%=readOnlyForAll%>"/>
				     	<span id="unitSpan"><%=unitSpecimen%></span>
				     	<html:hidden property="unit"/>
				    </td>
				 </tr>

				<tr>
				 	<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel">
					   <label for="className">
					   		<bean:message key="specimen.positionInStorageContainer"/>
					   </label>
					</td>
				 	<td class="formField">
		     			<html:text styleClass="formFieldSized3" maxlength="10"  styleId="storageContainer" property="storageContainer" />
		     			-
		     			<html:text styleClass="formFieldSized3"  maxlength="10" styleId="positionDimensionOne" property="positionDimensionOne" />
		     			-
		     			<html:text styleClass="formFieldSized3" maxlength="10"  styleId="positionDimensionTwo" property="positionDimensionTwo" />
					</td>
					<td class="formField">
						<html:button property="mapButton" styleClass="actionButton" styleId="Map"
							onclick="javascript:NewWindow('ShowFramedPage.do?pageOf=pageOfSpecimen','name','810','320','yes');return false" >
							<bean:message key="buttons.map"/>
						</html:button>
					</td>
				 </tr>
	
				 <tr>
			     	<td class="formRequiredNotice" width="5">&nbsp;</td>
				    <td class="formLabel">
						<label for="barcode">
							<bean:message key="specimen.barcode"/>
						</label>
					</td>
				    <td class="formField" colspan="2">
						<html:text styleClass="formFieldSized"  maxlength="50" size="30" styleId="barcode" property="barcode" readonly="<%=readOnlyForAll%>" />
		        	</td>
				 </tr>
				 
				 <tr>
			     	<td class="formRequiredNotice" width="5">&nbsp;</td>
				    <td class="formLabel">
						<label for="comments">
							<bean:message key="specimen.comments"/>
						</label>
					</td>
				    <td class="formField" colspan="2">
				    	<html:textarea styleClass="formFieldSized" rows="3" styleId="comments" property="comments" readonly="<%=readOnlyForAll%>"/>
				    </td>
				 </tr>
				 
				 <tr>
				     <td class="formTitle" height="20" colspan="2">
				     	<bean:message key="specimen.externalIdentifier"/>
				     </td>
				     <td class="formButtonField">
				     	<html:button property="addExId" styleClass="actionButton" onclick="insExIdRow('addExternalIdentifier')">
				     		<bean:message key="buttons.addMore"/>
				     	</html:button>
				    </td>
				    <td class="formTitle" align="Right">
							<html:button property="deleteValue" styleClass="actionButton" onclick="deleteChecked('addExternalIdentifier','CreateSpecimen.do?operation=<%=operation%>&pageOf=&status=true',document.forms[0].exIdCounter,'chk_',false)">
								<bean:message key="buttons.delete"/>
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
					    <td class="formRightSubTableTitle">
							<bean:message key="externalIdentifier.value"/>
						</td>
						<td class="formRightSubTableTitle">
							<label for="delete" align="center">
								<bean:message key="addMore.delete" />
							</label>
						</td>
					 </tr>
				  <tbody id="addExternalIdentifier">
				  <%
				  	for(int i=exIdRows;i>=1;i--)
				  	{
						String exName = "externalIdentifierValue(ExternalIdentifier:" + i + "_name)";
						String exValue = "externalIdentifierValue(ExternalIdentifier:" + i + "_value)";
						String check = "chk_"+i;
				  %>
					<tr>
					 	<td class="formSerialNumberField" width="5"><%=i%>.</td>
					    <td class="formField">
				     		<html:text styleClass="formFieldSized10"  maxlength="50" styleId="<%=exName%>" property="<%=exName%>" readonly="<%=readOnlyForAll%>"/>
				    	</td>
				    	<td class="formField">
				     		<html:text styleClass="formFieldSized10"  maxlength="50" styleId="<%=exValue%>" property="<%=exValue%>" readonly="<%=readOnlyForAll%>"/>
				    	</td>
				    	<%
							String key = "";
							boolean bool = Utility.isPersistedValue(map,key);
							String condition = "";
							if(bool)
								condition = "disabled='disabled'";

						%>
						<td class="formField" width="5">
							<input type=checkbox name="<%=check %>" id="<%=check %>" <%=condition%>>		
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
									<td colspan="3">
										<html:reset styleClass="actionButton">
											<bean:message key="buttons.reset"/>
										</html:reset>
									</td> 
									<%--td>
										<html:reset styleClass="actionButton">
											<bean:message key="buttons.moreSpecimen"/>
										</html:reset>
									</td--%>
								</tr>
							</table>
							<!-- action buttons end -->
				  		</td>
				 	</tr>
				 </logic:notEqual>
				 
				</logic:notEqual>				
			 
			 <!-- NEW SPECIMEN REGISTRATION ends-->
	</table>
 </html:form>
	