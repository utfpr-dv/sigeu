package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import br.edu.utfpr.dv.sigeu.entities.PeriodoLetivo;
import br.edu.utfpr.dv.sigeu.service.PeriodoLetivoService;

@Named
@ViewScoped
public class PesquisaPeriodoLetivoBean extends JavaBean {

    private static final long serialVersionUID = -7338998125000395663L;

    @EJB
    private PeriodoLetivoService periodoLetivoService;

    @Inject
    private LoginBean loginBean;

    //
    private String textoPesquisa;
    private List<PeriodoLetivo> lista;

    //

    @PostConstruct
    public void init() {
	try {
	    String nullS = null;
	    lista = periodoLetivoService.pesquisar(loginBean.getCampus(), nullS);
	} catch (Exception e) {
	}
    }

    /**
     * Realiza a pesquisa de itens
     */
    public void pesquisa() {
	try {
	    this.lista = periodoLetivoService.pesquisar(loginBean.getCampus(), textoPesquisa);
	} catch (Exception e) {
	    e.printStackTrace();
	    addErrorMessage("Pesquisa", "Erro na pesquisa");
	}
    }

    public List<PeriodoLetivo> getLista() {
	return lista;
    }

    public String getTextoPesquisa() {
	return textoPesquisa;
    }

    public void setTextoPesquisa(String textoPesquisa) {
	this.textoPesquisa = textoPesquisa;
    }

}
