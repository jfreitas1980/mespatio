<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<link rel="StyleSheet" href="css/visualIdentity.css" type="text/css" />
<link rel="StyleSheet" href="css/panel.css" type="text/css" />
<script type="text/javascript" src="css/buttons.js"></script>

<body class="body_background">
<f:view>
<f:loadBundle var="msg" basename="messages" />
	<h:form id="formView">

		<table cellspacing="0" border="0" class="boxCreateEdit">
			<tr>
				<td class="iFrameTitle"><h:outputLabel
					value="#{msg.view_login}" /></td>
			</tr>

			<tr>
				<td><h:messages layout="table" styleClass="hMessage" /></td>
			</tr>

			<tr>
				<td class="boxCreateEditRow"
					style="padding-top: 21px; padding-bottom: 4px;"><h:outputLabel
					value="#{msg.loginMsg}" /></td>
			</tr>

			<tr>
				<td class="boxCreateEditRow" style="padding-bottom: 8px;"><h:inputText
					value="#{loginControllerBean.login}" required="true"
					requiredMessage="Field login is required" /></td>
			</tr>

			<tr>
				<td class="boxCreateEditRow"
					style="padding-top: 21px; padding-bottom: 4px;"><h:outputLabel
					value="#{msg.passwordMsg}" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" style="padding-bottom: 8px;"><h:inputSecret
					value="#{loginControllerBean.password}"
					requiredMessage="Field password is required">
				</h:inputSecret></td>
			</tr>
			
			<tr>
				<td class="boxCreateEditRow"
					 align="right">
				<table cellspacing="0" border="0">
					<tr>
						<h:commandLink action="#{loginControllerBean.loginUser}">
							<div class="customButton"
								onmouseover="defineButtonStyleOver(this);"
								onmouseout="defineButtonStyleOut(this);"><h:outputLabel
								value="#{msg.button_ok}" /></div>

						</h:commandLink>
					</tr>
				</table>
				</td>
			</tr>
		</table>
	</h:form>
</f:view>
</body>
</html>