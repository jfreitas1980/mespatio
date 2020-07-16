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
        <table cellspacing="0" border="0">
            <tr>
                <td class="iFrameTitle">
                    <h:outputLabel value="#{msg.metaInternaList_titulo}" />
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
            <!-- Formulario de pesquisa -->
            <tr>
                <td class="boxSearchUserRowFilter" style="padding-top: 18px; padding-bottom: 4px;">        
                    <h:outputLabel value="#{msg.filter_title}" />
                </td>
            </tr>
            <tr>
                <td class="boxSearchUserRow">
                    <h:outputLabel value="#{msg.metaInternaList_tipopelota}" />
                </td>
            </tr>
            <tr>
                <td class="boxSearchUserRow">
                    <h:inputText id="NameFilter"
                        value="#{metaInternaController.metaInternaPesquisa.tipoPelota.descricaoTipoProduto}"
                        size="50" maxlength="50" />
                </td>
            </tr>
            <tr>
                <td class="boxSearchUserRow" style="padding-bottom: 18px; padding-top: 15px;" align="right">
                    <table cellspacing="0" border="0">
                        <tr>
                            <td>
                                <h:commandLink action="#{metaInternaController.buscaMetaInternaPorFiltro}">
                                    <div class="customButton"
                                        onmouseover="defineButtonStyleOver(this);"
                                        onmouseout="defineButtonStyleOut(this);">
                                        <h:outputLabel value="#{msg.button_search}" />
                                    </div>
                                </h:commandLink>
                            </td>
                            <td style="padding-left: 12px;">
                               <h:commandLink action="#{metaInternaController.limpaFiltro}"
                                   immediate="true">
                                   <div class="customButton"
                                       onmouseover="defineButtonStyleOver(this);"
                                       onmouseout="defineButtonStyleOut(this);">
                                       <h:outputLabel value="#{msg.button_clear}" />
                                   </div>
                               </h:commandLink>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <br>
        <!-- tabela de tipo-->
        <table cellspacing="0" border="0" class="painelTabela" width="790">
            <tr>
                <td class="box11">      
                    <rich:dataTable width="760" border="0" cellspacing="0"
                    		value="#{metaInternaController.metaInternaList}"
                            var="obj" 
                            id="metaInternaLista"
                            binding="#{metaInternaController.dataTableMetaInterna}"
                            rows="10"
                            styleClass="tabela" style="table-layout:fixed"
                            rowClasses="linhaPar, linhaImpar"
                            columnClasses="coluna1,coluna3,coluna3,coluna3,coluna3,coluna4"
                            headerClass="cabecalho">
                        <rich:column>    
							<f:facet name="header"><h:outputLabel value="#{msg.metaInternaList_tipopelota}" /></f:facet>
                            <h:outputText value="#{obj.tipoPelota.descricaoTipoProduto}"/>
                        </rich:column>
                        <rich:column>      
							<f:facet name="header"><h:outputLabel value="#{msg.metaInternaList_limitesuperior}" /></f:facet>
                            <h:outputText value="#{obj.limiteSuperiorValorMetaInterna}"/>
                        </rich:column>
                        <rich:column>      
							<f:facet name="header"><h:outputLabel value="#{msg.metaInternaList_limiteinferior}" /></f:facet>
                            <h:outputText value="#{obj.limiteInferiorValorMetaInterna}"/>
                        </rich:column>              
                        <rich:column>   
							<f:facet name="header"><h:outputLabel value="#{msg.metaInternaList_tipometa}" /></f:facet>
                            <h:outputText value="#{obj.tipoDaMetaInterna}"/>
                        </rich:column>       
                        <rich:column>   
							<f:facet name="header"><h:outputLabel value="#{msg.metaInternaList_dataInicio}" /></f:facet>
                            <h:outputText value="#{obj.dataDeCadastro}">
                              <f:convertDateTime pattern="#{msg.dateFormat}"/>
                            </h:outputText>
                        </rich:column>
                        <rich:column>
							<f:facet name="header"><h:outputLabel value="#{msg.label_edit}" /></f:facet>
                            <a4j:commandLink action="#{metaInternaController.alteraMetaInterna}">
                                <h:graphicImage value="../imagens/editar-a.png"
                                    onmouseover="this.src='../imagens/editar-b.png'"
                                    onmouseout="this.src='../imagens/editar-a.png'" styleClass="imgLink" />
                                <f:param value="#{obj.idMetaInterna}" name="id" />
                            </a4j:commandLink>  
                        </rich:column>  
                        <h:column>
							<f:facet name="header"><h:outputLabel value="#{msg.label_delete}" /></f:facet>
                            <a4j:commandLink action="#{metaInternaController.selectItem}" oncomplete="Richfaces.showModalPanel('conf_delete')" reRender="conf_delete">   
                                <h:graphicImage value="../imagens/excluir-a.png"
                                    onmouseover="this.src='../imagens/excluir-b.png'"
                                    onmouseout="this.src='../imagens/excluir-a.png'" width="20"
                                    height="21" styleClass="imgLink" />
                            </a4j:commandLink>
                        </h:column>                 
                        <f:facet name="footer">
                            <rich:datascroller id="lista" renderIfSinglePage="false" for="metaInternaLista" maxPages="20"/>
                        </f:facet>
                    </rich:dataTable>               
                </td>
            </tr>
            <tr>
                <td class="boxAdd" align="right">
                    <a4j:commandLink action="#{metaInternaController.novaMetaInterna}">
                        <h:graphicImage value="../imagens/adicionar-a.png"
                            onmouseover="this.src='../imagens/adicionar-b.png'"
                            onmouseout="this.src='../imagens/adicionar-a.png'"
                            styleClass="imgLink" />
                </a4j:commandLink></td>
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
                    action="#{metaInternaController.excluiMetaInterna}"
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