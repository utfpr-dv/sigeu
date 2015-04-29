package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.edu.utfpr.dv.sigeu.entities.TipoReserva;
import br.edu.utfpr.dv.sigeu.service.TipoReservaService;

@ManagedBean(name = "pesquisaTipoReservaBean")
@ViewScoped
public class PesquisaTipoReservaBean extends JavaBean {
	private static final long serialVersionUID = -7332998125885395663L;

	//
	private String textoPesquisa;
	private List<TipoReserva> listaTipoReserva;

	//

	public PesquisaTipoReservaBean() {
		try {
			listaTipoReserva = TipoReservaService.pesquisar(null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Realiza a pesquisa de itens
	 */
	public void pesquisa() {
		try {
			this.listaTipoReserva = TipoReservaService.pesquisar(textoPesquisa, null);
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
