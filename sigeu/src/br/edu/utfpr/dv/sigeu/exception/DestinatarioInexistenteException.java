package br.edu.utfpr.dv.sigeu.exception;

public class DestinatarioInexistenteException extends Exception {

	private static final long serialVersionUID = -274745211512741693L;
	
	public DestinatarioInexistenteException(){
		super("Necessário informar pelo menos um endereço de e-mail em to, cc ou bcc.");
	}
}
