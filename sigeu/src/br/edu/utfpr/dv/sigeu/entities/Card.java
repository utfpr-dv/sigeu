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
    @NamedQuery(name = "Card.findAll", query = "SELECT c FROM Card c")})
public class Card implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_card")
    private Integer idCard;
    @Basic(optional = false)
    private String lessonid;
    @Basic(optional = false)
    private String classroomids;
    @Basic(optional = false)
    private String period;
    @Basic(optional = false)
    private String days;
    @JoinColumn(name = "id_timetable", referencedColumnName = "id_timetable")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Timetable idTimetable;

    public Card() {
    }

    public Card(Integer idCard) {
        this.idCard = idCard;
    }

    public Card(Integer idCard, String lessonid, String classroomids, String period, String days) {
        this.idCard = idCard;
        this.lessonid = lessonid;
        this.classroomids = classroomids;
        this.period = period;
        this.days = days;
    }

    public Integer getIdCard() {
        return idCard;
    }

    public void setIdCard(Integer idCard) {
        this.idCard = idCard;
    }

    public String getLessonid() {
        return lessonid;
    }

    public void setLessonid(String lessonid) {
        this.lessonid = lessonid;
    }

    public String getClassroomids() {
        return classroomids;
    }

    public void setClassroomids(String classroomids) {
        this.classroomids = classroomids;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
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
        hash += (idCard != null ? idCard.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Card)) {
            return false;
        }
        Card other = (Card) object;
        if ((this.idCard == null && other.idCard != null) || (this.idCard != null && !this.idCard.equals(other.idCard))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.utfpr.dv.sigeu.entities.Card[ idCard=" + idCard + " ]";
    }
    
}
