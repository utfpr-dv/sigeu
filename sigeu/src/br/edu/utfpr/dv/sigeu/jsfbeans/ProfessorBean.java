package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.omnifaces.cdi.ViewScoped;

import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.entities.Professor;
import br.edu.utfpr.dv.sigeu.service.PessoaService;
import br.edu.utfpr.dv.sigeu.service.ProfessorService;

@Named
@ViewScoped
public class ProfessorBean extends JavaBean {
	@Inject
	private LoginBean loginBean;

	private static final long serialVersionUID = -4044653509641476L;
	private Integer editarId = null;
	private String pesquisaPessoa;
	private Pessoa pessoa;
	private Professor professor = new Professor();
	private List<Pessoa> listaPessoa;

	@PostConstruct
	public void init() {
		pesquisaPessoa = "";
		professor = new Professor();

		HttpServletRequest req = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();

		try {
			editarId = Integer.valueOf(req.getParameter("editarId"));
		} catch (Exception e) {
			//
		}

		if (editarId != null) {
			try {
				professor = ProfessorService.encontrePorId(editarId);

				if (professor == null) {
					professor = new Professor();
					addWarnMessage("Carregar", "Professor " + editarId
							+ " inexistente.");
				} else {
					if (professor.getProfessorPessoa() != null) {
						pessoa = professor.getProfessorPessoa().getIdPessoa();
						if (pessoa != null) {
							pesquisaPessoa = pessoa.getNomeCompleto();
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				addErrorMessage("Carregar", "Erro ao carregar dados");
			}
		}
	}

	/**
	 * Cria uma nova professor se o ID for nulo ou 0 ou altera uma professor já
	 * gravada no banco de dados se ela já existir
	 */
	public String gravar() {
		// addInfoMessage("", "PESSOA: " + pessoa.getNomeCompleto());
		if (pessoa == null) {
			addErrorMessage("Pessoa",
					"Nenhuma pessoa selecionada para relacionamento");
		} else {
			try {
				ProfessorService.atualizaProfessorPessoa(pessoa, professor);
				addInfoMessage("Professor Pessoa",
						"Cadastro atualizado com sucesso!");
			} catch (Exception e) {
				handleException(e);
				return null;
			}
		}

		return "PesquisaProfessor";
	}

	/**
	 * Retorna lista para autocompletar pesquisa de instituição
	 * 
	 * @param query
	 * @return
	 */
	public List<String> selecionaPessoa(String query) {
		pessoa = null;
		List<String> list = new ArrayList<String>();
		listaPessoa = null;

		try {
			// listaPessoa = PessoaService
			// .pesquisar(query, true, "PROFESSORES", 0);

			listaPessoa = PessoaService.pesquisar(loginBean.getCampus(), query, true, 0);

			if (listaPessoa != null && listaPessoa.size() > 0) {
				for (Pessoa i : listaPessoa) {
					list.add(i.getNomeCompleto() + " | " + i.getEmail());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("Selecionar", "Erro na pesquisa de Pessoas");
			return list;
		}

		return list;
	}

	public void definePessoa() {
		pessoa = null;

		for (Pessoa i : listaPessoa) {
			if (pesquisaPessoa.equals(i.getNomeCompleto() + " | " + i.getEmail())) {
				pessoa = i;
				addInfoMessage("Selecionar",
						"Pessoa selecionada: " + i.getNomeCompleto());
				break;
			}
		}

		pesquisaPessoa = pessoa.getNomeCompleto();
		listaPessoa = null;
	}

	public Integer getEditarId() {
		return editarId;
	}

	public void setEditarId(Integer editarId) {
		this.editarId = editarId;
	}

	public String getPesquisaPessoa() {
		return pesquisaPessoa;
	}

	public void setPesquisaPessoa(String pesquisaPessoa) {
		this.pesquisaPessoa = pesquisaPessoa;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public List<Pessoa> getListaPessoa() {
		return listaPessoa;
	}

	public void setListaPessoa(List<Pessoa> listaPessoa) {
		this.listaPessoa = listaPessoa;
	}

}
