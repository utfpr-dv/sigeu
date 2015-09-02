/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.dv.sigeu.entities;

import java.io.Serializable;
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
import javax.persistence.Table;

/**
 *
 * @author Tiago
 */
@Entity
@Table(name = "tipo_reserva")
@NamedQueries({
    @NamedQuery(name = "TipoReserva.findAll", query = "SELECT t FROM TipoReserva t")})
public class TipoReserva implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_tipo_reserva")
    private Integer idTipoReserva;
    @Basic(optional = false)
    private String descricao;
    @Basic(optional = false)
    private boolean ativo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoReserva", fetch = FetchType.LAZY)
    private List<Reserva> reservaList;
    @JoinColumn(name = "id_campus", referencedColumnName = "id_campus")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Campus idCampus;

    public TipoReserva() {
    }

    public TipoReserva(Integer idTipoReserva) {
        this.idTipoReserva = idTipoReserva;
    }

    public TipoReserva(Integer idTipoReserva, String descricao, boolean ativo) {
        this.idTipoReserva = idTipoReserva;
        this.descricao = descricao;
        this.ativo = ativo;
    }

    public Integer getIdTipoReserva() {
        return idTipoReserva;
    }

    public void setIdTipoReserva(Integer idTipoReserva) {
        this.idTipoReserva = idTipoReserva;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public List<Reserva> getReservaList() {
        return reservaList;
    }

    public void setReservaList(List<Reserva> reservaList) {
        this.reservaList = reservaList;
    }

    public Campus getIdCampus() {
        return idCampus;
    }

    public void setIdCampus(Campus idCampus) {
        this.idCampus = idCampus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoReserva != null ? idTipoReserva.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoReserva)) {
            return false;
        }
        TipoReserva other = (TipoReserva) object;
        if ((this.idTipoReserva == null && other.idTipoReserva != null) || (this.idTipoReserva != null && !this.idTipoReserva.equals(other.idTipoReserva))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.utfpr.dv.sigeu.entities.TipoReserva[ idTipoReserva=" + idTipoReserva + " ]";
    }
    
}
