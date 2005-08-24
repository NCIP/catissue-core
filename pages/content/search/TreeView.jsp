<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<% 	
	String pageOf = request.getParameter(Constants.PAGEOF);
	String storageContainerType = null;
	String propertyName = null;
	if (!pageOf.equals(Constants.PAGEOF_TISSUE_SITE))
		storageContainerType = (String)request.getAttribute(Constants.STORAGE_CONTAINER_TYPE);
	else
		propertyName = request.getParameter(Constants.PROPERTY_NAME);
		

%>

<script language="javascript">
	function closeWindow()
	{
		window.close();
	}

	function setParentWindowValue(elementName,elementValue)
	{ 
		for (var i=0;i < opener.document.forms[0].elements.length;i++)
	    {
	    	if (opener.document.forms[0].elements[i].name == elementName)
			{
				opener.document.forms[0].elements[i].value = elementValue;
			}
	    }
	}
</script>

<OBJECT classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"
    width="180" height="300" align="top" name="<%=Constants.TREE_APPLET_NAME%>"
	codebase="http://java.sun.com/products/plugin/autodl/jinstall-1_3_0-win.cab" MAYSCRIPT>
    <PARAM name="code" value="<%=Constants.QUERY_TREE_APPLET%>">
	<PARAM name="archive" value="Applet/QueryTree.jar">
	<PARAM name="type" value="application/x-java-applet;version=1.3">
	<PARAM name="<%=Constants.PAGEOF%>" value="<%=pageOf%>">
	<PARAM name="<%=Constants.STORAGE_CONTAINER_TYPE%>" value="<%=storageContainerType%>">
	<PARAM name="name" value="<%=Constants.TREE_APPLET_NAME%>">
	<PARAM name="<%=Constants.PROPERTY_NAME%>" value="<%=propertyName%>">
	<COMMENT>
		<EMBED type="application/x-java-applet;version=1.3" width="180"
			height="300" code="<%=Constants.QUERY_TREE_APPLET%>"
			codebase="<%=Constants.APPLET_CODEBASE%>"
			pluginspage="http://java.sun.com/products/plugin/autodl/jinstall-1_3_0-win.cab"
			archive="Applet/QueryTree.jar"
			pageOf="<%=pageOf%>"
			storageType="<%=storageContainerType%>"
			propertyName="<%=propertyName%>"
			name="<%=Constants.TREE_APPLET_NAME%>" MAYSCRIPT>
			
			<NOEMBED>
				No Java 2 SDK support for APPLET!!
					<a href="http://java.sun.com/products/plugin/">
						<code>http://java.sun.com/products/plugin/</code>
					</a>
			</NOEMBED>
		</EMBED>
	</COMMENT>
</OBJECT>