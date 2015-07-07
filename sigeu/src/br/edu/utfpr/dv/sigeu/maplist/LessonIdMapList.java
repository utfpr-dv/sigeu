package br.edu.utfpr.dv.sigeu.maplist;

import java.util.List;

import br.edu.utfpr.dv.sigeu.entities.Lesson;

import com.adamiworks.utils.MapList;

public class LessonIdMapList extends MapList<Lesson> {

	public LessonIdMapList(List<Lesson> list) {
		super(list);
	}

	@Override
	protected Object getIndexValue(Lesson t) {
		return t.getId();
	}

}
