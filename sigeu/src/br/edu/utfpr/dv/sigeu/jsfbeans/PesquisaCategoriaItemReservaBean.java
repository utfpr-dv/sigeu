package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.faces.bean.ManagedBean;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.entities.CategoriaItemReserva;
import br.edu.utfpr.dv.sigeu.service.CategoriaItemReservaService;

@ManagedBean
@ViewScoped
public class PesquisaCategoriaItemReservaBean extends JavaBean {
	@Inject
	private LoginBean loginBean;

	private static final long serialVersionUID = -7332998125885395663L;

	//
	private String textoPesquisa;
	private List<CategoriaItemReserva> listaCategoria;

	//

	public PesquisaCategoriaItemReservaBean() {
		try {
			listaCategoria = CategoriaItemReservaService.pesquisar(
					loginBean.getCampus(), null, null);
			// this.addInfoMessage("Pesquisar","Exibindo  " +
			// HibernateDAO.PESQUISA_LIMITE +
			// " itens. Pesquise utilizando parâmetros para obter mais registros.");
		} catch (Exception e) {
			// this.addErrorMessage("Pesquisar","Erro ao realizar pesquisa inicial. Entre em contato com o Admin.");
		}
	}

	/**
	 * Realiza a pesquisa de itens
	 */
	public void pesquisa() {
		try {
			this.listaCategoria = CategoriaItemReservaService.pesquisar(
					loginBean.getCampus(), textoPesquisa, null);

			for (CategoriaItemReserva c : listaCategoria) {
				Hibernate.initialize(c.getIdCampus().getIdInstituicao());
			}
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("Pesquisar", "Erro na pesquisa");
		}
	}

	public List<CategoriaItemReserva> getListaCategoria() {
		return listaCategoria;
	}

	public String getTextoPesquisa() {
		return textoPesquisa;
	}

	public void setTextoPesquisa(String textoPesquisa) {
		this.textoPesquisa = textoPesquisa;
	}
}
