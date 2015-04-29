package br.edu.utfpr.dv.sigeu.persistence;

import org.hibernate.Session;

/**
 * Classe de representação de uma unidade de trabalho (Unit Of Work - UOW) de
 * banco de dados. Cada transação pode ser utilizada em diversos objetos tipo
 * DAO, permitindo consistência entre cada operação em conjunto com o Hibernate.
 * 
 * @author Tiago
 *
 */
public class Transaction {
	private Session session = null;

	public Transaction() {
		super();
	}

	/**
	 * Inicia uma nova transação
	 */
	public void begin() {
		if (session == null) {
			session = HibernateUtil.currentInstance().openSession();
		}
		session.beginTransaction();
	}

	/**
	 * Encerra uma transação, desconectando-a do banco de dados;
	 */
	public void close() {
		session.flush();
		session.close();
	}

	/**
	 * Cancela uma UOW completamente.
	 */
	public void rollback() {
		session.getTransaction().rollback();
	}

	/**
	 * Persiste as informações no banco de dados permanentemente.
	 */
	public void commit() {
		session.getTransaction().commit();
		session.flush();
	}

	/**
	 * Retorna a sessão Hibernate
	 * 
	 * @return
	 */
	public Session getSession() {
		return session;
	}

}
