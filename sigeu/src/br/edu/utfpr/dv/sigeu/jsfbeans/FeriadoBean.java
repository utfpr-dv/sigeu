package br.edu.utfpr.dv.sigeu.jsfbeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import br.edu.utfpr.dv.sigeu.config.Config;
import br.edu.utfpr.dv.sigeu.entities.Feriado;
import br.edu.utfpr.dv.sigeu.exception.EntidadePossuiRelacionamentoException;
import br.edu.utfpr.dv.sigeu.service.FeriadoService;

@ManagedBean(name = "feriadoBean")
@ViewScoped
public class FeriadoBean extends JavaBean {
	private static final long serialVersionUID = -7332998125885395663L;

	private Integer editarId = null;
	private String pesquisaCategoria;
	//
	private Feriado feriado = new Feriado();

	public FeriadoBean() {
		feriado = new Feriado();

		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

		try {
			this.editarId = Integer.valueOf(req.getParameter("editarId"));
		} catch (Exception e) {
			//
		}

		if (this.editarId != null) {
			try {
				feriado = FeriadoService.encontrePorId(this.editarId);

				if (feriado == null) {
					feriado = new Feriado();
					this.addInfoMessage("Carregar"," " + this.editarId + " inexistente.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				addErrorMessage("Carregar","Erro ao carregar dados");
			}
		}
	}

	/**
	 * Cria uma nova se o ID for nulo ou 0 ou altera uma já gravada no banco de
	 * dados se ela já existir
	 */
	public void gravar() {
		feriado.setIdCampus(Config.getInstance().getCampus());

		try {
			FeriadoService.persistir(feriado);
			String msg = "Feriado " + feriado.getIdFeriado() + "-" + feriado.getDescricao() + " gravado com sucesso!";
			feriado = new Feriado();

			addInfoMessage("Gravar",msg);
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("Gravar","Erro na gravação!");
		}
	}

	/**
	 * Exclui uma do banco de dados
	 * 
	 * @param cat
	 */
	public void excluir() {
		if (feriado.getIdFeriado() == null) {
			addInfoMessage("Excluir"," ainda não foi incluída no banco de dados.");
		} else {
			try {
				String old = feriado.getDescricao();
				FeriadoService.remover(feriado);
				feriado = new Feriado();
				this.addInfoMessage("Excluir","Item " + old + " excluído com sucesso!");
			} catch (EntidadePossuiRelacionamentoException e) {
				this.addWarnMessage("Excluir","Feriado já possui relacionamentos. Solicite exclusão ao admin.");
			} catch (Exception e) {
				this.addErrorMessage("Excluir","Erro ao tentar excluir .");
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

	public Feriado getFeriado() {
		return feriado;
	}

	public void setFeriado(Feriado feriado) {
		this.feriado = feriado;
	}

	public String getPesquisaCategoria() {
		return pesquisaCategoria;
	}

	public void setPesquisaCategoria(String pesquisaCategoria) {
		this.pesquisaCategoria = pesquisaCategoria;
	}

}
