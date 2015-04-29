package br.edu.utfpr.dv.sigeu.exception;

public class EntidadePossuiRelacionamentoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2320731641493183957L;

	public EntidadePossuiRelacionamentoException(String entidade) {
		super("A entidade: " + entidade + " possui relacionamentos dependentes");
	}

}
