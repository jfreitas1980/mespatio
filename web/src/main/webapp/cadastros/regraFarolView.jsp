<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>

<html>
<head>
<title></title>
<link rel="StyleSheet" href="../css/visualIdentity.css" type="text/css" />
<script type="text/javascript" src="../js/buttons.js"></script>
<script type="text/javascript" src="../js/funcoes.js"></script>
</head>
<body class="body_background">
<f:view>
	<f:loadBundle basename="messages" var="msg" />
	<h:form prependId="false">
		<table cellspacing="0" border="0" class="boxCreateEdit">
			<tr>
				<td class="iFrameTitle" colspan="2"><h:outputLabel
					value="#{msg.regraFarolView_titulo}" /></td>
			</tr>
			<tr>
				<td colspan="2"><rich:messages layout="table"
					infoLabelClass="hMessage" errorLabelClass="hMessage"
					styleClass="hMessage">
					<f:facet name="infoMaker">
						<h:graphicImage value="../imagens/accept.png" />
					</f:facet>
					<f:facet name="errorMaker">
						<h:graphicImage value="../imagens/error.png" />
					</f:facet>
				</rich:messages></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow"
					style="padding-top: 21px; padding-bottom: 4px;" colspan="2"><h:outputLabel
					value="#{msg.regraFarolView_direcaocontabilizacao}: *" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" style="padding-bottom: 8px;"
					colspan="2">
					<h:selectOneMenu id="valorRegraFarol" label="#{msg.regraFarolView_direcaocontabilizacao}"
						onchange="#{regraFarolController.regraFarol.valorRegraFarol}"
						converter="valorRegraFarolEnumConversor"
						required="true"
						value="#{regraFarolController.regraFarol.valorRegraFarol}">
						<f:selectItems value="#{regraFarolController.valorList}" />
					</h:selectOneMenu>
				</td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" colspan="2">
					<h:outputLabel value="#{msg.regraFarolView_tipoproduto}:" />
				</td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" style="padding-bottom: 8px;"
					colspan="2">				
					<h:selectOneMenu id="tipoProdutoRegraFarol" label="#{msg.regraFarolView_tipoproduto}"
						onchange="#{regraFarolController.regraFarol.tipoProduto}"
						converter="tipoProdutoConversor"
						value="#{regraFarolController.regraFarol.tipoProduto}">
						<f:selectItems value="#{regraFarolController.tipoProdutoList}" />
					</h:selectOneMenu>
				</td>
			</tr>

			<tr>
				<td class="boxCreateEditRow" colspan="2"
					style="padding-bottom: 11px; padding-top: 8px;" align="center">
				<table cellspacing="0" border="0">
					<tr>
						<td><h:commandLink
							action="#{regraFarolController.salvarRegraFarol}">
							<div class="customButton"
								onmouseover="defineButtonStyleOver(this);"
								onmouseout="defineButtonStyleOut(this);"><h:outputLabel
								value="#{msg.button_save}" /></div>
						</h:commandLink></td>
						<td style="padding-left: 12px;"><h:commandLink
							action="cancelarRegraFarol" immediate="true">
							<div class="customButton"
								onmouseover="defineButtonStyleOver(this);"
								onmouseout="defineButtonStyleOut(this);"><h:outputLabel
								value="#{msg.button_cancel}" /></div>
						</h:commandLink></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
	</h:form>
</f:view>
</body>
</html>