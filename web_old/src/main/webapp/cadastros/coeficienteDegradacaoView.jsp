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
                    value="#{msg.coeficienteDegradacaoView_titulo}" /></td>
            </tr>
            <tr>
                <td colspan="2">
                    <rich:messages layout="table"
                        infoLabelClass="hMessage" errorLabelClass="hMessage"
                        styleClass="hMessage">
                        <f:facet name="infoMaker">
                            <h:graphicImage value="../imagens/accept.png" />
                        </f:facet>
                        <f:facet name="errorMaker">
                            <h:graphicImage value="../imagens/error.png" />
                        </f:facet>
                    </rich:messages>
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow" style="padding-top: 21px; padding-bottom: 4px;" colspan="2">
                    <h:outputLabel value="#{msg.coeficienteDegradacaoView_tipoproduto}:" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
					<h:selectOneMenu id="tipoDeProduto" label="#{msg.coeficienteDegradacaoView_tipoproduto}"
						onchange="#{coeficienteDeDegradacaoController.coeficienteDeDegradacao.tipoDeProduto}"
                        converter="tipoProdutoConversor"
						value="#{coeficienteDeDegradacaoController.coeficienteDeDegradacao.tipoDeProduto}">
						<f:selectItems value="#{coeficienteDeDegradacaoController.tipoProdutoList}" />
					</h:selectOneMenu>
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.coeficienteDegradacaoView_valor}: *" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <h:inputText id="valorCoeficiente" label="#{msg.coeficienteDegradacaoView_valor}"
                        value="#{coeficienteDeDegradacaoController.coeficienteDeDegradacao.valorDoCoeficiente}" size="10"
                        required="true" maxlength="10" onkeypress="return numeros(event);">
                        <f:validateDoubleRange
                            minimum="#{msg.coeficienteDegradacaoView_valorMinimo}"
                            maximum="#{msg.coeficienteDegradacaoView_valorMaximo}" />
                        <f:convertNumber minFractionDigits="0" maxFractionDigits="2"
                            pattern="#######.##" />
                        <rich:ajaxValidator event="onblur" />
                    </h:inputText>
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" colspan="2"
                    style="padding-bottom: 11px; padding-top: 8px;" align="center">
                    <table cellspacing="0" border="0">
                        <tr>
                            <td>
                                <h:commandLink action="#{coeficienteDeDegradacaoController.salvarCoeficienteDeDegradacao}">
                                    <div class="customButton"
                                        onmouseover="defineButtonStyleOver(this);"
                                        onmouseout="defineButtonStyleOut(this);">
                                        <h:outputLabel value="#{msg.button_save}" />
                                    </div>
                                </h:commandLink>
                            </td>
                            <td style="padding-left: 12px;">
                                <h:commandLink action="cancelarCoeficienteDeDegradacao"
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