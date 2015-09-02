/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.dv.sigeu.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Tiago
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Timetable.findAll", query = "SELECT t FROM Timetable t")})
public class Timetable implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_timetable")
    private Integer idTimetable;
    @Basic(optional = false)
    @Column(name = "nome_arquivo")
    private String nomeArquivo;
    @Basic(optional = false)
    @Column(name = "data_carregamento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCarregamento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTimetable", fetch = FetchType.LAZY)
    private List<Subject> subjectList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTimetable", fetch = FetchType.LAZY)
    private List<Teacher> teacherList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTimetable", fetch = FetchType.LAZY)
    private List<Period> periodList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTimetable", fetch = FetchType.LAZY)
    private List<Lesson> lessonList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTimetable", fetch = FetchType.LAZY)
    private List<Classroom> classroomList;
    @JoinColumn(name = "id_campus", referencedColumnName = "id_campus")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Campus idCampus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTimetable", fetch = FetchType.LAZY)
    private List<Clazz> clazzList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTimetable", fetch = FetchType.LAZY)
    private List<Card> cardList;

    public Timetable() {
    }

    public Timetable(Integer idTimetable) {
        this.idTimetable = idTimetable;
    }

    public Timetable(Integer idTimetable, String nomeArquivo, Date dataCarregamento) {
        this.idTimetable = idTimetable;
        this.nomeArquivo = nomeArquivo;
        this.dataCarregamento = dataCarregamento;
    }

    public Integer getIdTimetable() {
        return idTimetable;
    }

    public void setIdTimetable(Integer idTimetable) {
        this.idTimetable = idTimetable;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public Date getDataCarregamento() {
        return dataCarregamento;
    }

    public void setDataCarregamento(Date dataCarregamento) {
        this.dataCarregamento = dataCarregamento;
    }

    public List<Subject> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    public List<Teacher> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<Teacher> teacherList) {
        this.teacherList = teacherList;
    }

    public List<Period> getPeriodList() {
        return periodList;
    }

    public void setPeriodList(List<Period> periodList) {
        this.periodList = periodList;
    }

    public List<Lesson> getLessonList() {
        return lessonList;
    }

    public void setLessonList(List<Lesson> lessonList) {
        this.lessonList = lessonList;
    }

    public List<Classroom> getClassroomList() {
        return classroomList;
    }

    public void setClassroomList(List<Classroom> classroomList) {
        this.classroomList = classroomList;
    }

    public Campus getIdCampus() {
        return idCampus;
    }

    public void setIdCampus(Campus idCampus) {
        this.idCampus = idCampus;
    }

    public List<Clazz> getClazzList() {
        return clazzList;
    }

    public void setClazzList(List<Clazz> clazzList) {
        this.clazzList = clazzList;
    }

    public List<Card> getCardList() {
        return cardList;
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTimetable != null ? idTimetable.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Timetable)) {
            return false;
        }
        Timetable other = (Timetable) object;
        if ((this.idTimetable == null && other.idTimetable != null) || (this.idTimetable != null && !this.idTimetable.equals(other.idTimetable))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.utfpr.dv.sigeu.entities.Timetable[ idTimetable=" + idTimetable + " ]";
    }
    
}
