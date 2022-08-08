package br.edu.utfpr.dv.sigeu.util;

import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.adamiworks.utils.crypto.CryptoMode;
import com.adamiworks.utils.crypto.CryptoUtils;
import com.adamiworks.utils.mailsender.MailSender;
import com.adamiworks.utils.mailsender.MailSenderMessage;

import br.edu.utfpr.dv.sigeu.config.Config;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.MailServer;
import br.edu.utfpr.dv.sigeu.exception.DestinatarioInexistenteException;
import br.edu.utfpr.dv.sigeu.service.MailServerService;

public class MensagemEmail {

    private static final byte[] MAIL_KEY_PASSWORD = { -112, 78, -12, 45, -13, 51, -84, 8 };

    private List<MailSenderMessage> mensagens;

    private Campus campus;

    public MensagemEmail(Campus campus) {
	this.campus = campus;
    }

    /**
     * Cria uma mensagem texto-simples para 2 destinatários, um principal e uma
     * cópia, sem anexos
     * 
     * @param to
     * @param cc
     * @param assunto
     * @param conteudo
     * @throws DestinatarioInexistenteException
     */
    public void criaMensagemTextoSimples(String to, String cc, String assunto, String conteudo)
	    throws DestinatarioInexistenteException {
	String aTo[] = new String[] { to };
	String aCc[] = new String[] { cc };
	this.criaMensagem(aTo, aCc, null, assunto, conteudo, false, null);
    }

    /**
     * Cria uma mensagem HTML para 2 destinatários, um principal e uma cópia, sem
     * anexos
     * 
     * @param to
     * @param cc
     * @param assunto
     * @param html
     * @throws DestinatarioInexistenteException
     */
    public void criaMensagemHtml(String to, String cc, String assunto, String html)
	    throws DestinatarioInexistenteException {
	String aTo[] = new String[] { to };
	String aCc[] = new String[] { cc };
	this.criaMensagem(aTo, aCc, null, assunto, html, true, null);
    }

    /**
     * Cria uma mensagem a ser enviada. As mensagens ficarão retidas no objeto até
     * que o metodo <b>enviarMensagens</b> seja invocado.
     * 
     * @param to
     * @param cc
     * @param bcc
     * @param assunto
     * @param conteudo
     * @param conteudoHtml
     * @param pathArquivosAnexos
     * @throws DestinatarioInexistenteException
     */
    public void criaMensagem(String to[], String cc[], String bcc[], String assunto, String conteudo,
	    boolean conteudoHtml, List<String> pathArquivosAnexos) throws DestinatarioInexistenteException {
	if (to == null && cc == null && bcc == null) {
	    throw new DestinatarioInexistenteException();
	}

	if (mensagens == null) {
	    mensagens = new ArrayList<MailSenderMessage>();
	}

	MailSenderMessage mensagem = new MailSenderMessage("SIGEU UTFPR DV", campus.getMailServer().getFromEmail());

	if (to != null) {
	    for (String s : to) {
		mensagem.addTo(s);
	    }
	}

	if (cc != null) {
	    for (String s : cc) {
		mensagem.addCc(s);
	    }
	}

	if (bcc != null) {
	    for (String s : bcc) {
		mensagem.addBcc(s);
	    }
	}

	mensagem.setSubject(assunto);

	if (conteudoHtml) {
	    mensagem.setHtmlBody(conteudo);
	} else {
	    mensagem.setBody(conteudo);
	}

	if (pathArquivosAnexos != null) {
	    for (String file : pathArquivosAnexos) {
		mensagem.addAttachFile(file);
	    }
	}

	mensagens.add(mensagem);
    }

    private MailServerService getService() {
	try {
	    InitialContext initialContext = new InitialContext();
	    MailServerService mailService = (MailServerService) initialContext.lookup("java:module/MailServerService");

	    return mailService;
	} catch (NamingException e) {
	    e.printStackTrace();
	}

	return null;

    }

    /**
     * Envia todas as mensagens armazenadas no objeto
     * 
     * ATENÇÃO!!!
     * 
     * ESTE MÉTODO NÃO POSSUI IDENTIFICADOR DE ESCOPO (private, protected ou public)
     * POIS DEVE SER EXECUTADO APENAS PELA THREAD DE ENVIO DE E-MAILS.
     * 
     * @throws Exception
     */
    void enviarMensagensThread() {
	System.out.println("--> ThreadEnviaMensagemEmail: INICIO");
	CryptoUtils cu;
	try {
	    cu = new CryptoUtils(CryptoMode.DES, MAIL_KEY_PASSWORD);

	    MailServer server = getService().encontrePorCampus(campus);
	    String password = cu.decrypt(server.getPassword());

	    MailSender sender = new MailSender(server.getAuthenticationRequired(), server.getHost(), server.getPort(),
		    server.getSsl(), server.getStarttls(), server.getPlainTextOverTls(), server.getFromEmail(),
		    server.getUserName(), password);

	    for (MailSenderMessage message : mensagens) {
		sender.addMessage(message);
	    }

	    sender.sendMessages();
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    System.out.println("--> ThreadEnviaMensagemEmail: FIM");
	}
    }

    public void enviaMensagens() {
	if (!Config.getInstance().isDebugMode()) {
	    ThreadEnviaMensagemEmail thread = new ThreadEnviaMensagemEmail(this);
	    thread.start();
	} else {
	    this.enviarMensagensThread();
	}
    }

    public List<MailSenderMessage> getMensagens() {
	return mensagens;
    }

    public void setMensagens(List<MailSenderMessage> mensagens) {
	this.mensagens = mensagens;
    }

    public Campus getCampus() {
	return campus;
    }

    public void setCampus(Campus campus) {
	this.campus = campus;
    }
}
