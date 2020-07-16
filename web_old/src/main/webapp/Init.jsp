<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<link rel="StyleSheet" href="css/visualIdentity.css" type="text/css" />
</head>
<body>
<f:view>
    <f:loadBundle var="msg" basename="messages" />
    <table width="1000" border="0" cellspacing="0">

        <tr>
            <td width="991" height="100" background="imagens/bkg-topo-slice.png"
                valign="bottom">
            <table border="0" cellspacing="0" height="100">
                <tr height="77">
                    <td width="32"></td>
                    <td>
                    <div class="appTitle"><h:outputLabel
                        value="#{msg.projectName}" /></div>
                    </td>
                </tr>
                <tr>
                    <td width="32"></td>
                    <td><jsp:include page="/menu.jsp" /></td>
                </tr>
            </table>
            </td>
            <td width="9" height="100"
                background="imagens/bkg-topo-slice -repeat.png"></td>
        </tr>
        <tr>
            <td align="center" colspan="2">
            <table border="0" cellspacing="0" align="center">
                <tr>
                    <td background="imagens/bkg-conteudo-slice.png" width="980"
                        height="500"><iframe id="contentArea" name="contentArea"
                        width="100%" height="100%" frameborder="0" style="background: url('imagens/bkg-conteudo-slice.png');"></iframe></td>
                </tr>
            </table>
            </td>
        </tr>
    </table>
</f:view>
</body>
</html>