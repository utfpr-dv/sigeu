package br.edu.utfpr.dv.sigeu.exception;

import java.text.SimpleDateFormat;

import br.edu.utfpr.dv.sigeu.entities.Reserva;

public class ExisteReservaConcorrenteException extends Exception {

	private static final long serialVersionUID = 23207316413183957L;
	private static SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");
	private static SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");

	public ExisteReservaConcorrenteException(Reserva reserva) {
		super(
				"Reserva do item "
						+ reserva.getIdItemReserva().getRotulo()
						+ " possui reservas feitas anteriormente que conflitam com a data/hora: "
						+ sdfData.format(reserva.getData()) + " de "
						+ sdfHora.format(reserva.getHoraInicio()) + " até "
						+ sdfHora.format(reserva.getHoraFim()));
	}

}
