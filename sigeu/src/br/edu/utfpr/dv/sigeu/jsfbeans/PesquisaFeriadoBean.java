package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import br.edu.utfpr.dv.sigeu.entities.Feriado;
import br.edu.utfpr.dv.sigeu.service.FeriadoService;

@Named
@ViewScoped
public class PesquisaFeriadoBean extends JavaBean {
	@Inject
	private LoginBean loginBean;

	private static final long serialVersionUID = -7338998125882395663L;

	//
	private String textoPesquisa;
	private Date dataInicial;
	private Date dataFinal;
	private List<Feriado> lista;

	//

	@PostConstruct
	public void init() {
		try {
			lista = FeriadoService.pesquisar(loginBean.getCampus(), null);
			// this.addInfoMessage("Pesquisa", "Exibindo " +
			// HibernateDAO.PESQUISA_LIMITE + " itens. Pesquise utilizando
			// parâmetros para obter mais registros.");
		} catch (Exception e) {
			// this.addErrorMessage("Pesquisa", "Erro ao realizar pesquisa
			// inicial. Entre em contato com o Admin.");
		}
	}

	/**
	 * Realiza a pesquisa de itens
	 */
	public void pesquisa() {
		try {
			this.lista = FeriadoService.pesquisar(loginBean.getCampus(), textoPesquisa);
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("Pesquisa", "Erro na pesquisa");
		}
	}

	/**
	 * Realiza a pesquisa de feriados por data
	 */
	public void pesquisaPorData() {
		try {
			this.lista = FeriadoService.pesquisarPorData(loginBean.getCampus(), dataInicial, dataFinal);
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("Pesquisa", "Erro na pesquisa");
		}
	}

	public List<Feriado> getLista() {
		return lista;
	}

	public String getTextoPesquisa() {
		return textoPesquisa;
	}

	public void setTextoPesquisa(String textoPesquisa) {
		this.textoPesquisa = textoPesquisa;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

}
