package br.edu.utfpr.dv.sigeu.service;

import br.edu.utfpr.dv.sigeu.dao.TransacaoDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.entities.Transacao;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class TransacaoService {

	public static Transacao criar(Campus campus, Pessoa pessoaLogin,
			String descricao) {
		Transaction trans = new Transaction();
		trans.begin();

		TransacaoDAO dao = new TransacaoDAO(trans);
		Transacao t = dao.criaTransacao(campus, pessoaLogin, descricao);
		trans.commit();
		trans.close();
		return t;
	}
}
