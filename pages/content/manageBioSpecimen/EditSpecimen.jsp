<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script>

window.dhx_globalImgPath = "dhtmlx_suite/imgs/";

</script>
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/css/dhtmlxcombo.css">
<script  src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script  src="dhtmlx_suite/js/dhtmlxcombo.js"></script>

<SCRIPT>var imgsrc="images/";</SCRIPT>

<LINK href="css/catissue_suite.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />

<script language="JavaScript" type="text/javascript" src="jss/newSpecimen.js"></script>

<!------------------------------------------------------------------------------->

<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxwindows.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxwindows_dhx_skyblue.css">

<script src="dhtmlx_suite/js/dhtmlxcontainer.js"></script>
<script src="dhtmlx_suite/js/dhtmlxwindows.js"></script>
<script src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript"	src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/caTissueSuite.js"></script>
<script src="jss/ajax.js" type="text/javascript"></script>

<!----------------------------------------------------------------------->

<html:form action="NewSpecimenEdit.do">

							  <html:hidden name="specimenDTO" property="generateLabel"/>
							  <html:hidden name="specimenDTO" property="operation"/>
								
								<html:hidden name="specimenDTO" property="parentSpecimenId"/>
								
								<html:hidden name="specimenDTO" property="id" styleId="id"/>
								
								<html:hidden name="specimenDTO" property="specimenCollectionGroupId"/>
								

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">

<tr>
		<td class="td_color_bfdcf3">
			<table border="0" cellpadding="0" cellspacing="0">
		      <tr>
				<td class="td_table_head">
					<span class="wh_ar_b">
						<bean:message key="app.newSpecimen" />
					</span>
				</td>
		        <td>
					<img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Specimen" width="31" height="24" hspace="0" vspace="0" />
				</td>
		      </tr>
		    </table>
		</td>
	  </tr>

	  <tr>
		<td class="tablepadding">
	
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		      <tr>
				<td class="td_tab_bg" >
					<img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1"></td>
						<td valign="bottom">
							<a onclick="newspecimenPage()" id="specimenDetailsTab" href="#">	
								<img src="images/uIEnhancementImages/tab_specimen_details1.gif" alt="Specimen Details"  width="126" height="22" border="0">
							</a>
						</td>
					<td valign="bottom">
					<a href="#">
						<img src="images/uIEnhancementImages/tab_events2.gif" alt="Events" width="56" height="22" onclick="eventClicked('');" border="0"></a></td><td valign="bottom"><a href="#"><img src="images/uIEnhancementImages/tab_view_surgical2.gif" alt="View Surgical Pathology Report" width="216" height="22" border="0" onclick="viewSPR('','');"></a></td><td valign="bottom"><a href="#"><img src="images/uIEnhancementImages/tab_view_annotation2.gif" alt="View Annotation" width="116" height="22" border="0" onClick="viewAnnotations('',document.forms[0].id.value,'','','')"></a></td><td align="left" valign="bottom" class="td_color_bfdcf3" >
							<a id="consentViewTab" href="#" onClick="consentTab('')"><img src="images/uIEnhancementImages/tab_consents2.gif" alt="Consents" width="76" border="0" height="22" >
					</a>
					</td>
					<td width="90%" align="left" valign="bottom" class="td_tab_bg" >&nbsp;
					</td>
				</tr>
		    </table>
	
		    <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
			<tr>
			<td><div id="mainTable"style="display:block"><table width="100%"  border="0" cellpadding="3" cellspacing="0" >
				<tr>
		          <td><%@ include file="/pages/content/common/ActionErrors.jsp" %></td>
		        </tr>
				<tr>
		          <td align="left" class="showhide">
					<table width="100%" border="0" cellpadding="3" cellspacing="0" >
					<!-- NEW SPECIMEN REGISTRATION BEGINS-->
						<tr>
				<logic:empty name="specimenDTO" property="parentSpecimenName">
				<logic:notEmpty name="specimenDTO" property="specimenCollectionGroupName">
		                  <td width="1%" align="center" class="black_ar">
							<span class="blue_ar_b">
								<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
							</span>
						  </td>
		                  <td width="17%" align="left" class="black_ar">
							<label for="specimenCollectionGroupName">
								<bean:message key="newSpecimen.groupName"/>
							</label>
						  </td>
						  <td width="34%" align="left" class="black_ar">
							<html:hidden name="specimenDTO" property="specimenCollectionGroupName" styleId="specimenCollectionGroupName"/>
								<label for="specimenCollectionGroupName">
									<bean:write name="specimenDTO" property="specimenCollectionGroupName" scope="request"/>
								</label>
						  </td>
				</logic:notEmpty>
				</logic:empty>
				
				<logic:notEmpty name="specimenDTO" property="parentSpecimenName">
						  <td width="1%" align="center" class="black_ar">
							<span class="blue_ar_b">
								&nbsp;
							</span>
						  </td>
						  <td width="17%" align="left" class="black_ar">
								<label for="parentSpecimenId">
									<bean:message key="newSpecimen.parentLabel"/>
								</label>
    					  </td>
 			        	  <td width="34%" align="left" class="black_ar">
						   		
								<html:hidden name="specimenDTO" property="parentSpecimenName"/>
								<label for="parentSpecimenId">
									<bean:write name="specimenDTO" property="parentSpecimenName" scope="request"/>
								</label>

 			        	  </td>
				</logic:notEmpty>

				
						  <td width="1%" align="center">
							<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
						  </td>
						  <td width="17%" align="left" class="black_ar">
							<label for="lineage">
								<bean:message key="specimen.lineage"/>
							</label>
						  </td>
						  <td width="34%" align="left" class="black_ar">
							<label for="lineage">
								<bean:write name="specimenDTO" property="lineage" scope="request"/>
								<html:hidden name="specimenDTO" property="lineage"/>
							</label>
						  </td>
				
						</tr>
				

						<tr>
							<td align="center" class="black_ar">
				
							</td>
							<td align="left" class="black_ar">
								<label for="label">
									<bean:message key="specimen.label"/>
								</label>
							</td>
							<td align="left">
								<html:text styleClass="black_ar" size="30" maxlength="255"  styleId="label" name="specimenDTO" property="label" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)"/>
							</td>

							<td align="center" class="black_ar">&nbsp;</td>
							<td align="left" class="black_ar">
								<label for="barcode">
									<bean:message key="specimen.barcode"/>
								</label>
							</td>
						
								<td width="34%" align="left" class="black_ar">
								
									<label for="barcode">
									<html:text name="specimenDTO" 
								styleClass="black_ar" maxlength="255" size="30"
								styleId="barcode" property="barcode" onblur="processData(this)"/>
								
									</label>


								
								</td>
								
						</tr>
				
						<tr>
							<td align="center" class="black_ar">
								<span class="blue_ar_b">
									<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
								</span>
							</td>
			                <td align="left" class="black_ar">
								<label for="className">
								    <bean:message key="specimen.type"/>
						     	</label>
							</td>
							<td align="left" class="black_new">
							
							
							<html:select property="className" name="specimenDTO" 
							styleClass="formFieldSized" styleId="className" size="1"
							onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)">
							<html:options collection="specimenClassList"
								labelProperty="name" property="value" />
						</html:select>
							
						
								 
							</td>

							<td align="center" class="black_ar">
								<span class="blue_ar_b">
									<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
								</span>
							</td>
			                <td align="left" class="black_ar">
								<label for="type">
								     <bean:message key="specimen.subType"/>
						     	</label>
							</td>
							<td align="left" class="black_new">
							<html:select property="type" name="specimenDTO" 
							styleClass="formFieldSized" styleId="type" size="1"
							onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)">
							<html:options collection="specimenTypeList"
								labelProperty="name" property="value" />
						</html:select>
						
								</td>
							</tr>
							<tr>
								<td align="center" class="black_ar">
									<span class="blue_ar_b">
									</span>
								</td>
			                    <td align="left" class="black_ar">
									<label for="tissueSite">
										<bean:message key="specimen.tissueSite"/>
									</label>
								</td>
								<td align="left" class="black_new" noWrap>
								<html:select property="tissueSite" name="specimenDTO" 
							styleClass="formFieldSized" styleId="tissueSite" size="1"
							onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)">
							<html:options collection="tissueSiteList"
								labelProperty="name" property="value" />
						</html:select>
								
									
									<span class="black_ar">
									<a href="#"																						onclick="NewWindow('','tissuesite','360','525','no');return							false">
											<img src="images/uIEnhancementImages/ic_cl_diag.gif" alt="Clinical Diagnosis" width="16" height="16" border="0"/></a></span></td>

			                    <td align="center" class="black_ar">
				
				
								</td>
			                    <td align="left" class="black_ar">
									<label for="tissueSide">
										<bean:message key="specimen.tissueSide"/>
									</label>
								</td>
								<td align="left" class="black_new">
								<html:select property="tissueSide" name="specimenDTO" 
							styleClass="formFieldSized" styleId="tissueSide" size="1"
							onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)">
							<html:options collection="tissueSideList"
								labelProperty="name" property="value" />
						</html:select>
								
									
								</td>
							</tr>
							<tr>
								<td align="center" class="black_ar">
				
									&nbsp;
				
								</td>
			                    <td align="left" class="black_ar">
									<label for="pathologicalStatus">
										<bean:message key="specimen.pathologicalStatus"/>
									</label>
								</td>
				
				
								<td align="left" class="black_new">
								<html:select property="pathologicalStatus" name="specimenDTO" 
							styleClass="formFieldSized" styleId="pathologicalStatus" size="1"
							onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)">
							<html:options collection="pathologicalStatusList"
								labelProperty="name" property="value" />
						</html:select>
								
									
						        </td>
				

				
								<td align="center" class="black_ar">&nbsp;
								</td>
								<td align="left" class="black_ar">
									<label for="createdDate">
										<bean:message key="specimen.createdDate"/>
									</label>
								</td>
								<td class="black_ar" >
								<html:text property="createdDate" styleClass="black_ar"
							       styleId="createdDate" size="10" name="specimenDTO" onblur="processData(this)"/>
							   	<span class="grey_ar_s capitalized">[<bean:message key="date.pattern" />]</span>&nbsp;
								</td>
				
							</tr>
							<tr>
								<td align="center" class="black_ar">
				
									&nbsp;
				
								</td>
			                    <td align="left" class="black_ar">
									<label for="quantity">
										<bean:message key="specimen.quantity"/>
									</label>
								</td>
								<td align="left" class="black_ar">
									<html:text styleClass="black_ar" size="10" maxlength="10"  styleId="quantity" property="quantity" name="specimenDTO"  style="text-align:right" onblur="processData(this)"/>
								     <span id="unitSpan">
				
									 </span>
								     <html:hidden property="unit"/>
								</td>
				                <td align="center" class="black_ar">&nbsp;</td>
								<td align="left" class="black_ar">
									<label for="concentration">
										<bean:message key="specimen.concentration"/>
									</label>
								</td>
								<td align="left" class="black_ar">
									<html:text styleClass="black_ar" maxlength="10"  size="10" styleId="concentration" property="concentration" style="text-align:right" name="specimenDTO" onblur="processData(this)"
							     		 disabled="false"/>
										<bean:message key="specimen.concentrationUnit"/>
								</td>
							</tr>
			
							<tr>
								 <td align="center" class="black_ar">&nbsp;
								 </td>
								 <td align="left" class="black_ar">&nbsp;
								 </td>
								 <td align="left" valign="top">
									<html:checkbox property="available" onblur="processData(this)">
									</html:checkbox>
									<span class="black_ar">
										<label for="available">
											<bean:message key="specimen.available" />
										</label>
									</span>
								</td>

								<td align="center" class="black_ar">
			
			&nbsp;
			
							</td>
							<td align="left" class="black_ar">
								<label for="availableQuantity">
										<bean:message key="specimen.availableQuantity" />
								</label>
							</td>
								<td width="28%" align="left" class="black_ar">
									<html:text styleClass="black_ar" maxlength="10"  size="10" styleId="availableQuantity" property="availableQuantity"		name="specimenDTO" style="text-align:right" onblur="processData(this)"/>
									<span id="unitSpan1">
			
									</span>
								</td>
							</tr>
			
			
							<tr>
								<td align="center" class="black_ar">
									<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
								</td>
				                <td align="left" class="black_ar">
									<label for="collectionStatus">
									<bean:message key="specimenCollectionGroup.collectionStatus" />

									</label>
								</td>
								<td class="black_new">
								
								<html:select property="collectionStatus" name="specimenDTO" 
							styleClass="formFieldSized" styleId="collectionStatus" size="1"
							onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)">
							<html:options collection="collectionStatusList"
								labelProperty="name" property="value" />
						</html:select>
								
									
								</td>

								<td align="center" class="black_ar">
									&nbsp;
								</td>
				                <td align="left" class="black_ar">
									<label for="activityStatus">
										<bean:message key="participant.activityStatus" />
									</label>
								</td>
								<td align="left" class="black_ar">
								
									<label for="activityStatus">
									<bean:write name="specimenDTO" property="activityStatus" scope="request"/>
										
									</label>
									<html:hidden name="specimenDTO" property="activityStatus"/>
								
								</td>
							</tr>
						
							<tr>
								<td width="1%" align="center">
						
								&nbsp;
						
								</td>
			                    <td width="16%" align="left" class="black_ar">
									<label for="className">
									   	<bean:message key="specimen.positionInStorageContainer"/>
								   </label>
								</td>
								

								
								<td colspan="4" class="black_ar">
					<!-------Select Box Begins----->
					
								<logic:equal name="specimenDTO" property="isVirtual" value="true">
									
									<input type="text" size="30" maxlength="255"  class="black_ar"  value='Virtually Located' readonly style="border:0px" id="storageContainerPosition" />
								</logic:equal>
								<logic:equal name="specimenDTO" property="isVirtual" value="false">
								<input type="text" size="30" maxlength="255"  class="black_ar"  value='<bean:write name="specimenDTO" property="containerName" scope="request"/>:(<bean:write name="specimenDTO" property="pos1" scope="request"/>,<bean:write name="specimenDTO" property="pos2" scope="request"/>)' readonly style="border:0px" id="storageContainerPosition" />
									
								</logic:equal>
								<logic:equal name="specimenDTO" property="collectionStatus" value="Collected">
									<input type="button" class="blue_ar_b" value="Edit" onclick="loadDHTMLXWindowForTransferEvent()" />
								</logic:equal>
								<logic:equal name="specimenDTO" property="collectionStatus" value="Pending">
									<input type="button" class="blue_ar_b" value="Select Container" onclick="loadDHTMLXWindowForTransferEvent()" />
								</logic:equal>
								
								<html:hidden name="specimenDTO" property="isVirtual" styleId="isVirtual"/>
								<html:hidden name="specimenDTO" property="containerName" styleId="containerName"/>
								<html:hidden name="specimenDTO" property="pos1" styleId="pos1"/>
								<html:hidden name="specimenDTO" property="pos2" styleId="pos2"/>
								<html:hidden name="specimenDTO" property="containerId" />
											</td>
											</tr>

									

		

								<tr>
								<td align="left">&nbsp;</td>
									<td align="left" valign="top" class="black_ar">
										<label for="comments">
											<bean:message key="specimen.comments"/>
										</label>
									</td>

								 	<td align="left" valign="top" colspan="4">
										<html:textarea styleClass="black_ar_s"  rows="3" cols="90" name="specimenDTO" styleId="comments" property="comments" onblur="processData(this)"/>
									</td>
								</tr>
							</table>
							</td>
							</tr>
							<!-- collectionstatus -->

							<tr>
							<td width="100%" class="bottomtd">

		

	
		
							</td></tr>

		<tr>
		<td>
		
							</td>
							</tr>
							</table>
							</div>
							</td>
							</tr>
							

        <tr>
          <td valign="middle" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="childSpecimen.label" /></span></td>
        </tr>

								<tr>
								<!--
          <td valign="middle" class="black_ar" >
		  -->
		  <td valign="top">
		  <table width="100%" border="0" cellpadding="3" cellspacing="0">
		       <tr>
                  <td width="17%" align="left" nowrap class="black_ar" colspan="2">
						<input type="radio" value="1" id="aliquotCheck" name="specimenChild" onclick="onCheckboxButtonClick(this)" checked="true"/>
								<bean:message key="app.none" />&nbsp;
						<input type="radio" value="2" id="aliquotChk" name="specimenChild" onclick="onCheckboxButtonClick(this)"/>
								<bean:message key="aliquots.title"/>
								&nbsp;
						<input type="radio" value="3" id="deriveChk" name="specimenChild" onclick="onCheckboxButtonClick(this)"/>
								<bean:message key="specimen.derivative" />
								&nbsp;
						<!-- 11706 S Desctiption : Remove equal check for Edit operation only....-->
						<input type="radio" value="4" id="createCpChildCheckBox" name="specimenChild" onclick="onCheckboxButtonClick(this)"/>
								<bean:message key="create.CpChildSp"/>
						<!-- 11706 E -->
					</td>
				</tr>

				<!--specimenPageButton-->
				<tr><td colspan="2"></td></tr>

					<tr>
						<td class="black_ar" width="18%" nowrap>
								 <div style="display:none" id="derivedDiv">
								 <bean:message key="summary.page.count" />&nbsp;
								<html:text styleClass="black_ar" styleId="numberOfSpecimens" size="10" property="numberOfSpecimens" style="text-align:right"/></div>
								<div style="display:block" id="aliquotDiv"><bean:message key="summary.page.count" />&nbsp;
								<html:text styleClass="black_ar" styleId="noOfAliquots" size="10" property="noOfAliquots" disabled="true" style="text-align:right"/></div>
								</td>
								<td class="black_ar" width="75%">
								<bean:message key="aliquots.qtyPerAliquot"/>&nbsp;

								<html:text styleClass="black_ar" styleId="quantityPerAliquot" size="10" property="quantityPerAliquot" disabled="true" style="text-align:right"/>

						</td>
					</tr>
				     <tr>
								<td class="dividerline" colspan="3"><span class="black_ar"></td>
								</tr>
								<tr>
		
	
								  <td>
		
			 				        </td>

			 				</tr>
	
							<tr>
								<td class="bottomtd"></td>
							</tr>
	
						   	 	<tr>
							  		<td align="left" colspan="2" class="buttonbg">
										<table cellpadding="4" cellspacing="0" border="0" id="specimenPageButton" width="100%"> 
	


	<tr>

		<td class="buttonbg">
		  
				<!--<html:button
					styleClass="blue_ar_b" property="submitButton"
					title="Submit only"
					value="Submit"
					onclick="submit()">
				</html:button>
		 
		
		
		
		
		
			|&nbsp;<html:button
				styleClass="blue_ar_c" property="moreButton"
				title="Submit and add events"
				value="Submit"
				onclick="submitMore()">
			</html:button>
		


		
						| --><input type="button" value="Submit"
							onclick="submitTabData()" class="blue_ar_b">
	
		<input type="checkbox" name="objCheckbox"  id="objCheckbox" style="display:none" value="team" checked/>
		</td>
	</tr>
</table>
							  		</td>
							 	</tr>

<!-- NEW SPECIMEN REGISTRATION ends-->
				</table>
			</td>
		</tr>
		<tr>
			<td height="*">&nbsp;</td>
		</tr>
		</table>
	 </td>
   </tr>
</table>
</html:form>

<script>
var z = dhtmlXComboFromSelect("tissueSite");
z.enableFilteringMode(true);
var z = dhtmlXComboFromSelect("tissueSide");
z.enableFilteringMode(true);
var z = dhtmlXComboFromSelect("pathologicalStatus");
z.enableFilteringMode(true);

var z = dhtmlXComboFromSelect("className");
z.enableFilteringMode(true);
var z = dhtmlXComboFromSelect("type");
z.enableFilteringMode(true);

//////////////////////////////////////////////////////////
//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
var tabDataJSON = {};
tabDataJSON["id"] = document.getElementById("id").value; 
//alert(tabDataJSON);

//initialization of clinicalstudytab page
function initialize(startDateObj,endDateObj)
{
	var startDate = calendar.init(startDateObj,"%m-%d-%Y");
	var endDate = calendar.init(endDateObj,"%m-%d-%Y");
}


//creates json string for the changed fields and will validate the fields too
function processData(obj)
{//alert(obj.value);
tabDataJSON["id"] = document.getElementById("id").value; 
	tabDataJSON[obj.name] = obj.value; //after rendering struts html tag the 'property' attribute becomes 'name' attribute.
}


//submits changed data
function submitTabData()
{
	var isVirtual = document.getElementById("isVirtual").value;
	if(!isVirtual)
	{
		tabDataJSON["containerName"]= document.getElementById("containerName").value;
		tabDataJSON["pos1"]= document.getElementById("pos1").value;
		tabDataJSON["pos2"]= document.getElementById("pos2").value;
	}
	dhtmlxAjax.postSync("UpdateSpecimen.do","dataJSON="+JSON.stringify(tabDataJSON));
}

function updateHelpURL()
{
	var URL="";
	
		
	
	return URL;
}

function loadDHTMLXWindowForTransferEvent()
{
//alert(document.getElementById('barcode'));
var w =700;
var h =450;
var x = (screen.width / 3) - (w / 2);
var y = 0;
dhxWins = new dhtmlXWindows(); 
dhxWins.createWindow("containerPositionPopUp", x, y, w, h);
var url = "ShowStoragePositionGridView.do?pageOf=pageOfSpecimen&forwardTo=gridView&pos1=<bean:write name='specimenDTO' property='pos1' scope='request'/>&pos2=<bean:write name='specimenDTO' property='pos2' scope='request'/>&holdSpecimenClass=<bean:write name='specimenDTO' property='className' scope='request'/>&holdSpecimenType=<bean:write name='specimenDTO' property='type' scope='request'/>&containerName=<bean:write name='specimenDTO' property='containerName' scope='request'/>&collectionProtocolId=&collStatus=<bean:write name='specimenDTO' property='collectionStatus' scope='request'/>&isVirtual=<bean:write name='specimenDTO' property='isVirtual' scope='request'/>";
dhxWins.window("containerPositionPopUp").attachURL(url);                     
//url : either an action class or you can specify jsp page path directly here
dhxWins.window("containerPositionPopUp").button("park").hide();
dhxWins.window("containerPositionPopUp").allowResize();
dhxWins.window("containerPositionPopUp").setModal(true);
dhxWins.window("containerPositionPopUp").setText("");    //it's the title for the popup
}
</script>
