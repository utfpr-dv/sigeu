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
@Table(name = "vw_usuario_reserva")
@NamedQueries({ @NamedQuery(name = "VwUsuarioReserva.findAll", query = "SELECT v FROM VwUsuarioReserva v") })
public class VwUsuarioReserva implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id_campus")
	private Integer idCampus;
	@Column(name = "nome_usuario")
	private String nomeUsuario;

	public VwUsuarioReserva() {
	}

	public Integer getIdCampus() {
		return idCampus;
	}

	public void setIdCampus(Integer idCampus) {
		this.idCampus = idCampus;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

}
