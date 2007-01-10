<head>
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

<table summary="" cellpadding="3" cellspacing="0" border="0" id="externalIdentifiers" width="100%">
		<tr>
		     <td class="formTitle" height="20" colspan="2">
		     	<bean:message key="specimen.externalIdentifier"/>
		     </td>
		     <td class="formButtonField" colspan="2">
		     	<html:button property="addExId" styleClass="actionButton" onclick="insExIdRow('addExternalIdentifier')">
		     		<bean:message key="buttons.addMore"/>
		     	</html:button>
		    </td>
		    <td class="formTitle" align="Right">

					<html:button property="deleteExId" styleClass="actionButton" onclick="deleteExternalIdentifiers();" disabled="true">
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
		    <td class="formRightSubTableTitle" colspan="2">
				<bean:message key="externalIdentifier.value"/>
			</td>
			<td class="formRightSubTableTitle">
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
			    <td class="formField">
		     		<html:text styleClass="formFieldSized15" maxlength="255"  styleId="<%=exName%>" property="<%=exName%>" readonly="<%=readOnlyForAll%>"/>
		    	</td>
		    	<td class="formField" colspan="2">
		     		<html:text styleClass="formFieldSized15" maxlength="255"  styleId="<%=exValue%>" property="<%=exValue%>" readonly="<%=readOnlyForAll%>"/>
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