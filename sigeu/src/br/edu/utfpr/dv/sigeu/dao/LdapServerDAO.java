package br.edu.utfpr.dv.sigeu.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;

import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.LdapServer;
import br.edu.utfpr.dv.sigeu.persistence.HibernateDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class LdapServerDAO extends HibernateDAO<LdapServer> {

	public LdapServerDAO(Transaction transaction) {
		super(transaction);
	}

	@Override
	public LdapServer encontrePorId(Integer id) {
		/**
		 * O ID de servidores LDAP são únicos
		 */
		String hql = "from LdapServer o where o.idServer = :id";
		Query q = session.createQuery(hql);
		q.setInteger("id", id);
		return (LdapServer) q.uniqueResult();
	}

	/**
	 * Encontra um servidor LDAP através do endereço de e-mail institucional.
	 * 
	 * @param email
	 * @return
	 */
	public LdapServer encontrePorEmail(String email) {
		String suffix = null;
		int posAt;

		email = email.trim().toLowerCase();
		posAt = email.indexOf("@");

		if (posAt <= 0) {
			return null;
		}

		suffix = email.substring(posAt);

		String hql = "from LdapServer o where o.sufixoEmail = :suffix";
		Query q = session.createQuery(hql);
		q.setString("suffix", suffix);

		LdapServer ldap = (LdapServer) q.uniqueResult();

		if (ldap != null) {
			Hibernate.initialize(ldap.getIdCampus());
		}

		return ldap;
	}

	@Override
	public String getNomeSequencia() {
		return "ldap";
	}

	@Override
	public void preCriacao(LdapServer o) {
		Integer id = this.gerarNovoId().intValue();
		o.setIdServer(id);
	}

	public List<LdapServer> pesquisa(Campus campus, String textoPesquisa, int limit) {
		if (textoPesquisa == null || textoPesquisa.trim().equals("")) {
			return this.pesquisa(campus,limit);
		}
		String hql = "from LdapServer o where o.idCampus.idCampus = :idCampus AND upper(o.sufixoEmail) like upper(:q) or upper(o.host) like upper(:q) order by o.ativo DESC, upper(o.sufixoEmail) ASC";
		Query q = session.createQuery(hql);
		q.setInteger("idCampus", campus.getIdCampus());
		q.setString("q", "%" + textoPesquisa + "%");
		if (limit > 0) {
			q.setMaxResults(limit);
		}
		List<?> list = q.list();

		if (list.size() > 0) {
			List<LdapServer> retorno = new ArrayList<LdapServer>();

			for (Object o : list) {
				retorno.add((LdapServer) o);
			}
			return retorno;
		}
		return null;
	}

	public List<LdapServer> pesquisa(Campus campus, int limit) {
		String hql = "from LdapServer o WHERE o.idCampus.idCampus = :idCampus order by o.ativo DESC, upper(o.sufixoEmail) ASC";
		Query q = session.createQuery(hql);
		q.setInteger("idCampus", campus.getIdCampus());
		if (limit > 0) {
			q.setMaxResults(limit);
		}
		List<?> list = q.list();

		if (list.size() > 0) {
			List<LdapServer> retorno = new ArrayList<LdapServer>();

			for (Object o : list) {
				retorno.add((LdapServer) o);
			}
			return retorno;
		}
		return null;
	}

	@Override
	public void preAlteracao(LdapServer o) {
		// TODO Auto-generated method stub

	}
}
