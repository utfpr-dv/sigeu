package br.edu.utfpr.dv.sigeu.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginFilter implements Filter {

	public static final String SESSION_USUARIO_AUTENTICADO = "email_usuario";

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		boolean loginOk = false;
		String email = null;
		HttpServletRequest requestHttp = ((HttpServletRequest) request);

		//System.out.println("LoginFilter: " + requestHttp.getRequestURI());

		Object emailSessao = requestHttp.getSession().getAttribute(SESSION_USUARIO_AUTENTICADO);

		if (emailSessao != null) {
			email = (String) emailSessao;
		}

		//System.out.println("E-mail registrado é: " + email);

		loginOk = (email != null && email.trim().length() > 0);

		if (!loginOk) {
			// Manda para página de login
			String contextPath = requestHttp.getContextPath();
			((HttpServletResponse) response).sendRedirect(contextPath + "/Login.xhtml");
		} else {
			// Continua na página solicitada
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}
}