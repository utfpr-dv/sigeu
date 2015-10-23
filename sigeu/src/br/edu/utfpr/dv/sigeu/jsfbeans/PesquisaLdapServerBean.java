package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import br.edu.utfpr.dv.sigeu.entities.LdapServer;
import br.edu.utfpr.dv.sigeu.service.LdapServerService;

@Named
@ViewScoped
public class PesquisaLdapServerBean extends JavaBean {

	@Inject
	private LoginBean loginBean;
	
	private static final long serialVersionUID = 1917504765711660254L;
	//
	private String textoPesquisa;
	private List<LdapServer> lista;

	//

	@PostConstruct
	public void init() {
		try {
			lista = LdapServerService.pesquisar(loginBean.getCampus(),null);
			//this.addInfoMessage("Pesquisar", "Exibindo  " + HibernateDAO.PESQUISA_LIMITE + " itens. Pesquise utilizando parâmetros para obter mais registros.");
		} catch (Exception e) {
			//this.addErrorMessage("Pesquisar", "Erro ao realizar pesquisa inicial. Entre em contato com o Admin.");
		}
	}

	/**
	 * Realiza a pesquisa de itens
	 */
	public void pesquisa() {
		try {
			this.lista = LdapServerService.pesquisar(loginBean.getCampus(),textoPesquisa);
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("Pesquisar", "Erro na pesquisa");
		}
	}

	public List<LdapServer> getLista() {
		return lista;
	}

	public String getTextoPesquisa() {
		return textoPesquisa;
	}

	public void setTextoPesquisa(String textoPesquisa) {
		this.textoPesquisa = textoPesquisa;
	}

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}
}
