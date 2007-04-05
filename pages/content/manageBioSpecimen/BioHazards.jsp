<head>
<%
	String bhIdArray [] = (String []) request.getAttribute(Constants.BIOHAZARD_ID_LIST);
	String bhNameArray [] = (String []) request.getAttribute(Constants.BIOHAZARD_NAME_LIST);
	String bhTypeArray [] = (String []) request.getAttribute(Constants.BIOHAZARD_TYPES_LIST);
	// Patch_Id: Improve_Space_Usability_On_Specimen_Page_3
	// variable to get the current display style of collect biohazard table
	String bhDispType1=request.getParameter("bhDispType");
//	List biohazardList = (List)request.getAttribute(Constants.BIOHAZARD_TYPE_LIST);
%>
<script language="JavaScript">
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
		
	//ADD MORE FUNCTIONALITY FOR BIOHAZARDS
		function insBhRow(subdivtag)
		{
			var val = parseInt(document.forms[0].bhCounter.value);
			val = val + 1;
			document.forms[0].bhCounter.value = val;
			
			var r = new Array(); 
			r = document.getElementById(subdivtag).rows;
			var q = r.length;
			var x=document.getElementById(subdivtag).insertRow(0);
			
			// First Cell
			var spreqno=x.insertCell(0);
			spreqno.className="formSerialNumberField";
			sname=(q+1);
			//var identifier = "biohazardValue(Biohazard:" + (q+1) +"_id)";
			//var hiddenTag = "<input type='hidden' name='" + identifier + "' value='' id='" + identifier + "'>";
			spreqno.innerHTML= "" + sname;

			//Second Cell
			var spreqtype=x.insertCell(1);
			spreqtype.className="formField";
			sname="";

			var name = "biohazardValue(Biohazard:" + (q+1) + "_type)";
		// Mandar : 434 : for tooltip 
			sname="<select name='" + name + "' size='1' class='formFieldSized15' id='" + name + "' onchange=onBiohazardTypeSelected(this) onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";
			<%
					if(biohazardList != null && biohazardList.size() != 0)
					{
						for(int i=0;i<biohazardList.size();i++)
						{
							NameValueBean bean = (NameValueBean)biohazardList.get(i);
			%>
							sname = sname + "<option value='<%=bean.getValue()%>'><%=bean.getName()%></option>";
			<%			}
					}
			%>
			sname = sname + "</select>";
			spreqtype.innerHTML="" + sname;
		
			//Third Cellvalue()
			var spreqsubtype=x.insertCell(2);
			spreqsubtype.className="formField";
			spreqsubtype.colSpan=2;
			sname="";

			name = "biohazardValue(Biohazard:" + (q+1) + "_id)";
			sname= "";
	//Mandar : 434 : for tooltip 		
			sname="<select name='" + name + "' size='1' class='formFieldSized15' id='bhId" + (q+1) + "' onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";
			sname = sname + "<option value='-1'><%=Constants.SELECT_OPTION%></option>";			
			sname = sname + "</select>";
			spreqsubtype.innerHTML="" + sname;
			
			//Fourth Cell
			var checkb=x.insertCell(3);
			checkb.className="formField";
			checkb.colSpan=2;
			sname="";
			var name = "chk_bio_"+ (q+1);
			sname="<input type='checkbox' name='" + name +"' id='" + name +"' value='C' onClick=\"enableButton(document.forms[0].deleteBiohazard,document.forms[0].bhCounter,'chk_bio_')\">";
			checkb.innerHTML=""+sname;
		}
		
	// biohazard selection
		function onBiohazardTypeSelected(element)
		{ 
			var i = (element.name).indexOf("_");
			var indColon = (element.name).indexOf(":");
			var comboNo = (element.name).substring(indColon+1,i);
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
	</script>
</head>

<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%">
				 <tr>
				 <!--	/**
						* Name : Vijay_Pande
						* Reviewer : Santosh_Chandak
						* Bug ID: Improve_Space_Usability_On_Specimen_Page
						* Patch ID: Improve_Space_Usability_On_Specimen_Page_3
						* See also: 1-5
						* Description: To improve space usability on specimen page, the table which are rarely used are kept invisible by default. 
 						* Following image is provided to toggle between the show and hide the table. (Biohazard table).
						*/   -->	
					<td class="formFieldAllBorders" align="right" width="1%">
						<a id="imageBH" style="text-decoration:none" href="javascript:switchStyle('imageBH','bhDispType','biohazards');" >  
						<img src="images/nolines_minus.gif" border="0" width="18" height="18"/>
						</a>
						<input type="hidden" name="bhDispType" value="<%=bhDispType1%>" id="bhDispType" />
					</td> 
					<!-- Patch ends here -->
				     <td class="formTitle" width="200" height="20" colspan="2">
				     	<bean:message key="specimen.biohazards"/>
				     </td>
				     <td class="formButtonField" width="300" colspan="2">
				     	<html:button property="addBiohazard" styleClass="actionButton" onclick="insBhRow('addBiohazardRow')">
				     		<bean:message key="buttons.addMore"/>
				     	</html:button>
				    </td>
				    <!-- Patch_Id: Improve_Space_Usability_On_Specimen_Page_3
						Description: variable to set the function name and its argument pageOf to set for the delete button  -->
					<%
					String delBioHazard="deleteBioHazards('pageOfMultipleSpecimen')";
					if((String)request.getAttribute(Constants.PAGEOF)!=null)
					{
						delBioHazard="deleteBioHazards('"+(String)request.getAttribute(Constants.PAGEOF)+"');";
					}
					%>
				    <td class="formTitle" align="Right">
							<html:button property="deleteBiohazard" styleClass="actionButton" onclick='<%=delBioHazard%>' disabled="true">
								<bean:message key="buttons.delete"/>
							</html:button>
					</td>
					<!-- Patch ends here -->
				  </tr>
				  
				  <tr>
					  <td colspan="6" width="100%">
						<!-- Patch_Id: Improve_Space_Usability_On_Specimen_Page_3
							 Description: Table is seperated from its title to collapse/expand    -->
						  <table summary="" cellpadding="3" cellspacing="0" border="0" width="100%" id="biohazards">
						  <!-- Patch ends here -->
						  <tr>
							<td class="formSerialNumberLabel" width="5">
								#
							</td>
							<td class="formLeftSubTableTitle" width="350">
								<bean:message key="biohazards.type"/>
							</td>
							<td class="formRightSubTableTitle" colspan="2" width="350">
								<bean:message key="biohazards.name"/>
							</td>
							<td class="formRightSubTableTitle" width="50">
									<label for="delete" align="center">
										<bean:message key="addMore.delete" />
									</label>
							</td>
						 </tr>
						 
						 <tbody id="addBiohazardRow">
						 <html:hidden property="bhCounter"/>
						 <%
							Map bioHazardMap = form.getBiohazard();

							for(int i=bhRows;i>=1;i--)
							{
								String bhType = "biohazardValue(Biohazard:" + i + "_type)";
								String bhTypeKey = "Biohazard:" + i + "_type";
								String bhId	  = "biohazardValue(Biohazard:" + i + "_id)";
								String biohzId = "biohazardValue(Biohazard:" + i + "_persisted)";
								String check = "chk_bio_"+i;
						  %>
							<tr>
								<td class="formSerialNumberField" width="5"><%=i%>.
									<html:hidden property="<%=biohzId%>" />
								</td>
								<td class="formField">
		<!-- Mandar : 434 : for tooltip -->
									<html:select property="<%=bhType%>" styleClass="formFieldSized15" styleId="<%=bhType%>" size="1" onchange="onBiohazardTypeSelected(this)"
									 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
										<html:options collection="<%=Constants.BIOHAZARD_TYPE_LIST%>" labelProperty="name" property="value"/>
									</html:select>
								</td>
								<td class="formField" colspan="2">
		<!-- Mandar : 434 : for tooltip -->
									<html:select property="<%=bhId%>" styleClass="formFieldSized15" styleId="<%="bhId" + i%>" size="1"
									 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
										<html:option value="-1"><%=Constants.SELECT_OPTION%></html:option>
										<%
											String type = (String)bioHazardMap.get(bhTypeKey);
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
								<%
									String biohzKey = "Biohazard:" + i + "_persisted";
									String condition = (String)bioHazardMap.get(biohzKey);
									boolean biohzBool = Boolean.valueOf(condition).booleanValue();
									String biohzCondition = "";
									if(biohzBool)
										biohzCondition = "disabled='disabled'";
								%>
								<td class="formField" width="5">
									<input type=checkbox name="<%=check %>" id="<%=check %>" <%=biohzCondition%> onClick="enableButton(document.forms[0].deleteBiohazard,document.forms[0].bhCounter,'chk_bio_')">		
								</td>
							 </tr>
						  <% } %>
						 </tbody>
						</table>
					</td>
				</tr>
				
</table>