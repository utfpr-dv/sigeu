package br.edu.utfpr.dv.sigeu.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.adamiworks.utils.FileUtils;

/**
 * Classe com configurações gerais fixas da aplicação. As configurações
 * dispostas nesta classe não estão preparadas para serem alteradas em modo de
 * execução (runtime), são configurações para o início do aplicativo durante o
 * carregamento do servidor HTTP.
 * 
 * @author Tiago
 *
 */
public class DatabaseConfig {
	private static DatabaseConfig self;
	public static final String CONFIG_FOLDER = "config";
	public static final String DATABASE_CONFIG_FILENAME = "database.properties";

	//
	//private String appPath;
	private Properties databaseProperties;

	//

	/**
	 * Construtor privado para não permitir instâncias extras
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private DatabaseConfig() {
		// // Identifica o local do projeto
		// StringBuilder sbAppPath = new StringBuilder();
		//
		// if (OSUtils.currentInstance().isWindows()) {
		// Map<String, String> env = System.getenv();
		// String pf = env.get("ProgramFiles");
		// sbAppPath.append(pf).append(File.separator).append(Config.APPLICATION_CODE);
		// } else if (OSUtils.currentInstance().isMac()) {
		// sbAppPath.append(File.separator).append("Applications").append(File.separator).append(Config.APPLICATION_CODE);
		// } else if (OSUtils.currentInstance().isUnix() ||
		// OSUtils.currentInstance().isSolaris()) {
		// sbAppPath.append(File.separator).append("etc").append(File.separator).append(Config.APPLICATION_CODE);
		// }
		//
		// this.appPath = sbAppPath.toString();
		//

		// Carrega arquivo de configurações de banco de dados
		// File databaseConfigFile = new File(appPath + File.separator +
		// CONFIG_FOLDER + File.separator + DATABASE_CONFIG_FILENAME);
		// File databaseConfigFile = new File("database.properties");
		// databaseProperties = new Properties();

		try {
			databaseProperties = FileUtils.getPropertiesFromClasspath("database.properties");
			// databaseProperties.load(new FileInputStream(databaseConfigFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//

	}

	/**
	 * Retorna a própria instância para uso
	 * 
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static DatabaseConfig getInstance() {
		if (self == null) {
			self = new DatabaseConfig();
		}
		return self;
	}

	/**
	 * Retorna classe tipo Properties com todas as configurações do arquivo de
	 * configurações de banco de dados
	 * 
	 * @return
	 */
	public Properties getDatabaseProperties() {
		return databaseProperties;
	}

	/**
	 * Busca apenas um único parâmetro e retorna seu valor conforme especificado
	 * no arquivo de configurações de banco de dados
	 * 
	 * @param p
	 * @return
	 */
	public String getProperty(DatabaseParameter p) {
		return this.databaseProperties.getProperty(p.getParameter());
	}

}
