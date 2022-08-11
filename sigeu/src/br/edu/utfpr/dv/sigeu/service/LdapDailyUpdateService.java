package br.edu.utfpr.dv.sigeu.service;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

import br.edu.utfpr.dv.sigeu.dao.CampusDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.util.ValidationUtils;

@Stateless
public class LdapDailyUpdateService {

    @EJB
    private IntegrationService integrationService;

    @EJB
    private CampusDAO campusDAO;

    @EJB
    private EmailService emailService;

    @Schedule(hour = "3", minute = "0", second = "0", persistent = false)
    public void runDaily() {
	Campus campusDv = campusDAO.encontrePorId(100);

	try {
	    integrationService.atualizaPessoasLdap(campusDv, null);
	} catch (Exception e) {
	    emailService.enviarEmailErro(campusDv, "Erro na sincronização diária do LDAP:\n" + ValidationUtils.getRootCauseMsg(e));
	    e.printStackTrace();
	}
    }
}