package br.edu.utfpr.dv.sigeu.config;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.faces.context.FacesContext;

import com.adamiworks.utils.FileUtils;

import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;

public class Config {

	public static final String APPLICATION_NAME = "Sistema Integrado de Gestão Universitária";
	public static final String APPLICATION_CODE = "SIGEU";
	public static final String CONFIG_PATH_UPLOAD = "path.upload";
	//
	private static Config self;
	//
	private Campus campus;
	private Pessoa pessoaLogin;
	private Properties config;
	private boolean debugMode;
	private int threadMax = 2;

	static {
		self = new Config();
	}

	private Config() {
		// Lê arquivo de configurações
		try {
			config = FileUtils.getPropertiesFromClasspath("sigeu.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.debugMode = false;
		try {
			String debug = config.getProperty("debug").trim().toLowerCase();
			if (debug != null) {
				if (debug.equals("true")) {
					this.debugMode = true;
				}
			}
		} catch (Exception e) {
			// ignora
		}

		try {
			String thread = config.getProperty("thread.max").trim()
					.toLowerCase();
			if (thread != null) {
				this.threadMax = Integer.valueOf(thread);
			}
		} catch (Exception e) {
			this.threadMax = 2;
		}
	}

	public static Config getInstance() {
		return self;
	}

	/**
	 * Retorna o valor de uma propriedade do arquivo sigeu.properties. As
	 * propriedades disponíveis são constantes dessa mesma classe iniciadas por
	 * <code><b>CONFIG_</b></code>.
	 * 
	 * @param name
	 *            Constantes desta mesma classe iniciada por
	 *            <code><b>CONFIG_</b></code>.
	 * @return
	 */
	public String getConfig(String name) {
		return config.getProperty(name);
	}

	/**
	 * Insere uma variável de sessão e define seu valor
	 * 
	 * @param key
	 * @param value
	 */
	public void setSessionVariable(String key, Object value) {
		Map<String, Object> map = FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap();
		map.put(key, value);
	}

	/**
	 * Recupera o valor de uma variável de sessão
	 * 
	 * @param key
	 * @return
	 */
	public Object getSessionVariable(String key) {
		Map<String, Object> map = FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap();
		return map.get(key);
	}

	public Campus getCampus() {
		return campus;
	}

	public void setCampus(Campus campus) {
		this.campus = campus;
	}

	public Pessoa getPessoaLogin() {
		return pessoaLogin;
	}

	public void setPessoaLogin(Pessoa pessoaLogin) {
		this.pessoaLogin = pessoaLogin;
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public int getThreadMax() {
		return threadMax;
	}

}
