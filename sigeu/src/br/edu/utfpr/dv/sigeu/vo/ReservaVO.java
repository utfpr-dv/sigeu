package br.edu.utfpr.dv.sigeu.vo;

import br.edu.utfpr.dv.sigeu.entities.Campus;

public class ReservaVO {
	// Campos visuais (são exibidos)
	private boolean excluir;
	private String nomeItemReserva;
	private String dataReserva;
	private String horaReserva;
	private String motivoReserva;
	private String usuarioReserva;
	//
	// Campos de controle (ocultos)
	private Campus campus;
	private Integer idReserva;

	public boolean isExcluir() {
		return excluir;
	}

	public void setExcluir(boolean excluir) {
		this.excluir = excluir;
	}

	public String getNomeItemReserva() {
		return nomeItemReserva;
	}

	public void setNomeItemReserva(String nomeItemReserva) {
		this.nomeItemReserva = nomeItemReserva;
	}

	public String getDataReserva() {
		return dataReserva;
	}

	public void setDataReserva(String dataReserva) {
		this.dataReserva = dataReserva;
	}

	public String getHoraReserva() {
		return horaReserva;
	}

	public void setHoraReserva(String horaReserva) {
		this.horaReserva = horaReserva;
	}

	public String getMotivoReserva() {
		return motivoReserva;
	}

	public void setMotivoReserva(String motivoReserva) {
		this.motivoReserva = motivoReserva;
	}

	public String getUsuarioReserva() {
		return usuarioReserva;
	}

	public void setUsuarioReserva(String usuarioReserva) {
		this.usuarioReserva = usuarioReserva;
	}

	public Campus getCampus() {
		return campus;
	}

	public void setCampus(Campus campus) {
		this.campus = campus;
	}

	public Integer getIdReserva() {
		return idReserva;
	}

	public void setIdReserva(Integer idReserva) {
		this.idReserva = idReserva;
	}

}
