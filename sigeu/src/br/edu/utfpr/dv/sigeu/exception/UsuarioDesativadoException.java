package br.edu.utfpr.dv.sigeu.exception;

public class UsuarioDesativadoException extends Exception {

	private static final long serialVersionUID = 6160661794475970292L;

	public UsuarioDesativadoException(String usuario) {
		super("Acesso desativado para o usuário: " + usuario);
	}

}
