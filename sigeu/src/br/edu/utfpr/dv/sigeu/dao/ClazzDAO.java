package br.edu.utfpr.dv.sigeu.dao;

import javax.ejb.Stateless;

import org.hibernate.Query;

import br.edu.utfpr.dv.sigeu.entities.Clazz;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;

@Stateless
public class ClazzDAO extends HibernateDAO<Clazz> {

    @Override
    public Clazz encontrePorId(Integer id) {
	String hql = "from Clazz o where o.idClazz = :id";
	Query q = session.createQuery(hql);
	q.setInteger("id", id);
	return (Clazz) q.uniqueResult();
    }

    @Override
    public String getNomeSequencia() {
	return "clazz";
    }

    @Override
    public void preCriacao(Clazz o) {
	Long id = this.gerarNovoId();
	o.setIdClazz(id.intValue());
    }

    @Override
    public void preAlteracao(Clazz o) {

    }
}