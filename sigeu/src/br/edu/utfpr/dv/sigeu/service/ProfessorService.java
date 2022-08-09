package br.edu.utfpr.dv.sigeu.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.dao.ProfessorDAO;
import br.edu.utfpr.dv.sigeu.dao.ProfessorPessoaDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.entities.Professor;
import br.edu.utfpr.dv.sigeu.entities.ProfessorPessoa;

@Stateless
public class ProfessorService {

    @EJB
    private ProfessorDAO professorDAO;

    @EJB
    private ProfessorPessoaDAO professorPessoaDAO;

    /**
     * Realiza a pesquisa no banco de dados conforme o texto
     * 
     * @param textoPesquisa
     * @return
     * @throws Exception
     */
    public List<Professor> pesquisar(Campus campus, String textoPesquisa) throws Exception {
	List<Professor> lista = professorDAO.pesquisa(campus, textoPesquisa);

	if (lista != null && lista.size() > 0) {
	    for (Professor p : lista) {
		if (p.getProfessorPessoa() != null) {
		    Hibernate.initialize(p.getProfessorPessoa().getIdPessoa());
		}
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
    public Professor encontrePorId(Integer editarId) throws Exception {
	return professorDAO.encontrePorId(editarId);
    }

    /**
     * Atualiza registro de Professor Pessoa
     * 
     * @param pessoa
     * @param professor
     */
    public void atualizaProfessorPessoa(Pessoa pessoa, Professor professor) {
	if (pessoa != null && professor != null) {

	    boolean exists = false;

	    ProfessorPessoa pp = null;
	    pp = professorPessoaDAO.encontrePorId(professor.getIdProfessor());

	    if (pp == null) {
		exists = false;
		pp = new ProfessorPessoa();
		pp.setIdProfessor(professor.getIdProfessor());
		pp.setProfessor(professor);
	    }

	    pp.setIdPessoa(pessoa);

	    if (!exists) {
		professorPessoaDAO.criar(pp);
	    } else {
		professorPessoaDAO.alterar(pp);
	    }
	}
    }
}