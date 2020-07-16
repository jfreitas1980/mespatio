package com.hdntec.gestao.web.controller;

import java.io.ByteArrayInputStream;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import com.hdntec.gestao.web.util.SessionUtil;
import com.hdntec.gestao.web.vo.Permission;
import com.hdntec.gestao.web.vo.User;


//import com.hdntec.gestao.web.util.SessionUtil;

public class LoginControllerBean {

	private String login;

	private String password;

	 private User user;

	public void setLogin(String login) {
		this.login = login;
	}

	public String getLogin() {
		return login;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
	
	public String loginUser() {
		String returnedString = "failed";
		returnedString = executarLogin();
		return returnedString;
	}
	
	
	public String logout(){		
		executarLogout(this.login);		
		return "logout";
	}

	
	 private String executarLogin()
	    {
	       String result = "success";
		 Service service = new Service();
	        Call call;
	        try {
	            call = (Call) service.createCall();

	            call.setTargetEndpointAddress(SessionUtil.getBundle().getString("login.webservice.endpoint"));
	            QName name = new QName("login");
	            call.setOperationName(name);
	            String loginValue = this.login;
	            String senhaValue = new String(this.password);
	            String ret = (String) call.invoke(new Object[]{loginValue, senhaValue});

	            String code = ret.substring(0, 3);

	            if ("400".equals(code)) {
	            	result = "failed";	            	
	            } else {
	                String xml = ret.substring(3);
	                if ("200".equals(code)) {
	                    this.loadUser(xml);
	                    Permission permission = new Permission();
	                    permission.setName(SessionUtil.getBundle().getString("permissao.editar.planta"));
	                    if (!this.user.getProfile().getPermissions().contains(permission)) {
	                    	result = "failed";
	                    }
	                } else if ("500".equals(code)) {
	                    int initMessage = xml.indexOf("<MESSAGE>") + "<MESSAGE>".length();
	                    int endMessage = xml.indexOf("<\\MESSAGE>");
	                    String message = xml.substring(initMessage, endMessage);
	                    result = "failed";
	                } else {
	                	result = "failed";
	                }
	            }

	        } catch (JAXBException ex) {
	            ex.printStackTrace();
	        } catch (RemoteException ex) {
	        	ex.printStackTrace();
	        } catch (ServiceException ex) {
	        	ex.printStackTrace();
	        }
			return result;
	    }

	  private void loadUser(String xmlContent) throws JAXBException {
	        JAXBContext jaxbContext = JAXBContext
						.newInstance("com.hdntec.gestao.web.vo");
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

				ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes());

				this.user = (User) unmarshaller.unmarshal(inputStream);
	    }

	  private void executarLogout(String user) {
		    Service service = new Service();
		    Call call;
		    try {
		        call = (Call) service.createCall();

		        call.setTargetEndpointAddress(SessionUtil.getBundle().getString("login.webservice.endpoint"));
		        QName name = new QName("logout");
		        call.setOperationName(name);
		        /**TODO REMOVER HARD*/
		        String login = user;
		        call.invoke(new Object[]{login});
		    }catch (Exception e) {
		    	System.out.println(e.getClass().getName());
				e.printStackTrace();
			}
	   }
	  
}
