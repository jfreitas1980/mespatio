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
                    <h:outputLabel value="#{msg.usinaView_titulo}" />
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
                <td class="boxCreateEditRow" style="padding-top: 21px; padding-bottom: 4px;">
                    <h:outputLabel value="#{msg.usinaView_nome}: *" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;">
                    <h:inputText id="Nome"
                        value="#{usinaController.usina.nomeUsina}" label="#{msg.usinaView_nome}"
                        required="true" size="60" maxlength="60" />
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow">
                    <h:outputLabel value="#{msg.usinaView_estadooperacao}: *" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;">
					<h:selectOneMenu id="estadoOperacao"
                        onchange="#{usinaController.usina.estado}"
						required="true"
                        converter="estadoEstruturaEnumConversor"
                        value="#{usinaController.usina.estado}" label="#{msg.usinaView_estadooperacao}">          
                        <f:selectItems
                            value="#{usinaController.estadoEstruturasList}" />
                    </h:selectOneMenu>                       
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow">
                    <h:outputLabel value="#{msg.usinaView_taxaoperacao}: *" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;">
                    <h:inputText id="taxaOperacao"
                        value="#{usinaController.usina.taxaDeOperacao}" label="#{msg.usinaView_taxaoperacao}"
						required="true" onkeypress="return numeros(event);"
                        size="10" maxlength="10">
                        <f:validateDoubleRange
                            minimum="#{msg.usinaView_taxaoperacaoMinima}"
                            maximum="#{msg.usinaView_taxaoperacaoMaxima}" />
                        <f:convertNumber minFractionDigits="0" maxFractionDigits="2"
                            pattern="#######.##" />
					</h:inputText>
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow">
                    <h:outputLabel value="#{msg.usinaView_faseprocesso}: *" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;">
                    <h:inputText id="codFaseProcesso"
                        value="#{usinaController.usina.codigoFaseProcessoUsina}" label="#{msg.usinaView_faseprocesso}"
                        required="true" onkeypress="return numeros(event);"
                        size="10" maxlength="10">
                        <f:validateDoubleRange
                            minimum="#{msg.usinaView_faseprocessoMinima}"
                            maximum="#{msg.usinaView_faseprocessoMaxima}" />
                        <f:convertNumber minFractionDigits="0" maxFractionDigits="0"
                            pattern="##########" />
					</h:inputText>
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow">
                    <h:outputLabel value="#{msg.usinaView_itemcontrole}: *" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;">
                    <h:inputText id="codItemControle"
                        value="#{usinaController.usina.cdItemControleUsina}" label="#{msg.usinaView_itemcontrole}"
                        required="true" onkeypress="return numeros(event);"
                        size="10" maxlength="10">
                        <f:validateDoubleRange
                            minimum="#{msg.usinaView_itemcontroleMinima}"
                            maximum="#{msg.usinaView_itemcontroleMaxima}" />
                        <f:convertNumber minFractionDigits="0" maxFractionDigits="0"
                            pattern="##########" />
					</h:inputText>
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow">
                    <h:outputLabel value="#{msg.usinaView_tipoprocesso}: *" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;">
                    <h:inputText id="codTipoProcesso"
                        value="#{usinaController.usina.cdTipoProcessoUsina}" label="#{msg.usinaView_tipoprocesso}"
                        required="true" size="255" maxlength="255" />
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow">
                    <h:outputLabel value="#{msg.usinaView_arearesponsavel}: *" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;">
                    <h:inputText id="codAreaResponsavel"
                        value="#{usinaController.usina.cdAreaRespEdUsina}" label="#{msg.usinaView_arearesponsavel}"
						required="true"
                        size="255" maxlength="255" />
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow">
                    <h:outputLabel value="#{msg.usinaView_patio}: *" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;">
					<h:selectOneMenu id="patio"
                        onchange="#{usinaController.usina.patioExpurgoPellet}"
                        converter="patioConversor"
                        value="#{usinaController.usina.patioExpurgoPellet}" label="#{msg.usinaView_patio}">          
                        <f:selectItems
                            value="#{usinaController.patioList}" />
                    </h:selectOneMenu>   
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow">
                    <h:outputLabel value="#{msg.usinaView_taxageracao}: *" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;">
					<h:inputText id="taxaGeracaoPellet"
                        value="#{usinaController.usina.taxaGeracaoPellet}" label="#{msg.usinaView_taxageracao}"
						required="true" onkeypress="return numeros(event);"
                        size="10" maxlength="10">
                        <f:validateDoubleRange
                            minimum="#{msg.usinaView_taxaoperacaoMinima}"
                            maximum="#{msg.usinaView_taxaoperacaoMaxima}" />
                        <f:convertNumber minFractionDigits="0" maxFractionDigits="2"
                            pattern="#######.##" />
					</h:inputText>
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow">
                    <h:outputLabel value="#{msg.usinaView_qtdproduto}: *" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;">
					<h:inputText id="qtdProduto"
                        value="#{usinaController.usina.qtdProdutoAtualizacao}" label="#{msg.usinaView_qtdproduto}"
                        onkeypress="return numeros(event);"
                        size="10" maxlength="10">
                        <f:validateDoubleRange
                            minimum="#{msg.usinaView_qtdprodutoMinima}"
                            maximum="#{msg.usinaView_qtdprodutoleMaxima}" />
                        <f:convertNumber minFractionDigits="0" maxFractionDigits="0"
                            pattern="##########" />
					</h:inputText>
                </td>
            </tr>
			<td class="boxCreateEditRow" colspan="2"
                    style="padding-bottom: 11px; padding-top: 8px;" align="center">
                    <table cellspacing="0" border="0">
                        <tr>
                            <td>
                                <a4j:commandLink action="#{usinaController.gravaUsina}">
                                    <div class="customButton"
                                        onmouseover="defineButtonStyleOver(this);"
                                        onmouseout="defineButtonStyleOut(this);">
                                        <h:outputLabel value="#{msg.button_confirm}" />
                                    </div>
                                </a4j:commandLink>
                            </td>
                            <td style="padding-left: 12px;">
                                <a4j:commandLink action="voltarUsina"
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