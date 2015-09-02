package br.edu.utfpr.dv.sigeu.dao;

import java.util.List;

import org.hibernate.Query;

import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.UriPermissao;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class UriPermissaoDAO extends HibernateDAO<UriPermissao> {

	public UriPermissaoDAO(Transaction transaction) {
		super(transaction);
	}

	@Override
	public UriPermissao encontrePorId(Integer id) {
		String hql = "from UriPermissao o where o.idPermissao = :id";
		Query q = session.createQuery(hql);
		q.setInteger("id", id);
		return (UriPermissao) q.uniqueResult();
	}

	@Override
	public String getNomeSequencia() {
		return "instituicao";
	}

	@Override
	public void preCriacao(UriPermissao o) {
		// Automatic
	}

	public UriPermissao pesquisaUriPorNomeGrupoPessoa(Campus campus,
			String nomeGrupo, String uri) {
		String hql = "SELECT o FROM UriPermissao o JOIN GrupoPessoa g WHERE o.uri = :uri AND g.nome = :nome AND g.idCampus.idCampus = :idCampus AND o.idCampus.idCampus = :idCampus";
		Query q = session.createQuery(hql);
		q.setInteger("idCampus", campus.getIdCampus());
		q.setString("nome", nomeGrupo.trim().toUpperCase());
		q.setString("uri", uri);
		UriPermissao p = (UriPermissao) q.uniqueResult();
		return p;
	}

	public List<UriPermissao> pesquisaPermissoesPorUri(Campus campus, String uri) {
		String hql = "SELECT o FROM UriPermissao o WHERE o.uri = :uri AND o.idCampus.idCampus = :idCampus";
		Query q = session.createQuery(hql);
		q.setInteger("idCampus", campus.getIdCampus());
		q.setString("uri", uri);
		return this.pesquisaObjetos(q, 0);
	}

	@Override
	public void preAlteracao(UriPermissao o) {
		// TODO Auto-generated method stub

	}
}
