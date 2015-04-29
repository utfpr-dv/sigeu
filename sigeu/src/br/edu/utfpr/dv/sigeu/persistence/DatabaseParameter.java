package br.edu.utfpr.dv.sigeu.persistence;

public enum DatabaseParameter {

	DATABASE_DRIVER(1, "database.driver"), DATABASE_URL(2, "database.url"), DATABASE_USER(3, "database.user"), DATABASE_PASSWORD(
			4, "database.password"), DATABASE_POOL_MIN(10, "database.pool.min"), DATABASE_POOL_MAX(11,
			"database.pool.max"), DATABASE_POOL_INCREMENT(12, "database.pool.increment");

	private int code;
	private String parameter;

	private DatabaseParameter(int code, String parameter) {
		this.code = code;
		this.parameter = parameter;
	}

	public int getCode() {
		return code;
	}

	public String getParameter() {
		return parameter;
	}

}
