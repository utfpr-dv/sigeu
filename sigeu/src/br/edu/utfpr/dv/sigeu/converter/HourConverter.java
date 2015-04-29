package br.edu.utfpr.dv.sigeu.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "hourConverter", forClass = Date.class)
public class HourConverter implements Converter {

	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

	@Override
	public Date getAsObject(FacesContext context, UIComponent component, String value) {
		Date date;
		try {
			date = new Date(sdf.parse(value).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return date;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return sdf.format((Date) value);
	}

}
