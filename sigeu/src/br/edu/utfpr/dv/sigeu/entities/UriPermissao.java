/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.dv.sigeu.entities;

import java.io.Serializable;
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
import javax.persistence.Table;

/**
 *
 * @author Tiago
 */
@Entity
@Table(name = "uri_permissao")
@NamedQueries({
    @NamedQuery(name = "UriPermissao.findAll", query = "SELECT u FROM UriPermissao u")})
public class UriPermissao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_permissao")
    private Integer idPermissao;
    @Basic(optional = false)
    private String uri;
    @Basic(optional = false)
    private boolean acesso;
    @JoinColumn(name = "id_campus", referencedColumnName = "id_campus")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Campus idCampus;
    @JoinColumn(name = "id_grupo_pessoa", referencedColumnName = "id_grupo_pessoa")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private GrupoPessoa idGrupoPessoa;

    public UriPermissao() {
    }

    public UriPermissao(Integer idPermissao) {
        this.idPermissao = idPermissao;
    }

    public UriPermissao(Integer idPermissao, String uri, boolean acesso) {
        this.idPermissao = idPermissao;
        this.uri = uri;
        this.acesso = acesso;
    }

    public Integer getIdPermissao() {
        return idPermissao;
    }

    public void setIdPermissao(Integer idPermissao) {
        this.idPermissao = idPermissao;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean getAcesso() {
        return acesso;
    }

    public void setAcesso(boolean acesso) {
        this.acesso = acesso;
    }

    public Campus getIdCampus() {
        return idCampus;
    }

    public void setIdCampus(Campus idCampus) {
        this.idCampus = idCampus;
    }

    public GrupoPessoa getIdGrupoPessoa() {
        return idGrupoPessoa;
    }

    public void setIdGrupoPessoa(GrupoPessoa idGrupoPessoa) {
        this.idGrupoPessoa = idGrupoPessoa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPermissao != null ? idPermissao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UriPermissao)) {
            return false;
        }
        UriPermissao other = (UriPermissao) object;
        if ((this.idPermissao == null && other.idPermissao != null) || (this.idPermissao != null && !this.idPermissao.equals(other.idPermissao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.utfpr.dv.sigeu.entities.UriPermissao[ idPermissao=" + idPermissao + " ]";
    }
    
}
