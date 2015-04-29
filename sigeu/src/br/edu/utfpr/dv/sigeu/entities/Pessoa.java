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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Tiago
 */
@Entity
@Table(name = "pessoa")
@NamedQueries({
    @NamedQuery(name = "Pessoa.findAll", query = "SELECT p FROM Pessoa p")})
public class Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_pessoa")
    private Integer idPessoa;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="E-mail inválido")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 254)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nome_completo")
    private String nomeCompleto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "senha_md5")
    private String senhaMd5;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pessoa_fisica")
    private boolean pessoaFisica;
    @Size(max = 20)
    @Column(name = "cnpj_cpf")
    private String cnpjCpf;
    @Size(max = 32)
    @Column(name = "matricula")
    private String matricula;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ativo")
    private boolean ativo;
    @ManyToMany(mappedBy = "pessoaList", fetch = FetchType.LAZY)
    private List<GrupoPessoa> grupoPessoaList;
    @JoinColumn(name = "id_campus", referencedColumnName = "id_campus")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Campus idCampus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPessoa", fetch = FetchType.LAZY)
    private List<Transacao> transacaoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPessoa", fetch = FetchType.LAZY)
    private List<ProfessorPessoa> professorPessoaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPessoa", fetch = FetchType.LAZY)
    private List<Reserva> reservaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuario", fetch = FetchType.LAZY)
    private List<Reserva> reservaList1;

    public Pessoa() {
    }

    public Pessoa(Integer idPessoa) {
        this.idPessoa = idPessoa;
    }

    public Pessoa(Integer idPessoa, String email, String nomeCompleto, String senhaMd5, boolean pessoaFisica, boolean ativo) {
        this.idPessoa = idPessoa;
        this.email = email;
        this.nomeCompleto = nomeCompleto;
        this.senhaMd5 = senhaMd5;
        this.pessoaFisica = pessoaFisica;
        this.ativo = ativo;
    }

    public Integer getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Integer idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getSenhaMd5() {
        return senhaMd5;
    }

    public void setSenhaMd5(String senhaMd5) {
        this.senhaMd5 = senhaMd5;
    }

    public boolean getPessoaFisica() {
        return pessoaFisica;
    }

    public void setPessoaFisica(boolean pessoaFisica) {
        this.pessoaFisica = pessoaFisica;
    }

    public String getCnpjCpf() {
        return cnpjCpf;
    }

    public void setCnpjCpf(String cnpjCpf) {
        this.cnpjCpf = cnpjCpf;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public List<GrupoPessoa> getGrupoPessoaList() {
        return grupoPessoaList;
    }

    public void setGrupoPessoaList(List<GrupoPessoa> grupoPessoaList) {
        this.grupoPessoaList = grupoPessoaList;
    }

    public Campus getIdCampus() {
        return idCampus;
    }

    public void setIdCampus(Campus idCampus) {
        this.idCampus = idCampus;
    }

    public List<Transacao> getTransacaoList() {
        return transacaoList;
    }

    public void setTransacaoList(List<Transacao> transacaoList) {
        this.transacaoList = transacaoList;
    }

    public List<ProfessorPessoa> getProfessorPessoaList() {
        return professorPessoaList;
    }

    public void setProfessorPessoaList(List<ProfessorPessoa> professorPessoaList) {
        this.professorPessoaList = professorPessoaList;
    }

    public List<Reserva> getReservaList() {
        return reservaList;
    }

    public void setReservaList(List<Reserva> reservaList) {
        this.reservaList = reservaList;
    }

    public List<Reserva> getReservaList1() {
        return reservaList1;
    }

    public void setReservaList1(List<Reserva> reservaList1) {
        this.reservaList1 = reservaList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPessoa != null ? idPessoa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Pessoa)) {
            return false;
        }
        Pessoa other = (Pessoa) object;
        if ((this.idPessoa == null && other.idPessoa != null) || (this.idPessoa != null && !this.idPessoa.equals(other.idPessoa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.utfpr.dv.sigeu.entities.Pessoa[ idPessoa=" + idPessoa + " ]";
    }
    
}
