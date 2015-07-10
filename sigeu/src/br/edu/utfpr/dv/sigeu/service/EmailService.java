package br.edu.utfpr.dv.sigeu.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import br.edu.utfpr.dv.sigeu.config.Config;
import br.edu.utfpr.dv.sigeu.entities.ItemReserva;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.entities.Reserva;
import br.edu.utfpr.dv.sigeu.enumeration.DiaEnum;
import br.edu.utfpr.dv.sigeu.enumeration.StatusReserva;
import br.edu.utfpr.dv.sigeu.exception.DestinatarioInexistenteException;
import br.edu.utfpr.dv.sigeu.sort.ReservaDataComparator;
import br.edu.utfpr.dv.sigeu.util.MensagemEmail;

public class EmailService {

	/**
	 * Envia e-mail de confirmação de reserva .
	 * 
	 * @param reserva
	 * @throws Exception
	 */
	public static void enviaEmailConfirmacao(Reserva reserva) throws Exception {
		MensagemEmail email = new MensagemEmail();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

		// Collections.sort(listaReserva, new ReservaDataComparator());

		String emailUsuario = reserva.getIdUsuario().getEmail();
		String emailReserva = reserva.getIdPessoa().getEmail();
		String emailAutorizador = reserva.getIdAutorizador().getEmail();

		if (Config.getInstance().isDebugMode()) {
			emailUsuario = "tiagoadami@utfpr.edu.br";
			emailReserva = "tiagoadami@utfpr.edu.br";
			emailAutorizador = "tiagoadami@utfpr.edu.br";
		}

		String assunto = "SIGEU: Confirmação de Reserva(s)";

		StringBuilder sb = new StringBuilder(
				"CONFIRMAÇÃO DE RESERVA REALIZADA ATRAVÉS DO SIGEU:\n\n");

		try {
			// for (Reserva r : listaReserva) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(reserva.getData());
			String diaDaSemana = DiaEnum.getDiaEnumByDia(
					cal.get(Calendar.DAY_OF_WEEK)).getNome();

			String data = dateFormat.format(reserva.getData());
			String horario = "horário de "
					+ timeFormat.format(reserva.getHoraInicio()) + " até "
					+ timeFormat.format(reserva.getHoraFim()) + " hs.";
			// String assunto = "Reserva " + r.getIdItemReserva().getNome()
			// + " em " + data + " (" + diaDaSemana + ")";

			String motivo = reserva.getMotivo().replaceAll("\\r?\\n", " ");
			motivo = motivo.replaceAll("\\r\\n", " ");
			motivo = motivo.replaceAll("\\r", " ");
			motivo = motivo.replaceAll("\\n", " ");

			// StringBuilder sb = new
			// StringBuilder("CONFIRMAÇÃO DE RESERVA PARA: ").append(r.getIdUsuario().getNomeCompleto().trim().toUpperCase());
			// sb.append(" (").append(r.getIdTipoReserva().getDescricao()).append(")\n\n");
			// sb.append(r.getIdItemReserva().getNome()).append("\n");
			// sb.append(diaDaSemana).append(", ").append(data).append(" ").append(horario).append("\n\n");
			// sb.append("Motivo:\n").append(motivo).append("\n\n\n");
			// sb.append("Reserva ").append("#").append(r.getIdReserva()).append(" feita por ");
			// sb.append(r.getIdPessoa().getNomeCompleto().trim().toUpperCase()).append("\n\n");
			// sb.append("Este é um e-mail automático enviado pelo SIGEU - Sistema de Gestão Universitária");

			sb.append("Reservado para:\n");
			sb.append(reserva.getNomeUsuario().trim().toUpperCase()).append(
					"\n\n");

			sb.append(
					reserva.getIdItemReserva().getIdCategoria().getNome()
							.toUpperCase()).append("\n");

			sb.append(reserva.getIdItemReserva().getNome().toUpperCase())
					.append("\n");

			sb.append(diaDaSemana).append(", dia ").append(data).append(", ")
					.append(horario).append("\n\n");

			sb.append("Tipo de Reserva:\n")
					.append(reserva.getIdTipoReserva().getDescricao())
					.append("\n\n");

			sb.append("Motivo:\n").append(motivo).append("\n\n");

			sb.append("---\n\n");

			sb.append("Quem fez a reserva: ")
					.append(reserva.getIdPessoa().getNomeCompleto().trim()
							.toUpperCase()).append("\n");

			sb.append("Quem autorizou a reserva: ")
					.append(reserva.getIdAutorizador().getNomeCompleto().trim()
							.toUpperCase()).append("\n\n\n");

			sb.append("ATENÇÃO: Este é um e-mail automático enviado pelo SIGEU. Para maiores informações ");
			sb.append("entre em contato direto com o responsável pela reserva assinalado acima.");

			// sb.append("---\n\n");

			// }

			String ls_to[] = { emailUsuario };
			String ls_cc[] = { emailReserva, emailAutorizador };
			email.criaMensagem(ls_to, ls_cc, null, assunto, sb.toString(),
					false, null);
			// email.criaMensagemTextoSimples(emailUsuario, emailReserva,
			// assunto, sb.toString());

			email.enviaMensagens();
		} catch (DestinatarioInexistenteException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cria o e-mail de confirmação da reserva, retornando o objeto para enviar
	 * os e-mails.
	 * 
	 * @param listaReservas
	 * @throws Exception
	 */
	public static void enviaEmailConfirmacao(List<Reserva> listaReservas)
			throws Exception {
		Collections.sort(listaReservas, new ReservaDataComparator());

		for (Reserva reserva2 : listaReservas) {
			if (reserva2.getStatus()
					.equals(StatusReserva.EFETIVADA.getStatus())) {
				EmailService.enviaEmailConfirmacao(reserva2);
			}
		}
	}

	/**
	 * Envia e-mail informando o cancelamento das reservas
	 * 
	 * @param reserva
	 * @param motivoCancelamento
	 * @throws Exception
	 */
	public static void enviaEmailCancelamento(Reserva reserva,
			String motivoCancelamento) throws Exception {
		MensagemEmail email = new MensagemEmail();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

		Pessoa pessoa = Config.getInstance().getPessoaLogin();

		// Collections.sort(reserva, new ReservaDataComparator());

		String emailUsuario = reserva.getIdUsuario().getEmail();
		String emailReserva = reserva.getIdPessoa().getEmail();
		String emailLogin = pessoa.getEmail();

		if (Config.getInstance().isDebugMode()) {
			emailUsuario = "tiagoadami@utfpr.edu.br";
			emailReserva = "tiagoadami@utfpr.edu.br";
			emailLogin = "tiagoadami@utfpr.edu.br";
		}

		String assunto = "SIGEU: Cancelamento de Reserva(s)";

		StringBuilder sb = new StringBuilder(
				"CANCELAMENTO DE RESERVA FEITO ATRAVÉS DO SIGEU:\n\n");

		try {
			// for (Reserva r : reserva) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(reserva.getData());
			String diaDaSemana = DiaEnum.getDiaEnumByDia(
					cal.get(Calendar.DAY_OF_WEEK)).getNome();

			String data = dateFormat.format(reserva.getData());
			String horario = "horário de "
					+ timeFormat.format(reserva.getHoraInicio()) + " até "
					+ timeFormat.format(reserva.getHoraFim()) + " hs.";

			String motivo = reserva.getMotivo().replaceAll("\\r?\\n", " ");
			motivo = motivo.replaceAll("\\r\\n", " ");
			motivo = motivo.replaceAll("\\r", " ");
			motivo = motivo.replaceAll("\\n", " ");

			sb.append("Estava reservado para:\n");
			sb.append(reserva.getNomeUsuario().trim().toUpperCase()).append(
					"\n\n");

			sb.append(
					reserva.getIdItemReserva().getIdCategoria().getNome()
							.toUpperCase()).append("\n");

			sb.append(reserva.getIdItemReserva().getNome().toUpperCase())
					.append("\n");

			sb.append(diaDaSemana).append(", dia ").append(data).append(", ")
					.append(horario).append("\n\n");

			sb.append("Tipo de Reserva:\n")
					.append(reserva.getIdTipoReserva().getDescricao())
					.append("\n\n");

			sb.append("Motivo:\n").append(motivo).append("\n\n");

			sb.append("---\n\n");

			sb.append("Quem havia feito a reserva: ")
					.append(reserva.getIdPessoa().getNomeCompleto().trim()
							.toUpperCase()).append("\n");

			sb.append("Quem havia autorizado a reserva: ")
					.append(reserva.getIdAutorizador().getNomeCompleto().trim()
							.toUpperCase()).append("\n");

			sb.append("Quem cancelou a reserva: ")
					.append(pessoa.getNomeCompleto().trim().toUpperCase())
					.append("\n\n");

			sb.append("Motivo do cancelamento:\n").append(motivoCancelamento)
					.append("\n\n\n");

			sb.append("ATENÇÃO: Este é um e-mail automático enviado pelo SIGEU. Para maiores informações ");
			sb.append("entre em contato direto com o responsável pelo cancelamento assinalado acima.");

			String ls_to[] = { emailUsuario };
			String ls_cc[] = { emailReserva, emailLogin };

			// Envia mensagem para o usuário e para quem reservou, e também para
			// o usuário logado
			email.criaMensagem(ls_to, ls_cc, null, assunto, sb.toString(),
					false, null);

			// Envia as mensagens por Thread
			email.enviaMensagens();
		} catch (DestinatarioInexistenteException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Envia e-mail informando o cancelamento das reservas
	 * 
	 * @param listaReservas
	 * @param motivoCancelamento
	 * @throws Exception
	 */
	public static void enviaEmailCancelamento(List<Reserva> listaReservas,
			String motivoCancelamento) throws Exception {
		Collections.sort(listaReservas, new ReservaDataComparator());

		for (Reserva reserva2 : listaReservas) {
			if (reserva2.getStatus()
					.equals(StatusReserva.CANCELADA.getStatus())) {
				EmailService.enviaEmailCancelamento(reserva2,
						motivoCancelamento);
			}
		}
	}

	/**
	 * Envia e-mail solicitando intervenção do autorizador.
	 * 
	 * @param autorizador
	 * @param itemReserva
	 */
	public static void enviaEmailAutorizador(Pessoa autorizador,
			ItemReserva itemReserva) {
		try {
			String emailAutorizador = autorizador.getEmail();

			if (Config.getInstance().isDebugMode()) {
				emailAutorizador = "tiagoadami@utfpr.edu.br";
			}

			String assunto = "SIGEU: Autorizações pendentes";

			StringBuilder sb = new StringBuilder("Prezado servidor:\n\n");
			sb.append("Existem reservas pendentes do item \"");
			sb.append(itemReserva.getIdCategoria().getNome());
			sb.append(": ");
			sb.append(itemReserva.getNome());
			sb.append("\" que requisitam sua atenção.\n\n");
			sb.append("Por gentileza, acesse o sistema pelo endereço ").append(
					Config.APPLICATION_URL);
			sb.append(" e acesse o menu \"Reservas\", \"Autorizações\" para obter a lista das reservas pendentes.\n\n\n\n\n");
			sb.append("Você recebeu este e-mail porque está cadastrado como responsável pelas reservas do item supracitado.\n\n");
			sb.append("Caso haja algum engano, por gentileza entre em contato com o administrador do sistema.");

			MensagemEmail email = new MensagemEmail();

			// Envia mensagem para o autorizador
			email.criaMensagemTextoSimples(emailAutorizador, null, assunto,
					sb.toString());

			// Envia as mensagens por Thread
			email.enviaMensagens();
		} catch (DestinatarioInexistenteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
