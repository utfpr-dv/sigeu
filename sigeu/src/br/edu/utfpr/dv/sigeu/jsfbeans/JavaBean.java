package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

public abstract class JavaBean implements Serializable {
	private static final long serialVersionUID = -2L;

	/**
	 * Adiciona uma nova mensagem de erro para ser exibida na página
	 * 
	 * @param message
	 */
	public void addErrorMessage(String title, String message) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, title));
	}

	/**
	 * Adiciona uma nova mensagem de alerta para ser exibida na página
	 * 
	 * @param message
	 */
	public void addWarnMessage(String title, String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, message, title));
	}

	/**
	 * Adiciona uma nova mensagem informativa para ser exibida na página
	 * 
	 * @param message
	 */
	public void addInfoMessage(String title, String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, message, title));
	}

	/**
	 * Adiciona uma nova mensagem de erro fatal para ser exibida na página
	 * 
	 * @param message
	 */
	public void addFatalMessage(String title, String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_FATAL, message, title));
	}

	/**
	 * Redireciona para uma nova página
	 * 
	 * @param url
	 */
	public void redirect(String url) {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(url);
		} catch (IOException e) {
			e.printStackTrace();
			this.addErrorMessage("Página indisponível", "Não foi possível acessar: " + url);
		}
	}

	/**
	 * Atualiza um componente visual na página que chamou o Bean.
	 * 
	 * @param name
	 */
	public void update(String name) {
		RequestContext.getCurrentInstance().update(name);
	}

	/**
	 * Retorna apenas o código de um campo combinado "ID"-"Descrição".
	 * 
	 * @param valorCampoComposto
	 * @param regex
	 * @return
	 */
	public Integer getSplitId(String valorCampoComposto, String regex) {
		Integer ret = 0;

		if (regex == null || regex.trim().length() == 0) {
			regex = " ";
		}

		if (valorCampoComposto != null) {
			String[] values = valorCampoComposto.split(regex);

			try {

				ret = Integer.valueOf(values[0].substring(1).trim());
				return ret;
			} catch (Exception e) {
				ret = -1;
			}
		}

		return ret;
	}

	public void clearMessages() {
		Iterator<String> itIds = FacesContext.getCurrentInstance().getClientIdsWithMessages();
		while (itIds.hasNext()) {
			List<FacesMessage> messageList = FacesContext.getCurrentInstance().getMessageList(itIds.next());
			if (!messageList.isEmpty()) {
				messageList.clear();
			}
		}
	}

	protected void handleException(Throwable t) {
		addErrorMessage("Exception", t.toString());
		// Se este print ocorrer, a mensagem de erro não será exibida na tela
		// t.printStackTrace();
	}

	protected void handleException(String msg, Throwable t) {
		if (t != null) {
			addErrorMessage("Exception", msg + " [" + t.toString());
		} else {
			addErrorMessage("Exception", msg);
		}
		// Se este print ocorrer, a mensagem de erro não será exibida na tela
		// t.printStackTrace();
	}
}
