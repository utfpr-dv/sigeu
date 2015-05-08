package br.edu.utfpr.dv.sigeu.jsfbeans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.exception.GenericJDBCException;

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

		int posAt = this.email.indexOf("@");

		if (posAt < 0 || posAt == 0 || posAt == email.length() - 1) {
			this.addErrorMessage("E-Mail", "[" + email + "] não é um endereço de e-mail válido.");
		} else {

			try {
				this.setPessoaLogin(LoginService.autentica(email, password));

				if (pessoaLogin == null) {
					this.addErrorMessage("Login", "Acesso não autorizado!");
				} else {

					this.setNomeUsuario(pessoaLogin.getNomeCompleto());

					HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
					request.getSession().setAttribute(LoginFilter.SESSION_USUARIO_AUTENTICADO, email);

					// FacesContext.getCurrentInstance().getExternalContext().redirect("/restrito/Home.xhtml");
				}
			} catch (CommunicationException e) {
				ok = false;
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro de comunicação com o servidor LDAP. Informe ao administrador do sistema.", null));
				e.printStackTrace();
			} catch (NamingException e) {
				ok = false;
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário ou senha inválidos. Tente novamente.", null));
				e.printStackTrace();
			} catch (ServidorLdapNaoCadastradoException e) {
				ok = false;
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Servidor de autenticação não cadastrado para o endereço de e-mail especificado.", null));
				e.printStackTrace();
			} catch (UsuarioDesativadoException e) {
				ok = false;
				this.addWarnMessage("Login", "Usuário não autorizado. Entre em contato com sua chefia imediata solicitando acesso.");
				e.printStackTrace();
			} catch (GenericJDBCException e) {
				ok = false;
				this.addErrorMessage("Login", e.getMessage());
			} catch (Exception e) {
				ok = false;
				String msg = e.getMessage();
				if (msg == null || msg.trim().length() == 0) {
					msg = "Ocorreu um erro ao tentar comunicar com o servidor de autenticação";
				}
				this.addErrorMessage("Login", msg);
				e.printStackTrace();
			}
			if (ok) {
				this.addWarnMessage("DEBUG", "EXECUTANDO EM MODO DEBUG!");
				return "/restrito/Home.xhtml";
			}
		}

		return null;
	}

	public String logoff() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

		request.getSession().removeAttribute(LoginFilter.SESSION_USUARIO_AUTENTICADO);

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

}
