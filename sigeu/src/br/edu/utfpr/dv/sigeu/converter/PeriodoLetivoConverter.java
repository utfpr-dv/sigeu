package br.edu.utfpr.dv.sigeu.converter;

import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.edu.utfpr.dv.sigeu.config.Config;
import br.edu.utfpr.dv.sigeu.entities.PeriodoLetivo;
import br.edu.utfpr.dv.sigeu.service.PeriodoLetivoService;

@FacesConverter(value = "periodoLetivoConverter", forClass = Date.class)
public class PeriodoLetivoConverter implements Converter {

	@Override
	public PeriodoLetivo getAsObject(FacesContext context, UIComponent component, String value) {
		try {
			PeriodoLetivo pl = PeriodoLetivoService.encontrePorNome(Config.getInstance().getCampus(), value);

			// if (pl == null) {
			// System.out.println("---> NÃO ENCONTROU PERIODO LETIVO <---");
			// }

			return pl;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null || !(value instanceof PeriodoLetivo)) {
			// System.out.println("---> PERIODO LETIVO NULL OU VAZIO <---");
			return "";
		}

		String periodoLetivo = ((PeriodoLetivo) value).getNome();

		// if (periodoLetivo == null) {
		// System.out.println("---> NÃO ENCONTROU PERIODO LETIVO <---");
		// }

		return periodoLetivo;
	}

}
