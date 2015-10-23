package br.edu.utfpr.dv.sigeu.exception;

public class CampusNaoLocalizadoException extends Exception {

	private static final long serialVersionUID = -3161793651821520522L;

	public CampusNaoLocalizadoException(){
		super("Campus nao localizado para o usuario informado. Informe o administrador do sistema.");
	}
	
}
