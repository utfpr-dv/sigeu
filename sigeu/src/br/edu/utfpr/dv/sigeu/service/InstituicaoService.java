package br.edu.utfpr.dv.sigeu.service;

import java.util.List;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.dao.InstituicaoDAO;
import br.edu.utfpr.dv.sigeu.entities.Instituicao;
import br.edu.utfpr.dv.sigeu.exception.EntidadePossuiRelacionamentoException;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class InstituicaoService {
	/**
	 * Cria novo
	 * 
	 * @param i
	 */
	public static void criar(Instituicao i) {
		Transaction trans = new Transaction();
		trans.begin();

		InstituicaoDAO dao = new InstituicaoDAO(trans);
		dao.criar(i);

		trans.commit();
		trans.close();
	}

	/**
	 * Altera existente
	 * 
	 * @param i
	 */
	public static void alterar(Instituicao i) {
		Transaction trans = new Transaction();
		trans.begin();

		InstituicaoDAO dao = new InstituicaoDAO(trans);
		dao.alterar(i);

		trans.commit();
		trans.close();
	}

	/**
	 * Realiza a pesquisa no banco de dados conforme o texto
	 * 
	 * @param textoPesquisa
	 * @return
	 * @throws Exception
	 */
	public static List<Instituicao> pesquisar(String textoPesquisa) throws Exception {
		List<Instituicao> lista = null;

		Transaction trans = new Transaction();

		try {
			trans.begin();

			InstituicaoDAO dao = new InstituicaoDAO(trans);
			if (textoPesquisa == null || textoPesquisa.trim().length() <= 0) {
				lista = dao.pesquisa(HibernateDAO.PESQUISA_LIMITE);
			} else {
				lista = dao.pesquisa(textoPesquisa, 0);
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
	 * Encontra por ID
	 * 
	 * @param editarId
	 * @return
	 * @throws Exception
	 */
	public static Instituicao encontrePorId(Integer editarId) throws Exception {
		Transaction trans = new Transaction();

		try {
			trans.begin();

			InstituicaoDAO dao = new InstituicaoDAO(trans);
			Instituicao obj = dao.encontrePorId(editarId);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}

	/**
	 * Remove
	 * 
	 * @param i
	 * @throws EntidadePossuiRelacionamentoException
	 * @throws Exception
	 */
	public static void remover(Instituicao i) throws EntidadePossuiRelacionamentoException, Exception {
		Transaction trans = new Transaction();

		try {
			trans.begin();

			InstituicaoDAO dao = new InstituicaoDAO(trans);
			Instituicao instituicaoBd = dao.encontrePorId(i.getIdInstituicao());

			Hibernate.initialize(instituicaoBd.getCampusList());

			if (instituicaoBd.getCampusList().size() > 0) {
				throw new EntidadePossuiRelacionamentoException(instituicaoBd.getNome());
			}

			dao.remover(instituicaoBd);
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}
}
