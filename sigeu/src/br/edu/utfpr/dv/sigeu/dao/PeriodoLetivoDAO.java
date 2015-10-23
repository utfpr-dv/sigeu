package br.edu.utfpr.dv.sigeu.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.PeriodoLetivo;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class PeriodoLetivoDAO extends HibernateDAO<PeriodoLetivo> {

	public PeriodoLetivoDAO(Transaction transaction) {
		super(transaction);
	}

	@Override
	public PeriodoLetivo encontrePorId(Integer id) {
		String hql = "from PeriodoLetivo o where o.idPeriodoLetivo = :id";
		Query q = session.createQuery(hql);
		q.setInteger("id", id);
		return (PeriodoLetivo) q.uniqueResult();
	}

	public PeriodoLetivo encontrePorNome(Campus campus, String nome) {
		String hql = "from PeriodoLetivo o where o.nome = :nome and o.idCampus.idCampus = :idCampus";
		Query q = session.createQuery(hql);
		q.setString("nome", nome);
		q.setInteger("idCampus", campus.getIdCampus());
		return (PeriodoLetivo) q.uniqueResult();
	}

	@Override
	public String getNomeSequencia() {
		return "periodoLetivo";
	}

	@Override
	public void preCriacao(PeriodoLetivo o) {
		Integer val = this.gerarNovoId().intValue();
		o.setIdPeriodoLetivo(val);
		o.setNome(o.getNome().toUpperCase().trim());
	}

	public List<PeriodoLetivo> pesquisa(Campus campus, int limit) {
		String hql = "from PeriodoLetivo o where o.idCampus.idCampus = :idCampus order by o.dataInicio ASC";
		Query q = session.createQuery(hql);
		q.setInteger("idCampus", campus.getIdCampus().intValue());
		if (limit > 0) {
			q.setMaxResults(limit);
		}
		List<?> list = q.list();

		if (list.size() > 0) {
			List<PeriodoLetivo> retorno = new ArrayList<PeriodoLetivo>();

			for (Object o : list) {
				retorno.add((PeriodoLetivo) o);
			}
			return retorno;
		}
		return null;
	}

	public List<PeriodoLetivo> pesquisa(Campus campus, String textoPesquisa, int limit) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT o ");
		hql.append("FROM PeriodoLetivo o ");
		hql.append("WHERE ");

		if (textoPesquisa != null && textoPesquisa.trim().length() > 0) {
			hql.append("upper(o.nome) like upper(:q) AND ");
		}

		hql.append("o.idCampus.idCampus = :idCampus ");
		hql.append("ORDER BY upper(o.nome) ASC ");

		Query q = session.createQuery(hql.toString());
		q.setInteger("idCampus", campus.getIdCampus());

		if (textoPesquisa != null && textoPesquisa.trim().length() > 0) {
			q.setString("q", "%" + textoPesquisa + "%");
		}

		return this.pesquisaObjetos(q, limit);
	}

	public List<PeriodoLetivo> pesquisa(Campus campus) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT o ");
		hql.append("FROM PeriodoLetivo o ");
		hql.append("WHERE ");
		hql.append("o.idCampus.idCampus = :idCampus ");
		hql.append("ORDER BY upper(o.nome) ASC ");

		Query q = session.createQuery(hql.toString());
		q.setInteger("idCampus", campus.getIdCampus());

		return this.pesquisaObjetos(q, 0);
	}

	@Override
	public void preAlteracao(PeriodoLetivo o) {
		o.setNome(o.getNome().toUpperCase().trim());
	}

	public PeriodoLetivo encontreAtual(Campus campus, Date data) {
		String hql = "from PeriodoLetivo o where :data between o.dataInicio AND o.dataFim AND o.idCampus.idCampus = :idCampus";
		Query q = session.createQuery(hql);
		q.setDate("data", data);
		q.setInteger("idCampus", campus.getIdCampus());
		return (PeriodoLetivo) q.uniqueResult();
	}

}
