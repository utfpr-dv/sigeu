package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import br.edu.utfpr.dv.sigeu.entities.TipoReserva;
import br.edu.utfpr.dv.sigeu.service.TipoReservaService;

@Named
@ViewScoped
public class PesquisaTipoReservaBean extends JavaBean {
	@Inject
	private LoginBean loginBean;
	
	private static final long serialVersionUID = -7332998125885395663L;

	//
	private String textoPesquisa;
	private List<TipoReserva> listaTipoReserva;

	//

	@PostConstruct
	public void init() {
		try {
			listaTipoReserva = TipoReservaService.pesquisar(loginBean.getCampus(), null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Realiza a pesquisa de itens
	 */
	public void pesquisa() {
		try {
			this.listaTipoReserva = TipoReservaService.pesquisar(loginBean.getCampus(), textoPesquisa, null);
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("Pesquisar","Erro na pesquisa");
		}
	}

	public List<TipoReserva> getListaTipoReserva() {
		return listaTipoReserva;
	}

	public String getTextoPesquisa() {
		return textoPesquisa;
	}

	public void setTextoPesquisa(String textoPesquisa) {
		this.textoPesquisa = textoPesquisa;
	}
}
