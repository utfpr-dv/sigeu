package br.edu.utfpr.dv.sigeu.dao;

import java.util.Calendar;

import org.hibernate.Query;

import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.entities.Transacao;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class TransacaoDAO extends HibernateDAO<Transacao> {

	public TransacaoDAO(Transaction transaction) {
		super(transaction);
	}

	@Override
	public Transacao encontrePorId(Integer id) {
		String hql = "from Transacao o where o.idTransacao = :id";
		Query q = session.createQuery(hql);
		q.setInteger("id", id);
		return (Transacao) q.uniqueResult();
	}

	@Override
	public String getNomeSequencia() {
		return "transacao";
	}

	@Override
	public void preCriacao(Transacao o) {
		Integer id = this.gerarNovoId().intValue();
		o.setIdTransacao(id);
	}

	/**
	 * Cria uma nova transação e retorna o objeto gravado
	 * 
	 * @param campus
	 * @param pessoa
	 * @param descricao
	 * @return
	 */
	public Transacao criaTransacao(Campus campus, Pessoa pessoa, String descricao) {
		Transacao t = new Transacao();
		t.setIdCampus(campus);
		t.setIdPessoa(pessoa);
		t.setDescricao(descricao);
		t.setDataHora(Calendar.getInstance().getTime());
		this.criar(t);
		return t;
	}

	@Override
	public void preAlteracao(Transacao o) {
		// TODO Auto-generated method stub
		
	}

}
