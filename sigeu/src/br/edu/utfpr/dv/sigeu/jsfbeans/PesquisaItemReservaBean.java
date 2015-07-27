package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import br.edu.utfpr.dv.sigeu.entities.ItemReserva;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.service.ItemReservaService;
import br.edu.utfpr.dv.sigeu.service.PessoaService;

@Named
@ViewScoped
public class PesquisaItemReservaBean extends JavaBean {
	
	@Inject
	private LoginBean loginBean;
	
	private static final long serialVersionUID = -7332998125885395663L;

	//
	private String textoPesquisa;
	private List<ItemReserva> lista;

	private int status = 1;
	private ItemReserva itemReserva;

	private List<Pessoa> listaPessoa;
	private String campoPessoa;
	private Pessoa pessoa;

	//

	@PostConstruct
	public void init() {
		try {
			lista = ItemReservaService.pesquisar(loginBean.getCampus(), null, null);
			// this.addInfoMessage("Pesquisar", "Exibindo  " +
			// HibernateDAO.PESQUISA_LIMITE +
			// " itens. Pesquise utilizando parâmetros para obter mais registros.");
		} catch (Exception e) {
			// this.addErrorMessage("Pesquisar",
			// "Erro ao realizar pesquisa inicial. Entre em contato com o Admin.");
		}
	}

	/**
	 * Realiza a pesquisa de itens
	 */
	public void pesquisa() {
		try {
			this.lista = ItemReservaService.pesquisar(loginBean.getCampus(), textoPesquisa, null);

			// RequestContext.getCurrentInstance().update("frmPesquisa");
			// RequestContext.getCurrentInstance().openDialog("PesquisaItemReservaModal");
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("Pesquisar", "Erro na pesquisa");
		}
	}

	public void editaAutorizadores(ItemReserva i) {
		// System.out.println("---> " + i.getIdItemReserva());
		this.status = 2;
		try {
			this.itemReserva = ItemReservaService.encontrePorId(i
					.getIdItemReserva());
		} catch (Exception e) {
			addErrorMessage("Erro", e.getMessage());
		}
	}

	public void excluiAutorizador(Pessoa p) {
		List<Pessoa> pessoaList = itemReserva.getPessoaList();

		List<Pessoa> novaList = new ArrayList<Pessoa>();

		for (Pessoa pl : pessoaList) {
			if (pl.getIdPessoa() != p.getIdPessoa()) {
				novaList.add(pl);
			}
		}

		itemReserva.setPessoaList(novaList);

		try {
			ItemReservaService.alterar(itemReserva);
			this.itemReserva = ItemReservaService.encontrePorId(itemReserva
					.getIdItemReserva());
		} catch (Exception e) {
			addErrorMessage("Erro", e.getMessage());
		}
	}

	public List<String> selecionaPessoa(String query) {
		List<String> list = new ArrayList<String>();
		listaPessoa = null;
		pessoa = null;

		if (query != null && query.trim().length() > 0) {
			try {
				listaPessoa = PessoaService.pesquisar(loginBean.getCampus(), query, true, 14);

				if (listaPessoa != null && listaPessoa.size() > 0) {
					for (Pessoa p : listaPessoa) {
						list.add(p.getNomeCompleto() + " (Mat:"
								+ p.getMatricula() + ")");
					}
				} else {
					this.addInfoMessage("Selecionar",
							"Nenhum usuário encontrado.");
				}

			} catch (Exception e) {
				e.printStackTrace();
				this.addErrorMessage("Selecionar",
						"Erro na pesquisa de usuários.");
				return list;
			}
		}
		return list;
	}

	public void definePessoa() {
		pessoa = null;

		for (Pessoa p : listaPessoa) {
			String match = p.getNomeCompleto() + " (Mat:" + p.getMatricula()
					+ ")";

			if (campoPessoa.equals(match)) {
				pessoa = p;
				break;
			}
		}

		campoPessoa = pessoa.getNomeCompleto();
	}

	public void adicionarAutorizador() {
		if (pessoa != null) {
			List<Pessoa> pessoaList = itemReserva.getPessoaList();

			List<Pessoa> novaList = new ArrayList<Pessoa>();

			for (Pessoa pl : pessoaList) {
				if (pl.getIdPessoa() != pessoa.getIdPessoa()) {
					novaList.add(pl);
				}
			}

			novaList.add(pessoa);
			itemReserva.setPessoaList(novaList);

			try {
				ItemReservaService.alterar(itemReserva);
				this.itemReserva = ItemReservaService.encontrePorId(itemReserva
						.getIdItemReserva());
			} catch (Exception e) {
				addErrorMessage("Erro", e.getMessage());
			}
		} else {
			addErrorMessage("Pessoa", "Autorizador não selecionado");
		}

	}

	public void cancelaAdicionarAutorizador() {
		this.status = 1;
		this.pessoa = null;
		this.itemReserva = null;
	}

	public List<ItemReserva> getLista() {
		return lista;
	}

	public String getTextoPesquisa() {
		return textoPesquisa;
	}

	public void setTextoPesquisa(String textoPesquisa) {
		this.textoPesquisa = textoPesquisa;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public ItemReserva getItemReserva() {
		return itemReserva;
	}

	public void setItemReserva(ItemReserva itemReserva) {
		this.itemReserva = itemReserva;
	}

	public List<Pessoa> getListaPessoa() {
		return listaPessoa;
	}

	public void setListaPessoa(List<Pessoa> listaPessoa) {
		this.listaPessoa = listaPessoa;
	}

	public String getCampoPessoa() {
		return campoPessoa;
	}

	public void setCampoPessoa(String campoPessoa) {
		this.campoPessoa = campoPessoa;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

}
