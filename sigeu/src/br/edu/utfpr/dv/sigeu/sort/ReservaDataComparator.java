package br.edu.utfpr.dv.sigeu.sort;

import java.util.Comparator;

import br.edu.utfpr.dv.sigeu.entities.Reserva;

/**
 * Comparador de datas para ordenação em listas
 * 
 * @author Tiago
 *
 */
public class ReservaDataComparator implements Comparator<Reserva> {

	@Override
	public int compare(Reserva r1, Reserva r2) {
		return r1.getData().compareTo(r2.getData());
		//return DateTimeUtils.dateDiff(r1.getData(), r2.getData()).intValue();
	}
}
