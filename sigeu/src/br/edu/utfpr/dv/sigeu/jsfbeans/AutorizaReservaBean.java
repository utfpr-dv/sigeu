package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.entities.Reserva;
import br.edu.utfpr.dv.sigeu.enumeration.StatusReserva;
import br.edu.utfpr.dv.sigeu.exception.ExisteReservaConcorrenteException;
import br.edu.utfpr.dv.sigeu.service.EmailService;
import br.edu.utfpr.dv.sigeu.service.ReservaService;
import br.edu.utfpr.dv.sigeu.vo.ReservaVO;

import com.adamiworks.utils.DateTimeUtils;

@Named
@ViewScoped
public class AutorizaReservaBean extends JavaBean {
	@Inject
	private LoginBean loginBean;

	private static final long serialVersionUID = 3379394482374794722L;

	private List<ReservaVO> listaReservaVO;
	private Pessoa autorizador;

	@PostConstruct
	public void init() {
		this.autorizador = loginBean.getPessoaLogin();
		this.atualizaLista();
	}

	/**
	 * Busca as reservas pendentes do banco de dados
	 */
	public void atualizaLista() {
		this.listaReservaVO = ReservaService.listaReservasPendentes(
				loginBean.getCampus(), autorizador);
	}

	/**
	 * Grava autorizações
	 */
	public void gravaAutorizacoes() {
		List<Reserva> listaReservasAutorizadas = new ArrayList<Reserva>();
		List<Reserva> listaReservasCanceladas = new ArrayList<Reserva>();

		for (ReservaVO vo : listaReservaVO) {
			Reserva r = ReservaService.encontrePorId(vo.getIdReserva());

			if (vo.isAutorizar()) {
				r.setStatus(StatusReserva.EFETIVADA.getStatus());
				r.setIdAutorizador(autorizador);
				listaReservasAutorizadas.add(r);
			} else if (vo.isExcluir()) {
				r.setStatus(StatusReserva.CANCELADA.getStatus());
				listaReservasCanceladas.add(r);
			}
		}

		if (listaReservasCanceladas.size() > 0
				|| listaReservasAutorizadas.size() > 0) {

			try {
				if (listaReservasAutorizadas.size() > 0) {
					ReservaService
							.alterar(loginBean.getCampus(),
									loginBean.getPessoaLogin(),
									listaReservasAutorizadas,
									"Autorização de reservas");
				}

				if (listaReservasCanceladas.size() > 0) {
					ReservaService
							.alterar(loginBean.getCampus(),
									loginBean.getPessoaLogin(),
									listaReservasCanceladas,
									"Cancelamento de reservas");
				}

				addInfoMessage("Autorização",
						"Todas as autorizações e cancelamentos foram gravadas com sucesso!");
			} catch (ExisteReservaConcorrenteException e) {
				addErrorMessage("Autorização",
						"Gravação de autorizações não permitida.");
				addErrorMessage("Autorização", e.getMessage());
			} catch (Exception e) {
				addErrorMessage("Autorização", e.getMessage());
			}
		} else {
			addWarnMessage("Autorização", "Nada a autorizar ou a cancelar.");
		}

		// Se chegou até aqui, manda o e-mail de confirmação
		try {
			for (Reserva r : listaReservasAutorizadas) {
				String emails[] = new String[] { "", "", "" };
				emails[0] = loginBean.getPessoaLogin().getEmail();
				emails[1] = r.getIdUsuario().getEmail();
				emails[2] = r.getIdAutorizador().getEmail();
				EmailService.enviaEmailConfirmacao(loginBean.getCampus(), r,
						emails);
			}

		} catch (Exception e) {
			addErrorMessage("Email", "Erro ao mandar e-mails de confirmação.");
			addErrorMessage("Email", e.getMessage());
		}

		// Se chegou até aqui, manda o e-mail de cancelamento das reservas
		// canceladas
		try {
			for (Reserva r : listaReservasCanceladas) {
				String emails[] = new String[] { "", "", "" };
				emails[0] = loginBean.getPessoaLogin().getEmail();
				emails[1] = r.getIdUsuario().getEmail();
				emails[2] = r.getIdPessoa().getEmail();
				EmailService.enviaEmailCancelamento(r.getIdCampus(), r, emails,
						loginBean.getPessoaLogin(),
						"Entre em contato para mais informações.");
			}
		} catch (Exception e) {
			addErrorMessage("Email", "Erro ao mandar e-mails de Cancelamento.");
			addErrorMessage("Email", e.getMessage());
		}

		atualizaLista();
	}

	/**
	 * Cancela todas as autorizações marcadas
	 */
	public void cancelaAutorizacoes() {
		for (ReservaVO vo : listaReservaVO) {
			vo.setAutorizar(false);
			vo.setExcluir(false);
		}
	}

	/**
	 * Chamado durante o evento de clique sobre o check "Autorizar"
	 * 
	 * @param item
	 */
	public void marcaParaAutorizar(ReservaVO item) {
		// Marca para não excluir quando está autorizando
		item.setExcluir(false);

		Calendar itemStart = DateTimeUtils.getCalendarFromDates(item.getReserva()
				.getData(), item.getReserva().getHoraInicio());
		Calendar itemEnd = DateTimeUtils.getCalendarFromDates(item.getReserva()
				.getData(), item.getReserva().getHoraFim());

		for (ReservaVO vo : listaReservaVO) {
			if (vo.getIdReserva() != item.getIdReserva()
					&& vo.getReserva().getIdItemReserva().getIdItemReserva() == item
							.getReserva().getIdItemReserva().getIdItemReserva()) {
				// Verifica todas as reservas conflitantes e marca para exclusão
				Calendar voStart = DateTimeUtils.getCalendarFromDates(vo
						.getReserva().getData(), vo.getReserva()
						.getHoraInicio());
				Calendar voEnd = DateTimeUtils.getCalendarFromDates(vo.getReserva()
						.getData(), vo.getReserva().getHoraFim());

				boolean conflita = DateTimeUtils.conflicts(itemStart, itemEnd,
						voStart, voEnd);

				if (conflita) {
					vo.setExcluir(true);
					vo.setAutorizar(false);
				}

			}
		}
	}

	public void marcaParaExcluir(ReservaVO item) {
		item.setAutorizar(false);
	}

	public List<ReservaVO> getListaReservaVO() {
		return listaReservaVO;
	}

	public void setListaReservaVO(List<ReservaVO> listaReservaVO) {
		this.listaReservaVO = listaReservaVO;
	}

	public Pessoa getAutorizador() {
		return autorizador;
	}

	public void setAutorizador(Pessoa autorizador) {
		this.autorizador = autorizador;
	}

}
