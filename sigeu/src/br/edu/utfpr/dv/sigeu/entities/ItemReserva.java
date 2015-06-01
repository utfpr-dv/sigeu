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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "item_reserva")
@NamedQueries({
    @NamedQuery(name = "ItemReserva.findAll", query = "SELECT i FROM ItemReserva i")})
public class ItemReserva implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_item_reserva")
    private Integer idItemReserva;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @Column(name = "rotulo")
    private String rotulo;
    @Column(name = "patrimonio")
    private String patrimonio;
    @Column(name = "detalhes")
    private String detalhes;
    @Basic(optional = false)
    @Column(name = "ativo")
    private boolean ativo;
    @Column(name = "codigo")
    private String codigo;
    @JoinTable(name = "autorizacao_item_reserva", joinColumns = {
        @JoinColumn(name = "id_item_reserva", referencedColumnName = "id_item_reserva")}, inverseJoinColumns = {
        @JoinColumn(name = "id_pessoa", referencedColumnName = "id_pessoa")})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Pessoa> pessoaList;
    @JoinColumn(name = "id_campus", referencedColumnName = "id_campus")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Campus idCampus;
    @JoinColumn(name = "id_categoria", referencedColumnName = "id_categoria")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CategoriaItemReserva idCategoria;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idItemReserva", fetch = FetchType.LAZY)
    private List<Reserva> reservaList;

    public ItemReserva() {
    }

    public ItemReserva(Integer idItemReserva) {
        this.idItemReserva = idItemReserva;
    }

    public ItemReserva(Integer idItemReserva, String nome, String rotulo, boolean ativo) {
        this.idItemReserva = idItemReserva;
        this.nome = nome;
        this.rotulo = rotulo;
        this.ativo = ativo;
    }

    public Integer getIdItemReserva() {
        return idItemReserva;
    }

    public void setIdItemReserva(Integer idItemReserva) {
        this.idItemReserva = idItemReserva;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRotulo() {
        return rotulo;
    }

    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getPatrimonio() {
        return patrimonio;
    }

    public void setPatrimonio(String patrimonio) {
        this.patrimonio = patrimonio;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    public boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public List<Pessoa> getPessoaList() {
        return pessoaList;
    }

    public void setPessoaList(List<Pessoa> pessoaList) {
        this.pessoaList = pessoaList;
    }

    public Campus getIdCampus() {
        return idCampus;
    }

    public void setIdCampus(Campus idCampus) {
        this.idCampus = idCampus;
    }

    public CategoriaItemReserva getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(CategoriaItemReserva idCategoria) {
        this.idCategoria = idCategoria;
    }

    public List<Reserva> getReservaList() {
        return reservaList;
    }

    public void setReservaList(List<Reserva> reservaList) {
        this.reservaList = reservaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idItemReserva != null ? idItemReserva.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemReserva)) {
            return false;
        }
        ItemReserva other = (ItemReserva) object;
        if ((this.idItemReserva == null && other.idItemReserva != null) || (this.idItemReserva != null && !this.idItemReserva.equals(other.idItemReserva))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.utfpr.dv.sigeu.entities.ItemReserva[ idItemReserva=" + idItemReserva + " ]";
    }
    
}
