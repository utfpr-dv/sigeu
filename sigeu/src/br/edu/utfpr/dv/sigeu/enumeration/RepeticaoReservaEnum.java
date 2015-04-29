package br.edu.utfpr.dv.sigeu.enumeration;

public enum RepeticaoReservaEnum {
	SEM_REPETICAO("N", "Sem repetição"), SEMANAL("S", "Semanal");

	private String id;
	private String descricao;

	private RepeticaoReservaEnum(String id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public String getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
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
