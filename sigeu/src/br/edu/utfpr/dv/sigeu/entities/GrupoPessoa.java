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
@Table(name = "grupo_pessoa")
@NamedQueries({
    @NamedQuery(name = "GrupoPessoa.findAll", query = "SELECT g FROM GrupoPessoa g")})
public class GrupoPessoa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_grupo_pessoa")
    private Integer idGrupoPessoa;
    @Basic(optional = false)
    private String nome;
    @JoinTable(name = "pessoa_grupo_pessoa", joinColumns = {
        @JoinColumn(name = "id_grupo_pessoa", referencedColumnName = "id_grupo_pessoa")}, inverseJoinColumns = {
        @JoinColumn(name = "id_pessoa", referencedColumnName = "id_pessoa")})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Pessoa> pessoaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idGrupoPessoa", fetch = FetchType.LAZY)
    private List<UriPermissao> uriPermissaoList;
    @JoinColumn(name = "id_campus", referencedColumnName = "id_campus")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Campus idCampus;

    public GrupoPessoa() {
    }

    public GrupoPessoa(Integer idGrupoPessoa) {
        this.idGrupoPessoa = idGrupoPessoa;
    }

    public GrupoPessoa(Integer idGrupoPessoa, String nome) {
        this.idGrupoPessoa = idGrupoPessoa;
        this.nome = nome;
    }

    public Integer getIdGrupoPessoa() {
        return idGrupoPessoa;
    }

    public void setIdGrupoPessoa(Integer idGrupoPessoa) {
        this.idGrupoPessoa = idGrupoPessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Pessoa> getPessoaList() {
        return pessoaList;
    }

    public void setPessoaList(List<Pessoa> pessoaList) {
        this.pessoaList = pessoaList;
    }

    public List<UriPermissao> getUriPermissaoList() {
        return uriPermissaoList;
    }

    public void setUriPermissaoList(List<UriPermissao> uriPermissaoList) {
        this.uriPermissaoList = uriPermissaoList;
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
        hash += (idGrupoPessoa != null ? idGrupoPessoa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GrupoPessoa)) {
            return false;
        }
        GrupoPessoa other = (GrupoPessoa) object;
        if ((this.idGrupoPessoa == null && other.idGrupoPessoa != null) || (this.idGrupoPessoa != null && !this.idGrupoPessoa.equals(other.idGrupoPessoa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.utfpr.dv.sigeu.entities.GrupoPessoa[ idGrupoPessoa=" + idGrupoPessoa + " ]";
    }
    
}
