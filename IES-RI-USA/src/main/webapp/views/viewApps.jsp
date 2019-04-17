
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>View Applicants</title>
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css">

<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script
	src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$('#accTbl').DataTable({
			"pagingType" : "full_numbers"
		});
	});
</script>

</head>

<%@ include file="header-inner.jsp" %>
<body>
	<h2>View Accounts</h2>

	<font color='red'>${failure}</font>
	<font color='green'>${success}</font>

	<table border="1" id="accTbl">
		<thead>
			<tr>
				<td>S.No</td>
				<td>First Name</td>
				<td>Last Name</td>
				<td>Date of Birth</td>
				<td>SSN</td>
				<td>Action</td>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${appModel}" var="app" varStatus="index">
				<tr>
					<td><c:out value="${index.count}" /></td>
					<td><c:out value="${app.firstName}" /></td>
					<td><c:out value="${app.lastName}" /></td>
					<td><c:out value="${app.dob}" /></td>
					<td><c:out value="${app.ssn}" /></td>

					<td><a href="editApp?appId=${app.appId}">Edit</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>

   