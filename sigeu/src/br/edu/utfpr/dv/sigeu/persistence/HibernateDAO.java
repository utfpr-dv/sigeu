package br.edu.utfpr.dv.sigeu.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.edu.utfpr.dv.sigeu.dao.SequenciaDAO;

public abstract class HibernateDAO<E> {
	public static final int PESQUISA_LIMITE = 7;
	protected Transaction transaction = null;
	protected Session session = null;

	public HibernateDAO(Transaction transaction) {
		this.transaction = transaction;
		this.session = transaction.getSession();
	}

	public abstract E encontrePorId(Integer id);

	public abstract String getNomeSequencia();

	/**
	 * Gera um novo ID na tabela de Sequencias incrementando o contador.
	 * 
	 * @return
	 */
	public Long gerarNovoId() {
		Transaction trans = new Transaction();
		trans.begin();

		SequenciaDAO dao = new SequenciaDAO(trans);
		Long val = dao.getNextValue(this.getNomeSequencia());

		while (this.encontrePorId(val.intValue()) != null) {
			val = dao.getNextValue(this.getNomeSequencia());
		}

		trans.commit();
		trans.close();
		return val;
	}

	/**
	 * Método utilizado para tratamento de dados antes de persistir o registro
	 * criando um novo.
	 * 
	 * @param o
	 */
	public abstract void preCriacao(E o);

	/**
	 * Método utilizado para tratamento de dados antes de persistir o registro
	 * alterando um existente.
	 * 
	 * @param o
	 */
	public abstract void preAlteracao(E o);

	public void criar(E o) {
		this.preCriacao(o);
		session.save(o);
	}

	public void alterar(E o) {
		this.preAlteracao(o);
		session.update(o);
	}

	public void remover(E o) {
		session.delete(o);
	}

	/**
	 * Método auxiliar para realizar pesquisas
	 * 
	 * @param query
	 *            Objeto tipo Query do Hibernate
	 * @param limit
	 *            Limite de registros a serem retornados
	 * @return Lista de entidades encontradas no banco de dados. Se não
	 *         encontrar registros, retorna uma lista vazia ao invés de uma
	 *         lista nula
	 */
	@SuppressWarnings("unchecked")
	public List<E> pesquisaObjetos(Query q, int limit) {
		List<E> lista = new ArrayList<E>();

		if (limit > 0) {
			q.setMaxResults(limit);
		}

		List<?> list = q.list();

		if (list.size() > 0) {
			for (Object o : list) {
				lista.add(((E) o));
			}
		}

		return lista;
	}
}
