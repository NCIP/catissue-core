<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<% 	
	String pageOf = request.getParameter(Constants.PAGEOF);
	String storageContainerType = (String)request.getAttribute(Constants.STORAGE_CONTAINER_TYPE);
%>

<OBJECT classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"
    width="180" height="300" align="top"
	codebase="http://java.sun.com/products/plugin/autodl/jinstall-1_3_0-win.cab">
    <PARAM name="code" value="<%=Constants.QUERY_TREE_APPLET%>">
	<PARAM name="archive" value="Applet/QueryTree.jar">
	<PARAM name="type" value="application/x-java-applet;version=1.3">
	<PARAM name="<%=Constants.PAGEOF%>" value="<%=pageOf%>">
	<PARAM name="<%=Constants.STORAGE_CONTAINER_TYPE%>" value="<%=storageContainerType%>">
	<COMMENT>
		<EMBED type="application/x-java-applet;version=1.3" width="180"
			height="300" code="<%=Constants.QUERY_TREE_APPLET%>"
			codebase="<%=Constants.APPLET_CODEBASE%>"
			pluginspage="http://java.sun.com/products/plugin/autodl/jinstall-1_3_0-win.cab"
			archive="Applet/QueryTree.jar"
			pageOf="<%=pageOf%>"
			storageType="<%=storageContainerType%>">
			
			<NOEMBED>
				No Java 2 SDK support for APPLET!!
					<a href="http://java.sun.com/products/plugin/">
						<code>http://java.sun.com/products/plugin/</code>
					</a>
			</NOEMBED>
		</EMBED>
	</COMMENT>
</OBJECT>
