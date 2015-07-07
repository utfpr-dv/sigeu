package br.edu.utfpr.dv.sigeu.dao;

import java.util.List;

import org.hibernate.Query;

import br.edu.utfpr.dv.sigeu.entities.Lesson;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class LessonDAO extends HibernateDAO<Lesson> {

	public LessonDAO(Transaction transaction) {
		super(transaction);
	}

	@Override
	public Lesson encontrePorId(Integer id) {
		String hql = "from Lesson o where o.idLesson = :id";
		Query q = session.createQuery(hql);
		q.setInteger("id", id);
		return (Lesson) q.uniqueResult();
	}

	public Lesson encontrePorId(Integer idTimetable, String id) {
		String hql = "from Lesson o where o.id = :id AND o.idTimetable.idTimetable = :idTimetable";
		Query q = session.createQuery(hql);
		q.setString("id", id);
		q.setInteger("idTimetable", idTimetable);
		Lesson lesson = (Lesson) q.uniqueResult();
		return lesson;
	}
	
	public List<Lesson> encontrePorTimeTable(Integer idTimetable) {
		String hql = "from Lesson o where o.idTimetable.idTimetable = :idTimetable";
		Query q = session.createQuery(hql);
		q.setInteger("idTimetable", idTimetable);
		return this.pesquisaObjetos(q, 0);
	}
	
	@Override
	public String getNomeSequencia() {
		return "lesson";
	}

	@Override
	public void preCriacao(Lesson o) {
		Long id = this.gerarNovoId();
		o.setIdLesson(id.intValue());
	}

	@Override
	public void preAlteracao(Lesson o) {
		// TODO Auto-generated method stub

	}

}
