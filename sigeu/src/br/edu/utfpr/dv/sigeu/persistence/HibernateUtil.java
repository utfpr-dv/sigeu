package br.edu.utfpr.dv.sigeu.persistence;

import java.beans.PropertyVetoException;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.adamiworks.utils.hibernate.DatabaseConfig;
import com.adamiworks.utils.hibernate.DatabaseParameter;
import com.adamiworks.utils.hibernate.HibernateEntityMappings;

import br.edu.utfpr.dv.sigeu.config.Config;

public class HibernateUtil {
	public static final int HIBERNATE_BATCH_SIZE = 100;
	private static HibernateUtil self;
	// private ComboPooledDataSource cpds = null;

	private SessionFactory sessionFactory = null;
	private ServiceRegistry serviceRegistry = null;

	/**
	 * Construtor privado que inicializa o pool de conexões
	 * 
	 * @throws PropertyVetoException
	 * @throws DatabaseConfigException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private HibernateUtil() {
		String driver = null;
		String url = null;
		String user = null;
		String password = null;
		int poolMin = 1;
		int poolMax = 1;
		int poolInc = 1;

		driver = DatabaseConfig.getInstance().getProperty(DatabaseParameter.DATABASE_DRIVER);
		url = DatabaseConfig.getInstance().getProperty(DatabaseParameter.DATABASE_URL);
		user = DatabaseConfig.getInstance().getProperty(DatabaseParameter.DATABASE_USER);
		password = DatabaseConfig.getInstance().getProperty(DatabaseParameter.DATABASE_PASSWORD);
		poolMin = Integer.valueOf(DatabaseConfig.getInstance().getProperty(DatabaseParameter.DATABASE_POOL_MIN));
		poolMax = Integer.valueOf(DatabaseConfig.getInstance().getProperty(DatabaseParameter.DATABASE_POOL_MAX));
		poolInc = Integer.valueOf(DatabaseConfig.getInstance().getProperty(DatabaseParameter.DATABASE_POOL_INCREMENT));

		Configuration configuration = new Configuration();

		configuration.setProperty("hibernate.connection.driver_class", driver);
		configuration.setProperty("hibernate.connection.url", url);
		configuration.setProperty("hibernate.connection.username", user);
		configuration.setProperty("hibernate.connection.password", password);
		configuration.setProperty("hibernate.connection.autocommit", "false");
		configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		//configuration.setProperty("hibernate.show_sql", "false");
		configuration.setProperty("hibernate.show_sql", String.valueOf(Config.getInstance().isDebugMode()));
		configuration.setProperty("hibernate.order_updates", "true");
		configuration.setProperty("hibernate.default_schema", "public");
		configuration.setProperty("hibernate.c3p0.acquire_increment", String.valueOf(poolInc));
		configuration.setProperty("hibernate.c3p0.idle_test_period", "1200");
		configuration.setProperty("hibernate.c3p0.max_statements", String.valueOf(HIBERNATE_BATCH_SIZE));
		configuration.setProperty("hibernate.c3p0.min_size", String.valueOf(poolMin));
		configuration.setProperty("hibernate.c3p0.max_size", String.valueOf(poolMax));
		configuration.setProperty("hibernate.jdbc.batch_size", String.valueOf(HIBERNATE_BATCH_SIZE));

		// configuration.setProperty("hibernate.c3p0.unreturnedConnectionTimeout",
		// "10");

		// configuration.configure();

		// Add Classes
		// HibernateClassMappings.addClasses(configuration);
		HibernateEntityMappings.loadClasses(configuration, "br.edu.utfpr.dv.sigeu.entities");

		serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();

		sessionFactory = configuration.buildSessionFactory(serviceRegistry);

	}

	/**
	 * Recupera o único objeto compartilhado para gerenciamento de conexões ao
	 * banco de dados
	 * 
	 * @return
	 */
	public static HibernateUtil currentInstance() {
		if (self == null) {
			self = new HibernateUtil();
		}
		return self;
	}

	/**
	 * Returns a new Hibernate Database Session
	 * 
	 * @return
	 */
	public Session openSession() {
		return sessionFactory.openSession();
	}

}
