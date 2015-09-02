/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.dv.sigeu.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Tiago
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Period.findAll", query = "SELECT p FROM Period p")})
public class Period implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_period")
    private Integer idPeriod;
    @Basic(optional = false)
    private String name;
    @Basic(optional = false)
    private String shortname;
    @Basic(optional = false)
    @Temporal(TemporalType.TIME)
    private Date starttime;
    @Basic(optional = false)
    @Temporal(TemporalType.TIME)
    private Date endtime;
    @Basic(optional = false)
    private int ordem;
    @JoinColumn(name = "id_timetable", referencedColumnName = "id_timetable")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Timetable idTimetable;

    public Period() {
    }

    public Period(Integer idPeriod) {
        this.idPeriod = idPeriod;
    }

    public Period(Integer idPeriod, String name, String shortname, Date starttime, Date endtime, int ordem) {
        this.idPeriod = idPeriod;
        this.name = name;
        this.shortname = shortname;
        this.starttime = starttime;
        this.endtime = endtime;
        this.ordem = ordem;
    }

    public Integer getIdPeriod() {
        return idPeriod;
    }

    public void setIdPeriod(Integer idPeriod) {
        this.idPeriod = idPeriod;
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

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
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
        hash += (idPeriod != null ? idPeriod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Period)) {
            return false;
        }
        Period other = (Period) object;
        if ((this.idPeriod == null && other.idPeriod != null) || (this.idPeriod != null && !this.idPeriod.equals(other.idPeriod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.utfpr.dv.sigeu.entities.Period[ idPeriod=" + idPeriod + " ]";
    }
    
}
