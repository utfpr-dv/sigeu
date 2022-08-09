package br.edu.utfpr.dv.sigeu.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.dao.ItemReservaDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.CategoriaItemReserva;
import br.edu.utfpr.dv.sigeu.entities.ItemReserva;
import br.edu.utfpr.dv.sigeu.exception.EntidadePossuiRelacionamentoException;

@Stateless
public class ItemReservaService {

    @EJB
    private ItemReservaDAO dao;

    /**
     * Cria nova
     * 
     * @param cat
     */
    public void criar(ItemReserva cat) {
	dao.criar(cat);
    }

    /**
     * Altera uma existente
     * 
     * @param cat
     */
    public void alterar(ItemReserva cat) {
	dao.alterar(cat);
    }

    /**
     * Verifica se já existe objeto. Se não existe, cria. Se existe, atualiza.
     * 
     * @param item
     * @throws Exception
     */
    public void persistir(ItemReserva item) throws Exception {
	// Anula o código de patrimônio se forem inseridos apenas espaços em
	// branco
	if (item.getPatrimonio() != null && item.getPatrimonio().trim().length() == 0) {
	    item.setPatrimonio(null);
	} else {
	    item.setPatrimonio(item.getPatrimonio().toUpperCase().trim());
	}

	if (item.getIdItemReserva() != null) {
	    dao.alterar(item);
	} else {
	    dao.criar(item);
	}
    }

    /**
     * Realiza a pesquisa no banco de dados conforme o texto
     * 
     * @param textoPesquisa
     * @param ativo         null para todos
     * @return
     * @throws Exception
     */
    public List<ItemReserva> pesquisar(Campus campus, String textoPesquisa, Boolean ativo) throws Exception {
	return pesquisar(campus, null, textoPesquisa, ativo);
    }

    /**
     * 
     * @param editarId
     * @return
     * @throws Exception
     */
    public ItemReserva encontrePorId(Integer editarId) throws Exception {
	ItemReserva obj = dao.encontrePorId(editarId);
	if (obj != null) {
	    Hibernate.initialize(obj.getIdCampus());
	    Hibernate.initialize(obj.getIdCategoria());
	    Hibernate.initialize(obj.getIdCampus().getIdInstituicao());
	}
	return obj;
    }

    /**
     * Remove uma
     * 
     * @param
     * @throws Exception
     */
    public void remover(ItemReserva item) throws Exception {
	ItemReserva existente = dao.encontrePorId(item.getIdItemReserva());

	if (existente != null) {
	    Hibernate.initialize(existente.getReservaList());
	}

	if (existente.getReservaList().size() > 0) {
	    throw new EntidadePossuiRelacionamentoException(existente.getNome());
	}

	dao.remover(existente);
    }

    public List<ItemReserva> pesquisar(Campus campus, CategoriaItemReserva categoriaItemReserva, String textoPesquisa, Boolean ativo) throws Exception {
	List<ItemReserva> lista = null;

	lista = dao.pesquisa(campus, categoriaItemReserva, textoPesquisa, ativo, 0);

	if (lista != null) {
	    for (ItemReserva c : lista) {
		Hibernate.initialize(c.getIdCampus());
		Hibernate.initialize(c.getIdCategoria());
		Hibernate.initialize(c.getIdCampus().getIdInstituicao());
	    }
	}

	return lista;
    }
}
