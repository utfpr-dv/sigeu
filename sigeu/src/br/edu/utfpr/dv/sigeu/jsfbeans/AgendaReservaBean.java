package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.edu.utfpr.dv.sigeu.entities.CategoriaItemReserva;
import br.edu.utfpr.dv.sigeu.entities.ItemReserva;
import br.edu.utfpr.dv.sigeu.entities.Period;
import br.edu.utfpr.dv.sigeu.entities.Reserva;
import br.edu.utfpr.dv.sigeu.enumeration.DiaEnum;
import br.edu.utfpr.dv.sigeu.exception.DestinatarioInexistenteException;
import br.edu.utfpr.dv.sigeu.service.ItemReservaService;
import br.edu.utfpr.dv.sigeu.service.ReservaService;
import br.edu.utfpr.dv.sigeu.util.MensagemEmail;
import br.edu.utfpr.dv.sigeu.vo.PeriodoReservaVO;

import com.adamiworks.utils.DateUtils;
import com.adamiworks.utils.StringUtils;

@ManagedBean(name = "agendaReservaBean")
@ViewScoped
public class AgendaReservaBean extends JavaBean {

	private static final long serialVersionUID = -2936780347819989712L;

	private String campoItem;
	private Date data;

	private ItemReserva itemReserva;
	private List<Reserva> listaReserva;
	private List<ItemReserva> listaItemReserva;
	private List<Period> listaPeriod;
	private List<PeriodoReservaVO> listaPeriodoReservaVO;

	private SimpleDateFormat sdfHora;
	private PeriodoReservaVO horarioVO;

	public AgendaReservaBean() {
		this.carrega();
	}

	/**
	 * Método interno para carregar os
	 */
	private void carrega() {
		try {
			listaPeriod = ReservaService.getAllPeriods();
			data = Calendar.getInstance().getTime();
			sdfHora = new SimpleDateFormat("HH:mm");

			horarioVO = new PeriodoReservaVO();

			for (Period period : listaPeriod) {
				StringBuilder h = new StringBuilder();
				h.append(period.getShortname().length() > 2 ? period.getShortname().trim() : StringUtils.padLeft(period.getShortname(), 3, "0"));
				h.append(" \n");
				h.append(sdfHora.format(period.getStarttime()));
				h.append(" / \n");
				h.append(sdfHora.format(period.getEndtime()));
				horarioVO.setHorario(period.getOrdem(), h.toString());
			}
		} catch (Exception e) {
			addErrorMessage("Iniciar", "Erro ao carregar tela!");
			e.printStackTrace();
		}
	}

	/**
	 * Lista Itens para o método autocompletar
	 * 
	 * @param query
	 * @return
	 */
	public List<String> selecionaItem(String query) {
		List<String> list = new ArrayList<String>();
		listaItemReserva = null;

		try {
			listaItemReserva = ItemReservaService.pesquisar(query, true);

			if (listaItemReserva != null && listaItemReserva.size() > 0) {
				for (ItemReserva i : listaItemReserva) {
					list.add(i.getNome());
				}
			} else {
				this.addInfoMessage("Selecionar", "Nenhum item de reserva encontrado.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			this.addErrorMessage("Selecionar", "Erro na pesquisa de Itens.");
			return list;
		}

		return list;
	}

	/**
	 * Seleciona o objeto de controle Item
	 */
	public void defineItem() {
		itemReserva = null;

		for (ItemReserva i : listaItemReserva) {
			if (campoItem.equals(i.getNome())) {
				itemReserva = i;
				break;
			}
		}

		listaItemReserva = null;
		campoItem = itemReserva.getNome();
	}

	/**
	 * Pesquisa todas as reservas de um determinado dia
	 */
	public void pesquisa() {
		if (data == null) {
			this.addWarnMessage("Data", "Data inválida!");
		} else {
			if (campoItem == null || campoItem.trim().length() == 0) {
				itemReserva = null;
			}
			// if (itemReserva == null) {
			// this.addWarnMessage("Item",
			// "Item de reserva não pode estar vazio!");
			// } else {

			try {
				CategoriaItemReserva categoria = null;
				if (itemReserva != null) {
					categoria = itemReserva.getIdCategoria();
				}

				listaReserva = ReservaService.pesquisaReservasDoDia(data, categoria, itemReserva);

				if (listaReserva.size() > 0) {
					reservaParaAgenda();
				} else {
					addInfoMessage("Reserva", "Nenhuma reserva encontrada!");
				}

			} catch (Exception e) {
				this.addErrorMessage("Reserva", "Erro ao pesquisar reservas!");
				e.printStackTrace();
			}
			// }
		}
	}

	private void reservaParaAgenda() {
		listaPeriodoReservaVO = new ArrayList<PeriodoReservaVO>();

		System.out.println("---> Reservas: " + listaReserva.size());

		for (Reserva r : listaReserva) {
			PeriodoReservaVO vo = new PeriodoReservaVO();
			Calendar horaInicio = DateUtils.getCalendarMergingDates(r.getData(), r.getHoraInicio());
			Calendar horaFim = DateUtils.getCalendarMergingDates(r.getData(), r.getHoraFim());

			boolean found = false;
			for (PeriodoReservaVO prVO : listaPeriodoReservaVO) {
				if (r.getIdItemReserva().getRotulo().equals(prVO.getNomeItemReserva())) {
					vo = prVO;
					found = true;
					break;
				}
			}

			DiaEnum dia = DiaEnum.getDiaEnumByDia(horaInicio.get(Calendar.DAY_OF_WEEK));
			vo.setNomeItemReserva(r.getIdItemReserva().getRotulo());
			vo.setData(r.getData());
			vo.setDiaDaSemana(dia.getNome());

			// Agora altera aqueles que têm horário
			for (Period p : listaPeriod) {
				StringBuilder motivo = new StringBuilder();
				motivo.append(r.getIdUsuario().getNomeCompleto());
				motivo.append(" \n");
				motivo.append(r.getMotivo());
				Calendar perInicio = DateUtils.getCalendarMergingDates(r.getData(), p.getStarttime());
				Calendar perFim = DateUtils.getCalendarMergingDates(r.getData(), p.getEndtime());

				boolean conflicts = DateUtils.conflicts(horaInicio, horaFim, perInicio, perFim);

				if (conflicts) {
					vo.setCor(p.getOrdem(), r.getCor());
					vo.setRotulo(p.getOrdem(), "Reservado");
					vo.setMotivo(p.getOrdem(), motivo.toString());
				} else {
					if (vo.getRotulo(p.getOrdem()) == null) {
						vo.setCor(p.getOrdem(), "#FFFFFF");
						vo.setRotulo(p.getOrdem(), "Livre");
						vo.setMotivo(p.getOrdem(), "Horário Livre! Clique para fazer a reserva.");
					}
				}
			}

			if (!found) {
				listaPeriodoReservaVO.add(vo);
			}
		}
		System.out.println("---> Recursos: " + listaPeriodoReservaVO.size());
	}

	/**
	 * Método de teste para envio de e-mails
	 */
	@Deprecated
	public void sendMail() {
		MensagemEmail email = new MensagemEmail();

		String msg = "<html><body><br/><h2>Confirmação de Reservas<h2><table><tr><td><b>DATA<b><td><td><b>DIA DA SEMANA<b><td><td><b>RECURSO<b><td><td><b>HORÁRIO<b><td><td><b>USUÁRIO<b><td><td><b>TIPO<b><td><td><b>MOTIVO<b><td><tr><tr><td><b>17/04/2015<b><td><td>Sexta-Feira<td><td>Sala de Aula<td><td>08:00hs - 09:00hs<td><td>RENATA DA SILVA DESSBESEL<td><td>Aula de Reposição<td><td>Aula de reposição de matemática (indução de inteiros, cálculo diferencial integral entre outros). (TESTE)<td><tr><tr><td><b>24/04/2015<b><td><td>Sexta-Feira<td><td>Sala de Aula<td><td>08:00hs - 09:00hs<td><td>RENATA DA SILVA DESSBESEL<td><td>Aula de Reposição<td><td>Aula de reposição de matemática (indução de inteiros, cálculo diferencial integral entre outros). (TESTE)<td><tr><tr><td><b>01/05/2015<b><td><td>Sexta-Feira<td><td>Sala de Aula<td><td>08:00hs - 09:00hs<td><td>RENATA DA SILVA DESSBESEL<td><td>Aula de Reposição<td><td>Aula de reposição de matemática (indução de inteiros, cálculo diferencial integral entre outros). (TESTE)<td><tr><tr><td><b>08/05/2015<b><td><td>Sexta-Feira<td><td>Sala de Aula<td><td>08:00hs - 09:00hs<td><td>RENATA DA SILVA DESSBESEL<td><td>Aula de Reposição<td><td>Aula de reposição de matemática (indução de inteiros, cálculo diferencial integral entre outros). (TESTE)<td><tr><tr><td><b>15/05/2015<b><td><td>Sexta-Feira<td><td>Sala de Aula<td><td>08:00hs - 09:00hs<td><td>RENATA DA SILVA DESSBESEL<td><td>Aula de Reposição<td><td>Aula de reposição de matemática (indução de inteiros, cálculo diferencial integral entre outros). (TESTE)<td><tr><tr><td><b>22/05/2015<b><td><td>Sexta-Feira<td><td>Sala de Aula<td><td>08:00hs - 09:00hs<td><td>RENATA DA SILVA DESSBESEL<td><td>Aula de Reposição<td><td>Aula de reposição de matemática (indução de inteiros, cálculo diferencial integral entre outros). (TESTE)<td><tr><tr><td><b>29/05/2015<b><td><td>Sexta-Feira<td><td>Sala de Aula<td><td>08:00hs - 09:00hs<td><td>RENATA DA SILVA DESSBESEL<td><td>Aula de Reposição<td><td>Aula de reposição de matemática (indução de inteiros, cálculo diferencial integral entre outros). (TESTE)<td><tr><tr><td><b>05/06/2015<b><td><td>Sexta-Feira<td><td>Sala de Aula<td><td>08:00hs - 09:00hs<td><td>RENATA DA SILVA DESSBESEL<td><td>Aula de Reposição<td><td>Aula de reposição de matemática (indução de inteiros, cálculo diferencial integral entre outros). (TESTE)<td><tr><tr><td><b>12/06/2015<b><td><td>Sexta-Feira<td><td>Sala de Aula<td><td>08:00hs - 09:00hs<td><td>RENATA DA SILVA DESSBESEL<td><td>Aula de Reposição<td><td>Aula de reposição de matemática (indução de inteiros, cálculo diferencial integral entre outros). (TESTE)<td><tr><tr><td><b>19/06/2015<b><td><td>Sexta-Feira<td><td>Sala de Aula<td><td>08:00hs - 09:00hs<td><td>RENATA DA SILVA DESSBESEL<td><td>Aula de Reposição<td><td>Aula de reposição de matemática (indução de inteiros, cálculo diferencial integral entre outros). (TESTE)<td><tr><tr><td><b>26/06/2015<b><td><td>Sexta-Feira<td><td>Sala de Aula<td><td>08:00hs - 09:00hs<td><td>RENATA DA SILVA DESSBESEL<td><td>Aula de Reposição<td><td>Aula de reposição de matemática (indução de inteiros, cálculo diferencial integral entre outros). (TESTE)<td><tr><table><br/><br/><h3>SIGEU - Sistema Integrado de Gestão Universitária<h3><body></html>";

		try {
			email.criaMensagemHtml("tiagoadami@utfpr.edu.br", "derdi-dv@utfpr.edu.br", "Texto Simples", msg);
			email.enviaMensagens();
		} catch (DestinatarioInexistenteException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Reserva> getListaReserva() {
		return listaReserva;
	}

	public void setListaReserva(List<Reserva> listaReserva) {
		this.listaReserva = listaReserva;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getCampoItem() {
		return campoItem;
	}

	public void setCampoItem(String campoItem) {
		this.campoItem = campoItem;
	}

	public ItemReserva getItemReserva() {
		return itemReserva;
	}

	public void setItemReserva(ItemReserva itemReserva) {
		this.itemReserva = itemReserva;
	}

	public List<ItemReserva> getListaItemReserva() {
		return listaItemReserva;
	}

	public void setListaItemReserva(List<ItemReserva> listaItemReserva) {
		this.listaItemReserva = listaItemReserva;
	}

	public List<Period> getListaPeriod() {
		return listaPeriod;
	}

	public void setListaPeriod(List<Period> listaPeriod) {
		this.listaPeriod = listaPeriod;
	}

	public List<PeriodoReservaVO> getListaPeriodoReservaVO() {
		return listaPeriodoReservaVO;
	}

	public void setListaPeriodoReservaVO(List<PeriodoReservaVO> listaPeriodoReservaVO) {
		this.listaPeriodoReservaVO = listaPeriodoReservaVO;
	}

	public PeriodoReservaVO getHorarioVO() {
		return horarioVO;
	}

	public void setHorarioVO(PeriodoReservaVO horarioVO) {
		this.horarioVO = horarioVO;
	}

}
