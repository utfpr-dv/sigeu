package br.edu.utfpr.dv.sigeu.exception;

public class DatabaseConfigException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5558016537855154617L;

	public DatabaseConfigException() {
		super("Parametro invalido no arquivo database.properties");
	}

}
