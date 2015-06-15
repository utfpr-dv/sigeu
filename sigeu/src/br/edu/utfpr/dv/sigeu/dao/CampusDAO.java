package br.edu.utfpr.dv.sigeu.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;

import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.Instituicao;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class CampusDAO extends HibernateDAO<Campus> {

	public CampusDAO(Transaction transaction) {
		super(transaction);
	}

	@Override
	public Campus encontrePorId(Integer id) {
		String hql = "from Campus o where o.idCampus = :id";
		Query q = session.createQuery(hql);
		q.setInteger("id", id);

		Campus c = (Campus) q.uniqueResult();

		if (c != null) {
			Hibernate.initialize(c.getLdapServerList());
		}

		return c;
	}

	@Override
	public String getNomeSequencia() {
		return "campus";
	}

	@Override
	public void defineId(Campus o) {
		Long val = this.gerarNovoId();
		o.setIdCampus(val.intValue());
	}

	public List<Campus> pesquisa(String textoPesquisa, int limit) {
		return this.pesquisa(textoPesquisa, null, limit);
	}

	public List<Campus> pesquisa(String textoPesquisa, Instituicao instituicao,
			int limit) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT o ");
		hql.append("FROM Campus o JOIN o.idInstituicao i ");
		hql.append("WHERE ");

		if (instituicao != null) {
			hql.append("i.idInstituicao = :ins AND ");
		}

		if (textoPesquisa != null) {
			hql.append("( upper(o.sigla) like upper(:q) or upper(o.nome) like upper(:q) ) AND ");
		}

		hql.append("o.idCampus > 0 order by o.ativo DESC, upper(o.nome) ASC");

		Query q = session.createQuery(hql.toString());

		if (instituicao != null) {
			q.setInteger("ins", instituicao.getIdInstituicao());
		}

		if (textoPesquisa != null) {
			q.setString("q", textoPesquisa);
		}

		return this.pesquisaObjetos(q, limit);
	}

	public List<Campus> pesquisa(Instituicao instituicao, int limit) {
		return this.pesquisa(null, instituicao, limit);
	}

}
