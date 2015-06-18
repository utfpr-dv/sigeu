package br.edu.utfpr.dv.sigeu.dao;

import org.hibernate.Query;

import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.MailServer;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class MailServerDAO extends HibernateDAO<MailServer> {

	public MailServerDAO(Transaction transaction) {
		super(transaction);
	}

	@Override
	public MailServer encontrePorId(Integer id) {
		return null;
	}

	public MailServer encontrePorCampus(Campus campus) {
		String hql = "from MailServer o where o.idCampus.idCampus = :id";
		Query q = session.createQuery(hql);
		q.setInteger("id", campus.getIdCampus());
		return (MailServer) q.uniqueResult();
	}

	@Override
	public String getNomeSequencia() {
		return "mailServer";
	}

	@Override
	public void preCriacao(MailServer o) {

	}

	@Override
	public void preAlteracao(MailServer o) {
		// TODO Auto-generated method stub
		
	}

}
