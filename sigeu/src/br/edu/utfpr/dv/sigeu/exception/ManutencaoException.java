package br.edu.utfpr.dv.sigeu.exception;

public class ManutencaoException extends Exception {

	private static final long serialVersionUID = -416066179447509123L;

	public ManutencaoException() {
		super("Aplicativo em manutenção. Aguarde até o reestabelecimento dos serviços.");
	}

}
