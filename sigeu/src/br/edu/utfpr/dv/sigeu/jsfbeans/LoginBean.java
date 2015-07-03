package br.edu.utfpr.dv.sigeu.jsfbeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.exception.GenericJDBCException;

import br.edu.utfpr.dv.sigeu.config.Config;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.exception.ServidorLdapNaoCadastradoException;
import br.edu.utfpr.dv.sigeu.exception.UsuarioDesativadoException;
import br.edu.utfpr.dv.sigeu.service.LoginService;
import br.edu.utfpr.dv.sigeu.util.LoginFilter;

import com.adamiworks.utils.StringUtils;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean extends JavaBean {
	private static final long serialVersionUID = 6545494024577623349L;

	private String email;
	private String password;
	private String nomeUsuario;

	private Pessoa pessoaLogin;

	public String login() {
		boolean ok = true;

		// int posAt = this.email.indexOf("@");

		// if (posAt < 0 || posAt == 0 || posAt == email.length() - 1) {
		// this.addErrorMessage("E-Mail", "[" + email
		// + "] não é um endereço de e-mail válido.");
		// } else {

		try {
			pessoaLogin = LoginService.autentica(email, password);

			if (pessoaLogin == null) {
				this.addErrorMessage("Login",
						"E-mail não cadastrado ou senha inválida!");
			} else {
				if (!pessoaLogin.getAtivo()) {
					this.addErrorMessage("Login",
							"Acesso inativado. Informe ao administrador do sistema.");
				} else {
					this.setNomeUsuario(pessoaLogin.getNomeCompleto());

					HttpServletRequest request = (HttpServletRequest) FacesContext
							.getCurrentInstance().getExternalContext()
							.getRequest();
					request.getSession().setAttribute(
							LoginFilter.SESSION_USUARIO_AUTENTICADO, email);

					// FacesContext.getCurrentInstance().getExternalContext().redirect("/restrito/Home.xhtml");
				}
			}
		} catch (CommunicationException e) {
			ok = false;
			handleException(
					"Erro de comunicação com o servidor LDAP. Informe ao administrador do sistema.",
					e);
		} catch (NamingException e) {
			ok = false;
			handleException("Usuário ou senha inválidos.", null);
		} catch (ServidorLdapNaoCadastradoException e) {
			ok = false;
			handleException(
					"Servidor de autenticação não cadastrado para o endereço de e-mail especificado.",
					e);
		} catch (UsuarioDesativadoException e) {
			ok = false;
			handleException(
					"Usuário não autorizado. Entre em contato com o Administrador do sistema.",
					e);
		} catch (GenericJDBCException e) {
			ok = false;
			handleException(e);
		} catch (Exception e) {
			ok = false;
			handleException(
					"Ocorreu um erro ao tentar comunicar com o servidor de autenticação",
					e);
		}
		if (ok) {
			if (Config.getInstance().isDebugMode()) {
				this.addWarnMessage("DEBUG", "EXECUTANDO EM MODO DEBUG!");
			}
			return "/restrito/Home.xhtml";
		}
		// }

		return null;
	}

	public String logoff() {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();

		request.getSession().removeAttribute(
				LoginFilter.SESSION_USUARIO_AUTENTICADO);

		return "/Logoff";
	}

	@Override
	public String toString() {
		return "LoginBean [email=" + email + ", password="
				+ StringUtils.generateMD5Hash(password) + "]";
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

}
