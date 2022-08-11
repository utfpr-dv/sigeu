package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.edu.utfpr.dv.sigeu.entities.PeriodoLetivo;
import br.edu.utfpr.dv.sigeu.service.IntegrationService;
import br.edu.utfpr.dv.sigeu.service.PeriodoLetivoService;
import br.edu.utfpr.dv.sigeu.util.ValidationUtils;

@Named
@ViewScoped
public class ReservaAdminBean extends JavaBean {

    @EJB
    private PeriodoLetivoService periodoLetivoService;

    @EJB
    private IntegrationService integrationService;

    @Inject
    private LoginBean loginBean;

    private Integer progress = 0;

    private static final long serialVersionUID = 7554618101134711624L;

    // Campos da importação do XML
    private List<PeriodoLetivo> listaPeriodoLetivo;
    private PeriodoLetivo periodoLetivo;
    private String xmlFileName;

    @PostConstruct
    public void init() {
	try {
	    listaPeriodoLetivo = periodoLetivoService.pesquisar(loginBean.getCampus());
	} catch (Exception e) {
	    addErrorMessage("Carregar Periodos", "Erro ao carregar períodos letivos.");
	    addErrorMessage("Periodo Letivo", e.getMessage());
	}
    }

    public void atualizarCadastrosDoLdapPreMessage() {
	addWarnMessage("Atualização cadastral", "Atualização em andamento. NÃO FECHE OU MUDE DE PÁGINA ATÉ O PROCESSO SER CONCLUÍDO.");
    }

    public void atualizarCadastrosDoLdap() {
	try {
	    setProgress(0);

	    integrationService.atualizaPessoasLdap(loginBean.getCampus(), progress -> setProgress(progress));

	    setProgress(100);
	} catch (Exception e) {
	    setProgress(0);
	    RequestContext.getCurrentInstance().execute("endAtualizaLdap()");
	    addErrorMessage("Atualização cadastral", "Houve um erro durante a atualização de cadastros. Informe ao Admin.");
	    e.printStackTrace();
	}
    }

    public void atualizarCadastrosDoLdapPostMessage() {
	addInfoMessage("LDAP", "Cadastros atualizados!");
    }

    /**
     * Recebe o arquivo XML para importação de dados
     * 
     * @param event
     */
    public void uploadXMLAscTables(FileUploadEvent event) {
	try {
	    UploadedFile xmlFile = event.getFile();
	    this.xmlFileName = null;

	    if (xmlFile == null) {
		addErrorMessage("XML", "Arquivo não foi importado!");
	    } else {
		String fileName = xmlFile.getFileName();
		// byte[] data = event.getFile().getContents();
		byte[] data = IOUtils.toByteArray(xmlFile.getInputstream());

		if (data == null) {
		    addErrorMessage(fileName, "Arquivo importado não contem dados!");
		} else {
		    integrationService.writeUploadFile(fileName, data);
		    this.xmlFileName = fileName;
		    addInfoMessage("XML", "Arquivo importado com sucesso! Pronto para criar calendário de aulas. Clique no botão Processar para continuar.");
		}
	    }
	} catch (Exception e) {
	    addErrorMessage("Upload XML", "O upload do arquivo XML falhou.");
	    addErrorMessage("Upload XML", e.getMessage());
	}
    }

    public void processaXmlAscTablesPreMessage() {
	addWarnMessage("Importação do XML", "Importação do XML em andamento. NÃO FECHE OU MUDE DE PÁGINA ATÉ O PROCESSO SER CONCLUÍDO.");
    }

    public void processaXmlAscTables() {
	try {
	    clearMessages();
	    setProgress(0);

	    Integer timetableId = integrationService.importXml(loginBean.getCampus(), xmlFileName);

	    try {
		integrationService.geraReservasDoXml(loginBean.getCampus(), loginBean.getPessoaLogin(), timetableId, periodoLetivo.getIdPeriodoLetivo(),
			progress -> setProgress(progress));

		setProgress(100);
		System.out.println("--> IMPORTAÇÃO DO XML FINALIZADA.");

		addInfoMessage("Importação XML", "Importação realizada com sucesso!");
	    } catch (Exception e) {
		setProgress(0);
		RequestContext.getCurrentInstance().execute("endAtualizaXML()");
		addErrorMessage("Processamento XML", "O processamento do XML falhou");
		addErrorMessage("Processamento XML", ValidationUtils.getRootCauseMsg(e));
		e.printStackTrace();
	    }
	} catch (Exception e) {
	    setProgress(0);
	    RequestContext.getCurrentInstance().execute("endAtualizaXML()");
	    addErrorMessage("Importação XML", "A importação do XML falhou");
	    addErrorMessage("Importação XML", ValidationUtils.getRootCauseMsg(e));
	    e.printStackTrace();
	}
    }

    public void processaXmlAscTablesPostMessage() {
	addInfoMessage("Importação do XML", "Importação do XML concluída.");
    }

    public List<PeriodoLetivo> getListaPeriodoLetivo() {
	return listaPeriodoLetivo;
    }

    public void setListaPeriodoLetivo(List<PeriodoLetivo> listaPeriodoLetivo) {
	this.listaPeriodoLetivo = listaPeriodoLetivo;
    }

    public PeriodoLetivo getPeriodoLetivo() {
	return periodoLetivo;
    }

    public void setPeriodoLetivo(PeriodoLetivo periodoLetivo) {
	this.periodoLetivo = periodoLetivo;
    }

    public String getXmlFileName() {
	return xmlFileName;
    }

    public void setXmlFileName(String xmlFileName) {
	this.xmlFileName = xmlFileName;
    }

    public Integer getProgress() {
	return progress;
    }

    public void setProgress(Integer progress) {
	this.progress = progress;
    }
}