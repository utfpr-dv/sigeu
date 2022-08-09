package br.edu.utfpr.dv.sigeu.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.dao.FeriadoDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.Feriado;

@Stateless
public class FeriadoService {

    @EJB
    private FeriadoDAO dao;

    /**
     * Cria nova
     * 
     * @param cat
     */
    public void criar(Feriado cat) {
	dao.criar(cat);
    }

    /**
     * Altera uma existente
     * 
     * @param cat
     */
    public void alterar(Feriado cat) {
	dao.alterar(cat);
    }

    /**
     * Verifica se já existe objeto. Se não existe, cria. Se existe, atualiza.
     * 
     * @param item
     * @throws Exception
     */
    public void persistir(Feriado item) throws Exception {
	if (item.getIdFeriado() != null) {
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
    public List<Feriado> pesquisar(Campus campus, String textoPesquisa) throws Exception {
	List<Feriado> lista = null;

	if (textoPesquisa == null || textoPesquisa.trim().length() <= 0) {
	    lista = dao.pesquisa(campus, 0);
	} else {
	    lista = dao.pesquisa(campus, textoPesquisa, 0);
	}

	if (lista != null) {
	    for (Feriado c : lista) {
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
    public Feriado encontrePorId(Integer editarId) throws Exception {
	Feriado obj = dao.encontrePorId(editarId);

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
    public void remover(Feriado item) throws Exception {
	Feriado existente = dao.encontrePorId(item.getIdFeriado());

	dao.remover(existente);
    }

    public List<Feriado> pesquisarPorData(Campus campus, Date dataInicial, Date dataFinal) throws Exception {
	List<Feriado> lista = null;

	lista = dao.pesquisa(campus, dataInicial, dataFinal);

	if (lista != null) {
	    for (Feriado c : lista) {
		Hibernate.initialize(c.getIdCampus());
		Hibernate.initialize(c.getIdCampus().getIdInstituicao());
	    }
	}

	return lista;
    }

    /**
     * Confere se a data passada como parâmetro é um feriado.
     * 
     * @param data Data a ser conferida
     * @return Retorna true se ao menos um feriado for encontrado na data.
     */
    public boolean verificaFeriado(Campus campus, Date data) {
	List<Feriado> lista = null;

	lista = dao.pesquisa(campus, data);

	return (lista != null && lista.size() > 0);

    }
}