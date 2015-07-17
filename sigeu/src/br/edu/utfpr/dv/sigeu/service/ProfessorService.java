package br.edu.utfpr.dv.sigeu.service;

import java.util.List;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.dao.ProfessorDAO;
import br.edu.utfpr.dv.sigeu.dao.ProfessorPessoaDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.entities.Professor;
import br.edu.utfpr.dv.sigeu.entities.ProfessorPessoa;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class ProfessorService {

	/**
	 * Realiza a pesquisa no banco de dados conforme o texto
	 * 
	 * @param textoPesquisa
	 * @return
	 * @throws Exception
	 */
	public static List<Professor> pesquisar(Campus campus, String textoPesquisa)
			throws Exception {
		List<Professor> lista = null;

		Transaction trans = new Transaction();

		try {
			trans.begin();

			ProfessorDAO dao = new ProfessorDAO(trans);
			lista = dao.pesquisa(campus, textoPesquisa);

			if (lista != null && lista.size() > 0) {
				for (Professor p : lista) {
					if (p.getProfessorPessoa() != null) {
						Hibernate.initialize(p.getProfessorPessoa()
								.getIdPessoa());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
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
	public static Professor encontrePorId(Integer editarId) throws Exception {
		Transaction trans = new Transaction();

		try {
			trans.begin();

			ProfessorDAO dao = new ProfessorDAO(trans);
			Professor obj = dao.encontrePorId(editarId);

			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}

	/**
	 * Atualiza registro de Professor Pessoa
	 * 
	 * @param pessoa
	 * @param professor
	 */
	public static void atualizaProfessorPessoa(Pessoa pessoa,
			Professor professor) {
		if (pessoa != null && professor != null) {

			Transaction trans = null;

			try {
				trans = new Transaction();
				trans.begin();

				boolean exists = false;
				ProfessorPessoaDAO professorPessoaDAO = new ProfessorPessoaDAO(
						trans);
				ProfessorPessoa pp = null;
				pp = professorPessoaDAO.encontrePorId(professor
						.getIdProfessor());

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

				trans.commit();
			} catch (Exception e) {
				throw e;
			} finally {
				if (trans != null) {
					trans.close();
				}
			}
		}
	}

}
