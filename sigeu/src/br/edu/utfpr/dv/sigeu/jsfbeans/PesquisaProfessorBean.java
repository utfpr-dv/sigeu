package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import br.edu.utfpr.dv.sigeu.entities.Professor;
import br.edu.utfpr.dv.sigeu.service.ProfessorService;

@Named
@ViewScoped
public class PesquisaProfessorBean extends JavaBean {
	@Inject
	private LoginBean loginBean;
	
	private static final long serialVersionUID = -7335395433L;

	//
	private String textoPesquisa;
	private List<Professor> lista;

	//

	@PostConstruct
	public void init() {
		try {
			lista = ProfessorService.pesquisar(loginBean.getCampus(), null);
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
			this.lista = ProfessorService.pesquisar(loginBean.getCampus(), textoPesquisa);
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("Pesquisar", "Erro na pesquisa");
		}
	}

	public List<Professor> getLista() {
		return lista;
	}

	public String getTextoPesquisa() {
		return textoPesquisa;
	}

	public void setTextoPesquisa(String textoPesquisa) {
		this.textoPesquisa = textoPesquisa;
	}
}
