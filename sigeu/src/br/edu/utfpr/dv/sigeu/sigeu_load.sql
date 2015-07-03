-- Inclui versão do programa
INSERT INTO public.parametro (codigo, valor) VALUES( 'versao_database', '1.0.0' );

-- altera tabelas para mudar o tipo de auto increment
alter table public.reserva alter column id_reserva set data type integer;
alter table public.reserva alter column id_reserva set default nextval('reserva_id_reserva_seq');
alter table public.reserva alter column id_reserva set not null;
alter table public.pessoa alter column id_pessoa set data type integer;
alter table public.pessoa alter column id_pessoa set default nextval('pessoa_id_pessoa_seq');
alter table public.pessoa alter column id_pessoa set not null;

-- altera sequences
ALTER SEQUENCE pessoa_id_pessoa_seq RESTART with 1000;
ALTER SEQUENCE reserva_id_reserva_seq RESTART with 1000;

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
VALUES( 1, 1, 'ADMINISTRADOR', md5('1'), 'derdi-dv@utfpr.edu.br', false, 0, true, true );

-- Cria grupo de Administração
INSERT INTO grupo_pessoa( id_campus, id_grupo_pessoa, nome ) 
VALUES( 1, 1, 'ADMINISTRADORES' );

-- Relaciona Admin ao grupo Administradores
INSERT INTO pessoa_grupo_pessoa( id_pessoa, id_grupo_pessoa ) 
VALUES( 1, 1 );

-- Cria parâmetro com nome do sistema
INSERT INTO parametro( codigo, valor ) 
VALUES( 'nome_sistema', 'SIGEU - Sistema Integrado para Gestão de Universidades' );

-- Cria parâmetro com ajuda de LOGIN
INSERT INTO parametro( codigo, valor )
VALUES( 'login_ajuda', 'Utilize seu email institucional para autenticação.' );

-- Cria parâmetro com ajuda de navegador
INSERT INTO parametro( codigo, valor )
VALUES( 'login_browser', 'Esse site é melhor visualizado em resoluçao 1024x768 nos navegadores: Safari, Firefox, Chrome e Opera Browser.' );

-- Insere tipos de reserva padrão para UTFPR
INSERT INTO public.tipo_reserva ( id_tipo_reserva, id_campus, descricao, ativo ) VALUES ( 0001, 100, upper('Aula Regular'), true );
INSERT INTO public.tipo_reserva ( id_tipo_reserva, id_campus, descricao, ativo ) VALUES ( 0002, 100, upper('Aula de Reposição'), true );
INSERT INTO public.tipo_reserva ( id_tipo_reserva, id_campus, descricao, ativo ) VALUES ( 0010, 100, upper('Conc. Público ou Proc. Seletivo'), true );
INSERT INTO public.tipo_reserva ( id_tipo_reserva, id_campus, descricao, ativo ) VALUES ( 0005, 100, upper('Empréstimo Temporário'), true );
INSERT INTO public.tipo_reserva ( id_tipo_reserva, id_campus, descricao, ativo ) VALUES ( 0008, 100, upper('Planejamento'), true );
INSERT INTO public.tipo_reserva ( id_tipo_reserva, id_campus, descricao, ativo ) VALUES ( 0009, 100, upper('Monitoria'), true );
INSERT INTO public.tipo_reserva ( id_tipo_reserva, id_campus, descricao, ativo ) VALUES ( 0003, 100, upper('Reunião'), true );
INSERT INTO public.tipo_reserva ( id_tipo_reserva, id_campus, descricao, ativo ) VALUES ( 0004, 100, upper('Videoconferência'), true );
INSERT INTO public.tipo_reserva ( id_tipo_reserva, id_campus, descricao, ativo ) VALUES ( 0007, 100, upper('Outro'), true );

-- Insere categorias de item de reserva padrão UTFPR
INSERT INTO public.categoria_item_reserva ( id_categoria, id_campus, nome, ativo ) VALUES ( 1, 100, 'SALA / AUDITÓRIO', true );
INSERT INTO public.categoria_item_reserva ( id_categoria, id_campus, nome, ativo ) VALUES ( 2, 100, 'EQUIPAMENTO', true );
INSERT INTO public.categoria_item_reserva ( id_categoria, id_campus, nome, ativo ) VALUES ( 3, 100, 'LABORATÓRIO', true );

-- Insere servidor e conta de e-mail para UTFPR DV
INSERT INTO public.mail_server(id_campus, authentication_required, starttls, ssl, plain_text_over_tls, host, port, from_email, user_name, password )
VALUES( 100, true, true, true, true, 'mail.utfpr.edu.br', 465, 'derdi-dv@utfpr.edu.br', 'derdi-dv', 'hfk1kzBRVEYiIjO6+z5pp9bMlJGfkWo1' );

-- View para pesquisa de reservas por nome de usuário
CREATE OR REPLACE VIEW public.VW_USUARIO_RESERVA AS
SELECT DISTINCT 
	id_campus, 
	nome_usuario 
FROM 
	public.reserva 
ORDER BY 
	id_campus, 
	nome_usuario;

-- VIEW para identificação de professor/pessoa
CREATE OR REPLACE VIEW public.VW_PROFESSOR_PESSOA
AS
SELECT
	prof.id_professor,
	prof.name AS nome_professor,
	pess.id_pessoa,
	pess.nome_completo AS nome_pessoa
FROM
	public.professor prof LEFT OUTER JOIN
	public.professor_pessoa pp ON
		prof.id_professor = pp.id_professor LEFT OUTER JOIN
	public.pessoa pess ON
		pess.id_pessoa = pp.id_pessoa
ORDER BY
	prof.name;
	
-- TRIGGER FUNCTION PARA EVITAR RESERVAS DUPLICADAS
CREATE OR REPLACE FUNCTION tf_reserva_concorrente() 
RETURNS TRIGGER AS
$BODY$
DECLARE 
	i_id_periodo_letivo INTEGER;
	i_count INTEGER;
	d_data_pl DATE;
BEGIN
	IF (NEW.status = 'E') THEN
		SELECT
			data_fim 
		INTO
			d_data_pl
		FROM 
			periodo_letivo 
		WHERE 
			current_date 
		BETWEEN 
			data_inicio AND data_fim;
	
		IF NEW.data > d_data_pl THEN
			RAISE 'Reservas para % além do término do período letivo atual %', NEW.data, d_data_pl;
		END IF;
			
		SELECT
			pl.id_periodo_letivo
		INTO
			i_id_periodo_letivo
		FROM
			public.periodo_letivo pl
		WHERE
				pl.id_transacao_reserva = NEW.id_transacao
			AND	pl.id_campus = NEW.id_campus;
		
		IF (i_id_periodo_letivo IS NULL) THEN
			SELECT
				count(r2.id_reserva) AS cnt
			INTO
				i_count
			FROM
				public.reserva r2
			WHERE
				    r2.data = NEW.data
				AND r2.status = 'E'
				AND r2.id_campus = NEW.id_campus
				AND r2.id_item_reserva = NEW.id_item_reserva
				AND (
					( NEW.hora_inicio >= r2.hora_inicio AND NEW.hora_inicio < r2.hora_fim ) OR
					( NEW.hora_fim > r2.hora_inicio AND NEW.hora_fim <= r2.hora_fim )
				)
				AND r2.id_reserva <> NEW.id_reserva;
			
			IF i_count > 0 THEN
				RAISE 'Reserva duplicada para item: % em % de % a %', NEW.id_item_reserva, NEW.data, NEW.hora_inicio, NEW.hora_fim USING ERRCODE = 'unique_violation';
			END IF;
		END IF;
	END IF;

	RETURN NEW;
END 
$BODY$
LANGUAGE 'plpgsql' VOLATILE;

-- TRIGGER INSERT
CREATE TRIGGER tr_reserva_concorrente_ins
BEFORE INSERT
ON public.reserva
FOR EACH ROW
EXECUTE PROCEDURE tf_reserva_concorrente();

-- TRIGGER UPDATE
CREATE TRIGGER tr_reserva_concorrente_upd
BEFORE UPDATE
ON public.reserva
FOR EACH ROW
EXECUTE PROCEDURE tf_reserva_concorrente();