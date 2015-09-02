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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Tiago
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Clazz.findAll", query = "SELECT c FROM Clazz c")})
public class Clazz implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_clazz")
    private Integer idClazz;
    @Basic(optional = false)
    private String id;
    @Basic(optional = false)
    private String name;
    @Basic(optional = false)
    private String shortname;
    @JoinColumn(name = "id_timetable", referencedColumnName = "id_timetable")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Timetable idTimetable;

    public Clazz() {
    }

    public Clazz(Integer idClazz) {
        this.idClazz = idClazz;
    }

    public Clazz(Integer idClazz, String id, String name, String shortname) {
        this.idClazz = idClazz;
        this.id = id;
        this.name = name;
        this.shortname = shortname;
    }

    public Integer getIdClazz() {
        return idClazz;
    }

    public void setIdClazz(Integer idClazz) {
        this.idClazz = idClazz;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public Timetable getIdTimetable() {
        return idTimetable;
    }

    public void setIdTimetable(Timetable idTimetable) {
        this.idTimetable = idTimetable;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idClazz != null ? idClazz.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Clazz)) {
            return false;
        }
        Clazz other = (Clazz) object;
        if ((this.idClazz == null && other.idClazz != null) || (this.idClazz != null && !this.idClazz.equals(other.idClazz))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.utfpr.dv.sigeu.entities.Clazz[ idClazz=" + idClazz + " ]";
    }
    
}
