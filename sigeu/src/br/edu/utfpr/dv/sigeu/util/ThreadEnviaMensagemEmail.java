package br.edu.utfpr.dv.sigeu.util;

import br.edu.utfpr.dv.sigeu.config.Config;

public class ThreadEnviaMensagemEmail extends Thread {
	private MensagemEmail mensagemEmail;

	public ThreadEnviaMensagemEmail(MensagemEmail mensagemEmail) {
		super();
		this.mensagemEmail = mensagemEmail;
	}

	@Override
	public void run() {
		super.run();
		try {
			if (Config.getInstance().isSendMail()) {
				mensagemEmail.enviarMensagensThread();
			} else {
				System.out.println("    >>> Envio de e-mail desabilitado.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
