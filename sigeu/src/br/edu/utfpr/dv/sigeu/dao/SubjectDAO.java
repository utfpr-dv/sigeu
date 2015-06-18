package br.edu.utfpr.dv.sigeu.dao;

import org.hibernate.Query;

import br.edu.utfpr.dv.sigeu.entities.Subject;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class SubjectDAO extends HibernateDAO<Subject> {

	public SubjectDAO(Transaction transaction) {
		super(transaction);
	}

	@Override
	public Subject encontrePorId(Integer id) {
		String hql = "from Subject o where o.idSubject = :id";
		Query q = session.createQuery(hql);
		q.setInteger("id", id);
		return (Subject) q.uniqueResult();
	}

	@Override
	public String getNomeSequencia() {
		return "subject";
	}

	@Override
	public void preCriacao(Subject o) {
		Long id = this.gerarNovoId();
		o.setIdSubject(id.intValue());
	}

	@Override
	public void preAlteracao(Subject o) {
		// TODO Auto-generated method stub
		
	}

}
