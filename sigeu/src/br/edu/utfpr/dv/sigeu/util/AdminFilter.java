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

import br.edu.utfpr.dv.sigeu.config.Config;

public class AdminFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		boolean loginOk = false;

		HttpServletRequest requestHttp = ((HttpServletRequest) request);

		String uri = "";
		// String queryString = "";

		if (request instanceof HttpServletRequest) {
			uri = requestHttp.getRequestURI();
			// url = ((HttpServletRequest) request).getRequestURL().toString();
			// queryString = ((HttpServletRequest) request).getQueryString();
		}

		// System.out.println("URI: " + uri);

		if (uri.startsWith("/sigeu/admin/")) {
			loginOk = Config.getInstance().getPessoaLogin().getAdmin();
		} else {
			loginOk = true;
		}

		if (!loginOk) {
			// Manda para página de erro
			String contextPath = requestHttp.getContextPath();
			((HttpServletResponse) response).sendRedirect(contextPath
					+ "/Oops.xhtml");
		} else {
			// Continua na página solicitada
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}
}