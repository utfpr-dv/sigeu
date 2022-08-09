package br.edu.utfpr.dv.sigeu.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.dao.CategoriaItemReservaDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.CategoriaItemReserva;
import br.edu.utfpr.dv.sigeu.exception.EntidadePossuiRelacionamentoException;

@Stateless
public class CategoriaItemReservaService {

    @EJB
    private CategoriaItemReservaDAO dao;

    /**
     * Cria nova categoria
     * 
     * @param cat
     */
    public void criar(CategoriaItemReserva cat) {
	dao.criar(cat);
    }

    /**
     * Altera uma categoria existente
     * 
     * @param cat
     */
    public void alterar(CategoriaItemReserva cat) {
	dao.alterar(cat);
    }

    /**
     * Verifica se já existe objeto. Se não existe, cria. Se existe, atualiza.
     * 
     * @param cat
     * @throws Exception
     */
    public void persistir(CategoriaItemReserva cat) throws Exception {
	if (cat.getIdCategoria() != null) {
	    dao.alterar(cat);
	} else {
	    dao.criar(cat);
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
    public List<CategoriaItemReserva> pesquisar(Campus campus, String textoPesquisa, Boolean ativo) throws Exception {
	List<CategoriaItemReserva> lista = null;

	lista = dao.pesquisa(campus, textoPesquisa, ativo, 0);

	if (lista != null) {
	    for (CategoriaItemReserva c : lista) {
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
    public CategoriaItemReserva encontrePorId(Integer editarId) throws Exception {

	CategoriaItemReserva obj = dao.encontrePorId(editarId);

	if (obj != null) {
	    Hibernate.initialize(obj.getIdCampus());
	    Hibernate.initialize(obj.getIdCampus().getIdInstituicao());
	}
	return obj;
    }

    /**
     * Remove uma categoria
     * 
     * @param categoria
     * @throws Exception
     */
    public void remover(CategoriaItemReserva categoria) throws Exception {

	CategoriaItemReserva cat = dao.encontrePorId(categoria.getIdCategoria());

	if (cat != null) {
	    Hibernate.initialize(cat.getItemReservaList());
	}

	if (cat.getItemReservaList().size() > 0) {
	    throw new EntidadePossuiRelacionamentoException(cat.getNome());
	}

	dao.remover(cat);
    }
}