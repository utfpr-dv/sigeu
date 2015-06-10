package br.edu.utfpr.dv.sigeu.jsfbeans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.edu.utfpr.dv.sigeu.config.Config;
import br.edu.utfpr.dv.sigeu.entities.PeriodoLetivo;
import br.edu.utfpr.dv.sigeu.service.IntegrationService;
import br.edu.utfpr.dv.sigeu.service.PeriodoLetivoService;
import br.edu.utfpr.dv.sigeu.service.PessoaService;

@ManagedBean(name = "reservaAdminBean")
@ViewScoped
public class ReservaAdminBean extends JavaBean {

	private static final long serialVersionUID = 7554618101134711624L;

	// Campos da importação do XML
	private List<PeriodoLetivo> listaPeriodoLetivo;
	private PeriodoLetivo periodoLetivo;
	private String xmlFileName;

	public ReservaAdminBean() {
		super();
		try {
			listaPeriodoLetivo = PeriodoLetivoService.pesquisar(Config
					.getInstance().getCampus());
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

	public void atualizarCadastrosDoLdap() throws Exception {
		Thread.sleep(1000);

		try {
			PessoaService.atualizaPessoasLdap(Config.getInstance()
					.getPessoaLogin().getEmail());
		} catch (Exception e) {
			this.addErrorMessage("Atualização cadastral",
					"Houve um erro durante a atualização de cadastros. Informe ao Admin.");
			e.printStackTrace();
			throw e;
		}

		this.addInfoMessage("Atualização cadastral",
				"Atualização cadastral concluída!");
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
			timetable_id = IntegrationService.importXml(xmlFileName);

			try {
				IntegrationService.criaCalendarioAula(timetable_id,
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

	public void relacionaProfessorPessoa() {
		try {
			IntegrationService.relacionaProfessorPessoa();
			addInfoMessage("", "Relacionamento Concluído!");
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("", e.getMessage());
		}
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

}
