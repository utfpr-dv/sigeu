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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Tiago
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Reserva.findAll", query = "SELECT r FROM Reserva r")})
public class Reserva implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_reserva")
    private Integer idReserva;
    @Basic(optional = false)
    @Column(name = "data_gravacao")
    @Temporal(TemporalType.DATE)
    private Date dataGravacao;
    @Basic(optional = false)
    @Column(name = "hora_gravacao")
    @Temporal(TemporalType.TIME)
    private Date horaGravacao;
    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    private Date data;
    @Basic(optional = false)
    @Column(name = "hora_inicio")
    @Temporal(TemporalType.TIME)
    private Date horaInicio;
    @Basic(optional = false)
    @Column(name = "hora_fim")
    @Temporal(TemporalType.TIME)
    private Date horaFim;
    @Basic(optional = false)
    @Column(name = "nome_usuario")
    private String nomeUsuario;
    @Basic(optional = false)
    @Column(name = "email_notificacao")
    private String emailNotificacao;
    @Basic(optional = false)
    private String motivo;
    @Basic(optional = false)
    private String rotulo;
    @Basic(optional = false)
    private String cor;
    @Basic(optional = false)
    private Character status;
    @Column(name = "motivo_cancelamento")
    private String motivoCancelamento;
    @Basic(optional = false)
    private boolean importado;
    @JoinColumn(name = "id_campus", referencedColumnName = "id_campus")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Campus idCampus;
    @JoinColumn(name = "id_item_reserva", referencedColumnName = "id_item_reserva")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ItemReserva idItemReserva;
    @JoinColumn(name = "id_autorizador", referencedColumnName = "id_pessoa")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Pessoa idAutorizador;
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

    public Reserva() {
    }

    public Reserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public Reserva(Integer idReserva, Date dataGravacao, Date horaGravacao, Date data, Date horaInicio, Date horaFim, String nomeUsuario, String emailNotificacao, String motivo, String rotulo, String cor, Character status, boolean importado) {
        this.idReserva = idReserva;
        this.dataGravacao = dataGravacao;
        this.horaGravacao = horaGravacao;
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.nomeUsuario = nomeUsuario;
        this.emailNotificacao = emailNotificacao;
        this.motivo = motivo;
        this.rotulo = rotulo;
        this.cor = cor;
        this.status = status;
        this.importado = importado;
    }

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
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

    public boolean getImportado() {
        return importado;
    }

    public void setImportado(boolean importado) {
        this.importado = importado;
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

    public Pessoa getIdAutorizador() {
        return idAutorizador;
    }

    public void setIdAutorizador(Pessoa idAutorizador) {
        this.idAutorizador = idAutorizador;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReserva != null ? idReserva.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
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
