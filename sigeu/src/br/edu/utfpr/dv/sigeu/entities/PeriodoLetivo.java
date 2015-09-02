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

/**
 *
 * @author Tiago
 */
@Entity
@Table(name = "periodo_letivo")
@NamedQueries({
    @NamedQuery(name = "PeriodoLetivo.findAll", query = "SELECT p FROM PeriodoLetivo p")})
public class PeriodoLetivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_periodo_letivo")
    private Integer idPeriodoLetivo;
    @Basic(optional = false)
    private String nome;
    @Basic(optional = false)
    @Column(name = "data_inicio")
    @Temporal(TemporalType.DATE)
    private Date dataInicio;
    @Basic(optional = false)
    @Column(name = "data_fim")
    @Temporal(TemporalType.DATE)
    private Date dataFim;
    @JoinColumn(name = "id_campus", referencedColumnName = "id_campus")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Campus idCampus;
    @JoinColumn(name = "id_transacao_reserva", referencedColumnName = "id_transacao")
    @ManyToOne(fetch = FetchType.LAZY)
    private Transacao idTransacaoReserva;

    public PeriodoLetivo() {
    }

    public PeriodoLetivo(Integer idPeriodoLetivo) {
        this.idPeriodoLetivo = idPeriodoLetivo;
    }

    public PeriodoLetivo(Integer idPeriodoLetivo, String nome, Date dataInicio, Date dataFim) {
        this.idPeriodoLetivo = idPeriodoLetivo;
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public Integer getIdPeriodoLetivo() {
        return idPeriodoLetivo;
    }

    public void setIdPeriodoLetivo(Integer idPeriodoLetivo) {
        this.idPeriodoLetivo = idPeriodoLetivo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Campus getIdCampus() {
        return idCampus;
    }

    public void setIdCampus(Campus idCampus) {
        this.idCampus = idCampus;
    }

    public Transacao getIdTransacaoReserva() {
        return idTransacaoReserva;
    }

    public void setIdTransacaoReserva(Transacao idTransacaoReserva) {
        this.idTransacaoReserva = idTransacaoReserva;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPeriodoLetivo != null ? idPeriodoLetivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PeriodoLetivo)) {
            return false;
        }
        PeriodoLetivo other = (PeriodoLetivo) object;
        if ((this.idPeriodoLetivo == null && other.idPeriodoLetivo != null) || (this.idPeriodoLetivo != null && !this.idPeriodoLetivo.equals(other.idPeriodoLetivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.utfpr.dv.sigeu.entities.PeriodoLetivo[ idPeriodoLetivo=" + idPeriodoLetivo + " ]";
    }
    
}
