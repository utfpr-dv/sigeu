package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;

import com.adamiworks.utils.DateTimeUtils;
import com.adamiworks.utils.StringUtils;

import org.omnifaces.cdi.ViewScoped;

import br.edu.utfpr.dv.sigeu.entities.CategoriaItemReserva;
import br.edu.utfpr.dv.sigeu.entities.ItemReserva;
import br.edu.utfpr.dv.sigeu.entities.Period;
import br.edu.utfpr.dv.sigeu.entities.Reserva;
import br.edu.utfpr.dv.sigeu.entities.TipoReserva;
import br.edu.utfpr.dv.sigeu.enumeration.DiaEnum;
import br.edu.utfpr.dv.sigeu.service.EmailService;
import br.edu.utfpr.dv.sigeu.service.ItemReservaService;
import br.edu.utfpr.dv.sigeu.service.ReservaService;
import br.edu.utfpr.dv.sigeu.service.TipoReservaService;
import br.edu.utfpr.dv.sigeu.vo.PeriodoReservaVO;
import br.edu.utfpr.dv.sigeu.vo.ReservaVO;

@Named
@ViewScoped
public class AgendaReservaBean extends JavaBean {

    @EJB
    private TipoReservaService tipoReservaService;

    @EJB
    private ItemReservaService itemReservaService;

    @EJB
    private ReservaService reservaService;

    @EJB
    private EmailService emailService;

    @Inject
    private LoginBean loginBean;

    private static final long serialVersionUID = -2936780347819989712L;

    private String campoItem;
    private Date data;
    private Date data2;
    private Date horaInicial;
    private Date horaFinal;

    private ItemReserva itemReserva;
    private List<Reserva> listaReserva;
    private List<ItemReserva> listaItemReserva;
    private List<Period> listaPeriod;
    private List<PeriodoReservaVO> listaPeriodoReservaVO;
    private List<ReservaVO> listaReservaVO;
    private List<ReservaVO> listaReservaCancelamentoVO;
    private TipoReserva tipoReserva;
    private List<TipoReserva> listaTipoReserva;

    private SimpleDateFormat formatHora;
    private SimpleDateFormat formatData;
    private PeriodoReservaVO horarioVO;
    private String nomeUsuario;
    private String motivo;
    private int showTab = 1;
    private String motivoCancelamento;
    private Reserva reservaCancelar;

    @PostConstruct
    public void init() {
	try {
	    listaPeriod = reservaService.getAllPeriods(loginBean.getCampus());
	    data = Calendar.getInstance().getTime();
	    data2 = Calendar.getInstance().getTime();
	    formatHora = new SimpleDateFormat("HH:mm");
	    formatData = new SimpleDateFormat("dd/MM/yyyy");

	    Calendar hi = Calendar.getInstance();
	    Calendar hf = Calendar.getInstance();

	    hi.set(Calendar.HOUR_OF_DAY, 0);
	    hi.set(Calendar.MINUTE, 1);

	    hf.set(Calendar.HOUR_OF_DAY, 23);
	    hf.set(Calendar.MINUTE, 59);

	    horaInicial = hi.getTime();
	    horaFinal = hf.getTime();

	    horarioVO = new PeriodoReservaVO();

	    for (Period period : listaPeriod) {
		StringBuilder h = new StringBuilder();
		h.append(StringUtils.padLeft(StringUtils.left(period.getShortname().trim(), 3), 3, "0"));
		h.append(" \n");
		h.append(formatHora.format(period.getStarttime()));
		h.append(" - \n");
		h.append(formatHora.format(period.getEndtime()));
		horarioVO.setHorario(period.getOrdem(), h.toString());
	    }

	    listaTipoReserva = tipoReservaService.pesquisar(loginBean.getCampus(), null, true);
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
	    listaItemReserva = itemReservaService.pesquisar(loginBean.getCampus(), query, null);

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
	if (horaInicial.after(horaFinal) || horaInicial.equals(horaFinal)) {
	    this.addErrorMessage("Horário inválido", "Hora inicial deve ser menor que hora final.");
	} else {
	    if (data == null || data2 == null) {
		this.addErrorMessage("Período", "Período inválido!");
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

		    listaReservaVO = new ArrayList<ReservaVO>();
		    listaReservaCancelamentoVO = new ArrayList<ReservaVO>();

		    listaReserva = reservaService.pesquisaReservasEfetivadas(loginBean.getCampus(), data, data2,
			    horaInicial, horaFinal, tipoReserva, categoria, itemReserva, nomeUsuario, motivo, true);

		    if (listaReserva.size() > 0) {
			reservaParaAgenda();

			// Processa a lista de reserva VO
			for (Reserva reserva : listaReserva) {
			    ReservaVO vo = new ReservaVO();
			    vo.setCampus(reserva.getIdCampus());
			    vo.setDataReserva(formatData.format(reserva.getData()));
			    vo.setHoraReserva(formatHora.format(reserva.getHoraInicio()) + " a "
				    + formatHora.format(reserva.getHoraFim()));
			    vo.setMotivoReserva(reserva.getMotivo());
			    vo.setNomeItemReserva(reserva.getIdItemReserva().getNome());
			    vo.setTipoReserva(reserva.getIdTipoReserva().getDescricao());
			    vo.setUsuarioReserva(reserva.getNomeUsuario());
			    vo.setCor(reserva.getCor());
			    vo.setIdReserva(reserva.getIdReserva());

			    vo.setReserva(reserva);

			    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE");
			    String asWeek = dateFormat.format(reserva.getData());

			    vo.setDiaSemana(asWeek);

			    listaReservaVO.add(vo);
			}

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
    }

    private void reservaParaAgenda() {
	listaPeriodoReservaVO = new ArrayList<PeriodoReservaVO>();

	// System.out.println("---> Reservas: " + listaReserva.size());

	for (Reserva r : listaReserva) {
	    PeriodoReservaVO vo = new PeriodoReservaVO();
	    Calendar horaInicio = DateTimeUtils.getCalendarFromDates(r.getData(), r.getHoraInicio());
	    Calendar horaFim = DateTimeUtils.getCalendarFromDates(r.getData(), r.getHoraFim());

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
		Calendar perInicio = DateTimeUtils.getCalendarFromDates(r.getData(), p.getStarttime());
		Calendar perFim = DateTimeUtils.getCalendarFromDates(r.getData(), p.getEndtime());

		boolean conflicts = DateTimeUtils.conflicts(horaInicio, horaFim, perInicio, perFim);

		if (conflicts) {
		    vo.setCor(p.getOrdem(), r.getCor());
		    vo.setRotulo(p.getOrdem(), r.getIdTipoReserva().getDescricao());
		    vo.setMotivo(p.getOrdem(), motivo.toString());
		} else {
		    if (vo.getRotulo(p.getOrdem()) == null) {
			vo.setCor(p.getOrdem(), "#FFFFFF");
			// vo.setRotulo(p.getOrdem(), "Livre");
			vo.setRotulo(p.getOrdem(), "");
			vo.setMotivo(p.getOrdem(), "Horário Livre.");
		    }
		}
	    }

	    if (!found) {
		listaPeriodoReservaVO.add(vo);
	    }
	}
	System.out.println("---> Recursos: " + listaPeriodoReservaVO.size());
    }

    public void cancelaReserva(Reserva r) {
	listaReservaCancelamentoVO = new ArrayList<ReservaVO>();
	ReservaVO vo = new ReservaVO();
	vo.setCampus(r.getIdCampus());
	vo.setDataReserva(formatData.format(r.getData()));
	vo.setHoraReserva(formatHora.format(r.getHoraInicio()) + " a " + formatHora.format(r.getHoraFim()));
	vo.setMotivoReserva(r.getMotivo());
	vo.setNomeItemReserva(r.getIdItemReserva().getNome());
	vo.setTipoReserva(r.getIdTipoReserva().getDescricao());
	vo.setUsuarioReserva(r.getNomeUsuario());
	vo.setCor(r.getCor());
	vo.setIdReserva(r.getIdReserva());

	vo.setReserva(r);

	SimpleDateFormat dateFormat = new SimpleDateFormat("EEE");
	String asWeek = dateFormat.format(r.getData());

	vo.setDiaSemana(asWeek);

	listaReservaCancelamentoVO.add(vo);
	this.reservaCancelar = r;
	this.showTab = 2;
    }

    public void gravaCancelamentoReserva() {

	// listaReservaCancelamentoVO =
	// ReservaService.listaReservaPorTransacao(loginBean.getCampus(),
	// r.getIdTransacao().getIdTransacao());

	List<Reserva> listExcluir = new ArrayList<Reserva>();
	listExcluir.add(reservaCancelar);

	try {
	    String emails[] = emailService.getEmailFromReservas(listExcluir);

	    emailService.enviaEmailCancelamento(loginBean.getCampus(), listExcluir, emails, loginBean.getPessoaLogin(),
		    motivoCancelamento);
	} catch (Exception e) {
	    addErrorMessage("Erro", "Erro ao tentar criar e-mail de exclusão de reserva.");
	    e.printStackTrace();
	}

	try {
	    reservaService.cancelaReserva(reservaCancelar, motivoCancelamento);
	} catch (Exception e) {
	    addErrorMessage("Erro", "Erro ao tentar excluir reserva " + reservaCancelar.getIdReserva());
	    e.printStackTrace();
	}

	this.showTab = 1;
	// this.motivoCancelamento = "";

	// Refaz pesquisa
	pesquisa();

	addInfoMessage("Reserva", "Reserva cancelada com sucesso! A confirmação será enviada por e-mail em instantes.");
    }

    public void cancelaUi() {
	this.showTab = 1;
    }

    public void checkPeriodo() {
	if (this.data.compareTo(this.data2) > 0) {
	    this.data2 = this.data;
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

    public List<ReservaVO> getListaReservaVO() {
	return listaReservaVO;
    }

    public void setListaReservaVO(List<ReservaVO> listaReservaVO) {
	this.listaReservaVO = listaReservaVO;
    }

    public TipoReserva getTipoReserva() {
	return tipoReserva;
    }

    public void setTipoReserva(TipoReserva tipoReserva) {
	this.tipoReserva = tipoReserva;
    }

    public List<TipoReserva> getListaTipoReserva() {
	return listaTipoReserva;
    }

    public void setListaTipoReserva(List<TipoReserva> listaTipoReserva) {
	this.listaTipoReserva = listaTipoReserva;
    }

    public String getNomeUsuario() {
	return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
	this.nomeUsuario = nomeUsuario;
    }

    public Date getData2() {
	return data2;
    }

    public void setData2(Date data2) {
	this.data2 = data2;
    }

    public String getMotivo() {
	return motivo;
    }

    public void setMotivo(String motivo) {
	this.motivo = motivo;
    }

    public int getShowTab() {
	return showTab;
    }

    public void setShowTab(int showTab) {
	this.showTab = showTab;
    }

    public String getMotivoCancelamento() {
	return motivoCancelamento;
    }

    public void setMotivoCancelamento(String motivoCancelamento) {
	this.motivoCancelamento = motivoCancelamento;
    }

    public List<ReservaVO> getListaReservaCancelamentoVO() {
	return listaReservaCancelamentoVO;
    }

    public void setListaReservaCancelamentoVO(List<ReservaVO> listaReservaCancelamentoVO) {
	this.listaReservaCancelamentoVO = listaReservaCancelamentoVO;
    }

    public Reserva getReservaCancelar() {
	return reservaCancelar;
    }

    public void setReservaCancelar(Reserva reservaCancelar) {
	this.reservaCancelar = reservaCancelar;
    }

    public Date getHoraInicial() {
	return horaInicial;
    }

    public void setHoraInicial(Date horaInicial) {
	this.horaInicial = horaInicial;
    }

    public Date getHoraFinal() {
	return horaFinal;
    }

    public void setHoraFinal(Date horaFinal) {
	this.horaFinal = horaFinal;
    }

}
