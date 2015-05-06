package br.edu.utfpr.dv.sigeu.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;

import br.edu.utfpr.dv.sigeu.config.Config;
import br.edu.utfpr.dv.sigeu.dao.ItemReservaDAO;
import br.edu.utfpr.dv.sigeu.dao.PeriodDAO;
import br.edu.utfpr.dv.sigeu.dao.ReservaDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.CategoriaItemReserva;
import br.edu.utfpr.dv.sigeu.entities.ItemReserva;
import br.edu.utfpr.dv.sigeu.entities.Period;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.entities.Reserva;
import br.edu.utfpr.dv.sigeu.entities.TipoReserva;
import br.edu.utfpr.dv.sigeu.entities.Transacao;
import br.edu.utfpr.dv.sigeu.enumeration.DiaEnum;
import br.edu.utfpr.dv.sigeu.enumeration.RepeticaoReservaEnum;
import br.edu.utfpr.dv.sigeu.exception.DestinatarioInexistenteException;
import br.edu.utfpr.dv.sigeu.exception.ExisteReservaConcorrenteException;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;
import br.edu.utfpr.dv.sigeu.util.MensagemEmail;
import br.edu.utfpr.dv.sigeu.vo.ReservaVO;

import com.adamiworks.utils.DateUtils;

public class ReservaService {

	public static void gravar(Reserva reserva) throws Exception {
		Transaction trans = new Transaction();
		Transacao transacao = TransacaoService.criar("Reserva do item " + reserva.getIdItemReserva().getNome());
		reserva.setIdTransacao(transacao);

		try {
			trans.begin();
			ReservaDAO dao = new ReservaDAO(trans);
			dao.criar(reserva);
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			trans.close();
		}
	}

	/**
	 * Cria e-mails de confirmação de reserva retornando o objeto para envio de
	 * e-mails.
	 * 
	 * @param listaReserva
	 * @throws Exception
	 */
	public static MensagemEmail criaEmailConfirmacao(List<Reserva> listaReserva) throws Exception {
		MensagemEmail email = new MensagemEmail();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

		try {
			for (Reserva r : listaReserva) {
				String emailUsuario = r.getIdUsuario().getEmail();
				String emailReserva = r.getIdPessoa().getEmail();

				if (Config.getInstance().isDebugMode()) {
					emailUsuario = "derdi-dv@utfpr.edu.br";
					emailReserva = "derdi-dv@utfpr.edu.br";
				}

				Calendar cal = Calendar.getInstance();
				cal.setTime(r.getData());
				String diaDaSemana = DiaEnum.getDiaEnumByDia(cal.get(Calendar.DAY_OF_WEEK)).getNome();

				String data = dateFormat.format(r.getData());
				String horario = "das " + timeFormat.format(r.getHoraInicio()) + " hs às " + timeFormat.format(r.getHoraFim()) + " hs";
				String assunto = "Reserva " + r.getIdItemReserva().getNome() + " em " + data + " (" + diaDaSemana + ")";
				String motivo = r.getMotivo().replaceAll("\\r?\\n", "\n   ");

				StringBuilder sb = new StringBuilder("CONFIRMAÇÃO DE RESERVA PARA: ").append(r.getIdUsuario().getNomeCompleto().trim().toUpperCase());
				sb.append(" (").append(r.getIdTipoReserva().getDescricao()).append(")\n\n");
				sb.append(r.getIdItemReserva().getNome()).append("\n");
				sb.append(diaDaSemana).append(", ").append(data).append(" ").append(horario).append("\n\n");
				sb.append("Motivo:\n").append(motivo).append("\n\n\n");
				sb.append("Reserva ").append("#").append(r.getIdReserva()).append(" feita por ");
				sb.append(r.getIdPessoa().getNomeCompleto().trim().toUpperCase()).append("\n\n");
				sb.append("Este é um e-mail automático enviado pelo SIGEU - Sistema de Gestão Universitária");

				email.criaMensagemTextoSimples(emailUsuario, emailReserva, assunto, sb.toString());
			}

			return email;
		} catch (DestinatarioInexistenteException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Cria o e-mail de confirmação da reserva, retornando o objeto para enviar
	 * os e-mails.
	 * 
	 * @param reserva
	 * @throws Exception
	 */
	public static MensagemEmail criaEmailConfirmacao(Reserva reserva) throws Exception {
		List<Reserva> lr = new ArrayList<Reserva>();
		lr.add(reserva);
		return ReservaService.criaEmailConfirmacao(lr);
	}

	/**
	 * Envia 1 e-mail informando o cancelamento das reservas
	 * 
	 * @param listaReserva
	 * @param motivoCancelamento
	 * @throws Exception
	 */
	public static MensagemEmail criaEmailCancelamento(List<Reserva> listaReserva, String motivoCancelamento) throws Exception {
		MensagemEmail email = new MensagemEmail();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

		Pessoa pessoa = Config.getInstance().getPessoaLogin();

		try {
			for (Reserva r : listaReserva) {
				String emailUsuario = r.getIdUsuario().getEmail();
				String emailReserva = r.getIdPessoa().getEmail();

				if (Config.getInstance().isDebugMode()) {
					emailUsuario = "derdi-dv@utfpr.edu.br";
					emailReserva = "derdi-dv@utfpr.edu.br";
				}

				Calendar cal = Calendar.getInstance();
				cal.setTime(r.getData());
				String diaDaSemana = DiaEnum.getDiaEnumByDia(cal.get(Calendar.DAY_OF_WEEK)).getNome();

				String data = dateFormat.format(r.getData());
				String horario = "das " + timeFormat.format(r.getHoraInicio()) + " hs às " + timeFormat.format(r.getHoraFim()) + " hs";
				String assunto = "Cancelamento de reserva " + r.getIdItemReserva().getNome() + " em " + data + " (" + diaDaSemana + ")";
				String motivo = r.getMotivo().replaceAll("\\r?\\n", "\n   ");

				StringBuilder sb = new StringBuilder("**CANCELAMENTO** DE RESERVA PARA: ").append(r.getIdUsuario().getNomeCompleto().trim().toUpperCase());
				sb.append(" (").append(r.getIdTipoReserva().getDescricao()).append(")\n\n");
				sb.append(r.getIdItemReserva().getNome()).append("\n");
				sb.append(diaDaSemana).append(", ").append(data).append(" ").append(horario).append("\n\n");
				sb.append("Motivo da Reserva:\n").append(motivo).append("\n\n\n");
				sb.append("Cancelamento de reserva ").append("#").append(r.getIdReserva()).append(" feita por ");
				sb.append(pessoa.getNomeCompleto().trim().toUpperCase()).append("\n\n");
				sb.append("Motivo do Cancelamento:\n").append(motivoCancelamento).append("\n\n");
				sb.append("Este é um e-mail automático enviado pelo SIGEU - Sistema de Gestão Universitária");

				email.criaMensagemTextoSimples(emailUsuario, emailReserva, assunto, sb.toString());

				// Envia também um e-mail p/ quem cancelou
				email.criaMensagemTextoSimples(pessoa.getEmail(), null, assunto, sb.toString());
			}

			return email;
		} catch (DestinatarioInexistenteException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Duplica um objeto de reserva
	 * 
	 * @param reserva
	 * @return
	 */
	public static Reserva duplicar(Reserva reserva) {
		Reserva r = new Reserva();
		r.setCor(reserva.getCor());
		r.setData(reserva.getData());
		r.setEmailNotificacao(reserva.getEmailNotificacao());
		r.setHoraFim(reserva.getHoraFim());
		r.setHoraInicio(reserva.getHoraInicio());
		r.setIdCampus(reserva.getIdCampus());
		r.setIdItemReserva(reserva.getIdItemReserva());
		r.setIdPessoa(reserva.getIdPessoa());
		r.setIdTipoReserva(reserva.getIdTipoReserva());
		r.setIdTransacao(reserva.getIdTransacao());
		r.setIdUsuario(reserva.getIdUsuario());
		r.setMotivo(reserva.getMotivo());
		r.setRotulo(reserva.getRotulo());

		return r;
	}

	/**
	 * Grava uma reserva de forma recorrente até uma data limite.
	 * 
	 * @param reserva
	 * @param tipoRecorrencia
	 * @param dataLimite
	 * @throws Exception
	 */
	public static List<Reserva> gravarRecorrente(Reserva reserva, RepeticaoReservaEnum tipoRecorrencia, Date dataLimite) throws Exception {
		if (tipoRecorrencia.equals(RepeticaoReservaEnum.SEMANAL)) {
			Transaction trans = new Transaction();
			Transacao transacao = TransacaoService.criar("Reserva do item " + reserva.getIdItemReserva().getNome() + " semanal");
			reserva.setIdTransacao(transacao);

			try {
				trans.begin();

				ReservaDAO dao = new ReservaDAO(trans);

				Calendar calLimite = Calendar.getInstance();
				calLimite.setTime(dataLimite);

				Calendar calData = Calendar.getInstance();
				calData.setTime(reserva.getData());
				int diaDaSemana = calData.get(Calendar.DAY_OF_WEEK);

				// Incrementa 1 dia para começar o laço
				calData.add(Calendar.DAY_OF_MONTH, 1);

				boolean existeConcorrencia = false;

				List<Reserva> listaGravacao = new ArrayList<Reserva>();

				while (true) {
					if (calData.getTimeInMillis() > calLimite.getTimeInMillis()) {
						break;
					}

					int diaDaSemanaAtual = calData.get(Calendar.DAY_OF_WEEK);

					if (diaDaSemanaAtual == diaDaSemana) {
						Reserva r = ReservaService.duplicar(reserva);
						r.setData(calData.getTime());

						existeConcorrencia = ReservaService.existeConcorrente(r);

						if (!existeConcorrencia) {
							listaGravacao.add(r);
						} else {
							break;
						}
					}

					calData.add(Calendar.DAY_OF_MONTH, 1);
				}

				if (existeConcorrencia) {
					trans.rollback();
					throw new ExisteReservaConcorrenteException(reserva);
				} else {
					// Grava a reserva inicial
					listaGravacao.add(reserva);

					for (Reserva r : listaGravacao) {
						dao.criar(r);
					}

					trans.commit();
				}

				return listaGravacao;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				trans.close();
			}
		}
		return null;
	}

	/**
	 * Confere se já existe reserva concorrente
	 * 
	 * @param reserva
	 * @return
	 * @throws Exception
	 */
	public static boolean existeConcorrente(Reserva reserva) throws Exception {
		Transaction trans = new Transaction();

		try {
			trans.begin();
			ItemReservaDAO dao = new ItemReservaDAO(trans);

			List<ItemReserva> lista = dao.pesquisaItemReservaDisponivel(reserva.getIdCampus(), reserva.getData(), reserva.getHoraInicio(),
					reserva.getHoraFim(), reserva.getIdItemReserva().getIdCategoria(), reserva.getIdItemReserva());

			if (lista != null && lista.size() == 0) {
				return true;
			}

			return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}

	/**
	 * Busca todas as reservas do dia para uma categoria e item, se ele não
	 * estiver null
	 * 
	 * @param data
	 * @param categoria
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public static List<Reserva> pesquisaReservasDoDia(Date data, CategoriaItemReserva categoria, ItemReserva item) throws Exception {
		Transaction trans = new Transaction();

		try {
			trans.begin();
			ReservaDAO dao = new ReservaDAO(trans);

			List<Reserva> lista = dao.pesquisaReserva(Config.getInstance().getCampus(), data, categoria, item);

			if (lista != null && lista.size() > 0) {
				for (Reserva r : lista) {
					// Hibernate.initialize(r.getIdItemReserva());
					Hibernate.initialize(r.getIdItemReserva().getIdCategoria());
					Hibernate.initialize(r.getIdTipoReserva());
					Hibernate.initialize(r.getIdUsuario());
					Hibernate.initialize(r.getIdPessoa());
					Hibernate.initialize(r.getIdTransacao());
				}
			}

			return lista;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}

	/**
	 * Busca todos os itens que estão disponíveis para a data e horário,
	 * categoria e item (Se não estiver null)
	 * 
	 * @param data
	 * @param horaInicial
	 * @param horaFinal
	 * @param categoria
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public static List<ItemReserva> pesquisaItemReservaDisponivel(Date data, Date horaInicial, Date horaFinal, CategoriaItemReserva categoria, ItemReserva item)
			throws Exception {
		Transaction trans = new Transaction();

		try {
			trans.begin();
			ItemReservaDAO dao = new ItemReservaDAO(trans);

			List<ItemReserva> lista = dao.pesquisaItemReservaDisponivel(Config.getInstance().getCampus(), data, horaInicial, horaFinal, categoria, item);

			if (lista != null && lista.size() > 0) {
				for (ItemReserva i : lista) {
					Hibernate.initialize(i.getIdCategoria());
				}
			}

			return lista;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}

	/**
	 * Retorna uma lista de todas as reservas de uma data feitas por uma pessoa.
	 * 
	 * @param pessoa
	 *            Não pode ser null
	 * @param data
	 *            Não pode ser null
	 * @param categoria
	 *            pode ser null
	 * @param itemReserva
	 *            poder ser null
	 * @return
	 * @throws Exception
	 */
	public static List<Reserva> pesquisaReservas(Pessoa pessoa, Date data, CategoriaItemReserva categoria, ItemReserva item) throws Exception {
		Transaction trans = new Transaction();

		try {
			trans.begin();
			ReservaDAO dao = new ReservaDAO(trans);

			List<Reserva> lista = dao.pesquisaReserva(Config.getInstance().getCampus(), pessoa, data, categoria, item);

			if (lista != null && lista.size() > 0) {
				for (Reserva r : lista) {
					// Hibernate.initialize(r.getIdItemReserva());
					Hibernate.initialize(r.getIdItemReserva().getIdCategoria());
					Hibernate.initialize(r.getIdTipoReserva());
					Hibernate.initialize(r.getIdTransacao());
				}
			}

			return lista;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}

	public static Reserva pesquisaReservaPorId(Integer id) throws Exception {
		Transaction trans = new Transaction();

		try {
			trans.begin();
			ReservaDAO dao = new ReservaDAO(trans);
			Reserva r = dao.encontrePorId(id);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}

	public static void excluir(Reserva r) throws Exception {
		Transaction trans = new Transaction();

		try {
			trans.begin();
			ReservaDAO dao = new ReservaDAO(trans);

			dao.remover(r);

			trans.commit();
		} catch (Exception e) {
			trans.rollback();
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			trans.close();
		}
	}

	/**
	 * Lista as reservas do Campus conforme filtros
	 * 
	 * @param data
	 * @param horaInicial
	 * @param horaFinal
	 * @param categoria
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public static List<Reserva> pesquisaReservas(Date data, Date horaInicial, Date horaFinal, CategoriaItemReserva categoria, ItemReserva item)
			throws Exception {
		Transaction trans = new Transaction();

		try {
			trans.begin();
			ReservaDAO dao = new ReservaDAO(trans);

			List<Reserva> lista = dao.pesquisaReserva(Config.getInstance().getCampus(), data, horaInicial, horaFinal, categoria, item);

			if (lista != null && lista.size() > 0) {
				for (Reserva r : lista) {
					// Hibernate.initialize(r.getIdItemReserva());
					Hibernate.initialize(r.getIdItemReserva().getIdCategoria());
					Hibernate.initialize(r.getIdTransacao());
				}
			}

			return lista;

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			trans.close();
		}
	}

	public static List<Period> getAllPeriods() throws Exception {
		Transaction trans = new Transaction();

		try {
			trans.begin();
			PeriodDAO dao = new PeriodDAO(trans);

			List<Period> lista = dao.getAll(Config.getInstance().getCampus());
			return lista;

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			trans.close();
		}
	}

	public static List<Reserva> pesquisaReservas(Date data, TipoReserva tipoReserva) throws Exception {
		Transaction trans = new Transaction();

		try {
			trans.begin();
			ReservaDAO dao = new ReservaDAO(trans);

			List<Reserva> lista = dao.pesquisaReserva(Config.getInstance().getCampus(), data, tipoReserva);

			if (lista != null && lista.size() > 0) {
				for (Reserva r : lista) {
					// Hibernate.initialize(r.getIdItemReserva());
					Hibernate.initialize(r.getIdItemReserva().getIdCategoria());
					Hibernate.initialize(r.getIdTransacao());
				}
			}

			return lista;

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			trans.close();
		}
	}

	/**
	 * Com base em uma lista de itens, confere se há disponibilidade na data
	 * solicitada e na recorrência solicitada.
	 * 
	 * @param data
	 * @param horaInicio
	 * @param horaFim
	 * @param dataLimite
	 * @param tipoRecorrencia
	 * @param listaItemReserva
	 * @return
	 * @throws Exception
	 */
	public static List<ItemReserva> removeItensNaoDisponiveisParaReservaRecorrente(Date data, Date horaInicio, Date horaFim,
			RepeticaoReservaEnum tipoRecorrencia, Date dataLimite, List<ItemReserva> listaItemReserva) throws Exception {

		if (!tipoRecorrencia.equals(RepeticaoReservaEnum.SEM_REPETICAO)) {
			// Somente se o tipo de recorrência for diferente de SEM_REPETICAO
			List<ItemReserva> listaDeItensARemover = new ArrayList<ItemReserva>();

			Calendar calendarLimite = Calendar.getInstance();
			calendarLimite.setTime(dataLimite);

			Calendar calendarAtual = Calendar.getInstance();
			calendarAtual.setTime(data);

			// Zera hora, minuto, segundo e milissegundo para
			// garantir que o teste do While seja true até o último dia
			calendarAtual.set(Calendar.HOUR_OF_DAY, 0);
			calendarAtual.set(Calendar.MINUTE, 0);
			calendarAtual.set(Calendar.SECOND, 0);
			calendarAtual.set(Calendar.MILLISECOND, 0);
			calendarLimite.set(Calendar.HOUR_OF_DAY, 0);
			calendarLimite.set(Calendar.MINUTE, 0);
			calendarLimite.set(Calendar.SECOND, 0);
			calendarLimite.set(Calendar.MILLISECOND, 0);

			int diaDaSemana = calendarAtual.get(Calendar.DAY_OF_WEEK);

			while (true) {
				long milissecondsCalendarAtual = calendarAtual.getTimeInMillis();
				long milissecondsCalendarLimite = calendarLimite.getTimeInMillis();

				if (milissecondsCalendarAtual > milissecondsCalendarLimite) {
					break;
				}

				// System.out.println("---");
				// System.out.println("Conferindo: " + calendarAtual.getTime() +
				// " <= " + dataLimite);

				int diaDaSemanaAtual = calendarAtual.get(Calendar.DAY_OF_WEEK);

				// System.out.println("Dia da semana: " + diaDaSemanaAtual +
				// " em relação a " + diaDaSemana);

				if (tipoRecorrencia.equals(RepeticaoReservaEnum.SEMANAL)) {

					if (diaDaSemanaAtual != diaDaSemana) {
						// Define novo dia e continua o laço
						calendarAtual.add(Calendar.DAY_OF_MONTH, 1);
						continue;
					}
				}

				for (ItemReserva i : listaItemReserva) {
					if (listaDeItensARemover.contains(i)) {
						continue;
					}

					Reserva r = new Reserva();
					r.setData(calendarAtual.getTime());
					r.setHoraFim(horaFim);
					r.setHoraInicio(horaInicio);
					r.setIdCampus(Config.getInstance().getCampus());
					r.setIdItemReserva(i);
					r.setIdPessoa(Config.getInstance().getPessoaLogin());

					// System.out.println("Conferindo data: " +
					// calendarAtual.getTime() + " item: " + i.getNome());

					if (ReservaService.existeConcorrente(r)) {
						listaDeItensARemover.add(i);
						// System.out.println("--> Existe concorrência!");
					}
				}

				calendarAtual.add(Calendar.DAY_OF_MONTH, 1);
			}

			// Cria uma lista de retorno
			List<ItemReserva> listaDeItensARetornar = new ArrayList<ItemReserva>();

			for (ItemReserva itemReserva : listaItemReserva) {
				if (!listaDeItensARemover.contains(itemReserva)) {
					listaDeItensARetornar.add(itemReserva);
				}
			}

			return listaDeItensARetornar;
		} else {
			// Se não for recorrente, retorna a própria lista de itens
			return listaItemReserva;
		}
	}

	/**
	 * Verifica no banco de dados se existe mais que uma reserva para a mesma
	 * transação, caracterizando reserva recorrente. Se não houver, retorna
	 * null. Se houver, retorna a data final das reservas recorrentes.
	 * 
	 * @param r
	 * @return
	 */
	public static Date verificaReservaRecorrente(Reserva r) {
		Transaction trans = new Transaction();

		try {
			trans.begin();
			ReservaDAO dao = new ReservaDAO(trans);

			List<Reserva> lista = dao.listaReservaPorTransacao(Config.getInstance().getCampus(), r.getIdTransacao().getIdTransacao());

			if (lista != null && lista.size() > 0) {
				return lista.get(lista.size() - 1).getData();
			}

			return null;

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			trans.close();
		}

	}

	/**
	 * Apaga todas as reservas da transação informada.
	 * 
	 * @param idTransacao
	 */
	public static void removerReservasPorTransacao(Integer idTransacao) {
		Transaction trans = new Transaction();

		try {
			trans.begin();
			ReservaDAO dao = new ReservaDAO(trans);
			dao.removeByTransacao(Config.getInstance().getCampus(), idTransacao);
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			trans.close();
		}
	}

	public static List<ReservaVO> listToVO(List<Reserva> list) {
		List<ReservaVO> listVO = new ArrayList<ReservaVO>();

		for (Reserva reserva : list) {
			ReservaVO vo = new ReservaVO();
			vo.setExcluir(false);
			vo.setCampus(reserva.getIdCampus());
			vo.setDataReserva(DateUtils.format(reserva.getData(), "dd/MM/yyyy"));
			vo.setHoraReserva(DateUtils.format(reserva.getHoraInicio(), "HH:mm") + "-" + DateUtils.format(reserva.getHoraFim(), "HH:mm"));
			vo.setIdReserva(reserva.getIdReserva());
			vo.setMotivoReserva(reserva.getMotivo());
			vo.setNomeItemReserva(reserva.getIdItemReserva().getNome());
			vo.setUsuarioReserva(reserva.getIdUsuario().getNomeCompleto());
			listVO.add(vo);
		}

		return listVO;
	}

	/**
	 * Busca todas as reservas de uma transação em formato VO.
	 * 
	 * @param campus
	 * @param idTransacao
	 * @return
	 */
	public static List<ReservaVO> listaReservaPorTransacao(Campus campus, Integer idTransacao) {
		Transaction trans = new Transaction();

		try {
			trans.begin();
			ReservaDAO dao = new ReservaDAO(trans);
			List<Reserva> list = dao.listaReservaPorTransacao(Config.getInstance().getCampus(), idTransacao);

			for (Reserva r : list) {
				Hibernate.initialize(r.getIdTransacao());
				Hibernate.initialize(r.getIdCampus());
			}

			return ReservaService.listToVO(list);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			trans.close();
		}
	}

}
