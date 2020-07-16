<%@taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@taglib uri="http://richfaces.org/a4j" prefix="a4j" %>
<%@taglib uri="http://richfaces.org/rich" prefix="rich" %>
<html>
<head>
    <title></title>
    <link rel="StyleSheet" href="../css/visualIdentity.css" type="text/css" />
	<link rel="StyleSheet" href="../css/panel.css" type="text/css" />
    <script type="text/javascript" src="../js/buttons.js" ></script>
    <script type="text/javascript" src="../js/funcoes.js" ></script>
</head>
<body class="body_background">
<f:view>  
    <f:loadBundle basename="messages" var="msg" />
    <h:form prependId="false">
        <table cellspacing="0" border="0" class="boxCreateEdit" width="630">
            <tr>
                <td class="iFrameTitle">
                    <h:outputLabel value="#{msg.patioView_titulo}" />
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
                    <h:outputLabel value="#{msg.patioView_nome}: *" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;">
                    <h:inputText id="nomePatio" label="#{msg.patioView_nome}"
                        value="#{patioController.patio.nomePatio}" size="60"
                        required="true" maxlength="60">
                        <rich:ajaxValidator event="onblur" />
                    </h:inputText>
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow">
                    <h:outputLabel value="#{msg.patioView_estadoOperacao}: *" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;">
                    <h:selectOneMenu id="estadoOperacao" label="#{msg.patioView_estadoOperacao}"
                        onchange="#{patioController.patio.estado}"
                        converter="estadoEstruturaEnumConversor"
                        required="true"
                        value="#{patioController.patio.estado}">          
                        <f:selectItems
                            value="#{patioController.estadoEstruturaList}" />
                    </h:selectOneMenu>                    
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow">
                    <h:outputLabel value="#{msg.patioView_numero}: *" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;">
                    <h:inputText id="Numero" label="#{msg.patioView_numero}"
                        value="#{patioController.patio.numero}"
						required="true" onkeypress="return numeros(event);"
                        size="10" maxlength="10" >
                        <f:validateDoubleRange
                            minimum="#{msg.patioView_numeroMinimo}"
                            maximum="#{msg.patioView_numeroMaximo}" />
                        <f:convertNumber minFractionDigits="0" maxFractionDigits="0"
                            pattern="##########" />
                    </h:inputText>
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow">
                    <h:outputLabel value="#{msg.patioView_largura}: *" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;">
                    <h:inputText id="Largura" label="#{msg.patioView_largura}"
                        value="#{patioController.patio.largura}"
						required="true" onkeypress="return numeros(event);"
                        size="10" maxlength="10" >
                        <f:validateDoubleRange
                            minimum="#{msg.patioView_larguraMinima}"
                            maximum="#{msg.patioView_larguraMaxima}" />
                        <f:convertNumber minFractionDigits="0" maxFractionDigits="2"
                            pattern="#######.##" />
                    </h:inputText>
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow">
                    <h:outputLabel value="#{msg.patioView_comprimento}: *" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;">
                    <h:inputText id="Comprimento" label="#{msg.patioView_comprimento}"
                        value="#{patioController.patio.comprimento}"
						required="true" onkeypress="return numeros(event);"
                        size="10" maxlength="10" >
                        <f:validateDoubleRange
                            minimum="#{msg.patioView_comprimentoMinimo}"
                            maximum="#{msg.patioView_comprimentoMaximo}" />
                        <f:convertNumber minFractionDigits="0" maxFractionDigits="2"
                            pattern="#######.##" />
                    </h:inputText>
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow">
                    <h:outputLabel value="#{msg.patioView_capacidade}: *" />
                </td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;">
                    <h:inputText id="capacidadeMaxima" label="#{msg.patioView_capacidade}"
                        value="#{patioController.patio.capacidadeMaxima}"
						required="true" onkeypress="return numeros(event);"
                        size="10" maxlength="10" >
                        <f:validateDoubleRange
                            minimum="#{msg.patioView_capacidadeMinima}"
                            maximum="#{msg.patioView_capacidadeMaxima}" />
                        <f:convertNumber minFractionDigits="0" maxFractionDigits="2"
                            pattern="#######.##" />
                    </h:inputText>
                </td>
            </tr>
        </table>
        <br>
        <!-- tabela de Balizas-->
        <table cellspacing="0" border="0" class="painelTabela" width="630">
            <tr>
                <td class="box11" colspan="3">      
                    <rich:dataTable width="600" border="0" cellspacing="0"
                            value="#{patioController.balizaList}"
                            var="obj"
                            binding="#{patioController.dataTableBaliza}"
                            id="balizas" 
                            rows="10"
                            styleClass="tabela" style="table-layout:fixed"
                            rowClasses="linhaPar, linhaImpar"
                            columnClasses="coluna200,coluna200,coluna3,coluna3"
                            headerClass="cabecalho">
                        <rich:column>
							<f:facet name="header"><h:outputLabel value="#{msg.patioView_balizaInicial}" /></f:facet>
                            <h:outputText value="#{obj.balizaInicial.nomeBaliza}"/>
                        </rich:column>
                        <rich:column>
							<f:facet name="header"><h:outputLabel value="#{msg.patioView_balizaFinal}" /></f:facet>
                            <h:outputText value="#{obj.balizaFinal.nomeBaliza}"/>
                        </rich:column>
                        <rich:column>
							<f:facet name="header"><h:outputLabel value="#{msg.patioView_balizaTipo}" /></f:facet>
                            <h:outputText value="#{obj.tipo}"/>
                        </rich:column>                  
                        <h:column>
							<f:facet name="header"><h:outputLabel value="#{msg.label_delete}" /></f:facet>
                            <a4j:commandLink action="#{patioController.selectBalizas}" oncomplete="Richfaces.showModalPanel('conf_delete')" reRender="conf_delete">   
                                <h:graphicImage value="../imagens/excluir-a.png"
                                    onmouseover="this.src='../imagens/excluir-b.png'"
                                    onmouseout="this.src='../imagens/excluir-a.png'" width="20"
                                    height="21" styleClass="imgLink" />
                            </a4j:commandLink>
                        </h:column>                 
                        <f:facet name="footer">
                            <rich:datascroller id="lista" renderIfSinglePage="false" for="balizas" maxPages="20"/>
                        </f:facet>
                    </rich:dataTable>               
                </td>
            </tr>
            <tr>
				<td class="boxSearchUserRow" align="left" colspan="2">
					<h:outputText value="#{msg.patioView_qtdBaliza}:"/>
                    <h:inputText id="qtdBalizas"
                        value="#{patioController.qtdBalizas}"
                        size="10" maxlength="4" >
                        <f:validateDoubleRange
                            minimum="#{msg.patioView_qtdBalizaMinima}"
                            maximum="#{msg.patioView_qtdBalizaMaxima}" />
                        <f:convertNumber minFractionDigits="0" maxFractionDigits="0"
                            pattern="####" />
                    </h:inputText>
                </td>
                <td class="boxAdd" align="left">
                    <a4j:commandLink action="#{patioController.novaBaliza}">
                        <h:graphicImage value="../imagens/adicionar-a.png"
                            onmouseover="this.src='../imagens/adicionar-b.png'"
                            onmouseout="this.src='../imagens/adicionar-a.png'"
                            styleClass="imgLink" />
                </a4j:commandLink></td>
            </tr>
        </table>
        <br>
        <!-- tabela de Maquinas -->
        <table cellspacing="0" border="0" class="painelTabela" width="630">
            <tr>
                <td class="box11">      
                    <rich:dataTable width="600" border="0" cellspacing="0"
                            value="#{patioController.patio.listaDeMaquinasDoPatio}"
                            var="obj"
                            binding="#{patioController.dataTableMaquina}"
                            id="maquinas" 
                            rows="10"
                            styleClass="tabela" style="table-layout:fixed"
                            rowClasses="linhaPar, linhaImpar"
                            columnClasses="coluna300,coluna150,coluna150"
                            headerClass="cabecalho">
                        <rich:column>
							<f:facet name="header"><h:outputLabel value="#{msg.patioView_maquina}" /></f:facet>
                            <h:outputText value="#{obj.nomeMaquina}"/>
                        </rich:column>
                        <rich:column>
							<f:facet name="header"><h:outputLabel value="#{msg.label_edit}" /></f:facet>
                            <h:commandLink action="#{patioController.alteraMaquina}">
                                <h:graphicImage value="../imagens/editar-a.png"
                                    onmouseover="this.src='../imagens/editar-b.png'"
                                    onmouseout="this.src='../imagens/editar-a.png'" styleClass="imgLink" />
                                <f:param value="#{obj.idMaquina}" name="id" />
                            </h:commandLink>  
                        </rich:column>
                        <h:column>
							<f:facet name="header"><h:outputLabel value="#{msg.label_delete}" /></f:facet>
                            <a4j:commandLink action="#{patioController.selectMaquina}" oncomplete="Richfaces.showModalPanel('conf_delete')" reRender="conf_delete">
                                <h:graphicImage value="../imagens/excluir-a.png"
                                    onmouseover="this.src='../imagens/excluir-b.png'"
                                    onmouseout="this.src='../imagens/excluir-a.png'" width="20"
                                    height="21" styleClass="imgLink" />
                                <f:param value="#{obj.idMaquina}" name="id" />
                            </a4j:commandLink>
                        </h:column>
                        <f:facet name="footer">
                            <rich:datascroller id="lista" renderIfSinglePage="false" for="maquinas" maxPages="20"/>
                        </f:facet>
                    </rich:dataTable>
                </td>
            </tr>
            <tr>
                <td class="boxAdd" align="right">
                    <h:commandLink action="#{patioController.novaMaquina}">
                        <h:graphicImage value="../imagens/adicionar-a.png"
                            onmouseover="this.src='../imagens/adicionar-b.png'"
                            onmouseout="this.src='../imagens/adicionar-a.png'"
                            styleClass="imgLink" />
                </h:commandLink></td>
            </tr>
        </table>
        <br>
        <!-- Botoes do cadastro -->
        <table cellspacing="0" border="0" class="boxCreateEdit" width="630">
            <tr>
                <td class="boxCreateEditRow"
                    style="padding-bottom: 11px; padding-top: 8px;" align="center">
                    <table cellspacing="0" border="0">
                        <tr>
                            <td>
                                <a4j:commandLink action="#{patioController.gravaPatio}">
                                    <div class="customButton"
                                        onmouseover="defineButtonStyleOver(this);"
                                        onmouseout="defineButtonStyleOut(this);">
                                        <h:outputLabel value="#{msg.button_confirm}" />
                                    </div>
                                </a4j:commandLink>
                            </td>
                            <td style="padding-left: 12px;">
                                <a4j:commandLink action="voltarPatio"
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
                    action="#{patioController.excluiObjeto}"
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