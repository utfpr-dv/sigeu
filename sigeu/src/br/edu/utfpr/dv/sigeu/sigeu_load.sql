-- Cria contador de instituicao
INSERT INTO SEQUENCIA( nome, valor ) VALUES( 'instituicao', 101 );

-- Cria contador de campus
INSERT INTO SEQUENCIA( nome, valor ) VALUES( 'campus', 101 );

-- Cria contador de pessoa
INSERT INTO SEQUENCIA( nome, valor ) VALUES( 'pessoa', 101 );

-- Cria contador de LDAP
INSERT INTO SEQUENCIA( nome, valor ) VALUES( 'ldap', 101 );

-- Cria contador de Grupo de pessoa
INSERT INTO SEQUENCIA( nome, valor ) VALUES( 'grupo_pessoa', 100 );


-- Insere Instituição Ministério da Educação (para administração)
INSERT INTO INSTITUICAO( id_instituicao, sigla, nome, ativo ) 
VALUES( 1, 'MEC', 'Ministério da Educação', true );

-- Insere UTFPR (para administração)
INSERT INTO INSTITUICAO( id_instituicao, sigla, nome, ativo ) 
VALUES( 100, 'UTFPR', 'Universidade Tecnológica Federal do Paraná', true );

-- Insere Campus Brasil (para administração)
INSERT INTO CAMPUS( id_instituicao, id_campus, sigla, nome, ativo ) 
VALUES( 1, 1, 'BRASIL', 'Ministério da Educação - Governo Federal', true );

-- Insere Campus Dois Vizinhos da UTFPR
INSERT INTO CAMPUS( id_instituicao, id_campus, sigla, nome, ativo ) 
VALUES( 100, 100, 'DV', 'UTFPR - Campus Dois Vizinhos', true );

INSERT INTO LDAP_SERVER( id_campus, id_server, host, port, ssl, basedn, sufixo_email, var_ldap_uid, var_ldap_nome_completo, var_ldap_email, var_ldap_cnpj_cpf, var_ldap_matricula, var_ldap_campus )
VALUES( 100, 100, 'ldap.dv.utfpr.edu.br', 636, true, 'dc=utfpr,dc=edu,dc=br', '@utfpr.edu.br', 'uid', 'cn', 'mail', 'employeeNumber', 'carLicense', 'l' );

-- Cria pessoa para o Administrador do sistema no campus BRASIL do MEC
INSERT INTO PESSOA( id_campus, id_pessoa, nome_completo, senha_md5, email, pessoa_fisica, matricula, ativo, admin )
VALUES( 1, 1, 'Administrador', md5('1'), 'admin', false, 0, true, true );

-- Cria grupo de Administração
INSERT INTO grupo_pessoa( id_campus, id_grupo_pessoa, nome ) 
VALUES( 1, 1, 'Administradores' );

-- Relaciona Admin ao grupo Administradores
INSERT INTO pessoa_grupo_pessoa( id_pessoa, id_grupo_pessoa ) 
VALUES( 1, 1 );

-- Cria parâmetro com nome do sistema
INSERT INTO parametro( codigo, valor ) 
VALUES( 'nome_sistema', 'SGU - Sistema de Gestão de Universidades' );

-- Cria parâmetro com ajuda de LOGIN
INSERT INTO parametro( codigo, valor )
VALUES( 'login_ajuda', 'Utilize seu email institucional para autenticação.' );

-- Cria parâmetro com ajuda de navegador
INSERT INTO parametro( codigo, valor )
VALUES( 'login_browser', 'Esse site é melhor visualizado em resoluçao 1024x768 nos navegadores: Safari, Firefox, Chrome e Opera Browser.' );

-- Insere tipos de reserva padrão para UTFPR
INSERT INTO public.tipo_reserva ( id_tipo_reserva, id_campus, descricao, ativo ) VALUES ( 1, 100, 'Aula Regular', true );
INSERT INTO public.tipo_reserva ( id_tipo_reserva, id_campus, descricao, ativo ) VALUES ( 2, 100, 'Aula de Reposição', true );
INSERT INTO public.tipo_reserva ( id_tipo_reserva, id_campus, descricao, ativo ) VALUES ( 3, 100, 'Reunião', true );
INSERT INTO public.tipo_reserva ( id_tipo_reserva, id_campus, descricao, ativo ) VALUES ( 4, 100, 'Semana Acadêmica', true );
INSERT INTO public.tipo_reserva ( id_tipo_reserva, id_campus, descricao, ativo ) VALUES ( 5, 100, 'Empréstimo Temporário (comodato)', true );
INSERT INTO public.tipo_reserva ( id_tipo_reserva, id_campus, descricao, ativo ) VALUES ( 6, 100, 'Evento Interno', true );
INSERT INTO public.tipo_reserva ( id_tipo_reserva, id_campus, descricao, ativo ) VALUES ( 7, 100, 'Evento Externo', true );
INSERT INTO public.tipo_reserva ( id_tipo_reserva, id_campus, descricao, ativo ) VALUES ( 8, 100, 'Planejamento', true );
INSERT INTO public.tipo_reserva ( id_tipo_reserva, id_campus, descricao, ativo ) VALUES ( 9, 100, 'Monitoria', true );
INSERT INTO public.tipo_reserva ( id_tipo_reserva, id_campus, descricao, ativo ) VALUES ( 10, 100, 'Concurso Público', true );
INSERT INTO public.tipo_reserva ( id_tipo_reserva, id_campus, descricao, ativo ) VALUES ( 11, 100, 'Processo Seletivo', true );

-- Insere categorias de item de reserva padrão UTFPR
INSERT INTO public.categoria_item_reserva ( id_categoria, id_campus, nome, ativo ) VALUES ( 1, 100, 'Sala de Aula', true );
INSERT INTO public.categoria_item_reserva ( id_categoria, id_campus, nome, ativo ) VALUES ( 2, 100, 'Projetor Multimídia', true );
INSERT INTO public.categoria_item_reserva ( id_categoria, id_campus, nome, ativo ) VALUES ( 3, 100, 'Laboratório', true );
INSERT INTO public.categoria_item_reserva ( id_categoria, id_campus, nome, ativo ) VALUES ( 4, 100, 'Miniauditório', true );

-- Insere servidor e conta de e-mail para UTFPR DV
INSERT INTO public.mail_server(id_campus, authentication_required, starttls, ssl, plain_text_over_tls, host, port, from_email, user_name, password )
VALUES( 100, true, true, true, true, 'mail.utfpr.edu.br', 465, 'derdi-dv@utfpr.edu.br', 'derdi-dv', 'hfk1kzBRVEYiIjO6+z5pp9bMlJGfkWo1' );