package br.edu.utfpr.dv.sigeu.dao;

import org.hibernate.Query;

import br.edu.utfpr.dv.sigeu.entities.Classroom;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class ClassroomDAO extends HibernateDAO<Classroom> {

	public ClassroomDAO(Transaction transaction) {
		super(transaction);
	}

	@Override
	public Classroom encontrePorId(Integer id) {
		String hql = "from Classroom o where o.idClassroom = :id";
		Query q = session.createQuery(hql);
		q.setInteger("id", id);
		return (Classroom) q.uniqueResult();
	}

	@Override
	public String getNomeSequencia() {
		return "classroom";
	}

	@Override
	public void preCriacao(Classroom o) {
		Long id = this.gerarNovoId();
		o.setIdClassroom(id.intValue());
	}

	@Override
	public void preAlteracao(Classroom o) {
		// TODO Auto-generated method stub
		
	}

}
