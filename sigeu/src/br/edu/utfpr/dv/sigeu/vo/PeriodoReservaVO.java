package br.edu.utfpr.dv.sigeu.vo;

import java.io.Serializable;
import java.util.Date;

public class PeriodoReservaVO implements Serializable {
	private static final long serialVersionUID = -6545997839475340725L;

	private Integer idReserva;
	private Date data;
	private String diaDaSemana;
	//
	private String nomeItemReserva;
	//
	private String rotulo01;
	private String rotulo02;
	private String rotulo03;
	private String rotulo04;
	private String rotulo05;
	private String rotulo06;
	private String rotulo07;
	private String rotulo08;
	private String rotulo09;
	private String rotulo10;
	private String rotulo11;
	private String rotulo12;
	private String rotulo13;
	private String rotulo14;
	private String rotulo15;
	private String rotulo16;
	private String rotulo17;
	private String cor01;
	private String cor02;
	private String cor03;
	private String cor04;
	private String cor05;
	private String cor06;
	private String cor07;
	private String cor08;
	private String cor09;
	private String cor10;
	private String cor11;
	private String cor12;
	private String cor13;
	private String cor14;
	private String cor15;
	private String cor16;
	private String cor17;
	private String horario01;
	private String horario02;
	private String horario03;
	private String horario04;
	private String horario05;
	private String horario06;
	private String horario07;
	private String horario08;
	private String horario09;
	private String horario10;
	private String horario11;
	private String horario12;
	private String horario13;
	private String horario14;
	private String horario15;
	private String horario16;
	private String horario17;
	private String motivo01;
	private String motivo02;
	private String motivo03;
	private String motivo04;
	private String motivo05;
	private String motivo06;
	private String motivo07;
	private String motivo08;
	private String motivo09;
	private String motivo10;
	private String motivo11;
	private String motivo12;
	private String motivo13;
	private String motivo14;
	private String motivo15;
	private String motivo16;
	private String motivo17;

	public void setRotulo(int n, String rotulo) {
		switch (n) {
		case 1:
			this.rotulo01 = rotulo;
			break;

		case 2:
			this.rotulo02 = rotulo;
			break;
		case 3:
			this.rotulo03 = rotulo;
			break;
		case 4:
			this.rotulo04 = rotulo;
			break;
		case 5:
			this.rotulo05 = rotulo;
			break;
		case 6:
			this.rotulo06 = rotulo;
			break;
		case 7:
			this.rotulo07 = rotulo;
			break;
		case 8:
			this.rotulo08 = rotulo;
			break;
		case 9:
			this.rotulo09 = rotulo;
			break;
		case 10:
			this.rotulo10 = rotulo;
			break;
		case 11:
			this.rotulo11 = rotulo;
			break;
		case 12:
			this.rotulo12 = rotulo;
			break;
		case 13:
			this.rotulo13 = rotulo;
			break;
		case 14:
			this.rotulo14 = rotulo;
			break;
		case 15:
			this.rotulo15 = rotulo;
			break;
		case 16:
			this.rotulo16 = rotulo;
			break;
		case 17:
			this.rotulo17 = rotulo;
			break;

		default:
			break;
		}
	}

	public String getRotulo(int n) {
		switch (n) {
		case 1:
			return this.rotulo01;
		case 2:
			return this.rotulo02;
		case 3:
			return this.rotulo03;
		case 4:
			return this.rotulo04;
		case 5:
			return this.rotulo05;
		case 6:
			return this.rotulo06;
		case 7:
			return this.rotulo07;
		case 8:
			return this.rotulo08;
		case 9:
			return this.rotulo09;
		case 10:
			return this.rotulo10;
		case 11:
			return this.rotulo11;
		case 12:
			return this.rotulo12;
		case 13:
			return this.rotulo13;
		case 14:
			return this.rotulo14;
		case 15:
			return this.rotulo15;
		case 16:
			return this.rotulo16;
		case 17:
			return this.rotulo17;

		default:
			break;
		}

		return null;
	}

	public void setCor(int n, String cor) {
		switch (n) {
		case 1:
			this.cor01 = cor;
			break;

		case 2:
			this.cor02 = cor;
			break;
		case 3:
			this.cor03 = cor;
			break;
		case 4:
			this.cor04 = cor;
			break;
		case 5:
			this.cor05 = cor;
			break;
		case 6:
			this.cor06 = cor;
			break;
		case 7:
			this.cor07 = cor;
			break;
		case 8:
			this.cor08 = cor;
			break;
		case 9:
			this.cor09 = cor;
			break;
		case 10:
			this.cor10 = cor;
			break;
		case 11:
			this.cor11 = cor;
			break;
		case 12:
			this.cor12 = cor;
			break;
		case 13:
			this.cor13 = cor;
			break;
		case 14:
			this.cor14 = cor;
			break;
		case 15:
			this.cor15 = cor;
			break;
		case 16:
			this.cor16 = cor;
			break;
		case 17:
			this.cor17 = cor;
			break;

		default:
			break;
		}
	}

	public void setHorario(int n, String horario) {
		switch (n) {
		case 1:
			this.horario01 = horario;
			break;

		case 2:
			this.horario02 = horario;
			break;
		case 3:
			this.horario03 = horario;
			break;
		case 4:
			this.horario04 = horario;
			break;
		case 5:
			this.horario05 = horario;
			break;
		case 6:
			this.horario06 = horario;
			break;
		case 7:
			this.horario07 = horario;
			break;
		case 8:
			this.horario08 = horario;
			break;
		case 9:
			this.horario09 = horario;
			break;
		case 10:
			this.horario10 = horario;
			break;
		case 11:
			this.horario11 = horario;
			break;
		case 12:
			this.horario12 = horario;
			break;
		case 13:
			this.horario13 = horario;
			break;
		case 14:
			this.horario14 = horario;
			break;
		case 15:
			this.horario15 = horario;
			break;
		case 16:
			this.horario16 = horario;
			break;
		case 17:
			this.horario17 = horario;
			break;

		default:
			break;
		}
	}

	public void setMotivo(int n, String motivo) {
		switch (n) {
		case 1:
			this.motivo01 = motivo;
			break;

		case 2:
			this.motivo02 = motivo;
			break;
		case 3:
			this.motivo03 = motivo;
			break;
		case 4:
			this.motivo04 = motivo;
			break;
		case 5:
			this.motivo05 = motivo;
			break;
		case 6:
			this.motivo06 = motivo;
			break;
		case 7:
			this.motivo07 = motivo;
			break;
		case 8:
			this.motivo08 = motivo;
			break;
		case 9:
			this.motivo09 = motivo;
			break;
		case 10:
			this.motivo10 = motivo;
			break;
		case 11:
			this.motivo11 = motivo;
			break;
		case 12:
			this.motivo12 = motivo;
			break;
		case 13:
			this.motivo13 = motivo;
			break;
		case 14:
			this.motivo14 = motivo;
			break;
		case 15:
			this.motivo15 = motivo;
			break;
		case 16:
			this.motivo16 = motivo;
			break;
		case 17:
			this.motivo17 = motivo;
			break;

		default:
			break;
		}
	}

	public Integer getIdReserva() {
		return idReserva;
	}

	public Date getData() {
		return data;
	}

	public String getDiaDaSemana() {
		return diaDaSemana;
	}

	public String getNomeItemReserva() {
		return nomeItemReserva;
	}

	public String getRotulo01() {
		return rotulo01;
	}

	public String getRotulo02() {
		return rotulo02;
	}

	public String getRotulo03() {
		return rotulo03;
	}

	public String getRotulo04() {
		return rotulo04;
	}

	public String getRotulo05() {
		return rotulo05;
	}

	public String getRotulo06() {
		return rotulo06;
	}

	public String getRotulo07() {
		return rotulo07;
	}

	public String getRotulo08() {
		return rotulo08;
	}

	public String getRotulo09() {
		return rotulo09;
	}

	public String getRotulo10() {
		return rotulo10;
	}

	public String getRotulo11() {
		return rotulo11;
	}

	public String getRotulo12() {
		return rotulo12;
	}

	public String getRotulo13() {
		return rotulo13;
	}

	public String getRotulo14() {
		return rotulo14;
	}

	public String getRotulo15() {
		return rotulo15;
	}

	public String getRotulo16() {
		return rotulo16;
	}

	public String getRotulo17() {
		return rotulo17;
	}

	public String getCor01() {
		return cor01;
	}

	public String getCor02() {
		return cor02;
	}

	public String getCor03() {
		return cor03;
	}

	public String getCor04() {
		return cor04;
	}

	public String getCor05() {
		return cor05;
	}

	public String getCor06() {
		return cor06;
	}

	public String getCor07() {
		return cor07;
	}

	public String getCor08() {
		return cor08;
	}

	public String getCor09() {
		return cor09;
	}

	public String getCor10() {
		return cor10;
	}

	public String getCor11() {
		return cor11;
	}

	public String getCor12() {
		return cor12;
	}

	public String getCor13() {
		return cor13;
	}

	public String getCor14() {
		return cor14;
	}

	public String getCor15() {
		return cor15;
	}

	public String getCor16() {
		return cor16;
	}

	public String getCor17() {
		return cor17;
	}

	public String getHorario01() {
		return horario01;
	}

	public String getHorario02() {
		return horario02;
	}

	public String getHorario03() {
		return horario03;
	}

	public String getHorario04() {
		return horario04;
	}

	public String getHorario05() {
		return horario05;
	}

	public String getHorario06() {
		return horario06;
	}

	public String getHorario07() {
		return horario07;
	}

	public String getHorario08() {
		return horario08;
	}

	public String getHorario09() {
		return horario09;
	}

	public String getHorario10() {
		return horario10;
	}

	public String getHorario11() {
		return horario11;
	}

	public String getHorario12() {
		return horario12;
	}

	public String getHorario13() {
		return horario13;
	}

	public String getHorario14() {
		return horario14;
	}

	public String getHorario15() {
		return horario15;
	}

	public String getHorario16() {
		return horario16;
	}

	public String getHorario17() {
		return horario17;
	}

	public void setIdReserva(Integer idReserva) {
		this.idReserva = idReserva;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setDiaDaSemana(String diaDaSemana) {
		this.diaDaSemana = diaDaSemana;
	}

	public void setNomeItemReserva(String nomeItemReserva) {
		this.nomeItemReserva = nomeItemReserva;
	}

	public String getMotivo01() {
		return motivo01;
	}

	public String getMotivo02() {
		return motivo02;
	}

	public String getMotivo03() {
		return motivo03;
	}

	public String getMotivo04() {
		return motivo04;
	}

	public String getMotivo05() {
		return motivo05;
	}

	public String getMotivo06() {
		return motivo06;
	}

	public String getMotivo07() {
		return motivo07;
	}

	public String getMotivo08() {
		return motivo08;
	}

	public String getMotivo09() {
		return motivo09;
	}

	public String getMotivo10() {
		return motivo10;
	}

	public String getMotivo11() {
		return motivo11;
	}

	public String getMotivo12() {
		return motivo12;
	}

	public String getMotivo13() {
		return motivo13;
	}

	public String getMotivo14() {
		return motivo14;
	}

	public String getMotivo15() {
		return motivo15;
	}

	public String getMotivo16() {
		return motivo16;
	}

	public String getMotivo17() {
		return motivo17;
	}

}
