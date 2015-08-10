package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.edu.utfpr.dv.sigeu.entities.PeriodoLetivo;
import br.edu.utfpr.dv.sigeu.service.IntegrationService;
import br.edu.utfpr.dv.sigeu.service.PeriodoLetivoService;

@Named
@ViewScoped
public class ReservaAdminBean extends JavaBean {
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
			listaPeriodoLetivo = PeriodoLetivoService.pesquisar(loginBean
					.getCampus());
		} catch (Exception e) {
			addErrorMessage("Carregar Periodos",
					"Erro ao carregar períodos letivos.");
			addErrorMessage("Periodo Letivo", e.getMessage());
		}
	}

	public void atualizarCadastrosDoLdapPreMessage() {

		addWarnMessage(
				"Atualização cadastral",
				"Atualização em andamento. NÃO FECHE OU MUDE DE PÁGINA ATÉ O PROCESSO SER CONCLUÍDO.");
	}

	public void atualizarCadastrosDoLdap() {
		this.progress = 0;

		try {
			IntegrationService.atualizaPessoasLdap(this, loginBean.getCampus());

		} catch (Exception e) {
			this.addErrorMessage("Atualização cadastral",
					"Houve um erro durante a atualização de cadastros. Informe ao Admin.");
			e.printStackTrace();
		}

		this.progress = 0;
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
					this.addErrorMessage(fileName,
							"Arquivo importado não contem dados!");
				} else {
					IntegrationService.writeUploadFile(fileName, data);
					this.xmlFileName = fileName;
					this.addInfoMessage(
							"XML",
							"Arquivo importado com sucesso! Pronto para criar calendário de aulas. Clique no botão Processar para continuar.");
				}
			}
		} catch (Exception e) {
			addErrorMessage("Upload XML", "O upload do arquivo XML falhou.");
			addErrorMessage("Upload XML", e.getMessage());
		}
	}

	public void processaXmlAscTablesPreMessage() {
		addWarnMessage(
				"Importação do XML",
				"Importação do XML em andamento. NÃO FECHE OU MUDE DE PÁGINA ATÉ O PROCESSO SER CONCLUÍDO.");
	}

	/**
	 * Processa o arquivo de XML recém importado
	 * 
	 */
	public void processaXmlAscTables() {
		this.clearMessages();
		Integer timetable_id = 0;
		try {
			timetable_id = IntegrationService.importXml(loginBean.getCampus(),
					xmlFileName);

			try {
				IntegrationService.geraReservasDoXml(loginBean.getCampus(),
						loginBean.getPessoaLogin(), this, timetable_id,
						periodoLetivo.getIdPeriodoLetivo());

				System.out.println("--> IMPORTAÇÃO DO XML FINALIZADA.");

			} catch (Exception e) {
				addErrorMessage("Processamento XML",
						"O processamento do XML falhou");
				addErrorMessage("Processamento XML", e.getMessage());
				e.printStackTrace();
			}
		} catch (Exception e) {
			addErrorMessage("Importação XML", "A importação do XML falhou");
			addErrorMessage("Importação XML", e.getMessage());
			e.printStackTrace();
		}

		addInfoMessage("Importação XML", "Importação realizada com sucesso!");

	}

	public void processaXmlAscTablesPostMessage() {
		addWarnMessage("Importação do XML", "Importação do XML concluída.");
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
