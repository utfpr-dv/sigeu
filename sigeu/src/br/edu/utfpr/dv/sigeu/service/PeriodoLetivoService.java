package br.edu.utfpr.dv.sigeu.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.dao.PeriodoLetivoDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.PeriodoLetivo;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;

@Stateless
public class PeriodoLetivoService {

    @EJB
    private PeriodoLetivoDAO dao;

    /**
     * Cria nova
     * 
     * @param pl
     */
    public void criar(PeriodoLetivo pl) throws Exception {
	dao.criar(pl);
    }

    /**
     * Altera uma existente
     * 
     * @param cat
     */
    public void alterar(PeriodoLetivo cat) throws Exception {
	dao.alterar(cat);
    }

    /**
     * Verifica se já existe objeto. Se não existe, cria. Se existe, atualiza.
     * 
     * @param item
     * @throws Exception
     */
    public void persistir(PeriodoLetivo item) throws Exception {
	if (item.getIdPeriodoLetivo() != null) {
	    dao.alterar(item);
	} else {
	    dao.criar(item);
	}
    }

    /**
     * Realiza a pesquisa no banco de dados conforme o texto
     * 
     * @param textoPesquisa
     * @return
     * @throws Exception
     */
    public List<PeriodoLetivo> pesquisar(Campus campus, String textoPesquisa) throws Exception {
	List<PeriodoLetivo> lista = null;

	if (textoPesquisa == null || textoPesquisa.trim().length() <= 0) {
	    lista = dao.pesquisa(campus, HibernateDAO.PESQUISA_LIMITE);
	} else {
	    lista = dao.pesquisa(campus, textoPesquisa, 0);
	}

	if (lista != null) {
	    for (PeriodoLetivo c : lista) {
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
    public PeriodoLetivo encontrePorId(Integer editarId) throws Exception {
	PeriodoLetivo obj = dao.encontrePorId(editarId);
	if (obj != null) {
	    Hibernate.initialize(obj.getIdCampus());
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
    public void remover(PeriodoLetivo item) throws Exception {
	PeriodoLetivo existente = dao.encontrePorId(item.getIdPeriodoLetivo());

	dao.remover(existente);
    }

    /**
     * Realiza a pesquisa no banco de dados pelo campus
     * 
     * @param campus
     * @return
     * @throws Exception
     */
    public List<PeriodoLetivo> pesquisar(Campus campus) throws Exception {
	return dao.pesquisa(campus);
    }

    public PeriodoLetivo encontrePorNome(Campus campus, String value) throws Exception {
	return dao.encontrePorNome(campus, value);
    }

    public PeriodoLetivo encontreAtual(Campus campus, Date data) throws Exception {
	return dao.encontreAtual(campus, data);
    }
}