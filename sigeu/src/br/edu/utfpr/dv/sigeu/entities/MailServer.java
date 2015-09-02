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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Tiago
 */
@Entity
@Table(name = "mail_server")
@NamedQueries({
    @NamedQuery(name = "MailServer.findAll", query = "SELECT m FROM MailServer m")})
public class MailServer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_campus")
    private Integer idCampus;
    @Basic(optional = false)
    @Column(name = "authentication_required")
    private boolean authenticationRequired;
    @Basic(optional = false)
    private boolean starttls;
    @Basic(optional = false)
    private boolean ssl;
    @Basic(optional = false)
    @Column(name = "plain_text_over_tls")
    private boolean plainTextOverTls;
    @Basic(optional = false)
    private String host;
    @Basic(optional = false)
    private int port;
    @Basic(optional = false)
    @Column(name = "from_email")
    private String fromEmail;
    @Basic(optional = false)
    @Column(name = "user_name")
    private String userName;
    @Basic(optional = false)
    private String password;
    @JoinColumn(name = "id_campus", referencedColumnName = "id_campus", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Campus campus;

    public MailServer() {
    }

    public MailServer(Integer idCampus) {
        this.idCampus = idCampus;
    }

    public MailServer(Integer idCampus, boolean authenticationRequired, boolean starttls, boolean ssl, boolean plainTextOverTls, String host, int port, String fromEmail, String userName, String password) {
        this.idCampus = idCampus;
        this.authenticationRequired = authenticationRequired;
        this.starttls = starttls;
        this.ssl = ssl;
        this.plainTextOverTls = plainTextOverTls;
        this.host = host;
        this.port = port;
        this.fromEmail = fromEmail;
        this.userName = userName;
        this.password = password;
    }

    public Integer getIdCampus() {
        return idCampus;
    }

    public void setIdCampus(Integer idCampus) {
        this.idCampus = idCampus;
    }

    public boolean getAuthenticationRequired() {
        return authenticationRequired;
    }

    public void setAuthenticationRequired(boolean authenticationRequired) {
        this.authenticationRequired = authenticationRequired;
    }

    public boolean getStarttls() {
        return starttls;
    }

    public void setStarttls(boolean starttls) {
        this.starttls = starttls;
    }

    public boolean getSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public boolean getPlainTextOverTls() {
        return plainTextOverTls;
    }

    public void setPlainTextOverTls(boolean plainTextOverTls) {
        this.plainTextOverTls = plainTextOverTls;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCampus != null ? idCampus.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MailServer)) {
            return false;
        }
        MailServer other = (MailServer) object;
        if ((this.idCampus == null && other.idCampus != null) || (this.idCampus != null && !this.idCampus.equals(other.idCampus))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.utfpr.dv.sigeu.entities.MailServer[ idCampus=" + idCampus + " ]";
    }
    
}
