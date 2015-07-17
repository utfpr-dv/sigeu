package br.edu.utfpr.dv.sigeu.service;

import java.util.List;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.dao.ItemReservaDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.CategoriaItemReserva;
import br.edu.utfpr.dv.sigeu.entities.ItemReserva;
import br.edu.utfpr.dv.sigeu.exception.EntidadePossuiRelacionamentoException;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class ItemReservaService {
	/**
	 * Cria nova
	 * 
	 * @param cat
	 */
	public static void criar(ItemReserva cat) {
		Transaction trans = new Transaction();
		trans.begin();

		ItemReservaDAO dao = new ItemReservaDAO(trans);
		dao.criar(cat);

		trans.commit();
		trans.close();
	}

	/**
	 * Altera uma existente
	 * 
	 * @param cat
	 */
	public static void alterar(ItemReserva cat) {
		Transaction trans = new Transaction();
		trans.begin();

		ItemReservaDAO dao = new ItemReservaDAO(trans);
		dao.alterar(cat);

		trans.commit();
		trans.close();
	}

	/**
	 * Verifica se já existe objeto. Se não existe, cria. Se existe, atualiza.
	 * 
	 * @param item
	 * @throws Exception
	 */
	public static void persistir(ItemReserva item) throws Exception {
		Transaction trans = new Transaction();

		try {
			trans.begin();

			// Anula o código de patrimônio se forem inseridos apenas espaços em
			// branco
			if (item.getPatrimonio() != null
					&& item.getPatrimonio().trim().length() == 0) {
				item.setPatrimonio(null);
			} else {
				item.setPatrimonio(item.getPatrimonio().toUpperCase().trim());
			}

			ItemReservaDAO dao = new ItemReservaDAO(trans);

			if (item.getIdItemReserva() != null) {
				dao.alterar(item);
			} else {
				dao.criar(item);
			}
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}

	/**
	 * Realiza a pesquisa no banco de dados conforme o texto
	 * 
	 * @param textoPesquisa
	 * @param ativo
	 *            null para todos
	 * @return
	 * @throws Exception
	 */
	public static List<ItemReserva> pesquisar(Campus campus,
			String textoPesquisa, Boolean ativo) throws Exception {
		return ItemReservaService.pesquisar(campus, null, textoPesquisa, ativo);
	}

	/**
	 * 
	 * @param editarId
	 * @return
	 * @throws Exception
	 */
	public static ItemReserva encontrePorId(Integer editarId) throws Exception {
		Transaction trans = new Transaction();

		try {
			trans.begin();
			ItemReservaDAO dao = new ItemReservaDAO(trans);
			ItemReserva obj = dao.encontrePorId(editarId);
			if (obj != null) {
				Hibernate.initialize(obj.getIdCampus());
				Hibernate.initialize(obj.getIdCategoria());
				Hibernate.initialize(obj.getIdCampus().getIdInstituicao());
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}

	/**
	 * Remove uma
	 * 
	 * @param
	 * @throws Exception
	 */
	public static void remover(ItemReserva item) throws Exception {
		Transaction trans = new Transaction();

		try {
			trans.begin();

			ItemReservaDAO dao = new ItemReservaDAO(trans);
			ItemReserva existente = dao.encontrePorId(item.getIdItemReserva());

			if (existente != null) {
				Hibernate.initialize(existente.getReservaList());
			}

			if (existente.getReservaList().size() > 0) {
				throw new EntidadePossuiRelacionamentoException(
						existente.getNome());
			}

			dao.remover(existente);
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}

	public static List<ItemReserva> pesquisar(Campus campus,
			CategoriaItemReserva categoriaItemReserva, String textoPesquisa,
			Boolean ativo) throws Exception {
		List<ItemReserva> lista = null;

		Transaction trans = new Transaction();

		try {
			trans.begin();

			ItemReservaDAO dao = new ItemReservaDAO(trans);

			lista = dao.pesquisa(campus, categoriaItemReserva, textoPesquisa,
					ativo, 0);

			if (lista != null) {
				for (ItemReserva c : lista) {
					Hibernate.initialize(c.getIdCampus());
					Hibernate.initialize(c.getIdCategoria());
					Hibernate.initialize(c.getIdCampus().getIdInstituicao());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}

		return lista;
	}
}
