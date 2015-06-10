package br.edu.utfpr.dv.sigeu.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.config.Config;
import br.edu.utfpr.dv.sigeu.dao.PessoaDAO;
import br.edu.utfpr.dv.sigeu.entities.GrupoPessoa;
import br.edu.utfpr.dv.sigeu.entities.LdapServer;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

import com.adamiworks.utils.ldap.LdapUtils;

public class PessoaService {

	/**
	 * Cria nova entrada para uma pessoa
	 * 
	 * @param p
	 */
	public static void criar(Pessoa p) throws Exception {
		Transaction trans = new Transaction();
		trans.begin();

		PessoaDAO dao = new PessoaDAO(trans);
		dao.criar(p);

		trans.commit();
		trans.close();
	}

	/**
	 * Altera uma pessoa existente
	 * 
	 * @param p
	 */
	public static void alterar(Pessoa p) throws Exception {
		Transaction trans = new Transaction();
		trans.begin();

		PessoaDAO dao = new PessoaDAO(trans);
		dao.alterar(p);

		trans.commit();
		trans.close();
	}

	/**
	 * Busca uma pessoa do banco de dados através do seu ID
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static Pessoa encontrePorId(Integer id) throws Exception {
		return PessoaService.encontrePorId(id, false);
	}

	/**
	 * Busca uma pessoa do banco de dados através do seu ID
	 * 
	 * @return
	 */
	public static Pessoa encontrePorId(Integer id, boolean carregaGrupos)
			throws Exception {
		Transaction trans = new Transaction();
		Pessoa p = null;
		try {
			trans.begin();

			PessoaDAO dao = new PessoaDAO(trans);
			p = dao.encontrePorId(id);

			if (p != null) {
				Hibernate.initialize(p.getIdCampus());
				Hibernate.initialize(p.getIdCampus().getIdInstituicao());
				Hibernate.initialize(p.getGrupoPessoaList());
			}

			// if (carregaGrupos) {
			// for (GrupoPessoa gp : p.getGrupoPessoaList()) {
			// // Faz nada
			// }
			// }
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			trans.close();
		}

		return p;
	}

	/**
	 * Busca uma pessoa do banco de dados através do seu Email
	 * 
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public static Pessoa encontrePorEmail(String email) throws Exception {
		Transaction trans = new Transaction();
		trans.begin();

		PessoaDAO dao = new PessoaDAO(trans);
		Pessoa p;
		try {
			p = dao.encontrePorEmail(email);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			trans.close();
		}

		return p;
	}

	public static Pessoa encontrePorCnpjCpf(String cnpjCpf) throws Exception {
		Transaction trans = new Transaction();
		trans.begin();

		PessoaDAO dao = new PessoaDAO(trans);
		Pessoa p;
		try {
			p = dao.encontrePorCnpjCpf(Config.getInstance().getCampus(),
					cnpjCpf);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			trans.close();
		}

		return p;
	}

	/**
	 * Pesquisa todas as pessoas
	 * 
	 * @param textoPesquisa
	 * @return
	 * @throws Exception
	 */
	public static List<Pessoa> pesquisar(String textoPesquisa) throws Exception {
		List<Pessoa> lista = null;

		Transaction trans = new Transaction();

		try {
			trans.begin();

			PessoaDAO dao = new PessoaDAO(trans);
			if (textoPesquisa == null || textoPesquisa.trim().length() <= 0) {
				lista = dao.pesquisa(Config.getInstance().getCampus(),
						HibernateDAO.PESQUISA_LIMITE);
			} else {
				lista = dao.pesquisa(Config.getInstance().getCampus(),
						textoPesquisa, 0);
			}

			if (lista != null) {
				for (Pessoa p : lista) {
					Hibernate.initialize(p.getIdCampus());
					Hibernate.initialize(p.getIdCampus().getIdInstituicao());
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
	 * Lê toda a estrutura de cadastros do servidor LDAP e cadastra todas as
	 * pessoas na base de dados
	 * 
	 * @param emailLogin
	 *            e-mail de quem fez o login para buscar qual é o servidor LDAP.
	 * @throws Exception
	 */
	public static void atualizaPessoasLdap(String emailLogin) throws Exception {
		LdapServer ldap = LdapServerService.encontrePorEmail(emailLogin);
		LdapUtils ldapUtils = new LdapUtils(ldap.getHost(), ldap.getPort(),
				ldap.getSsl(), true, ldap.getBasedn(), ldap.getVarLdapUid());

		// ///////////////////////////////////
		List<String> mapa = ldapUtils.getAllLdapInfo(ldap.getVarLdapUid());

		int criadas = 0;
		int alteradas = 0;

		for (String s : mapa) {
			String attrs[] = s.split(LdapUtils.ENTRY_SEPARATOR);

			String cnpjCpf = null;
			String matricula = null;
			String nomeCompleto = null;
			String email = null;
			String uid = null;
			String lCampus = null;

			for (String a : attrs) {
				String map[] = a.split(":");

				if (map[0].trim().toUpperCase()
						.equals(ldap.getVarLdapCnpjCpf().toUpperCase())) {
					cnpjCpf = map[1].trim();
				}

				if (map[0].trim().toUpperCase()
						.equals(ldap.getVarLdapEmail().toUpperCase())) {
					email = map[1].trim();
				}

				if (map[0].trim().toUpperCase()
						.equals(ldap.getVarLdapMatricula().toUpperCase())) {
					matricula = map[1].trim();
				}

				if (map[0].trim().toUpperCase()
						.equals(ldap.getVarLdapNomeCompleto().toUpperCase())) {
					nomeCompleto = map[1].trim();
				}

				if (map[0].trim().toUpperCase()
						.equals(ldap.getVarLdapUid().toUpperCase())) {
					uid = map[1].trim();
				}

				if (map[0].trim().toUpperCase()
						.equals(ldap.getVarLdapCampus().toUpperCase())) {
					lCampus = map[1].trim();
				}
			}

			if (uid.matches("[A-Z|a-z]{1}[0-9].*")) {
				// Ignora os cadastros de alunos
				continue;
			}

			System.out
					.println("==========================================================");
			System.out.println(uid);

			Pessoa pessoa = null;

			if (cnpjCpf == null || matricula == null || nomeCompleto == null) {
				continue;
			}

			if (email == null || email.trim().length() == 0) {
				// Restringe cadastro apenas a servidores que possuem e-mail
				continue;
			}

			// Cadastra apenas pessoas do mesmo servidor de e-mail
			/**
			 * CANCELADO
			 */
			// if (!ldap.getIdCampus().getSigla().toUpperCase().trim()
			// .equals(lCampus.toUpperCase())) {
			// continue;
			// }

			pessoa = PessoaService.encontrePorEmail(email);

			boolean update = true;

			// Atualiza dados da Pessoa/Usuário
			if (pessoa == null) {
				pessoa = new Pessoa();
				update = false;
				pessoa.setAtivo(true);
				pessoa.setSenhaMd5("00000000000000000000000000000000");
			} else {
				int fieldsIgnored = 0;

				if (pessoa.getCnpjCpf().equals(cnpjCpf)) {
					fieldsIgnored++;
				}

				if (pessoa.getEmail().equals(email)) {
					fieldsIgnored++;
				}

				if (pessoa.getMatricula().equals(matricula)) {
					fieldsIgnored++;
				}

				if (pessoa.getNomeCompleto().equals(nomeCompleto)) {
					fieldsIgnored++;
				}

				// Se todos os campos continuam iguais, não faz nada
				if (fieldsIgnored >= 4) {
					continue;
				}
			}

			pessoa.setCnpjCpf(cnpjCpf);
			pessoa.setEmail(email);
			pessoa.setIdCampus(ldap.getIdCampus());
			pessoa.setMatricula(matricula);
			pessoa.setNomeCompleto(nomeCompleto);
			pessoa.setPessoaFisica(true);

			if (update) {
				PessoaService.alterar(pessoa);
				alteradas++;
			} else {

				PessoaService.criar(pessoa);
				criadas++;
			}

			// Confere os grupos da Pessoa
			String baseDn = ldapUtils.getDnByUid(uid);
			List<GrupoPessoa> grupos = new ArrayList<GrupoPessoa>();
			List<String> nomeGrupos = ldapUtils.getLdapOuByUid(uid, baseDn);

			for (String grupo : nomeGrupos) {
				GrupoPessoa gp = GrupoPessoaService.encontrePorDescricao(grupo);

				if (gp == null) {
					gp = new GrupoPessoa();
					gp.setIdCampus(ldap.getIdCampus());
					gp.setNome(grupo);

					GrupoPessoaService.criar(gp);
				}

				grupos.add(gp);
			}

			// Atualiza os grupos da pessoa
			GrupoPessoaService.atualizaGrupos(pessoa, grupos);

			System.out.println("Criadas: " + criadas + " / Alteradas: "
					+ alteradas);
			System.out
					.println("==========================================================\n");
		}
	}

	/**
	 * Pesquisa as pessoas passando o parametro ativo ou não
	 * 
	 * @param query
	 * @param ativo
	 * @return
	 * @throws Exception
	 */
	public static List<Pessoa> pesquisar(String query, boolean ativo, int limit)
			throws Exception {
		List<Pessoa> lista = null;

		Transaction trans = new Transaction();

		try {
			trans.begin();

			PessoaDAO dao = new PessoaDAO(trans);

			if (query == null || query.trim().length() <= 0) {
				if (limit == 0) {
					limit = HibernateDAO.PESQUISA_LIMITE;
				}
				lista = dao.pesquisa(Config.getInstance().getCampus(), ativo,
						limit);
			} else {
				lista = dao.pesquisa(Config.getInstance().getCampus(), query,
						ativo, limit);
			}

			if (lista != null) {
				for (Pessoa p : lista) {
					Hibernate.initialize(p.getIdCampus());
					Hibernate.initialize(p.getIdCampus().getIdInstituicao());
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
}
