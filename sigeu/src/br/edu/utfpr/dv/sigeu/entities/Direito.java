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
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Tiago
 */
@Entity
@Table(name = "direito")
@NamedQueries({ @NamedQuery(name = "Direito.findAll", query = "SELECT d FROM Direito d") })
public class Direito implements Serializable {
	private static final long serialVersionUID = 10192836471235L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_direito")
	private Integer idDireito;

	@JoinColumn(name = "id_pessoa", referencedColumnName = "id_pessoa")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Pessoa idPessoa;

	@JoinColumn(name = "id_campus", referencedColumnName = "id_campus")
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private Campus idCampus;

	public Direito() {
		
	}

	@Basic(optional = false)
	@Column(name = "processo")
	private String processo;

	@Basic(optional = false)
	@Column(name = "autoriza")
	private boolean autoriza;

	public Integer getIdDireito() {
		return idDireito;
	}

	public void setIdDireito(Integer idDireito) {
		this.idDireito = idDireito;
	}

	public Pessoa getIdPessoa() {
		return idPessoa;
	}

	public void setIdPessoa(Pessoa idPessoa) {
		this.idPessoa = idPessoa;
	}

	public Campus getIdCampus() {
		return idCampus;
	}

	public void setIdCampus(Campus idCampus) {
		this.idCampus = idCampus;
	}

	public String getProcesso() {
		return processo;
	}

	public void setProcesso(String processo) {
		this.processo = processo;
	}

	public boolean isAutoriza() {
		return autoriza;
	}

	public void setAutoriza(boolean autoriza) {
		this.autoriza = autoriza;
	}

}
