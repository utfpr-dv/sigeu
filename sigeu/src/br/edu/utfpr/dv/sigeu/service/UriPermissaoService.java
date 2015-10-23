package br.edu.utfpr.dv.sigeu.service;

import java.util.List;

import br.edu.utfpr.dv.sigeu.dao.UriPermissaoDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.GrupoPessoa;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.entities.UriPermissao;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class UriPermissaoService {

	public static UriPermissao pesquisaPorNomeGrupoPessoa(Campus campus,
			String nomeGrupo, String uri) {
		Transaction trans = null;

		try {
			trans = new Transaction();
			trans.begin();
			UriPermissaoDAO dao = new UriPermissaoDAO(trans);
			UriPermissao per = dao.pesquisaUriPorNomeGrupoPessoa(campus,
					nomeGrupo, uri);
			return per;
		} catch (Exception e) {
			throw e;
		} finally {
			if (trans != null) {
				trans.close();
			}
		}
	}

	/**
	 * Verifica permissão de acesso à URI.
	 *
	 * @param pessoa
	 * @param uri
	 * @return
	 */
	public static boolean verificaPermissaoDeAcesso(Pessoa pessoa, String uri) {
		Transaction trans = null;

		try {
			trans = new Transaction();
			trans.begin();
			UriPermissaoDAO dao = new UriPermissaoDAO(trans);

			// Recupera a lista de permissoes por grupo do campus
			List<UriPermissao> list = dao.pesquisaPermissoesPorUri(
					pessoa.getIdCampus(), uri);

			/**
			 * Verifica se há bloqueio em pelo menos 1 grupo da pessoa. Se
			 * houver, está bloqueado. Bloqueio sempre haverá precedência sobre
			 * permissao.
			 */
			List<GrupoPessoa> grupos = pessoa.getGrupoPessoaList();

			for (GrupoPessoa g : grupos) {
				// Verifica se o grupo da pessoa está cadastrado
				
				//System.out.println("Grupo Pessoa = "+g.getIdGrupoPessoa());
				for (UriPermissao u : list) {
					//System.out.println("  Grupo Permissao = "+u.getIdGrupoPessoa().getIdGrupoPessoa());
					if (u.getIdGrupoPessoa().getIdGrupoPessoa() == g
							.getIdGrupoPessoa()) {
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
		} catch (Exception e) {
			throw e;
		} finally {
			if (trans != null) {
				trans.close();
			}
		}
	}

	public static void criar(UriPermissao uriPermissao) {
		Transaction trans = null;

		try {
			trans = new Transaction();
			trans.begin();
			UriPermissaoDAO dao = new UriPermissaoDAO(trans);
			dao.criar(uriPermissao);
			trans.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			if (trans != null) {
				trans.close();
			}
		}
	}
}
