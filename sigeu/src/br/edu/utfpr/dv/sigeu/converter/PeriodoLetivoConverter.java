package br.edu.utfpr.dv.sigeu.converter;

import java.util.Date;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.PeriodoLetivo;
import br.edu.utfpr.dv.sigeu.service.PeriodoLetivoService;
import br.edu.utfpr.dv.sigeu.util.LoginFilter;

@FacesConverter(value = "periodoLetivoConverter", forClass = Date.class)
public class PeriodoLetivoConverter implements Converter {

	@Override
	public PeriodoLetivo getAsObject(FacesContext context,
			UIComponent component, String value) {
		try {
			Map<String, Object> sessionMap = context.getExternalContext()
					.getSessionMap();
			Campus campus = (Campus) sessionMap.get(LoginFilter.SESSION_CAMPUS);
			PeriodoLetivo pl = PeriodoLetivoService.encontrePorNome(campus,
					value);

			return pl;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value == null || !(value instanceof PeriodoLetivo)) {
			// System.out.println("---> PERIODO LETIVO NULL OU VAZIO <---");
			return "";
		}

		String periodoLetivo = ((PeriodoLetivo) value).getNome();

		return periodoLetivo;
	}

}
