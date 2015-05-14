package br.edu.utfpr.dv.sigeu.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.Query;

import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class PessoaDAO extends HibernateDAO<Pessoa> {

	public PessoaDAO(Transaction transaction) {
		super(transaction);
	}

	@Override
	public Pessoa encontrePorId(Integer id) {
		String hql = "from Pessoa o where o.idPessoa = :id";
		Query q = session.createQuery(hql);
		q.setInteger("id", id);
		return (Pessoa) q.uniqueResult();
	}

	public Pessoa encontrePorEmail(String email) throws Exception {
		String hql = "from Pessoa o where o.email = :email";
		Query q = session.createQuery(hql);
		q.setCacheMode(CacheMode.REFRESH);
		q.setString("email", email);
		return (Pessoa) q.uniqueResult();
	}

	public Pessoa encontrePorCnpjCpf(Campus campus, String cnpjCpf) throws Exception {
		String hql = "from Pessoa o where o.cnpjCpf = :cnpjCpf and o.idCampus.idCampus = :idCampus";
		Query q = session.createQuery(hql);
		q.setString("cnpjCpf", cnpjCpf);
		q.setInteger("idCampus", campus.getIdCampus());
		return (Pessoa) q.uniqueResult();
	}

	@Override
	public String getNomeSequencia() {
		return "pessoa";
	}

	@Override
	public void defineId(Pessoa o) {
		Integer id = this.gerarNovoId().intValue();
		o.setIdPessoa(id);
	}

	public List<Pessoa> pesquisa(Campus campus, String textoPesquisa, int limit) {
		if (textoPesquisa == null || textoPesquisa.trim().equals("")) {
			return this.pesquisa(campus, limit);
		}

		String hql = "from Pessoa o where (upper(o.email) like upper(:q) or upper(o.nomeCompleto) like upper(:q)) and o.idCampus.idCampus = :idCampus order by o.ativo DESC, upper(o.nomeCompleto) ASC";
		Query q = session.createQuery(hql);
		q.setString("q", "%" + textoPesquisa + "%");
		q.setInteger("idCampus", campus.getIdCampus());
		if (limit > 0) {
			q.setMaxResults(limit);
		}
		List<?> list = q.list();

		if (list.size() > 0) {
			List<Pessoa> retorno = new ArrayList<Pessoa>();

			for (Object o : list) {
				Pessoa p = (Pessoa)o;
				retorno.add(p);
			}
			return retorno;
		}
		return null;
	}

	public List<Pessoa> pesquisa(Campus campus, int limit) {
		String hql = "from Pessoa o WHERE o.idCampus.idCampus = :idCampus order by o.ativo DESC, upper(o.nomeCompleto) ASC";
		Query q = session.createQuery(hql);
		q.setInteger("idCampus", campus.getIdCampus());
		if (limit > 0) {
			q.setMaxResults(limit);
		}
		List<?> list = q.list();

		if (list.size() > 0) {
			List<Pessoa> retorno = new ArrayList<Pessoa>();

			for (Object o : list) {
				retorno.add((Pessoa) o);
			}
			return retorno;
		}
		return null;
	}

	public List<Pessoa> pesquisa(Campus campus, boolean ativo, int limit) {
		String hql = "from Pessoa o WHERE o.idCampus.idCampus = :idCampus and o.ativo = :ativo order by o.ativo DESC, upper(o.nomeCompleto) ASC";
		Query q = session.createQuery(hql);
		q.setInteger("idCampus", campus.getIdCampus());
		q.setBoolean("ativo", ativo);

		if (limit > 0) {
			q.setMaxResults(limit);
		}
		List<?> list = q.list();

		if (list.size() > 0) {
			List<Pessoa> retorno = new ArrayList<Pessoa>();

			for (Object o : list) {
				retorno.add((Pessoa) o);
			}
			return retorno;
		}
		return null;
	}

	public List<Pessoa> pesquisa(Campus campus, String query, boolean ativo, int limit) {
		if (query == null || query.trim().equals("")) {
			return this.pesquisa(campus, ativo, limit);
		}

		String hql = "from Pessoa o where o.ativo = :ativo and (upper(o.email) like upper(:q) or upper(o.nomeCompleto) like upper(:q)) and o.idCampus.idCampus = :idCampus order by o.ativo DESC, upper(o.nomeCompleto) ASC";
		Query q = session.createQuery(hql);
		q.setString("q", "%" + query + "%");
		q.setInteger("idCampus", campus.getIdCampus());
		q.setBoolean("ativo", ativo);

		if (limit > 0) {
			q.setMaxResults(limit);
		}
		List<?> list = q.list();

		if (list.size() > 0) {
			List<Pessoa> retorno = new ArrayList<Pessoa>();

			for (Object o : list) {
				retorno.add((Pessoa) o);
			}
			return retorno;
		}
		return null;
	}
}
