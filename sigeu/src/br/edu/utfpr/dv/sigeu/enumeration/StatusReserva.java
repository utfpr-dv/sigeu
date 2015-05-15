package br.edu.utfpr.dv.sigeu.enumeration;

public enum StatusReserva {
	EFETIVADA("E", "Efetivada"), 
	CANCELADA("C", "Cancelada"), 
	PENDENTE("P", "Pendente");

	private String status;
	private String descricao;

	private StatusReserva(String status, String descricao) {
		this.status = status;
		this.descricao = descricao;
	}

	public String getStatus() {
		return status;
	}

	public String getDescricao() {
		return descricao;
	}

}
