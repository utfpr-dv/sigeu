package br.edu.utfpr.dv.sigeu.jsfbeans;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.exception.GenericJDBCException;

import br.edu.utfpr.dv.sigeu.config.Config;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.exception.ServidorLdapNaoCadastradoException;
import br.edu.utfpr.dv.sigeu.exception.UsuarioDesativadoException;
import br.edu.utfpr.dv.sigeu.service.LoginService;
import br.edu.utfpr.dv.sigeu.util.LoginFilter;

import com.adamiworks.utils.StringUtils;
import com.adamiworks.utils.hibernate.DatabaseConfig;
import com.adamiworks.utils.hibernate.DatabaseParameter;

@Named
@SessionScoped
/*
 * ATENÇÃO: Quando se usa named Bean precisa ser
 * javax.enterprise.context.SessionScoped
 */
public class LoginBean extends JavaBean {
	private static final long serialVersionUID = 6545494024577623349L;

	private String email;
	private String password;
	private String nomeUsuario;

	private String serverInfo;
	
	private String appInfo;

	private Pessoa pessoaLogin;

	// Usado quando for escolher outro campus diferente do campus de cadastro da
	// pessoa
	private Campus campus;

	public String login() {
		boolean ok = true;

		this.serverInfo = "Server: " + DatabaseConfig.getInstance().getProperty(DatabaseParameter.DATABASE_URL);
		
		this.appInfo = Config.APPLICATION_NAME + " - " + Config.APPLICATION_CODE + " v"+Config.APPLICATION_VERSION;

		try {
			pessoaLogin = LoginService.autentica(email, password);

			if (pessoaLogin == null) {
				this.addErrorMessage("Login", "E-mail não cadastrado ou senha inválida!");
			} else {
				if (!pessoaLogin.getAtivo()) {
					this.addErrorMessage("Login", "Acesso inativado. Informe ao administrador do sistema.");
				} else {
					this.setNomeUsuario(pessoaLogin.getNomeCompleto());

					campus = pessoaLogin.getIdCampus();

					HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
							.getExternalContext().getRequest();

					request.getSession().setAttribute(LoginFilter.SESSION_EMAIL_LOGIN, email);

					request.getSession().setAttribute(LoginFilter.SESSION_PESSOA_LOGIN, pessoaLogin);

					request.getSession().setAttribute(LoginFilter.SESSION_CAMPUS, campus);

				}
			}
		} catch (CommunicationException e) {
			ok = false;
			handleException("Erro de comunicação com o servidor LDAP. Informe ao administrador do sistema.", e);
		} catch (NamingException e) {
			ok = false;
			handleException("Usuário ou senha inválidos.", null);
		} catch (ServidorLdapNaoCadastradoException e) {
			ok = false;
			handleException("Servidor de autenticação não cadastrado para o endereço de e-mail especificado.", e);
		} catch (UsuarioDesativadoException e) {
			ok = false;
			handleException("Usuário não autorizado. Entre em contato com o Administrador do sistema.", e);
		} catch (GenericJDBCException e) {
			ok = false;
			handleException(e);
		} catch (Exception e) {
			ok = false;
			handleException("Ocorreu um erro ao tentar comunicar com o servidor de autenticação", e);
		}
		if (ok) {
			if (Config.getInstance().isDebugMode()) {
				this.addErrorMessage("DEBUG", "EXECUTANDO EM MODO DEBUG!");
			}
			return "/restrito/Home.xhtml";
		}
		// }

		return null;
	}

	public String logoff() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		request.getSession().removeAttribute(LoginFilter.SESSION_EMAIL_LOGIN);

		return "/Logoff";
	}

	
	
	@Override
	public String toString() {
		return "LoginBean [email=" + email + ", password=" + StringUtils.generateMD5Hash(password) + "]";
	}

	public Pessoa getPessoaLogin() {
		return pessoaLogin;
	}

	public void setPessoaLogin(Pessoa pessoaLogin) {
		this.pessoaLogin = pessoaLogin;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getServerInfo() {
		return serverInfo;
	}

	public void setServerInfo(String serverInfo) {
		this.serverInfo = serverInfo;
	}

	public Campus getCampus() {
		return campus;
	}

	public void setCampus(Campus campus) {
		this.campus = campus;
	}

	public String getAppInfo() {
		return appInfo;
	}

	public void setAppInfo(String appInfo) {
		this.appInfo = appInfo;
	}
}
