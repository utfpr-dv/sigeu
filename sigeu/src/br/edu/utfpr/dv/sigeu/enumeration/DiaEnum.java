package br.edu.utfpr.dv.sigeu.enumeration;

import java.util.Calendar;

public enum DiaEnum {
	SEGUNDA("100000", "Segunda-Feira", Calendar.MONDAY), TERCA("010000",
			"Terça-Feira", Calendar.TUESDAY), QUARTA("001000", "Quarta-Feira",
			Calendar.WEDNESDAY), QUINTA("000100", "Quinta-Feira",
			Calendar.THURSDAY), SEXTA("000010", "Sexta-Feira", Calendar.FRIDAY), SABADO(
			"000001", "Sábado", Calendar.SATURDAY), DOMINGO("000000",
			"Domingo", Calendar.SUNDAY);

	private String id;
	private String nome;
	private int dia;

	private DiaEnum(String id, String nome, int dia) {
		this.id = id;
		this.nome = nome;
		this.dia = dia;
	}

	public String getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public int getDia() {
		return dia;
	}

	public static DiaEnum getDiaEnumById(String id) {
		for (DiaEnum d : DiaEnum.values()) {
			if (id.equals(d.getId())) {
				return d;
			}
		}
		return null;
	}

	public static DiaEnum getDiaEnumByDia(int dia) {
		for (DiaEnum d : DiaEnum.values()) {
			if (dia == d.getDia()) {
				return d;
			}
		}
		return null;
	}
}
