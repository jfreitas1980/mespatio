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
                    <h:outputLabel value="#{msg.bercoView_titulo}" />
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
                    <h:outputLabel value="#{msg.bercoView_nome}: *" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <h:inputText id="Name" label="#{msg.bercoView_nome}"
                        value="#{bercoController.berco.nomeBerco}"
                        required="true" size="60" maxlength="60" />
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.bercoView_estadooperacao}: *" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
					<h:selectOneMenu id="estadoOperacao" label="#{msg.bercoView_estadooperacao}"
                        onchange="#{bercoController.berco.estado}"
                        converter="estadoEstruturaEnumConversor" 
                        required="true"
                        value="#{bercoController.berco.estado}">          
                        <f:selectItems
                            value="#{bercoController.estadoEstruturasList}" />
                    </h:selectOneMenu>                   
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.bercoView_comprimentomax}: *" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <h:inputText id="comprimentoMaximo" label="#{msg.bercoView_comprimentomax}"
                        value="#{bercoController.berco.comprimentoMaximo}"
                        onkeypress="return numeros(event);" required="true" size="10" maxlength="10">
                        <f:validateDoubleRange
                            minimum="#{msg.bercoView_comprimentomaxMinima}"
                            maximum="#{msg.bercoView_comprimentomaxMaxima}" />
                        <f:convertNumber minFractionDigits="0" maxFractionDigits="2"
                            pattern="#######.##" />
					</h:inputText>
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.bercoView_caladomax}: *" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <h:inputText id="caladoMaximo" label="#{msg.bercoView_caladomax}"
                        value="#{bercoController.berco.caladoMaximo}"
                        onkeypress="return numeros(event);" required="true" size="10" maxlength="10">
                        <f:validateDoubleRange
                            minimum="#{msg.bercoView_caladomaxMinima}"
                            maximum="#{msg.bercoView_caladomaxMaxima}" />
                        <f:convertNumber minFractionDigits="0" maxFractionDigits="2"
                            pattern="#######.##" />
				</h:inputText>
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.bercoView_bocamax}: *" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <h:inputText id="bocaMaxima" label="#{msg.bercoView_bocamax}"
                        value="#{bercoController.berco.bocaMaxima}"
                        onkeypress="return numeros(event);" required="true" size="10" maxlength="10">
                        <f:validateDoubleRange
                            minimum="#{msg.bercoView_bocamaxMinima}"
                            maximum="#{msg.bercoView_bocamaxMaxima}" />
                        <f:convertNumber minFractionDigits="0" maxFractionDigits="2"
                            pattern="#######.##" />
					</h:inputText>
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.bercoView_identificadorBerco}: *" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <h:inputText id="identificadorBerco" label="#{msg.bercoView_identificadorBerco}"
                        value="#{bercoController.berco.identificadorBerco}"
                        required="true" size="60" maxlength="60" />
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow" colspan="2"
                    style="padding-bottom: 11px; padding-top: 8px;" align="center">
                    <table cellspacing="0" border="0">
                        <tr>
                            <td>
                                <h:commandLink action="#{bercoController.gravaBerco}">
                                    <div class="customButton"
                                        onmouseover="defineButtonStyleOver(this);"
                                        onmouseout="defineButtonStyleOut(this);">
                                        <h:outputLabel value="#{msg.button_confirm}" />
                                    </div>
                                </h:commandLink>
                            </td>
                            <td style="padding-left: 12px;">
                                <h:commandLink action="voltarBerco"
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