<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%
	String operation = (String)request.getParameter(Constants.OPERATION);
	if (operation == null)
	{
		operation = "add";
	}
	Long id = (Long)request.getAttribute("id");
	String queryString = request.getQueryString();
	SpecimenCollectionGroupForm scgForm=(SpecimenCollectionGroupForm)request.getAttribute("specimenCollectionGroupForm");
	String submittedFor=(String)request.getParameter(Constants.SUBMITTED_FOR);
	Long collectionProtocolId=new  Long(0);
	Long siteId=new  Long(0);
	Long ParticipantId=new  Long(0);
	String protocolParticipantIdentifier="";
	if(scgForm!=null)
	{
		collectionProtocolId=scgForm.getCollectionProtocolId();
		siteId=scgForm.getSiteId();
		ParticipantId=scgForm.getParticipantId();
		protocolParticipantIdentifier=scgForm.getProtocolParticipantIdentifier();
	}
	if(queryString==null)
	{
			queryString = queryString  + "operation=edit&pageOf=pageOfSpecimenCollectionGroup";
	}
	queryString = queryString + "&pageOf=pageOfSpecimenCollectionGroup&id="+id;
	Long participantEntityId = Utility.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN_COLLN_GROUP);
	String participantAnnotationsQueryString = "?entityId="+participantEntityId+"&entityRecordId="+id;	
	String str="&collectionProtocolId="+ collectionProtocolId.toString()+"&siteId="+siteId.toString()+"&participantId="+ParticipantId.toString()+"&protocolParticipantIdentifier="+protocolParticipantIdentifier;

	if(operation!=null && !operation.equalsIgnoreCase("add"))
	{	
		str=str+"&"+Constants.SHOW_CONSENTS+"=yes";
	}
	str=str+"&"+Constants.SUBMITTED_FOR+"="+submittedFor;
	if(collectionProtocolId!=0)
	{
		queryString=queryString+str; 
	}
	else
	{
		queryString=queryString+"&"+Constants.SUBMITTED_FOR+"="+submittedFor;
	}
	

%>
<html>
<head>
	<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
	<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/dhtml_comp/css/dhtmlXTabbar.css"/>
	<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/dhtml_comp/css/dhtmlXGrid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/styleSheet.css" />
		<script  src="<%=request.getContextPath()%>/jss/javaScript.js"></script>
		<script  src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXCommon.js"></script>
		<script  src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXTabbar.js"></script>
		<script  src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXTabBar_start.js"></script>
		<script  src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXCommon.js"></script>
		<script  src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXGrid.js"></script>
		<script  src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXGridCell.js"></script>
		<script src="<%=request.getContextPath()%>/jss/javaScript.js" type="text/javascript"></script>
		
</head>  
<body>
<div id="a_tabbar" style="width:800;height:600" ></div>
<script>
			var opr = '<%=operation%>';
			var str= '<%=queryString%>';
			if(opr=="createMultipleSpecimen")
			{
				top.location.href="<%=request.getContextPath()%>/InitMultipleSpecimen.do?operation=add&menuSelected=15";
			}
			else
			{
				if(opr == "createNewSpecimen")
				{
					top.location.href="<%=request.getContextPath()%>/NewSpecimen.do?operation=add&pageOf=pageOfNewSpecimen&menuSelected=15&virtualLocated=true&showConsents=yes&tableId4=disable";
				}
				else
				{
					if(opr == "afterAdd")
					{
						top.location.href="<%=request.getContextPath()%>/LoadSpecimenCollGroupPage.do?operation=edit&id=<%=id%>";
					}
					else
					{
						if((opr == 'null')||(opr=="add"))
						{	
							initiallizeAddSCGTabs('<%=request.getContextPath()%>','<%=str%>');
						}
						else
						{
							initiallizeEditSCGTabs('<%=request.getContextPath()%>','<%=queryString%>','<%=participantAnnotationsQueryString%>');
						}
					}
				}
			}
</script>
</body>
</html>