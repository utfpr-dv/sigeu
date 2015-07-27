package br.edu.utfpr.dv.sigeu.jsfbeans;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.omnifaces.cdi.ViewScoped;

import br.edu.utfpr.dv.sigeu.entities.PeriodoLetivo;
import br.edu.utfpr.dv.sigeu.exception.EntidadePossuiRelacionamentoException;
import br.edu.utfpr.dv.sigeu.service.PeriodoLetivoService;

@Named
@ViewScoped
public class PeriodoLetivoBean extends JavaBean {

	@Inject
	private LoginBean loginBean;
	
	private static final long serialVersionUID = -7332998125045395663L;

	private Integer editarId = null;
	//
	private PeriodoLetivo periodoLetivo = new PeriodoLetivo();

	@PostConstruct
	public void init() {
		periodoLetivo = new PeriodoLetivo();

		HttpServletRequest req = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();

		try {
			this.editarId = Integer.valueOf(req.getParameter("editarId"));
		} catch (Exception e) {
			//
		}

		if (this.editarId != null) {
			try {
				periodoLetivo = PeriodoLetivoService
						.encontrePorId(this.editarId);

				if (periodoLetivo == null) {
					periodoLetivo = new PeriodoLetivo();
					this.addInfoMessage("Carregar", " " + this.editarId
							+ " inexistente.");
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
		periodoLetivo.setIdCampus(loginBean.getCampus());

		try {
			PeriodoLetivoService.persistir(periodoLetivo);
			String msg = "PeriodoLetivo " + periodoLetivo.getIdPeriodoLetivo()
					+ "-" + periodoLetivo.getNome() + " gravado com sucesso!";
			periodoLetivo = new PeriodoLetivo();

			addInfoMessage("Gravar", msg);
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
		if (periodoLetivo.getIdPeriodoLetivo() == null) {
			addInfoMessage("Excluir",
					"Período Letivo ainda não foi incluído no banco de dados.");
		} else {
			try {
				String old = periodoLetivo.getNome();
				PeriodoLetivoService.remover(periodoLetivo);
				periodoLetivo = new PeriodoLetivo();
				this.addInfoMessage("Excluir", "Período Letivo " + old
						+ " excluído com sucesso!");
			} catch (EntidadePossuiRelacionamentoException e) {
				this.addWarnMessage("Excluir",
						"Período Letivo já possui relacionamentos. Solicite exclusão ao admin.");
			} catch (Exception e) {
				this.addErrorMessage("Excluir", "Erro ao tentar excluir .");
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

	public PeriodoLetivo getPeriodoLetivo() {
		return periodoLetivo;
	}

	public void setPeriodoLetivo(PeriodoLetivo periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}
}
