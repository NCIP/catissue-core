<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo"%>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.domain.Specimen"%>
<%@ page import="java.util.*"%>

<%
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
%>
<head>
	<%if(pageOf != null && pageOf.equals(Constants.CP_QUERY_PAGEOF_MULTIPLE_SPECIMEN_STORAGE_LOCATION))
	{
		String nodeId = "SpecimenCollectionGroup_";
		if(session.getAttribute("specimenCollectionGroupId") != null) {
			String scgId = (String) session.getAttribute("specimenCollectionGroupId");
			session.removeAttribute("specimenCollectionGroupId");
			nodeId = nodeId + scgId;
		}
%>
		<script language="javascript">
			var cpId = window.parent.frames[0].document.getElementById("cpId").value;
			var participantId = window.parent.frames[0].document.getElementById("participantId").value;
			window.parent.frames[1].location="showTree.do?<%=Constants.CP_SEARCH_CP_ID%>="+cpId+"&<%=Constants.CP_SEARCH_PARTICIPANT_ID%>="+participantId+"&nodeId=<%=nodeId%>";
			
		</script>
	<%}%>

<script language="JavaScript" type="text/javascript"
	src="jss/javascript.js"></script>
	
<script language="JavaScript">
	
	var newWindow;
	function showNewPage(action)
	{
	   	if(newWindow!=null)
		{
		   newWindow.close();
		}
	     newWindow = window.open(action,'','scrollbars=yes,status=yes,resizable=yes,width=860, height=600');
	     newWindow.focus(); 
	
    }
		
	function CPQuerySpecimen(id)
	{
		alert(id);
		window.parent.frames[2].location = "QuerySpecimenSearch.do?pageOf=pageOfNewSpecimenCPQuery&id="+id;
		alert("hi");
	}	
		
		
	</script>
	
</head>

<html:messages id="messageKey" message="true" header="messages.header"
	footer="messages.footer">
	<%=messageKey%>
</html:messages>

<%Collection specimenCollection = (Collection) request
					.getAttribute(Constants.SAVED_SPECIMEN_COLLECTION);
%>

<html:errors />

<html:form action="<%=Constants.ALIQUOT_ACTION%>">

	<table summary="" cellpadding="0" cellspacing="0" border="0"
		class="contentPage" width="660">
		<tr>
			<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0"
				width="660">

				<tr>
					<td class="formTitle" height="20" colspan="8"><bean:message
						key="multipleSpecimen.report.specimens" /></td>
				</tr>
				<tr>
					<td class="formSerialNumberField" width="10">#</td>
					<td class="formRequiredLabel">&nbsp;<bean:message key="specimen.label" /></td>
					<td class="formRequiredLabel">&nbsp;<bean:message key="specimen.barcode" /></td>
					<td class="formRequiredLabel">&nbsp;<bean:message key="specimen.type" /></td>
					<td class="formRequiredLabel">&nbsp;<bean:message key="specimen.subType" /></td>
					<td class="formRequiredLabel">&nbsp;<bean:message key="buttons.addNew" /></td>
					<td class="formRequiredLabel">&nbsp;<bean:message key="buttons.addNew" /></td>
					<td class="formRequiredLabel">&nbsp;<bean:message key="link.Distribute" /></td>
				</tr>
				<%int i = 0;
				Iterator specimenItr = specimenCollection.iterator();
				while (specimenItr.hasNext())
				{
					i++;
					Specimen specimen = (Specimen) specimenItr.next();
                    String specimenLabel = specimen.getLabel();
					
					
					String onClickSpecimenFunction = "showNewPage('SearchObject.do?pageOf=pageOfNewSpecimen&operation=search&id=" + specimen.getId() + "')";
					String onClickAliquotFunction = "showNewPage('Aliquots.do?pageOf=pageOfAliquot&menuSelected=15&label=" + specimenLabel + "')";
					String onClickDeriveFunction = "showNewPage('CreateSpecimen.do?operation=add&pageOf=&menuSelected=15&virtualLocated=true&parentSpecimenLabel=" + specimenLabel + "')";
					String onClickDistributeFunction = "showNewPage('Distribution.do?operation=add&pageOf=pageOfDistribution&menuSelected=16&label=" + specimenLabel + "')";
					if(pageOf != null && pageOf.equals(Constants.CP_QUERY_PAGEOF_MULTIPLE_SPECIMEN_STORAGE_LOCATION))
					{
						onClickSpecimenFunction = "CPQuerySpecimen(" + specimen.getId() + ")";
						onClickAliquotFunction = "showNewPage('Aliquots.do?pageOf=pageOfAliquot&menuSelected=15&label=" + specimenLabel + "')";
						onClickDeriveFunction = "showNewPage('CreateSpecimen.do?operation=add&pageOf=&menuSelected=15&virtualLocated=true&parentSpecimenLabel=" + specimenLabel + "')";
						onClickDistributeFunction = "showNewPage('Distribution.do?operation=add&pageOf=pageOfDistribution&menuSelected=16&label=" + specimenLabel + "')";
					}							
					String barcode = specimen.getBarcode();
					if(barcode==null)
					{
					    barcode = "";
					}
					%>
				<tr>
					<td class="formSerialNumberField" width="5"><%=i%></td>
					<td class="formField">&nbsp;
					<html:link href="#" styleId="specimen" onclick="<%=onClickSpecimenFunction%>">
					<%=specimen.getLabel()%>
					</html:link>
					</td>
					<td class="formField">&nbsp;
					&nbsp;<%=barcode%>
					</td>
					<td class="formField">&nbsp;
					&nbsp;<%=specimen.getClassName()%>
					</td>
					<td class="formField">&nbsp;
					&nbsp;<%=specimen.getType()%>
					</td>
					<td class="formField">&nbsp;
					<html:link href="#" styleId="aliquot" onclick="<%=onClickAliquotFunction%>">
					<bean:message key="link.Aliquot" />
					</html:link></td>
					<td class="formField">&nbsp;
					<html:link href="#" styleId="derive" onclick="<%=onClickDeriveFunction%>">
					<bean:message key="link.Derive" />
					</html:link></td>
					<td class="formField">&nbsp;
					<html:link href="#" styleId="derive" onclick="<%=onClickDistributeFunction%>">
					<bean:message key="link.Distribute" />
					</html:link></td>
				</tr>
				<%}

			%>
			</table>
			</td>
		</tr>
	</table>
</html:form>
