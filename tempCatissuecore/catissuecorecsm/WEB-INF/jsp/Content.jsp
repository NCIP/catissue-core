<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<title>Content</title>
<s:head theme="ajax" debug="true" />
<link rel="stylesheet" type="text/css" href="styleSheet.css" />
<script src="script.js" type="text/javascript"></script>
<script>
	<!-- transport: "XMLHTTPTransport" -->
    function treeNodeSelected(nodeId) {
        dojo.io.bind({
            url: "<s:url value='Criteria.action' />?nodeId="+nodeId,
            load: function(type, data, evt) {
                var displayDiv = dojo.byId("displayId");
                displayDiv.innerHTML = data
            },
            mimeType: "text/html"
        });
    };

    dojo.event.topic.subscribe("treeSelected", this, "treeNodeSelected");
</script>
</head>
<body>
<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">

<%@ include file="include/header.inc" %>	
	
  <tr>
    <td height="100%" align="center" valign="top">
      <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%" width="771">

<%@ include file="include/applicationHeader.inc" %>

        <tr>
          <td valign="top">
            <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%" width="100%">
              <tr>
                <td height="20" class="mainMenu">
                
					<!-- main menu begins -->
					<table summary="" cellpadding="0" cellspacing="0" border="0" height="20">
					  <tr>
					    <td width="1"><!-- anchor to skip main menu --><a href="#content"><img src="images/shim.gif" alt="Skip Menu" width="1" height="1" border="0" /></a></td>
						<!-- link 1 begins -->
					    <td height="20" class="mainMenuItem" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()" onclick="document.location.href='Home.action'">
					      <a class="mainMenuLink" href="Home.action">HOME</a>
					    </td>
					    <!-- link 1 ends -->
					    <td><img src="images/mainMenuSeparator.gif" width="1" height="16" alt="" /></td>
					    <!-- link 2 begins -->
					    <td height="20" class="mainMenuItemOver" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'mainMenuItemOver'),hideCursor()" onclick="document.location.href='ShowDynamicTree.action'">
					      <a class="mainMenuLink" href="ShowDynamicTree.action">CRITERIA</a>
					    </td>
					    <!-- link 2 ends -->
					    <td><img src="images/mainMenuSeparator.gif" width="1" height="16" alt="" /></td>
					  </tr>
					</table>
					<!-- main menu ends -->
                  
                </td>
              </tr>
              
<!--_____ main content begins _____-->
              <tr>
                <td valign="top">
                  <!-- target of anchor to skip menus --><a name="content" />
                  <table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%" height="100%">
										<tr>
											<td valign="top">
												<table cellpadding="0" cellspacing="0" border="1" bordercolor="white" class="contentBegins">
													<tr>
														<td colspan="2">
															<h2>Example caBIO Domain Class Browser</h2>
															<h3>Please click on any of the tree nodes.</h3>
															<p>To view the search criteria for a class, expand a package listed below and select a class.  To search for records, provide valid search criteria and click the Submit button.  For any date attributes, please use the syntax: mm-dd-yyyy.</p> 
														</td>
													</tr>
													<tr>
														<td valign="top" style="border:0px; border-right:1px; border-style:solid; border-color:black;">
															<div style="width: 300px; float:left; margin: 7px; overflow: auto;">
															<s:tree 
															    theme="ajax"
															    rootNode="%{classTreeRootNode}" 
															    childCollectionProperty="children" 
															    nodeIdProperty="id"
															    nodeTitleProperty="name"
															    treeSelectedTopic="treeSelected">
															</s:tree> 
															</div>														
														</td>
														<td valign="top">
															<div id="displayId" style="width: 450px; float:left; margin: 7px; overflow: auto;">
																&nbsp;
															</div>														
														</td>														
													</tr>														
												</table>
											</td>
										</tr>
									</table>
                </td>
              </tr>
<!--_____ main content ends _____-->
              
              <tr>
                <td height="20" width="100%" class="footerMenu">
                
                  <!-- application ftr begins -->
<%@ include file="include/applicationFooter.inc" %>
                  <!-- application ftr ends -->
                  
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td>
    
<%@ include file="include/footer.inc" %>
    
    </td>
  </tr>
</table>
</body>
</html>
