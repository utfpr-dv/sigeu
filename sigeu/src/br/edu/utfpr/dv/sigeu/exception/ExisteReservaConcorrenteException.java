package br.edu.utfpr.dv.sigeu.exception;

import br.edu.utfpr.dv.sigeu.entities.Reserva;

public class ExisteReservaConcorrenteException extends Exception {

	private static final long serialVersionUID = 23207316413183957L;

	public ExisteReservaConcorrenteException(Reserva reserva) {
		super("Reserva do item" + reserva.getIdItemReserva().getRotulo() + " possui reservas concorrentes");
	}

}
