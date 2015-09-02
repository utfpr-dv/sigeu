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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Tiago
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Feriado.findAll", query = "SELECT f FROM Feriado f")})
public class Feriado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_feriado")
    private Integer idFeriado;
    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    private Date data;
    @Basic(optional = false)
    private Character tipo;
    @Basic(optional = false)
    private String descricao;
    @JoinColumn(name = "id_campus", referencedColumnName = "id_campus")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Campus idCampus;

    public Feriado() {
    }

    public Feriado(Integer idFeriado) {
        this.idFeriado = idFeriado;
    }

    public Feriado(Integer idFeriado, Date data, Character tipo, String descricao) {
        this.idFeriado = idFeriado;
        this.data = data;
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public Integer getIdFeriado() {
        return idFeriado;
    }

    public void setIdFeriado(Integer idFeriado) {
        this.idFeriado = idFeriado;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Character getTipo() {
        return tipo;
    }

    public void setTipo(Character tipo) {
        this.tipo = tipo;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFeriado != null ? idFeriado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Feriado)) {
            return false;
        }
        Feriado other = (Feriado) object;
        if ((this.idFeriado == null && other.idFeriado != null) || (this.idFeriado != null && !this.idFeriado.equals(other.idFeriado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.utfpr.dv.sigeu.entities.Feriado[ idFeriado=" + idFeriado + " ]";
    }
    
}
