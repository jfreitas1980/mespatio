<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<style type="text/css">
@import "css/menu.css";
</style>
<script>

// JavaScript Document
function sendToContentArea(endereco){
    document.getElementById("contentArea").src=endereco;
}

function sendToTop(endereco){
	window.top.location=endereco;
}

</script>

<f:view>
    <f:loadBundle var="msg" basename="messages" />
    <ul id="nav">
        <li>
            <div id="basem"><a href="#"><h:outputLabel value="#{msg.menu_cadastros}" /></a></div>
                <ul>
                    <li><a href="#"
                        onclick="sendToContentArea('faces/cadastros/tipoProdutoList.jsp')" class="itemm"
                        onmouseover="this.setAttribute('class', 'itemmover');"
                        onmouseout="this.setAttribute('class', 'itemm');"><h:outputLabel
                        value="#{msg.menu_tipoproduto}" /></a>
                    </li>
                    <li><a href="#" onclick="sendToContentArea('faces/cadastros/metaInternaList.jsp')"
                        class="itemm" onmouseover="this.setAttribute('class', 'itemmover');"
                        onmouseout="this.setAttribute('class', 'itemm');"><h:outputLabel
                        value="#{msg.menu_metainterna}" /></a>
                    </li>
                    <li><a href="#" onclick="sendToContentArea('faces/cadastros/regraFarolList.jsp')"
                        class="itemm" onmouseover="this.setAttribute('class', 'itemmover');"
                        onmouseout="this.setAttribute('class', 'itemm');"><h:outputLabel
                        value="#{msg.menu_regrafarol}" /></a>
                    </li>
                    <li><a href="#" onclick="sendToContentArea('faces/cadastros/coeficienteDegradacaoList.jsp')"
                        class="itemm" onmouseover="this.setAttribute('class', 'itemmover');"
                        onmouseout="this.setAttribute('class', 'itemm');"><h:outputLabel
                        value="#{msg.menu_coeficiente}" /></a>
                    </li>                    
                    <li><a href="#" onclick="sendToContentArea('faces/cadastros/tipoItemControleList.jsp')"
                        class="itemm" onmouseover="this.setAttribute('class', 'itemmover');"
                        onmouseout="this.setAttribute('class', 'itemm');"><h:outputLabel
                        value="#{msg.menu_tipoitemdecontrole}" /></a>
                    </li>    
                    <li><a href="#" onclick="sendToContentArea('faces/cadastros/plantaList.jsp')"
                        class="itemm" onmouseover="this.setAttribute('class', 'itemmover');"
                        onmouseout="this.setAttribute('class', 'itemm');"><h:outputLabel
                        value="#{msg.menu_planta}" /></a>
                    </li>
					<li><a href="#"
						onclick="sendToTop('ServletLogout')" class="itemm"
						class="itemm" onmouseover="this.setAttribute('class', 'itemmover');"
                        onmouseout="this.setAttribute('class', 'itemm');">
						  <h:outputLabel value="#{msg.logout}" /></a>
                    </li>                    
                </ul>
        </li>
        <!-- <li>
            <div id="basem"><a href="#"><h:outputLabel value="#{msg.index_rollingStockCore}" /></a></div>
            <ul>
                <li><a href="#"
                    onclick="sendToContentArea('faces/rollingStockCore/locomotiveTypeList.jsp')" class="itemm"
                    onmouseover="this.setAttribute('class', 'itemmover');"
                    onmouseout="this.setAttribute('class', 'itemm');"><h:outputLabel
                    value="#{msg.index_rollingStockCore_locomotiveType}" /></a>
                </li>
                <li><a href="#" onclick="sendToContentArea('faces/rollingStockCore/locomotiveList.jsp')"
                    class="itemm" onmouseover="this.setAttribute('class', 'itemmover');"
                    onmouseout="this.setAttribute('class', 'itemm');"><h:outputLabel
                    value="#{msg.index_rollingStockCore_locomotive}" /></a>
                </li>
            </ul>
        </li> -->
    </ul>
</f:view>
