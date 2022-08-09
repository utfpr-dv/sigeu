package br.edu.utfpr.dv.sigeu.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.dao.CampusDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.Instituicao;
import br.edu.utfpr.dv.sigeu.exception.EntidadePossuiRelacionamentoException;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;

@Stateless
public class CampusService {

    @EJB
    private CampusDAO campusDAO;

    /**
     * Conta a qtde de campus
     */
    public Integer contarCampus() {
	return campusDAO.contarCampus();
    }

    /**
     * Cria novo
     * 
     * @param i
     */
    public void criar(Campus i) {
	campusDAO.criar(i);
    }

    /**
     * Altera existente
     * 
     * @param i
     */
    public void alterar(Campus i) {
	campusDAO.alterar(i);
    }

    /**
     * Realiza a pesquisa no banco de dados conforme o texto
     * 
     * @param textoPesquisa
     * @return
     * @throws Exception
     */
    public List<Campus> pesquisar(String textoPesquisa) throws Exception {
	List<Campus> lista = null;

	lista = campusDAO.pesquisa(textoPesquisa, 0);

	for (Campus c : lista) {
	    Hibernate.initialize(c.getIdInstituicao());
	}

	return lista;
    }

    /**
     * Realiza a pesquisa no banco de dados conforme o texto
     * 
     * @param textoPesquisa
     * @return
     * @throws Exception
     */
    public List<Campus> pesquisar(String textoPesquisa, Instituicao instituicao) throws Exception {
	List<Campus> lista = null;

	if (textoPesquisa == null || textoPesquisa.trim().length() <= 0) {
	    lista = campusDAO.pesquisa(instituicao, HibernateDAO.PESQUISA_LIMITE);
	} else {
	    lista = campusDAO.pesquisa(textoPesquisa, instituicao, 0);
	}

	for (Campus c : lista) {
	    Hibernate.initialize(c.getIdInstituicao());
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
    public Campus encontrePorId(Integer editarId) throws Exception {
	Campus obj = campusDAO.encontrePorId(editarId);
	Hibernate.initialize(obj.getIdInstituicao());

	return obj;
    }

    /**
     * Remove
     * 
     * @param i
     * @throws MessagingException
     * @throws Exception
     */
    public void remover(Campus i) throws EntidadePossuiRelacionamentoException, Exception {
	Campus campusBd = campusDAO.encontrePorId(i.getIdCampus());

	Hibernate.initialize(campusBd.getGrupoPessoaList());
	Hibernate.initialize(campusBd.getPessoaList());

	if (campusBd.getGrupoPessoaList().size() > 0 || campusBd.getPessoaList().size() > 0) {
	    throw new EntidadePossuiRelacionamentoException(campusBd.getNome());
	}

	campusDAO.remover(campusBd);
    }

    public Campus encontrePorEmail(String email) throws Exception {
	Campus obj = campusDAO.encontrePorEmail(email);

	if (obj != null) {
	    Hibernate.initialize(obj.getIdInstituicao());
	    Hibernate.initialize(obj.getIdCampus());
	    Hibernate.initialize(obj.getLdapServerList());
	}
	return obj;
    }

    public Campus encontrePorSigla(String sigla) throws Exception {
	Campus obj = campusDAO.encontrePorSigla(sigla);

	if (obj != null) {
	    Hibernate.initialize(obj.getIdInstituicao());
	    Hibernate.initialize(obj.getIdCampus());
	    Hibernate.initialize(obj.getLdapServerList());
	}
	return obj;
    }
}