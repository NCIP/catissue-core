<%@ page language="java" contentType="text/html" %>

<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>


<html>

<body>
	<table>
		<tr>
			<td>
				<c:out value="Search String : ${titliSearchForm.searchString}" />
			</td>
			
		</tr>
		<tr>
			<table border="2">
				<tr>
					<th>Database</th>
					<th>Table</th>
					<th>Unique Key</th>
				</tr>
						
				<c:forEach items="${titliSearchForm.resultList}" var="match" >
					
					<tr>
						<td>
							<c:out value="${match.databaseName}"  />
						</td>	
						<td>
							<c:out value="${match.tableName}"  />
						</td>						
						<td>
							<c:out value="${match.uniqueKeys}"  />
						</td>						
					</tr>
				</c:forEach>
				
			</table>
		
		</tr>
			
	</table>

</body>

</html>