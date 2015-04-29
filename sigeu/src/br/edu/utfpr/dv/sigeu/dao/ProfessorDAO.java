package br.edu.utfpr.dv.sigeu.dao;

import java.util.List;

import org.hibernate.Query;

import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.Professor;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class ProfessorDAO extends HibernateDAO<Professor> {

	public ProfessorDAO(Transaction transaction) {
		super(transaction);
	}

	@Override
	public Professor encontrePorId(Integer id) {
		String hql = "from Professor o where o.idProfessor = :id";
		Query q = session.createQuery(hql);
		q.setInteger("id", id);
		return (Professor) q.uniqueResult();
	}

	public Professor encontrePorCodigo(Campus campus, String codigo) {
		String hql = "from Professor o where o.codigo = :codigo AND o.idCampus.idCampus = :id";
		Query q = session.createQuery(hql);
		q.setString("codigo", codigo);
		q.setInteger("id", campus.getIdCampus());
		return (Professor) q.uniqueResult();
	}

	public List<Professor> pesquisaTodos(Campus campus) {
		String hql = "from Professor o where o.idCampus.idCampus = :id";
		Query q = session.createQuery(hql);
		q.setInteger("id", campus.getIdCampus());

		return this.pesquisaObjetos(q, 0);
	}

	@Override
	public String getNomeSequencia() {
		return "professor";
	}

	@Override
	public void defineId(Professor o) {
		Integer val = this.gerarNovoId().intValue();
		o.setIdProfessor(val);
	}

}
