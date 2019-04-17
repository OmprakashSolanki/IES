<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">

<title>Edit Applicant Details</title>
<style>
.error {
	color: #FF0000
}
</style>
<link rel="stylesheet" type="text/css" href="css/EditAcc.css">
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">

<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script
	src="https://cdn.jsdelivr.net/jquery.validation/1.16.0/jquery.validate.min.js"></script>
<script>
	$(function() {
		$('form[id="editAppForm"]').validate({
			rules : {
				firstName : 'required',
				lastName : 'required',
				dob : 'required',
				gender : 'required',
				ssn : 'required'
			},
			messages : {
				firstName : 'Please enter first name',
				lastName : 'please enter last name',
				dob : 'Please select dob',
				gender : 'Please select Gender',
				ssn : 'Please enter SSN'
			},
			submitHandler : function(form) {
				form.submit();
			}
		});
		$("#datepicker").datepicker({
			changeMonth : true,
			changeYear : true,
			maxDate : new Date(),
			dateFormat : 'mm-dd-yy'
		});
	});
</script>
</head>
<%@ include file="header-inner.jsp"%>

<h3 style="color: green; text-align: center">${successMsg}</h3>
<h3 style="color: red; text-align: center">${failureMsg}</h3>
<h1>Edit Accounts</h1>
<table>
	<form:form action="editApp" method="POST" modelAttribute="appModel"
		id="editAppForm">
		<tr>
			<td>Application Id</td>
			<td><form:input path="appId" id="appno" readonly="true" /></td>
		</tr>
		
		<tr>
			<td>First Name</td>
			<td><form:input path="firstName" id="fname" /></td>
		</tr>
		<tr>
			<td>LastName</td>
			<td><form:input path="lastName" id="lname" /></td>
		</tr>
			<tr>
			<td>Date Of Birth :</td>
			<td><form:input path="dob" id="datepicker" /></td>
		</tr>
			<tr>
		<td>Gender :</td>
			<td><form:radiobuttons path="gender" items="${gendersList}" /></td>
		</tr>
			<tr>
		<td>SSN NO :</td>
			<td><form:input path="ssn" id="ssn" /></td>
		</tr>
			<tr>
		<td>Phone Number :</td>
			<td><form:input path="phno" id="phno" /></td>
		</tr>
			<tr>
		<td>Email :</td>
			<td><form:input path="email" id="email" readonly="true" /></td>
			<td>
		<span id="emailMsg" style="color: red"></span>
		</td></tr>
		
			<form:hidden path="createDate" />
			<form:hidden path="updateDate" />
			<tr>
			<td>
			<input type="submit" value="Update" id="updateAppBtn" /><td>
		</tr>
	
		</form:form>
		</table>

</body>
</html>