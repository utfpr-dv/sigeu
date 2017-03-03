UPDATE public.parametro SET valor = '1.4.4' WHERE codigo = 'versao_database';

CREATE INDEX ie_pessoa_nome_completo ON pessoa(nome_completo);

CREATE INDEX ie_pessoa_matricula ON pessoa(matricula);

CREATE INDEX ie_pessoa_cnpjcpf ON pessoa(cnpj_cpf);

