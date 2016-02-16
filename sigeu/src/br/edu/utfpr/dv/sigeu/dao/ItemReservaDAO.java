package br.edu.utfpr.dv.sigeu.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;

import com.adamiworks.utils.FileUtils;

import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.CategoriaItemReserva;
import br.edu.utfpr.dv.sigeu.entities.ItemReserva;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class ItemReservaDAO extends HibernateDAO<ItemReserva> {

	public ItemReservaDAO(Transaction transaction) {
		super(transaction);
	}

	@Override
	public ItemReserva encontrePorId(Integer id) {
		if (id == null) {
			return null;
		}
		String hql = "from ItemReserva o where o.id = :id";
		Query q = session.createQuery(hql);
		q.setInteger("id", id);

		ItemReserva i = (ItemReserva) q.uniqueResult();

		if (i != null) {
			Hibernate.initialize(i.getPessoaList());
		}

		return i;
	}

	public ItemReserva encontrePorDescricao(Campus campus, String descricao) {
		String hql = "from ItemReserva o where upper(o.nome) = upper(:des) and o.idCampus.idCampus = :idCampus";
		Query q = session.createQuery(hql);
		q.setInteger("idCampus", campus.getIdCampus());
		q.setString("des", descricao);
		return (ItemReserva) q.uniqueResult();
	}

	public ItemReserva encontrePorDescricaoECategoria(Campus campus, CategoriaItemReserva categoria, String descricao) {
		String hql = "from ItemReserva o where upper(o.nome) = upper(:des) AND o.idCampus.idCampus = :idCampus AND ";
		hql += "o.idCategoria.idCategoria = :idCategoria";
		Query q = session.createQuery(hql);
		q.setInteger("idCampus", campus.getIdCampus());
		q.setInteger("idCategoria", categoria.getIdCategoria());
		q.setString("des", descricao);
		return (ItemReserva) q.uniqueResult();
	}

	public ItemReserva encontrePorRotulo(Campus campus, String rotulo) {
		String hql = "from ItemReserva o where upper(o.rotulo) = upper(:des) AND o.idCampus.idCampus = :idCampus ";
		Query q = session.createQuery(hql);
		q.setInteger("idCampus", campus.getIdCampus());
		q.setString("des", rotulo);
		return (ItemReserva) q.uniqueResult();
	}

	public ItemReserva encontrePorRotuloECategoria(Campus campus, CategoriaItemReserva categoria, String rotulo) {
		String hql = "from ItemReserva o where upper(o.rotulo) = upper(:des) AND o.idCampus.idCampus = :idCampus AND ";
		hql += "o.idCategoria.idCategoria = :idCategoria";
		Query q = session.createQuery(hql);
		q.setInteger("idCampus", campus.getIdCampus());
		q.setInteger("idCategoria", categoria.getIdCategoria());
		q.setString("des", rotulo);
		return (ItemReserva) q.uniqueResult();
	}

	public ItemReserva encontrePorDescricaoECategoria(Campus campus, Integer categoria, String descricao) {
		String hql = "from ItemReserva o where upper(o.nome) = upper(:des) AND o.idCampus.idCampus = :idCampus AND ";
		hql += "o.idCategoria.idCategoria = :idCategoria";
		Query q = session.createQuery(hql);
		q.setInteger("idCampus", campus.getIdCampus());
		q.setInteger("idCategoria", categoria);
		q.setString("des", descricao);
		return (ItemReserva) q.uniqueResult();
	}

	@Override
	public void preCriacao(ItemReserva o) {
		Long val = this.gerarNovoId();
		o.setIdItemReserva(val.intValue());
		o.setNome(o.getNome().toUpperCase().trim());
	}

	@Override
	public String getNomeSequencia() {
		return "item_reserva";
	}

	public List<ItemReserva> pesquisa(Campus campus, CategoriaItemReserva categoria, String textoPesquisa,
			Boolean ativo, int limit) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT o ");
		hql.append("FROM ItemReserva o ");
		hql.append("WHERE ");

		if (categoria != null && categoria.getIdCategoria() > 0) {
			hql.append("o.idCategoria.idCategoria = :idCategoria AND ");
		}

		if (textoPesquisa != null && textoPesquisa.trim().length() > 0) {
			hql.append(
					"(upper(o.nome) like upper(:q) OR upper(o.rotulo) like upper(:q) OR upper(o.patrimonio) like upper(:q) ) AND ");
		}

		if (ativo != null) {
			hql.append("o.ativo = :ativo AND ");
		}

		hql.append("o.idCampus.idCampus = :idCampus ");
		hql.append("ORDER BY o.ativo DESC, upper(o.nome) ASC ");

		Query q = session.createQuery(hql.toString());
		q.setInteger("idCampus", campus.getIdCampus());

		if (categoria != null && categoria.getIdCategoria() > 0) {
			q.setInteger("idCategoria", categoria.getIdCategoria());
		}

		if (textoPesquisa != null && textoPesquisa.trim().length() > 0) {
			q.setString("q", "%" + textoPesquisa + "%");
		}

		if (ativo != null) {
			q.setBoolean("ativo", ativo);
		}

		return this.pesquisaObjetos(q, limit);
	}

	public List<ItemReserva> pesquisaItemReservaDisponivel(Campus campus, Date data, Date horaInicial, Date horaFinal,
			CategoriaItemReserva categoria, ItemReserva item) {

		String sql = FileUtils.readTextFile("/br/edu/utfpr/dv/sigeu/sqlquery/Query001.sql");
		Query query = session.createSQLQuery(sql).addEntity(ItemReserva.class);

		query.setInteger("idCampus", campus.getIdCampus());
		query.setInteger("idCategoria", categoria.getIdCategoria());

		if (item == null) {
			query.setInteger("idItemReserva", 0);
		} else {
			query.setInteger("idItemReserva", item.getIdItemReserva());
		}

		query.setDate("data", data);
		query.setTime("horaInicio", horaInicial);
		query.setTime("horaFim", horaFinal);

		List<?> list = query.list();
		List<ItemReserva> lista = new ArrayList<ItemReserva>();

		for (Object object : list) {
			lista.add((ItemReserva) object);
		}

		return lista;
	}

	public List<ItemReserva> pesquisa(Campus campus, String textoPesquisa, Boolean ativo, int limit) {
		return this.pesquisa(campus, null, textoPesquisa, ativo, limit);
	}

	public List<ItemReserva> pesquisa(Campus campus, String textoPesquisa, int limit) {
		return this.pesquisa(campus, textoPesquisa, null, limit);
	}

	@Override
	public void preAlteracao(ItemReserva o) {
		o.setNome(o.getNome().trim().toUpperCase());

	}

}