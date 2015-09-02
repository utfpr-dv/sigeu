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
import javax.persistence.OneToOne;

/**
 *
 * @author Tiago
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Campus.findAll", query = "SELECT c FROM Campus c")})
public class Campus implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_campus")
    private Integer idCampus;
    @Basic(optional = false)
    private String sigla;
    @Basic(optional = false)
    private String nome;
    @Column(name = "url_logo")
    private String urlLogo;
    @Basic(optional = false)
    private boolean ativo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCampus", fetch = FetchType.LAZY)
    private List<ItemReserva> itemReservaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCampus", fetch = FetchType.LAZY)
    private List<Pessoa> pessoaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCampus", fetch = FetchType.LAZY)
    private List<Transacao> transacaoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCampus", fetch = FetchType.LAZY)
    private List<LdapServer> ldapServerList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCampus", fetch = FetchType.LAZY)
    private List<Disciplina> disciplinaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCampus", fetch = FetchType.LAZY)
    private List<Reserva> reservaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCampus", fetch = FetchType.LAZY)
    private List<TipoReserva> tipoReservaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCampus", fetch = FetchType.LAZY)
    private List<PeriodoLetivo> periodoLetivoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCampus", fetch = FetchType.LAZY)
    private List<Classe> classeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCampus", fetch = FetchType.LAZY)
    private List<UriPermissao> uriPermissaoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCampus", fetch = FetchType.LAZY)
    private List<CategoriaItemReserva> categoriaItemReservaList;
    @JoinColumn(name = "id_instituicao", referencedColumnName = "id_instituicao")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Instituicao idInstituicao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCampus", fetch = FetchType.LAZY)
    private List<Timetable> timetableList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCampus", fetch = FetchType.LAZY)
    private List<Professor> professorList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCampus", fetch = FetchType.LAZY)
    private List<GrupoPessoa> grupoPessoaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCampus", fetch = FetchType.LAZY)
    private List<Feriado> feriadoList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "campus", fetch = FetchType.LAZY)
    private MailServer mailServer;

    public Campus() {
    }

    public Campus(Integer idCampus) {
        this.idCampus = idCampus;
    }

    public Campus(Integer idCampus, String sigla, String nome, boolean ativo) {
        this.idCampus = idCampus;
        this.sigla = sigla;
        this.nome = nome;
        this.ativo = ativo;
    }

    public Integer getIdCampus() {
        return idCampus;
    }

    public void setIdCampus(Integer idCampus) {
        this.idCampus = idCampus;
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

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
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

    public List<Pessoa> getPessoaList() {
        return pessoaList;
    }

    public void setPessoaList(List<Pessoa> pessoaList) {
        this.pessoaList = pessoaList;
    }

    public List<Transacao> getTransacaoList() {
        return transacaoList;
    }

    public void setTransacaoList(List<Transacao> transacaoList) {
        this.transacaoList = transacaoList;
    }

    public List<LdapServer> getLdapServerList() {
        return ldapServerList;
    }

    public void setLdapServerList(List<LdapServer> ldapServerList) {
        this.ldapServerList = ldapServerList;
    }

    public List<Disciplina> getDisciplinaList() {
        return disciplinaList;
    }

    public void setDisciplinaList(List<Disciplina> disciplinaList) {
        this.disciplinaList = disciplinaList;
    }

    public List<Reserva> getReservaList() {
        return reservaList;
    }

    public void setReservaList(List<Reserva> reservaList) {
        this.reservaList = reservaList;
    }

    public List<TipoReserva> getTipoReservaList() {
        return tipoReservaList;
    }

    public void setTipoReservaList(List<TipoReserva> tipoReservaList) {
        this.tipoReservaList = tipoReservaList;
    }

    public List<PeriodoLetivo> getPeriodoLetivoList() {
        return periodoLetivoList;
    }

    public void setPeriodoLetivoList(List<PeriodoLetivo> periodoLetivoList) {
        this.periodoLetivoList = periodoLetivoList;
    }

    public List<Classe> getClasseList() {
        return classeList;
    }

    public void setClasseList(List<Classe> classeList) {
        this.classeList = classeList;
    }

    public List<UriPermissao> getUriPermissaoList() {
        return uriPermissaoList;
    }

    public void setUriPermissaoList(List<UriPermissao> uriPermissaoList) {
        this.uriPermissaoList = uriPermissaoList;
    }

    public List<CategoriaItemReserva> getCategoriaItemReservaList() {
        return categoriaItemReservaList;
    }

    public void setCategoriaItemReservaList(List<CategoriaItemReserva> categoriaItemReservaList) {
        this.categoriaItemReservaList = categoriaItemReservaList;
    }

    public Instituicao getIdInstituicao() {
        return idInstituicao;
    }

    public void setIdInstituicao(Instituicao idInstituicao) {
        this.idInstituicao = idInstituicao;
    }

    public List<Timetable> getTimetableList() {
        return timetableList;
    }

    public void setTimetableList(List<Timetable> timetableList) {
        this.timetableList = timetableList;
    }

    public List<Professor> getProfessorList() {
        return professorList;
    }

    public void setProfessorList(List<Professor> professorList) {
        this.professorList = professorList;
    }

    public List<GrupoPessoa> getGrupoPessoaList() {
        return grupoPessoaList;
    }

    public void setGrupoPessoaList(List<GrupoPessoa> grupoPessoaList) {
        this.grupoPessoaList = grupoPessoaList;
    }

    public List<Feriado> getFeriadoList() {
        return feriadoList;
    }

    public void setFeriadoList(List<Feriado> feriadoList) {
        this.feriadoList = feriadoList;
    }

    public MailServer getMailServer() {
        return mailServer;
    }

    public void setMailServer(MailServer mailServer) {
        this.mailServer = mailServer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCampus != null ? idCampus.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Campus)) {
            return false;
        }
        Campus other = (Campus) object;
        if ((this.idCampus == null && other.idCampus != null) || (this.idCampus != null && !this.idCampus.equals(other.idCampus))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.utfpr.dv.sigeu.entities.Campus[ idCampus=" + idCampus + " ]";
    }
    
}
