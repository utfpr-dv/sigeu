package br.edu.utfpr.dv.sigeu.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import br.edu.utfpr.dv.sigeu.config.Config;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.ItemReserva;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.entities.Reserva;
import br.edu.utfpr.dv.sigeu.enumeration.DiaEnum;
import br.edu.utfpr.dv.sigeu.exception.DestinatarioInexistenteException;
import br.edu.utfpr.dv.sigeu.sort.ReservaDataComparator;
import br.edu.utfpr.dv.sigeu.util.MensagemEmail;

public class EmailService {

	public static void enviaEmailConfirmacao(Campus campus, Reserva reserva, String[] emails) throws Exception {
		List<Reserva> reservas = new ArrayList<Reserva>();
		reservas.add(reserva);
		EmailService.enviaEmailConfirmacao(campus, reservas, emails);
	}

	/**
	 * Envia e-mail de confirmação de reserva .
	 * 
	 * @param reserva
	 * @throws Exception
	 */
	public static void enviaEmailConfirmacao(Campus campus, List<Reserva> listaReserva, String[] emails)
			throws Exception {

		MensagemEmail email = new MensagemEmail(campus);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

		Collections.sort(listaReserva, new ReservaDataComparator());

		if (Config.getInstance().isDebugMode()) {
			emails = new String[] { "tiagoadami@utfpr.edu.br" };
		}

		String assunto = "SIGEU: Confirmação de Reserva(s)";

		StringBuilder sb = new StringBuilder(
				"<h1>CONFIRMAÇÃO DE RESERVA(S) REALIZADA(S) ATRAVÉS DO SIGEU:</h1><br/><table cellspacing=\"2\">");

		sb.append("<tr>");
		sb.append("<td><b>Recurso</b></td>");
		sb.append("<td><b>Tipo</b></td>");
		sb.append("<td><b>Dia da Semana</b></td>");
		sb.append("<td><b>Data</b></td>");
		sb.append("<td><b>Horário</b></td>");
		sb.append("<td><b>Usuário</b></td>");
		sb.append("<td><b>Descrição</b></td>");
		sb.append("<td><b>Motivo</b></td>");
		sb.append("<td><b>Quem fez a reserva</b></td>");
		sb.append("<td><b>Quem autorizou</b></td>");
		sb.append("</tr>");

		try {
			for (Reserva r : listaReserva) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(r.getData());
				String diaDaSemana = DiaEnum.getDiaEnumByDia(cal.get(Calendar.DAY_OF_WEEK)).getNome();

				String data = dateFormat.format(r.getData());
				String horario = timeFormat.format(r.getHoraInicio()) + " - " + timeFormat.format(r.getHoraFim());
				// String assunto = "Reserva " + r.getIdItemReserva().getNome()
				// + " em " + data + " (" + diaDaSemana + ")";

				String motivo = r.getMotivo().replaceAll("\\r?\\n", " ");
				motivo = motivo.replaceAll("\\r\\n", " ");
				motivo = motivo.replaceAll("\\r", " ");
				motivo = motivo.replaceAll("\\n", " ");

				sb.append("<tr>");

				// Recurso
				sb.append("<td>").append(r.getIdItemReserva().getNome().toUpperCase()).append("</td>");

				// Tipo
				sb.append("<td>").append(r.getIdItemReserva().getIdCategoria().getNome().toUpperCase()).append("</td>");
				// Dia da Semana
				sb.append("<td>").append(diaDaSemana).append("</td>");

				// Data
				sb.append("<td>").append(data).append("</td>");

				// Horário
				sb.append("<td>").append(horario).append("</td>");

				// Usuário
				sb.append("<td>").append(r.getNomeUsuario().trim().toUpperCase()).append("</td>");

				// Descriçao
				sb.append("<td>").append(r.getIdTipoReserva().getDescricao()).append("</td>");

				// Motivo
				sb.append("<td>").append(motivo).append("</td>");

				// Quem fez a reseva
				sb.append("<td>").append(r.getIdPessoa().getNomeCompleto().trim().toUpperCase()).append("</td>");

				// Quem autorizou
				sb.append("<td>").append(r.getIdAutorizador().getNomeCompleto().trim().toUpperCase()).append("</td>");

				sb.append("</tr>");

			}

			sb.append("</table><br/>");
			sb.append("<b><i>ATENÇÃO: Este é um e-mail automático enviado pelo SIGEU. Para mais informações ");
			sb.append("entre em contato direto com o responsável pela reserva assinalado acima.</b></i>");

			email.criaMensagem(emails, null, null, assunto, sb.toString(), true, null);

			email.enviaMensagens();
		} catch (DestinatarioInexistenteException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void enviaEmailCancelamento(Campus campus, Reserva reserva, String[] emails, Pessoa login,
			String motivoCancelamento) throws Exception {
		List<Reserva> reservas = new ArrayList<Reserva>();
		reservas.add(reserva);
		EmailService.enviaEmailCancelamento(campus, reservas, emails, login, motivoCancelamento);
	}

	/**
	 * Envia e-mail de confirmação de reserva .
	 * 
	 * @param reserva
	 * @throws Exception
	 */
	public static void enviaEmailCancelamento(Campus campus, List<Reserva> listaReserva, String[] emails, Pessoa login,
			String motivoCancelamento) throws Exception {

		MensagemEmail email = new MensagemEmail(campus);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

		Collections.sort(listaReserva, new ReservaDataComparator());

		if (Config.getInstance().isDebugMode()) {
			emails = new String[] { "tiagoadami@utfpr.edu.br" };
		}

		String assunto = "SIGEU: Cancelamento de Reserva(s)";

		StringBuilder sb = new StringBuilder(
				"<h1>CANCELAMENTO DE RESERVA(S) REALIZADA(S) ATRAVÉS DO SIGEU:</h1><br/><table cellspacing=\"2\">");

		sb.append("<tr>");
		sb.append("<td><b>Recurso</b></td>");
		sb.append("<td><b>Tipo</b></td>");
		sb.append("<td><b>Dia da Semana</b></td>");
		sb.append("<td><b>Data</b></td>");
		sb.append("<td><b>Horário</b></td>");
		sb.append("<td><b>Usuário</b></td>");
		sb.append("<td><b>Descrição</b></td>");
		sb.append("<td><b>Motivo</b></td>");
		sb.append("<td><b>Quem cancelou</b></td>");
		sb.append("<td><b>Motivo do Cancelamento</b></td>");
		sb.append("</tr>");

		try {
			for (Reserva r : listaReserva) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(r.getData());
				String diaDaSemana = DiaEnum.getDiaEnumByDia(cal.get(Calendar.DAY_OF_WEEK)).getNome();

				String data = dateFormat.format(r.getData());
				String horario = timeFormat.format(r.getHoraInicio()) + " - " + timeFormat.format(r.getHoraFim());
				// String assunto = "Reserva " + r.getIdItemReserva().getNome()
				// + " em " + data + " (" + diaDaSemana + ")";

				String motivo = r.getMotivo().replaceAll("\\r?\\n", " ");
				motivo = motivo.replaceAll("\\r\\n", " ");
				motivo = motivo.replaceAll("\\r", " ");
				motivo = motivo.replaceAll("\\n", " ");

				sb.append("<tr>");

				// Recurso
				sb.append("<td>").append(r.getIdItemReserva().getNome().toUpperCase()).append("</td>");

				// Tipo
				sb.append("<td>").append(r.getIdItemReserva().getIdCategoria().getNome().toUpperCase()).append("</td>");
				// Dia da Semana
				sb.append("<td>").append(diaDaSemana).append("</td>");

				// Data
				sb.append("<td>").append(data).append("</td>");

				// Horário
				sb.append("<td>").append(horario).append("</td>");

				// Usuário
				sb.append("<td>").append(r.getNomeUsuario().trim().toUpperCase()).append("</td>");

				// Descriçao
				sb.append("<td>").append(r.getIdTipoReserva().getDescricao()).append("</td>");

				// Motivo
				sb.append("<td>").append(motivo).append("</td>");

				// Quem cancelou
				sb.append("<td>").append(login.getNomeCompleto().trim().toUpperCase()).append("</td>");

				// motivo
				sb.append("<td>").append(motivoCancelamento).append("</td>");

				sb.append("</tr>");

			}

			sb.append("</table><br/>");
			sb.append("<b><i>ATENÇÃO: Este é um e-mail automático enviado pelo SIGEU. Para mais informações ");
			sb.append("entre em contato direto com o responsável pela reserva assinalado acima.</b></i>");

			email.criaMensagem(emails, null, null, assunto, sb.toString(), true, null);

			email.enviaMensagens();
		} catch (DestinatarioInexistenteException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Envia e-mail solicitando intervenção do autorizador.
	 * 
	 * @param autorizador
	 * @param itemReserva
	 */
	public static void enviaEmailAutorizador(Campus campus, Pessoa autorizador, ItemReserva itemReserva) {
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
			sb.append("Por gentileza, acesse o sistema pelo endereço ").append(Config.getInstance().getUrl());
			sb.append(
					" e acesse o menu \"Reservas\", \"Autorizar Reservas Pendentes\" para obter a lista das reservas pendentes.\n\n\n\n\n");
			sb.append(
					"Você recebeu este e-mail porque está cadastrado como responsável pelas reservas do item supracitado.\n\n");
			sb.append("Caso haja algum engano, por gentileza entre em contato com o administrador do sistema.");

			MensagemEmail email = new MensagemEmail(campus);

			// Envia mensagem para o autorizador
			email.criaMensagemTextoSimples(emailAutorizador, null, assunto, sb.toString());

			// Envia as mensagens por Thread
			email.enviaMensagens();
		} catch (DestinatarioInexistenteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String[] getEmailFromReservas(List<Reserva> listReservas) {
		List<String> emails = new ArrayList<String>();

		boolean exist = false;

		for (Reserva r : listReservas) {
			// Pessoa
			exist = false;
			for (String s : emails) {
				if (s.equals(r.getIdPessoa().getEmail())) {
					exist = true;
					break;
				}
			}
			if (!exist) {
				emails.add(r.getIdPessoa().getEmail());
			}

			// Usuario
			exist = false;
			for (String s : emails) {
				if (s.equals(r.getIdUsuario().getEmail())) {
					exist = true;
					break;
				}
			}
			if (!exist) {
				emails.add(r.getIdUsuario().getEmail());
			}

			// autorizador
			exist = false;
			for (String s : emails) {
				if (s.equals(r.getIdAutorizador().getEmail())) {
					exist = true;
					break;
				}
			}
			if (!exist) {
				emails.add(r.getIdAutorizador().getEmail());
			}

			// Email de notificacao
			exist = false;
			for (String s : emails) {
				if (s.equals(r.getEmailNotificacao())) {
					exist = true;
					break;
				}
			}
			if (!exist) {
				emails.add(r.getEmailNotificacao());
			}
		}

		Object objs[] = emails.toArray();
		String emailsArray[] = new String[emails.size()];

		for (int i = 0; i < objs.length; i++) {
			Object o = objs[i];
			emailsArray[i] = (String) o;
		}

		return emailsArray;
	}

	public static String[] getEmailFromReserva(Reserva reserva) {
		List<Reserva> listReservas = new ArrayList<Reserva>();
		listReservas.add(reserva);
		return getEmailFromReservas(listReservas);
	}
}
