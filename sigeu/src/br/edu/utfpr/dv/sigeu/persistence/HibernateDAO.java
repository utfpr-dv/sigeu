package br.edu.utfpr.dv.sigeu.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;

import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;

import br.edu.utfpr.dv.sigeu.entities.Sequencia;

@Stateless
public abstract class HibernateDAO<E> {

    public static final int PESQUISA_LIMITE = 15;
    public static final int HIBERNATE_BATCH_SIZE = 100;

    @PersistenceContext(unitName = "sigeuPU")
    protected Session session;

    public abstract E encontrePorId(Integer id);

    public abstract String getNomeSequencia();

    /**
     * Gera um novo ID na tabela de Sequencias incrementando o contador.
     * 
     * @return
     */
    public Long gerarNovoId() {
	Long val = getNextValue(this.getNomeSequencia());

	while (this.encontrePorId(val.intValue()) != null) {
	    val = getNextValue(this.getNomeSequencia());
	}

	return val;
    }

    private Long getNextValue(String nomeSequencia) {
	Long val = 0L;

	String hql = "from Sequencia o where o.nome = :nome";
	Query q = session.createQuery(hql);
	q.setString("nome", nomeSequencia);

	Sequencia seq;
	Object o = q.uniqueResult();

	if (o != null) {
	    seq = (Sequencia) o;
	    session.buildLockRequest(LockOptions.UPGRADE).lock(seq);
	    val = seq.getValor() + 1;
	    seq.setValor(val);
	    session.update(seq);
	} else {
	    seq = new Sequencia();
	    seq.setNome(nomeSequencia);
	    seq.setValor(1);
	    val = 1L;
	    session.persist(seq);
	}
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
     * @param query Objeto tipo Query do Hibernate
     * @param limit Limite de registros a serem retornados
     * @return Lista de entidades encontradas no banco de dados. Se não encontrar
     *         registros, retorna uma lista vazia ao invés de uma lista nula
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
