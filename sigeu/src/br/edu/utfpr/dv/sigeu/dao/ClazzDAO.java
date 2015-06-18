package br.edu.utfpr.dv.sigeu.dao;

import org.hibernate.Query;

import br.edu.utfpr.dv.sigeu.entities.Clazz;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class ClazzDAO extends HibernateDAO<Clazz> {

	public ClazzDAO(Transaction transaction) {
		super(transaction);
	}

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
		// TODO Auto-generated method stub
		
	}

}
