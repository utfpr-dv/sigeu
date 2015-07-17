package br.edu.utfpr.dv.sigeu.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class HistoryFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

//		HttpServletRequest requestHttp = ((HttpServletRequest) request);
//
//		String uri = "";
//
//		if (request instanceof HttpServletRequest) {
//			uri = requestHttp.getRequestURI();
//		}

		//ConfigHistory.getInstance().addHistoryEntry(uri);
		//System.out.println(ConfigHistory.getInstance().toString());

		// Continua na página solicitada
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}
}