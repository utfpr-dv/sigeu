package br.edu.utfpr.dv.sigeu.service;

import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.dao.PeriodoLetivoDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.PeriodoLetivo;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class PeriodoLetivoService {
	/**
	 * Cria nova
	 * 
	 * @param pl
	 */
	public static void criar(PeriodoLetivo pl) throws Exception {
		Transaction trans = new Transaction();
		try {
			trans.begin();

			PeriodoLetivoDAO dao = new PeriodoLetivoDAO(trans);
			dao.criar(pl);

			trans.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			trans.close();
		}
	}

	/**
	 * Altera uma existente
	 * 
	 * @param cat
	 */
	public static void alterar(PeriodoLetivo cat) throws Exception {
		Transaction trans = new Transaction();
		try {
			trans.begin();

			PeriodoLetivoDAO dao = new PeriodoLetivoDAO(trans);
			dao.alterar(cat);

			trans.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			trans.close();
		}
		trans.close();
	}

	/**
	 * Verifica se já existe objeto. Se não existe, cria. Se existe, atualiza.
	 * 
	 * @param item
	 * @throws Exception
	 */
	public static void persistir(PeriodoLetivo item) throws Exception {
		Transaction trans = new Transaction();

		try {
			trans.begin();

			PeriodoLetivoDAO dao = new PeriodoLetivoDAO(trans);

			if (item.getIdPeriodoLetivo() != null) {
				dao.alterar(item);
			} else {
				dao.criar(item);
			}

			trans.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			trans.close();
		}
	}

	/**
	 * Realiza a pesquisa no banco de dados conforme o texto
	 * 
	 * @param textoPesquisa
	 * @return
	 * @throws Exception
	 */
	public static List<PeriodoLetivo> pesquisar(Campus campus, String textoPesquisa) throws Exception {
		List<PeriodoLetivo> lista = null;

		Transaction trans = new Transaction();

		try {
			trans.begin();

			PeriodoLetivoDAO dao = new PeriodoLetivoDAO(trans);

			if (textoPesquisa == null || textoPesquisa.trim().length() <= 0) {
				lista = dao.pesquisa(campus, HibernateDAO.PESQUISA_LIMITE);
			} else {
				lista = dao.pesquisa(campus, textoPesquisa, 0);
			}

			if (lista != null) {
				for (PeriodoLetivo c : lista) {
					Hibernate.initialize(c.getIdCampus());
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

	/**
	 * 
	 * @param editarId
	 * @return
	 * @throws Exception
	 */
	public static PeriodoLetivo encontrePorId(Integer editarId) throws Exception {
		Transaction trans = new Transaction();

		try {
			trans.begin();
			PeriodoLetivoDAO dao = new PeriodoLetivoDAO(trans);
			PeriodoLetivo obj = dao.encontrePorId(editarId);
			if (obj != null) {
				Hibernate.initialize(obj.getIdCampus());
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
	public static void remover(PeriodoLetivo item) throws Exception {
		Transaction trans = new Transaction();

		try {
			trans.begin();

			PeriodoLetivoDAO dao = new PeriodoLetivoDAO(trans);
			PeriodoLetivo existente = dao.encontrePorId(item.getIdPeriodoLetivo());

			dao.remover(existente);
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}

	/**
	 * Realiza a pesquisa no banco de dados pelo campus
	 * 
	 * @param campus
	 * @return
	 * @throws Exception
	 */
	public static List<PeriodoLetivo> pesquisar(Campus campus) throws Exception {
		List<PeriodoLetivo> lista = null;

		Transaction trans = new Transaction();

		try {
			trans.begin();

			PeriodoLetivoDAO dao = new PeriodoLetivoDAO(trans);

			lista = dao.pesquisa(campus);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}

		return lista;
	}

	public static PeriodoLetivo encontrePorNome(Campus campus, String value) throws Exception {

		Transaction trans = new Transaction();

		try {
			trans.begin();
			PeriodoLetivoDAO dao = new PeriodoLetivoDAO(trans);
			return dao.encontrePorNome(campus, value);
		} catch (Exception e) {
			throw e;
		} finally {
			trans.close();
		}
	}

	public static PeriodoLetivo encontreAtual(Campus campus, Date data) throws Exception {

		Transaction trans = new Transaction();

		try {
			trans.begin();
			PeriodoLetivoDAO dao = new PeriodoLetivoDAO(trans);
			return dao.encontreAtual(campus, data);
		} catch (Exception e) {
			throw e;
		} finally {
			trans.close();
		}
	}

}
