package br.edu.utfpr.dv.sigeu.service;

import br.edu.utfpr.dv.sigeu.dao.MailServerDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.MailServer;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class MailServerService {
	/**
	 * Cria nova
	 * 
	 * @param cat
	 */
	public static void criar(MailServer cat) {
		Transaction trans = null;
		try {
			trans = new Transaction();
			trans.begin();

			MailServerDAO dao = new MailServerDAO(trans);
			dao.criar(cat);

			trans.commit();
		} catch (Exception e) {

		} finally {
			trans.close();
		}
	}

	/**
	 * Altera uma existente
	 * 
	 * @param cat
	 */
	public static void alterar(MailServer cat) {
		Transaction trans = null;
		try {
			trans = new Transaction();
			trans.begin();

			MailServerDAO dao = new MailServerDAO(trans);
			dao.alterar(cat);

			trans.commit();
		} catch (Exception e) {

		} finally {
			trans.close();
		}
	}

	public static void remover(MailServer item) throws Exception {
		Transaction trans = new Transaction();

		try {
			trans.begin();

			MailServerDAO dao = new MailServerDAO(trans);
			dao.remover(item);

			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}

	public static MailServer encontrePorCampus(Campus campus) throws Exception {
		Transaction trans = new Transaction();

		try {
			trans.begin();

			MailServerDAO dao = new MailServerDAO(trans);
			MailServer ms = dao.encontrePorCampus(campus);

			return ms;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}
}
