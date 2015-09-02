/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.dv.sigeu.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Tiago
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Transacao.findAll", query = "SELECT t FROM Transacao t")})
public class Transacao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_transacao")
    private Integer idTransacao;
    @Basic(optional = false)
    @Column(name = "data_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;
    @Basic(optional = false)
    private String descricao;
    @JoinColumn(name = "id_campus", referencedColumnName = "id_campus")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Campus idCampus;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id_pessoa")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Pessoa idPessoa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTransacao", fetch = FetchType.LAZY)
    private List<Reserva> reservaList;
    @OneToMany(mappedBy = "idTransacaoReserva", fetch = FetchType.LAZY)
    private List<PeriodoLetivo> periodoLetivoList;

    public Transacao() {
    }

    public Transacao(Integer idTransacao) {
        this.idTransacao = idTransacao;
    }

    public Transacao(Integer idTransacao, Date dataHora, String descricao) {
        this.idTransacao = idTransacao;
        this.dataHora = dataHora;
        this.descricao = descricao;
    }

    public Integer getIdTransacao() {
        return idTransacao;
    }

    public void setIdTransacao(Integer idTransacao) {
        this.idTransacao = idTransacao;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Campus getIdCampus() {
        return idCampus;
    }

    public void setIdCampus(Campus idCampus) {
        this.idCampus = idCampus;
    }

    public Pessoa getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Pessoa idPessoa) {
        this.idPessoa = idPessoa;
    }

    public List<Reserva> getReservaList() {
        return reservaList;
    }

    public void setReservaList(List<Reserva> reservaList) {
        this.reservaList = reservaList;
    }

    public List<PeriodoLetivo> getPeriodoLetivoList() {
        return periodoLetivoList;
    }

    public void setPeriodoLetivoList(List<PeriodoLetivo> periodoLetivoList) {
        this.periodoLetivoList = periodoLetivoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTransacao != null ? idTransacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transacao)) {
            return false;
        }
        Transacao other = (Transacao) object;
        if ((this.idTransacao == null && other.idTransacao != null) || (this.idTransacao != null && !this.idTransacao.equals(other.idTransacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.utfpr.dv.sigeu.entities.Transacao[ idTransacao=" + idTransacao + " ]";
    }
    
}
