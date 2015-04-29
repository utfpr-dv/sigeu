package br.edu.utfpr.dv.sigeu.vo;

/**
 * Simples VO para uso quando é necessário uma lista de vários objetos com
 * titulo e valor
 * 
 * @author Tiago
 *
 */
public class RotuloValorVO {
	private String rotulo;
	private String valor;

	public RotuloValorVO(String rotulo, String valor) {
		super();
		this.rotulo = rotulo;
		this.valor = valor;
	}

	public String getRotulo() {
		return rotulo;
	}

	public void setRotulo(String rotulo) {
		this.rotulo = rotulo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
