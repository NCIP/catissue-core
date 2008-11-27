<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
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
       if(navigator.appName.indexOf("Microsoft")!=-1)
		{
			interfaceObj = window.document.getElementById("Layout");
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
       
	//	alert("Node id is:"+nodeId);
        jsReady = true;
		callFlexMethod();
		
		//Invoking the Action script method passing the Selected Tree node Id 
		interfaceObj.myFlexMethod(nodeId);
	//	interfaceObj.initTree();
	}
	function refreshCpParticipants(participantId)
	{
        
		 callFlexMethod();
	     interfaceObj.editParticipant(participantId,"");

	}

	/**
	Added treeNode so that on node from tree is selected 
	*/
	function editParticipant(participantId,treeNodeId)
	{
		 callFlexMethod();
	     interfaceObj.editParticipant(participantId,treeNodeId);
	
	}

	function onParticipantClick(pId,cpId,refParticipants)
	{
	  window.parent.frames[1].location ="QueryParticipantSearch.do?pageOf=pageOfParticipantCPQueryEdit&operation=edit&<%=Constants.CP_SEARCH_CP_ID%>="+cpId+"&id="+pId;
	}
  
    /*
       To register a participant for a CP
	*/
	function RegisterParticipants(cpId,refParticipants)
	{
      
		if(cpId == "-1")
		{
				alert("please select collection protocol.");
		}    
        else
		{
			window.parent.frames[1].location = "QueryParticipant.do?operation=add&pageOf=<%=Constants.PAGE_OF_PARTICIPANT_CP_QUERY%>&clearConsentSession=true&<%=Constants.CP_SEARCH_CP_ID%>="+cpId+"&refresh="+refParticipants;
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
			 var nameIndex1 = name.indexOf("@");
			  var name1 = "";
			  var evtDate = "";
				if(nameIndex1 != -1)
				{
					name1 = name.substring(0,nameIndex1);
				  	evtDate = name.substring(nameIndex1+1);
		    
				}	
			<%if(access != null && access.equals("Denied"))
			{%>
			
			window.parent.frames[1].location = "CPQuerySpecimenCollectionGroupForTech.do?pageOf=pageOfSpecimenCollectionGroupCPQuery&operation=edit&id="+id1+"&name="+name1+"&evtDate="+evtDate;
			<%}else {%>
				if(isFuture != "")
				{
				   var ind = isFuture.indexOf(":");
				   var eventId = isFuture.substring(0,ind);
                 	
					window.parent.frames[1].location = "QuerySpecimenCollectionGroupSearch.do?pageOf=pageOfSpecimenCollectionGroupAdd&operation=add&id="+id1+"&<%=Constants.CP_SEARCH_PARTICIPANT_ID%>="+pId+"&<%=Constants.CP_SEARCH_CP_ID%>="+cpId+"&<%=Constants.QUERY_RESULTS_COLLECTION_PROTOCOL_EVENT_ID%>="+eventId+"&clickedNodeId="+id+"&evtDate="+evtDate;
                 }else
				 {
					
					 window.parent.frames[1].location = "QuerySpecimenCollectionGroupSearch.do?pageOf=pageOfSpecimenCollectionGroupCPQueryEdit&refresh=false&operation=edit&id="+id1+"&<%=Constants.CP_SEARCH_PARTICIPANT_ID%>="+pId+"&<%=Constants.CP_SEARCH_CP_ID%>="+cpId+"&clickedNodeId="+id+"&evtDate="+evtDate;
                  }
			<%}%>
		}
		else
		{
			if(obj1 == "<%=Constants.SUB_COLLECTION_PROTOCOL%>")
			{
			  var nameIndex = name.indexOf(":");
			  var parentCPId = "";
			  var regDate = "";
				if(nameIndex != -1)
				{
					parentCPId = name.substring(0,nameIndex);
				  	regDate = name.substring(nameIndex+1);
		    
				}	
				 window.parent.frames[1].location = "CPQuerySubCollectionProtocolRegistration.do?pageOf=pageOfCollectionProtocolRegistrationCPQuery&refresh=false&operation=add&<%=Constants.CP_SEARCH_PARTICIPANT_ID%>="+pId+"&<%=Constants.CP_SEARCH_CP_ID%>="+cpId+"&participantId="+pId+"&clickedNodeId="+id+"&regDate="+regDate+"&parentCPId="+parentCPId;
			}
		
		else
		{
         
		   window.parent.frames[1].location = "QuerySpecimenSearch.do?pageOf=pageOfNewSpecimenCPQuery&operation=edit&id="+id1+"&refresh=false&<%=Constants.CP_SEARCH_PARTICIPANT_ID%>="+pId+"&<%=Constants.CP_SEARCH_CP_ID%>="+cpId;
		}
		}
	};
 </script> 

</head>
<body onLoad="f1()">
<%@ include file="/pages/content/common/ActionErrors.jsp" %>   
 <object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" id="Layout" width="100%" height="100%">
		<param name=movie value="flexclient/biospecimen/Layout.swf"> 
		<param name=quality value=high>
		<param name="swliveconnect" value="true">
		<param name="wmode" value="transparent" />				 
		<embed src="flexclient/biospecimen/Layout.swf" quality="high" bgcolor="#ffffff"
			width="100%" height="100%" id="Layout" name="Layout" align="middle"
			play="true"
			loop="false"
			quality="high"
			wmode="transparent"
			allowScriptAccess="sameDomain"
			type="application/x-shockwave-flash"
			pluginspage="http://www.adobe.com/go/getflashplayer">
		</embed>
   
   </object>
   <script>
   function f1()
   {
	var t=setTimeout("f2()",100);
   }
   function f2()
   {
	   var t=setTimeout("window.resizeBy(-1,-1)",100);
	   var t=setTimeout("window.resizeBy(1,1)",100);
   }
   </script>
   <!-- <input type="hidden" name="participantId" id="participantId"/>
	<input type="hidden" name="cpId" id="cpId"/>
    -->
</body>
</html>