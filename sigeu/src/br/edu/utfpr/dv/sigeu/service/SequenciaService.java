package br.edu.utfpr.dv.sigeu.service;

import br.edu.utfpr.dv.sigeu.dao.SequenciaDAO;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;

public class SequenciaService {
	public static Long incrementa(String nome) {
		Transaction trans = new Transaction();
		trans.begin();

		SequenciaDAO dao = new SequenciaDAO(trans);
		Long val = dao.getNextValue(nome);
		trans.commit();
		trans.close();
		return val;
	}
}
