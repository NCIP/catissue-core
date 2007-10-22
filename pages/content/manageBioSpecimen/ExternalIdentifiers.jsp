<!-- Patch_Id: Improve_Space_Usability_On_Specimen_Page_4
	Description: import statement required for Constants.PAGEOF  -->
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<head>
<%
	// Patch_Id: Improve_Space_Usability_On_Specimen_Page_4
	// variable to get the current display style of collect external identifier table
	String eiDispType1=request.getParameter("eiDispType");
%>
<script language="JavaScript">
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
		var identifier = "externalIdentifierValue(ExternalIdentifier:" + (q+1) +"_id)";
		var hiddenTag = "<input type='hidden' name='" + identifier + "' value='' id='" + identifier + "'>";
		spreqno.innerHTML="" + rowno + "." + hiddenTag;
	
		//Second Cell
		var spreqtype=x.insertCell(1);
		spreqtype.className="formField";
		spreqtype.colSpan=1;
		sname="";
		
		var name = "externalIdentifierValue(ExternalIdentifier:" + rowno +"_name)";
		sname="<input type='text' name='" + name + "'  maxlength='50' class='formFieldSized15' id='" + name + "'>";      
	
	
		spreqtype.innerHTML="" + sname;
	
		//Third Cell
		var spreqsubtype=x.insertCell(2);
		spreqsubtype.className="formField";
		spreqsubtype.colSpan=2;
		sname="";
		
		name = "externalIdentifierValue(ExternalIdentifier:" + rowno +"_value)";
		sname= "";
			
		sname="<input type='text' name='" + name + "' maxlength='50'  class='formFieldSized15' id='" + name + "'>"   
		
		spreqsubtype.innerHTML="" + sname;
			
		//Fourth Cell
		var checkb=x.insertCell(3);
		checkb.className="formField";
		checkb.colSpan=2;
		sname="";
		var name = "chk_ex_"+ rowno;
		sname="<input type='checkbox' name='" + name +"' id='" + name +"' value='C' onClick=\"enableButton(document.forms[0].deleteExId,document.forms[0].exIdCounter,'chk_ex_')\">";
		checkb.innerHTML=""+sname;
	}
		
		
	</script>
</head>

<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" id="externalIdentifiersTable">
		<tr>
		<!--			/**
						* Name : Vijay_Pande
						* Reviewer : Santosh_Chandak
						* Bug ID: Improve_Space_Usability_On_Specimen_Page
						* Patch ID: Improve_Space_Usability_On_Specimen_Page_4
						* See also: 1-5
						* Description: To improve space usability on specimen page, the table which are rarely used are kept invisible by default. 
 						* Following image is provided to toggle between the show and hide the table (External identifiers table).
						*/   -->		
			<td class="formFieldAllBorders" align="right" width="1%">
				<a id="imageEI" style="text-decoration:none" href="javascript:switchStyle('imageEI','eiDispType','externalIdentifiers','addExId');">  
				<img src="images/nolines_minus.gif" border="0" width="18" height="18"/>
				</a>
				<input type="hidden" name="eiDispType" value="<%=eiDispType1%>" id="eiDispType" />
			</td> 
			<!-- Patch ends here -->
		     <td class="formTitle" width="200" height="20" colspan="2">
		     	<bean:message key="specimen.externalIdentifier"/>
		     </td>
		     <td class="formButtonField" width="300" colspan="2">
		     	<html:button property="addExId" styleClass="actionButton" styleId="addExId" onclick="insExIdRow('addExternalIdentifier')">
		     		<bean:message key="buttons.addMore"/>
		     	</html:button>
		    </td>
		     <!-- 	Patch_Id: Improve_Space_Usability_On_Specimen_Page_4
					Description: variable to set the function name and its argument pageOf to set for the delete button  -->
			<%
			String delExtIds="deleteExternalIdentifiers('pageOfMultipleSpecimen')";
			if((String)request.getAttribute(Constants.PAGEOF)!=null)
			{
				delExtIds="deleteExternalIdentifiers('"+(String)request.getAttribute(Constants.PAGEOF)+"');";
			}
			%>
		    <td class="formTitle" align="Right">
					<html:button property="deleteExId" styleClass="actionButton" onclick='<%=delExtIds%>' disabled="true">
						<bean:message key="buttons.delete"/>
					</html:button>
			</td>
			<!-- Patch ends here -->
		 </tr>
		 <tr>
			 <td colspan="6" width="100%">
			 <!-- Patch_Id: Improve_Space_Usability_On_Specimen_Page_4
				 Decription: Table is seperated from its title to collapse/expand    -->
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%" id="externalIdentifiers">
			<!-- Patch ends here -->
		 	<tr>
			 	<td class="formSerialNumberLabel" width="5">
			     	#
			    </td>
			    <td class="formLeftSubTableTitle" width="350">
					<bean:message key="externalIdentifier.name"/>
				</td>
			    <td class="formRightSubTableTitle" colspan="2" width="350">
					<bean:message key="externalIdentifier.value"/>
				</td>
				<td class="formRightSubTableTitle" width="50">
					<label for="delete" align="center">
						<bean:message key="addMore.delete" />
					</label>
				</td>
			 </tr>
		 	 <tbody id="addExternalIdentifier">
		 	 <html:hidden property="exIdCounter"/>
			  <%
			  	for(int i=exIdRows;i>=1;i--)
			  	{
					String exName = "externalIdentifierValue(ExternalIdentifier:" + i + "_name)";
					String exValue = "externalIdentifierValue(ExternalIdentifier:" + i + "_value)";
					String exIdentifier = "externalIdentifierValue(ExternalIdentifier:" + i +"_id)";
					String check = "chk_ex_"+i;
			  %>
				<tr>
				 	<td class="formSerialNumberField" width="5"><%=i%>.
					 			<html:hidden property="<%=exIdentifier%>" />
			 		</td>
				    <td class="formField" width="365" >
			     		<html:text styleClass="formFieldSized15" maxlength="255"  styleId="<%=exName%>" property="<%=exName%>" />
			    	</td>
			    	<td class="formField" colspan="2">
			     		<html:text styleClass="formFieldSized15" maxlength="255"  styleId="<%=exValue%>" property="<%=exValue%>" />
			    	</td>
			    	<%
						String exKey = "ExternalIdentifier:" + i +"_id";
						boolean exBool = Utility.isPersistedValue(map,exKey);
						String exCondition = "";
						if(exBool)
							exCondition = "disabled='disabled'";
						%>
					<td class="formField" width="5">
						<input type=checkbox name="<%=check %>" id="<%=check %>" <%=exCondition%> onClick="enableButton(document.forms[0].deleteExId,document.forms[0].exIdCounter,'chk_ex_')">		
					</td>
				 </tr>
			  <% } %>
		 </tbody>
		 </table>
	 	</td>
	 </tr>
</table>