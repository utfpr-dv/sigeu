package br.edu.utfpr.dv.sigeu.dao;

import java.util.List;

import org.hibernate.Query;

import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.Timetable;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class TimetableDAO extends HibernateDAO<Timetable> {

	public TimetableDAO(Transaction transaction) {
		super(transaction);
	}

	@Override
	public Timetable encontrePorId(Integer id) {
		String hql = "from Timetable o where o.idTimetable = :id";
		Query q = session.createQuery(hql);
		q.setInteger("id", id);
		return (Timetable) q.uniqueResult();
	}

	@Override
	public String getNomeSequencia() {
		return "timetable";
	}

	@Override
	public void preCriacao(Timetable o) {
		Integer val = this.gerarNovoId().intValue();
		o.setIdTimetable(val);
	}

	public List<Timetable> getAll(Campus campus) {
		String hql = "from Timetable o where o.idCampus = :idCampus";
		Query q = session.createQuery(hql);
		q.setInteger("idCampus", campus.getIdCampus());
		return this.pesquisaObjetos(q, 0);
	}

	public void deleteAllPreviousTimetables(Campus campus) throws Exception {
		try {
			List<Timetable> list = this.getAll(campus);

			for (Timetable timetable : list) {

				// CARD
				String sql = "DELETE FROM card WHERE id_timetable = :id";
				Query delete = session.createSQLQuery(sql);
				delete.setInteger("id", timetable.getIdTimetable());
				delete.executeUpdate();

				sql = "DELETE FROM lesson WHERE id_timetable = :id";
				delete = session.createSQLQuery(sql);
				delete.setInteger("id", timetable.getIdTimetable());
				delete.executeUpdate();

				sql = "DELETE FROM period WHERE id_timetable = :id";
				delete = session.createSQLQuery(sql);
				delete.setInteger("id", timetable.getIdTimetable());
				delete.executeUpdate();

				sql = "DELETE FROM classroom WHERE id_timetable = :id";
				delete = session.createSQLQuery(sql);
				delete.setInteger("id", timetable.getIdTimetable());
				delete.executeUpdate();

				sql = "DELETE FROM teacher WHERE id_timetable = :id";
				delete = session.createSQLQuery(sql);
				delete.setInteger("id", timetable.getIdTimetable());
				delete.executeUpdate();

				sql = "DELETE FROM subject WHERE id_timetable = :id";
				delete = session.createSQLQuery(sql);
				delete.setInteger("id", timetable.getIdTimetable());
				delete.executeUpdate();

				sql = "DELETE FROM clazz WHERE id_timetable = :id";
				delete = session.createSQLQuery(sql);
				delete.setInteger("id", timetable.getIdTimetable());
				delete.executeUpdate();

				sql = "DELETE FROM timetable WHERE id_timetable = :id";
				delete = session.createSQLQuery(sql);
				delete.setInteger("id", timetable.getIdTimetable());
				delete.executeUpdate();

			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void preAlteracao(Timetable o) {
		// TODO Auto-generated method stub

	}
}
