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
    @NamedQuery(name = "Classroom.findAll", query = "SELECT c FROM Classroom c")})
public class Classroom implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_classroom")
    private Integer idClassroom;
    @Basic(optional = false)
    private String id;
    @Basic(optional = false)
    private String name;
    @Basic(optional = false)
    private String shortname;
    @JoinColumn(name = "id_timetable", referencedColumnName = "id_timetable")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Timetable idTimetable;

    public Classroom() {
    }

    public Classroom(Integer idClassroom) {
        this.idClassroom = idClassroom;
    }

    public Classroom(Integer idClassroom, String id, String name, String shortname) {
        this.idClassroom = idClassroom;
        this.id = id;
        this.name = name;
        this.shortname = shortname;
    }

    public Integer getIdClassroom() {
        return idClassroom;
    }

    public void setIdClassroom(Integer idClassroom) {
        this.idClassroom = idClassroom;
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
        hash += (idClassroom != null ? idClassroom.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Classroom)) {
            return false;
        }
        Classroom other = (Classroom) object;
        if ((this.idClassroom == null && other.idClassroom != null) || (this.idClassroom != null && !this.idClassroom.equals(other.idClassroom))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.utfpr.dv.sigeu.entities.Classroom[ idClassroom=" + idClassroom + " ]";
    }
    
}
