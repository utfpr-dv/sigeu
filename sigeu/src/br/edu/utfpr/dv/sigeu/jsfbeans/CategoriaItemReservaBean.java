package br.edu.utfpr.dv.sigeu.jsfbeans;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.omnifaces.cdi.ViewScoped;

import br.edu.utfpr.dv.sigeu.entities.CategoriaItemReserva;
import br.edu.utfpr.dv.sigeu.exception.EntidadePossuiRelacionamentoException;
import br.edu.utfpr.dv.sigeu.service.CategoriaItemReservaService;

@Named
@ViewScoped
public class CategoriaItemReservaBean extends JavaBean {
	@Inject
	private LoginBean loginBean;

	private static final long serialVersionUID = -7332998125885395663L;

	private Integer editarId = null;
	//
	private CategoriaItemReserva categoria = new CategoriaItemReserva();

	@PostConstruct
	public void init() {
		categoria = new CategoriaItemReserva();
		categoria.setAtivo(true);

		HttpServletRequest req = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();

		try {
			this.editarId = Integer.valueOf(req.getParameter("editarId"));
		} catch (Exception e) {
			//
		}

		if (this.editarId != null) {
			try {
				this.categoria = CategoriaItemReservaService
						.encontrePorId(this.editarId);

				if (this.categoria == null) {
					this.categoria = new CategoriaItemReserva();
					this.addInfoMessage("Carregar", "Categoria "
							+ this.editarId + " inexistente.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				addErrorMessage("Carregar", "Erro ao carregar dados");
			}
		}
	}

	/**
	 * Cria uma nova categoria se o ID for nulo ou 0 ou altera uma categoria já
	 * gravada no banco de dados se ela já existir
	 */
	public void gravar() {
		categoria.setIdCampus(loginBean.getCampus());

		try {
			CategoriaItemReservaService.persistir(this.categoria);
			String msg = "Categoria " + categoria.getIdCategoria() + "-"
					+ categoria.getNome() + " gravada com sucesso!";
			this.categoria = new CategoriaItemReserva();
			this.categoria.setAtivo(true);

			addInfoMessage("Gravar", msg);
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("Gravar", "Erro na gravação!");
		}
	}

	/**
	 * Exclui uma categoria do banco de dados
	 * 
	 * @param cat
	 */
	public void excluir() {
		if (this.categoria.getIdCategoria() == null) {
			addInfoMessage("Excluir",
					"Categoria ainda não foi incluída no banco de dados.");
		} else {
			try {
				String old = this.categoria.getNome();
				CategoriaItemReservaService.remover(categoria);
				this.categoria = new CategoriaItemReserva();
				this.addInfoMessage("Excluir", "Categoria " + old
						+ " excluída com sucesso!");
			} catch (EntidadePossuiRelacionamentoException e) {
				this.addWarnMessage("Excluir",
						"Categoria já possui relacionamentos. Solicite exclusão ao admin.");
			} catch (Exception e) {
				this.addErrorMessage("Excluir",
						"Erro ao tentar excluir categoria.");
			}
		}
	}

	// ///////////////////////////////

	public CategoriaItemReserva getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaItemReserva categoria) {
		this.categoria = categoria;
	}

	public Integer getEditarId() {
		return editarId;
	}

	public void setEditarId(Integer editarId) {
		this.editarId = editarId;
	}

}
