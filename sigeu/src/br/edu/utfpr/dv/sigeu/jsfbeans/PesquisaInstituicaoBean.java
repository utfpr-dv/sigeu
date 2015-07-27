package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import br.edu.utfpr.dv.sigeu.entities.Instituicao;
import br.edu.utfpr.dv.sigeu.service.InstituicaoService;

@Named
@ViewScoped
public class PesquisaInstituicaoBean extends JavaBean {
	private static final long serialVersionUID = -7332998115885395433L;

	//
	private String textoPesquisa;
	private List<Instituicao> lista;

	//

	@Inject
	private LoginBean loginBean;

	@PostConstruct
	public void init() {
		try {
			lista = InstituicaoService.pesquisar(null);
			// this.addInfoMessage("Pesquisa", "Exibindo  " +
			// HibernateDAO.PESQUISA_LIMITE +
			// " itens. Pesquise utilizando parâmetros para obter mais registros.");
		} catch (Exception e) {
			// this.addErrorMessage("Pesquisa",
			// "Erro ao realizar pesquisa inicial. Entre em contato com o Admin.");
		}
	}

	/**
	 * Realiza a pesquisa de itens
	 */
	public void pesquisa() {
		try {
			this.lista = InstituicaoService.pesquisar(textoPesquisa);
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("Pesquisa", "Erro na pesquisa");
		}
	}

	public List<Instituicao> getLista() {
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
