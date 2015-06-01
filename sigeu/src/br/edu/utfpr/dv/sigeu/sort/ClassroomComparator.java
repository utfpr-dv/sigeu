package br.edu.utfpr.dv.sigeu.sort;

import java.util.Comparator;

import br.edu.utfpr.dv.sigeu.entities.Classroom;

/**
 * Comparador de datas para ordenação em listas
 * 
 * @author Tiago
 *
 */
public class ClassroomComparator implements Comparator<Classroom> {

	@Override
	public int compare(Classroom c1, Classroom c2) {
		return c1.getName().compareTo(c2.getName());
	}

}
