package br.edu.utfpr.dv.sigeu.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import br.edu.utfpr.dv.sigeu.config.Config;

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

	//
	// private String appPath;
	private Properties databaseProperties;

	//

	/**
	 * Construtor privado para não permitir instâncias extras
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private DatabaseConfig() {
		try {
			databaseProperties = FileUtils
					.getPropertiesFromClasspath(Config.CONFIG_FILE);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		if (Config.getInstance().isDebugMode()) {
			return this.databaseProperties.getProperty("dev."
					+ p.getParameter());
		}
		return this.databaseProperties.getProperty(p.getParameter());
	}

}
