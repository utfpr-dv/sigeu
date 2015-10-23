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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author Tiago
 */
@Entity
@NamedQueries({ @NamedQuery(name = "Pessoa.findAll", query = "SELECT p FROM Pessoa p") })
public class Pessoa implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_pessoa")
	private Integer idPessoa;
	@Basic(optional = false)
	private String email;
	@Basic(optional = false)
	@Column(name = "nome_completo")
	private String nomeCompleto;
	@Basic(optional = false)
	@Column(name = "senha_md5")
	private String senhaMd5;
	@Basic(optional = false)
	@Column(name = "pessoa_fisica")
	private boolean pessoaFisica;
	@Column(name = "cnpj_cpf")
	private String cnpjCpf;
	private String matricula;
	@Basic(optional = false)
	private boolean ativo;
	@Basic(optional = false)
	private boolean admin;
	@Basic(optional = false)
	private boolean externo;

	@Column(name = "ldap_campus")
	@Basic(optional = false)
	private String ldapCampus;

	@ManyToMany(mappedBy = "pessoaList", fetch = FetchType.LAZY)
	private List<GrupoPessoa> grupoPessoaList;
	@ManyToMany(mappedBy = "pessoaList", fetch = FetchType.LAZY)
	private List<ItemReserva> itemReservaList;
	@JoinColumn(name = "id_campus", referencedColumnName = "id_campus")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Campus idCampus;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idPessoa", fetch = FetchType.LAZY)
	private List<Transacao> transacaoList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idPessoa", fetch = FetchType.LAZY)
	private List<ProfessorPessoa> professorPessoaList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idAutorizador", fetch = FetchType.LAZY)
	private List<Reserva> reservaList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idPessoa", fetch = FetchType.LAZY)
	private List<Reserva> reservaList1;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuario", fetch = FetchType.LAZY)
	private List<Reserva> reservaList2;

	public Pessoa() {
	}

	public Pessoa(Integer idPessoa) {
		this.idPessoa = idPessoa;
	}

	public Pessoa(Integer idPessoa, String email, String nomeCompleto, String senhaMd5, boolean pessoaFisica,
			boolean ativo, boolean admin, boolean externo) {
		this.idPessoa = idPessoa;
		this.email = email;
		this.nomeCompleto = nomeCompleto;
		this.senhaMd5 = senhaMd5;
		this.pessoaFisica = pessoaFisica;
		this.ativo = ativo;
		this.admin = admin;
		this.externo = externo;
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

	public boolean getAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean getExterno() {
		return externo;
	}

	public void setExterno(boolean externo) {
		this.externo = externo;
	}

	public List<GrupoPessoa> getGrupoPessoaList() {
		return grupoPessoaList;
	}

	public void setGrupoPessoaList(List<GrupoPessoa> grupoPessoaList) {
		this.grupoPessoaList = grupoPessoaList;
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

	public List<Reserva> getReservaList2() {
		return reservaList2;
	}

	public void setReservaList2(List<Reserva> reservaList2) {
		this.reservaList2 = reservaList2;
	}

	public String getLdapCampus() {
		return ldapCampus;
	}

	public void setLdapCampus(String ldapCampus) {
		this.ldapCampus = ldapCampus;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idPessoa != null ? idPessoa.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Pessoa)) {
			return false;
		}
		Pessoa other = (Pessoa) object;
		if ((this.idPessoa == null && other.idPessoa != null)
				|| (this.idPessoa != null && !this.idPessoa.equals(other.idPessoa))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "br.edu.utfpr.dv.sigeu.entities.Pessoa[ idPessoa=" + idPessoa + " ]";
	}

}
