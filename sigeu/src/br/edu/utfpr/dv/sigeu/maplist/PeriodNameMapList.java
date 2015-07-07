package br.edu.utfpr.dv.sigeu.maplist;

import java.util.List;

import br.edu.utfpr.dv.sigeu.entities.Period;

import com.adamiworks.utils.MapList;

public class PeriodNameMapList extends MapList<Period> {

	public PeriodNameMapList(List<Period> list) {
		super(list);
	}

	@Override
	protected Object getIndexValue(Period t) {
		return t.getName();
	}

}
