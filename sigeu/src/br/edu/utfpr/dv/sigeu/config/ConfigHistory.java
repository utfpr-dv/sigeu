package br.edu.utfpr.dv.sigeu.config;

import java.util.ArrayList;
import java.util.List;

public class ConfigHistory {
	private List<String> history;

	public ConfigHistory() {
		history = new ArrayList<String>();
	}

	public void addHistoryEntry(String url) {
		history.add(url);
	}

	public String back() {
		String ret = null;
		int size = history.size();

		if (size > 1) {
			ret = history.get(size - 2);
		}

		return ret;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ConfigHistory:\n");

		for (String s : history) {
			sb.append("\t").append(s).append("\n");
		}

		return sb.toString();
	}

}
