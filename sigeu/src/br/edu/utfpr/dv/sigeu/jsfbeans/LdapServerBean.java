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
import br.edu.utfpr.dv.sigeu.entities.LdapServer;
import br.edu.utfpr.dv.sigeu.exception.EntidadePossuiRelacionamentoException;
import br.edu.utfpr.dv.sigeu.service.CampusService;
import br.edu.utfpr.dv.sigeu.service.InstituicaoService;
import br.edu.utfpr.dv.sigeu.service.LdapServerService;

@Named
@ViewScoped
public class LdapServerBean extends JavaBean {

	private static final long serialVersionUID = 7309144685247380621L;
	private Integer editarId = null;
	//
	private String pesquisaInstituicao;
	private Instituicao instituicao;
	private List<Instituicao> listaInstituicao;
	//
	private Campus campus;
	private String pesquisaCampus;
	private List<Campus> listaCampus;

	//
	private LdapServer ldapServer = new LdapServer();

	@PostConstruct
	public void init() {
		ldapServer = new LdapServer();
		ldapServer.setAtivo(true);
		// ldapServer.setIdCampus(new Campus());
		// ldapServer.getIdCampus().setIdInstituicao(new Instituicao());

		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

		try {
			this.editarId = Integer.valueOf(req.getParameter("editarId"));
		} catch (Exception e) {
			//
		}

		if (this.editarId != null) {
			try {
				this.ldapServer = LdapServerService.encontrePorId(this.editarId);

				if (this.ldapServer == null) {
					this.ldapServer = new LdapServer();
					this.addInfoMessage("Carregar", "LdapServer " + this.editarId + " inexistente.");
				} else {
					this.instituicao = ldapServer.getIdCampus().getIdInstituicao();
					this.campus = ldapServer.getIdCampus();
					this.pesquisaInstituicao = instituicao.getSigla();
					this.pesquisaCampus = campus.getSigla();
				}
			} catch (Exception e) {
				e.printStackTrace();
				addErrorMessage("Carregar", "Erro ao carregar dados");
			}
		}
	}

	/**
	 * Cria uma nova ldapServer se o ID for nulo ou 0 ou altera uma ldapServer
	 * já gravada no banco de dados se ela já existir
	 */
	public void gravar() {
		boolean novo = (ldapServer.getIdServer() == null);

		if (campus == null || instituicao == null || campus.getIdCampus() == null || instituicao.getIdInstituicao() == null) {
			addWarnMessage("Gravar", "Instituição/Campus não selecionados.");
		} else {
			// ldapServer.setCampus(campus);
			ldapServer.setIdCampus(campus);

			try {
				if (!novo) {
					LdapServerService.alterar(ldapServer);
				} else {
					LdapServerService.criar(ldapServer);
				}

				String label = ldapServer.getIdServer() + "-" + ldapServer.getSufixoEmail();

				this.ldapServer = new LdapServer();
				this.ldapServer.setAtivo(true);
				this.campus = new Campus();
				this.instituicao = new Instituicao();
				this.pesquisaCampus = "";
				this.pesquisaInstituicao = "";

				addInfoMessage("Gravar", "LdapServer " + label + " " + (novo ? "criado" : "alterado") + " com sucesso!");

			} catch (Exception e) {
				e.printStackTrace();
				addErrorMessage("Gravar", "Erro na gravação!");
			} finally {
				if (novo) {
					this.ldapServer.setIdServer(null);
				}
			}
		}
	}

	/**
	 * Exclui uma ldapServer do banco de dados
	 * 
	 * @param ent
	 */
	public void excluir() {
		if (this.ldapServer.getIdServer() == null) {
			addInfoMessage("Excluir", "LdapServer ainda não foi incluído no banco de dados.");
		} else {
			try {
				String old = this.ldapServer.getHost();
				LdapServerService.remover(ldapServer);
				this.ldapServer = new LdapServer();
				this.addInfoMessage("Excluir", "LdapServer " + old + " excluído com sucesso!");
			} catch (EntidadePossuiRelacionamentoException e) {
				this.addWarnMessage("Excluir", "LdapServer já possui relacionamentos. Solicite exclusão ao admin.");
			} catch (Exception e) {
				this.addErrorMessage("Excluir", "Erro ao tentar excluir ldapServer.");
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
			this.addErrorMessage("Selecionar", "Erro na pesquisa de siglas de instituições");
			return list;
		}

		return list;
	}

	/**
	 * Retorna lista para autocompletar pesquisa de Campus
	 * 
	 * @param query
	 * @return
	 */
	public List<String> selecionaCampus(String query) {
		List<String> list = new ArrayList<String>();

		if (instituicao == null) {
			this.addWarnMessage("Selecionar", "Antes de selecionar um Campus é preciso selecionar uma Instituição.");
		} else {
			listaCampus = null;

			try {
				listaCampus = CampusService.pesquisar(query, instituicao);

				for (Campus i : listaCampus) {
					list.add(i.getSigla() + " - " + i.getNome());
				}

			} catch (Exception e) {
				e.printStackTrace();
				this.addErrorMessage("Selecionar", "Erro na pesquisa de siglas de Campus");
				return list;
			}
		}
		return list;
	}

	/**
	 * Seta o atributo Instituição do Bean
	 */
	public void defineInstituicao() {
		instituicao = null;

		for (Instituicao i : listaInstituicao) {
			if (pesquisaInstituicao.equals(i.getSigla() + " - " + i.getNome())) {
				instituicao = i;
				addInfoMessage("Selecionar", "Instituição selecionada: " + i.getSigla() + " - " + i.getNome());
				break;
			}
		}
		if (instituicao == null) {
			addWarnMessage("Selecionar", "Nenhuma instituição encontrada!");
			this.pesquisaInstituicao = "";
			this.pesquisaCampus = "";
			this.instituicao = new Instituicao();
			this.campus = new Campus();
		} else {
			this.pesquisaInstituicao = instituicao.getSigla();
			this.pesquisaCampus = "";
			this.campus = new Campus();
		}
	}

	/**
	 * Seta o atributo Campus do Bean
	 */
	public void defineCampus() {
		campus = null;

		for (Campus i : listaCampus) {
			if (pesquisaCampus.equals(i.getSigla() + " - " + i.getNome())) {
				campus = i;
				addInfoMessage("Selecionar", "Campus selecionado: " + i.getSigla() + " - " + i.getNome());
				break;
			}
		}

		if (campus == null) {
			addWarnMessage("Selecionar", "Nenhum campus encontrado!");
			campus = new Campus();
			this.pesquisaCampus = "";
		} else {
			this.pesquisaCampus = campus.getSigla();
		}
	}

	public Integer getEditarId() {
		return editarId;
	}

	public void setEditarId(Integer editarId) {
		this.editarId = editarId;
	}

	public Instituicao getInstituicao() {
		return instituicao;
	}

	public void setInstituicao(Instituicao instituicao) {
		this.instituicao = instituicao;
	}

	public List<Instituicao> getListaInstituicao() {
		return listaInstituicao;
	}

	public void setListaInstituicao(List<Instituicao> listaInstituicao) {
		this.listaInstituicao = listaInstituicao;
	}

	public Campus getCampus() {
		return campus;
	}

	public void setCampus(Campus campus) {
		this.campus = campus;
	}

	public List<Campus> getListaCampus() {
		return listaCampus;
	}

	public void setListaCampus(List<Campus> listaCampus) {
		this.listaCampus = listaCampus;
	}

	public LdapServer getLdapServer() {
		return ldapServer;
	}

	public void setLdapServer(LdapServer ldapServer) {
		this.ldapServer = ldapServer;
	}

	public String getPesquisaInstituicao() {
		return pesquisaInstituicao;
	}

	public void setPesquisaInstituicao(String pesquisaInstituicao) {
		this.pesquisaInstituicao = pesquisaInstituicao;
	}

	public String getPesquisaCampus() {
		return pesquisaCampus;
	}

	public void setPesquisaCampus(String pesquisaCampus) {
		this.pesquisaCampus = pesquisaCampus;
	}

}
