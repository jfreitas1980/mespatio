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
                    value="#{msg.titulo_view_tipoproduto}" />
                </td>
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
                    <h:outputLabel value="#{msg.tipoProdutoView_descricao}:*" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <h:inputText id="descricaoTipoProduto"
                        value="#{tipoProdutoController.tipoProduto.descricaoTipoProduto}" label="#{msg.tipoProdutoView_descricao}" 
                        size="60" required="true" maxlength="60">
                        <rich:ajaxValidator event="onblur" />
                    </h:inputText>
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.tipoProdutoView_codigotipo}:*" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <h:inputText id="codigoTipoProduto"
                        value="#{tipoProdutoController.tipoProduto.codigoTipoProduto}" label="#{msg.tipoProdutoView_codigotipo}" size="30"
                        required="true" maxlength="30" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.tipoProdutoView_codigofamilia}:*" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <h:inputText id="codigoFamiliaTipoProduto"
                        value="#{tipoProdutoController.tipoProduto.codigoFamiliaTipoProduto}" label="#{msg.tipoProdutoView_codigofamilia}" 
                        size="30" required="true" maxlength="30" />
                </td>
            </tr>

            <tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.tipoProdutoView_insumo}:" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <h:selectOneMenu
                        id="codigoInsumoTipoProduto"
                        onchange="#{tipoProdutoController.tipoProduto.codigoInsumoTipoProduto}"
                        converter="tipoProdutoConversor" 
                        value="#{tipoProdutoController.tipoProduto.codigoInsumoTipoProduto}" label="#{msg.tipoProdutoView_insumo}">          
                        <f:selectItems
                            value="#{tipoProdutoController.insumoList}" />
                    </h:selectOneMenu>
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.tipoProdutoView_tipo}:" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <h:selectOneMenu id="tipoDeProduto"
                        onchange="#{tipoProdutoController.tipoProduto.tipoDeProduto}"
                        converter="tipoDeProdutoEnumConversor"
                        value="#{tipoProdutoController.tipoProduto.tipoDeProduto}" label="#{msg.tipoProdutoView_tipo}">          
                        <f:selectItems
                            value="#{tipoProdutoController.tipoList}" />
                    </h:selectOneMenu>
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.tipoProdutoView_coridentificacao}: *" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <rich:colorPicker id="corIdentificacao"
                    converter="corTipoProdutoConversor" 
                    required="true"
                    value="#{tipoProdutoController.tipoProduto.corIdentificacao}"
                                       colorMode="rgb"/>
                    
                </td>
            </tr>
            <!--<tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.tipoProdutoView_codigosap}: *" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <h:inputText id="codigoSAPDado" label="#{msg.tipoProdutoView_codigosap}"
                        value="#{tipoProdutoController.tipoProduto.codigoSAPDado}" size="10"
                        onkeypress="return numeros(event);" required="true" maxlength="10">
                        <f:validateDoubleRange
                            minimum="#{msg.tipoProdutoView_codigosapMinimo}"
                            maximum="#{msg.tipoProdutoView_codigosapMaximo}" />
                        <f:convertNumber minFractionDigits="0" maxFractionDigits="0"
                            pattern="##########" />
					</h:inputText>
                </td>
            </tr>-->
            <tr>
                <td class="boxCreateEditRow" colspan="2"
                    style="padding-bottom: 11px; padding-top: 8px;" align="center">
                    <table cellspacing="0" border="0">
                        <tr>
                            <td>
                                <a4j:commandLink action="#{tipoProdutoController.gravaTipoProduto}">
                                    <div class="customButton"
                                        onmouseover="defineButtonStyleOver(this);"
                                        onmouseout="defineButtonStyleOut(this);">
                                        <h:outputLabel value="#{msg.button_save}" />
                                    </div>
                                </a4j:commandLink>
                            </td>
                            <td style="padding-left: 12px;">
                                <a4j:commandLink action="cancelarTipoProduto"
                                        immediate="true">
                                    <div class="customButton"
                                        onmouseover="defineButtonStyleOver(this);"
                                        onmouseout="defineButtonStyleOut(this);">
                                        <h:outputLabel value="#{msg.button_cancel}" />
                                    </div>
                                </a4j:commandLink>
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