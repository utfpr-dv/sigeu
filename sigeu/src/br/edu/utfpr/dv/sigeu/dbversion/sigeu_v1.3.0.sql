UPDATE public.parametro SET valor = '1.3.0' WHERE codigo = 'versao_database';

DROP INDEX ak_ldap_sufixo_email;

CREATE TABLE public.direito (
	id_pessoa INTEGER NOT NULL,
	id_campus INTEGER NOT NULL,
	processo  VARCHAR(64) NOT NULL,
	autoriza  BOOLEAN NOT NULL DEFAULT FALSE
);

ALTER TABLE public.direito ADD CONSTRAINT pk_direito PRIMARY KEY (id_pessoa, id_campus, processo);

ALTER TABLE public.direito ADD CONSTRAINT fk_pessoa_direito FOREIGN KEY(id_pessoa) REFERENCES public.pessoa(id_pessoa);

ALTER TABLE public.direito ADD CONSTRAINT fk_campus_direito FOREIGN KEY(id_campus) REFERENCES public.campus(id_campus);

ALTER TABLE PESSOA ADD ldap_campus VARCHAR(64) NOT NULL default 'RT';