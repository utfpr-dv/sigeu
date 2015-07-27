package br.edu.utfpr.dv.sigeu.jsfbeans;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.omnifaces.cdi.ViewScoped;

import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.service.PessoaService;

@Named
@ViewScoped
public class PessoaBean extends JavaBean {
	//@Inject
	//private LoginBean loginBean;

	private static final long serialVersionUID = 7309144685247380621L;
	private int editarId = 0;
	//
	private Pessoa pessoa = new Pessoa();

	@PostConstruct
	public void init() {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

		try {
			this.editarId = Integer.valueOf(req.getParameter("editarId"));
		} catch (Exception e) {
			//
		}

		if (this.editarId > 0) {
			try {
				this.pessoa = PessoaService.encontrePorId(this.editarId);

				if (this.pessoa == null) {
					this.pessoa = new Pessoa();
					// this.pessoa.setCampus(new Campus());
					// this.pessoa.getIdCampus().setIdInstituicao(new
					// Instituicao());
					this.addInfoMessage("Carregar", "Pessoa " + this.editarId + " inexistente.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				addErrorMessage("Carregar", "Erro ao carregar dados");
			}
		}
	}

	/**
	 * Cria uma nova pessoa se o ID for nulo ou 0 ou altera uma pessoa já
	 * gravada no banco de dados se ela já existir
	 */
	public void gravar() {
		if (editarId == 0) {
			this.addErrorMessage("Gravar", "Inclusão de pessoas é feito através de Login.");
		} else {
			try {
				PessoaService.alterar(pessoa);
				String label = pessoa.getIdPessoa() + "-" + pessoa.getNomeCompleto();
				addInfoMessage("Gravar", "Pessoa [" + label + "] alterada com sucesso!");

			} catch (Exception e) {
				e.printStackTrace();
				addErrorMessage("Gravar", "Erro na gravação!");
			} finally {

			}
		}
	}

	public Integer getEditarId() {
		return editarId;
	}

	public void setEditarId(Integer editarId) {
		this.editarId = editarId;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
}
