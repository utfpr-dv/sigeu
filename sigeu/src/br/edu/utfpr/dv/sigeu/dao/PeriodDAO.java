package br.edu.utfpr.dv.sigeu.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.Period;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class PeriodDAO extends HibernateDAO<Period> {

	public PeriodDAO(Transaction transaction) {
		super(transaction);
	}

	@Override
	public Period encontrePorId(Integer id) {
		String hql = "from Period o where o.idPeriod = :id";
		Query q = session.createQuery(hql);
		q.setInteger("id", id);
		return (Period) q.uniqueResult();
	}

	public Period encontrePorNome(Integer idTimetable, String nome) {
		String hql = "from Period o where o.name = :nome and o.idTimetable.idTimetable = :id";
		Query q = session.createQuery(hql);
		q.setString("nome", nome);
		q.setInteger("id", idTimetable);
		return (Period) q.uniqueResult();
	}

	@Override
	public String getNomeSequencia() {
		return "period";
	}

	@Override
	public void preCriacao(Period o) {
		// Deve vir preenchido.
		//Long id = this.gerarNovoId();
		//o.setIdPeriod(id.intValue());
	}

	public List<Period> getAll(Campus campus) {
		String hql = "SELECT o from Period o JOIN o.idTimetable t where t.idCampus.idCampus = :idCampus";
		Query q = session.createQuery(hql);
		q.setInteger("idCampus", campus.getIdCampus());

		List<Period> l = new ArrayList<Period>();
		List<?> ret = q.list();

		for (Object o : ret) {
			Period p = (Period) o;
			l.add(p);
		}

		return l;
	}

	@Override
	public void preAlteracao(Period o) {
		// TODO Auto-generated method stub
		
	}

}
