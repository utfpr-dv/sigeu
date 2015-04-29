package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.edu.utfpr.dv.sigeu.entities.ItemReserva;
import br.edu.utfpr.dv.sigeu.service.ItemReservaService;

@ManagedBean(name = "pesquisaItemReservaBean")
@ViewScoped
public class PesquisaItemReservaBean extends JavaBean {
	private static final long serialVersionUID = -7332998125885395663L;

	//
	private String textoPesquisa;
	private List<ItemReserva> lista;

	//

	public PesquisaItemReservaBean() {
		try {
			lista = ItemReservaService.pesquisar(null, null);
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
			this.lista = ItemReservaService.pesquisar(textoPesquisa, null);

			// RequestContext.getCurrentInstance().update("frmPesquisa");
			// RequestContext.getCurrentInstance().openDialog("PesquisaItemReservaModal");
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("Pesquisar", "Erro na pesquisa");
		}
	}

	public List<ItemReserva> getLista() {
		return lista;
	}

	public String getTextoPesquisa() {
		return textoPesquisa;
	}

	public void setTextoPesquisa(String textoPesquisa) {
		this.textoPesquisa = textoPesquisa;
	}
}
