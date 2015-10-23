package br.edu.utfpr.dv.sigeu.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;

import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.Instituicao;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
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
	public void preCriacao(Campus o) {
		Long val = this.gerarNovoId();
		o.setIdCampus(val.intValue());
	}

	public List<Campus> pesquisa(String textoPesquisa, int limit) {
		return this.pesquisa(textoPesquisa, null, limit);
	}

	public List<Campus> pesquisa(String textoPesquisa, Instituicao instituicao, int limit) {
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

	/**
	 * Método para contar quantos campus existem cadastrados. Apenas para
	 * inicializar o pool de conexões durante o início do servidor de
	 * aplicações, haja vista que a tabela não terá muitos registros.
	 * 
	 * @return
	 */
	public Integer contarCampus() {
		Integer count = ((Long) session.createQuery("select count(*) from Campus").uniqueResult()).intValue();
		return count;
	}

	public List<Campus> pesquisa(Instituicao instituicao, int limit) {
		return this.pesquisa(null, instituicao, limit);
	}

	@Override
	public void preAlteracao(Campus o) {
		// TODO Auto-generated method stub

	}

	public Campus encontrePorEmail(String email) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT o ");
		hql.append("FROM Pessoa o JOIN o.idCampus c ");
		hql.append("WHERE o.email = :email");

		Query q = session.createQuery(hql.toString());
		q.setString("email", email);

		Campus c = null;

		Pessoa p = (Pessoa) q.uniqueResult();

		if (p != null) {
			c = p.getIdCampus();
		}

		return c;
	}

	public Campus encontrePorSigla(String sigla) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT o ");
		hql.append("FROM Campus o  ");
		hql.append("WHERE c.sigla = :sigla");

		Query q = session.createQuery(hql.toString());
		q.setString("sigla", sigla);

		Campus c = null;

		c = (Campus) q.uniqueResult();

		if (c != null) {
			Hibernate.initialize(c.getIdInstituicao());
			Hibernate.initialize(c.getLdapServerList());
		}

		return c;
	}

}
