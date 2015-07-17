package br.edu.utfpr.dv.sigeu.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.utfpr.dv.sigeu.util.LoginFilter;

public class LogoffServlet extends HttpServlet {

	private static final long serialVersionUID = 4453764842741961297L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("LogoffServlet.doGet");
		this.logoff(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("LogoffServlet.doPost");
		this.logoff(req, resp);
	}

	private void logoff(HttpServletRequest req, HttpServletResponse resp) {
		req.getSession().removeAttribute(LoginFilter.SESSION_EMAIL_LOGIN);
		String url = req.getContextPath();

		// Set response content type
		resp.setContentType("text/html");
		resp.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
		resp.setHeader("Location", url);
	}

}
