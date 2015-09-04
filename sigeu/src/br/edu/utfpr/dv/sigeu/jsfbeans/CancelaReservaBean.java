package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;

import br.edu.utfpr.dv.sigeu.entities.Reserva;
import br.edu.utfpr.dv.sigeu.service.EmailService;
import br.edu.utfpr.dv.sigeu.service.ReservaService;
import br.edu.utfpr.dv.sigeu.vo.ReservaVO;

@Named
@ViewScoped
public class CancelaReservaBean extends JavaBean {

	@Inject
	private LoginBean loginBean;

	private static final long serialVersionUID = 6166875342336109321L;

	private List<ReservaVO> listaReservaVO;
	private String motivoCancelamento;

	@PostConstruct
	public void init() {
		HttpServletRequest req = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		String trans = null;
		try {
			trans = req.getParameter("trans");
			// System.out.println(trans);
		} catch (Exception e) {
			//
		}

		Integer idTransacao = null;

		if (trans != null) {
			try {
				idTransacao = Integer.valueOf(trans);
			} catch (Exception e) {
				// nada a fazer, apenas ignorar
			}
		}

		if (idTransacao != null && idTransacao > 0) {
			this.carregaLista(idTransacao);
		}
	}

	/**
	 * Carrega lista de reservas
	 * 
	 * @param idTransacao
	 */
	private void carregaLista(Integer idTransacao) {
		listaReservaVO = ReservaService.listaReservaPorTransacao(
				loginBean.getCampus(), idTransacao);
		// System.out.println(">>> Carregou " + listaReservaVO.size() +
		// " reservas");
	}

	/**
	 * Exclui todas as reservas marcadas
	 */
	public void excluiReservas() {
		if (listaReservaVO != null) {
			// System.out.println(">>> Excluindo " + listaReservaVO.size() +
			// " reservas");

			List<Reserva> listExcluir = new ArrayList<Reserva>();

			for (ReservaVO vo : listaReservaVO) {
				if (vo.isExcluir()) {
					try {
						listExcluir.add(ReservaService.pesquisaReservaPorId(vo
								.getIdReserva()));
					} catch (Exception e) {
						addErrorMessage(
								"Erro",
								"Erro ao tentar carregar reserva "
										+ vo.getIdReserva() + " de "
										+ vo.getDataReserva());
					}
				}
			}

			try {
				String emails[] = EmailService
						.getEmailFromReservas(listExcluir);
				EmailService.enviaEmailCancelamento(loginBean.getCampus(),
						listExcluir, emails, loginBean.getPessoaLogin(),
						motivoCancelamento);
			} catch (Exception e) {
				addErrorMessage("Erro",
						"Erro ao tentar criar e-mail de exclusão de reserva.");
				e.printStackTrace();
			}

			for (Reserva r : listExcluir) {
				try {
					ReservaService.excluir(r);
				} catch (Exception e) {
					addErrorMessage("Erro", "Erro ao tentar excluir reserva "
							+ r.getIdReserva());
					e.printStackTrace();
				}
			}

			this.fechaModal();
		}
	}

	/**
	 * Abre o modal para exibir os itens da lista
	 */
	public void abreModal() {
		Map<String, Object> options = new HashMap<String, Object>();
		Map<String, List<String>> args = new HashMap<String, List<String>>();
		options.put("modal", true);
		options.put("resizable", false);
		options.put("contentHeight", 500);
		options.put("contentWidth", 900);
		RequestContext.getCurrentInstance().openDialog("CancelaReserva",
				options, args);
	}

	/**
	 * Quando a chamada é feita de outra lista passando uma reserva como
	 * parametro
	 * 
	 * @param r
	 */
	public void excluiReserva(Reserva r) {
		this.abreModal();
		this.carregaLista(r.getIdTransacao().getIdTransacao());
	}

	/**
	 * Fecha a janela modal com a lista de reservas
	 */
	public void fechaModal() {
		RequestContext.getCurrentInstance().closeDialog("CancelaReserva");
	}

	public List<ReservaVO> getListaReservaVO() {
		return listaReservaVO;
	}

	public void setListaReservaVO(List<ReservaVO> listaReservaVO) {
		this.listaReservaVO = listaReservaVO;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getMotivoCancelamento() {
		return motivoCancelamento;
	}

	public void setMotivoCancelamento(String motivoCancelamento) {
		this.motivoCancelamento = motivoCancelamento;
	}

}
