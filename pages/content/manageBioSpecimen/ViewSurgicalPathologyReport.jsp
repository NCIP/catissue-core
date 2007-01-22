<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>

<script>
function expand(action)
{
	switchObj = document.getElementById('paricipantInformation');
	imageObj = document.getElementById('image');
	
	if(action== 'hide') //Clicked on - image
	{
		switchObj.style.display = 'none';				
		
		imageObj.innerHTML = '<img src="images/nolines_plus.gif" border="0" /> ';
		imageObj.href="javascript:expand('show');";
	}
	else  							   //Clicked on + image
	{
		if(navigator.appName == "Microsoft Internet Explorer")
		{					
			switchObj.style.display = 'block';
		}
		else
		{
			switchObj.style.display = 'table-row';
		}
		imageObj.innerHTML = '<img src="images/nolines_minus.gif" border="0" /> ';
		imageObj.href="javascript:expand('hide');";
	}
}

function show(obj)
{
	switchObj=document.getElementById(obj);
	if(navigator.appName == "Microsoft Internet Explorer")
	{					
		switchObj.style.display = 'block';
	}
	else
	{
		switchObj.style.display = 'table-row';
	}
}
function hide(obj)
{
	switchObj=document.getElementById(obj);
	switchObj.style.display='none'
}

function showDeidReport()
{

	if(document.getElementById('showDeReportChkbox').checked==true)
	{
		hide('identifiedReportInfo');
		expand('hide');
		show('deidReportInfo');

	}
	else
	{
		hide('deidReportInfo');
		show('identifiedReportInfo');
		expand('show');
	}
}
function report()
{
	hide('deidReportInfo');
	show('reportTable');
	show('categoryHighlighter');
	show('identifiedReportInfo');
	expand('show');
}
function compareReport()
{
	expand('hide')
	show('reportTable');
	show('identifiedReportInfo');
	show('deidReportInfo');
	show('categoryHighlighter');
}
function myRequests()
{
	
}
function confirmSubmit()
{
	if (confirm('Are you sure you want to Submit Comments?'))
  {
    return true;
  }
  else
  {
    return false;
  }
}
function submitReviewComments()
{
	if(confirmSubmit())
	{
		document.forms[0].submittedFor.value='review';
		var action="SurgicalPathologyReportEventParam.do?operation=add&pageOf=<%=pageOf%>"
		document.forms[0].action=action;
		document.forms[0].submit();
	}
}
function submitQuarantineComments()
{
	if(confirmSubmit())
	{
		document.forms[0].submittedFor.value='quarantine';
		var action="SurgicalPathologyReportEventParam.do?operation=add&pageOf=<%=pageOf%>"
		document.forms[0].action=action;
		document.forms[0].submit();
	}
}
</script>
<% readOnlyForAll=true; %>
<table id="reportTable" summary="" cellspacing="5" cellpadding="0" border="0"  style="table-layout:fixed" width="750" >
	 <tr>
		<td><html:hidden property="id" />
		<html:hidden property="identifiedReportId" />
		<html:hidden property="deIdentifiedReportId" />
		<html:hidden property="submittedFor"/>
		<html:hidden property="onSubmit"/>
		</td>
	</tr>
<%if(pageOf.equalsIgnoreCase(Constants.PAGEOF_PARTICIPANT))
{
%>
	<tr>
		<td class="formFieldNoBordersSimple" colspan="3">
			<b>
				<bean:message key="viewSPR.reportInfo.reportId"/> : 
			</b>
		
				<input type="select" name="Report ID" />
		</td>
	</tr>
<%
}
%>	
	<tr>
		<td colspan="3">
		<table width="100%">
			<tr>
				<td class="formFieldNoBordersBold" width="60%">
					<a href="javascript:report()">
						<bean:message key="viewSPR.report" />
					</a>&nbsp;&nbsp;|&nbsp;&nbsp;
			
					<a href="javascript:compareReport()">
						<bean:message key="viewSPR.compareReports" />
					</a>&nbsp;&nbsp;|&nbsp;&nbsp;
			
					<a href="javascript:myRequests()">
						<bean:message key="viewSPR.myRequests" />
					</a>
				</td>
				<td class="formFieldNoBordersBold"  align="right" width="40%">
					<input type="checkbox" id="showDeReportChkbox" selected="false" onclick="showDeidReport()">
					<bean:message key="viewSPR.showDeIdenfiedReport" />
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td width="15%" rowspan="8" valign="top" >
		<table border="0" cellpadding="5" cellspacing="0" width="100%" id="categoryHighlighter" >
			<tr>
				<td colspan="2" class="formTitle" height="20">
					<bean:message key="viewSurgicalPathologyReport.categoryHighlighter.title"/>
				</td>
			</tr>
			<tr>
				<td class="formLeftSubTableTitle" width="20%"><input type="checkbox" id="selectAll" selected="false"></td>
				<td class="formRightSubTableTitle">
					<bean:message key="app.selectAll" />
				</td>
			</tr>
			<tr>
				<td class="formFieldWithNoTopBorder">&nbsp;</td>
				<td class="formRequiredLabel">&nbsp;</td>
			</tr>
		</table>
		</td>
		<td colspan="2" width="85%" >
		<table border="0" cellpadding="0" cellspacing="0"   width="100%" id="table2" >
			<tr>
				<td class="formTitle" height="20" >
					<bean:message key="viewSurgicalPathologyReport.participantInformation.title"/>						
				</td>
				
				<td class="formFieldAllBorders" align="right" width="1%">
					<a id="image" style="text-decoration:none" href="javascript:expand('hide');">  
					<img src="images/nolines_minus.gif" border="0" width="18" height="18"/>
				</td> 
			</tr>
			<tr>
				<td colspan="2" >
				<table border="0" cellpadding="5" cellspacing="0" width="100%" id="paricipantInformation" >
					<tr>
						<td class="formFieldWithNoTopBorder" width="50%" height="20" colspan="2">
							<b>  
								<bean:message key="user.lastName" /> : 
							</b>
							<% if(formSPR.getLastName()!=null) {%>
				     			<%=formSPR.getLastName()%>
							<%}%>
					     </td>
						<td class="formField" width="50%" height="20" colspan="2">
							<b>
								<bean:message key="user.firstName" /> : 
							</b>
							<% if(formSPR.getFirstName()!=null) {%>
				     			<%=formSPR.getFirstName()%>
							<%}%>
					     </td>
					</tr>
					<tr>
						<td class="formFieldWithNoTopBorder" width="50%" height="20" colspan="2">
							<b>
								<bean:message key="participant.birthDate" /> : 
							</b>
							<% if(formSPR.getBirthDate()!=null) {%>
				     			<%=formSPR.getBirthDate()%>
							<%}%>
					     </td>
						<td class="formField" width="50%" height="20" colspan="2">
							<b>
								<bean:message key="participant.deathDate" /> : 
							</b>
							<% if(formSPR.getDeathDate()!=null) {%>
				     			<%=formSPR.getDeathDate()%>
							<%}%>
					     </td>
					</tr>
					<tr>
						<td class="formFieldWithNoTopBorder" width="50%" height="20" colspan="2">
							<b>
								<bean:message key="participant.ethnicity" /> : 
							</b>
							<% if(formSPR.getEthinicity()!=null) {%>
				     			<%=formSPR.getEthinicity()%>
							<%}%>
					     </td>
						<td class="formField" width="50%" height="20" colspan="2">
							<b>
								<bean:message key="participant.race" /> : 
							</b>
<%
String race;
int count=5;
if (formSPR.getRace() != null)
{
			Collection raceCollection=formSPR.getRace();
			Iterator it = raceCollection.iterator();
			StringBuffer tempRace=new StringBuffer();
			
			while (it.hasNext())
			{
				race=new String("that");
			    tempRace=tempRace.append((String)it.next());
				tempRace=tempRace.append(",");
				count=5;
			}
			race=tempRace.toString();
			if(race.length()>0)
			{
				race=race.substring(0,race.length()-1);
			}
%>
									<%=race%>
<%}%>
					     </td>
					</tr>
					<tr>
						<td class="formFieldWithNoTopBorder" width="50%" height="20" colspan="2">
							<b>
								<bean:message key="participant.gender" /> : 
							</b>
							<% if(formSPR.getGender()!=null) {%>
				     			<%=formSPR.getGender()%>
							<%}%>
					     </td>
						<td class="formField" width="50%" height="20" colspan="2">
							<b>
								<bean:message key="participant.genotype" /> : 
							</b>
							<% if(formSPR.getSexGenotype()!=null) {%>
				     			<%=formSPR.getSexGenotype()%>
							<%}%>
					     </td>
					</tr>
					<tr>
						<td class="formFieldWithNoTopBorder" width="50%" height="20" colspan="4">
							<b>
								<bean:message key="participant.socialSecurityNumber" /> : 
							</b>
							<% if(formSPR.getSocialSecurityNumber()!=null) {%>
				     			<%=formSPR.getSocialSecurityNumber()%>
							<%}%>
					     </td>
					</tr>

					<!-- Medical Identifiers Begin here -->
				 <tr>
				     <td class="formTitle" height="20" colspan="4">
				     	<bean:message key="participant.medicalIdentifier"/>
				     </td>
				  </tr>
				 <tr>
				 	<td class="formSerialNumberLabel" width="5">
				     	#
				    </td>
				    <td class="formLeftSubTableTitle">
						<bean:message key="medicalrecord.source"/>
					</td>
				    <td class="formRightSubTableTitle">
						<bean:message key="medicalrecord.number"/>
					</td>
				 </tr>	 
				 <tbody id="addMore">
				<%
				for(int i=1;i<=noOfRows;i++)
				{
					String siteName = "ParticipantMedicalIdentifier:"+i+"_Site_id";
					String medicalRecordNumber = "ParticipantMedicalIdentifier:"+i+"_medicalRecordNumber";
					
				%>
				 <tr>
				 	<td class="formFieldWithNoTopBorder" width="5"><%=i%>.
				 		
				 	</td>
				      <td class="formField">
				     	<%=formSPR.getValue(siteName)%>

				    </td>
				    <td  class="formField" colspan="2">
						<%=formSPR.getValue(medicalRecordNumber)%>
				    </td>
				 </tr>
				 <%
				}
				%>
				 </tbody>  					
				  <!-- Medical Identifiers End here -->


				</table>
				</td>
			</tr>
			
		</table>
		</td></tr>
		
		<tr><td colspan="2" >
		<table border="0" cellpadding="5" cellspacing="0" width="100%" id="identifiedReportInfo" >
			<tr>
				<td colspan="3" class="formTitle" height="20">
					<bean:message key="viewSurgicalPathologyReport.identifiedReportInformation.title"/>
				</td>
			</tr>
			<tr>
				<td class="formFieldWithNoTopBorder"  height="20" >
					<b>
						<bean:message key="viewSPR.reportInfo.reportId"/> : 
					</b>
				    	<% if(formSPR.getIdentifiedReportId()!=0) {%>
				     		<%=formSPR.getIdentifiedReportId()%>
						<%}%>
				</td>
				<td class="formField"  height="20" >
					<b>
						<bean:message key="viewSPR.reportInfo.accessionNumber" /> : 
					</b>
						<% if(formSPR.getIdentifiedReportAccessionNumber()!=null) {%>
				     		<%=formSPR.getIdentifiedReportAccessionNumber()%>
						<%}%>
				</td>
				<td class="formField"  height="20">
					<b>
						<bean:message key="specimenCollectionGroup.site"/> : 
					</b>
						<% if(formSPR.getIdentifiedReportSite()!=null) {%>
				     		<%=formSPR.getIdentifiedReportSite()%>
						<%}%>
				</td>
			</tr>
			<tr>
				<td  class="formFieldWithNoTopBorder" colspan="3" >
				
				<div id="identifiedReportText" style="overflow:auto;height:200px;width:100%">
					<% if(formSPR.getIdentifiedReportTextContent()!=null) {%>
				     		<%=formSPR.getIdentifiedReportTextContent()%>
						<%}%>
				</div>
				</td>
			</tr>
		</table>
		</td>
		</tr>
		<tr><td colspan="2" >
		<table border="0" cellpadding="5" cellspacing="0" width="100%" id="deidReportInfo" style='display:none'>
			<tr>
				<td colspan="3" class="formTitle" height="20">
					<bean:message key="viewSurgicalPathologyReport.deIdentifiedReportInformation.title"/>
				</td>
			</tr>
			<tr>
				<td class="formFieldWithNoTopBorder"  height="20" >
					<b>
						<bean:message key="viewSPR.reportInfo.reportId"/> : 
					</b>
				    	<% if(formSPR.getDeIdentifiedReportId()!=0) {%>
				     		<%=formSPR.getDeIdentifiedReportId()%>
						<%}%>
				</td>
				<td class="formField"  height="20" >
					<b>
						<bean:message key="viewSPR.reportInfo.accessionNumber" /> : 
					</b>
						<% if(formSPR.getDeIdentifiedReportAccessionNumber()!=null) {%>
				     		<%=formSPR.getDeIdentifiedReportAccessionNumber()%>
						<%}%>
				</td>
				<td class="formField"  height="20">
					<b>
						<bean:message key="specimenCollectionGroup.site"/> : 
					</b>
						<% if(formSPR.getDeIdentifiedReportSite()!=null) {%>
				     		<%=formSPR.getDeIdentifiedReportSite()%>
						<%}%>
				</td>
			</tr>
			<tr>
				<td  class="formFieldWithNoTopBorder" colspan="3" >
				
				<div id="identifiedReportText" style="overflow:auto;height:200px;width:100%">
					<% if(formSPR.getDeIdentifiedReportTextContent()!=null) {%>
				     		<%=formSPR.getDeIdentifiedReportTextContent()%>
						<%}%>
				</div>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>	
	<td colspan="2">
		<table id="commentsTable" style="table-layout:fixed" width="100%">
			<tr>
				<td width="80%" colspan="2" class="formTitle" height="20">
					<bean:message key="requestdetails.header.label.Comments"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<html:textarea property="comments" rows="3" cols="58"/>
				</td>
			</tr>
			
			<tr>
				
				<td colspan="2" align="right">
<%if(!(formSPR.getIdentifiedReportId()!=0 || formSPR.getDeIdentifiedReportId()!=0)) 
{%>
					<html:button property="action1" styleClass="actionButton" onclick="submitReviewComments()" disabled="true">
						<bean:message key="viewSPR.requestForReview.button.cation" />
					</html:button>
<%}
else 
{%>
					<html:button property="action1" styleClass="actionButton" onclick="submitReviewComments()" >
					<bean:message key="viewSPR.requestForReview.button.cation" />
					</html:button>
<%}%>
						

<%if(formSPR.getDeIdentifiedReportId()!=0)
{%>
					<html:button property="action2" styleClass="actionButton" onclick="submitQuarantineComments()">
						<bean:message key="viewSPR.requestForQuarantine.button.cation" />
					</html:button>
<%}
else 
{%>
					<html:button property="action2" styleClass="actionButton" onclick="submitQuarantineComments()" disabled="true">
						<bean:message key="viewSPR.requestForQuarantine.button.cation" />
					</html:button>
<%}%>
				</td>
			</tr>	
		</table>
	</td>
	</tr>
</table>