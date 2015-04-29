package br.edu.utfpr.dv.sigeu.persistence;

import java.util.List;

import org.hibernate.cfg.Configuration;

import com.adamiworks.utils.ClassUtils;

public class HibernateClassMappings {

	/**
	 * Used by DatabaseConfig to add classes to Hibernate Persistence Manager.
	 * 
	 * @param config
	 */
	public static void addClasses(Configuration config) {
		List<Class<?>> classes;
		try {
			classes = ClassUtils.getClassesForPackage("br.edu.utfpr.dv.sigeu.entities");
			for (Class<?> c : classes) {
				config.addAnnotatedClass(c);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
