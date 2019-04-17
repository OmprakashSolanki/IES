<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">

<title>IES Application Registration</title>
<style>
.error {
	color: #FF0000
}
</style>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">

<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script
	src="https://cdn.jsdelivr.net/jquery.validation/1.16.0/jquery.validate.min.js"></script>
<script>
	$(function() {
		$('form[id="appRegForm"]').validate({
			rules : {
				firstName : 'required',
				lastName : 'required',
				email : {
					email : true,
				},
				
				dob : 'required',
				gender : 'required',
				ssn : 'required'
			},
			messages : {
				firstName : 'Please enter first name',
				lastName : 'please enter last name',
				email : 'Please enter a valid email',
				
				dob : 'Please select dob',
				gender : 'Please select Gender',
				phno : 'Please enter Phno',
				ssn : 'Please enter SSN'
			},
			submitHandler : function(form) {
				form.submit();
			}
		});

		$("#ssn").blur(function() {
			var enteredSsn = $("#ssn").val();
			$.ajax({
				url : window.location + "/validateSsn",
				data : "ssn=" + enteredSsn,
				success : function(result) {
					if (result == 'Duplicate') {
						$("#ssnMsg").html("SSN already registered.!!");
						$("#ssn").focus();
					} else {
						$("#ssnMsg").html("");
					}
				}
			});
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
<%@ include file="header-inner.jsp"%><br />
<body>

	<font color='green'>${successMsg}</font>
	<font color='red'>${failureMsg}</font>

	<h2> IES Application Registration</h2>
	<form:form action="appReg" method="POST" modelAttribute="appModel"
		id="appRegForm">
		<table>
			<tr>
				<td>First Name</td>
				<td><form:input path="firstName" /></td>
			</tr>
			<tr>
				<td>Last Name</td>
				<td><form:input path="lastName" /></td>
			</tr>
			<tr>
				<td>Date Of Birth:</td>
				<td><form:input path="dob" id="datepicker" /></td>
			</tr>
			<tr>
				<td>Gender:</td>
				<td><form:radiobuttons path="gender" items="${gendersList}" /></td>
			</tr>

			<tr>
				<td>SSN:</td>
				<td><form:input path="ssn" id="ssn" /></td>
				<td><font color='red'><span id="ssnMsg"></span></font></td>
				
			</tr>

			<tr>
				<td>Phno:</td>
				<td><form:input path="phno" /></td>
			</tr>
			<tr>
				<td>Email</td>
				<td><form:input path="email"  /></td>
				<td><font color='red'><span id="emailMsg"></span></font></td>
			</tr>

			<tr>
				<td><input type="reset" value="Reset" /></td>
				<td><input type="Submit" value="Create" /></td>
			</tr>

		</table>


	</form:form>

</body>
</html>
