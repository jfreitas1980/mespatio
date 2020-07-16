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
		<table cellspacing="0" border="0" class="boxCreateEdit" width="490">
			<tr>
				<td class="iFrameTitle"><h:outputLabel
					value="#{msg.correiaView_titulo}" /></td>
			</tr>
			<tr>
				<td><rich:messages layout="table" infoLabelClass="hMessage"
					errorLabelClass="hMessage">
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
					style="padding-top: 21px; padding-bottom: 4px;"><h:outputLabel
					value="#{msg.correiaView_nome}: *" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" style="padding-bottom: 8px;"><h:inputText
					id="Nome" label="#{msg.correiaView_nome}"
					value="#{correiaController.correia.nomeCorreia}" required="true"
					size="60" maxlength="60" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow"><h:outputLabel
					value="#{msg.correiaView_estadooperacao}: *" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" style="padding-bottom: 8px;"><h:selectOneMenu
					id="estadoMaquina" label="#{msg.correiaView_estadooperacao}"
					onchange="#{correiaController.correia.estado}"
					converter="estadoMaquinaEnumConversor" required="true"
					value="#{correiaController.correia.estado}">
					<f:selectItems value="#{correiaController.estadoMaquinaList}" />
				</h:selectOneMenu></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow"><h:outputLabel
					value="#{msg.correiaView_numero}: *" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" style="padding-bottom: 8px;"><h:inputText
					id="Numero" label="#{msg.correiaView_numero}"
					value="#{correiaController.correia.numero}" required="true"
					onkeypress="return numeros(event);" size="10" maxlength="10">
					<f:validateDoubleRange minimum="#{msg.correiaView_numeroMinimo}"
						maximum="#{msg.correiaView_numeroMaximo}" />
					<f:convertNumber minFractionDigits="0" maxFractionDigits="0"
						pattern="##########" />
				</h:inputText></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow"><h:outputLabel
					value="#{msg.correiaView_taxaoperacao}: *" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" style="padding-bottom: 8px;"><h:inputText
					id="taxaOperacao" label="#{msg.correiaView_taxaoperacao}"
					value="#{correiaController.correia.taxaDeOperacao}"
					onkeypress="return numeros(event);" required="true" size="10"
					maxlength="10">
					<f:validateDoubleRange
						minimum="#{msg.correiaView_taxaoperacaoMinima}"
						maximum="#{msg.correiaView_taxaoperacaoMaxima}" />
					<f:convertNumber minFractionDigits="0" maxFractionDigits="2"
						pattern="#######.##" />
				</h:inputText></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow"><h:outputLabel
					value="#{msg.correiaView_patiosuperior}:" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" style="padding-bottom: 8px;"><h:selectOneMenu
					id="patioSuperior" label="#{msg.correiaView_patiosuperior}"
					onchange="#{correiaController.correia.patioSuperior}"
					converter="patioConversor"
					value="#{correiaController.correia.patioSuperior}">
					<f:selectItems value="#{correiaController.patioList}" />
				</h:selectOneMenu></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow"><h:outputLabel
					value="#{msg.correiaView_patioinferior}:" /></td>
			</tr>
			<tr>
				<td class="boxCreateEditRow" style="padding-bottom: 8px;"><h:selectOneMenu
					id="patioInferior" label="#{msg.correiaView_patioinferior}"
					onchange="#{correiaController.correia.patioInferior}"
					converter="patioConversor"
					value="#{correiaController.correia.patioInferior}">
					<f:selectItems value="#{correiaController.patioList}" />
				</h:selectOneMenu></td>
			</tr>
		</table>
		<br>

		<!-- tabela de tipo-->
		<table cellspacing="0" border="0" class="painelTabela" width="490">
			<tr>
				<td class="box11"><rich:dataTable width="460" border="0"
					cellspacing="0"
					value="#{correiaController.correia.listaDeMaquinas}" var="obj"
					id="maquinas" binding="#{correiaController.dataTableMaquina}"
					rows="10" styleClass="tabela" style="table-layout:fixed"
					rowClasses="linhaPar, linhaImpar"
					columnClasses="coluna1,coluna3,coluna3" headerClass="cabecalho">
					<rich:column>
						<f:facet name="header">
							<h:outputLabel value="#{msg.correiaView_maquinas}" />
						</f:facet>
						<h:outputText value="#{obj.nomeMaquina}" />
					</rich:column>
					<rich:column>
						<f:facet name="header">
							<h:outputLabel value="#{msg.label_edit}" />
						</f:facet>
						<h:commandLink action="#{correiaController.alteraMaquina}">
							<h:graphicImage value="../imagens/editar-a.png"
								onmouseover="this.src='../imagens/editar-b.png'"
								onmouseout="this.src='../imagens/editar-a.png'"
								styleClass="imgLink" />
							<f:param value="#{obj.idMaquina}" name="id" />
						</h:commandLink>
					</rich:column>
					<h:column>
						<f:facet name="header">
							<h:outputLabel value="#{msg.label_delete}" />
						</f:facet>
						<a4j:commandLink action="#{correiaController.selectMaquina}"
							oncomplete="Richfaces.showModalPanel('conf_delete')"
							reRender="maquinas">
							<h:graphicImage value="../imagens/excluir-a.png"
								onmouseover="this.src='../imagens/excluir-b.png'"
								onmouseout="this.src='../imagens/excluir-a.png'" width="20"
								height="21" styleClass="imgLink" />
							<f:param value="#{obj.idMaquina}" name="id" />
						</a4j:commandLink>
					</h:column>
					<f:facet name="footer">
						<rich:datascroller id="lista" renderIfSinglePage="false"
							for="maquinas" maxPages="20" />
					</f:facet>
				</rich:dataTable></td>
			</tr>
			<tr>
				<td class="boxAdd" align="right"><h:commandLink
					action="#{correiaController.novaMaquina}">
					<h:graphicImage value="../imagens/adicionar-a.png"
						onmouseover="this.src='../imagens/adicionar-b.png'"
						onmouseout="this.src='../imagens/adicionar-a.png'"
						styleClass="imgLink" />
				</h:commandLink></td>
			</tr>
		</table>
		<br>
		<table cellspacing="0" border="0" class="boxCreateEdit" width="490">
			<tr>
				<td class="boxSearchUserRow"
					style="padding-bottom: 11px; padding-top: 8px;" align="center">
				<table cellspacing="0" border="0" align="center">
					<tr>
						<td><h:commandLink action="#{correiaController.gravaCorreia}">
							<div class="customButton"
								onmouseover="defineButtonStyleOver(this);"
								onmouseout="defineButtonStyleOut(this);"><h:outputLabel
								value="#{msg.button_confirm}" /></div>
						</h:commandLink></td>
						<td style="padding-left: 12px;"><h:commandLink
							action="voltarCorreia" immediate="true">
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
		<!-- Form modal de exclusao -->
		<rich:modalPanel id="conf_delete" minHeight="110" minWidth="300"
			height="110" width="300">
			<f:facet name="header">
				<h:outputLabel styleClass="hMessageDialog"
					value="#{msg.label_delete}" />
			</f:facet>
			<center><h:outputLabel styleClass="hMessageDialog"
				value="#{msg.deletionQuestion}" /> <br>
			<h:form>
				<h:panelGrid columns="2">
					<h:column>
						<h:commandButton id="btnExcluir" styleClass="customButton"
							action="#{correiaController.removeMaquina}"
							onclick="Richfaces.hideModalPanel('conf_delete')"
							value="#{msg.button_yes}" />
					</h:column>
					<h:column>
						<h:commandButton id="btnCancel" styleClass="customButton"
							value="#{msg.button_no}"
							onclick="Richfaces.hideModalPanel('conf_delete')" />
					</h:column>
					<h:column>
					</h:column>
				</h:panelGrid>
			</h:form></center>
		</rich:modalPanel>	
</f:view>
</body>
</html>