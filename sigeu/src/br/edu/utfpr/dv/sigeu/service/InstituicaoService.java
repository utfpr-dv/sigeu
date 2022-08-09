package br.edu.utfpr.dv.sigeu.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.dao.InstituicaoDAO;
import br.edu.utfpr.dv.sigeu.entities.Instituicao;
import br.edu.utfpr.dv.sigeu.exception.EntidadePossuiRelacionamentoException;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;

@Stateless
public class InstituicaoService {

    @EJB
    private InstituicaoDAO dao;

    /**
     * Cria novo
     * 
     * @param i
     */
    public void criar(Instituicao i) {
	dao.criar(i);
    }

    /**
     * Altera existente
     * 
     * @param i
     */
    public void alterar(Instituicao i) {
	dao.alterar(i);
    }

    /**
     * Realiza a pesquisa no banco de dados conforme o texto
     * 
     * @param textoPesquisa
     * @return
     * @throws Exception
     */
    public List<Instituicao> pesquisar(String textoPesquisa) throws Exception {
	List<Instituicao> lista = null;

	if (textoPesquisa == null || textoPesquisa.trim().length() <= 0) {
	    lista = dao.pesquisa(HibernateDAO.PESQUISA_LIMITE);
	} else {
	    lista = dao.pesquisa(textoPesquisa, 0);
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
    public Instituicao encontrePorId(Integer editarId) throws Exception {
	return dao.encontrePorId(editarId);
    }

    /**
     * Remove
     * 
     * @param i
     * @throws MessagingException
     * @throws Exception
     */
    public void remover(Instituicao i) throws EntidadePossuiRelacionamentoException, Exception {
	Instituicao instituicaoBd = dao.encontrePorId(i.getIdInstituicao());

	Hibernate.initialize(instituicaoBd.getCampusList());

	if (instituicaoBd.getCampusList().size() > 0) {
	    throw new EntidadePossuiRelacionamentoException(instituicaoBd.getNome());
	}

	dao.remover(instituicaoBd);
    }
}
