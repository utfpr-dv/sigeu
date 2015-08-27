UPDATE public.parametro SET valor = '1.0.5' WHERE codigo = 'versao_database';

CREATE OR REPLACE VIEW vw_reserva_livre AS
-- HORÁRIO LIVRE DA MEIA NOITE ATÉ O INÍCIO DA PRIMEIRA RESERVA
SELECT 
	CAST(0 AS INTEGER) as id_reserva,
	id_transacao,
	id_campus,
	data_gravacao,
	hora_gravacao,
	data,
	CAST('00:00' AS TIME) AS hora_inicio,
	hora_inicio - (INTERVAL '1 min') AS hora_fim,
	id_tipo_reserva,
	id_item_reserva,
	id_pessoa,
	id_autorizador,
	id_usuario,
	nome_usuario,
	email_notificacao,
	motivo,
	rotulo,
	cor,
	status,
	motivo_cancelamento,
	importado
FROM	(
	SELECT
		id_reserva,
		CAST(0 AS INTEGER) AS id_transacao,
		CAST(0 AS INTEGER) AS id_campus,
		current_date as data_gravacao,
		current_time as hora_gravacao,
		data,
		hora_inicio,
		hora_fim,
		CAST(0 AS INTEGER) AS id_tipo_reserva,
		id_item_reserva,
		CAST(1 AS INTEGER) AS id_pessoa,
		CAST(1 AS INTEGER) AS id_autorizador,
		CAST(1 AS INTEGER) AS id_usuario,
		CAST('** LIVRE **' AS VARCHAR(255)) AS nome_usuario,
		CAST('' AS VARCHAR(256)) AS email_notificacao,
		CAST('** LIVRE **' AS VARCHAR(4000)) AS motivo,
		CAST('' AS VARCHAR(32)) AS rotulo,
		CAST('#FFFFFF' AS VARCHAR(12)) as cor,
		CAST('E' AS CHAR(1)) as status,
		CAST('' AS VARCHAR(4000)) as motivo_cancelamento,
		false as importado,
		min(id_reserva) over(partition by data, id_item_reserva order by data, id_item_reserva) as first_id_reserva
	FROM	
		reserva R1
	WHERE
		NOT EXISTS (
			SELECT
				1
			FROM
				RESERVA R2
			WHERE
				R1.DATA = R2.DATA AND
				R1.ID_ITEM_RESERVA = R2.ID_ITEM_RESERVA AND
				/* SE HÁ ALGUMA RESERVA INICIANDO ENTRE 00:00 E 00:10 IGNORA */
				R2.HORA_INICIO <= '00:10'
		) AND
		status = 'E'
	) tmp1
WHERE
	id_reserva = first_id_reserva

UNION ALL

-- HORÁRIOS LIVRES ENTRE AS RESERVAS
SELECT 
	id_reserva,
	id_transacao,
	id_campus,
	data_gravacao,
	hora_gravacao,
	data,
	coalesce(L1,hora_fim) + (INTERVAL '1 min') AS hora_inicio,
	coalesce(L2,'00:00') - (INTERVAL '1 min') AS hora_fim,
	id_tipo_reserva,
	id_item_reserva,
	id_pessoa,
	id_autorizador,
	id_usuario,
	nome_usuario,
	email_notificacao,
	motivo,
	rotulo,
	cor,
	status,
	motivo_cancelamento,
	importado	
FROM	(
	SELECT
		CAST(0 AS INTEGER) AS id_reserva,
		CAST(0 AS INTEGER) AS id_transacao,
		CAST(0 AS INTEGER) AS id_campus,
		current_date as data_gravacao,
		current_time as hora_gravacao,
		data,
		hora_inicio,
		hora_fim,
		lag(hora_fim) over (ORDER BY id_item_reserva, data, hora_inicio, hora_fim) AS L1,
		lead(hora_inicio) over (ORDER BY id_item_reserva, data, hora_inicio, hora_fim) AS L2,
		CAST(0 AS INTEGER) AS id_tipo_reserva,
		id_item_reserva,
		CAST(1 AS INTEGER) AS id_pessoa,
		CAST(1 AS INTEGER) AS id_autorizador,
		CAST(1 AS INTEGER) AS id_usuario,
		CAST('** LIVRE **' AS VARCHAR(255)) AS nome_usuario,
		CAST('' AS VARCHAR(256)) AS email_notificacao,
		CAST('** LIVRE **' AS VARCHAR(4000)) AS motivo,
		CAST('' AS VARCHAR(32)) AS rotulo,
		CAST('#FFFFFF' AS VARCHAR(12)) as cor,
		CAST('E' AS CHAR(1)) as status,
		CAST('' AS VARCHAR(4000)) as motivo_cancelamento,
		false as importado
	FROM	
		reserva
	WHERE
		status = 'E'
	) tmp2;
	
CREATE TABLE datas(
	data DATE not null
);

ALTER TABLE datas ADD CONSTRAINT pk_datas PRIMARY KEY (data);

CREATE OR REPLACE FUNCTION sp_preenche_datas()
RETURNS INTEGER
AS
$BODY$
DECLARE
	ld_data DATE;
BEGIN
	SELECT '2014-01-01' INTO ld_data;
	
	FOR i IN 1..1000000 LOOP
		INSERT INTO DATAS(DATA) VALUES(ld_data);
		SELECT ld_data +1 INTO ld_data;
	END LOOP;

	RETURN 1;
END
$BODY$
LANGUAGE 'plpgsql' VOLATILE;

select sp_preenche_datas();