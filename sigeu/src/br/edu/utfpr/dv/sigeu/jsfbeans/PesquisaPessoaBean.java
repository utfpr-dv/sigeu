package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.edu.utfpr.dv.sigeu.config.Config;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.service.PessoaService;

@ManagedBean(name = "pesquisaPessoaBean")
@ViewScoped
public class PesquisaPessoaBean extends JavaBean {

	private static final long serialVersionUID = -6452041264020252302L;
	//
	private String textoPesquisa;
	private List<Pessoa> lista;

	//

	public PesquisaPessoaBean() {
		try {
			lista = PessoaService.pesquisar(null);
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
			this.lista = PessoaService.pesquisar(textoPesquisa);
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("Pesquisar", "Erro na pesquisa");
		}
	}

	public void atualizarCadastrosDoLdap() throws Exception {
		try {
			PessoaService.atualizaPessoasLdap(Config.getInstance().getPessoaLogin().getEmail());
		} catch (Exception e) {
			this.addErrorMessage("Atualização cadastral", "Houve um erro durante a atualização de cadastros. Informe ao Admin.");
			e.printStackTrace();
			throw e;
		}
		this.addInfoMessage("Atualização cadastral", "Atualização cadastral concluída!");
	}

	public List<Pessoa> getLista() {
		return lista;
	}

	public String getTextoPesquisa() {
		return textoPesquisa;
	}

	public void setTextoPesquisa(String textoPesquisa) {
		this.textoPesquisa = textoPesquisa;
	}
}
