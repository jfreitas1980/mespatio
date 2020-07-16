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
                    value="#{msg.tipoItemControleView_titulo}" /></td>
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
                    <h:outputLabel value="#{msg.tipoItemControleView_descricao}: *" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <h:inputText id="descricao" label="#{msg.tipoItemControleView_descricao}"
                        value="#{tipoItemDeControleController.tipoItemDeControle.descricaoTipoItemControle}" size="60"
                        required="true" maxlength="60">
                        <rich:ajaxValidator event="onblur" />
                    </h:inputText>
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.tipoItemControleView_unidade}: *" /></td>
            </tr>
            
 			<tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <h:inputText id="unidade" label="#{msg.tipoItemControleView_unidade}"
                        value="#{tipoItemDeControleController.tipoItemDeControle.unidade}" size="60"
                        required="true" maxlength="60">
                        <rich:ajaxValidator event="onblur" />
                    </h:inputText>
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <h:selectOneMenu id="Operacao" label="#{msg.tipoItemControleView_tipoItemControle}"
                        onchange="#{tipoItemDeControleController.tipoItemDeControle.tipoItemControle}"
                        converter="tipoItemEnumConversor"
                        required="true"
                        value="#{tipoItemDeControleController.tipoItemDeControle.tipoItemControle}">          
                        <f:selectItems
                            value="#{tipoItemDeControleController.estadoEstruturaList}" />
                    </h:selectOneMenu>
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.tipoItemControleView_inicioescala}: *" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <h:inputText id="valorEscalaInicial" label="#{msg.tipoItemControleView_inicioescala}"
                        value="#{tipoItemDeControleController.tipoItemDeControle.inicioEscala}" size="10"
                        required="true" maxlength="10" onkeypress="return numeros(event);">
                        <f:validateDoubleRange
                            minimum="#{msg.tipoItemControleView_inicioescalaMinimo}"
                            maximum="#{msg.tipoItemControleView_inicioescalaMaximo}" />
                        <f:convertNumber minFractionDigits="0" maxFractionDigits="3"
                             locale="pt_BR" pattern="###,###.###"/>
                        <rich:ajaxValidator event="onblur" />
                    </h:inputText>
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.tipoItemControleView_fimescala}:*" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <h:inputText id="valorFinalEscala" label="#{msg.tipoItemControleView_fimescala}"
                        value="#{tipoItemDeControleController.tipoItemDeControle.fimEscala}" size="10"
                        required="true" maxlength="10" onkeypress="return numeros(event);">
                        <f:validateDoubleRange
                            minimum="#{msg.tipoItemControleView_fimescalaMinimo}"
                            maximum="#{msg.tipoItemControleView_fimescalaMaximo}" />
                        <f:convertNumber minFractionDigits="0" maxFractionDigits="3"
                             locale="pt_BR" pattern="###,###.###"/>
                        <rich:ajaxValidator event="onblur" />
                    </h:inputText>
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow" colspan="2">
                    <h:outputLabel value="#{msg.tipoItemControleView_multiplicidadeescala}: *" /></td>
            </tr>
            <tr>
                <td class="boxCreateEditRow" style="padding-bottom: 8px;" colspan="2">
                    <h:inputText id="multiplicidadeEscala" label="#{msg.tipoItemControleView_multiplicidadeescala}"
						value="#{tipoItemDeControleController.tipoItemDeControle.multiplicidadeEscala}" size="10"
                        required="true" maxlength="10" onkeypress="return numeros(event);">
                        <f:validateDoubleRange
                            minimum="#{msg.tipoItemControleView_multiplicidadeescalaMinimo}"
                            maximum="#{msg.tipoItemControleView_multiplicidadeescalaMaximo}" />
                       <f:convertNumber minFractionDigits="0" maxFractionDigits="3"
                             locale="pt_BR" pattern="###,###.###"/>
                        <rich:ajaxValidator event="onblur" />
                    </h:inputText>
                </td>
            </tr>
			<tr>
                <td class="boxCreateEditRow" colspan="4">
					<h:selectBooleanCheckbox label="#{msg.tipoItemControleView_relevante}" id="relevante" styleClass="checkbox" value="#{tipoItemDeControleController.tipoItemDeControle.relevante}" required="true" />
                    <h:outputText value="#{msg.tipoItemControleView_relevante} *" />
				</td>
            </tr>

			<tr>
				<td class="boxCreateEditRow" style="padding-bottom: 8px;">
					<table cellspacing="0" border="0" width="8" class="boxCreateEdit">
						<tr>
                			<td class="boxCreateEditRow" style="padding-top: 13px; padding-bottom: 4px;">
                    		<h:outputLabel value="#{msg.tipoItemControleView_pelletfeed}" /></td>
            			</tr>
						<tr>
							<td><hr></td>
						</tr>
						<tr style="border-top: 1px;">
                			<td class="boxCreateEditRow" style="padding-top: 7px; padding-bottom: 4px;">
                    		<h:outputLabel value="#{msg.tipoItemControleView_tipoprocesso}:" /></td>
            			</tr>
						<tr style="border-bottom: none;">
							<td class="boxCreateEditRow" style="padding-top: 4px; padding-bottom: 4px;">
								<h:inputText id="tipoProcesso" label="#{msg.tipoItemControleView_tipoprocesso}"
			                        value="#{tipoItemDeControleController.tipoItemDeControle.tipoProcessoPelletFeed}" 
			                        size="60" maxlength="255">
			                        <rich:ajaxValidator event="onblur" />
			                    </h:inputText>
							</td>
						</tr>
						<tr style="border-bottom: none;">
                			<td class="boxCreateEditRow" style="padding-top: 4px; padding-bottom: 4px;">
                    		<h:outputLabel value="#{msg.tipoItemControleView_arearesponsavel}:" /></td>
            			</tr>
						<tr style="border-bottom: none;">
							<td class="boxCreateEditRow" style="padding-top: 4px; padding-bottom: 4px;">
								<h:inputText id="areaResponsavel" label="#{msg.tipoItemControleView_arearesponsavel}"
			                        value="#{tipoItemDeControleController.tipoItemDeControle.areaRespEDPelletFeed}" size="60"
			                        maxlength="255">
			                        <rich:ajaxValidator event="onblur" />
			                    </h:inputText>
							</td>
						</tr>
						<tr style="border-bottom: none;">
                			<td class="boxCreateEditRow" style="padding-top: 4px; padding-bottom: 4px;">
                    		<h:outputLabel value="#{msg.tipoItemControleView_coditemcontrole}:" /></td>
            			</tr>
						<tr>
							<td class="boxCreateEditRow" style="padding-top: 4px; padding-bottom: 4px;">
								<h:inputText id="codItemControle" label="#{msg.tipoItemControleView_coditemcontrole}"
			                        value="#{tipoItemDeControleController.tipoItemDeControle.cdTipoItemControlePelletFeed}" size="10"
			                        maxlength="10" onkeypress="return numeros(event);">
			                        <f:validateDoubleRange
			                            minimum="#{msg.tipoItemControleView_coditemcontroleMinimo}"
			                            maximum="#{msg.tipoItemControleView_coditemcontroleMaximo}" />
			                        <f:convertNumber minFractionDigits="0" maxFractionDigits="0"
			                            pattern="##########" />
			                        <rich:ajaxValidator event="onblur" />
			                    </h:inputText>
							</td>
						</tr>
					</table>
				</td>
				<td class="boxCreateEditRow" style="padding-bottom: 8px;">
					<table cellspacing="0" border="0" width="8" class="boxCreateEdit">
						<tr>
                			<td class="boxCreateEditRow" style="padding-top: 13px; padding-bottom: 4px;">
                    		<h:outputLabel value="#{msg.tipoItemControleView_pelota}" /></td>
            			</tr>
						<tr>
							<td><hr></td>
						</tr>
						<tr>
                			<td class="boxCreateEditRow" style="padding-top: 7px; padding-bottom: 4px;">
                    		<h:outputLabel value="#{msg.tipoItemControleView_tipoprocesso}:" /></td>
            			</tr>
						<tr style="border-bottom: none;">
							<td class="boxCreateEditRow" style="padding-top: 4px; padding-bottom: 4px;">
								<h:inputText id="tipoProcessoPelota" label="#{msg.tipoItemControleView_tipoprocesso}"
			                        value="#{tipoItemDeControleController.tipoItemDeControle.tipoProcessoPelota}" size="60"
			                        maxlength="255">
			                        <rich:ajaxValidator event="onblur" />
			                    </h:inputText>
							</td>
						</tr>
						<tr style="border-bottom: none;">
                			<td class="boxCreateEditRow" style="padding-top: 4px; padding-bottom: 4px;">
                    		<h:outputLabel value="#{msg.tipoItemControleView_arearesponsavel}:" /></td>
            			</tr>
						<tr style="border-bottom: none;">
							<td class="boxCreateEditRow" style="padding-top: 4px; padding-bottom: 4px;">
								<h:inputText id="areaResponsavelPelota" label="#{msg.tipoItemControleView_arearesponsavel}"
			                        value="#{tipoItemDeControleController.tipoItemDeControle.areaRespEDPelota}" size="60"
			                        maxlength="255">
			                        <rich:ajaxValidator event="onblur" />
			                    </h:inputText>
							</td>
						</tr>
						<tr style="border-bottom: none;">
                			<td class="boxCreateEditRow" style="padding-top: 4px; padding-bottom: 4px;">
                    		<h:outputLabel value="#{msg.tipoItemControleView_coditemcontrole}:" /></td>
            			</tr>
						<tr>
							<td class="boxCreateEditRow" style="padding-top: 4px; padding-bottom: 4px;">
								<h:inputText id="codItemControlePelota" label="#{msg.tipoItemControleView_coditemcontrole}"
			                        value="#{tipoItemDeControleController.tipoItemDeControle.cdTipoItemControlePelota}" size="10"
			                        maxlength="10" onkeypress="return numeros(event);">
			                        <f:validateDoubleRange
			                            minimum="#{msg.tipoItemControleView_coditemcontroleMinimo}"
			                            maximum="#{msg.tipoItemControleView_coditemcontroleMaximo}" />
			                        <f:convertNumber minFractionDigits="0" maxFractionDigits="0"
			                            pattern="##########" />
			                        <rich:ajaxValidator event="onblur" />
			                    </h:inputText>
							</td>
						</tr>
					</table>
				</td>
			</tr>			
			<tr>
                <td class="boxCreateEditRow" colspan="2"
                    style="padding-bottom: 11px; padding-top: 8px;" align="center">
                    <table cellspacing="0" border="0">
                        <tr>
                            <td>
                                <h:commandLink action="#{tipoItemDeControleController.salvarTipoItemDeControle}">
                                    <div class="customButton"
                                        onmouseover="defineButtonStyleOver(this);"
                                        onmouseout="defineButtonStyleOut(this);">
                                        <h:outputLabel value="#{msg.button_save}" />
                                    </div>
                                </h:commandLink>
                            </td>
                            <td style="padding-left: 12px;">
                                <h:commandLink action="cancelarTipoItemDeControle"
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