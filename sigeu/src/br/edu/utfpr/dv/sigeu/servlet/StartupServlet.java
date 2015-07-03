package br.edu.utfpr.dv.sigeu.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Classe responsável por iniciar todos os utilitários e outras classes
 * necessárias durante o início do servidor de aplicações. Esta classe não pode
 * conter lógica, apenas deve fazer a chamada às outras classes.
 * 
 * @author Tiago
 *
 */
public class StartupServlet extends HttpServlet {

	private static final long serialVersionUID = 2727235447782761659L;

	@Override
	public void init() throws ServletException {
		super.init();

		/**
		 * Cria as conexões iniciais do pool de conexões, diminuindo o tempo de
		 * resposta no primeiro login.
		 */
//		System.out.print("===> Iniciando pool de conexões... ");
//		int qtd = CampusService.contarCampus();
//		System.out.println("===> Pool de conexões iniciado para " + qtd
//				+ " campus");
	}

}
