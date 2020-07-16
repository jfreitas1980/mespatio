<%@taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@taglib uri="http://richfaces.org/a4j" prefix="a4j" %>
<%@taglib uri="http://richfaces.org/rich" prefix="rich" %>
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
                <td class="iFrameTitle">
                    <h:outputLabel value="#{msg.carregadoraDeNaviosView_titulo}" />
                </td>
            </tr>
            <tr>
                <td>
                    <rich:messages layout="table" infoLabelClass="hMessage" errorLabelClass="hMessage" >
                        <f:facet name="infoMaker">
                            <h:graphicImage value = "../imagens/accept.png"/>
                        </f:facet>
                        <f:facet name="errorMaker">
                            <h:graphicImage value = "../imagens/error.png"/>
                        </f:facet>
                    </rich:messages> 
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-top: 21px; padding-bottom: 4px;" colspan="2">
                    <h:outputLabel value="#{msg.carregadoraDeNaviosView_nome}: *" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <h:inputText id="Name" label="#{msg.carregadoraDeNaviosView_nome}"
                        value="#{carregadoraDeNaviosController.carregadora.nomeCarregadeiraDeNavios}"
                        required="true" size="60" maxlength="60">
					</h:inputText>
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.carregadoraDeNaviosView_estadooperacao}: *" /></td>
            </tr>
            <tr>
                <td class="boxSearchUserRow" style="padding-bottom: 8px;" colspan="2">
					<h:selectOneMenu id="estadoMaquina" label="#{msg.carregadoraDeNaviosView_estadooperacao}"
						onchange="#{carregadoraDeNaviosController.carregadora.estado}"
						converter="estadoMaquinaEnumConversor"
						required="true"
						value="#{carregadoraDeNaviosController.carregadora.estado}">
                        <f:selectItems
                            value="#{carregadoraDeNaviosController.estadoMaquinaList}" />
                    </h:selectOneMenu>             
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.carregadoraDeNaviosView_taxaoperacao}: *" />
                </td>
            </tr>
            <tr>
                <td class="boxSearchUserRow" style="padding-bottom: 8px;" colspan="2">
                    <h:inputText id="taxaOperacao" label="#{msg.carregadoraDeNaviosView_taxaoperacao}"
                        value="#{carregadoraDeNaviosController.carregadora.taxaDeOperacao}"
                        onkeypress="return numeros(event);" required="true" size="10" maxlength="10">
                        <f:validateDoubleRange
                            minimum="#{msg.carregadoraDeNaviosView_taxaoperacaoMinima}"
                            maximum="#{msg.carregadoraDeNaviosView_taxaoperacaoMaxima}" />
                        <f:convertNumber minFractionDigits="0" maxFractionDigits="2"
                            pattern="#######.##" />
				</h:inputText>
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.carregadoraDeNaviosView_datamanutencao}:" />
                </td>
            </tr>
            <tr>
                <td class="boxSearchUserRow" colspan="2">
					<rich:calendar id="dataManutencao" datePattern="#{msg.dateFormat}" converter="dataConversor"
						value="#{carregadoraDeNaviosController.carregadora.proximaManutencao}" label="#{msg.carregadoraDeNaviosView_datamanutencao}"/> 
                </td>
            </tr>
			<tr>			
				<td class="boxCreateEditRow" colspan="2"
                    style="padding-bottom: 11px; padding-top: 8px;" align="center">
                    <table cellspacing="0" border="0">
                        <tr>
                            <td>
                                <h:commandLink action="#{carregadoraDeNaviosController.gravaCarregadoraDeNavios}">
                                    <div class="customButton"
                                        onmouseover="defineButtonStyleOver(this);"
                                        onmouseout="defineButtonStyleOut(this);">
                                        <h:outputLabel value="#{msg.button_confirm}" />
                                    </div>
                                </h:commandLink>
                            </td>
                            <td style="padding-left: 12px;">
                                <h:commandLink action="voltarCarregadora"
                                        immediate="true">
                                    <div class="customButton"
                                        onmouseover="defineButtonStyleOver(this);"
                                        onmouseout="defineButtonStyleOut(this);">
                                        <h:outputLabel value="#{msg.button_cancel}" />
                                    </div>
                                </h:commandLink>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>     
    </h:form>   
</f:view>
</body>
</html>