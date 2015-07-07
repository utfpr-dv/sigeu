package br.edu.utfpr.dv.sigeu.maplist;

import java.util.List;

import br.edu.utfpr.dv.sigeu.entities.Professor;

import com.adamiworks.utils.MapList;

public class ProfessorCodigoMapList extends MapList<Professor> {

	public ProfessorCodigoMapList(List<Professor> list) {
		super(list);
	}

	@Override
	protected Object getIndexValue(Professor t) {
		return t.getCodigo();
	}

}
