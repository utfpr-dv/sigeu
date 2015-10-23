package br.edu.utfpr.dv.sigeu.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.dao.GrupoPessoaDAO;
import br.edu.utfpr.dv.sigeu.dao.PessoaDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.GrupoPessoa;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class GrupoPessoaService {

	/**
	 * Cria um novo grupo
	 * 
	 * @param gp
	 */
	public static void criar(GrupoPessoa gp) {
		Transaction trans = new Transaction();
		trans.begin();

		GrupoPessoaDAO dao = new GrupoPessoaDAO(trans);
		dao.criar(gp);

		trans.commit();
		trans.close();
	}

	public static void alterar(GrupoPessoa gp) {
		Transaction trans = new Transaction();
		trans.begin();

		GrupoPessoaDAO dao = new GrupoPessoaDAO(trans);
		dao.alterar(gp);

		trans.commit();
		trans.close();
	}

	/**
	 * Exclui os grupos que não estão mais relacionados e inclui os grupos que
	 * faltam relacionamento
	 * 
	 * @param trans
	 *            Transação de controle. Este método foi projetado para ser
	 *            utilizado em um loop com várias atualizações de grupos, e não
	 *            somente uma. Portanto o controle transacional não deve ficar
	 *            interno a este método.
	 * @param pessoa
	 *            Objeto pessoa do banco de dados
	 * @param grupos
	 *            Lista de grupos do LDAP
	 */
	public static void atualizaGrupos(Transaction trans, Pessoa pessoa, List<GrupoPessoa> grupos) throws Exception {
		// try {
		// trans = new Transaction();
		// trans.begin();

		PessoaDAO pessoaDAO = new PessoaDAO(trans);
		GrupoPessoaDAO grupoPessoaDAO = new GrupoPessoaDAO(trans);

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
					grupoPessoaDAO.alterar(grupo);
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
				GrupoPessoa grupo = grupoPessoaDAO.encontrePorId(id);
				List<Pessoa> pessoaList = grupo.getPessoaList();

				if (pessoaList == null) {
					pessoaList = new ArrayList<Pessoa>();
				}
				pessoaList.add(pessoa);

				grupo.setPessoaList(pessoaList);

				grupoPessoaDAO.alterar(grupo);
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
	public static GrupoPessoa encontrePorDescricao(Campus campus, String descricao)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
		Transaction trans = new Transaction();
		trans.begin();

		GrupoPessoaDAO dao = new GrupoPessoaDAO(trans);
		GrupoPessoa gp = dao.encontrePorDescricao(campus, descricao);

		if (gp != null) {
			Hibernate.initialize(gp.getIdCampus());
			Hibernate.initialize(gp.getIdCampus().getIdInstituicao());
		}
		// trans.commit();
		trans.close();

		return gp;
	}

	/**
	 * Retorna um grupo de pessoa por seu ID
	 * 
	 * @return
	 */
	public static GrupoPessoa encontrePorId(Integer id) {
		Transaction trans = new Transaction();
		trans.begin();

		GrupoPessoaDAO dao = new GrupoPessoaDAO(trans);
		GrupoPessoa gp = dao.encontrePorId(id);
		Hibernate.initialize(gp.getIdCampus());
		Hibernate.initialize(gp.getIdCampus().getIdInstituicao());

		// trans.commit();
		trans.close();

		return gp;
	}
}
