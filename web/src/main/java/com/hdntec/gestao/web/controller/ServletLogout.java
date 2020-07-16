package com.hdntec.gestao.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hdntec.gestao.hibernate.HibernateUtil;


@SuppressWarnings("serial")
public class ServletLogout extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LoginControllerBean login = (LoginControllerBean) request.getSession()
				.getAttribute("loginControllerBean");
		if(login != null){
			login.logout();
		}
		
		//Fechando a sessao
		HibernateUtil.closeSession();
		
		response.sendRedirect("index.jsf");

	}

}
