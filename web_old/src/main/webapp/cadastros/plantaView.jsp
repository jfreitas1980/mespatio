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
    <h:form prependId="false">
        <table cellspacing="0" border="0" class="boxCreateEdit" width="490">
            <tr>
                <td class="iFrameTitle">
                    <h:outputLabel value="#{msg.plantaView_titulo}" />
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
                    <h:outputLabel value="#{msg.plantaView_nomePlanta}:*" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;">
                    <h:inputText id="nomePlanta" label="#{msg.plantaView_nomePlanta}"
                        value="#{plantaController.situacaoPatio.planta.nomePlanta}" size="50"
                        required="true" maxlength="60">
                        <rich:ajaxValidator event="onblur" />
                    </h:inputText>
                </td>
            </tr>
        </table>
        <br>
        <!-- tabela de Pier-->
        <table cellspacing="0" border="0" class="painelTabela" width="490">
            <tr>
                <td class="box11">      
                    <rich:dataTable width="460" border="0" cellspacing="0"
                            value="#{plantaController.situacaoPatio.planta.listaPiers}"
                            var="obj" 
                            id="pier"
                            binding="#{plantaController.dataTablePier}"
                            rows="10"
                            styleClass="tabela" style="table-layout:fixed"
                            rowClasses="linhaPar, linhaImpar"
                            columnClasses="coluna1,coluna3,coluna3"
                            headerClass="cabecalho">
                        <rich:column>
							<f:facet name="header"><h:outputLabel value="#{msg.plantaView_nomePier}" /></f:facet>
                            <h:outputText value="#{obj.nomePier}"/>
                        </rich:column>
                        <rich:column>
							<f:facet name="header"><h:outputLabel value="#{msg.label_edit}" /></f:facet>
                            <h:commandLink action="#{plantaController.alteraPier}">
                                <h:graphicImage value="../imagens/editar-a.png"
                                    onmouseover="this.src='../imagens/editar-b.png'"
                                    onmouseout="this.src='../imagens/editar-a.png'" styleClass="imgLink" />
                                <f:param value="#{obj.idPier}" name="id" />
                            </h:commandLink>  
                        </rich:column>  
                        <h:column>
							<f:facet name="header"><h:outputLabel value="#{msg.label_delete}" /></f:facet>
                            <a4j:commandLink action="#{plantaController.selectPier}" oncomplete="Richfaces.showModalPanel('conf_delete')" reRender="conf_delete">
                                <h:graphicImage value="../imagens/excluir-a.png"
                                    onmouseover="this.src='../imagens/excluir-b.png'"
                                    onmouseout="this.src='../imagens/excluir-a.png'" width="20"
                                    height="21" styleClass="imgLink" />
                                <f:param value="#{obj.idPier}" name="id" />
                            </a4j:commandLink>
                        </h:column>                 
                        <f:facet name="footer">
                            <rich:datascroller id="lista" renderIfSinglePage="false" for="pier" maxPages="20"/>
                        </f:facet>
                    </rich:dataTable>               
                </td>
            </tr>
            <tr>
                <td class="boxAdd" align="right">
                    <h:commandLink action="#{plantaController.novoPier}">
                        <h:graphicImage value="../imagens/adicionar-a.png"
                            onmouseover="this.src='../imagens/adicionar-b.png'"
                            onmouseout="this.src='../imagens/adicionar-a.png'"
                            styleClass="imgLink" />
                </h:commandLink></td>
            </tr>
        </table>
        <br>
        <!-- tabela de tipo-->
        <table cellspacing="0" border="0" class="painelTabela" width="490">
            <tr>
                <td class="box11">      
                    <rich:dataTable width="460" border="0" cellspacing="0"
                            value="#{plantaController.situacaoPatio.planta.listaPatios}"
                            var="obj" 
                            id="patio"
                            binding="#{plantaController.dataTablePatio}"
                            rows="10"
                            styleClass="tabela" style="table-layout:fixed"
                            rowClasses="linhaPar, linhaImpar"
                            columnClasses="coluna1,coluna3,coluna3"
                            headerClass="cabecalho">
                        <rich:column>
							<f:facet name="header"><h:outputLabel value="#{msg.plantaView_nomePatio}" /></f:facet>
                            <h:outputText value="#{obj.nomePatio}"/>
                        </rich:column>
                        <rich:column>
							<f:facet name="header"><h:outputLabel value="#{msg.label_edit}" /></f:facet>
                            <h:commandLink action="#{plantaController.alteraPatio}">
                                <h:graphicImage value="../imagens/editar-a.png"
                                    onmouseover="this.src='../imagens/editar-b.png'"
                                    onmouseout="this.src='../imagens/editar-a.png'" styleClass="imgLink" />
                                <f:param value="#{obj.idPatio}" name="id" />
                            </h:commandLink>  
                        </rich:column>  
                        <h:column>
							<f:facet name="header"><h:outputLabel value="#{msg.label_delete}" /></f:facet>
                            <a4j:commandLink action="#{plantaController.selectPatio}"  oncomplete="Richfaces.showModalPanel('conf_delete')" reRender="conf_delete">
                                <h:graphicImage value="../imagens/excluir-a.png"
                                    onmouseover="this.src='../imagens/excluir-b.png'"
                                    onmouseout="this.src='../imagens/excluir-a.png'" width="20"
                                    height="21" styleClass="imgLink" />
                                <f:param value="#{obj.idPatio}" name="id" />
                            </a4j:commandLink>
                        </h:column>                 
                        <f:facet name="footer">
                            <rich:datascroller id="lista" renderIfSinglePage="false" for="patio" maxPages="20"/>
                        </f:facet>
                    </rich:dataTable>               
                </td>
            </tr>
            <tr>
                <td class="boxAdd" align="right">
                    <h:commandLink action="#{plantaController.novoPatio}">
                        <h:graphicImage value="../imagens/adicionar-a.png"
                            onmouseover="this.src='../imagens/adicionar-b.png'"
                            onmouseout="this.src='../imagens/adicionar-a.png'"
                            styleClass="imgLink" />
                </h:commandLink></td>
            </tr>
        </table>
	<br>
        <!-- tabela de tipo-->
        <table cellspacing="0" border="0" class="painelTabela" width="490">
            <tr>
                <td class="box11">      
                    <rich:dataTable width="460" border="0" cellspacing="0"
                            value="#{plantaController.situacaoPatio.planta.listaCorreias}"
                            var="obj" 
                            id="correia"
                            binding="#{plantaController.dataTableCorreia}"
                            rows="10"
                            styleClass="tabela" style="table-layout:fixed"
                            rowClasses="linhaPar, linhaImpar"
                            columnClasses="coluna1,coluna3,coluna3"
                            headerClass="cabecalho">
                        <rich:column>
							<f:facet name="header"><h:outputLabel value="#{msg.plantaView_nomeCorreia}" /></f:facet>
                            <h:outputText value="#{obj.nomeCorreia}"/>
                        </rich:column>
                        <rich:column>
							<f:facet name="header"><h:outputLabel value="#{msg.label_edit}" /></f:facet>
                            <h:commandLink action="#{plantaController.alteraCorreia}">
                                <h:graphicImage value="../imagens/editar-a.png"
                                    onmouseover="this.src='../imagens/editar-b.png'"
                                    onmouseout="this.src='../imagens/editar-a.png'" styleClass="imgLink" />
                                <f:param value="#{obj.idCorreia}" name="id" />
                            </h:commandLink>  
                        </rich:column>  
                        <h:column>
							<f:facet name="header"><h:outputLabel value="#{msg.label_delete}" /></f:facet>
                            <a4j:commandLink action="#{plantaController.selectCorreia}" oncomplete="Richfaces.showModalPanel('conf_delete')" reRender="conf_delete">   
                                <h:graphicImage value="../imagens/excluir-a.png"
                                    onmouseover="this.src='../imagens/excluir-b.png'"
                                    onmouseout="this.src='../imagens/excluir-a.png'" width="20"
                                    height="21" styleClass="imgLink" />
                                <f:param value="#{obj.idCorreia}" name="id" />
                            </a4j:commandLink>
                        </h:column>
                        <f:facet name="footer">
                            <rich:datascroller id="lista" renderIfSinglePage="false" for="correia" maxPages="20"/>
                        </f:facet>
                    </rich:dataTable>               
                </td>
            </tr>
            <tr>
                <td class="boxAdd" align="right">
                    <h:commandLink action="#{plantaController.novaCorreia}">
                        <h:graphicImage value="../imagens/adicionar-a.png"
                            onmouseover="this.src='../imagens/adicionar-b.png'"
                            onmouseout="this.src='../imagens/adicionar-a.png'"
                            styleClass="imgLink" />
                </h:commandLink></td>
            </tr>
        </table>
	   <br>
        <!-- tabela de tipo-->
        <table cellspacing="0" border="0" class="painelTabela" width="490">
            <tr>
                <td class="box11">      
                    <rich:dataTable width="460" border="0" cellspacing="0"
                            value="#{plantaController.situacaoPatio.planta.listaUsinas}"
                            var="obj" 
                            id="usina"
                            binding="#{plantaController.dataTableUsina}"
                            rows="10"
                            styleClass="tabela" style="table-layout:fixed"
                            rowClasses="linhaPar, linhaImpar"
                            columnClasses="coluna1,coluna3,coluna3"
                            headerClass="cabecalho">
                        <rich:column>
							<f:facet name="header"><h:outputLabel value="#{msg.plantaView_nomeUsina}" /></f:facet>
                            <h:outputText value="#{obj.nomeUsina}"/>
                        </rich:column>
                        <rich:column>
							<f:facet name="header"><h:outputLabel value="#{msg.label_edit}" /></f:facet>
                            <h:commandLink action="#{plantaController.alteraUsina}">
                                <h:graphicImage value="../imagens/editar-a.png"
                                    onmouseover="this.src='../imagens/editar-b.png'"
                                    onmouseout="this.src='../imagens/editar-a.png'" styleClass="imgLink" />
                                <f:param value="#{obj.idUsina}" name="id" />
                            </h:commandLink>  
                        </rich:column>  
                        <h:column>
							<f:facet name="header"><h:outputLabel value="#{msg.label_delete}" /></f:facet>
                            <a4j:commandLink action="#{plantaController.selectUsina}" oncomplete="Richfaces.showModalPanel('conf_delete')" reRender="conf_delete">
                                <h:graphicImage value="../imagens/excluir-a.png"
                                    onmouseover="this.src='../imagens/excluir-b.png'"
                                    onmouseout="this.src='../imagens/excluir-a.png'" width="20"
                                    height="21" styleClass="imgLink" />
                                <f:param value="#{obj.idUsina}" name="id" />
                            </a4j:commandLink>
                        </h:column>                 
                        <f:facet name="footer">
                            <rich:datascroller id="lista" renderIfSinglePage="false" for="usina" maxPages="20"/>
                        </f:facet>
                    </rich:dataTable>               
                </td>
            </tr>
            <tr>
                <td class="boxAdd" align="right">
                    <h:commandLink action="#{plantaController.novaUsina}">
                        <h:graphicImage value="../imagens/adicionar-a.png"
                            onmouseover="this.src='../imagens/adicionar-b.png'"
                            onmouseout="this.src='../imagens/adicionar-a.png'"
                            styleClass="imgLink" />
                </h:commandLink></td>
            </tr>
        </table>
        <br>
        <!-- Botoes do cadastro -->
        <table cellspacing="0" border="0" class="boxCreateEdit" width="490">
            <tr>
                <td class="boxCreateEditRow"
                    style="padding-bottom: 11px; padding-top: 8px;" align="center">
                    <table cellspacing="0" border="0">
                        <tr>
                            <td>
                                <a4j:commandLink action="#{plantaController.gravaPlanta}">
                                    <div class="customButton"
                                        onmouseover="defineButtonStyleOver(this);"
                                        onmouseout="defineButtonStyleOut(this);">
                                        <h:outputLabel value="#{msg.button_save}" />
                                    </div>
                                </a4j:commandLink>
                            </td>
                            <td style="padding-left: 12px;">
                                <a4j:commandLink action="cancelarPlanta"
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
    <!-- Form modal de exclusao -->
    <rich:modalPanel id="conf_delete" minHeight="110" minWidth="300"   
            height="110" width="300">
            <f:facet name="header">   
                <h:outputLabel styleClass="hMessageDialog" value="#{msg.label_delete}" />   
            </f:facet>
            <center>           
               <h:outputLabel styleClass="hMessageDialog" value="#{msg.deletionQuestion}" /> <br>
               <h:form>
               <h:panelGrid columns="2">
                <h:column>
               <h:commandButton id="btnExcluir"
                    styleClass="customButton" 
                    action="#{plantaController.excluiObjeto}"
                    onclick="Richfaces.hideModalPanel('conf_delete')"
                    value="#{msg.button_yes}"/>
                </h:column>
                <h:column>
                <h:commandButton id="btnCancel"
                    styleClass="customButton"
                    value="#{msg.button_no}" 
                    onclick="Richfaces.hideModalPanel('conf_delete')" />
                </h:column>
                <h:column>
                </h:column>
              </h:panelGrid>       
              </h:form>   
            </center>
    </rich:modalPanel>
</f:view>
</body>
</html>