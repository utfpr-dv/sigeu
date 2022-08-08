package br.edu.utfpr.dv.sigeu.service;

import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.dao.TipoReservaDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.TipoReserva;
import br.edu.utfpr.dv.sigeu.exception.EntidadePossuiRelacionamentoException;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

@Stateless
public class TipoReservaService {

    /**
     * Cria novo tipo
     * 
     * @param tipo
     */
    public void criar(TipoReserva tipo) throws Exception {
	Transaction trans = null;
	try {
	    trans = new Transaction();
	    trans.begin();

	    TipoReservaDAO dao = new TipoReservaDAO(trans);
	    dao.criar(tipo);

	    trans.commit();
	} catch (Exception e) {
	    throw e;
	} finally {
	    trans.close();
	}
    }

    /**
     * Altera um tipo existente
     * 
     * @param obj
     */
    public void alterar(TipoReserva obj) throws Exception {
	Transaction trans = new Transaction();
	try {
	    trans.begin();

	    TipoReservaDAO dao = new TipoReservaDAO(trans);
	    dao.alterar(obj);

	    trans.commit();
	} catch (Exception e) {
	    throw e;
	} finally {
	    trans.close();
	}
    }

    /**
     * Verifica se já existe objeto. Se não existe, cria. Se existe, atualiza.
     * 
     * @param obj
     * @throws Exception
     */
    public void persistir(TipoReserva obj) throws Exception {
	if (obj.getIdTipoReserva() != null) {
	    alterar(obj);
	} else {
	    criar(obj);
	}
    }

    /**
     * Realiza a pesquisa no banco de dados conforme o texto
     * 
     * @param textoPesquisa
     * @param ativo         Informar null para trazer todos os objetos
     *                      ativos/inativos
     * @return
     * @throws Exception
     */
    public List<TipoReserva> pesquisar(Campus campus, String textoPesquisa, Boolean ativo) throws Exception {
	List<TipoReserva> lista = null;
	Transaction trans = new Transaction();

	try {
	    trans.begin();
	    TipoReservaDAO dao = new TipoReservaDAO(trans);

	    lista = dao.pesquisa(campus, textoPesquisa, ativo, 0);

	    if (lista != null) {
		for (TipoReserva c : lista) {
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
    public TipoReserva encontrePorId(Integer editarId) throws Exception {
	Transaction trans = new Transaction();

	try {
	    trans.begin();
	    TipoReservaDAO dao = new TipoReservaDAO(trans);
	    TipoReserva obj = dao.encontrePorId(editarId);

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
     * Remove um tipo
     * 
     * @param categoria
     * @throws Exception
     */
    public void remover(TipoReserva categoria) throws Exception {
	Transaction trans = new Transaction();

	try {
	    trans.begin();

	    TipoReservaDAO dao = new TipoReservaDAO(trans);
	    TipoReserva tipo = dao.encontrePorId(categoria.getIdTipoReserva());

	    if (tipo != null) {
		Hibernate.initialize(tipo.getReservaList());
	    }

	    if (tipo.getReservaList().size() > 0) {
		throw new EntidadePossuiRelacionamentoException(tipo.getDescricao());
	    }

	    dao.remover(tipo);
	    trans.commit();
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new Exception(e);
	} finally {
	    trans.close();
	}
    }

}
