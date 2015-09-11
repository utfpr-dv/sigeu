package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.omnifaces.cdi.ViewScoped;

import br.edu.utfpr.dv.sigeu.entities.CategoriaItemReserva;
import br.edu.utfpr.dv.sigeu.entities.ItemReserva;
import br.edu.utfpr.dv.sigeu.exception.EntidadePossuiRelacionamentoException;
import br.edu.utfpr.dv.sigeu.service.CategoriaItemReservaService;
import br.edu.utfpr.dv.sigeu.service.ItemReservaService;

@Named
@ViewScoped
public class ItemReservaBean extends JavaBean {
	@Inject
	private LoginBean loginBean;

	private static final long serialVersionUID = -7332998125885395663L;

	private Integer editarId = null;
	private String pesquisaCategoria;
	private List<CategoriaItemReserva> listaCategoria = null;
	//
	private ItemReserva itemReserva = new ItemReserva();

	@PostConstruct
	public void init() {
		itemReserva = new ItemReserva();
		itemReserva.setNumeroHorasAntecedencia(0);
		itemReserva.setAtivo(true);

		HttpServletRequest req = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();

		try {
			this.editarId = Integer.valueOf(req.getParameter("editarId"));
		} catch (Exception e) {
			//
		}

		if (this.editarId != null) {
			try {
				itemReserva = ItemReservaService.encontrePorId(this.editarId);

				if (itemReserva == null) {
					itemReserva = new ItemReserva();
					this.addInfoMessage("Carregar", " " + this.editarId
							+ " inexistente.");
				} else {
					this.pesquisaCategoria = itemReserva.getIdCategoria()
							.getNome();
				}
			} catch (Exception e) {
				e.printStackTrace();
				addErrorMessage("Carregar", "Erro ao carregar dados");
			}
		}
	}

	/**
	 * Cria uma nova se o ID for nulo ou 0 ou altera uma já gravada no banco de
	 * dados se ela já existir
	 */
	public void gravar() {
		itemReserva.setIdCampus(loginBean.getCampus());

		try {
			ItemReservaService.persistir(itemReserva);
			String msg = "Item " + itemReserva.getIdItemReserva() + "-"
					+ itemReserva.getNome() + " gravado com sucesso!";
			itemReserva = new ItemReserva();
			itemReserva.setAtivo(true);

			this.pesquisaCategoria = "";
			this.listaCategoria = null;

			addInfoMessage("Gravar", msg);

			pesquisaCategoria = "";

		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("Gravar", "Erro na gravação!");
		}
	}

	/**
	 * Exclui uma do banco de dados
	 * 
	 * @param cat
	 */
	public void excluir() {
		if (itemReserva.getIdItemReserva() == null) {
			addInfoMessage("Excluir",
					" ainda não foi incluída no banco de dados.");
		} else {
			try {
				String old = itemReserva.getNome();
				ItemReservaService.remover(itemReserva);
				itemReserva = new ItemReserva();
				this.addInfoMessage("Excluir", "Item " + old
						+ " excluío com sucesso!");
			} catch (EntidadePossuiRelacionamentoException e) {
				this.addWarnMessage("Excluir",
						"Item já possui relacionamentos. Solicite exclusão ao admin.");
			} catch (Exception e) {
				this.addErrorMessage("Excluir", "Erro ao tentar excluir .");
			}
		}
	}

	/**
	 * Retorna lista para autocompletar pesquisa de instituição
	 * 
	 * @param query
	 * @return
	 */
	public List<String> selecionaCategoria(String query) {
		List<String> list = new ArrayList<String>();
		listaCategoria = null;

		try {
			listaCategoria = CategoriaItemReservaService.pesquisar(
					loginBean.getCampus(), query, true);

			for (CategoriaItemReserva i : listaCategoria) {
				list.add(i.getNome());
			}

		} catch (Exception e) {
			e.printStackTrace();
			this.addErrorMessage("Selecionar",
					"Erro na pesquisa de siglas de categorias");
			return list;
		}

		return list;
	}

	/**
	 * Atribui uma categoria ao item de reserva
	 */
	public void defineCategoria() {
		for (CategoriaItemReserva i : listaCategoria) {
			if (pesquisaCategoria.equals(i.getNome())) {
				itemReserva.setIdCategoria(i);
				addInfoMessage("Selecionar",
						"Instituição selecionada: " + i.getNome());
				this.pesquisaCategoria = i.getNome();
				break;
			}
		}
	}

	// ///////////////////////////////

	public Integer getEditarId() {
		return editarId;
	}

	public void setEditarId(Integer editarId) {
		this.editarId = editarId;
	}

	public ItemReserva getItemReserva() {
		return itemReserva;
	}

	public void setItemReserva(ItemReserva itemReserva) {
		this.itemReserva = itemReserva;
	}

	public String getPesquisaCategoria() {
		return pesquisaCategoria;
	}

	public void setPesquisaCategoria(String pesquisaCategoria) {
		this.pesquisaCategoria = pesquisaCategoria;
	}

}
