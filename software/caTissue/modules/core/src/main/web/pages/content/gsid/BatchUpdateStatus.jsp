<%@ page language="java" isELIgnored="false"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%
double percent=(Double)request.getAttribute("statusPercentage");
boolean error=(Boolean)request.getAttribute("statusError"); 
out.print("<div><span id='percentage'>"+(int)percent+"</span>");
out.print("<span id='error'>"+error+"</span></div>");
%>