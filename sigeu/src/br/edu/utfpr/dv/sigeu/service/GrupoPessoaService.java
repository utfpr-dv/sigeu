package br.edu.utfpr.dv.sigeu.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.dao.GrupoPessoaDAO;
import br.edu.utfpr.dv.sigeu.dao.PessoaDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.GrupoPessoa;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;

@Stateless
public class GrupoPessoaService {

    @EJB
    private PessoaDAO pessoaDAO;

    @EJB
    private GrupoPessoaDAO dao;

    /**
     * Cria um novo grupo
     * 
     * @param gp
     */
    public void criar(GrupoPessoa gp) {
	dao.criar(gp);
    }

    public void alterar(GrupoPessoa gp) {
	dao.alterar(gp);
    }

    /**
     * Exclui os grupos que não estão mais relacionados e inclui os grupos que
     * faltam relacionamento
     * 
     * @param pessoa Objeto pessoa do banco de dados
     * @param grupos Lista de grupos do LDAP
     */
    public void atualizaGrupos(Pessoa pessoa, List<GrupoPessoa> grupos) throws Exception {
	// try {
	// trans = new Transaction();
	// trans.begin();

	// Busca novamente do banco de dados
	// pessoa = pessoaDAO.encontrePorId(pessoa.getIdPessoa());

	List<GrupoPessoa> gruposCadastrados = pessoa.getGrupoPessoaList();
	// List<GrupoPessoa> listaVazia = new ArrayList<GrupoPessoa>();

	boolean modificado = false;

	if (gruposCadastrados == null || gruposCadastrados.size() == 0) {
	    modificado = true;
	}

	if (!modificado) {
	    // Verifica se algum grupo foi removido
	    for (GrupoPessoa gp : grupos) {
		boolean eliminado = true;
		boolean naoRelacionado = true;

		for (GrupoPessoa grupoCadastrado : gruposCadastrados) {
		    // if (grupoCadastrado.getNome().equals(gp.getNome())) {
		    if (grupoCadastrado.getIdGrupoPessoa() == gp.getIdGrupoPessoa()) {
			eliminado = false;
			break;
		    }
		}

		if (eliminado) {
		    modificado = true;
		    break;
		}

		for (GrupoPessoa grupoCadastrado : gruposCadastrados) {
		    // if (gp.getNome().equals(grupoCadastrado.getNome())) {
		    if (gp.getIdGrupoPessoa() == grupoCadastrado.getIdGrupoPessoa()) {
			naoRelacionado = false;
			break;
		    }
		}

		if (naoRelacionado) {
		    modificado = true;
		    break;
		}
	    }
	}

	if (modificado) {
	    // Elimina todos os grupos
	    if (gruposCadastrados != null && gruposCadastrados.size() > 0) {
		for (int i = pessoa.getGrupoPessoaList().size() - 1; i >= 0; i--) {
		    GrupoPessoa grupo = pessoa.getGrupoPessoaList().get(i);
		    grupo.getPessoaList().remove(pessoa);
		    dao.alterar(grupo);
		    // i--;
		}
	    }

	    pessoa.setGrupoPessoaList(null);
	    pessoaDAO.alterar(pessoa);

	    // Adiciona a pessoa a cada grupo
	    // List<Pessoa> pessoaList = new ArrayList<Pessoa>();
	    // pessoaList.add(pessoa);

	    // Inclui novamente os grupos buscando do banco de dados
	    gruposCadastrados = new ArrayList<GrupoPessoa>();

	    for (int i = 0; i < grupos.size(); i++) {
		Integer id = grupos.get(i).getIdGrupoPessoa();
		GrupoPessoa grupo = dao.encontrePorId(id);
		List<Pessoa> pessoaList = grupo.getPessoaList();

		if (pessoaList == null) {
		    pessoaList = new ArrayList<Pessoa>();
		}
		pessoaList.add(pessoa);

		grupo.setPessoaList(pessoaList);

		dao.alterar(grupo);
		gruposCadastrados.add(grupo);
	    }

	    // Readiciona todos os grupos
	    pessoa.setGrupoPessoaList(gruposCadastrados);

	    pessoaDAO.alterar(pessoa);
	}

	// trans.commit();
	// } catch (Exception e) {
	// throw e;
	// } finally {
	// if (trans != null) {
	// trans.close();
	// }
	//
	// }

    }

    /**
     * Retorna um grupo de pessoa por sua descricao ou null
     * 
     * @param descricao
     * @return
     * @throws SQLException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public GrupoPessoa encontrePorDescricao(Campus campus, String descricao)
	    throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {

	GrupoPessoa gp = dao.encontrePorDescricao(campus, descricao);

	if (gp != null) {
	    Hibernate.initialize(gp.getIdCampus());
	    Hibernate.initialize(gp.getIdCampus().getIdInstituicao());
	}

	return gp;
    }

    public GrupoPessoa encontrePorDescricaoCerto(Campus campus, String descricao)
	    throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {

	GrupoPessoa gp = dao.encontrePorDescricao(campus, descricao);

	if (gp != null) {
	    Hibernate.initialize(gp.getIdCampus());
	    Hibernate.initialize(gp.getIdCampus().getIdInstituicao());
	}

	return gp;
    }

    /**
     * Retorna um grupo de pessoa por seu ID
     * 
     * @return
     */
    public GrupoPessoa encontrePorId(Integer id) {
	GrupoPessoa gp = dao.encontrePorId(id);
	Hibernate.initialize(gp.getIdCampus());
	Hibernate.initialize(gp.getIdCampus().getIdInstituicao());

	return gp;
    }
}