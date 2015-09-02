package br.edu.utfpr.dv.sigeu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import br.edu.utfpr.dv.sigeu.config.Config;
import br.edu.utfpr.dv.sigeu.dao.LdapServerDAO;
import br.edu.utfpr.dv.sigeu.dao.PessoaDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.GrupoPessoa;
import br.edu.utfpr.dv.sigeu.entities.LdapServer;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.exception.ServidorLdapNaoCadastradoException;
import br.edu.utfpr.dv.sigeu.exception.UsuarioDesativadoException;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

import com.adamiworks.utils.StringUtils;
import com.adamiworks.utils.ldap.LdapUtils;

public class LoginService {

	/**
	 * Autentica usuário através de e-mail e senha.
	 * 
	 * @param email
	 * @param password
	 * @return Pessoa
	 * @throws Exception
	 */
	public static Pessoa autentica(String email, String password)
			throws Exception {

		boolean admin = email.trim().toUpperCase().equals("ADMIN");

		/*
		 * ===================================================================
		 * TODO - REMOVER DEPOIS QUE CONFIGURAR LOGIN POR INSTITUIÇÃO E CAMPUS
		 * ===================================================================
		 */
		// Fixa Campus DV
		Campus campus = CampusService.encontrePorId(100);

		// Se não é um e-mail, apenas nome, então...
		if (!admin && !email.contains("@")) {
			email += campus.getLdapServerList().get(0).getSufixoEmail();
		}

		/* =================================================================== */

		Pessoa pessoa = null;
		String hash = null;

		// Confere se autenticou por LDAP. Em caso positivo, já cadastra a
		// pessoa no banco de dados
		// LdapServer ldap = LoginService.getLdapByEmail(email);
		LdapServer ldap = null;

		if (!admin) {
			ldap = campus.getLdapServerList().get(0);
		}

		if (!admin && ldap == null) {
			// Não existe servidor cadastrado
			throw new ServidorLdapNaoCadastradoException(
					"E-mail inválido ou Servidor LDAP não encontrado (" + email
							+ ")");
		}

		boolean novo = false;

		if (admin) {
			pessoa = PessoaService.encontrePorId(1);
		} else {
			pessoa = PessoaService.encontrePorEmail(email, campus);

			if (pessoa == null) {
				novo = true;
				pessoa = new Pessoa();
			} else {
				novo = false;
				if (!pessoa.getAtivo()) {
					throw new UsuarioDesativadoException(email);
				}
			}
		}

		hash = StringUtils.generateMD5Hash(password);

		boolean ldapAuth = true;

		if (!admin) {
			// Servidor cadastrado, verificando senha
			String uid = email.substring(0, email.indexOf("@"));

			LdapUtils ldapUtils = new LdapUtils(ldap.getHost(), ldap.getPort(),
					ldap.getSsl(), true, ldap.getBasedn(), ldap.getVarLdapUid());

			// Caso ocorra falha de senha, uma exceção será
			// disparada
			try {
				ldapUtils.authenticate(uid, password);
			} catch (NamingException e) {
				/**
				 * Se não conseguiu se conectar ao servidor LDAP compara a senha
				 * com a última gravada na tabela PESSOA
				 */
				if (!hash.equals(pessoa.getSenhaMd5())) {
					throw e;
				}

				ldapAuth = false;
			}

			// Se chegou aqui é porque a validação ocorreu com sucesso sem
			// exception. Então pesquisa se já existe essa pessoa cadastrada
			// Recupera dados do usuário no LDAP
			Map<String, String> dataLdap = ldapUtils.getLdapProperties(uid);

			// Atualiza dados da Pessoa/Usuário
			String cnpjCpf = dataLdap.get(ldap.getVarLdapCnpjCpf());
			String matricula = dataLdap.get(ldap.getVarLdapMatricula());
			String nomeCompleto = dataLdap.get(ldap.getVarLdapNomeCompleto());

			pessoa.setAtivo(true);
			pessoa.setCnpjCpf(cnpjCpf);
			pessoa.setEmail(email);
			pessoa.setIdCampus(ldap.getIdCampus());
			pessoa.setMatricula(matricula);
			pessoa.setNomeCompleto(nomeCompleto);
			pessoa.setPessoaFisica(true);
			pessoa.setSenhaMd5(hash);

			// Confere os grupos da Pessoa
			List<GrupoPessoa> grupos = new ArrayList<GrupoPessoa>();
			if (ldapAuth) {
				String baseDn = ldapUtils.getDnByUid(uid);
				List<String> nomeGrupos = ldapUtils.getLdapOuByUid(uid, baseDn);

				for (String s : nomeGrupos) {
					GrupoPessoa gp = GrupoPessoaService.encontrePorDescricao(
							campus, s);

					if (gp == null) {
						gp = new GrupoPessoa();
						gp.setIdCampus(ldap.getIdCampus());
						gp.setNome(s);

						GrupoPessoaService.criar(gp);
					}

					grupos.add(gp);
				}

				if (novo) {
					// Não existe pessoa cadastrada, CADASTRANDO
					PessoaService.criar(pessoa);
				} else {
					// Pessoa já existe, será necessário atualizá-la
					PessoaService.alterar(pessoa);
				}
			} else {
				String s = Config.NOME_GRUPO_EXTERNO;
				GrupoPessoa gp = GrupoPessoaService.encontrePorDescricao(
						campus, s);
				if (gp == null) {
					gp = new GrupoPessoa();
					gp.setIdCampus(ldap.getIdCampus());
					gp.setNome(s);

					GrupoPessoaService.criar(gp);
				}
				grupos.add(gp);
			}

			// Atualiza os grupos da pessoa
			Transaction trans = null;
			try {
				trans = new Transaction();
				trans.begin();
				PessoaDAO pessoaDAO = new PessoaDAO(trans);

				/**
				 * Busca novamente a pessoa do banco de dados para conferir os
				 * grupos
				 */
				pessoa = pessoaDAO.encontrePorId(pessoa.getIdPessoa());
				GrupoPessoaService.atualizaGrupos(trans, pessoa, grupos);
				trans.commit();
			} catch (Exception e) {
				throw e;
			} finally {
				if (trans != null) {
					trans.close();
				}
			}

			// // Atualiza servidor LDAP da Pessoa
			// if (pessoa.getPessoaLdapServer() == null) {
			// PessoaLdapServer pls = new PessoaLdapServer();
			// PessoaLdapServerPK pk = new PessoaLdapServerPK();
			// pk.setIdPessoa(pessoa.getPessoaPK().getIdPessoa());
			// pk.setIdCampus(pessoa.getCampus().getIdCampus());
			// pls.setPessoa(pessoa);
			// pls.setLdapServer(ldap);
			// pessoa.setPessoaLdapServer(pls);
			// PessoaService.alterar(pessoa);
			// }
		} else {
			if (!hash.equals(pessoa.getSenhaMd5())) {
				throw new NamingException("Usuário admin senha inválida.");
			}

		}

		// Busca novamente do banco para preencher atributos faltantes
		pessoa = PessoaService.encontrePorId(pessoa.getIdPessoa());

		return pessoa;

	}

	/**
	 * Busca um objeto LdapServer por e-mail
	 * 
	 * @param email
	 * @return
	 */
	public static LdapServer getLdapByEmail(String email) throws Exception {
		Transaction trans = new Transaction();
		LdapServer ldap = null;
		try {
			trans.begin();

			LdapServerDAO ldapDAO;

			ldapDAO = new LdapServerDAO(trans);
			ldap = ldapDAO.encontrePorEmail(email);
		} catch (Exception e) {
			throw e;
		} finally {
			trans.close();
		}

		return ldap;
	}

}
