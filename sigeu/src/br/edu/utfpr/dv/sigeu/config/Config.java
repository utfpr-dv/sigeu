package br.edu.utfpr.dv.sigeu.config;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.faces.context.FacesContext;

import com.adamiworks.utils.FileUtils;

public class Config {

    public static final String APPLICATION_NAME = "Sistema Integrado de Gestão Universitária";
    public static final String APPLICATION_CODE = "SIGEU";
    public static final String APPLICATION_VERSION = "1.5.0";
    public static final String CONFIG_ADMIN = "admin";
    public static final String CONFIG_DEBUG = "debug";
    public static final String CONFIG_PATH_UPLOAD = "path.upload";
    public static final String CONFIG_FILE = "config.properties";
    public static final String NOME_GRUPO_EXTERNO = "EXTERNO";
    public static final String SEND_MAIL = "sendmail";
    //
    private static Config self;
    //
    private Properties config;
    private boolean debugMode;
    private boolean adminMode;
    private int threadMax = 2;
    private String url;
    private boolean sendMail = false;

    static {
	self = new Config();
    }

    private Config() {
	// Lê arquivo de configurações
	try {
	    config = FileUtils.getPropertiesFromClasspath(CONFIG_FILE);
	} catch (IOException e) {
	    e.printStackTrace();
	}

	this.adminMode = false;
	try {
	    System.out.print("Validando modo Admin: [");
	    String admin = config.getProperty(CONFIG_ADMIN).trim().toLowerCase();
	    System.out.println(admin + "]");
	    if (admin != null) {
		if (admin.toLowerCase().equals("true")) {
		    System.out.println("*** SISTEMA ESTÁ EM MANUTENÇÃO ***");
		    this.adminMode = true;
		}
	    }
	} catch (Exception e) {
	    // ignora
	}

	this.debugMode = false;

	try {
	    String debug = config.getProperty(CONFIG_DEBUG).trim().toLowerCase();
	    if (debug != null) {
		if (debug.equals("true")) {
		    this.debugMode = true;
		}
	    }
	} catch (Exception e) {
	    // ignora
	}

	try {
	    String thread = config.getProperty("thread.max").trim().toLowerCase();
	    if (thread != null) {
		this.threadMax = Integer.valueOf(thread);
	    }
	} catch (Exception e) {
	    this.threadMax = 2;
	}

	try {
	    url = config.getProperty("url").trim().toLowerCase();
	} catch (Exception e) {
	    // ignora
	}

	this.sendMail = true;

	try {
	    String sendMailstr = config.getProperty(SEND_MAIL).trim().toLowerCase();
	    if (sendMailstr != null) {
		if (sendMailstr.equals("false")) {
		    this.sendMail = false;
		}
	    }
	} catch (Exception e) {
	    // ignora
	}
    }

    public static Config getInstance() {
	// if (self == null) {
	// self = new Config();
	// }
	return self;
    }

    /**
     * Retorna o valor de uma propriedade do arquivo sigeu.properties. As
     * propriedades disponíveis são constantes dessa mesma classe iniciadas por
     * <code><b>CONFIG_</b></code>.
     * 
     * @param name Constantes desta mesma classe iniciada por
     *             <code><b>CONFIG_</b></code>.
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
	Map<String, Object> map = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	map.put(key, value);
    }

    /**
     * Recupera o valor de uma variável de sessão
     * 
     * @param key
     * @return
     */
    public Object getSessionVariable(String key) {
	Map<String, Object> map = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	return map.get(key);
    }

    public boolean isDebugMode() {
	return debugMode;
    }

    public int getThreadMax() {
	return threadMax;
    }

    public String getUrl() {
	return url;
    }

    public boolean isAdminMode() {
	return adminMode;
    }

    public boolean isSendMail() {
	return sendMail;
    }
}