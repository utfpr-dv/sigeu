package br.edu.utfpr.dv.sigeu.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.Feriado;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class FeriadoDAO extends HibernateDAO<Feriado> {

	public FeriadoDAO(Transaction transaction) {
		super(transaction);
	}

	@Override
	public Feriado encontrePorId(Integer id) {
		if (id == null) {
			return null;
		}
		String hql = "from Feriado o where o.id = :id order by o.data";
		Query q = session.createQuery(hql);
		q.setInteger("id", id);
		return (Feriado) q.uniqueResult();
	}

	public Feriado encontrePorDescricao(Campus campus, String descricao) {
		String hql = "from Feriado o where o.idCampus.idCampus = :idCampus AND upper(o.descricao) = upper(:des) order by o.data";
		Query q = session.createQuery(hql);
		q.setString("des", descricao);
		q.setInteger("idCampus", campus.getIdCampus().intValue());
		return (Feriado) q.uniqueResult();
	}

	@Override
	public void preCriacao(Feriado o) {
		Long val = this.gerarNovoId();
		o.setIdFeriado(val.intValue());
		o.setDescricao(o.getDescricao().toUpperCase().trim());
	}

	@Override
	public String getNomeSequencia() {
		return "feriado";
	}

	public List<Feriado> pesquisa(Campus campus, String textoPesquisa, int limit) {
		if (textoPesquisa == null || textoPesquisa.trim().equals("")) {
			return this.pesquisa(campus, limit);
		}
		String hql = "from Feriado o where (upper(o.descricao) like upper(:q)) and o.idCampus.idCampus = :idCampus order by o.data ASC";
		Query q = session.createQuery(hql);
		q.setString("q", "%" + textoPesquisa + "%");
		q.setInteger("idCampus", campus.getIdCampus());

		if (limit > 0) {
			q.setMaxResults(limit);
		}

		List<?> list = q.list();

		if (list.size() > 0) {
			List<Feriado> retorno = new ArrayList<Feriado>();

			for (Object o : list) {
				retorno.add((Feriado) o);
			}
			return retorno;
		}
		return null;
	}

	public List<Feriado> pesquisa(Campus campus, int limit) {
		String hql = "from Feriado o WHERE o.idCampus.idCampus = :idCampus order by o.data ASC";
		Query q = session.createQuery(hql);
		q.setInteger("idCampus", campus.getIdCampus());

		if (limit > 0) {
			q.setMaxResults(limit);
		}
		List<?> list = q.list();

		if (list.size() > 0) {
			List<Feriado> retorno = new ArrayList<Feriado>();

			for (Object o : list) {
				retorno.add((Feriado) o);
			}
			return retorno;
		}
		return null;
	}

	public List<Feriado> pesquisa(Campus campus, Date dataInicial, Date dataFinal) {
		String hql = "from Feriado o WHERE o.idCampus.idCampus = :idCampus AND o.data between :d1 and :d2 order by o.data ASC";

		Query q = session.createQuery(hql);
		q.setInteger("idCampus", campus.getIdCampus());
		q.setDate("d1", dataInicial);
		q.setDate("d2", dataFinal);
		List<?> list = q.list();

		if (list.size() > 0) {
			List<Feriado> retorno = new ArrayList<Feriado>();

			for (Object o : list) {
				retorno.add((Feriado) o);
			}
			return retorno;
		}
		return null;
	}

	public List<Feriado> pesquisa(Campus campus, Date data) {
		return this.pesquisa(campus, data, data);
	}

	@Override
	public void preAlteracao(Feriado o) {
		o.setDescricao(o.getDescricao().toUpperCase().trim());
	}
}
