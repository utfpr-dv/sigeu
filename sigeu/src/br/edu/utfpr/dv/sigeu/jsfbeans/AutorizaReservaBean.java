package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.adamiworks.utils.DateUtils;

import br.edu.utfpr.dv.sigeu.config.Config;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.entities.Reserva;
import br.edu.utfpr.dv.sigeu.enumeration.StatusReserva;
import br.edu.utfpr.dv.sigeu.exception.ExisteReservaConcorrenteException;
import br.edu.utfpr.dv.sigeu.service.ReservaService;
import br.edu.utfpr.dv.sigeu.vo.ReservaVO;

@ManagedBean(name = "autorizaReservaBean")
@ViewScoped
public class AutorizaReservaBean extends JavaBean {

	private static final long serialVersionUID = 3379394482374794722L;

	private List<ReservaVO> listaReservaVO;
	private Pessoa autorizador;

	/**
	 * Constructor
	 */
	public AutorizaReservaBean() {
		this.autorizador = Config.getInstance().getPessoaLogin();
		this.atualizaLista();
	}

	/**
	 * Busca as reservas pendentes do banco de dados
	 */
	public void atualizaLista() {
		this.listaReservaVO = ReservaService
				.listaReservasPendentes(autorizador);
	}

	/**
	 * Grava autorizações
	 */
	public void gravaAutorizacoes() {
		List<Reserva> listReservas = new ArrayList<Reserva>();

		for (ReservaVO vo : listaReservaVO) {
			Reserva r = ReservaService.encontrePorId(vo.getIdReserva());

			if (vo.isAutorizar()) {
				r.setStatus(StatusReserva.EFETIVADA.getStatus());
				r.setIdPessoa(autorizador);
			} else {
				r.setStatus(StatusReserva.CANCELADA.getStatus());
			}

			if (vo.isAutorizar() || vo.isExcluir()) {
				listReservas.add(r);
			}
		}

		if (listReservas.size() > 0) {
			try {
				ReservaService.alterar(listReservas, "Autorização de reservas");

				addInfoMessage("Autorização",
						"Todas as autorizações e cancelamentos foram gravas com sucesso!");
			} catch (ExisteReservaConcorrenteException e) {
				addErrorMessage("Autorização",
						"Gravação de autorizações não permitida.");
				addErrorMessage("Autorização", e.getMessage());
			} catch (Exception e) {
				addErrorMessage("Autorização", e.getMessage());
			}
		} else {
			addWarnMessage("Autorização", "Nada a autorizar.");
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

		Calendar itemStart = DateUtils.getCalendarFromDates(item.getReserva()
				.getData(), item.getReserva().getHoraInicio());
		Calendar itemEnd = DateUtils.getCalendarFromDates(item.getReserva()
				.getData(), item.getReserva().getHoraFim());

		for (ReservaVO vo : listaReservaVO) {
			if (vo.getIdReserva() != item.getIdReserva()
					&& vo.getReserva().getIdItemReserva().getIdItemReserva() == item
							.getReserva().getIdItemReserva().getIdItemReserva()) {
				// Verifica todas as reservas conflitantes e marca para exclusão
				Calendar voStart = DateUtils.getCalendarFromDates(vo
						.getReserva().getData(), vo.getReserva()
						.getHoraInicio());
				Calendar voEnd = DateUtils.getCalendarFromDates(vo.getReserva()
						.getData(), vo.getReserva().getHoraFim());

				boolean conflita = DateUtils.conflicts(itemStart, itemEnd,
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
