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
        <table cellspacing="0" border="0" class="boxCreateEdit" width="590">
            <tr>
                <td class="iFrameTitle">
                    <h:outputLabel value="#{msg.pierView_titulo}" />
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
                    <h:outputLabel value="#{msg.pierView_nome}: *" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;">
                    <h:inputText id="nomePier" label="#{msg.pierView_nome}"
                        value="#{pierController.pier.nomePier}" size="60"
                        required="true" maxlength="60">
                        <rich:ajaxValidator event="onblur" />
                    </h:inputText>
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.pierView_estadoOperacao}: *" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <h:selectOneMenu id="estadoOperacao" label="#{msg.pierView_estadoOperacao}"
                        onchange="#{pierController.pierSelecionado.estado}"
                        converter="estadoMaquinaEnumConversor"
                        value="#{pierController.pierSelecionado.estado}">          
                        <f:selectItems
                            value="#{pierController.estadoEstruturaList}" />
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
                        value="#{pierController.pierSelecionado.dtInicio}" />
                </td>
            </tr>            
        
        </table>
        <br>
        <!-- tabela de Carregadora de Navios-->
        <table cellspacing="0" border="0" class="painelTabela" width="590">
            <tr>
                <td class="box11">      
                    <rich:dataTable width="560" border="0" cellspacing="0"
                            value="#{pierController.pier.listaDeMetaCarregadoraDeNavios}"
                            var="obj" 
                            id="carregadoraNavios"
                            binding="#{pierController.dataTableCarregadora}"
                            rows="10"
                            styleClass="tabela" style="table-layout:fixed"
                            rowClasses="linhaPar, linhaImpar"
                            columnClasses="coluna1,coluna3,coluna3"
                            headerClass="cabecalho">
                        <rich:column>
							<f:facet name="header"><h:outputLabel value="#{msg.pierView_carregadora}" /></f:facet>
                            <h:outputText value="#{obj.nomeCarregadeiraDeNavios}"/>
                        </rich:column>
                        <rich:column>
							<f:facet name="header"><h:outputLabel value="#{msg.label_edit}" /></f:facet>
                            <h:commandLink action="#{pierController.alteraCarregadora}">
                                <h:graphicImage value="../imagens/editar-a.png"
                                    onmouseover="this.src='../imagens/editar-b.png'"
                                    onmouseout="this.src='../imagens/editar-a.png'" styleClass="imgLink" />
                                <f:param value="#{obj.idCarregadeiraDeNavios}" name="id" />
                            </h:commandLink>  
                        </rich:column>
                        <h:column>
							<f:facet name="header"><h:outputLabel value="#{msg.label_delete}" /></f:facet>
                            <a4j:commandLink action="#{pierController.selectCarregadora}" oncomplete="Richfaces.showModalPanel('conf_delete')" reRender="conf_delete">   
                                <h:graphicImage value="../imagens/excluir-a.png"
                                    onmouseover="this.src='../imagens/excluir-b.png'"
                                    onmouseout="this.src='../imagens/excluir-a.png'" width="20"
                                    height="21" styleClass="imgLink" />
                                <f:param value="#{obj.idCarregadeiraDeNavios}" name="id" />
                            </a4j:commandLink>
                        </h:column>                 
                        <f:facet name="footer">
                            <rich:datascroller id="lista" renderIfSinglePage="false" for="carregadoraNavios" maxPages="20"/>
                        </f:facet>
                    </rich:dataTable>
                </td>
            </tr>
            <tr>
                <td class="boxAdd" align="right">
                    <h:commandLink action="#{pierController.novaCarregadora}">
                        <h:graphicImage value="../imagens/adicionar-a.png"
                            onmouseover="this.src='../imagens/adicionar-b.png'"
                            onmouseout="this.src='../imagens/adicionar-a.png'"
                            styleClass="imgLink" />
                </h:commandLink></td>
            </tr>
        </table>
	<br>
        <!-- tabela de Berco-->
        <table cellspacing="0" border="0" class="painelTabela" width="590">
            <tr>
                <td class="box11">      
                    <rich:dataTable width="560" border="0" cellspacing="0"
                            value="#{pierController.pier.listaDeMetaBercosDeAtracacao}"
                            var="obj" 
                            id="berco"
                            binding="#{pierController.dataTableBerco}"
                            rows="10"
                            styleClass="tabela" style="table-layout:fixed"
                            rowClasses="linhaPar, linhaImpar"
                            columnClasses="coluna1,coluna3,coluna3"
                            headerClass="cabecalho">
                        <rich:column>
							<f:facet name="header"><h:outputLabel value="#{msg.pierView_berco}" /></f:facet>
                            <h:outputText value="#{obj.nomeBerco}"/>
                        </rich:column>
                        <rich:column>
							<f:facet name="header"><h:outputLabel value="#{msg.label_edit}" /></f:facet>
                            <h:commandLink action="#{pierController.alteraBerco}">
                                <h:graphicImage value="../imagens/editar-a.png"
                                    onmouseover="this.src='../imagens/editar-b.png'"
                                    onmouseout="this.src='../imagens/editar-a.png'" styleClass="imgLink" />
                                <f:param value="#{obj.idBerco}" name="id" />
                            </h:commandLink>  
                        </rich:column>  
                        <h:column>
							<f:facet name="header"><h:outputLabel value="#{msg.label_delete}" /></f:facet>
                            <a4j:commandLink action="#{pierController.selectBerco}" oncomplete="Richfaces.showModalPanel('conf_delete')" reRender="conf_delete">   
                                <h:graphicImage value="../imagens/excluir-a.png"
                                    onmouseover="this.src='../imagens/excluir-b.png'"
                                    onmouseout="this.src='../imagens/excluir-a.png'" width="20"
                                    height="21" styleClass="imgLink" />
                                <f:param value="#{obj.idBerco}" name="id" />                                                            
                            </a4j:commandLink>
                        </h:column>                 
                        <f:facet name="footer">
                            <rich:datascroller id="lista" renderIfSinglePage="false" for="berco" maxPages="20"/>
                        </f:facet>
                    </rich:dataTable>               
                </td>
            </tr>
            <tr>
                <td class="boxAdd" align="right">
                    <h:commandLink action="#{pierController.novoBerco}">
                        <h:graphicImage value="../imagens/adicionar-a.png"
                            onmouseover="this.src='../imagens/adicionar-b.png'"
                            onmouseout="this.src='../imagens/adicionar-a.png'"
                            styleClass="imgLink" />
                </h:commandLink></td>
            </tr>
        </table>
        <br>
        <!-- Botoes do cadastro -->
        <table cellspacing="0" border="0" class="boxCreateEdit" width="590">
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 11px; padding-top: 8px;" align="center">
                    <table cellspacing="0" border="0">
                        <tr>
                            <td>
                                <a4j:commandLink action="#{pierController.gravaPier}">
                                    <div class="customButton"
                                        onmouseover="defineButtonStyleOver(this);"
                                        onmouseout="defineButtonStyleOut(this);">
                                        <h:outputLabel value="#{msg.button_confirm}" />
                                    </div>
                                </a4j:commandLink>
                            </td>
                            <td style="padding-left: 12px;">
                                <a4j:commandLink action="#{pierController.cancelar}" >
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
                    action="#{pierController.excluiObjeto}"
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