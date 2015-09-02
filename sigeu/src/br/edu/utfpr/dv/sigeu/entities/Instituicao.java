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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author Tiago
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Instituicao.findAll", query = "SELECT i FROM Instituicao i")})
public class Instituicao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_instituicao")
    private Integer idInstituicao;
    @Basic(optional = false)
    private String sigla;
    @Basic(optional = false)
    private String nome;
    @Basic(optional = false)
    private boolean ativo;
    @Column(name = "url_logo")
    private String urlLogo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idInstituicao", fetch = FetchType.LAZY)
    private List<Campus> campusList;

    public Instituicao() {
    }

    public Instituicao(Integer idInstituicao) {
        this.idInstituicao = idInstituicao;
    }

    public Instituicao(Integer idInstituicao, String sigla, String nome, boolean ativo) {
        this.idInstituicao = idInstituicao;
        this.sigla = sigla;
        this.nome = nome;
        this.ativo = ativo;
    }

    public Integer getIdInstituicao() {
        return idInstituicao;
    }

    public void setIdInstituicao(Integer idInstituicao) {
        this.idInstituicao = idInstituicao;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
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

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public List<Campus> getCampusList() {
        return campusList;
    }

    public void setCampusList(List<Campus> campusList) {
        this.campusList = campusList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idInstituicao != null ? idInstituicao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Instituicao)) {
            return false;
        }
        Instituicao other = (Instituicao) object;
        if ((this.idInstituicao == null && other.idInstituicao != null) || (this.idInstituicao != null && !this.idInstituicao.equals(other.idInstituicao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.utfpr.dv.sigeu.entities.Instituicao[ idInstituicao=" + idInstituicao + " ]";
    }
    
}
