<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>
<html>
<head>
<title></title>
<link rel="StyleSheet" href="../css/visualIdentity.css" type="text/css" />
<link rel="StyleSheet" href="../css/panel.css" type="text/css" />
<script type="text/javascript" src="../js/buttons.js"></script>
<script type="text/javascript" src="../js/funcoes.js"></script>
</head>
<body class="body_background">
<f:view>
	<f:loadBundle basename="messages" var="msg" />
	<h:form>
		<table cellspacing="0" border="0" class="boxCreateEdit">
			<tr>
				<td class="iFrameTitle" colspan="2"><h:outputLabel
					value="#{msg.maquinaView_titulo}" /></td>
			</tr>
			<tr>
				<td colspan="2"><rich:messages layout="table"
					infoLabelClass="hMessage" errorLabelClass="hMessage">
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
					value="#{msg.maquinaView_nome}: *" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" style="padding-bottom: 8px;"
					colspan="2"><h:inputText id="NomeMaquina"
					label="#{msg.maquinaView_nome}"
					value="#{maquinaDoPatioController.maquina.nomeMaquina}"
					required="true" size="60" maxlength="60" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" colspan="2"><h:outputLabel
					value="#{msg.maquinaView_estadodeoperacao}: *" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" style="padding-bottom: 8px;"
					colspan="2"><h:selectOneMenu id="estadoOperacao"
					label="#{msg.maquinaView_estadodeoperacao}"
					onchange="#{maquinaDoPatioController.maquina.estado}"
					required="true" converter="estadoMaquinaEnumConversor"
					value="#{maquinaDoPatioController.maquina.estado}">
					<f:selectItems
						value="#{maquinaDoPatioController.estadoMaquinaList}" />
				</h:selectOneMenu></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" colspan="2"><h:outputLabel
					value="#{msg.maquinaView_taxadeoperacao}: *" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" style="padding-bottom: 8px;"
					colspan="2"><h:inputText id="taxaOperacao"
					label="#{msg.maquinaView_taxadeoperacao}"
					value="#{maquinaDoPatioController.maquina.taxaDeOperacaoNominal}"
					required="true" size="10" maxlength="10">
					<f:validateDoubleRange minimum="#{msg.maquinaView_taxaMinima}"
						maximum="#{msg.maquinaView_taxaMaxima}" />
					<f:convertNumber minFractionDigits="0" maxFractionDigits="2"
						pattern="#######.##" />
				</h:inputText></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" colspan="2"><h:outputLabel
					value="#{msg.maquinaView_proximamanutencao}: *" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" style="padding-bottom: 8px;"
					colspan="2"><rich:calendar id="dataManutencao"
					label="#{msg.maquinaView_proximamanutencao}"
					datePattern="#{msg.dateFormat}" required="true"
					converter="dataConversor"
					value="#{maquinaDoPatioController.maquina.proximaManutencao}" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" colspan="2"><h:outputLabel
					value="#{msg.maquinaView_posicao}:" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow"><h:selectBooleanCheckbox
					label="#{msg.maquinaView_giralanca}" id="Giralanca"
					styleClass="checkbox"
					value="#{maquinaDoPatioController.maquina.giraLanca}" /> <h:outputText
					value="#{msg.maquinaView_giralanca}" /></td>
				<td class="boxCreateEditRow"><h:selectBooleanCheckbox
					label="#{msg.maquinaView_bracopatio}" id="BracoNoPatio"
					styleClass="checkbox" disabled="true"
					value="#{maquinaDoPatioController.maquina.bracoNoPatioInferior}" />
				<h:outputText value="#{msg.maquinaView_bracopatio}" /></td>
			</tr>

			<tr>
				<td class="boxCreateEditRow" style="padding-bottom: 8px;"
					colspan="2"><h:selectOneMenu id="Posicao"
					label="#{msg.maquinaView_posicao}"
					onchange="#{maquinaDoPatioController.maquina.posicao}"
					converter="balizaConversor"
					value="#{maquinaDoPatioController.maquina.posicao}">
					<f:selectItems value="#{maquinaDoPatioController.posicaoList}" />
				</h:selectOneMenu></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" colspan="2"><h:outputLabel
					value="#{msg.maquinaView_velocidade}: *" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" style="padding-bottom: 8px;"
					colspan="2"><h:inputText id="VelocidadeDeslocamento"
					label="#{msg.maquinaView_velocidade}"
					value="#{maquinaDoPatioController.maquina.velocidadeDeslocamento}"
					required="true" size="10" maxlength="10">
					<f:validateDoubleRange
						minimum="#{msg.maquinaView_velocidadeMinima}"
						maximum="#{msg.maquinaView_velocidadeMaxima}" />
					<f:convertNumber minFractionDigits="0" maxFractionDigits="2"
						pattern="#######.##" />
				</h:inputText></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" colspan="2"><h:outputLabel
					value="#{msg.maquinaView_tipomaquina}: *" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" style="padding-bottom: 8px;"
					colspan="2"><h:selectOneMenu id="TipoMaquina"
					label="#{msg.maquinaView_tipomaquina}"
					onchange="#{maquinaDoPatioController.maquina.tipoDaMaquina}"
					required="true" converter="tipoMaquinaEnumConversor"
					value="#{maquinaDoPatioController.maquina.tipoDaMaquina}">
					<f:selectItems
						value="#{maquinaDoPatioController.tipoDaMaquinaList}" />
				</h:selectOneMenu></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" colspan="2"><h:outputLabel
					value="#{msg.maquinaView_pimsestado}:" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" style="padding-bottom: 8px;"
					colspan="2"><h:inputText id="PIMSEstado"
					label="#{msg.maquinaView_pimsestado}"
					value="#{maquinaDoPatioController.maquina.tagPimsEstado}"
					size="255" maxlength="255" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" colspan="2"><h:outputLabel
					value="#{msg.maquinaView_pimsposicionamento}:" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" style="padding-bottom: 8px;"
					colspan="2"><h:inputText id="PIMSPosicionamento"
					label="#{msg.maquinaView_pimsposicionamento}"
					value="#{maquinaDoPatioController.maquina.tagPimsPosicionamento}"
					size="255" maxlength="255" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" colspan="2"><h:outputLabel
					value="#{msg.maquinaView_pimsangulolateral}:" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" style="padding-bottom: 8px;"
					colspan="2"><h:inputText id="PIMSAnguloLateral"
					label="#{msg.maquinaView_pimsangulolateral}"
					value="#{maquinaDoPatioController.maquina.tagPimsAnguloLaterialLanca}"
					size="255" maxlength="255" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" colspan="2"><h:outputLabel
					value="#{msg.maquinaView_pimsanguloaltura}:" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" style="padding-bottom: 8px;"
					colspan="2"><h:inputText id="PIMSAnguloALtura"
					label="#{msg.maquinaView_pimsanguloaltura}"
					value="#{maquinaDoPatioController.maquina.tagPimsAnguloAlturaLanca}"
					size="255" maxlength="255" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" colspan="2"
					style="padding-bottom: 11px; padding-top: 8px;" align="center">
				<table cellspacing="0" border="0">
					<tr>
						<td><h:commandLink
							action="#{maquinaDoPatioController.gravaMaquina}">
							<div class="customButton"
								onmouseover="defineButtonStyleOver(this);"
								onmouseout="defineButtonStyleOut(this);"><h:outputLabel
								value="#{msg.button_confirm}" /></div>
						</h:commandLink></td>
						<td style="padding-left: 12px;"><h:commandLink
							action="#{maquinaDoPatioController.cancelaMaquina}"
							immediate="true">
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
	</center>
</f:view>
</body>
</html>