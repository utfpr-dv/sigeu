package br.edu.utfpr.dv.sigeu.util;

public class ValidationUtils {

    public static String getRootCauseMsg(Exception e) {
	Throwable rootCause = e;

	while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
	    rootCause = rootCause.getCause();
	}

	String msg = rootCause.getMessage();

	return msg != null ? msg : e.toString();
    }
}