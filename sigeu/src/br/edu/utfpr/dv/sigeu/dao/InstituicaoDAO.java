package br.edu.utfpr.dv.sigeu.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import br.edu.utfpr.dv.sigeu.entities.Instituicao;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class InstituicaoDAO extends HibernateDAO<Instituicao> {

	public InstituicaoDAO(Transaction transaction) {
		super(transaction);
	}

	@Override
	public Instituicao encontrePorId(Integer id) {
		String hql = "from Instituicao o where o.idInstituicao = :id";
		Query q = session.createQuery(hql);
		q.setInteger("id", id);
		return (Instituicao) q.uniqueResult();
	}

	@Override
	public String getNomeSequencia() {
		return "instituicao";
	}

	@Override
	public void preCriacao(Instituicao o) {
		Long val = this.gerarNovoId();
		o.setIdInstituicao(val.intValue());
	}

	public List<Instituicao> pesquisa(String textoPesquisa, int limit) {
		if (textoPesquisa == null || textoPesquisa.trim().equals("")) {
			return this.pesquisa(limit);
		}
		String hql = "from Instituicao o where upper(o.sigla) like upper(:q) or upper(o.nome) like upper(:q) order by o.ativo DESC, upper(o.nome) ASC";
		Query q = session.createQuery(hql);
		q.setString("q", "%" + textoPesquisa + "%");
		if (limit > 0) {
			q.setMaxResults(limit);
		}

		List<?> list = q.list();

		if (list.size() > 0) {
			List<Instituicao> retorno = new ArrayList<Instituicao>();

			for (Object o : list) {
				retorno.add((Instituicao) o);
			}
			return retorno;
		}
		return null;
	}

	public List<Instituicao> pesquisa(int limit) {
		String hql = "from Instituicao o order by o.ativo DESC, upper(o.nome) ASC";
		Query q = session.createQuery(hql);
		if (limit > 0) {
			q.setMaxResults(limit);
		}

		List<?> list = q.list();

		if (list.size() > 0) {
			List<Instituicao> retorno = new ArrayList<Instituicao>();

			for (Object o : list) {
				retorno.add((Instituicao) o);
			}
			return retorno;
		}
		return null;
	}

	@Override
	public void preAlteracao(Instituicao o) {
		// TODO Auto-generated method stub
		
	}
}
