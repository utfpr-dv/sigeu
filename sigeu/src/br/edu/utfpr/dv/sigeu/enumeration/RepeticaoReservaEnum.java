package br.edu.utfpr.dv.sigeu.enumeration;

public enum RepeticaoReservaEnum {
	SEM_REPETICAO("N", "Sem repetição", 0), SEMANAL("S", "Semanal", 7), DIARIO("D", "Diário", 1);

	private String id;
	private String descricao;
	private int dias;

	private RepeticaoReservaEnum(String id, String descricao, int dias) {
		this.id = id;
		this.descricao = descricao;
		this.dias = dias;
	}

	public String getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public int getDias() {
		return dias;
	}

	public static RepeticaoReservaEnum getEnum(String id) {
		RepeticaoReservaEnum repeticaoReservaEnum = null;

		for (RepeticaoReservaEnum rre : RepeticaoReservaEnum.values()) {
			if (rre.getId().equals(id)) {
				repeticaoReservaEnum = rre;
				break;
			}
		}

		return repeticaoReservaEnum;
	}
}
