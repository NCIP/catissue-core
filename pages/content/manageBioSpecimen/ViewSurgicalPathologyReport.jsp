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
<%@ page import="edu.wustl.catissuecore.bean.ConceptHighLightingBean"%>
<%@ page import="java.util.regex.Matcher"%>
<%@ page import="java.util.regex.Pattern"%>

<script src="jss/ajax.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/viewSPR.js"></script>
<LINK href="css/styleSheet.css" type=text/css rel=stylesheet>
<%
	Map mapPMI = null;
	int noOfRowsPMI=0;
	ViewSurgicalPathologyReportForm formSPR=null;
	Object objAbsForm=null;
	String deidText=null;
	if(operation.equals("viewSPR"))
	{		
		objAbsForm = request.getAttribute("viewSurgicalPathologyReportForm");
   		
		if(objAbsForm != null && objAbsForm instanceof ViewSurgicalPathologyReportForm)
		{
			formName=Constants.VIEW_SPR_ACTION;
			formSPR=(ViewSurgicalPathologyReportForm)objAbsForm;
			mapPMI = formSPR.getValues();
			noOfRowsPMI = formSPR.getCounter();
			deidText=formSPR.getDeIdentifiedReportTextContent();
		}
	}
	List conceptClassificationList1 = (List)request.getAttribute(Constants.CONCEPT_BEAN_LIST);
	String[] onClickMethod = null;
	String[] colours = Constants.CATEGORY_HIGHLIGHTING_COLOURS;
	String[] conceptName = null;
	String[] startOff = null;
	String[] endOff = null;	
	if(conceptClassificationList1!=null)
	{
		onClickMethod=new String[conceptClassificationList1.size()];
		conceptName = new String[conceptClassificationList1.size()];
		startOff = new String[conceptClassificationList1.size()];
		endOff = new String[conceptClassificationList1.size()];	
		for(int i=0;i<conceptClassificationList1.size();i++)
		{
			ConceptHighLightingBean referentClassificationObj=(ConceptHighLightingBean) conceptClassificationList1.get(i);
			conceptName[i] = referentClassificationObj.getConceptName();
			startOff[i] = referentClassificationObj.getStartOffsets();
			endOff[i] = referentClassificationObj.getEndOffsets();	
			Pattern p = Pattern.compile("['\"]");
			Matcher m = p.matcher(conceptName[i]);
			conceptName[i] = m.replaceAll("");
		}
	}
%>

<head>
	<script>
	<%for(int i=0;i<colours.length;i++)
	{
	%>
		colours[<%=i%>]='<%=colours[i]%>';
	<%
	}
	%>
	<%for(int i=0;i<conceptName.length;i++)
	{
	%>
		conceptName[<%=i%>]='<%=conceptName[i]%>';
		startOff[<%=i%>]='<%=startOff[i]%>';
		endOff[<%=i%>]='<%=endOff[i]%>';
	<%
	}
	%>

	function checkBoxClicked()
	{
		document.getElementById("deidentifiedReportText").innerHTML="<PRE>"+document.getElementById("deidText").innerHTML+"</PRE>";
		for(i=0;i<conceptName.length;i++)
		{	
			selectByOffset(document.getElementById("select"+i),startOff[i],endOff[i],colours[i],conceptName[i]);	
		}		
	}
	</script>
	<style>
	pre {
	 white-space: pre-wrap;       /* css-3 */
	 white-space: -moz-pre-wrap;  /* Mozilla, since 1999 */
	 white-space: -pre-wrap;      /* Opera 4-6 */
	 white-space: -o-pre-wrap;    /* Opera 7 */
	 word-wrap: break-word;       /* Internet Explorer 5.5+ */
	}
	</style>
</head>
<html:form action="ViewSurgicalPathologyReport">
<!-- Main table start -->
<table id="reportTable" summary="" cellspacing="5" cellpadding="0" border="0"  style="table-layout:fixed" width="650" >
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
<!-- if pageOf is pageOfParticipant then display drop down list of surgical pathology number -->
<% if(pageOf.equals(Constants.PAGEOF_PARTICIPANT) || pageOf.equals(Constants.PAGE_OF_PARTICIPANT_CP_QUERY))
{
%>
	<tr>
		<td class="formFieldNoBordersSimple">
			<b>
				<bean:message key="viewSPR.reportInfo.spn"/> : 
			</b>
			<logic:notEmpty name="viewSurgicalPathologyReportForm" property="reportIdList" >
			
				<c:set var="reportIdElt" value="${viewSurgicalPathologyReportForm.reportIdList}"/>
				<jsp:useBean id="reportIdElt" type="java.util.List"/>
				<html:select property="reportId" styleClass="formFieldSized" styleId="reportId" size="1"
				 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onchange="sendRequestForReportInfo()">
					<html:options collection="reportIdElt" labelProperty="name" property="value"/>
				</html:select>

			</logic:notEmpty>
		</td>
	</tr>
<%
}
%>
	<tr>
		<td>
		<!-- block to diaply default links -->
			<table width="100%" >
				<tr>
					<td class="formTitle" height="20" colspan="2">
						<bean:message key="viewSPR.view.title" />
					</td>
				</tr>
				<tr>
					<td class="formFieldNoBordersBold" >
						<input type=radio name="review" value="abc1" checked="checked" onClick="clickOnLinkReport()" />
							<bean:message key="viewSPR.identifiedReport" />
					</td>
					<td class="formFieldNoBordersBold" >
						<input type=radio name="review" value="abc2" onClick="clickOnLinkShowDeidReport()" />
							<bean:message key="viewSPR.deIdenfiedReport" />
					</td>
				</tr>
				<tr>
					<td class="formFieldNoBordersBold" >
						<input type=radio name="review" value="abc3" onClick="clickOnLinkCompareReport()" />
							<bean:message key="viewSPR.compareReports" />
					</td>
					<td class="formFieldNoBordersBold" >
						<input type=radio name="review" value="abc4" onClick="" />
							<bean:message key="viewSPR.myRequests" />						
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
								<%=formSPR.getUserName()%> <bean:message key="viewSPR.label.comment"/>
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
		<td valign="top" >
			
		</td>
	</tr>
	<tr>
		<td>
		
		<table border="0" cellpadding="0" cellspacing="0"   width="100%" id="table2" >
			<tr>
				<td class="formTitle" height="20">
					<bean:message key="viewSPR.participantInformation.title"/>						
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
						<td class="formFieldWithNoTopBorder" width="365"  height="20" colspan="2">
							<b>  
								<bean:message key="user.lastName" /> : 
							</b>
							<logic:notEmpty name="viewSurgicalPathologyReportForm" property="lastName" >
								<%=formSPR.getLastName()%>
							</logic:notEmpty>
					     </td>
						<td class="formField" width="365" height="20" colspan="2">
							<b>
								<bean:message key="user.firstName" /> : 
							</b>
							<logic:notEmpty name="viewSurgicalPathologyReportForm" property="firstName" >
				     			<%=formSPR.getFirstName()%>
							</logic:notEmpty>
					     </td>
					</tr>
					<tr>
						<td class="formFieldWithNoTopBorder" width="50%" height="20" colspan="2">
							<b>
								<bean:message key="participant.birthDate" /> : 
							</b>
							<logic:notEmpty name="viewSurgicalPathologyReportForm" property="birthDate" >
				     			<%=formSPR.getBirthDate()%>
							</logic:notEmpty>
					     </td>
						<td class="formField" width="50%" height="20" colspan="2">
							<b>
								<bean:message key="participant.deathDate" /> : 
							</b>
							<logic:notEmpty name="viewSurgicalPathologyReportForm" property="deathDate" >
				     			<%=formSPR.getDeathDate()%>
							</logic:notEmpty>
					     </td>
					</tr>
					<tr>
						<td class="formFieldWithNoTopBorder" width="50%" height="20" colspan="2">
							<b>
								<bean:message key="participant.ethnicity" /> : 
							</b>
							<logic:notEmpty name="viewSurgicalPathologyReportForm" property="ethinicity" >
				     			<%=formSPR.getEthinicity()%>
							</logic:notEmpty>
					     </td>
						<td class="formField" width="50%" height="20" colspan="2">
							<b>
								<bean:message key="participant.race" /> : 
							</b>
							<logic:notEmpty name="viewSurgicalPathologyReportForm" property="race" >
									<%=formSPR.getRace()%>
							</logic:notEmpty>
					     </td>
					</tr>
					<tr>
						<td class="formFieldWithNoTopBorder" width="50%" height="20" colspan="2">
							<b>
								<bean:message key="participant.gender" /> : 
							</b>
							<logic:notEmpty name="viewSurgicalPathologyReportForm" property="gender" >
				     			<%=formSPR.getGender()%>
							</logic:notEmpty>
					     </td>
						<td class="formField" width="50%" height="20" colspan="2">
							<b>
								<bean:message key="participant.genotype" /> : 
							</b>
							<logic:notEmpty name="viewSurgicalPathologyReportForm" property="sexGenotype" >
				     			<%=formSPR.getSexGenotype()%>
							</logic:notEmpty>
					     </td>
					</tr>
					<tr>
						<td class="formFieldWithNoTopBorder" width="50%" height="20" colspan="4">
							<b>
								<bean:message key="participant.socialSecurityNumber" /> : 
							</b>
							<logic:notEmpty name="viewSurgicalPathologyReportForm" property="socialSecurityNumber" >
				     			<%=formSPR.getSocialSecurityNumber()%>
							</logic:notEmpty>
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
				for(int i=1;i<=noOfRowsPMI;i++)
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
		
		<tr>
		<td>
		<table border="0" cellpadding="5" cellspacing="0" width="100%" id="identifiedReportInfo" >
			<tr>
				<td colspan="3" class="formTitle" height="20">
					<bean:message key="viewSPR.identifiedReportInformation.title"/>
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
						<span id="surgicalPathologyNumber">
						<logic:notEmpty name="viewSurgicalPathologyReportForm" property="surgicalPathologyNumber" >
				     		<%=formSPR.getSurgicalPathologyNumber()%>
						</logic:notEmpty>
						</span>
				</td>
				<td class="formField"  height="20">
					<b>
						<bean:message key="specimenCollectionGroup.site"/> : 
					</b>
						<span id="identifiedReportSite">
						<logic:notEmpty name="viewSurgicalPathologyReportForm" property="identifiedReportSite" >
				     		<%=formSPR.getIdentifiedReportSite()%>
						</logic:notEmpty>
						</span>
				</td>
			</tr>
			<tr>
				<td  class="formFieldWithNoTopBorderFontSize1" colspan="3" >
				
				<div id="identifiedReportText" style="overflow:auto;height:200px;width:628"><PRE><logic:notEmpty name="viewSurgicalPathologyReportForm" property="identifiedReportTextContent" ><%=formSPR.getIdentifiedReportTextContent()%></logic:notEmpty></PRE>
				</div>
				</td>
			</tr>
		</table>
		</td>
		</tr>
		<tr>
		<td>
		<table border="0" cellpadding="5" cellspacing="0" width="100%" id="deidReportInfo" style='display:none'>
			<tr>
				<td colspan="3" class="formTitle" height="20">
					<bean:message key="viewSPR.deIdentifiedReportInformation.title"/>
				</td>
			</tr>
			<tr>
				<td colspan="3" class="formField">
					<table border="0" cellpadding="0" width="100%" cellspacing="0" id="categoryHighlighter" >
						<tr>
							<td class="formSubTitle" height="20"  nowrap>
								<bean:message key="viewSPR.categoryHighlighter.title"/>
							</td>
						</tr>
						 <tr>
						  <td>
						  <table id="classificationNames" border="0"  cellpadding="5" width="100%" cellspacing="0">
						<%
						  List conceptClassificationList = (List)request.getAttribute(Constants.CONCEPT_BEAN_LIST);
						  int chkBoxNo = 0;			  
						  if(conceptClassificationList != null && conceptClassificationList.size() > 0)
						  {%>
						 
						  <tr id="classificationNamesRow">
						<logic:iterate id="referentClassificationObj" collection="<%= conceptClassificationList %>" type="edu.wustl.catissuecore.bean.ConceptHighLightingBean">
							<td class="formFieldNoBordersBold">
								<% String chkBoxId = "select"+chkBoxNo; %>
								<input type="checkbox" id="<%=chkBoxId %>" onclick="checkBoxClicked()" />
								<% String spanStyle = "background-color:"+colours[chkBoxNo];%>
								<span id="classificationName" style="<%=spanStyle %>">
									<%=referentClassificationObj.getClassificationName() %>	
								</span>		
							</td>
						
						<% chkBoxNo++;%>
						</logic:iterate>
						</tr>
						
					<%} %>
					</table>
						</td>
					</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td  class="formFieldWithNoTopBorderFontSize1" colspan="3" >
				
				<div id="deidentifiedReportText" style="overflow:auto;height:200px;width:628"><PRE><logic:notEmpty name="viewSurgicalPathologyReportForm" property="deIdentifiedReportTextContent" ><%=formSPR.getDeIdentifiedReportTextContent()%></logic:notEmpty></PRE>
				</div>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>	
	<td>
		<table id="commentsTable" style="table-layout:fixed" width="100%">
			<tr>
				<td width="80%" colspan="2" class="formTitle" height="20">
					<bean:message key="viewSPR.label.comment"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<html:textarea property="comments" rows="3" cols="76"/>
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
	Boolean isReviewDisabled=true;
	Boolean isQuarantineDisabled=true;
	if((!(formSPR.getIdentifiedReportId().equals("")) || formSPR.getDeIdentifiedReportId()!=0)) 
	{
		isReviewDisabled=false;
	}
	if(formSPR.getDeIdentifiedReportId()!=0)
	{
		isQuarantineDisabled=false;
	}
%>
					<html:button property="action1" styleClass="actionButton" onclick="<%=submitReviewComments%>" disabled="<%=isReviewDisabled%>" >
						<bean:message key="viewSPR.button.requestForReview" />
					</html:button>

					<html:button property="action2" styleClass="actionButton" onclick="<%=submitQuarantineComments%>" disabled="<%=isQuarantineDisabled%>" >
						<bean:message key="viewSPR.button.requestForQuarantine" />
					</html:button>
<%
	}
%>
				</td>
			</tr>	
		</table>
	</td>
	</tr>
	<tr>
		<td>
			<span id="deidText" style="display:none"><PRE><%=deidText%></PRE></span>
		</td>
	</tr>
</table>	
</html:form>