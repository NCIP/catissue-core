<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ include file="/pages/content/common/CollectionProtocolCommon.jsp" %>
<%@ page import="java.util.*"%>

<%
   String participantId = null;
   String cpId = null;
   String access = null;
   access = (String)session.getAttribute("Access");
%>
 
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
  <script language="JavaScript">
    
	//Added By Baljeet For flex
	var interfaceObj;
    var jsReady = false;
	function callFlexMethod()
	{
       if(navigator.appName.indexOf("microsoft")!=-1)
		{
			interfaceObj = window["Layout"];
		}
		else
		{
			interfaceObj = document["Layout"];
		}
	}
	
	// called to check if the page has initialized and JavaScript is available, called by flex
	function isReady() 
	{
		
		return jsReady;
	}

	

	function pageInit(nodeId)
	{
       
		//alert("Node id is:"+nodeId);
        jsReady = true;
		callFlexMethod();
		
		//Invoking the Action script method passing the Selected Tree node Id 
		interfaceObj.myFlexMethod(nodeId);
	}
	function refreshCpParticipants()
	{
         callFlexMethod();
	     interfaceObj.refreshParticipants();
	}
	function onParticipantClick(pId,cpId)
	{
      window.parent.frames[1].location ="QueryParticipantSearch.do?pageOf=pageOfParticipantCPQueryEdit&operation=edit&<%=Constants.CP_SEARCH_CP_ID%>="+cpId+"&id="+pId;
	 
	}
  
    /*
       To register a participant for a CP
	*/
	function RegisterParticipants(cpId)
	{
        
		if(cpId == "-1")
		{
				alert("please select collection protocol.");
		}    
        else
		{
			window.parent.frames[1].location = "QueryParticipant.do?operation=add&pageOf=<%=Constants.PAGE_OF_PARTICIPANT_CP_QUERY%>&clearConsentSession=true&<%=Constants.CP_SEARCH_CP_ID%>="+cpId;
			
		} 

    }

    /*
       This method is invoked on click of a tree node
	*/
	function tonclick(id, cpId, pId, name)
	{
        var index = id.indexOf(":");
		var isFuture = "";
		
		if(index != -1)
		{
			isFuture = id.substring(index+1);
		  	var str = id.substring(0,index);
		    
		}
		else
		{
			var str = id;
		}    

        var i = str.indexOf('_');
		var obj1 = str.substring(0,i);
		
		var id1 = str.substring(i+1);
		
		if(obj1 == "<%=Constants.SPECIMEN_COLLECTION_GROUP%>")
		{

			<%if(access != null && access.equals("Denied"))
			{%>
			
			window.parent.frames[1].location = "CPQuerySpecimenCollectionGroupForTech.do?pageOf=pageOfSpecimenCollectionGroupCPQuery&operation=edit&id="+id1+"&name="+name;
			<%}else {%>
				if(isFuture != "")
				{
				   var ind = isFuture.indexOf(":");
				   var eventId = isFuture.substring(0,ind);
                 	
					window.parent.frames[1].location = "QuerySpecimenCollectionGroupSearch.do?pageOf=pageOfSpecimenCollectionGroupAdd&operation=add&id="+id1+"&<%=Constants.CP_SEARCH_PARTICIPANT_ID%>="+pId+"&<%=Constants.CP_SEARCH_CP_ID%>="+cpId+"&<%=Constants.QUERY_RESULTS_COLLECTION_PROTOCOL_EVENT_ID%>="+eventId+"&clickedNodeId="+id;
                 }else
				 {
					 window.parent.frames[1].location = "QuerySpecimenCollectionGroupSearch.do?pageOf=pageOfSpecimenCollectionGroupCPQueryEdit&operation=edit&id="+id1+"&<%=Constants.CP_SEARCH_PARTICIPANT_ID%>="+pId+"&<%=Constants.CP_SEARCH_CP_ID%>="+cpId+"&clickedNodeId="+id;
                  }
			<%}%>
		}
		else
		{
           window.parent.frames[1].location = "QuerySpecimenSearch.do?pageOf=pageOfNewSpecimenCPQuery&operation=edit&id="+id1+"&<%=Constants.CP_SEARCH_PARTICIPANT_ID%>="+pId+"&<%=Constants.CP_SEARCH_CP_ID%>="+cpId;
		}
	
	};
 </script> 

</head>
<body>
   <html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	
   </html:messages>

   <html:errors/>
   
 <object>
           <embed src="flexclient/biospecimen/Layout.swf" quality="high" bgcolor="#ffffff"
				width="100%" height="100%" name="Layout" align="middle"
				play="true"
				loop="false"
				quality="high"
				allowScriptAccess="sameDomain"
				type="application/x-shockwave-flash"
				pluginspage="http://www.adobe.com/go/getflashplayer">
			</embed>
   
   </object>
   <!-- <input type="hidden" name="participantId" id="participantId"/>
	<input type="hidden" name="cpId" id="cpId"/>
    -->
</body>
</html>