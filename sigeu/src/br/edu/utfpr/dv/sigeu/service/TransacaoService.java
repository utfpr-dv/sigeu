package br.edu.utfpr.dv.sigeu.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.edu.utfpr.dv.sigeu.dao.TransacaoDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.entities.Transacao;

@Stateless
public class TransacaoService {

    @EJB
    private TransacaoDAO dao;

    public Transacao criar(Campus campus, Pessoa pessoaLogin, String descricao) {
	return dao.criaTransacao(campus, pessoaLogin, descricao);
    }
}