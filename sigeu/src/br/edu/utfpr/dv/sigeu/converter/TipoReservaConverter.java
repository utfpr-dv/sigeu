package br.edu.utfpr.dv.sigeu.converter;

import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.TipoReserva;
import br.edu.utfpr.dv.sigeu.service.TipoReservaService;
import br.edu.utfpr.dv.sigeu.util.LoginFilter;

@FacesConverter(value = "tipoReservaConverter", forClass = TipoReserva.class)
public class TipoReservaConverter implements Converter {

	@Override
	public TipoReserva getAsObject(FacesContext context, UIComponent component,
			String value) {
		TipoReserva ret = null;
		try {
			Map<String, Object> sessionMap = context.getExternalContext()
					.getSessionMap();
			Campus campus = (Campus) sessionMap.get(LoginFilter.SESSION_CAMPUS);
			List<TipoReserva> list = TipoReservaService.pesquisar(campus, null,
					null);
			for (TipoReserva t : list) {
				if (t.getDescricao().equals(value)) {
					ret = t;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return value.toString();
	}

}
