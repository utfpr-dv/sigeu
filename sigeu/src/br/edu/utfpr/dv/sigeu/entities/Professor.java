/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.dv.sigeu.entities;

import java.io.Serializable;
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
import javax.persistence.OneToOne;

/**
 *
 * @author Tiago
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Professor.findAll", query = "SELECT p FROM Professor p")})
public class Professor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_professor")
    private Integer idProfessor;
    private String codigo;
    @Basic(optional = false)
    private String name;
    @Basic(optional = false)
    private Character genero;
    private String cor;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "professor", fetch = FetchType.LAZY)
    private ProfessorPessoa professorPessoa;
    @JoinColumn(name = "id_campus", referencedColumnName = "id_campus")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Campus idCampus;

    public Professor() {
    }

    public Professor(Integer idProfessor) {
        this.idProfessor = idProfessor;
    }

    public Professor(Integer idProfessor, String name, Character genero) {
        this.idProfessor = idProfessor;
        this.name = name;
        this.genero = genero;
    }

    public Integer getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(Integer idProfessor) {
        this.idProfessor = idProfessor;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Character getGenero() {
        return genero;
    }

    public void setGenero(Character genero) {
        this.genero = genero;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public ProfessorPessoa getProfessorPessoa() {
        return professorPessoa;
    }

    public void setProfessorPessoa(ProfessorPessoa professorPessoa) {
        this.professorPessoa = professorPessoa;
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
        hash += (idProfessor != null ? idProfessor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Professor)) {
            return false;
        }
        Professor other = (Professor) object;
        if ((this.idProfessor == null && other.idProfessor != null) || (this.idProfessor != null && !this.idProfessor.equals(other.idProfessor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.utfpr.dv.sigeu.entities.Professor[ idProfessor=" + idProfessor + " ]";
    }
    
}
