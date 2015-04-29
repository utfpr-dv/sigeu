package br.edu.utfpr.dv.sigeu.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "dateConverter", forClass = Date.class)
public class DateConverter implements Converter {

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

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
