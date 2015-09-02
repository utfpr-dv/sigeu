/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.dv.sigeu.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Tiago
 */
@Entity
@Table(name = "vw_reserva_livre")
@NamedQueries({ @NamedQuery(name = "VwReservaLivre.findAll", query = "SELECT v FROM VwReservaLivre v") })
public class VwReservaLivre implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id_reserva")
	private Integer idReserva;
	@Column(name = "id_transacao")
	private Integer idTransacao;
	@Column(name = "id_campus")
	private Integer idCampus;
	@Column(name = "data_gravacao")
	@Temporal(TemporalType.DATE)
	private Date dataGravacao;
	@Column(name = "hora_gravacao")
	@Temporal(TemporalType.TIME)
	private Date horaGravacao;
	@Temporal(TemporalType.DATE)
	private Date data;
	@Column(name = "hora_inicio")
	@Temporal(TemporalType.TIME)
	private Date horaInicio;
	@Column(name = "hora_fim")
	@Temporal(TemporalType.TIME)
	private Date horaFim;
	@Column(name = "id_tipo_reserva")
	private Integer idTipoReserva;
	@Column(name = "id_item_reserva")
	private Integer idItemReserva;
	@Column(name = "id_pessoa")
	private Integer idPessoa;
	@Column(name = "id_autorizador")
	private Integer idAutorizador;
	@Column(name = "id_usuario")
	private Integer idUsuario;
	@Column(name = "nome_usuario")
	private String nomeUsuario;
	@Column(name = "email_notificacao")
	private String emailNotificacao;
	private String motivo;
	private String rotulo;
	private String cor;
	private Character status;
	@Column(name = "motivo_cancelamento")
	private String motivoCancelamento;
	private Boolean importado;

	public VwReservaLivre() {
	}

	public Integer getIdReserva() {
		return idReserva;
	}

	public void setIdReserva(Integer idReserva) {
		this.idReserva = idReserva;
	}

	public Integer getIdTransacao() {
		return idTransacao;
	}

	public void setIdTransacao(Integer idTransacao) {
		this.idTransacao = idTransacao;
	}

	public Integer getIdCampus() {
		return idCampus;
	}

	public void setIdCampus(Integer idCampus) {
		this.idCampus = idCampus;
	}

	public Date getDataGravacao() {
		return dataGravacao;
	}

	public void setDataGravacao(Date dataGravacao) {
		this.dataGravacao = dataGravacao;
	}

	public Date getHoraGravacao() {
		return horaGravacao;
	}

	public void setHoraGravacao(Date horaGravacao) {
		this.horaGravacao = horaGravacao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Date horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Date getHoraFim() {
		return horaFim;
	}

	public void setHoraFim(Date horaFim) {
		this.horaFim = horaFim;
	}

	public Integer getIdTipoReserva() {
		return idTipoReserva;
	}

	public void setIdTipoReserva(Integer idTipoReserva) {
		this.idTipoReserva = idTipoReserva;
	}

	public Integer getIdItemReserva() {
		return idItemReserva;
	}

	public void setIdItemReserva(Integer idItemReserva) {
		this.idItemReserva = idItemReserva;
	}

	public Integer getIdPessoa() {
		return idPessoa;
	}

	public void setIdPessoa(Integer idPessoa) {
		this.idPessoa = idPessoa;
	}

	public Integer getIdAutorizador() {
		return idAutorizador;
	}

	public void setIdAutorizador(Integer idAutorizador) {
		this.idAutorizador = idAutorizador;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getEmailNotificacao() {
		return emailNotificacao;
	}

	public void setEmailNotificacao(String emailNotificacao) {
		this.emailNotificacao = emailNotificacao;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getRotulo() {
		return rotulo;
	}

	public void setRotulo(String rotulo) {
		this.rotulo = rotulo;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getMotivoCancelamento() {
		return motivoCancelamento;
	}

	public void setMotivoCancelamento(String motivoCancelamento) {
		this.motivoCancelamento = motivoCancelamento;
	}

	public Boolean getImportado() {
		return importado;
	}

	public void setImportado(Boolean importado) {
		this.importado = importado;
	}

}
