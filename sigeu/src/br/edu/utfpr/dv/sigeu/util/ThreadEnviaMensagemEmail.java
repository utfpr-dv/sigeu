package br.edu.utfpr.dv.sigeu.util;


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
			mensagemEmail.enviarMensagensThread();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
