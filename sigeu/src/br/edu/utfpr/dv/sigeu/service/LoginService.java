package br.edu.utfpr.dv.sigeu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import com.adamiworks.utils.StringUtils;
import com.adamiworks.utils.ldap.LdapUtils;

import br.edu.utfpr.dv.sigeu.config.Config;
import br.edu.utfpr.dv.sigeu.dao.PessoaDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.GrupoPessoa;
import br.edu.utfpr.dv.sigeu.entities.LdapServer;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.exception.CampusNaoLocalizadoException;
import br.edu.utfpr.dv.sigeu.exception.ServidorLdapNaoCadastradoException;
import br.edu.utfpr.dv.sigeu.exception.UsuarioDesativadoException;

@Stateless
public class LoginService {

    @EJB
    private PessoaService pessoaService;

    @EJB
    private PessoaDAO pessoaDAO;

    @EJB
    private GrupoPessoaService grupoPessoaService;

    @EJB
    private CampusService campusService;

    /**
     * Autentica usuário através de e-mail e senha.
     * 
     * @param email
     * @param password
     * @return Pessoa
     * @throws Exception
     */
    public Pessoa autentica(String email, String password) throws Exception {

	boolean admin = email.trim().toUpperCase().equals("ADMIN");
	Pessoa pessoa = null;
	String hash = StringUtils.generateMD5Hash(password);

	// Fixa Campus 100
	Campus campus = campusService.encontrePorId(100);
	// Campus campus = CampusService.encontrePorEmail(email);

	if (admin) {
	    /*
	     * USUÁRIO ADMIN
	     */

	    pessoa = pessoaService.encontrePorId(1);

	    if (pessoa == null) {
		throw new UsuarioDesativadoException("admin");
	    } else {
		if (!hash.equals(pessoa.getSenhaMd5())) {
		    throw new NamingException("Usuário admin senha inválida.");
		}
	    }
	} else {
	    /*
	     * USUÁRIO AUTENTICADO PELO LDAP
	     */

	    if (campus.getLdapServerList() == null || campus.getLdapServerList().size() == 0) {
		throw new CampusNaoLocalizadoException();
	    }

	    // Se não é um e-mail, apenas nome, então...
	    if (!email.contains("@")) {
		email += campus.getLdapServerList().get(0).getSufixoEmail();
	    }

	    // Confere se autenticou por LDAP. Em caso positivo, já cadastra a
	    // pessoa no banco de dados
	    // LdapServer ldap = LoginService.getLdapByEmail(email);
	    LdapServer ldap = campus.getLdapServerList().get(0);

	    if (ldap == null) {
		throw new ServidorLdapNaoCadastradoException("Login/E-mail inválido ou Servidor LDAP não encontrado");
	    }

	    boolean novo = false;

	    pessoa = pessoaService.encontrePorEmail(email, campus);

	    if (pessoa == null) {
		novo = true;
		pessoa = new Pessoa();
		// Importantíssimo!
		pessoa.setExterno(false);
	    } else {
		novo = false;
		if (!pessoa.getAtivo()) {
		    throw new UsuarioDesativadoException(email);
		}
	    }

	    String uid = email.substring(0, email.indexOf("@"));

	    LdapUtils ldapUtils = new LdapUtils(ldap.getHost(), ldap.getPort(), ldap.getSsl(), true, ldap.getBasedn(), ldap.getVarLdapUid());

	    boolean ldapAuth = true;

	    // Caso ocorra falha de senha, uma exceção será
	    // disparada
	    if (pessoa.getExterno()) {
		// Autenticação para externos é feita com a senha do banco de
		// dados
		if (!hash.equals(pessoa.getSenhaMd5())) {
		    /*
		     * Senha não confere. Emite erro.
		     */
		    throw new NamingException("Usuario ou senha invalidos! [" + email + "]");
		}

		ldapAuth = false;
	    } else {
		// Usuários não externos autentica no LDAP
		ldapUtils.authenticate(uid, password);
		hash = "";
	    }

	    // Se chegou aqui é porque a validação ocorreu com sucesso sem
	    // exception. Então pesquisa se já existe essa pessoa cadaservice strada
	    // Recupera dados do usuário no LDAP
	    Map<String, String> dataLdap = ldapUtils.getLdapProperties(uid);

	    // Atualiza dados da Pessoa/Usuário
	    String cnpjCpf = dataLdap.get(ldap.getVarLdapCnpjCpf());
	    String matricula = dataLdap.get(ldap.getVarLdapMatricula());
	    String nomeCompleto = dataLdap.get(ldap.getVarLdapNomeCompleto());

	    pessoa.setAtivo(true);
	    pessoa.setCnpjCpf(cnpjCpf);
	    pessoa.setEmail(email);
	    // pessoa.setIdCampus(ldap.getIdCampus());
	    pessoa.setMatricula(matricula);
	    pessoa.setNomeCompleto(nomeCompleto);
	    pessoa.setPessoaFisica(true);
	    pessoa.setSenhaMd5(hash);
	    pessoa.setIdCampus(campus);

	    // Confere os grupos da Pessoa
	    List<GrupoPessoa> grupos = new ArrayList<GrupoPessoa>();
	    if (ldapAuth) {
		/*
		 * Usuários autenticados pelo LDAP
		 */
		String baseDn = ldapUtils.getDnByUid(uid);
		List<String> nomeGrupos = ldapUtils.getLdapOuByUid(uid, baseDn);

		for (String s : nomeGrupos) {
		    GrupoPessoa gp = grupoPessoaService.encontrePorDescricao(campus, s);

		    if (gp == null) {
			gp = new GrupoPessoa();
			gp.setIdCampus(ldap.getIdCampus());
			gp.setNome(s);

			grupoPessoaService.criar(gp);
		    }

		    grupos.add(gp);
		}

		if (novo) {
		    // Não existe pessoa cadastrada, CADASTRANDO
		    pessoaService.criar(pessoa);
		} else {
		    // Pessoa já existe, será necessário atualizá-la
		    pessoaService.alterar(pessoa);
		}

		pessoa = atualizaGruposPessoa(pessoa, grupos);
	    } else if (pessoa.getExterno()) {
		/*
		 * Usuários externos
		 */

		String s = Config.NOME_GRUPO_EXTERNO;
		GrupoPessoa gp = grupoPessoaService.encontrePorDescricao(campus, s);
		if (gp == null) {
		    gp = new GrupoPessoa();
		    gp.setIdCampus(ldap.getIdCampus());
		    gp.setNome(s);

		    grupoPessoaService.criar(gp);
		}
		grupos.add(gp);

		pessoa = atualizaGruposPessoa(pessoa, grupos);
	    }
	}

//		// Busca novamente do banco para preencher atributos faltantes
//		if (!admin) {
//			pessoa = PessoaService.encontrePorId(pessoa.getIdPessoa());
//		}

	return pessoa;

    }

    // Atualiza os grupos da pessoa
    private Pessoa atualizaGruposPessoa(Pessoa pessoa, List<GrupoPessoa> grupos) throws Exception {

	/**
	 * Busca novamente a pessoa do banco de dados para conferir os grupos
	 */
	pessoa = pessoaDAO.encontrePorId(pessoa.getIdPessoa());
	grupoPessoaService.atualizaGrupos(pessoa, grupos);

	return pessoa;
    }

    // /**
    // * Busca um objeto LdapServer por e-mail
    // *
    // * @param email
    // * @return
    // */
    // public static LdapServer getLdapByEmail(String email) throws Exception {
    // Transaction trans = new Transaction();
    // LdapServer ldap = null;
    // try {
    // trans.begin();
    //
    // LdapServerDAO ldapDAO;
    //
    // ldapDAO = new LdapServerDAO(trans);
    // ldap = ldapDAO.encontrePorEmail(email);
    // } catch (Exception e) {
    // throw e;
    // } finally {
    // trans.close();
    // }
    //
    // return ldap;
    // }

}
