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
                    <h:outputLabel value="#{msg.balizaView_titulo}" />
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
                    <h:outputLabel value="#{msg.balizaView_nome}:*" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;">
                    <h:inputText id="NomeBaliza" label="#{msg.balizaView_nome}"
                        value="#{balizaController.baliza.nomeBaliza}" size="60"
                        required="true" maxlength="60">
                        <rich:ajaxValidator event="onblur" />
                    </h:inputText>
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow">
                    <h:outputLabel value="#{msg.balizaView_largura}: *" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;">
                    <h:inputText id="Largura" label="#{msg.balizaView_largura}"
                        value="#{balizaController.baliza.largura}"
						required="true" onkeypress="return numeros(event);"
                        size="10" maxlength="10" >
                        <f:validateDoubleRange
                            minimum="#{msg.balizaView_larguraMinima}"
                            maximum="#{msg.balizaView_larguraMaxima}" />
                        <f:convertNumber minFractionDigits="0" maxFractionDigits="2"
                            pattern="#######.##" />
                    </h:inputText>
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow">
                    <h:outputLabel value="#{msg.balizaView_capacidade}: *" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;">
                    <h:inputText id="capacidadeMaxima" label="#{msg.balizaView_capacidade}"
                        value="#{balizaController.baliza.capacidadeMaxima}"
						required="true" onkeypress="return numeros(event);"
                        size="10" maxlength="10" >
                        <f:validateDoubleRange
                            minimum="#{msg.balizaView_capacidadeMinima}"
                            maximum="#{msg.balizaView_capacidadeMaxima}" />
                        <f:convertNumber minFractionDigits="0" maxFractionDigits="2"
                            pattern="#######.##" />
                    </h:inputText>
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow">
                    <h:outputLabel value="#{msg.balizaView_estadoOperacao}: *" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;">
                    <h:selectOneMenu id="Operacao" label="#{msg.balizaView_estadoOperacao}"
                        onchange="#{balizaController.baliza.estado}"
                        converter="estadoMaquinaEnumConversor"
                        required="true"
                        value="#{balizaController.baliza.estado}">          
                        <f:selectItems
                            value="#{balizaController.estadoEstruturaList}" />
                    </h:selectOneMenu>                    
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow">
                    <h:outputLabel value="#{msg.balizaView_tipo}:*" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;">
                    <h:selectOneMenu id="tipoBaliza" label="#{msg.balizaView_tipo}"
                        onchange="#{balizaController.baliza.tipoBaliza}"
                        converter="tipoBalizaConversor"
                        required="true"
                        value="#{balizaController.baliza.tipoBaliza}">
                        <f:selectItems
                            value="#{balizaController.tipoList}" />
                    </h:selectOneMenu>
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" colspan="2"
                    style="padding-bottom: 11px; padding-top: 8px;" align="center">
                    <table cellspacing="0" border="0">
                        <tr>
                            <td>
                                <h:commandLink action="#{balizaController.gravaBaliza}">
                                    <div class="customButton"
                                        onmouseover="defineButtonStyleOver(this);"
                                        onmouseout="defineButtonStyleOut(this);">
                                        <h:outputLabel value="#{msg.button_confirm}" />
                                    </div>
                                </h:commandLink>
                            </td>
                            <td style="padding-left: 12px;">
                                <h:commandLink action="voltarBaliza"
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