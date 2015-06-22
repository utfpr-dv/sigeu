package br.edu.utfpr.dv.sigeu.dao;

import java.util.List;

import org.hibernate.Hibernate;
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

		Professor p = (Professor) q.uniqueResult();

		if (p != null) {
			Hibernate.initialize(p.getProfessorPessoa());
			if (p.getProfessorPessoa() != null) {
				Hibernate.initialize(p.getProfessorPessoa().getIdPessoa());
			}
		}

		return p;
	}

	public Professor encontrePorCodigo(Campus campus, String codigo) {
		String hql = "from Professor o where o.codigo = :codigo AND o.idCampus.idCampus = :id";
		Query q = session.createQuery(hql);
		q.setString("codigo", codigo);
		q.setInteger("id", campus.getIdCampus());
		
		Professor p =(Professor) q.uniqueResult(); 
		
		if(p!=null){
			Hibernate.initialize(p.getProfessorPessoa());
			
			if(p.getProfessorPessoa()!=null){
				Hibernate.initialize(p.getProfessorPessoa().getIdPessoa());
			}
		}
		
		
		return p;
	}

	public List<Professor> pesquisaTodos(Campus campus) {
		String hql = "from Professor o where o.idCampus.idCampus = :id order by o.name ASC";
		Query q = session.createQuery(hql);
		q.setInteger("id", campus.getIdCampus());

		return this.pesquisaObjetos(q, 0);
	}

	public List<Professor> pesquisa(Campus campus, String termo) {
		if (termo == null || termo.trim().length() == 0) {
			return this.pesquisa(campus);
		}
		String hql = "from Professor o where o.idCampus.idCampus = :id and upper(o.name) like :termo order by o.name ASC";
		Query q = session.createQuery(hql);

		q.setInteger("id", campus.getIdCampus());
		q.setString("termo", "%" + termo.trim().toUpperCase() + "%");

		return this.pesquisaObjetos(q, 0);
	}

	public List<Professor> pesquisa(Campus campus) {
		String hql = "from Professor o where o.idCampus.idCampus = :id order by o.name ASC";
		Query q = session.createQuery(hql);

		q.setInteger("id", campus.getIdCampus());

		return this.pesquisaObjetos(q, 0);
	}

	@Override
	public String getNomeSequencia() {
		return "professor";
	}

	@Override
	public void preCriacao(Professor o) {
		Integer val = this.gerarNovoId().intValue();
		o.setIdProfessor(val);
		o.setName(o.getName().trim().toUpperCase());
	}

	@Override
	public void preAlteracao(Professor o) {
		o.setName(o.getName().trim().toUpperCase());
	}

}
