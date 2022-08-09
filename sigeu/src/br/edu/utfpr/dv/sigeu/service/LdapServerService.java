package br.edu.utfpr.dv.sigeu.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.dao.LdapServerDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.LdapServer;
import br.edu.utfpr.dv.sigeu.exception.EntidadePossuiRelacionamentoException;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;

@Stateless
public class LdapServerService {

    @EJB
    private LdapServerDAO dao;

    /**
     * Cria novo
     * 
     * @param i
     */
    public void criar(LdapServer i) {
	dao.criar(i);
    }

    /**
     * Altera existente
     * 
     * @param i
     */
    public void alterar(LdapServer i) {
	dao.alterar(i);
    }

    /**
     * Realiza a pesquisa no banco de dados conforme o texto
     * 
     * @param textoPesquisa
     * @return
     * @throws Exception
     */
    public List<LdapServer> pesquisar(Campus campus, String textoPesquisa) throws Exception {
	List<LdapServer> lista = null;

	if (textoPesquisa == null || textoPesquisa.trim().length() <= 0) {
	    lista = dao.pesquisa(campus, HibernateDAO.PESQUISA_LIMITE);
	} else {
	    lista = dao.pesquisa(campus, textoPesquisa, 0);
	}

	if (lista != null) {
	    for (LdapServer l : lista) {
		Hibernate.initialize(l.getIdCampus());
		Hibernate.initialize(l.getIdCampus().getIdInstituicao());
	    }
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
    public LdapServer encontrePorId(Integer editarId) throws Exception {
	LdapServer obj = dao.encontrePorId(editarId);
	if (obj != null) {
	    Hibernate.initialize(obj.getIdCampus());
	    Hibernate.initialize(obj.getIdCampus().getIdInstituicao());
	}
	return obj;
    }

    /**
     * Remove
     * 
     * @param i
     * @throws MessagingException
     * @throws Exception
     */
    public void remover(LdapServer i) throws EntidadePossuiRelacionamentoException, Exception {
	LdapServer ldapServerBd = dao.encontrePorId(i.getIdServer());

	dao.remover(ldapServerBd);
    }

    public LdapServer encontrePorEmail(String email) throws Exception {
	return dao.encontrePorEmail(email);
    }
}
