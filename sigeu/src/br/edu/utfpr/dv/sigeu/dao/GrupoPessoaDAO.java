package br.edu.utfpr.dv.sigeu.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.Query;

import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.GrupoPessoa;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class GrupoPessoaDAO extends HibernateDAO<GrupoPessoa> {

	public GrupoPessoaDAO(Transaction transaction) {
		super(transaction);
	}

	@Override
	public GrupoPessoa encontrePorId(Integer id) {
		String hql = "from GrupoPessoa o where o.idGrupoPessoa = :id";
		Query q = session.createQuery(hql);
		q.setInteger("id", id);
		GrupoPessoa g = (GrupoPessoa) q.uniqueResult();
		if (g != null) {
			Hibernate.initialize(g.getPessoaList());
		}
		return g;
	}

	/**
	 * Recupera um grupo de pessoa através de sua descrição única
	 * 
	 * @param descricao
	 * @return
	 * @throws SQLException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public GrupoPessoa encontrePorDescricao(Campus campus, String descricao)
			throws SQLException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		String hql = "from GrupoPessoa o where o.nome = :d and o.idCampus.idCampus = :idCampus";
		Query q = session.createQuery(hql);
		q.setString("d", descricao);
		q.setInteger("idCampus", campus.getIdCampus());
		return (GrupoPessoa) q.uniqueResult();
	}

	@Override
	public String getNomeSequencia() {
		return "grupo_pessoa";
	}

	@Override
	public void preCriacao(GrupoPessoa o) {
		Long val = this.gerarNovoId();
		o.setIdGrupoPessoa(val.intValue());
		o.setNome(o.getNome().toUpperCase().trim());
	}

	@Override
	public void preAlteracao(GrupoPessoa o) {
		o.setNome(o.getNome().toUpperCase().trim());
	}

}
