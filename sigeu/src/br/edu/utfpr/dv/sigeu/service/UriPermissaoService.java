package br.edu.utfpr.dv.sigeu.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.edu.utfpr.dv.sigeu.dao.UriPermissaoDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.GrupoPessoa;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.entities.UriPermissao;

@Stateless
public class UriPermissaoService {

    @EJB
    private UriPermissaoDAO dao;

    public UriPermissao pesquisaPorNomeGrupoPessoa(Campus campus, String nomeGrupo, String uri) {
	return dao.pesquisaUriPorNomeGrupoPessoa(campus, nomeGrupo, uri);
    }

    /**
     * Verifica permissão de acesso à URI.
     *
     * @param pessoa
     * @param uri
     * @return
     */
    public boolean verificaPermissaoDeAcesso(Pessoa pessoa, String uri) {

	// System.out.println(" "+uri);

	if (pessoa.getExterno() && pessoa.isExternoReserva() && uri.equals("/sigeu/restrito/Reserva.xhtml")) {
	    return true;
	}

	// Recupera a lista de permissoes por grupo do campus
	List<UriPermissao> list = dao.pesquisaPermissoesPorUri(pessoa.getIdCampus(), uri);

	/**
	 * Verifica se há bloqueio em pelo menos 1 grupo da pessoa. Se houver, está
	 * bloqueado. Bloqueio sempre haverá precedência sobre permissao.
	 */
	List<GrupoPessoa> grupos = pessoa.getGrupoPessoaList();

	for (GrupoPessoa g : grupos) {
	    // Verifica se o grupo da pessoa está cadastrado

	    // System.out.println("Grupo Pessoa = "+g.getIdGrupoPessoa());
	    for (UriPermissao u : list) {
		// System.out.println(" Grupo Permissao =
		// "+u.getIdGrupoPessoa().getIdGrupoPessoa());
		if (u.getIdGrupoPessoa().getIdGrupoPessoa() == g.getIdGrupoPessoa()) {
		    return u.getAcesso();
		}
	    }
	    /*
	     * Não cadastrar automaticamente
	     */
	    // if (exists) {
	    // return permissao.getAcesso();
	    // } else {
	    // /**
	    // * Quando não está cadastrado, considera liberado e cria
	    // * novo registro
	    // */
	    // permissao = new UriPermissao();
	    // permissao.setAcesso(true);
	    // permissao.setIdCampus(pessoa.getIdCampus());
	    // permissao.setIdGrupoPessoa(g);
	    // permissao.setUri(uri);
	    // UriPermissaoService.criar(permissao);
	    // return true;
	    // }
	}

	/**
	 * Se não encontrou registros, libera.
	 */
	return true;
    }

    public void criar(UriPermissao uriPermissao) {
	dao.criar(uriPermissao);
    }
}