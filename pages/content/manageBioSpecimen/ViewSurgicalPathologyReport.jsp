<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ViewSurgicalPathologyReportForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.domain.pathology.ConceptReferent"%>
<%@ page import="edu.wustl.catissuecore.domain.pathology.Concept"%>

<script src="jss/ajax.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<LINK href="css/styleSheet.css" type=text/css rel=stylesheet>
<script language="JavaScript">

function finishReview()
{
	if(confirmSubmit())
	{
		document.forms[0].submittedFor.value='review';
		document.forms[0].forwardTo.value='success';
		var action="SurgicalPathologyReportEventParam.do?operation=edit&requestFor=REVIEW"
		document.forms[0].action=action;
		document.forms[0].submit();
	}
}

function submitAcceptComments()
{
	if(confirmSubmit())
	{
		document.forms[0].submittedFor.value='quarantine';
		document.forms[0].forwardTo.value='success';
		var action="SurgicalPathologyReportEventParam.do?operation=edit&requestFor=ACCEPT"
		document.forms[0].acceptReject.value=1;
		document.forms[0].action=action;
		document.forms[0].submit();
	}
}
//<!--function to submit quarantine comments-->
function submitRejectComments()
{
	if(confirmSubmit())
	{
		document.forms[0].submittedFor.value='quarantine';
		document.forms[0].forwardTo.value='success';
		var action="SurgicalPathologyReportEventParam.do?operation=edit&requestFor=REJECT"
		document.forms[0].acceptReject.value=2;
		document.forms[0].action=action;
		document.forms[0].submit();
		
	}
}

//Added by Ashish
function selectByOffset(checkbox,start,end,colour,conceptName)
{
	var innerHtml = document.getElementById("deidentifiedReportText").innerHTML;
	var i = ReplaceTags(innerHtml);

	var startArr = start.split(",");
	var endArr = end.split(",");
	var conceptNameArr = conceptName.split(",");
	var newtext = "";
	var count = 0;
	for(var x=0;x<startArr.length;x++)
	{		
		var subStr = i.substring(startArr[x],endArr[x]);
		
		var textBeforeString = i.substring(0,startArr[x]);
		var textAfterString = i.substring(endArr[x]);
		
		if(checkbox.checked==false)
		{
			colour='white';
			//conceptName="";
		}
		var text = "<span title="+conceptName[x]+" style='background-color:"+colour+"'>"+subStr+"</span>";
			
		if(count == 0)
		{
			newtext = innerHtml.replace(subStr,text);
			count++;
		}
		else
		{
			newtext = newtext.replace(subStr,text);
		}	
		
		//newtext = 	textBeforeString + text + textAfterString;
	}
	document.getElementById("deidentifiedReportText").innerHTML=newtext;
}
var regExp = /<\/?[^>]+>/gi;
function ReplaceTags(xStr)
{
  xStr = xStr.replace(regExp,"");
  return xStr;
}
</script>

<head>

</head>


<html:form action="ViewSurgicalPathologyReport">
<!-- Main table start -->
<table id="reportTable" summary="" cellspacing="5" cellpadding="0" border="0"  style="table-layout:fixed" width="750" >
	 <tr>
		<td>
			<html:hidden property="id" />
			<html:hidden property="identifiedReportId" />
			<html:hidden property="deIdentifiedReportId" />
			<html:hidden property="submittedFor"/>
			<html:hidden property="forwardTo"/>
			<html:hidden property="pageOf"/>
			<html:hidden property="acceptReject"/>
		</td>
	</tr>
<!-- if pageOf is pageOfParticipant then display drop down list of report accession number -->
<%if(pageOf.equalsIgnoreCase(Constants.PAGEOF_PARTICIPANT))
{
%>
	<tr>
		<td class="formFieldNoBordersSimple" colspan="3">
			<b>
				<bean:message key="viewSPR.reportInfo.spn"/> : 
			</b>
			<% if(formSPR.getReportIdList()!=null)
			{
			%>
						<c:set var="reportIdElt" value="${viewSurgicalPathologyReportForm.reportIdList}"/>
						<jsp:useBean id="reportIdElt" type="java.util.List"/>
				     	<html:select property="reportId" styleClass="formFieldSized" styleId="reportId" size="1"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="reportIdElt" labelProperty="name" property="value"/>
						</html:select>

			<%
				}
			%>
		</td>
	</tr>
<%
}
%>	
	<tr>
		<td colspan="3">
		<!-- block to diaply default links -->
			<table width="100%">
				<tr>
					<td class="formFieldNoBordersBold" width="60%">
						<a href="javascript:clickOnLinkReport()">
							<bean:message key="viewSPR.report" />
						</a>&nbsp;&nbsp;|&nbsp;&nbsp;
				
						<a href="javascript:clickOnLinkCompareReport()">
							<bean:message key="viewSPR.compareReports" />
						</a>&nbsp;&nbsp;|&nbsp;&nbsp;
				
						<a href="javascript:clickOnLinkMyRequests()">
							<bean:message key="viewSPR.myRequests" />
						</a>
					</td>
					<td class="formFieldNoBordersBold"  align="right" width="40%">
						<input type="checkbox" id="showDeReportChkbox" selected="false" onclick="clickOnLinkShowDeidReport()">
						<bean:message key="viewSPR.showDeIdenfiedReport" />
					</td>
				</tr>
				<%
				String requestFor=(String)request.getParameter(Constants.REQUEST_FOR);
				if(requestFor!=null||pageOf.equals(Constants.REVIEW_SPR)||pageOf.equals(Constants.QUARANTINE_SPR)||requestFor!=null)
				{
				%>
					<tr>
					<td width="80%" colspan="2" class="formTitle" height="20">
						<% if(formSPR.getUserName()!=null||requestFor!=null)
							{
						%>
								<%=formSPR.getUserName()%> <bean:message key="requestdetails.header.label.Comments"/>
						<%
							}
						%>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<html:textarea property="userComments" rows="2" cols="89" readonly="true"/>
					</td>
				</tr>
				<%
				}
				%>
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
			<!-- tr>
				<td class="formLeftSubTableTitle" width="20%"><input type="checkbox" id="selectAll" selected="false"></td>
				<td class="formRightSubTableTitle">
					<bean:message key="app.selectAll" />
				</td>
			</tr-->		
			
			<%
			  List conceptClassificationList = (List)request.getAttribute(Constants.CONCEPT_BEAN_LIST);
			  int chkBoxNo = 0;			  
			  if(conceptClassificationList != null && conceptClassificationList.size() > 0)
			  {%>
			<logic:iterate id="referentClassificationObj" collection="<%= conceptClassificationList %>" type="edu.wustl.catissuecore.bean.ConceptHighLightingBean">
			
			<%				
				String conceptName = referentClassificationObj.getConceptName();
				String startOff = referentClassificationObj.getStartOffsets();
				String endOff = referentClassificationObj.getEndOffsets();				
				String[] colours = Constants.CATEGORY_HIGHLIGHTING_COLOURS;
			%>
			
			<tr>
				<td class="formFieldWithNoTopBorder">
					<% String chkBoxId = "select"+chkBoxNo; 
					   String onClickArgument = "selectByOffset(this,'"+startOff+"','"+endOff+"','"+colours[chkBoxNo]+"','"+conceptName+"')";	
					%>
					<input type="checkbox" id="<%=chkBoxId %>" onclick="<%=onClickArgument %>" />
				</td>
				<td class="formRequiredLabel">
				<% String spanStyle = "background-color:"+colours[chkBoxNo];%>
					<span id="classificationName" style="<%=spanStyle %>">
						<%=referentClassificationObj.getClassificationName() %>	
					</span>		
				</td>
			</tr>
			<% chkBoxNo++;%>
			</logic:iterate>
		<%} %>
		</table>
		</td>
		<td colspan="2" width="85%" >
		<table border="0" cellpadding="0" cellspacing="0"   width="100%" id="table2" >
			<tr>
				<td class="formTitle" height="20" >
					<bean:message key="viewSurgicalPathologyReport.participantInformation.title"/>						
				</td>
				
				<td class="formFieldAllBorders" align="right" width="1%">
					<a id="image" style="text-decoration:none" href="javascript:switchStyle('hide');">  
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
					if(!((String)formSPR.getValue(siteName)).startsWith("--"))
					{
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
			<!--	<td class="formFieldWithNoTopBorder"  height="20" >
					<b>
						<bean:message key="viewSPR.reportInfo.reportId"/> : 
					</b>
				    	<% if(!formSPR.getIdentifiedReportId().equals("")) {%>
				     		<%=formSPR.getIdentifiedReportId()%>
						<%}%>
				</td> -->
				<td class="formFieldWithNoTopBorder"  height="20" >
					<b>
						<bean:message key="viewSPR.reportInfo.spn" /> : 
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
				
				<div id="identifiedReportText" style="overflow:auto;height:200px;width:100%"><%if(formSPR.getIdentifiedReportTextContent()!=null){%><%=formSPR.getIdentifiedReportTextContent()%><%}%></div>
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
		<!--	<tr>
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
			</tr>  -->
			<tr>
				<td  class="formFieldWithNoTopBorder" colspan="3" >
				
				<div id="deidentifiedReportText" style="overflow:auto;height:200px;width:100%"><%if(formSPR.getDeIdentifiedReportTextContent()!=null){%><%=formSPR.getDeIdentifiedReportTextContent()%><%}%></div>
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
<%
	pageOf=request.getParameter(Constants.PAGEOF);
	if(pageOf.equals(Constants.REVIEW_SPR))
	{
%>
		<input type="button" name="doneButton" style="actionButton" value="Finish Review " onclick="finishReview()"/>
		
<%					
	}
	else if(pageOf.equals(Constants.QUARANTINE_SPR))
	{
%>
		
		<input type="button" name="doneButton" style="actionButton" value="Accept" onclick="submitAcceptComments()"/>
		
		<input type="button" name="doneButton" style="actionButton" value="Reject" onclick="submitRejectComments()"/>
		
<%
	}
	else
	{
%>

<%
	String consentTier =(String)request.getParameter("consentTierCounter");
	String submitReviewComments = "submitReviewComments('"+ consentTier+"')";
	String submitQuarantineComments = "submitQuarantineComments('"+ consentTier+"')";
	if(!(formSPR.getIdentifiedReportId().equals("") || formSPR.getDeIdentifiedReportId()!=0)) 
{%>
					<html:button property="action1" styleClass="actionButton" onclick="<%=submitReviewComments%>" disabled="true">
						<bean:message key="viewSPR.requestForReview.button.cation" />
					</html:button>
<%}
else 
{%>
					<html:button property="action1" styleClass="actionButton" onclick="<%=submitReviewComments%>" >
					<bean:message key="viewSPR.requestForReview.button.cation" />
					</html:button>
<%}%>
						

<%if(formSPR.getDeIdentifiedReportId()!=0)
{%>
					<html:button property="action2" styleClass="actionButton" onclick="<%=submitQuarantineComments%>" >
						<bean:message key="viewSPR.requestForQuarantine.button.cation" />
					</html:button>
<%}
else 
{%>
					<html:button property="action2" styleClass="actionButton" onclick="<%=submitQuarantineComments%>" disabled="true">
						<bean:message key="viewSPR.requestForQuarantine.button.cation" />
					</html:button>
<%}%>

				
<%
	}
%>


				</td>
			</tr>	
		</table>
	</td>
	</tr>
</table>
</html:form>