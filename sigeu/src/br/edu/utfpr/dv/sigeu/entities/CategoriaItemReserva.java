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
@Table(name = "categoria_item_reserva")
@NamedQueries({
    @NamedQuery(name = "CategoriaItemReserva.findAll", query = "SELECT c FROM CategoriaItemReserva c")})
public class CategoriaItemReserva implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_categoria")
    private Integer idCategoria;
    @Basic(optional = false)
    private String nome;
    @Basic(optional = false)
    private boolean ativo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCategoria", fetch = FetchType.LAZY)
    private List<ItemReserva> itemReservaList;
    @JoinColumn(name = "id_campus", referencedColumnName = "id_campus")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Campus idCampus;

    public CategoriaItemReserva() {
    }

    public CategoriaItemReserva(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public CategoriaItemReserva(Integer idCategoria, String nome, boolean ativo) {
        this.idCategoria = idCategoria;
        this.nome = nome;
        this.ativo = ativo;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public List<ItemReserva> getItemReservaList() {
        return itemReservaList;
    }

    public void setItemReservaList(List<ItemReserva> itemReservaList) {
        this.itemReservaList = itemReservaList;
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
        hash += (idCategoria != null ? idCategoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CategoriaItemReserva)) {
            return false;
        }
        CategoriaItemReserva other = (CategoriaItemReserva) object;
        if ((this.idCategoria == null && other.idCategoria != null) || (this.idCategoria != null && !this.idCategoria.equals(other.idCategoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.utfpr.dv.sigeu.entities.CategoriaItemReserva[ idCategoria=" + idCategoria + " ]";
    }
    
}
