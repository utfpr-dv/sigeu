package br.edu.utfpr.dv.sigeu.enumeration;

public enum StatusReserva {
	EFETIVADA('E', "Efetivada"), CANCELADA('C', "Cancelada"), PENDENTE('P',
			"Pendente");

	private Character status;
	private String descricao;

	private StatusReserva(Character status, String descricao) {
		this.status = status;
		this.descricao = descricao;
	}

	public Character getStatus() {
		return status;
	}

	public String getDescricao() {
		return descricao;
	}

	public static StatusReserva getFromStatus(Character status) {
		switch (status) {
		case 'E':
			return EFETIVADA;

		case 'C':
			return CANCELADA;

		case 'P':
			return PENDENTE;
		}
		return null;
	}

}
