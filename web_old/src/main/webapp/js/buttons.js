var common = 
	"background-repeat: no-repeat;" +
	"height: 27px;" +
	"width: 70px;" +
	"padding-top: 5px;" +
	"text-align: center;" +
	"vertical-align: bottom;" +
	"font-family: Verdana;" +
	"font-size: 11px;";

var buttomOver = 
	"color: #FFFFFF;" +
	"background-image: url(../imagens/botao_01-b.png);" +
	common;

var buttomOut = 
	"color: #383838;" +
	"background-image: url(../imagens/botao_01-a.png);" +
	common;

function defineButtonStyleOver(button){
	defineButtonStyle(button, buttomOver);
}

function defineButtonStyleOut(button){
	defineButtonStyle(button, buttomOut);
}

function defineButtonStyle(button, css){
	if(window.attachEvent) {
         button.style.setAttribute('cssText',css);
    } else {
         button.setAttribute('style',css);
    }
}