package br.edu.utfpr.dv.sigeu.dao;

import org.hibernate.Query;

import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.entities.Professor;
import br.edu.utfpr.dv.sigeu.entities.ProfessorPessoa;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class ProfessorPessoaDAO extends HibernateDAO<ProfessorPessoa> {

	public ProfessorPessoaDAO(Transaction transaction) {
		super(transaction);
	}

	@Override
	public ProfessorPessoa encontrePorId(Integer id) {
		String hql = "from ProfessorPessoa o where o.professor.idProfessor = :id";
		Query q = session.createQuery(hql);
		q.setInteger("id", id);
		return (ProfessorPessoa) q.uniqueResult();
	}

	public ProfessorPessoa encontrePorPessoa(Pessoa pessoa) {
		String hql = "from ProfessorPessoa o where o.idPessoa.idPessoa = :id";
		Query q = session.createQuery(hql);
		q.setInteger("id", pessoa.getIdPessoa());
		return (ProfessorPessoa) q.uniqueResult();
	}

	public ProfessorPessoa encontrePorProfessor(Professor professor) {
		String hql = "from ProfessorPessoa o where o.professor.idProfessor = :id";
		Query q = session.createQuery(hql);
		q.setInteger("id", professor.getIdProfessor());
		return (ProfessorPessoa) q.uniqueResult();
	}

	@Override
	public String getNomeSequencia() {
		return "professorPessoa";
	}

	@Override
	public void preCriacao(ProfessorPessoa o) {
		/** Não pode fazer nada aqui. */
		// Integer val = this.gerarNovoId().intValue();
		// o.setIdProfessor(val);
	}

	@Override
	public void preAlteracao(ProfessorPessoa o) {
		// TODO Auto-generated method stub
		
	}

}
