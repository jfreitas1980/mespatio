function numeros(e){
    var nav4 = window.Event ? true : false;
    if(nav4) {
        var whichCode = e.which;
        if (whichCode == null) {
        	whichCode = e.keyCode;
        }
    } else {
    	var whichCode = e.keyCode;
        if (whichCode == null) {
        	whichCode = e.which;
        }
    }
    if ((whichCode > 47 && whichCode < 58) || whichCode == 8 || whichCode == 9 || whichCode == 0 || whichCode == 44) {
    	return true;
    } else {        
    	return false;
    }
}

function limitText(limitField, limitNum) {
    if (limitField.value.length > limitNum) {
        limitField.value = limitField.value.substring(0, limitNum);
    } 
}
