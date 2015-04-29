package br.edu.utfpr.dv.sigeu.exception;

public class ServidorLdapNaoCadastradoException extends Exception {

	private static final long serialVersionUID = 6160661794475970292L;

	public ServidorLdapNaoCadastradoException(String causa) {
		super("Falha de login: " + causa);
	}

}
