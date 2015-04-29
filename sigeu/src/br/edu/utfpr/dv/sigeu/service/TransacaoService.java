package br.edu.utfpr.dv.sigeu.service;

import br.edu.utfpr.dv.sigeu.config.Config;
import br.edu.utfpr.dv.sigeu.dao.TransacaoDAO;
import br.edu.utfpr.dv.sigeu.entities.Transacao;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class TransacaoService {

	public static Transacao criar(String descricao) {
		Transaction trans = new Transaction();
		trans.begin();

		TransacaoDAO dao = new TransacaoDAO(trans);
		Transacao t = dao.criaTransacao(Config.getInstance().getCampus(), Config.getInstance().getPessoaLogin(), descricao);
		trans.commit();
		trans.close();
		return t;
	}
}
