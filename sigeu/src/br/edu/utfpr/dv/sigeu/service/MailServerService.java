package br.edu.utfpr.dv.sigeu.service;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.adamiworks.utils.crypto.CryptoMode;
import com.adamiworks.utils.crypto.CryptoUtils;
import com.adamiworks.utils.mailsender.MailSender;
import com.adamiworks.utils.mailsender.MailSenderMessage;

import br.edu.utfpr.dv.sigeu.dao.MailServerDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.MailServer;
import br.edu.utfpr.dv.sigeu.util.MensagemEmail;

@Stateless
public class MailServerService {

    private static final byte[] MAIL_KEY_PASSWORD = { -112, 78, -12, 45, -13, 51, -84, 8 };

    @EJB
    private MailServerDAO dao;

    /**
     * Cria nova
     * 
     * @param cat
     */
    public void criar(MailServer cat) {
	dao.criar(cat);
    }

    /**
     * Altera uma existente
     * 
     * @param cat
     */
    public void alterar(MailServer cat) {
	dao.alterar(cat);
    }

    public void remover(MailServer item) throws Exception {
	dao.remover(item);
    }

    public MailServer encontrePorCampus(Campus campus) throws Exception {
	return dao.encontrePorCampus(campus);
    }

    @Asynchronous
    public void enviaMensagens(MensagemEmail mensagem) {
	System.out.println("--> ThreadEnviaMensagemEmail: INICIO");
	CryptoUtils cu;
	try {
	    cu = new CryptoUtils(CryptoMode.DES, MAIL_KEY_PASSWORD);

	    MailServer server = encontrePorCampus(mensagem.getCampus());
	    String password = cu.decrypt(server.getPassword());

	    MailSender sender = new MailSender(server.getAuthenticationRequired(), server.getHost(), server.getPort(), server.getSsl(), server.getStarttls(),
		    server.getPlainTextOverTls(), server.getFromEmail(), server.getUserName(), password);

	    for (MailSenderMessage message : mensagem.getMensagens()) {
		sender.addMessage(message);
	    }

	    sender.sendMessages();
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    System.out.println("--> ThreadEnviaMensagemEmail: FIM");
	}
    }
}