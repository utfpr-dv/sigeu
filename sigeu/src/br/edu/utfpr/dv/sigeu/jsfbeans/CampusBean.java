package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.omnifaces.cdi.ViewScoped;

import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.Instituicao;
import br.edu.utfpr.dv.sigeu.exception.EntidadePossuiRelacionamentoException;
import br.edu.utfpr.dv.sigeu.service.CampusService;
import br.edu.utfpr.dv.sigeu.service.InstituicaoService;

@Named
@ViewScoped
public class CampusBean extends JavaBean {

	private static final long serialVersionUID = -4044653509348641476L;
	private Integer editarId = null;
	private String pesquisaInstituicao;
	private List<Instituicao> listaInstituicao;

	//
	private Campus campus = new Campus();

	@PostConstruct
	public void init() {
		campus = new Campus();
		campus.setAtivo(true);
		campus.setIdInstituicao(new Instituicao());

		HttpServletRequest req = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();

		try {
			this.editarId = Integer.valueOf(req.getParameter("editarId"));
		} catch (Exception e) {
			//
		}

		if (this.editarId != null) {
			try {
				this.campus = CampusService.encontrePorId(this.editarId);

				if (this.campus == null) {
					this.campus = new Campus();
					this.addInfoMessage("Carregar", "Campus " + this.editarId
							+ " inexistente.");
				} else {
					this.pesquisaInstituicao = campus.getIdInstituicao()
							.getSigla();
				}
			} catch (Exception e) {
				e.printStackTrace();
				addErrorMessage("Carregar", "Erro ao carregar dados");
			}
		}
	}

	/**
	 * Cria uma nova campus se o ID for nulo ou 0 ou altera uma campus já
	 * gravada no banco de dados se ela já existir
	 */
	public void gravar() {
		boolean novo = (campus.getIdCampus() == null || this.campus
				.getIdCampus() == 0);

		if (this.getCampus().getIdInstituicao() == null
				|| this.getCampus().getIdInstituicao().getIdInstituicao() == null) {
			addWarnMessage("Gravar", "Instituição não selecionada.");
		} else {

			try {
				if (!novo) {
					CampusService.alterar(campus);
				} else {
					CampusService.criar(campus);
				}

				String label = campus.getIdCampus() + "-" + campus.getSigla();

				this.campus = new Campus();
				this.campus.setAtivo(true);
				this.campus.setIdInstituicao(new Instituicao());

				addInfoMessage("Gravar", "Campus " + label + " "
						+ (novo ? "criado" : "alterado") + " com sucesso!");

			} catch (Exception e) {
				e.printStackTrace();
				addErrorMessage("Gravar", "Erro na gravação!");
			} finally {
				if (novo) {
					this.campus.setIdCampus(null);
				}
			}
		}
	}

	/**
	 * Exclui uma campus do banco de dados
	 * 
	 * @param ent
	 */
	public void excluir() {
		if (this.campus.getIdCampus() == null) {
			addInfoMessage("Excluir",
					"Campus ainda não foi incluído no banco de dados.");
		} else {
			try {
				String old = this.campus.getSigla();
				CampusService.remover(campus);
				this.campus = new Campus();
				this.addInfoMessage("Excluir", "Campus " + old
						+ " excluído com sucesso!");
			} catch (EntidadePossuiRelacionamentoException e) {
				this.addWarnMessage("Excluir",
						"Campus já possui relacionamentos. Solicite exclusão ao admin.");
			} catch (Exception e) {
				this.addErrorMessage("Excluir",
						"Erro ao tentar excluir campus.");
			}
		}
	}

	/**
	 * Retorna lista para autocompletar pesquisa de instituição
	 * 
	 * @param query
	 * @return
	 */
	public List<String> selecionaInstituicao(String query) {
		List<String> list = new ArrayList<String>();
		listaInstituicao = null;

		try {
			listaInstituicao = InstituicaoService.pesquisar(query);

			for (Instituicao i : listaInstituicao) {
				list.add(i.getSigla() + " - " + i.getNome());
			}

		} catch (Exception e) {
			e.printStackTrace();
			this.addErrorMessage("Selecionar",
					"Erro na pesquisa de siglas de instituições");
			return list;
		}

		return list;
	}

	public void defineInstituicao() {
		for (Instituicao i : listaInstituicao) {
			if (pesquisaInstituicao.equals(i.getSigla() + " - " + i.getNome())) {
				campus.setIdInstituicao(i);
				addInfoMessage(
						"Selecionar",
						"Instituição selecionada: " + i.getSigla() + " - "
								+ i.getNome());
				break;
			}
		}

		pesquisaInstituicao = campus.getIdInstituicao().getSigla();
		listaInstituicao = null;
	}

	// ///////////////////////////////

	public Campus getCampus() {
		return campus;
	}

	public void setCampus(Campus campus) {
		this.campus = campus;
	}

	public Integer getEditarId() {
		return editarId;
	}

	public void setEditarId(Integer editarId) {
		this.editarId = editarId;
	}

	public String getPesquisaInstituicao() {
		return pesquisaInstituicao;
	}

	public void setPesquisaInstituicao(String pesquisaInstituicao) {
		this.pesquisaInstituicao = pesquisaInstituicao;
	}

	public List<Instituicao> getListaInstituicao() {
		return listaInstituicao;
	}

}
