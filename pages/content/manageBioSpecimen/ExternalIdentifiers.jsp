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
		//var spreqno=x.insertCell(0);
		//spreqno.className="formSerialNumberField";
		sname=(q+1);
		var identifier = "externalIdentifierValue(ExternalIdentifier:" + (q+1) +"_id)";
		var hiddenTag = "<input type='hidden' name='" + identifier + "' value='' id='" + identifier + "'>";
		//spreqno.innerHTML="" + rowno + "." + hiddenTag;
	
		var checkb=x.insertCell(0);
		checkb.className="black_ar";
		//checkb.colSpan=2;
		sname="";
		var name = "chk_ex_"+ rowno;
		sname="<input type='checkbox' name='" + name +"' id='" + name +"' value='C' onClick=\"enableButton(document.forms[0].deleteExId,document.forms[0].exIdCounter,'chk_ex_')\">";
		checkb.innerHTML=""+sname + hiddenTag;

		//Second Cell
		var spreqtype=x.insertCell(1);
		spreqtype.className="black_ar";
		//spreqtype.colSpan=1;
		sname="";
		
		var name = "externalIdentifierValue(ExternalIdentifier:" + rowno +"_name)";
		sname="<input type='text' name='" + name + "'  maxlength='50' size='30' class='black_ar' id='" + name + "'>";      
	
	
		spreqtype.innerHTML="" + sname ;
	
		//Third Cell
		var spreqsubtype=x.insertCell(2);
		spreqsubtype.className="black_ar";
		//spreqsubtype.colSpan=2;
		sname="";
		
		name = "externalIdentifierValue(ExternalIdentifier:" + rowno +"_value)";
		sname= "";
			
		sname="<input type='text' name='" + name + "' maxlength='50' size='15' class='black_ar' style='text-align:right' id='" + name + "'>"   
		
		spreqsubtype.innerHTML="" + sname;
			
		//Fourth Cell
		
	}
		
		
	</script>
</head>

<table cellpadding="0" cellspacing="0" border="0" width="100%" id="externalIdentifiersTable">
		<tr onclick="showHide('add_id')">
		
		     <td width="96%" align="left" class="tr_bg_blue1" colspan="2"><span class="blue_ar_b">&nbsp;
		     	<bean:message key="specimen.externalIdentifier"/>
				<input type="hidden" name="eiDispType" value="<%=eiDispType1%>" id="eiDispType" />
		     </span>
			 </td>
			 <td width="4%" align="right" class="tr_bg_blue1"><a id="imgArrow_add_id" href="#"><img src="images/uIEnhancementImages/dwn_arrow1.gif" width="80" height="9" hspace="10" border="0" alt="Show Details" /></a>
			 </td>
		     
			<%
			String delExtIds="deleteExternalIdentifiers('pageOfMultipleSpecimen')";
			if((String)request.getAttribute(Constants.PAGEOF)!=null)
			{
				delExtIds="deleteExternalIdentifiers('"+(String)request.getAttribute(Constants.PAGEOF)+"');";
			}
			%>
		   
		 </tr>
		 <tr>
          <td colspan="5" class="showhide1" width="100%"><div id="add_id" style="display:none" >
			<table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr class="tableheading">
			 	<td align="left" class="black_ar_b" width="6%">
				<label for="delete" align="center">
					<bean:message key="app.select" />
				</label>
				</td>
			    <td align="left" class="black_ar_b" width="20%">
					<bean:message key="externalIdentifier.name"/>
				</td>
			    <td class="black_ar_b" width="70%" >
					<bean:message key="externalIdentifier.value"/>
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

				 		<html:hidden property="<%=exIdentifier%>" />
						<%
							String exKey = "ExternalIdentifier:" + i +"_id";
										boolean exBool = AppUtility.isPersistedValue(map,exKey);
										String exCondition = "";
										if(exBool)
											exCondition = "disabled='disabled'";
						%>
					<td align="left" class="black_ar" ><input type=checkbox name="<%=check %>" id="<%=check %>" <%=exCondition%> onClick="enableButton(document.forms[0].deleteExId,document.forms[0].exIdCounter,'chk_ex_')">	
                  </td>
			 		<td class="black_ar">
			     		<html:text styleClass="black_ar" maxlength="255" size="30" styleId="<%=exName%>" property="<%=exName%>" />
			    	</td>
			    	<td class="black_ar">
			     		<html:text styleClass="black_ar" maxlength="255" size="15" styleId="<%=exValue%>" property="<%=exValue%>" style="text-align:right"/>
			    	</td>
			    	
					
				 </tr>
			  <% } %>
		 </tbody>
		  <tr>
              <td colspan="3" align="left" class="black_ar" >
			  <html:button property="addExId" styleClass="black_ar" styleId="addExId" onclick="insExIdRow('addExternalIdentifier')" accesskey="A">
		     		<bean:message key="buttons.addMore"/>
		     	</html:button>
			 <html:button property="deleteExId" styleClass="black_ar" onclick='<%=delExtIds%>' disabled="true" accesskey="D">
						<bean:message key="buttons.delete" />
					</html:button>
              </td>
                      </tr>
                  
                
		 </table>
	 	</td>
	 </tr>
</table>