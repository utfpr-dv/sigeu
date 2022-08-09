package br.edu.utfpr.dv.sigeu.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.edu.utfpr.dv.sigeu.dao.PessoaDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.GrupoPessoa;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;

@Stateless
public class PessoaService {

    @EJB
    private PessoaDAO pessoaDAO;

    /**
     * Cria nova entrada para uma pessoa
     * 
     * @param p
     */
    public void criar(Pessoa p) throws Exception {
	pessoaDAO.criar(p);
    }

    /**
     * Altera uma pessoa existente
     * 
     * @param p
     */
    public void alterar(Pessoa p) throws Exception {
	pessoaDAO.alterar(p);
    }

    /**
     * Busca uma pessoa do banco de dados através do seu ID
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public Pessoa encontrePorId(Integer id) throws Exception {
	return encontrePorId(id, false);
    }

    /**
     * Busca uma pessoa do banco de dados através do seu ID
     * 
     * @return
     */
    public Pessoa encontrePorId(Integer id, boolean carregaGrupos) throws Exception {
	return pessoaDAO.encontrePorId(id);
    }

    /**
     * Busca uma pessoa do banco de dados através do seu Email e Campus
     * 
     * @param email
     * @param campus
     * @return
     * @throws Exception
     */
    public Pessoa encontrePorEmail(String email, Campus campus) throws Exception {
	return pessoaDAO.encontrePorEmail(email, campus);
    }

    public Pessoa encontrePorCnpjCpf(Campus campus, String cnpjCpf) throws Exception {
	return pessoaDAO.encontrePorCnpjCpf(campus, cnpjCpf);
    }

    /**
     * Pesquisa todas as pessoas
     * 
     * @param textoPesquisa
     * @return
     * @throws Exception
     */
    public List<Pessoa> pesquisar(Campus campus, String textoPesquisa) throws Exception {
	List<Pessoa> lista = null;

	if (textoPesquisa == null || textoPesquisa.trim().length() <= 0) {
	    lista = pessoaDAO.pesquisa(campus, HibernateDAO.PESQUISA_LIMITE);
	} else {
	    lista = pessoaDAO.pesquisa(campus, textoPesquisa, 0);
	}

	return lista;
    }

    /**
     * Pesquisa as pessoas passando o parametro ativo ou não
     * 
     * @param query
     * @param ativo
     * @return
     * @throws Exception
     */
    public List<Pessoa> pesquisar(Campus campus, String query, boolean ativo, int limit) throws Exception {
	List<Pessoa> lista = null;

	if (query == null || query.trim().length() <= 0) {
	    if (limit == 0) {
		limit = HibernateDAO.PESQUISA_LIMITE;
	    }
	    lista = pessoaDAO.pesquisa(campus, ativo, limit);
	} else {
	    lista = pessoaDAO.pesquisa(campus, query, ativo, limit);
	}

	return lista;
    }

    /**
     * Pesquisa as pessoas passando o parametro ativo ou não
     * 
     * @param query
     * @param ativo
     * @param grupo
     * @param limit
     * @return
     * @throws Exception
     */
    public List<Pessoa> pesquisar(Campus campus, String query, boolean ativo, String grupo, int limit) throws Exception {
	List<Pessoa> lista = null;
	List<Pessoa> listaRetorno = null;

	if (query == null || query.trim().length() <= 0) {
	    if (limit == 0) {
		limit = HibernateDAO.PESQUISA_LIMITE;
	    }
	    lista = pessoaDAO.pesquisa(campus, ativo, limit);
	} else {
	    lista = pessoaDAO.pesquisa(campus, query, ativo, limit);
	}

	listaRetorno = lista;

	if (grupo != null && grupo.trim().length() > 0 && lista != null && lista.size() > 0) {
	    listaRetorno = new ArrayList<Pessoa>();

	    grupo = grupo.trim().toUpperCase();

	    for (Pessoa p : lista) {

		for (GrupoPessoa gp : p.getGrupoPessoaList()) {
		    if (gp.getNome().trim().toUpperCase().equals(grupo)) {
			listaRetorno.add(p);
			break;
		    }
		}
	    }
	}

	return listaRetorno;
    }
}