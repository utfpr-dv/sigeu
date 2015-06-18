package br.edu.utfpr.dv.sigeu.dao;

import org.hibernate.LockOptions;
import org.hibernate.Query;

import br.edu.utfpr.dv.sigeu.entities.Sequencia;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class SequenciaDAO extends HibernateDAO<Sequencia> {

	public SequenciaDAO(Transaction transaction) {
		super(transaction);
	}

	@Override
	public Sequencia encontrePorId(Integer id) {
		return null;
	}

	public Long getNextValue(String nomeSequencia) {
		Long val = 0L;

		String hql = "from Sequencia o where o.nome = :nome";
		Query q = session.createQuery(hql);
		q.setString("nome", nomeSequencia);

		Sequencia seq;
		Object o = q.uniqueResult();

		if (o != null) {
			seq = (Sequencia) o;
			session.buildLockRequest(LockOptions.UPGRADE).lock(seq);
			val = seq.getValor() + 1;
			seq.setValor(val);
			session.update(seq);
		} else {
			seq = new Sequencia();
			seq.setNome(nomeSequencia);
			seq.setValor(1);
			val = 1L;
			session.persist(seq);
		}
		return val;
	}

	@Override
	public String getNomeSequencia() {
		return "sequencia";
	}

	@Override
	public void preCriacao(Sequencia o) {
		// Nothing to do
	}

	@Override
	public void preAlteracao(Sequencia o) {
		// TODO Auto-generated method stub
		
	}

}
