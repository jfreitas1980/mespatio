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
                    value="#{msg.metaInternaView_titulo}" /></td>
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
                    <h:outputLabel value="#{msg.metaInternaView_tipopelota}:" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <h:selectOneMenu id="tipoPelota" label="#{msg.metaInternaView_tipopelota}"
						onchange="#{metaInternaController.metaInterna.tipoPelota}"
						converter="tipoProdutoConversor"
						value="#{metaInternaController.metaInterna.tipoPelota}">
                        <f:selectItems
                            value="#{metaInternaController.tipoPelotaList}" />
                    </h:selectOneMenu>
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow" style="padding-top: 21px; padding-bottom: 4px;" colspan="2">
                    <h:outputLabel value="#{msg.tipoItemControleList_titulo}:" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <h:selectOneMenu id="tipoItemControle" label="#{msg.tipoItemControleList_titulo}"
						onchange="#{metaInternaController.metaInterna.tipoItemDeControle}"
						converter="tipoItemConversor"
						value="#{metaInternaController.metaInterna.tipoItemDeControle}">
                        <f:selectItems
                            value="#{metaInternaController.tipoItemList}" />
                    </h:selectOneMenu>
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.metaInternaView_limitesuperior}:" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <h:inputText id="limiteSuperior" label="#{msg.metaInternaView_limitesuperior}"
                        value="#{metaInternaController.metaInterna.limiteSuperiorValorMetaInterna}" size="10"
                        maxlength="10" onkeypress="return numeros(event);">
                        <f:validateDoubleRange
                            minimum="#{msg.metaInternaView_limitesuperiorMinimo}"
                            maximum="#{msg.metaInternaView_limitesuperiorMaximo}" />
                        <f:convertNumber minFractionDigits="0" maxFractionDigits="2"
                            pattern="#######.##" />
                        <rich:ajaxValidator event="onblur" />
                    </h:inputText>
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.metaInternaView_limiteinferior}:" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <h:inputText id="limiteInferior" label="#{msg.metaInternaView_limiteinferior}"
                        value="#{metaInternaController.metaInterna.limiteInferiorValorMetaInterna}" size="10"
                        maxlength="10" onkeypress="return numeros(event);">
                        <f:validateDoubleRange
                            minimum="#{msg.metaInternaView_limiteinferiorMinimo}"
                            maximum="#{msg.metaInternaView_limiteinferiorMaximo}" />
                        <f:convertNumber minFractionDigits="0" maxFractionDigits="2"
                            pattern="#######.##" />
                        <rich:ajaxValidator event="onblur" />
                    </h:inputText>
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.metaInternaView_tipometa}: *" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
					<h:selectOneMenu id="tipoMeta" label="#{msg.metaInternaView_tipometa}"
						onchange="#{metaInternaController.metaInterna.tipoDaMetaInterna}"
						converter="tipoMetaInternaEnumConversor"
						required="true"
						value="#{metaInternaController.metaInterna.tipoDaMetaInterna}">
                        <f:selectItems
                            value="#{metaInternaController.tipoMetaList}" />
					</h:selectOneMenu>
                </td>
            </tr>
 			<tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.metaInternaList_dataInicio}: *" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
					<rich:calendar id="dataInicio" label="#{msg.metaInternaList_dataInicio}"
                        datePattern="#{msg.dateFormat}" 
						required="true"
                        converter="dataConversor"                                                                    
                        value="#{metaInternaController.metaInterna.dtInicio}" />
                </td>
            </tr>            
			<tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.metaInternaList_dataInicio}: *" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
					<rich:calendar id="dataFim" label="#{msg.metaInternaList_dataFim}"
                        datePattern="#{msg.dateFormat}" 
						required="true"
                        converter="dataConversor"                                                                       
                        value="#{metaInternaController.metaInterna.dtFim}" />
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow" colspan="2"
                    style="padding-bottom: 11px; padding-top: 8px;" align="center">
                    <table cellspacing="0" border="0">
                        <tr>
                            <td>
                                <h:commandLink action="#{metaInternaController.salvarMetaInterna}">
                                    <div class="customButton"
                                        onmouseover="defineButtonStyleOver(this);"
                                        onmouseout="defineButtonStyleOut(this);">
                                        <h:outputLabel value="salvar" />
                                    </div>
                                </h:commandLink>
                            </td>
                            <td style="padding-left: 12px;">
                                <h:commandLink action="cancelarMetaInterna"
                                        immediate="true">
                                    <div class="customButton"
                                        onmouseover="defineButtonStyleOver(this);"
                                        onmouseout="defineButtonStyleOut(this);">
                                        <h:outputLabel value="cancelar" />
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