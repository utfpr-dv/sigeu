package br.edu.utfpr.dv.sigeu.jsfbeans;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.omnifaces.cdi.ViewScoped;

import br.edu.utfpr.dv.sigeu.entities.Instituicao;
import br.edu.utfpr.dv.sigeu.exception.EntidadePossuiRelacionamentoException;
import br.edu.utfpr.dv.sigeu.service.InstituicaoService;

@Named
@ViewScoped
public class InstituicaoBean extends JavaBean {

	private static final long serialVersionUID = 60168807985304986L;
	private Integer editarId = null;
	//
	private Instituicao instituicao = new Instituicao();

	@Inject
	private LoginBean loginBean;

	@PostConstruct
	public void init() {
		instituicao = new Instituicao();
		instituicao.setAtivo(true);

		HttpServletRequest req = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();

		try {
			this.editarId = Integer.valueOf(req.getParameter("editarId"));
		} catch (Exception e) {
			//
		}

		if (this.editarId != null) {
			try {
				this.instituicao = InstituicaoService
						.encontrePorId(this.editarId);

				if (this.instituicao == null) {
					this.instituicao = new Instituicao();
					this.addInfoMessage("Carregar", "Instituicao "
							+ this.editarId + " inexistente.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				addErrorMessage("Carregar", "Erro ao carregar dados");
			}
		}
	}

	/**
	 * Cria uma nova instituicao se o ID for nulo ou 0 ou altera uma instituicao
	 * já gravada no banco de dados se ela já existir
	 */
	public void gravar() {
		boolean novo = (instituicao.getIdInstituicao() == null || this.instituicao
				.getIdInstituicao() == 0);

		try {
			if (!novo) {
				InstituicaoService.alterar(instituicao);
			} else {
				InstituicaoService.criar(instituicao);
			}

			String label = instituicao.getIdInstituicao() + "-"
					+ instituicao.getSigla();

			this.instituicao = new Instituicao();
			instituicao.setAtivo(true);

			addInfoMessage("Gravar", "Instituição " + label
					+ " gravada com sucesso!");

		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("Gravar", "Erro na gravação!");
		} finally {
			if (novo) {
				this.instituicao.setIdInstituicao(null);
			}
		}
	}

	/**
	 * Exclui uma instituicao do banco de dados
	 * 
	 * @param ent
	 */
	public void excluir() {
		if (this.instituicao.getIdInstituicao() == null) {
			addInfoMessage("Excluir",
					"Instituição ainda não foi incluída no banco de dados.");
		} else {
			try {
				String old = this.instituicao.getSigla();
				InstituicaoService.remover(instituicao);
				this.instituicao = new Instituicao();
				this.addInfoMessage("Excluir", "Instituicao " + old
						+ " excluída com sucesso!");
			} catch (EntidadePossuiRelacionamentoException e) {
				this.addWarnMessage("Excluir",
						"Instituicao já possui relacionamentos. Solicite exclusão ao admin.");
			} catch (Exception e) {
				this.addErrorMessage("Excluir",
						"Erro ao tentar excluir instituicao.");
			}
		}
	}

	// ///////////////////////////////

	public Instituicao getInstituicao() {
		return instituicao;
	}

	public void setInstituicao(Instituicao instituicao) {
		this.instituicao = instituicao;
	}

	public Integer getEditarId() {
		return editarId;
	}

	public void setEditarId(Integer editarId) {
		this.editarId = editarId;
	}

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

}
