/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.dv.sigeu.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Tiago
 */
@Entity
@Table(name = "reserva")
@NamedQueries({ @NamedQuery(name = "Reserva.findAll", query = "SELECT r FROM Reserva r") })
public class Reserva implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "id_reserva")
	private Integer idReserva;
	@Basic(optional = false)
	@NotNull
	@Column(name = "data")
	@Temporal(TemporalType.DATE)
	private Date data;
	@Basic(optional = false)
	@NotNull
	@Column(name = "hora_inicio")
	@Temporal(TemporalType.TIME)
	private Date horaInicio;
	@Basic(optional = false)
	@NotNull
	@Column(name = "hora_fim")
	@Temporal(TemporalType.TIME)
	private Date horaFim;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 256)
	@Column(name = "email_notificacao")
	private String emailNotificacao;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 4000)
	@Column(name = "motivo")
	private String motivo;
	@JoinColumn(name = "id_campus", referencedColumnName = "id_campus")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Campus idCampus;
	@JoinColumn(name = "id_item_reserva", referencedColumnName = "id_item_reserva")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private ItemReserva idItemReserva;
	@JoinColumn(name = "id_pessoa", referencedColumnName = "id_pessoa")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Pessoa idPessoa;
	@JoinColumn(name = "id_usuario", referencedColumnName = "id_pessoa")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Pessoa idUsuario;
	@JoinColumn(name = "id_tipo_reserva", referencedColumnName = "id_tipo_reserva")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoReserva idTipoReserva;
	@JoinColumn(name = "id_transacao", referencedColumnName = "id_transacao")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Transacao idTransacao;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 32)
	@Column(name = "rotulo")
	private String rotulo;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 12)
	@Column(name = "cor")
	private String cor = "#BBD2D2";

	public Reserva() {
	}

	public Reserva(Integer idReserva) {
		this.idReserva = idReserva;
	}

	public Reserva(Integer idReserva, Date data, Date horaInicio, Date horaFim, String emailNotificacao, String motivo) {
		this.idReserva = idReserva;
		this.data = data;
		this.horaInicio = horaInicio;
		this.horaFim = horaFim;
		this.emailNotificacao = emailNotificacao;
		this.motivo = motivo;
	}

	public Integer getIdReserva() {
		return idReserva;
	}

	public void setIdReserva(Integer idReserva) {
		this.idReserva = idReserva;
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

	public Campus getIdCampus() {
		return idCampus;
	}

	public void setIdCampus(Campus idCampus) {
		this.idCampus = idCampus;
	}

	public ItemReserva getIdItemReserva() {
		return idItemReserva;
	}

	public void setIdItemReserva(ItemReserva idItemReserva) {
		this.idItemReserva = idItemReserva;
	}

	public Pessoa getIdPessoa() {
		return idPessoa;
	}

	public void setIdPessoa(Pessoa idPessoa) {
		this.idPessoa = idPessoa;
	}

	public Pessoa getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Pessoa idUsuario) {
		this.idUsuario = idUsuario;
	}

	public TipoReserva getIdTipoReserva() {
		return idTipoReserva;
	}

	public void setIdTipoReserva(TipoReserva idTipoReserva) {
		this.idTipoReserva = idTipoReserva;
	}

	public Transacao getIdTransacao() {
		return idTransacao;
	}

	public void setIdTransacao(Transacao idTransacao) {
		this.idTransacao = idTransacao;
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

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idReserva != null ? idReserva.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Reserva)) {
			return false;
		}
		Reserva other = (Reserva) object;
		if ((this.idReserva == null && other.idReserva != null) || (this.idReserva != null && !this.idReserva.equals(other.idReserva))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "br.edu.utfpr.dv.sigeu.entities.Reserva[ idReserva=" + idReserva + " ]";
	}

}
