package br.edu.utfpr.dv.sigeu.service;

import java.util.List;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.dao.LdapServerDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.LdapServer;
import br.edu.utfpr.dv.sigeu.exception.EntidadePossuiRelacionamentoException;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class LdapServerService {
	/**
	 * Cria novo
	 * 
	 * @param i
	 */
	public static void criar(LdapServer i) {
		Transaction trans = new Transaction();
		trans.begin();

		LdapServerDAO dao = new LdapServerDAO(trans);
		dao.criar(i);

		trans.commit();
		trans.close();
	}

	/**
	 * Altera existente
	 * 
	 * @param i
	 */
	public static void alterar(LdapServer i) {
		Transaction trans = new Transaction();
		trans.begin();

		LdapServerDAO dao = new LdapServerDAO(trans);
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
	public static List<LdapServer> pesquisar(Campus campus, String textoPesquisa) throws Exception {
		List<LdapServer> lista = null;

		Transaction trans = new Transaction();

		try {
			trans.begin();

			LdapServerDAO dao = new LdapServerDAO(trans);
			if (textoPesquisa == null || textoPesquisa.trim().length() <= 0) {
				lista = dao.pesquisa(campus, HibernateDAO.PESQUISA_LIMITE);
			} else {
				lista = dao.pesquisa(campus, textoPesquisa, 0);
			}

			if (lista != null) {
				for (LdapServer l : lista) {
					Hibernate.initialize(l.getIdCampus());
					Hibernate.initialize(l.getIdCampus().getIdInstituicao());
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
	 * Encontra por ID
	 * 
	 * @param editarId
	 * @return
	 * @throws Exception
	 */
	public static LdapServer encontrePorId(Integer editarId) throws Exception {
		Transaction trans = new Transaction();

		try {
			trans.begin();

			LdapServerDAO dao = new LdapServerDAO(trans);
			LdapServer obj = dao.encontrePorId(editarId);
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
	 * Remove
	 * 
	 * @param i
	 * @throws EntidadePossuiRelacionamentoException
	 * @throws Exception
	 */
	public static void remover(LdapServer i) throws EntidadePossuiRelacionamentoException, Exception {
		Transaction trans = new Transaction();

		try {
			trans.begin();

			LdapServerDAO dao = new LdapServerDAO(trans);
			LdapServer ldapServerBd = dao.encontrePorId(i.getIdServer());

			dao.remover(ldapServerBd);
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}

	public static LdapServer encontrePorEmail(String email) throws Exception {
		Transaction trans = new Transaction();
		LdapServer ldap = null;

		try {
			trans.begin();

			LdapServerDAO dao = new LdapServerDAO(trans);
			ldap = dao.encontrePorEmail(email);
			return ldap;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}
}
