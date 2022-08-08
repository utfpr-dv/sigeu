package br.edu.utfpr.dv.sigeu;

import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;

@Stateless
public class SessionSingleton {

    @PersistenceContext(unitName = "sigeuPU")
    private Session session;

    public Session getSession() {
	return session;
    }
}