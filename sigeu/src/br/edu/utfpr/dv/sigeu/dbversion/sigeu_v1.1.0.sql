UPDATE public.parametro SET valor = '1.1.0' WHERE codigo = 'versao_database';

ALTER TABLE pessoa ADD COLUMN externo boolean not null default false;

CREATE TABLE public.uri_permissao(
	id_permissao	serial not null,
	id_campus	integer not null,
	id_grupo_pessoa	integer not null,
	uri		varchar(1024) not null,
	acesso		boolean not null default true
);

ALTER TABLE public.uri_permissao ADD CONSTRAINT pk_uri_permissao PRIMARY KEY (id_permissao);

ALTER TABLE public.uri_permissao ADD CONSTRAINT fk__grupo_pessoa__uri_permissao__id_grupo_pessoa FOREIGN KEY(id_grupo_pessoa) REFERENCES public.grupo_pessoa(id_grupo_pessoa);

ALTER TABLE public.uri_permissao ADD CONSTRAINT fk__campus__uri_permissao__id_campus FOREIGN KEY(id_campus) REFERENCES public.campus(id_campus);

CREATE UNIQUE INDEX AK_URI_PERMISSAO_01 ON public.uri_permissao(id_campus,id_grupo_pessoa,uri);