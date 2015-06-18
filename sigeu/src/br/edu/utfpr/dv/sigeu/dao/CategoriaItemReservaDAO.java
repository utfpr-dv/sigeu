package br.edu.utfpr.dv.sigeu.dao;

import java.util.List;

import org.hibernate.Query;

import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.CategoriaItemReserva;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class CategoriaItemReservaDAO extends HibernateDAO<CategoriaItemReserva> {

	public CategoriaItemReservaDAO(Transaction transaction) {
		super(transaction);
	}

	@Override
	public CategoriaItemReserva encontrePorId(Integer id) {
		if (id == null) {
			return null;
		}
		String hql = "from CategoriaItemReserva o where o.idCategoria = :id";
		Query q = session.createQuery(hql);
		q.setInteger("id", id);
		return (CategoriaItemReserva) q.uniqueResult();
	}

	public CategoriaItemReserva encontrePorDescricao(Campus campus, String descricao) {
		String hql = "from CategoriaItemReserva o where o.idCampus.idCampus = :idCampus and upper(o.nome) = upper(:des)";
		Query q = session.createQuery(hql);
		q.setString("des", descricao);
		q.setInteger("idCampus", campus.getIdCampus());
		return (CategoriaItemReserva) q.uniqueResult();
	}

	@Override
	public void preCriacao(CategoriaItemReserva o) {
		Long val = this.gerarNovoId();
		o.setIdCategoria(val.intValue());
		o.setNome(o.getNome().toUpperCase().trim());
	}

	@Override
	public String getNomeSequencia() {
		return "categoria_item_reserva";
	}

	public List<CategoriaItemReserva> pesquisa(Campus campus, String textoPesquisa, Boolean ativo, int limit) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT o ");
		hql.append("FROM CategoriaItemReserva o ");
		hql.append("WHERE ");

		if (textoPesquisa != null && textoPesquisa.trim().length() > 0) {
			hql.append("upper(o.nome) like upper(:q) AND ");
		}

		if (ativo != null) {
			hql.append("o.ativo = :ativo AND ");
		}

		hql.append("o.idCampus.idCampus = :idCampus ");
		hql.append("ORDER BY o.ativo DESC, upper(o.nome) ASC ");

		Query q = session.createQuery(hql.toString());
		q.setInteger("idCampus", campus.getIdCampus());

		if (textoPesquisa != null && textoPesquisa.trim().length() > 0) {
			q.setString("q", "%" + textoPesquisa + "%");
		}

		if (ativo != null) {
			q.setBoolean("ativo", ativo);
		}

		return this.pesquisaObjetos(q, limit);
	}

	public List<CategoriaItemReserva> pesquisa(Campus campus, String textoPesquisa, int limit) {
		Boolean ativo = null;
		return this.pesquisa(campus, null, ativo, limit);
	}

	@Override
	public void preAlteracao(CategoriaItemReserva o) {
		o.setNome(o.getNome().toUpperCase().trim());
	}

}
