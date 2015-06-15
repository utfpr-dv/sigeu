package br.edu.utfpr.dv.sigeu.service;

import java.util.List;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.config.Config;
import br.edu.utfpr.dv.sigeu.dao.PessoaDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class PessoaService {

	/**
	 * Cria nova entrada para uma pessoa
	 * 
	 * @param p
	 */
	public static void criar(Pessoa p) throws Exception {
		Transaction trans = new Transaction();
		trans.begin();

		PessoaDAO dao = new PessoaDAO(trans);
		dao.criar(p);

		trans.commit();
		trans.close();
	}

	/**
	 * Altera uma pessoa existente
	 * 
	 * @param p
	 */
	public static void alterar(Pessoa p) throws Exception {
		Transaction trans = new Transaction();
		trans.begin();

		PessoaDAO dao = new PessoaDAO(trans);
		dao.alterar(p);

		trans.commit();
		trans.close();
	}

	/**
	 * Busca uma pessoa do banco de dados através do seu ID
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static Pessoa encontrePorId(Integer id) throws Exception {
		return PessoaService.encontrePorId(id, false);
	}

	/**
	 * Busca uma pessoa do banco de dados através do seu ID
	 * 
	 * @return
	 */
	public static Pessoa encontrePorId(Integer id, boolean carregaGrupos)
			throws Exception {
		Transaction trans = new Transaction();
		Pessoa p = null;
		try {
			trans.begin();

			PessoaDAO dao = new PessoaDAO(trans);
			p = dao.encontrePorId(id);

			if (p != null) {
				Hibernate.initialize(p.getIdCampus());
				Hibernate.initialize(p.getIdCampus().getIdInstituicao());
				Hibernate.initialize(p.getGrupoPessoaList());
			}

			// if (carregaGrupos) {
			// for (GrupoPessoa gp : p.getGrupoPessoaList()) {
			// // Faz nada
			// }
			// }
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			trans.close();
		}

		return p;
	}

	/**
	 * Busca uma pessoa do banco de dados através do seu Email e Campus
	 * 
	 * @param email
	 * @param campus
	 * @return
	 * @throws Exception
	 */
	public static Pessoa encontrePorEmail(String email, Campus campus)
			throws Exception {
		Transaction trans = new Transaction();
		trans.begin();

		PessoaDAO dao = new PessoaDAO(trans);
		Pessoa p;
		try {
			p = dao.encontrePorEmail(email, campus);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			trans.close();
		}

		return p;
	}

	public static Pessoa encontrePorCnpjCpf(String cnpjCpf) throws Exception {
		Transaction trans = new Transaction();
		trans.begin();

		PessoaDAO dao = new PessoaDAO(trans);
		Pessoa p;
		try {
			p = dao.encontrePorCnpjCpf(Config.getInstance().getCampus(),
					cnpjCpf);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			trans.close();
		}

		return p;
	}

	/**
	 * Pesquisa todas as pessoas
	 * 
	 * @param textoPesquisa
	 * @return
	 * @throws Exception
	 */
	public static List<Pessoa> pesquisar(String textoPesquisa) throws Exception {
		List<Pessoa> lista = null;

		Transaction trans = new Transaction();

		try {
			trans.begin();

			PessoaDAO dao = new PessoaDAO(trans);
			if (textoPesquisa == null || textoPesquisa.trim().length() <= 0) {
				lista = dao.pesquisa(Config.getInstance().getCampus(),
						HibernateDAO.PESQUISA_LIMITE);
			} else {
				lista = dao.pesquisa(Config.getInstance().getCampus(),
						textoPesquisa, 0);
			}

			if (lista != null) {
				for (Pessoa p : lista) {
					Hibernate.initialize(p.getIdCampus());
					Hibernate.initialize(p.getIdCampus().getIdInstituicao());
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
	 * Pesquisa as pessoas passando o parametro ativo ou não
	 * 
	 * @param query
	 * @param ativo
	 * @return
	 * @throws Exception
	 */
	public static List<Pessoa> pesquisar(String query, boolean ativo, int limit)
			throws Exception {
		List<Pessoa> lista = null;

		Transaction trans = new Transaction();

		try {
			trans.begin();

			PessoaDAO dao = new PessoaDAO(trans);

			if (query == null || query.trim().length() <= 0) {
				if (limit == 0) {
					limit = HibernateDAO.PESQUISA_LIMITE;
				}
				lista = dao.pesquisa(Config.getInstance().getCampus(), ativo,
						limit);
			} else {
				lista = dao.pesquisa(Config.getInstance().getCampus(), query,
						ativo, limit);
			}

			if (lista != null) {
				for (Pessoa p : lista) {
					Hibernate.initialize(p.getIdCampus());
					Hibernate.initialize(p.getIdCampus().getIdInstituicao());
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
