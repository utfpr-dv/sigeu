package br.edu.utfpr.dv.sigeu.jsfbeans;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.omnifaces.cdi.ViewScoped;

import br.edu.utfpr.dv.sigeu.entities.TipoReserva;
import br.edu.utfpr.dv.sigeu.exception.EntidadePossuiRelacionamentoException;
import br.edu.utfpr.dv.sigeu.service.TipoReservaService;

@Named
@ViewScoped
public class TipoReservaBean extends JavaBean {
	@Inject
	private LoginBean loginBean;
	
	private static final long serialVersionUID = -7332998125885395663L;

	private Integer editarId = null;
	//
	private TipoReserva tipoReserva = new TipoReserva();

	@PostConstruct
	public void init() {
		tipoReserva = new TipoReserva();
		tipoReserva.setAtivo(true);

		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

		try {
			this.editarId = Integer.valueOf(req.getParameter("editarId"));
		} catch (Exception e) {
			//
		}

		if (this.editarId != null) {
			try {
				this.tipoReserva = TipoReservaService.encontrePorId(this.editarId);

				if (this.tipoReserva == null) {
					this.tipoReserva = new TipoReserva();
					this.addInfoMessage("Carregar", "TipoReserva " + this.editarId + " inexistente.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				addErrorMessage("Carregar", "Erro ao carregar dados");
			}
		}
	}

	/**
	 * Cria uma nova tipoReserva se o ID for nulo ou 0 ou altera uma tipoReserva
	 * já gravada no banco de dados se ela já existir
	 */
	public void gravar() {
		tipoReserva.setIdCampus(loginBean.getCampus());

		try {
			TipoReservaService.persistir(this.tipoReserva);
			String msg = "TipoReserva " + tipoReserva.getIdTipoReserva() + "-" + tipoReserva.getDescricao() + " gravada com sucesso!";
			this.tipoReserva = new TipoReserva();
			this.tipoReserva.setAtivo(true);

			addInfoMessage("Gravar", msg);
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("Gravar", "Erro na gravação!");
		}
	}

	/**
	 * Exclui uma tipoReserva do banco de dados
	 * 
	 * @param cat
	 */
	public void excluir() {
		if (this.tipoReserva.getIdTipoReserva() == null) {
			addInfoMessage("Excluir", "TipoReserva ainda não foi incluída no banco de dados.");
		} else {
			try {
				String old = this.tipoReserva.getDescricao();
				TipoReservaService.remover(tipoReserva);
				this.tipoReserva = new TipoReserva();
				this.addInfoMessage("Excluir", "TipoReserva " + old + " excluída com sucesso!");
			} catch (EntidadePossuiRelacionamentoException e) {
				this.addWarnMessage("Excluir", "TipoReserva já possui relacionamentos. Solicite exclusão ao admin.");
			} catch (Exception e) {
				this.addErrorMessage("Excluir", "Erro ao tentar excluir tipoReserva.");
			}
		}
	}

	// ///////////////////////////////

	public TipoReserva getTipoReserva() {
		return tipoReserva;
	}

	public void setTipoReserva(TipoReserva tipoReserva) {
		this.tipoReserva = tipoReserva;
	}

	public Integer getEditarId() {
		return editarId;
	}

	public void setEditarId(Integer editarId) {
		this.editarId = editarId;
	}

}
