/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.dv.sigeu.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Tiago
 */
@Entity
@Table(name = "vw_professor_pessoa")
@NamedQueries({ @NamedQuery(name = "VwProfessorPessoa.findAll", query = "SELECT v FROM VwProfessorPessoa v") })
public class VwProfessorPessoa implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id_professor")
	private Integer idProfessor;
	@Column(name = "nome_professor")
	private String nomeProfessor;
	@Column(name = "id_pessoa")
	private Integer idPessoa;
	@Column(name = "nome_pessoa")
	private String nomePessoa;

	public VwProfessorPessoa() {
	}

	public Integer getIdProfessor() {
		return idProfessor;
	}

	public void setIdProfessor(Integer idProfessor) {
		this.idProfessor = idProfessor;
	}

	public String getNomeProfessor() {
		return nomeProfessor;
	}

	public void setNomeProfessor(String nomeProfessor) {
		this.nomeProfessor = nomeProfessor;
	}

	public Integer getIdPessoa() {
		return idPessoa;
	}

	public void setIdPessoa(Integer idPessoa) {
		this.idPessoa = idPessoa;
	}

	public String getNomePessoa() {
		return nomePessoa;
	}

	public void setNomePessoa(String nomePessoa) {
		this.nomePessoa = nomePessoa;
	}

}
