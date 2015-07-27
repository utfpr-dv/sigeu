package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.omnifaces.cdi.ViewScoped;

import br.edu.utfpr.dv.sigeu.entities.Feriado;
import br.edu.utfpr.dv.sigeu.exception.EntidadePossuiRelacionamentoException;
import br.edu.utfpr.dv.sigeu.service.FeriadoService;

@Named
@ViewScoped
public class FeriadoBean extends JavaBean {
	@Inject
	private LoginBean loginBean;
	
	private static final long serialVersionUID = -7332998125885395663L;

	private Integer editarId = null;
	private String pesquisaCategoria;
	//
	private Feriado feriado = new Feriado();
	private Date dataFinal = null;

	@PostConstruct
	public void init() {
		feriado = new Feriado();

		HttpServletRequest req = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();

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
	public String gravar() {
		feriado.setIdCampus(loginBean.getCampus());

		try {
			if (dataFinal != null) {
				Calendar data = Calendar.getInstance();
				data.setTime(feriado.getData());

				while (true) {
					Feriado f = new Feriado();
					f.setData(data.getTime());
					f.setDescricao(feriado.getDescricao());
					f.setIdCampus(feriado.getIdCampus());
					f.setIdFeriado(null);
					f.setTipo(feriado.getTipo());

					// System.out.println("Gravando feriado: " +
					// data.toString());

					FeriadoService.persistir(f);

					data.add(Calendar.DAY_OF_MONTH, 1);

					if (data.getTime().compareTo(dataFinal) > 0) {
						break;
					}
				}

				dataFinal = null;
			} else {
				FeriadoService.persistir(feriado);
			}
			String msg = "Feriado " + feriado.getDescricao()
					+ " gravado com sucesso!";
			feriado = new Feriado();

			addInfoMessage("Gravar", msg);
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("Gravar", "Erro na gravação!");
		}
		
		return "PesquisaFeriado";
	}

	/**
	 * Exclui uma do banco de dados
	 * 
	 * @param cat
	 */
	public String excluir() {
		if (feriado.getIdFeriado() == null) {
			addInfoMessage("Excluir",
					" ainda não foi incluída no banco de dados.");
		} else {
			try {
				String old = feriado.getDescricao();
				FeriadoService.remover(feriado);
				feriado = new Feriado();
				this.addInfoMessage("Excluir", "Item " + old
						+ " excluído com sucesso!");
			} catch (EntidadePossuiRelacionamentoException e) {
				this.addWarnMessage("Excluir",
						"Feriado já possui relacionamentos. Solicite exclusão ao admin.");
			} catch (Exception e) {
				this.addErrorMessage("Excluir", "Erro ao tentar excluir .");
			}
		}
		
		return "PesquisaFeriado";
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

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
}
