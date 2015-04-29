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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Tiago
 */
@Entity
@Table(name = "period")
public class Period implements Serializable {
	private static final long serialVersionUID = 1981273912176L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "id_period")
	private Integer idPeriod;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "name")
	private String name;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "shortname")
	private String shortname;
	@Basic(optional = false)
	@NotNull
	@Column(name = "starttime")
	@Temporal(TemporalType.TIME)
	private Date starttime;
	@Basic(optional = false)
	@NotNull
	@Column(name = "endtime")
	@Temporal(TemporalType.TIME)
	private Date endtime;
	@JoinColumn(name = "id_timetable", referencedColumnName = "id_timetable")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Timetable idTimetable;

	@Basic(optional = false)
	@NotNull
	@Column(name = "ordem")
	private Integer ordem;

	public Period() {
	}

	public Period(Integer idPeriod) {
		this.idPeriod = idPeriod;
	}

	public Period(Integer idPeriod, String name, String shortname, Date starttime, Date endtime) {
		this.idPeriod = idPeriod;
		this.name = name;
		this.shortname = shortname;
		this.starttime = starttime;
		this.endtime = endtime;
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

	public Timetable getIdTimetable() {
		return idTimetable;
	}

	public void setIdTimetable(Timetable idTimetable) {
		this.idTimetable = idTimetable;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idPeriod != null ? idPeriod.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
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
