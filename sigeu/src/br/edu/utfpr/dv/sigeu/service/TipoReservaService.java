package br.edu.utfpr.dv.sigeu.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.dao.TipoReservaDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.TipoReserva;
import br.edu.utfpr.dv.sigeu.exception.EntidadePossuiRelacionamentoException;

@Stateless
public class TipoReservaService {

    @EJB
    private TipoReservaDAO dao;

    /**
     * Cria novo tipo
     * 
     * @param tipo
     */
    public void criar(TipoReserva tipo) throws Exception {
	dao.criar(tipo);
    }

    /**
     * Altera um tipo existente
     * 
     * @param obj
     */
    public void alterar(TipoReserva obj) throws Exception {
	dao.alterar(obj);
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
	List<TipoReserva> lista = dao.pesquisa(campus, textoPesquisa, ativo, 0);

	if (lista != null) {
	    for (TipoReserva c : lista) {
		Hibernate.initialize(c.getIdCampus());
		Hibernate.initialize(c.getIdCampus().getIdInstituicao());
	    }
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
	TipoReserva obj = dao.encontrePorId(editarId);

	if (obj != null) {
	    Hibernate.initialize(obj.getIdCampus());
	    Hibernate.initialize(obj.getIdCampus().getIdInstituicao());
	}

	return obj;
    }

    /**
     * Remove um tipo
     * 
     * @param categoria
     * @throws Exception
     */
    public void remover(TipoReserva categoria) throws Exception {
	TipoReserva tipo = dao.encontrePorId(categoria.getIdTipoReserva());

	if (tipo != null) {
	    Hibernate.initialize(tipo.getReservaList());
	}

	if (tipo.getReservaList().size() > 0) {
	    throw new EntidadePossuiRelacionamentoException(tipo.getDescricao());
	}

	dao.remover(tipo);
    }
}