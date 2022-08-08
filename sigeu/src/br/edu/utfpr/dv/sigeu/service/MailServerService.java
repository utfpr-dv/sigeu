package br.edu.utfpr.dv.sigeu.service;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;

import com.adamiworks.utils.crypto.CryptoMode;
import com.adamiworks.utils.crypto.CryptoUtils;
import com.adamiworks.utils.mailsender.MailSender;
import com.adamiworks.utils.mailsender.MailSenderMessage;

import br.edu.utfpr.dv.sigeu.dao.MailServerDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.MailServer;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;
import br.edu.utfpr.dv.sigeu.util.MensagemEmail;

@Stateless
public class MailServerService {

    private static final byte[] MAIL_KEY_PASSWORD = { -112, 78, -12, 45, -13, 51, -84, 8 };

    /**
     * Cria nova
     * 
     * @param cat
     */
    public void criar(MailServer cat) {
	Transaction trans = null;
	try {
	    trans = new Transaction();
	    trans.begin();

	    MailServerDAO dao = new MailServerDAO(trans);
	    dao.criar(cat);

	    trans.commit();
	} catch (Exception e) {

	} finally {
	    trans.close();
	}
    }

    /**
     * Altera uma existente
     * 
     * @param cat
     */
    public void alterar(MailServer cat) {
	Transaction trans = null;
	try {
	    trans = new Transaction();
	    trans.begin();

	    MailServerDAO dao = new MailServerDAO(trans);
	    dao.alterar(cat);

	    trans.commit();
	} catch (Exception e) {

	} finally {
	    trans.close();
	}
    }

    public void remover(MailServer item) throws Exception {
	Transaction trans = new Transaction();

	try {
	    trans.begin();

	    MailServerDAO dao = new MailServerDAO(trans);
	    dao.remover(item);

	    trans.commit();
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new Exception(e);
	} finally {
	    trans.close();
	}
    }

    public MailServer encontrePorCampus(Campus campus) throws Exception {
	Transaction trans = new Transaction();

	try {
	    trans.begin();

	    MailServerDAO dao = new MailServerDAO(trans);
	    MailServer ms = dao.encontrePorCampus(campus);

	    return ms;
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new Exception(e);
	} finally {
	    trans.close();
	}
    }

    @Asynchronous
    public void enviaMensagens(MensagemEmail mensagem) {
	System.out.println("--> ThreadEnviaMensagemEmail: INICIO");
	CryptoUtils cu;
	try {
	    cu = new CryptoUtils(CryptoMode.DES, MAIL_KEY_PASSWORD);

	    MailServer server = encontrePorCampus(mensagem.getCampus());
	    String password = cu.decrypt(server.getPassword());

	    MailSender sender = new MailSender(server.getAuthenticationRequired(), server.getHost(), server.getPort(),
		    server.getSsl(), server.getStarttls(), server.getPlainTextOverTls(), server.getFromEmail(),
		    server.getUserName(), password);

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
