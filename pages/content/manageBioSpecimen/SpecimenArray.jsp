<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>


<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="java.util.Map,java.util.List"%>
<%@ page import="edu.wustl.catissuecore.actionForm.SpecimenArrayForm"%>
<%@ page import="edu.wustl.common.util.tag.ScriptGenerator" %>
<script src="jss/javaScript.js" type="text/javascript"></script>
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>
<html:errors/>
<% 
	SpecimenArrayForm form = (SpecimenArrayForm)request.getAttribute("specimenArrayForm");
	String exceedsMaxLimit = (String)request.getAttribute(Constants.EXCEEDS_MAX_LIMIT);
	String operation = (String)request.getAttribute(Constants.OPERATION);
	String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
	String formAction;
    if (operation.equals(Constants.EDIT))
    {
        formAction = Constants.SPECIMENARRAY_EDIT_ACTION;
    }
    else
    {
        formAction = Constants.SPECIMENARRAY_ADD_ACTION;
    }
%>	
<script>
function toStoragePositionChange(element)
{
	var autoDiv=document.getElementById("auto");
	var manualDiv=document.getElementById("manual");

	if(element.value == 1)
	{
		autoDiv.style.display = 'block';
		manualDiv.style.display = 'none';
	}
	else
	{
		autoDiv.style.display = 'none';
		manualDiv.style.display = 'block';
	}
}

function checkStotagePosition()
{
	var selectBox=document.getElementById("stContSelection");
	var autoDiv=document.getElementById("auto");
	var manualDiv=document.getElementById("manual");
	if(selectBox.value == 1)
	{
		autoDiv.style.display = 'block';
		manualDiv.style.display = 'none';
	}
	else
	{
		autoDiv.style.display = 'none';
		manualDiv.style.display = 'block';
	}
}
</script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/Hashtable.js"></script>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" /> 
<body onload="checkStotagePosition()">
<html:form action="<%=formAction%>">
<html:hidden property="id" />
<html:hidden property="operation" value="<%=operation%>"/>
<html:hidden property="submittedFor" value="<%=submittedFor%>"/>
<html:hidden property="subOperation" value=""/>
<html:hidden property="createSpecimenArray" value="<%=form.getCreateSpecimenArray()%>"/>
<html:hidden property="containerId" styleId="containerId"/>
<html:hidden property="forwardTo"/>

<!-- For Ordering System -->
<html:hidden  property="isDefinedArray" value="<%=form.getIsDefinedArray()%>"/>
<html:hidden  property="newArrayOrderItemId" value="<%=form.getNewArrayOrderItemId()%>"/>	

<!-----------New Code Begins----------------->

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
  <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head">
			<span class="wh_ar_b">
				<bean:message key="SpecimenArray.header" />
				</span></td>
        <td><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Specimen Array" width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
		<td class="td_tab_bg"><img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50"  height="1"></td>
<!-- Add Edit Tabs for the add Operation-->
<logic:equal name="operation" value="<%=Constants.ADD%>">
		<td valign="bottom"><img src="images/uIEnhancementImages/tab_add_selected.jpg" alt="Add Specimen Array" width="57" height="22" /></td>
		<td valign="bottom"><html:link page="/SimpleQueryInterface.do?pageOf=pageOfSpecimenArray&aliasName=SpecimenArray"><img src="images/uIEnhancementImages/tab_edit_notSelected.jpg" alt="Edit Specimen Array" width="59" height="22" border="0" /></html:link></td>
</logic:equal>
<!-- Add Edit Tabs for the edit Operation-->
<logic:equal name="operation" value="<%=Constants.EDIT%>">
		<td valign="bottom"><html:link page="/SpecimenArray.do?operation=add&amp;pageOf=pageOfSpecimenArray"><img src="images/uIEnhancementImages/tab_add_notSelected.jpg" alt="Add Specimen Array" width="57" height="22" /></html:link></td>
		<td valign="bottom"><img src="images/uIEnhancementImages/tab_edit_selected.jpg" alt="Edit Specimen Array" width="59" height="22" border="0" /></td>
</logic:equal>
		<td valign="bottom"><html:link page="/SpecimenArrayAliquots.do?pageOf=pageOfSpecimenArrayAliquot"><img src="images/uIEnhancementImages/tab_aliquot2.gif" alt="Aliquot Specimen Array" width="66" height="22" /></html:link></td>
        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
    <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
        <tr>
          <td colspan="2" align="left" class=" grey_ar_s">&nbsp;<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
			<bean:message key="commonRequiredField.message" />
			</td>
        </tr>
        <tr>
          <td colspan="2" align="left" class="tr_bg_blue1"><span class="blue_ar_b"> &nbsp;
			<logic:equal name="operation" value="<%=Constants.ADD%>">
						<bean:message key="array.title"/>
					</logic:equal>
					<logic:equal name="operation" value="<%=Constants.EDIT%>">
						<bean:message key="array.editTitle"/>
					</logic:equal>
					</span>
				</td>
			</tr>
			<tr>
          <td colspan="2" align="left" class="showhide"><table width="100%" border="0" cellpadding="3" cellspacing="0">
             <tr>
                <td width="1%" align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                <td width="17%" align="left" class="black_ar">
					<label for="specimenArrayType">
						<bean:message key="array.arrayType" />
					</label>
				</td>
				<td width="18%" align="left" class="black_new" nowrap><html:select property="specimenArrayTypeId" styleClass="formFieldSizedNew" styleId="state" size="1" onchange="changeArrayType()">
						<html:options collection="<%=Constants.SPECIMEN_ARRAY_TYPE_LIST%>" labelProperty="name" property="value"/>
					</html:select></td>
                <td width="16%" align="left">
					<html:link href="#" styleId="newSpecimenArrayType" styleClass="view" onclick="addNewAction('SpecimenArrayAddNew.do?addNewForwardTo=specimenarraytype&forwardTo=specimenarray&addNewFor=specimenArrayTypeId&subOperation=ChangeArraytype')">
							<bean:message key="buttons.addNew" />
					</html:link>
				</td>
                <td width="1%" align="center"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                <td width="17%" align="left" class="black_ar">
					<label for="createdBy">
						<bean:message key="array.createdBy" />
					</label>
				</td>
                <td width="20%" align="left" class="black_new" nowrap>
					<html:select property="createdBy" styleClass="formFieldSizedNew" styleId="state" size="1">
						<html:options collection="<%=Constants.USERLIST%>" labelProperty="name" property="value"/>
					</html:select>
				</td>
				<td width="10%" align="left" valign="top">&nbsp;</td>
              </tr>
              <tr>
                <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                <td align="left" class="black_ar">
					<label for="arrayLabel">
						<bean:message key="array.arrayLabel" />
					</label>
				</td>
				<td align="left">
					<html:text styleClass="black_ar"  maxlength="name"  size="30" styleId="name" property="name"/>
				</td>
				<td align="left" class="black_ar">&nbsp;</td>
                <td align="center" class="black_ar">&nbsp;</td>
                <td align="left" class="black_ar">
					<label for="barcode">
						<bean:message key="array.barcode"/> 
					</label>
				</td>
				<td align="left">
					<html:text styleClass="black_ar"  maxlength="255"  size="30" styleId="barcode" property="barcode"/>
				</td>
                <td align="left" valign="top">&nbsp;</td>
              </tr>
			  <tr>
                <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                <td align="left" class="black_ar">
					<label for="specimenClass">
						<bean:message key="arrayType.specimenClass" />
					</label>
				</td>
				<td align="left" nowrap class="black_new">
					<html:select property="specimenClass" styleClass="formFieldSizedNew" styleId="state" size="1" disabled="true">
						<html:options collection="<%=Constants.SPECIMEN_CLASS_LIST%>" labelProperty="name" property="value"/>
					</html:select>
				</td>
				<td align="left" class="black_ar">&nbsp;</td>
                <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                <td align="left" class="black_ar">
					<label for="specimenType">
						<bean:message key="arrayType.specimenType" />
					</label>
				</td>
				<td align="left" nowrap class="black_new">
					<html:select property="specimenTypes" styleClass="formFieldSizedNew" styleId="state" size="4" multiple="true" disabled="true">
						<html:options collection="<%=Constants.SPECIMEN_TYPE_LIST%>" labelProperty="name" property="value"/>
					</html:select>
				</td>
				<td align="left" valign="top">&nbsp;</td>
              </tr>
              <tr>
                <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                <td align="left" class="black_ar">
					<label for="className">
					   		<bean:message key="specimen.positionInStorageContainer"/>
					   </label>
				</td>
				<td colspan="6" align="left" nowrap>
					<table width="100%" border="0" cellspacing="0" cellpadding="3">
						<tr>
						  <td width="10%" class="black_new">
								<html:select property="stContSelection" styleClass="black_ar"
											styleId="stContSelection" size="1" onmouseover="showTip(this.id)"
											onmouseout="hideTip(this.id)" onchange= "toStoragePositionChange(this)">
										<html:options collection="storagePositionListForSpecimenArray"
														labelProperty="name" property="value" />
								</html:select> 
							</td>
							<td width="90%" align="left">
			<div Style="display:block" id="auto" >
					   
					
										<%-- n-combo-box start --%>
				<%
					Map dataMap = (Map) request.getAttribute(Constants.AVAILABLE_CONTAINER_MAP);
										
					String[] labelNames = {"ID","Pos1","Pos2"};
					labelNames = Constants.STORAGE_CONTAINER_LABEL;
					String[] attrNames = { "storageContainer", "positionDimensionOne", "positionDimensionTwo"};
					String[] tdStyleClassArray = { "formFieldSized15", "customFormField", "customFormField"}; 
					
					//String[] initValues = new String[3];
					//initValues[0] = form.getStorageContainer();
					//initValues[1] = String.valueOf(form.getPositionDimensionOne());
					//initValues[2] = String.valueOf(form.getPositionDimensionTwo());
					
					String[] initValues = new String[3];
					List initValuesList = (List)request.getAttribute("initValues");
					if(initValuesList != null)
					{
						initValues = (String[])initValuesList.get(0);
					}

					//System.out.println("NewSpecimen :: "+initValues[0]+"<>"+initValues[1]+"<>"+initValues[2]);			
					String rowNumber = "1";
					String styClass = "formFieldSized5";
					String tdStyleClass = "customFormField";
					String onChange = "onCustomListBoxChange(this)";
					String arrayTypeId = form.getSpecimenArrayTypeId()+"";
					String frameUrl = "ShowFramedPage.do?pageOf=pageOfSpecimen&amp;selectedContainerName=selectedContainerName&amp;pos1=pos1&amp;pos2=pos2&amp;containerId=containerId"
							+ "&" + Constants.CAN_HOLD_SPECIMEN_ARRAY_TYPE +"=" + arrayTypeId;
							
					String buttonOnClicked = "mapButtonClickedOnNewSpecimen('"+frameUrl+"')";  		
					// String buttonOnClicked = "javascript:NewWindow('"+frameUrl+"','name','800','600','no');return false";
					String noOfEmptyCombos = "3";
					
					int radioSelected = form.getStContSelection();
					
					System.out.println("HERE-->" + radioSelected);
					boolean dropDownDisable = false;
					boolean textBoxDisable = false;					
					if(radioSelected == 1)
					{									
						textBoxDisable = true;
						dropDownDisable = false;
					}
					else if(radioSelected == 2)
					{
						dropDownDisable = true;		
                        textBoxDisable = false;						
					}
					
				%>
				
				<%=ScriptGenerator.getJSForOutermostDataTable()%>
				<%=ScriptGenerator.getJSEquivalentFor(dataMap,rowNumber)%>
				
				<!--<td class="formField" colSpan="4">
						<table border="0">													
							<tr>								
								<td ><html:radio value="1" onclick="onRadioButtonGroupClickForArray(this)" styleId="stContSelection" property="stContSelection" /></td>								
								<td>-->
									<ncombo:nlevelcombo dataMap="<%=dataMap%>" 
										attributeNames="<%=attrNames%>" 
										initialValues="<%=initValues%>"  
										styleClass = "black_new" 
										tdStyleClass = "black_new" 
										tdStyleClassArray="<%=tdStyleClassArray%>"
										labelNames="<%=labelNames%>" 
										rowNumber="<%=rowNumber%>" 
										onChange = "<%=onChange%>"
										formLabelStyle="nComboGroup"
										disabled = "false"
										noOfEmptyCombos = "<%=noOfEmptyCombos%>"/>
										</tr>										
										</table>
								</div>
							
			<div Style="display:none" id="manual" >
							<table cellpadding="2" cellspacing="0" border="0" >
						<tr>
							<td class="groupelements">
									<html:text styleClass="black_ar"  size="20" styleId="selectedContainerName" property="selectedContainerName" disabled= "false"/>
								</td>
								<td class="groupelements">
									<html:text styleClass="black_ar" style="text-align:right" size="2" styleId="pos1" property="pos1" disabled= "false"/>
								</td>
								<td class="groupelements">
									<html:text styleClass="black_ar" style="text-align:right" size="2" styleId="pos2" property="pos2" disabled= "false"/>
								</td>
								<td class="groupelements">
									<html:button styleClass="black_ar" property="containerMap" onclick="<%=buttonOnClicked%>" disabled= "false">
										<bean:message key="buttons.map"/>
									</html:button>
								</td>
							</tr>
						</table>
					</div>
								</td>
							</tr>											
						</table>		
				</td>

                </tr>
				<logic:equal name="exceedsMaxLimit" value="true">
					<tr>
						<td>
							<bean:message key="container.maxView"/>
						</td>
					</tr>
				</logic:equal>
				<tr>
					<td align="center" class="black_ar_t">&nbsp;</td>
					<td align="left" class="black_ar_t">
						<label for="comments">
						<bean:message key="app.comments"/>
					</label>
				</td>
				<td colspan="6" align="left" nowrap>
					<html:textarea styleClass="black_ar" rows="3" styleId="comments" cols="90" property="comment"/>
				</td>
				 </tr>
            
          </table></td>
        </tr>
        
        <tr onclick="showHide('cap_id')">
			<td align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;
				<label for="capacity">
					<bean:message key="app.capacity" /> &nbsp;
				</label>
					</span>
			</td>
			<td align="right" class="tr_bg_blue1"><a href="#" id="imgArrow_cap_id"><img src="images/uIEnhancementImages/up_arrow.gif" alt="Show Details" width="80" height="9" hspace="10" border="0"/></a></td>
        </tr>
			<tr >
          <td colspan="5" class="showhide1"><div id="cap_id" style="display:block"><table width="100%" border="0" cellpadding="3" cellspacing="0">
            <tr>
              <td width="1%" align="center" class="black_ar">&nbsp;</td>
              <td width="17%" align="left" class="black_ar">
				<label for="oneDimensionCapacity">
					<bean:message key="app.oneDimension" />
				</label>
			  </td>
			  <td width="20%" align="left">
						<html:text styleClass="black_ar" maxlength="10"  size="15" styleId="oneDimensionCapacity" property="oneDimensionCapacity" readonly="true" style="text-align:right"/>
			  </td>
			  <td width="17%" align="left" class="black_ar">
					<label for="oneDimensionCapacity">
						<bean:message key="app.twoDimension" />
					</label>
			  </td>
			<td width="45%">			<html:text styleClass="black_ar" maxlength="10"  size="15" styleId="twoDimensionCapacity" property="twoDimensionCapacity" readonly="true" style="text-align:right"/>
						</td>
            </tr>
          </table></td>
        </tr>
		 <tr  >
          <td colspan="2" class="bottomtd"></td>
        </tr>
        <tr  >
          <td align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;
			<label for="addspecimens">
									<bean:message key="array.addspecimens" />
								</label>
							</span>
							</td>
					<td align="right" class="tr_bg_blue1">&nbsp;</td>
        </tr>
		<td colspan="2" class="showhide1"><table width="100%" border="0" cellpadding="3" cellspacing="0">
            <tr>
              <td width="1%" align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="3" /></td>
              <td width="17%" align="left" class="black_ar">
				<label for="array.enterSpecimenBy">
									<bean:message key="array.enterSpecimenBy" />
								</label>
				</td>
				<td width="7%" align="left" class="black_ar">
					<html:radio styleId="enterSpecimenBy" property="enterSpecimenBy" value="Label" onclick="doClickEnterSpecimenBy();"/> Label 
				</td>
				<td width="75%" align="left" class="black_ar">
								<html:radio styleId="enterSpecimenBy" property="enterSpecimenBy" value="Barcode" onclick="doClickEnterSpecimenBy();"/> Barcode 
							</td>
				</tr>
			<% 
							boolean disabled= true; 
							if (form.getCreateSpecimenArray().equals("yes")) 
							{
								disabled = false; 
						%>
						<tr>
							<td class="black_ar" colspan="4">
							<script language="JavaScript" type="text/javascript">
									platform = navigator.platform.toLowerCase();
									document.writeln('<APPLET\n' +
													'CODEBASE = "<%=Constants.APPLET_CODEBASE%>"\n'+
													'ARCHIVE = "CaTissueApplet.jar"\n'+
													'CODE = "<%=Constants.SPECIMEN_ARRAY_APPLET%>"\n'+
													'ALT = "Specimen Array Applet"\n'+
													'NAME = "<%=Constants.SPECIMEN_ARRAY_APPLET_NAME%>"'
													);
									if (platform.indexOf("mac") != -1)
									{
										document.writeln('width="1000" height="150" MAYSCRIPT>');
									} 
									else
									{
										document.writeln('width="100%" height="150" MAYSCRIPT>');
									}
									
									var url = location.protocol + '//' + location.hostname + ':' + location.port + location.pathname + '/../';
									document.writeln('<PARAM name="type" value="application/x-java-applet;jpi-version=1.3">\n' +
													'<PARAM name="name" value="<%=Constants.SPECIMEN_ARRAY_APPLET_NAME%>">\n'+
													'<PARAM name="rowCount" value="<%=form.getOneDimensionCapacity()%>">\n'+
													'<PARAM name="columnCount" value="<%=form.getTwoDimensionCapacity()%>">\n'+
													'<PARAM name="enterSpecimenBy" value="<%=form.getEnterSpecimenBy()%>">\n'+
													'<PARAM name="specimenClass" value="<%=form.getSpecimenClass()%>">\n'+
													'<PARAM name="session_id" value="<%=session.getId()%>">\n'+
													'<PARAM name = "<%=Constants.APPLET_SERVER_URL_PARAM_NAME%>" value= "' + url + '">\n'+ 
													'</APPLET>'
												    );
							</script>
							</td>
							</tr>				
						<%}%>
					<!-- Bug 4251-->
					
          </table></td>
        </tr>
		<tr  >
          <td colspan="2" class="bottomtd"></td>
        </tr>
        <tr  ><html:hidden property="onSubmit" />
          <td colspan="2" class="buttonbg">
			<html:button property="uploadSpecimenArrayButton" styleClass="blue_ar_b"						onclick="doUploadSpecimenArray();" disabled="<%=disabled%>" accesskey="Enter">
									<bean:message  key="buttons.uploadSpecimenArray" />
								</html:button>&nbsp;

					<%	
									String deleteAction="deleteSpecimenArray('" + Constants.DELETE_SPECIMEN_ARRAY +"','" + Constants.BIO_SPECIMEN + "')";
								%>
					<logic:equal name="operation" value="edit">
								|&nbsp;<html:button styleClass="blue_ar_c" property="disableSpecimenArray"
											onclick="<%=deleteAction%>">
									<bean:message key="buttons.delete"/>
								</html:button>&nbsp;
					</logic:equal>
								|&nbsp;
					<html:link page="/ManageAdministrativeData.do" styleClass="cancellink">
						<bean:message key="buttons.cancel" />
					</html:link>
							
		  </td>
        </tr>
    </table></td>
  </tr>
</table>

<!-----------New Code Ends------------------->
</html:form>
</body>