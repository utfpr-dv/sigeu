package br.edu.utfpr.dv.sigeu.dao;

import java.util.List;

import org.hibernate.Query;

import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.TipoReserva;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class TipoReservaDAO extends HibernateDAO<TipoReserva> {

	public TipoReservaDAO(Transaction transaction) {
		super(transaction);
	}

	@Override
	public TipoReserva encontrePorId(Integer id) {
		if (id == null) {
			return null;
		}
		String hql = "from TipoReserva o where o.idTipoReserva = :id";
		Query q = session.createQuery(hql);
		q.setInteger("id", id);
		return (TipoReserva) q.uniqueResult();
	}

	public TipoReserva encontrePorDescricao(Campus campus, String descricao) {
		String hql = "from TipoReserva o where o.idCampus.idCampus = :idCampus and o.descricao = upper(:des)";
		Query q = session.createQuery(hql);
		q.setString("des", descricao);
		q.setInteger("idCampus", campus.getIdCampus());
		return (TipoReserva) q.uniqueResult();
	}

	@Override
	public void preCriacao(TipoReserva o) {
		Long val = this.gerarNovoId();
		o.setIdTipoReserva(val.intValue());

		o.setDescricao(o.getDescricao().trim().toUpperCase());
	}

	@Override
	public String getNomeSequencia() {
		return "tipo_reserva";
	}

	public List<TipoReserva> pesquisa(Campus campus, String textoPesquisa,
			Boolean ativo, int limit) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT o ");
		hql.append("FROM TipoReserva o ");
		hql.append("WHERE ");

		if (textoPesquisa != null && textoPesquisa.trim().length() > 0) {
			hql.append("o.descricao like upper(:q) AND ");
		}

		if (ativo != null) {
			hql.append("o.ativo = :ativo AND ");
		}

		hql.append("o.idCampus.idCampus = :idCampus ");
		hql.append("ORDER BY o.ativo DESC, o.descricao ASC ");

		Query q = session.createQuery(hql.toString());
		q.setInteger("idCampus", campus.getIdCampus());

		if (textoPesquisa != null && textoPesquisa.trim().length() > 0) {
			q.setString("q", "%" + textoPesquisa.toUpperCase().trim() + "%");
		}

		if (ativo != null) {
			q.setBoolean("ativo", ativo);
		}

		return this.pesquisaObjetos(q, limit);
	}

	public List<TipoReserva> pesquisa(Campus campus, String textoPesquisa,
			int limit) {
		Boolean ativo = null;
		return this.pesquisa(campus, null, ativo, limit);
	}

	@Override
	public void preAlteracao(TipoReserva o) {
		o.setDescricao(o.getDescricao().trim().toUpperCase());
	}

}
