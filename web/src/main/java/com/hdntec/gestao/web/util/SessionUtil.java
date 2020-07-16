package com.hdntec.gestao.web.util;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;


public class SessionUtil {
  private static ResourceBundle bundle = ResourceBundle.getBundle("messages",
			FacesContext.getCurrentInstance().getViewRoot().getLocale());

  public static void addErrorMessage(String msg){
	  msg = bundle.getString(msg);
	  FacesMessage facesMsg =new FacesMessage(FacesMessage.SEVERITY_ERROR,msg,msg);
	  FacesContext fc = FacesContext.getCurrentInstance();
	  fc.addMessage(null,facesMsg);	  
  }
  public static void addSucessMessage(String msg){
	  msg = bundle.getString(msg);
	  FacesMessage facesMsg =new FacesMessage(FacesMessage.SEVERITY_INFO,msg,msg);
	  FacesContext fc = FacesContext.getCurrentInstance();
	  fc.addMessage("sucessInfo",facesMsg);	  
  }

  public static void setBundle(ResourceBundle bundle) {
		SessionUtil.bundle = bundle;
	}
  
//Getters -----------------------------------------------------------------------------------

  public static Object getSessionMapValue(String key) {
      return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(key);
  }
  
  public static ResourceBundle getBundle() {
	  return bundle;
  }

  // Setters -----------------------------------------------------------------------------------


  public static void setSessionMapValue(String key, Object value) {
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(key, value);      
  }


  
}
