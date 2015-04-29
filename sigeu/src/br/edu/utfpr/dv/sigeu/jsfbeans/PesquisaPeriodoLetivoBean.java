package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.edu.utfpr.dv.sigeu.entities.PeriodoLetivo;
import br.edu.utfpr.dv.sigeu.service.PeriodoLetivoService;

@ManagedBean(name = "pesquisaPeriodoLetivoBean")
@ViewScoped
public class PesquisaPeriodoLetivoBean extends JavaBean {
	private static final long serialVersionUID = -7338998125000395663L;

	//
	private String textoPesquisa;
	private List<PeriodoLetivo> lista;

	//

	public PesquisaPeriodoLetivoBean() {
		try {
			String nullS = null;
			lista = PeriodoLetivoService.pesquisar(nullS);
		} catch (Exception e) {
		}
	}

	/**
	 * Realiza a pesquisa de itens
	 */
	public void pesquisa() {
		try {
			this.lista = PeriodoLetivoService.pesquisar(textoPesquisa);
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("Pesquisa", "Erro na pesquisa");
		}
	}

	public List<PeriodoLetivo> getLista() {
		return lista;
	}

	public String getTextoPesquisa() {
		return textoPesquisa;
	}

	public void setTextoPesquisa(String textoPesquisa) {
		this.textoPesquisa = textoPesquisa;
	}

}
