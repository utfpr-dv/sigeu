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
    @NamedQuery(name = "Lesson.findAll", query = "SELECT l FROM Lesson l")})
public class Lesson implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_lesson")
    private Integer idLesson;
    @Basic(optional = false)
    private String id;
    @Basic(optional = false)
    private String classids;
    @Basic(optional = false)
    private String subjectids;
    @Basic(optional = false)
    private String teacherids;
    @Basic(optional = false)
    private String classroomids;
    @JoinColumn(name = "id_timetable", referencedColumnName = "id_timetable")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Timetable idTimetable;

    public Lesson() {
    }

    public Lesson(Integer idLesson) {
        this.idLesson = idLesson;
    }

    public Lesson(Integer idLesson, String id, String classids, String subjectids, String teacherids, String classroomids) {
        this.idLesson = idLesson;
        this.id = id;
        this.classids = classids;
        this.subjectids = subjectids;
        this.teacherids = teacherids;
        this.classroomids = classroomids;
    }

    public Integer getIdLesson() {
        return idLesson;
    }

    public void setIdLesson(Integer idLesson) {
        this.idLesson = idLesson;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassids() {
        return classids;
    }

    public void setClassids(String classids) {
        this.classids = classids;
    }

    public String getSubjectids() {
        return subjectids;
    }

    public void setSubjectids(String subjectids) {
        this.subjectids = subjectids;
    }

    public String getTeacherids() {
        return teacherids;
    }

    public void setTeacherids(String teacherids) {
        this.teacherids = teacherids;
    }

    public String getClassroomids() {
        return classroomids;
    }

    public void setClassroomids(String classroomids) {
        this.classroomids = classroomids;
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
        hash += (idLesson != null ? idLesson.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lesson)) {
            return false;
        }
        Lesson other = (Lesson) object;
        if ((this.idLesson == null && other.idLesson != null) || (this.idLesson != null && !this.idLesson.equals(other.idLesson))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.utfpr.dv.sigeu.entities.Lesson[ idLesson=" + idLesson + " ]";
    }
    
}
